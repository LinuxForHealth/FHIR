/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.core.r4b.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.core.r4b.Core410ResourceProvider;
import com.ibm.fhir.registry.spi.FHIRRegistryResourceProvider;

public class CoreResourceProviderTest {

    @Test
    public void testR4BSpecResourceProvider() {
        FHIRRegistryResourceProvider provider = new Core410ResourceProvider();
        Assert.assertEquals(provider.getRegistryResources().size(), 3756);
    }
}
