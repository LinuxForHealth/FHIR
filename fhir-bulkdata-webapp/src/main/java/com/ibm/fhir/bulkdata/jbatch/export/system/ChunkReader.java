/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.jbatch.export.system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
import javax.ws.rs.core.Response;

import com.ibm.cloud.objectstorage.services.s3.model.PartETag;
import com.ibm.fhir.bulkdata.audit.BulkAuditLogger;
import com.ibm.fhir.bulkdata.common.BulkDataUtils;
import com.ibm.fhir.bulkdata.dto.ReadResultDTO;
import com.ibm.fhir.bulkdata.export.system.resource.SystemExportResourceHandler;
import com.ibm.fhir.bulkdata.jbatch.context.BatchContextAdapter;
import com.ibm.fhir.bulkdata.jbatch.export.data.ExportCheckpointUserData;
import com.ibm.fhir.bulkdata.jbatch.export.data.ExportTransientUserData;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.operation.bulkdata.model.type.BulkDataContext;
import com.ibm.fhir.operation.bulkdata.model.type.OperationFields;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.ResourceResult;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.fhir.persistence.helper.FHIRPersistenceHelper;
import com.ibm.fhir.persistence.helper.FHIRTransactionHelper;
import com.ibm.fhir.search.SearchConstants;
import com.ibm.fhir.search.context.FHIRSearchContext;

/**
 * Bulk Export for System - ChunkReader.
 * Processes a Page at a Time Per Read per ResourceType.
 */
@Dependent
public class ChunkReader extends AbstractItemReader {

    private final static Logger logger = Logger.getLogger(ChunkReader.class.getName());

    private BulkAuditLogger auditLogger = new BulkAuditLogger();

    private SystemExportResourceHandler handler = new SystemExportResourceHandler();

    // The handle to the persistence instance used to fetch the resources we want to export.
    FHIRPersistence fhirPersistence;

    // The resource type class of the resources being exported by this instance (derived from the injected
    // fhirResourceType value).
    Class<? extends Resource> resourceType;

    private BulkDataContext ctx = null;

    // Initialized to zero so we can increment it on every call to readItem (including the first one).
    int pageNum = 0;

    // Control the number of records to read in each "item".
    int pageSize = ConfigurationFactory.getInstance().getCorePageSize();

    int indexOfCurrentTypeFilter = 0;

    boolean isDoDuplicationCheck = false;

    // Used to prevent the same resource from being exported multiple times when multiple _typeFilter for the same
    // resource type are used, which leads to multiple search requests which can have overlaps of resources,
    // loadedResourceIds and isDoDuplicationCheck are always reset when moving to the next resource type.
    Set<String> loadedResourceIds = new HashSet<>();

    private long executionId = -1;

    // Search parameters for resource types gotten from fhir.typeFilters job parameter.
    Map<Class<? extends Resource>, List<Map<String, List<String>>>> searchParametersForResoureTypes = null;

    @Inject
    @Any
    @BatchProperty(name = OperationFields.PARTITION_RESOURCETYPE)
    private String partResourceType;

    @Inject
    StepContext stepCtx;

    @Inject
    JobContext jobCtx;

    public ChunkReader() {
        super();
    }

    @Override
    public void open(Serializable checkpoint) throws Exception {
        executionId = jobCtx.getExecutionId();
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        JobExecution jobExecution = jobOperator.getJobExecution(executionId);

        BatchContextAdapter contextAdapter = new BatchContextAdapter(jobExecution.getJobParameters());
        ctx = contextAdapter.getStepContextForSystemChunkReader();
        ctx.setPartitionResourceType(partResourceType);

        if (checkpoint != null) {
            ExportCheckpointUserData checkPointData = (ExportCheckpointUserData) checkpoint;
            pageNum = checkPointData.getLastWrittenPageNum();
            indexOfCurrentTypeFilter = checkPointData.getIndexOfCurrentTypeFilter();

            // We use setTransient from checkpoint when we have just uploaded to COS.
            stepCtx.setTransientUserData(ExportTransientUserData.fromCheckPointUserData(checkPointData));
        }

        // Register the context to get the right configuration.
        ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
        adapter.registerRequestContext(ctx.getTenantId(), ctx.getDatastoreId(), ctx.getIncomingUrl());

        searchParametersForResoureTypes = BulkDataUtils.getSearchParametersFromTypeFilters(ctx.getFhirTypeFilters());
        resourceType = ModelSupport.getResourceType(ctx.getPartitionResourceType());

        FHIRPersistenceHelper fhirPersistenceHelper = new FHIRPersistenceHelper(handler.getSearchHelper());
        fhirPersistence = fhirPersistenceHelper.getFHIRPersistenceImplementation();

        List<Map<String, List<String>>> typeFilters = searchParametersForResoureTypes.get(resourceType);
        isDoDuplicationCheck = typeFilters != null && typeFilters.size() > 1 ?
                true : adapter.shouldStorageProviderCheckDuplicate(ctx.getSource());
    }

