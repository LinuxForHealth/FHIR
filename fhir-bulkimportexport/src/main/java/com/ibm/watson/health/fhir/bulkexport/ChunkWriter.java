/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.bulkexport;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;

import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.model.CreateBucketRequest;
import com.ibm.cloud.objectstorage.services.s3.model.ObjectMetadata;
import com.ibm.cloud.objectstorage.services.s3.model.PutObjectRequest;
import com.ibm.waston.health.fhir.bulkcommon.COSUtils;
import com.ibm.waston.health.fhir.bulkcommon.Constants;

/**
 * Bulk export Chunk implementation - the Writer.
 * 
 * @author Albert Wang
 */
public class ChunkWriter extends AbstractItemWriter {
    private final static Logger logger = Logger.getLogger(ChunkWriter.class.getName());
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
     * The Cos object name.
     */
    @Inject
    @BatchProperty(name = "cos.bucket.objectname")
    String cosBucketObjectName;
    
    /**
     * Fhir ResourceType.
     */
    @Inject
    @BatchProperty(name = "fhir.resourcetype")
    String fhirResourceType;
    
    /**
     * The cos.pagesperobject.
     */
    @Inject
    @BatchProperty(name = "cos.pagesperobject")
    String pagesPerCosObject;
    
    @Inject
    JobContext jobContext;

    /**
     * Logging helper.
     */
    private void log(String method, Object msg) {
        logger.info(method + ": " + String.valueOf(msg));
    }

    /**
     * @see AbstractItemWriter#AbstractItemWriter()
     */
    public ChunkWriter() {
        super();
    }

    private void pushFhirJsons2Cos(String combinedJsons) throws Exception {

        if (cosClient == null) {
            return;
        }
        
        TransientUserData chunkData = (TransientUserData)jobContext.getTransientUserData();
        if (chunkData != null && chunkData.isSingleCosObject()) {
            if (chunkData.getUploadId() == null) {
                chunkData.setUploadId(COSUtils.startPartUpload(cosClient, cosBucketName, cosBucketObjectName));
            }

            InputStream newStream = new ByteArrayInputStream(combinedJsons.getBytes(StandardCharsets.UTF_8));
            chunkData.getCosDataPacks().add(COSUtils.multiPartUpload(cosClient, cosBucketName, cosBucketObjectName, 
                    chunkData.getUploadId(), newStream, combinedJsons.getBytes(StandardCharsets.UTF_8).length, chunkData.getPartNum()));
            chunkData.setPartNum(chunkData.getPartNum()+1);
            chunkData.setCurrentPartSize(0);
            
            if (chunkData.getPageNum() > chunkData.getLastPageNum()) {
                COSUtils.finishMultiPartUpload(cosClient, cosBucketName, cosBucketObjectName, chunkData.getUploadId(), chunkData.getCosDataPacks());
            }
            
        } else {
            int numofPagePerCosObject;
            try {
                numofPagePerCosObject = Integer.parseInt(pagesPerCosObject);
            } catch (Exception e) {
                numofPagePerCosObject = Constants.DEFAULT_NUMOFPAGES_EACH_COS_OBJECT;
            }
            InputStream newStream = new ByteArrayInputStream(combinedJsons.getBytes(StandardCharsets.UTF_8));
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(combinedJsons.getBytes(StandardCharsets.UTF_8).length);
            
            String itemName;
            if (chunkData != null) {
                    itemName = "job" + jobContext.getExecutionId() + "/" + fhirResourceType 
                            + "_" + (int) Math.ceil((chunkData.getPageNum()-1)/numofPagePerCosObject) + ".ndjson";
            } else {
                itemName = cosBucketPathPrefix + "/" + UUID.randomUUID().toString();
            }

            PutObjectRequest req = new PutObjectRequest(cosBucketName, itemName, newStream, metadata);
            cosClient.putObject(req);

            log("pushFhirJsons2Cos", itemName + " was successfully written to COS");            
        }
        
    }

    /**
     * @see AbstractItemWriter#writeItems(List<java.lang.Object>)
     */
    @SuppressWarnings("unchecked")
    public void writeItems(List<java.lang.Object> arg0) throws Exception {
        cosClient = COSUtils.getCosClient(cosCredentialIbm, cosApiKeyProperty, cosSrvinstId, cosEndpintUrl, cosLocation);

        if (cosClient == null) {
            log("writeItems", "Failed to get CosClient!");
            return;
        } else {
            log("writeItems", "Succeed get CosClient!");
        }

        if (cosBucketName == null) {
            cosBucketName = Constants.DEFAULT_COS_BUCKETNAME;
        }

        cosBucketName = cosBucketName.toLowerCase();

        if (cosBucketPathPrefix == null) {
            cosBucketPathPrefix = UUID.randomUUID().toString();
        }

        if (!cosClient.doesBucketExist(cosBucketName)) {
            CreateBucketRequest req = new CreateBucketRequest(cosBucketName);
            cosClient.createBucket(req);
        }
        
        String combinedJsons = null;
        for (Object resListObject : arg0) {
            List<String> resList = ((List<String>) resListObject);
            for (String resJsons : resList) {
                if (combinedJsons == null) {
                    combinedJsons = resJsons;
                } else {
                    combinedJsons = combinedJsons + "," + "\r\n" + resJsons; 
                }
            }
        }
        if (combinedJsons!= null && combinedJsons.length() > 0 ) {
            pushFhirJsons2Cos(combinedJsons);
        }

    }
}
