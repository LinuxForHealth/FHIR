/*
 * (C) Copyright IBM Corp. 2018, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.search.test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Logger;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.model.resource.Basic;
import org.linuxforhealth.fhir.model.test.TestUtil;

/**
 * <a href="https://hl7.org/fhir/search.html#date">FHIR Specification: Search - Date</a> Tests
 */
public abstract class AbstractSearchDateTest extends AbstractPLSearchTest {
    private static final String CLASSNAME = AbstractSearchDateTest.class.getName();
    private static final Logger logger = Logger.getLogger(CLASSNAME);

    @Override
    protected Basic getBasicResource() throws Exception {
        return TestUtil.readExampleResource("json/ibm/basic/BasicDate.json");
    }
    @Override
    protected void setTenant() throws Exception {
        FHIRRequestContext.get().setTenantId("date");

        // this might deserve its own method, but just use setTenant for now
        // since its called before creating any resources
        TimeZone.setDefault(TimeZone.getTimeZone("GMT-4:00"));
        System.out.println(ZoneId.systemDefault());
    }

    @Test
    public void testSearchDate_date_Year_noTZ() throws Exception {
        // "date" is 2018-10-29

        assertSearchReturnsSavedResource(     "date",   "2018");
        assertSearchDoesntReturnSavedResource("date", "ne2018");
        assertSearchDoesntReturnSavedResource("date", "lt2018");
        assertSearchDoesntReturnSavedResource("date", "gt2018");
        assertSearchReturnsSavedResource(     "date", "le2018");
        assertSearchReturnsSavedResource(     "date", "ge2018");
        assertSearchDoesntReturnSavedResource("date", "sa2018");
        assertSearchDoesntReturnSavedResource("date", "eb2018");
        assertSearchReturnsSavedResource(     "date", "ap2018");
    }
    @Test
    public void testSearchDate_date_Month_noTZ() throws Exception {
        // "date" is 2018-10-29

        assertSearchReturnsSavedResource(     "date",   "2018-10");
        assertSearchDoesntReturnSavedResource("date", "ne2018-10");
        assertSearchDoesntReturnSavedResource("date", "lt2018-10");
        assertSearchDoesntReturnSavedResource("date", "gt2018-10");
        assertSearchReturnsSavedResource(     "date", "le2018-10");
        assertSearchReturnsSavedResource(     "date", "ge2018-10");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10");
        assertSearchReturnsSavedResource(     "date", "ap2018-10");
    }
    @Test
    public void testSearchDate_date_Day_noTZ() throws Exception {
        // "date" is 2018-10-29

        // the day before
        assertSearchDoesntReturnSavedResource("date",   "2018-10-28");
        assertSearchReturnsSavedResource(     "date", "ne2018-10-28");
        assertSearchDoesntReturnSavedResource("date", "lt2018-10-28");
        assertSearchReturnsSavedResource(     "date", "gt2018-10-28");
        assertSearchDoesntReturnSavedResource("date", "le2018-10-28");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-28");
        assertSearchReturnsSavedResource(     "date", "sa2018-10-28");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-28");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-28");

