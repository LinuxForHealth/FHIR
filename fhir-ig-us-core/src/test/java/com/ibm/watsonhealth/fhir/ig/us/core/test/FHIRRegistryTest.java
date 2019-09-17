/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.ig.us.core.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.watson.health.fhir.model.resource.StructureDefinition;
import com.ibm.watson.health.fhir.registry.FHIRRegistry;

public class FHIRRegistryTest {
    @Test
    public void testRegistry() {
        StructureDefinition definition = FHIRRegistry.getInstance().getResource("http://hl7.org/fhir/us/core/StructureDefinition/pediatric-bmi-for-age", StructureDefinition.class);
        Assert.assertNotNull(definition);
    }
}