/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.ig.us.spl;

import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.compile;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.annotation.Constraint;
import org.linuxforhealth.fhir.model.resource.StructureDefinition;
import org.linuxforhealth.fhir.model.util.ModelSupport;
import org.linuxforhealth.fhir.profile.ProfileSupport;
import org.linuxforhealth.fhir.registry.resource.FHIRRegistryResource;
import org.linuxforhealth.fhir.registry.spi.FHIRRegistryResourceProvider;

public class ConstraintGeneratorTest {
    @Test
    public static void testConstraintGenerator() throws Exception {
        FHIRRegistryResourceProvider provider = new ResourceProvider();
        for (FHIRRegistryResource registryResource : provider.getRegistryResources()) {
            if (StructureDefinition.class.equals(registryResource.getResourceType())) {
                String url = registryResource.getUrl();
                System.out.println(url);
                String kind = registryResource.getKind();
                if ("resource".equals(kind) || "complex-type".equals(kind)) {
                    Class<?> type = ModelSupport.isResourceType(registryResource.getType()) ? ModelSupport.getResourceType(registryResource.getType()) : Class.forName("org.linuxforhealth.fhir.model.type." + registryResource.getType());
                    for (Constraint constraint : ProfileSupport.getConstraints(url, type)) {
                        System.out.println("    " + constraint);
                        if (!Constraint.LOCATION_BASE.equals(constraint.location())) {
                            compile(constraint.location());
                        }
                        compile(constraint.expression());
                    }
                }
            }
        }
    }
}
