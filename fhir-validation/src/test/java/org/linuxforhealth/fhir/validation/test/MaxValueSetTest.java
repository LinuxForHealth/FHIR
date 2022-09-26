/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.validation.test;

import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.compile;
import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.annotation.Constraint;
import org.linuxforhealth.fhir.model.resource.Device;
import org.linuxforhealth.fhir.model.resource.Device.Specialization;
import org.linuxforhealth.fhir.model.resource.OperationOutcome.Issue;
import org.linuxforhealth.fhir.model.resource.StructureDefinition;
import org.linuxforhealth.fhir.model.type.Canonical;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.Coding;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.Meta;
import org.linuxforhealth.fhir.model.type.Narrative;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.Xhtml;
import org.linuxforhealth.fhir.model.type.code.NarrativeStatus;
import org.linuxforhealth.fhir.model.util.ValidationSupport;
import org.linuxforhealth.fhir.profile.ConstraintGenerator;
import org.linuxforhealth.fhir.registry.FHIRRegistry;
import org.linuxforhealth.fhir.validation.FHIRValidator;
import org.linuxforhealth.fhir.validation.util.FHIRValidationUtil;

/**
 * Tests constraint generation and validation of a binding that defines a maxValueSet.
 */
public class MaxValueSetTest {
    private static final String ENGLISH_US = "en-US";

