/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.provider.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.model.Bucket;
import com.ibm.cloud.objectstorage.services.s3.model.CreateBucketRequest;
import com.ibm.cloud.objectstorage.services.s3.model.GetObjectRequest;
import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request;
import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Result;
import com.ibm.cloud.objectstorage.services.s3.model.PartETag;
import com.ibm.cloud.objectstorage.services.s3.model.S3Object;
import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectInputStream;
import com.ibm.fhir.bulkdata.common.BulkDataUtils;
import com.ibm.fhir.bulkdata.dto.ReadResultDTO;
import com.ibm.fhir.bulkdata.jbatch.export.data.ExportTransientUserData;
import com.ibm.fhir.bulkdata.jbatch.load.data.ImportTransientUserData;
import com.ibm.fhir.bulkdata.provider.Provider;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.operation.bulkdata.client.S3ClientGenerator;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.operation.bulkdata.config.s3.S3HostStyle;

/**
 * S3Provider for ibm-cos and aws-s3
 */
public class S3Provider implements Provider {

    private static final Logger LOG = Logger.getLogger(S3Provider.class.getName());

    private static final long COS_PART_MINIMALSIZE = ConfigurationFactory.getInstance().getCoreCosPartUploadTriggerSize();
    
    private static final S3ClientGenerator GENERATOR = new S3ClientGenerator();

    private ImportTransientUserData transientUserData = null;
    private ExportTransientUserData chunkData = null;

    private long parseFailures = 0l;

    private List<Resource> resources = new ArrayList<>();
    private AmazonS3 client = null;

    private String bucketName = null;
    private boolean pathStyle;

    private boolean create = false;

    private long executionId = -1;

    private String cosBucketPathPrefix = null;
    private String fhirResourceType = null;

    private static final long READ_BLOCK_OPT = 524288L;
    private int maxRead = ConfigurationFactory.getInstance().getImportNumberOfFhirResourcesPerRead(null);

    /**
     * Used for Testing
     */
    protected S3Provider() {
        // NOP
    }

    /**
     * The provider loads the Client based on the Source.
     * The Source (storageProvider) must be s3 compatible.
     * @param source
     * @throws FHIRException
     */
    public S3Provider(String source) throws FHIRException {
        ConfigurationAdapter adapter = ConfigurationFactory.getInstance();

        boolean useFhirServerTrustStore = adapter.shouldCoreCosUseServerTruststore();

        String cosLocation = adapter.getStorageProviderLocation(source);
        String cosEndpointUrl = adapter.getStorageProviderEndpointInternal(source);
        bucketName = adapter.getStorageProviderBucketName(source);

        boolean iam = adapter.isStorageProviderAuthTypeIam(source);

        String apiKey = null;
        String resourceId = null;
        String authType = adapter.getStorageProviderAuthType(source);
        if (authType == null) {
            LOG.warning("S3Wrapper: No Auth Type Found");
        } else if ("hmac".equalsIgnoreCase(authType)) {
            apiKey = adapter.getStorageProviderAuthTypeHmacAccessKey(source);
            resourceId = adapter.getStorageProviderAuthTypeHmacSecretKey(source);
        } else if ("basic".equalsIgnoreCase(authType)) {
            apiKey = adapter.getStorageProviderAuthTypeUsername(source);
            resourceId = adapter.getStorageProviderAuthTypePassword(source);
        } else if ("iam".equalsIgnoreCase(authType)) {
            apiKey = adapter.getStorageProviderAuthTypeIamApiKey(source);
            resourceId = adapter.getStorageProviderAuthTypeIamApiResourceInstanceId(source);
        }

        // Set up the connection to Cos
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("Connecting to S3: [" + cosEndpointUrl + "] [" + cosLocation + "]");
        }

        create = adapter.shouldStorageProviderCreate(source);

        // There are two styles PATH/VIRTUAL_HOST
        S3HostStyle kind = adapter.getS3HostStyleByStorageProvider(source);
        pathStyle = S3HostStyle.PATH.equals(kind);

