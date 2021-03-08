/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.jbatch.export.fast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.batch.api.chunk.AbstractItemReader;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.model.PartETag;
import com.ibm.fhir.bulkdata.common.BulkDataUtils;
import com.ibm.fhir.bulkdata.jbatch.context.BatchContextAdapter;
import com.ibm.fhir.bulkdata.jbatch.export.fast.checkpoint.ResourceExportCheckpointAlgorithm;
import com.ibm.fhir.bulkdata.jbatch.export.fast.data.CheckpointUserData;
import com.ibm.fhir.bulkdata.jbatch.export.fast.data.TransientUserData;
import com.ibm.fhir.bulkdata.provider.impl.S3Provider;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.operation.bulkdata.model.type.BulkDataContext;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.ResourcePayload;
import com.ibm.fhir.persistence.helper.FHIRPersistenceHelper;
import com.ibm.fhir.persistence.helper.FHIRTransactionHelper;
import com.ibm.fhir.search.date.DateTimeHandler;

/**
 * A high-performance version of the bulk-export job which doesn't support typeFilter and so
 * can use a more efficient mechanism to page through the set of resources filtered by _lastModified.
 *
 * The Java Batch framework separates the reader from the writer. On the face of it, separation of
 * concerns seems like a good idea. However, because the fetching of the data is controlled by the
 * persistence layer and the fact that we may need to end one multi-part upload and continue feeding
 * a new multi-part upload, the separation makes things a lot harder and less readable - there is a
 * lack of cohesion.
 *
 * To address this, we handle both reading and writing in this class, leaving the ItemWriter as a
 * minimal stub. The framework is still leveraged for checkpointing.
 *
 * The {@link #readItem()} call simply returns an Object if there is more data to process. The
 * {@link ResourceExportCheckpointAlgorithm} will always say we need a checkpoint, so the
 * checkpoint will occur immediately after each read/write.
 *
 * The export is complete when {@link #readItem()} returns null.
 *
 * The export assumes that the persistence layer can efficiently scan forward based on the last_updated
 * time of a resource. It is possible that multiple resources may share the same last_updated time, so
 * this class has to track which resources have already been processed for a given timestamp. This is
 * fairly easy, because the persistence layer must provide the data in order of last_updated. Because
 * the number of resources for a given timestamp is probably very small, it is more efficient to track
 * it this way than asking the persistence layer (e.g. JDBC) to sort. This can cause performance issues,
 * because it negates the benefit of traversing an index in order.
 */
@Dependent
public class ResourcePayloadReader extends AbstractItemReader {
    private final static Logger logger = Logger.getLogger(ResourcePayloadReader.class.getName());
    private static final String CLASS = ResourcePayloadReader.class.getName();

    // S3 client API to IBM Cloud Object Storage
    private S3Provider wrapper = null;
    private AmazonS3 cosClient = null;

    // The handle to the persistence instance used to fetch the resources we want to export
    FHIRPersistence fhirPersistence;

    // The resource type class of the resources being exported by this instance (derived from the injected fhirResourceType value
    Class<? extends Resource> resourceType;

    private BulkDataContext ctx = null;

    String fhirResourceType;
    private boolean isExportPublic = true;

    String cosBucketName;
    String cosBucketPathPrefix;

    // The maximum resources per COS Object
    int resourcesPerObject = ConfigurationFactory.getInstance().getCoreCosMaxResources();

    // The Java Batch context object
    @Inject
    StepContext stepCtx;

    // The context object representing this export job
    @Inject
    JobContext jobContext;

    // Track the resources we've processed sharing the most recent timestamp - for correct checkpoint
    private Set<Long> resourcesForLastTimestamp = new HashSet<>();

    // Each time the timestamp changes, we need to clear resourcesForLastTimestamp
    private Instant lastTimestamp;

    // The last_modified timestamp to start scanning from
    private Instant fromLastModified;

    // An end time to scan to, if any
    private Instant toLastModified;

    // Cap the part upload size to avoid local memory issues. Also need to avoid transaction timeout
    private long partUploadTriggerSize = ConfigurationFactory.getInstance().getCoreCosMinSize() * 10L;

    // How large should a single COS item (file) be
    private long maxObjectSize = ConfigurationFactory.getInstance().getCoreCosMaxSize();

