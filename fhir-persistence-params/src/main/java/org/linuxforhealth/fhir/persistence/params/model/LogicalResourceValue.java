/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.params.model;

import java.sql.Timestamp;

/**
 * A DTO representing a record from logical_resources
 */
public class LogicalResourceValue {
    private final short shardKey;
    private final long logicalResourceId;
    private final String resourceType;
    private final String logicalId;
    private final int versionId;
    private final Timestamp lastUpdated;
    private final String parameterHash;

    /**
     * Builder for fluent creation of LogicalResourceValue objects
     */
    public static class Builder {
        private short shardKey;
        private long logicalResourceId;
        private String resourceType;
        private String logicalId;
        private int versionId;
        private Timestamp lastUpdated;
        private String parameterHash;

        /**
         * Set the shardKey
         * @param shardKey
         * @return
         */
        public Builder withShardKey(short shardKey) {
            this.shardKey = shardKey;
            return this;
        }

        /**
         * Set the logicalResourceId value
         * @param logicalResourceId
         * @return
         */
        public Builder withLogicalResourceId(long logicalResourceId) {
            this.logicalResourceId = logicalResourceId;
            return this;
        }

        /**
         * Set the resourceType value
         * @param resourceType
         * @return
         */
        public Builder withResourceType(String resourceType) {
            this.resourceType = resourceType;
            return this;
        }

        /**
         * Set the logicalId value
         * @param logicalId
         * @return
         */
        public Builder withLogicalId(String logicalId) {
            this.logicalId = logicalId;
            return this;
        }

        /**
         * Set the versionId value
         * @param versionId
         * @return
         */
        public Builder withVersionId(int versionId) {
            this.versionId = versionId;
            return this;
        }

        /**
         * Set the lastUpdated value
         * @param lastUpdated
         * @return
         */
        public Builder withLastUpdated(Timestamp lastUpdated) {
            this.lastUpdated = lastUpdated;
            return this;
        }

        /**
         * Set the parameterHash value
         * @param parameterHash
         * @return
         */
        public Builder withParameterHash(String parameterHash) {
            this.parameterHash = parameterHash;
            return this;
        }

        /**
         * Create a new {@link LogicalResourceValue} using the current state of this {@link Builder}
         * @return
         */
        public LogicalResourceValue build() {
            return new LogicalResourceValue(shardKey, logicalResourceId, resourceType, logicalId, versionId, lastUpdated, parameterHash);
        }
    }

    /**
     * Factory function to create a fresh instance of a {@link Builder}
     * @return
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Canonical constructor
     * 
     * @param shardKey
     * @param logicalResourceId
     * @param resourceType
     * @param logicalId
     * @param lastUpdated
     * @param parameterHash
     */
    public LogicalResourceValue(short shardKey, long logicalResourceId, String resourceType, String logicalId, int versionId, Timestamp lastUpdated, String parameterHash) {
        this.shardKey = shardKey;
        this.logicalResourceId = logicalResourceId;
        this.resourceType = resourceType;
        this.logicalId = logicalId;
        this.versionId = versionId;
        this.lastUpdated = lastUpdated;
        this.parameterHash = parameterHash;
    }

    
    /**
     * @return the shardKey
     */
    public short getShardKey() {
        return shardKey;
    }

    
    /**
     * @return the logicalResourceId
     */
    public long getLogicalResourceId() {
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
     * @return the lastUpdated
     */
    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    
    /**
     * @return the parameterHash
     */
    public String getParameterHash() {
        return parameterHash;
    }

    /**
     * @return the versionId
     */
    public int getVersionId() {
        return versionId;
    }

}
