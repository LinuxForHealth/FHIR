/*
 * (C) Copyright IBM Corp. 2019, 2020
 * 
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path;

/**
 * This class is part of the implementation for the Types and Reflection section of the FHIRPath specification:
 * @see <a href="http://hl7.org/fhirpath/2018Sep/index.html#types-and-reflection">FHIRPath Types and Reflection</a>
 */
public interface TypeInfo {
    /**
     * The namespace of this TypeInfo
     * 
     * @return
     *     the namespace of this TypeInfo
     */
    default String getNamespace() {
        return null;
    }
    
    /**
     * The name of this TypeInfo
     * 
     * @return
     *     the name of this TypeInfo
     */
    default String getName() {
        return null;
    }
}
