/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.bucket.persistence;

/**
 * Constants used in the FHIR bucket loader schema
 */
public class SchemaConstants {
    
    // common constants
    public static final boolean NOT_NULL = false;
    public static final boolean NULLABLE = true;
    
    // common prefixes
    public static final String FK = "FK";
    public static final String IDX = "IDX";
    public static final String UNQ = "UNQ";
    
    // The job allocation sequence
    public static final String JOB_ALLOCATION_SEQ = "JOB_ALLOCATION_SEQ";
    
    // A table to normalize the resource type name associated with each recorded resource
    public static final String RESOURCE_TYPES = "RESOURCE_TYPES";
    public static final String RESOURCE_TYPE = "RESOURCE_TYPE";
    public static final String RESOURCE_TYPE_ID = "RESOURCE_TYPE_ID";

    // Each time a bucket loader process starts it registers a new entry
    // in the loader_instances table. The scanner thread periodically
    // updates the HEARTBEAT_TSTAMP to indicate liveness. Periodically
    // other loaders check for missed heartbeats and can reset allocated
    // but unfinished work.
    public static final String LOADER_INSTANCES = "LOADER_INSTANCES";
    public static final String LOADER_INSTANCE_ID = "LOADER_INSTANCE_ID";
    public static final String LOADER_INSTANCE_KEY = "LOADER_INSTANCE_KEY";
    public static final String HEARTBEAT_TSTAMP = "HEARTBEAT_TSTAMP";
    public static final String STATUS = "STATUS";
    public static final String HOSTNAME = "HOSTNAME";
    public static final String PID = "PID";
    public static final String CURRENT_ID = "CURRENT_ID";
    
    // The bucket path roots to scan looking for files. Usually fixed at start time
    public static final String BUCKET_PATHS = "BUCKET_PATHS";
    public static final String BUCKET_PATH_ID = "BUCKET_PATH_ID";
    public static final String BUCKET_NAME = "BUCKET_NAME";
    public static final String BUCKET_PATH = "BUCKET_PATH";
    public static final String SCAN_TSTAMP = "SCAN_TSTAMP"; // tstamp of the latest scan
    public static final String VERSION = "VERSION";

    // The individual resource bundles with their SHA256 hash so
    // we can easily detect changes
    public static final String RESOURCE_BUNDLES = "RESOURCE_BUNDLES";
    public static final String RESOURCE_BUNDLE_ID = "RESOURCE_BUNDLE_ID";
    public static final String OBJECT_NAME = "OBJECT_NAME";
    public static final String OBJECT_SIZE = "OBJECT_SIZE";
    public static final String FILE_TYPE = "FILE_TYPE";
    public static final String ETAG = "ETAG";
    public static final String LAST_MODIFIED = "LAST_MODIFIED";
    public static final String FILE_SHA256 = "FILE_SHA256";
    public static final String LOADER_ID = "LOADER_ID";
    public static final String ALLOCATION_ID = "ALLOCATION_ID";
    public static final String LOAD_STARTED = "LOAD_STARTED";
    public static final String LOAD_COMPLETED = "LOAD_COMPLETED";
    public static final String FAILURE_COUNT = "FAILURE_COUNT";
    public static final String HTTP_STATUS_CODE = "HTTP_STATUS_CODE";
    public static final String HTTP_STATUS_TEXT = "HTTP_STATUS_TEXT";
    public static final String ROWS_PROCESSED = "ROWS_PROCESSED";
    public static final int HTTP_STATUS_TEXT_LEN = 64;
    
    // Tracking individual loads
    public static final String RESOURCE_BUNDLE_LOADS = "RESOURCE_BUNDLE_LOADS";
    public static final String RESOURCE_BUNDLE_LOAD_ID = "RESOURCE_BUNDLE_LOAD_ID";
    
    // The FHIR resource identities created mapped to the bundles
    public static final String LOGICAL_RESOURCES = "LOGICAL_RESOURCES";
    public static final String LOGICAL_RESOURCE_ID = "LOGICAL_RESOURCE_ID";
    public static final String LOGICAL_ID = "LOGICAL_ID";
    public static final String LINE_NUMBER = "LINE_NUMBER";
    public static final String CREATED_TSTAMP = "CREATED_TSTAMP";
    public static final String RESPONSE_TIME_MS = "RESPONSE_TIME_MS";
    
    // Errors encountered processing a particular resource bundle file
    public static final String RESOURCE_BUNDLE_ERRORS = "RESOURCE_BUNDLE_ERRORS";
    public static final String ERROR_TEXT = "ERROR_TEXT";
    public static final String ERROR_TSTAMP = "ERROR_TSTAMP";
    public static final int ERROR_TEXT_LEN = 1024;
    
    public static final int LOGICAL_ID_BYTES = 64;
    public static final int SHA256_BASE64_BYTES = 44;
    public static final int UUID_LENGTH = 36;
}