/*
 * (C) Copyright IBM Corp. 2018, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.test;

import static org.testng.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.Instant;

import org.testng.annotations.Test;

import com.ibm.fhir.search.SearchConstants.Prefix;
import com.ibm.fhir.search.parameters.QueryParameterValue;

/**
 * This testng test class contains methods that test the QueryParameterValue class.
 */
public class QueryParameterValueTest extends BaseSearchTest {


    @Test
    public void testValueString() throws Exception {
        QueryParameterValue testObj = new QueryParameterValue();
        String valueString = "valueString";
        testObj.setValueString(valueString);
        assertEquals(testObj.getValueString(), valueString);
        assertEquals(testObj.toString(), "valueString");
        assertEquals(testObj.toString(), "valueString");
    }

    @Test
    public void testValueNumber() throws Exception {
        QueryParameterValue testObj = new QueryParameterValue();
        BigDecimal valueNumber = BigDecimal.valueOf(100);
        testObj.setValueNumber(valueNumber);
        assertEquals(testObj.getValueNumber(), valueNumber);
        assertEquals(testObj.toString(), "100");
        assertEquals(testObj.toString(), "100");
    }

    @Test
    public void testValueCode() throws Exception {
        QueryParameterValue testObj = new QueryParameterValue();
        String valueSystem = "";
        String valueCode = "valueCode";
        testObj.setValueSystem(valueSystem);
        testObj.setValueCode(valueCode);
        assertEquals(testObj.getValueSystem(), valueSystem);
        assertEquals(testObj.getValueCode(), valueCode);
        assertEquals(testObj.toString(), "|valueCode");
        assertEquals(testObj.toString(), "|valueCode");
    }

    @Test
    public void testValueSystemCode() throws Exception {
        QueryParameterValue testObj = new QueryParameterValue();
        String valueSystem = "";
        String valueCode = "valueCode";
        testObj.setValueSystem(valueSystem);
        testObj.setValueCode(valueCode);
        assertEquals(testObj.getValueSystem(), valueSystem);
        assertEquals(testObj.getValueCode(), valueCode);
        assertEquals(testObj.toString(), "|valueCode");
        assertEquals(testObj.toString(), "|valueCode");
    }

    @Test
    public void testValueDate() throws Exception {
        QueryParameterValue testObj = new QueryParameterValue();
        Prefix prefix = Prefix.EQ;
        String valueDate = "2019-12-11";
        Instant valueDateLowerBound = Instant.parse("2019-12-11T00:00:00Z");
        Instant valueDateUpperBound = Instant.parse("2019-12-11T23:59:59.999999Z");
        testObj.withPrefix(prefix);
        testObj.setValueDate(valueDate);
        testObj.setValueDateLowerBound(valueDateLowerBound);
        testObj.setValueDateUpperBound(valueDateUpperBound);
        assertEquals(testObj.getPrefix(), prefix);
        assertEquals(testObj.getValueDate(), valueDate);
        assertEquals(testObj.getValueDateLowerBound(), valueDateLowerBound);
        assertEquals(testObj.getValueDateUpperBound(), valueDateUpperBound);
        assertEquals(testObj.toString(), "eq2019-12-11");
        assertEquals(testObj.toString(), "eq2019-12-11");
    }
}
