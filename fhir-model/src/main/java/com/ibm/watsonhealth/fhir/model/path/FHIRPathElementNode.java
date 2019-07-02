/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path;

import java.util.Collection;

import com.ibm.watsonhealth.fhir.model.type.Element;

public class FHIRPathElementNode implements FHIRPathNode {
    private final String name;
    private final Element element;
    private final FHIRPathType type;
    
    public FHIRPathElementNode(String name, Element element) {
        this.name = name;
        this.element = element;        
        type = FHIRPathType.from("FHIR", element.getClass().getSimpleName());
    }
    
    public Element element() {
        return element;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public FHIRPathType type() {
        return type;
    }

    @Override
    public boolean hasValue() {
        return false;
    }

    @Override
    public FHIRPathNode getValue() {
        return null;
    }

    @Override
    public Collection<FHIRPathNode> children() {
        return null;
    }
}
