/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.config.s3;

/**
 * There are two types of hosts virtual-host and path.
 * @see https://docs.aws.amazon.com/AmazonS3/latest/userguide/access-bucket-intro.html
 */
public enum S3HostStyle {
    PATH("path"),
    VIRTUAL_HOST("host");

    private final String value;

    S3HostStyle(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static S3HostStyle from(String value) {
        for (S3HostStyle c : S3HostStyle.values()) {
            if (c.value.equals(value)) {
                return c;
            }
        }
        throw new IllegalArgumentException(value);
    }
}
