/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.bulkexport;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
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

    @Inject
    JobContext jobContext;

    /**
     * @see AbstractItemWriter#AbstractItemWriter()
     */
    public ChunkWriter() {
        super();
    }

    private void pushFhirJsons2Cos(InputStream in, int dataLength) throws Exception {
        if (cosClient == null) {
            logger.warning("pushFhirJsons2Cos: no cosClient!");
            throw new Exception("pushFhirJsons2Cos: no cosClient!");
        }

        TransientUserData chunkData = (TransientUserData) jobContext.getTransientUserData();
        if (chunkData == null) {
            logger.warning("pushFhirJsons2Cos: chunkData is null, this should never happen!");
            throw new Exception("pushFhirJsons2Cos: chunkData is null, this should never happen!");
        }
        if (chunkData.isSingleCosObject()) {
            if (chunkData.getUploadId() == null) {
                chunkData.setUploadId(COSUtils.startPartUpload(cosClient, cosBucketName, cosBucketObjectName));
            }

            chunkData.getCosDataPacks().add(COSUtils.multiPartUpload(cosClient, cosBucketName, cosBucketObjectName,
                    chunkData.getUploadId(), in, dataLength, chunkData.getPartNum()));
            logger.info("pushFhirJsons2Cos: " + dataLength + " bytes were successfully appended to COS object - "
                    + cosBucketObjectName);
            chunkData.setPartNum(chunkData.getPartNum() + 1);
            chunkData.getBufferStream().reset();

            if (chunkData.getPageNum() > chunkData.getLastPageNum()) {
                COSUtils.finishMultiPartUpload(cosClient, cosBucketName, cosBucketObjectName, chunkData.getUploadId(),
                        chunkData.getCosDataPacks());
                jobContext.setExitStatus("Export was finished; " + cosBucketObjectName);
            }

        } else {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(dataLength);

            String itemName;
            if (cosBucketPathPrefix != null && cosBucketPathPrefix.trim().length() > 0) {
                itemName = cosBucketPathPrefix + "/" + fhirResourceType + "_" + chunkData.getPartNum() + ".ndjson";
            } else {
                itemName = "job" + jobContext.getExecutionId() + "/" + fhirResourceType + "_" + chunkData.getPartNum()
                        + ".ndjson";
            }

            PutObjectRequest req = new PutObjectRequest(cosBucketName, itemName, in, metadata);
            cosClient.putObject(req);
            chunkData.setPartNum(chunkData.getPartNum() + 1);
            chunkData.getBufferStream().reset();
            logger.info(
                    "pushFhirJsons2Cos: " + itemName + "(" + dataLength + " bytes) was successfully written to COS");
            if (jobContext.getExitStatus() == null) {
                jobContext.setExitStatus("Export was finished; " + itemName);
            } else {
                jobContext.setExitStatus(jobContext.getExitStatus() + " : " + itemName);
            }
        }

    }

    /**
     * @see AbstractItemWriter#writeItems(List<java.lang.Object>)
     */
    public void writeItems(List<java.lang.Object> arg0) throws Exception {
        cosClient = COSUtils.getCosClient(cosCredentialIbm, cosApiKeyProperty, cosSrvinstId, cosEndpintUrl,
                cosLocation);

        if (cosClient == null) {
            logger.warning("writeItems: Failed to get CosClient!");
            throw new Exception("writeItems: Failed to get CosClient!!");
        } else {
            logger.finer("writeItems: Got CosClient successfully!");
        }
        if (cosBucketName == null) {
            cosBucketName = Constants.DEFAULT_COS_BUCKETNAME;
        }
        cosBucketName = cosBucketName.toLowerCase();
        if (!cosClient.doesBucketExist(cosBucketName)) {
            CreateBucketRequest req = new CreateBucketRequest(cosBucketName);
            cosClient.createBucket(req);
        }

        TransientUserData chunkData = (TransientUserData) jobContext.getTransientUserData();
        if (chunkData == null) {
            logger.warning("writeItems: chunkData is null, this should never happen!");
            throw new Exception("writeItems: chunkData is null, this should never happen!");
        } else {
            if (chunkData.getBufferStream().size() > 0) {
                pushFhirJsons2Cos(new ByteArrayInputStream(chunkData.getBufferStream().toByteArray()),
                        chunkData.getBufferStream().size());
            }
        }
    }
}
