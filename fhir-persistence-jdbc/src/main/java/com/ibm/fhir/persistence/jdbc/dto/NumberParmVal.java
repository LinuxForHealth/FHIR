/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dto;

import java.math.BigDecimal;
import java.util.Objects;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.util.ParameterHashUtil;

/**
 * This class defines the Data Transfer Object representing a row in the X_NUMBER_VALUES tables.
 */
public class NumberParmVal extends ExtractedParameterValue {

    private BigDecimal valueNumber;
    private BigDecimal valueNumberLow;
    private BigDecimal valueNumberHigh;

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
    @Override
    public void accept(ExtractedParameterValueVisitor visitor) throws FHIRPersistenceException {
        visitor.visit(this);
    }

    @Override
    public String getHash(ParameterHashUtil parameterHashUtil) {
        StringBuilder sb = new StringBuilder();
        sb.append(Objects.toString(valueNumber, ""));
        sb.append("|").append(Objects.toString(valueNumberLow, ""));
        sb.append("|").append(Objects.toString(valueNumberHigh, ""));
        return parameterHashUtil.getNameValueHash(getHashHeader(), sb.toString());
    }
}