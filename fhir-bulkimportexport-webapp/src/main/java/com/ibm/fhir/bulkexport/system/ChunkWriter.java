/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkexport.system;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;

import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.model.CannedAccessControlList;
import com.ibm.cloud.objectstorage.services.s3.model.CreateBucketRequest;
import com.ibm.cloud.objectstorage.services.s3.model.ObjectMetadata;
import com.ibm.cloud.objectstorage.services.s3.model.PutObjectRequest;
import com.ibm.fhir.bulkcommon.BulkDataUtils;
import com.ibm.fhir.bulkcommon.Constants;
import com.ibm.fhir.bulkexport.common.TransientUserData;
import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;

/**
 * Bulk export Chunk implementation - the Writer.
 *
 */
public class ChunkWriter extends AbstractItemWriter {
    private static final Logger logger = Logger.getLogger(ChunkWriter.class.getName());
    private AmazonS3 cosClient = null;
    private final boolean isExportPublic = FHIRConfigHelper.getBooleanProperty(FHIRConfiguration.PROPERTY_BULKDATA_BATCHJOB_ISEXPORTPUBLIC, true);

    /**
     * The IBM COS API key or S3 access key.
     */
    @Inject
    @BatchProperty(name = Constants.COS_API_KEY)
    String cosApiKeyProperty;

    /**
     * The IBM COS service instance id or s3 secret key.
     */
    @Inject
    @BatchProperty(name = Constants.COS_SRVINST_ID)
    String cosSrvinstId;

    /**
     * The Cos End point URL.
     */
    @Inject
    @BatchProperty(name = Constants.COS_ENDPOINT_URL)
    String cosEndpointUrl;

    /**
     * The Cos End point location.
     */
    @Inject
    @BatchProperty(name = Constants.COS_LOCATION)
    String cosLocation;

    /**
     * The Cos bucket name.
     */
    @Inject
    @BatchProperty(name = Constants.COS_BUCKET_NAME)
    String cosBucketName;

    /**
     * The Cos bucket path prefix.
     */
    @Inject
    @BatchProperty(name = Constants.EXPORT_COS_OBJECT_PATHPREFIX)
    String cosBucketPathPrefix;

    /**
     * If use IBM credential or Amazon secret keys.
     */
    @Inject
    @BatchProperty(name = Constants.COS_IS_IBM_CREDENTIAL)
    String cosCredentialIbm;

    /**
     * Fhir resource type to process.
     */
    @Inject
    @BatchProperty(name = Constants.PARTITION_RESOURCE_TYPE)
    String fhirResourceType;

    /**
     * The Cos object name(only used by system export for exporting single resource type)
     */
    @Inject
    @BatchProperty(name = Constants.EXPORT_COS_OBJECTNAME)
    String cosBucketObjectName;

    @Inject
    StepContext stepCtx;

    @Inject
    JobContext jobContext;

    /**
     * @see javax.batch.api.chunk.AbstractItemWriter#AbstractItemWriter()
     */
    public ChunkWriter() {
        super();
    }

