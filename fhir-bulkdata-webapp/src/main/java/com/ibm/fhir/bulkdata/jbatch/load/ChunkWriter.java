/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.jbatch.load;

import static com.ibm.fhir.model.type.String.string;

import java.io.Serializable;
import java.sql.Date;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.model.util.SaltHash;
import com.ibm.fhir.model.visitor.ResourceFingerprintVisitor;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.operation.bulkdata.model.type.BulkDataContext;
import com.ibm.fhir.operation.bulkdata.model.type.OperationFields;
import com.ibm.fhir.operation.bulkdata.model.type.StorageType;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.FHIRPersistenceSupport;
import com.ibm.fhir.persistence.InteractionStatus;
import com.ibm.fhir.persistence.SingleResourceResult;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.fhir.persistence.context.FHIRPersistenceEvent;
import com.ibm.fhir.persistence.helper.FHIRPersistenceHelper;
import com.ibm.fhir.persistence.helper.FHIRTransactionHelper;
import com.ibm.fhir.persistence.util.FHIRPersistenceUtil;
import com.ibm.fhir.validation.exception.FHIRValidationException;

/**
 * BulkData $import ChunkWriter
 */
@Dependent
public class ChunkWriter extends AbstractItemWriter {

    private static final Logger logger = Logger.getLogger(ChunkWriter.class.getName());
    private static final int FIRST_VERSION = 1;

    private BulkAuditLogger auditLogger = new BulkAuditLogger();

    private static final byte[] NDJSON_LINESEPERATOR = ConfigurationFactory.getInstance().getEndOfFileDelimiter(null);

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
    @BatchProperty(name = OperationFields.PARTITION_MATRIX)
    private String matrix;

    @Inject
    @Any
    @BatchProperty(name = OperationFields.PARTITION_RESOURCETYPE)
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

            boolean shouldCollectOperationOutcomes = adapter
                    .shouldStorageProviderCollectOperationOutcomes(ctx.getSource());

