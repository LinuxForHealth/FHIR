/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.provider.impl;

import static com.ibm.fhir.model.type.String.string;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
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
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.operation.bulkdata.client.S3ClientGenerator;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.operation.bulkdata.config.s3.S3HostStyle;
import com.ibm.fhir.operation.bulkdata.model.type.StorageType;

/**
 * S3Provider for ibm-cos and aws-s3
 */
public class S3Provider implements Provider {

    private static final Logger LOG = Logger.getLogger(S3Provider.class.getName());
    private static final byte[] NDJSON_LINESEPERATOR = ConfigurationFactory.getInstance().getEndOfFileDelimiter(null);

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

    private String source = null;
    private String cosBucketPathPrefix = null;
    private String fhirResourceType = null;

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
        this.source = source;
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
        int mux = 0;

        // The cached FHIRParserException
        FHIRParserException fpeDownstream = null;

        // Closed when the Scope is out. The size is double the read window.
        // The backing array is allocated at creation.
        ByteArrayOutputStream cacheOut = new ByteArrayOutputStream(512000);
        boolean complete = false;
        while (!complete) {
            // Condition: At the end of the file... and it should never be more than the file Size
            // however, in rare circumstances the person may have 'grown' or added to the file
            // while operating on the $import and we want to defensively end rather than an exact match
            // Early exit from the loop...
            long start = this.transientUserData.getCurrentBytes();
            if (this.transientUserData.getImportFileSize() <= start) {
                complete = true; // NOP
                break;
            }

            // Condition: Window would exceed the maximum File Size
            // Prune the end to -1 off the maximum.
            // The following is 256K window. 256K is used so we only drain a portion of the inputstream.
            // and not the whole file's input stream.
            long end = start + 256000;
            if (end >= this.transientUserData.getImportFileSize()) {
                end = this.transientUserData.getImportFileSize() - 1;
                complete = true; // We still need to process the bytes.
            }

            // Request the start and end of the S3ObjectInputStream that's going to be retrieved
            GetObjectRequest req = new GetObjectRequest(b, workItem)
                                            .withRange(start, end);

            if (LOG.isLoggable(Level.FINE)) {
                // Useful when debugging edge of the stream problems
                LOG.fine("S3ObjectInputStream --- " + start + " " + end);
            }

            boolean parsedWithIssue = false;
            try (S3Object obj = c.getObject(req);
                    S3ObjectInputStream in = obj.getObjectContent();
                    BufferedInputStream buffer = new BufferedInputStream(in);
                    CountingStream reader = new CountingStream(cacheOut, in)) {

                // The interior block allows a drain operation to be executed finally.
                // as a best practice we want to drain the remainder of the input
                // this drain should be at worst 255K (-1 for new line character)
                try {
                    String resourceStr = reader.readLine();
                    // The first line is a large resource
                    if (resourceStr == null) {
                        this.transientUserData.setCurrentBytes(this.transientUserData.getCurrentBytes() + reader.length);
                        reader.length = 0;
                        mux++;
                    }

                    while (resourceStr != null && totalReads < maxRead) {
                        try (StringReader stringReader = new StringReader(resourceStr)) {
                            tempResources.add(FHIRParser.parser(Format.JSON).parse(stringReader));
                        } catch (FHIRParserException fpe) {
                            // Log and skip the invalid FHIR resource.
                            parseFailures++;
                            parsedWithIssue = true;
                            fpeDownstream = fpe;
                        }

                        long priorLineLength = reader.length;
                        reader.length = 0;
                        resourceStr = reader.readLine();

                        if (!parsedWithIssue) {
                            this.transientUserData.setCurrentBytes(this.transientUserData.getCurrentBytes() + priorLineLength);
                            numberOfBytesRead += reader.length;
                            totalReads++;
                        } else if ((parsedWithIssue && resourceStr != null)
                                || (parsedWithIssue && 
                                        (this.transientUserData.getImportFileSize() <= this.transientUserData.getCurrentBytes() + priorLineLength))) { 
                            // This is potentially end of bad line
                            // -or-
                            // This is the last line failing to parse
                            long line = this.transientUserData.getNumOfProcessedResources() + totalReads;
                            LOG.log(Level.SEVERE, "readResources: Failed to parse line " + totalReads + " of [" + workItem + "].", fpeDownstream);
                            String msg = "readResources: " + "Failed to parse line " + line + " of [" + workItem + "].";

                            ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
                            String out = adapter.getOperationOutcomeProvider(source);
                            boolean collectImportOperationOutcomes = adapter.shouldStorageProviderCollectOperationOutcomes(source)
                                    && !StorageType.HTTPS.equals(adapter.getStorageProviderStorageType(out));
                            if (collectImportOperationOutcomes) {
                                FHIRGenerator.generator(Format.JSON)
                                    .generate(generateException(line, msg),
                                            transientUserData.getBufferStreamForImportError());
                                transientUserData.getBufferStreamForImportError().write(NDJSON_LINESEPERATOR);
                            }
                        }
                    }
                } catch (Exception ex) {
                    LOG.warning("readFhirResourceFromObjectStore: Error proccesing file [" + workItem + "] - " + ex.getMessage());
                    // Throw exception to fail the job, the job can be continued from the current checkpoint after the
                    // problem is solved.
                    throw new FHIRException("Unable to read from S3 during processing", ex);
                } finally {
                    try {
                        reader.drain();
                    } catch (Exception s3e) {
                        LOG.fine(() -> "Error while draining the stream, this is benign");
                        LOG.throwing("S3Provider", "readFromObjectStoreWithLowMaxRange", s3e);
                    }
                }

                // Increment if the last line fails
                if (this.transientUserData.getImportFileSize() <= this.transientUserData.getCurrentBytes()) {
                    parseFailures++;
                }
            } catch (FHIRException fe) {
                throw fe;
            } catch (Exception e) {
                throw new FHIRException("Unable to read from S3 File", e);
            }

            // Condition: The optimized block and the number of Resources read
            // exceed the minimum thresholds or the maximum size of a single resource
            if (tempResources.size() >= maxRead) {
                LOG.fine("TempResourceSize " + tempResources.size());
                complete = true;
            }

            // Condition: The optimized block is exceeded and the number of resources is
            // only one so we want to threshold a maximum number of resources
            // 512K * 5 segments (we don't want to repeat too much work) = 2.6M
            if (numberOfBytesRead > 2621440 && tempResources.size() >= 1) {
                complete = true;
            }

            // Condition: The maximum read block is exceeded and we have at least one Resource
            // 2147483648 / (256*1024*1024) = 8192 Reads
            if (mux == 8193) {
                throw new FHIRException("Too Long a Line");
            }

            // We've read more than one window
            if (mux > 1 && tempResources.size() >=1) {
                break;
            }
        }

