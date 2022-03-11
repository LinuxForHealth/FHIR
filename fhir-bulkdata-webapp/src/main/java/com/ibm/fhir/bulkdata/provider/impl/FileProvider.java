/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.provider.impl;

import static com.ibm.fhir.model.type.String.string;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.bulkdata.common.BulkDataUtils;
import com.ibm.fhir.bulkdata.dto.ReadResultDTO;
import com.ibm.fhir.bulkdata.jbatch.export.data.ExportTransientUserData;
import com.ibm.fhir.bulkdata.jbatch.load.data.ImportTransientUserData;
import com.ibm.fhir.bulkdata.provider.Provider;
import com.ibm.fhir.core.FHIRMediaType;
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
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.operation.bulkdata.model.type.StorageType;

/**
 * Wraps behaviors on the File objects on the local volumes.
 */
public class FileProvider implements Provider {

    private static final Logger logger = Logger.getLogger(FileProvider.class.getName());

    private static final byte[] NDJSON_LINESEPERATOR = ConfigurationFactory.getInstance().getEndOfFileDelimiter(null);

    private String source = null;
    private long parseFailures = 0l;

    private ImportTransientUserData transientUserData = null;
    private List<Resource> resources = new ArrayList<>();
    private String fhirResourceType = null;
    private String fileName = null;
    private String exportPathPrefix = null;

    private ExportTransientUserData chunkData = null;

    private OutputStream out = null;

    private ConfigurationAdapter configuration = ConfigurationFactory.getInstance();
    private int maxRead = configuration.getImportNumberOfFhirResourcesPerRead(null);

    public FileProvider(String source) {
        this.source = source;
    }

    private String getFilePath(String workItem) {
        return configuration.getBaseFileLocation(source) + "/" + workItem;
    }

    @Override
    public long getSize(String workItem) throws FHIRException {
        return getSizeWithAbsolute(getFilePath(workItem), workItem);
    }

    public long getSizeWithAbsolute(String absolutePath, String workItem) throws FHIRException {
     // This may be error prone as the file itself may be compressed or on a compressed volume.
        try {
            return Files.size(new File(absolutePath).toPath());
        } catch (IOException e) {
            throw new FHIRException("Files size is not computable '" + workItem + "'", e);
        }
    }

    @Override
    public void readResources(long numOfLinesToSkip, String workItem) throws FHIRException {
        readResourcesWithAbsolute(numOfLinesToSkip, getFilePath(workItem));
    }

    public void readResourcesWithAbsolute(long numOfLinesToSkip, String absolutePath) throws FHIRException {
        resources = new ArrayList<>();

        long line = numOfLinesToSkip;
        try (RandomAccessFile raf = new RandomAccessFile(Paths.get(absolutePath).toFile(), "r")) {
            raf.seek(this.transientUserData.getCurrentBytes());

            try (InputStream in = Channels.newInputStream(raf.getChannel());
                    BufferedInputStream sourceBuffer = new BufferedInputStream(in);
                    CountingStream counter = new CountingStream(sourceBuffer)) {

                String resourceStr = counter.readLine();
                if (resourceStr == null || resourceStr.isEmpty()) {
                    this.transientUserData.setCurrentBytes(this.transientUserData.getCurrentBytes() + 1);
                }

                int chunkRead = 0;
                while (resourceStr != null && chunkRead < maxRead) {
                    chunkRead++;

                    try (StringReader stringReader = new StringReader(resourceStr)) {
                        resources.add(FHIRParser.parser(Format.JSON).parse(stringReader));
                    } catch (FHIRParserException e) {
                        // Log and skip the invalid FHIR resource.
                        parseFailures++;
                        String msg = "readResources: " + "Failed to parse line " + line + " of [" + source + "].";

                        ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
                        String out = adapter.getOperationOutcomeProvider(source);
                        boolean collectImportOperationOutcomes = adapter
                                .shouldStorageProviderCollectOperationOutcomes(source)
                                && !StorageType.HTTPS.equals(adapter.getStorageProviderStorageType(out));
                        if (collectImportOperationOutcomes) {
                            FHIRGenerator.generator(Format.JSON)
                                    .generate(generateException(line, msg),
                                            transientUserData.getBufferStreamForImportError());
                            transientUserData.getBufferStreamForImportError().write(NDJSON_LINESEPERATOR);
                        }
                        logger.log(Level.WARNING, msg);
                    }

                    this.transientUserData.setCurrentBytes(this.transientUserData.getCurrentBytes() + counter.length);
                    counter.length = 0;
                    counter.out.reset();

                    resourceStr = counter.readLine();

                    line++;
                }

            }
        } catch (Exception e) {
            throw new FHIRException("Unable to read from Local File", e);
        }
    }

    /**
     * This specialized delegate that facilitates reading of resources into bytes
     * without the buffering as there is a local file with direct read and seek.
     */
    public static class CountingStream extends InputStream {
        private static int LF = '\n';
        private static final long MAX_LENGTH_PER_LINE = 2147483648l;

