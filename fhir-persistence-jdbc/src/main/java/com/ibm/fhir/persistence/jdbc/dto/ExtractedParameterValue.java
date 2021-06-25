/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dto;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * A search parameter value extracted from a resource and ready to store / index for search
 */
public abstract class ExtractedParameterValue {

    // The name (code) of this parameter
    private String name;

    // A subset of search params are also stored at the whole-system level
    private boolean wholeSystem;

    // The resource type associated with this parameter
    private String resourceType;

    // The base resource name
    private String base;

    /**
     * Protected constructor
     */
    protected ExtractedParameterValue() {
    }

    /**
     * Getter for the parameter's resource type
     * @return
     */
    public String getResourceType() {
        return this.resourceType;
    }

    /**
     * Setter for the parameter's resource type
     * @param resourceType
     */
    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    /**
     * We know our type, so we can call the correct method on the visitor
     */
    public abstract void accept(ExtractedParameterValueVisitor visitor) throws FHIRPersistenceException;

    /**
     * @return the base
     */
    public String getBase() {
        return this.base;
    }

    /**
     * @param base the base to set
     */
    public void setBase(String base) {
        this.base = base;
    }

    /**
     * @return the wholeSystem
     */
    public boolean isWholeSystem() {
        return wholeSystem;
    }

    /**
     * @param wholeSystem the wholeSystem to set
     */
    public void setWholeSystem(boolean wholeSystem) {
        this.wholeSystem = wholeSystem;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}