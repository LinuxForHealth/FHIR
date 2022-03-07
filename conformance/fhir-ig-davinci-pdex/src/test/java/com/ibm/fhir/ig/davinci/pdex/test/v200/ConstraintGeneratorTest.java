/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.davinci.pdex.test.v200;

import static com.ibm.fhir.path.util.FHIRPathUtil.compile;

import org.testng.annotations.Test;

import com.ibm.fhir.ig.davinci.pdex.PDEX200ResourceProvider;
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.resource.StructureDefinition;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.profile.ProfileSupport;
import com.ibm.fhir.registry.resource.FHIRRegistryResource;
import com.ibm.fhir.registry.spi.FHIRRegistryResourceProvider;

public class ConstraintGeneratorTest {
    @Test
    public static void testConstraintGenerator() throws Exception {
        FHIRRegistryResourceProvider provider = new PDEX200ResourceProvider();
        for (FHIRRegistryResource registryResource : provider.getRegistryResources()) {
            if (StructureDefinition.class.equals(registryResource.getResourceType())) {
                String url = registryResource.getUrl();
                System.out.println(url);
                Class<?> type = ModelSupport.isResourceType(registryResource.getType()) ? ModelSupport.getResourceType(registryResource.getType()) : Extension.class;
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
}