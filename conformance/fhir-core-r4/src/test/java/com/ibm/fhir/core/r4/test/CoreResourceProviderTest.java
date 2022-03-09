/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.core.r4.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.core.r4.Core401ResourceProvider;
import com.ibm.fhir.registry.spi.FHIRRegistryResourceProvider;

public class CoreResourceProviderTest {
    @Test
    public void testR4SpecResourceProvider() {
        FHIRRegistryResourceProvider provider = new Core401ResourceProvider();
        Assert.assertEquals(provider.getRegistryResources().size(), 11251);
    }
}
