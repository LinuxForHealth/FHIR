/*
 * (C) Copyright IBM Corp. 2019, 2020
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
import com.ibm.fhir.bulkcommon.BulkDataUtils;
import com.ibm.fhir.bulkcommon.Constants;
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
     * The data source storage type.
     */
    @Inject
    @BatchProperty(name = Constants.IMPORT_FHIR_STORAGE_TYPE)
    String dataSourceStorageType;

    /**
     * The IBM COS API key or S3 access key.
     */
    @Inject
    @BatchProperty(name = Constants.COS_API_KEY)
    String cosApiKeyProperty;

    /**
     * The IBM COS service instance id or S3 secret key.
     */
    @Inject
    @BatchProperty(name = Constants.COS_SRVINST_ID)
    String cosSrvinstId;

    /**
     * The IBM COS or S3 End point URL.
     */
    @Inject
    @BatchProperty(name = Constants.COS_ENDPOINT_URL)
    String cosEndpintUrl;

    /**
     * The IBM COS or S3 location.
     */
    @Inject
    @BatchProperty(name = Constants.COS_LOCATION)
    String cosLocation;

    /**
     * The IBM COS or S3 bucket name to import from.
     */
    @Inject
    @BatchProperty(name = Constants.COS_BUCKET_NAME)
    String cosBucketName;

    /**
     * If use IBM credential or S3 secret keys.
     */
    @Inject
    @BatchProperty(name = Constants.COS_IS_IBM_CREDENTIAL)
    String cosCredentialIbm;

    /**
     * Work item to process.
     */
    @Inject
    @BatchProperty(name = Constants.IMPORT_PARTITTION_WORKITEM)
    String importPartitionWorkitem;

    /**
     * Fhir resource type to process.
     */
    @Inject
    @BatchProperty(name = Constants.IMPORT_PARTITTION_RESOURCE_TYPE)
    String importPartitionResourceType;

    public ChunkReader() {
        super();
    }

    @Override
    public Object readItem() throws Exception {
        List<Resource> loadedFhirResources = new ArrayList<Resource>();
        logger.info("readItem: get work item:" + importPartitionWorkitem + " resource type: " + importPartitionResourceType);

        ImportTransientUserData chunkData = (ImportTransientUserData) stepCtx.getTransientUserData();
        if (chunkData == null) {
            chunkData = new ImportTransientUserData(importPartitionWorkitem, numOfLinesToSkip, importPartitionResourceType);
            stepCtx.setTransientUserData(chunkData);
        } else {
            numOfLinesToSkip = chunkData.getNumOfProcessedResources();
        }

        int imported = 0;

        switch (BulkImportDataSourceStorageType.from(dataSourceStorageType)) {
        case HTTPS:
            imported = BulkDataUtils.readFhirResourceFromHttps(importPartitionWorkitem, numOfLinesToSkip, loadedFhirResources);
            break;
        case FILE:
            imported = BulkDataUtils.readFhirResourceFromLocalFile(importPartitionWorkitem, numOfLinesToSkip, loadedFhirResources);
            break;
        case AWSS3:
        case IBMCOS:
            cosClient = BulkDataUtils.getCosClient(cosCredentialIbm, cosApiKeyProperty, cosSrvinstId, cosEndpintUrl,
                    cosLocation);
            if (cosClient == null) {
                logger.warning("readItem: Failed to get CosClient!");
                return null;
            } else {
                logger.finer("readItem: Got CosClient successfully!");
            }
            imported = BulkDataUtils.readFhirResourceFromObjectStore(cosClient, cosBucketName, importPartitionWorkitem, numOfLinesToSkip, loadedFhirResources);
            break;
        default:
            break;
        }
        logger.info("readItem: loaded " + imported + " " + importPartitionResourceType + " from " + importPartitionWorkitem);
        chunkData.setNumOfToBeImported(imported);
        if (imported == 0) {
            return null;
        } else {
            return loadedFhirResources;
        }
    }

    @Override
    public void open(Serializable checkpoint) throws Exception {
        if (checkpoint != null) {
            ImportCheckPointData checkPointData = (ImportCheckPointData) checkpoint;
            importPartitionWorkitem = checkPointData.getImportPartitionWorkitem();
            numOfLinesToSkip = checkPointData.getNumOfProcessedResources();

            stepCtx.setTransientUserData(ImportTransientUserData.fromImportCheckPointData(checkPointData));
        }
    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return ImportCheckPointData.fromImportTransientUserData((ImportTransientUserData)stepCtx.getTransientUserData());
    }

}