        // 256kb block
        private ByteArrayOutputStream out = new ByteArrayOutputStream(256000);
        private boolean eol = false;
        private long length = 0;

        private InputStream delegate;

        /**
         * ctor
         * @param in
         */
        public CountingStream(InputStream in) {
            this.delegate = in;
        }

        /**
         * reset the line
         */
        public void resetLine() {
            out.reset();
            eol = false;
        }

        /**
         * @return the length of the resources returned in the reader
         */
        public long getLength() {
            return length;
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

        public boolean eol() {
            return eol;
        }

        /**
         * Returns the line that is aggregated up until a new line character
         * @return
         * @throws IOException
         */
        public String readLine() throws IOException {
            int r = read();
            while (r != -1) {
                if (eol()) {
                    eol = false;
                    return getLine();
                }
                r = read();
            }
            if (r == -1 && length > 0) {
                return getLine();
            }
            return getLine();
        }

        @Override
        public int read() throws IOException {
            int r = delegate.read();
            if (r == -1) {
                return -1;
            }
            byte b = (byte) r;
            if (LF == (int) b) {
                length++;
                eol = true;
            } else {
                length++;
                if (length == MAX_LENGTH_PER_LINE) {
                    throw new IOException("Current Line in NDJSON exceeds limit " + MAX_LENGTH_PER_LINE);
                }
                out.write(b);
            }
            return b;
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
    public void registerTransient(long executionId, ExportTransientUserData transientUserData, String exportPathPrefix, String fhirResourceType) throws Exception {
        if (transientUserData == null) {
            String msg = "registerTransient: chunkData is null, this should never happen!";
            logger.warning(msg);
            throw new Exception(msg);
        }

        this.chunkData = transientUserData;
        this.fhirResourceType = fhirResourceType;
        this.exportPathPrefix = exportPathPrefix;
    }

    @Override
    public void close() throws Exception {
        if (out != null) {
            out.close();
        }
    }

    @Override
    public void writeResources(String mediaType, List<ReadResultDTO> dtos) throws Exception {
        if (!FHIRMediaType.APPLICATION_NDJSON.equals(mediaType)) {
            throw new UnsupportedOperationException("FileProvider does not support writing files of type " + mediaType);
        }
        if (chunkData.getBufferStream().size() == 0) {
            // Early exit condition: nothing to write so just set the latWrittenPageNum and return
            chunkData.setLastWrittenPageNum(chunkData.getPageNum());
            return;
        }

        if (out == null) {
            this.fileName = exportPathPrefix + File.separator + fhirResourceType + "_" + chunkData.getUploadCount() + ".ndjson";
            String base = configuration.getBaseFileLocation(source);

            String folder = base + File.separator + exportPathPrefix + File.separator;
            Path folderPath = Paths.get(folder);
            try {
                Files.createDirectories(folderPath);
            } catch(IOException ioe) {
                if (!Files.exists(folderPath)) {
                    throw ioe;
                }
            }

            String fn = base + File.separator + fileName;
            Path p1 = Paths.get(fn);
            try {
                // This is a trap. Be sure to mark CREATE and APPEND.
                out = Files.newOutputStream(p1, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (IOException e) {
                logger.warning("Error creating a file '" + fn + "'");
                throw e;
            }
        }

        chunkData.getBufferStream().writeTo(out);
        chunkData.getBufferStream().reset();

        if (chunkData.isFinishCurrentUpload()) {
            // Partition status for the exported resources, e.g, Patient[1000,1000,200]
            BulkDataUtils.updateSummary(fhirResourceType, chunkData);

            out.close();
            out = null;
            chunkData.setPartNum(1);
            chunkData.setCurrentUploadResourceNum(0);
            chunkData.setCurrentUploadSize(0);
            chunkData.setFinishCurrentUpload(false);
            chunkData.setUploadCount(chunkData.getUploadCount() + 1);
        } else {
            chunkData.setPartNum(chunkData.getPartNum() + 1);
        }

        chunkData.setLastWrittenPageNum(chunkData.getPageNum());
    }

    @Override
    public void pushEndOfJobOperationOutcomes(ByteArrayOutputStream baos, String folder, String fileName) throws FHIRException {
            String base = configuration.getBaseFileLocation(source);

            if (baos.size() > 0) {
                Path folderPath = Paths.get(base + File.separator + folder);
                try {
                    Files.createDirectories(folderPath);
                } catch (IOException ioe) {
                    if (!Files.exists(folderPath)) {
                        throw new FHIRException(
                                "Error accessing operationoutcome folder during $import '" + folderPath + "'");
                    }
                }

                String fn = base + File.separator + folder + File.separator + fileName;
                Path p1 = Paths.get(fn);
                try {
                    // Be sure to mark CREATE and APPEND.
                    out = Files.newOutputStream(p1, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                    baos.writeTo(out);
                    baos.reset();
                    out.close();
                } catch (IOException e) {
                    logger.warning("Error creating a file '" + fn + "'");
                    throw new FHIRException("Error creating a file operationoutcome during $import '" + fn + "'");
                }
            }
    }
}
