/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.operation.bulkdata.model.type;

public enum StorageType {
    HTTPS("https"),
    FILE("file"),
    AWSS3("aws-s3"),
    IBMCOS("ibm-cos"),
    AZURE("azure-blob");
    // We don't yet support gcp-bucket.

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