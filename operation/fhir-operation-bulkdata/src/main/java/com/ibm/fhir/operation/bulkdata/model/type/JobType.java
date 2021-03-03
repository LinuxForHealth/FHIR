/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.model.type;

/**
 * List of the Jobs
 */
public enum JobType {

    EXPORT_PATIENT("FhirBulkExportPatientChunkJob"),
    EXPORT_GROUP("FhirBulkExportGroupChunkJob"),
    EXPORT("FhirBulkExportChunkJob"),
    EXPORT_FAST("FhirBulkExportFastJob"),
    IMPORT("FhirBulkImportChunkJob");

    private final String value;

    JobType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static JobType from(String value) {
        for (JobType c : JobType.values()) {
            if (c.value.equals(value)) {
                return c;
            }
        }
        throw new IllegalArgumentException(value);
    }
}