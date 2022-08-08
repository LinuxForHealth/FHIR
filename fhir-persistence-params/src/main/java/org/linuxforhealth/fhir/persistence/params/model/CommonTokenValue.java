/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.params.model;

import java.util.Objects;

/**
 * Represents a common_token_value record which may or may not yet exist
 * in the database. If it exists in the database, we may not yet have
 * retrieved its common_token_value_id.
 */
public class CommonTokenValue implements Comparable<CommonTokenValue> {
    private final short shardKey;
    private final CodeSystemValue codeSystemValue;
    private final String tokenValue;

    // the id gets set after we find/create it in the database
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

    @Override
    public int compareTo(CommonTokenValue that) {
        int result = this.tokenValue.compareTo(that.tokenValue);
        if (0 == result) {
            result = this.codeSystemValue.compareTo(that.codeSystemValue);
            if (0 == result) {
                result = Short.compare(this.shardKey, that.shardKey);
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(shardKey, codeSystemValue.hashCode(), tokenValue);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CommonTokenValue) {
            return 0 == compareTo((CommonTokenValue)obj);
        }
        return false;
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