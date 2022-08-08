/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.util;

import java.util.List;

import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.type.Reference;

/**
 * This class contains utility functions for finding "reference" fields within a FHIR resource.
 */
public class ReferenceFinder {
    /**
     * Returns a list of Reference fields that are found in the 
     * specified resource instance.  For example, an Observation has a "subject" field of type
     * Reference, so if this method is called on an Observation instance that has the "subject" field
     * set, then we'll return the Reference instance that represents this "subject" field 
     * @param resource the FHIR resource for which we want to find all Reference fields
     * @return list of Reference instances found in the specified FHIR resource
     * @throws Exception
     */
    public static List<Reference> getReferences(Resource resource) throws Exception {
        CollectingVisitor<Reference> visitor = new CollectingVisitor<Reference>(Reference.class);
        resource.accept(visitor);
        return visitor.getResult();
    }
}
