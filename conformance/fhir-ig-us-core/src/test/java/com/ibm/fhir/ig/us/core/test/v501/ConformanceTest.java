/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.us.core.test.v501;

import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_FALSE;
import static com.ibm.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_TRUE;
import static com.ibm.fhir.validation.util.FHIRValidationUtil.countErrors;
import static com.ibm.fhir.validation.util.FHIRValidationUtil.countInformation;
import static com.ibm.fhir.validation.util.FHIRValidationUtil.countWarnings;
import static org.testng.Assert.assertEquals;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.patch.exception.FHIRPatchException;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.CarePlan;
import com.ibm.fhir.model.resource.Condition;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Provenance;
import com.ibm.fhir.model.resource.Provenance.Agent;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Address;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.AdministrativeGender;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.exception.FHIRPathException;
import com.ibm.fhir.path.util.FHIRPathUtil;
import com.ibm.fhir.validation.FHIRValidator;
import com.ibm.fhir.validation.exception.FHIRValidationException;

public class ConformanceTest {
    private static final String US_CORE_PATIENT = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-patient";

    @Test
    public void testConformsToWithEmptyContext() throws Exception {
        try (InputStream in = ConformanceTest.class.getClassLoader().getResourceAsStream("JSON/501/tests/us-core-patient.json")) {
            Patient patient = FHIRParser.parser(Format.JSON).parse(in);
            List<Issue> issues = FHIRValidator.validator().validate(patient);
            issues.forEach(System.out::println);
            assertEquals(issues.size(), 3);
        }
    }

