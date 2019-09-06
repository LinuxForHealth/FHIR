/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.us.core.test;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.ibm.watson.health.fhir.conformance.registry.FHIRConformanceRegistry;
import com.ibm.watson.health.fhir.model.resource.StructureDefinition;

public class FHIRConformanceRegistryTest {
    @Test
    public void testConformanceRegistry() {
        StructureDefinition definition = FHIRConformanceRegistry.getInstance().getConformanceResource("http://hl7.org/fhir/us/core/StructureDefinition/pediatric-bmi-for-age", StructureDefinition.class);
        Assert.assertNotNull(definition);
    }
}
