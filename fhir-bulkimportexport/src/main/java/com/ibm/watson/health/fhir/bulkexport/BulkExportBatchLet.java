/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.bulkexport;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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

import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.model.CreateBucketRequest;
import com.ibm.cloud.objectstorage.services.s3.model.ObjectMetadata;
import com.ibm.cloud.objectstorage.services.s3.model.PartETag;
import com.ibm.cloud.objectstorage.services.s3.model.PutObjectRequest;
import com.ibm.waston.health.fhir.bulkcommon.COSUtils;
import com.ibm.waston.health.fhir.bulkcommon.Constants;
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
    // Please make sure pageSize is greater or equal to cosBatchSize.
    // cosBatchSize is used in batchlet job only.
    int pageSize = Constants.DEFAULT_SEARCH_PAGE_SIZE, cosBatchSize = Constants.DEFAULT_SEARCH_PAGE_SIZE;
    boolean isSingleCosObject = false;
    String uploadId;
    List<PartETag> cosDataPacks = new ArrayList<PartETag>();
    int partNum = 1;
    
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
     * The Cos object name.
     */
    @Inject
    @BatchProperty(name = "cos.bucket.objectname")
    String cosBucketObjectName;
    

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
        
        if (isSingleCosObject) {
            InputStream newStream = new ByteArrayInputStream(combinedJsons.getBytes(StandardCharsets.UTF_8));
            cosDataPacks.add(COSUtils.multiPartUpload(cosClient, cosBucketName, cosBucketObjectName, uploadId, newStream, combinedJsons.getBytes(StandardCharsets.UTF_8).length, partNum));
            partNum ++;
            log("pushFhirJsons2Cos", " Contents were successfully appended to COS object: " + cosBucketObjectName);
        } else {            
            ObjectMetadata metadata = new ObjectMetadata();
            combinedJsons = "[\r\n" + combinedJsons + "\r\n]";
            metadata.setContentLength(combinedJsons.getBytes(StandardCharsets.UTF_8).length);

            String itemName = cosBucketPathPrefix + "/" + UUID.randomUUID().toString();

            InputStream newStream = new ByteArrayInputStream(combinedJsons.getBytes(StandardCharsets.UTF_8));
            PutObjectRequest req = new PutObjectRequest(cosBucketName, itemName, newStream, metadata);
            cosClient.putObject(req);     
            log("pushFhirJsons2Cos", itemName + " was successfully written to COS");
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
        cosClient = COSUtils.getCosClient(cosCredentialIbm, cosApiKeyProperty, cosSrvinstId, cosEndpintUrl, cosLocation);

        if (cosClient == null) {
            log("process", "Failed to get CosClient!");
            return "Failed to get CosClient!";
        } else {
            log("process", "Succeed get CosClient!");
        }

        if (cosBucketName == null) {
            cosBucketName = Constants.DEFAULT_COS_BUCKETNAME;
        }

        cosBucketName = cosBucketName.toLowerCase();

        if (cosBucketPathPrefix == null) {
            cosBucketPathPrefix = UUID.randomUUID().toString();
        }
        
        if (cosBucketObjectName != null && cosBucketObjectName.trim().length() > 0) {
            isSingleCosObject = true;
        }

        COSUtils.listBuckets(cosClient);

        if (!cosClient.doesBucketExist(cosBucketName)) {
            CreateBucketRequest req = new CreateBucketRequest(cosBucketName);
            cosClient.createBucket(req);
        }

        if (fhirTenant == null) {
            fhirTenant = Constants.DEFAULT_FHIR_TENANT;
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

        searchContext.setPageSize(pageSize);
        
        String combinedJsons = null;
        
        if (isSingleCosObject) {
            combinedJsons = "[" + "\r\n";
            uploadId = COSUtils.startPartUpload(cosClient, cosBucketName, cosBucketObjectName);
        }

        while (!stopRequested) {
            searchContext.setPageNumber(pageNum);
            FHIRTransactionHelper txn = new FHIRTransactionHelper(fhirPersistence.getTransaction());
            txn.begin();
            persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(null, searchContext);
            List<Resource> resources = fhirPersistence.search(persistenceContext, resourceType);
            txn.commit();
              
            pageNum++;

            int count = 0;
            
            if (resources != null) {
                for (Resource res : resources) {
                    if (stopRequested) {
                        exitStatus = "Export stopped!" + " exported: " + exported;
                        break;
                    }
                    count++;
                    exported++;
                    String ndJsonLine = res.toString().replace("\r", "").replace("\n", "");
                    if (combinedJsons == null) {
                        combinedJsons = ndJsonLine;
                    } else {
                        if (combinedJsons.length() < 10) {
                            // if isSingleCosObject, then we don't need "," for the first record.
                            combinedJsons = combinedJsons + ndJsonLine;
                        } else {
                            combinedJsons = combinedJsons + "," + "\r\n" + ndJsonLine;
                        }
                    }

                    if (count == cosBatchSize) {
                        if (isSingleCosObject) {
                            if (pageNum <= searchContext.getLastPageNumber()) {
                             // There is more contents to come for the single Cos object, so add "," to the end.
                                combinedJsons = combinedJsons + ",";
                                // only push to COS if the combinedJsons is greater than the minimal COS part size.
                                if (combinedJsons.getBytes(StandardCharsets.UTF_8).length >= Constants.COS_PART_MINIMALSIZE) {
                                    pushFhirJsons2Cos(combinedJsons);
                                    count = 0;
                                    combinedJsons = null;
                                }
                            } else {
                             // There is no more contents to come for the single Cos object, so add "]" to the end and push to COS.
                                combinedJsons = combinedJsons + "\r\n]";
                                pushFhirJsons2Cos(combinedJsons);
                                count = 0;
                                combinedJsons = null;
                            }   
                        } else {
                            pushFhirJsons2Cos(combinedJsons);
                            count = 0;
                            combinedJsons = null;
                        }
                    }
                }

                if (combinedJsons != null) {
                    if (isSingleCosObject && combinedJsons.length() > 10) {
                        if (pageNum <= searchContext.getLastPageNumber() && !stopRequested) {
                         // There is more contents to come for the single Cos object, so add "," to the end.
                            combinedJsons = combinedJsons + ",";
                            // only push to COS if the combinedJsons is greater than the minimal COS part size.
                            if (combinedJsons.getBytes(StandardCharsets.UTF_8).length >= Constants.COS_PART_MINIMALSIZE) {
                                pushFhirJsons2Cos(combinedJsons);
                                count = 0;
                                combinedJsons = null;
                            }
                        } else {
                         // There is no more contents to come for the single Cos object, so add "]" to the end and push to COS.
                            combinedJsons = combinedJsons + "\r\n]";
                            pushFhirJsons2Cos(combinedJsons);
                            count = 0;
                            combinedJsons = null;
                        }   
                    } else {
                        pushFhirJsons2Cos(combinedJsons);
                        count = 0;
                        combinedJsons = null;
                    }
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
        
        // always finish the upload no matter if any real content has been uploaded.
        if (isSingleCosObject) {
            COSUtils.finishMultiPartUpload(cosClient, cosBucketName, cosBucketObjectName, uploadId, cosDataPacks);
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
