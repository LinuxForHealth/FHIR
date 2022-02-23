/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.provider.impl;

import com.ibm.cloud.objectstorage.ClientConfiguration;
import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
import com.ibm.cloud.objectstorage.auth.BasicAWSCredentials;
import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
import com.ibm.fhir.bulkdata.jbatch.load.data.ImportTransientUserData;
import com.ibm.fhir.exception.FHIRException;

/**
 * Run the logic directly against an S3 instance and bucket
 */
public class S3ProviderMain {

    /**
     * Runs a test with S3Providers
     * @param args
     * @throws FHIRException
     */
    public static void main(String[] args) throws FHIRException {
        BasicAWSCredentials credentials = new BasicAWSCredentials(args[1], args[2]);

        ClientConfiguration clientConfig =
                new ClientConfiguration()
                    .withRequestTimeout(10000)
                    .withTcpKeepAlive(true)
                    .withSocketTimeout(10000);

        com.ibm.cloud.objectstorage.services.s3.AmazonS3 client = AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withEndpointConfiguration(new EndpointConfiguration(args[0], "us"))
            .withClientConfiguration(clientConfig)
            .withPathStyleAccessEnabled(true)
            .build();

        S3Provider provider = new S3Provider();
        long size = provider.getSize(client, "fhirbulkdata", "r4_AllergyIntolerance.ndjson");
        System.out.println("Size: " + size);

        ImportTransientUserData transientUserData = ImportTransientUserData.Builder.builder()
                .build();
        transientUserData.setImportFileSize(size);
        provider.registerTransient(transientUserData);
        provider.readFromObjectStoreWithLowMaxRange(client, "fhirbulkdata", "r4_AllergyIntolerance.ndjson");

        System.out.println(provider.getResources().size());
    }
}