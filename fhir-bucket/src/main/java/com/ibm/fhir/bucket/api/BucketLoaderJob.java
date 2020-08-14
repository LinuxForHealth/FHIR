/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.api;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Represents an allocated job to load a bundle
 */
public class BucketLoaderJob {

    private final long resourceBundleId;
    
    private final String bucketName;
    
    private final String bucketPath;
    
    private final String objectName;
    
    private final long objectSize;
    
    private final FileType fileType;
    
    // The number of resources read from the object
    private int inflight;
    
    // The number of resources processed
    private final AtomicInteger completedCount = new AtomicInteger(0);
    
    // Callback when the last resource is processed
    private Consumer<BucketLoaderJob> jobCompleteCallback;
    
    /**
     * @param resourceBundleId
     * @param bucketName
     * @param bucketPath
     * @param objectName
     * @param objectSize
     */
    public BucketLoaderJob(long resourceBundleId, String bucketName, String bucketPath, String objectName, long objectSize, FileType ft) {
        this.resourceBundleId = resourceBundleId;
        this.bucketName = bucketName;
        this.bucketPath = bucketPath;
        this.objectName = objectName;
        this.objectSize = objectSize;
        this.fileType = ft;
    }

    /**
     * Register a Consumer to call back when the job is done
     * @param cb
     */
    public void registerCallback(Consumer<BucketLoaderJob> cb) {
        this.jobCompleteCallback = cb;
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

    /**
     * Get the type of file this object represents
     * @return
     */
    public FileType getFileType() {
        return this.fileType;
    }

    /**
     * @return the resourceBundleId
     */
    public long getResourceBundleId() {
        return resourceBundleId;
    }

    /**
     * Increment the number of operations inflight
     */
    public void incInflight() {
        this.inflight++;
    }

    /**
     * Signal operation complete.
     */
    public void operationComplete() {
        if (completedCount.addAndGet(1) == this.inflight && this.jobCompleteCallback != null) {
            // job is done, so make the call
            this.jobCompleteCallback.accept(this);
        }
    }
}
