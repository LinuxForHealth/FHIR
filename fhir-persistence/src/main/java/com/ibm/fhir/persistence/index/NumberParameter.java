/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.index;

import java.math.BigDecimal;

/**
 * A number search parameter
 */
public class NumberParameter extends SearchParameterValue {
    private BigDecimal value;
    private BigDecimal lowValue;
    private BigDecimal highValue;
    
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
}
