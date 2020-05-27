/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.carin.bb.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.ig.carin.bb.BBResourceProvider;
import com.ibm.fhir.registry.spi.FHIRRegistryResourceProvider;

public class BBResourceProviderTest {
    @Test
    public void testBBResourceProvider() {
        FHIRRegistryResourceProvider provider = new BBResourceProvider();
        Assert.assertEquals(provider.getRegistryResources().size(), 41);
    }
}
