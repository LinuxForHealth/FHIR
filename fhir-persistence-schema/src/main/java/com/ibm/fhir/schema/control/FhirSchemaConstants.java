/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control;

/**
 * Constants related to Schema creation and updating.
 */
public class FhirSchemaConstants {

    // This limit is used to limit db2 size in a stored procedure.
    public static final int STORED_PROCEDURE_SIZE_LIMIT = 1048576;

    // Make sure the connection pool is sized larger than the thread pool
    public static final int CONNECTION_POOL_HEADROOM = 9;

    // The default size of the thread pool for parallel operations
    public static final int DEFAULT_THREAD_POOL_SIZE = 1; // serialize operations

    // Size of string columns in the search tables. DSTU2 was 511
    public static final int MAX_SEARCH_STRING_BYTES = 1024;
    public static final int MAX_TOKEN_VALUE_BYTES = 1024;
    public static final int LOGICAL_ID_BYTES = 255;

    // Currently a constant admin schema name
    public static final String FHIR_ADMIN = "FHIR_ADMIN";

    // Default tablespace
    public static final String FHIR_TS = "FHIR_TS";
    public static final int FHIR_TS_EXTENT_KB = 128;

    // Group of privilege grants used for FHIRUSER access
    public static final String FHIR_USER_GRANT_GROUP = "fhiruser";
    public static final String FHIR_BATCH_GRANT_GROUP = "fhirbatch";
    public static final String FHIR_OAUTH_GRANT_GROUP = "fhiroauth";

    public static final String FHIR_SEQUENCE = "FHIR_SEQUENCE";
    public static final String FHIR_REF_SEQUENCE = "FHIR_REF_SEQUENCE";
    public static final String TENANT_SEQUENCE = "TENANT_SEQUENCE";
    public static final long FHIR_REF_SEQUENCE_START = 20000;
    public static final int FHIR_REF_SEQUENCE_CACHE = 1000;
    public static final int FHIR_IDENTITY_SEQUENCE_CACHE = 1000;

    // DO NOT CHANGE. Integrity of DB relies on these not changing
    public static final String REFERENCES_SEQUENCE = "REFERENCES_SEQUENCE";
    public static final int REFERENCES_SEQUENCE_START = 1;
    public static final int REFERENCES_SEQUENCE_CACHE = 1000;
    public static final int REFERENCES_SEQUENCE_INCREMENT = 20;

    // Tenant constants
    public static final String MT_ID = "MT_ID";
    public static final String TENANT_HASH = "TENANT_HASH";
    public static final String TENANT_SALT = "TENANT_SALT";
    public static final String TENANT_NAME = "TENANT_NAME";
    public static final String TENANT_STATUS = "TENANT_STATUS";

    public static final String COMMON_CANONICAL_VALUES = "COMMON_CANONICAL_VALUES";
    public static final String CANONICAL_ID = "CANONICAL_ID";
    public static final String URL = "URL";
    public static final String PROFILES = "PROFILES";
    public static final String SECURITY = "SECURITY";
    public static final String TAGS = "TAGS";
    public static final int CANONICAL_URL_BYTES = 1024; // a reasonable value of our choosing
    public static final int VERSION_BYTES = 16;
    public static final int FRAGMENT_BYTES = 16;

    // R4 Logical Resources
    public static final String LOGICAL_RESOURCES = "LOGICAL_RESOURCES";
    public static final String REINDEX_TSTAMP = "REINDEX_TSTAMP";
    public static final String REINDEX_TXID = "REINDEX_TXID";
    public static final String REINDEX_SEQ = "REINDEX_SEQ";
    public static final String RESOURCE_CHANGE_LOG = "RESOURCE_CHANGE_LOG";

