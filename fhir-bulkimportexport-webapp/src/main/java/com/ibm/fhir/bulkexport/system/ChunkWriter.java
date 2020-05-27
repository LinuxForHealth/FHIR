/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkexport.system;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.model.CreateBucketRequest;
import com.ibm.fhir.bulkcommon.BulkDataUtils;
import com.ibm.fhir.bulkcommon.Constants;
import com.ibm.fhir.bulkexport.common.TransientUserData;
import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;

/**
 * Bulk export Chunk implementation - the Writer.
 *
 */
@Dependent
public class ChunkWriter extends AbstractItemWriter {
    private static final Logger logger = Logger.getLogger(ChunkWriter.class.getName());
    private AmazonS3 cosClient = null;
    private boolean isExportPublic = true;

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

        String itemName;
        if (cosBucketPathPrefix != null && cosBucketPathPrefix.trim().length() > 0) {
          itemName = cosBucketPathPrefix + "/" + fhirResourceType + "_" + chunkData.getUploadCount() + ".ndjson";

        } else {
          itemName = "job" + jobContext.getExecutionId() + "/" + fhirResourceType + "_" + chunkData.getUploadCount() + ".ndjson";
        }

        if (chunkData.getUploadId() == null) {
            chunkData.setUploadId(BulkDataUtils.startPartUpload(cosClient, cosBucketName, itemName, isExportPublic));
        }

        chunkData.getCosDataPacks().add(BulkDataUtils.multiPartUpload(cosClient, cosBucketName, itemName,
                chunkData.getUploadId(), in, dataLength, chunkData.getPartNum()));
        logger.info("pushFhirJsons2Cos: " + dataLength + " bytes were successfully appended to COS object - "
                + itemName);
        chunkData.setPartNum(chunkData.getPartNum() + 1);
        chunkData.getBufferStream().reset();

        if (chunkData.getPageNum() > chunkData.getLastPageNum() || chunkData.isFinishCurrentUpload()) {
            BulkDataUtils.finishMultiPartUpload(cosClient, cosBucketName, itemName, chunkData.getUploadId(),
                    chunkData.getCosDataPacks());
            // Partition status for the exported resources, e.g, Patient[1000,1000,200]
            if (chunkData.getResourceTypeSummary() == null) {
                chunkData.setResourceTypeSummary(fhirResourceType + "[" + chunkData.getCurrentUploadResourceNum());
                if (chunkData.getPageNum() > chunkData.getLastPageNum()) {
                    chunkData.setResourceTypeSummary(chunkData.getResourceTypeSummary() + "]");
                }
            } else {
                chunkData.setResourceTypeSummary(chunkData.getResourceTypeSummary() + "," + chunkData.getCurrentUploadResourceNum());
                if (chunkData.getPageNum() > chunkData.getLastPageNum()) {
                    chunkData.setResourceTypeSummary(chunkData.getResourceTypeSummary() + "]");
                    stepCtx.setTransientUserData(chunkData);
                }
            }

            if (chunkData.getPageNum() <= chunkData.getLastPageNum()) {
                chunkData.setPartNum(1);
                chunkData.setUploadId(null);
                chunkData.setCurrentUploadResourceNum(0);
                chunkData.setCurrentUploadSize(0);
                chunkData.setFinishCurrentUpload(false);
                chunkData.getCosDataPacks().clear();
                chunkData.setUploadCount(chunkData.getUploadCount() + 1);
            }
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

    @Override
    public void open(Serializable checkpoint) throws Exception  {
        isExportPublic = FHIRConfigHelper.getBooleanProperty(FHIRConfiguration.PROPERTY_BULKDATA_BATCHJOB_ISEXPORTPUBLIC, true);

    }
}
