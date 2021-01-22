/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.jbatch.bulkdata.export.fast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemReader;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.model.CreateBucketRequest;
import com.ibm.cloud.objectstorage.services.s3.model.PartETag;
import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.jbatch.bulkdata.common.BulkDataUtils;
import com.ibm.fhir.jbatch.bulkdata.common.Constants;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.ResourcePayload;
import com.ibm.fhir.persistence.helper.FHIRPersistenceHelper;
import com.ibm.fhir.persistence.helper.FHIRTransactionHelper;

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
 */
@Dependent
public class ResourcePayloadReader extends AbstractItemReader {
    private final static Logger logger = Logger.getLogger(ResourcePayloadReader.class.getName());
    private static final String CLASS = ResourcePayloadReader.class.getName();

    // S3 client API to IBM Cloud Object Storage
    private AmazonS3 cosClient = null;

    // TODO ???
    boolean isSingleCosObject = false;

    // TODO understand this
    // The current position in the id-list we read back after
    int currentOffset = 0;


    // The handle to the persistence instance used to fetch the resources we want to export
    FHIRPersistence fhirPersistence;

    // The resource type class of the resources being exported by this instance (derived from the injected fhirResourceType value
    Class<? extends Resource> resourceType;

    /**
     * Fhir tenant id.
     */
    @Inject
    @BatchProperty(name = Constants.FHIR_TENANT)
    String fhirTenant;

    /**
     * Fhir data store id.
     */
    @Inject
    @BatchProperty(name = Constants.FHIR_DATASTORE_ID)
    String fhirDatastoreId;

    /**
     * Fhir resource type to process.
     */
    @Inject
    @BatchProperty(name = Constants.PARTITION_RESOURCE_TYPE)
    String fhirResourceType;


    // TODO ???
    private boolean isExportPublic = true;

    // The IBM COS API key or S3 access key.
    @Inject
    @BatchProperty(name = Constants.COS_API_KEY)
    String cosApiKeyProperty;

    /**
     * The IBM COS service instance id or s3 secret key.
     */
    @Inject
    @BatchProperty(name = Constants.COS_SRVINST_ID)
    String cosSrvinstId;

    /**
     * The Cos End point URL.
     */
    @Inject
    @BatchProperty(name = Constants.COS_ENDPOINT_URL)
    String cosEndpointUrl;

    /**
     * The Cos End point location.
     */
    @Inject
    @BatchProperty(name = Constants.COS_LOCATION)
    String cosLocation;

    /**
     * The Cos bucket name.
     */
    @Inject
    @BatchProperty(name = Constants.COS_BUCKET_NAME)
    String cosBucketName;

    /**
     * The Cos bucket path prefix.
     */
    @Inject
    @BatchProperty(name = Constants.EXPORT_COS_OBJECT_PATHPREFIX)
    String cosBucketPathPrefix;

    /**
     * If use IBM credential or Amazon secret keys.
     */
    @Inject
    @BatchProperty(name = Constants.COS_IS_IBM_CREDENTIAL)
    String cosCredentialIbm;

    /**
     * Fhir export format.
     */
    @Inject
    @BatchProperty(name = Constants.EXPORT_FHIR_FORMAT)
    protected String fhirExportFormat;

    /**
     * Fhir Search from date.
     */
    @Inject
    @BatchProperty(name = Constants.EXPORT_FHIR_SEARCH_FROMDATE)
    String fhirSearchFromDate;

    /**
     * Fhir search to date.
     */
    @Inject
    @BatchProperty(name = Constants.EXPORT_FHIR_SEARCH_TODATE)
    String fhirSearchToDate;

    /**
     * Fhir export type filters. Ignored for the basicsystem export
     */
    @Inject
    @BatchProperty(name = Constants.EXPORT_FHIR_SEARCH_TYPEFILTERS)
    String fhirTypeFilters;

    /**
     * Fhir search page size. Not used
     */
    @Inject
    @BatchProperty(name = Constants.EXPORT_FHIR_SEARCH_PAGESIZE)
    String fhirSearchPageSize;

    @Inject
    @BatchProperty(name = Constants.INCOMING_URL)
    String incomingUrl;

