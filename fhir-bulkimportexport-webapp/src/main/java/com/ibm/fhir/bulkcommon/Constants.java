/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

/**
 * Constants for the JavaBatch Jobs.
 *
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
    public static final int DEFAULT_NUMOFPAGES_EACH_COS_OBJECT = 10;
    public static final int DEFAULT_NUMOFOBJECTS_PERREAD = 1;
    public static final int DEFAULT_MAXCOSFILE_SIZE = 104857600;
    public static final String FHIR_SEARCH_LASTUPDATED = "_lastUpdated";
    public static final byte[] NDJSON_LINESEPERATOR = "\r\n".getBytes();

    public static final int IMPORT_MAX_PARTITIONPROCESSING_THREADNUMBER = 10;
    public static final int IMPORT_NUMOFFHIRRESOURCES_PERREAD = 1000;
    public static final String IMPORT_INPUT_RESOURCE_TYPE = "type";
    public static final String IMPORT_INPUT_RESOURCE_URL = "url";


    // Job parameters
    public static final String COS_API_KEY = "cos.api.key";
    public static final String COS_SRVINST_ID = "cos.srvinst.id";
    public static final String COS_ENDPOINT_URL = "cos.endpointurl";
    public static final String COS_LOCATION = "cos.location";
    public static final String COS_BUCKET_NAME = "cos.bucket.name";
    public static final String COS_IS_IBM_CREDENTIAL = "cos.credential.ibm";
    public static final String COS_OPERATIONOUTCOMES_BUCKET_NAME = "cos.operationoutcomes.bucket.name";
    public static final String FHIR_TENANT = "fhir.tenant";
    public static final String FHIR_DATASTORE_ID = "fhir.datastoreid";
    public static final String IMPORT_PARTITTION_WORKITEM = "import.partiton.workitem";
    public static final String IMPORT_PARTITTION_RESOURCE_TYPE = "import.partiton.resourcetype";
    public static final String IMPORT_FHIR_STORAGE_TYPE = "import.fhir.storagetype";

    // Control if push OperationOutcomes to COS/S3.
    public static final boolean IMPORT_IS_COLLECT_OPERATIONOUTCOMES = false;
    // Control if reuse the input stream of the data source across the chunks.
    public static final boolean IMPORT_IS_REUSE_INPUTSTREAM = false;
}
