/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.bulkdata.provider.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.bulkdata.common.BulkDataUtils;
import org.linuxforhealth.fhir.bulkdata.dto.ReadResultDTO;
import org.linuxforhealth.fhir.bulkdata.jbatch.export.data.ExportTransientUserData;
import org.linuxforhealth.fhir.bulkdata.jbatch.load.data.ImportTransientUserData;
import org.linuxforhealth.fhir.bulkdata.jbatch.load.exception.FHIRLoadException;
import org.linuxforhealth.fhir.bulkdata.provider.Provider;
import org.linuxforhealth.fhir.exception.FHIRException;
import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.generator.FHIRGenerator;
import org.linuxforhealth.fhir.model.generator.exception.FHIRGeneratorException;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.parser.exception.FHIRParserException;
import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.type.code.IssueType;
import org.linuxforhealth.fhir.model.util.FHIRUtil;
import org.linuxforhealth.fhir.operation.bulkdata.config.ConfigurationAdapter;
import org.linuxforhealth.fhir.operation.bulkdata.config.ConfigurationFactory;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.BlobServiceVersion;
import com.azure.storage.blob.models.BlobRange;
import com.azure.storage.blob.models.BlobRequestConditions;
import com.azure.storage.blob.models.BlobStorageException;
import com.azure.storage.blob.specialized.AppendBlobClient;

/**
 * AzureProvider integrates the BulkData feature with Azure Blob Storage.
 * The code uses the Blob Clients.
 *
 * $export: The AppendBlobClient is used during upload to append to an export file.
 * $import: The BlobClient with BlobRange is used to retrieve windows of data.
 *
 * @see  https://docs.microsoft.com/en-us/dotnet/api/azure.storage.blobs.specialized.appendblobclient?view=azure-dotnet
 */
public class AzureProvider implements Provider {
    private static final Logger LOG = Logger.getLogger(AzureProvider.class.getName());

    private static final byte[] NEWLINE = "\r\n".getBytes();

    private static final int MAX_BLOCK_SIZE = 4194303;

    private ImportTransientUserData transientUserData = null;
    private ExportTransientUserData chunkData = null;

    private String cosBucketPathPrefix = null;
    private String fhirResourceType = null;

    private List<Resource> resources = new ArrayList<>();

    private long parseFailures = 0;
    private long currentBytes = 0;

    private String connectionString;
    private String container;

    // The version of the Azure Blob API to use
    private String serviceVersion;

    private String workItem;

    private boolean collect = false;

    private BlobClient client;

    /**
     * Configures the Azure based on the storageProvider source
     * @param source
     */
    public AzureProvider(String source) {
        this.connectionString = ConfigurationFactory.getInstance().getStorageProviderAuthTypeConnectionString(source);
        this.container = ConfigurationFactory.getInstance().getStorageProviderBucketName(source);
        this.collect = ConfigurationFactory.getInstance().shouldStorageProviderCollectOperationOutcomes(source);
        this.serviceVersion = ConfigurationFactory.getInstance().getProviderAzureServiceVersion(source);
    }

    /**
     * registers the overrides for testing.
     * @param connectionString
     * @param container the container (also known as a bucket)
     * @param chunkData the export data
     * @param cosBucketPathPrefix path prefix
     * @param fhirResourceType resource type
     * @param currentBytes the long value or location of the end of the last resource
     */
    protected void registerOverride(String connectionString, String container, ExportTransientUserData chunkData,
        String cosBucketPathPrefix, String fhirResourceType, long currentBytes) {
        this.connectionString = connectionString;
        this.container = container;
        this.chunkData = chunkData;
        this.cosBucketPathPrefix = cosBucketPathPrefix;
        this.fhirResourceType = fhirResourceType;
        this.currentBytes = currentBytes;
    }

    /**
     * initializes the blob client
     * @param workItem
     */
    protected void initializeBlobClient(String workItem) {
        if (client == null) {
            client = new BlobClientBuilder()
                .connectionString(connectionString)
                .containerName(container)
                .blobName(workItem)
                .serviceVersion(getBlobServiceVersion())
                .buildClient();
        }
    }

    @Override
    public void createSource() throws FHIRException {
        try {
            BlobContainerClient blobContainerClient = new BlobContainerClientBuilder()
                .connectionString(connectionString)
                .containerName(container)
                .serviceVersion(getBlobServiceVersion())
                .buildClient();
            blobContainerClient.create();
            LOG.info("Container is created '" + container + "'");
        } catch (BlobStorageException error) {
            LOG.fine("Can't create container. It already exists");
            LOG.throwing(this.getClass().getName(), "createSource", error);
        }
    }