    // Control the number of records to read in each "item".
    @Inject
    @BatchProperty(name = Constants.COS_BUCKET_FILE_MAX_RESOURCES)
    String cosBucketFileMaxResources;
    int resourcesPerItem = Constants.DEFAULT_SEARCH_PAGE_SIZE;

    // The Java Batch context object
    @Inject
    StepContext stepCtx;

    // The context object representing this export job
    @Inject
    JobContext jobContext;

    // The last_modified timestamp to start scanning from
    private Instant fromLastModified;

    // The last resource id we read
    private Long fromResourceId;

    // An end time to scan to, if any
    private Instant toLastModified;

    // The number of seconds we use to break up each fetch into smaller chunks
    private int spanSeconds = 86400;

    // Cap the part upload size to avoid local memory issues. Also need to avoid transaction timeout
    private long partUploadTriggerSize = Constants.COS_PART_MINIMALSIZE * 10L;

    // How large should a single COS item (file) be
    private long maxItemSize = Constants.DEFAULT_COSFILE_MAX_SIZE;

    // The initial size of the buffer we use for export. Most resources are
    // under 10K so this is a reasonable initial value
    private final int initialBufferSize = 10 * 1024;

    private static final char NDJSON_LINE_SEPARATOR = '\n';

    // Assume for now we have a transaction timeout of 120s, but need to give the upload some time to work
    // TODO configuration
    private long txTimeoutMillis = 60000L;

    // The nanoTime after which we want to stop processing to avoid a Liberty transaction timeout
    private long txEndTime;

    // The buffer space we collect data into until we hit the threshold to push to COS
    private ByteArrayOutputStream outputStream;

    // The sum of the multi-part sizes for the current upload
    private long currentItemSize;

    // The COS multi-part upload id for the current COS item
    private String uploadId;

    // The name of the COS item we are currently uploading to
    private String currentItemName;

    // The number of resources we've exported to the current COS item
    private int currentItemResourceCount;

    // For large exports, we might need to split the data into multiple items
    private int currentUploadNumber;

    // The result tags from parts previously uploaded
    private List<PartETag> uploadedParts = new ArrayList<>();

    // Used to record an exception from the lambda used to process payloads
    private Exception processingException;

    /**
     * Public constructor
     */
    public ResourcePayloadReader() {
        super();
        this.outputStream = new ByteArrayOutputStream(this.initialBufferSize);

        // Initialize the configuration from the injected string values
        if (this.fhirSearchFromDate != null) {
            this.fromLastModified = Instant.parse(this.fhirSearchFromDate);
        }

        if (this.fhirSearchToDate != null) {
            this.toLastModified = Instant.parse(this.fhirSearchToDate);
        }

        if (this.cosBucketFileMaxResources != null) {
            this.resourcesPerItem = Integer.parseInt(this.cosBucketFileMaxResources);
        }
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
        if (checkpoint != null) {
            // initialize the state of this ItemReader using information from
            // the checkpoint
            CheckpointUserData cp = (CheckpointUserData)checkpoint;
            loadStateFrom(cp);

            // Just in case the framework tries to reopen an existing instance,
            // make sure we start with an empty output stream
            this.outputStream.reset();
        } else {
            // New job, so need to initialize our checkpoint data based on the
            // job parameters which the framework injects
            CheckpointUserData cp = new CheckpointUserData();
            cp.setFromLastModified(this.fromLastModified);
        }

        if (fhirTenant == null) {
            fhirTenant = Constants.DEFAULT_FHIR_TENANT;
            logger.fine(logPrefix() + " Using default tenant");
        }

        if (fhirDatastoreId == null) {
            fhirDatastoreId = Constants.DEFAULT_FHIR_TENANT;
            logger.fine(logPrefix() + " Using default datastore-id");
        }

        FHIRPersistenceHelper fhirPersistenceHelper = new FHIRPersistenceHelper();
        fhirPersistence = fhirPersistenceHelper.getFHIRPersistenceImplementation();
        resourceType = ModelSupport.getResourceType(fhirResourceType);

        if (cosBucketName == null || cosBucketName.isEmpty()) {
            throw new IllegalStateException("cosBucketName not set");
        }

        // Set up the connection to Cos
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Connecting to COS: " + cosEndpointUrl + " [" + cosLocation + "]");
        }
        isExportPublic = FHIRConfigHelper.getBooleanProperty(FHIRConfiguration.PROPERTY_BULKDATA_BATCHJOB_ISEXPORTPUBLIC, true);
        boolean isCosClientUseFhirServerTrustStore = FHIRConfigHelper
                .getBooleanProperty(FHIRConfiguration.PROPERTY_BULKDATA_BATCHJOB_USEFHIRSERVERTRUSTSTORE, false);
        cosClient = BulkDataUtils.getCosClient(cosCredentialIbm, cosApiKeyProperty, cosSrvinstId, cosEndpointUrl,
                cosLocation, isCosClientUseFhirServerTrustStore);

