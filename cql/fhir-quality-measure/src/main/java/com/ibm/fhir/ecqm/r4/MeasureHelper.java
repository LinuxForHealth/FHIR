/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.ecqm.r4;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.Measure;

/**
 * Utility methods for working with Measure resources
 */
public class MeasureHelper {
    public static String getPrimaryLibraryId(Measure measure) throws FHIROperationException {
        String primaryLibraryId = null;
        if( measure.getLibrary() != null && measure.getLibrary().size() == 1 ) {
            primaryLibraryId = measure.getLibrary().get(0).getValue();
        } else {
            // See https://hl7.org/fhir/us/cqfmeasures/2021May/StructureDefinition-computable-measure-cqfm.html
            throw new FHIROperationException("Measures utilizing CQL SHALL reference one and only one CQL library (and that referenced library MUST be the primary library for the measure)");
        }
        
        return primaryLibraryId;
    }
}
