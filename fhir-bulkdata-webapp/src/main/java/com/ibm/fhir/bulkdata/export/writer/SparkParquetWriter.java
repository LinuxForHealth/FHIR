/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.export.writer;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.SparkSession.Builder;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.fhir.model.resource.Resource;

public class SparkParquetWriter implements AutoCloseable {
    private static final Logger logger = Logger.getLogger(SparkParquetWriter.class.getName());

    private SparkSession spark;

    /**
     * Create a SparkParquetWriter that can only write to file URIs
     */
    public SparkParquetWriter() {
        spark = SparkSession.builder()
            .appName("parquetWriter")
            .master("local[*]")
            .config("spark.ui.enabled", false)
            .config("spark.driver.port", "38000")
            .config("spark.port.maxRetries", "1000")
            .config("spark.driver.host", "127.0.0.1")
            .getOrCreate();
    }

    /**
     * Create a SparkParquetWriter that can write to either file or cos URIs
     *
     * @param isCOS whether to use IBM Cloud Identity and Access Management; if false we use "HMAC" auth
     * @param cosEndpoint the S3 endpoint to connect to; include the scheme, typically "https://")
     * @param apiKeyOrAccessKey a valid ServiceCredential ApiKey (for IAM) or AccessKey (for HMAC)
     * @param serviceInstanceIdOrSecretKey a valid ServiceInstanceId (for IAM) or SecretKey (for HMAC)
     */
    public SparkParquetWriter(boolean useIAM, String cosEndpoint, String apiKeyOrAccessKey, String serviceInstanceIdOrSecretKey) {
        Builder sessionBuilder = SparkSession.builder()
                .appName("parquetWriter")
                .master("local[*]")
                .config("spark.ui.enabled", false)
                .config("fs.cos.impl", "com.ibm.stocator.fs.ObjectStoreFileSystem")
                .config("fs.stocator.scheme.list", "cos")
                .config("fs.stocator.cos.impl", "com.ibm.stocator.fs.cos.COSAPIClient")
                .config("fs.stocator.cos.scheme", "cos");

        if (useIAM) {
            sessionBuilder
                .config("fs.cos.fhir.endpoint", cosEndpoint)
                .config("fs.cos.fhir.iam.api.key", apiKeyOrAccessKey)
                .config("fs.cos.fhir.iam.service.id", serviceInstanceIdOrSecretKey);
        } else {
            sessionBuilder
                .config("fs.cos.fhir.endpoint", cosEndpoint)
                .config("fs.cos.fhir.access.key", apiKeyOrAccessKey)
                .config("fs.cos.fhir.secret.key", serviceInstanceIdOrSecretKey);
        }

        spark = sessionBuilder.getOrCreate();
    }

    /**
     * Write a list of resources to a parquet file under a single logical file that is actually a directory.
     *
     * @param resources the list of resources to write
     * @param outDirName the target directory, using either a file URI or a cos URI like "cos://bucket.service/object-key"
     * @throws FHIRGeneratorException
     * @implnote If a cos URI is passed, the file will be created if needed. For a file URI
     */
    public void writeParquet(List<Resource> resources, String outDirName)
            throws FHIRGeneratorException {
        List<String> jsonResources = new ArrayList<>();

        FHIRGenerator generator = FHIRGenerator.generator(Format.JSON);
        for (Resource singleResource : resources) {
            StringWriter stringWriter = new StringWriter();
            generator.generate(singleResource, stringWriter);
            jsonResources.add(stringWriter.toString());
        }

        Dataset<String> jDataset = spark.createDataset(jsonResources, Encoders.STRING());
        Dataset<?> jsonDF = spark.read().json(jDataset);

        if (logger.isLoggable(Level.FINEST)) {
            // Show prints to system.out
            // We'd need to temporarily redirect that to our own OutputStream to properly log this
            // but its really just for debug, so I chose to print it to System.out for now.
            jsonDF.show(false);
        }

        jsonDF.coalesce(1).write().mode("append").parquet(outDirName);
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Parquet file written under " + outDirName);
        }
    }

    @Override
    public void close() throws Exception {
        // TODO: anything to clean up?
    }
}
