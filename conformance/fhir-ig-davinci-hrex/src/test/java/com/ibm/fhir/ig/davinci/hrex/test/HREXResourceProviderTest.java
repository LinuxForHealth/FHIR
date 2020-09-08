/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.davinci.hrex.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.ig.davinci.hrex.HREXResourceProvider;
import com.ibm.fhir.registry.spi.FHIRRegistryResourceProvider;

public class HREXResourceProviderTest {
    @Test
    public void testEpdxResourceProvider() {
        FHIRRegistryResourceProvider provider = new HREXResourceProvider();
        Assert.assertEquals(provider.getRegistryResources().size(), 19);
    }
}