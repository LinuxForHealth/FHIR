/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.omop.test;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Organization;
import com.ibm.fhir.model.resource.Practitioner;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.visitor.DefaultVisitor;

public class PatientBundleLoader {
    public static void main(String[] args) throws Exception {
        Set<String> resourceTypeNames = new HashSet<>();
        List<String> organizationIds = new ArrayList<>();
        List<String> practitionerIds = new ArrayList<>();
        Set<String> systems = new HashSet<>();

        File dir = new File("src/test/resources/fhir/");
        for (File file : dir.listFiles()) {
            if (!file.isDirectory()) {
                System.out.println(file.getName());
                try (FileReader reader = new FileReader(file)) {
                    try {
                        Bundle bundle = FHIRParser.parser(Format.JSON).parse(reader);
                        for (Bundle.Entry entry : bundle.getEntry()) {
                            Resource resource = entry.getResource();
                            String id = resource.getId();
                            if (resource.is(Organization.class)) {
                                organizationIds.add(id);
                            }
                            if (resource.is(Practitioner.class)) {
                                practitionerIds.add(id);
                            }
                            String resourceTypeName = resource.getClass().getSimpleName();
                            resourceTypeNames.add(resourceTypeName);
                            System.out.println("    " + resourceTypeName + "/" + id);
                            resource.accept(new DefaultVisitor(true) {
                                @Override
                                public boolean visit(String elementName, int elementIndex, Coding coding) {
                                    systems.add(coding.getSystem().getValue());
                                    return false;
                                }

                                @Override
                                public boolean visit(String elementName, int elementIndex, CodeableConcept codeableConcept) {
                                    codeableConcept.getCoding().forEach(coding -> systems.add(coding.getSystem().getValue()));
                                    return false;
                                }
                            });
                        }
                    } catch (FHIRParserException e) {
                        System.err.println("Unable to load: " + file.getName() + " due to exception: " + e.getMessage());
                    }
                }
            }
        }

        System.out.println("organizationIds: " + organizationIds.size());
        Set<String> uniqueOrganizationIds = new HashSet<>(organizationIds);
        System.out.println("uniqueOrganizationIds: " + uniqueOrganizationIds.size());

        System.out.println("practitionerIds: " + practitionerIds.size());
        Set<String> uniquePractitionerIds = new HashSet<>(organizationIds);
        System.out.println("uniquePractitionerIds: " + uniquePractitionerIds.size());

        System.out.println("resourceTypeNames: " + resourceTypeNames);
        System.out.println("systems: " + systems);
    }
}
