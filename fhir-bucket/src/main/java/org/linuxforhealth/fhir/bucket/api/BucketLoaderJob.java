/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.bucket.api;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Represents an allocated job to load a bundle
 */
public class BucketLoaderJob {

    private final long resourceBundleLoadId;
    
    private final long resourceBundleId;
    
    private final String bucketName;
    
    private final String bucketPath;
    
    private final String objectName;
    
    private final long objectSize;
    
    private final FileType fileType;
    
    private final int version;

    // true while the file is being processed
    private volatile boolean fileProcessing = true;
    
    // The number of entries read from the object
    private volatile int entryCount;
    
    // The number of entries for which processing has been completed
    private final AtomicInteger completedCount = new AtomicInteger(0);
    
    // How many of the jobs have failed
    private final AtomicInteger failureCount = new AtomicInteger(0);
        
    // Callback when the last resource is processed
    private Consumer<BucketLoaderJob> jobCompleteCallback;
    
    // nanoTime when the job was started
    private long processingStartTime;
    
    // nanoTime when the job and all of its entries completed
    private long processingEndTime;
    
    private long lastCallResponseTime;
    
    // how many resources were generated when processing this job
    private volatile int totalResourceCount = 0;
    
    /**
     * Public constructor
     * @param resourceBundleLoadId
     * @param resourceBundleId
     * @param bucketName
     * @param bucketPath
     * @param objectName
     * @param objectSize
     * @param ft
     * @param version
     */
    public BucketLoaderJob(long resourceBundleLoadId, long resourceBundleId, String bucketName, String bucketPath, String objectName, long objectSize, FileType ft, int version) {
        this.resourceBundleLoadId = resourceBundleLoadId;
        this.resourceBundleId = resourceBundleId;
        this.bucketName = bucketName;
        this.bucketPath = bucketPath;
        this.objectName = objectName;
        this.objectSize = objectSize;
        this.fileType = ft;
        this.version = version;
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
        return bucketName + ":" + getObjectKey() + "[v" + version + "]: " + completedCount.get() + "/" + entryCount + " failed=" + failureCount.get();
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
    public void addEntry() {
        this.entryCount++;
    }

    /**
     * Signal operation complete.
     */
    public void operationComplete(boolean success) {
        // keep track of how many rows in the job failed
        if (!success) {
            failureCount.addAndGet(1);
        }

        // If all the entries are done AND we've completed scanning the file
        if (completedCount.addAndGet(1) == this.entryCount && !this.fileProcessing) {
            this.processingEndTime = System.nanoTime();
            
            if (this.jobCompleteCallback != null) {
                // job is done, so make the callback
                this.jobCompleteCallback.accept(this);
            }
        }
    }
    
    /**
     * Called after the file has been read (but the job is not complete
     * until the completedCount matches the itemCount
     */
    public void fileProcessingComplete() {
        if (this.fileProcessing) {
            this.fileProcessing = false;
            
            // If we've also processed all the registered entries, then
            // we can make the call to signal to mark the job as done
            if (completedCount.get() == this.entryCount) {
                // job is done, so make the call
                this.processingEndTime = System.nanoTime();
                if (this.jobCompleteCallback != null) {
                    this.jobCompleteCallback.accept(this);
                }
            }
            
        } else {
            throw new IllegalStateException("fileProcessingComplete called more than once");
        }
    }

    /**
     * How many rows have failed
     * @return
     */
    public int getFailureCount() {
        return this.failureCount.get();
    }
    
    /**
     * How many rows have been completed so far
     * @return
     */
    public int getCompletedCount() {
        return this.completedCount.get();
    }

    /**
     * @return the entryCount. This value is only meaningful after fileProcessingComplete().
     * If called before, the value may not representative of all the entries in the file
     */
    public int getEntryCount() {
        return this.entryCount;
    }

    /**
     * @return the resourceBundleLoadId
     */
    public long getResourceBundleLoadId() {
        return resourceBundleLoadId;
    }

    /**
     * @return the version
     */
    public int getVersion() {
        return version;
    }

    /**
     * @return the processingStartTime
     */
    public long getProcessingStartTime() {
        return processingStartTime;
    }

    /**
     * @param processingStartTime the processingStartTime to set
     */
    public void setProcessingStartTime(long processingStartTime) {
        this.processingStartTime = processingStartTime;
    }

    /**
     * @return the processingEndTime
     */
    public long getProcessingEndTime() {
        return processingEndTime;
    }

    /**
     * @return the lastCallResponseTime in milliseconds
     */
    public long getLastCallResponseTime() {
        return lastCallResponseTime;
    }

    /**
     * @param lastCallResponseTime the lastCallResponseTime to set in milliseconds
     */
    public void setLastCallResponseTime(long lastCallResponseTime) {
        this.lastCallResponseTime = lastCallResponseTime;
    }

    /**
     * Increment the total number of resources that have been created when
     * processing this job
     * @param resources
     */
    public void addTotalResourceCount(int resources) {
        this.totalResourceCount += resources;
    }

    /**
     * @return the totalResourceCount
     */
    public int getTotalResourceCount() {
        return totalResourceCount;
    }
}
