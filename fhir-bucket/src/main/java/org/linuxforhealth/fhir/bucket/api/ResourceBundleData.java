/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.bucket.api;

import java.util.Date;

/**
 * A record from RESOURCE_BUNDLES
 */
public class ResourceBundleData {

    private final long resourceBundleId;
                
    private final long objectSize;
    
    private final FileType fileType;

    private final String eTag;
    
    private final Date lastModified;
    
    private final int version;
    
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
        String eTag, Date lastModified, Date scanTstamp, int version) {
        this.resourceBundleId = resourceBundleId;
        this.objectSize = objectSize;
        this.fileType = ft;
        this.eTag = eTag;
        this.lastModified = lastModified;
        this.scanTstamp = scanTstamp;
        this.version = version;
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

    /**
     * @return the version
     */
    public int getVersion() {
        return version;
    }
}
