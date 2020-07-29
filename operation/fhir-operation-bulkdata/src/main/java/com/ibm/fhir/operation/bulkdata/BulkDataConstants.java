/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.operation.bulkdata;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.operation.bulkdata.config.BulkDataConfigUtil;

/**
 * Constants for BulkData operations
 */
public class BulkDataConstants {
    public static final String MEDIA_TYPE_ND_JSON = "application/fhir+ndjson";
    public static final String MEDIA_TYPE_PARQUET = "application/fhir+parquet";

    // Import
    public static final String INPUT_FORMAT = MEDIA_TYPE_ND_JSON;
    public static final List<String> INPUT_FORMATS = Collections.unmodifiableList(Arrays.asList(INPUT_FORMAT));
    public static final List<String> STORAGE_TYPES =
            Collections
                    .unmodifiableList(Arrays.asList(DataSourceStorageType.HTTPS.value, DataSourceStorageType.FILE.value,
                            DataSourceStorageType.AWSS3.value, DataSourceStorageType.IBMCOS.value));
    public static final List<String> STORAGE_CONTENT_ENCODING =
            Collections.unmodifiableList(Arrays.asList("text", "text/plain"));
    public static final int IMPORT_MAX_DEFAULT_INPUTS = 5;

    // Export
    public static final List<String> EXPORT_FORMATS =
            Collections.unmodifiableList(Arrays.asList(
                MEDIA_TYPE_ND_JSON,
                MEDIA_TYPE_PARQUET
            ));
    public static final List<String> NDJSON_VARIANTS =
            Collections.unmodifiableList(Arrays.asList(
                "application/ndjson",
                "ndjson"
            ));

    // Export
    public static final String PARAM_OUTPUT_FORMAT = "_outputFormat";
    public static final String PARAM_SINCE = "_since";
    public static final String PARAM_TYPE = "_type";
    public static final String PARAM_TYPE_FILTER = "_typeFilter";
    public static final String PARAM_GROUP_ID = "groupId";
    public static final String PARAM_JOB = "job";

    // Encryption key used for JavaBatch Job ID
    public static final SecretKeySpec BATCHJOBID_ENCRYPTION_KEY =
            BulkDataConfigUtil.getBatchJobIdEncryptionKey(FHIRConfigHelper
                    .getStringProperty(FHIRConfiguration.PROPERTY_BULKDATA_BATCHJOBID_ENCRYPTION_KEY, null));

    // Status
    public static final List<String> SUCCESS_STATUS = Collections.unmodifiableList(Arrays.asList("COMPLETED"));
    public static final List<String> FAILED_STATUS = Collections.unmodifiableList(Arrays.asList("FAILED", "ABANDONED"));
    public static final List<String> STOPPED_STATUS = Collections.unmodifiableList(Arrays.asList("STOPPED"));

    // Import
    public static final String PARAM_INPUT_FORMAT = "inputFormat";
    public static final String PARAM_INPUT_SOURCE = "inputSource";
    public static final String PARAM_INPUTS = "input";
    public static final String PARAM_STORAGE_DETAIL = "storageDetail";

    /**
     * Search Modifiers
     */
    public enum ExportType {
        SYSTEM, PATIENT, GROUP, INVALID;
    }

    public enum DataSourceStorageType {
        HTTPS("https"), FILE("file"), AWSS3("aws-s3"), IBMCOS("ibm-cos");
        // We don't yet support gcp-bucket, azure-blob.

        private final String value;

        DataSourceStorageType(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }

        public static DataSourceStorageType from(String value) {
            for (DataSourceStorageType c : DataSourceStorageType.values()) {
                if (c.value.equals(value)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(value);
        }
    }

    private BulkDataConstants() {
        // No Operation
    }
}