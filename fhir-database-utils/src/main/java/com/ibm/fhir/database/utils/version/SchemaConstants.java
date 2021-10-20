/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.version;

/**
 * Schema Constants
 */
public class SchemaConstants {
    public static final String VERSION_HISTORY = "VERSION_HISTORY";
    public static final String VERSION = "VERSION";
    public static final String APPLIED = "APPLIED";
    public static final String OBJECT_TYPE = "OBJECT_TYPE";
    public static final String OBJECT_NAME = "OBJECT_NAME";
    public static final String SCHEMA_NAME = "SCHEMA_NAME";
    
    // the CONTROL table
    public static final String CONTROL = "CONTROL";
    public static final String LEASE_OWNER_HOST = "LEASE_OWNER_HOST";
    public static final String LEASE_OWNER_UUID = "LEASE_OWNER_UUID";
    public static final String LEASE_UNTIL = "LEASE_UNTIL";
    
    // the SCHEMA_VERSIONS table
    public static final String SCHEMA_VERSIONS = "SCHEMA_VERSIONS";
    public static final String RECORD_ID = "RECORD_ID";
    public static final String VERSION_ID = "VERSION_ID";
}
