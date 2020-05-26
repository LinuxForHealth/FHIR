/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkcommon;

/**
 * Constants for BulkExportImport.
 *
 */
public class Constants {

    public static final String DEFAULT_FHIR_TENANT = "default";
    public static final String DEFAULT_COS_BUCKETNAME = "fhir-bulkImExport-Connectathon";

    // The minimal size (5M bytes) for COS multiple-parts upload.
    public static final int COS_PART_MINIMALSIZE = 5242880;
    public static final int DEFAULT_SEARCH_PAGE_SIZE = 1000;
    public static final int DEFAULT_COSFILE_MAX_SIZE = 209715200;
    public static final int DEFAULT_COSFILE_MAX_RESOURCESNUMBER = 500000;
    public static final String FHIR_SEARCH_LASTUPDATED = "_lastUpdated";
    public static final byte[] NDJSON_LINESEPERATOR = "\r\n".getBytes();

    public static final int IMPORT_MAX_PARTITIONPROCESSING_THREADNUMBER = 10;
    public static final int EXPORT_MAX_PARTITIONPROCESSING_THREADNUMBER = 10;
    // The number of resources to commit to DB in each batch, the slower the DB connection, the smaller
    // this value should be set.
    public static final int IMPORT_NUMOFFHIRRESOURCES_PERREAD = 20;
    public static final String IMPORT_INPUT_RESOURCE_TYPE = "type";
    public static final String IMPORT_INPUT_RESOURCE_URL = "url";
    public static final int IMPORT_INFLY_RATE_NUMOFFHIRRESOURCES = 2000;


    // Job parameters
    public static final String COS_API_KEY = "cos.api.key";
    public static final String COS_SRVINST_ID = "cos.srvinst.id";
    public static final String COS_ENDPOINT_URL = "cos.endpointurl";
    public static final String COS_LOCATION = "cos.location";
    public static final String COS_BUCKET_NAME = "cos.bucket.name";
    public static final String COS_IS_IBM_CREDENTIAL = "cos.credential.ibm";
    public static final String COS_BUCKET_FILE_MAX_SIZE = "cos.bucket.filemaxsize";
    public static final String COS_BUCKET_FILE_MAX_RESOURCES = "cos.bucket.filemaxresources";
    // COS bucket for import OperationOutcomes
    public static final String COS_OPERATIONOUTCOMES_BUCKET_NAME = "cos.operationoutcomes.bucket.name";
    public static final String FHIR_TENANT = "fhir.tenant";
    public static final String FHIR_DATASTORE_ID = "fhir.datastoreid";
    public static final String FHIR_RESOURCETYPES = "fhir.resourcetype";
    public static final String IMPORT_FHIR_STORAGE_TYPE = "import.fhir.storagetype";
    public static final String IMPORT_FHIR_IS_VALIDATION_ON = "import.fhir.validation";
    public static final String IMPORT_FHIR_DATASOURCES = "fhir.dataSourcesInfo";
    public static final String EXPORT_FHIR_SEARCH_FROMDATE = "fhir.search.fromdate";
    public static final String EXPORT_FHIR_SEARCH_TODATE = "fhir.search.todate";
    public static final String EXPORT_FHIR_SEARCH_PAGESIZE = "fhir.search.pagesize";
    public static final String EXPORT_FHIR_SEARCH_TYPEFILTERS = "fhir.typeFilters";
    public static final String EXPORT_FHIR_SEARCH_PATIENTGROUPID = "fhir.search.patientgroupid";
    public static final String EXPORT_COS_OBJECT_PATHPREFIX = "cos.bucket.pathprefix";

    // Partition work item info generated in ImportPartitionMapper.
    public static final String IMPORT_PARTITTION_WORKITEM = "import.partition.workitem";
    public static final String PARTITION_RESOURCE_TYPE = "partition.resourcetype";

    // Control if push OperationOutcomes to COS/S3.
    public static final boolean IMPORT_IS_COLLECT_OPERATIONOUTCOMES = true;
    // Retry times when https or amazon s3 client timeout or other error happens, e.g, timeout can happen if the batch write to DB takes
    // longer than the socket timeout, set to retry once for now.
    public static final int IMPORT_RETRY_TIMES = 1;
    public static final int COS_REQUEST_TIMEOUT = 10000;
    // Batch writing to DB can take long time which can make the idle COS/S3 client connection timeout, so set the client socket timeout
    // to 120 seconds which is the default DB2 timeout.
    public static final int COS_SOCKET_TIMEOUT = 120000;
}
