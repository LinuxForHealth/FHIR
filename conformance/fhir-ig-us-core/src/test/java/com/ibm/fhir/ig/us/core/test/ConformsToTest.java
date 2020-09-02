/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.us.core.test;

import static org.testng.Assert.assertEquals;

import java.io.InputStream;
import java.util.List;

import org.testng.annotations.Test;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.validation.FHIRValidator;

public class ConformsToTest {
    @Test
    public void testConformsToWithEmptyContext() throws Exception {
        try (InputStream in = ConformsToTest.class.getClassLoader().getResourceAsStream("JSON/us-core-patient.json")) {
            Patient patient = FHIRParser.parser(Format.JSON).parse(in);
            List<Issue> issues = FHIRValidator.validator().validate(patient);
            issues.forEach(System.out::println);
            assertEquals(issues.size(), 2);
        }
    }
}