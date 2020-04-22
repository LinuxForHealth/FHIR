/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.validation.test;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.ibm.fhir.examples.ExamplesUtil;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Basic;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.Bundle.Entry.Request;
import com.ibm.fhir.model.resource.Bundle.Entry.Response;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Practitioner;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.model.type.code.HTTPVerb;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.validation.FHIRValidator;

public class BundleValidationTest {
    @Test
    public static void testValidationOfBundleOfBundle() throws Exception {
        FHIRParser parser = FHIRParser.parser(Format.JSON);

        Bundle bundleTemplate = parser.parse(ExamplesUtil.resourceReader("json/ibm/minimal/Bundle-1.json"));

        Bundle validInnerBundle = bundleTemplate;

        Bundle invalidInnerBundle = bundleTemplate.toBuilder()
                                                  .type(BundleType.BATCH)
                                                  .entry(Entry.builder().fullUrl(Uri.of("BadURI")).build())
                                                  .build();

        Bundle outerBundle = bundleTemplate.toBuilder()
                                           .entry(Entry.builder().resource(validInnerBundle).build())
                                           .entry(Entry.builder().resource(invalidInnerBundle).build())
                                           .build();

        FHIRGenerator.generator(Format.JSON, true).generate(outerBundle, System.out);
        System.out.println();

        List<Issue> issues = FHIRValidator.validator().validate(outerBundle);

        if (!issues.isEmpty()) {
            System.out.println("Issue(s) found:");
            for (Issue issue : issues) {
                System.out.println("    severity: " + issue.getSeverity().getValue() + ", type: " + issue.getCode().getValue() + ", details: " + issue.getDetails().getText().getValue() + ", expression: " + issue.getExpression().get(0).getValue());
            }
        }

        assertEquals(issues.size(), 2);

        assertEquals(issues.get(0).getSeverity(), IssueSeverity.ERROR);
        assertTrue(issues.get(0).getDetails().getText().getValue().contains("bdl-3: entry.request mandatory for batch/transaction/history, otherwise prohibited"));
        assertTrue(issues.get(0).getExpression().size() == 1);
        assertTrue(issues.get(0).getExpression().get(0).getValue().equals("Bundle.entry[1].resource"));

        assertEquals(issues.get(1).getSeverity(), IssueSeverity.ERROR);
        assertTrue(issues.get(1).getDetails().getText().getValue().contains("bdl-5: must be a resource unless there's a request or response"));
        assertTrue(issues.get(1).getExpression().size() == 1);
        assertTrue(issues.get(1).getExpression().get(0).getValue().equals("Bundle.entry[1].resource.entry[0]"));
    }

    @Test
    public static void testValidationOfBundleEntryReferenceToContainedResource() throws Exception {
        FHIRParser parser = FHIRParser.parser(Format.JSON);

        Bundle bundle = parser.parse(ExamplesUtil.resourceReader("json/ibm/minimal/Bundle-1.json"));
        Patient patient = parser.parse(ExamplesUtil.resourceReader("json/ibm/minimal/Patient-1.json"));
        Practitioner practitioner = parser.parse(ExamplesUtil.resourceReader("json/ibm/minimal/Practitioner-1.json"));

        patient = patient.toBuilder()
                         .contained(practitioner.toBuilder().id("test").build())
                         .generalPractitioner(Reference.builder().reference(String.of("#test")).build())
                         .build();

        bundle = bundle.toBuilder()
                       .entry(Entry.builder().resource(patient).build())
                       .build();

        FHIRGenerator.generator(Format.JSON, true).generate(bundle, System.out);
        System.out.println();

        List<Issue> issues = FHIRValidator.validator().validate(bundle);

        if (!issues.isEmpty()) {
            System.out.println("Issue(s) found:");
            for (Issue issue : issues) {
                System.out.println("    severity: " + issue.getSeverity().getValue() + ", type: " + issue.getCode().getValue() + ", details: " + issue.getDetails().getText().getValue() + ", expression: " + issue.getExpression().get(0).getValue());
                assertNotEquals(issue.getSeverity(), IssueSeverity.ERROR, "Resource contains unexpected validation error: " + issue);
            }
        }
    }

