/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.jbatch.bulkdata.load;

import static com.ibm.fhir.jbatch.bulkdata.common.Constants.COS_API_KEY;
import static com.ibm.fhir.jbatch.bulkdata.common.Constants.COS_BUCKET_NAME;
import static com.ibm.fhir.jbatch.bulkdata.common.Constants.COS_ENDPOINT_URL;
import static com.ibm.fhir.jbatch.bulkdata.common.Constants.COS_IS_IBM_CREDENTIAL;
import static com.ibm.fhir.jbatch.bulkdata.common.Constants.COS_LOCATION;
import static com.ibm.fhir.jbatch.bulkdata.common.Constants.COS_SRVINST_ID;
import static com.ibm.fhir.jbatch.bulkdata.common.Constants.DEFAULT_FHIR_TENANT;
import static com.ibm.fhir.jbatch.bulkdata.common.Constants.FHIR_DATASTORE_ID;
import static com.ibm.fhir.jbatch.bulkdata.common.Constants.FHIR_TENANT;
import static com.ibm.fhir.jbatch.bulkdata.common.Constants.IMPORT_FHIR_DATASOURCES;
import static com.ibm.fhir.jbatch.bulkdata.common.Constants.IMPORT_FHIR_STORAGE_TYPE;
import static com.ibm.fhir.jbatch.bulkdata.common.Constants.IMPORT_INPUT_RESOURCE_TYPE;
import static com.ibm.fhir.jbatch.bulkdata.common.Constants.IMPORT_INPUT_RESOURCE_URL;
import static com.ibm.fhir.jbatch.bulkdata.common.Constants.IMPORT_MAX_PARTITIONPROCESSING_THREADNUMBER;
import static com.ibm.fhir.jbatch.bulkdata.common.Constants.IMPORT_PARTITTION_WORKITEM;
import static com.ibm.fhir.jbatch.bulkdata.common.Constants.PARTITION_RESOURCE_TYPE;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.partition.PartitionMapper;
import javax.batch.api.partition.PartitionPlan;
import javax.batch.api.partition.PartitionPlanImpl;
import javax.batch.runtime.context.StepContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request;
import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Result;
import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectSummary;
import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.jbatch.bulkdata.common.BulkDataUtils;

@Dependent
public class ImportPartitionMapper implements PartitionMapper {
    private static final Logger logger = Logger.getLogger(ImportPartitionMapper.class.getName());
    private AmazonS3 cosClient = null;

    @Inject
    StepContext stepCtx;

    /**
     * The data source storage type.
     * e.g, https, file, aws-s3, ibm-cos
     */
    @Inject
    @BatchProperty(name = IMPORT_FHIR_STORAGE_TYPE)
    String dataSourceStorageType;

    /**
     * The data sources info - base64 encoded data sources in Json array like following:
     * <p>
     * https
     * </p>
     *
     * <pre>
       [{
         "type": "Patient",
         "url": "https://client.example.org/patient_file_2.ndjson?sig=RHIX5Xcg0Mq2rqI3OlWT"
        },{
         "type": "Observations",
         "url": "https://client.example.org/obseration_file_19.ndjson?sig=RHIX5Xcg0Mq2rqI3OlWT"
       }]
     * </pre>
     * <p>
     * ibm-cos or aws-s3
     * </p>
     *
     * <pre>
       [{
          "type": "Patient",
          "url": "QHvFOj705i+9+WQXmuMHuiyH/QDCAZLl86aS6GydVp4=/Patient_1.ndjson"
        },{
          "type": "Observations",
          "url": "QHvFOj705i+9+WQXmuMHuiyH/QDCAZLl86aS6GydVp4=/Observation_1.ndjson"
        }]
     * </pre>
     * <p>
     * file
     * </p>
     *
     * <pre>
       [{
          "type": "Patient",
          "url": "/tmp/Patient_1.ndjson"
        },{
          "type": "Observations",
          "url": "/tmp/Observation_1.ndjson"
        }]
     * </pre>
     */
    @Inject
    @BatchProperty(name = IMPORT_FHIR_DATASOURCES)
    String dataSourcesInfo;

    /**
     * The IBM COS API key or S3 access key.
     */
    @Inject
    @BatchProperty(name = COS_API_KEY)
    String cosApiKeyProperty;

    /**
     * The IBM COS service instance id or S3 secret key.
     */
    @Inject
    @BatchProperty(name = COS_SRVINST_ID)
    String cosSrvinstId;

    /**
     * The IBM COS or S3 End point URL.
     */
    @Inject
    @BatchProperty(name = COS_ENDPOINT_URL)
    String cosEndpointUrl;

    /**
     * The IBM COS or S3 location.
     */
    @Inject
    @BatchProperty(name = COS_LOCATION)
    String cosLocation;

    /**
     * The IBM COS or S3 bucket name to import from.
     */
    @Inject
    @BatchProperty(name = COS_BUCKET_NAME)
    String cosBucketName;

    /**
     * If use IBM credential.
     */
    @Inject
    @BatchProperty(name = COS_IS_IBM_CREDENTIAL)
    String cosCredentialIbm;

    /**
     * Fhir tenant id.
     */
    @Inject
    @BatchProperty(name = FHIR_TENANT)
    String fhirTenant;

    /**
     * Fhir data store id.
     */
    @Inject
    @BatchProperty(name = FHIR_DATASTORE_ID)
    String fhirDatastoreId;

    public ImportPartitionMapper() {
        // No Operation
    }

    class FhirDataSource {
        private String type;
        private String url;

