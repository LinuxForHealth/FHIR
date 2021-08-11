/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dto;


/**
 * DTO representing a record in COMMON_TOKEN_VALUES
 */
public class CommonTokenValue {

    private final int codeSystemId;

    // tokenValue can be null
    private final String tokenValue;

    public CommonTokenValue(int codeSystemId, String tokenValue) {
        if (codeSystemId < 0) {
            // Called before the code-system record was created (or fetched from) the database
            throw new IllegalArgumentException("Invalid codeSystemId argument");
        }

        this.codeSystemId = codeSystemId;
        this.tokenValue = tokenValue;
    }

    /**
     * @return the codeSystemId
     */
    public int getCodeSystemId() {
        return codeSystemId;
    }

    /**
     * @return the tokenValue
     */
    public String getTokenValue() {
        return tokenValue;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(codeSystemId) * 37 + (tokenValue == null ? 7 : tokenValue.hashCode());
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof CommonTokenValue) {
            CommonTokenValue that = (CommonTokenValue)other;
            return this.codeSystemId == that.codeSystemId
                    && ( this.tokenValue == null && that.tokenValue == null
                            || this.tokenValue != null && this.tokenValue.equals(that.tokenValue)
                        );
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "[codeSystemId=" + codeSystemId + ", tokenValue=" + tokenValue + "]";
    }
}