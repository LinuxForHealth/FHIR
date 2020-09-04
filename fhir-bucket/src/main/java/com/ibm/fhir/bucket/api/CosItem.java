/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.api;

import java.util.Date;

/**
 * Represents an item found in a COS bucket
 */
public class CosItem {
    // The name of the bucket holding the item
    private final String bucketName;

    // The name (key) of the object in the bucket
    private final String itemName;
    
    // size of the object in bytes
    private final long size;
    
    private final FileType fileType;
    
    // The hash of the object according to COS
    private final String eTag;
    
    // The timestamp when this object was last modified according to COS
    private final Date lastModified;
        
    // The database assigned identifier
    private long resourceBucketId;
    
    /**
     * Public constructor
     * @param bucketName
     * @param itemName
     * @param size
     * @param fileType
     */
    public CosItem(String bucketName, String itemName, long size, FileType fileType, String eTag, Date lastModified) {
        this.bucketName = bucketName;
        this.itemName = itemName;
        this.size = size;
        this.fileType = fileType;
        this.eTag = eTag;
        this.lastModified = lastModified;
    }
    
    @Override
    public String toString() {
        return bucketName + ":" + itemName + "[" + size + "]";
    }

    /**
     * @return the bucketName
     */
    public String getBucketName() {
        return bucketName;
    }

    
    /**
     * @return the itemName
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * @return the size
     */
    public long getSize() {
        return this.size;
    }

    /**
     * @return the fileType
     */
    public FileType getFileType() {
        return this.fileType;
    }

    /**
     * @return the eTag
     */
    public String geteTag() {
        return eTag;
    }

    /**
     * @return the lastModified
     */
    public Date getLastModified() {
        return lastModified;
    }

    /**
     * @return the resourceBucketId
     */
    public long getResourceBucketId() {
        return resourceBucketId;
    }

    /**
     * @param resourceBucketId the resourceBucketId to set
     */
    public void setResourceBucketId(long resourceBucketId) {
        this.resourceBucketId = resourceBucketId;
    }
}
