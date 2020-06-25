/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.jbatch.bulkdata.load;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemReader;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.context.StepContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.jbatch.bulkdata.common.BulkDataUtils;
import com.ibm.fhir.jbatch.bulkdata.common.Constants;
import com.ibm.fhir.model.resource.Resource;

/**
 * Bulk import Chunk implementation - the Reader.
 *
 */
@Dependent
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
    String cosEndpointUrl;

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
    @BatchProperty(name = Constants.PARTITION_RESOURCE_TYPE)
    String importPartitionResourceType;
    
    /**
     * Fhir tenant id.
     */
    @Inject
    @BatchProperty(name = Constants.FHIR_TENANT)
    String fhirTenant;

    /**
     * Fhir data store id.
     */
    @Inject
    @BatchProperty(name = Constants.FHIR_DATASTORE_ID)
    String fhirDatastoreId;

    public ChunkReader() {
        super();
    }

    @Override
    public Object readItem() throws Exception {
        // If the job is being stopped or in other status except for "started", then stop the read.
        if (!stepCtx.getBatchStatus().equals(BatchStatus.STARTED)) {
            return null;
        }
        List<Resource> loadedFhirResources = new ArrayList<Resource>();
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("readItem: get work item:" + importPartitionWorkitem + " resource type: " + importPartitionResourceType);
        }

        ImportTransientUserData chunkData = (ImportTransientUserData) stepCtx.getTransientUserData();
        numOfLinesToSkip = chunkData.getNumOfProcessedResources();

        long readStartTimeInMilliSeconds = System.currentTimeMillis();
        int numOfLoaded = 0;
        int numOfParseFailures = 0;
        switch (BulkImportDataSourceStorageType.from(dataSourceStorageType)) {
        case HTTPS:
            numOfParseFailures = BulkDataUtils.readFhirResourceFromHttps(importPartitionWorkitem, numOfLinesToSkip, loadedFhirResources, chunkData);
            break;
        case FILE:
            numOfParseFailures = BulkDataUtils.readFhirResourceFromLocalFile(importPartitionWorkitem, numOfLinesToSkip, loadedFhirResources, chunkData);
            break;
        case AWSS3:
        case IBMCOS:
            numOfParseFailures = BulkDataUtils.readFhirResourceFromObjectStore(cosClient, cosBucketName, importPartitionWorkitem,
                    numOfLinesToSkip, loadedFhirResources, chunkData);
            break;
        default:
            logger.warning("readItem: Data source storage type not found!");
            break;
        }

        chunkData.setTotalReadMilliSeconds(chunkData.getTotalReadMilliSeconds() + (System.currentTimeMillis() - readStartTimeInMilliSeconds));
        chunkData.setNumOfParseFailures(chunkData.getNumOfParseFailures() + numOfParseFailures);
        numOfLoaded = loadedFhirResources.size();
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("readItem: loaded " + numOfLoaded + " " + importPartitionResourceType + " from " + importPartitionWorkitem);
        }
        chunkData.setNumOfToBeImported(numOfLoaded);
        if (numOfLoaded == 0) {
            return null;
        } else {
            return loadedFhirResources;
        }
    }

    @Override
    public void open(Serializable checkpoint) throws Exception {
        if (BulkImportDataSourceStorageType.from(dataSourceStorageType).equals(BulkImportDataSourceStorageType.AWSS3)
                || BulkImportDataSourceStorageType.from(dataSourceStorageType).equals(BulkImportDataSourceStorageType.IBMCOS)) {

            if (fhirTenant == null) {
                fhirTenant = "default";
                logger.info("open: Set tenant to default!");
            }
            if (fhirDatastoreId == null) {
                fhirDatastoreId = Constants.DEFAULT_FHIR_TENANT;
                logger.info("open: Set DatastoreId to default!");
            }

            FHIRRequestContext.set(new FHIRRequestContext(fhirTenant, fhirDatastoreId));
            boolean isCosClientUseFhirServerTrustStore = FHIRConfigHelper
                .getBooleanProperty(FHIRConfiguration.PROPERTY_BULKDATA_BATCHJOB_ISCOSCLIENTUSEFHIRSERVERTRUSTSTORE, false);
            cosClient =
                BulkDataUtils.getCosClient(cosCredentialIbm, cosApiKeyProperty, cosSrvinstId, cosEndpointUrl,
                    cosLocation, isCosClientUseFhirServerTrustStore);

            if (cosClient == null) {
                logger.warning("open: Failed to get CosClient!");
                throw new Exception("Failed to get CosClient!!");
            } else {
                logger.finer("open: Got CosClient successfully!");
            }
        }

        if (checkpoint != null) {
            ImportCheckPointData checkPointData = (ImportCheckPointData) checkpoint;
            importPartitionWorkitem = checkPointData.getImportPartitionWorkitem();
            numOfLinesToSkip = checkPointData.getNumOfProcessedResources();
            checkPointData.setInFlyRateBeginMilliSeconds(System.currentTimeMillis());
            stepCtx.setTransientUserData(ImportTransientUserData.fromImportCheckPointData(checkPointData));
        } else {
            ImportTransientUserData chunkData = (ImportTransientUserData)ImportTransientUserData.Builder.builder()
                    .importPartitionWorkitem(importPartitionWorkitem)
                    .numOfProcessedResources(numOfLinesToSkip)
                    .importPartitionResourceType(importPartitionResourceType)
                    // This naming pattern is used in bulkdata operation to generate file links for import OperationOutcomes.
                    // e.g, for input file test1.ndjson, if there is any error during the importing, then the errors are in
                    // test1.ndjson_oo_errors.ndjson
                    // Note: for those good imports, we don't really generate any meaningful OperationOutcome, so only error import
                    //       OperationOutcomes are supported for now.
                    .uniqueIDForImportOperationOutcomes(importPartitionWorkitem + "_oo_success.ndjson")
                    .uniqueIDForImportFailureOperationOutcomes(importPartitionWorkitem + "_oo_errors.ndjson")
                    .build();
            long importFileSize = 0;
            switch (BulkImportDataSourceStorageType.from(dataSourceStorageType)) {
            case HTTPS:
                importFileSize = BulkDataUtils.getHttpsFileSize(importPartitionWorkitem);
                break;
            case FILE:
                importFileSize = BulkDataUtils.getLocalFileSize(importPartitionWorkitem);
                break;
            case AWSS3:
            case IBMCOS:
                importFileSize = BulkDataUtils.getCosFileSize(cosClient, cosBucketName, importPartitionWorkitem);
                break;
            default:
                throw new IllegalStateException ("Doesn't support data source storage type '" + dataSourceStorageType + "'!");
            }
            chunkData.setImportFileSize(importFileSize);
            chunkData.setInFlyRateBeginMilliSeconds(System.currentTimeMillis());
            stepCtx.setTransientUserData(chunkData);
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