    // Type of change C - Create, U - Update, S - Soft Delete. H - Hard Delete
    public static final String CHANGE_TYPE = "CHANGE_TYPE";

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
    public static final String FRAGMENT = "FRAGMENT";
    public static final String RESOURCE_ID = "RESOURCE_ID";
    public static final String CURRENT_RESOURCE_ID = "CURRENT_RESOURCE_ID";
    public static final String CHANGE_TSTAMP = "CHANGE_TSTAMP";
    public static final String VERSION_ID = "VERSION_ID";
    public static final String VERSION = "VERSION";
    public static final String IS_DELETED = "IS_DELETED";
    public static final String LAST_UPDATED = "LAST_UPDATED";
    public static final String PARAMETER_HASH = "PARAMETER_HASH";
    public static final int PARAMETER_HASH_BYTES = 44; // For SHA-256 encoded as Base64
    public static final String PARAMETER_NAME = "PARAMETER_NAME";
    public static final String PARAMETER_NAME_ID = "PARAMETER_NAME_ID";
    public static final String STR_VALUE = "STR_VALUE";
    public static final String STR_VALUE_LCASE = "STR_VALUE_LCASE";
    public static final String CODE_SYSTEM_ID = "CODE_SYSTEM_ID";
    public static final String CODE_SYSTEM_NAME = "CODE_SYSTEM_NAME";
    public static final String TOKEN_VALUE = "TOKEN_VALUE";
    public static final String COMPOSITE_ID = "COMPOSITE_ID";
    public static final String RESOURCE_PAYLOAD_KEY = "RESOURCE_PAYLOAD_KEY";
    public static final int UUID_LEN = 36;
    
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

    // Table for Normalization of References (Internal and External)
    public static final String LOCAL_REFERENCES = "LOCAL_REFERENCES";
    public static final String REF_LOGICAL_RESOURCE_ID = "REF_LOGICAL_RESOURCE_ID";
//    public static final String EXTERNAL_SYSTEMS = "EXTERNAL_SYSTEMS";
//    public static final String EXTERNAL_SYSTEM_ID = "EXTERNAL_SYSTEM_ID";
//    public static final String EXTERNAL_SYSTEM_NAME = "EXTERNAL_SYSTEM_NAME";
//    public static final String EXTERNAL_REFERENCES = "EXTERNAL_REFERENCES";

    // Mapping table identifying the profiles associated with a particular resource
    public static final String LOGICAL_RESOURCE_PROFILES = "LOGICAL_RESOURCE_PROFILES";
    public static final String LOGICAL_RESOURCE_SECURITY = "LOGICAL_RESOURCE_SECURITY";
    public static final String LOGICAL_RESOURCE_TAGS = "LOGICAL_RESOURCE_TAGS";

    // For V0006 (issue #1366) token_values become normalized to improve storage efficiency
    public static final String COMMON_TOKEN_VALUES = "COMMON_TOKEN_VALUES";
    public static final String COMMON_TOKEN_VALUE_ID = "COMMON_TOKEN_VALUE_ID";

    // The table mapping a resource to its (shared) token values
    public static final String RESOURCE_TOKEN_REFS = "RESOURCE_TOKEN_REFS";
    public static final String REF_RESOURCE_TYPE_ID = "REF_RESOURCE_TYPE_ID";
    public static final String REF_VERSION_ID = "REF_VERSION_ID";

    // View suffix to overlay the new common_token_values and resource_token_refs tables
    public static final String TOKEN_VALUES_V = "TOKEN_VALUES_V";

    public static final String LOGICAL_RESOURCE_COMPARTMENTS = "LOGICAL_RESOURCE_COMPARTMENTS";
    public static final String COMPARTMENT_LOGICAL_RESOURCE_ID = "COMPARTMENT_LOGICAL_RESOURCE_ID";
    public static final String COMPARTMENT_NAME_ID = "COMPARTMENT_NAME_ID";

    public static final String PG_FILLFACTOR_PROP = "fillfactor";
    public static final int PG_FILLFACTOR_VALUE = 90; // do not change without bumping schema versions for affected tables

    // Support for $erase operation
    public static final String ERASED_RESOURCES = "ERASED_RESOURCES";
    public static final String ERASED_RESOURCE_ID = "ERASED_RESOURCE_ID";
    public static final String ERASED_RESOURCE_GROUP_ID = "ERASED_RESOURCE_GROUP_ID";
}