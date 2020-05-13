/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.vhdir;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.registry.spi.FHIRRegistryResourceProvider;

public class VHDirResourceProviderTest {
    @Test
    public void testGetResources() {
        FHIRRegistryResourceProvider provider = new VHDirResourceProvider();
        Assert.assertEquals(provider.getRegistryResources().size(), 25);
    }
}
