/*
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.search.test;

import static org.testng.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.model.resource.Basic;
import com.ibm.fhir.model.test.TestUtil;

/**
 * <a href="https://hl7.org/fhir/search.html#date>FHIR Specification: Search
 * - Date</a> Tests
 */
public abstract class AbstractSearchDateTest extends AbstractPLSearchTest {

    protected Basic getBasicResource() throws Exception {
        return TestUtil.readExampleResource("json/ibm/basic/BasicDate.json");
    }

    protected void setTenant() throws Exception {
        FHIRRequestContext.get().setTenantId("date");
    }

    @Test
    public void testSearchDate_date() throws Exception {
        // "date" is 2018-10-29
        assertSearchReturnsSavedResource("date", "2018-10-29");
        assertSearchDoesntReturnSavedResource("date", "ne2018-10-29");
        assertSearchDoesntReturnSavedResource("date", "lt2018-10-29");
        assertSearchDoesntReturnSavedResource("date", "gt2018-10-29");
        assertSearchReturnsSavedResource("date", "le2018-10-29");
        assertSearchReturnsSavedResource("date", "ge2018-10-29");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-29");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-29");
        assertSearchReturnsSavedResource("date", "ap2018-10-29");

        // Specificity, says the EQ range does not overlap. 
        assertSearchDoesntReturnSavedResource("date", "2018-10-29T17:12:00-04:00");

