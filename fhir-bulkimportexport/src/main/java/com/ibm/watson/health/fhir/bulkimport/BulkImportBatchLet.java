/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.bulkimport;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.Batchlet;
import javax.inject.Inject;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ibm.cloud.objectstorage.ClientConfiguration;
import com.ibm.cloud.objectstorage.SDKGlobalConfiguration;
import com.ibm.cloud.objectstorage.auth.AWSCredentials;
import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
import com.ibm.cloud.objectstorage.auth.BasicAWSCredentials;
import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
import com.ibm.cloud.objectstorage.services.s3.model.Bucket;
import com.ibm.cloud.objectstorage.services.s3.model.GetObjectRequest;
import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request;
import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Result;
import com.ibm.cloud.objectstorage.services.s3.model.S3Object;
import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectSummary;
import com.ibm.watson.health.fhir.config.FHIRConfiguration;
import com.ibm.watson.health.fhir.config.FHIRRequestContext;
import com.ibm.watson.health.fhir.model.format.Format;
import com.ibm.watson.health.fhir.model.parser.FHIRParser;
import com.ibm.watson.health.fhir.model.resource.Resource;
import com.ibm.watson.health.fhir.persistence.FHIRPersistence;
import com.ibm.watson.health.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.watson.health.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.watson.health.fhir.persistence.helper.FHIRPersistenceHelper;
import com.ibm.watson.health.fhir.persistence.helper.FHIRTransactionHelper;

/**
 * Bulk import Batchlet implementation.
 * 
 * @author Albert Wang
 */
public class BulkImportBatchLet implements Batchlet {
    private AmazonS3 cosClient = null;

    /**
     * The IBM COS API key or S3 access key.
     */
    @Inject
    @BatchProperty(name = "cos.api.key")
    String cosApiKeyProperty;

    /**
     * The IBM COS service instance id or s3 secret key.
     */
    @Inject
    @BatchProperty(name = "cos.srvinst.id")
    String cosSrvinstId;

    /**
     * The Cos End point URL.
     */
    @Inject
    @BatchProperty(name = "cos.endpointurl")
    String cosEndpintUrl;

    /**
     * The Cos End point URL.
     */
    @Inject
    @BatchProperty(name = "cos.location")
    String cosLocation;

    /**
     * The Cos bucket name.
     */
    @Inject
    @BatchProperty(name = "cos.bucket.name")
    String cosBucketName;

    /**
     * If use IBM credential.
     */
    @Inject
    @BatchProperty(name = "cos.credential.ibm")
    String cosCredentialIbm;

    /**
     * Fhir tenant id.
     */
    @Inject
    @BatchProperty(name = "fhir.tenant")
    String fhirTenant;

    private void getCosClient() {
        SDKGlobalConfiguration.IAM_ENDPOINT = "https://iam.cloud.ibm.com/oidc/token";
        AWSCredentials credentials;
        if (cosCredentialIbm.equalsIgnoreCase("Y")) {
            credentials = new BasicIBMOAuthCredentials(cosApiKeyProperty, cosSrvinstId);
        } else {
            credentials = new BasicAWSCredentials(cosApiKeyProperty, cosSrvinstId);
        }

        ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(8000);
        clientConfig.setUseTcpKeepAlive(true);

        cosClient = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(new EndpointConfiguration(cosEndpintUrl, cosLocation))
                .withPathStyleAccessEnabled(true).withClientConfiguration(clientConfig).build();
    }

    /**
     * Default constructor.
     */
    public BulkImportBatchLet() {

    }

    private final static Logger logger = Logger.getLogger(BulkImportBatchLet.class.getName());

    /**
     * Logging helper.
     */
    private void log(String method, Object msg) {
        logger.info(method + ": " + String.valueOf(msg));
    }

    /**
     * This flag gets set if the batchlet is stopped. This will stop the import.
     */
    private boolean stopRequested = false;

