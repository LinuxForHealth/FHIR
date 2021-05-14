/*
 * (C) Copyright IBM Corp. 2017,2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dto;

import java.math.BigDecimal;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.JDBCConstants;

/**
 * This class defines the Data Transfer Object representing a row in the X_QUANTITY_VALUES tables.
 */
public class QuantityParmVal implements ExtractedParameterValue {

    private String resourceType;
    private String name;
    private BigDecimal valueNumber;
    private BigDecimal valueNumberLow;
    private BigDecimal valueNumberHigh;
    private String valueSystem;
    private String valueCode;

    // The SearchParameter base type. If "Resource", then this is a Resource-level attribute
    private String base;

    public QuantityParmVal() {
        super();
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public BigDecimal getValueNumber() {
        return valueNumber;
    }

    public void setValueNumber(BigDecimal valueNumber) {
        this.valueNumber = valueNumber;
    }

    public String getValueSystem() {
        if (valueSystem == null) {
            return JDBCConstants.DEFAULT_TOKEN_SYSTEM;
        }
        return valueSystem;
    }

    public void setValueSystem(String valueSystem) {
        this.valueSystem = valueSystem;
    }

    public String getValueCode() {
        // X_QUANTITY_VALUES tables have non-nullable CODE column, so use empty string for no code
        if (valueCode == null) {
            return "";
        }
        return valueCode;
    }

    public void setValueCode(String valueCode) {
        this.valueCode = valueCode;
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

    @Override
    public String getResourceType() {
        return resourceType;
    }

    @Override
    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    /**
     * We know our type, so we can call the correct method on the visitor
     */
    @Override
    public void accept(ExtractedParameterValueVisitor visitor) throws FHIRPersistenceException {
        visitor.visit(this);
    }

    /**
     * @return the base
     */
    @Override
    public String getBase() {
        return base;
    }

    /**
     * @param base the base to set
     */
    @Override
    public void setBase(String base) {
        this.base = base;
    }
}
