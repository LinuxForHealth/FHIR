/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.util;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.testng.annotations.Test;


public class FHIRUrlParserTest {

    @Test(groups = { "server-basic" }, expectedExceptions = { IllegalArgumentException.class })
    public void testNullUrlString() {
        new FHIRUrlParser(null);
    }

    @Test(groups = { "server-basic" }, expectedExceptions = { IllegalArgumentException.class })
    public void testEmptyUrlString() {
        new FHIRUrlParser("");
    }

    @Test(groups = { "server-basic" })
    public void testShortPath() {
        FHIRUrlParser parser = new FHIRUrlParser("Patient");
        assertEquals("Patient", parser.getPath());
        String[] s = parser.getPathTokens();
        assertNotNull(s);
        assertEquals(1, s.length);
        assertEquals("Patient", s[0]);
    }

    @Test(groups = { "server-basic" })
    public void testLongerPath() {
        FHIRUrlParser parser = new FHIRUrlParser("Patient/123/_history/74");
        assertEquals("Patient/123/_history/74", parser.getPath());
        String[] s = parser.getPathTokens();
        assertNotNull(s);
        assertEquals(4, s.length);
        assertEquals("Patient", s[0]);
        assertEquals("123", s[1]);
        assertEquals("_history", s[2]);
        assertEquals("74", s[3]);
    }

    @Test(groups = { "server-basic" })
    public void testShortQuery() {
        FHIRUrlParser parser = new FHIRUrlParser("?var1=1");
        assertEquals("var1=1", parser.getQuery());
        MultivaluedMap<String, String> queryParams = parser.getQueryParameters();
        assertNotNull(queryParams);
        assertEquals(1, queryParams.size());
        assertEquals("1", queryParams.getFirst("var1"));
    }

    /**
     * @see https://www.hl7.org/fhir/DSTU2/search.html#escaping
     */
    @Test
    public void testEscapedQuery() {
        FHIRUrlParser parser = new FHIRUrlParser("?var1=a,b&var2=a\\,b");
        assertEquals("var1=a,b&var2=a\\,b", parser.getQuery());
        MultivaluedMap<String, String> queryParams = parser.getQueryParameters();
        assertNotNull(queryParams);
        // TODO: should be parsed into two parameters
        assertEquals(2, queryParams.size());
        assertEquals("a,b", queryParams.getFirst("var1"));
        assertEquals("a\\,b", queryParams.getFirst("var2"));
    }

    @Test
    public void testEncodedQuery() {
        FHIRUrlParser parser = new FHIRUrlParser("?var1=a%26b");
        assertEquals("var1=a%26b", parser.getQuery());
        MultivaluedMap<String, String> queryParams = parser.getQueryParameters();
        assertNotNull(queryParams);
        assertEquals(1, queryParams.size());
        assertEquals("a&b", queryParams.getFirst("var1"));
    }

    @Test
    public void testEncodedQuery2() {
        FHIRUrlParser parser = new FHIRUrlParser("?url=http%3A%2F%2Facme%2Eorg%2Ffhir%2FValueSet%2F123%2Chttp%3A%2F%2Facme%2Eorg%2Ffhir%2FValueSet%2F124%5C%2CValueSet%2F125");
        assertEquals("url=http%3A%2F%2Facme%2Eorg%2Ffhir%2FValueSet%2F123%2Chttp%3A%2F%2Facme%2Eorg%2Ffhir%2FValueSet%2F124%5C%2CValueSet%2F125", parser.getQuery());
        MultivaluedMap<String, String> queryParams = parser.getQueryParameters();
        assertNotNull(queryParams);
        assertEquals(1, queryParams.size());
        assertEquals("http://acme.org/fhir/ValueSet/123,http://acme.org/fhir/ValueSet/124\\,ValueSet/125", queryParams.getFirst("url"));
    }

    @Test(groups = { "server-basic" })
    public void testQueryMultipleValues() {
        FHIRUrlParser parser = new FHIRUrlParser("?var1=1&var1=2&var1=3");
        assertEquals("var1=1&var1=2&var1=3", parser.getQuery());
        MultivaluedMap<String, String> queryParams = parser.getQueryParameters();
        assertNotNull(queryParams);
        assertEquals(1, queryParams.size());
        List<String> var1Values = queryParams.get("var1");
        assertNotNull(var1Values);
        assertEquals(3, var1Values.size());
        assertEquals("1", var1Values.get(0));
        assertEquals("2", var1Values.get(1));
        assertEquals("3", var1Values.get(2));
    }

    @Test(groups = { "server-basic" })
    public void testWholeEnchilada() {
        FHIRUrlParser parser = new FHIRUrlParser("Patient/123/_history?_count=100&_since=12-31-1970&_format=json");
        assertEquals("Patient/123/_history", parser.getPath());
        assertEquals("_count=100&_since=12-31-1970&_format=json", parser.getQuery());

        String[] s = parser.getPathTokens();
        assertNotNull(s);
        assertEquals(3, s.length);
        assertEquals("Patient", s[0]);
        assertEquals("123", s[1]);
        assertEquals("_history", s[2]);

        MultivaluedMap<String, String> queryParams = parser.getQueryParameters();
        assertNotNull(queryParams);
        assertEquals(3, queryParams.size());
        assertEquals("100", queryParams.getFirst("_count"));
        assertEquals("12-31-1970", queryParams.getFirst("_since"));
        assertEquals("json", queryParams.getFirst("_format"));
    }
}
