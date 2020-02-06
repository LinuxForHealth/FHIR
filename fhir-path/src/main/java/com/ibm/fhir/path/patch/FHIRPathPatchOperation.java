/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.patch;

import com.ibm.fhir.model.patch.FHIRPatch;
import com.ibm.fhir.model.resource.Parameters.Parameter;

abstract class FHIRPathPatchOperation implements FHIRPatch {
    public static final String OPERATION = "operation";
    
    public static final String TYPE = "type";
    public static final String PATH = "path";
    public static final String NAME = "name";
    public static final String VALUE = "value";
    public static final String INDEX = "index";
    public static final String SOURCE = "source";
    public static final String DESTINATION = "destination";
    
    /**
     * Infer the element name from a given fhirPath
     * 
     * @param fhirPath
     *            A "simple" fhirpath expression with no functions or operations
     * @return the elementName of the element that the given path would select
     */
    protected String getElementName(String fhirPath) {
        String[] segments = fhirPath.split("\\.");
        String lastSegment = segments[segments.length - 1];
        if (lastSegment.contains("[")) {
            return lastSegment.substring(0, lastSegment.indexOf("["));
        }
        return lastSegment;
    }

    /**
     * Convert the FHIRPathPatchOperation to a Parameters.Parameter element
     */
    public abstract Parameter toParameter();
}