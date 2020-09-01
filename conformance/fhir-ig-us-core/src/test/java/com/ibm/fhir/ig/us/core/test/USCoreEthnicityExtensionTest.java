/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.us.core.test;

import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_FALSE;
import static com.ibm.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_TRUE;
import static com.ibm.fhir.validation.util.FHIRValidationUtil.countErrors;
import static com.ibm.fhir.validation.util.FHIRValidationUtil.countInformation;
import static com.ibm.fhir.validation.util.FHIRValidationUtil.countWarnings;

import java.util.Collection;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.AdministrativeGender;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.validation.FHIRValidator;

public class USCoreEthnicityExtensionTest {
    /**
     * Test the FHIRPath 'conformsTo' function on a valid US Core Ethnicity Extension
     */
    @Test
    public void testUSCoreEthnicityExtension1() throws Exception {
        Extension extension = Extension.builder()
                .extension(Extension.builder()
                    .url("ombCategory")
                    .value(Coding.builder()
                        .system(Uri.of("urn:oid:2.16.840.1.113883.6.238"))
                        .code(Code.of("2135-2"))
                        .display(string("Hispanic or Latino"))
                        .build())
                    .build())
                .extension(Extension.builder()
                    .url("detailed")
                    .value(Coding.builder()
                        .system(Uri.of("urn:oid:2.16.840.1.113883.6.238"))
                        .code(Code.of("2169-1"))
                        .display(string("Colombian"))
                        .build())
                    .build())
                .extension(Extension.builder()
                    .url("text")
                    .value(string("Hispanic or Latino - Colombian"))
                    .build())
                .url("http://hl7.org/fhir/us/core/StructureDefinition/us-core-ethnicity")
                .build();

        System.out.println(extension);

        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(extension, "conformsTo('http://hl7.org/fhir/us/core/StructureDefinition/us-core-ethnicity')");
        System.out.println("result: " + result);

        List<Issue> issues = evaluator.getEvaluationContext().getIssues();
        issues.forEach(System.out::println);

        Assert.assertEquals(result, SINGLETON_TRUE);
        Assert.assertEquals(issues.size(), 0);
    }

    /**
     * Test the FHIRPath 'conformsTo' function on a US Core Ethnicity Extension with an invalid detailed ethnicity code
     */
    @Test
    public void testUSCoreEthnicityExtension2() throws Exception {
        Extension extension = Extension.builder()
                .extension(Extension.builder()
                    .url("ombCategory")
                    .value(Coding.builder()
                        .system(Uri.of("urn:oid:2.16.840.1.113883.6.238"))
                        .code(Code.of("2135-2"))
                        .display(string("Hispanic or Latino"))
                        .build())
                    .build())
                .extension(Extension.builder()
                    .url("detailed")
                    .value(Coding.builder()
                        .system(Uri.of("urn:oid:2.16.840.1.113883.6.238"))
                        .code(Code.of("xxx"))
                        .display(string("Colombian"))
                        .build())
                    .build())
                .extension(Extension.builder()
                    .url("text")
                    .value(string("Hispanic or Latino - Colombian"))
                    .build())
                .url("http://hl7.org/fhir/us/core/StructureDefinition/us-core-ethnicity")
                .build();

        System.out.println(extension);

        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(extension, "conformsTo('http://hl7.org/fhir/us/core/StructureDefinition/us-core-ethnicity')");
        System.out.println("result: " + result);

        List<Issue> issues = evaluator.getEvaluationContext().getIssues();
        issues.forEach(System.out::println);

        Assert.assertEquals(result, SINGLETON_FALSE);
        Assert.assertEquals(issues.size(), 2);
    }

    /**
     * Test the FHIRValidator on a US Core Patient with a valid US Core Ethnicity Extension
     */
    @Test
    public void testUSCoreEthnicityExtension3() throws Exception {
        Patient patient = Patient.builder()
                .extension(Extension.builder()
                    .extension(Extension.builder()
                        .url("ombCategory")
                        .value(Coding.builder()
                            .system(Uri.of("urn:oid:2.16.840.1.113883.6.238"))
                            .code(Code.of("2135-2"))
                            .display(string("Hispanic or Latino"))
                            .build())
                        .build())
                    .extension(Extension.builder()
                        .url("detailed")
                        .value(Coding.builder()
                            .system(Uri.of("urn:oid:2.16.840.1.113883.6.238"))
                            .code(Code.of("2169-1"))
                            .display(string("Colombian"))
                            .build())
                        .build())
                    .extension(Extension.builder()
                        .url("text")
                        .value(string("Hispanic or Latino - Colombian"))
                        .build())
                    .url("http://hl7.org/fhir/us/core/StructureDefinition/us-core-ethnicity")
                    .build())
                .identifier(Identifier.builder()
                    .system(Uri.of("http://someuri.org"))
                    .value(string("someValue"))
                    .build())
                .name(HumanName.builder()
                    .given(string("John"))
                    .family(string("Doe"))
                    .build())
                .gender(AdministrativeGender.MALE)
                .build();

        FHIRValidator validator = FHIRValidator.validator();
        List<Issue> issues = validator.validate(patient, "http://hl7.org/fhir/us/core/StructureDefinition/us-core-patient");

        issues.forEach(System.out::println);

        Assert.assertEquals(countWarnings(issues), 1);
        Assert.assertEquals(countErrors(issues), 0);
    }

    /**
     * Test the FHIRValidator on a US Core Patient with a US Core Ethnicity Extension containing an invalid detailed ethnicity code
     */
    @Test
    public void testUSCoreEthnicityExtension4() throws Exception {
        Patient patient = Patient.builder()
                .extension(Extension.builder()
                    .extension(Extension.builder()
                        .url("ombCategory")
                        .value(Coding.builder()
                            .system(Uri.of("urn:oid:2.16.840.1.113883.6.238"))
                            .code(Code.of("2135-2"))
                            .display(string("Hispanic or Latino"))
                            .build())
                        .build())
                    .extension(Extension.builder()
                        .url("detailed")
                        .value(Coding.builder()
                            .system(Uri.of("urn:oid:2.16.840.1.113883.6.238"))
                            .code(Code.of("xxx"))
                            .display(string("Colombian"))
                            .build())
                        .build())
                    .extension(Extension.builder()
                        .url("text")
                        .value(string("Hispanic or Latino - Colombian"))
                        .build())
                    .url("http://hl7.org/fhir/us/core/StructureDefinition/us-core-ethnicity")
                    .build())
                .identifier(Identifier.builder()
                    .system(Uri.of("http://someuri.org"))
                    .value(string("someValue"))
                    .build())
                .name(HumanName.builder()
                    .given(string("John"))
                    .family(string("Doe"))
                    .build())
                .gender(AdministrativeGender.MALE)
                .build();

        FHIRValidator validator = FHIRValidator.validator();
        List<Issue> issues = validator.validate(patient, "http://hl7.org/fhir/us/core/StructureDefinition/us-core-patient");

        issues.forEach(System.out::println);

        Assert.assertEquals(countWarnings(issues), 1);
        Assert.assertEquals(countErrors(issues), 2);
        Assert.assertEquals(countInformation(issues), 1);
    }
}