        // Create a COS/S3 client if it's not created yet.
        client = getClient(iam, apiKey, resourceId, cosEndpointUrl, cosLocation, useFhirServerTrustStore);

        if (client == null) {
            LOG.warning("Failed to get client!");
            throw new FHIRException("Failed to get client!!");
        } else {
            LOG.fine("Succeed to get client!");
        }

        if (bucketName != null) {
            LOG.fine("Succeed to get BucketName!");
            // Naming convention has it as - The bucket name can be between 3 and 63 characters long, and can contain only lower-case characters, numbers, periods, and dashes.
            this.bucketName = bucketName.trim().toLowerCase();
        }
    }

    /**
     * gets the s3 client used with ibm/aws/minio
     * @param iam
     * @param cosApiKeyProperty
     * @param cosSrvinstId
     * @param cosEndpointUrl
     * @param cosLocation
     * @param useFhirServerTrustStore
     * @return
     */
    private AmazonS3 getClient(boolean iam, String cosApiKeyProperty, String cosSrvinstId, String cosEndpointUrl, String cosLocation, boolean useFhirServerTrustStore) {
        return GENERATOR.getClient(iam, cosApiKeyProperty, cosSrvinstId, cosEndpointUrl, cosLocation, useFhirServerTrustStore, pathStyle);
    }

    /**
     * checks to see if the bucket exists.
     *
     * @return
     */
    public boolean exists() {
        // Only valid with a path access style, else we short circuit
        return client != null &&
                (!pathStyle || !client.doesBucketExistV2(bucketName));
    }

    /**
     * lists the buckets when logging out bucket details
     */
    public void listBuckets() {
        if (client != null && LOG.isLoggable(Level.FINE)) {
            final List<Bucket> bucketList = client.listBuckets();
            int bucketCount = 1;
            for (final Bucket bucket : bucketList) {
                LOG.fine("[" + bucketCount + "] - '" + bucket.getName() + "'");
                bucketCount++;
            }
        }
    }

    /**
     * get the list of objects based on the continuation token.
     *
     * @param prefix the search key
     * @param continuationToken
     *            null or string indicating a continuation token.
     * @return
     * @throws FHIRException
     */
    public ListObjectsV2Result getListObject(String prefix, String continuationToken) throws FHIRException {
        if (client != null) {
            ListObjectsV2Request request =
                    new ListObjectsV2Request()
                            .withPrefix(prefix)
                            .withMaxKeys(1000)
                            .withContinuationToken(continuationToken)
                            .withBucketName(bucketName);
            return client.listObjectsV2(request);
        } else {
            throw new FHIRException("Client is not created");
        }
    }

    @Override
    public long getSize(String workItem) throws FHIRException {
        return getSize(client, bucketName, workItem);
    }

    /**
     * Get Size enables multiple use case beyond the default of getSize 
     * 
     * @implNote the Unit is in Bytes
     * 
     * @param c
     * @param bn
     * @param workItem
     * @return
     * @throws FHIRException
     */
    public long getSize(AmazonS3 c, String bn, String workItem) throws FHIRException {
        try {
            S3Object item = c.getObject(new GetObjectRequest(bn, workItem));
            return item.getObjectMetadata().getContentLength();
        } catch (Exception e) {
            throw new FHIRException("Error Getting File Size '" + bn + "/" + workItem + "'", e);
        }
    }

    @Override
    public List<Resource> getResources() throws FHIRException {
        return resources;
    }

    @Override
    public long getNumberOfParseFailures() throws FHIRException {
        return parseFailures;
    }

    @Override
    public void registerTransient(ImportTransientUserData transientUserData) {
        this.transientUserData = transientUserData;
    }

    @Override
    public long getNumberOfLoaded() throws FHIRException {
        return this.resources.size();
    }

    @Override
    public void readResources(long numOfLinesToSkip, String workItem) throws FHIRException {
        readFromObjectStoreWithLowMaxRange(client, bucketName, workItem);
    }

    /**
     * Reads Object Store with Low and Max Ranges
     * @param c
     * @param b
     * @param workItem
     * @throws FHIRException
     */
    protected void readFromObjectStoreWithLowMaxRange(AmazonS3 c, String b, String workItem) throws FHIRException {

        // Don't add tempResources to resources until we're done (we do retry), it's a temporary cache of the Resources
        List<Resource> tempResources = new ArrayList<>();

        // number of bytes read.
        long numberOfBytesRead = 0l;
        int totalReads = 0;

        // Parse/End of Line
        boolean lastParseFailed = false;
        FHIRParserException fpe = null;
        StringBuilder cache = new StringBuilder();
        String temp = null;

        boolean complete = false;
        while (!complete) {
            // Condition: The optimized block and the number of Resources read
            // exceed the minimum thresholds and the maximum size of a single resource
            if (numberOfBytesRead > READ_BLOCK_OPT && tempResources.size() >= maxRead) {
                break;
            }

            // Condition: The optimized block is exceeded and the number of resources is
            // only one so we want to threshold a maximum number of resources
            // 512K * 5 segments (we don't want to repeat too much work) = 2.6M
            if (numberOfBytesRead > 2621440 && tempResources.size() >= 1) {
                break;
            }

            // Condition: The maximum read block is exceeded and we have at least one Resource
            // 2147483648 / (256*1024*1024) = 8192 (we
            if (totalReads == 8193) {
                throw new FHIRException("Too Long a Line");
            }

            // Condition: At the end of the file... and it should never be more than the file Size
            // however, in rare circumstances the person may have 'grown' or added to the file
            // while operating on the $import and we want to defensively end rather than an exact match
            // Early exit from the loop...
            long start = this.transientUserData.getCurrentBytes();
            if (this.transientUserData.getImportFileSize() <= start) {
                break;
            }

            // Condition: Window would exceed the maximum File Size
            // Prune the end to -1 off the maximum.
            // The following is 256K window. 256 is used so we only drain a portion of the inputstream.
            // and not the whole file's input stream.
            long end = start + 268435456L;
            if (end >= this.transientUserData.getImportFileSize()) {
                end = this.transientUserData.getImportFileSize() - 1;
                complete = true; // We still need to process the bytes.
            }

            // Set the start and end of the S3 Object inputstream that we are going to retrieve
            GetObjectRequest req = new GetObjectRequest(b, workItem)
                    .withRange(start, end);

            try (S3Object obj = c.getObject(req);
                    S3ObjectInputStream in = obj.getObjectContent();
                    CountInputStreamReader reader = new CountInputStreamReader(in)) {
                // The interior block allows a drain operation to be executed.
                // as a best practice we want to drain the remainder of the input
                // this drain should be 0-256K.
                try {
                    int chunkRead = 0;
                    boolean continueRead = true;
                    String resourceStr = reader.readLine();
                    while (continueRead && resourceStr != null) {
                        chunkRead++;

                        try (StringReader stringReader = new StringReader(cache.toString() + resourceStr)) {
                            tempResources.add(FHIRParser.parser(Format.JSON).parse(stringReader));
                            lastParseFailed = false;
                            cache = new StringBuilder();
                        } catch (FHIRParserException fpeTemp) {
                            // Log and skip the invalid FHIR resource.
                            lastParseFailed = true;
                            fpe = fpeTemp;
                            temp = resourceStr;
                        }

                        resourceStr = reader.readLine();
                        // Condition: last read in this block...
                        if (resourceStr == null && lastParseFailed) {
                            // Not yet a failure
                            fpe = null;
                            cache.append(temp);
                        } else if (lastParseFailed) {
                            // It failed on the nearly last one
                            parseFailures++;
                            LOG.log(Level.INFO, "readResources: Failed to parse line " + chunkRead + " of [" + workItem + "].", fpe);
                            fpe = null;
                        }

                        this.transientUserData.setCurrentBytes(this.transientUserData.getCurrentBytes() + reader.getLength());
                    }
                    totalReads += chunkRead;
                } catch (Exception ex) {
                    LOG.warning("readFhirResourceFromObjectStore: Error proccesing file [" + workItem + "] - " + ex.getMessage());
                    // Throw exception to fail the job, the job can be continued from the current checkpoint after the
                    // problem is solved.
                    try {
                        reader.drain();
                    } catch (Exception s3e) {
                        LOG.fine(() -> "Error while draining the stream, this is benign");
                        LOG.throwing("S3Provider", "readFromObjectStoreWithLowMaxRange", s3e);
                    }
                    throw new FHIRException("Unable to read from S3 during processing", ex);
                }

                // Increment if the last line fails
                if (lastParseFailed && this.transientUserData.getImportFileSize() <= this.transientUserData.getCurrentBytes()) {
                    parseFailures++;
                }
            } catch (FHIRException fe) {
                throw fe;
            } catch (Exception e) {
                throw new FHIRException("Unable to read from S3 File", e);
            }

        }

        // Add the accumulated resources
        this.resources.addAll(tempResources);
    }

    /**
     * This specialized class enables a BufferedReader.readLine like implementation
     * with buffering.
     *
     * @implNote this is intentionally protected so we can run S3ProviderMain tests.
     */
    protected static class CountInputStreamReader extends InputStreamReader {
        private static final char CR = '\r';
        private static final char LF = '\n';

        private static final long MAX_LENGTH_PER_LINE = 2147483648l;

        private long length = 0;

        // We buffer 256K characters
        private char[] buffer = new char[268435456];
        private int bufferIdx = 0;
        private int bufferSize = 0;

        public CountInputStreamReader(InputStream in) {
            super(in);
        }

        /**
         * Read the line
         * @return
         * @throws IOException
         * @throws FHIRException
         */
        public String readLine() throws IOException, FHIRException {
            StringBuilder builder = new StringBuilder();

            // Read while we are able and it must be under 2G
            boolean read = true;
            int lineLength = 0;
            while (read && lineLength < MAX_LENGTH_PER_LINE) {
                // Determine if we need to reset the buffer
                if (bufferIdx == 0 || bufferIdx == bufferSize) {
                    bufferSize = super.read(buffer);
                    bufferIdx = 0;
                }

                if (bufferSize == -1) {
                    // Empty Line
                    if (builder.length() == 0) {
                        return null;
                    }
                    // End of Line... End of Read.
                    read = false;
                } else {
                    // We have data in the buffer
                    for (;bufferIdx < bufferSize && read; bufferIdx++) {
                        char ch = buffer[bufferIdx];
                        if (ch == CR) {
                            // \r case - We're stripping out the CR early on
                            length++;
                        } else if (ch == LF) {
                            // \n case
                            length++;
                            read = false;
                        } else {
                            builder.append(buffer[bufferIdx]);
                            length++;
                            // We only need to account for line length here.
                            lineLength++;
                        }
                    }
                }
            }

            if (lineLength == MAX_LENGTH_PER_LINE) {
                // Should we throw a custom exception Line Too Long
                throw new FHIRException("Too Long a Line");
            }

            return builder.toString();
        }

        /**
         * @return the length of the resources returned in the reader
         */
        public long getLength() {
            return length;
        }

        /**
         * drains the stream so we don't leave a hanging connection
         * @throws IOException
         */
        public void drain() throws IOException {
            int l = super.read(buffer);
            while (l != -1) {
                l = super.read(buffer);
            }
        }
    }

    /**
     * legacy read from object store uses a skip lines approach which isn't as efficient
     * since all of the data is sucked down, versus retrieve by range.
     *
     * @param numOfLinesToSkip
     * @param workItem
     * @throws FHIRException
     */
    protected void legacyReadFromObjectStore(long numOfLinesToSkip, String workItem) throws FHIRException {
        try {
            parseFailures = BulkDataUtils.readFhirResourceFromObjectStore(client, bucketName, workItem, (int) numOfLinesToSkip, resources, transientUserData);
        } catch (Exception e) {
            throw new FHIRException("Unable to read from S3 File", e);
        }
    }


    @Override
    public void pushOperationOutcomes() throws FHIRException {
        try {
            // Upload OperationOutcomes in buffer if it reaches the minimal size for multiple-parts upload.
            if (transientUserData.getBufferStreamForImport().size() > COS_PART_MINIMALSIZE) {
                if (transientUserData.getUploadIdForOperationOutcomes() == null) {
                    transientUserData.setUploadIdForOperationOutcomes(BulkDataUtils.startPartUpload(client,
                            bucketName, transientUserData.getUniqueIDForImportOperationOutcomes()));
                }

                transientUserData.getDataPacksForOperationOutcomes().add(BulkDataUtils.multiPartUpload(client,
                        bucketName,
                        transientUserData.getUniqueIDForImportOperationOutcomes(),
                        transientUserData.getUploadIdForOperationOutcomes(),
                        new ByteArrayInputStream(transientUserData.getBufferStreamForImport().toByteArray()),
                        transientUserData.getBufferStreamForImport().size(),
                        transientUserData.getPartNumForOperationOutcomes()));

                if (LOG.isLoggable(Level.FINE)) {
                    LOG.fine("pushImportOperationOutcomesToCOS: " + transientUserData.getBufferStreamForImport().size()
                            + " bytes were successfully appended to COS object - " + transientUserData.getUniqueIDForImportOperationOutcomes());
                }
                transientUserData.setPartNumForOperationOutcomes(transientUserData.getPartNumForOperationOutcomes() + 1);
                transientUserData.getBufferStreamForImport().reset();
            }

            // Upload OperationOutcomes in failure buffer if it reaches the minimal size for multiple-parts upload.
            if (transientUserData.getBufferStreamForImportError().size() > COS_PART_MINIMALSIZE) {
                if (transientUserData.getUploadIdForFailureOperationOutcomes() == null) {
                    transientUserData.setUploadIdForFailureOperationOutcomes(BulkDataUtils.startPartUpload(client,
                            bucketName, transientUserData.getUniqueIDForImportFailureOperationOutcomes()));
                }

                transientUserData.getDataPacksForFailureOperationOutcomes().add(BulkDataUtils.multiPartUpload(client,
                        bucketName,
                        transientUserData.getUniqueIDForImportFailureOperationOutcomes(),
                        transientUserData.getUploadIdForFailureOperationOutcomes(),
                        new ByteArrayInputStream(transientUserData.getBufferStreamForImportError().toByteArray()),
                        transientUserData.getBufferStreamForImportError().size(),
                        transientUserData.getPartNumForFailureOperationOutcomes()));
                if (LOG.isLoggable(Level.FINE)) {
                    LOG.fine("pushImportOperationOutcomesToOS: " + transientUserData.getBufferStreamForImportError().size()
                            + " bytes were successfully appended to COS object - " + transientUserData.getUniqueIDForImportFailureOperationOutcomes());
                }
                transientUserData.setPartNumForFailureOperationOutcomes(transientUserData.getPartNumForFailureOperationOutcomes() + 1);
                transientUserData.getBufferStreamForImportError().reset();
            }
        } catch (Exception e) {
            throw new FHIRException("Unable to write to S3 OperationOutcome File", e);
        }
    }

    @Override
    public void createSource() throws FHIRException {
        if (client != null) {
            if (create && !this.exists()) {
                CreateBucketRequest req = new CreateBucketRequest(bucketName);
                client.createBucket(req);
            }
        } else {
            throw new FHIRException("Client is not created");
        }
    }

    public AmazonS3 getClient() {
        return client;
    }

    @Override
    public void close() throws Exception {
        LOG.fine("closing the S3Wrapper");
    }

    @Override
    public void writeResources(String mediaType, List<ReadResultDTO> dtos) throws Exception {
        // Only if we're greater than zero, otherwise there is nothing to upload.
        if (chunkData.getBufferStream().size() > 0) {
            // TODO try PipedOutputStream -> PipedInputStream instead?  Or maybe a ByteBuffer with a flip instead?
            pushFhirJsonsToCos(new ByteArrayInputStream(chunkData.getBufferStream().toByteArray()), chunkData.getBufferStream().size());
        }
        chunkData.setLastWrittenPageNum(chunkData.getPageNum());
    }

    @Override
    public void registerTransient(long executionId, ExportTransientUserData transientUserData, String cosBucketPathPrefix, String fhirResourceType) throws Exception {
        if (transientUserData == null) {
            LOG.warning("registerTransient: chunkData is null, this should never happen!");
            throw new Exception("registerTransient: chunkData is null, this should never happen!");
        }

        this.executionId = executionId;
        this.chunkData = transientUserData;
        this.cosBucketPathPrefix = cosBucketPathPrefix;
        this.fhirResourceType = fhirResourceType;
    }

    private void pushFhirJsonsToCos(InputStream in, int dataLength) throws Exception {
        String itemName;
        if (cosBucketPathPrefix != null && cosBucketPathPrefix.trim().length() > 0) {
            itemName = cosBucketPathPrefix + "/" + fhirResourceType + "_" + chunkData.getUploadCount() + ".ndjson";
        } else {
            itemName = "system_export_" + executionId + "/" + fhirResourceType + "_" + chunkData.getUploadCount() + ".ndjson";
        }

        String uploadId = chunkData.getUploadId();
        if (uploadId == null) {
            uploadId = BulkDataUtils.startPartUpload(client, bucketName, itemName);
            chunkData.setUploadId(uploadId);
        }

        chunkData.getCosDataPacks().add(BulkDataUtils.multiPartUpload(client, bucketName, itemName, uploadId, in, dataLength, chunkData.getPartNum()));
        LOG.info("pushFhirJsonsToCos: '" + dataLength + "' bytes were successfully appended to COS object - '" + itemName + "' uploadId='" + uploadId + "'");
        chunkData.setPartNum(chunkData.getPartNum() + 1);
        chunkData.getBufferStream().reset();

        // Close it out...
        if (chunkData.isFinishCurrentUpload()) {
            BulkDataUtils.finishMultiPartUpload(client, bucketName, itemName, uploadId, chunkData.getCosDataPacks());
            // Partition status for the exported resources, e.g, Patient[1000,1000,200]
            if (chunkData.getResourceTypeSummary() == null) {
                chunkData.setResourceTypeSummary(fhirResourceType + "[" + chunkData.getCurrentUploadResourceNum());
                if (chunkData.getPageNum() >= chunkData.getLastPageNum()) {
                    chunkData.setResourceTypeSummary(chunkData.getResourceTypeSummary() + "]");
                }
            } else {
                chunkData.setResourceTypeSummary(chunkData.getResourceTypeSummary() + "," + chunkData.getCurrentUploadResourceNum());
                if (chunkData.getPageNum() >= chunkData.getLastPageNum()) {
                    chunkData.setResourceTypeSummary(chunkData.getResourceTypeSummary() + "]");
                }
            }

            if (chunkData.getPageNum() < chunkData.getLastPageNum()) {
                chunkData.setPartNum(1);
                chunkData.setUploadId(null);
                chunkData.setCurrentUploadResourceNum(0);
                chunkData.setCurrentUploadSize(0);
                chunkData.setFinishCurrentUpload(false);
                chunkData.getCosDataPacks().clear();
                chunkData.setUploadCount(chunkData.getUploadCount() + 1);
            }
        }
    }

    @Override
    public void pushEndOfJobOperationOutcomes(ByteArrayOutputStream baos, String folder, String fileName)
            throws FHIRException {
        String fn = folder  + "/" + fileName;

        if (baos.size() > 0) {
            try {
                String uploadId = BulkDataUtils.startPartUpload(client, bucketName, fn);

                PartETag tag = BulkDataUtils.multiPartUpload(client, bucketName, fn, uploadId,
                        new ByteArrayInputStream(baos.toByteArray()), baos.size(), 1);
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("pushEndOfJobOperationOutcomes: " + baos.size()
                            + " bytes were successfully appended to COS object - " + fn);
                }
                baos.reset();

                BulkDataUtils.finishMultiPartUpload(client, bucketName, fn, uploadId, Arrays.asList(tag));
            } catch (Exception e) {
                logger.warning("Error creating a operation outcomes '" + fn + "'");
                throw new FHIRException("Error creating a file operation outcome during $import '" + fn + "'");
            }
        }
    }
}
