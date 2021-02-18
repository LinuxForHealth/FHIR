/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.jbatch.bulkdata.export.patient;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.batch.api.chunk.AbstractItemReader;
import javax.batch.runtime.context.StepContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.ibm.cloud.objectstorage.services.s3.model.PartETag;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.jbatch.bulkdata.common.BulkDataUtils;
import com.ibm.fhir.jbatch.bulkdata.common.Constants;
import com.ibm.fhir.jbatch.bulkdata.context.BatchContextAdapter;
import com.ibm.fhir.jbatch.bulkdata.export.data.CheckPointUserData;
import com.ibm.fhir.jbatch.bulkdata.export.data.TransientUserData;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.operation.bulkdata.model.type.BulkDataContext;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.fhir.persistence.helper.FHIRPersistenceHelper;
import com.ibm.fhir.persistence.helper.FHIRTransactionHelper;
import com.ibm.fhir.search.SearchConstants;
import com.ibm.fhir.search.compartment.CompartmentUtil;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * Bulk patient export Chunk implementation - the Reader.
 *
 */
@Dependent
public class ChunkReader extends AbstractItemReader {
    private final static Logger logger = Logger.getLogger(ChunkReader.class.getName());
    protected int pageNum = 1;
    // Control the number of records to read in each "item".
    protected int pageSize;

    protected FHIRPersistence fhirPersistence;
    Class<? extends Resource> resourceType;

    // Search parameters for resource types gotten from fhir.typeFilters job parameter.
    Map<Class<? extends Resource>, List<Map<String, List<String>>>> searchParametersForResoureTypes = null;
    // Used to prevent the same resource from being exported multiple times when multiple _typeFilter for the same
    // resource type are used, which leads to multiple search requests which can have overlaps of resources,
    // loadedResourceIds and isDoDuplicationCheck are always reset when moving to the next resource type.
    Set<String> loadedResourceIds = new HashSet<>();
    boolean isDoDuplicationCheck = false;

    private BulkDataContext ctx = null;

    @Inject
    StepContext stepCtx;

    public ChunkReader() {
        super();
    }

    @Override
    public void open(Serializable checkpoint) throws Exception {
        if (checkpoint != null) {
            CheckPointUserData checkPointData = (CheckPointUserData) checkpoint;
            pageNum = checkPointData.getLastWritePageNum();
            stepCtx.setTransientUserData(TransientUserData.fromCheckPointUserData(checkPointData));
        }

        BatchContextAdapter stepContextAdapter = new BatchContextAdapter(stepCtx);
        ctx = stepContextAdapter.getStepContextForPatientChunkReader();

        // Register the context to get the right configuration.
        ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
        adapter.registerRequestContext(ctx.getTenantId(), ctx.getDatastoreId(), ctx.getIncomingUrl());

        FHIRPersistenceHelper fhirPersistenceHelper = new FHIRPersistenceHelper();
        fhirPersistence = fhirPersistenceHelper.getFHIRPersistenceImplementation();
        searchParametersForResoureTypes = BulkDataUtils.getSearchParametersFromTypeFilters(ctx.getFhirTypeFilters());

        resourceType = ModelSupport.getResourceType(ctx.getFhirResourceType());
        pageSize = adapter.getCorePageSize();
    }

    @Override
    public void close() throws Exception {
        // do nothing
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return CheckPointUserData.fromTransientUserData((TransientUserData) stepCtx.getTransientUserData());
    }

