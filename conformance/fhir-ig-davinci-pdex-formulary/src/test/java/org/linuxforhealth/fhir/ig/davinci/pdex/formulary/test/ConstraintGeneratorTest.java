/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.ig.davinci.pdex.formulary.test;

import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.compile;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.ig.davinci.pdex.formulary.Formulary101ResourceProvider;
import org.linuxforhealth.fhir.ig.davinci.pdex.formulary.Formulary110ResourceProvider;
import org.linuxforhealth.fhir.ig.davinci.pdex.formulary.Formulary200ResourceProvider;
import org.linuxforhealth.fhir.model.annotation.Constraint;
import org.linuxforhealth.fhir.model.resource.StructureDefinition;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.util.ModelSupport;
import org.linuxforhealth.fhir.profile.ProfileSupport;
import org.linuxforhealth.fhir.registry.resource.FHIRRegistryResource;
import org.linuxforhealth.fhir.registry.spi.FHIRRegistryResourceProvider;

public class ConstraintGeneratorTest {
    @Test
    public static void test101ConstraintGenerator() throws Exception {
        FHIRRegistryResourceProvider provider = new Formulary101ResourceProvider();
        for (FHIRRegistryResource registryResource : provider.getRegistryResources()) {
            if (StructureDefinition.class.equals(registryResource.getResourceType())) {
                String url = registryResource.getUrl() + "|" + registryResource.getVersion();
                System.out.println(url);
                Class<?> type = ModelSupport.isResourceType(registryResource.getType()) ? ModelSupport.getResourceType(registryResource.getType()) : Extension.class;
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

    @Test
    public static void test110ConstraintGenerator() throws Exception {
        FHIRRegistryResourceProvider provider = new Formulary110ResourceProvider();
        for (FHIRRegistryResource registryResource : provider.getRegistryResources()) {
            if (StructureDefinition.class.equals(registryResource.getResourceType())) {
                String url = registryResource.getUrl() + "|" + registryResource.getVersion();
                System.out.println(url);
                Class<?> type = ModelSupport.isResourceType(registryResource.getType()) ? ModelSupport.getResourceType(registryResource.getType()) : Extension.class;
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
    
    @Test
    public static void test200ConstraintGenerator() throws Exception {
        FHIRRegistryResourceProvider provider = new Formulary200ResourceProvider();
        for (FHIRRegistryResource registryResource : provider.getRegistryResources()) {
            if (StructureDefinition.class.equals(registryResource.getResourceType())) {
                String url = registryResource.getUrl() + "|" + registryResource.getVersion();
                System.out.println(url);
                Class<?> type = ModelSupport.isResourceType(registryResource.getType()) ? ModelSupport.getResourceType(registryResource.getType()) : Extension.class;
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

    public static void main(String[] args) {
        String url = "http://hl7.org/fhir/us/davinci-drug-formulary/StructureDefinition/usdf-DrugTierDefinition-extension|1.0.1";
        for (Constraint constraint : ProfileSupport.getConstraints(url, Extension.class)) {
            System.out.println("    " + constraint);
            if (!Constraint.LOCATION_BASE.equals(constraint.location())) {
                compile(constraint.location());
            }
            compile(constraint.expression());
        }

    }
}