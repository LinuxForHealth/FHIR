/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.bucket.scanner;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.bucket.api.BucketLoaderJob;
import org.linuxforhealth.fhir.bucket.api.BucketPath;
import org.linuxforhealth.fhir.bucket.api.FileType;
import org.linuxforhealth.fhir.bucket.api.ResourceEntry;
import org.linuxforhealth.fhir.bucket.cos.COSClient;

/**
 * Grabs work from the bucket database and dispatches items
 * to the thread pool.
 *
 */
public class COSReader extends BaseFileReader {
    private static final Logger logger = Logger.getLogger(COSReader.class.getName());

    // Abstraction of our connection to cloud object storage
    private final COSClient client;

    /**
     * Public constructor
     * @param commonPool thread pool shared by the readers and request handler
     * @param fileType the file type this read is responsible for processing
     * @param client
     * @param resourceHandler
     * @param poolSize
     * @param da
     * @param incremental
     * @param recycleSeconds
     */
    public COSReader(ExecutorService commonPool, FileType fileType, COSClient client, Consumer<ResourceEntry> resourceHandler, int maxInflight, DataAccess da, 
        boolean incremental, int recycleSeconds, boolean incrementalExact,
        double bundleCostFactor, Collection<BucketPath> bucketPaths) {
        super(commonPool, fileType, resourceHandler, maxInflight, da, incremental, recycleSeconds, incrementalExact, bundleCostFactor, bucketPaths);
        this.client = client;
    }

    @Override
    protected void processThr(final BucketLoaderJob job) {
        // Ask the COS client to open the stream for the object and pass it to
        // our process method for reading
        try {
            logger.info("Processing job: " + job.toString());
            job.setProcessingStartTime(System.nanoTime());
            switch (job.getFileType()) {
            case NDJSON:
                // Process the object using our NDJSON reader
                client.process(job.getBucketName(), job.getObjectKey(), br -> processNDJSON(job, br));
                break;
            case JSON:
                // Process the object using our JSON reader
                client.process(job.getBucketName(), job.getObjectKey(), rdr -> processJSON(job, rdr));
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
}