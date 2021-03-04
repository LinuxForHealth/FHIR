/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.jbatch.export.patient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import com.ibm.cloud.objectstorage.services.s3.model.PartETag;
import com.ibm.fhir.bulkdata.common.BulkDataUtils;
import com.ibm.fhir.bulkdata.dto.ReadResultDTO;
import com.ibm.fhir.bulkdata.export.patient.resource.PatientResourceHandler;
import com.ibm.fhir.bulkdata.jbatch.context.BatchContextAdapter;
import com.ibm.fhir.bulkdata.jbatch.export.data.CheckPointUserData;
import com.ibm.fhir.bulkdata.jbatch.export.data.TransientUserData;
import com.ibm.fhir.model.resource.Patient;
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
 * Bulk Export for Patient - ChunkReader.
 * Processes a Page of Patients at a Time Per Read
 */
@Dependent
public class ChunkReader extends AbstractItemReader {

    private final static Logger logger = Logger.getLogger(ChunkReader.class.getName());

    private PatientResourceHandler handler = new PatientResourceHandler();

    // Maintains the pointer to the current resources by pageNum,pageSize
    protected int pageNum = 1;
    protected int pageSize;

    protected Class<? extends Resource> resourceType;

    // Search parameters for resource types gotten from fhir.typeFilters job parameter.
    protected Map<Class<? extends Resource>, List<Map<String, List<String>>>> searchParametersForResoureTypes = null;

    protected BulkDataContext ctx = null;

    private FHIRPersistence fhirPersistence = null;

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
            CheckPointUserData checkPointData = (CheckPointUserData) checkpoint;
            pageNum = checkPointData.getLastWritePageNum();
            stepCtx.setTransientUserData(TransientUserData.fromCheckPointUserData(checkPointData));
        }

        // Register the context to get the right configuration.
        ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
        adapter.registerRequestContext(ctx.getTenantId(), ctx.getDatastoreId(), ctx.getIncomingUrl());

        searchParametersForResoureTypes = BulkDataUtils.getSearchParametersFromTypeFilters(ctx.getFhirTypeFilters());
        resourceType = ModelSupport.getResourceType(ctx.getFhirResourceType());

        pageSize = adapter.getCorePageSize();

        FHIRPersistenceHelper fhirPersistenceHelper = new FHIRPersistenceHelper();
        fhirPersistence = fhirPersistenceHelper.getFHIRPersistenceImplementation();
    }

    @Override
    public void close() throws Exception {
        // No Operation
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        CheckPointUserData chunkData = CheckPointUserData.fromTransientUserData((TransientUserData) stepCtx.getTransientUserData());
        return chunkData;
    }

    @Override
    public Object readItem() throws Exception {
        if (!BatchStatus.STARTED.equals(jobCtx.getBatchStatus())) {
            // short-circuit
            return null;
        }

        TransientUserData chunkData = (TransientUserData) stepCtx.getTransientUserData();
        if (chunkData != null && pageNum > chunkData.getLastPageNum()) {
            // No more page to read, so return null to end the reading.
            chunkData.setMoreToExport(false);
            return null;
        }

        Map<String, List<String>> queryParameters = new HashMap<>();

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

        FHIRSearchContext searchContext = SearchUtil.parseQueryParameters(Patient.class, queryParameters);
        searchContext.setPageSize(pageSize);
        searchContext.setPageNumber(pageNum);

        ReadResultDTO dto = null;

        // Note we're already running inside a transaction (started by the Javabatch framework)
        // so this txn will just wrap it...the commit won't happen until the checkpoint
        FHIRTransactionHelper txn = new FHIRTransactionHelper(fhirPersistence.getTransaction());
        txn.begin();
        try {
            FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(null, searchContext);
            List<Resource> resources = fhirPersistence.search(persistenceContext, Patient.class).getResource();

            dto = new ReadResultDTO(resources);
            pageNum++;

            if (chunkData == null) {
                // @formatter:off
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
                            .lastWritePageNum(1)
                            .build();
                // @formatter:on

            } else {
                chunkData.setPageNum(pageNum);
                chunkData.setLastPageNum(searchContext.getLastPageNumber());
            }

            if (dto != null && !dto.empty()) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("readItem[" + ctx.getPartitionResourceType() + "]: loaded " + dto.size() + " patients");
                }

                List<String> patientIds = dto.getResources().stream()
                            .filter(item -> item.getId() != null)
                            .map(item -> item.getId())
                            .collect(Collectors.toList());

                if (patientIds != null && patientIds.size() > 0) {
                    handler.register(chunkData, ctx, fhirPersistence, pageSize, resourceType, searchParametersForResoureTypes);
                    handler.fillChunkData(resources, patientIds);
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