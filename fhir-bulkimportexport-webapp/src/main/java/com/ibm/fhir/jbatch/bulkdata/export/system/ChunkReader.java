/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.jbatch.bulkdata.export.system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemReader;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

import com.ibm.cloud.objectstorage.services.s3.model.PartETag;
import com.ibm.fhir.jbatch.bulkdata.common.BulkDataUtils;
import com.ibm.fhir.jbatch.bulkdata.common.Constants;
import com.ibm.fhir.jbatch.bulkdata.context.BatchContextAdapter;
import com.ibm.fhir.jbatch.bulkdata.export.data.CheckPointUserData;
import com.ibm.fhir.jbatch.bulkdata.export.data.TransientUserData;
import com.ibm.fhir.jbatch.bulkdata.export.system.resource.SystemExportResourceHandler;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.operation.bulkdata.model.type.BulkDataContext;
import com.ibm.fhir.operation.bulkdata.model.type.OperationFields;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.fhir.persistence.helper.FHIRPersistenceHelper;
import com.ibm.fhir.persistence.helper.FHIRTransactionHelper;
import com.ibm.fhir.search.SearchConstants;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * Bulk system export Chunk implementation - the Reader.
 */
@Dependent
public class ChunkReader extends AbstractItemReader {

    private final static Logger logger = Logger.getLogger(ChunkReader.class.getName());

    private SystemExportResourceHandler handler = new SystemExportResourceHandler();

    // The handle to the persistence instance used to fetch the resources we want to export
    FHIRPersistence fhirPersistence;

    // The resource type class of the resources being exported by this instance (derived from the injected
    // fhirResourceType value
    Class<? extends Resource> resourceType;

    @Inject
    @Any
    @BatchProperty (name = OperationFields.PARTITION_RESOURCETYPE)
    private String partResourceType;

    @Inject
    StepContext stepCtx;

    @Inject
    JobContext jobCtx;

    private BulkDataContext ctx = null;

    int pageNum = 1;
    // Control the number of records to read in each "item".
    int pageSize = 100;

    int indexOfCurrentTypeFilter = 0;

    boolean isDoDuplicationCheck = false;

    private long executionId = -1;

    // Search parameters for resource types gotten from fhir.typeFilters job parameter.
    Map<Class<? extends Resource>, List<Map<String, List<String>>>> searchParametersForResoureTypes = null;

    public ChunkReader() {
        super();
    }

    @Override
    public void open(Serializable checkpoint) throws Exception {
        try {
            executionId = jobCtx.getExecutionId();
            JobOperator jobOperator = BatchRuntime.getJobOperator();
            JobExecution jobExecution = jobOperator.getJobExecution(executionId);

            BatchContextAdapter contextAdapter = new BatchContextAdapter(jobExecution.getJobParameters());
            ctx = contextAdapter.getStepContextForSystemChunkReader();
            ctx.setPartitionResourceType(partResourceType);

            if (checkpoint != null) {
                CheckPointUserData checkPointData = (CheckPointUserData) checkpoint;
                pageNum = checkPointData.getLastWritePageNum();
                indexOfCurrentTypeFilter = checkPointData.getIndexOfCurrentTypeFilter();

                // We use setTransient from checkpoint when we have just uploaded to COS.
                stepCtx.setTransientUserData(TransientUserData.fromCheckPointUserData(checkPointData));
            }

            // Register the context to get the right configuration.
            ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
            adapter.registerRequestContext(ctx.getTenantId(), ctx.getDatastoreId(), ctx.getIncomingUrl());

            FHIRPersistenceHelper fhirPersistenceHelper = new FHIRPersistenceHelper();
            fhirPersistence = fhirPersistenceHelper.getFHIRPersistenceImplementation();

            searchParametersForResoureTypes = BulkDataUtils.getSearchParametersFromTypeFilters(ctx.getFhirTypeFilters());
            resourceType = ModelSupport.getResourceType(ctx.getPartitionResourceType());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Export ChunkReader: open failed job[" + executionId + "]", e);
            throw e;
        }
    }