    // The initial size of the buffer we use for export. Most resources are
    // under 10K so this is a reasonable initial value
    private final int initialBufferSize = 10 * 1024;

    private static final char NDJSON_LINE_SEPARATOR = '\n';

    // Assume for now we have a transaction timeout of 120s, but need to give the upload some time to work
    // TODO configuration
    private long txTimeoutMillis = 90000L;

    // The nanoTime after which we want to stop processing to avoid a Liberty transaction timeout
    private long txEndTime;

    // The buffer space we collect data into until we hit the threshold to push to COS
    private ByteArrayOutputStream outputStream;

    // The sum of the multi-part sizes for parts that have been uploaded
    private long currentObjectSize;

    // The COS multi-part upload id for the current COS item
    private String uploadId;

    // The name of the COS object we are currently uploading to
    private String currentObjectName;

    // The number of resources we've exported to the current COS object
    private int currentObjectResourceCount;

    // For large exports, we might need to split the data into multiple COS objects
    private int currentUploadNumber = 1;

    // The result tags from parts previously uploaded
    private List<PartETag> uploadedParts = new ArrayList<>();

    // Used to record an exception from the lambda used to process payloads
    private Exception processingException;

    // How many resources processed in the current readItem call (transaction)
    private int resourcesProcessed;

    // Tracking the resource counts for each COS object we've loaded (for final status)
    private List<Integer> resourceCounts = new ArrayList<>();

    /**
     * Public constructor
     */
    public ResourcePayloadReader() {
        super();
        this.outputStream = new ByteArrayOutputStream(this.initialBufferSize);
    }

    /**
     * Get a prefix which can be used to identify the job in log messages
     * @return
     */
    private String logPrefix() {
        return jobContext.getJobName() + "[" + jobContext.getExecutionId() + "]";
    }

    @Override
    public void open(Serializable checkpoint) throws Exception {
        // Crucially important to set up the request context to make sure we
        // use the correct tenant and datasource going forward
        BatchContextAdapter stepContextAdapter = new BatchContextAdapter(stepCtx);
        ctx = stepContextAdapter.getStepContextForFastResourceWriter();

        ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
        adapter.registerRequestContext(ctx.getTenantId(), ctx.getDatastoreId(), ctx.getIncomingUrl());

        fhirResourceType = ctx.getFhirResourceType();

        String source = ctx.getSource();

        cosBucketName = adapter.getStorageProviderBucketName(source);
        cosBucketPathPrefix = ctx.getCosBucketPathPrefix();

        // Initialize the configuration from the injected string values
        String fhirSearchFromDate = ctx.getFhirSearchFromDate();
        if (fhirSearchFromDate != null) {
            // Date/time format based on FHIR Search Examples:
            //   2019-01-01T08:21:26.94-04:00
            //   2019-01-01T08:21:26Z
            TemporalAccessor ta = DateTimeHandler.parse(fhirSearchFromDate);
            this.fromLastModified = DateTimeHandler.generateValue(ta);
            logger.fine(logPrefix() + " fromLastModified = " + fhirSearchFromDate + "(" + fromLastModified + ")");
        }

        String fhirSearchToDate = ctx.getFhirSearchToDate();
        if (fhirSearchToDate != null) {
            TemporalAccessor ta = DateTimeHandler.parse(fhirSearchToDate);
            this.toLastModified = DateTimeHandler.generateValue(ta);
            logger.fine(logPrefix() + " toLastModified = " + fhirSearchToDate + "(" + toLastModified + ")");
        }

        // Start tracking the resources occurring for the most recent timestamp
        this.resourcesForLastTimestamp.clear();
        this.lastTimestamp = this.fromLastModified;

        // If we are being initialized from a checkpoint, then update our internal status
        // accordingly
        if (checkpoint != null) {
            CheckpointUserData cp = (CheckpointUserData)checkpoint;
            loadStateFrom(cp);

            // Just in case the framework tries to reopen an existing instance,
            // make sure we start with an empty output stream
            this.outputStream.reset();
        }

        // Transient user data is required to signal completion of this partition
        // to the collector (and then analyzer)
        stepCtx.setTransientUserData(new TransientUserData());

        FHIRPersistenceHelper fhirPersistenceHelper = new FHIRPersistenceHelper();
        fhirPersistence = fhirPersistenceHelper.getFHIRPersistenceImplementation();
        resourceType = ModelSupport.getResourceType(fhirResourceType);

        isExportPublic = adapter.isStorageProviderExportPublic(source);

        wrapper = new S3Provider(source);

        // Make sure we have the bucket and conditionally create it.
        wrapper.createSource();

        cosClient = wrapper.getClient();
    }

