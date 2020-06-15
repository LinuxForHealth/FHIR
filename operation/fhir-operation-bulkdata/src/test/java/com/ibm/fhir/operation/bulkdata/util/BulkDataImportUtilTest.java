/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.util;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStreamReader;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.resource.Parameters;

public class BulkDataImportUtilTest {
    @BeforeClass
    public void setup() {
        FHIRConfiguration.setConfigHome("src/test/resources/testdata");
    }

    @Test
    public void testBulkImportUtilInputs() throws IOException, FHIRException {
        FHIRRequestContext.set(new FHIRRequestContext("default"));
        assertFalse(BulkDataImportUtil.retrieveInputs(loadTestFile("/testdata/import/import-demo.json")).isEmpty());
        assertEquals(BulkDataImportUtil.retrieveInputs(loadTestFile("/testdata/import/import-demo.json")).size(), 2);
    }

    @Test
    public void testBulkImportUtilStorageDetails() throws IOException, FHIRException {
        FHIRRequestContext.set(new FHIRRequestContext("default"));
        assertNotNull(BulkDataImportUtil.retrieveStorageDetails(loadTestFile("/testdata/import/import-demo.json")));
        assertEquals(
                BulkDataImportUtil.retrieveStorageDetails(loadTestFile("/testdata/import/import-demo.json")).getType(),
                "https");
    }

    @Test
    public void testBulkImportUtilFormat() throws IOException, FHIRException {
        FHIRRequestContext.set(new FHIRRequestContext("default"));
        assertNotNull(BulkDataImportUtil.retrieveInputFormat(loadTestFile("/testdata/import/import-demo.json")));
        assertEquals(BulkDataImportUtil.retrieveInputFormat(loadTestFile("/testdata/import/import-demo.json")),
                "application/fhir+ndjson");
    }

    @Test(expectedExceptions = { FHIROperationException.class })
    public void testBulkImportUtilFormatNull() throws IOException, FHIRException {
        FHIRRequestContext.set(new FHIRRequestContext("default"));
        BulkDataImportUtil.retrieveInputFormat(null);
    }

    @Test(expectedExceptions = { FHIROperationException.class })
    public void testBulkImportUtilFormatEmptyParameters() throws IOException, FHIRException {
        FHIRRequestContext.set(new FHIRRequestContext("default"));
        BulkDataImportUtil.retrieveInputFormat(loadTestFile("/testdata/import/import-demo-empty.json"));
    }

    @Test(expectedExceptions = { FHIROperationException.class })
    public void testBulkImportUtilFormatBadFormat() throws IOException, FHIRException {
        FHIRRequestContext.set(new FHIRRequestContext("default"));
        BulkDataImportUtil.retrieveInputFormat(loadTestFile("/testdata/import/import-demo-bad-format.json"));
    }

    @Test(expectedExceptions = { FHIROperationException.class })
    public void testBulkImportUtilFormatBadFormatNull() throws IOException, FHIRException {
        FHIRRequestContext.set(new FHIRRequestContext("default"));
        BulkDataImportUtil.retrieveInputFormat(loadTestFile("/testdata/import/import-demo-bad-format2.json"));
    }

    @Test(expectedExceptions = { FHIROperationException.class })
    public void testBulkImportUtilFormatBadFormatWrongType() throws IOException, FHIRException {
        FHIRRequestContext.set(new FHIRRequestContext("default"));
        BulkDataImportUtil.retrieveInputFormat(loadTestFile("/testdata/import/import-demo-bad-format3.json"));
    }

    @Test
    public void testBulkImportUtilSource() throws IOException, FHIRException {
        FHIRRequestContext.set(new FHIRRequestContext("default"));
        assertNotNull(BulkDataImportUtil.retrieveInputSource(loadTestFile("/testdata/import/import-demo.json")));
        assertEquals(BulkDataImportUtil.retrieveInputSource(loadTestFile("/testdata/import/import-demo.json")),
                "inputSource");
    }

    @Test(expectedExceptions = { FHIROperationException.class })
    public void testBulkImportUtilSourceNull() throws IOException, FHIRException {
        FHIRRequestContext.set(new FHIRRequestContext("default"));
        BulkDataImportUtil.retrieveInputSource(null);
    }

    @Test(expectedExceptions = { FHIROperationException.class })
    public void testBulkImportUtilSourceBadFormatNull() throws IOException, FHIRException {
        FHIRRequestContext.set(new FHIRRequestContext("default"));
        BulkDataImportUtil.retrieveInputSource(loadTestFile("/testdata/import/import-demo-bad-format2.json"));
    }

    @Test(expectedExceptions = { FHIROperationException.class })
    public void testBulkImportUtilSourceBadFormatWrongType() throws IOException, FHIRException {
        FHIRRequestContext.set(new FHIRRequestContext("default"));
        BulkDataImportUtil.retrieveInputSource(loadTestFile("/testdata/import/import-demo-bad-format3.json"));
    }

    @Test
    public void testCheckAllowedTotalSizeForTenantOrSystem() throws FHIROperationException {
        BulkDataImportUtil.checkAllowedTotalSizeForTenantOrSystem(1);
        assertTrue(true);
    }

    @Test(expectedExceptions = { FHIROperationException.class })
    public void testCheckAllowedTotalSizeForTenantOrSystemFail() throws FHIROperationException {
        BulkDataImportUtil.checkAllowedTotalSizeForTenantOrSystem(100);
    }

    public Parameters loadTestFile(String file) throws FHIRParserException, IOException {
        Parameters parameters = null;
        try (InputStreamReader reader = new InputStreamReader(BulkDataImportUtilTest.class.getResourceAsStream(file))) {
            parameters = FHIRParser.parser(Format.JSON).parse(reader);
        }
        return parameters;
    }
}