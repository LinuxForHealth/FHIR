/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.index;

import java.util.Objects;

/**
 * A local reference search parameter value
 */
public class ReferenceParameter extends SearchParameterValue {
    private String resourceType;
    private String logicalId;

    // for storing versioned references
    private Integer refVersionId;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Reference[");
        addDescription(result);
        result.append(",");
        result.append(resourceType);
        result.append(",");
        result.append(logicalId);
        result.append(",");
        result.append(refVersionId);
        result.append("]");
        return result.toString();
    }
    
    /**
     * @return the refVersionId
     */
    public Integer getRefVersionId() {
        return refVersionId;
    }

    /**
     * @param refVersionId the refVersionId to set
     */
    public void setRefVersionId(Integer refVersionId) {
        this.refVersionId = refVersionId;
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
     * @return the logicalId
     */
    public String getLogicalId() {
        return logicalId;
    }

    /**
     * @param logicalId the logicalId to set
     */
    public void setLogicalId(String logicalId) {
        this.logicalId = logicalId;
    }

    @Override
    public int hashCode() {
        // yeah I know I could include the super hashCode in the list of values,
        // but this avoids unnecessary boxing
        return 31 * super.hashCode() + Objects.hash(resourceType, logicalId, refVersionId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ReferenceParameter) {
            ReferenceParameter that = (ReferenceParameter)obj;
            return super.equals(obj)
                    && Objects.equals(this.resourceType, that.resourceType)
                    && Objects.equals(this.logicalId, that.logicalId)
                    && Objects.equals(this.refVersionId, that.refVersionId);
        }
        return false;
    }
}