    @Test
    public void testUSCoreMedicationRequest() throws Exception {
        // This test is used to diagnose and test MedicationRequest which throws an error.
        try (Reader r = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("JSON/501/MedicationRequest-uscore-mo2.json"))) {
            Resource resource = FHIRParser.parser(Format.JSON).parse(r);
            List<Issue> issues = FHIRValidator.validator().validate(resource);
            issues.forEach(item -> {
                if (item.getSeverity().getValue().equals("error")) {
                    System.out.println(item);
                }
            });
            Assert.assertEquals(countErrors(issues), 0);
        } catch (Exception e) {
            throw e;
        }
    }

    @Test
    public void testUSCorePulseOximetry() throws Exception {
        /**
         * This test is used to diagnose and test Code 'L/min' is invalid
         */
        try (Reader r = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("JSON/501/Observation-satO2-fiO2.json"))) {
            Resource resource = FHIRParser.parser(Format.JSON).parse(r);
            List<Issue> issues = FHIRValidator.validator().validate(resource);
            issues.forEach(item -> {
                if (item.getSeverity().getValue().equals("error")) {
                    System.out.println(item);
                }
            });
            Assert.assertEquals(countErrors(issues), 0);
        } catch (Exception e) {
            throw e;
        }
    }

    @Test
    public void testUSCoreCarePlan() throws Exception {
        InputStream in = getClass().getClassLoader().getResourceAsStream("JSON/501/tests/us-core-careplan.json");
        CarePlan carePlan = FHIRParser.parser(Format.JSON).parse(in);
        List<Issue> issues = FHIRValidator.validator().validate(carePlan);
        issues.forEach(System.out::println);
        assertEquals(issues.size(), 0);
    }

    @Test
    public void testUSCoreValidation1() throws Exception {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("JSON/501/tests/us-core-patient-no-name-asserted.json")) {
            Patient patient = FHIRParser.parser(Format.JSON).parse(in);
            List<Issue> issues = FHIRValidator.validator().validate(patient);
            issues.forEach(System.out::println);
            Assert.assertEquals(countErrors(issues), 2);
        }
    }

    @Test
    public void testUSCoreValidation2() throws Exception {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("JSON/501/tests/us-core-patient-no-name-not-asserted.json")) {
            Patient patient = FHIRParser.parser(Format.JSON).parse(in);
            List<Issue> issues = FHIRValidator.validator().validate(patient);
            issues.forEach(System.out::println);
            Assert.assertEquals(countErrors(issues), 0);
        }
    }

    @Test
    public void testUSCoreValidation5() throws Exception {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("JSON/501/tests/Pamela954_Johns824_4818eca9-c6d2-4fa0-a234-7244e620391e.json")) {
            Bundle bundle = FHIRParser.parser(Format.JSON).parse(in);
            FHIRValidator validator = FHIRValidator.validator();
            List<Issue> issues = validator.validate(bundle);
            issues.stream()
                .filter(issue -> issue.getSeverity().equals(IssueSeverity.ERROR))
                .forEach(System.out::println);
            Assert.assertEquals(countErrors(issues), 0);
        }
    }

    @Test
    public void testUSCoreValidation6() throws Exception {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("JSON/501/tests/1.json")) {
            Observation observation = FHIRParser.parser(Format.JSON).parse(in);
            List<Issue> issues = FHIRValidator.validator().validate(observation);
            Assert.assertEquals(countErrors(issues), 0);
        }
    }

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
        List<Issue> issues = validator.validate(patient, US_CORE_PATIENT);

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
        List<Issue> issues = validator.validate(patient, US_CORE_PATIENT);

        issues.forEach(System.out::println);

        // one for generated-us-core-patient-Patient.extension<http://hl7.org/fhir/us/core/StructureDefinition/us-core-ethnicity>
        // and one for the corresponding "not a valid member of ValueSet http://hl7.org/fhir/us/core/ValueSet/detailed-ethnicity"
        Assert.assertEquals(countErrors(issues), 2);
        Assert.assertEquals(countWarnings(issues), 1);
        Assert.assertEquals(countInformation(issues), 1);
    }

    /**
     * Test the FHIRPath 'conformsTo' function on a valid US Core Race Extension
     */
    @Test
    public void testUSCoreRaceExtension1() throws Exception {
        Extension extension = Extension.builder()
                .extension(Extension.builder()
                    .url("ombCategory")
                    .value(Coding.builder()
                        .system(Uri.of("urn:oid:2.16.840.1.113883.6.238"))
                        .code(Code.of("1002-5"))
                        .display(string("American Indian or Alaska Native"))
                        .build())
                    .build())
                .extension(Extension.builder()
                    .url("detailed")
                    .value(Coding.builder()
                        .system(Uri.of("urn:oid:2.16.840.1.113883.6.238"))
                        .code(Code.of("1735-0"))
                        .display(string("Alaska Native"))
                        .build())
                    .build())
                .extension(Extension.builder()
                    .url("text")
                    .value(string("American Indian or Alaska Native - Alaska Native"))
                    .build())
                .url("http://hl7.org/fhir/us/core/StructureDefinition/us-core-race")
                .build();

        System.out.println(extension);

        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(extension, "conformsTo('http://hl7.org/fhir/us/core/StructureDefinition/us-core-race')");
        System.out.println("result: " + result);

        List<Issue> issues = evaluator.getEvaluationContext().getIssues();
        issues.forEach(System.out::println);

        Assert.assertEquals(result, SINGLETON_TRUE);
        Assert.assertEquals(issues.size(), 0);
    }

    /**
     * Test the FHIRPath 'conformsTo' function on a US Core Race Extension with an invalid detailed race code
     */
    @Test
    public void testUSCoreRaceExtension2() throws Exception {
        Extension extension = Extension.builder()
                .extension(Extension.builder()
                    .url("ombCategory")
                    .value(Coding.builder()
                        .system(Uri.of("urn:oid:2.16.840.1.113883.6.238"))
                        .code(Code.of("1002-5"))
                        .display(string("American Indian or Alaska Native"))
                        .build())
                    .build())
                .extension(Extension.builder()
                    .url("detailed")
                    .value(Coding.builder()
                        .system(Uri.of("urn:oid:2.16.840.1.113883.6.238"))
                        .code(Code.of("xxx"))
                        .display(string("Alaska Native"))
                        .build())
                    .build())
                .extension(Extension.builder()
                    .url("text")
                    .value(string("American Indian or Alaska Native - Alaska Native"))
                    .build())
                .url("http://hl7.org/fhir/us/core/StructureDefinition/us-core-race")
                .build();

        System.out.println(extension);

        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(extension, "conformsTo('http://hl7.org/fhir/us/core/StructureDefinition/us-core-race')");
        System.out.println("result: " + result);

        List<Issue> issues = evaluator.getEvaluationContext().getIssues();
        issues.forEach(System.out::println);

        Assert.assertEquals(result, SINGLETON_FALSE);
        Assert.assertEquals(issues.size(), 2);
    }

    /**
     * Test the FHIRValidator on a US Core Patient with a valid US Core Race Extension
     */
    @Test
    public void testUSCoreRaceExtension3() throws Exception {
        Patient patient = Patient.builder()
                .extension(Extension.builder()
                    .extension(Extension.builder()
                        .url("ombCategory")
                        .value(Coding.builder()
                            .system(Uri.of("urn:oid:2.16.840.1.113883.6.238"))
                            .code(Code.of("1002-5"))
                            .display(string("American Indian or Alaska Native"))
                            .build())
                        .build())
                    .extension(Extension.builder()
                        .url("detailed")
                        .value(Coding.builder()
                            .system(Uri.of("urn:oid:2.16.840.1.113883.6.238"))
                            .code(Code.of("1735-0"))
                            .display(string("Alaska Native"))
                            .build())
                        .build())
                    .extension(Extension.builder()
                        .url("text")
                        .value(string("American Indian or Alaska Native - Alaska Native"))
                        .build())
                    .url("http://hl7.org/fhir/us/core/StructureDefinition/us-core-race")
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
        List<Issue> issues = validator.validate(patient, US_CORE_PATIENT);

        issues.forEach(System.out::println);

        Assert.assertEquals(countWarnings(issues), 1);
        Assert.assertEquals(countErrors(issues), 0);
    }

    /**
     * Test the FHIRValidator on a US Core Patient with a US Core Race Extension containing an invalid detailed race code
     */
    @Test
    public void testUSCoreRaceExtension4() throws Exception {
        Patient patient = Patient.builder()
                .extension(Extension.builder()
                    .extension(Extension.builder()
                        .url("ombCategory")
                        .value(Coding.builder()
                            .system(Uri.of("urn:oid:2.16.840.1.113883.6.238"))
                            .code(Code.of("1002-5"))
                            .display(string("American Indian or Alaska Native"))
                            .build())
                        .build())
                    .extension(Extension.builder()
                        .url("detailed")
                        .value(Coding.builder()
                            .system(Uri.of("urn:oid:2.16.840.1.113883.6.238"))
                            .code(Code.of("xxx"))
                            .display(string("Alaska Native"))
                            .build())
                        .build())
                    .extension(Extension.builder()
                        .url("text")
                        .value(string("American Indian or Alaska Native - Alaska Native"))
                        .build())
                    .url("http://hl7.org/fhir/us/core/StructureDefinition/us-core-race")
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
        List<Issue> issues = validator.validate(patient, US_CORE_PATIENT);

        issues.forEach(System.out::println);

        // one for generated-us-core-patient-Patient.extension<http://hl7.org/fhir/us/core/StructureDefinition/us-core-race>
        // and one for the corresponding "not a valid member of ValueSet http://hl7.org/fhir/us/core/ValueSet/detailed-race"
        Assert.assertEquals(countErrors(issues), 2);
        Assert.assertEquals(countWarnings(issues), 1);
        Assert.assertEquals(countInformation(issues), 1);
    }

    @Test
    void testConditionWithMultipleCategory() throws FHIRValidationException {
        Condition condition = Condition.builder()
                .text(Narrative.EMPTY)
                .clinicalStatus(CodeableConcept.builder()
                        .coding(Coding.builder()
                            .system(Uri.of("http://terminology.hl7.org/CodeSystem/condition-clinical"))
                            .code(Code.of("active"))
                            .build())
                        .build())
                .subject(Reference.builder()
                        .display("ref")
                        .build())
                .code(CodeableConcept.builder()
                        .coding(Coding.builder()
                                .system(Uri.of("http://snomed.info/sct"))
                                .code(Code.of("404684003"))
                                .build())
                        .build())
                .category(CodeableConcept.builder()
                        .text("test")
                        .build())
                .category(CodeableConcept.builder()
                        .coding(Coding.builder()
                            .system(Uri.of("http://hl7.org/fhir/us/core/STU4/CodeSystem-condition-category.html"))
                            .code(Code.of("health-concern"))
                            .build())
                        .build())
                .build();

        FHIRValidator validator = FHIRValidator.validator();
        List<Issue> issues = validator.validate(condition, "http://hl7.org/fhir/us/core/StructureDefinition/us-core-condition");

        issues.forEach(System.out::println);

        Assert.assertEquals(countErrors(issues), 0);
        // warning 1:  us-core-1: A code in Condition.category SHOULD be from US Core Condition Category Codes value set.
        // causedBy
        // warning 2:  Membership check was not performed: value set 'http://hl7.org/fhir/us/core/ValueSet/us-core-condition-code' is empty or could not be expanded
        Assert.assertEquals(countWarnings(issues), 2);
    }

    @Test
    void testProvenanceWithMultipleAgent() throws FHIRValidationException, FHIRPathException, FHIRPatchException {
        Provenance provenance = Provenance.builder()
                .text(Narrative.EMPTY)
                .target(Reference.builder()
                        .reference("Observation/123")
                        .build())
                .recorded(Instant.now())
                .agent(Agent.builder()
                        .who(Reference.builder()
                            .reference("Patient/123")
                            .build())
                        .build())
                .agent(Agent.builder()
                        .who(Reference.builder()
                            .reference("Practitioner/abc")
                            .build())
                        .build())
                .agent(Agent.builder()
                        .who(Reference.builder()
                            .reference("Device/xyz")
                            .build())
                        .onBehalfOf(Reference.builder()
                            .reference("RelatedPerson/bleh")
                            .build())
                        .build())
                .build();

        FHIRValidator validator = FHIRValidator.validator();
        List<Issue> issues = validator.validate(provenance, "http://hl7.org/fhir/us/core/StructureDefinition/us-core-provenance");

        issues.forEach(System.out::println);

        Assert.assertEquals(countErrors(issues), 1);
        Assert.assertEquals(countWarnings(issues), 0);

        Reference refToAdd = Reference.builder()
            .reference("Organization/fix")
            .build();
        provenance = FHIRPathUtil.add(provenance, "Provenance.agent[1]", "onBehalfOf", refToAdd);
        issues = validator.validate(provenance, "http://hl7.org/fhir/us/core/StructureDefinition/us-core-provenance");
        Assert.assertEquals(countErrors(issues), 0);
    }

    /**
     * Ensures that we can validate patient addresses against the USPS Two Letter Alphabetic Codes
     */
    @Test
    public void testUSCoreValidateAddress() throws Exception {
        FHIRValidator validator = FHIRValidator.validator();
        Patient patient = Patient.builder()
                .text(Narrative.EMPTY)
                .identifier(Identifier.builder()
                    .system(Uri.of("http://someuri.org"))
                    .value(string("someValue"))
                    .build())
                .name(HumanName.builder()
                    .given(string("John"))
                    .family(string("Doe"))
                    .build())
                .gender(AdministrativeGender.MALE)
                .address(Address.builder()
                    .line("test")
                    .build())
                .build();

        // no state = no warning or info messages
        List<Issue> issues = validator.validate(patient, US_CORE_PATIENT);
        Assert.assertEquals(countWarnings(issues), 0);
        Assert.assertEquals(countInformation(issues), 0);

        // invalid state = info messages about the unexpected code
        patient = FHIRPathUtil.add(patient, "address[0]", "state", string("bogus"));
        System.out.println(patient);
        issues = validator.validate(patient, US_CORE_PATIENT);
        issues.forEach(System.out::println);
        Assert.assertEquals(countWarnings(issues), 0);
        Assert.assertEquals(countInformation(issues), 2);
        Assert.assertEquals(issues.get(0).getDetails().getText().getValue(), "Code 'bogus' is not a valid member of ValueSet "
                + "with URL=http://hl7.org/fhir/us/core/ValueSet/us-core-usps-state and version=5.0.1");

        // valid state = no warning or info messages
        patient = FHIRPathUtil.replace(patient, "address[0].state", string("WI"));
        System.out.println(patient);
        issues = validator.validate(patient, US_CORE_PATIENT);
        issues.forEach(System.out::println);
        Assert.assertEquals(countWarnings(issues), 0);
        Assert.assertEquals(countInformation(issues), 0);
    }
}