/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.bulkdata.jbatch.export.fast.data;

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

    private static final long serialVersionUID = 1990637350637543207L;

    // The last_modified timestamp to start scanning from
    private Instant fromLastModified;

    // The upload id for Cos
    private String uploadId;

    // The overall size of the object
    private long currentObjectSize;

    // For tracking the individual results for a multi-part upload
    private List<PartETag> uploadedParts = new ArrayList<>();

    // The COS object name
    private String currentObjectName;

    // The number of resources added to the current object
    private int currentObjectResourceCount;

    // How many objects have we currently uploaded
    private int currentUploadNumber;

    // The resource ids sharing the last timestamp we processed
    private List<Long> resourcesForLastTimestamp = new ArrayList<>();

    // The resource type name associated with the partition
    private String resourceType;

    // A list of how many resources were stored per file
    private List<Integer> resourceCounts = new ArrayList<>();

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
     * @return the currentObjectSize
     */
    public long getCurrentObjectSize() {
        return currentObjectSize;
    }


    /**
     * @param currentObjectSize the currentObjectSize to set
     */
    public void setCurrentObjectSize(long currentObjectSize) {
        this.currentObjectSize = currentObjectSize;
    }


    /**
     * @return the currentObjectName
     */
    public String getCurrentObjectName() {
        return currentObjectName;
    }


    /**
     * @param currentObjectName the currentObjectName to set
     */
    public void setCurrentObjectName(String currentObjectName) {
        this.currentObjectName = currentObjectName;
    }


    /**
     * @return the currentObjectResourceCount
     */
    public int getCurrentObjectResourceCount() {
        return currentObjectResourceCount;
    }


    /**
     * @param currentObjectResourceCount the currentObjectResourceCount to set
     */
    public void setCurrentObjectResourceCount(int currentObjectResourceCount) {
        this.currentObjectResourceCount = currentObjectResourceCount;
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

    /**
     * @return the resourcesForLastTimestamp
     */
    public List<Long> getResourcesForLastTimestamp() {
        return Collections.unmodifiableList(resourcesForLastTimestamp);
    }

    /**
     * @param resourcesForLastTimestamp the resourcesForLastTimestamp to set
     */
    public void setResourcesForLastTimestamp(Collection<Long> resourcesForLastTimestamp) {
        this.resourcesForLastTimestamp.clear();
        this.resourcesForLastTimestamp.addAll(resourcesForLastTimestamp);
    }

    /**
     * Get an immutable list of the resource counts for each file processed so far
     * @return
     */
    public List<Integer> getResourceCounts() {
        return Collections.unmodifiableList(this.resourceCounts);
    }

    public void setResourceCounts(Collection<Integer> resourceCounts) {
        this.resourceCounts.clear();
        this.resourceCounts.addAll(resourceCounts);
    }

    /**
     * @return the resourceType
     */
    public String getResourceType() {
        return resourceType;
    }

    /**
     * @param resourceType the resourceType to set
     */
    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }
}