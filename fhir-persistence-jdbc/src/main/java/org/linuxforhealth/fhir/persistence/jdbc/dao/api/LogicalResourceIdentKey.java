/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.dao.api;

import java.util.Objects;

/**
 * A DTO representing a mapping of a logical_resource identity to its database
 * logical_resource_id value.
 * @implNote use record in Java 17
 */
public class LogicalResourceIdentKey {

    private final int resourceTypeId;
    private final String logicalId;

    /**
     * Public constructor
     * @param resourceTypeId
     * @param logicalId
     */
    public LogicalResourceIdentKey(int resourceTypeId, String logicalId) {
        this.resourceTypeId = resourceTypeId;
        this.logicalId = logicalId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof LogicalResourceIdentKey) {
            LogicalResourceIdentKey that = (LogicalResourceIdentKey)obj;
            return this.resourceTypeId == that.resourceTypeId
                    && this.logicalId.equals(that.logicalId);
        }

        throw new IllegalArgumentException("invalid type");
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.resourceTypeId, this.logicalId);
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
