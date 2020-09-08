/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.davinci.pdex.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.ig.davinci.pdex.EPDXResourceProvider;
import com.ibm.fhir.registry.spi.FHIRRegistryResourceProvider;

public class EPDXResourceProviderTest {
    @Test
    public void testEpdxResourceProvider() {
        FHIRRegistryResourceProvider provider = new EPDXResourceProvider();
        Assert.assertEquals(provider.getRegistryResources().size(), 14);
    }
}