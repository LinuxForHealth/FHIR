/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.hl7.terminology.test;

import static org.testng.Assert.assertNotNull;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.registry.FHIRRegistry;

public class FHIRRegistryTest {
    @Test
    public void testVersionedResource() {
        // a random resource url from the package
        String url = "http://terminology.hl7.org/CodeSystem/v2-0391";

        CodeSystem codeSystem = FHIRRegistry.getInstance().getResource(url, CodeSystem.class);
        assertNotNull(codeSystem);
        assertNotNull(codeSystem.getVersion().getValue());

        codeSystem = FHIRRegistry.getInstance().getResource(url + "|" + codeSystem.getVersion().getValue(), CodeSystem.class);
        Assert.assertNotNull(codeSystem);
    }
}
