/*
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.util;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.Test;

import com.ibm.fhir.persistence.jdbc.util.AboveUtil;

/**
 * 
 */
public class AboveUtilTest {

    /**
     * test uri:above modifier
     */
    @Test
    public void testAboveUtil() {

        runAboveValueQueryUtilTest("https", "", new String[] {});
        runAboveValueQueryUtilTest("https:", "", new String[] {});
        runAboveValueQueryUtilTest("https://", "IN ( ? )", "https://");
        runAboveValueQueryUtilTest("https://a", "IN ( ? )", "https://a");
        
        runAboveValueQueryUtilTest("http://acme.org/fhir/ValueSet/123/_history/5", "IN ( ?, ?, ?, ?, ?, ? )","http://acme.org/fhir/ValueSet/123/_history/5", "http://acme.org/fhir/ValueSet/123/_history", "http://acme.org/fhir/ValueSet/123", "http://acme.org/fhir/ValueSet", "http://acme.org/fhir", "http://acme.org");
        runAboveValueQueryUtilTest("https://acme.org/fhir/ValueSet/123/_history/5", "IN ( ?, ?, ?, ?, ?, ? )","https://acme.org/fhir/ValueSet/123/_history/5", "https://acme.org/fhir/ValueSet/123/_history", "https://acme.org/fhir/ValueSet/123", "https://acme.org/fhir/ValueSet", "https://acme.org/fhir", "https://acme.org");
        runAboveValueQueryUtilTest("http://acme.org/fhir/ValueSet/123/_history", "IN ( ?, ?, ?, ?, ? )", "http://acme.org/fhir/ValueSet/123/_history", "http://acme.org/fhir/ValueSet/123", "http://acme.org/fhir/ValueSet", "http://acme.org/fhir", "http://acme.org");
        runAboveValueQueryUtilTest("http://acme.org/fhir/ValueSet/123", "IN ( ?, ?, ?, ? )", "http://acme.org/fhir/ValueSet/123", "http://acme.org/fhir/ValueSet", "http://acme.org/fhir", "http://acme.org");
        runAboveValueQueryUtilTest("http://acme.org/fhir/ValueSet", "IN ( ?, ?, ? )","http://acme.org/fhir/ValueSet", "http://acme.org/fhir", "http://acme.org");
        runAboveValueQueryUtilTest("http://acme.org/fhir", "IN ( ?, ? )","http://acme.org/fhir", "http://acme.org");
        runAboveValueQueryUtilTest("http://acme.org", "IN ( ? )", "http://acme.org");
        runAboveValueQueryUtilTest("http://acme.org/", "IN ( ? )", "http://acme.org");
        runAboveValueQueryUtilTest("http://acme.org/fhir", "IN ( ?, ? )","http://acme.org/fhir", "http://acme.org");
        runAboveValueQueryUtilTest("ntp://acme.org/fhir", "IN ( ?, ? )","ntp://acme.org/fhir", "ntp://acme.org");
        runAboveValueQueryUtilTest("acme.org/fhir?name=value", "", new String[] {});
    }

    public void runAboveValueQueryUtilTest(String url, String queryPart, String... parts) {
        StringBuilder builder = new StringBuilder();
        List<String> output = AboveUtil.generateAboveValuesQuery(url, builder);

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
}