    /**
     * Tests the generation of a maxValueSet constraint.
     * @throws Exception an exception
     */
    @Test
    public void testConstraintGenerator() throws Exception {

        // Tests the generation of constraints generated from bindings that include a MaxValueSet extension,
        // by using a Device profile and extensions created specifically for this test.
        //
        // Each of the following combinations is tested:
        //   Choice: No/Yes; Optional: No/Yes; Repeatable: No/Yes
        //-----------------------------------------------
        // Choice: No; Optional: No; Repeatable: No
        //-----[Device.specialization.systemType]
        // Choice: No; Optional: No; Repeatable: Yes
        //-----[Device.statusReason] (no MaxValueSet)
        // Choice: No; Optional: Yes; Repeatable: No
        //-----[Device.type]
        // Choice: No; Optional: Yes; Repeatable: Yes
        //-----[Device.safety]
        // Choice: Yes; Optional: No; Repeatable: No
        //----[test-language-primary-extension]
        // Choice: Yes; Optional: No; Repeatable: Yes
        //----[test-language-others-req-extension]
        // Choice: Yes; Optional: Yes; Repeatable: No
        //----[test-language-secondary-extension]
        // Choice: Yes; Optional: Yes; Repeatable: Yes
        //----[test-language-others-opt-extension]
        //-----------------------------------------------

        StructureDefinition structureDefinition = FHIRRegistry.getInstance().getResource("http://example.com/fhir/StructureDefinition/test-device", StructureDefinition.class);
        ConstraintGenerator generator = new ConstraintGenerator(structureDefinition);
        List<Constraint> constraints = generator.generate();
        assertEquals(constraints.size(), 7);
        constraints.forEach(constraint -> compile(constraint.expression()));
        assertEquals(constraints.get(3).expression(), "statusReason.count() >= 1");
        assertEquals(constraints.get(4).expression(), "type.exists() implies (type.exists() and type.all(memberOf('http://hl7.org/fhir/ValueSet/languages', 'extensible') and memberOf('http://hl7.org/fhir/ValueSet/all-languages', 'required')))");
        assertEquals(constraints.get(5).expression(), "specialization.exists() implies (specialization.count() >= 1 and specialization.all(systemType.exists() and systemType.all(memberOf('http://hl7.org/fhir/ValueSet/languages', 'extensible') and memberOf('http://hl7.org/fhir/ValueSet/all-languages', 'required'))))");
        assertEquals(constraints.get(6).expression(), "safety.exists() implies (safety.count() >= 1 and safety.all(memberOf('http://hl7.org/fhir/ValueSet/languages', 'extensible') and memberOf('http://hl7.org/fhir/ValueSet/all-languages', 'required')))");

        structureDefinition = FHIRRegistry.getInstance().getResource("http://example.com/fhir/StructureDefinition/test-language-primary-extension", StructureDefinition.class);
        generator = new ConstraintGenerator(structureDefinition);
        constraints = generator.generate();
        assertEquals(constraints.size(), 2);
        constraints.forEach(constraint -> compile(constraint.expression()));
        assertEquals(constraints.get(1).expression(), "value.where(isTypeEqual(CodeableConcept)).exists() and value.where(isTypeEqual(CodeableConcept)).all(memberOf('http://hl7.org/fhir/ValueSet/languages', 'preferred') and memberOf('http://hl7.org/fhir/ValueSet/all-languages', 'required'))");

        structureDefinition = FHIRRegistry.getInstance().getResource("http://example.com/fhir/StructureDefinition/test-language-secondary-extension", StructureDefinition.class);
        generator = new ConstraintGenerator(structureDefinition);
        constraints = generator.generate();
        assertEquals(constraints.size(), 2);
        constraints.forEach(constraint -> compile(constraint.expression()));
        assertEquals(constraints.get(1).expression(), "value.where(isTypeEqual(Coding)).exists() implies (value.where(isTypeEqual(Coding)).exists() and value.where(isTypeEqual(Coding)).all(memberOf('http://hl7.org/fhir/ValueSet/languages', 'preferred') and memberOf('http://hl7.org/fhir/ValueSet/all-languages', 'required')))");

        structureDefinition = FHIRRegistry.getInstance().getResource("http://example.com/fhir/StructureDefinition/test-language-tertiary-extension", StructureDefinition.class);
        generator = new ConstraintGenerator(structureDefinition);
        constraints = generator.generate();
        assertEquals(constraints.size(), 2);
        constraints.forEach(constraint -> compile(constraint.expression()));
        assertEquals(constraints.get(1).expression(), "value.where(isTypeEqual(code)).exists() and value.where(isTypeEqual(code)).all(memberOf('http://hl7.org/fhir/ValueSet/languages', 'preferred') and memberOf('http://hl7.org/fhir/ValueSet/all-languages', 'required'))");

        structureDefinition = FHIRRegistry.getInstance().getResource("http://example.com/fhir/StructureDefinition/test-language-others-opt-extension", StructureDefinition.class);
        generator = new ConstraintGenerator(structureDefinition);
        constraints = generator.generate();
        assertEquals(constraints.size(), 2);
        constraints.forEach(constraint -> compile(constraint.expression()));
        assertEquals(constraints.get(1).expression(), "value.where(isTypeEqual(CodeableConcept)).exists() implies (value.where(isTypeEqual(CodeableConcept)).count() >= 1 and value.where(isTypeEqual(CodeableConcept)).all(memberOf('http://hl7.org/fhir/ValueSet/languages', 'preferred') and memberOf('http://hl7.org/fhir/ValueSet/all-languages', 'required')))");

        structureDefinition = FHIRRegistry.getInstance().getResource("http://example.com/fhir/StructureDefinition/test-language-others-req-extension", StructureDefinition.class);
        generator = new ConstraintGenerator(structureDefinition);
        constraints = generator.generate();
        assertEquals(constraints.size(), 2);
        constraints.forEach(constraint -> compile(constraint.expression()));
        assertEquals(constraints.get(1).expression(), "value.where(isTypeEqual(CodeableConcept)).count() >= 1 and value.where(isTypeEqual(CodeableConcept)).all(memberOf('http://hl7.org/fhir/ValueSet/languages', 'preferred') and memberOf('http://hl7.org/fhir/ValueSet/all-languages', 'required'))");
    }

