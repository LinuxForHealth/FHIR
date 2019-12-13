/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkexport;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
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
import com.ibm.fhir.bulkcommon.COSUtils;
import com.ibm.fhir.bulkcommon.Constants;

/**
 * Bulk export Chunk implementation - the Writer.
 *
 */
public class ChunkWriter extends AbstractItemWriter {
    private static final Logger logger = Logger.getLogger(ChunkWriter.class.getName());
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
     * @see javax.batch.api.chunk.AbstractItemWriter#AbstractItemWriter()
     */
    public ChunkWriter() {
        super();
    }

    private void pushFhirJsons2Cos(InputStream in, int dataLength) throws Exception {
        if (cosClient == null) {
            logger.warning("pushFhirJsons2Cos: no cosClient!");
            throw new Exception("pushFhirJsons2Cos: no cosClient!");
        }

        List<String> ResourceTypes = Arrays.asList(fhirResourceType.split("\\s*,\\s*"));

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
                jobContext.setExitStatus(cosBucketObjectName + "; " + ResourceTypes.get(chunkData.getIndexOfCurrentResourceType())
                    + "[" + chunkData.getCurrentPartResourceNum() + "]");
            }

        } else {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(dataLength);

            String itemName;
            if (cosBucketPathPrefix != null && cosBucketPathPrefix.trim().length() > 0) {
                itemName = cosBucketPathPrefix + "/" + ResourceTypes.get(chunkData.getIndexOfCurrentResourceType())
                            + "_" + chunkData.getPartNum() + ".ndjson";
            } else {
                itemName = "job" + jobContext.getExecutionId() + "/" + ResourceTypes.get(chunkData.getIndexOfCurrentResourceType())
                            + "_" + chunkData.getPartNum() + ".ndjson";
            }

            PutObjectRequest req = new PutObjectRequest(cosBucketName, itemName, in, metadata);
            cosClient.putObject(req);
            logger.info(
                    "pushFhirJsons2Cos: " + itemName + "(" + dataLength + " bytes) was successfully written to COS");
            // Job exit status, e.g, Patient[1000,1000,200]:Observation[1000,1000,200]
            if (jobContext.getExitStatus() == null) {
                jobContext.setExitStatus(ResourceTypes.get(chunkData.getIndexOfCurrentResourceType())
                        + "[" + chunkData.getCurrentPartResourceNum());
                if (chunkData.getPageNum() > chunkData.getLastPageNum()) {
                    jobContext.setExitStatus(jobContext.getExitStatus() + "]");
                }
            } else {
                if (chunkData.getPartNum() == 1) {
                    jobContext.setExitStatus(jobContext.getExitStatus() + ":"
                            + ResourceTypes.get(chunkData.getIndexOfCurrentResourceType()) + "["
                            + chunkData.getCurrentPartResourceNum());
                } else {
                    jobContext.setExitStatus(jobContext.getExitStatus() + "," + chunkData.getCurrentPartResourceNum());
                }
                if (chunkData.getPageNum() > chunkData.getLastPageNum()) {
                    jobContext.setExitStatus(jobContext.getExitStatus() + "]");
                }
            }
            chunkData.setPartNum(chunkData.getPartNum() + 1);
            chunkData.getBufferStream().reset();
            chunkData.setCurrentPartResourceNum(0);
        }
    }

    /**
     * @see {@link javax.batch.api.chunk.AbstractItemWriter#writeItems(List)}
     */
    @Override
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
