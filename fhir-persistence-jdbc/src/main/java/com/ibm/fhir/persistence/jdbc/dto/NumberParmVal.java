/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dto;

import java.math.BigDecimal;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * This class defines the Data Transfer Object representing a row in the X_NUMBER_VALUES tables.
 */
public class NumberParmVal extends ExtractedParameterValue {

    private BigDecimal valueNumber;
    private BigDecimal valueNumberLow;
    private BigDecimal valueNumberHigh;

    // The SearchParameter base type. If "Resource", then this is a Resource-level attribute
    private String base;

    /**
     * Public constructor
     */
    public NumberParmVal() {
        super();
    }
    public BigDecimal getValueNumber() {
        return valueNumber;
    }

    public void setValueNumber(BigDecimal valueNumber) {
        this.valueNumber = valueNumber;
    }

    public BigDecimal getValueNumberLow() {
        return valueNumberLow;
    }

    public void setValueNumberLow(BigDecimal valueNumberLow) {
        this.valueNumberLow = valueNumberLow;
    }

    public BigDecimal getValueNumberHigh() {
        return valueNumberHigh;
    }

    public void setValueNumberHigh(BigDecimal valueNumberHigh) {
        this.valueNumberHigh = valueNumberHigh;
    }

    /**
     * We know our type, so we can call the correct method on the visitor
     */
    public void accept(ExtractedParameterValueVisitor visitor) throws FHIRPersistenceException {
        visitor.visit(this);
    }
}