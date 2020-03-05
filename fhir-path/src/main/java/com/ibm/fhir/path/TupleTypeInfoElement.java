/*
 * (C) Copyright IBM Corp. 2019, 2020
 * 
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path;

import java.util.Objects;

/**
 * This class is part of the implementation for the Types and Reflection section of the FHIRPath specification:
 * @see <a href="http://hl7.org/fhirpath/2018Sep/index.html#types-and-reflection">FHIRPath Types and Reflection</a>
 */
public class TupleTypeInfoElement {
    private final String name;
    private final String type;
    private final Boolean oneBased;
    
    public TupleTypeInfoElement(String name, String type) {
        this(name, type, null);
    }
    
    public TupleTypeInfoElement(String name, String type, Boolean oneBased) {
        this.name = Objects.requireNonNull(name);
        this.type = Objects.requireNonNull(type);
        this.oneBased = oneBased;
    }
    
    /**
     * The name of this TupleTypeInfoElement
     * 
     * @return
     *     the name of this TupleTypeInfoElement
     */
    public String getName() {
        return name;
    }
    
    /**
     * The type of this TupleTypeInfoElement
     * 
     * @return
     *     the type of this TupleTypeInfoElement
     */
    public String getType() {
        return type;
    }
    
    public Boolean isOneBased() {
        return oneBased;
    }
    
    /**
     * Indicates whether this TupleTypeInfoElement is equal to the parameter
     * 
     * @param obj
     *     the other {@link Object}
     * @return
     *     true if TupleTypeInfoElement is equal to the parameter, otherwise false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        TupleTypeInfoElement other = (TupleTypeInfoElement) obj;
        return Objects.equals(name, other.name) && 
                Objects.equals(type, other.type) && 
                Objects.equals(oneBased, other.oneBased);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        if (oneBased != null) {
            return String.format("TupleTypeInfoElement { name: '%s', type: '%s', oneBased: '%s' }", name, type, oneBased);
        }
        return String.format("TupleTypeInfoElement { name: '%s', type: '%s' }", name, type);
    }
}