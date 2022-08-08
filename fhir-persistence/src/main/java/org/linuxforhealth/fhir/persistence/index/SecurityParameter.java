/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.index;

import java.util.Objects;

/**
 * A security search parameter value
 */
public class SecurityParameter extends SearchParameterValue {
    private String valueSystem;
    private String valueCode;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Security[");
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

    @Override
    public int hashCode() {
        // yeah I know I could include the super hashCode in the list of values,
        // but this avoids unnecessary boxing
        return 31 * super.hashCode() + Objects.hash(valueSystem, valueCode);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SecurityParameter) {
            SecurityParameter that = (SecurityParameter)obj;
            return super.equals(obj)
                    && Objects.equals(this.valueCode, that.valueCode)
                    && Objects.equals(this.valueSystem, that.valueSystem);
        }
        return false;
    }
}