/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.jbatch.load;

import static com.ibm.fhir.model.type.String.string;

import java.io.Serializable;
import java.sql.Date;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import javax.ws.rs.core.Response;

import com.ibm.fhir.bulkdata.audit.BulkAuditLogger;
import com.ibm.fhir.bulkdata.common.BulkDataUtils;
import com.ibm.fhir.bulkdata.jbatch.context.BatchContextAdapter;
import com.ibm.fhir.bulkdata.jbatch.load.data.ImportTransientUserData;
import com.ibm.fhir.bulkdata.provider.Provider;
import com.ibm.fhir.bulkdata.provider.ProviderFactory;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.model.util.SaltHash;
import com.ibm.fhir.model.visitor.ResourceFingerprintVisitor;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.operation.bulkdata.model.type.BulkDataContext;
import com.ibm.fhir.operation.bulkdata.model.type.OperationFields;
import com.ibm.fhir.operation.bulkdata.model.type.StorageType;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceResourceDeletedException;
import com.ibm.fhir.persistence.helper.FHIRPersistenceHelper;
import com.ibm.fhir.persistence.helper.FHIRTransactionHelper;
import com.ibm.fhir.persistence.interceptor.FHIRPersistenceEvent;
import com.ibm.fhir.persistence.util.FHIRPersistenceUtil;
import com.ibm.fhir.validation.exception.FHIRValidationException;

/**
 * BulkData $import ChunkWriter
 */
@Dependent
public class ChunkWriter extends AbstractItemWriter {

    private static final Logger logger = Logger.getLogger(ChunkWriter.class.getName());

    private BulkAuditLogger auditLogger = new BulkAuditLogger();

    private static final byte[] NDJSON_LINESEPERATOR = ConfigurationFactory.getInstance().getEndOfFileDelimiter(null);

    @Inject
    StepContext stepCtx;

    @Inject
    JobContext jobCtx;

    @Inject
    @Any
    @BatchProperty (name = OperationFields.PARTITION_WORKITEM)
    private String workItem;

    @Inject
    @Any
    @BatchProperty(name = OperationFields.PARTITION_MATRIX)
    private String matrix;

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

            FHIRTransactionHelper txn = new FHIRTransactionHelper(fhirPersistence.getTransaction());

            // Register the context to get the right configuration.
            ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
            adapter.registerRequestContext(ctx.getTenantId(), ctx.getDatastoreId(), ctx.getIncomingUrl());

            int processedNum = 0;
            int succeededNum = 0;
            int failedNum = 0;
            ImportTransientUserData chunkData = (ImportTransientUserData) stepCtx.getTransientUserData();