        // the exact day
        assertSearchReturnsSavedResource(     "date",   "2018-10-29");
        assertSearchDoesntReturnSavedResource("date", "lt2018-10-29");
        assertSearchDoesntReturnSavedResource("date", "gt2018-10-29");
        assertSearchDoesntReturnSavedResource("date", "ne2018-10-29");
        assertSearchReturnsSavedResource(     "date", "le2018-10-29");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-29");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-29");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-29");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-29");

        // the day after
        assertSearchDoesntReturnSavedResource("date",   "2018-10-30");
        assertSearchReturnsSavedResource(     "date", "ne2018-10-30");
        assertSearchReturnsSavedResource(     "date", "lt2018-10-30");
        assertSearchDoesntReturnSavedResource("date", "gt2018-10-30");
        assertSearchReturnsSavedResource(     "date", "le2018-10-30");
        assertSearchDoesntReturnSavedResource("date", "ge2018-10-30");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-30");
        assertSearchReturnsSavedResource(     "date", "eb2018-10-30");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-30");
    }
    @Test
    public void testSearchDate_date_Seconds_noTZ() throws Exception {
        // "date" is 2018-10-29

        // the last second before 2018-10-29
        assertSearchDoesntReturnSavedResource("date",   "2018-10-28T23:59:59");
        assertSearchReturnsSavedResource(     "date", "ne2018-10-28T23:59:59");
        assertSearchDoesntReturnSavedResource("date", "lt2018-10-28T23:59:59");
        assertSearchReturnsSavedResource(     "date", "gt2018-10-28T23:59:59");
        assertSearchDoesntReturnSavedResource("date", "le2018-10-28T23:59:59");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-28T23:59:59");
        assertSearchReturnsSavedResource(     "date", "sa2018-10-28T23:59:59");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-28T23:59:59");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-28T23:59:59");

        // the first second of the day
        // This one doesn't return the resource because the search range does not include the entire target range.
        assertSearchDoesntReturnSavedResource("date",   "2018-10-29T00:00:00Z");
        // This one is the exact inverse of the previous.
        assertSearchReturnsSavedResource(     "date", "ne2018-10-29T00:00:00");
        assertSearchDoesntReturnSavedResource("date", "lt2018-10-29T00:00:00");
        assertSearchReturnsSavedResource(     "date", "gt2018-10-29T00:00:00");
        assertSearchReturnsSavedResource(     "date", "le2018-10-29T00:00:00");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-29T00:00:00");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-29T00:00:00");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-29T00:00:00");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-29T00:00:00");

        // a second in the middle of the day
        // This one doesn't return the resource because the search range does not include the entire target range.
        assertSearchDoesntReturnSavedResource("date",   "2018-10-29T17:12:00");
        // This one is the exact inverse of the previous.
        assertSearchReturnsSavedResource(     "date", "ne2018-10-29T17:12:00");
        assertSearchReturnsSavedResource(     "date", "lt2018-10-29T17:12:00");
        assertSearchReturnsSavedResource(     "date", "gt2018-10-29T17:12:00");
        assertSearchReturnsSavedResource(     "date", "le2018-10-29T17:12:00");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-29T17:12:00");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-29T17:12:00");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-29T17:12:00");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-29T17:12:00");

        // the last second of the day
        // This one doesn't return the resource because the search range does not include the entire target range.
        assertSearchDoesntReturnSavedResource("date",   "2018-10-29T23:59:59");
        // This one is the exact inverse of the previous.
        assertSearchReturnsSavedResource(     "date", "ne2018-10-29T23:59:59");
        assertSearchReturnsSavedResource(     "date", "lt2018-10-29T23:59:59");
        assertSearchDoesntReturnSavedResource("date", "gt2018-10-29T23:59:59");
        assertSearchReturnsSavedResource(     "date", "le2018-10-29T23:59:59");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-29T23:59:59");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-29T23:59:59");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-29T23:59:59");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-29T23:59:59");

        // the first second after 2018-10-29
        assertSearchDoesntReturnSavedResource("date",   "2018-10-30T00:00:00");
        assertSearchReturnsSavedResource(     "date", "ne2018-10-30T00:00:00");
        assertSearchReturnsSavedResource(     "date", "lt2018-10-30T00:00:00");
        assertSearchDoesntReturnSavedResource("date", "gt2018-10-30T00:00:00");
        assertSearchReturnsSavedResource(     "date", "le2018-10-30T00:00:00");
        assertSearchDoesntReturnSavedResource("date", "ge2018-10-30T00:00:00");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-30T00:00:00");
        assertSearchReturnsSavedResource(     "date", "eb2018-10-30T00:00:00");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-30T00:00:00");
    }
    @Test
    public void testSearchDate_date_Seconds_EDT() throws Exception {
        // "date" is 2018-10-29

        // the last second before 2018-10-29
        assertSearchDoesntReturnSavedResource("date",   "2018-10-28T23:59:59-04:00");
        assertSearchReturnsSavedResource(     "date", "ne2018-10-28T23:59:59-04:00");
        assertSearchDoesntReturnSavedResource("date", "lt2018-10-28T23:59:59-04:00");
        assertSearchReturnsSavedResource(     "date", "gt2018-10-28T23:59:59-04:00");
        assertSearchDoesntReturnSavedResource("date", "le2018-10-28T23:59:59-04:00");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-28T23:59:59-04:00");
        assertSearchReturnsSavedResource(     "date", "sa2018-10-28T23:59:59-04:00");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-28T23:59:59-04:00");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-28T23:59:59-04:00");

        // the first second of the day
        // This one shouldn't return the resource because the search range does not include the entire target range.
        assertSearchDoesntReturnSavedResource("date",   "2018-10-29T00:00:00-04:00");
        // This one is the exact inverse of the previous.
        assertSearchReturnsSavedResource(     "date", "ne2018-10-29T00:00:00-04:00");
        assertSearchDoesntReturnSavedResource("date", "lt2018-10-29T00:00:00-04:00");
        assertSearchReturnsSavedResource(     "date", "gt2018-10-29T00:00:00-04:00");
        assertSearchReturnsSavedResource(     "date", "le2018-10-29T00:00:00-04:00");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-29T00:00:00-04:00");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-29T00:00:00-04:00");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-29T00:00:00-04:00");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-29T00:00:00-04:00");

        // a second in the middle of the day
        // This one doesn't return the resource because the search range does not include the entire target range.
        assertSearchDoesntReturnSavedResource("date",   "2018-10-29T17:12:00-04:00");
        // This one is the exact inverse of the previous.
        assertSearchReturnsSavedResource(     "date", "ne2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource(     "date", "lt2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource(     "date", "gt2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource(     "date", "le2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-29T17:12:00-04:00");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-29T17:12:00-04:00");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-29T17:12:00-04:00");

        // the last second of the day
        // This one doesn't return the resource because the search range does not include the entire target range.
        assertSearchDoesntReturnSavedResource("date",   "2018-10-29T23:59:59-04:00");
        // This one is the exact inverse of the previous.
        assertSearchReturnsSavedResource(     "date", "ne2018-10-29T23:59:59-04:00");
        assertSearchReturnsSavedResource(     "date", "lt2018-10-29T23:59:59-04:00");
        assertSearchDoesntReturnSavedResource("date", "gt2018-10-29T23:59:59-04:00");
        assertSearchReturnsSavedResource(     "date", "le2018-10-29T23:59:59-04:00");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-29T23:59:59-04:00");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-29T23:59:59-04:00");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-29T23:59:59-04:00");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-29T23:59:59-04:00");

        // the first second after 2018-10-29
        assertSearchDoesntReturnSavedResource("date",   "2018-10-30T00:00:00-04:00");
        assertSearchReturnsSavedResource(     "date", "ne2018-10-30T00:00:00-04:00");
        assertSearchReturnsSavedResource(     "date", "lt2018-10-30T00:00:00-04:00");
        assertSearchDoesntReturnSavedResource("date", "gt2018-10-30T00:00:00-04:00");
        assertSearchReturnsSavedResource(     "date", "le2018-10-30T00:00:00-04:00");
        assertSearchDoesntReturnSavedResource("date", "ge2018-10-30T00:00:00-04:00");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-30T00:00:00-04:00");
        assertSearchReturnsSavedResource(     "date", "eb2018-10-30T00:00:00-04:00");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-30T00:00:00-04:00");
    }
    @Test
    public void testSearchDate_date_Seconds_UTC() throws Exception {
        // "date" is 2018-10-29

        // we set the system time to GMT-4, so this is really [2018-10-29T04:00:00Z, 2018-10-30T03:59:59.999999]

        // the last second before 2018-10-29
        assertSearchDoesntReturnSavedResource("date",   "2018-10-29T03:59:59Z");
        assertSearchReturnsSavedResource(     "date", "ne2018-10-29T03:59:59Z");
        assertSearchDoesntReturnSavedResource("date", "lt2018-10-29T03:59:59Z");
        assertSearchReturnsSavedResource(     "date", "gt2018-10-29T03:59:59Z");
        assertSearchDoesntReturnSavedResource("date", "le2018-10-29T03:59:59Z");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-29T03:59:59Z");
        assertSearchReturnsSavedResource(     "date", "sa2018-10-29T03:59:59Z");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-29T03:59:59Z");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-29T03:59:59Z");

        // the first second of the day
        // This one shouldn't return the resource because the search range does not include the entire target range.
        assertSearchDoesntReturnSavedResource("date",   "2018-10-29T04:00:00Z");
        // This one is the exact inverse of the previous.
        assertSearchReturnsSavedResource(     "date", "ne2018-10-29T04:00:00Z");
        assertSearchDoesntReturnSavedResource("date", "lt2018-10-29T04:00:00Z");
        assertSearchReturnsSavedResource(     "date", "gt2018-10-29T04:00:00Z");
        assertSearchReturnsSavedResource(     "date", "le2018-10-29T04:00:00Z");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-29T04:00:00Z");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-29T04:00:00Z");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-29T04:00:00Z");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-29T04:00:00Z");

        // a second in the middle of the day
        // This one doesn't return the resource because the search range does not include the entire target range.
        assertSearchDoesntReturnSavedResource("date",   "2018-10-29T17:12:00Z");
        // This one is the exact inverse of the previous.
        assertSearchReturnsSavedResource(     "date", "ne2018-10-29T17:12:00Z");
        assertSearchReturnsSavedResource(     "date", "lt2018-10-29T17:12:00Z");
        assertSearchReturnsSavedResource(     "date", "gt2018-10-29T17:12:00Z");
        assertSearchReturnsSavedResource(     "date", "le2018-10-29T17:12:00Z");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-29T17:12:00Z");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-29T17:12:00Z");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-29T17:12:00Z");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-29T17:12:00Z");

        // the last second of the day
        // This one doesn't return the resource because the search range does not include the entire target range.
        assertSearchDoesntReturnSavedResource("date",   "2018-10-30T03:59:59Z");
        // This one is the exact inverse of the previous.
        assertSearchReturnsSavedResource(     "date", "ne2018-10-30T03:59:59Z");
        assertSearchReturnsSavedResource(     "date", "lt2018-10-30T03:59:59Z");
        assertSearchDoesntReturnSavedResource("date", "gt2018-10-30T03:59:59Z");
        assertSearchReturnsSavedResource(     "date", "le2018-10-30T03:59:59Z");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-30T03:59:59Z");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-30T03:59:59Z");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-30T03:59:59Z");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-30T03:59:59Z");

        // the first second after 2018-10-29
        assertSearchDoesntReturnSavedResource("date",   "2018-10-30T04:00:00Z");
        assertSearchReturnsSavedResource(     "date", "ne2018-10-30T04:00:00Z");
        assertSearchReturnsSavedResource(     "date", "lt2018-10-30T04:00:00Z");
        assertSearchDoesntReturnSavedResource("date", "gt2018-10-30T04:00:00Z");
        assertSearchReturnsSavedResource(     "date", "le2018-10-30T04:00:00Z");
        assertSearchDoesntReturnSavedResource("date", "ge2018-10-30T04:00:00Z");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-30T04:00:00Z");
        assertSearchReturnsSavedResource(     "date", "eb2018-10-30T04:00:00Z");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-30T04:00:00Z");
    }
    @Test
    public void testSearchDate_date_Fractions_noTZ() throws Exception {
        // "date" is 2018-10-29

        // the last instant before 2018-10-29
        assertSearchDoesntReturnSavedResource("date",   "2018-10-28T23:59:59.999999");
        assertSearchReturnsSavedResource(     "date", "ne2018-10-28T23:59:59.999999");
        assertSearchDoesntReturnSavedResource("date", "lt2018-10-28T23:59:59.999999");
        assertSearchReturnsSavedResource(     "date", "gt2018-10-28T23:59:59.999999");
        assertSearchDoesntReturnSavedResource("date", "le2018-10-28T23:59:59.999999");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-28T23:59:59.999999");
        assertSearchReturnsSavedResource(     "date", "sa2018-10-28T23:59:59.999999");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-28T23:59:59.999999");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-28T23:59:59.999999");

        assertSearchDoesntReturnSavedResource("date",   "2018-10-28T23:59:59.999999999");
        assertSearchReturnsSavedResource(     "date", "ne2018-10-28T23:59:59.999999999");
        assertSearchDoesntReturnSavedResource("date", "lt2018-10-28T23:59:59.999999999");
        assertSearchReturnsSavedResource(     "date", "gt2018-10-28T23:59:59.999999999");
        assertSearchDoesntReturnSavedResource("date", "le2018-10-28T23:59:59.999999999");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-28T23:59:59.999999999");
        assertSearchReturnsSavedResource(     "date", "sa2018-10-28T23:59:59.999999999");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-28T23:59:59.999999999");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-28T23:59:59.999999999");

        // the first instant of the day
        // This one doesn't return the resource because the search range does not include the entire target range.
        assertSearchDoesntReturnSavedResource("date",   "2018-10-29T00:00:00.0Z");
        // This one is the exact inverse of the previous.
        assertSearchReturnsSavedResource(     "date", "ne2018-10-29T00:00:00.0");
        assertSearchDoesntReturnSavedResource("date", "lt2018-10-29T00:00:00.0");
        assertSearchReturnsSavedResource(     "date", "gt2018-10-29T00:00:00.0");
        assertSearchReturnsSavedResource(     "date", "le2018-10-29T00:00:00.0");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-29T00:00:00.0");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-29T00:00:00.0");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-29T00:00:00.0");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-29T00:00:00.0");

        // an instant in the middle of the day
        // This one doesn't return the resource because the search range does not include the entire target range.
        assertSearchDoesntReturnSavedResource("date",   "2018-10-29T17:12:00.0");
        // This one is the exact inverse of the previous.
        assertSearchReturnsSavedResource(     "date", "ne2018-10-29T17:12:00.0");
        assertSearchReturnsSavedResource(     "date", "lt2018-10-29T17:12:00.0");
        assertSearchReturnsSavedResource(     "date", "gt2018-10-29T17:12:00.0");
        assertSearchReturnsSavedResource(     "date", "le2018-10-29T17:12:00.0");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-29T17:12:00.0");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-29T17:12:00.0");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-29T17:12:00.0");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-29T17:12:00.0");

        // the last instant of the day
        // This one doesn't return the resource because the search range does not include the entire target range.
        assertSearchDoesntReturnSavedResource("date",   "2018-10-29T23:59:59.999999");
        // This one is the exact inverse of the previous.
        assertSearchReturnsSavedResource(     "date", "ne2018-10-29T23:59:59.999999");
        assertSearchReturnsSavedResource(     "date", "lt2018-10-29T23:59:59.999999");
        assertSearchDoesntReturnSavedResource("date", "gt2018-10-29T23:59:59.999999");
        assertSearchReturnsSavedResource(     "date", "le2018-10-29T23:59:59.999999");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-29T23:59:59.999999");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-29T23:59:59.999999");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-29T23:59:59.999999");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-29T23:59:59.999999");

        assertSearchDoesntReturnSavedResource("date",   "2018-10-29T23:59:59.999999999");
        assertSearchReturnsSavedResource(     "date", "ne2018-10-29T23:59:59.999999999");
        assertSearchReturnsSavedResource(     "date", "lt2018-10-29T23:59:59.999999999");
        assertSearchDoesntReturnSavedResource("date", "gt2018-10-29T23:59:59.999999999");
        assertSearchReturnsSavedResource(     "date", "le2018-10-29T23:59:59.999999999");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-29T23:59:59.999999999");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-29T23:59:59.999999999");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-29T23:59:59.999999999");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-29T23:59:59.999999999");

        // the first instant after 2018-10-29
        assertSearchDoesntReturnSavedResource("date",   "2018-10-30T00:00:00.0");
        assertSearchReturnsSavedResource(     "date", "ne2018-10-30T00:00:00.0");
        assertSearchReturnsSavedResource(     "date", "lt2018-10-30T00:00:00.0");
        assertSearchDoesntReturnSavedResource("date", "gt2018-10-30T00:00:00.0");
        assertSearchReturnsSavedResource(     "date", "le2018-10-30T00:00:00.0");
        assertSearchDoesntReturnSavedResource("date", "ge2018-10-30T00:00:00.0");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-30T00:00:00.0");
        assertSearchReturnsSavedResource(     "date", "eb2018-10-30T00:00:00.0");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-30T00:00:00.0");
    }
    @Test
    public void testSearchDate_date_Fractions_EDT() throws Exception {
        // "date" is 2018-10-29

        // the last instant before 2018-10-29
        assertSearchDoesntReturnSavedResource("date",   "2018-10-28T23:59:59.999999-04:00");
        assertSearchReturnsSavedResource(     "date", "ne2018-10-28T23:59:59.999999-04:00");
        assertSearchDoesntReturnSavedResource("date", "lt2018-10-28T23:59:59.999999-04:00");
        assertSearchReturnsSavedResource(     "date", "gt2018-10-28T23:59:59.999999-04:00");
        assertSearchDoesntReturnSavedResource("date", "le2018-10-28T23:59:59.999999-04:00");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-28T23:59:59.999999-04:00");
        assertSearchReturnsSavedResource(     "date", "sa2018-10-28T23:59:59.999999-04:00");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-28T23:59:59.999999-04:00");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-28T23:59:59.999999-04:00");

        assertSearchDoesntReturnSavedResource("date",   "2018-10-28T23:59:59.999999999-04:00");
        assertSearchReturnsSavedResource(     "date", "ne2018-10-28T23:59:59.999999999-04:00");
        assertSearchDoesntReturnSavedResource("date", "lt2018-10-28T23:59:59.999999999-04:00");
        assertSearchReturnsSavedResource(     "date", "gt2018-10-28T23:59:59.999999999-04:00");
        assertSearchDoesntReturnSavedResource("date", "le2018-10-28T23:59:59.999999999-04:00");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-28T23:59:59.999999999-04:00");
        assertSearchReturnsSavedResource(     "date", "sa2018-10-28T23:59:59.999999999-04:00");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-28T23:59:59.999999999-04:00");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-28T23:59:59.999999999-04:00");

        // the first instant of the day
        // This one doesn't return the resource because the search range does not include the entire target range.
        assertSearchDoesntReturnSavedResource("date",   "2018-10-29T00:00:00.0-04:00");
        // This one is the exact inverse of the previous.
        assertSearchReturnsSavedResource(     "date", "ne2018-10-29T00:00:00.0-04:00");
        assertSearchDoesntReturnSavedResource("date", "lt2018-10-29T00:00:00.0-04:00");
        assertSearchReturnsSavedResource(     "date", "gt2018-10-29T00:00:00.0-04:00");
        assertSearchReturnsSavedResource(     "date", "le2018-10-29T00:00:00.0-04:00");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-29T00:00:00.0-04:00");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-29T00:00:00.0-04:00");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-29T00:00:00.0-04:00");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-29T00:00:00.0-04:00");

        // an instant in the middle of the day
        // This one doesn't return the resource because the search range does not include the entire target range.
        assertSearchDoesntReturnSavedResource("date",   "2018-10-29T17:12:00.0-04:00");
        // This one is the exact inverse of the previous.
        assertSearchReturnsSavedResource(     "date", "ne2018-10-29T17:12:00.0-04:00");
        assertSearchReturnsSavedResource(     "date", "lt2018-10-29T17:12:00.0-04:00");
        assertSearchReturnsSavedResource(     "date", "gt2018-10-29T17:12:00.0-04:00");
        assertSearchReturnsSavedResource(     "date", "le2018-10-29T17:12:00.0-04:00");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-29T17:12:00.0-04:00");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-29T17:12:00.0-04:00");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-29T17:12:00.0-04:00");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-29T17:12:00.0-04:00");

        // the last instant of the day
        // This one doesn't return the resource because the search range does not include the entire target range.
        assertSearchDoesntReturnSavedResource("date",   "2018-10-29T23:59:59.999999-04:00");
        // This one is the exact inverse of the previous.
        assertSearchReturnsSavedResource(     "date", "ne2018-10-29T23:59:59.999999-04:00");
        assertSearchReturnsSavedResource(     "date", "lt2018-10-29T23:59:59.999999-04:00");
        assertSearchDoesntReturnSavedResource("date", "gt2018-10-29T23:59:59.999999-04:00");
        assertSearchReturnsSavedResource(     "date", "le2018-10-29T23:59:59.999999-04:00");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-29T23:59:59.999999-04:00");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-29T23:59:59.999999-04:00");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-29T23:59:59.999999-04:00");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-29T23:59:59.999999-04:00");

        assertSearchDoesntReturnSavedResource("date",   "2018-10-29T23:59:59.999999999-04:00");
        assertSearchReturnsSavedResource(     "date", "ne2018-10-29T23:59:59.999999999-04:00");
        assertSearchReturnsSavedResource(     "date", "lt2018-10-29T23:59:59.999999999-04:00");
        assertSearchDoesntReturnSavedResource("date", "gt2018-10-29T23:59:59.999999999-04:00");
        assertSearchReturnsSavedResource(     "date", "le2018-10-29T23:59:59.999999999-04:00");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-29T23:59:59.999999999-04:00");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-29T23:59:59.999999999-04:00");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-29T23:59:59.999999999-04:00");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-29T23:59:59.999999999-04:00");

        // the first instant after 2018-10-29
        assertSearchDoesntReturnSavedResource("date",   "2018-10-30T00:00:00.0-04:00");
        assertSearchReturnsSavedResource(     "date", "ne2018-10-30T00:00:00.0-04:00");
        assertSearchReturnsSavedResource(     "date", "lt2018-10-30T00:00:00.0-04:00");
        assertSearchDoesntReturnSavedResource("date", "gt2018-10-30T00:00:00.0-04:00");
        assertSearchReturnsSavedResource(     "date", "le2018-10-30T00:00:00.0-04:00");
        assertSearchDoesntReturnSavedResource("date", "ge2018-10-30T00:00:00.0-04:00");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-30T00:00:00.0-04:00");
        assertSearchReturnsSavedResource(     "date", "eb2018-10-30T00:00:00.0-04:00");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-30T00:00:00.0-04:00");
    }
    @Test
    public void testSearchDate_date_Fractions_IDT() throws Exception {
        // "date" is 2018-10-29

        // the last instant before 2018-10-29 in this time zone
        assertSearchDoesntReturnSavedResource("date",   "2018-10-28T23:59:59.999999+03:00");
        assertSearchReturnsSavedResource(     "date", "ne2018-10-28T23:59:59.999999+03:00");
        assertSearchDoesntReturnSavedResource("date", "lt2018-10-28T23:59:59.999999+03:00");
        assertSearchReturnsSavedResource(     "date", "gt2018-10-28T23:59:59.999999+03:00");
        assertSearchDoesntReturnSavedResource("date", "le2018-10-28T23:59:59.999999+03:00");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-28T23:59:59.999999+03:00");
        assertSearchReturnsSavedResource(     "date", "sa2018-10-28T23:59:59.999999+03:00");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-28T23:59:59.999999+03:00");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-28T23:59:59.999999+03:00");

        assertSearchDoesntReturnSavedResource("date",   "2018-10-28T23:59:59.999999999+03:00");
        assertSearchReturnsSavedResource(     "date", "ne2018-10-28T23:59:59.999999999+03:00");
        assertSearchDoesntReturnSavedResource("date", "lt2018-10-28T23:59:59.999999999+03:00");
        assertSearchReturnsSavedResource(     "date", "gt2018-10-28T23:59:59.999999999+03:00");
        assertSearchDoesntReturnSavedResource("date", "le2018-10-28T23:59:59.999999999+03:00");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-28T23:59:59.999999999+03:00");
        assertSearchReturnsSavedResource(     "date", "sa2018-10-28T23:59:59.999999999+03:00");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-28T23:59:59.999999999+03:00");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-28T23:59:59.999999999+03:00");

        // the first instant of the day in this time zone
        // This one shouldn't return the resource because the search range does not include the entire target range.
        assertSearchDoesntReturnSavedResource("date",   "2018-10-29T00:00:00.0+03:00");
        // This one is the exact inverse of the previous.
        assertSearchReturnsSavedResource(     "date", "ne2018-10-29T00:00:00.0+03:00");
        assertSearchDoesntReturnSavedResource("date", "lt2018-10-29T00:00:00.0+03:00");
        assertSearchReturnsSavedResource(     "date", "gt2018-10-29T00:00:00.0+03:00");
//        assertSearchReturnsSavedResource(     "date", "le2018-10-29T00:00:00.0+03:00");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-29T00:00:00.0+03:00");
//        assertSearchDoesntReturnSavedResource("date", "sa2018-10-29T00:00:00.0+03:00");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-29T00:00:00.0+03:00");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-29T00:00:00.0+03:00");

        // an instant in the middle of the day
        // This one doesn't return the resource because the search range does not include the entire target range.
        assertSearchDoesntReturnSavedResource("date",   "2018-10-29T17:12:00.0+03:00");
        // This one is the exact inverse of the previous.
        assertSearchReturnsSavedResource(     "date", "ne2018-10-29T17:12:00.0+03:00");
        assertSearchReturnsSavedResource(     "date", "lt2018-10-29T17:12:00.0+03:00");
        assertSearchReturnsSavedResource(     "date", "gt2018-10-29T17:12:00.0+03:00");
        assertSearchReturnsSavedResource(     "date", "le2018-10-29T17:12:00.0+03:00");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-29T17:12:00.0+03:00");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-29T17:12:00.0+03:00");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-29T17:12:00.0+03:00");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-29T17:12:00.0+03:00");

        // the last instant of the day
        // This one doesn't return the resource because the search range does not include the entire target range.
        assertSearchDoesntReturnSavedResource("date",   "2018-10-29T23:59:59.999999+03:00");
        // This one is the exact inverse of the previous.
        assertSearchReturnsSavedResource(     "date", "ne2018-10-29T23:59:59.999999+03:00");
        assertSearchReturnsSavedResource(     "date", "lt2018-10-29T23:59:59.999999+03:00");
