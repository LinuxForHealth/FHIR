/*
 * (C) Copyright IBM Corp. 2019
 * 
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.path;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * This class is part of the implementation for the Types and Reflection section of the FHIRPath specification:
 * @see <a href="http://hl7.org/fhirpath/2018Sep/index.html#types-and-reflection">FHIRPath Types and Reflection</a>
 */
public class TupleTypeInfo implements TypeInfo {
    private final List<TupleTypeInfoElement> element;
    
    public TupleTypeInfo(List<TupleTypeInfoElement> element) {
        this.element = Collections.unmodifiableList(Objects.requireNonNull(element));
    }
    
    public List<TupleTypeInfoElement> getElement() {
        return element;
    }
    
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
        TupleTypeInfo other = (TupleTypeInfo) obj;
        return Objects.equals(element, other.element);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(element);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String nl = System.lineSeparator();
        sb.append("TupleTypeInfo {").append(nl);
        sb.append("  element: {").append(nl);
        for (TupleTypeInfoElement e : element) {
            sb.append("    ").append(e).append(nl);
        }
        sb.append("  }").append(nl);
        sb.append("}").append(nl);
        return sb.toString();
    }
}