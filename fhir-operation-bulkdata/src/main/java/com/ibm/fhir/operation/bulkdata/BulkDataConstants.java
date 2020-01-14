/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.operation.bulkdata;

import java.util.Arrays;
import java.util.List;

/**
 * Constants for BulkData operations
 *
 */
public class BulkDataConstants {

    public static final String MEDIA_TYPE_ND_JSON = "application/fhir+ndjson";

    // Import
    public static final String INPUT_FORMAT = MEDIA_TYPE_ND_JSON;

    // Export
    public static final String EXPORT_FORMAT = MEDIA_TYPE_ND_JSON;
    public static final List<String> EXPORT_FORMATS = Arrays.asList(MEDIA_TYPE_ND_JSON, "application/ndjson", "ndjson" );

    // Export
    public static final String PARAM_OUTPUT_FORMAT = "_outputFormat";
    public static final String PARAM_SINCE = "_since";
    public static final String PARAM_TYPE = "_type";
    public static final String PARAM_TYPE_FILTER = "_typeFilter";


    /**
     * Search Modifiers
     */
    public enum ExportType
    {
        SYSTEM, PATIENT, GROUP;
    }
}
