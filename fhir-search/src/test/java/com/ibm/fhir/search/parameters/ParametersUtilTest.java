/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.parameters;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.model.type.code.ResourceTypeCode;
import com.ibm.fhir.model.type.code.SearchParamType;
import com.ibm.fhir.search.test.BaseSearchTest;

/**
 * Tests ParametersUtil
 */
public class ParametersUtilTest extends BaseSearchTest {
    public static final boolean DEBUG = false;

    @Test
    public void testGetAllSearchParameters() throws IOException {
        // Tests JSON
        Set<SearchParameter> params = ParametersUtil.getAllSearchParameters();
        assertNotNull(params);
        // Intentionally the data is captured in the bytearray output stream.
        try (ByteArrayOutputStream outBA = new ByteArrayOutputStream(); PrintStream out = new PrintStream(outBA, true);) {
            ParametersUtil.print(out);
            Assert.assertNotNull(outBA);
        }
        assertEquals(params.size(), 2795);
    }

    @Test
    public void testPrint() {
        // Test the output, OK, if it gets through.
        try (ByteArrayOutputStream outBA = new ByteArrayOutputStream(); PrintStream out = new PrintStream(outBA, true);) {
            ParametersUtil.print(out);
            assertNotNull(outBA);
            assertNotNull(outBA.toByteArray());
            if (DEBUG) {
                System.out.println(outBA);
            }
        } catch (Exception e) {
            fail();
        }

    }

    @Test
    public void testGetTenantSPs() {
        // getBuiltInSearchParameterMapByResourceType
        Map<String, ParametersMap> result = ParametersUtil.getTenantSPs("default");
        assertNotNull(result);
        assertNull(result.get("Junk"));
        assertFalse(result.get("Observation").isEmpty());
    }

    @Test
    public void testPrintSearchParameter() throws IOException {
        // Intentionally the data is caputred in the bytearray output stream.
        try (ByteArrayOutputStream outBA = new ByteArrayOutputStream(); PrintStream out = new PrintStream(outBA, true);) {

            SearchParameter.Builder builder =
                    SearchParameter.builder().url(Uri.of("test")).name(string("test")).status(PublicationStatus.DRAFT).description(Markdown.of("test")).code(Code.of("test")).base(Arrays.asList(ResourceTypeCode.ACCOUNT)).type(SearchParamType.NUMBER);
            builder.expression(string("test"));
            ParametersUtil.printSearchParameter(builder.build(), out);
            assertNotNull(outBA.toByteArray());
            if (DEBUG) {
                System.out.println(outBA.toString("UTF-8"));
            }
        }
    }

    @Test
    public void testInit() {
        ParametersUtil.init();
        assertTrue(true);
    }

    @Test
    public void testCheckAndWarnForIssueWithCodeAndName() {
        // Issue 202 : added warning and corresponding test.
        ParametersUtil.checkAndWarnForIssueWithCodeAndName(null, null);
        ParametersUtil.checkAndWarnForIssueWithCodeAndName(null, "");
        ParametersUtil.checkAndWarnForIssueWithCodeAndName("", null);
        ParametersUtil.checkAndWarnForIssueWithCodeAndName("", "");
        ParametersUtil.checkAndWarnForIssueWithCodeAndName("_code", "_code");

        // Run as individual unit test, you should see only one warning:
        // WARNING: The code and name of the search parameter does not match [_code] [_notcode]
        ParametersUtil.checkAndWarnForIssueWithCodeAndName("_code", "_notcode");

        assertTrue(true);
    }
}
