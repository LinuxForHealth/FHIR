/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.us.core.test;

import static com.ibm.fhir.validation.util.FHIRValidationUtil.countErrors;

import java.io.InputStream;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.validation.FHIRValidator;

public class USCoreValidationTest {
    @Test
    public void testUSCoreValidation1() throws Exception {
        try (InputStream in = USCoreValidationTest.class.getClassLoader().getResourceAsStream("JSON/us-core-patient-no-name-asserted.json")) {
            Patient patient = FHIRParser.parser(Format.JSON).parse(in);
            List<Issue> issues = FHIRValidator.validator().validate(patient);
            issues.forEach(System.out::println);
            Assert.assertEquals(countErrors(issues), 1);
        }
    }

    @Test
    public void testUSCoreValidation2() throws Exception {
        try (InputStream in = USCoreValidationTest.class.getClassLoader().getResourceAsStream("JSON/us-core-patient-no-name-not-asserted.json")) {
            Patient patient = FHIRParser.parser(Format.JSON).parse(in);
            List<Issue> issues = FHIRValidator.validator().validate(patient);
            issues.forEach(System.out::println);
            Assert.assertEquals(countErrors(issues), 0);
        }
    }

    @Test
    public void testUSCoreValidation3() throws Exception {
        try (InputStream in = USCoreValidationTest.class.getClassLoader().getResourceAsStream("XML/us-core-patient-no-name-asserted.xml")) {
            Patient patient = FHIRParser.parser(Format.XML).parse(in);
            List<Issue> issues = FHIRValidator.validator().validate(patient);
            issues.forEach(System.out::println);
            Assert.assertEquals(countErrors(issues), 1);
        }
    }

    @Test
    public void testUSCoreValidation4() throws Exception {
        try (InputStream in = USCoreValidationTest.class.getClassLoader().getResourceAsStream("XML/us-core-patient-no-name-not-asserted.xml")) {
            Patient patient = FHIRParser.parser(Format.XML).parse(in);
            List<Issue> issues = FHIRValidator.validator().validate(patient);
            issues.forEach(System.out::println);
            Assert.assertEquals(countErrors(issues), 0);
        }
    }

    @Test
    public void testUSCoreValidation5() throws Exception {
        try (InputStream in = USCoreValidationTest.class.getClassLoader().getResourceAsStream("JSON/Pamela954_Johns824_4818eca9-c6d2-4fa0-a234-7244e620391e.json")) {
            Bundle bundle = FHIRParser.parser(Format.JSON).parse(in);
            FHIRValidator validator = FHIRValidator.validator();
            List<Issue> issues = validator.validate(bundle);
            Assert.assertEquals(countErrors(issues), 0);
        }
    }

    @Test
    public void testUSCoreValidation6() throws Exception {
        try (InputStream in = USCoreValidationTest.class.getClassLoader().getResourceAsStream("JSON/1.json")) {
            Observation observation = FHIRParser.parser(Format.JSON).parse(in);
            List<Issue> issues = FHIRValidator.validator().validate(observation);
            Assert.assertEquals(countErrors(issues), 0);
        }
    }
}
