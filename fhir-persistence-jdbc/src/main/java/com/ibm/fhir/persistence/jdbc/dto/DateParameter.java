/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dto;

import java.sql.Timestamp;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.search.SearchConstants.Type;

/**
 * This class defines the Data Transfer Object representing a row in the X_DATE_VALUES tables.
 */
public class DateParameter implements IParameter {
    
    private long id;
    private long resourceId;
    private Type type;
    private String resourceType;
    private String name;
    private Timestamp valueDate;
    private Timestamp valueDateStart;
    private Timestamp valueDateEnd;
    
    // The SearchParameter base type. If "Resource", then this is a Resource-level attribute
    private String base;
    
    public enum TimeType{
        YEAR,
        YEAR_MONTH,
        LOCAL_DATE,
        ZONE_DATE,
        UNKNOWN,
        DEFAULT
    }

    public DateParameter() {
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

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Timestamp getValueDate() {
        return valueDate;
    }

    public void setValueDate(Timestamp valueDate) {
        this.valueDate = valueDate;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public long getResourceId() {
        return resourceId;
    }

    public void setResourceId(long resourceId) {
        this.resourceId = resourceId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
    
    /**
     * We know our type, so we can call the correct method on the visitor
     */
    public void accept(IParameterVisitor visitor) throws FHIRPersistenceException {
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
}