    @Override
    public Object readItem() throws Exception {
        logger.entering(CLASS, "readItem");

        // Not typical for Java batch implementations, readItem is called just once per transaction.
        // We therefore only return from this method if we've processed all the data, or to
        // avoid a transaction timeout
        long readStartTime = System.nanoTime();
        this.resourcesProcessed = 0; // reset the counter
        this.txEndTime = readStartTime + this.txTimeoutMillis * 1000000L;

        // Note we're already running inside a transaction (started by the Javabatch framework)
        // so this txn will just wrap it...the commit won't happen until the checkpoint
        FHIRTransactionHelper txn = new FHIRTransactionHelper(fhirPersistence.getTransaction());
        txn.begin();
        boolean moreData = true;
        try {
            // Keep scanning while there is more data to fetch but stop the scan early
            // so we don't exceed the transaction timeout. This break also allows a
            // checkpoint in case we need to restart for any reason.
            while (!isTxTimeExpired() && moreData) {

                // Ask the persistence layer to start fetching the records in the current scan window,
                // using the #processPayload lambda to handle each value as it is retrieved.
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("Fetching data from: " + fromLastModified + " to " + toLastModified);
                }

                // Make a note of how many resources were actually processed. If none were processed, this
                // is also an indication that we've reached the end.
                final int previouslyProcessed = this.resourcesProcessed;
                ResourcePayload last = fhirPersistence.fetchResourcePayloads(resourceType, fromLastModified, toLastModified, rp->processPayload(rp));
                final int newlyProcessed = this.resourcesProcessed - previouslyProcessed;

                // check for errors which may have occurred during processing (rare)
                if (this.processingException != null) {
                    logger.log(Level.SEVERE, logPrefix(), this.processingException);
                    throw this.processingException;
                }

                if (last != null && newlyProcessed > 0) {
                    // Update our state so that we can start the next scan from the correct position.
                    this.fromLastModified = last.getLastUpdated();
                } else {
                    // no data returned, so that's the end of the road - unless we exited early
                    // because the max transaction timeout expired
                    if (!isTxTimeExpired()) {
                        // The fetchResourcePayloads returned before the tx time expired, so
                        // we really don't have any more data
                        logger.fine(logPrefix() + " no more data");
                        moreData = false;
                    }
                }
            }

            if (uploadId != null && !moreData) {
                // There's no more data to fetch, so finalize the current upload before we leave
                completeCurrentUpload();
            }
        } finally {
            txn.end();

            // Log a simple stat to give us an idea how quickly we processed the data for this transaction
            double delta = (System.nanoTime() - readStartTime) / 1e9;
            logger.info(String.format("%s processed %d resources in %.2f seconds (rate=%.1f resources/second)", logPrefix(), this.resourcesProcessed, delta, resourcesProcessed/delta));
        }

        if (!moreData) {
            // Mark the partition as complete. It's a little sad that we need to track
            // this state, because it's something the framework ought to provide.
            TransientUserData tud = (TransientUserData)stepCtx.getTransientUserData();
            tud.setCompleted(true);
            tud.setResourceType(this.fhirResourceType);
            tud.setResourceCounts(this.resourceCounts);
        }