        public FhirDataSource(String type, String url) {
            super();
            this.type = type;
            this.url = url;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "FhirDataSource [type=" + type + ", url=" + url + "]";
        }
    }

    private List<FhirDataSource> getFhirDataSourcesForObjectStore(String dsTypeInfo, String dsDataLocationInfo)
            throws Exception {
        String nextToken = null;
        List<FhirDataSource> fhirDataSources = new ArrayList<>();

        if (fhirTenant == null) {
            fhirTenant = "default";
            logger.info("open: Set tenant to default!");
        }
        if (fhirDatastoreId == null) {
            fhirDatastoreId = DEFAULT_FHIR_TENANT;
            logger.info("open: Set DatastoreId to default!");
        }

        FHIRRequestContext.set(new FHIRRequestContext(fhirTenant, fhirDatastoreId));

        // Create a COS/S3 client if it's not created yet.
        if (cosClient == null) {
            boolean isCosClientUseFhirServerTrustStore = FHIRConfigHelper
                .getBooleanProperty(FHIRConfiguration.PROPERTY_BULKDATA_BATCHJOB_USEFHIRSERVERTRUSTSTORE, false);
            cosClient =
                    BulkDataUtils.getCosClient(cosCredentialIbm, cosApiKeyProperty, cosSrvinstId, cosEndpointUrl,
                            cosLocation, isCosClientUseFhirServerTrustStore);

            if (cosClient == null) {
                logger.warning("getFhirDataSourcesForObjectStore: Failed to get CosClient!");
                throw new Exception("Failed to get CosClient!!");
            } else {
                logger.finer("getFhirDataSourcesForObjectStore: Succeed get CosClient!");
            }
        }
        if (cosBucketName == null) {
            logger.warning("getFhirDataSourcesForObjectStore: Failed to get BucketName!");
            return fhirDataSources;
        } else {
            logger.finer("getFhirDataSourcesForObjectStore: Succeed get BucketName!");
        }
        cosBucketName = cosBucketName.toLowerCase();
        if (!cosClient.doesBucketExistV2(cosBucketName)) {
            logger.warning("getFhirDataSourcesForObjectStore: Bucket '" + cosBucketName + "' not found!");
            BulkDataUtils.listBuckets(cosClient);
            return fhirDataSources;
        }

        ListObjectsV2Result result = null;
        do {
            if (result != null) {
                nextToken = result.getNextContinuationToken();
            }
            ListObjectsV2Request request =
                    new ListObjectsV2Request().withBucketName(cosBucketName).withMaxKeys(1000)
                            .withContinuationToken(nextToken);
            result = cosClient.listObjectsV2(request);
            for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
                boolean isToBeProccessed = false;
                if (dsDataLocationInfo != null && !dsDataLocationInfo.trim().isEmpty()) {
                    if (objectSummary.getKey().startsWith(dsDataLocationInfo.trim())) {
                        isToBeProccessed = true;
                    }
                } else {
                    isToBeProccessed = true;
                }
                if (isToBeProccessed) {
                    logger.info("getFhirDataSourcesForObjectStore: ObjectStorge Object(" + objectSummary.getKey()
                            + ") - " + objectSummary.getSize() + " bytes.");
                    if (objectSummary.getSize() > 0) {
                        fhirDataSources.add(new FhirDataSource(dsTypeInfo, objectSummary.getKey()));
                    }
                }
            }
        } while (result != null && result.isTruncated());

        return fhirDataSources;
    }

    private List<FhirDataSource> getFhirDataSources(JsonArray dataSourceArray, BulkImportDataSourceStorageType type)
            throws Exception {
        List<FhirDataSource> fhirDataSources = new ArrayList<>();
        for (JsonValue jsonValue : dataSourceArray) {
            JsonObject dataSourceInfo = jsonValue.asJsonObject();
            String dsTypeInfo = dataSourceInfo.getString(IMPORT_INPUT_RESOURCE_TYPE);
            String dsDataLocationInfo = dataSourceInfo.getString(IMPORT_INPUT_RESOURCE_URL);

            switch (type) {
            case HTTPS:
            case FILE:
                fhirDataSources.add(new FhirDataSource(dsTypeInfo, dsDataLocationInfo));
                break;
            case AWSS3:
            case IBMCOS:
                fhirDataSources.addAll(getFhirDataSourcesForObjectStore(dsTypeInfo, dsDataLocationInfo));
                break;
            default:
                break;
            }
        }

        return fhirDataSources;
    }

    @Override
    public PartitionPlan mapPartitions() throws Exception {
        JsonArray dataSourceArray = BulkDataUtils.getDataSourcesFromJobInput(dataSourcesInfo);

        List<FhirDataSource> fhirDataSources =
                getFhirDataSources(dataSourceArray, BulkImportDataSourceStorageType.from(dataSourceStorageType));

        PartitionPlanImpl pp = new PartitionPlanImpl();
        pp.setPartitions(fhirDataSources.size());
        pp.setThreads(Math.min(IMPORT_MAX_PARTITIONPROCESSING_THREADNUMBER, fhirDataSources.size()));
        Properties[] partitionProps = new Properties[fhirDataSources.size()];

        int propCount = 0;
        for (FhirDataSource fhirDataSource : fhirDataSources) {
            Properties p = new Properties();
            p.setProperty(IMPORT_PARTITTION_WORKITEM, fhirDataSource.getUrl());
            p.setProperty(PARTITION_RESOURCE_TYPE, fhirDataSource.getType());

            partitionProps[propCount++] = p;
            System.out.println(p);
        }
        pp.setPartitionProperties(partitionProps);

        return pp;
    }
}