//        assertSearchDoesntReturnSavedResource("date", "gt2018-10-29T23:59:59.999999+03:00");
        assertSearchReturnsSavedResource(     "date", "le2018-10-29T23:59:59.999999+03:00");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-29T23:59:59.999999+03:00");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-29T23:59:59.999999+03:00");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-29T23:59:59.999999+03:00");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-29T23:59:59.999999+03:00");

        assertSearchDoesntReturnSavedResource("date",   "2018-10-29T23:59:59.999999999+03:00");
        assertSearchReturnsSavedResource(     "date", "ne2018-10-29T23:59:59.999999999+03:00");
        assertSearchReturnsSavedResource(     "date", "lt2018-10-29T23:59:59.999999999+03:00");
//        assertSearchDoesntReturnSavedResource("date", "gt2018-10-29T23:59:59.999999999+03:00");
        assertSearchReturnsSavedResource(     "date", "le2018-10-29T23:59:59.999999999+03:00");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-29T23:59:59.999999999+03:00");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-29T23:59:59.999999999+03:00");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-29T23:59:59.999999999+03:00");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-29T23:59:59.999999999+03:00");

        // the first instant after 2018-10-29
        assertSearchDoesntReturnSavedResource("date",   "2018-10-30T00:00:00.0+03:00");
        assertSearchReturnsSavedResource(     "date", "ne2018-10-30T00:00:00.0+03:00");
        assertSearchReturnsSavedResource(     "date", "lt2018-10-30T00:00:00.0+03:00");
//        assertSearchDoesntReturnSavedResource("date", "gt2018-10-30T00:00:00.0+03:00");
        assertSearchReturnsSavedResource(     "date", "le2018-10-30T00:00:00.0+03:00");
