/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.operation.bulkdata.util;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.config.FHIRConfiguration;
import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.core.FHIRVersionParam;
import org.linuxforhealth.fhir.exception.FHIRException;
import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.parser.exception.FHIRParserException;
import org.linuxforhealth.fhir.model.resource.Parameters;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationContext;

public class BulkDataImportUtilTest {

    @BeforeClass
    public void setup() {
        FHIRConfiguration.setConfigHome("target/test-classes");
    }

    @BeforeMethod
    public void startMethod(Method method) throws FHIRException {

        // Configure the request context for our search tests
        FHIRRequestContext context = FHIRRequestContext.get();
        context.setTenantId("default");
    }

    @AfterMethod
    public void clearThreadLocal() {
        FHIRRequestContext.remove();
    }

    @Test
    public void testBulkImportUtilInputs() throws IOException, FHIRException {
        FHIRRequestContext context = FHIRRequestContext.get();
        FHIRRequestContext.set(context);
        context.setTenantId("not-config");
        BulkDataImportUtil util = new BulkDataImportUtil(getContext(), loadTestFile("/testdata/import/import-demo.json"));
        assertFalse(util.retrieveInputs(FHIRVersionParam.VERSION_43).isEmpty());
        assertEquals(util.retrieveInputs(FHIRVersionParam.VERSION_43).size(), 2);
    }

    @Test
    public void testBulkImportUtilStorageDetails() throws IOException, FHIRException {
        BulkDataImportUtil util = new BulkDataImportUtil(null, loadTestFile("/testdata/import/import-demo.json"));
        assertNotNull(util.retrieveStorageDetails());
        assertEquals(util.retrieveStorageDetails().getType(), "ibm-cos");
    }

    @Test
    public void testBulkImportUtilFormat() throws IOException, FHIRException {
        BulkDataImportUtil util = new BulkDataImportUtil(null, loadTestFile("/testdata/import/import-demo.json"));
        assertNotNull(util.retrieveInputFormat());
        assertEquals(util.retrieveInputFormat(), "application/fhir+ndjson");
    }

    @Test(expectedExceptions = { FHIROperationException.class })
    public void testBulkImportUtilFormatNull() throws IOException, FHIRException {

        @SuppressWarnings("unused")
        BulkDataImportUtil util = new BulkDataImportUtil(null, null);
    }

    @Test(expectedExceptions = { FHIROperationException.class })
    public void testBulkImportUtilFormatEmptyParameters() throws IOException, FHIRException {

        BulkDataImportUtil util = new BulkDataImportUtil(null, loadTestFile("/testdata/import/import-demo-empty.json"));
        util.retrieveInputFormat();
    }

    @Test(expectedExceptions = { FHIROperationException.class })
    public void testBulkImportUtilFormatBadFormat() throws IOException, FHIRException {

        BulkDataImportUtil util = new BulkDataImportUtil(null, loadTestFile("/testdata/import/import-demo-bad-format.json"));
        util.retrieveInputFormat();
    }

    @Test(expectedExceptions = { FHIROperationException.class })
    public void testBulkImportUtilFormatBadFormatNull() throws IOException, FHIRException {

        BulkDataImportUtil util = new BulkDataImportUtil(null, loadTestFile("/testdata/import/import-demo-bad-format2.json"));
        util.retrieveInputFormat();
    }

    @Test(expectedExceptions = { FHIROperationException.class })
    public void testBulkImportUtilFormatBadFormatWrongType() throws IOException, FHIRException {

        BulkDataImportUtil util = new BulkDataImportUtil(null, loadTestFile("/testdata/import/import-demo-bad-format3.json"));
        util.retrieveInputFormat();
    }

    @Test
    public void testBulkImportUtilSource() throws IOException, FHIRException {

        BulkDataImportUtil util = new BulkDataImportUtil(null, loadTestFile("/testdata/import/import-demo.json"));
        assertNotNull(util.retrieveInputSource());
        assertEquals(util.retrieveInputSource(), "inputSource");
    }

