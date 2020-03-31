/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.carin.bb.test;

import com.ibm.fhir.ig.carin.bb.BBResourceProvider;
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.resource.StructureDefinition;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.profile.ProfileSupport;
import com.ibm.fhir.registry.resource.FHIRRegistryResource;
import com.ibm.fhir.registry.spi.FHIRRegistryResourceProvider;

public class ConstraintGeneratorTest {
    public static void main(String[] args) {
        FHIRRegistryResourceProvider provider = new BBResourceProvider();
        for (FHIRRegistryResource registryResource : provider.getResources()) {
            try {
                if (StructureDefinition.class.equals(registryResource.getResourceType())) {
                    String url = registryResource.getUrl();
                    System.out.println(url);
                    for (Constraint constraint : ProfileSupport.getConstraints(url, ModelSupport.getResourceType(registryResource.getType()))) {
                        System.out.println("    " + constraint);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
