/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dto;

import java.sql.Timestamp;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * This class defines the Data Transfer Object representing a row in the X_DATE_VALUES tables.
 */
public class DateParmVal implements ExtractedParameterValue {

    private String resourceType;
    private String name;
    private Timestamp valueDateStart;
    private Timestamp valueDateEnd;

    // The SearchParameter base type. If "Resource", then this is a Resource-level attribute
    private String base;

    public enum TimeType {
        YEAR,
        YEAR_MONTH,
        LOCAL_DATE,
        ZONE_DATE,
        UNKNOWN,
        DEFAULT
    }

    public DateParmVal() {
        super();
    }

    public Timestamp getValueDateStart() {
        return valueDateStart;
    }

    public void setValueDateStart(Timestamp valueDateStart) {
        this.valueDateStart = valueDateStart;
    }

    public Timestamp getValueDateEnd() {
        return valueDateEnd;
    }

    public void setValueDateEnd(Timestamp valueDateEnd) {
        this.valueDateEnd = valueDateEnd;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    /**
     * We know our type, so we can call the correct method on the visitor
     */
    public void accept(ExtractedParameterValueVisitor visitor) throws FHIRPersistenceException {
        visitor.visit(this);
    }

    /**
     * @return the base
     */
    public String getBase() {
        return base;
    }

    /**
     * @param base the base to set
     */
    public void setBase(String base) {
        this.base = base;
    }

    @Override
    public String toString() {
        return "DateParmVal [resourceType=" + resourceType + ", name=" + name
                + ", valueDateStart=" + valueDateStart + ", valueDateEnd=" + valueDateEnd + ", base=" + base + "]";
    }
}