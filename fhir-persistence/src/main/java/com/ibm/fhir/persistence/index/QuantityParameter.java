/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.index;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * A quantity search parameter value
 */
public class QuantityParameter extends SearchParameterValue {
    private BigDecimal valueNumber;
    private BigDecimal valueNumberLow;
    private BigDecimal valueNumberHigh;
    private String valueSystem;
    private String valueCode;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Quantity[");
        addDescription(result);
        result.append(",");
        result.append(valueNumber);
        result.append(",");
        result.append(valueNumberLow);
        result.append(",");
        result.append(valueNumberHigh);
        result.append(",");
        result.append(valueSystem);
        result.append(",");
        result.append(valueCode);
        result.append("]");
        return result.toString();
    }

    /**
     * @return the valueNumber
     */
    public BigDecimal getValueNumber() {
        return valueNumber;
    }
    
    /**
     * @param valueNumber the valueNumber to set
     */
    public void setValueNumber(BigDecimal valueNumber) {
        this.valueNumber = valueNumber;
    }
    
    /**
     * @return the valueNumberLow
     */
    public BigDecimal getValueNumberLow() {
        return valueNumberLow;
    }
    
    /**
     * @param valueNumberLow the valueNumberLow to set
     */
    public void setValueNumberLow(BigDecimal valueNumberLow) {
        this.valueNumberLow = valueNumberLow;
    }
    
    /**
     * @return the valueNumberHigh
     */
    public BigDecimal getValueNumberHigh() {
        return valueNumberHigh;
    }
    
    /**
     * @param valueNumberHigh the valueNumberHigh to set
     */
    public void setValueNumberHigh(BigDecimal valueNumberHigh) {
        this.valueNumberHigh = valueNumberHigh;
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
        return 31 * super.hashCode() + Objects.hash(valueNumber, valueNumberLow, valueNumberHigh, valueCode, valueSystem);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof QuantityParameter) {
            QuantityParameter that = (QuantityParameter)obj;
            return super.equals(obj)
                    && Objects.equals(this.valueNumber, that.valueNumber)
                    && Objects.equals(this.valueNumberLow, that.valueNumberLow)
                    && Objects.equals(this.valueNumberHigh, that.valueNumberHigh)
                    && Objects.equals(this.valueCode, that.valueCode)
                    && Objects.equals(this.valueSystem, that.valueSystem);
        }
        return false;
    }
}
