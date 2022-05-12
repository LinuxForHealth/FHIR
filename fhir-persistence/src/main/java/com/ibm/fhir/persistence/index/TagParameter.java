/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.index;


/**
 * A token search parameter value
 */
public class TagParameter extends SearchParameterValue {
    private String valueSystem;
    private String valueCode;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Tag[");
        addDescription(result);
        result.append(",");
        result.append(valueSystem);
        result.append(",");
        result.append(valueCode);
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
}
