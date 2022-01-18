/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.validation.test;

import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.validation.util.FHIRValidationUtil.countErrors;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Endpoint;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Integer;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.validation.FHIRValidator;
import com.ibm.fhir.validation.exception.FHIRValidationException;

import org.testng.Assert;
import org.testng.annotations.Test;

public class FHIRValidatorTest {
    @Test
    public void testPatientValidation() throws Exception {
        java.lang.String id = UUID.randomUUID().toString();

        Meta meta = Meta.builder().versionId(Id.of("1"))
                .lastUpdated(Instant.now(ZoneOffset.UTC))
                .build();

        String given = String.builder().value("John")
                .extension(Extension.builder()
                    .url("http://www.ibm.com/someExtension")
                    .value(String.of("value and extension"))
                    .build())
                .build();

        String otherGiven = String.builder()
                .extension(Extension.builder()
                    .url("http://www.ibm.com/someExtension")
                    .value(String.of("extension only"))
                    .build())
                .build();

        HumanName name = HumanName.builder()
                .id("someId")
                .given(given)
                .given(otherGiven)
                .given(String.of("value no extension"))
                .family(String.of("Doe"))
                .build();

        Patient patient = Patient.builder()
                .id(id)
                .active(Boolean.TRUE)
                .multipleBirth(Integer.of(2))
                .meta(meta)
                .name(name)
                .birthDate(Date.of(LocalDate.now()))
                .build();

        FHIRGenerator.generator(Format.JSON, true).generate(patient, System.out);

        System.out.println("");

        List<Issue> issues = FHIRValidator.validator().validate(patient);

        if (!issues.isEmpty()) {
            System.out.println("Issue(s) found:");
            for (Issue issue : issues) {
                System.out.println("    severity: " + issue.getSeverity().getValue() + ", type: " + issue.getCode().getValue() + ", details: " + issue.getDetails().getText().getValue() + ", expression: " + issue.getExpression().get(0).getValue());
            }
        }
        assertEquals(issues.size(), 3);
        assertEquals(issues.get(0).getSeverity(), IssueSeverity.WARNING);
        assertTrue(issues.get(0).getDetails().getText().getValue().contains("dom-6: A resource should have narrative for robust management"));
        assertTrue(issues.get(0).getExpression().size() == 1);
        assertTrue(issues.get(0).getExpression().get(0).getValue().equals("Patient"));
    }

    @Test
    public void testProfileNotSupported() throws Exception {
         Patient patient = Patient.builder()
                 .name(HumanName.builder()
                     .text(string("John Doe"))
                     .build())
                 .build();
         List<Issue> issues = FHIRValidator.validator().validate(patient, "http://unknown.profile");
         assertEquals(issues.size(), 3);
         assertEquals(issues.get(0).getSeverity(), IssueSeverity.ERROR);
         assertEquals(issues.get(0).getCode(), IssueType.NOT_SUPPORTED);
    }

    @Test
    public void testResourceAssertedProfileNotSupported() throws Exception {
        Patient patient = Patient.builder()
                .meta(Meta.builder()
                    .profile(Canonical.of("http://unknown.profile"))
                    .build())
                .name(HumanName.builder()
                    .text(string("John Doe"))
                    .build())
                .build();
        List<Issue> issues = FHIRValidator.validator().validate(patient);
        assertEquals(issues.size(), 3);
        assertEquals(issues.get(0).getSeverity(), IssueSeverity.WARNING);
        assertEquals(issues.get(0).getCode(), IssueType.NOT_SUPPORTED);
    }

    @Test
    public void testProfileNotApplicable() throws Exception {
        Patient patient = Patient.builder()
                .name(HumanName.builder()
                    .text(string("John Doe"))
                    .build())
                .build();
        List<Issue> issues = FHIRValidator.validator().validate(patient, "http://hl7.org/fhir/StructureDefinition/bp");
        assertEquals(issues.size(), 3);
        assertEquals(issues.get(0).getSeverity(), IssueSeverity.ERROR);
        assertEquals(issues.get(0).getCode(), IssueType.INVALID);
    }

    @Test
    public void testBPValidation1() throws Exception {
        try (InputStream in = FHIRValidatorTest.class.getClassLoader().getResourceAsStream("JSON/bp-missing-code-asserted.json")) {
            Observation observation = FHIRParser.parser(Format.JSON).parse(in);
            List<Issue> issues = FHIRValidator.validator().validate(observation);
            issues.forEach(System.out::println);
            Assert.assertEquals(countErrors(issues), 1);
        }
    }

    @Test
    public void testBPValidation2() throws Exception {
        try (InputStream in = FHIRValidatorTest.class.getClassLoader().getResourceAsStream("JSON/bp-missing-code-not-asserted.json")) {
            Observation observation = FHIRParser.parser(Format.JSON).parse(in);
            List<Issue> issues = FHIRValidator.validator().validate(observation);
            issues.forEach(System.out::println);
            Assert.assertEquals(countErrors(issues), 0);
        }
    }

    @Test
    public void testBPValidation3() throws Exception {
        try (InputStream in = FHIRValidatorTest.class.getClassLoader().getResourceAsStream("XML/bp-missing-code-asserted.xml")) {
            Observation observation = FHIRParser.parser(Format.XML).parse(in);
            List<Issue> issues = FHIRValidator.validator().validate(observation);
            issues.forEach(System.out::println);
            Assert.assertEquals(countErrors(issues), 1);
        }
    }

    @Test
    public void testBPValidation4() throws Exception {
        try (InputStream in = FHIRValidatorTest.class.getClassLoader().getResourceAsStream("XML/bp-missing-code-not-asserted.xml")) {
            Observation observation = FHIRParser.parser(Format.XML).parse(in);
            List<Issue> issues = FHIRValidator.validator().validate(observation);
            issues.forEach(System.out::println);
            Assert.assertEquals(countErrors(issues), 0);
        }
    }

    @Test
    public void testCodingValidation() throws FHIRValidationException {
        Endpoint.Builder builder = Endpoint.builder();
        builder.setValidating(false);
        builder.meta(Meta.builder()
                            .profile(
                                Canonical.of("http://hl7.org/fhir/us/davinci-pdex-plan-net/StructureDefinition/plannet-Endpoint"))
                            .lastUpdated(Instant.now())
                            .build());
        builder.connectionType(Coding.builder()
            .code(Code.of("hl7-fhir-opn"))
            .system(Uri.of("http://hl7.org/fhir/us/davinci-pdex-plan-net/CodeSystem/EndpointConnectionTypeCS"))
            .display("HL7 FHIR Operation")
            .build());
        Endpoint endpoint = builder.build();

        List<Issue> issues = FHIRValidator.validator().validate(endpoint);
        // 1. Profile 'http://hl7.org/fhir/us/davinci-pdex-plan-net/StructureDefinition/plannet-Endpoint' is not supported
        // 2. dom-6: A resource should have narrative for robust management
        // 3. Code 'hl7-fhir-opn' in system 'http://hl7.org/fhir/us/davinci-pdex-plan-net/CodeSystem/EndpointConnectionTypeCS' 
        //    is not a valid member of ValueSet with URL=http://hl7.org/fhir/ValueSet/endpoint-connection-type and version=4.0.1
        // 4. endpoint-0: The concept in this element must be from the specified value set 
        //    'http://hl7.org/fhir/ValueSet/endpoint-connection-type' if possible
        assertEquals(issues.size(), 4, "number of issues");
    }
}