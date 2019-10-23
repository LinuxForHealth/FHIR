/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.common;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.Date;

import org.testng.annotations.Test;

import com.ibm.fhir.database.utils.api.DataAccessException;

/**
 * Tests the behaviors related to date and time. 
 */
public class DateMathTest {

    @Test(enabled=true)
    public void testDay() {
        String s1 = "2017-01-01";
        Date d1 = DateMath.parse(s1);
        assertEquals(s1, DateMath.format(d1));
        
        String s2 = "2017-01-02";
        Date d2 = DateMath.parse(s2);
        
        assertEquals(d2, DateMath.addDays(d1, 1));
    }
    
    @Test(enabled=true)
    public void testMonth() {
        String s1 = "2017-01-01";
        Date d1 = DateMath.parse(s1);
        assertEquals(s1, DateMath.format(d1));
        
        String s2 = "2017-02-01";
        Date d2 = DateMath.parse(s2);
        
        assertEquals(d2, DateMath.addMonths(d1, 1));
    }
    
    @Test(enabled=true)
    public void testFormat2() {
        final String s1 = "2017-01-01";
        final String s2 = "20170101";
        Date d1 = DateMath.parse(s1);
        assertEquals(s2, DateMath.format2(d1));
    }
    
    @Test(enabled=true, expectedExceptions=DataAccessException.class)
    public void testError() {
        final String s1 = "2017/01/01";
        DateMath.parse(s1);
        fail("Expected exception for " + s1);
    }
    
    @Test(enabled=true)
    public void testTrunc() {
        final String s01 = "2017-01-01";
        final String s14 = "2017-01-14";
        
        Date d01 = DateMath.parse(s01);
        Date d14 = DateMath.parse(s14);
        
        assertEquals(DateMath.truncateToMonth(d14), d01);
    }
}
