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

    // Import
    public static final String INPUT_FORMAT = MEDIA_TYPE_ND_JSON;

    // Export
    public static final String EXPORT_FORMAT = MEDIA_TYPE_ND_JSON;
    public static final List<String> EXPORT_FORMATS =
            Collections.unmodifiableList(Arrays.asList(MEDIA_TYPE_ND_JSON, "application/ndjson", "ndjson"));

    // Export
    public static final String PARAM_OUTPUT_FORMAT = "_outputFormat";
    public static final String PARAM_SINCE = "_since";
    public static final String PARAM_TYPE = "_type";
    public static final String PARAM_TYPE_FILTER = "_typeFilter";
    public static final String PARAM_GROUP_ID = "groupId";
    public static final String PARAM_JOB = "job";

    // Status
    public static final List<String> SUCCESS_STATUS = Collections.unmodifiableList(Arrays.asList("COMPLETED"));
    public static final List<String> FAILED_STATUS = Collections.unmodifiableList(Arrays.asList("FAILED", "ABANDONED"));

    /**
     * Search Modifiers
     */
    public enum ExportType {
        SYSTEM,
        PATIENT,
        GROUP,
        INVALID;
    }

    private BulkDataConstants() {
        // No Operation
    }
}