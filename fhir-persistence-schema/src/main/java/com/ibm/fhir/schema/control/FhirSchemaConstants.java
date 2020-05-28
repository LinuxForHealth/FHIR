/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control;

/**
 * Constants related to Schema creation and updating. 
 */
public class FhirSchemaConstants {
    // A lower pool size is selected as default to limit the likelihood of contention on the DBMS.
    // Standard connection/thread pool size
    public static final int DEFAULT_POOL_SIZE = 1;
    
    // Size of string columns in the search tables. DSTU2 was 511
    public static final int MAX_SEARCH_STRING_BYTES = 1024;
    public static final int MAX_TOKEN_VALUE_BYTES = 1024;
    public static final int LOGICAL_ID_BYTES = 255;

    // The first version of every object
    public static final int INITIAL_VERSION = 1;

    // Default tablespace
    public static final String FHIR_TS = "FHIR_TS";
    public static final int FHIR_TS_EXTENT_KB = 128;

    // Group of privilege grants used for FHIRUSER access
    public static final String FHIR_USER_GRANT_GROUP = "fhiruser";
    public static final String FHIR_BATCH_GRANT_GROUP = "fhirbatch";

    public static final String FHIR_SEQUENCE = "FHIR_SEQUENCE";
    public static final String FHIR_REF_SEQUENCE = "FHIR_REF_SEQUENCE";
    public static final String TENANT_SEQUENCE = "TENANT_SEQUENCE";

    // Tenant constants
    public static final String MT_ID = "MT_ID";
    public static final String TENANT_HASH = "TENANT_HASH";
    public static final String TENANT_SALT = "TENANT_SALT";
    public static final String TENANT_NAME = "TENANT_NAME";
    public static final String TENANT_STATUS = "TENANT_STATUS";
    
    // R4 Logical Resources
    public static final String LOGICAL_RESOURCES = "LOGICAL_RESOURCES";
    
    // R4 Logical Resource Tags and Security are modeled as token values
    public static final String TOKEN_VALUES = "TOKEN_VALUES";
    
    // R4 Logical Resource Profile property is a REFERENCE (str_values)
    public static final String STR_VALUES = "STR_VALUES";

    // R4 Logical Resource Date values for things like lastUpdated
    public static final String DATE_VALUES = "DATE_VALUES";

    // R4 Special extension to LIST_LOGICAL_RESOURCES to support list items
    public static final String LIST_LOGICAL_RESOURCES = "LIST_LOGICAL_RESOURCES";
    public static final String LIST_LOGICAL_RESOURCE_ITEMS = "LIST_LOGICAL_RESOURCE_ITEMS";
    public static final String ITEM_LOGICAL_ID = "ITEM_LOGICAL_ID";
    
    public static final String PATIENT_CURRENT_REFS        = "PATIENT_CURRENT_REFS";
    public static final String PATIENT_LOGICAL_RESOURCES   = "PATIENT_LOGICAL_RESOURCES";
    public static final String CURRENT_PROBLEMS_LIST       = "CURRENT_PROBLEMS_LIST";
    public static final String CURRENT_MEDICATIONS_LIST    = "CURRENT_MEDICATIONS_LIST";
    public static final String CURRENT_ALLERGIES_LIST      = "CURRENT_ALLERGIES_LIST";
    public static final String CURRENT_DRUG_ALLERGIES_LIST = "CURRENT_DRUG_ALLERGIES_LIST";

    public static final String LOGICAL_ID = "LOGICAL_ID";
    public static final String LOGICAL_RESOURCE_ID = "LOGICAL_RESOURCE_ID";
    public static final String DATA = "DATA";
    public static final String RESOURCE_ID = "RESOURCE_ID";
    public static final String CURRENT_RESOURCE_ID = "CURRENT_RESOURCE_ID";
    public static final String VERSION_ID = "VERSION_ID";
    public static final String IS_DELETED = "IS_DELETED";
    public static final String LAST_UPDATED = "LAST_UPDATED";
    public static final String PARAMETER_NAME = "PARAMETER_NAME";
    public static final String PARAMETER_NAME_ID = "PARAMETER_NAME_ID";
    public static final String STR_VALUE = "STR_VALUE";
    public static final String STR_VALUE_LCASE = "STR_VALUE_LCASE";
    public static final String CODE_SYSTEM_ID = "CODE_SYSTEM_ID";
    public static final String CODE_SYSTEM_NAME = "CODE_SYSTEM_NAME";
    public static final String TOKEN_VALUE = "TOKEN_VALUE";

    public static final String RESOURCE_TYPES = "RESOURCE_TYPES";
    public static final String RESOURCE_TYPE = "RESOURCE_TYPE";
    public static final String RESOURCE_TYPE_ID = "RESOURCE_TYPE_ID";

    public static final String DATE_VALUE_DROPPED_COLUMN = "DATE_VALUE";
    public static final String DATE_START = "DATE_START";
    public static final String DATE_END = "DATE_END";
    public static final String NUMBER_VALUE = "NUMBER_VALUE";
    public static final String NUMBER_VALUE_LOW = "NUMBER_VALUE_LOW";
    public static final String NUMBER_VALUE_HIGH = "NUMBER_VALUE_HIGH";
    public static final String LATITUDE_VALUE = "LATITUDE_VALUE";
    public static final String LONGITUDE_VALUE = "LONGITUDE_VALUE";

    public static final String QUANTITY_VALUE = "QUANTITY_VALUE";
    public static final String QUANTITY_VALUE_LOW = "QUANTITY_VALUE_LOW";
    public static final String QUANTITY_VALUE_HIGH = "QUANTITY_VALUE_HIGH";
    public static final String CODE = "CODE";

    // Constants for shared table names
    public static final String PARAMETER_NAMES = "PARAMETER_NAMES";
    public static final String CODE_SYSTEMS = "CODE_SYSTEMS";
    public static final String TENANTS = "TENANTS";

    // Each tenant can have multiple access keys (like API KEYS)
    // to allow for non-disruptive rotation
    public static final String TENANT_KEYS = "TENANT_KEYS";
    public static final String TENANT_KEY_ID = "TENANT_KEY_ID";

    public static final String IDX = "IDX_";
    public static final String FK = "FK_";
    public static final String PK = "PK_";

    // Initial partition range values for table create
    public static final int PART_LOWER = 0;
    public static final int PART_UPPER = 0;

}