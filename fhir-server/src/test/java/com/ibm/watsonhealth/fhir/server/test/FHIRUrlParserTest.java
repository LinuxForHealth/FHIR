/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.test;

import org.testng.annotations.Test;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import com.ibm.watsonhealth.fhir.server.helper.FHIRUrlParser;


public class FHIRUrlParserTest extends FHIRServerTestBase {

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
