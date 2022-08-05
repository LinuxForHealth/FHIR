/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.operation.bulkdata.model.type;

/**
 * OperationFields which are used in the JobParameters or ExitStatus.
 */
public final class OperationFields {

    // Minimally Required
    public static final String FHIR_TENANT_ID = "fhir.tenant";
    public static final String FHIR_DATASTORE_ID = "fhir.datastoreid";
    public static final String FHIR_INCOMING_URL = "fhir.incomingUrl";
    public static final String FHIR_REQUESTING_USER = "fhir.requesting.user";

    // Outcome / Source for Import / Export
    public static final String FHIR_BULKDATA_SOURCE = "fhir.bulkdata.source";
    public static final String FHIR_BULKDATA_OUTCOME = "fhir.bulkdata.outcome";

    // Partition
    public static final String PARTITION_RESOURCETYPE = "partition.resourcetype";
    public static final String PARTITION_WORKITEM = "partition.workitem";
    /*
     * Used to identify the original file imported when it matrixes to segments
     * e.g. original import file is Patient.ndjson matrixes to
     * Patient.ndjson_seg0, Patient.ndjson_seg1 which are individual partitions.
     */
    public static final String PARTITION_MATRIX = "partition.matrix";

    // Parameters
    public static final String FHIR_SEARCH_FROM_DATE = "fhir.search.fromdate";
    public static final String FHIR_SEARCH_TO_DATE = "fhir.search.todate";
    public static final String FHIR_SEARCH_TYPE_FILTERS = "fhir.typeFilters";
    public static final String FHIR_EXPORT_FORMAT = "fhir.exportFormat";
    public static final String FHIR_RESOURCE_TYPES = "fhir.resourcetype";
    public static final String FHIR_SEARCH_PATIENT_GROUP_ID = "fhir.search.patientgroupid";

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
     *
     * Used with $import
     */
    public static final String FHIR_DATA_SOURCES_INFO = "fhir.dataSourcesInfo";

    /**
     * The data source storage type.
     * e.g, https, file, aws-s3, ibm-cos
     */
    public static final String FHIR_IMPORT_STORAGE_TYPE = "import.fhir.storagetype";

    // Prefix is to obfuscate the public path.
    public static final String COS_BUCKET_PATH_PREFIX = "cos.bucket.pathprefix";

    public static final String IMPORT_INPUT_RESOURCE_TYPE = "type";
    public static final String IMPORT_INPUT_RESOURCE_URL = "url";
}