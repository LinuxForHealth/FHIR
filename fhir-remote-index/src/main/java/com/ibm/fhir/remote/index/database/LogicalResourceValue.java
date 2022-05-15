/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.remote.index.database;

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
    public static class Builder {
        private short shardKey;
        private long logicalResourceId;
        private String resourceType;
        private String logicalId;
        private int versionId;
        private Timestamp lastUpdated;
        private String parameterHash;

        public Builder withShardKey(short shardKey) {
            this.shardKey = shardKey;
            return this;
        }
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
        public Builder withVersionId(int versionId) {
            this.versionId = versionId;
            return this;
        }
        public Builder withLastUpdated(Timestamp lastUpdated) {
            this.lastUpdated = lastUpdated;
            return this;
        }
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
     * Factor function to create a fresh instance of a {@link Builder}
     * @return
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Public constructor
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
