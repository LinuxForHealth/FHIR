/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.us.core.test;

import static com.ibm.fhir.validation.util.FHIRValidationUtil.countErrors;

import java.io.Reader;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.examples.ExamplesUtil;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.validation.FHIRValidator;

/**
 * This test is used to diagnose and test Code 'L/min' is invalid
 */
public class ObservationPulseOximetryTest {
    private String path = "json/profiles/fhir-ig-us-core/Observation-satO2-fiO2.json";
    private Format format = Format.JSON;

    @Test
    public void testUSCorePulseOximetry() throws Exception {
        try (Reader r = ExamplesUtil.resourceReader(path)) {
            Resource resource = FHIRParser.parser(format).parse(r);
            List<Issue> issues = FHIRValidator.validator().validate(resource);
            issues.forEach(item -> {
                if (item.getSeverity().getValue().equals("error")) {
                    System.out.println(item);
                }
            });
            Assert.assertEquals(countErrors(issues), 0);
        } catch (Exception e) {
            System.out.println("Exception with " + path);
            throw e;
        }
    }
}