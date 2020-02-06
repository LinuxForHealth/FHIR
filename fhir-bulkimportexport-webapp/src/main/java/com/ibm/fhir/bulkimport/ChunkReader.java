/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkimport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemReader;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;

import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.fhir.bulkcommon.COSUtils;
import com.ibm.fhir.model.resource.Resource;

/**
 * Bulk import Chunk implementation - the Reader.
 *
 */
public class ChunkReader extends AbstractItemReader {
    private static final Logger logger = Logger.getLogger(ChunkReader.class.getName());
    private AmazonS3 cosClient = null;
    private int numOfLinesToSkip = 0;

    @Inject
    StepContext stepCtx;

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
     * Work item to process.
     */
    @Inject
    @BatchProperty(name = "import.partiton.workitem")
    String importPartitionWorkitem;

    public ChunkReader() {
        super();
    }

    @Override
    public Object readItem() throws Exception {
        List<Resource> loadedFhirResources = new ArrayList<Resource>();
        logger.info("readItem: get work item:" + importPartitionWorkitem);

        cosClient = COSUtils.getCosClient(cosCredentialIbm, cosApiKeyProperty, cosSrvinstId, cosEndpintUrl,
                cosLocation);
        if (cosClient == null) {
            logger.warning("readItem: Failed to get CosClient!");
            return null;
        } else {
            logger.finer("readItem: Got CosClient successfully!");
        }

        CheckPointData chunkData = (CheckPointData) stepCtx.getTransientUserData();
        if (chunkData == null) {
            chunkData = new CheckPointData(importPartitionWorkitem, numOfLinesToSkip);
            stepCtx.setTransientUserData(chunkData);
        } else {
            numOfLinesToSkip = chunkData.getNumOfLinesToSkip();
        }

        int imported = COSUtils.readFhirResourceFromObjectStore(cosClient, cosBucketName, importPartitionWorkitem, numOfLinesToSkip, loadedFhirResources);
        logger.info("readItem: loaded " + imported + " fhir resources from " + importPartitionWorkitem);
        if (imported == 0) {
            return null;
        } else {
            return loadedFhirResources;
        }
    }

    @Override
    public void open(Serializable checkpoint) throws Exception {
        if (checkpoint != null) {
            CheckPointData checkPointData = (CheckPointData) checkpoint;
            importPartitionWorkitem = checkPointData.getImportPartitionWorkitem();
            numOfLinesToSkip = checkPointData.getNumOfLinesToSkip();
            stepCtx.setTransientUserData(checkPointData);
        }
    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return (CheckPointData) stepCtx.getTransientUserData();
    }

}
