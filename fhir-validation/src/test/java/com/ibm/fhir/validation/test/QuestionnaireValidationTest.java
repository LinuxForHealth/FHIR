/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.validation.test;

import java.io.Reader;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.examples.ExamplesUtil;
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Questionnaire;
import com.ibm.fhir.model.resource.StructureDefinition;
import com.ibm.fhir.profile.ConstraintGenerator;
import com.ibm.fhir.profile.ProfileSupport;
import com.ibm.fhir.validation.FHIRValidator;

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
