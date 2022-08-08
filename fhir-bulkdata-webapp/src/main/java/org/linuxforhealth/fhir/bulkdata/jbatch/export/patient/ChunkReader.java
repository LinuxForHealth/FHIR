/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.bulkdata.jbatch.export.patient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import org.linuxforhealth.fhir.bulkdata.audit.BulkAuditLogger;
import org.linuxforhealth.fhir.bulkdata.common.BulkDataUtils;
import org.linuxforhealth.fhir.bulkdata.dto.ReadResultDTO;
import org.linuxforhealth.fhir.bulkdata.export.patient.resource.PatientResourceHandler;
import org.linuxforhealth.fhir.bulkdata.jbatch.context.BatchContextAdapter;
import org.linuxforhealth.fhir.bulkdata.jbatch.export.data.ExportCheckpointUserData;
import org.linuxforhealth.fhir.bulkdata.jbatch.export.data.ExportTransientUserData;
import org.linuxforhealth.fhir.core.FHIRMediaType;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.util.ModelSupport;
import org.linuxforhealth.fhir.operation.bulkdata.config.ConfigurationAdapter;
import org.linuxforhealth.fhir.operation.bulkdata.config.ConfigurationFactory;
import org.linuxforhealth.fhir.operation.bulkdata.model.type.BulkDataContext;
import org.linuxforhealth.fhir.operation.bulkdata.model.type.OperationFields;
import org.linuxforhealth.fhir.persistence.FHIRPersistence;
import org.linuxforhealth.fhir.persistence.ResourceResult;
import org.linuxforhealth.fhir.persistence.context.FHIRPersistenceContext;
import org.linuxforhealth.fhir.persistence.context.FHIRPersistenceContextFactory;
import org.linuxforhealth.fhir.persistence.helper.FHIRPersistenceHelper;
import org.linuxforhealth.fhir.persistence.helper.FHIRTransactionHelper;
import org.linuxforhealth.fhir.search.SearchConstants;
import org.linuxforhealth.fhir.search.SearchConstants.Prefix;
import org.linuxforhealth.fhir.search.context.FHIRSearchContext;

/**
 * Bulk Export for Patient - ChunkReader.
 * Processes a Page of Patients at a Time Per Read
 */
@Dependent
public class ChunkReader extends AbstractItemReader {

    private final static Logger logger = Logger.getLogger(ChunkReader.class.getName());

    private BulkAuditLogger auditLogger = new BulkAuditLogger();

    private PatientResourceHandler handler = new PatientResourceHandler();

    // Initialized to zero so we can increment it on every call to readItem (including the first one).
    protected int pageNum = 0;

    // Control the number of records to read in each "item".
    protected int pageSize;

    protected Class<? extends Resource> resourceType;

    // Search parameters for resource types gotten from fhir.typeFilters job parameter.
    protected Map<Class<? extends Resource>, List<Map<String, List<String>>>> searchParametersForResoureTypes = null;

    protected BulkDataContext ctx = null;

    private FHIRPersistence fhirPersistence = null;

    boolean isDoDuplicationCheck = false;

    // Used to prevent the same resource from being exported multiple times when multiple _typeFilter for the same
    // resource type are used, which leads to multiple search requests which can have overlaps of resources,
    // loadedResourceIds and isDoDuplicationCheck are always reset when moving to the next resource type.
    Set<String> loadedPatientIds = new HashSet<>();

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
        long executionId = jobCtx.getExecutionId();
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        JobExecution jobExecution = jobOperator.getJobExecution(executionId);

        BatchContextAdapter contextAdapter = new BatchContextAdapter(jobExecution.getJobParameters());
        ctx = contextAdapter.getStepContextForPatientChunkReader();
        ctx.setPartitionResourceType(partResourceType);

        if (checkpoint != null) {
            ExportCheckpointUserData checkPointData = (ExportCheckpointUserData) checkpoint;
            pageNum = checkPointData.getLastWrittenPageNum();
            stepCtx.setTransientUserData(ExportTransientUserData.fromCheckPointUserData(checkPointData));
        }

        // Register the context to get the right configuration.
        ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
        adapter.registerRequestContext(ctx.getTenantId(), ctx.getDatastoreId(), ctx.getIncomingUrl());

        searchParametersForResoureTypes = BulkDataUtils.getSearchParametersFromTypeFilters(ctx.getFhirTypeFilters());
        resourceType = ModelSupport.getResourceType(partResourceType);

        pageSize = adapter.getCorePageSize();

