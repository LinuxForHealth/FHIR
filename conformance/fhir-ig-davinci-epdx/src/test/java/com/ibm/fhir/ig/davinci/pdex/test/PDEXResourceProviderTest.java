/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.davinci.pdex.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.ig.davinci.pdex.PDEXResourceProvider;
import com.ibm.fhir.registry.spi.FHIRRegistryResourceProvider;

public class PDEXResourceProviderTest {
    @Test
    public void testEpdxResourceProvider() {
        FHIRRegistryResourceProvider provider = new PDEXResourceProvider();
        Assert.assertEquals(provider.getRegistryResources().size(), 14);
    }
}