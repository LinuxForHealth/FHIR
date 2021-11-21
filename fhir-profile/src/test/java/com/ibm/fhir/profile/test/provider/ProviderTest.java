/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.profile.test.provider;

import static org.testng.Assert.assertNotNull;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.resource.StructureDefinition;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.profile.ProfileSupport;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.registry.resource.FHIRRegistryResource;

public class ProviderTest {
    @BeforeClass
    public void before() {
        FHIRRegistry.getInstance();
        FHIRRegistry.init();
    }

    @Test
    public void testProviderRegistryLookup() {
        String url = "http://ibm.com/example/fhir/StructureDefinition/orgRef";
        StructureDefinition sd = FHIRRegistry.getInstance().getResource(url, StructureDefinition.class);
        assertNotNull(sd);
    }

    @Test
    public void testProviderWithLocalLookup() throws Exception {
        TestRegistryResourceProvider provider = new TestRegistryResourceProvider();
        provider.init();
        for (FHIRRegistryResource registryResource : provider.getRegistryResources()) {
            if (StructureDefinition.class.equals(registryResource.getResourceType())) {
                String url = registryResource.getUrl();
                System.out.println(url);
                Class<?> type = ModelSupport.isResourceType(registryResource.getType()) ? ModelSupport.getResourceType(registryResource.getType()) : Extension.class;
                for (Constraint constraint : ProfileSupport.getConstraints(url, type)) {
                    System.out.println("    " + constraint);
                }
            }
        }
    }
}