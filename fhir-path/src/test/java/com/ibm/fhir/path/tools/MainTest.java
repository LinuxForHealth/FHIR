/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.path.tools;

import org.testng.annotations.Test;

/**
 * Tests the fhir-path-cli
 */
public class MainTest {
    @Test
    public void testCliHelp1() {
        String[] args = {"--help"};
        Main.main(args);
    }
    @Test
    public void testCliHelp2() {
        String[] args = {"-?"};
        Main.main(args);
    }
    @Test(expectedExceptions= {IllegalArgumentException.class})
    public void testInvalidValue() {
        String[] args = {"--invalid"};
        Main.main(args);
    }

    @Test(expectedExceptions= {IllegalArgumentException.class})
    public void testPathWithoutValue() {
        String[] args = {"--path"};
        Main.main(args);
    }

    @Test(expectedExceptions= {IllegalArgumentException.class})
    public void testPathWithEmptyValue() {
        String[] args = {"--path", ""};
        Main.main(args);
    }

    @Test(expectedExceptions= {IllegalArgumentException.class})
    public void testFormatWithoutValue() {
        String[] args = {"--format"};
        Main.main(args);
    }

    @Test(expectedExceptions= {IllegalArgumentException.class})
    public void testFormatWithBadValueEmpty() {
        String[] args = {"--format", ""};
        Main.main(args);
    }

    @Test(expectedExceptions= {IllegalArgumentException.class})
    public void testFormatWithBadValue() {
        String[] args = {"--format", "badbadbad"};
        Main.main(args);
    }

    @Test(expectedExceptions= {IllegalArgumentException.class})
    public void testFileWithBadEmpty() {
        String[] args = {"--file", ""};
        Main.main(args);
    }

    @Test(expectedExceptions= {IllegalArgumentException.class})
    public void testFileWithBadValue() {
        String[] args = {"--file", "badbadbad.json"};
        Main.main(args);
    }

    @Test(expectedExceptions= {IllegalArgumentException.class})
    public void testResourceWithBadEmpty() {
        String[] args = {"--resource", ""};
        Main.main(args);
    }

    @Test(expectedExceptions= {IllegalArgumentException.class})
    public void testResourceWithBadValue() {
        String[] args = {"--resource", "invalid"};
        Main.main(args);
    }

    @Test(expectedExceptions= {IllegalArgumentException.class})
    public void testPrettyAndNothingElse(){
        String[] args = {"--pretty"};
        Main.main(args);
    }

    @Test(expectedExceptions= {IllegalArgumentException.class})
    public void testFileWithPrettyAndNothingElse(){
        String[] args = {"--file", "../fhir-examples/src/main/resources/json/profiles/fhir-ig-davinci-pdex/Bundle-2000002.json"};
        Main.main(args);
    }

    @Test(expectedExceptions= {IllegalArgumentException.class})
    public void testTypeStdin() {
        String[] args = {"--type", "stdin"};
        Main.main(args);
    }

    @Test(expectedExceptions= {IllegalArgumentException.class})
    public void testTypeFile() {
        String[] args = {"--type", "file"};
        Main.main(args);
    }

    @Test(expectedExceptions= {IllegalArgumentException.class})
    public void testTypeResource() {
        String[] args = {"--type", "resource"};
        Main.main(args);
    }

    @Test(expectedExceptions= {IllegalArgumentException.class})
    public void testTypeResourceAndFileTriggerAMissingType() {
        String[] args = {"--resource", "resource", "--stdin"};
        Main.main(args);
    }

    @Test(expectedExceptions= {IllegalArgumentException.class})
    public void testTypeResourceAndInvalidType() {
        String[] args = {"--resource", "resource", "--type", "x"};
        Main.main(args);
    }

    @Test(expectedExceptions= {IllegalArgumentException.class})
    public void testTypeResourceAndNoType() {
        String[] args = {"--resource", "resource", "--pretty"};
        Main.main(args);
    }

    @Test(expectedExceptions= {IllegalArgumentException.class})
    public void testStrict() {
        String[] args = {"--rx"};
        Main.main(args);
    }

    @Test(expectedExceptions= {})
    public void testFileValid() {
        String[] args = {"--file", "../fhir-examples/src/main/resources/json/profiles/fhir-ig-davinci-pdex/Bundle-2000002.json",
                "--path", "entry.resource.id"};
        Main.main(args);
    }

    @Test(expectedExceptions= {})
    public void testFileValidWithPretty() {
        String[] args = {"--file", "../fhir-examples/src/main/resources/json/profiles/fhir-ig-davinci-pdex/Bundle-2000002.json",
                "--path", "entry.resource.id", "--pretty"};
        Main.main(args);
    }

    @Test(expectedExceptions= {})
    public void testFileValidWithResource() {
        String[] args = {"--file", "../fhir-examples/src/main/resources/json/profiles/fhir-ig-davinci-pdex/Bundle-2000002.json",
                "--path", "entry.resource"};
        Main.main(args);
    }

    @Test(expectedExceptions= {java.lang.IllegalArgumentException.class})
    public void testFileInvalidWithResourceBadFhirPathLang() {
        String[] args = {"--file", "../fhir-examples/src/main/resources/json/profiles/fhir-ig-davinci-pdex/Bundle-2000002.json",
                "--path", ".entry.resourcex"};
        Main.main(args);
    }

    @Test(expectedExceptions= {})
    public void testFileWithResourceBadPath() {
        String[] args = {"--file", "../fhir-examples/src/main/resources/json/profiles/fhir-ig-davinci-pdex/Bundle-2000002.json",
                "--path", "entry.resourcex"};
        Main.main(args);
    }

    @Test(expectedExceptions= {})
    public void testFileWithResourceTimestamp() {
        String[] args = {"--file", "../fhir-examples/src/main/resources/json/profiles/fhir-ig-davinci-pdex/Bundle-2000002.json",
                "--path", "entry.timestamp"};
        Main.main(args);
    }

    @Test(expectedExceptions= {})
    public void testFileWithResourceQuantity() {
        String[] args = {"--file", "../fhir-examples/src/main/resources/json/profiles/fhir-ig-davinci-pdex/Bundle-2000002.json",
                "--path", "entry.resource.quantity"};
        Main.main(args);
    }

    @Test(expectedExceptions= {})
    public void testFileWithPatientResourceBoolean() {
        String[] args = {"--file", "../fhir-examples//src/main/resources/json/profiles/fhir-ig-davinci-pdex/Patient-1.json",
                "--path", "active"};
        Main.main(args);
    }

    @Test(expectedExceptions= {})
    public void testFileWithPatientResourceDateTime() {
        String[] args = {"--file", "../fhir-examples//src/main/resources/json/profiles/fhir-ig-davinci-pdex/Patient-1.json",
                "--path", "meta.lastUpdated.value"};
        Main.main(args);
    }

    @Test(expectedExceptions= {})
    public void testFileWithPatientResourceDate() {
        String[] args = {"--file", "../fhir-examples//src/main/resources/json/profiles/fhir-ig-davinci-pdex/Patient-1.json",
                "--path", "birthDate.value"};
        Main.main(args);
    }
}