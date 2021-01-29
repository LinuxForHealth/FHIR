/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.date;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.time.Instant;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.TimeZone;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.search.SearchConstants.Prefix;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.parameters.QueryParameterValue;

public class DateTimeHandlerTest {

    @BeforeClass
    public void setSystemTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Test
    public void testYearParser() throws FHIRSearchException {
        TemporalAccessor acc = DateTimeHandler.parse("2019");
        assertNotNull(acc);
        assertTrue(acc.isSupported(ChronoField.YEAR));
        assertEquals(acc.getLong(ChronoField.YEAR), 2019);
        assertFalse(acc.isSupported(ChronoField.MONTH_OF_YEAR));
        assertFalse(acc.isSupported(ChronoField.DAY_OF_MONTH));
        assertFalse(acc.isSupported(ChronoField.HOUR_OF_DAY));
        assertFalse(acc.isSupported(ChronoField.MINUTE_OF_HOUR));
        assertFalse(acc.isSupported(ChronoField.SECOND_OF_MINUTE));
        assertFalse(acc.isSupported(ChronoField.MILLI_OF_SECOND));
        assertFalse(acc.isSupported(ChronoField.MICRO_OF_SECOND));
        assertFalse(acc.isSupported(ChronoField.NANO_OF_SECOND));
    }

