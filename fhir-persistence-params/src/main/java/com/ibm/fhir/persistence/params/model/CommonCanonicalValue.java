/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.params.model;

/**
 * Represents a common_canonical_value record which may or may not yet exist
 * in the database. If it exists in the database, we may not yet have
 * retrieved its canonical_id.
 */
public class CommonCanonicalValue {
    private final short shardKey;
    private final String url;
    private Long canonicalId;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(shardKey);
        result.append(",");
        result.append(url);
        result.append(",");
        result.append(canonicalId);
        return result.toString();
    }
    /**
     * Public constructor
     * @param shardKey
     * @param codeSystemValue
     * @param tokenValue
     */
    public CommonCanonicalValue(short shardKey, String url) {
        this.shardKey = shardKey;
        this.url = url;
    }

    /**
     * @return the canonicalId
     */
    public Long getCanonicalId() {
        return canonicalId;
    }

    /**
     * @param canonicalId the canonicalId to set
     */
    public void setCanonicalId(Long canonicalId) {
        this.canonicalId = canonicalId;
    }

    /**
     * @return the shardKey
     */
    public short getShardKey() {
        return shardKey;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }
}