/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dto;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.search.SearchConstants.Type;

/**
 * This class defines the Data Transfer Object representing a row in the X_LATLNG_VALUES tables.
 */
public class LocationParameter implements IParameter {
    
    private long id;
    private long resourceId;
    private Type type;
    private String resourceType;
    private String name;
    private Double valueLongitude;
    private Double valueLatitude;
    
    // The SearchParameter base type. If "Resource", then this is a Resource-level attribute
    private String base;
    
    public LocationParameter() {
        super();
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

    public Double getValueLongitude() {
        return valueLongitude;
    }

    public void setValueLongitude(Double valueLongitude) {
        this.valueLongitude = valueLongitude;
    }

    public Double getValueLatitude() {
        return valueLatitude;
    }

    public void setValueLatitude(Double valueLatitude) {
        this.valueLatitude = valueLatitude;
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
