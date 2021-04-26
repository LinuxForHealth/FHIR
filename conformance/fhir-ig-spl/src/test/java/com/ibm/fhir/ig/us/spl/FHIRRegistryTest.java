/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.us.spl;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.StructureDefinition;
import com.ibm.fhir.registry.FHIRRegistry;

public class FHIRRegistryTest {
    @Test
    public void testRegistry() {
        StructureDefinition definition =
                FHIRRegistry.getInstance()
                    .getResource("http://hl7.org/fhir/us/spl/StructureDefinition/DualSubmissionProvenance",
                        StructureDefinition.class);
        Assert.assertNotNull(definition);
    }
}