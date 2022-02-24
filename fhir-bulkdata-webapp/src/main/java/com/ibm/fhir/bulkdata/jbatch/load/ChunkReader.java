/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.jbatch.load;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemReader;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

import com.ibm.fhir.bulkdata.jbatch.context.BatchContextAdapter;
import com.ibm.fhir.bulkdata.jbatch.load.data.ImportCheckPointData;
import com.ibm.fhir.bulkdata.jbatch.load.data.ImportTransientUserData;
import com.ibm.fhir.bulkdata.provider.Provider;
import com.ibm.fhir.bulkdata.provider.ProviderFactory;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.operation.bulkdata.model.type.BulkDataContext;
import com.ibm.fhir.operation.bulkdata.model.type.OperationFields;

/**
 * BulkData Import ChunkReader
 */
@Dependent
public class ChunkReader extends AbstractItemReader {

    private static final Logger logger = Logger.getLogger(ChunkReader.class.getName());

    @Inject
    StepContext stepCtx;

    @Inject
    JobContext jobCtx;

    @Inject
    @Any
    @BatchProperty(name = OperationFields.PARTITION_WORKITEM)
    private String workItem;

    @Inject
    @Any
    @BatchProperty(name = OperationFields.PARTITION_RESOURCETYPE)
    private String resourceType;

    @Inject
    @Any
    @BatchProperty(name = OperationFields.PARTITION_MATRIX)
    private String matrix;

    long numOfLinesToSkip = 0;

    private BulkDataContext ctx = null;

    private long executionId = -1;

    public ChunkReader() {
        super();
    }

    @Override
    public void open(Serializable checkpoint) throws Exception {
        executionId = jobCtx.getExecutionId();
        JobExecution jobExecution = BatchRuntime.getJobOperator().getJobExecution(executionId);

        BatchContextAdapter ctxAdapter = new BatchContextAdapter(jobExecution.getJobParameters());
        ctx = ctxAdapter.getStepContextForImportChunkReader();
        ctx.setPartitionResourceType(resourceType);
        ctx.setImportPartitionWorkitem(workItem);

        // Register the context to get the right configuration.
        ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
        adapter.registerRequestContext(ctx.getTenantId(), ctx.getDatastoreId(), ctx.getIncomingUrl());

        if (checkpoint != null) {
            ImportCheckPointData checkPointData = (ImportCheckPointData) checkpoint;
            numOfLinesToSkip = checkPointData.getNumOfProcessedResources();
            checkPointData.setInFlyRateBeginMilliSeconds(System.currentTimeMillis());
            stepCtx.setTransientUserData(ImportTransientUserData.fromImportCheckPointData(checkPointData));
        } else {
            ImportTransientUserData chunkData = ImportTransientUserData.Builder.builder()
                    .importPartitionWorkitem(ctx.getImportPartitionWorkitem())
                    .matrixWorkItem(matrix)
                    .numOfProcessedResources(numOfLinesToSkip)
                    .importPartitionResourceType(ctx.getPartitionResourceType())
                    // This naming pattern is used in bulkdata operation to generate file links for import
                    // OperationOutcomes.
                    // e.g, for input file test1.ndjson, if there is any error during the importing, then the
                    // errors are in
                    // test1.ndjson_oo_errors.ndjson
                    // Note: for those good imports, we don't really generate any meaningful OperationOutcome,
                    // so only error import
                    // OperationOutcomes are supported for now.
                    .uniqueIDForImportOperationOutcomes(ctx.getImportPartitionWorkitem() + "_oo_success.ndjson")
                    .uniqueIDForImportFailureOperationOutcomes(ctx.getImportPartitionWorkitem() + "_oo_errors.ndjson")
                    .inFlyRateBeginMilliSeconds(System.currentTimeMillis())
                    .build();

            Provider wrapper = ProviderFactory.getSourceWrapper(ctx.getSource(), ctx.getDataSourceStorageType());
            long importFileSize = wrapper.getSize(workItem);
            chunkData.setImportFileSize(importFileSize);
            chunkData.setInFlyRateBeginMilliSeconds(System.currentTimeMillis());
            stepCtx.setTransientUserData(chunkData);
        }
    }

    @Override
    public void close() throws Exception {
        // No Operation
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return ImportCheckPointData.fromImportTransientUserData((ImportTransientUserData) stepCtx.getTransientUserData());
    }

    @Override
    public Object readItem() throws Exception {
        // If the job is being stopped or in other status except for "started", then stop the read.
        // This can happen when the 'open' method fails.
        if (!BatchStatus.STARTED.equals(stepCtx.getBatchStatus())) {
            return null;
        }

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("readItem: get work item:" + ctx.getImportPartitionWorkitem() + " resource type: " + ctx.getPartitionResourceType());
        }

        ImportTransientUserData chunkData = (ImportTransientUserData) stepCtx.getTransientUserData();
        numOfLinesToSkip = chunkData.getNumOfProcessedResources();

        if (logger.isLoggable(Level.FINE)) {
            logger.fine(() -> "Number of lines to skip are: '" + numOfLinesToSkip + "'");
        }

        Provider provider = ProviderFactory.getSourceWrapper(ctx.getSource(), ctx.getDataSourceStorageType());
        provider.registerTransient(chunkData);

        long readStartTimeInMilliSeconds = System.currentTimeMillis();
        provider.readResources(numOfLinesToSkip, ctx.getImportPartitionWorkitem());

        long numOfParseFailures = provider.getNumberOfParseFailures();
        long numOfLoaded = provider.getNumberOfLoaded();

        List<Resource> resources = provider.getResources();

        chunkData.addToTotalReadMilliSeconds(System.currentTimeMillis() - readStartTimeInMilliSeconds);
        chunkData.setNumOfParseFailures(numOfParseFailures);

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("readItem: loaded '" + numOfLoaded + "' '" + ctx.getPartitionResourceType() + "' from '" + ctx.getImportPartitionWorkitem() + "'");
        }

        chunkData.setNumOfToBeImported(numOfLoaded);
        if (numOfLoaded == 0) {
            return null;
        } else {
            return resources;
        }
    }
}