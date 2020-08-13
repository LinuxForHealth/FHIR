/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.persistence;


/**
 * Represents an allocated job to load a bundle
 */
public class BucketLoaderJob {

    private final long resourceBundleId;
    
    private final String bucketName;
    
    private final String bucketPath;
    
    private final String objectName;
    
    private final long objectSize;
    
    /**
     * @param resourceBundleId
     * @param bucketName
     * @param bucketPath
     * @param objectName
     * @param objectSize
     */
    public BucketLoaderJob(long resourceBundleId, String bucketName, String bucketPath, String objectName, long objectSize) {
        this.resourceBundleId = resourceBundleId;
        this.bucketName = bucketName;
        this.bucketPath = bucketPath;
        this.objectName = objectName;
        this.objectSize = objectSize;
    }
    
    @Override
    public String toString() {
        return bucketName + ":" + getObjectKey();
    }
    
    public String getBucketName() {
        return this.bucketName;
    }
    

    /**
     * Get the reconstituted object key which is a concatenation of the bucketPath and
     * the objectName, with some special treatment of objects in the root location because
     * we can't have an empty path (in the database)
     * @return
     */
    public String getObjectKey() {
        if (bucketPath.length() == 1 && bucketPath.charAt(0) == '/') {
            return objectName;
        } else {
            return bucketPath + objectName;
        }
    }
    
    /**
     * @return the objectSize in bytes
     */
    public long getObjectSize() {
        return this.objectSize;
    }
}
