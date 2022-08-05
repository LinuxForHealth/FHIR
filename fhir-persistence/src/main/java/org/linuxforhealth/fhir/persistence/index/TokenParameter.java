/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.index;

import java.util.Objects;

/**
 * A token search parameter value
 */
public class TokenParameter extends SearchParameterValue {
    private String valueSystem;
    private String valueCode;

    // for storing versioned references
    private Integer refVersionId;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Token[");
        addDescription(result);
        result.append(",");
        result.append(valueSystem);
        result.append(",");
        result.append(valueCode);
        result.append(",");
        result.append(refVersionId);
        result.append("]");
        return result.toString();
    }
    
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

    @Override
    public int hashCode() {
        // yeah I know I could include the super hashCode in the list of values,
        // but this avoids unnecessary boxing
        return 31 * super.hashCode() + Objects.hash(valueSystem, valueCode, refVersionId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TokenParameter) {
            TokenParameter that = (TokenParameter)obj;
            return super.equals(obj)
                    && Objects.equals(this.valueCode, that.valueCode)
                    && Objects.equals(this.valueSystem, that.valueSystem)
                    && Objects.equals(this.refVersionId, that.refVersionId);
        }
        return false;
    }
}