    @Test
    public static void testValidationOfBundleEntryReferenceToPeerContainedResource() throws Exception {
        FHIRParser parser = FHIRParser.parser(Format.JSON);

        Bundle bundle = parser.parse(ExamplesUtil.resourceReader("json/ibm/minimal/Bundle-1.json"));
        Patient patient = parser.parse(ExamplesUtil.resourceReader("json/ibm/minimal/Patient-1.json"));
        Practitioner practitioner = parser.parse(ExamplesUtil.resourceReader("json/ibm/minimal/Practitioner-1.json"));
        Basic basic = parser.parse(ExamplesUtil.resourceReader("json/ibm/minimal/Basic-1.json"));

        practitioner = practitioner.toBuilder()
                                   .id("test")
                                   .build();

        patient = patient.toBuilder()
                         .generalPractitioner(Reference.builder().reference(String.of("#test")).build())
                         .build();

        basic = basic.toBuilder()
                     .contained(patient)
                     .contained(practitioner)
                     .build();

        bundle = bundle.toBuilder()
                       .entry(Entry.builder().resource(basic).build())
                       .build();

        FHIRGenerator.generator(Format.JSON, true).generate(bundle, System.out);
        System.out.println();

        List<Issue> issues = FHIRValidator.validator().validate(bundle);

        if (!issues.isEmpty()) {
            System.out.println("Issue(s) found:");
            for (Issue issue : issues) {
                System.out.println("    severity: " + issue.getSeverity().getValue() + ", type: " + issue.getCode().getValue() + ", details: " + issue.getDetails().getText().getValue() + ", expression: " + issue.getExpression().get(0).getValue());
                assertNotEquals(issue.getSeverity(), IssueSeverity.ERROR, "Resource contains unexpected validation error: " + issue);
            }
        }
    }

    @Test
    public static void testValidBundleContainedInDomainResource() throws Exception {
        FHIRParser parser = FHIRParser.parser(Format.JSON);
        Patient patient = parser.parse(ExamplesUtil.resourceReader("json/ibm/minimal/Patient-1.json"));
        Practitioner practitioner = parser.parse(ExamplesUtil.resourceReader("json/ibm/minimal/Practitioner-1.json"));

        practitioner = practitioner.toBuilder()
                .id("practitioner")
                .extension(Extension.builder()
                    .url("http://ibm.com/fhir/ext")
                    .value(Reference.builder()
                        .reference(string("#bundle"))
                        .build())
                    .build())
                .build();

        Bundle bundle = parser.parse(ExamplesUtil.resourceReader("json/ibm/minimal/Bundle-1.json"));

        bundle = bundle.toBuilder().type(BundleType.BATCH)
                .id("bundle")
                .entry(Entry.builder()
                    .request(Request.builder()
                        .url(Uri.of("fhir-server/v4/api/Patient"))
                        .method(HTTPVerb.POST)
                        .build())
                    .build())
                .build();

        patient = patient.toBuilder()
                .extension(Extension.builder()
                        .url("http://ibm.com/fhir/ext")
                        .value(Reference.builder()
                            .reference(string("#bundle"))
                            .build())
                    .build())
                .contained(practitioner)
                .contained(bundle)
                .build();

        FHIRGenerator.generator(Format.JSON, true).generate(patient, System.out);
        System.out.println();

        List<Issue> issues = FHIRValidator.validator().validate(bundle);

        if (!issues.isEmpty()) {
            System.out.println("Issue(s) found:");
            for (Issue issue : issues) {
                System.out.println("    severity: " + issue.getSeverity().getValue() + ", type: " + issue.getCode().getValue() + ", details: " + issue.getDetails().getText().getValue() + ", expression: " + issue.getExpression().get(0).getValue());
                assertNotEquals(issue.getSeverity(), IssueSeverity.ERROR);
            }
        }
    }

    @Test
    public static void testInvalidBundleContainedInDomainResource() throws Exception {
        FHIRParser parser = FHIRParser.parser(Format.JSON);
        Patient patient = parser.parse(ExamplesUtil.resourceReader("json/ibm/minimal/Patient-1.json"));
        Bundle bundle = parser.parse(ExamplesUtil.resourceReader("json/ibm/minimal/Bundle-1.json"));

        bundle = bundle.toBuilder().type(BundleType.BATCH)
                .entry(Entry.builder()
                    .response(Response.builder()
                        .status(string("status"))
                        .build())
                    .build())
                .build();

        patient = patient.toBuilder().contained(bundle).build();

        FHIRGenerator.generator(Format.JSON, true).generate(patient, System.out);
        System.out.println();

        List<Issue> issues = FHIRValidator.validator().validate(bundle);

        List<Issue> errors = new ArrayList<>();
        if (!issues.isEmpty()) {
            System.out.println("Issue(s) found:");
            for (Issue issue : issues) {
                if (IssueSeverity.ERROR.equals(issue.getSeverity())) {
                    errors.add(issue);
                }
            }
        }

        assertEquals(errors.size(), 2);
        assertTrue(errors.get(0).getDetails().getText().getValue().startsWith("bdl-3"));
        assertTrue(errors.get(1).getDetails().getText().getValue().startsWith("bdl-4"));
    }
}