        // Condition: There is no complete resource to read.
        if (totalReads == 0) {
            LOG.warning("File grew since the start");
            this.transientUserData.setCurrentBytes(this.transientUserData.getImportFileSize());
        }

        // Add the accumulated resources
        this.resources.addAll(tempResources);
    }

    /**
     * This class is a counting stream delegate to facilitate specific S3 behavior.
     */
    public static class CountingStream extends InputStream {
        private static int LF = '\n';
        private static final long MAX_LENGTH_PER_LINE = 2147483648l;

        // 256kb block
        private ByteArrayOutputStream out;
        private long length = 0;

        private InputStream delegate;

        /**
         * 
         * @param out ByteArrayOutputStream caches the data cross reads
         * @param in InputStream is generally the S3InputStream
         */
        public CountingStream(ByteArrayOutputStream out, InputStream in) {
            this.out = out;
            this.delegate = in;
        }

        /**
         * Gets the String representing the line of bytes.
         * 
         * @return
         * @throws UnsupportedEncodingException
         */
        public String getLine() throws UnsupportedEncodingException {
            String str = new String(out.toByteArray(), "UTF-8");
            if (str.isEmpty()) {
                str = null;
            }
            return str;
        }

        @Override
        public int read() throws IOException {
            return delegate.read();
        }

        /**
         * drains the stream so we don't leave a hanging connection
         * @throws IOException
         */
        public void drain() throws IOException {
            int l = delegate.read();
            while (l != -1) {
                l = delegate.read();
            }
        }

        /**
         * 
         * @param counter
         * @return
         * @throws IOException
         */
        public String readLine() throws IOException {
            int r = read();
            if (r == -1) {
                return null;
            } else {
                String result = null;
                while (r != -1) {
                    byte b = (byte) r;
                    if (LF == (int) b) {
                        length++;
                        r = -1;
                        result = getLine();
                        out.reset();
                    } else {
                        length++;
                        if (length == MAX_LENGTH_PER_LINE) {
                            throw new IOException("Current Line in NDJSON exceeds limit " + MAX_LENGTH_PER_LINE);
                        }
                        out.write(b);
                        r = read();
                    }
                }
                return result;
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
                if (LOG.isLoggable(Level.FINE)) {
                    LOG.fine("pushEndOfJobOperationOutcomes: " + baos.size()
                            + " bytes were successfully appended to COS object - " + fn);
                }
                baos.reset();

                BulkDataUtils.finishMultiPartUpload(client, bucketName, fn, uploadId, Arrays.asList(tag));
            } catch (Exception e) {
                LOG.warning("Error creating a operation outcomes '" + fn + "'");
                throw new FHIRException("Error creating a file operation outcome during $import '" + fn + "'");
            }
        }
    }
    
    /**
     * Generate an operation outcome
     * 
     * @param lineNumber
     * @param msg
     * @return
     */
    private OperationOutcome generateException(long lineNumber, String msg) {
        Issue issue = Issue.builder()
                .severity(IssueSeverity.ERROR)
                .code(IssueType.EXCEPTION)
                .details(
                        CodeableConcept.builder()
                                .text(string(msg))
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