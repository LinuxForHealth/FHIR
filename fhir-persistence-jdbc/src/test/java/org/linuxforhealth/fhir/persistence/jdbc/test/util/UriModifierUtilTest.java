/*
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.test.util;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.persistence.jdbc.util.UriModifierUtil;

public class UriModifierUtilTest {
    /**
     * test uri:above modifier
     */
    @Test
    public void testAboveUtil() {
        runAboveValueQueryUtilTest("https", "", new String[] {});
        runAboveValueQueryUtilTest("https:", "", new String[] {});
        runAboveValueQueryUtilTest("https://", "TABLE.STRVALUES IN ( ? )", "https://");
        runAboveValueQueryUtilTest("https://a", "TABLE.STRVALUES IN ( ? )", "https://a");

        runAboveValueQueryUtilTest("http://acme.org/fhir/ValueSet/123/_history/5",
                "TABLE.STRVALUES IN ( ?, ?, ?, ?, ?, ? )", "http://acme.org/fhir/ValueSet/123/_history/5",
                "http://acme.org/fhir/ValueSet/123/_history", "http://acme.org/fhir/ValueSet/123",
                "http://acme.org/fhir/ValueSet", "http://acme.org/fhir", "http://acme.org");
        runAboveValueQueryUtilTest("https://acme.org/fhir/ValueSet/123/_history/5",
                "TABLE.STRVALUES IN ( ?, ?, ?, ?, ?, ? )", "https://acme.org/fhir/ValueSet/123/_history/5",
                "https://acme.org/fhir/ValueSet/123/_history", "https://acme.org/fhir/ValueSet/123",
                "https://acme.org/fhir/ValueSet", "https://acme.org/fhir", "https://acme.org");
        runAboveValueQueryUtilTest("http://acme.org/fhir/ValueSet/123/_history", "TABLE.STRVALUES IN ( ?, ?, ?, ?, ? )",
                "http://acme.org/fhir/ValueSet/123/_history", "http://acme.org/fhir/ValueSet/123",
                "http://acme.org/fhir/ValueSet", "http://acme.org/fhir", "http://acme.org");
        runAboveValueQueryUtilTest("http://acme.org/fhir/ValueSet/123", "TABLE.STRVALUES IN ( ?, ?, ?, ? )",
                "http://acme.org/fhir/ValueSet/123", "http://acme.org/fhir/ValueSet", "http://acme.org/fhir",
                "http://acme.org");
        runAboveValueQueryUtilTest("http://acme.org/fhir/ValueSet", "TABLE.STRVALUES IN ( ?, ?, ? )",
                "http://acme.org/fhir/ValueSet", "http://acme.org/fhir", "http://acme.org");
        runAboveValueQueryUtilTest("http://acme.org/fhir", "TABLE.STRVALUES IN ( ?, ? )", "http://acme.org/fhir",
                "http://acme.org");
        runAboveValueQueryUtilTest("http://acme.org", "TABLE.STRVALUES IN ( ? )", "http://acme.org");
        runAboveValueQueryUtilTest("http://acme.org/", "TABLE.STRVALUES IN ( ? )", "http://acme.org");
        runAboveValueQueryUtilTest("http://acme.org/fhir", "TABLE.STRVALUES IN ( ?, ? )", "http://acme.org/fhir",
                "http://acme.org");
        runAboveValueQueryUtilTest("ntp://acme.org/fhir", "TABLE.STRVALUES IN ( ?, ? )", "ntp://acme.org/fhir",
                "ntp://acme.org");
        runAboveValueQueryUtilTest("acme.org/fhir?name=value", "", new String[] {});
    }

    public void runAboveValueQueryUtilTest(String url, String queryPart, String... parts) {
        StringBuilder builder = new StringBuilder();
        List<String> output = UriModifierUtil.generateAboveValuesQuery(url, builder, "TABLE.STRVALUES");

        assertEquals(output.size(), parts.length);

        for (String part : parts) {
            output.remove(part);
        }

        assertEquals(builder.toString(), queryPart);

        boolean empty = output.isEmpty();
        if (!empty) {
            for (String s : output) {
                System.out.println("---> " + s);
            }
        }
        assertTrue(empty);
    }

    /**
     * test uri:below modifier
     */
    @Test
    public void testBelowModifier() {
        StringBuilder builder = new StringBuilder();
        UriModifierUtil.generateBelowValuesQuery(builder, "TABLE.MY_STR_VALUES");
        assertEquals(builder.toString(), "TABLE.MY_STR_VALUES = ? OR TABLE.MY_STR_VALUES LIKE ?");
    }
}