/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dto;

import java.util.ArrayList;
import java.util.List;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * This class defines the Data Transfer Object representing a row in the X_DATE_VALUES tables.
 */
public class CompositeParmVal implements ExtractedParameterValue {
    
    private String resourceType;
    private String name;
    private List<ExtractedParameterValue> component;
    
    // The SearchParameter base type. If "Resource", then this is a Resource-level attribute
    private String base;

    public CompositeParmVal() {
        super();
        component = new ArrayList<>(2);
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
        for (ExtractedParameterValue param : component) {
            param.accept(visitor);
        }
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

    /**
     * @return get the list of components in this composite parameter
     */
    public List<ExtractedParameterValue> getComponent() {
        return component;
    }

    /**
     * @param set the list of components in this composite parameter
     */
    public void setComponent(List<ExtractedParameterValue> components) {
        this.component.addAll(components);
    }

    /**
     * @return the second component in this composite parameter
     */
    public void addComponent(ExtractedParameterValue... component) {
        for (ExtractedParameterValue value : component) {
            this.component.add(value);
        }
    }
}
