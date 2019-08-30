/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.core.test.mains;

import static com.ibm.watson.health.fhir.core.FHIRUtilities.*;
import static org.testng.AssertJUnit.*;

import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

import org.testng.annotations.Test;

import com.ibm.watson.health.fhir.core.FHIRUtilities;

public class DateTestMain {
    // TODO remove when we are happy we can permanently delete the FHIRUtilities methods under test
//    public static void main(String[] args) throws Exception {
//        List<XMLGregorianCalendar> calendars = Arrays.asList(
//            parseDateTime("2015-12-31T20:00:00-04:00", false),   // timezone other than GMT
//            parseDateTime("2015-12-31T20:00:00-05:00", false),   // different timezone other than GMT
//            parseDateTime("2016-01-01", false),                  // date
//            parseDateTime("2016-01", false),                     // year month
//            parseDateTime("2016", false)                         // year
//        );
//        for (XMLGregorianCalendar calendar : calendars) {
//            if (isDateTime(calendar)) {
//                // fully-specified ISO 6801 date/time
//                Date date = calendar.toGregorianCalendar().getTime();
//                System.out.println("date in milliseconds: " + date.getTime());
//                System.out.println("date: " + formatTimestamp(date));
//                System.out.println("p.valueDate = '" + formatTimestamp(date) + "'");
//                System.out.println("timestamp: " + FHIRUtilities.convertToTimestamp(calendar));
//                System.out.println("");
//                
//            } else if (isPartialDate(calendar)) {
//                // partial date
//                System.out.println("raw timestamp: " + FHIRUtilities.convertToTimestamp(calendar));
//                Duration duration = createDuration(calendar);   // amount to add
//                setDefaults(calendar);  // set defaults
//                
//                Date start = calendar.toGregorianCalendar().getTime();
//                System.out.println("start time in milliseconds: " + start.getTime());
//                System.out.println("start: " + formatTimestamp(start));
//                System.out.println("start timestamp: " + FHIRUtilities.convertToTimestamp(calendar));
//                calendar.add(duration); // add duration
//                
//                Date end = calendar.toGregorianCalendar().getTime();
//                Date end2 = new Date(calendar.toGregorianCalendar().getTimeInMillis() - 1);
//                System.out.println("end time in milliseconds: " + (end.getTime()));
//                System.out.println("end:   " + formatTimestamp(end));
//                System.out.println("end2:  " + formatTimestamp(end2));
//                System.out.println("p.valueDate >= '" + formatTimestamp(start) + "' AND p.valueDate < '" + formatTimestamp(end) + "'");
//                System.out.println("p.valueDate >= '" + formatTimestamp(start) + "' AND p.valueDate < '" + formatTimestamp(end2) + "'");
//                System.out.println("end timestamp: " + FHIRUtilities.convertToTimestamp(calendar));
//                
//                System.out.println("");
//            }
//        }
//        
//        GregorianCalendar calendar = new GregorianCalendar();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        XMLGregorianCalendar xcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
//        System.out.println(xcal.toXMLFormat());
//    }
//    
//    @Test
//    public void testParseDateTime() {
//        testInput("2016-10-27", false);
//        testInput("asd872947238", true);
//        testInput("!@#$!&@(#-10-27", true);
//        testInput("2008-08-30T01:45:36.123Z", false);
//    }
//
//    private void testInput(String dateTimeString, boolean expectsException) {
//        try {
//            FHIRUtilities.parseDateTime(dateTimeString, true);
//            if (expectsException) {
//                fail("Didn't throw expected exception.");
//            }
//        } catch (IllegalArgumentException e) {
//            if (!expectsException) {
//                throw e;
//            }
//        }
//    }
}