        if (cosClient == null) {
            logger.warning("Connection to COS failed");
            throw new Exception("Connection to COS failed");
        } else {
            logger.fine("Connected to Cos");
        }

        // Make sure we have the bucket
        cosBucketName = cosBucketName.toLowerCase();
        if (!cosClient.doesBucketExistV2(cosBucketName)) {
            logger.info("Creating COS bucket: '" + this.cosBucketName + "'");
            CreateBucketRequest req = new CreateBucketRequest(cosBucketName);
            cosClient.createBucket(req);
        }


    }


    @Override
    public Object readItem() throws Exception {
        logger.entering(CLASS, "readItem");

        // Not typical for Java batch implementations, readItem is called just once per transaction.
        // We therefore only return from this method if we've processed all the data, or to
        // avoid a transaction timeout
        this.txEndTime = System.nanoTime() + this.txTimeoutMillis * 1000000L;

        // Note we're already running inside a transaction (started by the Javabatch framework)
        // so this txn will just wrap it...the commit won't happen until the checkpoint
        FHIRTransactionHelper txn = new FHIRTransactionHelper(fhirPersistence.getTransaction());
        txn.begin();
        boolean moreData = true;
        try {
            // We break up a potentially large query into smaller spans (e.g. daily). We keep
            // accumulating content until we hit our desired part-upload threshold or
            // we hit a time threshold because we don't want to exceed a transaction timeout.
            // We can also stop once our scan window goes past our configured end-time, if we
            // have one.
            final Instant lastClockTime = Instant.now(); // stop scanning after we hit this time
            while (!isTxTimeExpired() && moreData) {

                // Ask the persistence layer to start fetching the records in the current scan window,
                // using the #processPayload lambda to handle each value as it is retrieved.
                ResourcePayload last = fhirPersistence.fetchResourcePayloads(resourceType, fromLastModified, fromResourceId, toLastModified, spanSeconds, rp->processPayload(rp));

                // check for errors which may have occurred during processing (rare)
                if (this.processingException != null) {
                    logger.log(Level.SEVERE, logPrefix(), this.processingException);
                    throw this.processingException;
                }

                // Determine if we've reached the end
                if (last != null) {
                    // Update our state so that we can start the next scan from the correct
                    // location.
                    this.fromLastModified = last.getLastUpdated();
                    this.fromResourceId = last.getResourceId();
                } else {
                    // no data returned, so try the next window.
                    if (this.spanSeconds > 0) {
                        this.fromLastModified = this.fromLastModified.plusSeconds(spanSeconds);
                    } else {
                        // No data was returned and we didn't limit the query based on span, so
                        // that means there's nothing left
                        moreData = false;
                    }
                }

                // Stop processing when we reach the marked current time (lastClockTime)
                // or the last requested time (toLastModified).
                if (this.toLastModified != null && this.fromLastModified.isAfter(toLastModified)
                        || this.toLastModified.isAfter(lastClockTime)) {
                    // time to end this
                    moreData = false;
                }
            }

            if (!moreData) {
                completeCurrentUpload();
            }

        } finally {
            txn.end();
        }

        logger.exiting(CLASS, "readItem");
        return moreData ? new Object() : null;
    }

    /**
     * Process this payload result. Called as a lambda (callback) from the persistence layer
     * @param t
     * @return
     */
    public Boolean processPayload(ResourcePayload t) {

        try {
            // Check if this data would cause us to exceed the max item size. If
            // so, close the current start a new one
            if (this.uploadId != null && (currentItemSize + this.outputStream.size() > maxItemSize
                    || this.currentItemResourceCount + 1 == this.resourcesPerItem)) {
                completeCurrentUpload();
            }

            // Accumulate the payload in the outputStream buffer
            if (this.outputStream.size() > 0) {
                this.outputStream.write(NDJSON_LINE_SEPARATOR);
            }
            this.currentItemSize += t.transferTo(this.outputStream);
            this.currentItemResourceCount++;

            // now decide if we want to upload
            uploadWhenReady();

            // Should we continue processing? Need to keep an eye on the transaction time
            // to make sure the transaction completes before Liberty complains.
            return !isTxTimeExpired();
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
                this.currentItemName = cosBucketPathPrefix + "/" + fhirResourceType + "_" + this.currentUploadNumber + ".ndjson";
            } else {
                this.currentItemName = "job" + jobContext.getExecutionId() + "/" + fhirResourceType + "_" + this.currentUploadNumber + ".ndjson";
            }
            uploadId = BulkDataUtils.startPartUpload(cosClient, cosBucketName, this.currentItemName, isExportPublic);
        }

        // See if we've collected enough data to trigger the upload for
        // the current part. We want this size to be large to minimize
        // the total number of parts, but not so large that it causes
        // memory pressure for this JVM. Larger parts also mean longer
        // upload times, which need to be considered in relation to the
        // transaction timeout.
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
        int currentItemPartNumber = uploadedParts.size();
        byte[] buffer = outputStream.toByteArray();
        InputStream is = new ByteArrayInputStream(buffer);
        PartETag uploadResult = BulkDataUtils.multiPartUpload(cosClient, cosBucketName, currentItemName,
            this.uploadId, is, buffer.length, currentItemPartNumber);
        this.uploadedParts.add(uploadResult);
        outputStream.reset();
    }

    /**
     * Close out the current upload
     */
    private void completeCurrentUpload() throws Exception {
        if (uploadId == null) {
            return; // make this idempotent
        }

        // upload any final amount of data we have in the buffer
        if (this.outputStream.size() > 0) {
            uploadPart();
        }

        // Ask COS to finalize the upload for the current item.
        try  {
            BulkDataUtils.finishMultiPartUpload(cosClient, cosBucketName, currentItemName, uploadId,
                this.uploadedParts);
        } finally {
            resetUploadState();
        }
    }

    /**
     * Reset any state associated with the last upload
     */
    private void resetUploadState() {
        // This lines up very closely to the state we checkpoint
        this.uploadedParts.clear();
        this.uploadId = null;
        this.currentItemName = null;
        this.currentItemSize = 0;
        this.currentItemResourceCount = 0;
        this.currentUploadNumber++;
    }

    /**
     * Load the state of this object from the given checkpoint
     * @param cp
     */
    private void loadStateFrom(CheckpointUserData cp) {
        // Note the number comments are to help match this
        // with the checkpointInfo method below.
        this.uploadedParts.clear();
        this.fromLastModified = cp.getFromLastModified(); // 1
        this.fromResourceId = cp.getFromResourceId(); // 2
        this.uploadId = cp.getUploadId(); // 3
        this.uploadedParts.addAll(cp.getUploadedParts()); // 4
        this.currentItemName = cp.getCurrentItemName(); // 5
        this.currentItemSize = cp.getCurrentItemSize(); // 6
        this.currentItemResourceCount = cp.getCurrentItemResourceCount(); // 7
        this.currentUploadNumber = cp.getCurrentUploadNumber(); // 8
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        logger.entering(CLASS, "checkpointInfo");

        // Create a checkpoint object representing the current state of this reader. This checkpoint
        // data is used to initialize this reader again if the job has to be restarted for any reason
        CheckpointUserData cp = new CheckpointUserData();
        cp.setFromLastModified(this.fromLastModified); // 1
        cp.setFromResourceId(this.fromResourceId); // 2
        cp.setUploadId(this.uploadId); // 3
        cp.setUploadedParts(uploadedParts); // 4
        cp.setCurrentItemName(currentItemName); // 5
        cp.setCurrentItemSize(currentItemSize); // 6
        cp.setCurrentItemResourceCount(currentItemResourceCount); // 7
        cp.setCurrentUploadNumber(currentUploadNumber); // 8

        logger.exiting(CLASS, "checkpointInfo");
        return cp;
    }

    @Override
    public void close() throws Exception {
        // do nothing.
    }
}
