/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.us.core.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.watson.health.fhir.conformance.spi.ConformanceResourceProvider;
import com.ibm.watson.health.fhir.us.core.USCoreConformanceResourceProvider;

public class USCoreConformanceResourceProviderTest {
    @Test
    public static void testUSCoreConformanceResourceProvider() {
        ConformanceResourceProvider provider = new USCoreConformanceResourceProvider();
        Assert.assertEquals(provider.getConformanceResources().size(), 142);
    }
}