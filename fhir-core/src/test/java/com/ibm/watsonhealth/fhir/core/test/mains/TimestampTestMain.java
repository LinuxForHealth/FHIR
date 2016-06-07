/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.core.test.mains;

import java.sql.Timestamp;
import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

import com.ibm.watsonhealth.fhir.core.FHIRUtilities;

public class TimestampTestMain {
    public static void main(String[] args) throws Exception {
        XMLGregorianCalendar calendar = FHIRUtilities.parseDateTime("1944-08-11", true);
        System.out.println(calendar);
        
        Timestamp timestamp = new Timestamp(calendar.toGregorianCalendar().getTimeInMillis());       
        System.out.println(timestamp);
        System.out.println(timestamp.getTime());
        
        timestamp = Timestamp.valueOf(FHIRUtilities.formatTimestamp(calendar.toGregorianCalendar().getTime()));
        System.out.println(timestamp);
        System.out.println(timestamp.getTime());
        
        calendar = FHIRUtilities.parseDateTime("1944-08-11", false);
        Date time = calendar.toGregorianCalendar().getTime();
        System.out.println("partial date (not normalized): " + FHIRUtilities.formatTimestamp(time));
        
        FHIRUtilities.setDefaults(calendar);
        
        time = calendar.toGregorianCalendar().getTime();
        System.out.println("partial date (normalized): " + FHIRUtilities.formatTimestamp(time));
        
        calendar = FHIRUtilities.parseDateTime("1944-08-10T20:00:00-04:00", false);
        System.out.println(FHIRUtilities.formatTimestamp(calendar.toGregorianCalendar().getTime()));
        calendar = calendar.normalize();
        System.out.println(FHIRUtilities.formatTimestamp(calendar.toGregorianCalendar().getTime()));
        
        timestamp = FHIRUtilities.convertToTimestamp(calendar);
        System.out.println(timestamp);
        
        System.out.println(FHIRUtilities.formatCalendar(timestamp));
    }
}
