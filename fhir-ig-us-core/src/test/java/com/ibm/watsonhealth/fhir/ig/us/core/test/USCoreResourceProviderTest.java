/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.ig.us.core.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.watson.health.fhir.ig.us.core.USCoreResourceProvider;
import com.ibm.watson.health.fhir.registry.spi.FHIRRegistryResourceProvider;

public class USCoreResourceProviderTest {
    @Test
    public static void testUSCoreResourceProvider() {
        FHIRRegistryResourceProvider provider = new USCoreResourceProvider();
        Assert.assertEquals(provider.getResources().size(), 143);
    }
}