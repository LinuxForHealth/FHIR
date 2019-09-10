/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.conformance.spec.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.watson.health.fhir.conformance.registry.FHIRConformanceRegistry;
import com.ibm.watson.health.fhir.model.resource.StructureDefinition;

public class FHIRConformanceRegistryTest {
    @Test
    public void testConformanceRegistry() {
        StructureDefinition structureDefinition = FHIRConformanceRegistry.getInstance().getConformanceResource("http://hl7.org/fhir/StructureDefinition/Account", StructureDefinition.class);
        Assert.assertNotNull(structureDefinition);
    }
}