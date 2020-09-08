/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.davinci.pdex.test;

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

public class ProfileTest {
    @SuppressWarnings("unused")
    private String expectation = null;
    private String path = null;
    private Format format = Format.JSON;

    public ProfileTest(String expectation, String path, boolean json) {
        this.expectation = expectation;
        this.path = path;

        if (!json) {
            this.format = Format.XML;
        }
    }

    @Test
    public void testUSCoreValidation() throws Exception {
        try (Reader r = ExamplesUtil.resourceReader(path)) {
            System.out.println("Davinci EPDX Testing -> " + path);
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