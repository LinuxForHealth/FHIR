/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dto;

import java.util.ArrayList;
import java.util.List;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * This class defines the Data Transfer Object representing a composite parameter.
 */
public class CompositeParmVal extends ExtractedParameterValue {

    private List<ExtractedParameterValue> component;

    /**
     * Public constructor
     */
    public CompositeParmVal() {
        component = new ArrayList<>(2);
    }

    /**
     * We know our type, so we can call the correct method on the visitor
     */
    public void accept(ExtractedParameterValueVisitor visitor) throws FHIRPersistenceException {
        visitor.visit(this);
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