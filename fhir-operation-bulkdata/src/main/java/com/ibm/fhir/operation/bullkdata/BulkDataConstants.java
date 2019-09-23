/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.operation.bullkdata;

import java.util.Arrays;
import java.util.List;

/**
 * @author pbastide 
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
    
}
