/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.hl7.terminology.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.registry.FHIRRegistry;

public class FHIRRegistryTest {
    @Test
    public void testVersionedResource() {
        CodeSystem codeSystem = FHIRRegistry.getInstance().getResource("http://terminology.hl7.org/CodeSystem/v2-0391|2.4", CodeSystem.class);
        Assert.assertNotNull(codeSystem);

        codeSystem = FHIRRegistry.getInstance().getResource("http://terminology.hl7.org/CodeSystem/v2-0391|2.6", CodeSystem.class);
        Assert.assertNotNull(codeSystem);

        codeSystem = FHIRRegistry.getInstance().getResource("http://terminology.hl7.org/CodeSystem/v2-0391", CodeSystem.class);
        Assert.assertNotNull(codeSystem);
        Assert.assertTrue(codeSystem.getUrl().getValue().endsWith("2.6"));
    }
}
