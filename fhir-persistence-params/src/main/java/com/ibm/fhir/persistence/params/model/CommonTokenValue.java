/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.params.model;

/**
 * Represents a common_token_value record which may or may not yet exist
 * in the database. If it exists in the database, we may not yet have
 * retrieved its common_token_value_id.
 */
public class CommonTokenValue {
    private final short shardKey;
    private final CodeSystemValue codeSystemValue;
    private final String tokenValue;
    private Long commonTokenValueId;

    /**
     * Public constructor
     * @param shardKey
     * @param codeSystemValue
     * @param tokenValue
     */
    public CommonTokenValue(short shardKey, CodeSystemValue codeSystemValue, String tokenValue) {
        this.shardKey = shardKey;
        this.codeSystemValue = codeSystemValue;
        this.tokenValue = tokenValue;
    }

    /**
     * @return the commonTokenValueId
     */
    public Long getCommonTokenValueId() {
        return commonTokenValueId;
    }

    /**
     * @param commonTokenValueId the commonTokenValueId to set
     */
    public void setCommonTokenValueId(Long commonTokenValueId) {
        this.commonTokenValueId = commonTokenValueId;
    }

    /**
     * @return the codeSystemValue
     */
    public CodeSystemValue getCodeSystemValue() {
        return codeSystemValue;
    }

    /**
     * @return the tokenValue
     */
    public String getTokenValue() {
        return tokenValue;
    }

    /**
     * @return the shardKey
     */
    public short getShardKey() {
        return shardKey;
    }
}