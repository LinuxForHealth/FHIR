/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.util;

import static org.testng.Assert.assertEquals;

import java.time.Instant;

import org.testng.annotations.Test;

import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.persistence.jdbc.util.QueryBuilderUtil;

/**
 * Unit test for parameter DateTime utilities provided by {@link QueryBuilderUtil}
 * @author rarnold
 *
 */
public class QueryBuilderUtilTest {

    @Test
    public void testDateTimeFull() {
        Instant startRef = Instant.parse("2018-01-01T00:00:00Z");
        DateTime dt = DateTime.builder().value("2018-01-01T00:00:00Z").build();

        Instant start = Instant.from(dt.getValue());
        assertEquals(startRef, start, "getStart failed");
        
    }
    
    @Test
    public void testDateTimeFull2() {
        Instant startRef = Instant.parse("2019-01-01T00:00:00Z");
        DateTime dt = DateTime.builder().value("2019-01-01T00:00:00Z").build();

        Instant inst = QueryBuilderUtil.getInstant(dt);
        assertEquals(startRef, inst, "getInstant failed");
    }

    @Test
    public void testDateTimePartial() {
        Instant startRef = Instant.parse("2019-01-01T00:00:00Z");
        DateTime dt = DateTime.builder().value("2019-01-01").build();

        // getInstant should know what to do with a partial DateTime
        Instant inst = QueryBuilderUtil.getInstant(dt);
        assertEquals(startRef, inst, "getInstant failed");
    }

    
    @Test
    public void testDateTimeDay() {
        Instant startRef = Instant.parse("2018-01-01T00:00:00Z");
        Instant endRef = Instant.parse("2018-01-02T00:00:00Z");
        
        DateTime dt = DateTime.builder().value("2018-01-01").build();
        Instant start = QueryBuilderUtil.getStart(dt);
        assertEquals(startRef, start, "getStart failed");
        
        Instant end = QueryBuilderUtil.getEnd(dt);
        assertEquals(endRef, end, "getEnd failed");
    }
    
    @Test
    public void testDateTimeYearMonth() {
        Instant startRef = Instant.parse("2018-01-01T00:00:00Z");
        Instant endRef = Instant.parse("2018-02-01T00:00:00Z");
        
        DateTime dt = DateTime.builder().value("2018-01").build();        
        Instant start = QueryBuilderUtil.getStart(dt);
        assertEquals(startRef, start, "getStart failed");
        
        Instant end = QueryBuilderUtil.getEnd(dt);
        assertEquals(endRef, end, "getEnd failed");
    }
    
    @Test
    public void testDateTimeYear() {
        Instant startRef = Instant.parse("2018-01-01T00:00:00Z");
        Instant endRef = Instant.parse("2019-01-01T00:00:00Z");

        DateTime dt = DateTime.builder().value("2018").build();
        Instant start = QueryBuilderUtil.getStart(dt);
        assertEquals(startRef, start, "getStart failed");

        Instant end = QueryBuilderUtil.getEnd(dt);
        assertEquals(endRef, end, "getEnd failed");
    }
    
    
    @Test
    public void testDateDay() {
        Instant startRef = Instant.parse("2018-01-01T00:00:00Z");
        Instant endRef = Instant.parse("2018-01-02T00:00:00Z");
        
        Date date = Date.builder().value("2018-01-01").build();
        Instant start = QueryBuilderUtil.getStart(date);
        assertEquals(startRef, start, "getStart failed");
        
        Instant end = QueryBuilderUtil.getEnd(date);
        assertEquals(endRef, end, "getEnd failed");
    }
    
    @Test
    public void testDateYearMonth() {
        Instant startRef = Instant.parse("2018-01-01T00:00:00Z");
        Instant endRef = Instant.parse("2018-02-01T00:00:00Z");
        
        Date date = Date.builder().value("2018-01").build();        
        Instant start = QueryBuilderUtil.getStart(date);
        assertEquals(startRef, start, "getStart failed");
        
        Instant end = QueryBuilderUtil.getEnd(date);
        assertEquals(endRef, end, "getEnd failed");
    }
    
    @Test
    public void testDateYear() {
        Instant startRef = Instant.parse("2018-01-01T00:00:00Z");
        Instant endRef = Instant.parse("2019-01-01T00:00:00Z");

        Date date = Date.builder().value("2018").build();
        Instant start = QueryBuilderUtil.getStart(date);
        assertEquals(startRef, start, "getStart failed");

        Instant end = QueryBuilderUtil.getEnd(date);
        assertEquals(endRef, end, "getEnd failed");
    }

}
