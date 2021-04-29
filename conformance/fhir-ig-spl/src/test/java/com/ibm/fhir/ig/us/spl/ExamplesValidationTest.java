/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.us.spl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.testng.annotations.Test;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.validation.FHIRValidator;
import com.ibm.fhir.validation.exception.FHIRValidationException;

public class ExamplesValidationTest {
    @Test
    public void testValidationJson() throws Exception {
        try (Stream<Path> paths1 = Files.list(Paths.get("src/test/resources/json"));
                Stream<Path> paths2 = Files.list(Paths.get("src/test/resources/json"))) {
            List<Path> badPaths = paths1.filter(p -> verify(p)).collect(Collectors.toList());
            List<Path> goodPaths = paths2.filter(p -> !verify(p)).collect(Collectors.toList());

            System.out.println("Bad Paths");
            badPaths.forEach(px -> System.out.println(px));
            System.out.println();
            System.out.println("Good Paths");
            goodPaths.forEach(px -> System.out.println(px));
        }
    }

    @Test
    public void testValidationWithOrganization() throws Exception {
        verify(Paths.get("src/test/resources/json/Organization-ExampleEstablishment.json"));
    }

    public static boolean verify(Path p) {
        boolean invalid = false;
        try (InputStream in = new FileInputStream(p.toFile())) {
            System.out.println("Path: " + p);
            Resource r = FHIRParser.parser(Format.JSON).parse(in);
            List<Issue> validate = FHIRValidator.validator().validate(r);
            for (Issue issue : validate) {
                if (IssueSeverity.ERROR.equals(issue.getSeverity()) || IssueSeverity.ERROR.equals(issue.getSeverity())) {
                    System.out.println(issue);
//                  System.out.println(r);
                    invalid = true;
                }
            }
            System.out.println("--------------------------------");
        } catch (IOException | FHIRParserException e) {
            e.printStackTrace();
            invalid = true;
        } catch (FHIRValidationException e) {
            e.printStackTrace();
            invalid = true;
        }
        return invalid;
    }
}