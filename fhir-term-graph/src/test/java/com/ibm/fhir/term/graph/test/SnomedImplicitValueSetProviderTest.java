/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.graph.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.registry.FHIRRegistry;

public class SnomedImplicitValueSetProviderTest {
    @Test
    public void testSnomedImplicitValueSetProvider() {
        ValueSet valueSet = FHIRRegistry.getInstance().getResource("http://snomed.info/sct?fhir_vs", ValueSet.class);
        Assert.assertNotNull(valueSet);
        System.out.println(valueSet);

        valueSet = FHIRRegistry.getInstance().getResource("http://snomed.info/sct?fhir_vs=195967001", ValueSet.class);
        Assert.assertNotNull(valueSet);
        System.out.println(valueSet);
    }
}
