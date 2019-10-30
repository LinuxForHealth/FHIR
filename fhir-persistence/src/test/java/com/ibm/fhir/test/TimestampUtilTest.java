/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.test;

import static org.testng.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.TimeZone;

import org.testng.annotations.Test;

import com.ibm.fhir.persistence.util.TimestampUtil;

/**
 * Unit test for {@link TimestampUtil}

 *
 */
public class TimestampUtilTest {
    
    @Test
    public void testConversion() {
        
        final String tstamp000 = "2017-05-10T13:01:01.000Z";
        final String tstamp001 = "2017-05-10T13:01:01.001Z";
        final String tstamp002 = "2017-01-01T00:00:00.000Z";
        final String tstamp003 = "2012-09-17";
        final String tstamp004 = "2017-09-01T12:00:00.000-04:00";
        final String tstamp005 = "2017-09-01T12:00:00.000";
        final String tstamp999 = "2017-12-31T23:59:59.999Z";

        final TimestampUtil util = TimestampUtil.create();
        Date d000 = util.getTimestamp(tstamp000);
        Date d001 = util.getTimestamp(tstamp001);
        Date d002 = util.getTimestamp(tstamp002);
        Date d003 = util.getTimestamp(tstamp003);
        Date d004 = util.getTimestamp(tstamp004);
        Date d005 = util.getTimestamp(tstamp005);
        Date d999 = util.getTimestamp(tstamp999);
        
        final String test000 = util.format(d000);
        final String test001 = util.format(d001);
        final String test002 = util.format(d002);
        final String test003 = util.format(d003);
        final String test004 = util.format(d004);
        final String test005 = util.format(d005);
        final String test999 = util.format(d999);
        
        assertEquals(tstamp000, test000);
        assertEquals(tstamp001, test001);
        assertEquals(tstamp002, test002);
        assertEquals(tstamp003.concat("T00:00:00.000Z"), test003);
        assertEquals(OffsetDateTime.parse(tstamp004).toInstant().toString(), 
                OffsetDateTime.parse(test004).toInstant().toString()); //converted to UTC
        assertEquals(tstamp005 + "Z", test005); //include Z as default offset
        assertEquals(tstamp999, test999);
    }
    
    @Test
    public void testFormat() {
        final String str = "2017-05-28T17:23:34.427Z";
        final TimestampUtil util = TimestampUtil.create();
        Date d = util.getTimestamp(str);
        
        // Make sure we got the right date by cross-checking with
        // the legacy SimpleDateFormat stuff
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        final String out = df.format(d);
        assertEquals(out, str);
    }
}
