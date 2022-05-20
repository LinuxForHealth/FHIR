/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.remote.index.database;

import java.util.Objects;

/**
 * A DTO representing a record from logical_resource_ident
 */
public class LogicalResourceIdentValue implements Comparable<LogicalResourceIdentValue> {
    private final String resourceType;
    private final String logicalId;
    private Long logicalResourceId;
    private Integer resourceTypeId;

    public static class Builder {
        private String resourceType;
        private String logicalId;
        private Long logicalResourceId;

        public Builder withLogicalResourceId(long logicalResourceId) {
            this.logicalResourceId = logicalResourceId;
            return this;
        }
        public Builder withResourceType(String resourceType) {
            this.resourceType = resourceType;
            return this;
        }
        public Builder withLogicalId(String logicalId) {
            this.logicalId = logicalId;
            return this;
        }

        /**
         * Create a new {@link LogicalResourceValue} using the current state of this {@link Builder}
         * @return
         */
        public LogicalResourceIdentValue build() {
            return new LogicalResourceIdentValue(resourceType, logicalId, logicalResourceId);
        }
    }

    /**
     * Factor function to create a fresh instance of a {@link Builder}
     * @return
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Public constructor
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     */
    public LogicalResourceIdentValue(String resourceType, String logicalId, Long logicalResourceId) {
        this.resourceType = resourceType;
        this.logicalId = Objects.requireNonNull(logicalId);
        this.logicalResourceId = logicalResourceId;
    }

    /**
     * @return the logicalResourceId
     */
    public Long getLogicalResourceId() {
        return logicalResourceId;
    }

    /**
     * @return the resourceType
     */
    public String getResourceType() {
        return resourceType;
    }

    /**
     * @return the logicalId
     */
    public String getLogicalId() {
        return logicalId;
    }

    /**
     * Set the logicalResourceId value
     * @param logicalResourceId
     */
    public void setLogicalResourceId(Long logicalResourceId) {
        this.logicalResourceId = logicalResourceId;
    }

    @Override
    public int compareTo(LogicalResourceIdentValue that) {
        int result = this.resourceType.compareTo(that.resourceType);
        if (0 == result) {
            result = this.logicalId.compareTo(that.logicalId);
        }
        return result;
    }

    /**
     * @return the resourceTypeId
     */
    public Integer getResourceTypeId() {
        return resourceTypeId;
    }

    /**
     * @param resourceTypeId the resourceTypeId to set
     */
    public void setResourceTypeId(Integer resourceTypeId) {
        this.resourceTypeId = resourceTypeId;
    }
}