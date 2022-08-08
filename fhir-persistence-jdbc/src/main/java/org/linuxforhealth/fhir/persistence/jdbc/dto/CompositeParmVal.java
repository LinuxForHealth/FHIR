/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.dto;

import java.util.ArrayList;
import java.util.List;

import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;

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

    /**
     * We know our type, so we can call the correct method on the visitor
     */
    @Override
    public void accept(ExtractedParameterValueVisitor visitor) throws FHIRPersistenceException {
        visitor.visit(this);
    }

    @Override
    protected int compareToInner(ExtractedParameterValue o) {
        CompositeParmVal other = (CompositeParmVal) o;
        int retVal;

        List<ExtractedParameterValue> thisComponent = this.getComponent();
        List<ExtractedParameterValue> otherComponent = other.getComponent();
        if (thisComponent != null || otherComponent != null) {
            if (thisComponent == null) {
                return -1;
            } else if (otherComponent == null) {
                return 1;
            }
            Integer thisSize = thisComponent.size();
            Integer otherSize = otherComponent.size();
            for (int i=0; i<thisSize && i<otherSize; i++) {
                retVal = thisComponent.get(i).compareTo(otherComponent.get(i));
                if (retVal != 0) {
                    return retVal;
                }
            }
            retVal = thisSize.compareTo(otherSize);
            if (retVal != 0) {
                return retVal;
            }
        }

        return 0;
    }
}