            // Validates the Resources are valid for included profiles
            if (adapter.shouldStorageProviderValidateResources(ctx.getSource())) {
                long validationStartTimeInMilliSeconds = System.currentTimeMillis();
                for (Object objResJsonList : arg0) {
                    @SuppressWarnings("unchecked")
                    List<Resource> fhirResourceList = (List<Resource>) objResJsonList;

                    // Used to indicate line number in the OperationOutcomes
                    long cur = 1;
                    for (Resource fhirResource : fhirResourceList) {
                        long startTime = System.currentTimeMillis();
                        javax.ws.rs.core.Response.Status status = javax.ws.rs.core.Response.Status.PRECONDITION_FAILED;
                        try {
                            List<Issue> issues = BulkDataUtils.validateInput(fhirResource);

                            if (shouldCollectOperationOutcomes) {
                                OperationOutcome oo;
                                if (issues.isEmpty()) {
                                    oo = generateAllOkValidation(chunkData.getNumOfProcessedResources() + cur);
                                } else {
                                    oo = generateWarning(chunkData.getNumOfProcessedResources() + cur, issues);
                                }

                                FHIRGenerator.generator(Format.JSON).generate(oo, chunkData.getBufferStreamForImport());
                                chunkData.getBufferStreamForImport().write(NDJSON_LINESEPERATOR);
                            }
                            status = javax.ws.rs.core.Response.Status.OK;
                        } catch (FHIRValidationException | FHIROperationException e) {
                            logger.warning("Failed to validate '" + fhirResource.getId() + "' due to error: " + e.getMessage());
                            failedNum++;
                            failValidationIds.add(fhirResource.getClass().getName() + "/" + fhirResource.getId());

                            if (shouldCollectOperationOutcomes) {
                                OperationOutcome operationOutCome = generateException(
                                        chunkData.getNumOfProcessedResources() + cur, e);
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
                        cur++;
                    }
                }
                chunkData.addTotalValidationMilliSeconds(System.currentTimeMillis() - validationStartTimeInMilliSeconds);
            }

            // Validates the ResourceType matches
            if (!adapter.shouldStorageProviderAllowAllResources(ctx.getSource())) {
                long cur = 1;
                for (Object objResJsonList : arg0) {
                    @SuppressWarnings("unchecked")
                    List<Resource> fhirResourceList = (List<Resource>) objResJsonList;

                    for (Resource fhirResource : fhirResourceList) {
                        String assertedResourceType = fhirResource.getClass().getSimpleName();
                        if (!this.resourceType.equals(assertedResourceType)) {
                            failValidationIds.add(assertedResourceType + "/" + fhirResource.getId());

                            if (shouldCollectOperationOutcomes) {
                                OperationOutcome operationOutCome = generateSecurityException(
                                        failedNum + cur + chunkData.getNumOfProcessedResources(), fhirResource.getId(),
                                        assertedResourceType, this.resourceType);
                                FHIRGenerator.generator(Format.JSON).generate(operationOutCome, chunkData.getBufferStreamForImportError());
                                chunkData.getBufferStreamForImportError().write(NDJSON_LINESEPERATOR);
                            }

                            logger.warning("The resource being imported does not match the declared resource - '" + this.resourceType 
                                + " '" + assertedResourceType + "/" + fhirResource.getId() + "'");
                        }
                    }
                    cur++;
                }
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
            try {
                long cur = 1;
                for (Object objResJsonList : arg0) {
                    @SuppressWarnings("unchecked")
                    List<Resource> fhirResourceList = (List<Resource>) objResJsonList;

                    for (Resource fhirResource : fhirResourceList) {
                        try {
                            String id = fhirResource.getId();
                            processedNum++;
                            // Skip the resources which failed the validation
                            if (failValidationIds.contains(fhirResource.getClass().getSimpleName() + "/" + id)) {
                                continue;
                            }
                            OperationOutcome operationOutcome;
                            if (id == null) {
                                // Because there's no id provided, this has to be a create. The persistence layer no
                                // longer modifies the resource, so injection of meta elements must be done here
                                final Instant lastUpdated = Instant.now(ZoneOffset.UTC);
                                final String logicalId = fhirPersistence.generateResourceId();
                                Resource updatedResource = FHIRPersistenceUtil.copyAndSetResourceMetaFields(fhirResource, logicalId, FIRST_VERSION, lastUpdated);
                                long startTime = System.currentTimeMillis();
                                FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(null);
                                operationOutcome =
                                        fhirPersistence.create(persistenceContext, updatedResource).getOutcome();
                                if (auditLogger.shouldLog()) {
                                    // QA: We were sending the original Resource, not the result of the Interaction resource,
                                    // which does not have the Resource.id or the Resource.version
                                    // audit log entry based on the original resource, not the one we modified with meta elements
                                    long endTime = System.currentTimeMillis();
                                    String location = "@source:" + ctx.getSource() + "/" + ctx.getImportPartitionWorkitem();
                                    auditLogger.logCreateOnImport(updatedResource, new Date(startTime), new Date(endTime), Response.Status.CREATED, location, "BulkDataOperator");
                                }
                            } else {
                                Map<String, Object> props = new HashMap<>();
                                props.put(FHIRPersistenceEvent.PROPNAME_PERSISTENCE_IMPL, fhirPersistence);
                                props.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, fhirResource.getClass().getSimpleName());
                                props.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID, fhirResource.getId());

                                FHIRPersistenceEvent event = new FHIRPersistenceEvent(fhirResource, props);

                                // Set up the persistence context to include the deleted resources when we read
                                FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(event, true);
                                operationOutcome = conditionalFingerprintUpdate(chunkData, skip, fhirPersistence,
                                        persistenceContext, id, fhirResource, ctx,
                                        failedNum + cur + chunkData.getNumOfProcessedResources());
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
                                OperationOutcome operationOutCome = generateException(
                                        failedNum + cur + chunkData.getNumOfProcessedResources(), e);
                                FHIRGenerator.generator(Format.JSON).generate(operationOutCome, chunkData.getBufferStreamForImportError());
                                chunkData.getBufferStreamForImportError().write(NDJSON_LINESEPERATOR);
                            }
                        }
                        cur++;
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
     * conditional update checks to see if our cache contains the key, if not reads
     * from the db, and calculates the cache. The cache is saved within the context
     * of this particular execution, and then destroyed.
     *
     * @implNote considered using a shared cache, a few things with that to
     *           consider: 
     *  1 - the shared cache would have to be updated at the end of a transaction (we don't control it).
     *  2 - we would have to use a transaction sync registry to control the synchronization of the cache
     *  3 - Instead, we're doing a read then update.
     *
     * @param chunkData   the transient user data used increment the number of skips
     * @param skip        should skip the resource if it matches
     * @param persistence used to facilitate the calls to the underlying db
     * @param context     used in db calls
     * @param logicalId   the logical id of the FHIR resource (e.g. 1-2-3-4)
     * @param resource    the FHIR Resource
     * @param ctx         the bulk data context
     * @param line        the line number
     * @return outcomes including information or warnings
     * @throws Exception
     */
    public OperationOutcome conditionalFingerprintUpdate(ImportTransientUserData chunkData, boolean skip,
            FHIRPersistence persistence, FHIRPersistenceContext context, String logicalId, Resource resource,
            BulkDataContext ctx, long line) throws Exception {
        long startTime = System.currentTimeMillis();
        Response.Status status = Response.Status.OK;

        // Since issue 1869, we must perform the read at this point in order to obtain
        // the latest version id. The persistence layer no longer makes any changes to the
        // resource so the resource needs to be fully prepared before the persistence
        // create/update call is made
        SingleResourceResult<? extends Resource> oldResourceResult = persistence.read(context, resource.getClass(),
                logicalId);
        Resource oldResource = oldResourceResult.getResource();

        final com.ibm.fhir.model.type.Instant lastUpdated = FHIRPersistenceSupport.getCurrentInstant();
        final int newVersionNumber = oldResourceResult.getVersion() + 1;
        resource = FHIRPersistenceUtil.copyAndSetResourceMetaFields(resource, logicalId, newVersionNumber, lastUpdated);

        // If the resource was previously deleted, we need to treat this as a not to
        // skip, otherwise we end up with really inconsistent data
        // when there is a DELETED resource.
        boolean skipped = false;
        OperationOutcome oo;
        if (!skip || oldResourceResult.isDeleted() || oldResource == null) {
            SingleResourceResult<? extends Resource> result = persistence.update(context, resource);

            if (result.getStatus() == InteractionStatus.MODIFIED) {
                status = Response.Status.CREATED;
            }
            oo = result.getOutcome();
        } else {
            // Fingerprint old base line
            ResourceFingerprintVisitor fpOld = new ResourceFingerprintVisitor();
            oldResource.accept(fpOld);
            SaltHash oldBaseLine = fpOld.getSaltAndHash();

            // Fingerprint new base line
            ResourceFingerprintVisitor fp = new ResourceFingerprintVisitor(oldBaseLine);
            resource.accept(fp);
            SaltHash newBaseLine = fp.getSaltAndHash();

            if (oldBaseLine != null && oldBaseLine.equals(newBaseLine)) {
                // Outcome is an informational message (no change to datastore)
                String key = resource.getClass().getSimpleName() + "/" + logicalId;
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("Skipping $import - update for '" + key + "'");
                }
                chunkData.addToNumOfSkippedResources(1);
                oo = OperationOutcome.builder()
                        .issue(Issue.builder()
                                .severity(IssueSeverity.INFORMATION)
                                .code(IssueType.INFORMATIONAL)
                                .details(CodeableConcept.builder()
                                        .text(string(
                                                "Update to an existing resource matches the stored resource's hash; skipping the update for '"
                                                        + key + "'"))
                                        .build())
                                .extension(Extension.builder()
                                        .url("https://ibm.com/fhir/bulkdata/linenumber")
                                        .value(Long.toString(line))
                                        .build())
                                .build())
                        .build();
                skipped = true;
            } else {
                // Outcome is an Update
                // We need to update the db and update the local cache
                // Old Resource is set so we avoid an extra read
                context.getPersistenceEvent().setPrevFhirResource(oldResource);
                SingleResourceResult<? extends Resource> result = persistence.update(context, resource);
                oo = result.getOutcome();
            }
        }

        // Audit Logging
        if (auditLogger.shouldLog()) {
            long endTime = System.currentTimeMillis();
            String location = "@source:" + ctx.getSource() + "/" + ctx.getImportPartitionWorkitem();
            if (!skipped) {
                auditLogger.logUpdateOnImport(resource, new Date(startTime), new Date(endTime), status, location,
                        "BulkDataOperator");
            } else {
                auditLogger.logUpdateOnImportSkipped(oldResource, new Date(startTime), new Date(endTime), status,
                        location, "BulkDataOperator");
            }
        }
        return oo;
    }

    /**
     * Generates ALL_OK along with a line number.
     * 
     * @param lineNumber the location in the file.
     * @return
     */
    private OperationOutcome generateAllOkValidation(long lineNumber) {
        return OperationOutcome.builder()
                .issue(Issue.builder()
                        .severity(IssueSeverity.INFORMATION)
                        .code(IssueType.INFORMATIONAL)
                        .details(
                                CodeableConcept.builder()
                                        .text(string("All OK"))
                                        .build())
                        .extension(Extension.builder()
                                .url("https://ibm.com/fhir/bulkdata/linenumber")
                                .value(Long.toString(lineNumber))
                                .build())
                        .build())
                .build();
    }

    /**
     * Generates Warning and Informational along with a line number.
     * 
     * @param lineNumber the location in the file.
     * @param issues     to be added
     * @return
     */
    private OperationOutcome generateWarning(long lineNumber, List<Issue> issues) {
        Issue issue = Issue.builder()
                .severity(IssueSeverity.WARNING)
                .code(IssueType.INFORMATIONAL)
                .details(
                        CodeableConcept.builder()
                                .text(string("There is a warning included on the cited line"))
                                .build())
                .extension(Extension.builder()
                        .url("https://ibm.com/fhir/bulkdata/linenumber")
                        .value(Long.toString(lineNumber))
                        .build())
                .build();

        List<Issue> newIssues = new ArrayList<>();
        newIssues.addAll(issues);
        newIssues.add(issue);
        return OperationOutcome.builder()
                .issue(newIssues)
                .build();
    }

    /**
     * generate exception
     * 
     * @param lineNumber the location in the file
     * @param ex         exception to log
     * @return
     */
    private OperationOutcome generateException(long lineNumber, Exception ex) {
        Issue issue = Issue.builder()
                .severity(IssueSeverity.ERROR)
                .code(IssueType.EXCEPTION)
                .details(
                        CodeableConcept.builder()
                                .text(string("There is an exception: name=[" + ex.getClass().getName() + "] msg=["
                                        + ex.getMessage() + "]"))
                                .build())
                .extension(Extension.builder()
                        .url("https://ibm.com/fhir/bulkdata/linenumber")
                        .value(Long.toString(lineNumber))
                        .build())
                .build();
        return OperationOutcome.builder()
                .issue(Arrays.asList(issue))
                .build();
    }

    /**
     * generate exception
     * 
     * @param lineNumber the location in the file
     * @param id
     * @param assertedResourceType
     * @param actualResourceType
     * @return
     */
    private OperationOutcome generateSecurityException(long lineNumber, String id, String assertedResourceType,
            String actualResourceType) {
        Issue issue = Issue.builder()
                .severity(IssueSeverity.ERROR)
                .code(IssueType.SECURITY)
                .details(
                        CodeableConcept.builder()
                                .text(string("The resource being imported does not match the declared resource - '"
                                        + actualResourceType
                                        + " '" + assertedResourceType + "/" + id + "'"))
                                .build())
                .extension(Extension.builder()
                        .url("https://ibm.com/fhir/bulkdata/linenumber")
                        .value(Long.toString(lineNumber))
                        .build())
                .build();
        return OperationOutcome.builder()
                .issue(Arrays.asList(issue))
                .build();
    }
}
