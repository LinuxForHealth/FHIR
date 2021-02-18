/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.jbatch.bulkdata.common;

/**
 * Constants for BulkExportImport
 */
public class Constants {
    public static final String DEFAULT_FHIR_TENANT = "default";

    // How many resources should we store in a single COS item
    public static final int DEFAULT_MAX_RESOURCES_PER_ITEM = 10000;

    /**
     * The maximum number of resources read in each iteration of the the patient or group export ChunkReaders.
     */
    public static final int DEFAULT_PATIENT_EXPORT_SEARCH_PAGE_SIZE = 200;




    public static final String FHIR_SEARCH_LASTUPDATED = "_lastUpdated";
    public static final byte[] NDJSON_LINESEPERATOR = "\r\n".getBytes();


    public static final int EXPORT_MAX_PARTITIONPROCESSING_THREADNUMBER = 10;

    // The number of resources to commit to DB in each batch, the slower the DB connection, the smaller
    // this value should be set.
    public static final int IMPORT_NUMOFFHIRRESOURCES_PERREAD = 20;
    public static final int IMPORT_INFLY_RATE_NUMOFFHIRRESOURCES = 2000;
    public static final String IMPORT_INPUT_RESOURCE_TYPE = "type";
    public static final String IMPORT_INPUT_RESOURCE_URL = "url";


    // Job parameters
    public static final String COS_API_KEY = "cos.api.key";
    public static final String COS_SRVINST_ID = "cos.srvinst.id";
    public static final String COS_ENDPOINT_URL = "cos.endpoint.internal";
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
    public static final String EXPORT_FHIR_FORMAT = "fhir.exportFormat";
    public static final String EXPORT_FHIR_SEARCH_PATIENTGROUPID = "fhir.search.patientgroupid";
    public static final String EXPORT_COS_OBJECT_PATHPREFIX = "cos.bucket.pathprefix";

    // Partition work item info generated in ImportPartitionMapper.
    public static final String IMPORT_PARTITTION_WORKITEM = "import.partition.workitem";
    public static final String PARTITION_RESOURCE_TYPE = "import.partition.resourcetype";

    // Retry times when https or amazon s3 client timeout or other error happens, e.g, timeout can happen if the batch write to DB takes
    // longer than the socket timeout, set to retry once for now.
    public static final int IMPORT_RETRY_TIMES = 1;

    public static final String INCOMING_URL = "incomingUrl";
}
