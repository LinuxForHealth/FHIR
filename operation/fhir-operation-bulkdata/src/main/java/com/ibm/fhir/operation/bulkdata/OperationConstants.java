/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.operation.bulkdata.model.type.StorageType;

/**
 * The Bulk Data Import Export Constants are used to control the Operaiton behavior.
 */
public class OperationConstants {
    // Import
    public static final String INPUT_FORMAT = FHIRMediaType.APPLICATION_NDJSON;
    public static final List<String> INPUT_FORMATS = Collections.unmodifiableList(Arrays.asList(INPUT_FORMAT));
    public static final List<String> STORAGE_TYPES =
            Collections
                    .unmodifiableList(Arrays.asList(StorageType.HTTPS.value(), StorageType.FILE.value(),
                            StorageType.AWSS3.value(), StorageType.IBMCOS.value(),
                            StorageType.AZURE.value()));
    public static final List<String> STORAGE_CONTENT_ENCODING =
            Collections.unmodifiableList(Arrays.asList("text", "text/plain"));
    public static final int IMPORT_MAX_DEFAULT_INPUTS = 5;

    // Export
    public static final List<String> EXPORT_FORMATS =
            Collections.unmodifiableList(Arrays.asList(
                FHIRMediaType.APPLICATION_NDJSON,
                FHIRMediaType.APPLICATION_PARQUET
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

    // Status
    public static final List<String> SUCCESS_STATUS = Collections.unmodifiableList(Arrays.asList("COMPLETED"));
    public static final List<String> RUNNING_STATUS = Collections.unmodifiableList(Arrays.asList("STARTING", "STARTED"));
    public static final List<String> FAILED_STATUS = Collections.unmodifiableList(Arrays.asList("FAILED", "ABANDONED"));
    public static final List<String> STOPPED_STATUS = Collections.unmodifiableList(Arrays.asList("STOPPED"));

    public static final String FAILED_BAD_SOURCE = "FAILED_BAD_SOURCE";
    public static final String NO_SUCH_BUCKET = "NO_SUCH_BUCKET";

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

    private OperationConstants() {
        // No Operation
    }
}