    @Test
    public void testYearMonthParser() throws FHIRSearchException {
        TemporalAccessor acc = DateTimeHandler.parse("2019-10");
        assertNotNull(acc);
        assertTrue(acc.isSupported(ChronoField.YEAR));
        assertEquals(acc.getLong(ChronoField.YEAR), 2019);
        assertTrue(acc.isSupported(ChronoField.MONTH_OF_YEAR));
        assertEquals(acc.getLong(ChronoField.MONTH_OF_YEAR), 10);
        assertFalse(acc.isSupported(ChronoField.DAY_OF_MONTH));
        assertFalse(acc.isSupported(ChronoField.HOUR_OF_DAY));
        assertFalse(acc.isSupported(ChronoField.MINUTE_OF_HOUR));
        assertFalse(acc.isSupported(ChronoField.SECOND_OF_MINUTE));
        assertFalse(acc.isSupported(ChronoField.MILLI_OF_SECOND));
        assertFalse(acc.isSupported(ChronoField.MICRO_OF_SECOND));
        assertFalse(acc.isSupported(ChronoField.NANO_OF_SECOND));
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testYearMonthParserBadNoDash() throws FHIRSearchException {
        DateTimeHandler.parse("201910");
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testYearMonthParserBadYear() throws FHIRSearchException {
        DateTimeHandler.parse("-1010");
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testYearMonthParserGoodYearBadMonth() throws FHIRSearchException {
        DateTimeHandler.parse("1010-XX");
    }

    @Test
    public void testYearMonthDayParser() throws FHIRSearchException {
        TemporalAccessor acc = DateTimeHandler.parse("2019-10-11");
        assertNotNull(acc);
        assertTrue(acc.isSupported(ChronoField.YEAR));
        assertEquals(acc.getLong(ChronoField.YEAR), 2019);
        assertTrue(acc.isSupported(ChronoField.MONTH_OF_YEAR));
        assertEquals(acc.getLong(ChronoField.MONTH_OF_YEAR), 10);
        assertTrue(acc.isSupported(ChronoField.DAY_OF_MONTH));
        assertEquals(acc.getLong(ChronoField.DAY_OF_MONTH), 11);
        assertFalse(acc.isSupported(ChronoField.HOUR_OF_DAY));
        assertFalse(acc.isSupported(ChronoField.MINUTE_OF_HOUR));
        assertFalse(acc.isSupported(ChronoField.SECOND_OF_MINUTE));
        assertFalse(acc.isSupported(ChronoField.MILLI_OF_SECOND));
        assertFalse(acc.isSupported(ChronoField.MICRO_OF_SECOND));
        assertFalse(acc.isSupported(ChronoField.NANO_OF_SECOND));
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testYearMonthDayParserThirtySecondDay() throws FHIRSearchException {
        DateTimeHandler.parse("2019-10-32");
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testYearMonthDayParserMissingDash() throws FHIRSearchException {
        DateTimeHandler.parse("2019-1030");
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testYearMonthDayParserInvalidValue() throws FHIRSearchException {
        DateTimeHandler.parse("2019-10-3A");
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testYearMonthDayParserInvalidSeparator() throws FHIRSearchException {
        DateTimeHandler.parse("2019-10-30X");
    }

    @Test
    public void testYearMonthDayParserWithSeparator() throws FHIRSearchException {
        TemporalAccessor acc = DateTimeHandler.parse("2019-10-11T");
        assertNotNull(acc);
        assertTrue(acc.isSupported(ChronoField.YEAR));
        assertEquals(acc.getLong(ChronoField.YEAR), 2019);
        assertTrue(acc.isSupported(ChronoField.MONTH_OF_YEAR));
        assertEquals(acc.getLong(ChronoField.MONTH_OF_YEAR), 10);
        assertTrue(acc.isSupported(ChronoField.DAY_OF_MONTH));
        assertEquals(acc.getLong(ChronoField.DAY_OF_MONTH), 11);
        assertFalse(acc.isSupported(ChronoField.HOUR_OF_DAY));
        assertFalse(acc.isSupported(ChronoField.MINUTE_OF_HOUR));
        assertFalse(acc.isSupported(ChronoField.SECOND_OF_MINUTE));
        assertFalse(acc.isSupported(ChronoField.MILLI_OF_SECOND));
        assertFalse(acc.isSupported(ChronoField.MICRO_OF_SECOND));
        assertFalse(acc.isSupported(ChronoField.NANO_OF_SECOND));
    }

    @Test
    public void testYearMonthDaySeparatorParserHour() throws FHIRSearchException {
        TemporalAccessor acc = DateTimeHandler.parse("2019-10-11T01");
        assertNotNull(acc);
        assertTrue(acc.isSupported(ChronoField.YEAR));
        assertEquals(acc.getLong(ChronoField.YEAR), 2019);
        assertTrue(acc.isSupported(ChronoField.MONTH_OF_YEAR));
        assertEquals(acc.getLong(ChronoField.MONTH_OF_YEAR), 10);
        assertTrue(acc.isSupported(ChronoField.DAY_OF_MONTH));
        assertEquals(acc.getLong(ChronoField.DAY_OF_MONTH), 11);
        assertTrue(acc.isSupported(ChronoField.HOUR_OF_DAY));
        assertEquals(acc.getLong(ChronoField.HOUR_OF_DAY), 1);
        assertTrue(acc.isSupported(ChronoField.MINUTE_OF_HOUR));
        assertEquals(acc.getLong(ChronoField.MINUTE_OF_HOUR), 0);
        assertTrue(acc.isSupported(ChronoField.SECOND_OF_MINUTE));
        assertEquals(acc.getLong(ChronoField.SECOND_OF_MINUTE), 0);
        assertTrue(acc.isSupported(ChronoField.MILLI_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.MILLI_OF_SECOND), 0);
        assertTrue(acc.isSupported(ChronoField.MICRO_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.MICRO_OF_SECOND), 0);
        assertTrue(acc.isSupported(ChronoField.NANO_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.NANO_OF_SECOND), 0);

        acc = DateTimeHandler.parse("2019-10-11T21");
        assertNotNull(acc);
        assertTrue(acc.isSupported(ChronoField.YEAR));
        assertEquals(acc.getLong(ChronoField.YEAR), 2019);
        assertTrue(acc.isSupported(ChronoField.MONTH_OF_YEAR));
        assertEquals(acc.getLong(ChronoField.MONTH_OF_YEAR), 10);
        assertTrue(acc.isSupported(ChronoField.DAY_OF_MONTH));
        assertEquals(acc.getLong(ChronoField.DAY_OF_MONTH), 11);
        assertTrue(acc.isSupported(ChronoField.HOUR_OF_DAY));
        assertEquals(acc.getLong(ChronoField.HOUR_OF_DAY), 21);
        assertTrue(acc.isSupported(ChronoField.MINUTE_OF_HOUR));
        assertEquals(acc.getLong(ChronoField.MINUTE_OF_HOUR), 0);
        assertTrue(acc.isSupported(ChronoField.SECOND_OF_MINUTE));
        assertEquals(acc.getLong(ChronoField.SECOND_OF_MINUTE), 0);
        assertTrue(acc.isSupported(ChronoField.MILLI_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.MILLI_OF_SECOND), 0);
        assertTrue(acc.isSupported(ChronoField.MICRO_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.MICRO_OF_SECOND), 0);
        assertTrue(acc.isSupported(ChronoField.NANO_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.NANO_OF_SECOND), 0);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testYearMonthDaySeparatorParserHourInvalidNumber() throws FHIRSearchException {
        DateTimeHandler.parse("2019-10-30T2");
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testYearMonthDaySeparatorParserHourInvalidNumberHighTwoDigits() throws FHIRSearchException {
        DateTimeHandler.parse("2019-10-30T29");
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testYearMonthDaySeparatorParserHourInvalidNumberHighThreeDigits() throws FHIRSearchException {
        DateTimeHandler.parse("2019-10-30T663");
    }

    @Test
    public void testYearMonthDaySeparatorHourParserMinutes() throws FHIRSearchException {
        TemporalAccessor acc = DateTimeHandler.parse("2019-10-11T01:10");
        assertNotNull(acc);
        assertTrue(acc.isSupported(ChronoField.YEAR));
        assertEquals(acc.getLong(ChronoField.YEAR), 2019);
        assertTrue(acc.isSupported(ChronoField.MONTH_OF_YEAR));
        assertEquals(acc.getLong(ChronoField.MONTH_OF_YEAR), 10);
        assertTrue(acc.isSupported(ChronoField.DAY_OF_MONTH));
        assertEquals(acc.getLong(ChronoField.DAY_OF_MONTH), 11);
        assertTrue(acc.isSupported(ChronoField.HOUR_OF_DAY));
        assertEquals(acc.getLong(ChronoField.HOUR_OF_DAY), 1);
        assertTrue(acc.isSupported(ChronoField.MINUTE_OF_HOUR));
        assertEquals(acc.getLong(ChronoField.MINUTE_OF_HOUR), 10);
        assertTrue(acc.isSupported(ChronoField.SECOND_OF_MINUTE));
        assertEquals(acc.getLong(ChronoField.SECOND_OF_MINUTE), 0);
        assertTrue(acc.isSupported(ChronoField.MILLI_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.MILLI_OF_SECOND), 0);
        assertTrue(acc.isSupported(ChronoField.MICRO_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.MICRO_OF_SECOND), 0);
        assertTrue(acc.isSupported(ChronoField.NANO_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.NANO_OF_SECOND), 0);

        acc = DateTimeHandler.parse("2019-10-11T21:10");
        assertNotNull(acc);
        assertTrue(acc.isSupported(ChronoField.YEAR));
        assertEquals(acc.getLong(ChronoField.YEAR), 2019);
        assertTrue(acc.isSupported(ChronoField.MONTH_OF_YEAR));
        assertEquals(acc.getLong(ChronoField.MONTH_OF_YEAR), 10);
        assertTrue(acc.isSupported(ChronoField.DAY_OF_MONTH));
        assertEquals(acc.getLong(ChronoField.DAY_OF_MONTH), 11);
        assertTrue(acc.isSupported(ChronoField.HOUR_OF_DAY));
        assertEquals(acc.getLong(ChronoField.HOUR_OF_DAY), 21);
        assertTrue(acc.isSupported(ChronoField.MINUTE_OF_HOUR));
        assertEquals(acc.getLong(ChronoField.MINUTE_OF_HOUR), 10);
        assertTrue(acc.isSupported(ChronoField.SECOND_OF_MINUTE));
        assertEquals(acc.getLong(ChronoField.SECOND_OF_MINUTE), 0);
        assertTrue(acc.isSupported(ChronoField.MILLI_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.MILLI_OF_SECOND), 0);
        assertTrue(acc.isSupported(ChronoField.MICRO_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.MICRO_OF_SECOND), 0);
        assertTrue(acc.isSupported(ChronoField.NANO_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.NANO_OF_SECOND), 0);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testYearMonthDaySeparatorHourParserMinutesBadNumber() throws FHIRSearchException {
        DateTimeHandler.parse("2019-10-11T01:1");
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testYearMonthDaySeparatorHourParserMinutesBadNegative() throws FHIRSearchException {
        DateTimeHandler.parse("2019-10-11T01:-1");
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testYearMonthDaySeparatorHourParserMinutesBadHigh() throws FHIRSearchException {
        DateTimeHandler.parse("2019-10-11T01:199");
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testYearMonthDaySeparatorHourParserMinutesBadTwoDigit() throws FHIRSearchException {
        DateTimeHandler.parse("2019-10-11T01:60");
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testYearMonthDaySeparatorHourParserMinutesBadSeparator() throws FHIRSearchException {
        DateTimeHandler.parse("2019-10-11T01*60");
    }

    @Test
    public void testYearMonthDaySeparatorHourMinutesParserSeconds() throws FHIRSearchException {
        TemporalAccessor acc = DateTimeHandler.parse("2019-10-11T01:10:00");
        assertNotNull(acc);
        assertTrue(acc.isSupported(ChronoField.YEAR));
        assertEquals(acc.getLong(ChronoField.YEAR), 2019);
        assertTrue(acc.isSupported(ChronoField.MONTH_OF_YEAR));
        assertEquals(acc.getLong(ChronoField.MONTH_OF_YEAR), 10);
        assertTrue(acc.isSupported(ChronoField.DAY_OF_MONTH));
        assertEquals(acc.getLong(ChronoField.DAY_OF_MONTH), 11);
        assertTrue(acc.isSupported(ChronoField.HOUR_OF_DAY));
        assertEquals(acc.getLong(ChronoField.HOUR_OF_DAY), 1);
        assertTrue(acc.isSupported(ChronoField.MINUTE_OF_HOUR));
        assertEquals(acc.getLong(ChronoField.MINUTE_OF_HOUR), 10);
        assertTrue(acc.isSupported(ChronoField.SECOND_OF_MINUTE));
        assertEquals(acc.getLong(ChronoField.SECOND_OF_MINUTE), 0);
        assertTrue(acc.isSupported(ChronoField.MILLI_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.MILLI_OF_SECOND), 0);
        assertTrue(acc.isSupported(ChronoField.MICRO_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.MICRO_OF_SECOND), 0);
        assertTrue(acc.isSupported(ChronoField.NANO_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.NANO_OF_SECOND), 0);

        acc = DateTimeHandler.parse("2019-10-11T21:10:59");
        assertNotNull(acc);
        assertTrue(acc.isSupported(ChronoField.YEAR));
        assertEquals(acc.getLong(ChronoField.YEAR), 2019);
        assertTrue(acc.isSupported(ChronoField.MONTH_OF_YEAR));
        assertEquals(acc.getLong(ChronoField.MONTH_OF_YEAR), 10);
        assertTrue(acc.isSupported(ChronoField.DAY_OF_MONTH));
        assertEquals(acc.getLong(ChronoField.DAY_OF_MONTH), 11);
        assertTrue(acc.isSupported(ChronoField.HOUR_OF_DAY));
        assertEquals(acc.getLong(ChronoField.HOUR_OF_DAY), 21);
        assertTrue(acc.isSupported(ChronoField.MINUTE_OF_HOUR));
        assertEquals(acc.getLong(ChronoField.MINUTE_OF_HOUR), 10);
        assertTrue(acc.isSupported(ChronoField.SECOND_OF_MINUTE));
        assertEquals(acc.getLong(ChronoField.SECOND_OF_MINUTE), 59);
        assertTrue(acc.isSupported(ChronoField.MILLI_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.MILLI_OF_SECOND), 0);
        assertTrue(acc.isSupported(ChronoField.MICRO_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.MICRO_OF_SECOND), 0);
        assertTrue(acc.isSupported(ChronoField.NANO_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.NANO_OF_SECOND), 0);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testYearMonthDaySeparatorHourMinutesParserSecondsBadSeparator() throws FHIRSearchException {
        DateTimeHandler.parse("2019-10-11T01:30|99");
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testYearMonthDaySeparatorHourMinutesParserSecondsHigh() throws FHIRSearchException {
        DateTimeHandler.parse("2019-10-11T01:30:99");
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testYearMonthDaySeparatorHourMinutesParserSecondsNegative() throws FHIRSearchException {
        DateTimeHandler.parse("2019-10-11T01:30:-1");
    }

    @Test
    public void testYearMonthDaySeparatorHourMinutesSecondsParserMicroseconds() throws FHIRSearchException {
        TemporalAccessor acc = DateTimeHandler.parse("2019-10-11T01:10:00.000002");
        assertNotNull(acc);
        assertTrue(acc.isSupported(ChronoField.YEAR));
        assertEquals(acc.getLong(ChronoField.YEAR), 2019);
        assertTrue(acc.isSupported(ChronoField.MONTH_OF_YEAR));
        assertEquals(acc.getLong(ChronoField.MONTH_OF_YEAR), 10);
        assertTrue(acc.isSupported(ChronoField.DAY_OF_MONTH));
        assertEquals(acc.getLong(ChronoField.DAY_OF_MONTH), 11);
        assertTrue(acc.isSupported(ChronoField.HOUR_OF_DAY));
        assertEquals(acc.getLong(ChronoField.HOUR_OF_DAY), 1);
        assertTrue(acc.isSupported(ChronoField.MINUTE_OF_HOUR));
        assertEquals(acc.getLong(ChronoField.MINUTE_OF_HOUR), 10);
        assertTrue(acc.isSupported(ChronoField.SECOND_OF_MINUTE));
        assertEquals(acc.getLong(ChronoField.SECOND_OF_MINUTE), 0);
        assertTrue(acc.isSupported(ChronoField.MILLI_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.MILLI_OF_SECOND), 0);
        assertTrue(acc.isSupported(ChronoField.MICRO_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.MICRO_OF_SECOND), 2);
        assertTrue(acc.isSupported(ChronoField.NANO_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.NANO_OF_SECOND), 2000);

        acc = DateTimeHandler.parse("2019-10-11T21:10:59.999992");
        assertNotNull(acc);
        assertTrue(acc.isSupported(ChronoField.YEAR));
        assertEquals(acc.getLong(ChronoField.YEAR), 2019);
        assertTrue(acc.isSupported(ChronoField.MONTH_OF_YEAR));
        assertEquals(acc.getLong(ChronoField.MONTH_OF_YEAR), 10);
        assertTrue(acc.isSupported(ChronoField.DAY_OF_MONTH));
        assertEquals(acc.getLong(ChronoField.DAY_OF_MONTH), 11);
        assertTrue(acc.isSupported(ChronoField.HOUR_OF_DAY));
        assertEquals(acc.getLong(ChronoField.HOUR_OF_DAY), 21);
        assertTrue(acc.isSupported(ChronoField.MINUTE_OF_HOUR));
        assertEquals(acc.getLong(ChronoField.MINUTE_OF_HOUR), 10);
        assertTrue(acc.isSupported(ChronoField.SECOND_OF_MINUTE));
        assertEquals(acc.getLong(ChronoField.SECOND_OF_MINUTE), 59);
        assertTrue(acc.isSupported(ChronoField.MILLI_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.MILLI_OF_SECOND), 999);
        assertTrue(acc.isSupported(ChronoField.MICRO_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.MICRO_OF_SECOND), 999992);
        assertTrue(acc.isSupported(ChronoField.NANO_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.NANO_OF_SECOND), 999992000);

        acc = DateTimeHandler.parse("2019-10-11T21:10:59.9");
        assertNotNull(acc);
        assertTrue(acc.isSupported(ChronoField.YEAR));
        assertEquals(acc.getLong(ChronoField.YEAR), 2019);
        assertTrue(acc.isSupported(ChronoField.MONTH_OF_YEAR));
        assertEquals(acc.getLong(ChronoField.MONTH_OF_YEAR), 10);
        assertTrue(acc.isSupported(ChronoField.DAY_OF_MONTH));
        assertEquals(acc.getLong(ChronoField.DAY_OF_MONTH), 11);
        assertTrue(acc.isSupported(ChronoField.HOUR_OF_DAY));
        assertEquals(acc.getLong(ChronoField.HOUR_OF_DAY), 21);
        assertTrue(acc.isSupported(ChronoField.MINUTE_OF_HOUR));
        assertEquals(acc.getLong(ChronoField.MINUTE_OF_HOUR), 10);
        assertTrue(acc.isSupported(ChronoField.SECOND_OF_MINUTE));
        assertEquals(acc.getLong(ChronoField.SECOND_OF_MINUTE), 59);
        assertTrue(acc.isSupported(ChronoField.MILLI_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.MILLI_OF_SECOND), 900);
        assertTrue(acc.isSupported(ChronoField.MICRO_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.MICRO_OF_SECOND), 900000);
        assertTrue(acc.isSupported(ChronoField.NANO_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.NANO_OF_SECOND), 900000000);

        acc = DateTimeHandler.parse("2019-10-11T21:10:59.123456789");
        assertNotNull(acc);
        assertTrue(acc.isSupported(ChronoField.YEAR));
        assertEquals(acc.getLong(ChronoField.YEAR), 2019);
        assertTrue(acc.isSupported(ChronoField.MONTH_OF_YEAR));
        assertEquals(acc.getLong(ChronoField.MONTH_OF_YEAR), 10);
        assertTrue(acc.isSupported(ChronoField.DAY_OF_MONTH));
        assertEquals(acc.getLong(ChronoField.DAY_OF_MONTH), 11);
        assertTrue(acc.isSupported(ChronoField.HOUR_OF_DAY));
        assertEquals(acc.getLong(ChronoField.HOUR_OF_DAY), 21);
        assertTrue(acc.isSupported(ChronoField.MINUTE_OF_HOUR));
        assertEquals(acc.getLong(ChronoField.MINUTE_OF_HOUR), 10);
        assertTrue(acc.isSupported(ChronoField.SECOND_OF_MINUTE));
        assertEquals(acc.getLong(ChronoField.SECOND_OF_MINUTE), 59);
        assertTrue(acc.isSupported(ChronoField.MILLI_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.MILLI_OF_SECOND), 123);
        assertTrue(acc.isSupported(ChronoField.MICRO_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.MICRO_OF_SECOND), 123456);
        assertTrue(acc.isSupported(ChronoField.NANO_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.NANO_OF_SECOND), 123456000);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testYearMonthDaySeparatorHourMinutesSecondsParserMicrosecondsWrong() throws FHIRSearchException {
        DateTimeHandler.parse("2019-10-11T01:10:00.9X");
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testYearMonthDaySeparatorHourMinutesSecondsParserMicrosecondsTooLong() throws FHIRSearchException {
        DateTimeHandler.parse("2019-10-11T01:10:00.1234567890");
    }

    @Test
    public void testYearMonthDaySeparatorHourMinutesSecondsMicrosecondsParserZone() throws FHIRSearchException {
        TemporalAccessor acc = DateTimeHandler.parse("2019-10-11T01:10:00+01:00");
        assertNotNull(acc);
        assertTrue(acc.isSupported(ChronoField.YEAR));
        assertEquals(acc.getLong(ChronoField.YEAR), 2019);
        assertTrue(acc.isSupported(ChronoField.MONTH_OF_YEAR));
        assertEquals(acc.getLong(ChronoField.MONTH_OF_YEAR), 10);
        assertTrue(acc.isSupported(ChronoField.DAY_OF_MONTH));
        assertEquals(acc.getLong(ChronoField.DAY_OF_MONTH), 11);
        assertTrue(acc.isSupported(ChronoField.HOUR_OF_DAY));
        assertEquals(acc.getLong(ChronoField.HOUR_OF_DAY), 1);
        assertTrue(acc.isSupported(ChronoField.MINUTE_OF_HOUR));
        assertEquals(acc.getLong(ChronoField.MINUTE_OF_HOUR), 10);
        assertTrue(acc.isSupported(ChronoField.SECOND_OF_MINUTE));
        assertEquals(acc.getLong(ChronoField.SECOND_OF_MINUTE), 0);
        assertTrue(acc.isSupported(ChronoField.MILLI_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.MILLI_OF_SECOND), 0);
        assertTrue(acc.isSupported(ChronoField.MICRO_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.MICRO_OF_SECOND), 0);
        assertTrue(acc.isSupported(ChronoField.NANO_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.NANO_OF_SECOND), 0);

        acc = DateTimeHandler.parse("2019-10-11T21:10:59.999992+05:00");
        assertNotNull(acc);
        assertTrue(acc.isSupported(ChronoField.YEAR));
        assertEquals(acc.getLong(ChronoField.YEAR), 2019);
        assertTrue(acc.isSupported(ChronoField.MONTH_OF_YEAR));
        assertEquals(acc.getLong(ChronoField.MONTH_OF_YEAR), 10);
        assertTrue(acc.isSupported(ChronoField.DAY_OF_MONTH));
        assertEquals(acc.getLong(ChronoField.DAY_OF_MONTH), 11);
        assertTrue(acc.isSupported(ChronoField.HOUR_OF_DAY));
        assertEquals(acc.getLong(ChronoField.HOUR_OF_DAY), 21);
        assertTrue(acc.isSupported(ChronoField.MINUTE_OF_HOUR));
        assertEquals(acc.getLong(ChronoField.MINUTE_OF_HOUR), 10);
        assertTrue(acc.isSupported(ChronoField.SECOND_OF_MINUTE));
        assertEquals(acc.getLong(ChronoField.SECOND_OF_MINUTE), 59);
        assertTrue(acc.isSupported(ChronoField.MILLI_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.MILLI_OF_SECOND), 999);
        assertTrue(acc.isSupported(ChronoField.MICRO_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.MICRO_OF_SECOND), 999992);
        assertTrue(acc.isSupported(ChronoField.NANO_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.NANO_OF_SECOND), 999992000);

        acc = DateTimeHandler.parse("2019-10-11T21:10:59.9-05:00");
        assertNotNull(acc);
        assertTrue(acc.isSupported(ChronoField.YEAR));
        assertEquals(acc.getLong(ChronoField.YEAR), 2019);
        assertTrue(acc.isSupported(ChronoField.MONTH_OF_YEAR));
        assertEquals(acc.getLong(ChronoField.MONTH_OF_YEAR), 10);
        assertTrue(acc.isSupported(ChronoField.DAY_OF_MONTH));
        assertEquals(acc.getLong(ChronoField.DAY_OF_MONTH), 11);
        assertTrue(acc.isSupported(ChronoField.HOUR_OF_DAY));
        assertEquals(acc.getLong(ChronoField.HOUR_OF_DAY), 21);
        assertTrue(acc.isSupported(ChronoField.MINUTE_OF_HOUR));
        assertEquals(acc.getLong(ChronoField.MINUTE_OF_HOUR), 10);
        assertTrue(acc.isSupported(ChronoField.SECOND_OF_MINUTE));
        assertEquals(acc.getLong(ChronoField.SECOND_OF_MINUTE), 59);
        assertTrue(acc.isSupported(ChronoField.MILLI_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.MILLI_OF_SECOND), 900);
        assertTrue(acc.isSupported(ChronoField.MICRO_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.MICRO_OF_SECOND), 900000);
        assertTrue(acc.isSupported(ChronoField.NANO_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.NANO_OF_SECOND), 900000000);

        acc = DateTimeHandler.parse("2019-10-11T21:10:59.123456789-05:00");
        assertNotNull(acc);
        assertTrue(acc.isSupported(ChronoField.YEAR));
        assertEquals(acc.getLong(ChronoField.YEAR), 2019);
        assertTrue(acc.isSupported(ChronoField.MONTH_OF_YEAR));
        assertEquals(acc.getLong(ChronoField.MONTH_OF_YEAR), 10);
        assertTrue(acc.isSupported(ChronoField.DAY_OF_MONTH));
        assertEquals(acc.getLong(ChronoField.DAY_OF_MONTH), 11);
        assertTrue(acc.isSupported(ChronoField.HOUR_OF_DAY));
        assertEquals(acc.getLong(ChronoField.HOUR_OF_DAY), 21);
        assertTrue(acc.isSupported(ChronoField.MINUTE_OF_HOUR));
        assertEquals(acc.getLong(ChronoField.MINUTE_OF_HOUR), 10);
        assertTrue(acc.isSupported(ChronoField.SECOND_OF_MINUTE));
        assertEquals(acc.getLong(ChronoField.SECOND_OF_MINUTE), 59);
        assertTrue(acc.isSupported(ChronoField.MILLI_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.MILLI_OF_SECOND), 123);
        assertTrue(acc.isSupported(ChronoField.MICRO_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.MICRO_OF_SECOND), 123456);
        assertTrue(acc.isSupported(ChronoField.NANO_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.NANO_OF_SECOND), 123456000);

        acc = DateTimeHandler.parse("2019-10-11T21:10:59+05:00");
        assertNotNull(acc);
        assertTrue(acc.isSupported(ChronoField.YEAR));
        assertEquals(acc.getLong(ChronoField.YEAR), 2019);
        assertTrue(acc.isSupported(ChronoField.MONTH_OF_YEAR));
        assertEquals(acc.getLong(ChronoField.MONTH_OF_YEAR), 10);
        assertTrue(acc.isSupported(ChronoField.DAY_OF_MONTH));
        assertEquals(acc.getLong(ChronoField.DAY_OF_MONTH), 11);
        assertTrue(acc.isSupported(ChronoField.HOUR_OF_DAY));
        assertEquals(acc.getLong(ChronoField.HOUR_OF_DAY), 21);
        assertTrue(acc.isSupported(ChronoField.MINUTE_OF_HOUR));
        assertEquals(acc.getLong(ChronoField.MINUTE_OF_HOUR), 10);
        assertTrue(acc.isSupported(ChronoField.SECOND_OF_MINUTE));
        assertEquals(acc.getLong(ChronoField.SECOND_OF_MINUTE), 59);
        assertTrue(acc.isSupported(ChronoField.MILLI_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.MILLI_OF_SECOND), 0);
        assertTrue(acc.isSupported(ChronoField.MICRO_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.MICRO_OF_SECOND), 0);
        assertTrue(acc.isSupported(ChronoField.NANO_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.NANO_OF_SECOND), 0);

        acc = DateTimeHandler.parse("2019-10-11T21:10Z");
        assertNotNull(acc);
        assertTrue(acc.isSupported(ChronoField.YEAR));
        assertEquals(acc.getLong(ChronoField.YEAR), 2019);
        assertTrue(acc.isSupported(ChronoField.MONTH_OF_YEAR));
        assertEquals(acc.getLong(ChronoField.MONTH_OF_YEAR), 10);
        assertTrue(acc.isSupported(ChronoField.DAY_OF_MONTH));
        assertEquals(acc.getLong(ChronoField.DAY_OF_MONTH), 11);
        assertTrue(acc.isSupported(ChronoField.HOUR_OF_DAY));
        assertEquals(acc.getLong(ChronoField.HOUR_OF_DAY), 21);
        assertTrue(acc.isSupported(ChronoField.MINUTE_OF_HOUR));
        assertEquals(acc.getLong(ChronoField.MINUTE_OF_HOUR), 10);
        assertTrue(acc.isSupported(ChronoField.SECOND_OF_MINUTE));
        assertEquals(acc.getLong(ChronoField.SECOND_OF_MINUTE), 0);
        assertTrue(acc.isSupported(ChronoField.MILLI_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.MILLI_OF_SECOND), 0);
        assertTrue(acc.isSupported(ChronoField.MICRO_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.MICRO_OF_SECOND), 0);
        assertTrue(acc.isSupported(ChronoField.NANO_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.NANO_OF_SECOND), 0);

        acc = DateTimeHandler.parse("2019-10-11T21+00:00");
        assertNotNull(acc);
        assertTrue(acc.isSupported(ChronoField.YEAR));
        assertEquals(acc.getLong(ChronoField.YEAR), 2019);
        assertTrue(acc.isSupported(ChronoField.MONTH_OF_YEAR));
        assertEquals(acc.getLong(ChronoField.MONTH_OF_YEAR), 10);
        assertTrue(acc.isSupported(ChronoField.DAY_OF_MONTH));
        assertEquals(acc.getLong(ChronoField.DAY_OF_MONTH), 11);
        assertTrue(acc.isSupported(ChronoField.HOUR_OF_DAY));
        assertEquals(acc.getLong(ChronoField.HOUR_OF_DAY), 21);
        assertTrue(acc.isSupported(ChronoField.MINUTE_OF_HOUR));
        assertEquals(acc.getLong(ChronoField.MINUTE_OF_HOUR), 0);
        assertTrue(acc.isSupported(ChronoField.SECOND_OF_MINUTE));
        assertEquals(acc.getLong(ChronoField.SECOND_OF_MINUTE), 0);
        assertTrue(acc.isSupported(ChronoField.MILLI_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.MILLI_OF_SECOND), 0);
        assertTrue(acc.isSupported(ChronoField.MICRO_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.MICRO_OF_SECOND), 0);
        assertTrue(acc.isSupported(ChronoField.NANO_OF_SECOND));
        assertEquals(acc.getLong(ChronoField.NANO_OF_SECOND), 0);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testYearMonthDaySeparatorHourMinutesSecondsMicrosecondsParserZoneBadZone() throws FHIRSearchException {
        DateTimeHandler.parse("2019-10-11T01:10:59.999992XMD");
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testYearMonthDaySeparatorHourMinutesSecondsMicrosecondsParserZoneBadZoneUseWithDay()
            throws FHIRSearchException {
        DateTimeHandler.parse("2019-10-11TCDT");
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testBadMonthAndHour() throws FHIRSearchException {
        DateTimeHandler.parse("2019-10T01:10:59");
    }

    @Test
    public void testSerializerOutput() throws FHIRSearchException {
        TemporalAccessor acc = DateTimeHandler.parse("2019-10-11T01:10:00+01:00");
        assertNotNull(acc);
        assertEquals(DateTimeHandler.serialize(acc), "2019-10-11T01:10+01:00");
    }

    @Test
    public void testGenerateLowerUpperBoundWithApproximation() throws FHIRSearchException {
        String v = "2019-12-11T00:00:00+00:00";
        TemporalAccessor value = DateTimeHandler.parse(v);
        assertNotNull(value);
        Instant lowerBound = DateTimeHandler.generateLowerBound(Prefix.AP, value, v);
        Instant upperBound = DateTimeHandler.generateUpperBound(Prefix.AP, value, v);
        assertTrue(lowerBound.isBefore(upperBound));
    }

    @Test
    public void testGenerateLowerUpperBoundApproximation() throws FHIRSearchException {
        TemporalAccessor value = DateTimeHandler.parse("2019-12-11T00:00:00+00:00");
        TemporalAccessor nowValue = DateTimeHandler.parse("2019-12-21T00:00:00+00:00");
        Instant cur = java.time.Instant.from(value);
        Instant now = java.time.Instant.from(nowValue);

        Instant lowerBound = DateTimeHandler.generateLowerBoundApproximation(now, cur);
        Instant upperBound = DateTimeHandler.generateUpperBoundApproximation(now, cur);
        assertTrue(lowerBound.isBefore(upperBound));
        assertEquals(lowerBound.toString(), "2019-12-10T00:00:00Z");
        assertEquals(upperBound.toString(), "2019-12-12T00:00:00Z");
    }

    @Test
    public void testGenerateLowerUpperBoundWithNullPrefixYear() throws FHIRSearchException {
        String v = "2019";
        TemporalAccessor value = DateTimeHandler.parse(v);
        Instant lowerBound = DateTimeHandler.generateLowerBound(null, value, v);
        Instant upperBound = DateTimeHandler.generateUpperBound(null, value, v);
        assertEquals(lowerBound.toString(), "2019-01-01T00:00:00Z");
        assertEquals(upperBound.toString(), "2019-12-31T23:59:59.999999Z");
    }

    @Test
    public void testGenerateLowerUpperBoundWithNullPrefixYearMonth() throws FHIRSearchException {
        String v = "2019-12";
        TemporalAccessor value = DateTimeHandler.parse(v);
        Instant lowerBound = DateTimeHandler.generateLowerBound(null, value, v);
        Instant upperBound = DateTimeHandler.generateUpperBound(null, value, v);
        assertEquals(lowerBound.toString(), "2019-12-01T00:00:00Z");
        assertEquals(upperBound.toString(), "2019-12-31T23:59:59.999999Z");
    }

    @Test
    public void testGenerateLowerUpperBoundWithNullPrefixYearMonthDay() throws FHIRSearchException {
        String v = "2019-12-11";
        TemporalAccessor value = DateTimeHandler.parse(v);
        Instant lowerBound = DateTimeHandler.generateLowerBound(null, value, v);
        Instant upperBound = DateTimeHandler.generateUpperBound(null, value, v);
        assertEquals(lowerBound.toString(), "2019-12-11T00:00:00Z");
        assertEquals(upperBound.toString(), "2019-12-11T23:59:59.999999Z");
    }

    @Test
    public void testGenerateLowerUpperBoundWithNullPrefixYearMonthDayHours() throws FHIRSearchException {
        String v = "2019-10-11T11";
        TemporalAccessor value = DateTimeHandler.parse(v);
        Instant lowerBound = DateTimeHandler.generateLowerBound(null, value, v);
        Instant upperBound = DateTimeHandler.generateUpperBound(null, value, v);
        assertEquals(lowerBound.toString(), "2019-10-11T11:00:00Z");
        assertEquals(upperBound.toString(), "2019-10-11T11:59:59.999999Z");
    }

    @Test
    public void testGenerateLowerUpperBoundWithNullPrefixYearMonthDayHoursMinutes() throws FHIRSearchException {
        String v = "2019-10-11T11:00";
        TemporalAccessor value = DateTimeHandler.parse(v);
        Instant lowerBound = DateTimeHandler.generateLowerBound(null, value, v);
        Instant upperBound = DateTimeHandler.generateUpperBound(null, value, v);
        assertEquals(lowerBound.toString(), "2019-10-11T11:00:00Z");
        assertEquals(upperBound.toString(), "2019-10-11T11:00:59.999999Z");
    }

    @Test
    public void testGenerateLowerUpperBoundWithNullPrefixYearMonthDayHoursMinutesSeconds() throws FHIRSearchException {
        String v = "2019-10-11T11:00:00";
        TemporalAccessor value = DateTimeHandler.parse(v);
        Instant lowerBound = DateTimeHandler.generateLowerBound(null, value, v);
        Instant upperBound = DateTimeHandler.generateUpperBound(null, value, v);
        assertEquals(lowerBound.toString(), "2019-10-11T11:00:00Z");
        assertEquals(upperBound.toString(), "2019-10-11T11:00:00.999999Z");
    }

    @Test
    public void testToStringWithNullPrefixYearMonthDayHoursMinutesSeconds() throws FHIRSearchException {
        DateTime dt = DateTime.of("2019-10-11T11:00:00Z");
        assertEquals("2019-10-11T11:00:00Z", DateTime.PARSER_FORMATTER.format(dt.getValue()));
        assertEquals("2019-10-11T11:00Z", dt.getValue().toString());
    }

    @Test
    public void testGenerateLowerUpperBoundWithNullPrefixYearMonthDayHoursMinutesSecondsNanos()
            throws FHIRSearchException {
        String v = "2019-10-11T11:00:00.123456789";
        TemporalAccessor value = DateTimeHandler.parse(v);
        Instant lowerBound = DateTimeHandler.generateLowerBound(null, value, v);
        Instant upperBound = DateTimeHandler.generateUpperBound(null, value, v);
        assertEquals(lowerBound.toString(), "2019-10-11T11:00:00.123456Z");
        assertEquals(upperBound.toString(), "2019-10-11T11:00:00.123456Z");
    }

    @Test
    public void testGenerateLowerUpperBoundWithNonNullPrefix() throws FHIRSearchException {
        String v = "2019";
        TemporalAccessor value = DateTimeHandler.parse(v);
        Instant lowerBound = DateTimeHandler.generateLowerBound(Prefix.EQ, value, v);
        Instant upperBound = DateTimeHandler.generateUpperBound(Prefix.EQ, value, v);
        assertEquals(lowerBound.toString(), "2019-01-01T00:00:00Z");
        assertEquals(upperBound.toString(), "2019-12-31T23:59:59.999999Z");
    }

    @Test
    public void testGenerateLowerUpperBoundZonedDateTimePrecise() throws FHIRSearchException {
        String v = "2019-12-11T00:00:00+05:00";
        TemporalAccessor value = DateTimeHandler.parse(v);

        Instant lowerBound = DateTimeHandler.generateLowerBound(null, value, v);
        Instant upperBound = DateTimeHandler.generateUpperBound(null, value, v);
        assertEquals(lowerBound.toString(), "2019-12-10T19:00:00Z");
        assertEquals(upperBound.toString(), "2019-12-10T19:00:00.999999Z");
    }

    @Test
    public void testGenerateLowerUpperBoundZonedDateTimeYearMonthDayHoursMinutesSeconds() throws FHIRSearchException {
        String v = "2019-12-11T00:00:00+05:00";
        TemporalAccessor value = DateTimeHandler.parse(v);

        Instant lowerBound = DateTimeHandler.generateLowerBound(null, value, v);
        Instant upperBound = DateTimeHandler.generateUpperBound(null, value, v);
        assertEquals(lowerBound.toString(), "2019-12-10T19:00:00Z");
        assertEquals(upperBound.toString(), "2019-12-10T19:00:00.999999Z");
    }

    @Test
    public void testGenerateLowerUpperBoundZonedDateTimeYearMonthDayHoursMinutes() throws FHIRSearchException {
        String v = "2019-12-11T00:00+05:00";
        TemporalAccessor value = DateTimeHandler.parse(v);

        Instant lowerBound = DateTimeHandler.generateLowerBound(null, value, v);
        Instant upperBound = DateTimeHandler.generateUpperBound(null, value, v);
        assertEquals(lowerBound.toString(), "2019-12-10T19:00:00Z");
        assertEquals(upperBound.toString(), "2019-12-10T19:00:59.999999Z");
    }

    @Test
    public void testGenerateLowerUpperBoundZonedDateTimeYearMonthDayHours() throws FHIRSearchException {
        String v = "2019-12-11T00+05:00";
        TemporalAccessor value = DateTimeHandler.parse(v);

        Instant lowerBound = DateTimeHandler.generateLowerBound(null, value, v);
        Instant upperBound = DateTimeHandler.generateUpperBound(null, value, v);
        assertEquals(lowerBound.toString(), "2019-12-10T19:00:00Z");
        assertEquals(upperBound.toString(), "2019-12-10T19:59:59.999999Z");
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testGenerateLowerUpperBoundZonedDateTimeYearMonthDayLower() throws FHIRSearchException {
        String v = "2019-12-11+05:00";
        TemporalAccessor value = DateTimeHandler.parse(v);
        DateTimeHandler.generateLowerBound(null, value, v);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testGenerateLowerUpperBoundZonedDateTimeYearMonthDayUpper() throws FHIRSearchException {
        String v = "2019-12-11+05:00";
        TemporalAccessor value = DateTimeHandler.parse(v);
        DateTimeHandler.generateUpperBound(null, value, v);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testGenerateLowerUpperBoundZonedDateTimeYearMonthLower() throws FHIRSearchException {
        String v = "2019-12+05:00";
        TemporalAccessor value = DateTimeHandler.parse(v);
        DateTimeHandler.generateLowerBound(null, value, v);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testGenerateLowerUpperBoundZonedDateTimeYearMonthUpper() throws FHIRSearchException {
        String v = "2019-12+05:00";
        TemporalAccessor value = DateTimeHandler.parse(v);
        DateTimeHandler.generateUpperBound(null, value, v);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testGenerateLowerUpperBoundZonedDateTimeYearLower() throws FHIRSearchException {
        String v = "2019+05:00";
        TemporalAccessor value = DateTimeHandler.parse(v);
        DateTimeHandler.generateLowerBound(null, value, v);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testGenerateLowerUpperBoundZonedDateTimeYearUpper() throws FHIRSearchException {
        String v = "2019+05:00";
        TemporalAccessor value = DateTimeHandler.parse(v);
        DateTimeHandler.generateUpperBound(null, value, v);
    }

    @Test
    public void testParseYearMonthDate() throws FHIRSearchException {
        String v = "2019-12-11";
        QueryParameterValue parameterValue = new QueryParameterValue();
        DateTimeHandler.parse(null, parameterValue, v);
        assertEquals(parameterValue.getValueDate(), "2019-12-11");
        assertEquals(parameterValue.getValueDateLowerBound().toString(), "2019-12-11T00:00:00Z");
        assertEquals(parameterValue.getValueDateUpperBound().toString(), "2019-12-11T23:59:59.999999Z");
    }
}