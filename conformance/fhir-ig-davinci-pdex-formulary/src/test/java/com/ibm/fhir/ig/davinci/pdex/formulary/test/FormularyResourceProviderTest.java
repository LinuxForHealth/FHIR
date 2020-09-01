/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.davinci.pdex.formulary.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.ig.davinci.pdex.formulary.FormularyResourceProvider;
import com.ibm.fhir.registry.spi.FHIRRegistryResourceProvider;

public class FormularyResourceProviderTest {
    @Test
    public void testGetResources() {
        FHIRRegistryResourceProvider provider = new FormularyResourceProvider();
        Assert.assertEquals(provider.getRegistryResources().size(), 28);
    }
}