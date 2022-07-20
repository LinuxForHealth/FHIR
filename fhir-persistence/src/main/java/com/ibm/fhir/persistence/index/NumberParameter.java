/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.index;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * A number search parameter
 */
public class NumberParameter extends SearchParameterValue {
    private BigDecimal value;
    private BigDecimal lowValue;
    private BigDecimal highValue;
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Number[");
        addDescription(result);
        result.append(",");
        result.append(value);
        result.append(",");
        result.append(lowValue);
        result.append(",");
        result.append(highValue);
        result.append("]");
        return result.toString();
    }

    /**
     * @return the value
     */
    public BigDecimal getValue() {
        return value;
    }
    
    /**
     * @param value the value to set
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }
    
    /**
     * @return the lowValue
     */
    public BigDecimal getLowValue() {
        return lowValue;
    }
    
    /**
     * @param lowValue the lowValue to set
     */
    public void setLowValue(BigDecimal lowValue) {
        this.lowValue = lowValue;
    }
    
    /**
     * @return the highValue
     */
    public BigDecimal getHighValue() {
        return highValue;
    }
    
    /**
     * @param highValue the highValue to set
     */
    public void setHighValue(BigDecimal highValue) {
        this.highValue = highValue;
    }

    @Override
    public int hashCode() {
        // yeah I know I could include the super hashCode in the list of values,
        // but this avoids unnecessary boxing
        return 31 * super.hashCode() + Objects.hash(value, lowValue, highValue);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NumberParameter) {
            NumberParameter that = (NumberParameter)obj;
            return super.equals(obj)
                    && Objects.equals(this.value, that.value)
                    && Objects.equals(this.lowValue, that.lowValue)
                    && Objects.equals(this.highValue, that.highValue);
        }
        return false;
    }
}
