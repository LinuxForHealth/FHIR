/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.conformance.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.watson.health.fhir.conformance.spec.SpecConformanceResourceProvider;
import com.ibm.watson.health.fhir.conformance.spi.ConformanceResourceProvider;

public class SpecConformanceResourceProviderTest {
    @Test
    public void testSpecConformanceResourceProvider() {
        ConformanceResourceProvider provider = new SpecConformanceResourceProvider();
        Assert.assertEquals(provider.getConformanceResources().size(), 3369);
    }
}