    protected void fillChunkDataBuffer(List<String> patientIds) throws Exception {
        TransientUserData chunkData = (TransientUserData) stepCtx.getTransientUserData();
        int indexOfCurrentTypeFilter = 0;
        int compartmentPageNum = 1;
        int resSubTotal = 0;
        FHIRSearchContext searchContext;

        if (chunkData != null) {
            // TODO the following replaceAll can be dropped after issue(https://github.com/IBM/FHIR/issues/300) is fixed.
            patientIds.replaceAll(x -> "Patient/" + x);
            do {
                Map<String, List<String>> queryParameters = new HashMap<>();
                // Add the search parameters from the current typeFilter for current resource type.
                if (searchParametersForResoureTypes.get(resourceType) != null) {
                    queryParameters.putAll(searchParametersForResoureTypes.get(resourceType).get(indexOfCurrentTypeFilter));
                    if (searchParametersForResoureTypes.get(resourceType).size() > 1) {
                        isDoDuplicationCheck = true;
                    }
                }
                List<String> searchCriteria = new ArrayList<>();
                if (ctx.getFhirSearchFromDate() != null) {
                    // https://www.hl7.org/fhir/r4/search.html#prefix
                    searchCriteria.add("ge" + ctx.getFhirSearchFromDate());
                }
                if (ctx.getFhirSearchToDate() != null) {
                    searchCriteria.add("lt" + ctx.getFhirSearchToDate());
                }

                if (!searchCriteria.isEmpty()) {
                    queryParameters.put(SearchConstants.LAST_UPDATED, searchCriteria);
                }
                queryParameters.put(SearchConstants.SORT, Arrays.asList(SearchConstants.LAST_UPDATED));

                List<String> compartmentSearchCriterias = CompartmentUtil.getCompartmentResourceTypeInclusionCriteria("Patient", resourceType.getSimpleName());
                if (compartmentSearchCriterias.size() > 1) {
                    isDoDuplicationCheck = true;
                }

                for (String compartmentSearchCriteria: compartmentSearchCriterias) {
                    HashMap<String, List<String>> queryTmpParameters = new HashMap<>();
                    queryTmpParameters.putAll(queryParameters);

                    queryTmpParameters.put(compartmentSearchCriteria, Arrays.asList(String.join(",", patientIds)));
                    searchContext = SearchUtil.parseQueryParameters(resourceType, queryTmpParameters);

                    do {
                        searchContext.setPageSize(pageSize);
                        searchContext.setPageNumber(compartmentPageNum);
                        FHIRTransactionHelper txn = new FHIRTransactionHelper(fhirPersistence.getTransaction());
                        FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(null, searchContext);

                        List<Resource> resources;
                        txn.begin();
                        try {
                            resources = fhirPersistence.search(persistenceContext, resourceType).getResource();
                        } finally {
                            txn.end();
                        }
                        compartmentPageNum++;

                        for (Resource res : resources) {
                            if (res == null || (isDoDuplicationCheck && loadedResourceIds.contains(res.getId()))) {
                                continue;
                            }
                            try {
                                // No need to fill buffer for parquet because we're letting spark write to COS;
                                // we don't need to control the Multi-part upload like in the NDJSON case
                                if (!FHIRMediaType.APPLICATION_PARQUET.equals(ctx.getFhirExportFormat())) {
                                    FHIRGenerator.generator(Format.JSON).generate(res, chunkData.getBufferStream());
                                    chunkData.getBufferStream().write(Constants.NDJSON_LINESEPERATOR);
                                }
                                resSubTotal++;
                                if (isDoDuplicationCheck) {
                                    loadedResourceIds.add(res.getId());
                                }
                            } catch (FHIRGeneratorException e) {
                                if (res.getId() != null) {
                                    logger.log(Level.WARNING, "fillChunkDataBuffer: Error while writing resources with id '"
                                            + res.getId() + "'", e);
                                } else {
                                    logger.log(Level.WARNING,
                                            "fillChunkDataBuffer: Error while writing resources with unknown id", e);
                                }
                            } catch (IOException e) {
                                logger.warning("fillChunkDataBuffer: chunkDataBuffer written error!");
                                throw e;
                            }
                        }

                    } while (searchContext.getLastPageNumber() >= compartmentPageNum);
                    compartmentPageNum = 1;
                }

                indexOfCurrentTypeFilter++;
            } while (searchParametersForResoureTypes.get(resourceType) != null && indexOfCurrentTypeFilter < searchParametersForResoureTypes.get(resourceType).size());

            chunkData.setCurrentUploadResourceNum(chunkData.getCurrentUploadResourceNum() + resSubTotal);
            chunkData.setTotalResourcesNum(chunkData.getTotalResourcesNum() + resSubTotal);
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("fillChunkDataBuffer: Processed resources - " + resSubTotal + "; Bufferred data size - "
                        + chunkData.getBufferStream().size());
            }

        } else {
            logger.warning("fillChunkDataBuffer: chunkData is null, this should never happen!");
            throw new Exception("fillChunkDataBuffer: chunkData is null, this should never happen!");
        }

    }


    protected void fillChunkPatientDataBuffer(List<Resource> patients) throws Exception {
        int resSubTotal = 0;
        TransientUserData chunkData = (TransientUserData) stepCtx.getTransientUserData();
        for (Resource res : patients) {
            try {
                // No need to fill buffer for parquet because we're letting spark write to COS;
                // we don't need to control the Multi-part upload like in the NDJSON case
                if (!FHIRMediaType.APPLICATION_PARQUET.equals(ctx.getFhirExportFormat())) {
                    FHIRGenerator.generator(Format.JSON).generate(res, chunkData.getBufferStream());
                    chunkData.getBufferStream().write(Constants.NDJSON_LINESEPERATOR);
                }
                resSubTotal++;
            } catch (FHIRGeneratorException e) {
                if (res.getId() != null) {
                    logger.log(Level.WARNING, "fillChunkPatientDataBuffer: Error while writing resources with id '"
                            + res.getId() + "'", e);
                } else {
                    logger.log(Level.WARNING,
                            "fillChunkPatientDataBuffer: Error while writing resources with unknown id", e);
                }
            } catch (IOException e) {
                logger.warning("fillChunkPatientDataBuffer: chunkDataBuffer written error!");
                throw e;
            }
        }
        chunkData.setCurrentUploadResourceNum(chunkData.getCurrentUploadResourceNum() + resSubTotal);
        chunkData.setTotalResourcesNum(chunkData.getTotalResourcesNum() + resSubTotal);
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("fillChunkPatientDataBuffer: Processed resources - " + resSubTotal + "; Bufferred data size - "
                    + chunkData.getBufferStream().size());
        }
    }

    protected void fillChunkData(List<Resource> resources, List<String> patientIds) throws Exception {
        if ("Patient".equals(ctx.getPartitionResourceType()) &&  resources != null) {
            fillChunkPatientDataBuffer(resources);
        } else if ("Patient".equals(ctx.getPartitionResourceType()) && patientIds != null) {
            fillChunkDataBuffer(patientIds);
        }
    }

    @Override
    public Object readItem() throws Exception {
        TransientUserData chunkData = (TransientUserData) stepCtx.getTransientUserData();
        if (chunkData != null && pageNum > chunkData.getLastPageNum()) {
                // No more page to read, so return null to end the reading.
                chunkData.setMoreToExport(false);
                return null;
        }

        FHIRSearchContext searchContext;
        FHIRPersistenceContext persistenceContext;
        Map<String, List<String>> queryParameters = new HashMap<>();

        List<String> searchCreterial = new ArrayList<>();

        if (ctx.getFhirSearchFromDate() != null) {
            searchCreterial.add("ge" + ctx.getFhirSearchFromDate());
        }
        if (ctx.getFhirSearchToDate() != null) {
            searchCreterial.add("lt" + ctx.getFhirSearchToDate());
        }

        if (!searchCreterial.isEmpty()) {
            queryParameters.put(SearchConstants.LAST_UPDATED, searchCreterial);
        }

        queryParameters.put(SearchConstants.SORT, Arrays.asList(SearchConstants.LAST_UPDATED));
        searchContext = SearchUtil.parseQueryParameters(Patient.class, queryParameters);
        searchContext.setPageSize(pageSize);
        searchContext.setPageNumber(pageNum);
        List<Resource> resources = null;
        FHIRTransactionHelper txn = new FHIRTransactionHelper(fhirPersistence.getTransaction());
        txn.begin();

        try {
            persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(null, searchContext);
            resources = fhirPersistence.search(persistenceContext, Patient.class).getResource();
        } finally {
            txn.end();
        }
        pageNum++;

        if (chunkData == null) {
            chunkData = (TransientUserData)TransientUserData.Builder.builder()
                    .pageNum(pageNum)
                    .uploadId(null)
                    .cosDataPacks(new ArrayList<PartETag>())
                    .partNum(1)
                    .indexOfCurrentTypeFilter(0)
                    .resourceTypeSummary(null)
                    .totalResourcesNum(0)
                    .currentUploadResourceNum(0)
                    .currentUploadSize(0)
                    .uploadCount(1)
                    .lastPageNum(searchContext.getLastPageNumber())
                    .lastWritePageNum(1)
                    .build();

            stepCtx.setTransientUserData(chunkData);
        } else {
            chunkData.setPageNum(pageNum);
            chunkData.setLastPageNum(searchContext.getLastPageNumber());
        }

        if (resources != null) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("readItem(" + ctx.getPartitionResourceType() + "): loaded " + resources.size() + " patients");
            }

            List<String> patientIds = resources.stream().filter(item -> item.getId() != null).map(item -> item.getId()).collect(Collectors.toList());
            if (patientIds != null && patientIds.size() > 0) {
                fillChunkData(resources, patientIds);
            }
        } else {
            logger.fine("readItem: End of reading!");
        }

        return resources;
    }
}