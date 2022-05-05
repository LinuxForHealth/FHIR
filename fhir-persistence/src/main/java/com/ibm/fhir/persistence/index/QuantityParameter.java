/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.index;

import java.math.BigDecimal;

/**
 * A quantity search parameter value
 */
public class QuantityParameter extends SearchParameterValue {
    private BigDecimal valueNumber;
    private BigDecimal valueNumberLow;
    private BigDecimal valueNumberHigh;
    private String valueSystem;
    private String valueCode;

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
}
