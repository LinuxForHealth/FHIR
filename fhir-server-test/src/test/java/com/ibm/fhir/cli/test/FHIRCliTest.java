/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.cli.test;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.reporters.Files;

import com.ibm.fhir.cli.FHIRCLI;
import com.ibm.fhir.server.test.FHIRServerTestBase;

/**
 * Tests the FHIR-CLI (FHIR Command Line Interface) tool.
 *
 */
public class FHIRCliTest extends FHIRServerTestBase {
    // To see testcase output for debugging, set this to true.
    private boolean debug = false;

    private FHIRCLI cli = null;
    private String consoleOutput = null;
    private static final String dirPrefix = "target/test-classes";

    private static String patientId = null;

    // used to limit the number of result from searchAll to avoid timeouts for large dbs
    private static LocalDate dateOfTest = LocalDate.now();

    private static final Pattern locationURIPattern = Pattern.compile("URI:\\s*(\\S*)");

    private boolean deleteSupported = false;

    /**
     * Retrieve the server's conformance statement to determine the status of certain runtime options.
     *
     * @throws Exception
     */
    @BeforeClass
    public void retrieveConfig() throws Exception {
        deleteSupported = isDeleteSupported();
        System.out.println("Delete operation supported?: " + Boolean.valueOf(deleteSupported).toString());
    }

    @Test(expectedExceptions = {
            IllegalStateException.class
    })
    public void testMetadataError1() throws Exception {
        runTest("testMetadataError1", "-p", propsFile());
    }

    @Test
    public void testMetadataToConsole() throws Exception {
        runTest("testMetadataToConsole", "-p", propsFile(), "--operation", "metadata");
        verifyConsoleOutput("IBM FHIR Server", "(C) Copyright IBM Corporation 2016, 2021", "4.0.1");
    }

    @Test
    public void testMetadataToFile() throws Exception {
        runTest("testMetadataToFile", "-p", propsFile(), "--operation", "metadata", "--output", dirPrefix("metadata.json"));
        verifyFileContents("metadata.json", "IBM FHIR Server", "(C) Copyright IBM Corporation 2016, 2021", "4.0.1");
    }

    @Test
    public void testHelp() throws Exception {
        runTest("testHelp", "--help");
        verifyConsoleOutput("Provides access to the FHIR Client API via the command line");
    }

    @Test(expectedExceptions = {
            IllegalArgumentException.class
    })
    public void testBadOption() throws Exception {
        runTest("testBadOption", "--BADOPTION");
    }

    @Test
    public void testCreatePatient() throws Exception {
        runTest("testCreatePatient", "-p", propsFile(), "--operation", "create", "--resource", testData("Patient_MookieBetts.json"));
        verifyConsoleOutput("Status code: 201", "ETag: W/\"1\"", "Location URI: http", "Last modified:");
        patientId = getResourceIdFromConsoleOutput();
        assertNotNull(patientId);
    }

    @Test(dependsOnMethods={"testCreatePatient"})
    public void testReadPatient() throws Exception {
        assertNotNull(patientId);
        runTest("testReadPatient", "-p", propsFile(), "--operation", "read", "--type", "Patient", "--resourceId", patientId, "-o", dirPrefix("readPatient.json"));
        verifyConsoleOutput("Status code: 200", "ETag: W/\"1\"");
    }

    @Test(dependsOnMethods={"testReadPatient"})
    public void testUpdatePatient() throws Exception {
        assertNotNull(patientId);
        runTest("testUpdatePatient", "-p", propsFile(), "--operation", "update", "--resource", dirPrefix("readPatient.json"));
        verifyConsoleOutput("Status code: 200", "ETag: W/\"2\"");
    }

    @Test(dependsOnMethods={"testUpdatePatient"})
    public void testVreadPatient() throws Exception {
        assertNotNull(patientId);
        runTest("testVreadPatient", "-p", propsFile(), "--operation", "vread", "--type", "Patient", "--resourceId", patientId, "--versionId", "2", "-o", dirPrefix("vreadPatient.json"));
        verifyConsoleOutput("Status code: 200", "ETag: W/\"2\"");
    }

    @Test(dependsOnMethods={"testVreadPatient"})
    public void testHistoryPatient() throws Exception {
        assertNotNull(patientId);
        runTest("testHistoryPatient", "-p", propsFile(), "--operation", "history", "--type", "Patient", "--resourceId", patientId);
        verifyConsoleOutput("Status code: 200", patientId, "\"total\":2");
    }

