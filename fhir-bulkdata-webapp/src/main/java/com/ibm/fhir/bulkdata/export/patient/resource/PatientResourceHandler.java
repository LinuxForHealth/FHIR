/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.export.patient.resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.bulkdata.jbatch.export.data.TransientUserData;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.operation.bulkdata.model.type.BulkDataContext;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.fhir.search.SearchConstants;
import com.ibm.fhir.search.compartment.CompartmentUtil;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * The PatientResourceHandler controls the population of the Patient Resources (Or Group member Resources into the TransientData object)
 */
public class PatientResourceHandler {

    private final static Logger logger = Logger.getLogger(PatientResourceHandler.class.getName());

    // Used to prevent the same resource from being exported multiple times when multiple _typeFilter for the same
    // resource type are used, which leads to multiple search requests which can have overlaps of resources,
    // loadedResourceIds and isDoDuplicationCheck are always reset when moving to the next resource type.
    Set<String> loadedResourceIds = new HashSet<>();

    private TransientUserData chunkData;
    private BulkDataContext ctx;
    private FHIRPersistence fhirPersistence;
    private int pageSize = 1;
    private Class<? extends Resource> resourceType;
    private Map<Class<? extends Resource>, List<Map<String, List<String>>>> searchParametersForResoureTypes;

    private ConfigurationAdapter adapter = ConfigurationFactory.getInstance();

    public PatientResourceHandler() {
        // No Operation
    }

    public void register(TransientUserData chunkData, BulkDataContext ctx, FHIRPersistence fhirPersistence,
        int pageSize, Class<? extends Resource> resourceType, Map<Class<? extends Resource>, List<Map<String, List<String>>>> searchParametersForResoureTypes) {
        this.chunkData = chunkData;
        this.ctx = ctx;
        this.fhirPersistence = fhirPersistence;
        this.pageSize = pageSize;
        this.resourceType = resourceType;
        this.searchParametersForResoureTypes = searchParametersForResoureTypes;
    }

    public void fillChunkDataBuffer(List<String> patientIds) throws Exception {
        boolean isDoDuplicationCheck = adapter.shouldStorageProviderCheckDuplicate(ctx.getSource());
        int indexOfCurrentTypeFilter = 0;
        int compartmentPageNum = 1;
        int resSubTotal = 0;
        FHIRSearchContext searchContext;

        if (chunkData == null) {
            logger.warning("fillChunkDataBuffer: chunkData is null, this should never happen!");
            throw new Exception("fillChunkDataBuffer: chunkData is null, this should never happen!");
        }
        // TODO the following replaceAll can be dropped after issue(https://github.com/IBM/FHIR/issues/300) is
        // fixed.
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

            for (String compartmentSearchCriteria : compartmentSearchCriterias) {
                HashMap<String, List<String>> queryTmpParameters = new HashMap<>();
                queryTmpParameters.putAll(queryParameters);

                queryTmpParameters.put(compartmentSearchCriteria, Arrays.asList(String.join(",", patientIds)));
                searchContext = SearchUtil.parseQueryParameters(resourceType, queryTmpParameters);

                do {
                    searchContext.setPageSize(pageSize);
                    searchContext.setPageNumber(compartmentPageNum);

                    FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(null, searchContext);

                    List<Resource> resources = fhirPersistence.search(persistenceContext, resourceType).getResource();
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
                                chunkData.getBufferStream().write(ConfigurationFactory.getInstance().getEndOfFileDelimiter(ctx.getSource()));
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
                                logger.log(Level.WARNING, "fillChunkDataBuffer: Error while writing resources with unknown id", e);
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
        } while (searchParametersForResoureTypes.get(resourceType) != null
                && indexOfCurrentTypeFilter < searchParametersForResoureTypes.get(resourceType).size());

        chunkData.setCurrentUploadResourceNum(chunkData.getCurrentUploadResourceNum() + resSubTotal);
        chunkData.setCurrentUploadSize(chunkData.getCurrentUploadSize() + chunkData.getBufferStream().size());
        chunkData.setTotalResourcesNum(chunkData.getTotalResourcesNum() + resSubTotal);
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("fillChunkDataBuffer: Processed resources - " + resSubTotal + "; Bufferred data size - "
                    + chunkData.getBufferStream().size());
        }

    }

    public void fillChunkPatientDataBuffer(List<Resource> patients) throws Exception {
        int resSubTotal = 0;
        for (Resource res : patients) {
            try {
                // No need to fill buffer for parquet because we're letting spark write to COS;
                // we don't need to control the Multi-part upload like in the NDJSON case
                if (!FHIRMediaType.APPLICATION_PARQUET.equals(ctx.getFhirExportFormat())) {
                    FHIRGenerator.generator(Format.JSON).generate(res, chunkData.getBufferStream());
                    chunkData.getBufferStream().write(ConfigurationFactory.getInstance().getEndOfFileDelimiter(ctx.getSource()));
                }
                resSubTotal++;
            } catch (FHIRGeneratorException e) {
                if (res.getId() != null) {
                    logger.log(Level.WARNING, "fillChunkPatientDataBuffer: Error while writing resources with id '"
                            + res.getId() + "'", e);
                } else {
                    logger.log(Level.WARNING, "fillChunkPatientDataBuffer: Error while writing resources with unknown id", e);
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

    public void fillChunkData(List<Resource> resources, List<String> patientIds) throws Exception {
        if ("Patient".equals(ctx.getPartitionResourceType()) && resources != null) {
            fillChunkPatientDataBuffer(resources);
        } else if ("Patient".equals(ctx.getPartitionResourceType()) && patientIds != null) {
            fillChunkDataBuffer(patientIds);
        }
    }
}
