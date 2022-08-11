/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.bulkdata.provider.impl;

import static org.linuxforhealth.fhir.model.type.String.string;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.bulkdata.common.BulkDataUtils;
import org.linuxforhealth.fhir.bulkdata.dto.ReadResultDTO;
import org.linuxforhealth.fhir.bulkdata.jbatch.export.data.ExportTransientUserData;
import org.linuxforhealth.fhir.bulkdata.jbatch.load.data.ImportTransientUserData;
import org.linuxforhealth.fhir.bulkdata.provider.Provider;
import org.linuxforhealth.fhir.core.FHIRConstants;
import org.linuxforhealth.fhir.core.FHIRMediaType;
import org.linuxforhealth.fhir.exception.FHIRException;
import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.generator.FHIRGenerator;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.parser.exception.FHIRParserException;
import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.resource.OperationOutcome.Issue;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.code.IssueSeverity;
import org.linuxforhealth.fhir.model.type.code.IssueType;
import org.linuxforhealth.fhir.operation.bulkdata.config.ConfigurationAdapter;
import org.linuxforhealth.fhir.operation.bulkdata.config.ConfigurationFactory;
import org.linuxforhealth.fhir.operation.bulkdata.model.type.StorageType;

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
    private BufferedReader br = null;

    private ConfigurationAdapter configuration = ConfigurationFactory.getInstance();

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
            long line = 0;
            try {
                if (br == null) {
                    br = Files.newBufferedReader(Paths.get(getFilePath(workItem)));
                }
                // Imports must only skip lines if the size is higher, not equal to
                // otherwise we start skipping the last line to skip.
                for (int i = 0; i < numOfLinesToSkip; i++) {
                    line++;
                    br.readLine(); // We know the file has at least this number.
                }

                String resourceStr = br.readLine();
                int chunkRead = 0;
                int maxRead = configuration.getImportNumberOfFhirResourcesPerRead(null);
                while (resourceStr != null && chunkRead <= maxRead) {
                    line++;
                    chunkRead++;
                    try {
                        resources.add(FHIRParser.parser(Format.JSON).parse(new StringReader(resourceStr)));
                    } catch (FHIRParserException e) {
                        // Log and skip the invalid FHIR resource.
                        parseFailures++;

                        String msg = "readResources: " + "Failed to parse line " + line + " of [" + source + "].";

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
                        logger.log(Level.WARNING, msg);
                    }
                    resourceStr = br.readLine();
                }
            } finally {
                if (br != null) {
                    br.close();
                }
            }
        } catch (Exception e) {
            throw new FHIRException("Unable to read from Local File", e);
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
                        .url(FHIRConstants.EXT_BASE + "bulkdata/linenumber")
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
            // Early exit condition:  nothing to write so just set the latWrittenPageNum and return
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