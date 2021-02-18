/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.jbatch.bulkdata.load;

import static com.ibm.fhir.jbatch.bulkdata.common.Constants.NDJSON_LINESEPERATOR;

import java.io.Serializable;
import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.jbatch.bulkdata.audit.BulkAuditLogger;
import com.ibm.fhir.jbatch.bulkdata.common.BulkDataUtils;
import com.ibm.fhir.jbatch.bulkdata.context.BatchContextAdapter;
import com.ibm.fhir.jbatch.bulkdata.load.data.ImportTransientUserData;
import com.ibm.fhir.jbatch.bulkdata.source.type.SourceWrapper;
import com.ibm.fhir.jbatch.bulkdata.source.type.SourceWrapperFactory;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.operation.bulkdata.model.type.BulkDataContext;
import com.ibm.fhir.operation.bulkdata.model.type.OperationFields;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.fhir.persistence.helper.FHIRPersistenceHelper;
import com.ibm.fhir.persistence.helper.FHIRTransactionHelper;
import com.ibm.fhir.validation.exception.FHIRValidationException;

/**
 * BulkData $import ChunkWriter
 */
@Dependent
public class ChunkWriter extends AbstractItemWriter {

    private static final Logger logger = Logger.getLogger(ChunkWriter.class.getName());

    private BulkAuditLogger auditLogger = new BulkAuditLogger();

    @Inject
    StepContext stepCtx;

    @Inject
    JobContext jobCtx;

    @Inject
    @Any
    @BatchProperty (name = OperationFields.PARTITTION_WORKITEM)
    private String workItem;

    @Inject
    @Any
    @BatchProperty (name = OperationFields.PARTITION_RESOURCETYPE)
    private String resourceType;

    public ChunkWriter() {
        super();
    }

    @Override
    public void open(Serializable checkpoint) throws Exception {
        // No Operation
    }

