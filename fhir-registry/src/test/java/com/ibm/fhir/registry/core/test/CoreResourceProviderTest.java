/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.registry.core.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.registry.core.Core401ResourceProvider;
import com.ibm.fhir.registry.core.Core410ResourceProvider;
import com.ibm.fhir.registry.spi.FHIRRegistryResourceProvider;

public class CoreResourceProviderTest {
    @Test
    public void testR4SpecResourceProvider() {
        FHIRRegistryResourceProvider provider = new Core401ResourceProvider();
        Assert.assertEquals(provider.getRegistryResources().size(), 11251);
    }

    @Test
    public void testR4BSpecResourceProvider() {
        FHIRRegistryResourceProvider provider = new Core410ResourceProvider();
        Assert.assertEquals(provider.getRegistryResources().size(), 3528);
    }
}