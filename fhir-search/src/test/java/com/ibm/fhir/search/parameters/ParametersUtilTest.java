/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.parameters;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.search.compartment.CompartmentUtil;
import com.ibm.fhir.search.test.BaseSearchTest;

/**
 * Tests ParametersUtil
 */
public class ParametersUtilTest extends BaseSearchTest {
    public static final boolean DEBUG = false;

    CompartmentUtil compartmentHelper = new CompartmentUtil();
    ParametersUtil parametersHelper = new ParametersUtil(compartmentHelper);

    @Test
    public void testGetAllSearchParameters() throws IOException {
        // Tests JSON
        Set<SearchParameter> params = ParametersUtil.getAllSearchParameters();
        assertNotNull(params);
        // Intentionally the data is captured in the bytearray output stream.
        try (ByteArrayOutputStream outBA = new ByteArrayOutputStream(); PrintStream out = new PrintStream(outBA, true);) {
            parametersHelper.print(out);
            Assert.assertNotNull(outBA);
        }
        assertEquals(params.size(), 1379);
    }

    @Test
    public void testPrint() {
        // Test the output, OK, if it gets through.
        try (ByteArrayOutputStream outBA = new ByteArrayOutputStream(); PrintStream out = new PrintStream(outBA, true);) {
            parametersHelper.print(out);
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
        Map<String, ParametersMap> result = parametersHelper.getTenantSPs("default");
        assertNotNull(result);
        assertNull(result.get("Junk"));
        assertFalse(result.get("Observation").getCodes().isEmpty());
    }

    /**
     * To execute: update com.ibm.fhir.search.parameters.ParametersUtil log level to FINE
     * and manually invoke this test.  You should see only a single message:
     * <pre>
     * The code and name of the search parameter does not match [_code] [_notcode]
     * </pre>
     */
    @Test(enabled = false)
    public void testCheckAndWarnForIssueWithCodeAndName() {
        // Issue 202 : added warning and corresponding test.
        ParametersUtil.checkAndWarnForIssueWithCodeAndName(null, null);
        ParametersUtil.checkAndWarnForIssueWithCodeAndName(null, "");
        ParametersUtil.checkAndWarnForIssueWithCodeAndName("", null);
        ParametersUtil.checkAndWarnForIssueWithCodeAndName("", "");
        ParametersUtil.checkAndWarnForIssueWithCodeAndName("_code", "_code");

        // This is the one line that should produce a log message (level=FINE)
        ParametersUtil.checkAndWarnForIssueWithCodeAndName("_code", "_notcode");
    }
}
