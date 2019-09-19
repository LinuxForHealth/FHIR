/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.bulkimport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemReader;
import javax.inject.Inject;

import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request;
import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Result;
import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectSummary;
import com.ibm.waston.health.fhir.bulkcommon.COSUtils;
import com.ibm.waston.health.fhir.bulkcommon.Constants;

/**
 * Bulk import Chunk implementation - the Reader.
 * 
 * @author Albert Wang
 */
public class ChunkReader extends AbstractItemReader {
    private final static Logger logger = Logger.getLogger(ChunkReader.class.getName());
    private AmazonS3 cosClient = null;

    int pageNum = 0;
    String nextToken = "";

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
    @BatchProperty(name = "cos.read.maxobjects")
    String maxCOSObjectsPerRead;

    /**
     * The Cos object name.
     */
    @Inject
    @BatchProperty(name = "cos.bucket.objectname")
    String cosBucketObjectName;

    /**
     * If use IBM credential.
     */
    @Inject
    @BatchProperty(name = "cos.credential.ibm")
    String cosCredentialIbm;

    /**
     * @see AbstractItemReader#AbstractItemReader()
     */
    public ChunkReader() {
        super();
    }

    /**
     * @throws Exception
     * @see AbstractItemReader#readItem()
     */
    public Object readItem() throws Exception {
        List<String> resCosObjectNameList = new ArrayList<String>();
        cosClient = COSUtils.getCosClient(cosCredentialIbm, cosApiKeyProperty, cosSrvinstId, cosEndpintUrl,
                cosLocation);

        if (cosClient == null) {
            logger.warning("readItem: Failed to get CosClient!");
            return null;
        } else {
            logger.finer("readItem: Succeed get CosClient!");
        }
        if (cosBucketName == null) {
            cosBucketName = "fhir-bulkImExport-Connectathon";
        }
        cosBucketName = cosBucketName.toLowerCase();
        if (!cosClient.doesBucketExist(cosBucketName)) {
            logger.warning("readItem: Bucket '" + cosBucketName + "' not found!");
            COSUtils.listBuckets(cosClient);
            return null;
        }
        // Control the number of COS objects to read in each "item".
        int maxKeys = Constants.DEFAULT_NUMOFOBJECTS_PERREAD;
        if (maxCOSObjectsPerRead != null) {
            try {
                maxKeys = Integer.parseInt(maxCOSObjectsPerRead);
                logger.info("readItem: Number of COS Objects in each read - " + maxKeys + ".");
            } catch (Exception e) {
                logger.warning("readItem: Set Number of COS Objects in each read to default("
                        + Constants.DEFAULT_NUMOFOBJECTS_PERREAD + ").");
            }
        }

        if (nextToken.contentEquals("ALLDONE")) {
            return null;
        }

        ListObjectsV2Request request = new ListObjectsV2Request().withBucketName(cosBucketName).withMaxKeys(maxKeys)
                .withContinuationToken(nextToken);
        ListObjectsV2Result result = cosClient.listObjectsV2(request);
        for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
            boolean isToBeProccessed = false;
            if (cosBucketObjectName != null && cosBucketObjectName.trim().length() > 0) {
                if (objectSummary.getKey().startsWith(cosBucketObjectName.trim())) {
                    isToBeProccessed = true;
                }
            } else {
                isToBeProccessed = true;
            }
            if (isToBeProccessed) {
                logger.info("readItem: COS Object(" + objectSummary.getKey() + ") - " + objectSummary.getSize()
                        + " bytes.");
                if (objectSummary.getSize() > 0) {
                    resCosObjectNameList.add(objectSummary.getKey());
                }
            }
        }

        if (result.isTruncated()) {
            nextToken = result.getNextContinuationToken();
        } else {
            // Use this special token to sign the end of the import.
            nextToken = "ALLDONE";
        }

        return resCosObjectNameList;
    }

    @Override
    public void open(Serializable checkpoint) throws Exception {
        if (checkpoint != null) {
            nextToken = (String) checkpoint;
        }
    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return nextToken;
    }

}
