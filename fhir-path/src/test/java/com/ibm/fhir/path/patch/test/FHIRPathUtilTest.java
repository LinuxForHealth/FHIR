/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.path.patch.test;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.ibm.fhir.model.patch.exception.FHIRPatchException;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Practitioner;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.path.exception.FHIRPathException;
import com.ibm.fhir.path.util.FHIRPathUtil;

/**
 * Tests against the FHIRPathPatch helper methods in FHIRPathUtil
 */
public class FHIRPathUtilTest {
    Bundle bundle = Bundle.builder().type(BundleType.COLLECTION).build();
    Patient patient = Patient.builder().id("test").build();
    Practitioner practitioner = Practitioner.builder().id("test").build();

    @Test
    private void testAddListResource() throws FHIRPathException, FHIRPatchException {
        Patient modifiedPatient = FHIRPathUtil.add(patient, "Patient", "contained", practitioner);
        assertEquals(modifiedPatient.getContained().get(0), practitioner);
    }

    @Test
    private void testAddSingleResource() throws FHIRPathException, FHIRPatchException {
        Entry emptyEntry = Entry.builder().fullUrl(Uri.of("test")).build();

        Bundle modifiedBundle = FHIRPathUtil.add(bundle, "Bundle", "entry", emptyEntry);
        modifiedBundle = FHIRPathUtil.add(modifiedBundle, "Bundle", "entry", emptyEntry);
        modifiedBundle = FHIRPathUtil.add(modifiedBundle, "Bundle.entry[0]", "resource", patient);
        modifiedBundle = FHIRPathUtil.add(modifiedBundle, "Bundle.entry[1]", "resource", practitioner);

        assertEquals(modifiedBundle.getEntry().get(0).getResource(), patient);
        assertEquals(modifiedBundle.getEntry().get(1).getResource(), practitioner);
    }
}