    @Test(dependsOnMethods={"testHistoryPatient"})
    public void testSearchPatient() throws Exception {
        assertNotNull(patientId);
        runTest("testSearchPatient", "-p", propsFile(), "--operation", "search", "--type", "Patient", "--queryParam", "_id=" + patientId, "-o", dirPrefix("searchPatients.json"));
        verifyConsoleOutput("Status code: 200");
        verifyFileContents("searchPatients.json", "\"total\":1");
    }

    @Test(dependsOnMethods={"testSearchPatient"})
    public void testSearchAll() throws Exception {
        runTest("testSearchAll", "-p", propsFile(), "--operation", "search-all", "-qp", "_count=100", "-qp", "_lastUpdated=ge" + dateOfTest , "-o", dirPrefix("searchAll.json"));
        verifyConsoleOutput("Status code: 200");
    }

    @Test(dependsOnMethods={"testSearchAll"})
    public void testDeletePatient() throws Exception {
        assertNotNull(patientId);
        runTest("testDeletePatient", "-p", propsFile(), "--operation", "delete", "--type", "Patient", "--resourceId", patientId);
        if (deleteSupported) {
            verifyConsoleOutput("Status code: 200", "ETag: W/\"3\"");
        } else {
            verifyConsoleOutput("Status code: 405");

            // Next, do an update so that future version checks can work consistently
            // with or without delete support.
            runTest("testUpdatePatient", "-p", propsFile(), "--operation", "update", "--resource", dirPrefix("readPatient.json"));
            verifyConsoleOutput("Status code: 200", "ETag: W/\"3\"");
        }
    }

    @Test(dependsOnMethods={"testDeletePatient"})
    public void testReadDeletedPatient() throws Exception {
        assertNotNull(patientId);
        runTest("testReadDeletedPatient", "-p", propsFile(), "--operation", "read", "--type", "Patient", "--resourceId", patientId);
        if (deleteSupported) {
            verifyConsoleOutput("Status code: 410", "Resource", "is deleted.");
        } else {
            verifyConsoleOutput("Status code: 200");
        }
    }

    @Test(dependsOnMethods={"testReadDeletedPatient"})
    public void testUpdateDeletedPatient() throws Exception {
        String currentVersion = "3";
        String newVersion = "4";
        String ifMatchValue = "W/\"" + currentVersion + "\"";
        runTest("testUpdateDeletedPatient", "-p", propsFile(), "--operation", "update", "--resource", dirPrefix("vreadPatient.json"), "-H", "If-Match=" + ifMatchValue);
        verifyConsoleOutput("Status code: 201", "ETag: W/\"" + newVersion + "\"");
    }

    @Test(dependsOnMethods={"testUpdateDeletedPatient"})
    public void testConditionalUpdatePatient() throws Exception {
        assertNotNull(patientId);
        runTest("testConditionalUpdatePatient", "-p", propsFile(), "--operation", "conditional-update", "--resource", dirPrefix("vreadPatient.json"), "-qp", "_id=" + patientId);
        verifyConsoleOutput("Status code: 200", "ETag: W/\"5\"");
    }

    @Test(dependsOnMethods={"testConditionalUpdatePatient"})
    public void testConditionalDeletePatient() throws Exception {
        if (!deleteSupported) {
            return;
        }

        assertNotNull(patientId);
        runTest("testConditionalDeletePatient", "-p", propsFile(), "--operation", "conditional-delete", "--type", "Patient", "-qp", "_id=" + patientId);
        verifyConsoleOutput("Status code: 200", "ETag: W/\"6\"");
    }

    @Test(dependsOnMethods={"testConditionalDeletePatient"})
    public void testConditionalDeletePatientTwice() throws Exception {
        if (!deleteSupported) {
            return;
        }

        runTest("testConditionalDeletePatientError", "-p", propsFile(), "--operation", "conditional-delete", "--type", "Patient", "-qp", "_id=" + UUID.randomUUID().toString());
        verifyConsoleOutput("Status code: 200");
    }