    @Override
    public void close() throws Exception {
        // do nothing.
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return ExportCheckpointUserData.fromTransientUserData((ExportTransientUserData) stepCtx.getTransientUserData());
    }

    @Override
    public Object readItem() throws Exception {
        if (!BatchStatus.STARTED.equals(jobCtx.getBatchStatus())) {
            // short-circuit
            return null;
        }

        // Move the page number forward. On the first readItem call, this will increment from 0 to 1.
        pageNum++;

        ExportTransientUserData chunkData = (ExportTransientUserData) stepCtx.getTransientUserData();
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

        FHIRPersistenceContext persistenceContext;
        Map<String, List<String>> queryParameters = new HashMap<>();

        // TODO document how type filters are used here
        // Add the search parameters from the current typeFilter for current resource type.
        if (searchParametersForResoureTypes.get(resourceType) != null) {
            queryParameters.putAll(searchParametersForResoureTypes.get(resourceType).get(indexOfCurrentTypeFilter));
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

        queryParameters.put(SearchConstants.SORT, Arrays.asList(SearchConstants.LAST_UPDATED));

        FHIRSearchContext searchContext = handler.getSearchHelper().parseQueryParameters(resourceType, queryParameters);
        searchContext.setPageSize(pageSize);
        searchContext.setPageNumber(pageNum);
        List<? extends Resource> resources = null;

        ReadResultDTO dto = new ReadResultDTO();

        // Note we're already running inside a transaction (started by the Javabatch framework)
        // so this txn will just wrap it...the commit won't happen until the checkpoint
        FHIRTransactionHelper txn = new FHIRTransactionHelper(fhirPersistence.getTransaction());
        txn.begin();
        Date startTime = new Date(System.currentTimeMillis());
        try {
            // Execute the search query to obtain the page of resources
            persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(null, searchContext);
            List<ResourceResult<? extends Resource>> resourceResults = fhirPersistence.search(persistenceContext, resourceType).getResourceResults();
            resources = ResourceResult.toResourceList(resourceResults);

            if (isDoDuplicationCheck) {
                resources = resources.stream()
                        // the add returns false if the id already exists, which filters it out of the collection
                        .filter(r -> loadedResourceIds.add(r.getId()))
                        .collect(Collectors.toList());
            }
        } finally {
            txn.end();
            if (auditLogger.shouldLog() && resources != null) {
                Date endTime = new Date(System.currentTimeMillis());
                auditLogger.logSearchOnExport(ctx.getPartitionResourceType(), queryParameters, resources.size(), startTime, endTime,
                        Response.Status.OK, "@source:" + ctx.getSource(), "BulkDataOperator");
            }
        }

        if (chunkData == null) {
            chunkData = ExportTransientUserData.Builder.builder()
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
                    .lastWrittenPageNum(1)
                    .build();
        } else {
            chunkData.setPageNum(pageNum);
            chunkData.setIndexOfCurrentTypeFilter(indexOfCurrentTypeFilter);
            chunkData.setLastPageNum(searchContext.getLastPageNumber());
        }

        if (resources != null && !resources.isEmpty()) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("readItem: loaded " + resources.size() + " resources");
            }

            if (FHIRMediaType.APPLICATION_PARQUET.equals(ctx.getFhirExportFormat())) {
                dto.setResources(resources);
            }
            handler.fillChunkData(ctx.getFhirExportFormat(), chunkData, resources);
        } else {
            logger.fine("readItem: End of reading!");
        }

        stepCtx.setTransientUserData(chunkData);
        return dto;
    }
}