    /**
     * Tests the validation of a maxValueSet.
     * @throws Exception an exception
     */
    @Test
    public void testValidator() throws Exception {

        // No warnings/error
        Device device = buildDevice();
        List<Issue> issues = FHIRValidator.validator().validate(device);
        assertEquals(FHIRValidationUtil.countErrors(issues), 0);
        assertEquals(FHIRValidationUtil.countWarnings(issues), 0);

        // Error for missing statusReason
        device = buildDevice().toBuilder().statusReason(Collections.emptyList()).build();
        issues = FHIRValidator.validator().validate(device);
        assertEquals(FHIRValidationUtil.countErrors(issues), 1);
        assertEquals(FHIRValidationUtil.countWarnings(issues), 0);

        // Warning for statusReason
        device = buildDevice().toBuilder().statusReason(Arrays.asList(
                CodeableConcept.builder()
                        .coding(Coding.builder()
                                .system(Uri.of("http://terminology.hl7.org/CodeSystem/device-status-reason"))
                                .code(Code.of("invalidCode"))
                                .build())
                        .build(),
                CodeableConcept.builder()
                        .coding(Coding.builder()
                                .system(Uri.of("invalidSystem"))
                                .code(Code.of("online"))
                                .build())
                        .build()
                )).build();
        issues = FHIRValidator.validator().validate(device);
        assertEquals(FHIRValidationUtil.countErrors(issues), 0);
        assertEquals(FHIRValidationUtil.countWarnings(issues), 0);
        assertEquals(FHIRValidationUtil.countInformation(issues), 2);

        // Warning for type
        device = buildDevice().toBuilder()
                .type(CodeableConcept.builder().coding(Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of("tlh")).build()).build()).build();
        issues = FHIRValidator.validator().validate(device);
        assertEquals(FHIRValidationUtil.countErrors(issues), 0);
        assertEquals(FHIRValidationUtil.countWarnings(issues), 0);
        assertEquals(FHIRValidationUtil.countInformation(issues), 1);

