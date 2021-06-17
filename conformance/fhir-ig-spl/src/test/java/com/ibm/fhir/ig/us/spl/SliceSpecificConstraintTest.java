/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.us.spl;

import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.validation.util.FHIRValidationUtil.hasErrors;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.InputStream;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Organization;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.StructureDefinition;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.profile.ConstraintGenerator;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.registry.resource.FHIRRegistryResource;
import com.ibm.fhir.registry.util.FHIRRegistryResourceProviderAdapter;
import com.ibm.fhir.validation.FHIRValidator;

public class SliceSpecificConstraintTest {
    @BeforeClass
    public void beforeClass() throws Exception {
        try (InputStream in = SliceSpecificConstraintTest.class.getClassLoader().getResourceAsStream("json/StructureDefinition-IdentifiedOrganization.json")) {
            StructureDefinition profile = FHIRParser.parser(Format.JSON).parse(in);
            FHIRRegistry.getInstance().addProvider(new FHIRRegistryResourceProviderAdapter() {
                @Override
                public FHIRRegistryResource getRegistryResource(Class<? extends Resource> resourceType, String url, String version) {
                    if ("http://hl7.org/fhir/us/spl/StructureDefinition/IdentifiedOrganization".equals(url)) {
                        return FHIRRegistryResource.from(profile);
                    }
                    return null;
                }
            });
        }
    }

    @Test
    public void testSliceSpecificConstraint1() throws Exception {
        StructureDefinition profile = FHIRRegistry.getInstance().getResource("http://hl7.org/fhir/us/spl/StructureDefinition/IdentifiedOrganization", StructureDefinition.class);
        ConstraintGenerator generator = new ConstraintGenerator(profile);
        List<Constraint> constraints = generator.generate();
        assertTrue(constraints.get(0).expression().contains("value.length() = 9"));
    }

    @Test
    public void testSliceSpecificConstraint2() throws Exception {
        Organization organization = Organization.builder()
            .meta(Meta.builder()
                .profile(Canonical.of("http://hl7.org/fhir/us/spl/StructureDefinition/IdentifiedOrganization"))
                .build())
            .identifier(Identifier.builder()
                .system(Uri.of("urn:oid:1.3.6.1.4.1.519.1"))
                .value(string("222222222"))
                .build())
            .identifier(Identifier.builder()
                .system(Uri.of("http://ibm.com/systems/someOtherSystem"))
                .value(string("123456"))
                .build())
            .type(CodeableConcept.builder()
                .coding(Coding.builder()
                    .system(Uri.of("http://hl7.org/fhir/us/spl/CodeSystem/codesystem-splOrganizationTypes"))
                    .code(Code.of("Establishment"))
                    .build())
                .build())
            .name(string("My Identified Organization"))
            .build();
        FHIRValidator validator = FHIRValidator.validator();
        List<Issue> issues = validator.validate(organization);
        assertFalse(hasErrors(issues));
    }
}
