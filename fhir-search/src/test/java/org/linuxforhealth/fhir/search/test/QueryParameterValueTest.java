/*
 * (C) Copyright IBM Corp. 2018, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.search.test;

import static org.testng.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.Instant;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.search.SearchConstants.Prefix;
import org.linuxforhealth.fhir.search.SearchConstants.Type;
import org.linuxforhealth.fhir.search.parameters.QueryParameter;
import org.linuxforhealth.fhir.search.parameters.QueryParameterValue;

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

    @Test
    public void testToStringOfComposite() throws Exception {
        QueryParameterValue testObj = new QueryParameterValue();
        QueryParameter qp1 = new QueryParameter(Type.TOKEN, "qp1", null, null);
        QueryParameterValue qpv1 = new QueryParameterValue();
        String valueSystem1 = "valueSystem1";
        String valueCode1 = "valueCode1";
        qpv1.setValueSystem(valueSystem1);
        qpv1.setValueCode(valueCode1);
        qp1.getValues().add(qpv1);
        QueryParameter qp2 = new QueryParameter(Type.TOKEN, "qp2", null, null);
        QueryParameterValue qpv2 = new QueryParameterValue();
        String valueCode2 = "valueCode2";
        qpv2.setValueCode(valueCode2);
        qp2.getValues().add(qpv2);
        testObj.addComponent(qp1, qp2);
        // toString of an :of-type modifier uses '|' as component delimiter
        testObj.setOfTypeModifier(true);
        assertEquals(testObj.toString(), "valueSystem1|valueCode1|valueCode2");
        // Otherwise toString uses '$' as component delimiter
        testObj.setOfTypeModifier(false);
        assertEquals(testObj.toString(), "valueSystem1|valueCode1$valueCode2");
    }
}