    @Test(dependsOnMethods={"testCreatePatient"})
    public void testConditionalCreatePatient() throws Exception {
        runTest("testConditionalCreatePatient", "-p", propsFile(), "--operation", "conditional-create", "--resource", testData("Patient_MookieBetts.json"), "-qp", "_id=" + patientId);
        // According to https://www.hl7.org/fhir/R4/http.html#ccreate , conditional create against an existing resource should return 200
        verifyConsoleOutput("Status code: 200");
    }

    @Test(dependsOnMethods={"testCreatePatient"})
    public void testConditionalCreatePatientMultipleMatch() throws Exception {
        // Create an additional patient just to make sure there's multiple
        runTest("testCreatePatient", "-p", propsFile(), "--operation", "create", "--resource", testData("Patient_DavidOrtiz.json"));
        // Now the real test
        runTest("testConditionalCreatePatientError", "-p", propsFile(), "--operation", "conditional-create", "--resource", testData("Patient_MookieBetts.json"), "-qp", "gender=male");
        verifyConsoleOutput("Status code: 412");
    }

    @Test(dependsOnMethods={"testCreatePatient"})
    public void testConditionalCreatePatientError() throws Exception {
        runTest("testConditionalCreatePatientError", "-p", propsFile(), "--operation", "conditional-create", "--resource", testData("Patient_MookieBetts.json"), "-qp", "BADSEARCHPARAM=XXX");
        // Assumes the server is in "strict" mode by default
        verifyConsoleOutput("Status code: 400");
    }

    @Test
    public void testBatch() throws Exception {
        runTest("testBatch", "-p", propsFile(), "--operation", "batch", "-r", testData("batchCreates.json"));
        verifyConsoleOutput("Status code: 200");
    }

    @Test
    public void testTransaction() throws Exception {
        runTest("testTransaction", "-p", propsFile(), "--operation", "transaction", "-r", testData("transactionCreates.json"));
        verifyConsoleOutput("Status code: 200");
    }

    @Test
    public void testValidate() throws Exception {
        runTest("testValidate", "-p", propsFile(), "--operation", "validate", "-r", testData("Patient_MookieBetts.json"));
        verifyConsoleOutput("Status code: 200");
    }

    /**
     * Retrieves the resource id value from the location URI string contained in the console output.
     */
    private String getResourceIdFromConsoleOutput() {
        String resourceId = null;
        String locationURI = null;
        Matcher m = locationURIPattern.matcher(consoleOutput);
        if (m.find()) {
            locationURI = m.group(1);
            print("Found location uri: " + locationURI);
            String[] tokens = parseLocationURI(locationURI);
            resourceId = tokens[1];
            print("Resource id: " + resourceId);
        }

        return resourceId;
    }


    /**
     * Runs a fhir-cli test with the specified list of arguments
     *
     * @param methodName
     *            the name of the test method running the test
     * @param args
     *            the list of strings that represent the fhir-cli command-line arguments
     * @throws Exception
     */
    private void runTest(String methodName, String... args) throws Exception {
        print("\n*************** Test method: " + methodName + " ***************");
        print("Command-line arguments:");
        print(Arrays.asList(args).toString());

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(baos);
            FHIRCLI.setConsoleStream(printStream);
            cli = new FHIRCLI(args);
            cli.processCommand();
            consoleOutput = baos.toString();

            print("\nConsole output:");
            print(consoleOutput);
        } catch (Throwable t) {
            print("\nException:");
            print(FHIRCLI.getStackTrace(t));
            throw t;
        }
    }

    private void verifyConsoleOutput(String... expectedMsgs) {
        for (int i = 0; i < expectedMsgs.length; i++) {
            if (!consoleOutput.contains(expectedMsgs[i])) {
                fail("Console output didn't contain: '" + expectedMsgs[i] + "'");
            }
        }
    }

    private void verifyFileContents(String fname, String... expectedMsgs) throws Exception {
        String fileContents = Files.readFile(new FileInputStream(dirPrefix(fname)));
        for (int i = 0; i < expectedMsgs.length; i++) {
            if (!fileContents.contains(expectedMsgs[i])) {
                fail("File contents didn't contain: '" + expectedMsgs[i] + "'");
            }
        }
    }

    private void print(String msg) {
        if (debug) {
            System.out.println(msg);
        }
    }

    private static String dirPrefix(String fname) {
        return dirPrefix + File.separator + fname;
    }

    private static String testData(String fname) {
        return dirPrefix("testdata"+ File.separator + fname);
    }

    private static String propsFile() {
        return dirPrefix("test.properties");
    }
}