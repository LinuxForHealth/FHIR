/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.ecqm.r4;

import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.model.resource.Measure;

/**
 * Utility methods for working with Measure resources
 */
public class MeasureHelper {

    /**
     * Retrieve the primary library ID from a FHIR measure resource. The baseline
     * validation will not catch cases when no reference is provided and we
     * enforce here the rule from the CQF measures IG where exactly one library
     * reference is expected.
     * 
     * @param measure
     *            Measure resource
     * @return primary library ID
     * @throws FHIROperationException
     *             when there are more or less than one
     *             library references in the resource
     */
    public static String getPrimaryLibraryId(Measure measure) throws FHIROperationException {
        String primaryLibraryId = null;
        if (measure.getLibrary() != null && measure.getLibrary().size() == 1) {
            primaryLibraryId = measure.getLibrary().get(0).getValue();
        } else {
            // See https://hl7.org/fhir/us/cqfmeasures/2021May/StructureDefinition-computable-measure-cqfm.html
            throw new FHIROperationException("Measures utilizing CQL SHALL reference one and only one CQL library (and that referenced library MUST be the primary library for the measure)");
        }

        return primaryLibraryId;
    }
}