    @Override
    public void close() throws Exception {
        // do nothing.
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return CheckPointUserData.fromTransientUserData((TransientUserData) stepCtx.getTransientUserData());
    }

    @Override
    public Object readItem() throws Exception {
        if (!BatchStatus.STARTED.equals(jobCtx.getBatchStatus())) {
            // short-circuit
            return null;
        }

        try {
            TransientUserData chunkData = (TransientUserData) stepCtx.getTransientUserData();
            // If the search already reaches the last page, then check if need to move to the next typeFilter.
            if (chunkData != null && pageNum > chunkData.getLastPageNum()) {
                // We've hit the end the current set of pages, so see if there's another batch to work on
                if (searchParametersForResoureTypes.get(resourceType) == null
                        || searchParametersForResoureTypes.get(resourceType).size() <= indexOfCurrentTypeFilter + 1) {
                    chunkData.setMoreToExport(false);
                    return null;
                } else {
                    // If there is more typeFilter to process for current resource type, then reset pageNum only and move to
                    // the next typeFilter.
                    pageNum = 1;
                    indexOfCurrentTypeFilter++;
                }
            }

            FHIRSearchContext searchContext;
            FHIRPersistenceContext persistenceContext;
            Map<String, List<String>> queryParameters = new HashMap<>();

            // TODO document how type filters are used here
            // Add the search parameters from the current typeFilter for current resource type.
            if (searchParametersForResoureTypes.get(resourceType) != null) {
                queryParameters.putAll(searchParametersForResoureTypes.get(resourceType).get(indexOfCurrentTypeFilter));
                if (searchParametersForResoureTypes.get(resourceType).size() > 1) {
                    isDoDuplicationCheck = true;
                }
            }

            List<String> searchCriteria = new ArrayList<>();

            if (ctx.getFhirSearchFromDate() != null) {
                searchCriteria.add("ge" + ctx.getFhirSearchFromDate());
            }
            if (ctx.getFhirSearchToDate() != null) {
                searchCriteria.add("lt" + ctx.getFhirSearchToDate());
            }

            if (!searchCriteria.isEmpty()) {
                queryParameters.put(SearchConstants.LAST_UPDATED, searchCriteria);
            }

            queryParameters.put(SearchConstants.SORT, Arrays.asList(Constants.FHIR_SEARCH_LASTUPDATED));
            searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters);
            searchContext.setPageSize(pageSize);
            searchContext.setPageNumber(pageNum);
            List<Resource> resources = null;

            // Note we're already running inside a transaction (started by the Javabatch framework)
            // so this txn will just wrap it...the commit won't happen until the checkpoint
            FHIRTransactionHelper txn = new FHIRTransactionHelper(fhirPersistence.getTransaction());
            txn.begin();
            try {
                // Execute the search query to obtain the page of resources
                persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(null, searchContext);
                resources = fhirPersistence.search(persistenceContext, resourceType).getResource();
            } finally {
                txn.end();
            }
            pageNum++;

            if (chunkData == null) {
                chunkData =
                        (TransientUserData) TransientUserData.Builder.builder()
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
                            .lastWritePageNum(1).build();

                stepCtx.setTransientUserData(chunkData);
            } else {
                chunkData.setPageNum(pageNum);
                chunkData.setIndexOfCurrentTypeFilter(indexOfCurrentTypeFilter);
                chunkData.setLastPageNum(searchContext.getLastPageNumber());
            }

            if (resources != null) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("readItem: loaded " + resources.size() + " resources");
                }
                handler.fillChunkDataBuffer(ctx.getSource(), ctx.getFhirExportFormat(), chunkData, resources);
            } else {
                logger.fine("readItem: End of reading!");
            }
            return resources;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Export ChunkReader: readItem failed job[" + executionId + "]", e);
            throw e;
        }
    }
}