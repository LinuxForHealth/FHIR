/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.jbatch.bulkdata.load;

public enum BulkImportDataSourceStorageType {
    HTTPS("https"),
    FILE("file"),
    AWSS3("aws-s3"),
    IBMCOS("ibm-cos");

    private final String value;

    BulkImportDataSourceStorageType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static BulkImportDataSourceStorageType from(String value) {
        for (BulkImportDataSourceStorageType c : BulkImportDataSourceStorageType.values()) {
            if (c.value.equals(value)) {
                return c;
            }
        }
        throw new IllegalArgumentException(value);
    }
}
