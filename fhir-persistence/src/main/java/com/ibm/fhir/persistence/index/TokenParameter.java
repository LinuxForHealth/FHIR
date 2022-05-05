/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.index;


/**
 * A token search parameter value
 */
public class TokenParameter extends SearchParameterValue {
    private String valueSystem;
    private String valueCode;

    // for storing versioned references
    private Integer refVersionId;
    
    /**
     * @return the valueSystem
     */
    public String getValueSystem() {
        return valueSystem;
    }
    
    /**
     * @param valueSystem the valueSystem to set
     */
    public void setValueSystem(String valueSystem) {
        this.valueSystem = valueSystem;
    }
    
    /**
     * @return the valueCode
     */
    public String getValueCode() {
        return valueCode;
    }
    
    /**
     * @param valueCode the valueCode to set
     */
    public void setValueCode(String valueCode) {
        this.valueCode = valueCode;
    }

    /**
     * @return the refVersionId
     */
    public Integer getRefVersionId() {
        return refVersionId;
    }

    /**
     * @param refVersionId the refVersionId to set
     */
    public void setRefVersionId(Integer refVersionId) {
        this.refVersionId = refVersionId;
    }

}
