/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.davinci.hrex.test.v020;

import static com.ibm.fhir.path.util.FHIRPathUtil.compile;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Collection;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.ig.davinci.hrex.HREX020ResourceProvider;
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.resource.StructureDefinition;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.profile.ProfileSupport;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.registry.resource.FHIRRegistryResource;
import com.ibm.fhir.registry.spi.FHIRRegistryResourceProvider;

public class ProviderTest {

    @Test
    public void testHREXResourceProvider() {
        FHIRRegistryResourceProvider provider = new HREX020ResourceProvider();
        Collection<FHIRRegistryResource> registryResources = provider.getRegistryResources();
        assertNotNull(registryResources);
        assertTrue(!registryResources.isEmpty());
        for (FHIRRegistryResource fhirRegistryResource : registryResources) {
            assertNotNull(fhirRegistryResource.getResource());
        }
    }

    @Test
    public static void testConstraintGenerator() throws Exception {
        FHIRRegistryResourceProvider provider = new HREX020ResourceProvider();
        for (FHIRRegistryResource registryResource : provider.getRegistryResources()) {
            if (StructureDefinition.class.equals(registryResource.getResourceType())) {
                String url = registryResource.getUrl();
                System.out.println(url);
                Class<?> type =
                        ModelSupport.isResourceType(registryResource.getType()) ? ModelSupport.getResourceType(registryResource.getType()) : Extension.class;
                for (Constraint constraint : ProfileSupport.getConstraints(url, type)) {
                    System.out.println("    " + constraint);
                    if (!Constraint.LOCATION_BASE.equals(constraint.location())) {
                        compile(constraint.location());
                    }
                    compile(constraint.expression());
                }
                System.out.println("--- done");
            }
        }
    }

    @Test
    public void testRegistry() {
        StructureDefinition definition =
                FHIRRegistry.getInstance().getResource("http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-parameters-member-match-out", StructureDefinition.class);
        Assert.assertNotNull(definition);
    }
}