        // Not equals is the inverse of the previous. 
        assertSearchReturnsSavedResource("date", "ne2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource("date", "lt2018-10-29T17:12:00-04:00");
        // Date is actually 2018-10-29, so this would not be true, a second test is added to show a true.
        assertSearchDoesntReturnSavedResource("date", "gt2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource("date", "gt2018-10-28T17:12:00-04:00");
        assertSearchReturnsSavedResource("date", "le2018-10-29T17:12:00-04:00");
        // Date is actually 2018-10-29, so this would not be true, a second test is added to show a true. 
        assertSearchDoesntReturnSavedResource("date", "ge2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource("date", "ge2018-10-28T17:12:00-04:00");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource("date", "eb2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource("date", "ap2018-10-29T17:12:00-04:00");

        assertSearchDoesntReturnSavedResource("date", "2018-10-28");
        assertSearchReturnsSavedResource("date", "ne2018-10-28");
        assertSearchDoesntReturnSavedResource("date", "lt2018-10-28");
        assertSearchReturnsSavedResource("date", "gt2018-10-28");
        assertSearchDoesntReturnSavedResource("date", "le2018-10-28");
        assertSearchReturnsSavedResource("date", "ge2018-10-28");
        assertSearchReturnsSavedResource("date", "sa2018-10-28");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-28");
        assertSearchReturnsSavedResource("date", "ap2018-10-28");

        assertSearchReturnsSavedResource("date", "2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("date", "ne2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("date", "lt2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("date", "gt2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("date", "gt2018-10-27T23:59:59.999999Z");
        assertSearchReturnsSavedResource("date", "le2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("date", "ge2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("date", "ap2018-10-28T23:59:59.999999Z");

        assertSearchDoesntReturnSavedResource("date", "2018-10-30");
        assertSearchReturnsSavedResource("date", "ne2018-10-30");
        assertSearchReturnsSavedResource("date", "lt2018-10-30");
        assertSearchDoesntReturnSavedResource("date", "gt2018-10-30");
        assertSearchReturnsSavedResource("date", "le2018-10-30");
        assertSearchDoesntReturnSavedResource("date", "ge2018-10-30");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-30");
        assertSearchReturnsSavedResource("date", "eb2018-10-30");
        assertSearchReturnsSavedResource("date", "ap2018-10-30");

        assertSearchDoesntReturnSavedResource("date", "2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource("date", "ne2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource("date", "lt2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("date", "gt2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource("date", "le2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("date", "ge2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource("date", "eb2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource("date", "ap2018-10-30T00:00:00.000001Z");

        // The test is checking against ... valueDate": "2018-10-29"
        // As this test is using an implied range with SECONDS, it's the proper behavior. 
        assertSearchDoesntReturnSavedResource("date", "2018-10-29T21:12:00");
        assertSearchReturnsSavedResource("date", "2018-10-29T00:00:00.000000Z");
    }

    @Test
    public void testSearchDate_date_missing() throws Exception {
        assertSearchReturnsSavedResource("date:missing", "false");
        assertSearchDoesntReturnSavedResource("date:missing", "true");

        assertSearchReturnsSavedResource("missing-date:missing", "true");
        assertSearchDoesntReturnSavedResource("missing-date:missing", "false");
    }

    @Test
    public void testSearchDate_date_chained() throws Exception {
        // Date is specific - 2018-10-29
        assertSearchReturnsComposition("subject:Basic.date", "2018-10-29");
        assertSearchDoesntReturnComposition("subject:Basic.date", "2018-10-29T17:12:00-04:00");
        assertSearchDoesntReturnComposition("subject:Basic.date", "2018-10-29T17:12:00");
        assertSearchDoesntReturnComposition("subject:Basic.date", "2025-10-29");
    }

    @Test
    public void testSearchDate_date_revinclude() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>(1);
        queryParms.put("_revinclude", Collections.singletonList("Composition:subject"));
        queryParms.put("date", Collections.singletonList("2018-10-29"));
        assertTrue(searchReturnsResource(Basic.class, queryParms, savedResource));
        assertTrue(searchReturnsResource(Basic.class, queryParms, composition));
    }

    @Test
    public void testSearchDate_date_or() throws Exception {
        // Date is 2018-10-29
        assertSearchReturnsSavedResource("date", "2018-10-29,9999-01-01");
        assertSearchReturnsSavedResource("date", "9999-01-01,2018-10-29");
        assertSearchDoesntReturnSavedResource("date", "ne2018-10-29,9999-01-01");
        assertSearchDoesntReturnSavedResource("date", "lt2018-10-29,9999-01-01");

        assertSearchDoesntReturnSavedResource("date", "gt2018-10-29,9999-01-01");
        assertSearchReturnsSavedResource("date", "le2018-10-29,9999-01-01");
        assertSearchReturnsSavedResource("date", "ge2018-10-29,9999-01-01");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-29,9999-01-01");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-29,9999-01-01");

        assertSearchDoesntReturnSavedResource("date", "9999-01-01,lt2018-10-28");
        assertSearchReturnsSavedResource("date", "9999-01-01,lt2018-10-30");
        assertSearchDoesntReturnSavedResource("date", "9999-01-01,lt2018-10-29");
        assertSearchDoesntReturnSavedResource("date", "9999-01-01,gt2018-10-29");

        assertSearchReturnsSavedResource("date", "9999-01-01,le2018-10-29");
        assertSearchReturnsSavedResource("date", "9999-01-01,ge2018-10-29");

        assertSearchReturnsSavedResource("date", "9999-01-01,sa2018-10-28");
        assertSearchDoesntReturnSavedResource("date", "9999-01-01,sa2018-10-29");
        assertSearchDoesntReturnSavedResource("date", "9999-01-01,eb2018-10-29");
    }

    @Test
    public void testSearchDate_date_or_NE() throws Exception {
        // "date" is 2018-10-29
        assertSearchReturnsSavedResource("date", "9999-10-29,ne2018-10-28");
        assertSearchDoesntReturnSavedResource("date", "9999-01-01,ne2018-10-29");
        assertSearchDoesntReturnSavedResource("date", "ne2018-10-29,ne2018-10-29");

    }

    @Test
    public void testSearchDate_date_or_AP() throws Exception {
        assertSearchReturnsSavedResource("date", "ap2018-10-29,9999-01-01");
        assertSearchReturnsSavedResource("date", "9999-01-01,ap2018-10-29");
    }

    @Test
    public void testSearchDate_dateTime() throws Exception {
        // "dateTime" is 2018-10-29T17:12:00-04:00
        assertSearchReturnsSavedResource("dateTime", "2018-10-29");
        assertSearchDoesntReturnSavedResource("dateTime", "ne2018-10-29");
        assertSearchDoesntReturnSavedResource("dateTime", "lt2018-10-29");
        assertSearchDoesntReturnSavedResource("dateTime", "gt2018-10-29");
        assertSearchReturnsSavedResource("dateTime", "le2018-10-29");
        assertSearchReturnsSavedResource("dateTime", "ge2018-10-29");
        assertSearchReturnsSavedResource("dateTime", "sa2018-10-28");
        assertSearchDoesntReturnSavedResource("dateTime", "eb2018-10-29");
        assertSearchReturnsSavedResource("dateTime", "ap2018-10-29");

        assertSearchReturnsSavedResource("dateTime", "2018-10-29T17:12:00-04:00");
        assertSearchDoesntReturnSavedResource("dateTime", "ne2018-10-29T17:12:00-04:00");
        assertSearchDoesntReturnSavedResource("dateTime", "lt2018-10-29T17:12:00-04:00");
        assertSearchDoesntReturnSavedResource("dateTime", "gt2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource("dateTime", "le2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource("dateTime", "ge2018-10-29T17:12:00-04:00");
        assertSearchDoesntReturnSavedResource("dateTime", "sa2018-10-29T17:12:00-04:00");
        assertSearchDoesntReturnSavedResource("dateTime", "eb2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource("dateTime", "ap2018-10-29T17:12:00-04:00");

        assertSearchDoesntReturnSavedResource("dateTime", "2018-10-28");
        assertSearchReturnsSavedResource("dateTime", "ne2018-10-28");
        assertSearchDoesntReturnSavedResource("dateTime", "lt2018-10-28");
        assertSearchReturnsSavedResource("dateTime", "gt2018-10-28");
        assertSearchDoesntReturnSavedResource("dateTime", "le2018-10-28");
        assertSearchReturnsSavedResource("dateTime", "ge2018-10-28");
        assertSearchReturnsSavedResource("dateTime", "sa2018-10-28");
        assertSearchDoesntReturnSavedResource("dateTime", "eb2018-10-28");
        assertSearchReturnsSavedResource("dateTime", "ap2018-10-28");

        assertSearchDoesntReturnSavedResource("dateTime", "2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("dateTime", "ne2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("dateTime", "lt2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("dateTime", "gt2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("dateTime", "le2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("dateTime", "ge2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("dateTime", "sa2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("dateTime", "eb2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("dateTime", "ap2018-10-28T23:59:59.999999Z");

        assertSearchDoesntReturnSavedResource("dateTime", "2018-10-30");
        assertSearchReturnsSavedResource("dateTime", "ne2018-10-30");
        assertSearchReturnsSavedResource("dateTime", "lt2018-10-30");
        assertSearchDoesntReturnSavedResource("dateTime", "gt2018-10-30");
        assertSearchReturnsSavedResource("dateTime", "le2018-10-30");
        assertSearchDoesntReturnSavedResource("dateTime", "ge2018-10-30");
        assertSearchDoesntReturnSavedResource("dateTime", "sa2018-10-30");
        assertSearchReturnsSavedResource("dateTime", "eb2018-10-30");
        assertSearchReturnsSavedResource("dateTime", "ap2018-10-30");

        assertSearchDoesntReturnSavedResource("dateTime", "2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource("dateTime", "ne2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource("dateTime", "lt2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("dateTime", "gt2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource("dateTime", "le2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("dateTime", "ge2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("dateTime", "sa2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource("dateTime", "eb2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource("dateTime", "ap2018-10-30T00:00:00.000001Z");
    }

    @Test
    public void testSearchDate_dateTime_chained() throws Exception {
        assertSearchReturnsComposition("subject:Basic.dateTime", "2018-10-29T17:12:00-04:00");
        assertSearchReturnsComposition("subject:Basic.dateTime", "2018-10-29");

        assertSearchDoesntReturnSavedResource("subject:Basic.dateTime", "2025-10-29");
    }

    @Test
    public void testSearchDate_dateTime_missing() throws Exception {
        assertSearchReturnsSavedResource("dateTime:missing", "false");
        assertSearchDoesntReturnSavedResource("dateTime:missing", "true");

        assertSearchReturnsSavedResource("missing-dateTime:missing", "true");
        assertSearchDoesntReturnSavedResource("missing-dateTime:missing", "false");
    }

    @Test
    public void testSearchDate_instant() throws Exception {
        assertSearchReturnsSavedResource("instant", "2018-10-29T17:12:44-04:00");
    }

    @Test
    public void testSearchDate_instant_precise() throws Exception {
        // Searching by second should include all instants within that second (regardless of sub-seconds)
        assertSearchReturnsSavedResource("instant-precise", "0001-01-01T01:01:01Z");
        assertSearchDoesntReturnSavedResource("instant-precise", "0001-01-01T01:01:02Z");

        assertSearchReturnsSavedResource("instant-precise", "0001-01-01T01:01:01.1Z");
        assertSearchDoesntReturnSavedResource("instant-precise", "0001-01-01T01:01:01.12Z");

        assertSearchReturnsSavedResource("instant-precise", "0002-02-02T02:02:02.12Z");
        assertSearchDoesntReturnSavedResource("instant-precise", "0002-02-02T02:02:02.123Z");

        assertSearchReturnsSavedResource("instant-precise", "0003-03-03T03:03:03.123Z");
        assertSearchDoesntReturnSavedResource("instant-precise", "0003-03-03T03:03:03.1234Z");

        assertSearchReturnsSavedResource("instant-precise", "0004-04-04T04:04:04.1234Z");
        assertSearchDoesntReturnSavedResource("instant-precise", "0004-04-04T04:04:04.12345Z");

        assertSearchReturnsSavedResource("instant-precise", "0005-05-05T05:05:05.12345Z");
        assertSearchDoesntReturnSavedResource("instant-precise", "0005-05-05T05:05:05.123456Z");

        assertSearchReturnsSavedResource("instant-precise", "0006-06-06T06:06:06Z");
        assertSearchReturnsSavedResource("instant-precise", "0006-06-06T06:06:06.123456Z");
    }

    @Test
    public void testSearchDate_dateTime_precise() throws Exception {
        assertSearchReturnsSavedResource("dateTime-precise", "0001-01-01T01:01:01.1Z");
        assertSearchReturnsSavedResource("dateTime-precise", "0002-02-02T02:02:02.12Z");
        assertSearchReturnsSavedResource("dateTime-precise", "0003-03-03T03:03:03.123Z");
        assertSearchReturnsSavedResource("dateTime-precise", "0004-04-04T04:04:04.1234Z");
        assertSearchReturnsSavedResource("dateTime-precise", "0005-05-05T05:05:05.12345Z");
        assertSearchReturnsSavedResource("dateTime-precise", "0006-06-06T06:06:06.123456Z");
    }

    @Test
    public void testSearchDate_instant_chained() throws Exception {
        assertSearchReturnsComposition("subject:Basic.instant", "2018-10-29T17:12:44-04:00");
    }

    @Test
    public void testSearchDate_instant_missing() throws Exception {
        assertSearchReturnsSavedResource("instant:missing", "false");
        assertSearchDoesntReturnSavedResource("instant:missing", "true");

        assertSearchReturnsSavedResource("missing-instant:missing", "true");
        assertSearchDoesntReturnSavedResource("missing-instant:missing", "false");
    }

    ///////////////
    // Period tests
    ///////////////

    /*
     * <pre>
     * "url": "http://example.org/Period",
     * "start": "2018-10-29T17:12:00-04:00",
     * "end": "2018-10-29T17:18:00-04:00"
     * <br>
     * Translated to UTC:
     * 2018-10-29T21:12:00
     * 2018-10-29T21:18:00
     * </pre>
     */

    @Test
    public void testSearchDate_Period_EQ_Year() throws Exception {
        assertSearchReturnsSavedResource("Period", "2018");
        assertSearchDoesntReturnSavedResource("Period", "2017");
        assertSearchDoesntReturnSavedResource("Period", "2019");
    }

    @Test
    public void testSearchDate_Period_EQ_Month() throws Exception {
        assertSearchReturnsSavedResource("Period", "2018-10");
        assertSearchDoesntReturnSavedResource("Period", "2018-09");
        assertSearchDoesntReturnSavedResource("Period", "2018-11");
    }

    @Test
    public void testSearchDate_Period_EQ_Day() throws Exception {
        assertSearchReturnsSavedResource("Period", "2018-10-29");
        assertSearchDoesntReturnSavedResource("Period", "2018-10-28");
        assertSearchDoesntReturnSavedResource("Period", "2018-10-30");
    }

    @Test
    public void testSearchDate_Period_EQ_Hours() throws Exception {
        assertSearchReturnsSavedResource("Period", "2018-10-29T21");
        assertSearchDoesntReturnSavedResource("Period", "2018-10-29T11");
        assertSearchDoesntReturnSavedResource("Period", "2018-10-29T17");
        assertSearchDoesntReturnSavedResource("Period", "2018-10-29T22");
        assertSearchDoesntReturnSavedResource("Period", "2018-10-29T20");
    }

    @Test
    public void testSearchDate_Period_EQ_Minutes() throws Exception {
        // The Search Value of the implied range DOES NOT contain the start/end fully. 
        assertSearchDoesntReturnSavedResource("Period", "2018-10-29T21:13");
    }

    @Test
    public void testSearchDate_Period_EQ_Seconds() throws Exception {
        // The Search Value of the implied range DOES NOT contain the start/end fully.
        assertSearchDoesntReturnSavedResource("Period", "2018-10-29T21:13:00");
        assertSearchDoesntReturnSavedResource("Period", "2018-10-29T21:13:00.000123");
    }

    @Test
    public void testSearchDate_Period_EQ_ZonedDateTime() throws Exception {
        assertSearchReturnsSavedResource("Period", "2018-10-29T21-00:00");
        assertSearchReturnsSavedResource("Period", "2018-10-29T17-04:00");
        assertSearchDoesntReturnSavedResource("Period", "2018-10-29T17-05:00");
        assertSearchDoesntReturnSavedResource("Period", "2018-10-29T17:12:00-04:00");
        assertSearchDoesntReturnSavedResource("Period", "2018-10-29T17:12:44-04:00");

        assertSearchReturnsSavedResource("Period", "eq2018-10-29T21-00:00");
        assertSearchReturnsSavedResource("Period", "eq2018-10-29T17-04:00");
        assertSearchDoesntReturnSavedResource("Period", "eq2018-10-29T17-05:00");
        assertSearchDoesntReturnSavedResource("Period", "eq2018-10-29T17:12:00-04:00");
        assertSearchDoesntReturnSavedResource("Period", "eq2018-10-29T17:12:44-04:00");

        assertSearchDoesntReturnSavedResource("Period", "2018-10-29T17:18:00-04:00");
        assertSearchDoesntReturnSavedResource("Period", "2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("Period", "2018-10-28T23:59:59.999999Z");

    }

    @Test
    public void testSearchDate_Period_LT_Year() throws Exception {
        assertSearchDoesntReturnSavedResource("Period", "lt2018");
        assertSearchReturnsSavedResource("Period", "lt2019");
        assertSearchReturnsSavedResource("Period", "lt2020");
    }

    @Test
    public void testSearchDate_Period_LT_Month() throws Exception {
        assertSearchDoesntReturnSavedResource("Period", "lt2018-10");
        assertSearchReturnsSavedResource("Period", "lt2018-11");
    }

    @Test
    public void testSearchDate_Period_LT_Day() throws Exception {
        assertSearchDoesntReturnSavedResource("Period", "lt2018-10-29");
        assertSearchReturnsSavedResource("Period", "lt2018-10-30");
        assertSearchDoesntReturnSavedResource("Period", "lt2018-10-28");
    }

    @Test
    public void testSearchDate_Period_LT_Hours() throws Exception {
        assertSearchReturnsSavedResource("Period", "lt2018-10-29T22");
        assertSearchReturnsSavedResource("Period", "lt2018-10-29T22");
        assertSearchDoesntReturnSavedResource("Period", "lt2018-10-29T21");
        assertSearchDoesntReturnSavedResource("Period", "lt2018-10-29T17");
        assertSearchDoesntReturnSavedResource("Period", "lt2017-10-29T22");
        assertSearchDoesntReturnSavedResource("Period", "lt2018-10-29T20");
    }

    @Test
    public void testSearchDate_Period_LT_Minutes() throws Exception {
        assertSearchReturnsSavedResource("Period", "lt2018-10-29T21:19");
        assertSearchDoesntReturnSavedResource("Period", "lt2018-10-29T21:00:00");
        assertSearchReturnsSavedResource("Period", "lt2018-10-29T21:18:01");
    }

    @Test
    public void testSearchDate_Period_LT_Seconds() throws Exception {
        assertSearchReturnsSavedResource("Period", "lt2018-10-29T21:19:00");
        assertSearchDoesntReturnSavedResource("Period", "lt2018-10-29T21:13:00");
        assertSearchDoesntReturnSavedResource("Period", "lt2018-10-29T21:13:00.000123");
        assertSearchDoesntReturnSavedResource("Period", "lt2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("Period", "lt2018-10-30T00:00:00.000001Z");
    }

    @Test
    public void testSearchDate_Period_LT_ZonedDateTime() throws Exception {
        assertSearchReturnsSavedResource("Period", "lt2018-10-29T22-00:00");
        assertSearchReturnsSavedResource("Period", "lt2018-10-29T18-04:00");
        assertSearchDoesntReturnSavedResource("Period", "lt2018-10-29T15-05:00");
        assertSearchReturnsSavedResource("Period", "lt2018-10-29T17:18:01-04:00");
        assertSearchDoesntReturnSavedResource("Period", "lt2018-10-29T17:18:00-04:00");
        assertSearchDoesntReturnSavedResource("Period", "lt2018-10-29T17:12:00-04:00");
    }

    @Test
    public void testSearchDate_Period_LE_Year() throws Exception {
        assertSearchDoesntReturnSavedResource("Period", "le2017");
        assertSearchReturnsSavedResource("Period", "le2018");
        assertSearchReturnsSavedResource("Period", "le2019");
        assertSearchReturnsSavedResource("Period", "le2020");
    }

    @Test
    public void testSearchDate_Period_LE_Month() throws Exception {
        assertSearchDoesntReturnSavedResource("Period", "le2018-09");
        assertSearchReturnsSavedResource("Period", "le2018-10");
        assertSearchReturnsSavedResource("Period", "le2018-11");
    }

    @Test
    public void testSearchDate_Period_LE_Day() throws Exception {
        assertSearchDoesntReturnSavedResource("Period", "le2018-10-27");
        assertSearchDoesntReturnSavedResource("Period", "le2018-10-28");
        assertSearchReturnsSavedResource("Period", "le2018-10-29");
        assertSearchReturnsSavedResource("Period", "le2018-10-30");
    }

    @Test
    public void testSearchDate_Period_LE_Hours() throws Exception {
        assertSearchDoesntReturnSavedResource("Period", "le2017-10-29T22");
        assertSearchDoesntReturnSavedResource("Period", "le2018-10-29T17");
        assertSearchDoesntReturnSavedResource("Period", "le2018-10-29T20");
        assertSearchDoesntReturnSavedResource("Period", "le2018-10-28T21");
        assertSearchReturnsSavedResource("Period", "le2018-10-29T21");
        assertSearchReturnsSavedResource("Period", "le2018-10-29T22");
        assertSearchReturnsSavedResource("Period", "le2018-10-29T23");
    }

    @Test
    public void testSearchDate_Period_LE_Minutes() throws Exception {
        assertSearchReturnsSavedResource("Period", "le2018-10-29T21:19");
        assertSearchDoesntReturnSavedResource("Period", "le2018-10-29T21:00:00");
        assertSearchReturnsSavedResource("Period", "le2018-10-29T21:18:01");
    }

    @Test
    public void testSearchDate_Period_LE_Seconds() throws Exception {
        assertSearchReturnsSavedResource("Period", "le2018-10-29T21:19:00");
        assertSearchDoesntReturnSavedResource("Period", "le2018-10-29T21:13:00");
        assertSearchDoesntReturnSavedResource("Period", "le2018-10-29T21:13:00.000123");
        assertSearchDoesntReturnSavedResource("Period", "le2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("Period", "le2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("Period", "le2018-10-28T23:59:59.999999Z");
    }

    @Test
    public void testSearchDate_Period_LE_ZonedDateTime() throws Exception {
        assertSearchReturnsSavedResource("Period", "le2018-10-29T22-00:00");
        assertSearchReturnsSavedResource("Period", "le2018-10-29T18-04:00");
        assertSearchDoesntReturnSavedResource("Period", "le2018-10-29T15-05:00");
        assertSearchReturnsSavedResource("Period", "le2018-10-29T17:18:01-04:00");
        assertSearchDoesntReturnSavedResource("Period", "le2018-10-29T17:00:00-04:00");
        assertSearchDoesntReturnSavedResource("Period", "le2018-10-29T17:12:44-04:00");
        assertSearchReturnsSavedResource("Period", "le2018-10-29T17:18:00-04:00");
    }

    @Test
    public void testSearchDate_Period_GE_Year() throws Exception {
        assertSearchReturnsSavedResource("Period", "ge1901");
        assertSearchReturnsSavedResource("Period", "ge2018");
        assertSearchDoesntReturnSavedResource("Period", "ge2019");
        assertSearchDoesntReturnSavedResource("Period", "ge2020");
    }

    @Test
    public void testSearchDate_Period_GE_Month() throws Exception {
        assertSearchReturnsSavedResource("Period", "ge2018-09");
        assertSearchReturnsSavedResource("Period", "ge2018-10");
        assertSearchDoesntReturnSavedResource("Period", "ge2018-11");

    }

    @Test
    public void testSearchDate_Period_GE_Day() throws Exception {
        assertSearchReturnsSavedResource("Period", "ge2018-10-28");
        assertSearchReturnsSavedResource("Period", "ge2018-10-29");
        assertSearchDoesntReturnSavedResource("Period", "ge2018-10-30");
    }

    @Test
    public void testSearchDate_Period_GE_Hours() throws Exception {
        assertSearchDoesntReturnSavedResource("Period", "ge2019-10-29T22");
        assertSearchDoesntReturnSavedResource("Period", "ge2018-10-29T22");
        assertSearchReturnsSavedResource("Period", "ge2018-10-29T21");
        assertSearchReturnsSavedResource("Period", "ge2018-10-29T17");
        assertSearchReturnsSavedResource("Period", "ge2017-10-29T22");
        assertSearchReturnsSavedResource("Period", "ge2018-10-29T20");
    }

    @Test
    public void testSearchDate_Period_GE_Minutes() throws Exception {
        assertSearchDoesntReturnSavedResource("Period", "ge2018-10-29T21:19");
        assertSearchDoesntReturnSavedResource("Period", "ge2018-10-29T21:17");
        assertSearchDoesntReturnSavedResource("Period", "ge2018-10-29T21:18");
        assertSearchReturnsSavedResource("Period", "ge2017-10-29T21:12:00");
    }

    @Test
    public void testSearchDate_Period_GE_Seconds() throws Exception {
        assertSearchReturnsSavedResource("Period", "ge2018-10-29T21:12:00");
        assertSearchDoesntReturnSavedResource("Period", "ge2018-10-29T21:13:00");
        assertSearchDoesntReturnSavedResource("Period", "ge2018-10-29T21:13:00.000123");
        assertSearchReturnsSavedResource("Period", "ge2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("Period", "ge2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource("Period", "ge2018-10-28T23:59:59.999999Z");
    }

    @Test
    public void testSearchDate_Period_GE_ZonedDateTime() throws Exception {
        assertSearchDoesntReturnSavedResource("Period", "ge2018-10-29T22-00:00");
        assertSearchReturnsSavedResource("Period", "ge2018-10-29T21-00:00");

        assertSearchReturnsSavedResource("Period", "ge2018-10-29T17-04:00");
        assertSearchReturnsSavedResource("Period", "ge2018-10-29T15-05:00");
        assertSearchReturnsSavedResource("Period", "ge2018-10-29T17:11:01-04:00");
        assertSearchReturnsSavedResource("Period", "ge2018-10-29T17:00:00-04:00");
        assertSearchDoesntReturnSavedResource("Period", "ge2018-10-29T18:18:00-04:00");
        assertSearchReturnsSavedResource("Period", "ge2018-10-29T17:12:00-04:00");
        assertSearchDoesntReturnSavedResource("Period", "ge2018-10-29T17:12:44-04:00");
        assertSearchDoesntReturnSavedResource("Period", "ge2018-10-29T17:18:00-04:00");
    }

    @Test
    public void testSearchDate_Period_GT_Year() throws Exception {
        assertSearchReturnsSavedResource("Period", "gt2017");
        assertSearchDoesntReturnSavedResource("Period", "gt2018");
        assertSearchDoesntReturnSavedResource("Period", "gt2019");
        assertSearchDoesntReturnSavedResource("Period", "gt2020");
    }

    @Test
    public void testSearchDate_Period_GT_Month() throws Exception {
        assertSearchReturnsSavedResource("Period", "gt2018-09");
        assertSearchDoesntReturnSavedResource("Period", "gt2018-10");
        assertSearchDoesntReturnSavedResource("Period", "gt2018-11");
    }

    @Test
    public void testSearchDate_Period_GT_Day() throws Exception {
        assertSearchReturnsSavedResource("Period", "gt2018-10-28");
        assertSearchDoesntReturnSavedResource("Period", "gt2018-10-29");
        assertSearchDoesntReturnSavedResource("Period", "gt2018-10-30");
        assertSearchDoesntReturnSavedResource("Period", "gt2018-10-30");
    }

    @Test
    public void testSearchDate_Period_GT_Hours() throws Exception {
        assertSearchReturnsSavedResource("Period", "gt2018-10-29T20");
        assertSearchReturnsSavedResource("Period", "gt2018-10-29T17");
        assertSearchDoesntReturnSavedResource("Period", "gt2018-10-29T22");
        assertSearchReturnsSavedResource("Period", "gt2017-10-29T22");
        assertSearchDoesntReturnSavedResource("Period", "gt2018-10-29T21");
    }

    @Test
    public void testSearchDate_Period_GT_Minutes() throws Exception {
        assertSearchReturnsSavedResource("Period", "gt2018-10-29T21:11");
        assertSearchDoesntReturnSavedResource("Period", "gt2018-10-29T21:12");
        assertSearchDoesntReturnSavedResource("Period", "gt2018-10-29T21:13");
        assertSearchReturnsSavedResource("Period", "gt2018-10-29T21:00");
    }

    @Test
    public void testSearchDate_Period_GT_Seconds() throws Exception {
        assertSearchReturnsSavedResource("Period", "gt2018-10-29T21:11:00");
        assertSearchDoesntReturnSavedResource("Period", "gt2018-10-29T21:12:00");
        assertSearchDoesntReturnSavedResource("Period", "gt2018-10-29T21:13:00");
        assertSearchDoesntReturnSavedResource("Period", "gt2018-10-29T21:13:00.000123");
        assertSearchDoesntReturnSavedResource("Period", "gt2018-10-29T23:59:59.999999Z");
        assertSearchReturnsSavedResource("Period", "gt2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("Period", "gt2018-10-30T00:00:00.000001Z");
    }

    @Test
    public void testSearchDate_Period_GT_ZonedDateTime() throws Exception {
        assertSearchReturnsSavedResource("Period", "gt2018-10-29T20-00:00");
        assertSearchDoesntReturnSavedResource("Period", "gt2018-10-29T18-04:00");
        assertSearchReturnsSavedResource("Period", "gt2018-10-29T15-05:00");
        assertSearchDoesntReturnSavedResource("Period", "gt2018-10-29T17:18:01-04:00");
        assertSearchReturnsSavedResource("Period", "gt2018-10-29T17:11:59-04:00");
        assertSearchReturnsSavedResource("Period", "gt2018-10-29T17:12:44-03:00");
        assertSearchReturnsSavedResource("Period", "gt2018-10-29T17:12:00-03:00");
        assertSearchDoesntReturnSavedResource("Period", "gt2018-10-29T17:18:00-05:00");
    }

    @Test
    public void testSearchDate_Period_SA_Year() throws Exception {
        assertSearchReturnsSavedResource("Period", "sa2017");
        assertSearchDoesntReturnSavedResource("Period", "sa2018");
        assertSearchDoesntReturnSavedResource("Period", "sa2019");
        assertSearchDoesntReturnSavedResource("Period", "sa2020");
    }

    @Test
    public void testSearchDate_Period_SA_Month() throws Exception {
        assertSearchReturnsSavedResource("Period", "sa2018-09");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-11");
    }

    @Test
    public void testSearchDate_Period_SA_Day() throws Exception {
        assertSearchReturnsSavedResource("Period", "sa2018-10-28");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-29");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-30");
    }

    @Test
    public void testSearchDate_Period_SA_Hours() throws Exception {
        assertSearchReturnsSavedResource("Period", "sa2018-10-29T17");
        assertSearchReturnsSavedResource("Period", "sa2018-10-29T20");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-29T21");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-29T22");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-30T20");
    }

    @Test
    public void testSearchDate_Period_SA_Minutes() throws Exception {
        assertSearchReturnsSavedResource("Period", "sa2018-10-29T21:11");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-29T21:13");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-29T21:19");
    }

    @Test
    public void testSearchDate_Period_SA_Seconds() throws Exception {
        assertSearchReturnsSavedResource("Period", "sa2018-10-29T21:00:00");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-29T21:19:00");
        assertSearchReturnsSavedResource("Period", "sa2018-10-28T20:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-29T21:12:01");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-29T21:19:00");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-29T21:13:00");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-29T21:13:00.000123");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-30T00:00:00.000001Z");

    }

    @Test
    public void testSearchDate_Period_SA_ZonedDateTime() throws Exception {
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-29T22-00:00");
        assertSearchReturnsSavedResource("Period", "sa2018-10-29T18-02:00");
        assertSearchReturnsSavedResource("Period", "sa2018-10-29T15-05:00");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-29T17:18:01-04:00");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-29T17:18:00-04:00");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-29T17:12:00-04:00");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-29T17:12:44-04:00");
    }

    @Test
    public void testSearchDate_Period_EB_Year() throws Exception {
        assertSearchDoesntReturnSavedResource("Period", "eb2018");
        assertSearchReturnsSavedResource("Period", "eb2019");
        assertSearchReturnsSavedResource("Period", "eb2020");
    }

    @Test
    public void testSearchDate_Period_EB_Month() throws Exception {
        assertSearchDoesntReturnSavedResource("Period", "eb2018-10");
        assertSearchReturnsSavedResource("Period", "eb2018-11");
    }

    @Test
    public void testSearchDate_Period_EB_Day() throws Exception {
        assertSearchDoesntReturnSavedResource("Period", "eb2018-10-28");
        assertSearchDoesntReturnSavedResource("Period", "eb2018-10-29");
        assertSearchReturnsSavedResource("Period", "eb2018-10-30");
    }

    @Test
    public void testSearchDate_Period_EB_Hours() throws Exception {
        assertSearchReturnsSavedResource("Period", "eb2018-10-29T22");
        assertSearchReturnsSavedResource("Period", "eb2018-10-29T22");
        assertSearchDoesntReturnSavedResource("Period", "eb2018-10-29T21");
        assertSearchDoesntReturnSavedResource("Period", "eb2018-10-29T17");
        assertSearchDoesntReturnSavedResource("Period", "eb2017-10-29T22");
        assertSearchDoesntReturnSavedResource("Period", "eb2018-10-29T20");
    }

    @Test
    public void testSearchDate_Period_EB_Minutes() throws Exception {
        assertSearchReturnsSavedResource("Period", "eb2018-10-29T21:19");
        assertSearchDoesntReturnSavedResource("Period", "eb2018-10-29T21:00:00");
        assertSearchReturnsSavedResource("Period", "eb2018-10-29T21:18:01");
    }

    @Test
    public void testSearchDate_Period_EB_Seconds() throws Exception {
        assertSearchReturnsSavedResource("Period", "eb2018-10-29T21:19:00");
        assertSearchDoesntReturnSavedResource("Period", "eb2018-10-29T21:13:00");
        assertSearchDoesntReturnSavedResource("Period", "eb2018-10-29T21:13:00.000123");
        assertSearchDoesntReturnSavedResource("Period", "eb2018-10-28T23:59:59.999999Z");

        assertSearchDoesntReturnSavedResource("Period", "eb2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("Period", "eb2018-10-30T00:00:00.000001Z");

    }

    @Test
    public void testSearchDate_Period_EB_ZonedDateTime() throws Exception {
        assertSearchReturnsSavedResource("Period", "eb2018-10-29T22-00:00");
        assertSearchReturnsSavedResource("Period", "eb2018-10-29T18-04:00");
        assertSearchDoesntReturnSavedResource("Period", "eb2018-10-29T15-05:00");
        assertSearchReturnsSavedResource("Period", "eb2018-10-29T17:18:01-04:00");
        assertSearchDoesntReturnSavedResource("Period", "eb2018-10-29T17:18:00-04:00");

        assertSearchDoesntReturnSavedResource("Period", "eb2018-10-29T17:12:00-04:00");
        assertSearchDoesntReturnSavedResource("Period", "eb2018-10-29T17:12:44-04:00");
        assertSearchDoesntReturnSavedResource("Period", "eb2018-10-29T17:18:00-04:00");
    }

    @Test
    public void testSearchDate_Period_NE_Year() throws Exception {
        assertSearchDoesntReturnSavedResource("Period", "ne2018");
        assertSearchReturnsSavedResource("Period", "ne2019");
        assertSearchReturnsSavedResource("Period", "ne2020");
    }

    @Test
    public void testSearchDate_Period_NE_Month() throws Exception {
        assertSearchDoesntReturnSavedResource("Period", "ne2018-10");
        assertSearchReturnsSavedResource("Period", "ne2018-11");
    }

    @Test
    public void testSearchDate_Period_NE_Day() throws Exception {
        assertSearchDoesntReturnSavedResource("Period", "ne2018-10-29");
        assertSearchReturnsSavedResource("Period", "ne2018-10-30");
        assertSearchReturnsSavedResource("Period", "ne2018-10-28");
    }

    @Test
    public void testSearchDate_Period_NE_Hours() throws Exception {
        assertSearchReturnsSavedResource("Period", "ne2018-10-29T20");
        assertSearchDoesntReturnSavedResource("Period", "ne2018-10-29T21");
        assertSearchReturnsSavedResource("Period", "ne2018-10-29T22");
    }

    @Test
    public void testSearchDate_Period_NE_Minutes() throws Exception {
        assertSearchReturnsSavedResource("Period", "ne2018-10-29T21:00");
        assertSearchReturnsSavedResource("Period", "ne2018-10-29T21:12");
        assertSearchReturnsSavedResource("Period", "ne2018-10-29T21:12");
    }

    @Test
    public void testSearchDate_Period_NE_Seconds() throws Exception {
        assertSearchReturnsSavedResource("Period", "ne2018-10-29T21:18:01");
        assertSearchReturnsSavedResource("Period", "ne2018-10-29T21:19:00");
        assertSearchReturnsSavedResource("Period", "ne2018-10-29T21:13:00");
        assertSearchReturnsSavedResource("Period", "ne2018-10-29T21:13:00.000123");
        assertSearchReturnsSavedResource("Period", "ne2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource("Period", "ne2018-10-28T23:59:59.999999Z");
    }

    @Test
    public void testSearchDate_Period_NE_ZonedDateTime() throws Exception {
        assertSearchReturnsSavedResource("Period", "ne2018-10-29T22-00:00");
        assertSearchReturnsSavedResource("Period", "ne2018-10-29T18-04:00");
        assertSearchDoesntReturnSavedResource("Period", "ne2018-10-29T16-05:00");
        assertSearchReturnsSavedResource("Period", "ne2018-10-29T17:18:01-04:00");
        assertSearchReturnsSavedResource("Period", "ne2018-10-29T17:18:00-04:00");
        assertSearchReturnsSavedResource("Period", "ne2018-10-29T17:12:44-04:00");
        assertSearchReturnsSavedResource("Period", "ne2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource("Period", "ne2018-10-29T17:18:00-04:00");
    }

    @Test
    public void testSearchDate_Period_AP_Year() throws Exception {
        assertSearchReturnsSavedResource("Period", "ap2018");
        assertSearchDoesntReturnSavedResource("Period", "ap2100");
        assertSearchDoesntReturnSavedResource("Period", "ap2000");
        assertSearchDoesntReturnSavedResource("Period", "ap2025");
    }

    @Test
    public void testSearchDate_Period_AP_Month() throws Exception {
        assertSearchReturnsSavedResource("Period", "ap2018-10");
        assertSearchReturnsSavedResource("Period", "ap2018-11");
        assertSearchDoesntReturnSavedResource("Period", "ap2018-01");
    }

    @Test
    public void testSearchDate_Period_AP_Day() throws Exception {
        assertSearchDoesntReturnSavedResource("Period", "ap2018-01-01");
        assertSearchReturnsSavedResource("Period", "ap2018-10-28");
        assertSearchReturnsSavedResource("Period", "ap2018-10-29");
        assertSearchReturnsSavedResource("Period", "ap2018-10-30");
        assertSearchDoesntReturnSavedResource("Period", "ap2019-01-01");
    }

    @Test
    public void testSearchDate_Period_AP_Hours() throws Exception {
        assertSearchReturnsSavedResource("Period", "ap2018-10-29T22");
        assertSearchReturnsSavedResource("Period", "ap2018-10-29T17");
        assertSearchReturnsSavedResource("Period", "ap2018-10-29T20");
        assertSearchDoesntReturnSavedResource("Period", "ap2017-10-29T21");
        assertSearchDoesntReturnSavedResource("Period", "ap2017-10-29T22");
    }

    @Test
    public void testSearchDate_Period_AP_Minutes() throws Exception {
        assertSearchReturnsSavedResource("Period", "ap2018-10-29T21:19");
        assertSearchReturnsSavedResource("Period", "ap2018-10-29T21:00:00");
        assertSearchReturnsSavedResource("Period", "ap2018-10-29T21:18:01");
        assertSearchDoesntReturnSavedResource("Period", "ap2017-10-29T21:00:00");
    }

    @Test
    public void testSearchDate_Period_AP_Seconds() throws Exception {
        assertSearchReturnsSavedResource("Period", "ap2018-10-29T21:19:00");
        assertSearchReturnsSavedResource("Period", "ap2018-10-29T21:13:00");
        assertSearchDoesntReturnSavedResource("Period", "ap2017-10-29T21:13:00.000123");
        assertSearchDoesntReturnSavedResource("Period", "ap2019-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("Period", "ap2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource("Period", "ap2018-10-28T23:59:59.999999Z");
    }

    @Test
    public void testSearchDate_Period_AP_ZonedDateTime() throws Exception {
        assertSearchReturnsSavedResource("Period", "ap2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource("Period", "ap2018-10-29T17:18:00-04:00");
        assertSearchReturnsSavedResource("Period", "ap2018-10-29T17:12:44-04:00");
    }

    @Test
    public void testSearchDate_Period_NoStart() throws Exception {
        // "Period-noStart" has end=2018-10-29T17:18:00-04:00

        // the range of the search value doesn't fully contain the range of the target value
        assertSearchDoesntReturnSavedResource("Period-noStart", "2018-10-29");
        assertSearchReturnsSavedResource("Period-noStart", "ne2018-10-29");
        assertSearchReturnsSavedResource("Period-noStart", "lt2018-10-30");
        assertSearchDoesntReturnSavedResource("Period-noStart", "lt2018-10-29");
        assertSearchDoesntReturnSavedResource("Period-noStart", "gt2018-10-29");
        assertSearchReturnsSavedResource("Period-noStart", "le2018-10-29");
        assertSearchDoesntReturnSavedResource("Period-noStart", "ge2018-10-29");
        assertSearchDoesntReturnSavedResource("Period-noStart", "sa2018-10-29");
        assertSearchDoesntReturnSavedResource("Period-noStart", "eb2018-10-29");
        assertSearchDoesntReturnSavedResource("Period-noStart", "ap2018-10-29");

        // search on the dateTime at the end of the Period-noStart
        // the range of the search value doesn't fully contain the range of the target value
        assertSearchDoesntReturnSavedResource("Period-noStart", "2018-10-29T17:18:00+04:00");
        assertSearchReturnsSavedResource("Period-noStart", "ne2018-10-29T17:18:00-04:00");
        assertSearchDoesntReturnSavedResource("Period-noStart", "lt2018-10-29T17:18:00-04:00");
        assertSearchReturnsSavedResource("Period-noStart", "lt2018-10-29T17:18:01-04:00");
        
        assertSearchDoesntReturnSavedResource("Period-noStart", "gt2018-10-29T17:18:00-04:00");
        assertSearchReturnsSavedResource("Period-noStart", "le2018-10-29T17:18:00-04:00");
        assertSearchDoesntReturnSavedResource("Period-noStart", "ge2018-10-29T17:18:00-04:00");
        assertSearchDoesntReturnSavedResource("Period-noStart", "sa2018-10-29T17:18:00-04:00");
        assertSearchDoesntReturnSavedResource("Period-noStart", "eb2018-10-29T17:18:00-04:00");
        assertSearchDoesntReturnSavedResource("Period-noStart", "ap2018-10-29T17:18:00-04:00");

        assertSearchDoesntReturnSavedResource("Period-noStart", "2018-10-28");
        assertSearchReturnsSavedResource("Period-noStart", "ne2018-10-28");
        assertSearchDoesntReturnSavedResource("Period-noStart", "lt2018-10-28");
        assertSearchReturnsSavedResource("Period-noStart", "lt2018-10-30");
        assertSearchDoesntReturnSavedResource("Period-noStart", "gt2018-10-28");
        assertSearchDoesntReturnSavedResource("Period-noStart", "le2018-10-28");
        assertSearchReturnsSavedResource("Period-noStart", "le2018-10-29");
        assertSearchDoesntReturnSavedResource("Period-noStart", "ge2018-10-28");
        assertSearchDoesntReturnSavedResource("Period-noStart", "sa2018-10-28");
        assertSearchDoesntReturnSavedResource("Period-noStart", "eb2018-10-28");
        assertSearchDoesntReturnSavedResource("Period-noStart", "ap2018-10-28");

        assertSearchDoesntReturnSavedResource("Period-noStart", "2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("Period-noStart", "ne2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("Period-noStart", "lt2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("Period-noStart", "lt2018-10-29T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("Period-noStart", "gt2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("Period-noStart", "le2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("Period-noStart", "le2018-10-29T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("Period-noStart", "ge2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("Period-noStart", "sa2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("Period-noStart", "eb2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("Period-noStart", "ap2018-10-28T23:59:59.999999Z");

        assertSearchDoesntReturnSavedResource("Period-noStart", "2018-10-30");
        assertSearchReturnsSavedResource("Period-noStart", "ne2018-10-30");
        assertSearchReturnsSavedResource("Period-noStart", "lt2018-10-30");
        assertSearchDoesntReturnSavedResource("Period-noStart", "gt2018-10-30");
        assertSearchReturnsSavedResource("Period-noStart", "le2018-10-30");
        assertSearchDoesntReturnSavedResource("Period-noStart", "ge2018-10-30");
        assertSearchDoesntReturnSavedResource("Period-noStart", "sa2018-10-30");
        assertSearchReturnsSavedResource("Period-noStart", "eb2018-10-30");
        assertSearchDoesntReturnSavedResource("Period-noStart", "ap2018-10-30");

        assertSearchDoesntReturnSavedResource("Period-noStart", "2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource("Period-noStart", "ne2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource("Period-noStart", "lt2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("Period-noStart", "gt2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource("Period-noStart", "le2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("Period-noStart", "ge2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("Period-noStart", "sa2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource("Period-noStart", "eb2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("Period-noStart", "ap2018-10-30T00:00:00.000001Z");
    }

    @Test
    public void testSearchDate_Period_NoEnd() throws Exception {
        // "Period-noEnd" has start=2018-10-29T17:12:00-04:00
        // No End -  A missing upper boundary is "greater than" any actual date. 

        // the range of the search value doesn't fully contain the range of the target value
        assertSearchDoesntReturnSavedResource("Period-noEnd", "2018-10-29");
        assertSearchReturnsSavedResource("Period-noEnd", "ne2018-10-29");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "lt2018-10-29");
        assertSearchReturnsSavedResource("Period-noEnd", "gt2018-10-28");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "le2018-10-29");
        assertSearchReturnsSavedResource("Period-noEnd", "ge2018-10-29");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "sa2018-10-29");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "eb2018-10-29");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "ap2010-10-29");

        // search on the dateTime at the start of the Period-noEnd
        // the range of the search value doesn't fully contain the range of the target value
        assertSearchDoesntReturnSavedResource("Period-noEnd", "2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource("Period-noEnd", "ne2018-10-29T17:12:00-04:00");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "lt2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource("Period-noEnd", "gt2018-10-29T17:11:00-04:00");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "le2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource("Period-noEnd", "ge2018-10-29T17:12:00-04:00");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "sa2018-10-29T17:12:00-04:00");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "eb2018-10-29T17:12:00-04:00");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "ap2010-10-29T17:12:00-04:00");

        assertSearchDoesntReturnSavedResource("Period-noEnd", "2018-10-28");
        assertSearchReturnsSavedResource("Period-noEnd", "ne2018-10-28");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "lt2018-10-28");
        assertSearchReturnsSavedResource("Period-noEnd", "gt2018-10-28");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "le2018-10-28");
        assertSearchReturnsSavedResource("Period-noEnd", "ge2018-10-28");
        assertSearchReturnsSavedResource("Period-noEnd", "sa2018-10-28");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "eb2018-10-28");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "ap2010-10-28");

        assertSearchDoesntReturnSavedResource("Period-noEnd", "2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("Period-noEnd", "ne2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "lt2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("Period-noEnd", "gt2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "le2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("Period-noEnd", "ge2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("Period-noEnd", "sa2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "eb2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "ap2010-10-28T23:59:59.999999Z");

        assertSearchDoesntReturnSavedResource("Period-noEnd", "2018-10-30");
        assertSearchReturnsSavedResource("Period-noEnd", "ne2018-10-30");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "lt2018-10-30");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "gt2018-10-30");
        assertSearchReturnsSavedResource("Period-noEnd", "gt2018-10-28");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "gt2018-10-29");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "le2018-10-30");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "ge2018-10-30");
        assertSearchReturnsSavedResource("Period-noEnd", "ge2018-10-29");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "sa2018-10-30");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "eb2018-10-30");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "ap2018-10-30");

        assertSearchDoesntReturnSavedResource("Period-noEnd", "2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource("Period-noEnd", "ne2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "lt2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "gt2018-10-30T00:00:00.000000Z");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "gt2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "le2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "ge2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "sa2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "eb2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "ap2018-10-30T00:00:00.000001Z");

        // FINE: dateValue: Period-noEnd[12], value: [null], period: [2018-10-29 21:12:00.0, 9999-12-31 05:00:00.0]
        // "2018-10-29T17:12:00-04:00"
        assertSearchDoesntReturnSavedResource("Period-noEnd", "ge2018-10-30T00:00:00.000000Z");
        assertSearchReturnsSavedResource("Period-noEnd", "ge2018-10-29T00:00:00.000000Z");
    }

    @Test
    public void testSearchDate_Period_chained() throws Exception {
        assertSearchReturnsComposition("subject:Basic.Period", "2018-10-29");
    }

    @Test
    public void testSearchDate_Period_missing() throws Exception {
        assertSearchReturnsSavedResource("Period:missing", "false");
        assertSearchDoesntReturnSavedResource("Period:missing", "true");

        assertSearchReturnsSavedResource("missing-Period:missing", "true");
        assertSearchDoesntReturnSavedResource("missing-Period:missing", "false");
    }

    @Test
    public void testSearchDate_Period_or() throws Exception {
        assertSearchDoesntReturnSavedResource("Period", "2018-10-28,9999-01-01");
        assertSearchReturnsSavedResource("Period", "ne2018-10-28,9999-01-01");
        assertSearchDoesntReturnSavedResource("Period", "lt2018-10-28,9999-01-01");
        assertSearchReturnsSavedResource("Period", "gt2018-10-28,9999-01-01");
        assertSearchDoesntReturnSavedResource("Period", "le2018-10-28,9999-01-01");
        assertSearchReturnsSavedResource("Period", "ge2018-10-28,9999-01-01");
        assertSearchReturnsSavedResource("Period", "sa2018-10-28,9999-01-01");
        assertSearchDoesntReturnSavedResource("Period", "eb2018-10-28,9999-01-01");
        assertSearchReturnsSavedResource("Period", "ap2018-10-28,9999-01-01");
        assertSearchDoesntReturnSavedResource("Period", "ap2010-10-28,9999-01-01");

        assertSearchDoesntReturnSavedResource("Period", "9999-01-01,2018-10-28");
        assertSearchReturnsSavedResource("Period", "9999-01-01,ne2018-10-28");
        assertSearchDoesntReturnSavedResource("Period", "9999-01-01,lt2018-10-28");
        assertSearchReturnsSavedResource("Period", "9999-01-01,gt2018-10-28");
        assertSearchDoesntReturnSavedResource("Period", "9999-01-01,le2018-10-28");
        assertSearchReturnsSavedResource("Period", "9999-01-01,ge2018-10-28");
        assertSearchReturnsSavedResource("Period", "9999-01-01,sa2018-10-28");
        assertSearchDoesntReturnSavedResource("Period", "9999-01-01,eb2018-10-28");
        assertSearchDoesntReturnSavedResource("Period", "9999-01-01,ap2010-10-28");
    }

    // We decided that Periods with no start or end should not even be indexed 
    //    @Test
    //    public void testSearchDate_Period_NoStartOrEnd() throws Exception {
    //        assertSearchReturnsSavedResource("Period-noStartOrEnd", "2018-10-29T17:12:44-04:00");
    //    }

    // Timing search is not working properly.
    //    @Test
    //    public void testSearchDate_Timing_EventsOnly() throws Exception {
    //        testSearchDateReturnsResourceWithExtension("Timing-eventsOnly", "2018-10-29T17:12:44-04:00", "http://example.org/TimingEventsOnly");
    //    }
    //    @Test
    //    public void testSearchDate_Timing_BoundQuantity() throws Exception {
    //        testSearchDateReturnsResourceWithExtension("Timing-boundQuantity", "2018-10-29T17:12:44-04:00", "http://example.org/TimingBoundsQuantity");
    //    }
    //    @Test
    //    public void testSearchDate_Timing_BoundRange() throws Exception {
    //        testSearchDateReturnsResourceWithExtension("Timing-boundRange", "2018-10-29T17:12:00-04:00", "http://example.org/TimingBoundsRange");
    //    }
    //    @Test
    //    public void testSearchDate_Timing_BoundPeriod() throws Exception {
    //        testSearchDateReturnsResourceWithExtension("Timing-boundPeriod", "2018-10-29T17:18:00-04:00", "http://example.org/TimingBoundsPeriod");
    //    }

    /*
     * Currently, documented in our conformance statement. We do not support
     * modifiers on chained parameters.
     * https://ibm.github.io/FHIR/Conformance#search-modifiers
     * Refer to https://github.com/IBM/FHIR/issues/473 to track the issue.
     */

    //    @Test
    //    public void testSearchDate_instant_chained_missing() throws Exception {
    //        assertSearchReturnsSavedResource("subject:Basic.instant:missing", "false");
    //        assertSearchDoesntReturnSavedResource("subject:Basic.instant:missing", "true");
    //        
    //        assertSearchReturnsSavedResource("subject:Basic.missing-instant:missing", "true");
    //        assertSearchDoesntReturnSavedResource("subject:Basic.missing-instant:missing", "false");
    //    }

    //  @Test
    //  public void testSearchDate_Period_chained_missing() throws Exception {
    //      assertSearchReturnsComposition("subject:Basic.Period:missing", "false");
    //      assertSearchDoesntReturnComposition("subject:Basic.Period:missing", "true");
    //      
    //      assertSearchReturnsComposition("subject:Basic.missing-Period:missing", "true");
    //      assertSearchDoesntReturnComposition("subject:Basic.missing-Period:missing", "false");
    //  }

    /*
     * Currently, documented in our conformance statement. We do not support
     * modifiers on chained parameters.
     * https://ibm.github.io/FHIR/Conformance#search-modifiers
     * Refer to https://github.com/IBM/FHIR/issues/473 to track the issue.
     */

    //    @Test
    //    public void testSearchDate_date_chained_missing() throws Exception {
    //        assertSearchReturnsComposition("subject:Basic.date:missing", "false");
    //        assertSearchDoesntReturnSavedResource("subject:Basic.date:missing", "true");
    //        
    //        assertSearchReturnsComposition("subject:Basic.missing-date:missing", "true");
    //        assertSearchDoesntReturnSavedResource("subject:Basic.missing-date:missing", "false");
    //    }

    //    @Test
    //    public void testSearchDate_dateTime_chained_missing() throws Exception {
    //        assertSearchReturnsComposition("subject:Basic.dateTime:missing", "false");
    //        assertSearchDoesntReturnComposition("subject:Basic.dateTime:missing", "true");
    //        
    //        assertSearchReturnsComposition("subject:Basic.missing-dateTime:missing", "true");
    //        assertSearchDoesntReturnComposition("subject:Basic.missing-dateTime:missing", "false");
    //    }
}
