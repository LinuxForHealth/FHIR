/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.validation.test;

import java.io.Reader;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.examples.ExamplesUtil;
import org.linuxforhealth.fhir.model.annotation.Constraint;
import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.resource.OperationOutcome.Issue;
import org.linuxforhealth.fhir.model.resource.Questionnaire;
import org.linuxforhealth.fhir.model.resource.StructureDefinition;
import org.linuxforhealth.fhir.profile.ConstraintGenerator;
import org.linuxforhealth.fhir.profile.ProfileSupport;
import org.linuxforhealth.fhir.validation.FHIRValidator;

public class QuestionnaireValidationTest {
    @Test
    public void testQuestionnaireValidation() throws Exception {
        try (Reader reader = ExamplesUtil.resourceReader("json/spec/questionnaire-cqf-example.json")) {
            Questionnaire questionnaire = FHIRParser.parser(Format.JSON).parse(reader);

            StructureDefinition profile = ProfileSupport.getProfile(questionnaire.getMeta().getProfile().get(0).getValue());
            ConstraintGenerator generator = new ConstraintGenerator(profile);
            List<Constraint> constraints = generator.generate();
            constraints.forEach(System.out::println);

            List<Issue> issues = FHIRValidator.validator().validate(questionnaire);
            issues.forEach(System.out::println);

            Assert.assertEquals(issues.size(), 0);
        }
    }
}
