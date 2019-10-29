/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.core.test;

import static org.testng.Assert.assertTrue;

import java.util.Date;

import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRUtilities;

public class TimestampTest {
    
    @Test
    public void test1() throws Exception {
        
        String strTimeStamp = FHIRUtilities.formatTimestamp(new Date(System.currentTimeMillis()));
        //Check if the date format is "yyyy-MM-dd HH:mm:ss.SSS"
        assertTrue(strTimeStamp.matches("([0-9]{4})-([0-9]{2})-([0-9]{2}) ([0-9]{2}):([0-9]{2}):([0-9]{2}).([0-9]{3})"));


    }
}
