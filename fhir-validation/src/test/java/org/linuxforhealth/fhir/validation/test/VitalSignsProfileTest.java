/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.validation.test;

import org.linuxforhealth.fhir.model.resource.StructureDefinition;
import org.linuxforhealth.fhir.profile.ConstraintGenerator;
import org.linuxforhealth.fhir.profile.ProfileSupport;

public class VitalSignsProfileTest {
    private static final String VITAL_SIGNS_PROFILE_URL = "http://hl7.org/fhir/StructureDefinition/vitalsigns";
    public static void main(String[] args) {
        StructureDefinition vitalSignsProfile = ProfileSupport.getProfile(VITAL_SIGNS_PROFILE_URL);
        ConstraintGenerator generator = new ConstraintGenerator(vitalSignsProfile);
        generator.generate().stream().forEach(System.out::println);
    }
}