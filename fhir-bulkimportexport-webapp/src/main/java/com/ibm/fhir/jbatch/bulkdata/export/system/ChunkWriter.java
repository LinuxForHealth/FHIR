/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.jbatch.bulkdata.export.system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

import com.ibm.fhir.jbatch.bulkdata.context.BatchContextAdapter;
import com.ibm.fhir.jbatch.bulkdata.export.data.TransientUserData;
import com.ibm.fhir.jbatch.bulkdata.source.type.SourceWrapper;
import com.ibm.fhir.jbatch.bulkdata.source.type.SourceWrapperFactory;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.operation.bulkdata.model.type.BulkDataContext;
import com.ibm.fhir.operation.bulkdata.model.type.OperationFields;

/**
 * BulkExport ChunkWriter
 */
@Dependent
public class ChunkWriter extends AbstractItemWriter {

    private static final Logger logger = Logger.getLogger(ChunkWriter.class.getName());

    private boolean isExportPublic = true;

    private BulkDataContext ctx = null;
    private SourceWrapper wrapper = null;

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
        try {
            executionId = jobContext.getExecutionId();
            JobOperator jobOperator = BatchRuntime.getJobOperator();
            JobExecution jobExecution = jobOperator.getJobExecution(executionId);

            BatchContextAdapter contextAdapter = new BatchContextAdapter(jobExecution.getJobParameters());
            ctx = contextAdapter.getStepContextForSystemChunkWriter();

            // Register the context to get the right configuration.
            ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
            adapter.registerRequestContext(ctx.getTenantId(), ctx.getDatastoreId(), ctx.getIncomingUrl());

            String source = ctx.getSource();
            wrapper = SourceWrapperFactory.getSourceWrapper(source, adapter.getSourceStorageType(source).value());

            cosBucketPathPrefix = ctx.getCosBucketPathPrefix();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Export ChunkWriter: open failed job[" + executionId + "]", e);
            throw e;
        }
    }

    @Override
    public void close() throws Exception {
        try {
            wrapper.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Export ChunkWriter: close failed job[" + executionId + "]", e);
            throw e;
        }
    }

    @Override
    public void writeItems(List<java.lang.Object> resourceLists) throws Exception {
        try {
            wrapper.createSource();

            TransientUserData chunkData = (TransientUserData) stepCtx.getTransientUserData();
            if (chunkData == null) {
                logger.warning("writeItems: chunkData is null, this should never happen!");
                throw new Exception("writeItems: chunkData is null, this should never happen!");
            }

            List<Resource> resources = new ArrayList<>();
            // Is there a cleaner way to add these in a type-safe way?
            if (resourceLists instanceof List) {
                for (Object resourceList : resourceLists) {
                    if (resourceList instanceof Collection<?>) {
                        for (Object resource : (Collection<?>) resourceList) {
                            if (resource instanceof Resource) {
                                resources.add((Resource) resource);
                            }
                        }
                    }
                }
            }

            wrapper.registerTransient(executionId, chunkData, cosBucketPathPrefix, fhirResourceType, isExportPublic);
            wrapper.writeResources(ctx.getFhirExportFormat(), resources);
            stepCtx.setTransientUserData(chunkData);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Export ChunkWriter: writeItems failed job[" + executionId + "]", e);
            throw e;
        }
    }
}