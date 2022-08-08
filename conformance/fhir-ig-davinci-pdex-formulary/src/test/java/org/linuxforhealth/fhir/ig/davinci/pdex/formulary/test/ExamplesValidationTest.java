/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.ig.davinci.pdex.formulary.test;

import static org.linuxforhealth.fhir.validation.util.FHIRValidationUtil.countErrors;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.io.File;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.testng.ITest;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.resource.OperationOutcome.Issue;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.registry.test.ExampleIndex;
import org.linuxforhealth.fhir.validation.FHIRValidator;

public class ExamplesValidationTest implements ITest {
    private static final String EXAMPLES_PATH = "src/test/resources/examples/";
    private static final String INDEX_FILE_NAME = ".index.json";

    private final String path;

    public ExamplesValidationTest(String path) {
        this.path = path;
    }

    @Override
    public String getTestName() {
        if (!path.startsWith(EXAMPLES_PATH)) {
            throw new IllegalArgumentException("unexpected test path");
        }
        return path.substring(EXAMPLES_PATH.length());
    }

    @Test
    public void testDaVinciFormularyValidation() throws Exception {
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
            fail("Exception with " + path, e);
        }
    }

    @Factory
    public Object[] createInstances() {
        List<Object> result = new ArrayList<>();

        File[] directories = new File(EXAMPLES_PATH).listFiles(File::isDirectory);
        for (File versionDir : directories) {
            String versionPath = EXAMPLES_PATH + versionDir.getName() + "/";
            for (ExampleIndex.Entry entry : ExampleIndex.readIndex(Paths.get(versionPath, INDEX_FILE_NAME))) {
                result.add(new ExamplesValidationTest(versionPath + entry.getFileName()));
            }
        }

        return result.toArray();
    }

    public static void main(String[] args) throws Exception {
        new ExamplesValidationTest(EXAMPLES_PATH + "101/List-covplanV1002.json")
            .testDaVinciFormularyValidation();
    }
}