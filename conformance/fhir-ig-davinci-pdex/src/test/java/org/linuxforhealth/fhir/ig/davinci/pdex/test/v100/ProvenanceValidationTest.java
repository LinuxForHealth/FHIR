/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.ig.davinci.pdex.test.v100;

import static org.linuxforhealth.fhir.validation.util.FHIRValidationUtil.countErrors;
import static org.linuxforhealth.fhir.validation.util.FHIRValidationUtil.countInformation;
import static org.linuxforhealth.fhir.validation.util.FHIRValidationUtil.countWarnings;

import java.io.InputStream;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.resource.OperationOutcome.Issue;
import org.linuxforhealth.fhir.model.resource.Provenance;
import org.linuxforhealth.fhir.validation.FHIRValidator;

public class ProvenanceValidationTest {
    @Test
    public void testProvenanceValidation1() throws Exception {

        try (InputStream in = ProvenanceValidationTest.class.getClassLoader().getResourceAsStream("JSON/100/Provenance-Practitioner.json")) {
            Provenance provenance = FHIRParser.parser(Format.JSON).parse(in);
            List<Issue> issues = FHIRValidator.validator().validate(provenance);
            issues.forEach(System.out::println);
            Assert.assertEquals(countErrors(issues), 0);
            Assert.assertEquals(countWarnings(issues), 0);
            Assert.assertEquals(countInformation(issues), 1);
        }
    }
}