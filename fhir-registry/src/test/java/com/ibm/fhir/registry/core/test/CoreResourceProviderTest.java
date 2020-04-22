/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.registry.core.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.registry.core.CoreResourceProvider;
import com.ibm.fhir.registry.spi.FHIRRegistryResourceProvider;

public class CoreResourceProviderTest {
    @Test
    public void testSpecResourceProvider() {
        FHIRRegistryResourceProvider provider = new CoreResourceProvider();
        Assert.assertEquals(provider.getRegistryResources().size(), 11251);
    }
}
