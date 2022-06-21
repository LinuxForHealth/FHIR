/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.index;


/**
 * A string search parameter used for transporting values for remote indexing
 */
public class StringParameter extends SearchParameterValue {
    private String value;
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("String[");
        addDescription(result);
        result.append(",");
        result.append(value);
        result.append("]");
        return result.toString();
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }
    
    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
}
