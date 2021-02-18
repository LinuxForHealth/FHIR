/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.model.type;

public enum StorageType {
    HTTPS("https"),
    FILE("file"),
    AWSS3("aws-s3"),
    IBMCOS("ibm-cos");
    // We don't yet support gcp-bucket, azure-blob.

    private final String value;

    StorageType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static StorageType from(String value) {
        for (StorageType c : StorageType.values()) {
            if (c.value.equals(value)) {
                return c;
            }
        }
        throw new IllegalArgumentException(value);
    }
}
