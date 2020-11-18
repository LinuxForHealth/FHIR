/*
 * (C) Copyright IBM Corp. 2017,2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dto;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.search.SearchConstants.Type;
import com.ibm.fhir.search.util.ReferenceValue;

/**
 * DTO representing external and local reference parameters
 */
public class ReferenceParmVal implements ExtractedParameterValue {

    // The resource type name
    private String resourceType;

    // The name of the parameter (key into PARAMETER_NAMES)
    private String name;

    // The reference value
    //private String valueString;

    // The SearchParameter base type. If "Resource", then this is a Resource-level attribute
    private String base;

    // The value of the reference after it has been processed to determine target resource type, version etc.
    private ReferenceValue refValue;

    /**
     * Public constructor
     */
    public ReferenceParmVal() {
        super();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

//    public String getValueString() {
//        return valueString;
//    }

//    public void setValueString(String valueString) {
//        this.valueString = valueString;
//    }

    /**
     * Get the refValue
     * @return
     */
    public ReferenceValue getRefValue() {
        return this.refValue;
    }

    /**
     * Set the refValue
     * @param refValue
     */
    public void setRefValue(ReferenceValue refValue) {
        this.refValue = refValue;
    }

    /**
     * Get the reference type of the parameter (the origin, not the target of the reference)
     */
    public String getResourceType() {
        return resourceType;
    }

    /**
     * Set the reference type of the parameter (the origin, not the target of the reference)
     */
    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public Type getType() {
        return Type.REFERENCE;
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
}