/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.params.model;

import java.util.Objects;

/**
 * A DTO representing a record from logical_resource_ident
 */
public class LogicalResourceIdentValue implements Comparable<LogicalResourceIdentValue> {
    private final int resourceTypeId;
    private final String resourceType;
    private final String logicalId;
    private Long logicalResourceId;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("'");
        result.append(resourceType);
        result.append("/");
        result.append(logicalId);
        result.append("'");
        result.append(" => ");
        result.append(resourceTypeId);
        result.append("/");
        result.append(logicalResourceId);
        return result.toString();
    }

    /**
     * Builder for fluent creation of LogicalResourceIdentValue objects
     */
    public static class Builder {
        private int resourceTypeId;
        private String resourceType;
        private String logicalId;
        private Long logicalResourceId;

        /**
         * Set the resourceTypeId
         * @param resourceTypeId
         * @return
         */
        public Builder withResourceTypeId(int resourceTypeId) {
            this.resourceTypeId = resourceTypeId;
            return this;
        }

        /**
         * Set the logicalResourceId
         * @param logicalResourceId
         * @return
         */
        public Builder withLogicalResourceId(long logicalResourceId) {
            this.logicalResourceId = logicalResourceId;
            return this;
        }

        /**
         * Set the resourceType
         * @param resourceType
         * @return
         */
        public Builder withResourceType(String resourceType) {
            this.resourceType = resourceType;
            return this;
        }

        /**
         * Set the logicalId
         * @param logicalId
         * @return
         */
        public Builder withLogicalId(String logicalId) {
            this.logicalId = logicalId;
            return this;
        }

        /**
         * Create a new {@link LogicalResourceValue} using the current state of this {@link Builder}
         * @return
         */
        public LogicalResourceIdentValue build() {
            return new LogicalResourceIdentValue(resourceTypeId, resourceType, logicalId, logicalResourceId);
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
     * @param resourceTypeId
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     */
    public LogicalResourceIdentValue(int resourceTypeId, String resourceType, String logicalId, Long logicalResourceId) {
        this.resourceTypeId = resourceTypeId;
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
}