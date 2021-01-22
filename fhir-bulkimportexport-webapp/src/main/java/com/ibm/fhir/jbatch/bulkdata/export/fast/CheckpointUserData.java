/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.jbatch.bulkdata.export.fast;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.ibm.cloud.objectstorage.services.s3.model.PartETag;

/**
 * Checkpoint data representing the state of the basic system export job
 */
public class CheckpointUserData implements Serializable {
    private static final long serialVersionUID = 4685659616255803732L;

    // The last_modified timestamp to start scanning from
    private Instant fromLastModified;

    // The last resource id we read
    private Long fromResourceId;

    // The upload id for Cos
    private String uploadId;

    // The overall size of the item
    private long currentItemSize;

    // For tracking the individual results for a multi-part upload
    private List<PartETag> uploadedParts = new ArrayList<>();

    // The COS item name
    private String currentItemName;

    // The number of resources added to the current
    private int currentItemResourceCount;

    // How many items have we currently uploaded
    private int currentUploadNumber;


    /**
     * @return the fromLastModified
     */
    public Instant getFromLastModified() {
        return fromLastModified;
    }


    /**
     * @param fromLastModified the fromLastModified to set
     */
    public void setFromLastModified(Instant fromLastModified) {
        this.fromLastModified = fromLastModified;
    }


    /**
     * @return the fromResourceId
     */
    public Long getFromResourceId() {
        return fromResourceId;
    }


    /**
     * @param fromResourceId the fromResourceId to set
     */
    public void setFromResourceId(Long fromResourceId) {
        this.fromResourceId = fromResourceId;
    }


    /**
     * @return the uploadId
     */
    public String getUploadId() {
        return uploadId;
    }


    /**
     * @param uploadId the uploadId to set
     */
    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    /**
     * Getter for an immutable version of the upload result list
     * @return a list of {@link PartETag} objects
     */
    public List<PartETag> getUploadedParts() {
        return Collections.unmodifiableList(this.uploadedParts);
    }

    /**
     * Replace uploadedParts with values from the given parts collection
     * @param parts
     */
    public void setUploadedParts(Collection<PartETag> parts) {
        this.uploadedParts.clear();
        this.uploadedParts.addAll(parts);
    }

    /**
     * @return the currentItemSize
     */
    public long getCurrentItemSize() {
        return currentItemSize;
    }


    /**
     * @param currentItemSize the currentItemSize to set
     */
    public void setCurrentItemSize(long currentItemSize) {
        this.currentItemSize = currentItemSize;
    }


    /**
     * @return the currentItemName
     */
    public String getCurrentItemName() {
        return currentItemName;
    }


    /**
     * @param currentItemName the currentItemName to set
     */
    public void setCurrentItemName(String currentItemName) {
        this.currentItemName = currentItemName;
    }


    /**
     * @return the currentItemResourceCount
     */
    public int getCurrentItemResourceCount() {
        return currentItemResourceCount;
    }


    /**
     * @param currentItemResourceCount the currentItemResourceCount to set
     */
    public void setCurrentItemResourceCount(int currentItemResourceCount) {
        this.currentItemResourceCount = currentItemResourceCount;
    }


    /**
     * @return the currentUploadNumber
     */
    public int getCurrentUploadNumber() {
        return currentUploadNumber;
    }


    /**
     * @param currentUploadNumber the currentUploadNumber to set
     */
    public void setCurrentUploadNumber(int currentUploadNumber) {
        this.currentUploadNumber = currentUploadNumber;
    }
}