/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.mcode.test;

import com.ibm.fhir.ig.mcode.MCODEResourceProvider;
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.resource.StructureDefinition;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.profile.ProfileSupport;
import com.ibm.fhir.registry.resource.FHIRRegistryResource;
import com.ibm.fhir.registry.spi.FHIRRegistryResourceProvider;

public class ConstraintGeneratorTest {
    public static void main(String[] args) {
        FHIRRegistryResourceProvider provider = new MCODEResourceProvider();
        for (FHIRRegistryResource registryResource : provider.getResources()) {
            try {
                if (StructureDefinition.class.equals(registryResource.getResourceType())) {
                    String url = registryResource.getUrl();
                    System.out.println(url);
                    Class<?> type = ModelSupport.isResourceType(registryResource.getType()) ?
                            ModelSupport.getResourceType(registryResource.getType()) : Extension.class;
                    for (Constraint constraint : ProfileSupport.getConstraints(url, type)) {
                        System.out.println("    " + constraint);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
