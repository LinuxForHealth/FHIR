/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.jbatch.export.system;

import java.io.Serializable;
import java.util.List;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

import com.ibm.fhir.bulkdata.dto.ReadResultDTO;
import com.ibm.fhir.bulkdata.jbatch.context.BatchContextAdapter;
import com.ibm.fhir.bulkdata.jbatch.export.data.ExportTransientUserData;
import com.ibm.fhir.bulkdata.provider.Provider;
import com.ibm.fhir.bulkdata.provider.ProviderFactory;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.operation.bulkdata.model.type.BulkDataContext;
import com.ibm.fhir.operation.bulkdata.model.type.OperationFields;

/**
 * BulkExport System ChunkWriter outputs the incoming data to the given source.
 */
@Dependent
public class ChunkWriter extends AbstractItemWriter {
    private BulkDataContext ctx = null;
    private Provider wrapper = null;

    String cosBucketPathPrefix;

    private long executionId = -1;

    @Inject
    @Any
    @BatchProperty(name = OperationFields.PARTITION_RESOURCETYPE)
    String fhirResourceType;

    @Inject
    StepContext stepCtx;

    @Inject
    JobContext jobContext;

    public ChunkWriter() {
        super();
    }

    @Override
    public void open(Serializable checkpoint) throws Exception {
        executionId = jobContext.getExecutionId();
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        JobExecution jobExecution = jobOperator.getJobExecution(executionId);

        BatchContextAdapter contextAdapter = new BatchContextAdapter(jobExecution.getJobParameters());
        ctx = contextAdapter.getStepContextForSystemChunkWriter();

        // Register the context to get the right configuration.
        ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
        adapter.registerRequestContext(ctx.getTenantId(), ctx.getDatastoreId(), ctx.getIncomingUrl());

        String source = ctx.getSource();
        wrapper = ProviderFactory.getSourceWrapper(source, adapter.getStorageProviderStorageType(source).value());

        cosBucketPathPrefix = ctx.getCosBucketPathPrefix();
    }

    @Override
    public void close() throws Exception {
        wrapper.close();
    }

    @Override
    public void writeItems(List<java.lang.Object> resourceLists) throws Exception {
        if (!BatchStatus.STARTED.equals(jobContext.getBatchStatus())) {
            // short-circuit
            return;
        }
        wrapper.createSource();

        ExportTransientUserData chunkData = (ExportTransientUserData) stepCtx.getTransientUserData();
        wrapper.registerTransient(executionId, chunkData, cosBucketPathPrefix, fhirResourceType);

        if (!resourceLists.stream().allMatch(ReadResultDTO.class::isInstance)) {
            throw new IllegalStateException("Expected a list of ReadResultDTO");
        }
        @SuppressWarnings("unchecked")
        List<ReadResultDTO> dtos = (List<ReadResultDTO>)(List<?>) resourceLists;

        wrapper.writeResources(ctx.getFhirExportFormat(), dtos);
        stepCtx.setTransientUserData(chunkData);
    }
}