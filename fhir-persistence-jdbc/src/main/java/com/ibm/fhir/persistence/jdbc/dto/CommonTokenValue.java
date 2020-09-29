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
    
    private final int parameterNameId;

    private final int codeSystemId;
    
    private final String tokenValue;
    
    public CommonTokenValue(int parameterNameId, int codeSystemId, String tokenValue) {
        if (parameterNameId < 0) {
            // Called before the code-system record was created (or fetched from) the database
            throw new IllegalArgumentException("Invalid parameterNameId argument");
        }
        
        if (codeSystemId < 0) {
            // Called before the code-system record was created (or fetched from) the database
            throw new IllegalArgumentException("Invalid codeSystemId argument");
        }
        
        this.parameterNameId = parameterNameId;
        this.codeSystemId = codeSystemId;
        this.tokenValue = tokenValue;
    }

    /**
     * @return the parameterNameId
     */
    public int getParameterNameId() {
        return parameterNameId;
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
        return (Integer.hashCode(parameterNameId) * 37 + Integer.hashCode(codeSystemId)) * 37 + tokenValue.hashCode();
    }
    
    @Override
    public boolean equals(Object other) {
        if (other instanceof CommonTokenValue) {
            CommonTokenValue that = (CommonTokenValue)other;
            return this.parameterNameId == that.parameterNameId
                    && this.codeSystemId == that.codeSystemId
                    && this.tokenValue.equals(that.tokenValue);
        } else {
            return false;
        }
    }
}