    @Test(expectedExceptions = { FHIROperationException.class })
    public void testBulkImportUtilSourceBadFormatNull() throws IOException, FHIRException {

        BulkDataImportUtil util = new BulkDataImportUtil(null, loadTestFile("/testdata/import/import-demo-bad-format2.json"));
        util.retrieveInputSource();
    }

    @Test(expectedExceptions = { FHIROperationException.class })
    public void testBulkImportUtilSourceBadFormatWrongType() throws IOException, FHIRException {

        BulkDataImportUtil util = new BulkDataImportUtil(null, loadTestFile("/testdata/import/import-demo-bad-format3.json"));
        util.retrieveInputSource();
    }

    @Test
    public void testCheckAllowedTotalSizeForTenantOrSystem() throws FHIROperationException, FHIRParserException, IOException {
        BulkDataImportUtil util = new BulkDataImportUtil(null, loadTestFile("/testdata/import/import-demo-bad-format3.json"));
        util.checkAllowedTotalSizeForTenantOrSystem(1);
        assertTrue(true);
    }

    @Test(expectedExceptions = { FHIROperationException.class })
    public void testCheckAllowedTotalSizeForTenantOrSystemFail() throws FHIROperationException, FHIRParserException, IOException {
        BulkDataImportUtil util = new BulkDataImportUtil(null, loadTestFile("/testdata/import/import-demo-bad-format3.json"));
        util.checkAllowedTotalSizeForTenantOrSystem(100);
    }

    @Test
    public void testCheckAllowedResourceTypesForInputs() throws IOException, FHIRException {
        FHIRRequestContext context = FHIRRequestContext.get();
        FHIRRequestContext.set(context);
        context.setTenantId("config");
        BulkDataImportUtil util = new BulkDataImportUtil(getContext(), loadTestFile("/testdata/import/import-demo-config.json"));
        util.retrieveInputs(FHIRVersionParam.VERSION_43);
    }

    @Test(expectedExceptions = { FHIROperationException.class })
    public void testCheckAllowedResourceTypesForInputsBad() throws IOException, FHIRException {
        FHIRRequestContext context = FHIRRequestContext.get();
        FHIRRequestContext.set(context);
        context.setTenantId("not-config");
        BulkDataImportUtil util = new BulkDataImportUtil(getContext(), loadTestFile("/testdata/import/import-demo-not-config.json"));
        util.retrieveInputs(FHIRVersionParam.VERSION_43);
    }

    public Parameters loadTestFile(String file) throws FHIRParserException, IOException {
        Parameters parameters = null;
        try (InputStreamReader reader = new InputStreamReader(BulkDataImportUtilTest.class.getResourceAsStream(file))) {
            parameters = FHIRParser.parser(Format.JSON).parse(reader);
        }
        return parameters;
    }

    private FHIROperationContext getContext() {
        FHIROperationContext ctx = FHIROperationContext.createInstanceOperationContext("import");
        HttpHeaders httpHeaders = new HttpHeaders() {

            @Override
            public List<String> getRequestHeader(String name) {

                return Arrays.asList("default");
            }

            @Override
            public String getHeaderString(String name) {

                return "default";
            }

            @Override
            public MultivaluedMap<String, String> getRequestHeaders() {

                return null;
            }

            @Override
            public List<MediaType> getAcceptableMediaTypes() {

                return null;
            }

            @Override
            public List<Locale> getAcceptableLanguages() {

                return null;
            }

            @Override
            public MediaType getMediaType() {

                return null;
            }

            @Override
            public Locale getLanguage() {

                return null;
            }

            @Override
            public Map<String, Cookie> getCookies() {

                return null;
            }

            @Override
            public Date getDate() {

                return null;
            }

            @Override
            public int getLength() {

                return 0;
            }

        };
        ctx.setProperty(FHIROperationContext.PROPNAME_HTTP_HEADERS, httpHeaders);

        return ctx;
    }
}