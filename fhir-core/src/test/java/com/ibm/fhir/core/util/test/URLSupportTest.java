/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.core.util.test;

import static com.ibm.fhir.core.util.URLSupport.getFirst;
import static com.ibm.fhir.core.util.URLSupport.getPathSegments;
import static com.ibm.fhir.core.util.URLSupport.getQueryParameters;
import static com.ibm.fhir.core.util.URLSupport.parseQuery;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

public class URLSupportTest {
    @Test
    public void testGetQueryParameters() {
        String url = "http://ibm.com/fhir/ValueSet/generalizes?system=http://ibm.com/fhir/CodeSystem/cs5&code=r";
        Map<String, List<String>> queryParameters = getQueryParameters(url);
        Assert.assertEquals(getFirst(queryParameters, "system"), "http://ibm.com/fhir/CodeSystem/cs5");
        Assert.assertEquals(getFirst(queryParameters, "code"), "r");
    }

    @Test
    public void testGetPathSegments() {
        String url = "http://ibm.com/fhir/ValueSet/generalizes?system=http://ibm.com/fhir/CodeSystem/cs5&code=r";
        List<String> actual = getPathSegments(url);
        List<String> expected = Arrays.asList("fhir", "ValueSet", "generalizes");
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testParseQuery() throws Exception {
        String query = "name1=value1%7Cvalue2%7Cvalue3";
        Map<String, List<String>> queryParameters = parseQuery(query);
        Assert.assertEquals(getFirst(queryParameters, "name1"), "value1|value2|value3");
        queryParameters = parseQuery(query, false);
        Assert.assertEquals(getFirst(queryParameters, "name1"), "value1%7Cvalue2%7Cvalue3");
    }
}