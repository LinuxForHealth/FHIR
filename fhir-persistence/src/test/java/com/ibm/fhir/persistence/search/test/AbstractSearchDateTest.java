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
 * @author lmsurpre
 * @see https://hl7.org/fhir/r4/search.html#date
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
        
        // lt2018-10-29 is handled like lt[2018-10-29,2018-10-30) and so, confusingly, it overlaps with [2018-10-29,2018-10-30)
        assertSearchReturnsSavedResource("date", "lt2018-10-29");
        // gt2018-10-29 is handled like gt[2018-10-29,2018-10-30) and so, confusingly, it overlaps with [2018-10-29,2018-10-30)
        assertSearchReturnsSavedResource("date", "gt2018-10-29");
        assertSearchReturnsSavedResource("date", "le2018-10-29");
        assertSearchReturnsSavedResource("date", "ge2018-10-29");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-29");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-29");
        assertSearchReturnsSavedResource("date", "ap2018-10-29");
        
        assertSearchDoesntReturnSavedResource("date", "2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource("date", "ne2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource("date", "lt2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource("date", "gt2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource("date", "le2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource("date", "ge2018-10-29T17:12:00-04:00");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-29T17:12:00-04:00");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource("date", "ap2018-10-29T17:12:00-04:00");
        
        assertSearchDoesntReturnSavedResource("date", "2018-10-28");
        assertSearchReturnsSavedResource("date", "ne2018-10-28");
        assertSearchDoesntReturnSavedResource("date", "lt2018-10-28");
        assertSearchReturnsSavedResource("date", "gt2018-10-28");
        // this shouldn't return the resource but does.  le[2018-10-28,2018-10-29) shouldn't overlap with [2018-10-29,2018-10-30)
//        assertSearchDoesntReturnSavedResource("date", "le2018-10-28");
        assertSearchReturnsSavedResource("date", "ge2018-10-28");
        assertSearchReturnsSavedResource("date", "sa2018-10-28");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-28");
        assertSearchDoesntReturnSavedResource("date", "ap2018-10-28");
        
        assertSearchDoesntReturnSavedResource("date", "2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("date", "ne2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("date", "lt2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("date", "gt2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("date", "le2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("date", "ge2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("date", "sa2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("date", "ap2018-10-28T23:59:59.999999Z");
        
        assertSearchDoesntReturnSavedResource("date", "2018-10-30");
        assertSearchReturnsSavedResource("date", "ne2018-10-30");
        assertSearchReturnsSavedResource("date", "lt2018-10-30");
        assertSearchDoesntReturnSavedResource("date", "gt2018-10-30");
        assertSearchReturnsSavedResource("date", "le2018-10-30");
        assertSearchDoesntReturnSavedResource("date", "ge2018-10-30");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-30");
        assertSearchReturnsSavedResource("date", "eb2018-10-30");
        assertSearchDoesntReturnSavedResource("date", "ap2018-10-30");
        
        assertSearchDoesntReturnSavedResource("date", "2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource("date", "ne2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource("date", "lt2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("date", "gt2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource("date", "le2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("date", "ge2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource("date", "eb2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("date", "ap2018-10-30T00:00:00.000001Z");
        
        // This throws an error but shouldn't
        //assertSearchReturnsSavedResource("date", "2018-10-29T17:12:00");
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
        assertSearchReturnsComposition("subject:Basic.date", "2018-10-29");
        // This should return the resource but does not
//        assertSearchReturnsComposition("date", "2018-10-29T17:12:00-04:00");
        // This throws an error
//        assertSearchReturnsComposition("date", "2018-10-29T17:12:00");
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
        assertSearchReturnsSavedResource("date", "2018-10-29,9999-01-01");
        // This returns the target resource multiple times but should not
        //assertSearchReturnsSavedResource("date", "9999-01-01,2018-10-29");
        
        assertSearchDoesntReturnSavedResource("date", "ne2018-10-29,9999-01-01");
        assertSearchReturnsSavedResource("date", "lt2018-10-29,9999-01-01");
        assertSearchReturnsSavedResource("date", "gt2018-10-29,9999-01-01");
        assertSearchReturnsSavedResource("date", "le2018-10-29,9999-01-01");
        assertSearchReturnsSavedResource("date", "ge2018-10-29,9999-01-01");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-29,9999-01-01");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-29,9999-01-01");
        assertSearchReturnsSavedResource("date", "ap2018-10-29,9999-01-01");
        
        //assertSearchDoesntReturnSavedResource("date", "9999-01-01,ne2018-10-29");
        //assertSearchDoesntReturnSavedResource("date", "9999-01-01,lt2018-10-29");
        //assertSearchDoesntReturnSavedResource("date", "9999-01-01,gt2018-10-29");
        //assertSearchReturnsSavedResource("date", "9999-01-01,le2018-10-29");
        //assertSearchReturnsSavedResource("date", "9999-01-01,ge2018-10-29");
        //assertSearchDoesntReturnSavedResource("date", "9999-01-01,sa2018-10-29");
        //assertSearchDoesntReturnSavedResource("date", "9999-01-01,eb2018-10-29");
        //assertSearchReturnsSavedResource("date", "9999-01-01,ap2018-10-29");
    }

//    @Test
//    public void testSearchDate_date_chained_missing() throws Exception {
//        assertSearchReturnsComposition("subject:Basic.date:missing", "false");
//        assertSearchDoesntReturnSavedResource("subject:Basic.date:missing", "true");
//        
//        assertSearchReturnsComposition("subject:Basic.missing-date:missing", "true");
//        assertSearchDoesntReturnSavedResource("subject:Basic.missing-date:missing", "false");
//    }

    @Test
    public void testSearchDate_dateTime() throws Exception {
        // "dateTime" is 2018-10-29T17:12:00-04:00
        
        assertSearchReturnsSavedResource("dateTime", "2018-10-29");
//        assertSearchDoesntReturnSavedResource("dateTime", "ne2018-10-29");
        assertSearchReturnsSavedResource("dateTime", "lt2018-10-29");
//        assertSearchDoesntReturnSavedResource("dateTime", "gt2018-10-29");
//        assertSearchReturnsSavedResource("dateTime", "le2018-10-29");
        assertSearchReturnsSavedResource("dateTime", "ge2018-10-29");
//        assertSearchDoesntReturnSavedResource("dateTime", "sa2018-10-29");
        assertSearchDoesntReturnSavedResource("dateTime", "eb2018-10-29");
        assertSearchReturnsSavedResource("dateTime", "ap2018-10-29");
        
        assertSearchReturnsSavedResource("dateTime", "2018-10-29T17:12:00-04:00");
        assertSearchDoesntReturnSavedResource("dateTime", "ne2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource("dateTime", "lt2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource("dateTime", "gt2018-10-29T17:12:00-04:00");
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
        assertSearchDoesntReturnSavedResource("dateTime", "ap2018-10-28");
        
        assertSearchDoesntReturnSavedResource("dateTime", "2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("dateTime", "ne2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("dateTime", "lt2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("dateTime", "gt2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("dateTime", "le2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("dateTime", "ge2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("dateTime", "sa2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("dateTime", "eb2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("dateTime", "ap2018-10-28T23:59:59.999999Z");
        
        assertSearchDoesntReturnSavedResource("dateTime", "2018-10-30");
        assertSearchReturnsSavedResource("dateTime", "ne2018-10-30");
        assertSearchReturnsSavedResource("dateTime", "lt2018-10-30");
        assertSearchDoesntReturnSavedResource("dateTime", "gt2018-10-30");
        assertSearchReturnsSavedResource("dateTime", "le2018-10-30");
        assertSearchDoesntReturnSavedResource("dateTime", "ge2018-10-30");
        assertSearchDoesntReturnSavedResource("dateTime", "sa2018-10-30");
        assertSearchReturnsSavedResource("dateTime", "eb2018-10-30");
        assertSearchDoesntReturnSavedResource("dateTime", "ap2018-10-30");
        
        assertSearchDoesntReturnSavedResource("dateTime", "2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource("dateTime", "ne2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource("dateTime", "lt2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("dateTime", "gt2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource("dateTime", "le2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("dateTime", "ge2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("dateTime", "sa2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource("dateTime", "eb2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("dateTime", "ap2018-10-30T00:00:00.000001Z");
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
    
//    @Test
//    public void testSearchDate_dateTime_chained_missing() throws Exception {
//        assertSearchReturnsComposition("subject:Basic.dateTime:missing", "false");
//        assertSearchDoesntReturnComposition("subject:Basic.dateTime:missing", "true");
//        
//        assertSearchReturnsComposition("subject:Basic.missing-dateTime:missing", "true");
//        assertSearchDoesntReturnComposition("subject:Basic.missing-dateTime:missing", "false");
//    }
    
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
    
//    @Test
//    public void testSearchDate_instant_chained_missing() throws Exception {
//        assertSearchReturnsSavedResource("subject:Basic.instant:missing", "false");
//        assertSearchDoesntReturnSavedResource("subject:Basic.instant:missing", "true");
//        
//        assertSearchReturnsSavedResource("subject:Basic.missing-instant:missing", "true");
//        assertSearchDoesntReturnSavedResource("subject:Basic.missing-instant:missing", "false");
//    }
    
    ///////////////
    // Period tests
    ///////////////
    @Test
    public void testSearchDate_Period() throws Exception {
        // "Period" is 2018-10-29T17:12:00-04:00 to 2018-10-29T17:18:00-04:00
        assertSearchReturnsSavedResource("Period", "2018-10-29");
        assertSearchDoesntReturnSavedResource("Period", "ne2018-10-29");
        assertSearchReturnsSavedResource("Period", "lt2018-10-29");
        assertSearchReturnsSavedResource("Period", "gt2018-10-29");
        assertSearchReturnsSavedResource("Period", "le2018-10-29");
        assertSearchReturnsSavedResource("Period", "ge2018-10-29");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-29");
        assertSearchDoesntReturnSavedResource("Period", "eb2018-10-29");
        assertSearchReturnsSavedResource("Period", "ap2018-10-29");
        
        // search on the dateTime at the start of the period
        // the range of the search value doesn't fully contain the range of the target value
        assertSearchDoesntReturnSavedResource("Period", "2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource("Period", "ne2018-10-29T17:12:00-04:00");
        // 17:12:00 is interpreted as the range [17:12:00 to 17:12:01) and so this does intersect [17:12:00,17:18:00] 
        //assertSearchDoesntReturnSavedResource("Period", "lt2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource("Period", "gt2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource("Period", "le2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource("Period", "ge2018-10-29T17:12:00-04:00");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-29T17:12:00-04:00");
        assertSearchDoesntReturnSavedResource("Period", "eb2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource("Period", "ap2018-10-29T17:12:00-04:00");
        
        // search on a dateTime within the period
        // the range of the search value doesn't fully contain the range of the target value
        assertSearchDoesntReturnSavedResource("Period", "2018-10-29T17:12:44-04:00");
        assertSearchReturnsSavedResource("Period", "ne2018-10-29T17:12:44-04:00");
        assertSearchReturnsSavedResource("Period", "lt2018-10-29T17:12:44-04:00");
        assertSearchReturnsSavedResource("Period", "gt2018-10-29T17:12:44-04:00");
        assertSearchReturnsSavedResource("Period", "le2018-10-29T17:12:44-04:00");
        assertSearchReturnsSavedResource("Period", "ge2018-10-29T17:12:44-04:00");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-29T17:12:44-04:00");
        assertSearchDoesntReturnSavedResource("Period", "eb2018-10-29T17:12:44-04:00");
        assertSearchReturnsSavedResource("Period", "ap2018-10-29T17:12:44-04:00");
        
        // search on the dateTime at the end of the period
        // the range of the search value doesn't fully contain the range of the target value
        assertSearchDoesntReturnSavedResource("Period", "2018-10-29T17:18:00-04:00");
        assertSearchReturnsSavedResource("Period", "ne2018-10-29T17:18:00-04:00");
        assertSearchReturnsSavedResource("Period", "lt2018-10-29T17:18:00-04:00");
        assertSearchReturnsSavedResource("Period", "gt2018-10-29T17:18:00-04:00");
        assertSearchReturnsSavedResource("Period", "le2018-10-29T17:18:00-04:00");
        assertSearchReturnsSavedResource("Period", "ge2018-10-29T17:18:00-04:00");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-29T17:18:00-04:00");
        assertSearchDoesntReturnSavedResource("Period", "eb2018-10-29T17:18:00-04:00");
        assertSearchReturnsSavedResource("Period", "ap2018-10-29T17:18:00-04:00");
        
        assertSearchDoesntReturnSavedResource("Period", "2018-10-28");
        assertSearchReturnsSavedResource("Period", "ne2018-10-28");
        assertSearchDoesntReturnSavedResource("Period", "lt2018-10-28");
        assertSearchReturnsSavedResource("Period", "gt2018-10-28");
        assertSearchDoesntReturnSavedResource("Period", "le2018-10-28");
        assertSearchReturnsSavedResource("Period", "ge2018-10-28");
        assertSearchReturnsSavedResource("Period", "sa2018-10-28");
        assertSearchDoesntReturnSavedResource("Period", "eb2018-10-28");
        assertSearchDoesntReturnSavedResource("Period", "ap2018-10-28");
        
        assertSearchDoesntReturnSavedResource("Period", "2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("Period", "ne2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("Period", "lt2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("Period", "gt2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("Period", "le2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("Period", "ge2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("Period", "sa2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("Period", "eb2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("Period", "ap2018-10-28T23:59:59.999999Z");
        
        assertSearchDoesntReturnSavedResource("Period", "2018-10-30");
        assertSearchReturnsSavedResource("Period", "ne2018-10-30");
        assertSearchReturnsSavedResource("Period", "lt2018-10-30");
        assertSearchDoesntReturnSavedResource("Period", "gt2018-10-30");
        assertSearchReturnsSavedResource("Period", "le2018-10-30");
        assertSearchDoesntReturnSavedResource("Period", "ge2018-10-30");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-30");
        assertSearchReturnsSavedResource("Period", "eb2018-10-30");
        assertSearchDoesntReturnSavedResource("Period", "ap2018-10-30");
        
        assertSearchDoesntReturnSavedResource("Period", "2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource("Period", "ne2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource("Period", "lt2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("Period", "gt2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource("Period", "le2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("Period", "ge2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource("Period", "eb2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("Period", "ap2018-10-30T00:00:00.000001Z");
    }
    @Test
    public void testSearchDate_Period_NoStart() throws Exception {
        // "Period-noStart" has end=2018-10-29T17:18:00-04:00
        
        // the range of the search value doesn't fully contain the range of the target value
        assertSearchDoesntReturnSavedResource("Period-noStart", "2018-10-29");
        assertSearchReturnsSavedResource("Period-noStart", "ne2018-10-29");
        assertSearchReturnsSavedResource("Period-noStart", "lt2018-10-29");
        assertSearchReturnsSavedResource("Period-noStart", "gt2018-10-29");
        assertSearchReturnsSavedResource("Period-noStart", "le2018-10-29");
        assertSearchReturnsSavedResource("Period-noStart", "ge2018-10-29");
        assertSearchDoesntReturnSavedResource("Period-noStart", "sa2018-10-29");
        assertSearchDoesntReturnSavedResource("Period-noStart", "eb2018-10-29");
        assertSearchReturnsSavedResource("Period-noStart", "ap2018-10-29");
        
        // search on the dateTime at the end of the Period-noStart
        // the range of the search value doesn't fully contain the range of the target value
        assertSearchDoesntReturnSavedResource("Period-noStart", "2018-10-29T17:18:00-04:00");
        assertSearchReturnsSavedResource("Period-noStart", "ne2018-10-29T17:18:00-04:00");
        assertSearchReturnsSavedResource("Period-noStart", "lt2018-10-29T17:18:00-04:00");
        assertSearchReturnsSavedResource("Period-noStart", "gt2018-10-29T17:18:00-04:00");
        assertSearchReturnsSavedResource("Period-noStart", "le2018-10-29T17:18:00-04:00");
        assertSearchReturnsSavedResource("Period-noStart", "ge2018-10-29T17:18:00-04:00");
        assertSearchDoesntReturnSavedResource("Period-noStart", "sa2018-10-29T17:18:00-04:00");
        assertSearchDoesntReturnSavedResource("Period-noStart", "eb2018-10-29T17:18:00-04:00");
        assertSearchReturnsSavedResource("Period-noStart", "ap2018-10-29T17:18:00-04:00");
        
        assertSearchDoesntReturnSavedResource("Period-noStart", "2018-10-28");
        assertSearchReturnsSavedResource("Period-noStart", "ne2018-10-28");
        assertSearchReturnsSavedResource("Period-noStart", "lt2018-10-28");
        assertSearchReturnsSavedResource("Period-noStart", "gt2018-10-28");
        assertSearchReturnsSavedResource("Period-noStart", "le2018-10-28");
        assertSearchReturnsSavedResource("Period-noStart", "ge2018-10-28");
        assertSearchDoesntReturnSavedResource("Period-noStart", "sa2018-10-28");
        assertSearchDoesntReturnSavedResource("Period-noStart", "eb2018-10-28");
        assertSearchReturnsSavedResource("Period-noStart", "ap2018-10-28");
        
        assertSearchDoesntReturnSavedResource("Period-noStart", "2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("Period-noStart", "ne2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("Period-noStart", "lt2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("Period-noStart", "gt2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("Period-noStart", "le2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("Period-noStart", "ge2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("Period-noStart", "sa2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("Period-noStart", "eb2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("Period-noStart", "ap2018-10-28T23:59:59.999999Z");
        
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
        
        // the range of the search value doesn't fully contain the range of the target value
        assertSearchDoesntReturnSavedResource("Period-noEnd", "2018-10-29");
        assertSearchReturnsSavedResource("Period-noEnd", "ne2018-10-29");
        assertSearchReturnsSavedResource("Period-noEnd", "lt2018-10-29");
        assertSearchReturnsSavedResource("Period-noEnd", "gt2018-10-29");
        assertSearchReturnsSavedResource("Period-noEnd", "le2018-10-29");
        assertSearchReturnsSavedResource("Period-noEnd", "ge2018-10-29");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "sa2018-10-29");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "eb2018-10-29");
        assertSearchReturnsSavedResource("Period-noEnd", "ap2018-10-29");
        
        // search on the dateTime at the start of the Period-noEnd
        // the range of the search value doesn't fully contain the range of the target value
        assertSearchDoesntReturnSavedResource("Period-noEnd", "2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource("Period-noEnd", "ne2018-10-29T17:12:00-04:00");
        // 17:12:00 is interpreted as the range [17:12:00 to 17:12:01) and so this does intersect [17:12:00,17:18:00]
        //assertSearchDoesntReturnSavedResource("Period-noEnd", "lt2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource("Period-noEnd", "gt2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource("Period-noEnd", "le2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource("Period-noEnd", "ge2018-10-29T17:12:00-04:00");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "sa2018-10-29T17:12:00-04:00");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "eb2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource("Period-noEnd", "ap2018-10-29T17:12:00-04:00");
        
        assertSearchDoesntReturnSavedResource("Period-noEnd", "2018-10-28");
        assertSearchReturnsSavedResource("Period-noEnd", "ne2018-10-28");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "lt2018-10-28");
        assertSearchReturnsSavedResource("Period-noEnd", "gt2018-10-28");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "le2018-10-28");
        assertSearchReturnsSavedResource("Period-noEnd", "ge2018-10-28");
        assertSearchReturnsSavedResource("Period-noEnd", "sa2018-10-28");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "eb2018-10-28");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "ap2018-10-28");
        
        assertSearchDoesntReturnSavedResource("Period-noEnd", "2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("Period-noEnd", "ne2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "lt2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("Period-noEnd", "gt2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "le2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("Period-noEnd", "ge2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource("Period-noEnd", "sa2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "eb2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "ap2018-10-28T23:59:59.999999Z");
        
        assertSearchDoesntReturnSavedResource("Period-noEnd", "2018-10-30");
        assertSearchReturnsSavedResource("Period-noEnd", "ne2018-10-30");
        assertSearchReturnsSavedResource("Period-noEnd", "lt2018-10-30");
        assertSearchReturnsSavedResource("Period-noEnd", "gt2018-10-30");
        assertSearchReturnsSavedResource("Period-noEnd", "le2018-10-30");
        assertSearchReturnsSavedResource("Period-noEnd", "ge2018-10-30");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "sa2018-10-30");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "eb2018-10-30");
        assertSearchReturnsSavedResource("Period-noEnd", "ap2018-10-30");
        
        assertSearchDoesntReturnSavedResource("Period-noEnd", "2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource("Period-noEnd", "ne2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource("Period-noEnd", "lt2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource("Period-noEnd", "gt2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource("Period-noEnd", "le2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource("Period-noEnd", "ge2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "sa2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "eb2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource("Period-noEnd", "ap2018-10-30T00:00:00.000001Z");
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
        assertSearchDoesntReturnSavedResource("Period", "ap2018-10-28,9999-01-01");
        
        assertSearchDoesntReturnSavedResource("Period", "9999-01-01,2018-10-28");
        //assertSearchReturnsSavedResource("Period", "9999-01-01,ne2018-10-28");
        //assertSearchDoesntReturnSavedResource("Period", "9999-01-01,lt2018-10-28");
        //assertSearchReturnsSavedResource("Period", "9999-01-01,gt2018-10-28");
        //assertSearchDoesntReturnSavedResource("Period", "9999-01-01,le2018-10-28");
        //assertSearchReturnsSavedResource("Period", "9999-01-01,ge2018-10-28");
        //assertSearchReturnsSavedResource("Period", "9999-01-01,sa2018-10-28");
        //assertSearchDoesntReturnSavedResource("Period", "9999-01-01,eb2018-10-28");
        //assertSearchDoesntReturnSavedResource("Period", "9999-01-01,ap2018-10-28");
    }

//    @Test
//    public void testSearchDate_Period_chained_missing() throws Exception {
//        assertSearchReturnsComposition("subject:Basic.Period:missing", "false");
//        assertSearchDoesntReturnComposition("subject:Basic.Period:missing", "true");
//        
//        assertSearchReturnsComposition("subject:Basic.missing-Period:missing", "true");
//        assertSearchDoesntReturnComposition("subject:Basic.missing-Period:missing", "false");
//    }
    
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
}