//        assertSearchDoesntReturnSavedResource("date", "ge2018-10-30T00:00:00.0+03:00");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-30T00:00:00.0+03:00");
//        assertSearchReturnsSavedResource(     "date", "eb2018-10-30T00:00:00.0+03:00");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-30T00:00:00.0+03:00");
    }
    @Test
    public void testSearchDate_date_Fractions_UTC() throws Exception {
        // "date" is 2018-10-29

        // we set the system time to GMT-4, so this is really [2018-10-29T04:00:00Z, 2018-10-30T03:59:59.999999]

        // the last instant before 2018-10-29
        assertSearchDoesntReturnSavedResource("date",   "2018-10-29T03:59:59.999999Z");
        assertSearchReturnsSavedResource(     "date", "ne2018-10-29T03:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("date", "lt2018-10-29T03:59:59.999999Z");
        assertSearchReturnsSavedResource(     "date", "gt2018-10-29T03:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("date", "le2018-10-29T03:59:59.999999Z");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-29T03:59:59.999999Z");
        assertSearchReturnsSavedResource(     "date", "sa2018-10-29T03:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-29T03:59:59.999999Z");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-29T03:59:59.999999Z");

        assertSearchDoesntReturnSavedResource("date",   "2018-10-29T03:59:59.999999999Z");
        assertSearchReturnsSavedResource(     "date", "ne2018-10-29T03:59:59.999999999Z");
        assertSearchDoesntReturnSavedResource("date", "lt2018-10-29T03:59:59.999999999Z");
        assertSearchReturnsSavedResource(     "date", "gt2018-10-29T03:59:59.999999999Z");
        assertSearchDoesntReturnSavedResource("date", "le2018-10-29T03:59:59.999999999Z");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-29T03:59:59.999999999Z");
        assertSearchReturnsSavedResource(     "date", "sa2018-10-29T03:59:59.999999999Z");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-29T03:59:59.999999999Z");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-29T03:59:59.999999999Z");

        // the first instant of the day
        // This one doesn't return the resource because the search range does not include the entire target range.
        assertSearchDoesntReturnSavedResource("date",   "2018-10-29T04:00:00.0Z");
        // This one is the exact inverse of the previous.
        assertSearchReturnsSavedResource(     "date", "ne2018-10-29T04:00:00.0Z");
        assertSearchDoesntReturnSavedResource("date", "lt2018-10-29T04:00:00.0Z");
        assertSearchReturnsSavedResource(     "date", "gt2018-10-29T04:00:00.0Z");
        assertSearchReturnsSavedResource(     "date", "le2018-10-29T04:00:00.0Z");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-29T04:00:00.0Z");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-29T04:00:00.0Z");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-29T04:00:00.0Z");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-29T04:00:00.0Z");

        // an instant in the middle of the day
        // This one doesn't return the resource because the search range does not include the entire target range.
        assertSearchDoesntReturnSavedResource("date",   "2018-10-29T17:12:00.0Z");
        // This one is the exact inverse of the previous.
        assertSearchReturnsSavedResource(     "date", "ne2018-10-29T17:12:00.0Z");
        assertSearchReturnsSavedResource(     "date", "lt2018-10-29T17:12:00.0Z");
        assertSearchReturnsSavedResource(     "date", "gt2018-10-29T17:12:00.0Z");
        assertSearchReturnsSavedResource(     "date", "le2018-10-29T17:12:00.0Z");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-29T17:12:00.0Z");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-29T17:12:00.0Z");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-29T17:12:00.0Z");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-29T17:12:00.0Z");

        // the last instant of the day
        // This one doesn't return the resource because the search range does not include the entire target range.
        assertSearchDoesntReturnSavedResource("date",   "2018-10-30T03:59:59.999999Z");
        // This one is the exact inverse of the previous.
        assertSearchReturnsSavedResource(     "date", "ne2018-10-30T03:59:59.999999Z");
        assertSearchReturnsSavedResource(     "date", "lt2018-10-30T03:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("date", "gt2018-10-30T03:59:59.999999Z");
        assertSearchReturnsSavedResource(     "date", "le2018-10-30T03:59:59.999999Z");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-30T03:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-30T03:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-30T03:59:59.999999Z");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-30T03:59:59.999999Z");

        assertSearchDoesntReturnSavedResource("date",   "2018-10-30T03:59:59.999999999Z");
        assertSearchReturnsSavedResource(     "date", "ne2018-10-30T03:59:59.999999999Z");
        assertSearchReturnsSavedResource(     "date", "lt2018-10-30T03:59:59.999999999Z");
        assertSearchDoesntReturnSavedResource("date", "gt2018-10-30T03:59:59.999999999Z");
        assertSearchReturnsSavedResource(     "date", "le2018-10-30T03:59:59.999999999Z");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-30T03:59:59.999999999Z");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-30T03:59:59.999999999Z");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-30T03:59:59.999999999Z");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-30T03:59:59.999999999Z");

        // the first instant after 2018-10-29
        assertSearchDoesntReturnSavedResource("date",   "2018-10-30T04:00:00.0Z");
        assertSearchReturnsSavedResource(     "date", "ne2018-10-30T04:00:00.0Z");
        assertSearchReturnsSavedResource(     "date", "lt2018-10-30T04:00:00.0Z");
        assertSearchDoesntReturnSavedResource("date", "gt2018-10-30T04:00:00.0Z");
        assertSearchReturnsSavedResource(     "date", "le2018-10-30T04:00:00.0Z");
        assertSearchDoesntReturnSavedResource("date", "ge2018-10-30T04:00:00.0Z");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-30T04:00:00.0Z");
        assertSearchReturnsSavedResource(     "date", "eb2018-10-30T04:00:00.0Z");
        assertSearchReturnsSavedResource(     "date", "ap2018-10-30T04:00:00.0Z");
    }
    @Test
    public void testSearchDate_date_missing() throws Exception {
        assertSearchReturnsSavedResource(     "date:missing", "false");
        assertSearchDoesntReturnSavedResource("date:missing", "true");
        assertSearchReturnsSavedResource(     "missing-date:missing", "true");
        assertSearchDoesntReturnSavedResource("missing-date:missing", "false");
    }
    @Test
    public void testSearchDate_date_missing_dateTime() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>(1);
        queryParms.put("date:missing", Collections.singletonList("false"));
        queryParms.put("dateTime", Collections.singletonList("ne2019-12-30"));
        assertTrue(searchReturnsResource(Basic.class, queryParms, savedResource));
        queryParms.clear();
        queryParms.put("date:missing", Collections.singletonList("true"));
        queryParms.put("dateTime", Collections.singletonList("ne2019-12-30"));
        assertFalse(searchReturnsResource(Basic.class, queryParms, savedResource));
    }
    @Test
    public void testSearchDate_date_missing_dateTime_missing() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>(1);
        queryParms.put("date:missing", Collections.singletonList("false"));
        queryParms.put("dateTime:missing", Collections.singletonList("false"));
        assertTrue(searchReturnsResource(Basic.class, queryParms, savedResource));
        queryParms.clear();
        queryParms.put("date:missing", Collections.singletonList("false"));
        queryParms.put("dateTime:missing", Collections.singletonList("true"));
        assertFalse(searchReturnsResource(Basic.class, queryParms, savedResource));
    }
    @Test
    public void testSearchDate_date_chained() throws Exception {
        final String METHOD = "testSearchDate_date_chained";
        logger.entering(CLASSNAME, METHOD);
        try {
            // Date is specific - 2018-10-29
            assertSearchReturnsComposition("subject:Basic.date", "2018-10-29");
            assertSearchDoesntReturnComposition("subject:Basic.date", "2018-10-29T17:12:00-04:00");
            assertSearchDoesntReturnComposition("subject:Basic.date", "2018-10-29T17:12:00");
            assertSearchDoesntReturnComposition("subject:Basic.date", "2025-10-29");
        } finally {
            logger.exiting(CLASSNAME, METHOD);
        }
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
        assertSearchReturnsSavedResource(     "date", "2018-10-29,9999-01-01");
        assertSearchReturnsSavedResource(     "date", "9999-01-01,2018-10-29");
        assertSearchDoesntReturnSavedResource("date", "ne2018-10-29,9999-01-01");
        assertSearchDoesntReturnSavedResource("date", "lt2018-10-28,9999-01-01");

        assertSearchDoesntReturnSavedResource("date", "gt2018-10-30,9999-01-01");
        assertSearchReturnsSavedResource(     "date", "le2018-10-29,9999-01-01");
        assertSearchReturnsSavedResource(     "date", "ge2018-10-29,9999-01-01");
        assertSearchDoesntReturnSavedResource("date", "sa2018-10-29,9999-01-01");
        assertSearchDoesntReturnSavedResource("date", "eb2018-10-29,9999-01-01");
        assertSearchDoesntReturnSavedResource("date", "9999-01-01,lt2018-10-28");
        assertSearchReturnsSavedResource(     "date", "9999-01-01,lt2018-10-30");
        assertSearchDoesntReturnSavedResource("date", "9999-01-01,lt2018-10-29");
        assertSearchDoesntReturnSavedResource("date", "9999-01-01,lt2018-10-28");
        assertSearchDoesntReturnSavedResource("date", "9999-01-01,gt2018-10-29");
        assertSearchDoesntReturnSavedResource("date", "9999-01-01,gt2018-10-30");

        assertSearchReturnsSavedResource(     "date", "9999-01-01,le2018-10-29");
        assertSearchReturnsSavedResource(     "date", "9999-01-01,ge2018-10-29");
        assertSearchReturnsSavedResource(     "date", "9999-01-01,sa2018-10-28");
        assertSearchDoesntReturnSavedResource("date", "9999-01-01,sa2018-10-29");
        assertSearchDoesntReturnSavedResource("date", "9999-01-01,eb2018-10-29");
    }
    @Test
    public void testSearchDate_date_or_NE() throws Exception {
        // "date" is 2018-10-29
        assertSearchReturnsSavedResource(     "date", "9999-10-29,ne2018-10-28");
        assertSearchDoesntReturnSavedResource("date", "9999-01-01,ne2018-10-29");
        assertSearchDoesntReturnSavedResource("date", "ne2018-10-29,ne2018-10-29");
    }
    @Test
    public void testSearchDate_date_or_AP() throws Exception {
        assertSearchReturnsSavedResource(     "date", "ap2018-10-29,9999-01-01");
        assertSearchReturnsSavedResource(     "date", "9999-01-01,ap2018-10-29");
    }
    @Test
    public void testSearchDate_dateTime_Day_noTZ() throws Exception {
        // "dateTime" is 2019-12-31T20:00:00-04:00 (2020-01-01T00:00:00Z)

        // the day before
        assertSearchDoesntReturnSavedResource("dateTime",   "2019-12-30");
        assertSearchReturnsSavedResource(     "dateTime", "ne2019-12-30");
        assertSearchDoesntReturnSavedResource("dateTime", "lt2019-12-30");
        assertSearchReturnsSavedResource(     "dateTime", "gt2019-12-30");
        assertSearchDoesntReturnSavedResource("dateTime", "le2019-12-30");
        assertSearchReturnsSavedResource(     "dateTime", "ge2019-12-30");
        assertSearchReturnsSavedResource(     "dateTime", "sa2019-12-30");
        assertSearchDoesntReturnSavedResource("dateTime", "eb2019-12-30");
        // assertSearchReturnsSavedResource(     "dateTime", "ap2019-12-30");

        // the day of
        assertSearchReturnsSavedResource(     "dateTime",   "2019-12-31");
        assertSearchDoesntReturnSavedResource("dateTime", "ne2019-12-31");
        assertSearchDoesntReturnSavedResource("dateTime", "lt2019-12-31");
        assertSearchDoesntReturnSavedResource("dateTime", "gt2019-12-31");
        assertSearchReturnsSavedResource(     "dateTime", "le2019-12-31");
        assertSearchReturnsSavedResource(     "dateTime", "ge2019-12-31");
        assertSearchDoesntReturnSavedResource("dateTime", "sa2019-12-31");
        assertSearchDoesntReturnSavedResource("dateTime", "eb2019-12-31");
        assertSearchReturnsSavedResource(     "dateTime", "ap2019-12-31");

        // the day after
        assertSearchDoesntReturnSavedResource("dateTime",   "2020-01-01");
        assertSearchReturnsSavedResource(     "dateTime", "ne2020-01-01");
        assertSearchReturnsSavedResource(     "dateTime", "lt2020-01-01");
        assertSearchDoesntReturnSavedResource("dateTime", "gt2020-01-01");
        assertSearchReturnsSavedResource(     "dateTime", "le2020-01-01");
        assertSearchDoesntReturnSavedResource("dateTime", "ge2020-01-01");
        assertSearchDoesntReturnSavedResource("dateTime", "sa2020-01-01");
        assertSearchReturnsSavedResource(     "dateTime", "eb2020-01-01");
        // This test is very dynamic and is not reliable.
        assertSearchReturnsSavedResource(     "dateTime", "ap2020-01-01");
    }
    @Test
    public void testSearchDate_dateTime_Seconds_noTZ() throws Exception {
        // "dateTime" is 2019-12-31T20:00:00-04:00

        // we set the system to GMT-4, so it should match the timezone of the target dateTime

        // the second before
        assertSearchDoesntReturnSavedResource("dateTime",   "2019-12-31T19:59:59");
        assertSearchReturnsSavedResource(     "dateTime", "ne2019-12-31T19:59:59");
        assertSearchDoesntReturnSavedResource("dateTime", "lt2019-12-31T19:59:59");
        assertSearchReturnsSavedResource(     "dateTime", "gt2019-12-31T19:59:59");
        assertSearchDoesntReturnSavedResource("dateTime", "le2019-12-31T19:59:59");
        assertSearchReturnsSavedResource(     "dateTime", "ge2019-12-31T19:59:59");
        assertSearchReturnsSavedResource(     "dateTime", "sa2019-12-31T19:59:59");
        assertSearchDoesntReturnSavedResource("dateTime", "eb2019-12-31T19:59:59");
        assertSearchReturnsSavedResource(     "dateTime", "ap2019-12-31T19:59:59");

        // the exact second
        assertSearchReturnsSavedResource(     "dateTime",   "2019-12-31T20:00:00");
        assertSearchDoesntReturnSavedResource("dateTime", "ne2019-12-31T20:00:00");
        assertSearchDoesntReturnSavedResource("dateTime", "lt2019-12-31T20:00:00");
        assertSearchDoesntReturnSavedResource("dateTime", "gt2019-12-31T20:00:00");
        assertSearchReturnsSavedResource(     "dateTime", "le2019-12-31T20:00:00");
        assertSearchReturnsSavedResource(     "dateTime", "ge2019-12-31T20:00:00");
        assertSearchDoesntReturnSavedResource("dateTime", "sa2019-12-31T20:00:00");
        assertSearchDoesntReturnSavedResource("dateTime", "eb2019-12-31T20:00:00");
        assertSearchReturnsSavedResource(     "dateTime", "ap2019-12-31T20:00:00");

        // the next second
        assertSearchDoesntReturnSavedResource("dateTime",   "2019-12-31T20:00:01");
        assertSearchReturnsSavedResource(     "dateTime", "ne2019-12-31T20:00:01");
        assertSearchReturnsSavedResource(     "dateTime", "lt2019-12-31T20:00:01");
        assertSearchDoesntReturnSavedResource("dateTime", "gt2019-12-31T20:00:01");
        assertSearchReturnsSavedResource(     "dateTime", "le2019-12-31T20:00:01");
        assertSearchDoesntReturnSavedResource("dateTime", "ge2019-12-31T20:00:01");
        assertSearchDoesntReturnSavedResource("dateTime", "sa2019-12-31T20:00:01");
        assertSearchReturnsSavedResource(     "dateTime", "eb2019-12-31T20:00:01");
        assertSearchReturnsSavedResource(     "dateTime", "ap2019-12-31T20:00:01");
    }
    @Test
    public void testSearchDate_dateTime_Seconds_UTC() throws Exception {
        // "dateTime" is 2019-12-31T20:00:00-04:00
        assertSearchReturnsSavedResource(     "dateTime",   "2020-01-01T00:00:00Z");

        // the second before
        assertSearchDoesntReturnSavedResource("dateTime",   "2019-12-31T23:59:59Z");
        assertSearchReturnsSavedResource(     "dateTime", "ne2019-12-31T23:59:59Z");
        assertSearchDoesntReturnSavedResource("dateTime", "lt2019-12-31T23:59:59Z");
        assertSearchReturnsSavedResource(     "dateTime", "gt2019-12-31T23:59:59Z");
        assertSearchDoesntReturnSavedResource("dateTime", "le2019-12-31T23:59:59Z");
        assertSearchReturnsSavedResource(     "dateTime", "ge2019-12-31T23:59:59Z");
        assertSearchReturnsSavedResource(     "dateTime", "sa2019-12-31T23:59:59Z");
        assertSearchDoesntReturnSavedResource("dateTime", "eb2019-12-31T23:59:59Z");
        assertSearchReturnsSavedResource(     "dateTime", "ap2019-12-31T23:59:59Z");

        // the exact second
        assertSearchReturnsSavedResource(     "dateTime",   "2020-01-01T00:00:00Z");
        assertSearchDoesntReturnSavedResource("dateTime", "ne2020-01-01T00:00:00Z");
        assertSearchDoesntReturnSavedResource("dateTime", "lt2020-01-01T00:00:00Z");
        assertSearchDoesntReturnSavedResource("dateTime", "gt2020-01-01T00:00:00Z");
        assertSearchReturnsSavedResource(     "dateTime", "le2020-01-01T00:00:00Z");
        assertSearchReturnsSavedResource(     "dateTime", "ge2020-01-01T00:00:00Z");
        assertSearchDoesntReturnSavedResource("dateTime", "sa2020-01-01T00:00:00Z");
        assertSearchDoesntReturnSavedResource("dateTime", "eb2020-01-01T00:00:00Z");
        assertSearchReturnsSavedResource(     "dateTime", "ap2020-01-01T00:00:00Z");

        // the next second
        assertSearchDoesntReturnSavedResource("dateTime",   "2020-01-01T00:00:01Z");
        assertSearchReturnsSavedResource(     "dateTime", "ne2020-01-01T00:00:01Z");
        assertSearchReturnsSavedResource(     "dateTime", "lt2020-01-01T00:00:01Z");
        assertSearchDoesntReturnSavedResource("dateTime", "gt2020-01-01T00:00:01Z");
        assertSearchReturnsSavedResource(     "dateTime", "le2020-01-01T00:00:01Z");
        assertSearchDoesntReturnSavedResource("dateTime", "ge2020-01-01T00:00:01Z");
        assertSearchDoesntReturnSavedResource("dateTime", "sa2020-01-01T00:00:01Z");
        assertSearchReturnsSavedResource(     "dateTime", "eb2020-01-01T00:00:01Z");
        assertSearchReturnsSavedResource(     "dateTime", "ap2020-01-01T00:00:01Z");
    }
    @Test
    public void testSearchDate_dateTime_Fractions_noTZ() throws Exception {
        // "dateTime" is 2019-12-31T20:00:00-04:00

        // we set the system to GMT-4, so it should match the timezone of the target dateTime

        // the instant before
        assertSearchDoesntReturnSavedResource("dateTime",   "2019-12-31T19:59:59.999999");
        assertSearchReturnsSavedResource(     "dateTime", "ne2019-12-31T19:59:59.999999");
        assertSearchDoesntReturnSavedResource("dateTime", "lt2019-12-31T19:59:59.999999");
        assertSearchReturnsSavedResource(     "dateTime", "gt2019-12-31T19:59:59.999999");
        assertSearchDoesntReturnSavedResource("dateTime", "le2019-12-31T19:59:59.999999");
        assertSearchReturnsSavedResource(     "dateTime", "ge2019-12-31T19:59:59.999999");
        assertSearchReturnsSavedResource(     "dateTime", "sa2019-12-31T19:59:59.999999");
        assertSearchDoesntReturnSavedResource("dateTime", "eb2019-12-31T19:59:59.999999");
        assertSearchReturnsSavedResource(     "dateTime", "ap2019-12-31T19:59:59.999999");

        assertSearchDoesntReturnSavedResource("dateTime",   "2019-12-31T19:59:59.999999999");
        assertSearchReturnsSavedResource(     "dateTime", "ne2019-12-31T19:59:59.999999999");
        assertSearchDoesntReturnSavedResource("dateTime", "lt2019-12-31T19:59:59.999999999");
        assertSearchReturnsSavedResource(     "dateTime", "gt2019-12-31T19:59:59.999999999");
        assertSearchDoesntReturnSavedResource("dateTime", "le2019-12-31T19:59:59.999999999");
        assertSearchReturnsSavedResource(     "dateTime", "ge2019-12-31T19:59:59.999999999");
        assertSearchReturnsSavedResource(     "dateTime", "sa2019-12-31T19:59:59.999999999");
        assertSearchDoesntReturnSavedResource("dateTime", "eb2019-12-31T19:59:59.999999999");
        assertSearchReturnsSavedResource(     "dateTime", "ap2019-12-31T19:59:59.999999999");

        // the exact instant
        // This one does not return the resource because the search value is a single point
        // and the target value is a range from 2019-12-31T20:00:00-04:00 to 2019-12-31T20:00:01-04:00
        assertSearchDoesntReturnSavedResource("dateTime",   "2019-12-31T20:00:00.0");
        assertSearchReturnsSavedResource(     "dateTime", "ne2019-12-31T20:00:00.0");
        assertSearchDoesntReturnSavedResource("dateTime", "lt2019-12-31T20:00:00.0");
        // This one returns the resource because 20:00:00-04:00 is saved as a range [00:00:00,00:00:01)
        assertSearchReturnsSavedResource(     "dateTime", "gt2019-12-31T20:00:00.0");
        assertSearchReturnsSavedResource(     "dateTime", "le2019-12-31T20:00:00.0");
        assertSearchReturnsSavedResource(     "dateTime", "ge2019-12-31T20:00:00.0");
        assertSearchDoesntReturnSavedResource("dateTime", "sa2019-12-31T20:00:00.0");
        assertSearchDoesntReturnSavedResource("dateTime", "eb2019-12-31T20:00:00.0");
        assertSearchReturnsSavedResource(     "dateTime", "ap2019-12-31T20:00:00.0");

        // the next instant
        assertSearchDoesntReturnSavedResource("dateTime",   "2019-12-31T20:00:00.000001");
        assertSearchReturnsSavedResource(     "dateTime", "ne2019-12-31T20:00:00.000001");
        assertSearchReturnsSavedResource(     "dateTime", "lt2019-12-31T20:00:00.000001");
        assertSearchReturnsSavedResource(     "dateTime", "gt2019-12-31T20:00:00.000001");
        assertSearchReturnsSavedResource(     "dateTime", "le2019-12-31T20:00:00.000001");
        assertSearchReturnsSavedResource(     "dateTime", "ge2019-12-31T20:00:00.000001");
        assertSearchDoesntReturnSavedResource("dateTime", "sa2019-12-31T20:00:00.000001");
        assertSearchDoesntReturnSavedResource("dateTime", "eb2019-12-31T20:00:00.000001");
        assertSearchReturnsSavedResource(     "dateTime", "ap2019-12-31T20:00:00.000001");

        assertSearchDoesntReturnSavedResource("dateTime",   "2019-12-31T20:00:00.000001999");
        assertSearchReturnsSavedResource(     "dateTime", "ne2019-12-31T20:00:00.000001999");
        assertSearchReturnsSavedResource(     "dateTime", "lt2019-12-31T20:00:00.000001999");
        assertSearchReturnsSavedResource(     "dateTime", "gt2019-12-31T20:00:00.000001999");
        assertSearchReturnsSavedResource(     "dateTime", "le2019-12-31T20:00:00.000001999");
        assertSearchReturnsSavedResource(     "dateTime", "ge2019-12-31T20:00:00.000001999");
        assertSearchDoesntReturnSavedResource("dateTime", "sa2019-12-31T20:00:00.000001999");
        assertSearchDoesntReturnSavedResource("dateTime", "eb2019-12-31T20:00:00.000001999");
        assertSearchReturnsSavedResource(     "dateTime", "ap2019-12-31T20:00:00.000001999");
    }
    @Test
    public void testSearchDate_dateTime_Fractions_UTC() throws Exception {
        // "dateTime" is 2019-12-31T20:00:00-04:00

        // the instant before
        assertSearchDoesntReturnSavedResource("dateTime",   "2019-12-31T23:59:59.999999Z");
        assertSearchReturnsSavedResource(     "dateTime", "ne2019-12-31T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("dateTime", "lt2019-12-31T23:59:59.999999Z");
        assertSearchReturnsSavedResource(     "dateTime", "gt2019-12-31T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("dateTime", "le2019-12-31T23:59:59.999999Z");
        assertSearchReturnsSavedResource(     "dateTime", "ge2019-12-31T23:59:59.999999Z");
        assertSearchReturnsSavedResource(     "dateTime", "sa2019-12-31T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("dateTime", "eb2019-12-31T23:59:59.999999Z");
        assertSearchReturnsSavedResource(     "dateTime", "ap2019-12-31T23:59:59.999999Z");

        assertSearchDoesntReturnSavedResource("dateTime",   "2019-12-31T23:59:59.999999999Z");
        assertSearchReturnsSavedResource(     "dateTime", "ne2019-12-31T23:59:59.999999999Z");
        assertSearchDoesntReturnSavedResource("dateTime", "lt2019-12-31T23:59:59.999999999Z");
        assertSearchReturnsSavedResource(     "dateTime", "gt2019-12-31T23:59:59.999999999Z");
        assertSearchDoesntReturnSavedResource("dateTime", "le2019-12-31T23:59:59.999999999Z");
        assertSearchReturnsSavedResource(     "dateTime", "ge2019-12-31T23:59:59.999999999Z");
        assertSearchReturnsSavedResource(     "dateTime", "sa2019-12-31T23:59:59.999999999Z");
        assertSearchDoesntReturnSavedResource("dateTime", "eb2019-12-31T23:59:59.999999999Z");
        assertSearchReturnsSavedResource(     "dateTime", "ap2019-12-31T23:59:59.999999999Z");

        // the exact instant
        // This one does not return the resource because the search value is a single point
        // and the target value is a range from 2019-12-31T20:00:00-04:00 to 2019-12-31T20:00:01-04:00
        assertSearchDoesntReturnSavedResource("dateTime",   "2020-01-01T00:00:00.0Z");
        assertSearchReturnsSavedResource(     "dateTime", "ne2020-01-01T00:00:00.0Z");
        assertSearchDoesntReturnSavedResource("dateTime", "lt2020-01-01T00:00:00.0Z");
        // This one returns the resource because 20:00:00-04:00 is saved as a range [00:00:00,00:00:01)
        assertSearchReturnsSavedResource(     "dateTime", "gt2020-01-01T00:00:00.0Z");
        assertSearchReturnsSavedResource(     "dateTime", "le2020-01-01T00:00:00.0Z");
        assertSearchReturnsSavedResource(     "dateTime", "ge2020-01-01T00:00:00.0Z");
        assertSearchDoesntReturnSavedResource("dateTime", "sa2020-01-01T00:00:00.0Z");
        assertSearchDoesntReturnSavedResource("dateTime", "eb2020-01-01T00:00:00.0Z");
        assertSearchReturnsSavedResource(     "dateTime", "ap2020-01-01T00:00:00.0Z");

        // the next instant
        assertSearchDoesntReturnSavedResource("dateTime",   "2020-01-01T00:00:00.000001Z");
        assertSearchReturnsSavedResource(     "dateTime", "ne2020-01-01T00:00:00.000001Z");
        assertSearchReturnsSavedResource(     "dateTime", "lt2020-01-01T00:00:00.000001Z");
        assertSearchReturnsSavedResource(     "dateTime", "gt2020-01-01T00:00:00.000001Z");
        assertSearchReturnsSavedResource(     "dateTime", "le2020-01-01T00:00:00.000001Z");
        assertSearchReturnsSavedResource(     "dateTime", "ge2020-01-01T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("dateTime", "sa2020-01-01T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("dateTime", "eb2020-01-01T00:00:00.000001Z");
        assertSearchReturnsSavedResource(     "dateTime", "ap2020-01-01T00:00:00.000001Z");

        assertSearchDoesntReturnSavedResource("dateTime",   "2020-01-01T00:00:00.000001999Z");
        assertSearchReturnsSavedResource(     "dateTime", "ne2020-01-01T00:00:00.000001999Z");
        assertSearchReturnsSavedResource(     "dateTime", "lt2020-01-01T00:00:00.000001999Z");
        assertSearchReturnsSavedResource(     "dateTime", "gt2020-01-01T00:00:00.000001999Z");
        assertSearchReturnsSavedResource(     "dateTime", "le2020-01-01T00:00:00.000001999Z");
        assertSearchReturnsSavedResource(     "dateTime", "ge2020-01-01T00:00:00.000001999Z");
        assertSearchDoesntReturnSavedResource("dateTime", "sa2020-01-01T00:00:00.000001999Z");
        assertSearchDoesntReturnSavedResource("dateTime", "eb2020-01-01T00:00:00.000001999Z");
        assertSearchReturnsSavedResource(     "dateTime", "ap2020-01-01T00:00:00.000001999Z");
    }
    @Test
    public void testSearchDate_dateTime_chained() throws Exception {
        // "dateTime" is 2019-12-31T20:00:00-04:00

        assertSearchReturnsComposition("subject:Basic.dateTime", "2019-12-31T20:00:00-04:00");
        assertSearchDoesntReturnComposition("subject:Basic.dateTime", "2025-10-29");
    }
    @Test
    public void testSearchDate_dateTime_missing() throws Exception {
        assertSearchReturnsSavedResource(     "dateTime:missing", "false");
        assertSearchDoesntReturnSavedResource("dateTime:missing", "true");
        assertSearchReturnsSavedResource(     "missing-dateTime:missing", "true");
        assertSearchDoesntReturnSavedResource("missing-dateTime:missing", "false");
    }
    @Test
    public void testSearchDate_instant() throws Exception {
        // instant is 2018-10-29T17:12:44-04:00
        assertSearchReturnsSavedResource(     "instant", "2018-10-29T17:12:44-04:00");
        assertSearchReturnsSavedResource(     "instant", "2018-10-30T01:12:44+04:00");
        assertSearchReturnsSavedResource(     "instant", "2018-10-29T21:12:44+00:00");
        assertSearchReturnsSavedResource(     "instant", "2018-10-29T21:12:44-00:00");
        assertSearchReturnsSavedResource(     "instant", "2018-10-29T21:12:44Z");
    }
    @Test
    public void testSearchDate_instant_precise() throws Exception {
        // Searching by second should include all instants within that second (regardless of sub-seconds)
        assertSearchReturnsSavedResource(     "instant-precise", "0001-01-01T01:01:01Z");
        assertSearchDoesntReturnSavedResource("instant-precise", "0001-01-01T01:01:02Z");
        assertSearchReturnsSavedResource(     "instant-precise", "0001-01-01T01:01:01.1Z");
        assertSearchDoesntReturnSavedResource("instant-precise", "0001-01-01T01:01:01.12Z");
        assertSearchReturnsSavedResource(     "instant-precise", "0002-02-02T02:02:02.12Z");
        assertSearchDoesntReturnSavedResource("instant-precise", "0002-02-02T02:02:02.123Z");
        assertSearchReturnsSavedResource(     "instant-precise", "0003-03-03T03:03:03.123Z");
        assertSearchDoesntReturnSavedResource("instant-precise", "0003-03-03T03:03:03.1234Z");
        assertSearchReturnsSavedResource(     "instant-precise", "0004-04-04T04:04:04.1234Z");
        assertSearchDoesntReturnSavedResource("instant-precise", "0004-04-04T04:04:04.12345Z");
        assertSearchReturnsSavedResource(     "instant-precise", "0005-05-05T05:05:05.12345Z");
        assertSearchDoesntReturnSavedResource("instant-precise", "0005-05-05T05:05:05.123456Z");
        assertSearchDoesntReturnSavedResource("instant-precise", "0005-05-05T05:05:05.123456789Z");
        assertSearchReturnsSavedResource(     "instant-precise", "0006-06-06T06:06:06Z");
        assertSearchReturnsSavedResource(     "instant-precise", "0006-06-06T06:06:06.123456Z");
        assertSearchReturnsSavedResource(     "instant-precise", "0006-06-06T06:06:06.123456789Z");
    }
    @Test
    public void testSearchDate_dateTime_precise() throws Exception {
        assertSearchReturnsSavedResource(     "dateTime-precise", "0001-01-01T01:01:01.1Z");
        assertSearchReturnsSavedResource(     "dateTime-precise", "0002-02-02T02:02:02.12Z");
        assertSearchReturnsSavedResource(     "dateTime-precise", "0003-03-03T03:03:03.123Z");
        assertSearchReturnsSavedResource(     "dateTime-precise", "0004-04-04T04:04:04.1234Z");
        assertSearchReturnsSavedResource(     "dateTime-precise", "0005-05-05T05:05:05.12345Z");
        assertSearchReturnsSavedResource(     "dateTime-precise", "0006-06-06T06:06:06.123456Z");
        assertSearchReturnsSavedResource(     "dateTime-precise", "0006-06-06T06:06:06.123456789Z");
    }
    @Test
    public void testSearchDate_instant_chained() throws Exception {
        // Instant - 2018-10-29T17:12:44-04:00
        assertSearchReturnsComposition("subject:Basic.instant", "2018-10-29T17:12:44.000000-04:00");
        assertSearchReturnsComposition("subject:Basic.instant", "2018-10-29T17:12:44.000000999-04:00");
    }
    @Test
    public void testSearchDate_instant_missing() throws Exception {
        assertSearchReturnsSavedResource(     "instant:missing", "false");
        assertSearchDoesntReturnSavedResource("instant:missing", "true");
        assertSearchReturnsSavedResource(     "missing-instant:missing", "true");
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
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "2018");
        assertSearchDoesntReturnSavedResource("Period", "2017");
        assertSearchDoesntReturnSavedResource("Period", "2019");
    }
    @Test
    public void testSearchDate_Period_EQ_Month() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "2018-10");
        assertSearchDoesntReturnSavedResource("Period", "2018-09");
        assertSearchDoesntReturnSavedResource("Period", "2018-11");
    }
    @Test
    public void testSearchDate_Period_EQ_Day() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "2018-10-29");
        assertSearchDoesntReturnSavedResource("Period", "2018-10-28");
        assertSearchDoesntReturnSavedResource("Period", "2018-10-30");
    }
    @Test
    public void testSearchDate_Period_EQ_Hours() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "2018-10-29T21Z");
        assertSearchDoesntReturnSavedResource("Period", "2018-10-29T11Z");
        assertSearchDoesntReturnSavedResource("Period", "2018-10-29T17Z");
        assertSearchDoesntReturnSavedResource("Period", "2018-10-29T22Z");
        assertSearchDoesntReturnSavedResource("Period", "2018-10-29T20Z");
    }
    @Test
    public void testSearchDate_Period_EQ_Minutes() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        // The Search Value of the implied range DOES NOT contain the start/end fully.
        assertSearchDoesntReturnSavedResource("Period", "2018-10-29T21:13Z");
    }
    @Test
    public void testSearchDate_Period_EQ_Seconds() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        // The Search Value of the implied range DOES NOT contain the start/end fully.
        assertSearchDoesntReturnSavedResource("Period", "2018-10-29T21:13:00Z");
        assertSearchDoesntReturnSavedResource("Period", "2018-10-29T21:13:00.000123Z");
        assertSearchDoesntReturnSavedResource("Period", "2018-10-29T21:13:00.000123456Z");
    }
    @Test
    public void testSearchDate_Period_EQ_ZonedDateTime() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "2018-10-29T21-00:00");
        assertSearchReturnsSavedResource(     "Period", "2018-10-29T17-04:00");
        assertSearchDoesntReturnSavedResource("Period", "2018-10-29T17-05:00");
        assertSearchDoesntReturnSavedResource("Period", "2018-10-29T17:12:00-04:00");
        assertSearchDoesntReturnSavedResource("Period", "2018-10-29T17:12:44-04:00");
        assertSearchReturnsSavedResource(     "Period", "eq2018-10-29T21-00:00");
        assertSearchReturnsSavedResource(     "Period", "eq2018-10-29T17-04:00");
        assertSearchDoesntReturnSavedResource("Period", "eq2018-10-29T17-05:00");
        assertSearchDoesntReturnSavedResource("Period", "eq2018-10-29T17:12:00-04:00");
        assertSearchDoesntReturnSavedResource("Period", "eq2018-10-29T17:12:44-04:00");
        assertSearchDoesntReturnSavedResource("Period", "2018-10-29T17:18:00-04:00");
        assertSearchDoesntReturnSavedResource("Period", "2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("Period", "2018-10-30T00:00:00.000001999Z");
        assertSearchDoesntReturnSavedResource("Period", "2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("Period", "2018-10-28T23:59:59.999999999Z");
    }
    @Test
    public void testSearchDate_Period_LT_Year() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchDoesntReturnSavedResource("Period", "lt2017");
        assertSearchDoesntReturnSavedResource("Period", "lt2018");
        assertSearchReturnsSavedResource(     "Period", "lt2019");
    }
    @Test
    public void testSearchDate_Period_LT_Month() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchDoesntReturnSavedResource("Period", "lt2018-09");
        assertSearchDoesntReturnSavedResource("Period", "lt2018-10");
        assertSearchReturnsSavedResource(     "Period", "lt2018-11");
    }
    @Test
    public void testSearchDate_Period_LT_Day() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchDoesntReturnSavedResource("Period", "lt2018-10-28");
        assertSearchDoesntReturnSavedResource("Period", "lt2018-10-29");
        assertSearchReturnsSavedResource(     "Period", "lt2018-10-30");
    }
    @Test
    public void testSearchDate_Period_LT_Hours() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchDoesntReturnSavedResource("Period", "lt2018-10-29T17Z");
        assertSearchDoesntReturnSavedResource("Period", "lt2018-10-29T20Z");
        assertSearchDoesntReturnSavedResource("Period", "lt2018-10-29T21Z");
        assertSearchReturnsSavedResource(     "Period", "lt2018-10-29T22Z");
    }
    @Test
    public void testSearchDate_Period_LT_Minutes() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "lt2018-10-29T21:19Z");
        assertSearchDoesntReturnSavedResource("Period", "lt2018-10-29T21:00:00Z");
        assertSearchReturnsSavedResource(     "Period", "lt2018-10-29T21:18:01Z");
    }
    @Test
    public void testSearchDate_Period_LT_Seconds() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "lt2018-10-29T21:19:00");
        assertSearchReturnsSavedResource(     "Period", "lt2018-10-29T21:13:00");
        assertSearchReturnsSavedResource(     "Period", "lt2018-10-29T21:13:00.000123");
        assertSearchReturnsSavedResource(     "Period", "lt2018-10-29T21:13:00.000123456");
        assertSearchDoesntReturnSavedResource("Period", "lt2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("Period", "lt2018-10-28T23:59:59.999999999Z");
        assertSearchReturnsSavedResource(     "Period", "lt2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource(     "Period", "lt2018-10-30T00:00:00.000001999Z");
    }
    @Test
    public void testSearchDate_Period_LT_ZonedDateTime() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "lt2018-10-29T22-00:00");
        assertSearchReturnsSavedResource(     "Period", "lt2018-10-29T18-04:00");
        assertSearchDoesntReturnSavedResource("Period", "lt2018-10-29T15-05:00");

        assertSearchReturnsSavedResource(     "Period", "lt2018-10-29T17:18:01-04:00");
        assertSearchReturnsSavedResource(     "Period", "lt2018-10-29T17:18:00-04:00");
        assertSearchDoesntReturnSavedResource("Period", "lt2018-10-29T17:12:00-04:00");
        assertSearchDoesntReturnSavedResource("Period", "lt2018-10-29T17:11:00-04:00");
    }
    @Test
    public void testSearchDate_Period_LE_Year() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchDoesntReturnSavedResource("Period", "le2017");
        assertSearchReturnsSavedResource(     "Period", "le2018");
        assertSearchReturnsSavedResource(     "Period", "le2019");
    }
    @Test
    public void testSearchDate_Period_LE_Month() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchDoesntReturnSavedResource("Period", "le2018-09");
        assertSearchReturnsSavedResource(     "Period", "le2018-10");
        assertSearchReturnsSavedResource(     "Period", "le2018-11");
    }
    @Test
    public void testSearchDate_Period_LE_Day() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchDoesntReturnSavedResource("Period", "le2018-10-27");
        assertSearchDoesntReturnSavedResource("Period", "le2018-10-28");
        assertSearchReturnsSavedResource(     "Period", "le2018-10-29");
        assertSearchReturnsSavedResource(     "Period", "le2018-10-30");
    }
    @Test
    public void testSearchDate_Period_LE_Hours() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchDoesntReturnSavedResource("Period", "le2017-10-29T22Z");
        assertSearchDoesntReturnSavedResource("Period", "le2018-10-29T17Z");
        assertSearchDoesntReturnSavedResource("Period", "le2018-10-29T20Z");
        assertSearchDoesntReturnSavedResource("Period", "le2018-10-28T21Z");
        assertSearchReturnsSavedResource(     "Period", "le2018-10-29T21Z");
        assertSearchReturnsSavedResource(     "Period", "le2018-10-29T22Z");
        assertSearchReturnsSavedResource(     "Period", "le2018-10-29T23Z");
    }
    @Test
    public void testSearchDate_Period_LE_Minutes() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "le2018-10-29T21:19Z");
        assertSearchDoesntReturnSavedResource("Period", "le2018-10-29T21:00:00Z");
        assertSearchReturnsSavedResource(     "Period", "le2018-10-29T21:18:01Z");
    }
    @Test
    public void testSearchDate_Period_LE_Seconds() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "le2018-10-29T21:19:00");
        assertSearchReturnsSavedResource(     "Period", "le2018-10-29T21:13:00");
        assertSearchReturnsSavedResource(     "Period", "le2018-10-29T21:13:00.000123");
        assertSearchReturnsSavedResource(     "Period", "le2018-10-29T21:13:00.000123456");
        assertSearchReturnsSavedResource(     "Period", "le2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource(     "Period", "le2018-10-30T00:00:00.000001999Z");
        assertSearchDoesntReturnSavedResource("Period", "le2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("Period", "le2018-10-28T23:59:59.999999999Z");
    }
    @Test
    public void testSearchDate_Period_LE_ZonedDateTime() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "le2018-10-29T22-00:00");
        assertSearchReturnsSavedResource(     "Period", "le2018-10-29T18-04:00");
        assertSearchDoesntReturnSavedResource("Period", "le2018-10-29T15-05:00");
        assertSearchReturnsSavedResource(     "Period", "le2018-10-29T17:18:01-04:00");
        assertSearchDoesntReturnSavedResource("Period", "le2018-10-29T17:00:00-04:00");
        assertSearchReturnsSavedResource(     "Period", "le2018-10-29T17:12:44-04:00");
        assertSearchReturnsSavedResource(     "Period", "le2018-10-29T17:18:00-04:00");
    }
    @Test
    public void testSearchDate_Period_GE_Year() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "ge1901");
        assertSearchReturnsSavedResource(     "Period", "ge2018");
        assertSearchDoesntReturnSavedResource("Period", "ge2019");
        assertSearchDoesntReturnSavedResource("Period", "ge2020");
    }
    @Test
    public void testSearchDate_Period_GE_Month() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "ge2018-09");
        assertSearchReturnsSavedResource(     "Period", "ge2018-10");
        assertSearchDoesntReturnSavedResource("Period", "ge2018-11");
    }
    @Test
    public void testSearchDate_Period_GE_Day() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "ge2018-10-28");
        assertSearchReturnsSavedResource(     "Period", "ge2018-10-29");
        assertSearchDoesntReturnSavedResource("Period", "ge2018-10-30");
    }
    @Test
    public void testSearchDate_Period_GE_Hours() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchDoesntReturnSavedResource("Period", "ge2019-10-29T22Z");
        assertSearchDoesntReturnSavedResource("Period", "ge2018-10-29T22Z");
        assertSearchReturnsSavedResource(     "Period", "ge2018-10-29T21Z");
        assertSearchReturnsSavedResource(     "Period", "ge2018-10-29T17Z");
        assertSearchReturnsSavedResource(     "Period", "ge2017-10-29T22Z");
        assertSearchReturnsSavedResource(     "Period", "ge2018-10-29T20Z");
    }
    @Test
    public void testSearchDate_Period_GE_Minutes() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchDoesntReturnSavedResource("Period", "ge2018-10-29T21:19Z");
        assertSearchReturnsSavedResource(     "Period", "ge2018-10-29T21:17Z");
        assertSearchReturnsSavedResource(     "Period", "ge2018-10-29T21:18Z");
    }
    @Test
    public void testSearchDate_Period_GE_Seconds() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "ge2018-10-29T21:12:00Z");
        assertSearchReturnsSavedResource(     "Period", "ge2018-10-29T21:13:00Z");
        assertSearchReturnsSavedResource(     "Period", "ge2018-10-29T21:13:00.000123Z");
        assertSearchReturnsSavedResource(     "Period", "ge2018-10-29T21:13:00.000123456Z");
        assertSearchReturnsSavedResource(     "Period", "ge2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource(     "Period", "ge2018-10-28T23:59:59.999999999Z");
        assertSearchDoesntReturnSavedResource("Period", "ge2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("Period", "ge2018-10-30T00:00:00.000001999Z");
        assertSearchReturnsSavedResource(     "Period", "ge2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource(     "Period", "ge2018-10-28T23:59:59.999999999Z");
    }
    @Test
    public void testSearchDate_Period_GE_ZonedDateTime() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchDoesntReturnSavedResource("Period", "ge2018-10-29T22-00:00");
        assertSearchReturnsSavedResource(     "Period", "ge2018-10-29T21-00:00");
        assertSearchReturnsSavedResource(     "Period", "ge2018-10-29T17-04:00");
        assertSearchReturnsSavedResource(     "Period", "ge2018-10-29T15-05:00");
        assertSearchReturnsSavedResource(     "Period", "ge2018-10-29T17:11:01-04:00");
        assertSearchReturnsSavedResource(     "Period", "ge2018-10-29T17:00:00-04:00");
        assertSearchDoesntReturnSavedResource("Period", "ge2018-10-29T18:18:00-04:00");
        assertSearchReturnsSavedResource(     "Period", "ge2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource(     "Period", "ge2018-10-29T17:18:00-04:00");
    }
    @Test
    public void testSearchDate_Period_GT_Year() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "gt2017");
        assertSearchDoesntReturnSavedResource("Period", "gt2018");
        assertSearchDoesntReturnSavedResource("Period", "gt2019");
    }
    @Test
    public void testSearchDate_Period_GT_Month() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "gt2018-09");
        assertSearchDoesntReturnSavedResource("Period", "gt2018-10");
        assertSearchDoesntReturnSavedResource("Period", "gt2018-11");
    }
    @Test
    public void testSearchDate_Period_GT_Day() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "gt2018-10-28");
        assertSearchDoesntReturnSavedResource("Period", "gt2018-10-29");
        assertSearchDoesntReturnSavedResource("Period", "gt2018-10-30");
    }
    @Test
    public void testSearchDate_Period_GT_Hours() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"

        assertSearchReturnsSavedResource(     "Period", "gt2018-10-29T20Z");
        assertSearchDoesntReturnSavedResource("Period", "gt2018-10-29T21Z");
        assertSearchDoesntReturnSavedResource("Period", "gt2018-10-29T22Z");
    }
    @Test
    public void testSearchDate_Period_GT_Minutes() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "gt2018-10-29T21:11Z");
        assertSearchReturnsSavedResource(     "Period", "gt2018-10-29T21:13Z");
        assertSearchDoesntReturnSavedResource("Period", "gt2018-10-29T21:18Z");
    }
    @Test
    public void testSearchDate_Period_GT_Seconds() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "gt2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource(     "Period", "gt2018-10-29T21:11:00Z");
        assertSearchReturnsSavedResource(     "Period", "gt2018-10-29T21:12:00Z");
        assertSearchReturnsSavedResource(     "Period", "gt2018-10-29T21:13:00Z");
        assertSearchReturnsSavedResource(     "Period", "gt2018-10-29T21:13:00.000123Z");
        assertSearchReturnsSavedResource(     "Period", "gt2018-10-29T21:13:00.000123456Z");
        assertSearchDoesntReturnSavedResource("Period", "gt2018-10-29T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("Period", "gt2018-10-29T23:59:59.999999999Z");
        assertSearchDoesntReturnSavedResource("Period", "gt2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("Period", "gt2018-10-30T00:00:00.000001999Z");
    }
    @Test
    public void testSearchDate_Period_GT_ZonedDateTime() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "gt2018-10-29T20-00:00");
        assertSearchDoesntReturnSavedResource("Period", "gt2018-10-29T18-04:00");
        assertSearchReturnsSavedResource(     "Period", "gt2018-10-29T15-05:00");
        assertSearchDoesntReturnSavedResource("Period", "gt2018-10-29T17:18:01-04:00");
        assertSearchReturnsSavedResource(     "Period", "gt2018-10-29T17:11:59-04:00");
        assertSearchReturnsSavedResource(     "Period", "gt2018-10-29T17:12:44-03:00");
        assertSearchReturnsSavedResource(     "Period", "gt2018-10-29T17:12:00-03:00");
        assertSearchDoesntReturnSavedResource("Period", "gt2018-10-29T17:18:00-05:00");
    }
    @Test
    public void testSearchDate_Period_SA_Year() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "sa2017");
        assertSearchDoesntReturnSavedResource("Period", "sa2018");
        assertSearchDoesntReturnSavedResource("Period", "sa2019");
        assertSearchDoesntReturnSavedResource("Period", "sa2020");
    }
    @Test
    public void testSearchDate_Period_SA_Month() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "sa2018-09");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-11");
    }
    @Test
    public void testSearchDate_Period_SA_Day() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "sa2018-10-28");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-29");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-30");
    }
    @Test
    public void testSearchDate_Period_SA_Hours() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "sa2018-10-29T17Z");
        assertSearchReturnsSavedResource(     "Period", "sa2018-10-29T20Z");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-29T21Z");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-29T22Z");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-30T20Z");
    }
    @Test
    public void testSearchDate_Period_SA_Minutes() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "sa2018-10-29T21:11Z");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-29T21:13Z");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-29T21:19Z");
    }
    @Test
    public void testSearchDate_Period_SA_Seconds() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "sa2018-10-29T21:00:00Z");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-29T21:19:00Z");
        assertSearchReturnsSavedResource(     "Period", "sa2018-10-28T20:59:59.999999Z");
        assertSearchReturnsSavedResource(     "Period", "sa2018-10-28T20:59:59.999999999Z");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-29T21:12:01Z");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-29T21:19:00Z");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-29T21:13:00Z");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-29T21:13:00.000123Z");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-29T21:13:00.000123456Z");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-30T00:00:00.000001999Z");
    }
    @Test
    public void testSearchDate_Period_SA_ZonedDateTime() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-29T22-00:00");
        assertSearchReturnsSavedResource(     "Period", "sa2018-10-29T18-02:00");
        assertSearchReturnsSavedResource(     "Period", "sa2018-10-29T15-05:00");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-29T17:18:01-04:00");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-29T17:18:00-04:00");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-29T17:12:00-04:00");
        assertSearchDoesntReturnSavedResource("Period", "sa2018-10-29T17:12:44-04:00");
    }
    @Test
    public void testSearchDate_Period_EB_Year() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchDoesntReturnSavedResource("Period", "eb2018");
        assertSearchReturnsSavedResource(     "Period", "eb2019");
        assertSearchReturnsSavedResource(     "Period", "eb2020");
    }
    @Test
    public void testSearchDate_Period_EB_Month() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchDoesntReturnSavedResource("Period", "eb2018-10");
        assertSearchReturnsSavedResource(     "Period", "eb2018-11");
    }
    @Test
    public void testSearchDate_Period_EB_Day() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchDoesntReturnSavedResource("Period", "eb2018-10-28");
        assertSearchDoesntReturnSavedResource("Period", "eb2018-10-29");
        assertSearchReturnsSavedResource(     "Period", "eb2018-10-30");
    }
    @Test
    public void testSearchDate_Period_EB_Hours() throws Exception {
//      EDT: [2018-10-29T17:12:00-04:00, 2018-10-29T17:18:00-04:00]
        assertSearchDoesntReturnSavedResource("Period", "eb2018-10-29T17-04:00");
        assertSearchReturnsSavedResource(     "Period", "eb2018-10-29T18-04:00");

//      UTC: [2018-10-29T21:12:00Z, 2018-10-30T21:18:00-04:00]
        assertSearchDoesntReturnSavedResource("Period", "eb2018-10-29T21Z");
        assertSearchReturnsSavedResource(     "Period", "eb2018-10-29T22Z");
    }
    @Test
    public void testSearchDate_Period_EB_Minutes() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "eb2018-10-29T21:19Z");
        assertSearchDoesntReturnSavedResource("Period", "eb2018-10-29T21:00:00Z");
        assertSearchReturnsSavedResource(     "Period", "eb2018-10-29T21:18:01Z");
    }
    @Test
    public void testSearchDate_Period_EB_Seconds() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "eb2018-10-29T21:19:00Z");
        assertSearchDoesntReturnSavedResource("Period", "eb2018-10-29T21:13:00Z");
        assertSearchDoesntReturnSavedResource("Period", "eb2018-10-29T21:13:00.000123Z");
        assertSearchDoesntReturnSavedResource("Period", "eb2018-10-29T21:13:00.000123456Z");
        assertSearchDoesntReturnSavedResource("Period", "eb2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("Period", "eb2018-10-28T23:59:59.999999999Z");
        assertSearchReturnsSavedResource(     "Period", "eb2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource(     "Period", "eb2018-10-30T00:00:00.000001999Z");
    }
    @Test
    public void testSearchDate_Period_EB_ZonedDateTime() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "eb2018-10-29T22-00:00");
        assertSearchReturnsSavedResource(     "Period", "eb2018-10-29T18-04:00");
        assertSearchDoesntReturnSavedResource("Period", "eb2018-10-29T15-05:00");
        assertSearchReturnsSavedResource(     "Period", "eb2018-10-29T17:18:01-04:00");
        assertSearchDoesntReturnSavedResource("Period", "eb2018-10-29T17:18:00-04:00");
        assertSearchDoesntReturnSavedResource("Period", "eb2018-10-29T17:12:00-04:00");
        assertSearchDoesntReturnSavedResource("Period", "eb2018-10-29T17:12:44-04:00");
        assertSearchDoesntReturnSavedResource("Period", "eb2018-10-29T17:18:00-04:00");
    }
    @Test
    public void testSearchDate_Period_NE_Year() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchDoesntReturnSavedResource("Period", "ne2018");
        assertSearchReturnsSavedResource(     "Period", "ne2019");
        assertSearchReturnsSavedResource(     "Period", "ne2020");
    }
    @Test
    public void testSearchDate_Period_NE_Month() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchDoesntReturnSavedResource("Period", "ne2018-10");
        assertSearchReturnsSavedResource(     "Period", "ne2018-11");
    }
    @Test
    public void testSearchDate_Period_NE_Day() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchDoesntReturnSavedResource("Period", "ne2018-10-29");
        assertSearchReturnsSavedResource(     "Period", "ne2018-10-30");
        assertSearchReturnsSavedResource(     "Period", "ne2018-10-28");
    }
    @Test
    public void testSearchDate_Period_NE_Hours() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "ne2018-10-29T20Z");
        assertSearchDoesntReturnSavedResource("Period", "ne2018-10-29T21Z");
        assertSearchReturnsSavedResource(     "Period", "ne2018-10-29T22Z");
    }
    @Test
    public void testSearchDate_Period_NE_Minutes() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "ne2018-10-29T21:00");
        assertSearchReturnsSavedResource(     "Period", "ne2018-10-29T21:12");
        assertSearchReturnsSavedResource(     "Period", "ne2018-10-29T21:12");
    }
    @Test
    public void testSearchDate_Period_NE_Seconds() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "ne2018-10-29T21:18:01");
        assertSearchReturnsSavedResource(     "Period", "ne2018-10-29T21:19:00");
        assertSearchReturnsSavedResource(     "Period", "ne2018-10-29T21:13:00");
        assertSearchReturnsSavedResource(     "Period", "ne2018-10-29T21:13:00.000123");
        assertSearchReturnsSavedResource(     "Period", "ne2018-10-29T21:13:00.000123456");
        assertSearchReturnsSavedResource(     "Period", "ne2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource(     "Period", "ne2018-10-30T00:00:00.000001999Z");
        assertSearchReturnsSavedResource(     "Period", "ne2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource(     "Period", "ne2018-10-28T23:59:59.999999999Z");
    }
    @Test
    public void testSearchDate_Period_NE_ZonedDateTime() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "ne2018-10-29T22-00:00");
        assertSearchReturnsSavedResource(     "Period", "ne2018-10-29T18-04:00");
        assertSearchDoesntReturnSavedResource("Period", "ne2018-10-29T16-05:00");
        assertSearchReturnsSavedResource(     "Period", "ne2018-10-29T17:18:01-04:00");
        assertSearchReturnsSavedResource(     "Period", "ne2018-10-29T17:18:00-04:00");
        assertSearchReturnsSavedResource(     "Period", "ne2018-10-29T17:12:44-04:00");
        assertSearchReturnsSavedResource(     "Period", "ne2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource(     "Period", "ne2018-10-29T17:18:00-04:00");
    }
    @Test
    public void testSearchDate_Period_AP_Year() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "ap2018");
        assertSearchDoesntReturnSavedResource("Period", "ap2100");
        assertSearchDoesntReturnSavedResource("Period", "ap2000");
        assertSearchDoesntReturnSavedResource("Period", "ap2025");
    }
    @Test
    public void testSearchDate_Period_AP_Month() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "ap2018-10");
        assertSearchReturnsSavedResource(     "Period", "ap2018-11");
        assertSearchDoesntReturnSavedResource("Period", "ap2018-01");
    }
    @Test
    public void testSearchDate_Period_AP_Day() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchDoesntReturnSavedResource("Period", "ap2018-01-01");
        assertSearchReturnsSavedResource(     "Period", "ap2018-10-28");
        assertSearchReturnsSavedResource(     "Period", "ap2018-10-29");
        assertSearchReturnsSavedResource(     "Period", "ap2018-10-30");
        assertSearchDoesntReturnSavedResource("Period", "ap2020-01-01");
    }
    @Test
    public void testSearchDate_Period_AP_Hours() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "ap2018-10-29T22");
        assertSearchReturnsSavedResource(     "Period", "ap2018-10-29T17");
        assertSearchReturnsSavedResource(     "Period", "ap2018-10-29T20");
        assertSearchDoesntReturnSavedResource("Period", "ap2017-10-29T21");
        assertSearchDoesntReturnSavedResource("Period", "ap2017-10-29T22");
    }
    @Test
    public void testSearchDate_Period_AP_Minutes() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "ap2018-10-29T21:19");
        assertSearchReturnsSavedResource(     "Period", "ap2018-10-29T21:00:00");
        assertSearchReturnsSavedResource(     "Period", "ap2018-10-29T21:18:01");
        assertSearchDoesntReturnSavedResource("Period", "ap2017-10-29T21:00:00");
    }
    @Test
    public void testSearchDate_Period_AP_Seconds() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "ap2018-10-29T21:19:00");
        assertSearchReturnsSavedResource(     "Period", "ap2018-10-29T21:13:00");
        assertSearchDoesntReturnSavedResource("Period", "ap2017-10-29T21:13:00.000123");
        assertSearchDoesntReturnSavedResource("Period", "ap2017-10-29T21:13:00.000123456");
        assertSearchDoesntReturnSavedResource("Period", "ap2019-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("Period", "ap2019-10-28T23:59:59.999999999Z");
        assertSearchReturnsSavedResource(     "Period", "ap2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource(     "Period", "ap2018-10-30T00:00:00.000001999Z");
        assertSearchReturnsSavedResource(     "Period", "ap2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource(     "Period", "ap2018-10-28T23:59:59.999999999Z");
    }
    @Test
    public void testSearchDate_Period_AP_ZonedDateTime() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period", "ap2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource(     "Period", "ap2018-10-29T17:18:00-04:00");
        assertSearchReturnsSavedResource(     "Period", "ap2018-10-29T17:12:44-04:00");
    }
    @Test
    public void testSearchDate_Period_NoStart() throws Exception {
        // "Period-noStart" has end=2018-10-29T17:18:00-04:00
        // the range of the search value doesn't fully contain the range of the target value
        assertSearchDoesntReturnSavedResource("Period-noStart", "2018-10-29");
        assertSearchReturnsSavedResource(     "Period-noStart", "ne2018-10-29");
        assertSearchReturnsSavedResource(     "Period-noStart", "lt2018-10-30");
        assertSearchReturnsSavedResource(     "Period-noStart", "lt2018-10-29");
        assertSearchDoesntReturnSavedResource("Period-noStart", "gt2018-10-29");
        assertSearchDoesntReturnSavedResource("Period-noStart", "gt2018-10-30");

        assertSearchReturnsSavedResource(     "Period-noStart", "le2018-10-29");
        assertSearchReturnsSavedResource(     "Period-noStart", "ge2018-10-29");
        assertSearchDoesntReturnSavedResource("Period-noStart", "ge2018-10-30");
        assertSearchDoesntReturnSavedResource("Period-noStart", "sa2018-10-29");
        assertSearchDoesntReturnSavedResource("Period-noStart", "eb2018-10-29");
        assertSearchReturnsSavedResource(     "Period-noStart", "ap2018-10-29");

        // search on the dateTime at the end of the Period-noStart
        // the range of the search value doesn't fully contain the range of the target value
        assertSearchDoesntReturnSavedResource("Period-noStart", "2018-10-29T17:18:00+04:00");
        assertSearchReturnsSavedResource(     "Period-noStart", "ne2018-10-29T17:18:00-04:00");
        assertSearchReturnsSavedResource(     "Period-noStart", "lt2018-10-29T17:18:00-04:00");
        assertSearchReturnsSavedResource(     "Period-noStart", "lt2018-10-29T17:18:01-04:00");
        assertSearchDoesntReturnSavedResource("Period-noStart", "gt2018-10-29T17:18:00-04:00");
        assertSearchReturnsSavedResource(     "Period-noStart", "le2018-10-29T17:18:00-04:00");
        assertSearchReturnsSavedResource(     "Period-noStart", "ge2018-10-29T17:18:00-04:00");
        assertSearchDoesntReturnSavedResource("Period-noStart", "sa2018-10-29T17:18:00-04:00");
        assertSearchDoesntReturnSavedResource("Period-noStart", "eb2018-10-29T17:18:00-04:00");
        assertSearchReturnsSavedResource(     "Period-noStart", "ap2018-10-29T17:18:00-04:00");

        assertSearchDoesntReturnSavedResource("Period-noStart", "2018-10-28");
        assertSearchReturnsSavedResource(     "Period-noStart", "ne2018-10-28");
        assertSearchReturnsSavedResource(     "Period-noStart", "lt2018-10-28");
        assertSearchReturnsSavedResource(     "Period-noStart", "lt2018-10-30");
        assertSearchReturnsSavedResource(     "Period-noStart", "gt2018-10-28");
        assertSearchReturnsSavedResource(     "Period-noStart", "le2018-10-28");
        assertSearchReturnsSavedResource(     "Period-noStart", "le2018-10-29");
        assertSearchReturnsSavedResource(     "Period-noStart", "ge2018-10-28");
        assertSearchDoesntReturnSavedResource("Period-noStart", "sa2018-10-28");
        assertSearchDoesntReturnSavedResource("Period-noStart", "eb2018-10-28");
        assertSearchReturnsSavedResource(     "Period-noStart", "ap2018-10-28");

        assertSearchDoesntReturnSavedResource("Period-noStart", "2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource(     "Period-noStart", "ne2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource(     "Period-noStart", "lt2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource(     "Period-noStart", "lt2018-10-29T23:59:59.999999Z");
        assertSearchReturnsSavedResource(     "Period-noStart", "gt2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("Period-noStart", "gt2018-10-29T23:59:59.999999Z");
        assertSearchReturnsSavedResource(     "Period-noStart", "le2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource(     "Period-noStart", "le2018-10-29T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("Period-noStart", "sa2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("Period-noStart", "eb2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource(     "Period-noStart", "ap2018-10-28T23:59:59.999999Z");

        assertSearchDoesntReturnSavedResource("Period-noStart", "2018-10-28T23:59:59.999999999Z");
        assertSearchReturnsSavedResource(     "Period-noStart", "ne2018-10-28T23:59:59.999999999Z");
        assertSearchReturnsSavedResource(     "Period-noStart", "lt2018-10-28T23:59:59.999999999Z");
        assertSearchReturnsSavedResource(     "Period-noStart", "lt2018-10-29T23:59:59.999999999Z");
        assertSearchReturnsSavedResource(     "Period-noStart", "gt2018-10-28T23:59:59.999999999Z");
        assertSearchDoesntReturnSavedResource("Period-noStart", "gt2018-10-29T23:59:59.999999999Z");
        assertSearchReturnsSavedResource(     "Period-noStart", "le2018-10-28T23:59:59.999999999Z");
        assertSearchReturnsSavedResource(     "Period-noStart", "le2018-10-29T23:59:59.999999999Z");
        assertSearchDoesntReturnSavedResource("Period-noStart", "sa2018-10-28T23:59:59.999999999Z");
        assertSearchDoesntReturnSavedResource("Period-noStart", "eb2018-10-28T23:59:59.999999999Z");
        assertSearchReturnsSavedResource(     "Period-noStart", "ap2018-10-28T23:59:59.999999999Z");

        assertSearchDoesntReturnSavedResource("Period-noStart", "2018-10-30");
        assertSearchReturnsSavedResource(     "Period-noStart", "ne2018-10-30");
        assertSearchReturnsSavedResource(     "Period-noStart", "lt2018-10-30");
        assertSearchDoesntReturnSavedResource("Period-noStart", "gt2018-10-30");
        assertSearchReturnsSavedResource(     "Period-noStart", "le2018-10-30");
        assertSearchDoesntReturnSavedResource("Period-noStart", "ge2018-10-30");
        assertSearchDoesntReturnSavedResource("Period-noStart", "sa2018-10-30");
        assertSearchReturnsSavedResource(     "Period-noStart", "eb2018-10-30");
        assertSearchReturnsSavedResource(     "Period-noStart", "ap2018-10-30");

        assertSearchDoesntReturnSavedResource("Period-noStart", "2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource(     "Period-noStart", "ne2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource(     "Period-noStart", "lt2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("Period-noStart", "gt2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource(     "Period-noStart", "le2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("Period-noStart", "ge2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("Period-noStart", "sa2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource(     "Period-noStart", "eb2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource(     "Period-noStart", "ap2018-10-30T00:00:00.000001Z");

        assertSearchDoesntReturnSavedResource("Period-noStart", "2018-10-30T00:00:00.000001999Z");
        assertSearchReturnsSavedResource(     "Period-noStart", "ne2018-10-30T00:00:00.000001999Z");
        assertSearchReturnsSavedResource(     "Period-noStart", "lt2018-10-30T00:00:00.000001999Z");
        assertSearchDoesntReturnSavedResource("Period-noStart", "gt2018-10-30T00:00:00.000001999Z");
        assertSearchReturnsSavedResource(     "Period-noStart", "le2018-10-30T00:00:00.000001999Z");
        assertSearchDoesntReturnSavedResource("Period-noStart", "ge2018-10-30T00:00:00.000001999Z");
        assertSearchDoesntReturnSavedResource("Period-noStart", "sa2018-10-30T00:00:00.000001999Z");
        assertSearchReturnsSavedResource(     "Period-noStart", "eb2018-10-30T00:00:00.000001999Z");
        assertSearchReturnsSavedResource(     "Period-noStart", "ap2018-10-30T00:00:00.000001999Z");

        // ap has 10% leeway of the gap between now and the date, so pick a sufficiently late date
        assertSearchDoesntReturnSavedResource("Period-noStart", "ap2098-10-30");
    }
    @Test
    public void testSearchDate_Period_NoEnd() throws Exception {
        // "Period-noEnd" has start=2018-10-29T17:12:00-04:00
        // No End -  A missing upper boundary is "greater than" any actual date.

        // the range of the search value doesn't fully contain the range of the target value
        assertSearchDoesntReturnSavedResource("Period-noEnd", "2018-10-29");
        assertSearchReturnsSavedResource(     "Period-noEnd", "ne2018-10-29");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "lt2018-10-29");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "lt2018-10-28");
        assertSearchReturnsSavedResource(     "Period-noEnd", "gt2018-10-28");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "le2018-10-28");
        assertSearchReturnsSavedResource(     "Period-noEnd", "le2018-10-29");
        assertSearchReturnsSavedResource(     "Period-noEnd", "ge2018-10-29");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "sa2018-10-29");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "eb2018-10-29");
        assertSearchReturnsSavedResource(     "Period-noEnd", "ap2018-10-29");

        // search on the dateTime at the start of the Period-noEnd
        // the range of the search value doesn't fully contain the range of the target value
        assertSearchDoesntReturnSavedResource("Period-noEnd", "2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource(     "Period-noEnd", "ne2018-10-29T17:12:00-04:00");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "lt2018-10-29T17:12:00-04:00");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "lt2018-10-29T17:11:00-04:00");
        assertSearchReturnsSavedResource(     "Period-noEnd", "gt2018-10-29T17:11:00-04:00");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "le2018-10-29T17:11:00-04:00");
        assertSearchReturnsSavedResource(     "Period-noEnd", "le2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource(     "Period-noEnd", "ge2018-10-29T17:12:00-04:00");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "sa2018-10-29T17:12:00-04:00");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "eb2018-10-29T17:12:00-04:00");
        assertSearchReturnsSavedResource(     "Period-noEnd", "ap2018-10-29T17:12:00-04:00");

        assertSearchDoesntReturnSavedResource("Period-noEnd", "2018-10-28");
        assertSearchReturnsSavedResource(     "Period-noEnd", "ne2018-10-28");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "lt2018-10-28");
        assertSearchReturnsSavedResource(     "Period-noEnd", "gt2018-10-28");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "le2018-10-28");
        assertSearchReturnsSavedResource(     "Period-noEnd", "ge2018-10-28");
        assertSearchReturnsSavedResource(     "Period-noEnd", "sa2018-10-28");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "eb2018-10-28");
        assertSearchReturnsSavedResource(     "Period-noEnd", "ap2018-10-28");

        assertSearchDoesntReturnSavedResource("Period-noEnd", "2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource(     "Period-noEnd", "ne2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "lt2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource(     "Period-noEnd", "gt2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "le2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource(     "Period-noEnd", "ge2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource(     "Period-noEnd", "sa2018-10-28T23:59:59.999999Z");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "eb2018-10-28T23:59:59.999999Z");
        assertSearchReturnsSavedResource(     "Period-noEnd", "ap2018-10-28T23:59:59.999999Z");

        assertSearchDoesntReturnSavedResource("Period-noEnd", "2018-10-28T23:59:59.999999999Z");
        assertSearchReturnsSavedResource(     "Period-noEnd", "ne2018-10-28T23:59:59.999999999Z");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "lt2018-10-28T23:59:59.999999999Z");
        assertSearchReturnsSavedResource(     "Period-noEnd", "gt2018-10-28T23:59:59.999999999Z");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "le2018-10-28T23:59:59.999999999Z");
        assertSearchReturnsSavedResource(     "Period-noEnd", "ge2018-10-28T23:59:59.999999999Z");
        assertSearchReturnsSavedResource(     "Period-noEnd", "sa2018-10-28T23:59:59.999999999Z");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "eb2018-10-28T23:59:59.999999999Z");
        assertSearchReturnsSavedResource(     "Period-noEnd", "ap2018-10-28T23:59:59.999999999Z");

        assertSearchDoesntReturnSavedResource("Period-noEnd", "2018-10-30");
        assertSearchReturnsSavedResource(     "Period-noEnd", "ne2018-10-30");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "lt2018-10-28");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "lt2018-10-29");
        assertSearchReturnsSavedResource(     "Period-noEnd", "lt2018-10-30");
        assertSearchReturnsSavedResource(     "Period-noEnd", "gt2018-10-30");
        assertSearchReturnsSavedResource(     "Period-noEnd", "gt2018-10-28");
        assertSearchReturnsSavedResource(     "Period-noEnd", "gt2018-10-29");
        assertSearchReturnsSavedResource(     "Period-noEnd", "le2018-10-30");
        assertSearchReturnsSavedResource(     "Period-noEnd", "ge2018-10-30");
        assertSearchReturnsSavedResource(     "Period-noEnd", "ge2018-10-28");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "sa2018-10-30");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "eb2018-10-30");
        assertSearchReturnsSavedResource(     "Period-noEnd", "ap2018-10-30");

        assertSearchDoesntReturnSavedResource("Period-noEnd", "2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource(     "Period-noEnd", "ne2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource(     "Period-noEnd", "lt2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "lt2018-10-29T00:00:00.000001Z");
        assertSearchReturnsSavedResource(     "Period-noEnd", "gt2018-10-30T00:00:00.000000Z");
        assertSearchReturnsSavedResource(     "Period-noEnd", "gt2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource(     "Period-noEnd", "le2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource(     "Period-noEnd", "ge2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "sa2018-10-30T00:00:00.000001Z");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "eb2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource(     "Period-noEnd", "ap2018-10-30T00:00:00.000001Z");
        assertSearchReturnsSavedResource(     "Period-noEnd", "ge2018-10-30T00:00:00.000000Z");
        assertSearchReturnsSavedResource(     "Period-noEnd", "ge2018-10-29T00:00:00.000000Z");

        assertSearchDoesntReturnSavedResource("Period-noEnd", "2018-10-30T00:00:00.000001999Z");
        assertSearchReturnsSavedResource(     "Period-noEnd", "ne2018-10-30T00:00:00.000001999Z");
        assertSearchReturnsSavedResource(     "Period-noEnd", "lt2018-10-30T00:00:00.000001999Z");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "lt2018-10-29T00:00:00.000001999Z");
        assertSearchReturnsSavedResource(     "Period-noEnd", "gt2018-10-30T00:00:00.000000999Z");
        assertSearchReturnsSavedResource(     "Period-noEnd", "gt2018-10-30T00:00:00.000001999Z");
        assertSearchReturnsSavedResource(     "Period-noEnd", "le2018-10-30T00:00:00.000001999Z");
        assertSearchReturnsSavedResource(     "Period-noEnd", "ge2018-10-30T00:00:00.000001999Z");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "sa2018-10-30T00:00:00.000001999Z");
        assertSearchDoesntReturnSavedResource("Period-noEnd", "eb2018-10-30T00:00:00.000001999Z");
        assertSearchReturnsSavedResource(     "Period-noEnd", "ap2018-10-30T00:00:00.000001999Z");
        assertSearchReturnsSavedResource(     "Period-noEnd", "ge2018-10-30T00:00:00.000000999Z");
        assertSearchReturnsSavedResource(     "Period-noEnd", "ge2018-10-29T00:00:00.000000999Z");

        // ap has 10% leeway of the gap between now and the date, so pick a sufficiently early date
        assertSearchDoesntReturnSavedResource("Period-noEnd", "ap2000-10-28");
    }
    @Test
    public void testSearchDate_Period_chained() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsComposition("subject:Basic.Period", "2018-10-29");
    }
    @Test
    public void testSearchDate_Period_missing() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchReturnsSavedResource(     "Period:missing", "false");
        assertSearchDoesntReturnSavedResource("Period:missing", "true");
        assertSearchReturnsSavedResource(     "missing-Period:missing", "true");
        assertSearchDoesntReturnSavedResource("missing-Period:missing", "false");
    }
    @Test
    public void testSearchDate_Period_or() throws Exception {
//      "start": "2018-10-29T17:12:00-04:00",
//      "end": "2018-10-29T17:18:00-04:00"
        assertSearchDoesntReturnSavedResource("Period", "2018-10-28,9999-01-01");
        assertSearchReturnsSavedResource(     "Period", "ne2018-10-28,9999-01-01");
        assertSearchDoesntReturnSavedResource("Period", "lt2018-10-28,9999-01-01");
        assertSearchReturnsSavedResource(     "Period", "gt2018-10-28,9999-01-01");
        assertSearchDoesntReturnSavedResource("Period", "le2018-10-28,9999-01-01");
        assertSearchReturnsSavedResource(     "Period", "ge2018-10-28,9999-01-01");
        assertSearchReturnsSavedResource(     "Period", "sa2018-10-28,9999-01-01");
        assertSearchDoesntReturnSavedResource("Period", "eb2018-10-28,9999-01-01");
        assertSearchReturnsSavedResource(     "Period", "ap2018-10-28,9999-01-01");
        assertSearchDoesntReturnSavedResource("Period", "ap2010-10-28,9999-01-01");

        assertSearchDoesntReturnSavedResource("Period", "9999-01-01,2018-10-28");
        assertSearchReturnsSavedResource(     "Period", "9999-01-01,ne2018-10-28");
        assertSearchDoesntReturnSavedResource("Period", "9999-01-01,lt2018-10-28");
        assertSearchReturnsSavedResource(     "Period", "9999-01-01,gt2018-10-28");
        assertSearchDoesntReturnSavedResource("Period", "9999-01-01,le2018-10-28");
        assertSearchReturnsSavedResource(     "Period", "9999-01-01,ge2018-10-28");
        assertSearchReturnsSavedResource(     "Period", "9999-01-01,sa2018-10-28");
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

    @Test
    public void testSearchDate_instant_chained_missing() throws Exception {
        assertSearchReturnsComposition("subject:Basic.instant:missing", "false");
        assertSearchDoesntReturnComposition("subject:Basic.instant:missing", "true");

        assertSearchReturnsComposition("subject:Basic.missing-instant:missing", "true");
        assertSearchDoesntReturnComposition("subject:Basic.missing-instant:missing", "false");
    }
    @Test
    public void testSearchDate_Period_chained_missing() throws Exception {
        assertSearchReturnsComposition("subject:Basic.Period:missing", "false");
        assertSearchDoesntReturnComposition("subject:Basic.Period:missing", "true");

        assertSearchReturnsComposition("subject:Basic.missing-Period:missing", "true");
        assertSearchDoesntReturnComposition("subject:Basic.missing-Period:missing", "false");
    }
    @Test
    public void testSearchDate_date_chained_missing() throws Exception {
        assertSearchReturnsComposition("subject:Basic.date:missing", "false");
        assertSearchDoesntReturnComposition("subject:Basic.date:missing", "true");

        assertSearchReturnsComposition("subject:Basic.missing-date:missing", "true");
        assertSearchDoesntReturnComposition("subject:Basic.missing-date:missing", "false");
    }
    @Test
    public void testSearchDate_dateTime_chained_missing() throws Exception {
        assertSearchReturnsComposition("subject:Basic.dateTime:missing", "false");
        assertSearchDoesntReturnComposition("subject:Basic.dateTime:missing", "true");

        assertSearchReturnsComposition("subject:Basic.missing-dateTime:missing", "true");
        assertSearchDoesntReturnComposition("subject:Basic.missing-dateTime:missing", "false");
    }
    @Test
    public void testSearchDate_consolidated() throws Exception {
        // "date" is 2018-10-29
        Map<String,List<String>> queryParms = new HashMap<>();
        queryParms.put("date", Arrays.asList("ge2018-10-29","le2018-10-29"));
        assertSearchReturnsSavedResource(queryParms);
        queryParms.put("date", Arrays.asList("ge2018-10-29","le2018-10-29","eb2018-10-29"));
        assertSearchDoesntReturnSavedResource(queryParms);
        queryParms.put("date", Arrays.asList("ge2018-10-29","le2018-10-29","eb2018-10-30"));
        assertSearchReturnsSavedResource(queryParms);
        queryParms.put("date", Arrays.asList("ge2018-10-29","lt2018-10-29","le2018-10-29"));
        assertSearchDoesntReturnSavedResource(queryParms);
        queryParms.put("date", Arrays.asList("ge2018-10-29","lt2018-10-30","le2018-10-29"));
        assertSearchReturnsSavedResource(queryParms);
        queryParms.put("date", Arrays.asList("ge2018-10-29","gt2018-10-29","le2018-10-29"));
        assertSearchDoesntReturnSavedResource(queryParms);
        queryParms.put("date", Arrays.asList("ge2018-10-29","gt2018-10-28","le2018-10-29"));
        assertSearchReturnsSavedResource(queryParms);
        queryParms.put("date", Arrays.asList("ge2018-10-29","sa2018-10-29","le2018-10-29"));
        assertSearchDoesntReturnSavedResource(queryParms);
        queryParms.put("date", Arrays.asList("ge2018-10-29","sa2018-10-28","le2018-10-29"));
        assertSearchReturnsSavedResource(queryParms);
        queryParms.put("date", Arrays.asList("gt2018-10-29","ge2018-10-29","le2018-10-29","lt2018-10-29"));
        assertSearchDoesntReturnSavedResource(queryParms);
        queryParms.put("date", Arrays.asList("sa2018-10-29","ge2018-10-29","le2018-10-29","eb2018-10-29"));
        assertSearchDoesntReturnSavedResource(queryParms);
        queryParms.put("date", Arrays.asList("gt2018-10-29","sa2018-10-29","lt2018-10-29","eb2018-10-29"));
        assertSearchDoesntReturnSavedResource(queryParms);
        queryParms.put("date", Arrays.asList("gt2018-10-28","sa2018-10-28","ge2018-10-29","le2018-10-29","lt2018-10-30","eb2018-10-30"));
        assertSearchReturnsSavedResource(queryParms);

        // "dateTime" is 2019-12-31T20:00:00-04:00 (2020-01-01T00:00:00Z)
        queryParms.clear();
        queryParms.put("dateTime", Arrays.asList("ge2019-12-31T20:00:00","le2019-12-31T20:00:00"));
        assertSearchReturnsSavedResource(queryParms);
        queryParms.put("dateTime", Arrays.asList("ge2019-12-31T20:00:00","le2019-12-31T20:00:00","eb2019-12-31T20:00:00"));
        assertSearchDoesntReturnSavedResource(queryParms);
        queryParms.put("dateTime", Arrays.asList("ge2019-12-31T20:00:00","le2019-12-31T20:00:00","eb2019-12-31T20:00:01"));
        assertSearchReturnsSavedResource(queryParms);
        queryParms.put("dateTime", Arrays.asList("ge2019-12-31T20:00:00","le2019-12-31T20:00:00","lt2019-12-31T20:00:00"));
        assertSearchDoesntReturnSavedResource(queryParms);
        queryParms.put("dateTime", Arrays.asList("ge2019-12-31T20:00:00","le2019-12-31T20:00:00","lt2019-12-31T20:00:01"));
        assertSearchReturnsSavedResource(queryParms);
        queryParms.put("dateTime", Arrays.asList("gt2019-12-31T20:00:00","ge2019-12-31T20:00:00","le2019-12-31T20:00:00"));
        assertSearchDoesntReturnSavedResource(queryParms);
        queryParms.put("dateTime", Arrays.asList("gt2019-12-31T19:59:59","ge2019-12-31T20:00:00","le2019-12-31T20:00:00"));
        assertSearchReturnsSavedResource(queryParms);
        queryParms.put("dateTime", Arrays.asList("sa2019-12-31T20:00:00","ge2019-12-31T20:00:00","le2019-12-31T20:00:00"));
        assertSearchDoesntReturnSavedResource(queryParms);
        queryParms.put("dateTime", Arrays.asList("sa2019-12-31T19:59:59","ge2019-12-31T20:00:00","le2019-12-31T20:00:00"));
        assertSearchReturnsSavedResource(queryParms);
        queryParms.put("dateTime", Arrays.asList("gt2019-12-31T20:00:00","ge2019-12-31T20:00:00","le2019-12-31T20:00:00","lt2019-12-31T20:00:00"));
        assertSearchDoesntReturnSavedResource(queryParms);
        queryParms.put("dateTime", Arrays.asList("sa2019-12-31T20:00:00","ge2019-12-31T20:00:00","le2019-12-31T20:00:00","eb2019-12-31T20:00:00"));
        assertSearchDoesntReturnSavedResource(queryParms);
        queryParms.put("dateTime", Arrays.asList("sa2019-12-31T20:00:00","gt2019-12-31T20:00:00","lt2019-12-31T20:00:00","eb2019-12-31T20:00:00"));
        assertSearchDoesntReturnSavedResource(queryParms);
        queryParms.put("dateTime", Arrays.asList("sa2019-12-31T19:59:59","gt2019-12-31T19:59:59","ge2019-12-31T20:00:00","le2019-12-31T20:00:00","lt2019-12-31T20:00:01","eb2019-12-31T20:00:01"));
        assertSearchReturnsSavedResource(queryParms);

        // "Period.start": "2018-10-29T17:12:00-04:00",
        // "Period.end":   "2018-10-29T17:18:00-04:00"
        queryParms.clear();
        queryParms.put("Period", Arrays.asList("ge2018-10-29T17:18:00","le2018-10-29T17:12:00"));
        assertSearchReturnsSavedResource(queryParms);
        queryParms.put("Period", Arrays.asList("ge2018-10-29T17:18:00","le2018-10-29T17:12:00","eb2018-10-29T17:18:00"));
        assertSearchDoesntReturnSavedResource(queryParms);
        queryParms.put("Period", Arrays.asList("ge2018-10-29T17:18:00","le2018-10-29T17:12:00","eb2018-10-29T17:18:01"));
        assertSearchReturnsSavedResource(queryParms);
        queryParms.put("Period", Arrays.asList("ge2018-10-29T17:18:00","le2018-10-29T17:12:00","lt2018-10-29T17:12:00"));
        assertSearchDoesntReturnSavedResource(queryParms);
        queryParms.put("Period", Arrays.asList("ge2018-10-29T17:18:00","le2018-10-29T17:12:00","lt2018-10-29T17:12:01"));
        assertSearchReturnsSavedResource(queryParms);
        queryParms.put("Period", Arrays.asList("gt2018-10-29T17:18:00","ge2018-10-29T17:18:00","le2018-10-29T17:12:00"));
        assertSearchDoesntReturnSavedResource(queryParms);
        queryParms.put("Period", Arrays.asList("gt2018-10-29T17:17:59","ge2018-10-29T17:18:00","le2018-10-29T17:12:00"));
        assertSearchReturnsSavedResource(queryParms);
        queryParms.put("Period", Arrays.asList("sa2018-10-29T17:12:00","ge2018-10-29T17:18:00","le2018-10-29T17:12:00"));
        assertSearchDoesntReturnSavedResource(queryParms);
        queryParms.put("Period", Arrays.asList("sa2018-10-29T17:11:59","ge2018-10-29T17:18:00","le2018-10-29T17:12:00"));
        assertSearchReturnsSavedResource(queryParms);
        queryParms.put("Period", Arrays.asList("gt2018-10-29T17:18:00","ge2018-10-29T17:18:00","le2018-10-29T17:12:00","lt2018-10-29T17:12:00"));
        assertSearchDoesntReturnSavedResource(queryParms);
        queryParms.put("Period", Arrays.asList("sa2018-10-29T17:12:00","ge2018-10-29T17:18:00","le2018-10-29T17:12:00","eb2018-10-29T17:18:00"));
        assertSearchDoesntReturnSavedResource(queryParms);
        queryParms.put("Period", Arrays.asList("sa2018-10-29T17:12:00","gt2018-10-29T17:18:00","lt2018-10-29T17:12:00","eb2018-10-29T17:18:00"));
        assertSearchDoesntReturnSavedResource(queryParms);
        queryParms.put("Period", Arrays.asList("sa2018-10-29T17:11:59","gt2018-10-29T17:17:59","ge2018-10-29T17:18:00","le2018-10-29T17:12:00","lt2018-10-29T17:12:01","eb2018-10-29T17:18:01"));
        assertSearchReturnsSavedResource(queryParms);
    }
}
