/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.bulkdata.jbatch.export.fast.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.linuxforhealth.fhir.bulkdata.jbatch.export.fast.ExportPartitionAnalyzer;


/**
 * The final status of a job partition. Used to pass data from the partition collector
 * to the partition analyzer
 */
public class PartitionSummary implements Serializable {
    private static final long serialVersionUID = 1087401683164306162L;

    // The resource type name associated with the partition
    private String resourceType;

    // A list of how many resources were stored per file
    private List<Integer> resourceCounts = new ArrayList<>();

    /**
     * Static factory method to create a new {@link PartitionSummary} instance
     * from a TransientUserData object
     * @param tud
     * @return
     */
    public static PartitionSummary from(TransientUserData tud) {
        PartitionSummary result = new PartitionSummary();
        result.resourceCounts.addAll(tud.getResourceCounts());
        result.resourceType = tud.getResourceType();
        return result;
    }

    public String getResourceType() {
        return this.resourceType;
    }

    public List<Integer> getResourceCounts() {
        return Collections.unmodifiableList(this.resourceCounts);
    }

    /**
     * Compute the resource type summary needed by the {@link ExportPartitionAnalyzer}
     * @return the resourceTypeSummary
     */
    public String getResourceTypeSummary() {
        return this.resourceType + "[" + resourceCounts.stream().map(v -> v.toString()).collect(Collectors.joining(",")) + "]";
    }
}
