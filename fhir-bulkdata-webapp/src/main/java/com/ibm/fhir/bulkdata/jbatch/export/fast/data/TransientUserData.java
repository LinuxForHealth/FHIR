/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.jbatch.export.fast.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * Used to share state information among partition processing objects
 * such as the ResourcePayloadReader and ExportPartitionCollector. Note
 * that this information is not part of the checkpoint data because it
 * only relates to the current runtime state.
 */
public class TransientUserData implements Serializable {
    private static final long serialVersionUID = -2608183590405549563L;
    private boolean completed;

    // The resource type name associated with the partition
    private String resourceType;

    // A list of how many resources were stored per file
    private List<Integer> resourceCounts = new ArrayList<>();

    /**
     * @return the completed
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * @param completed the completed to set
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
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

    /**
     * Get an immutable list of the resource counts for each file processed so far
     * @return
     */
    public List<Integer> getResourceCounts() {
        return Collections.unmodifiableList(this.resourceCounts);
    }

    /**
     * Set the resourceCounts value to match the given collection
     * @param resourceCounts
     */
    public void setResourceCounts(Collection<Integer> resourceCounts) {
        this.resourceCounts.clear();
        this.resourceCounts.addAll(resourceCounts);
    }

}
