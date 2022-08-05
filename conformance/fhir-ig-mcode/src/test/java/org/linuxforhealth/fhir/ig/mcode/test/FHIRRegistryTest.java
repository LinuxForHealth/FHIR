/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.ig.mcode.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.resource.StructureDefinition;
import org.linuxforhealth.fhir.registry.FHIRRegistry;

public class FHIRRegistryTest {
    @Test
    public void testRegistry() {
        StructureDefinition definition = FHIRRegistry.getInstance().getResource("http://hl7.org/fhir/us/mcode/StructureDefinition/mcode-cancer-condition-parent", StructureDefinition.class);
        Assert.assertNotNull(definition);
    }
}
