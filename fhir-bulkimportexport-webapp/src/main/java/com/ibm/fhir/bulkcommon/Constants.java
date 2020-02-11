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
    public static final String IMPORT_PARTITTION_WORKITEM = "import.partiton.workitem";
    public static final String IMPORT_PARTITTION_RESOURCE_TYPE = "import.partiton.resourcetype";
    public static final int IMPORT_NUMOFFHIRRESOURCES_PERREAD = 100;
    public static final String IMPORT_INPUT_RESOURCE_TYPE = "type";
    public static final String IMPORT_INPUT_RESOURCE_URL = "url";
    // Control if push OperationOutcomes to COS/S3.
    public static final boolean IMPORT_IS_COLLECT_OPERATIONOUTCOMES = true;
}
