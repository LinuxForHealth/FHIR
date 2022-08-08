/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.scout.cql;


/**
 *
 */
public class SchemaConstants {

    // Break binary data into bite-sized pieces when storing
    public static final int CHUNK_SIZE = 1024 * 1024;
 
    public static final String LOGICAL_RESOURCES = "logical_resources";
    public static final String RESOURCE_HISTORY = "resource_history";
    public static final String PAYLOAD_CHUNKS = "payload_chunks";
    public static final String LAST_MODIFIED = "last_modified";
    
    // Resource type Parameter tables
    public static final String PARAM_STR_VALUES = "param_str_values";
    public static final String PARAM_STR_LOWER_VALUES = "param_str_lower_values";

    // System Parameter tables
    public static final String SYSTEM_STR_VALUES = "system_str_values";
    public static final String SYSTEM_STR_LOWER_VALUES = "system_str_lower_values";

}
