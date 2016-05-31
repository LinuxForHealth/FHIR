/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.core.test.mains;

import static com.ibm.watsonhealth.fhir.core.FHIRUtilities.createDuration;
import static com.ibm.watsonhealth.fhir.core.FHIRUtilities.formatTimestamp;
import static com.ibm.watsonhealth.fhir.core.FHIRUtilities.isDateTime;
import static com.ibm.watsonhealth.fhir.core.FHIRUtilities.isPartialDate;
import static com.ibm.watsonhealth.fhir.core.FHIRUtilities.parseDateTime;
import static com.ibm.watsonhealth.fhir.core.FHIRUtilities.setDefaults;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

public class DateTestMain {
    public static void main(String[] args) throws Exception {
        List<XMLGregorianCalendar> calendars = Arrays.asList(
            parseDateTime("2015-12-31T20:00:00-04:00", false),   // timezone other than GMT
            parseDateTime("2016-01-01", false),                  // date
            parseDateTime("2016-01", false),                     // year month
            parseDateTime("2016", false)                         // year
        );
        for (XMLGregorianCalendar calendar : calendars) {
            if (isDateTime(calendar)) {
                // fully-specified ISO 6801 date/time
                Date date = calendar.toGregorianCalendar().getTime();
                System.out.println("date in milliseconds: " + date.getTime());
                System.out.println("date: " + formatTimestamp(date));
                System.out.println("p.valueDate = '" + formatTimestamp(date) + "'");
                System.out.println("");
            } else if (isPartialDate(calendar)) {
                // partial date
                Duration duration = createDuration(calendar);   // amount to add
                setDefaults(calendar);  // set defaults
                
                Date start = calendar.toGregorianCalendar().getTime();
                System.out.println("start time in milliseconds: " + start.getTime());
                System.out.println("start: " + formatTimestamp(start));
                
                calendar.add(duration); // add duration
                
                Date end = calendar.toGregorianCalendar().getTime();
                System.out.println("end time in milliseconds: " + end.getTime());
                System.out.println("end:   " + formatTimestamp(end));
                System.out.println("p.valueDate >= '" + formatTimestamp(start) + "' AND p.valueDate < '" + formatTimestamp(end) + "'");
                System.out.println("");
            }
        }
    }
}
