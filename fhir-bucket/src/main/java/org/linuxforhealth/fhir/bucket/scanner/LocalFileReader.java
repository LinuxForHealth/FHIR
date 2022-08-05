/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.bucket.scanner;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.bucket.api.BucketLoaderJob;
import org.linuxforhealth.fhir.bucket.api.BucketPath;
import org.linuxforhealth.fhir.bucket.api.FileType;
import org.linuxforhealth.fhir.bucket.api.ResourceEntry;

/**
 * Grabs work from the bucket database and dispatches items
 * to the thread pool.
 */
public class LocalFileReader extends BaseFileReader {
    private static final Logger logger = Logger.getLogger(LocalFileReader.class.getName());

    /**
     * Public constructor
     * @param commonPool thread pool shared by the readers and request handler
     * @param resourceHandler
     * @param poolSize
     * @param da
     * @param incremental
     * @param recycleSeconds
     * @param incrementalExact
     * @param bundleCostFactor
     * @param bucketPaths
     */
    public LocalFileReader(ExecutorService commonPool, FileType fileType, Consumer<ResourceEntry> resourceHandler, int maxInflight, DataAccess da, boolean incremental, int recycleSeconds, boolean incrementalExact,
        double bundleCostFactor, Collection<BucketPath> bucketPaths) {
        super(commonPool, fileType, resourceHandler, maxInflight, da, incremental, recycleSeconds, incrementalExact, bundleCostFactor, bucketPaths);
    }

    @Override
    protected void processThr(final BucketLoaderJob job) {
        try {
            logger.info("Processing job: " + job.toString());
            job.setProcessingStartTime(System.nanoTime());
            switch (job.getFileType()) {
            case NDJSON:
                // Process the object using our NDJSON reader
                readFile(job.getBucketName(), job.getObjectKey(), br -> processNDJSON(job, br));
                break;
            case JSON:
                // Process the object using our JSON reader
                readFile(job.getBucketName(), job.getObjectKey(), rdr -> processJSON(job, rdr));
                break;
            default:
                logger.warning("Unrecognized file type for job: " + job.toString());
                break;
            }
        } catch (Exception x) {
            // make sure we don't propagate exceptions back to the pool thread. This probably
            // means we are having problems talking to COS or the FHIRBUCKET tracking database
            // because parsing and processing errors are handled inside the processNDJSON/processJSON
            // methods
            logger.log(Level.SEVERE, "Error processing job: " + job.toString(), x);
        }
    }

    /**
     * Read the file described by the job and feed the contents to the given consumer
     * @param bucketName
     * @param objectKey
     * @param consumer
     * @throws IOException
     */
    private void readFile(final String bucketName, final String objectKey, Consumer<BufferedReader> consumer) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(objectKey), StandardCharsets.UTF_8))) {
            consumer.accept(br);
        }
    }
}