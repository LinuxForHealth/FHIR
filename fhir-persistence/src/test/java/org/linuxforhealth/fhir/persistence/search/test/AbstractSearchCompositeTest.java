/*
 * (C) Copyright IBM Corp. 2018, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.search.test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.model.resource.Basic;
import org.linuxforhealth.fhir.model.test.TestUtil;

/**
 * @see https://hl7.org/fhir/r4/search.html#token
 */
public abstract class AbstractSearchCompositeTest extends AbstractPLSearchTest {

    @Override
    protected Basic getBasicResource() throws Exception {
        return TestUtil.readExampleResource("json/basic/BasicComposite.json");
    }

    @Override
    protected void setTenant() throws Exception {
        FHIRRequestContext.get().setTenantId("composite");
    }

    /////////////////
    // Token tests //
    /////////////////
    @Test
    public void testSearchToken_boolean() throws Exception {
        assertSearchReturnsSavedResource("composite-boolean", "true$true");
        assertSearchDoesntReturnSavedResource("composite-boolean", "false$true");
        assertSearchDoesntReturnSavedResource("composite-boolean", "true$false");
        assertSearchDoesntReturnSavedResource("composite-boolean", "false$false");
    }

    @Test
    public void testSearchToken_boolean_or() throws Exception {
        assertSearchReturnsSavedResource("composite-boolean", "true$true,true$true");
        assertSearchReturnsSavedResource("composite-boolean", "false$false,true$true");
        assertSearchReturnsSavedResource("composite-boolean", "true$true,false$false");
        assertSearchDoesntReturnSavedResource("composite-boolean", "false$false,false$false");
    }

    @Test
    public void testSearchToken_boolean_chained() throws Exception {
        assertSearchReturnsComposition("subject:Basic.composite-boolean", "true$true");
        assertSearchDoesntReturnComposition("subject:Basic.composite-boolean", "true$false");
        assertSearchDoesntReturnComposition("subject:Basic.composite-boolean", "false$true");
        assertSearchDoesntReturnComposition("subject:Basic.composite-boolean", "false$false");
    }

    @Test
    public void testSearchToken_boolean_reverse_chained() throws Exception {
        assertSearchReturnsSavedResource("_has:Composition:subject:composite-status-title", "preliminary$TEST");
        assertSearchDoesntReturnSavedResource("_has:Composition:subject:composite-status-title", "bad$TEST");
    }

