/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.validation.test;

import java.io.InputStream;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.generator.FHIRGenerator;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.resource.Bundle;
import org.linuxforhealth.fhir.model.resource.Bundle.Entry;
import org.linuxforhealth.fhir.model.resource.ExplanationOfBenefit;
import org.linuxforhealth.fhir.model.resource.OperationOutcome.Issue;
import org.linuxforhealth.fhir.model.type.code.BundleType;
import org.linuxforhealth.fhir.model.type.code.IssueSeverity;
import org.linuxforhealth.fhir.validation.FHIRValidator;

public class ExplanationOfBenefitTest {
    @Test
    public static void testExplanationOfBenefit() throws Exception {
        try (InputStream in = ExplanationOfBenefitTest.class.getClassLoader().getResourceAsStream("JSON/explanationofbenefit.json")) {
            ExplanationOfBenefit explanationOfBenefit = FHIRParser.parser(Format.JSON).parse(in);
            FHIRGenerator.generator(Format.JSON, true).generate(explanationOfBenefit, System.out);
            FHIRValidator.validator().validate(explanationOfBenefit).forEach(System.out::println);
            Bundle bundle = Bundle.builder()
                .type(BundleType.COLLECTION)
                .entry(Entry.builder()
                    .resource(explanationOfBenefit)
                    .build())
                .build();
            List<Issue> issues = FHIRValidator.validator().validate(bundle);
            Assert.assertEquals(issues.size(), 3);
            for (Issue issue : issues) {
                Assert.assertNotEquals(issue.getSeverity(), IssueSeverity.ERROR);
            }
        }
    }
}