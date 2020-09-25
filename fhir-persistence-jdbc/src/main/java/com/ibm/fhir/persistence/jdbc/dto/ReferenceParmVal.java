/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dto;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.search.SearchConstants.Type;

/**
 * DTO representing external and local reference parameters
 */
public class ReferenceParmVal implements ExtractedParameterValue {
    
    // The resource type name
    private String resourceType;
    
    // The name of the parameter (key into PARAMETER_NAMES)
    private String name;
    
    // The reference value
    private String valueString;
    
    // The system (only for external references, null for local references)
    private String system;
    
    // true if this reference is external, false if local
    private boolean external;
    
    // The SearchParameter base type. If "Resource", then this is a Resource-level attribute
    private String base;

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

    public String getValueString() {
        return valueString;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
    }


    public String getResourceType() {
        return resourceType;
    }

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

    /**
     * @return the system
     */
    public String getSystem() {
        return system;
    }

    /**
     * @param system the system to set
     */
    public void setSystem(String system) {
        this.system = system;
    }

    /**
     * @return the external
     */
    public boolean isExternal() {
        return external;
    }

    /**
     * @param external the external to set
     */
    public void setExternal(boolean external) {
        this.external = external;
    }
}