    @Test
    public void testSearchToken_boolean_revinclude() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>(1);
        queryParms.put("_revinclude", Collections.singletonList("Composition:subject"));
        queryParms.put("composite-boolean", Collections.singletonList("true$true"));
        assertTrue(searchReturnsResource(Basic.class, queryParms, savedResource));
        assertTrue(searchReturnsResource(Basic.class, queryParms, composition));
    }

    @Test
    public void testSearchToken_boolean_missing() throws Exception {
        assertSearchReturnsSavedResource("composite-boolean:missing", "false");
        assertSearchDoesntReturnSavedResource("composite-boolean:missing", "true");
    }

    @Test
    public void testSearchToken_code() throws Exception {
        assertSearchReturnsSavedResource("composite-code", "code$code");
        assertSearchReturnsSavedResource("composite-code", "|code$|code");
        assertSearchDoesntReturnSavedResource("composite-code", "miss$code");
        assertSearchDoesntReturnSavedResource("composite-code", "code$miss");
        assertSearchDoesntReturnSavedResource("composite-code", "miss$miss");
    }

    @Test
    public void testSearchToken_CodeableConcept() throws Exception {
        assertSearchReturnsSavedResource("composite-CodeableConcept", "code$code");
        assertSearchReturnsSavedResource("composite-CodeableConcept", "http://example.org/codesystem|code$http://example.org/codesystem|code");
    }

    @Test
    public void testSearchToken_Coding() throws Exception {
        assertSearchReturnsSavedResource("composite-Coding", "code$code");
        assertSearchReturnsSavedResource("composite-Coding", "http://example.org/codesystem|code$http://example.org/codesystem|code");
    }

    @Test
    public void testSearchToken_Identifier() throws Exception {
        assertSearchReturnsSavedResource("composite-Identifier", "code$code");
        assertSearchReturnsSavedResource("composite-Identifier", "http://example.org/identifiersystem|code$http://example.org/identifiersystem|code");
    }

    ////////////////////
    // Quantity tests //
    ////////////////////
    @Test
    public void testSearchQuantity_Quantity() throws Exception {
        assertSearchReturnsSavedResource("composite-Quantity", "25|http://unitsofmeasure.org|s$25|http://unitsofmeasure.org|s");
        assertSearchReturnsSavedResource("composite-Quantity", "25||s$25||s");
        assertSearchReturnsSavedResource("composite-Quantity", "25$25");
    }

    @Test
    public void testSearchQuantity_Range() throws Exception {
        // Range is 5-10 seconds

        assertSearchReturnsSavedResource("composite-Range", "gt4||s$lt11||s");
    }

    ////////////////
    // Date tests //
    ////////////////
    @Test
    public void testSearchDate_date() throws Exception {
        // "date" is 2018-10-29

        assertSearchReturnsSavedResource("composite-date", "2018-10-29$2018-10-29");

        assertSearchDoesntReturnSavedResource("composite-date", "ne2018-10-29$2018-10-29");
        assertSearchDoesntReturnSavedResource("composite-date", "lt2018-10-29$2018-10-29");
        assertSearchDoesntReturnSavedResource("composite-date", "gt2018-10-29$2018-10-29");
        assertSearchReturnsSavedResource("composite-date", "le2018-10-29$2018-10-29");
        assertSearchReturnsSavedResource("composite-date", "ge2018-10-29$2018-10-29");
        assertSearchDoesntReturnSavedResource("composite-date", "sa2018-10-29$2018-10-29");
        assertSearchDoesntReturnSavedResource("composite-date", "eb2018-10-29$2018-10-29");
        assertSearchReturnsSavedResource("composite-date", "ap2018-10-29$2018-10-29");

        assertSearchDoesntReturnSavedResource("composite-date", "2018-10-29$ne2018-10-29");
        assertSearchDoesntReturnSavedResource("composite-date", "2018-10-29$lt2018-10-29");
        assertSearchDoesntReturnSavedResource("composite-date", "2018-10-29$gt2018-10-29");
        assertSearchReturnsSavedResource("composite-date", "2018-10-29$le2018-10-29");
        assertSearchReturnsSavedResource("composite-date", "2018-10-29$ge2018-10-29");
        assertSearchDoesntReturnSavedResource("composite-date", "2018-10-29$sa2018-10-29");
        assertSearchDoesntReturnSavedResource("composite-date", "2018-10-29$eb2018-10-29");
        assertSearchReturnsSavedResource("composite-date", "2018-10-29$ap2018-10-29");
    }

    @Test
    public void testSearchDate_date_missing() throws Exception {
        assertSearchReturnsSavedResource("composite-date:missing", "false");
        assertSearchDoesntReturnSavedResource("composite-date:missing", "true");
    }

    @Test
    public void testSearchDate_date_missing_token_boolean() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>(1);
        queryParms.put("composite-date:missing", Collections.singletonList("false"));
        queryParms.put("composite-boolean", Collections.singletonList("true$true"));
        assertTrue(searchReturnsResource(Basic.class, queryParms, savedResource));
        queryParms.clear();
        queryParms.put("composite-date:missing", Collections.singletonList("true"));
        queryParms.put("composite-boolean", Collections.singletonList("true$true"));
        assertFalse(searchReturnsResource(Basic.class, queryParms, savedResource));
    }

    @Test
    public void testSearchDate_date_missing_token_boolean_missing() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>(1);
        queryParms.put("composite-date:missing", Collections.singletonList("false"));
        queryParms.put("composite-boolean:missing", Collections.singletonList("false"));
        assertTrue(searchReturnsResource(Basic.class, queryParms, savedResource));
        queryParms.clear();
        queryParms.put("composite-date:missing", Collections.singletonList("false"));
        queryParms.put("composite-boolean:missing", Collections.singletonList("true"));
        assertFalse(searchReturnsResource(Basic.class, queryParms, savedResource));
    }

    @Test
    public void testSearchDate_date_chained() throws Exception {
        assertSearchReturnsComposition("subject:Basic.composite-date", "2018-10-29$2018-10-29");
        assertSearchDoesntReturnComposition("subject:Basic.composite-date", "2025-10-29$2025-10-29");
    }

    @Test
    public void testSearchDate_date_revinclude() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>(1);
        queryParms.put("_revinclude", Collections.singletonList("Composition:subject"));
        queryParms.put("composite-date", Collections.singletonList("2018-10-29$2018-10-29"));
        assertTrue(searchReturnsResource(Basic.class, queryParms, savedResource));
        assertTrue(searchReturnsResource(Basic.class, queryParms, composition));
    }

    @Test
    public void testSearchDate_date_or() throws Exception {
        assertSearchReturnsSavedResource("composite-date", "2018-10-29$2018-10-29,9999-01-01$2018-10-29");
        assertSearchReturnsSavedResource("composite-date", "9999-01-01$2018-10-29,2018-10-29$2018-10-29");
    }

    @Test
    public void testSearchDate_dateTime() throws Exception {
        // "dateTime" is 2018-10-29T17:12:00-04:00
        assertSearchReturnsSavedResource("composite-dateTime", "2018-10-29$2018-10-29");
    }

    @Test
    public void testSearchDate_instant() throws Exception {
        // "instant" is 2018-10-29T17:12:00-04:00
        assertSearchReturnsSavedResource("composite-instant", "2018-10-29T17:12:44-04:00$2018-10-29T17:12:44-04:00");
    }

    @Test
    public void testSearchDate_Period() throws Exception {
        // "Period" is 2018-10-29T17:12:00-04:00 to 2018-10-29T17:18:00-04:00
        assertSearchReturnsSavedResource("composite-Period", "2018-10-29$2018-10-29");
    }

    //////////////////
    // Number tests //
    //////////////////
    @Test
    public void testSearchNumber_integer() throws Exception {
        assertSearchReturnsSavedResource("composite-integer", "12$12");
    }

    @Test
    public void testSearchNumber_decimal() throws Exception {
        // Targeted Value is 99.99

        // Range: 99.985, 99.995
        assertSearchReturnsSavedResource("composite-decimal", "99.99$99.99");
    }

    //////////////////
    // String tests //
    //////////////////
    @Test
    public void testSearchString_string() throws Exception {
        assertSearchReturnsSavedResource("composite-string", "testString$testString");
        assertSearchReturnsSavedResource("composite-string", "test$test");
        assertSearchDoesntReturnSavedResource("composite-string", "String$String");
    }

    //////////////////////////
    // Multiple types tests //
    //////////////////////////
    @Test
    public void testSearchMultiple_string_code_date_integer() throws Exception {
        assertSearchReturnsSavedResource("composite-string-code-date-integer", "test$code$2018-10-29$12");
        assertSearchDoesntReturnSavedResource("composite-string-code-date-integer", "string$code$2018-10-29$12");
        assertSearchDoesntReturnSavedResource("composite-string-code-date-integer", "testString$badcode$2018-10-29$12");
        assertSearchDoesntReturnSavedResource("composite-string-code-date-integer", "testString$code$2020-10-29$12");
        assertSearchDoesntReturnSavedResource("composite-string-code-date-integer", "testString$code$2018-10-29$0");
    }
}
