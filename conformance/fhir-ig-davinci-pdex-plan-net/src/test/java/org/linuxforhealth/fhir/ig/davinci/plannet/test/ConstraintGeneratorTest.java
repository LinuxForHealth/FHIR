/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.ig.davinci.plannet.test;

import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.compile;

import org.linuxforhealth.fhir.ig.davinci.pdex.plannet.PlanNet100ResourceProvider;
import org.linuxforhealth.fhir.ig.davinci.pdex.plannet.PlanNet110ResourceProvider;
import org.linuxforhealth.fhir.model.annotation.Constraint;
import org.linuxforhealth.fhir.model.resource.StructureDefinition;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.util.ModelSupport;
import org.linuxforhealth.fhir.profile.ProfileSupport;
import org.linuxforhealth.fhir.registry.resource.FHIRRegistryResource;
import org.linuxforhealth.fhir.registry.spi.FHIRRegistryResourceProvider;
import org.testng.annotations.Test;

public class ConstraintGeneratorTest {
    private static boolean DEBUG = true;

    @Test
    public static void test100ConstraintGenerator() throws Exception {
        FHIRRegistryResourceProvider provider = new PlanNet100ResourceProvider();
        for (FHIRRegistryResource registryResource : provider.getRegistryResources()) {
            if (StructureDefinition.class.equals(registryResource.getResourceType())) {
                String url = registryResource.getUrl();
                String version = registryResource.getVersion().toString();
                if (DEBUG) {
                    System.out.println(url);
                }
                Class<?> type = ModelSupport.isResourceType(registryResource.getType()) ? ModelSupport.getResourceType(registryResource.getType()) : Extension.class;
                for (Constraint constraint : ProfileSupport.getConstraints(url + "|" + version, type)) {
                    if (DEBUG) {
                        System.out.println("    " + constraint);
                    }
                    if (!Constraint.LOCATION_BASE.equals(constraint.location())) {
                        compile(constraint.location());
                    }
                    compile(constraint.expression());
                }
            }
        }
    }

    @Test
    public static void test110ConstraintGenerator() throws Exception {
        FHIRRegistryResourceProvider provider = new PlanNet110ResourceProvider();
        for (FHIRRegistryResource registryResource : provider.getRegistryResources()) {
            if (StructureDefinition.class.equals(registryResource.getResourceType())) {
                String url = registryResource.getUrl();
                String version = registryResource.getVersion().toString();
                if (DEBUG) {
                    System.out.println(url);
                }
                Class<?> type = ModelSupport.isResourceType(registryResource.getType()) ? ModelSupport.getResourceType(registryResource.getType()) : Extension.class;
                for (Constraint constraint : ProfileSupport.getConstraints(url + "|" + version, type)) {
                    if (DEBUG) {
                        System.out.println("    " + constraint);
                    }
                    if (!Constraint.LOCATION_BASE.equals(constraint.location())) {
                        compile(constraint.location());
                    }
                    compile(constraint.expression());
                }
            }
        }
    }
}
