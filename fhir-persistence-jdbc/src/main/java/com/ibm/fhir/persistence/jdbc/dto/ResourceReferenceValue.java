/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dto;

import java.util.Comparator;

/**
 * DTO representing a resource reference record.
 */
public class ResourceReferenceValue implements Comparable<ResourceReferenceValue> {
    private static final Comparator<String> NULL_SAFE_COMPARATOR = Comparator.nullsFirst(String::compareTo);
    
    private final String resourceType;
    private final int resourceTypeId;

    // the target logicalId...can be null
    private final String logicalId;

    /**
     * Canonical constructor
     * 
     * @param resourceType
     * @param resourceTypeId
     * @param logicalId
     */
    public ResourceReferenceValue(String resourceType, int resourceTypeId, String logicalId) {
        if (resourceTypeId < 0) {
            throw new IllegalArgumentException("Invalid resourceTypeId argument");
        }

        this.resourceType = resourceType;
        this.resourceTypeId = resourceTypeId;
        this.logicalId = logicalId;
    }

    @Override
    public int hashCode() {
        // We don't need to include codeSystem in the hash because codeSystemId is synonymous
        // with codeSystem as far as identity is concerned
        return Integer.hashCode(resourceTypeId) * 37 + (logicalId == null ? 7 : logicalId.hashCode());
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ResourceReferenceValue) {
            ResourceReferenceValue that = (ResourceReferenceValue)other;
            return this.resourceTypeId == that.resourceTypeId
                    && ( this.logicalId == null && that.logicalId == null
                            || this.logicalId != null && this.logicalId.equals(that.logicalId)
                        );
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "[resourceTypeId=" + resourceTypeId + ", logicalId=" + logicalId + "]";
    }

    @Override
    public int compareTo(ResourceReferenceValue other) {
        // allow ResourceReferenceValue objects to be sorted in a deterministic way. Note that
        // we sort on resourceType not resourceTypeId. This is to help avoid deadlocks with
        // Derby
        int result = NULL_SAFE_COMPARATOR.compare(resourceType, other.resourceType);
        if (result == 0) {
            result = NULL_SAFE_COMPARATOR.compare(logicalId, other.logicalId);
        }
        return result;
    }

    
    /**
     * @return the resourceType
     */
    public String getResourceType() {
        return resourceType;
    }

    
    /**
     * @return the resourceTypeId
     */
    public int getResourceTypeId() {
        return resourceTypeId;
    }

    
    /**
     * @return the logicalId
     */
    public String getLogicalId() {
        return logicalId;
    }
}