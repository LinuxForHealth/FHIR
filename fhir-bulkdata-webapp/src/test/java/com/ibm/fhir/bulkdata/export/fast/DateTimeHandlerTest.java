/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.export.fast;

import static org.testng.Assert.assertEquals;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.TemporalAccessor;

import org.testng.annotations.Test;

import com.ibm.fhir.search.date.DateTimeHandler;

/**
 * Unit tests for date-time processing related to export scenarios
 */
public class DateTimeHandlerTest {

    @Test
    public void testZ() throws Exception {
        final String dateStr = "2021-01-22T00:00:00Z";
        TemporalAccessor ta = DateTimeHandler.parse(dateStr);
        Instant inst = DateTimeHandler.generateValue(ta);

        // Check the instant matches our expectation
        assertEquals(inst.toString(), "2021-01-22T00:00:00Z");
    }

    @Test
    public void testTimezoneOffset0() throws Exception {
        final String dateStr = "2021-01-22T00:00:00-00:00";
        TemporalAccessor ta = DateTimeHandler.parse(dateStr);
        Instant inst = DateTimeHandler.generateValue(ta);

        // Check the instant matches our expectation
        assertEquals(inst.toString(), "2021-01-22T00:00:00Z");
    }

    @Test
    public void testTimestamp() throws Exception {
        final String dateStr = "2021-01-22T00:00:00Z";
        TemporalAccessor ta = DateTimeHandler.parse(dateStr);
        Instant inst = DateTimeHandler.generateValue(ta);

        // convert to a timestamp
        Timestamp ts = Timestamp.from(inst);
        System.out.println("timestamp value: " + ts);
    }

}