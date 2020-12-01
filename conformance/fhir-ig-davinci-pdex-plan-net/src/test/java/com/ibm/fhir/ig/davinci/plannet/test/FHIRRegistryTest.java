/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.davinci.plannet.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.fhir.model.resource.CapabilityStatement;
import com.ibm.fhir.registry.FHIRRegistry;

public class FHIRRegistryTest {
    @Test
    public void testRegistry() throws FHIRGeneratorException {
        CapabilityStatement definition = FHIRRegistry.getInstance()
                .getResource("http://hl7.org/fhir/us/davinci-pdex-plan-net/CapabilityStatement/plan-net", CapabilityStatement.class);
        FHIRGenerator.generator(Format.XML).generate(definition, System.out);
        Assert.assertNotNull(definition);
    }
}
