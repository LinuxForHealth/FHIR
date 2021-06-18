/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.us.spl;

import java.io.InputStream;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Organization;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.StructureDefinition;
import com.ibm.fhir.model.type.ContactPoint;
import com.ibm.fhir.profile.ConstraintGenerator;
import com.ibm.fhir.profile.ProfileSupport;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.registry.resource.FHIRRegistryResource;
import com.ibm.fhir.registry.util.FHIRRegistryResourceProviderAdapter;
import com.ibm.fhir.validation.FHIRValidator;

public class EstablishmentOrganizationTest {
    public static void main(String[] args) throws Exception {
        Logger logger = Logger.getLogger(ConstraintGenerator.class.getName());
        logger.setLevel(Level.FINEST);
        Handler h = new Handler() {
            @Override
            public void publish(LogRecord record) {
                System.out.println(record.getMessage());
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() throws SecurityException {
            }
        };
        h.setLevel(Level.FINEST);
        logger.addHandler(h);

        try (InputStream in = EstablishmentOrganizationTest.class.getClassLoader().getResourceAsStream("xml/StructureDefinition-EstablishmentOrganization.xml")) {
            StructureDefinition structureDefinition = FHIRParser.parser(Format.XML).parse(in);
            FHIRRegistry.getInstance().addProvider(new FHIRRegistryResourceProviderAdapter() {
                @Override
                public FHIRRegistryResource getRegistryResource(Class<? extends Resource> resourceType, String url, String version) {
                    if ("http://hl7.org/fhir/us/spl/StructureDefinition/EstablishmentOrganization".equals(url)) {
                        return FHIRRegistryResource.from(structureDefinition);
                    }
                    return null;
                }
            });
            ProfileSupport.getConstraints("http://hl7.org/fhir/us/spl/StructureDefinition/EstablishmentOrganization", Organization.class).forEach(System.out::println);
        }

        try (InputStream in = EstablishmentOrganizationTest.class.getClassLoader().getResourceAsStream("xml/StructureDefinition-SPLContactPoint.xml")) {
            StructureDefinition structureDefinition = FHIRParser.parser(Format.XML).parse(in);
            FHIRRegistry.getInstance().addProvider(new FHIRRegistryResourceProviderAdapter() {
                @Override
                public FHIRRegistryResource getRegistryResource(Class<? extends Resource> resourceType, String url, String version) {
                    if ("http://hl7.org/fhir/us/spl/StructureDefinition/SPLContactPoint".equals(url)) {
                        return FHIRRegistryResource.from(structureDefinition);
                    }
                    return null;
                }
            });
            ProfileSupport.getConstraints("http://hl7.org/fhir/us/spl/StructureDefinition/SPLContactPoint", ContactPoint.class).forEach(System.out::println);
        }

        try (InputStream in = EstablishmentOrganizationTest.class.getClassLoader().getResourceAsStream("xml/Organization-ExampleEstablishment.xml")) {
            Organization organization = FHIRParser.parser(Format.XML).parse(in);
            List<Issue> issues = FHIRValidator.validator().validate(organization);
            issues.forEach(System.out::println);
        }
    }
}
