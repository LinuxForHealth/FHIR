/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.provider.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.StringReader;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
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
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;

/**
 * Wraps behaviors on the File objects on the local volumes.
 */
public class FileProvider implements Provider {

    private static final Logger logger = Logger.getLogger(FileProvider.class.getName());

    private static final long READ_BLOCK_OPT = 524288L;

    private String source = null;
    private long parseFailures = 0l;
    @SuppressWarnings("unused")
    private ImportTransientUserData transientUserData = null;
    private List<Resource> resources = new ArrayList<>();
    private String fhirResourceType = null;
    private String fileName = null;
    private String exportPathPrefix = null;

    private ExportTransientUserData chunkData = null;

    private OutputStream out = null;

    private ConfigurationAdapter configuration = ConfigurationFactory.getInstance();
    private int maxRead = configuration.getImportNumberOfFhirResourcesPerRead(null);

    public FileProvider(String source) throws Exception {
        this.source = source;
    }

    private String getFilePath(String workItem) {
        return configuration.getBaseFileLocation(source) + "/" + workItem;
    }

    @Override
    public long getSize(String workItem) throws FHIRException {
        // This may be error prone as the file itself may be compressed or on a compressed volume.
        try {
            return Files.size(new File(getFilePath(workItem)).toPath());
        } catch (IOException e) {
            throw new FHIRException("Files size is not computable '" + workItem + "'", e);
        }
    }

    @Override
    public void readResources(long numOfLinesToSkip, String workItem) throws FHIRException {
        resources = new ArrayList<>();
        try {
            try (RandomAccessFile raf = new RandomAccessFile(Paths.get(getFilePath(workItem)).toFile(), "r")) {
                raf.seek(this.transientUserData.getCurrentBytes());

                try (InputStream in = Channels.newInputStream(raf.getChannel());
                        CountInputStreamReader reader = new CountInputStreamReader(in)) {

                    int chunkRead = 0;

                    String resourceStr = reader.readLine();
                    boolean continueRead = true;
                    while (continueRead && resourceStr != null) {
                        chunkRead++;
                        try (StringReader stringReader = new StringReader(resourceStr)){
                            resources.add(
                                        FHIRParser.parser(Format.JSON)
                                                    .parse(stringReader));
                        } catch (FHIRParserException e) {
                            // Log and skip the invalid FHIR resource.
                            parseFailures++;
                            logger.log(Level.INFO, "readResources: Failed to parse line " + (numOfLinesToSkip + chunkRead) + " of [" + source + "].", e);
                        }

                        // With small resources the 50 at a time gets really expensive.
                        // so we have a READ_BLOCK_OPT to get at least a decent segment to
                        // insert into the db.
                        if (chunkRead < maxRead || reader.getLength() < READ_BLOCK_OPT) {
                            resourceStr = reader.readLine();
                        } else {
                            resourceStr = null;
                            continueRead = false;
                        }
                    }
                    // The Channel enables us to skip the future seek.
                    this.transientUserData.setCurrentBytes(this.transientUserData.getCurrentBytes() + reader.getLength());
                }
            }
        } catch (Exception e) {
            throw new FHIRException("Unable to read from Local File", e);
        }
    }

    /**
     * This specialized class enables a BufferedReader.readLine like implementation
     * without the buffering as there is a local file with direct read and seek.
     */
    private static class CountInputStreamReader extends InputStreamReader {
        // Implies CR
        private static final int LF = '\n';
        private static final long MAX_LENGTH_PER_LINE = 2147483648l;

        private long length = 0;

        public CountInputStreamReader(InputStream in) {
            super(in);
        }

        /**
         * Read the line
         * @return
         * @throws IOException
         */
        public String readLine() throws IOException {
            int r = this.read();
            if (r == -1) {
                // End-of-stream
                return null;
            }
            boolean read = true;
            StringBuilder builder = new StringBuilder();
            int lineLength = 0;

            // Protect against attacks with a max line length (and max size we support in the db).
            while (read && lineLength < MAX_LENGTH_PER_LINE) {
                if (r == -1) {
                    read = false;
                } else if (r == LF) {
                    // \n  case
                    length++;
                    read = false;
                } else {
                    builder.append((char) r);
                    length++;
                    // We only need to account for line length here.
                    lineLength++;
                    r = this.read();
                }
            }
            return builder.toString();
        }

        /**
         * @return the length of the resources returned in the reader
         */
        public long getLength() {
            return length;
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
}