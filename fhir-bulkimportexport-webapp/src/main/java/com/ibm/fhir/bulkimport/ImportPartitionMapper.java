/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkimport;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.partition.PartitionMapper;
import javax.batch.api.partition.PartitionPlan;
import javax.batch.api.partition.PartitionPlanImpl;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request;
import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Result;
import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectSummary;
import com.ibm.fhir.bulkcommon.BulkDataUtils;
import com.ibm.fhir.bulkcommon.Constants;

public class ImportPartitionMapper implements PartitionMapper {
    private final static Logger logger = Logger.getLogger(ImportPartitionMapper.class.getName());
    private AmazonS3 cosClient = null;

    @Inject
    StepContext stepCtx;

    /**
     * The data source storage type.
     * e.g, https, file, aws-s3, ibm-cos
     */
    @Inject
    @BatchProperty(name = "fhir.storagetype")
    String dataSourceStorageType;

    /**
     * The data sources info - base64 encoded data sources in Json array like following:
     * <p> https </p>
     * <pre>
       [{
         "type": "Patient",
         "url": "https://client.example.org/patient_file_2.ndjson?sig=RHIX5Xcg0Mq2rqI3OlWT"
        },{
         "type": "Observations",
         "url": "https://client.example.org/obseration_file_19.ndjson?sig=RHIX5Xcg0Mq2rqI3OlWT"
       }]
     *  </pre>
     *
     * <p> ibm-cos or aws-s3</p>
     * <pre>
       [{
          "type": "Patient",
          "url": "QHvFOj705i+9+WQXmuMHuiyH/QDCAZLl86aS6GydVp4=/Patient_1.ndjson"
        },{
          "type": "Observations",
          "url": "QHvFOj705i+9+WQXmuMHuiyH/QDCAZLl86aS6GydVp4=/Observation_1.ndjson"
        }]
     *  </pre>
     *
     * <p> file </p>
     * <pre>
       [{
          "type": "Patient",
          "url": "/tmp/Patient_1.ndjson"
        },{
          "type": "Observations",
          "url": "/tmp/Observation_1.ndjson"
        }]
     *  </pre>
     */
    @Inject
    @BatchProperty(name = "fhir.dataSourcesInfo")
    String dataSourcesInfo;

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

    public ImportPartitionMapper() {

    }


    class FhirDataSource
    {
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
     };

     private List<FhirDataSource> getFhirDataSources4ObjectStore(String DSTypeInfo, String DSDataLocationInfo) {
         String nextToken = null;
         List<FhirDataSource> fhirDataSources = new ArrayList<>();
         cosClient = BulkDataUtils.getCosClient(cosCredentialIbm, cosApiKeyProperty, cosSrvinstId, cosEndpintUrl,
                 cosLocation);

         if (cosClient == null) {
             logger.warning("getFhirDataSources4ObjectStore: Failed to get CosClient!");
             return fhirDataSources;
         } else {
             logger.finer("getFhirDataSources4ObjectStore: Succeed get CosClient!");
         }
         if (cosBucketName == null) {
             logger.warning("getFhirDataSources4ObjectStore: Failed to get BucketName!");
             return fhirDataSources;
         }else {
             logger.finer("getFhirDataSources4ObjectStore: Succeed get BucketName!");
         }
         cosBucketName = cosBucketName.toLowerCase();
         if (!cosClient.doesBucketExist(cosBucketName)) {
             logger.warning("getFhirDataSources4ObjectStore: Bucket '" + cosBucketName + "' not found!");
             BulkDataUtils.listBuckets(cosClient);
             return fhirDataSources;
         }

         ListObjectsV2Result result = null;
         do {
             if (result != null) {
                 nextToken = result.getNextContinuationToken();
             }
             ListObjectsV2Request request = new ListObjectsV2Request().withBucketName(cosBucketName).withMaxKeys(1000)
                     .withContinuationToken(nextToken);
             result = cosClient.listObjectsV2(request);
             for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
                 boolean isToBeProccessed = false;
                 if (DSDataLocationInfo != null && !DSDataLocationInfo.trim().isEmpty()) {
                     if (objectSummary.getKey().startsWith(DSDataLocationInfo.trim())) {
                         isToBeProccessed = true;
                     }
                 } else {
                     isToBeProccessed = true;
                 }
                 if (isToBeProccessed) {
                     logger.info("getFhirDataSources4ObjectStore: ObjectStorge Object(" + objectSummary.getKey() + ") - " + objectSummary.getSize()
                             + " bytes.");
                     if (objectSummary.getSize() > 0) {
                         fhirDataSources.add(new FhirDataSource(DSTypeInfo, objectSummary.getKey()));
                     }
                 }
             }
         } while (result != null && result.isTruncated());

         return fhirDataSources;
     }


     private List<FhirDataSource> getFhirDataSources(JsonArray dataSourceArray, BulkImportDataSourceStorageType type)
     {
         List<FhirDataSource> fhirDataSources = new ArrayList<>();
         for (JsonValue jsonValue : dataSourceArray) {
             JsonObject dataSourceInfo = jsonValue.asJsonObject();
             String DSTypeInfo = dataSourceInfo.getString(Constants.IMPORT_INPUT_RESOURCE_TYPE);
             String DSDataLocationInfo = dataSourceInfo.getString(Constants.IMPORT_INPUT_RESOURCE_URL);

             switch (type) {
             case HTTPS:
             case FILE:
                 fhirDataSources.add(new FhirDataSource(DSTypeInfo, DSDataLocationInfo));
                 break;
             case AWSS3:
             case IBMCOS:
                 fhirDataSources.addAll(getFhirDataSources4ObjectStore(DSTypeInfo, DSDataLocationInfo));
                 break;
             default:
                 break;
             }
         }

         return fhirDataSources;
     }


    @Override
    public PartitionPlan mapPartitions() {
        JsonReader reader = Json.createReader(new StringReader(
                new String(Base64.getDecoder().decode(dataSourcesInfo), StandardCharsets.UTF_8)));
        JsonArray dataSourceArray = reader.readArray();
        reader.close();

        List<FhirDataSource> fhirDataSources = getFhirDataSources(dataSourceArray, BulkImportDataSourceStorageType.from(dataSourceStorageType));

        PartitionPlanImpl pp = new PartitionPlanImpl();
        pp.setPartitions(fhirDataSources.size());
        pp.setThreads(Math.min(Constants.IMPORT_MAX_PARTITIONPROCESSING_THREADNUMBER, fhirDataSources.size()));
        Properties[] partitionProps = new Properties[fhirDataSources.size()];

        int propCount = 0;
        for (FhirDataSource fhirDataSource : fhirDataSources) {
            Properties p = new Properties();
            p.setProperty(Constants.IMPORT_PARTITTION_WORKITEM, fhirDataSource.getUrl());
            p.setProperty(Constants.IMPORT_PARTITTION_RESOURCE_TYPE, fhirDataSource.getType());
            partitionProps[ propCount++ ] = p;
        }
        pp.setPartitionProperties(partitionProps);

        return pp;
    }

}