    /**
     * Get the configured BlobServiceVersion value or null if not configured
     * @return the BlobServiceVersion corresponding to this.serviceVersion, or null
     */
    private BlobServiceVersion getBlobServiceVersion() {
        BlobServiceVersion result = null;
        if (this.serviceVersion != null) {
            result = BlobServiceVersion.valueOf(this.serviceVersion);
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("BlobServiceVersion = " + result.getVersion());
            }
        }
        return result;
    }

    @Override
    public long getSize(String workItem) throws FHIRException {
        try {
            initializeBlobClient(workItem);
            long size = client.getProperties().getBlobSize();
            // Only null when we are using the AzureTransformer
            if (transientUserData != null) {
                transientUserData.setImportFileSize(size);
            }
            LOG.fine(() -> workItem + " [" + size + "]");
            return size;
        } catch (Exception e) {
            LOG.throwing("AzureProvider", "getSize", e);
            LOG.fine("Error Getting File Size '" + container + "/" + workItem + "'");
            throw new FHIRLoadException("Error Getting File Size '" + container + "/" + workItem + "'");
        }
    }

    /**
     * lists the blobs on the container.
     * @throws FHIRException
     */
    public void listBlobsForContainer() throws FHIRException {
        try {
            BlobContainerClient containerClient = new BlobContainerClientBuilder()
                    .connectionString(connectionString)
                    .containerName(container)
                    .buildClient();
            LOG.info("Listing the blobs");
            containerClient.listBlobs().forEach(blob -> LOG.info("BLOB: " + blob.getName()));
            LOG.info("Finished listing the blobs");
        } catch (Exception e) {
            throw new FHIRException("Error listing the blobs for '" + container + "'", e);
        }
    }

    /**
     * writes directly to Azure using AppendBlock client
     * @param workItem the file
     * @param in
     * @param size
     * @throws Exception
     */
    public void writeDirectly(String workItem, InputStream in, int size) throws Exception{
        initializeBlobClient(workItem);

        AppendBlobClient aClient = client.getAppendBlobClient();
        if (!client.exists().booleanValue() && size > 0) {
            aClient.create();
        }

        byte[] payload = new byte[MAX_BLOCK_SIZE];
        int len = in.read(payload, 0, MAX_BLOCK_SIZE);
        while (len != -1) {
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("Start Loop with '" + len + "'");
            }
            byte[] tmpPayload = payload;
            if (len < MAX_BLOCK_SIZE) {
                tmpPayload = Arrays.copyOfRange(payload, 0, len);
            }
            try (ByteArrayInputStream bais = new ByteArrayInputStream(tmpPayload)) {
                if (len > 0) {
                    aClient.appendBlock(bais, len);
                }
            }
            len = in.read(payload, 0, MAX_BLOCK_SIZE);
        }
        LOG.fine(() -> "Finished Loop");

        // Append a new line.
        try (ByteArrayInputStream bais = new ByteArrayInputStream(NEWLINE)) {
            aClient.appendBlock(bais, NEWLINE.length);
        }
    }

    @Override
    public void writeResources(String mediaType, List<ReadResultDTO> dtos) throws Exception {
        workItem = cosBucketPathPrefix + "/" + fhirResourceType + "_" + chunkData.getUploadCount() + ".ndjson";

        initializeBlobClient(workItem);

        AppendBlobClient aClient = client.getAppendBlobClient();

        byte[] baos = chunkData.getBufferStream().toByteArray();

        // Only create if it's not empty.
        if (!client.exists().booleanValue() && baos.length > 0) {
            aClient.create();
        }
        int current = 0;
        for (int i = 0; i <= (Math.ceil(baos.length/MAX_BLOCK_SIZE)); i++) {
            int payloadLength = MAX_BLOCK_SIZE;
            if (payloadLength + current > baos.length) {
                payloadLength = baos.length - current;
            }
            byte[] payload = new byte[payloadLength];
            for (int j = 0; j < payloadLength; j++) {
                payload[j] = baos[current];
                current++;
            }
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("Byte Progress: current='" + current + "' total='" + baos.length + "' payload='" + payload.length);
            }
            if (payload.length > 0) {
                aClient.appendBlock(
                    new ByteArrayInputStream(payload),
                        payload.length);
            }
        }

        LOG.fine(() -> "Export Write is finished");

        if (dtos != null) {
            dtos.clear();
        }
        chunkData.setPartNum(chunkData.getPartNum() + 1);
        chunkData.getBufferStream().reset();

        if (chunkData.isFinishCurrentUpload()) {
            // Partition status for the exported resources, e.g, Patient[1000,1000,200]
            BulkDataUtils.updateSummary(fhirResourceType, chunkData);

            ConfigurationAdapter config = ConfigurationFactory.getInstance();
            long resourceCountThreshold = config.getCoreAzureObjectResourceCountThreshold();
            long sizeThreshold = config.getCoreAzureObjectSizeThreshold();
            if (chunkData.getPageNum() < chunkData.getLastPageNum()) {
                chunkData.setPartNum(1);
                chunkData.setUploadId(null);
                chunkData.setCurrentUploadResourceNum(0);
                chunkData.setCurrentUploadSize(0);
                chunkData.setFinishCurrentUpload(false);
                chunkData.getCosDataPacks().clear();
                chunkData.setUploadCount(chunkData.getUploadCount() + 1);
            } else if (chunkData.getCurrentUploadSize() >= sizeThreshold || resourceCountThreshold >= chunkData.getCurrentUploadResourceNum()){
                chunkData.setUploadCount(chunkData.getUploadCount() + 1);
            }
        }
    }

    @Override
    public void readResources(long numOfLinesToSkip, String workItem) throws FHIRException {
        resources = new ArrayList<>();
        initializeBlobClient(workItem);
        this.workItem = workItem;

        boolean complete = false;
        int window = 0;

        // We process the BlobRange as a window of 100K up to 20000 windows.
        // 20000 windows X 100K means 2000M! has been retrieved, our maximum single resource size.
        // We increment the window after the processing of the current BlobRange.
        StringBuilder previous = new StringBuilder();
        while (!complete && window < 20000) {
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("Window [" + window + "]");
            }
            // Figure out the range that is needed
            long actualRange = 100000L;
            boolean endOfFile = (currentBytes + actualRange) > this.transientUserData.getImportFileSize();
            if (endOfFile) {
                actualRange = this.transientUserData.getImportFileSize() - currentBytes;
                LOG.fine(() -> "Hit the end of the file size='" + this.transientUserData.getImportFileSize() + "' rest='" + (this.transientUserData.getImportFileSize() - currentBytes) + "' currentBytes='" + currentBytes + "'");
                if (actualRange == 0) {
                    break;
                }
            }

            // Assemble the BlobRequestConditions and BlobRange
            BlobRequestConditions options = new BlobRequestConditions();
            BlobRange range = new BlobRange(currentBytes, actualRange);

            // Now we need to calculate where we left off in the download, and adjust the resume point.
            try (InputStream in = client.openInputStream(range, options);
                    CountInputStreamReader counter = new CountInputStreamReader(in);
                    BufferedReader reader = new BufferedReader(counter)) {
                complete = processLines(counter, reader, window, endOfFile, previous);
                LOG.fine(() -> "Number of bytes read are: '" + currentBytes + "'");
            } catch (IOException e) {
                LOG.throwing("AzureProvider", "readResources", e);
                LOG.severe("Problem accessing backend on Azure" + e.getMessage());
                throw new FHIRException("Problem accessing backend Container or Blob on Azure", e);
            }

            // No more processing when we are at the end of the file
            if (endOfFile) {
                transientUserData.setCurrentBytes(this.transientUserData.getImportFileSize());
                break;
            }

            window++;
        }
    }

    /**
     * processes the lines in the ndjson file.
     * @param counter
     * @param reader
     * @param window
     * @param endOfFile
     * @param previous
     * @return
     * @throws FHIRGeneratorException
     * @throws IOException
     */
    public boolean processLines(CountInputStreamReader counter, BufferedReader reader, int window, boolean endOfFile, StringBuilder previous) throws FHIRGeneratorException, IOException {
        boolean complete = false;
        int idx = 0;

        String tmpLine = "";

        String line = reader.readLine();
        while (line != null) {
            idx++;
            // Only when we're on the first line do we want to prefix it
            String compLine;

            if (idx == 1) {
                compLine = previous + line;
            } else {
                compLine = line;
            }

            tmpLine = line;
            line = reader.readLine();

            try (ByteArrayInputStream bais = new ByteArrayInputStream(compLine.getBytes())) {
                LOG.finer(() -> "Azure Resource [R] " + compLine);
                resources.add(FHIRParser.parser(Format.JSON).parse(bais));
            } catch (FHIRParserException fpe) {
                // We're on the last line, so there might be more
                // We may need to combine multiple windows retrieved from the source (up to 2GB)
                if (line == null) {
                    if (LOG.isLoggable(Level.FINE)) {
                        LOG.fine("Azure Resource [PARTIAL] '" + tmpLine + "' Previous: '" + previous + "'");
                    }
                    previous.append(tmpLine);
                } else {
                    addParseFailure(idx);
                }
            }
        }

        if (window > 0 && idx != 1 && currentBytes < transientUserData.getImportFileSize()) {
            // Important here, we're on the Nth window, and need to look back to find the last unparsed
            // and subtract it
            complete = true;
            currentBytes += counter.getLength() - tmpLine.getBytes().length;
        } else {
            currentBytes += counter.getLength();
        }

        // Set so we can move the window forward.
        transientUserData.setCurrentBytes(currentBytes);
        return complete || endOfFile;
    }

    /**
     * logs the parse failures.
     *
     * @param idx
     * @throws FHIRGeneratorException
     * @throws IOException
     */
    public void addParseFailure(int idx) throws FHIRGeneratorException, IOException {
        parseFailures++;
        OperationOutcome.Issue ooi = FHIRUtil.buildOperationOutcomeIssue("Failed to Process "
                + this.transientUserData.getNumOfProcessedResources() + idx, IssueType.EXCEPTION);
        OperationOutcome oo = OperationOutcome.builder().issue(ooi).build();
        FHIRGenerator.generator(Format.JSON).generate(oo, this.transientUserData.getBufferStreamForImportError());
        this.transientUserData.getBufferStreamForImportError().write("\r\n".getBytes());
    }

    /**
     * CountInputStreamReader counts the size of the input.
     */
    private static class CountInputStreamReader extends InputStreamReader {
        private long l = 0;

        public CountInputStreamReader(InputStream in) {
            super(in);
        }

        @Override
        public int read(char[] cbuf, int offset, int length) throws IOException {
            int r = super.read(cbuf, offset, length);
            if (r != -1) {
                l += r;
            }
            return r;
        }

        /**
         * @return the length of the resources returned in the reader
         */
        public long getLength() {
            return l;
        }
    }

    @Override
    public void pushOperationOutcomes() throws FHIRException {
        if (collect && this.transientUserData.getBufferStreamForImportError().size() != 0) {
            // Push the error
            String workItemError = this.workItem + "_oo_errors.ndjson";
            LOG.fine(() -> "Outputting the error to " +  workItemError);
            initializeBlobClient(workItemError);

            AppendBlobClient aClient = client.getAppendBlobClient();
            if (!client.exists().booleanValue()) {
                aClient.create();
            }
            aClient.appendBlock(
                new ByteArrayInputStream(this.transientUserData.getBufferStreamForImportError().toByteArray()),
                    this.transientUserData.getBufferStreamForImportError().size());

            this.transientUserData.getBufferStreamForImportError().reset();
            // Push the success
            // @implNote Sadly we don't do this right now.
        }
    }


    @Override
    public void close() throws Exception {
        // No Operation
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
    public long getNumberOfLoaded() throws FHIRException {
        return resources.size();
    }

    @Override
    public void registerTransient(ImportTransientUserData transientUserData) {
        this.transientUserData = transientUserData;
        this.currentBytes = transientUserData.getCurrentBytes();
    }

    @Override
    public void registerTransient(long executionId, ExportTransientUserData transientUserData, String cosBucketPathPrefix, String fhirResourceType) throws Exception {
        if (transientUserData == null) {
            LOG.warning("registerTransient: chunkData is null, this should never happen!");
            throw new Exception("registerTransient: chunkData is null, this should never happen!");
        }

        this.cosBucketPathPrefix = cosBucketPathPrefix;
        this.fhirResourceType = fhirResourceType;
        this.chunkData = transientUserData;
    }

    @Override
    public void pushEndOfJobOperationOutcomes(ByteArrayOutputStream baos, String folder, String fileName)
            throws FHIRException {
        String fn = folder + "/" + fileName;
        initializeBlobClient(fn);

        AppendBlobClient aClient = client.getAppendBlobClient();
        if (!client.exists().booleanValue()) {
            aClient.create();
        }

        byte[] ba = baos.toByteArray();
        int current = 0;
        for (int i = 0; i <= (Math.ceil(ba.length/MAX_BLOCK_SIZE)); i++) {
            int payloadLength = MAX_BLOCK_SIZE;
            if (payloadLength + current > ba.length) {
                payloadLength = ba.length - current;
            }
            byte[] payload = new byte[payloadLength];
            for (int j = 0; j < payloadLength; j++) {
                payload[j] = ba[current];
                current++;
            }
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("Byte Progress: current='" + current + "' total='" + ba.length + "' payload='" + payload.length);
            }
            if (payload.length > 0) {
                aClient.appendBlock(
                    new ByteArrayInputStream(payload),
                        payload.length);
            }
        }

        baos.reset();
    }
}