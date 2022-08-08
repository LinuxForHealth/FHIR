/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.core.r4b.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.core.r4b.Core430ResourceProvider;
import org.linuxforhealth.fhir.registry.spi.FHIRRegistryResourceProvider;

public class CoreResourceProviderTest {

    @Test
    public void testR4BSpecResourceProvider() {
        FHIRRegistryResourceProvider provider = new Core430ResourceProvider();
        Assert.assertEquals(provider.getRegistryResources().size(), 3756);
    }
}