        FHIRPersistenceHelper fhirPersistenceHelper = new FHIRPersistenceHelper(handler.getSearchHelper());
        fhirPersistence = fhirPersistenceHelper.getFHIRPersistenceImplementation();

        List<Map<String, List<String>>> typeFilters = searchParametersForResoureTypes.get(resourceType);
        isDoDuplicationCheck = typeFilters != null && typeFilters.size() > 1 ?
                true : adapter.shouldStorageProviderCheckDuplicate(ctx.getSource());
    }

    @Override
    public void close() throws Exception {
        // No Operation
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        ExportCheckpointUserData chunkData = ExportCheckpointUserData.fromTransientUserData((ExportTransientUserData) stepCtx.getTransientUserData());
        return chunkData;
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
        if (chunkData != null && pageNum > chunkData.getLastPageNum()) {
            // No more page to read, so return null to end the reading.
            chunkData.setMoreToExport(false);
            return null;
        }

        Map<String, List<String>> queryParameters = new HashMap<>();

        List<String> lastUpdatedCriteria = new ArrayList<>();
        if (ctx.getFhirSearchFromDate() != null) {
            lastUpdatedCriteria.add(Prefix.GE.value() + ctx.getFhirSearchFromDate());
        }
        if (ctx.getFhirSearchToDate() != null) {
            lastUpdatedCriteria.add(Prefix.LT.value() + ctx.getFhirSearchToDate());
        }
        if (!lastUpdatedCriteria.isEmpty()) {
            queryParameters.put(SearchConstants.LAST_UPDATED, lastUpdatedCriteria);
        }

        queryParameters.put(SearchConstants.SORT, Arrays.asList(SearchConstants.LAST_UPDATED));

        if (!Patient.class.isAssignableFrom(resourceType)) {
            // optimization to avoid reading the full resources since we only need their ids
            queryParameters.put(SearchConstants.ELEMENTS, Collections.singletonList("id"));
        }

        FHIRSearchContext searchContext = handler.getSearchHelper().parseQueryParameters(Patient.class, queryParameters);
        searchContext.setPageSize(pageSize);
        searchContext.setPageNumber(pageNum);

        ReadResultDTO dto = new ReadResultDTO();

        // Note we're already running inside a transaction (started by the Javabatch framework)
        // so this txn will just wrap it...the commit won't happen until the checkpoint
        FHIRTransactionHelper txn = new FHIRTransactionHelper(fhirPersistence.getTransaction());
        txn.begin();
        try {
            FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(null, searchContext, null);
            Date startTime = new Date(System.currentTimeMillis());
            List<ResourceResult<? extends Resource>> resourceResults = fhirPersistence.search(persistenceContext, Patient.class).getResourceResults();
            List<? extends Resource> patientResources = ResourceResult.toResourceList(resourceResults);
            if (isDoDuplicationCheck) {
                patientResources = patientResources.stream()
                        .filter(r -> loadedPatientIds.add(r.getId()))
                        .collect(Collectors.toList());
            }

            if (auditLogger.shouldLog() && patientResources != null) {
                Date endTime = new Date(System.currentTimeMillis());
                auditLogger.logSearchOnExport(ctx.getPartitionResourceType(), queryParameters, patientResources.size(), startTime, endTime, Response.Status.OK, "StorageProvider@" + ctx.getSource(), "BulkDataOperator");
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
                // do we want to support extending the last page number mid-export?
//                chunkData.setLastPageNum(searchContext.getLastPageNumber());
            }

            if (patientResources != null && !patientResources.isEmpty()) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("readItem[" + ctx.getPartitionResourceType() + "]: loaded " + patientResources.size() + " patients");
                }

                Set<String> patientIds = patientResources.stream()
                            .map(item -> item.getId())
                            .collect(Collectors.toSet());

                if (!patientIds.isEmpty()) {
                    handler.register(chunkData, ctx, fhirPersistence, pageSize, resourceType, searchParametersForResoureTypes, ctx.getSource());

                    List<? extends Resource> resources = Patient.class.isAssignableFrom(resourceType) ?
                            patientResources : handler.executeSearch(patientIds);
                    if (FHIRMediaType.APPLICATION_PARQUET.equals(ctx.getFhirExportFormat())) {
                        dto.setResources(resources);
                    }
                    handler.fillChunkData(ctx.getFhirExportFormat(), chunkData, resources);
                }
            } else {
                logger.fine("readItem: End of reading!");
            }
        } finally {
            txn.end();
        }

        stepCtx.setTransientUserData(chunkData);
        return dto;
    }

    /**
     * gets the persistence object
     * @return
     */
    protected FHIRPersistence getPersistence() {
        return fhirPersistence;
    }
}