        // Error for type
        device = buildDevice().toBuilder()
                .type(CodeableConcept.builder().coding(Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of("invalidLanguage")).build()).build()).build();
        issues = FHIRValidator.validator().validate(device);
        assertEquals(FHIRValidationUtil.countErrors(issues), 2);
        assertEquals(FHIRValidationUtil.countWarnings(issues), 0);
        assertEquals(FHIRValidationUtil.countInformation(issues), 1);

        // Warning and error for specialization.systemType
        device = buildDevice().toBuilder().specialization(Arrays.asList(
                Specialization.builder().systemType(CodeableConcept.builder().coding(Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of("tlh")).build()).build()).build(),
                Specialization.builder().systemType(CodeableConcept.builder().coding(Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of("invalidSystem")).build()).build()).build())).build();
        issues = FHIRValidator.validator().validate(device);
        assertEquals(FHIRValidationUtil.countErrors(issues), 2);
        assertEquals(FHIRValidationUtil.countWarnings(issues), 0);
        assertEquals(FHIRValidationUtil.countInformation(issues), 2);

        // Warning and error for safety
        device = buildDevice().toBuilder().safety(Arrays.asList(
                CodeableConcept.builder().coding(Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of("tlh")).build()).build(),
                CodeableConcept.builder().coding(Coding.builder().system(Uri.of("invalidSystem")).code(Code.of(ENGLISH_US)).build()).build()
                )).build();
        issues = FHIRValidator.validator().validate(device);
        assertEquals(FHIRValidationUtil.countErrors(issues), 2);
        assertEquals(FHIRValidationUtil.countWarnings(issues), 0);
        assertEquals(FHIRValidationUtil.countInformation(issues), 2);

        // Warning for test-language-primary-extension
        device = buildDevice().toBuilder()
                .extension(Collections.singletonList(Extension.builder().url("http://example.com/fhir/StructureDefinition/test-language-primary-extension")
                .value(CodeableConcept.builder().coding(Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of("tlh")).build()).build()).build())).build();
        issues = FHIRValidator.validator().validate(device);
        assertEquals(FHIRValidationUtil.countErrors(issues), 0);
        assertEquals(FHIRValidationUtil.countWarnings(issues), 0);
        assertEquals(FHIRValidationUtil.countInformation(issues), 1);

        // Error for test-language-primary-extension
        device = buildDevice().toBuilder()
                .extension(Collections.singletonList(Extension.builder().url("http://example.com/fhir/StructureDefinition/test-language-primary-extension")
                .value(CodeableConcept.builder().coding(Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of("invalidLanguage")).build()).build()).build())).build();
        issues = FHIRValidator.validator().validate(device);
        assertEquals(FHIRValidationUtil.countErrors(issues), 2);
        assertEquals(FHIRValidationUtil.countWarnings(issues), 0);
        assertEquals(FHIRValidationUtil.countInformation(issues), 2);

        // Warning for test-language-secondary-extension
        device = buildDevice().toBuilder()
                .extension(Collections.singletonList(Extension.builder().url("http://example.com/fhir/StructureDefinition/test-language-secondary-extension")
                .value(Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of("tlh")).build()).build())).build();
        issues = FHIRValidator.validator().validate(device);
        assertEquals(FHIRValidationUtil.countErrors(issues), 0);
        assertEquals(FHIRValidationUtil.countWarnings(issues), 0);
        assertEquals(FHIRValidationUtil.countInformation(issues), 2);

        // Error for test-language-secondary-extension
        device = buildDevice().toBuilder()
                .extension(Collections.singletonList(Extension.builder().url("http://example.com/fhir/StructureDefinition/test-language-secondary-extension")
                .value(Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of("invalidLanguage")).build()).build())).build();
        issues = FHIRValidator.validator().validate(device);
        assertEquals(FHIRValidationUtil.countErrors(issues), 2);
        assertEquals(FHIRValidationUtil.countWarnings(issues), 0);
        assertEquals(FHIRValidationUtil.countInformation(issues), 3);

        // Warning for test-language-tertiary-extension
        device = buildDevice().toBuilder()
                .extension(Collections.singletonList(Extension.builder().url("http://example.com/fhir/StructureDefinition/test-language-tertiary-extension")
                .value(Code.of("tlh")).build())).build();
        issues = FHIRValidator.validator().validate(device);
        assertEquals(FHIRValidationUtil.countErrors(issues), 0);
        assertEquals(FHIRValidationUtil.countWarnings(issues), 0);
        assertEquals(FHIRValidationUtil.countInformation(issues), 2);

        // Error for test-language-tertiary-extension
        device = buildDevice().toBuilder()
                .extension(Collections.singletonList(Extension.builder().url("http://example.com/fhir/StructureDefinition/test-language-tertiary-extension")
                .value(Code.of("invalidLanguage")).build())).build();
        issues = FHIRValidator.validator().validate(device);
        assertEquals(FHIRValidationUtil.countErrors(issues), 2);
        assertEquals(FHIRValidationUtil.countWarnings(issues), 0);
        assertEquals(FHIRValidationUtil.countInformation(issues), 3);
    }

    /**
     * Builds a device that will validate successfully.
     * @return a device
     */
    private Device buildDevice() {
        Device device = Device.builder()
            .text(Narrative.builder().div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Generated</div>")).status(NarrativeStatus.GENERATED).build())
            .meta(Meta.builder()
                .profile(Canonical.of("http://example.com/fhir/StructureDefinition/test-device|0.1.0"))
                .build())
            .statusReason(CodeableConcept.builder().coding(Coding.builder().system(Uri.of("http://terminology.hl7.org/CodeSystem/device-status-reason")).code(Code.of("online")).build()).build())
            .specialization(Specialization.builder()
                    .systemType(CodeableConcept.builder().coding(Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of(ENGLISH_US)).build()).build()).build())
            .extension(Extension.builder().url("http://example.com/fhir/StructureDefinition/test-language-primary-extension")
                    .value(CodeableConcept.builder().coding(Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of(ENGLISH_US)).build()).build()).build(),
                    Extension.builder().url("http://example.com/fhir/StructureDefinition/test-language-secondary-extension")
                    .value(Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of(ENGLISH_US)).build()).build(),
                    Extension.builder().url("http://example.com/fhir/StructureDefinition/test-language-tertiary-extension")
                    .value(Code.of(ENGLISH_US)).build())
            .build();
        return device;
    }
}