    @Override
    public void writeItems(List<java.lang.Object> arg0) throws Exception {
        long executionId = -1;
        try {
            executionId = jobCtx.getExecutionId();
            JobExecution jobExecution = BatchRuntime.getJobOperator().getJobExecution(executionId);

            BatchContextAdapter ctxAdapter = new BatchContextAdapter(jobExecution.getJobParameters());

            BulkDataContext ctx = ctxAdapter.getStepContextForImportChunkReader();
            ctx.setPartitionResourceType(resourceType);
            ctx.setImportPartitionWorkitem(workItem);

            Set<String> failValidationIds = new HashSet<>();

            FHIRPersistenceHelper fhirPersistenceHelper = new FHIRPersistenceHelper();
            FHIRPersistence fhirPersistence = fhirPersistenceHelper.getFHIRPersistenceImplementation();
            FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(null);

            FHIRTransactionHelper txn = new FHIRTransactionHelper(fhirPersistence.getTransaction());

            // Register the context to get the right configuration.
            ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
            adapter.registerRequestContext(ctx.getTenantId(), ctx.getDatastoreId(), ctx.getIncomingUrl());

            int processedNum = 0;
            int succeededNum = 0;
            int failedNum = 0;
            ImportTransientUserData chunkData = (ImportTransientUserData) stepCtx.getTransientUserData();

            // Validate the resources first if required.
            if (adapter.shouldSourceValidateResources(ctx.getSource())) {
                long validationStartTimeInMilliSeconds = System.currentTimeMillis();
                for (Object objResJsonList : arg0) {
                    @SuppressWarnings("unchecked")
                    List<Resource> fhirResourceList = (List<Resource>) objResJsonList;

                    for (Resource fhirResource : fhirResourceList) {
                        long startTime = System.currentTimeMillis();
                        javax.ws.rs.core.Response.Status status = javax.ws.rs.core.Response.Status.PRECONDITION_FAILED;
                        try {
                            BulkDataUtils.validateInput(fhirResource);
                            status = javax.ws.rs.core.Response.Status.OK;
                        } catch (FHIRValidationException | FHIROperationException e) {
                            logger.warning("Failed to validate '" + fhirResource.getId() + "' due to error: " + e.getMessage());
                            failedNum++;
                            failValidationIds.add(fhirResource.getId());

                            if (adapter.shouldSourceCollectOperationOutcomes(ctx.getSource())) {
                                OperationOutcome operationOutCome = FHIRUtil.buildOperationOutcome(e, false);
                                FHIRGenerator.generator(Format.JSON).generate(operationOutCome, chunkData.getBufferStreamForImportError());
                                chunkData.getBufferStreamForImportError().write(NDJSON_LINESEPERATOR);
                            }
                        } finally {
                            if (auditLogger.shouldLog()) {
                                long endTime = System.currentTimeMillis();
                                String location = "@source:" + ctx.getSource() + "/" + ctx.getImportPartitionWorkitem();
                                auditLogger.logValidateOnImport(fhirResource, new Date(startTime), new Date(endTime), status, location, ctx.getUsers());
                            }
                        }
                    }
                }
                chunkData.setTotalValidationMilliSeconds(chunkData.getTotalValidationMilliSeconds()
                        + (System.currentTimeMillis() - validationStartTimeInMilliSeconds));
            }

            // Begin writing the resources into DB.
            long writeStartTimeInMilliSeconds = System.currentTimeMillis();
            // Acquire a DB connection which will be used in the batch.
            // This doesn't really start the transaction, because the transaction has already been started by the JavaBatch
            // framework at this time point.
            txn.begin();

            // Controls the writing of operation outcomes to S3/COS
            boolean collectImportOperationOutcomes = adapter.shouldSourceCollectOperationOutcomes(ctx.getSource());

            try {
                for (Object objResJsonList : arg0) {
                    @SuppressWarnings("unchecked")
                    List<Resource> fhirResourceList = (List<Resource>) objResJsonList;

                    for (Resource fhirResource : fhirResourceList) {
                        try {
                            String id = fhirResource.getId();
                            processedNum++;
                            // Skip the resources which failed the validation
                            if (failValidationIds.contains(id)) {
                                continue;
                            }
                            OperationOutcome operationOutcome;
                            if (id == null) {
                                operationOutcome =
                                        fhirPersistence.create(persistenceContext, fhirResource).getOutcome();
                            } else {
                                operationOutcome =
                                        fhirPersistence.update(persistenceContext, id, fhirResource).getOutcome();
                            }

                            succeededNum++;
                            if (collectImportOperationOutcomes && operationOutcome != null) {
                                FHIRGenerator.generator(Format.JSON).generate(operationOutcome, chunkData.getBufferStreamForImport());
                                chunkData.getBufferStreamForImport().write(NDJSON_LINESEPERATOR);
                            }
                        } catch (FHIROperationException e) {
                            logger.warning("Failed to import '" + fhirResource.getId() + "' due to error: " + e.getMessage());
                            failedNum++;
                            if (collectImportOperationOutcomes) {
                                OperationOutcome operationOutCome = FHIRUtil.buildOperationOutcome(e, false);
                                FHIRGenerator.generator(Format.JSON).generate(operationOutCome, chunkData.getBufferStreamForImportError());
                                chunkData.getBufferStreamForImportError().write(NDJSON_LINESEPERATOR);
                            }
                        }
                    }
                }
            } finally {
                // Release the DB connection.
                // This doesn't really commit the transaction, because the transaction was started and will be committed
                // by the JavaBatch framework.
                txn.end();
            }

            chunkData.addToTotalWriteMilliSeconds(System.currentTimeMillis() - writeStartTimeInMilliSeconds);
            chunkData.addToNumOfProcessedResources(processedNum + chunkData.getNumOfParseFailures());
            chunkData.addToNumOfImportedResources(succeededNum);
            chunkData.addToNumOfImportFailures(failedNum + chunkData.getNumOfParseFailures());

            // Reset NumOfParseFailures for next batch.
            chunkData.setNumOfParseFailures(0);
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("writeItems: processed '" + processedNum + "' '" + ctx.getPartitionResourceType() + "' from '" + chunkData.getImportPartitionWorkitem()
                        + "'");
            }

            // Pushes to the Outcome Site
            if (collectImportOperationOutcomes) {
                SourceWrapper wrapper = SourceWrapperFactory.getSourceWrapper(ctx.getOutcome(), "ibm-cos");
                wrapper.registerTransient(chunkData);
                wrapper.pushOperationOutcomes();
            }
        } catch (FHIRException e) {
            logger.log(Level.SEVERE, "Import ChunkWriter.writeItems during job[" + executionId + "] - " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Import ChunkWriter.writeItems during job[" + executionId + "]", e);
            throw e;
        }
    }
}