/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.jbatch.bulkdata.source.type.impl;

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

import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.jbatch.bulkdata.common.BulkDataUtils;
import com.ibm.fhir.jbatch.bulkdata.common.Constants;
import com.ibm.fhir.jbatch.bulkdata.export.data.TransientUserData;
import com.ibm.fhir.jbatch.bulkdata.load.data.ImportTransientUserData;
import com.ibm.fhir.jbatch.bulkdata.source.type.SourceWrapper;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.operation.bulkdata.config.preflight.impl.FilePreflight;

/**
 * Wraps behaviors on the File objects on the local volumes.
 */
public class FileWrapper implements SourceWrapper {

    private static final Logger logger = Logger.getLogger(FileWrapper.class.getName());
    private String source = null;
    private long parseFailures = 0l;
    private ImportTransientUserData transientUserData = null;
    private List<Resource> resources = new ArrayList<>();

    private TransientUserData chunkData = null;

    private OutputStream out = null;

    public FileWrapper(String source) throws Exception {
        this.source = source;
        FilePreflight preflight = new FilePreflight(source, source, null);
        try {
            preflight.preflight();
        } catch (FHIROperationException e) {
            throw new Exception(e);
        }
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
        this.chunkData = transientUserData;

        ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
        String fileName = cosBucketPathPrefix + "_" + fhirResourceType + "_" + chunkData.getUploadCount() + ".ndjson";
        String base = adapter.getBaseFileLocation(source);

        String fn = base + "/" + fileName;
        Path p1 = Paths.get(fn);
        try {
            out = Files.newOutputStream(p1, StandardOpenOption.CREATE);
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
    public void writeResources(String mediaType, List<Resource> resources) throws Exception {
        for (Resource r : resources) {
            FHIRGenerator.generator(Format.JSON).generate(r, out);
            out.write(Constants.NDJSON_LINESEPERATOR);
        }
    }
}