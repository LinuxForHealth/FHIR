/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.bulkexport;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.Batchlet;
import javax.inject.Inject;

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
import com.ibm.cloud.objectstorage.services.s3.model.CreateBucketRequest;
import com.ibm.cloud.objectstorage.services.s3.model.ObjectMetadata;
import com.ibm.cloud.objectstorage.services.s3.model.PutObjectRequest;
import com.ibm.watson.health.fhir.config.FHIRConfiguration;
import com.ibm.watson.health.fhir.config.FHIRRequestContext;
import com.ibm.watson.health.fhir.model.resource.Resource;
import com.ibm.watson.health.fhir.model.util.ModelSupport;
import com.ibm.watson.health.fhir.persistence.FHIRPersistence;
import com.ibm.watson.health.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.watson.health.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.watson.health.fhir.persistence.helper.FHIRPersistenceHelper;
import com.ibm.watson.health.fhir.persistence.helper.FHIRTransactionHelper;
import com.ibm.watson.health.fhir.search.context.FHIRSearchContext;
import com.ibm.watson.health.fhir.search.util.SearchUtil;

/**
 * Bulk export Batchlet implementation.
 * 
 * @author Albert Wang
 */
public class BulkExportBatchLet implements Batchlet {
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
     * The Cos bucket name.
     */
    @Inject
    @BatchProperty(name = "cos.bucket.pathprefix")
    String cosBucketPathPrefix;

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

    /**
     * Fhir ResourceType.
     */
    @Inject
    @BatchProperty(name = "fhir.resourcetype")
    String fhirResourceType;

    /**
     * Fhir Search from date.
     */
    @Inject
    @BatchProperty(name = "fhir.search.fromdate")
    String fhirSearchFromDate;

    /**
     * Fhir search to date.
     */
    @Inject
    @BatchProperty(name = "fhir.search.todate")
    String fhirSearchToDate;

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
    public BulkExportBatchLet() {

    }

    private final static Logger logger = Logger.getLogger(BulkExportBatchLet.class.getName());

    /**
     * Logging helper.
     */
    private void log(String method, Object msg) {
        logger.info(method + ": " + String.valueOf(msg));
    }

    /**
     * This flag gets set if the batchlet is stopped. This will stop the export.
     */
    private boolean stopRequested = false;

    private void pushFhirJsons2Cos(String combinedJsons) throws Exception {

        if (cosClient == null)
            return;

        InputStream newStream = new ByteArrayInputStream(combinedJsons.getBytes(StandardCharsets.UTF_8));
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(combinedJsons.getBytes(StandardCharsets.UTF_8).length);

        String itemName = cosBucketPathPrefix + "/" + UUID.randomUUID().toString();

        PutObjectRequest req = new PutObjectRequest(cosBucketName, itemName, newStream, metadata);
        cosClient.putObject(req);

        log("pushFhirJsons2Cos", itemName + " was successfully written to COS");

    }

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

    /**
     * Main entry point.
     */
    @SuppressWarnings("unchecked")
    @Override
    public String process() throws Exception {
        String exitStatus = "Export Stopped before it's started!";
        log("process", "Begin get CosClient!");
        getCosClient();

        if (cosClient == null) {
            log("BulkImportBatchLet", "Failed to get CosClient!");
            return "Failed to get CosClient!";
        } else {
            log("process", "Succeed get CosClient!");
        }

        if (cosBucketName == null) {
            cosBucketName = "fhir-bulkImExport-Connectathon";
        }

        cosBucketName = cosBucketName.toLowerCase();

        if (cosBucketPathPrefix == null) {
            cosBucketPathPrefix = UUID.randomUUID().toString();
        }

        listBuckets();

        if (!cosClient.doesBucketExist(cosBucketName)) {
            CreateBucketRequest req = new CreateBucketRequest(cosBucketName);
            cosClient.createBucket(req);
        }

        if (fhirTenant == null) {
            fhirTenant = "default";
            log("process", "Set tenant to default!");
        }
        FHIRConfiguration.setConfigHome("./");
        FHIRRequestContext.set(new FHIRRequestContext(fhirTenant, fhirTenant));

        FHIRPersistenceHelper fhirPersistenceHelper = new FHIRPersistenceHelper();
        FHIRPersistence fhirPersistence = fhirPersistenceHelper.getFHIRPersistenceImplementation();

        Class<? extends Resource> resourceType = (Class<? extends Resource>) ModelSupport
                .getResourceType(fhirResourceType);
        FHIRSearchContext searchContext;
        FHIRPersistenceContext persistenceContext;
        Map<String, List<String>> queryParameters = new HashMap<>();
        String queryString;

        queryString = "&_lastUpdated=ge" + fhirSearchFromDate + "&_lastUpdated=lt" + fhirSearchToDate
                + "&_sort=_lastUpdated";
        queryParameters.put("_lastUpdated", Collections.singletonList("ge" + fhirSearchFromDate));
        queryParameters.put("_lastUpdated", Collections.singletonList("lt" + fhirSearchToDate));
        queryParameters.put("_sort", Arrays.asList(new String[] { "_lastUpdated" }));

        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);

        int pageNum = 1, exported = 0;
        int pageSize = 100, cosBatchSize = 20;

        searchContext.setPageSize(pageSize);

        while (!stopRequested) {
            searchContext.setPageNumber(pageNum);
            FHIRTransactionHelper txn = new FHIRTransactionHelper(fhirPersistence.getTransaction());
            txn.begin();
            persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(null, searchContext);
            List<Resource> resources = fhirPersistence.search(persistenceContext, resourceType);
            txn.commit();
              
            pageNum++;

            int count = 0;
            String combinedJsons = null;
            if (resources != null) {
                for (Resource res : resources) {
                    if (stopRequested) {
                        exitStatus = "Export stopped!" + " exported: " + exported;
                        break;
                    }
                    count++;
                    exported++;
                    if (combinedJsons == null) {
                        combinedJsons = "[" + res.toString();
                    } else {
                        combinedJsons = combinedJsons + "," + res.toString();
                    }

                    if (count == cosBatchSize) {
                        combinedJsons = combinedJsons + "]";
                        pushFhirJsons2Cos(combinedJsons);
                        count = 0;
                        combinedJsons = null;

                    }
                }

                if (combinedJsons != null) {
                    combinedJsons = combinedJsons + "]";
                    pushFhirJsons2Cos(combinedJsons);
                }


                // No more to export, so stop.
                if (pageNum > searchContext.getLastPageNumber()) {
                    stopRequested = true;
                    exitStatus = "Export finished! exported: " + exported;
                }

            } else {
                if (exported > 0) {
                    exitStatus = "Export finished! exported: " + exported;
                } else {
                    exitStatus = "Nothing exported!";
                }
                stopRequested = true;
            }

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
