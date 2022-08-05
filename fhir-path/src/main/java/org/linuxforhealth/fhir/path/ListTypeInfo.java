/*
 * (C) Copyright IBM Corp. 2019, 2020
 * 
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.path;

import java.util.Objects;

/**
 * This class is part of the implementation for the Types and Reflection section of the FHIRPath specification:
 * @see <a href="http://hl7.org/fhirpath/2018Sep/index.html#types-and-reflection">FHIRPath Types and Reflection</a>
 */
public class ListTypeInfo implements TypeInfo {
    private final String elementType;
    
    public ListTypeInfo(String elementType) {
        this.elementType = Objects.requireNonNull(elementType);
    }
    
    /**
     * The element type of this ListTypeInfo
     * 
     * @return
     *     the element type of this ListTypeInfo
     */
    public String getElementType() {
        return elementType;
    }
}
