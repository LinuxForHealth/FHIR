/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.validation.test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.resource.Observation;
import org.linuxforhealth.fhir.model.resource.StructureDefinition;
import org.linuxforhealth.fhir.profile.ProfileSupport;
import org.linuxforhealth.fhir.registry.FHIRRegistry;

public class ProfileSupportTest {
    @Test
    public void testProfileSupport() throws Exception {
        boolean exists = FHIRRegistry.getInstance().hasResource("http://example.com/fhir/StructureDefinition/test-dummy-profile", StructureDefinition.class);
        assertTrue(exists);

        StructureDefinition profile = FHIRRegistry.getInstance().getResource("http://example.com/fhir/StructureDefinition/test-dummy-profile", StructureDefinition.class);
        boolean applicable = ProfileSupport.isApplicable(profile, Observation.class);
        assertFalse(applicable);

        profile = FHIRRegistry.getInstance().getResource("http://example.com/fhir/StructureDefinition/my-observation", StructureDefinition.class);
        applicable = ProfileSupport.isApplicable(profile, Observation.class);
        assertTrue(applicable);

        applicable = ProfileSupport.isApplicable(profile, null);
        assertFalse(applicable);
    }
}
