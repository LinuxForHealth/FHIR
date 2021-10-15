/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.us.core.test.v400;

import static com.ibm.fhir.validation.util.FHIRValidationUtil.countErrors;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.validation.FHIRValidator;

/**
 * Runs through the Profile index for 4.0.0
 */
public class ProfileTest {

    private static final String INDEX = "./src/test/resources/JSON/400/index.txt";

    private String path = null;

    public ProfileTest() {
        // No Operation
    }

    public ProfileTest(String path) {
        this.path = path;
    }

    @Test
    public void testUSCoreValidation() throws Exception {
        try (Reader r = Files.newBufferedReader(Paths.get(path))) {
            Resource resource = FHIRParser.parser(Format.JSON).parse(r);
            List<Issue> issues = FHIRValidator.validator().validate(resource);
            issues.forEach(item -> {
                if (item.getSeverity().getValue().equals("error")) {
                    System.out.println(path + " " + item);
                }
            });
            assertEquals(countErrors(issues), 0);
        } catch (Exception e) {
            System.out.println("Exception with " + path);
            fail(e.toString());
        }
    }

    @Factory
    public Object[] createInstances() {
        List<Object> result = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(INDEX))) {
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