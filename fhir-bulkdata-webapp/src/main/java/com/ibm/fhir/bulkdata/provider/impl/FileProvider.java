/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.provider.impl;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.ibm.fhir.bulkdata.common.BulkDataUtils;
import com.ibm.fhir.bulkdata.dto.ReadResultDTO;
import com.ibm.fhir.bulkdata.jbatch.export.data.TransientUserData;
import com.ibm.fhir.bulkdata.jbatch.load.data.ImportTransientUserData;
import com.ibm.fhir.bulkdata.provider.Provider;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;

/**
 * Wraps behaviors on the File objects on the local volumes.
 */
public class FileProvider implements Provider {

    private static final Logger logger = Logger.getLogger(FileProvider.class.getName());
    private String source = null;
    private long parseFailures = 0l;
    private ImportTransientUserData transientUserData = null;
    private List<Resource> resources = new ArrayList<>();
    private String fhirResourceType = null;

    private int total = 0;

    private TransientUserData chunkData = null;

    private OutputStream out = null;

    private ConfigurationAdapter configuration = ConfigurationFactory.getInstance();

    public FileProvider(String source) throws Exception {
        this.source = source;
    }

    @Override
    public long getSize(String workItem) throws FHIRException {
        // This may be error prone as the file itself may be compressed or on a compressed volume.
        try {
            return Files.size((new File(workItem)).toPath());
        } catch (IOException e) {
            throw new FHIRException("Files size is not computable '" + workItem + "'", e);
        }
    }

    @Override
    public void readResources(long numOfLinesToSkip, String workItem) throws FHIRException {
        try {
            parseFailures = BulkDataUtils.readFhirResourceFromLocalFile(workItem, (int) numOfLinesToSkip, resources, transientUserData);
        } catch (Exception e) {
            throw new FHIRException("Unable to read from Local File", e);
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
    public void registerTransient(long executionId, TransientUserData transientUserData, String cosBucketPathPrefix, String fhirResourceType,
        boolean isExportPublic) throws Exception {
        if (transientUserData == null) {
            logger.warning("registerTransient: chunkData is null, this should never happen!");
            throw new Exception("registerTransient: chunkData is null, this should never happen!");
        }

        this.chunkData = transientUserData;
        this.fhirResourceType = fhirResourceType;
        ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
        String fileName = cosBucketPathPrefix + "_" + fhirResourceType + "_" + chunkData.getUploadCount() + ".ndjson";
        String base = adapter.getBaseFileLocation(source);

        String fn = base + "/" + fileName;
        Path p1 = Paths.get(fn);
        try {
            // This is a trap. Be sure to mark CREATE and APPEND.
            out = Files.newOutputStream(p1, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            logger.warning("Error creating a file '" + fn + "'");
            throw e;
        }
    }

    @Override
    public void close() throws Exception {
        if (out != null) {
            out.close();
        }
    }

    @Override
    public void writeResources(String mediaType, List<ReadResultDTO> dtos) throws Exception {
        for (ReadResultDTO dto : dtos) {
            total += dto.size();
            for (Resource r : dto.getResources()) {
                FHIRGenerator.generator(Format.JSON).generate(r, out);
                out.write(configuration.getEndOfFileDelimiter(source));
            }

            // This replaces the existing numbers each time.
            chunkData.setCurrentUploadResourceNum(total);
            StringBuilder output = new StringBuilder();
            output.append(fhirResourceType);
            output.append('[');
            output.append(total);
            output.append(']');

            chunkData.setResourceTypeSummary(output.toString());
        }
    }
}