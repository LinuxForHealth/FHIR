/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.validation.test;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import org.testng.annotations.Test;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Integer;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.validation.FHIRValidator;

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
        assertEquals(issues.size(), 1);
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
         assertEquals(issues.size(), 2);
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
        assertEquals(issues.size(), 2);
        assertEquals(issues.get(1).getSeverity(), IssueSeverity.WARNING);
        assertEquals(issues.get(1).getCode(), IssueType.NOT_SUPPORTED);
    }

    @Test
    public void testProfileNotApplicable() throws Exception {
        Patient patient = Patient.builder()
                .name(HumanName.builder()
                    .text(string("John Doe"))
                    .build())
                .build();
        List<Issue> issues = FHIRValidator.validator().validate(patient, "http://hl7.org/fhir/StructureDefinition/bp");
        assertEquals(issues.size(), 2);
        assertEquals(issues.get(0).getSeverity(), IssueSeverity.ERROR);
        assertEquals(issues.get(0).getCode(), IssueType.INVALID);
    }
}
