/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.hl7.terminology.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.hl7.terminology.Hl7Terminology310ResourceProvider;
import com.ibm.fhir.registry.spi.FHIRRegistryResourceProvider;

public class Hl7TerminologyResourceProviderTest {
    @Test
    public void testR4BSpecResourceProvider() {
        FHIRRegistryResourceProvider provider = new Hl7Terminology310ResourceProvider();
        Assert.assertEquals(provider.getRegistryResources().size(), 3528);
    }
}