        logger.exiting(CLASS, "readItem");
        return moreData ? new Object() : null;
    }

    /**
     * Process this payload result. Called as a lambda (callback) from the persistence layer.
     * This method collects the payloads into a buffer and will trigger a write of that buffer
     * to COS if we hit a certain threshold.
     * @param t
     * @return
     */
    public Boolean processPayload(ResourcePayload t) {

        try {
            // Track resources we've seen on the most recent timestamp. Resources will be fed
            // in timestamp order, but not necessarily resource order, so we need to skip resources
            // we've already processed - this occurs when running the query again because the
            // filter predicate is "last_update >= {fromLastModified}". This is all about avoiding
            // a sort in the database for performance reasons.
            if (this.lastTimestamp == null || !this.lastTimestamp.equals(t.getLastUpdated())) {
                // timestamp has changed, so reset the collection
                this.resourcesForLastTimestamp.clear();
                this.lastTimestamp = t.getLastUpdated();
            }

            // Skip any resources already processed for this timestamp (after a checkpoint restart)
            if (!this.resourcesForLastTimestamp.contains(t.getResourceId())) {
                this.resourcesForLastTimestamp.add(t.getResourceId());
                this.resourcesProcessed++; // track how many we've processed this transaction

                if (logger.isLoggable(Level.FINE)) {
                    logger.fine(logPrefix() + " Processing payload for '" + this.resourceType.getSimpleName() + "/" + t.getLogicalId() + "'");
                }

                // Check if this data would cause us to exceed the max object size. If
                // so, close the current upload and start a new one
                if (this.uploadId != null && (currentObjectSize + this.outputStream.size() > maxObjectSize
                        || this.currentObjectResourceCount >= this.resourcesPerObject)) {
                    if (logger.isLoggable(Level.FINE)) {
                        logger.fine(logPrefix() + " Completing current upload '" + this.uploadId + "', resources = " + currentObjectResourceCount
                            + ", currentObjectSize = " + currentObjectSize + " bytes");
                    }
                    completeCurrentUpload();
                }

                // Accumulate the payload in the outputStream buffer
                if (this.outputStream.size() > 0) {
                    this.outputStream.write(NDJSON_LINE_SEPARATOR);
                }
                this.currentObjectSize += t.transferTo(this.outputStream);
                this.currentObjectResourceCount++;

                // upload now if we have reached the Goldilocks threshold size for a part
                uploadWhenReady();
            }

            // Should we continue processing? Need to keep an eye on the transaction time
            // to make sure the transaction completes before Liberty complains.
            boolean timeExpired = isTxTimeExpired();
            if (timeExpired && logger.isLoggable(Level.FINE)) {
                logger.fine(logPrefix() + " Stopping to allow tx commit before timeout");
            }
            return !timeExpired;
        } catch (Exception x) {
            // Record the exception so that it can be rethrown from the main processing loop
            this.processingException = x;
            return Boolean.FALSE; // will break from the result set processing loop
        }
    }

    /**
     * Check to see if the current clock time exceeds the marker time we
     * laid down to stop processing to make sure we commit before the
     * Liberty transaction timeout limit.
     * @return
     */
    protected boolean isTxTimeExpired() {
        return System.nanoTime() > txEndTime;
    }

    /**
     * Initiate the upload to COS if we don't already have one active.
     */
    private void uploadWhenReady() throws Exception {
        // Initiate the upload if we don't have one active
        if (this.uploadId == null) {
            // Start a new upload
            if (cosBucketPathPrefix != null && cosBucketPathPrefix.trim().length() > 0) {
                this.currentObjectName = cosBucketPathPrefix + "/" + fhirResourceType + "_" + this.currentUploadNumber + ".ndjson";
            } else {
                this.currentObjectName = "job" + jobContext.getExecutionId() + "/" + fhirResourceType + "_" + this.currentUploadNumber + ".ndjson";
            }
            uploadId = BulkDataUtils.startPartUpload(cosClient, cosBucketName, this.currentObjectName, isExportPublic);

            if (logger.isLoggable(Level.FINE)) {
                logger.fine(logPrefix() + " Started new multi-part upload: '" + this.uploadId + "'");
            }
        }

        // Goldilocks. Each part needs to be large (because S3 limits
        // the total number of parts), but not so large that the
        // upload would take too long and exceed our transaction
        // timeout.
        if (this.outputStream.size() > this.partUploadTriggerSize) {
            uploadPart();
        }
    }

    /**
     * Upload the contents of the outputStream (data buffer) using the
     * current multi-part upload
     * @throws Exception
     */
    private void uploadPart() throws Exception {
        // S3 API: Part number must be an integer between 1 and 10000
        int currentObjectPartNumber = uploadedParts.size() + 1;
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(logPrefix() + " Uploading part# " + currentObjectPartNumber + " ["+ outputStream.size() + " bytes] for uploadId '" + this.uploadId + "'");
        }

        byte[] buffer = outputStream.toByteArray();
        InputStream is = new ByteArrayInputStream(buffer);
        PartETag uploadResult = BulkDataUtils.multiPartUpload(cosClient, cosBucketName, currentObjectName,
            this.uploadId, is, buffer.length, currentObjectPartNumber);
        this.uploadedParts.add(uploadResult);
        outputStream.reset();
    }

    /**
     * Close out the current upload
     */
    private void completeCurrentUpload() throws Exception {
        if (uploadId == null) {
            throw new IllegalStateException("Upload is not active");
        }

        // upload any final amount of data we have in the buffer
        if (this.outputStream.size() > 0) {
            logger.fine(logPrefix() + " uploading final part for '" + this.uploadId + "'");
            uploadPart();
        }

        // Ask COS to finalize the upload for the current object.
        try  {
            logger.fine(logPrefix() + " finishing multi-part upload '" + this.uploadId + "'");
            BulkDataUtils.finishMultiPartUpload(cosClient, cosBucketName, currentObjectName, uploadId,
                this.uploadedParts);

            // record how many resources we've exported for COS object. This is
            // used by the collector/analyzer to generate a list of objects. Inherited from
            // the legacy system export impl. It's currently not clear why we don't simply
            // store the list of item names and the associated count for each
            resourceCounts.add(this.currentObjectResourceCount);
        } finally {
            resetUploadState();
        }
    }

    /**
     * Reset any state associated with the last upload
     */
    private void resetUploadState() {
        // Note this only resets the state related to upload...it does not and
        // should not affect the state related to reading because we might still
        // have more data to process and upload into a new COS object.
        logger.fine(logPrefix() + " resetting state so we are ready to upload the next object");
        this.uploadedParts.clear();
        this.uploadId = null;
        this.currentObjectName = null;
        this.currentObjectSize = 0;
        this.currentObjectResourceCount = 0;
        this.currentUploadNumber++;
    }

    /**
     * Load the state of this object from the given checkpoint
     * @param cp
     */
    private void loadStateFrom(CheckpointUserData cp) {
        // Clear the internal collections before copying in the checkpoint values
        this.uploadedParts.clear();
        this.resourcesForLastTimestamp.clear();
        this.resourceCounts.clear();

        // Note the number comments are to help match this
        // with the checkpointInfo method below. fhirResourceType
        // is injected, so we don't need to set it from the checkpoint
        this.fromLastModified = cp.getFromLastModified(); // 1
        this.uploadId = cp.getUploadId(); // 2
        this.uploadedParts.addAll(cp.getUploadedParts()); // 3
        this.currentObjectName = cp.getCurrentObjectName(); // 4
        this.currentObjectSize = cp.getCurrentObjectSize(); // 5
        this.currentObjectResourceCount = cp.getCurrentObjectResourceCount(); // 6
        this.currentUploadNumber = cp.getCurrentUploadNumber(); // 7
        this.resourcesForLastTimestamp.addAll(cp.getResourcesForLastTimestamp()); // 8
        this.resourceCounts.addAll(cp.getResourceCounts()); // 9

        // As a final step, initialize the lastTimestamp to align with our initial state
        this.lastTimestamp = this.fromLastModified;
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        logger.entering(CLASS, "checkpointInfo");

        // Create a checkpoint object representing the current state of this reader. This checkpoint
        // data is used to initialize this reader again if the job has to be restarted for any reason
        CheckpointUserData cp = new CheckpointUserData();
        cp.setResourceType(this.fhirResourceType);
        cp.setFromLastModified(this.fromLastModified); // 1
        cp.setUploadId(this.uploadId); // 2
        cp.setUploadedParts(uploadedParts); // 3
        cp.setCurrentObjectName(currentObjectName); // 4
        cp.setCurrentObjectSize(currentObjectSize); // 5
        cp.setCurrentObjectResourceCount(currentObjectResourceCount); // 6
        cp.setCurrentUploadNumber(currentUploadNumber); // 7
        cp.setResourcesForLastTimestamp(this.resourcesForLastTimestamp); // 8
        cp.setResourceCounts(this.resourceCounts); // 9

        logger.exiting(CLASS, "checkpointInfo");
        return cp;
    }

    @Override
    public void close() throws Exception {
        // NOP
    }
}
