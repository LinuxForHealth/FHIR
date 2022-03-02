/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.davinci.pdex.test.v200;

import static com.ibm.fhir.validation.util.FHIRValidationUtil.countErrors;
import static org.testng.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Factory;
import org.testng.annotations.Test;

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

    public ProfileTest() {
        // No Operation
    }

    public ProfileTest(String path) {
        this.path = path;
    }

    @Test
    public void testPDexValidation() throws Exception {
        try (Reader r = new InputStreamReader(getClass().getResourceAsStream(path))) {
            Resource resource = FHIRParser.parser(Format.JSON).parse(r);
            List<Issue> issues = FHIRValidator.validator().validate(resource);
            issues.forEach(item -> {
                if (item.getSeverity().getValue().equals("error")) {
                    System.out.println("Davinci PDex Testing -> " + path);
                    System.out.println(item);
                }
            });
            assertEquals(countErrors(issues), 0);
        } catch (Exception e) {
            System.out.println("Exception with " + path);
            throw e;
        }
    }

    @Factory
    public Object[] createInstances() {
        List<Object> result = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/JSON/200/profiles-pdex-json.txt")))) {
            String line;
            while ((line = br.readLine()) != null) {
                result.add(new ProfileTest(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toArray();
    }
}