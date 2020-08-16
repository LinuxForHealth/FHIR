/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.api;

import java.util.Date;

/**
 * A record in the RESOURCE_BUNDLES table
 */
public class ResourceBundleData {

    private final long resourceBundleId;
                
    private final long objectSize;
    
    private final FileType fileType;

    private final String eTag;
    
    private final Date lastModified;
    
    // The tstamp for when this item was most recently updated in the bucket database
    private final Date scanTstamp;
    
    /**
     * @param resourceBundleId
     * @param bucketName
     * @param bucketPath
     * @param objectName
     * @param objectSize
     */
    public ResourceBundleData(long resourceBundleId, long objectSize, FileType ft,
        String eTag, Date lastModified, Date scanTstamp) {
        this.resourceBundleId = resourceBundleId;
        this.objectSize = objectSize;
        this.fileType = ft;
        this.eTag = eTag;
        this.lastModified = lastModified;
        this.scanTstamp = scanTstamp;
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
     * Does this record match exactly the 3 given values
     * @param objectSize
     * @param eTag
     * @param lastModified
     * @return
     */
    public boolean matches(long objectSize, String eTag, Date lastModified) {
        return this.objectSize == objectSize
                && this.eTag.equals(eTag)   
                && this.lastModified.equals(lastModified);
    }

    /**
     * @return the scanTstamp
     */
    public Date getScanTstamp() {
        return scanTstamp;
    }
}