            // Validate the resources first if required.
            if (adapter.shouldStorageProviderValidateResources(ctx.getSource())) {
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

                            if (adapter.shouldStorageProviderCollectOperationOutcomes(ctx.getSource())) {
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
                chunkData.addTotalValidationMilliSeconds(System.currentTimeMillis() - validationStartTimeInMilliSeconds);
            }

            // Begin writing the resources into DB.
            long writeStartTimeInMilliSeconds = System.currentTimeMillis();
            // Acquire a DB connection which will be used in the batch.
            // This doesn't really start the transaction, because the transaction has already been started by the JavaBatch
            // framework at this time point.
            txn.begin();

            // Controls the writing of operation outcomes to S3/COS
            // Similar code @see ImportPartitionCollector
            StorageType type = adapter.getStorageProviderStorageType(ctx.getOutcome());
            boolean collectImportOperationOutcomes = adapter.shouldStorageProviderCollectOperationOutcomes(ctx.getSource())
                    && (StorageType.AWSS3.equals(type) || StorageType.IBMCOS.equals(type) ||  StorageType.AZURE.equals(type));

            // Get the Skippable Update status
            boolean skip = adapter.enableSkippableUpdates();
            Map<String,SaltHash> localCache = new HashMap<>();
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
                                // Because there's no id provided, this has to be a create
                                long startTime = System.currentTimeMillis();
                                FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(null);
                                operationOutcome =
                                        fhirPersistence.create(persistenceContext, fhirResource).getOutcome();
                                if (auditLogger.shouldLog()) {
                                    long endTime = System.currentTimeMillis();
                                    String location = "@source:" + ctx.getSource() + "/" + ctx.getImportPartitionWorkitem();
                                    auditLogger.logCreateOnImport(fhirResource, new Date(startTime), new Date(endTime), Response.Status.CREATED, location, "BulkDataOperator");
                                }
                            } else {
                                Map<String, Object> props = new HashMap<>();
                                props.put(FHIRPersistenceEvent.PROPNAME_PERSISTENCE_IMPL, fhirPersistence);
                                props.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, fhirResource.getClass().getSimpleName());
                                props.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID, fhirResource.getId());

                                FHIRPersistenceEvent event = new FHIRPersistenceEvent(fhirResource, props);

                                FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(event);
                                long startTime = System.currentTimeMillis();
                                operationOutcome = conditionalFingerprintUpdate(chunkData, skip, localCache, fhirPersistence, persistenceContext, id, fhirResource);
                                if (auditLogger.shouldLog()) {
                                    long endTime = System.currentTimeMillis();
                                    String location = "@source:" + ctx.getSource() + "/" + ctx.getImportPartitionWorkitem();
                                    auditLogger.logUpdateOnImport(null, fhirResource, new Date(startTime), new Date(endTime), Response.Status.OK, location, "BulkDataOperator");
                                }
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
                Provider provider = ProviderFactory.getSourceWrapper(ctx.getOutcome(),
                    ConfigurationFactory.getInstance().getStorageProviderType(ctx.getOutcome()));
                provider.registerTransient(chunkData);
                provider.pushOperationOutcomes();
            }
        } catch (FHIRException e) {
            logger.log(Level.SEVERE, "Import ChunkWriter.writeItems during job[" + executionId + "] - " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Import ChunkWriter.writeItems during job[" + executionId + "]", e);
            throw e;
        }
    }

    /**
     * Get the current time which can be used for the lastUpdated field
     * @return current time in UTC
     */
    private com.ibm.fhir.model.type.Instant getCurrentInstant() {
        return com.ibm.fhir.model.type.Instant.now(ZoneOffset.UTC);
    }

    /**
     * conditional update checks to see if our cache contains the key, if not reads from the db, and calculates the cache.
     * The cache is saved within the context of this particular execution, and then destroyed.
     *
     * @implNote considered using a shared cache, a few things with that to consider:
     * 1 - the shared cache would have to be updated at the end of a transaction (we don't control it).
     * 2 - we would have to use a transaction sync registry to control the synchronization of the cache.
     * 3 - Instead, we're doing a read then update.
     *
     * @param chunkData the transient user data used increment the number of skips
     * @param skip should skip the resource if it matches
     * @param localCache map containing the key-saltHash
     * @param persistence used to facilitate the calls to the underlying db
     * @param context used in db calls
     * @param logicalId the logical id of the FHIR resource (e.g. 1-2-3-4)
     * @param resource the FHIR Resource
     * @return outcomes including information or warnings
     * @throws FHIRPersistenceException
     */
    public OperationOutcome conditionalFingerprintUpdate(ImportTransientUserData chunkData, boolean skip, Map<String, SaltHash> localCache, FHIRPersistence persistence, FHIRPersistenceContext context, String logicalId, Resource resource) throws FHIRPersistenceException {
        
        // Since issue 1869, we always need to always perform the read here in order to obtain the
        // latest version id. When we call the persistence layer, the resource should already
        // be updated with the correct id/meta values
        Resource oldResource = null;
        try {
            // This execution is in a try-catch-block since we want to catch
            // the resource deleted exception.
            oldResource = persistence.read(context, resource.getClass(), logicalId).getResource();
        } catch (FHIRPersistenceResourceDeletedException fpde) {
            logger.throwing("ChunkWriter", "conditionalFingerprintUpdate", fpde);
        }
        
        final com.ibm.fhir.model.type.Instant lastUpdated = getCurrentInstant();
        final int newVersionNumber = oldResource != null && oldResource.getMeta() != null && oldResource.getMeta().getVersionId() != null
                ? Integer.parseInt(oldResource.getMeta().getVersionId().getValue()) + 1 : 1;
        resource = FHIRPersistenceUtil.copyAndSetResourceMetaFields(resource, logicalId, newVersionNumber, lastUpdated);
        
        OperationOutcome oo;
        if (skip) {
            // Key is scoped to the ResourceType.
            String key = resourceType + "/" + logicalId;
            SaltHash oldBaseLine = localCache.get(key);

            ResourceFingerprintVisitor fp = new ResourceFingerprintVisitor();
            if (oldBaseLine == null) {
                // If the resource exists, then we need to fingerprint.
                if (oldResource != null) {
                    ResourceFingerprintVisitor fpOld = new ResourceFingerprintVisitor();
                    oldResource.accept(fpOld);
                    oldBaseLine = fpOld.getSaltAndHash();
                    fp = new ResourceFingerprintVisitor(oldBaseLine);
                }
            }

            resource.accept(fp);
            SaltHash newBaseLine = fp.getSaltAndHash();

            if (oldBaseLine != null && oldBaseLine.equals(newBaseLine)) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("Skipping $import - update for '" + key + "'");
                }
                chunkData.addToNumOfSkippedResources(1);
                oo =  OperationOutcome.builder()
                    .issue(Issue.builder()
                        .severity(IssueSeverity.INFORMATION)
                        .code(IssueType.INFORMATIONAL)
                        .details(CodeableConcept.builder()
                            .text(string("Update resource matches the existing resource; skipping the update for '" + key + "'"))
                            .build())
                        .build())
                    .build();
            } else {
                // We need to update the db and update the local cache
                if (oldResource != null) {
                    // Old Resource is set so we avoid an extra read
                    context.getPersistenceEvent().setPrevFhirResource(oldResource);
                }
                oo = persistence.update(context, logicalId, newVersionNumber, resource).getOutcome();
                localCache.put(key, newBaseLine);
            }
        } else {
            oo = persistence.update(context, logicalId, newVersionNumber, resource).getOutcome();
        }
        return oo;
    }
}