    /**
     * @param cosClient
     */
    public void listBuckets() {
        if (cosClient == null)
            return;

        log("listBuckets", "Buckets:");
        final List<Bucket> bucketList = cosClient.listBuckets();

        for (final Bucket bucket : bucketList) {
            log("listBuckets", bucket.getName());
        }
    }

    private String getItem(String bucketName, String itemName) {
        S3Object item = cosClient.getObject(new GetObjectRequest(bucketName, itemName));

        try (InputStreamReader in = new InputStreamReader(item.getObjectContent())) {
            final int bufferSize = 1024;
            final char[] buffer = new char[bufferSize];
            final StringBuilder out = new StringBuilder();

            for (;;) {
                int rsz = in.read(buffer, 0, buffer.length);
                if (rsz < 0)
                    break;
                out.append(buffer, 0, rsz);
            }
            logger.log(Level.FINER, out.toString());
            return out.toString();
        } catch (IOException ioe) {
            log("getItem", "Error reading file " + itemName);
            return null;
        }
    }

    /**
     * Main entry point.
     */
    @Override
    public String process() throws Exception {
        String exitStatus = "Import Stopped before it's started!";
        log("process", "Begin get CosClient!");
        getCosClient();

        if (cosClient == null) {
            log("process", "Failed to get CosClient!");
            return "Failed to get CosClient!";
        } else {
            log("process", "Succeed get CosClient!");
        }

        if (cosBucketName == null) {
            cosBucketName = "fhir-bulkImExport-Connectathon";
        }

        cosBucketName = cosBucketName.toLowerCase();

        listBuckets();

        if (!cosClient.doesBucketExist(cosBucketName)) {
            log("process", "Bucket not found!");
            return "Bucket not found!";
        }

        if (fhirTenant == null) {
            fhirTenant = "default";
            log("process", "Set tenant to default!");
        }
        FHIRConfiguration.setConfigHome("./");
        FHIRRequestContext.set(new FHIRRequestContext(fhirTenant, fhirTenant));

        FHIRPersistenceHelper fhirPersistenceHelper = new FHIRPersistenceHelper();
        FHIRPersistence fhirPersistence = fhirPersistenceHelper.getFHIRPersistenceImplementation();
        FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(null);

        boolean moreResults = true;
        String nextToken = "";
        int maxKeys = 100, imported = 0;

        while (moreResults && !stopRequested) {
            ListObjectsV2Request request = new ListObjectsV2Request().withBucketName(cosBucketName).withMaxKeys(maxKeys)
                    .withContinuationToken(nextToken);

            ListObjectsV2Result result = cosClient.listObjectsV2(request);
            for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
                log("process", "Item: " + objectSummary.getKey() + ", Bytes: " + objectSummary.getSize());

                String objectContents = getItem(cosBucketName, objectSummary.getKey());

                if (objectContents != null) {
                    JSONArray jsonJArray = new JSONArray(objectContents);

                    for (int i = 0; i < jsonJArray.length(); i++) {
                        JSONObject json_data = jsonJArray.getJSONObject(i);
                        Resource fhirRes = FHIRParser.parser(Format.JSON).parse(new StringReader(json_data.toString()));
                        FHIRTransactionHelper txn = new FHIRTransactionHelper(fhirPersistence.getTransaction());
                        txn.begin();
                        fhirPersistence.update(persistenceContext, fhirRes.getId().getValue(), fhirRes);
                        txn.commit();
                        imported++;
                    }
                }
            }

            if (result.isTruncated()) {
                nextToken = result.getNextContinuationToken();
            } else {
                nextToken = "";
                moreResults = false;
            }
        }

        if (imported > 0) {
            exitStatus = "Import completed: " + imported;
        }

        log("process", "ExitStatus: " + exitStatus);

        return exitStatus;
    }

    /**
     * Called if the batchlet is stopped by the container.
     */
    @Override
    public void stop() throws Exception {
        log("stop:", "Stop request accepted!");
        stopRequested = true;
    }
}
