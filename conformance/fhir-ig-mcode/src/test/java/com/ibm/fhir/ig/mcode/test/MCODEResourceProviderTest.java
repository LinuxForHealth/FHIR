/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.mcode.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.ig.mcode.MCODEResourceProvider;
import com.ibm.fhir.registry.spi.FHIRRegistryResourceProvider;

public class MCODEResourceProviderTest {
    @Test
    public void testMCODEResourceProvider() {
        FHIRRegistryResourceProvider provider = new MCODEResourceProvider();
        Assert.assertEquals(provider.getRegistryResources().size(), 56);
    }
}
