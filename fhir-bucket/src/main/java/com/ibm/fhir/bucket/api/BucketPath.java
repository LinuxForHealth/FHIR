/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.api;


/**
 * A combination of a bucket and a path prefix within that bucket
 */
public class BucketPath {

    private final String bucketName;
    
    private final String pathPrefix;
    
    public BucketPath(String bucketName, String pathPrefix) {
        this.bucketName = bucketName;
        this.pathPrefix = pathPrefix;
    }

    /**
     * @return the bucketName
     */
    public String getBucketName() {
        return bucketName;
    }

    /**
     * @return the pathPrefix
     */
    public String getPathPrefix() {
        return pathPrefix;
    }
    
}
