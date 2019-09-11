/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

/**
 * Constants for the JavaBatch Jobs.
 * 
 * @author Albert Wang
 */

package com.ibm.waston.health.fhir.bulkcommon;

public class Constants {
    
    public static final String DEFAULT_FHIR_TENANT = "default";
    public static final String DEFAULT_COS_BUCKETNAME = "fhir-bulkImExport-Connectathon";
    // The minimal size (5M bytes) for COS multiple-parts upload.
    public static final int COS_PART_MINIMALSIZE = 5242880;
    public static final int DEFAULT_SEARCH_PAGE_SIZE = 1000;
    public static final int DEFAULT_NUMOFPAGES_EACH_COS_OBJECT = 10;

}