    private void pushFhirJsonsToCos(InputStream in, int dataLength) throws Exception {
        if (cosClient == null) {
            logger.warning("pushFhirJsons2Cos: no cosClient!");
            throw new Exception("pushFhirJsons2Cos: no cosClient!");
        }

        TransientUserData chunkData = (TransientUserData) stepCtx.getTransientUserData();
        if (chunkData == null) {
            logger.warning("pushFhirJsons2Cos: chunkData is null, this should never happen!");
            throw new Exception("pushFhirJsons2Cos: chunkData is null, this should never happen!");
        }
        if (chunkData.isSingleCosObject()) {
            if (chunkData.getUploadId() == null) {
                chunkData.setUploadId(BulkDataUtils.startPartUpload(cosClient, cosBucketName, cosBucketObjectName, false));
            }

            chunkData.getCosDataPacks().add(BulkDataUtils.multiPartUpload(cosClient, cosBucketName, cosBucketObjectName,
                    chunkData.getUploadId(), in, dataLength, chunkData.getPartNum()));
            logger.info("pushFhirJsons2Cos: " + dataLength + " bytes were successfully appended to COS object - "
                    + cosBucketObjectName);
            chunkData.setPartNum(chunkData.getPartNum() + 1);
            chunkData.getBufferStream().reset();

            if (chunkData.getPageNum() > chunkData.getLastPageNum()) {
                BulkDataUtils.finishMultiPartUpload(cosClient, cosBucketName, cosBucketObjectName, chunkData.getUploadId(),
                        chunkData.getCosDataPacks());
                stepCtx.setExitStatus(cosBucketObjectName + "; " + fhirResourceType
                    + "[" + chunkData.getCurrentPartResourceNum() + "]");
            }

        } else {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(dataLength);

            String itemName;
            PutObjectRequest req;
            if (cosBucketPathPrefix != null && cosBucketPathPrefix.trim().length() > 0) {
                itemName = cosBucketPathPrefix + "/" + fhirResourceType + "_" + chunkData.getPartNum() + ".ndjson";
                if (isExportPublic) {
                    // Set expiration time to 2 hours(7200 seconds).
                    // Note: IBM COS doesn't honor this but also doesn't fail on this.
                    metadata.setExpirationTime(Date.from(Instant.now().plusSeconds(7200)));
                }

                req = new PutObjectRequest(cosBucketName, itemName, in, metadata);

                if (isExportPublic) {
                    // Give public read only access.
                    req.setCannedAcl(CannedAccessControlList.PublicRead);
                }

            } else {
                itemName = "job" + jobContext.getExecutionId() + "/" + fhirResourceType + "_" + chunkData.getPartNum() + ".ndjson";
                req = new PutObjectRequest(cosBucketName, itemName, in, metadata);
            }

            cosClient.putObject(req);
            logger.info(
                    "pushFhirJsons2Cos: " + itemName + "(" + dataLength + " bytes) was successfully written to COS");
            // Job exit status, e.g, Patient[1000,1000,200]:Observation[1000,1000,200]
            if (chunkData.getResourceTypeSummary() == null) {
                chunkData.setResourceTypeSummary(chunkData.getResourceTypeSummary() + fhirResourceType + "[" + chunkData.getCurrentPartResourceNum());
                if (chunkData.getPageNum() > chunkData.getLastPageNum()) {
                    chunkData.setResourceTypeSummary(chunkData.getResourceTypeSummary() + "]");
                }
            } else {
                if (chunkData.getPartNum() == 1) {
                    chunkData.setResourceTypeSummary(chunkData.getResourceTypeSummary() + ":" + fhirResourceType + "[" + chunkData.getCurrentPartResourceNum());
                } else {
                    chunkData.setResourceTypeSummary(chunkData.getResourceTypeSummary() + "," + chunkData.getCurrentPartResourceNum());
                }
                if (chunkData.getPageNum() > chunkData.getLastPageNum()) {
                    chunkData.setResourceTypeSummary(chunkData.getResourceTypeSummary() + "]");
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
        if (cosClient == null) {
            cosClient = BulkDataUtils.getCosClient(cosCredentialIbm, cosApiKeyProperty, cosSrvinstId, cosEndpointUrl, cosLocation);
            if (cosClient == null) {
                logger.warning("writeItems: Failed to get CosClient!");
                throw new Exception("writeItems: Failed to get CosClient!!");
            } else {
                logger.finer("writeItems: Got CosClient successfully!");
            }
        }

        if (cosBucketName == null) {
            cosBucketName = Constants.DEFAULT_COS_BUCKETNAME;
        }
        cosBucketName = cosBucketName.toLowerCase();
        if (!cosClient.doesBucketExistV2(cosBucketName)) {
            CreateBucketRequest req = new CreateBucketRequest(cosBucketName);
            cosClient.createBucket(req);
        }

        TransientUserData chunkData = (TransientUserData) stepCtx.getTransientUserData();
        if (chunkData == null) {
            logger.warning("writeItems: chunkData is null, this should never happen!");
            throw new Exception("writeItems: chunkData is null, this should never happen!");
        } else {
            if (chunkData.getBufferStream().size() > 0) {
                pushFhirJsonsToCos(new ByteArrayInputStream(chunkData.getBufferStream().toByteArray()),
                        chunkData.getBufferStream().size());
            }
        }
    }
}
