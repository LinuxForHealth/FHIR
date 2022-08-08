/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.type.Date;
import org.linuxforhealth.fhir.model.type.DateTime;
import org.linuxforhealth.fhir.model.type.Instant;
import org.linuxforhealth.fhir.model.type.Time;

/**
 * Date time Tests
 */
public class DateTimeTest {

    @Test(groups = { "instant-tests" })
    public void testInstantWithValidStringInputs() throws Exception{
        // Without miscro-second
        Instant.of("2018-12-31T20:00:00-04:00");
        // With nano-second
        Instant.of("2018-12-31T20:00:00.112000000-04:00");
        // With micro-second
        Instant.of("2018-12-31T20:00:00.112000-04:00");
        // With short micro-second
        Instant.of("2018-12-31T20:00:00.112-04:00");
        // With temporal nano-second
        Instant.of(ZonedDateTime.of(2020,8,25,13,20,23,123456789,ZoneId.systemDefault()));
        // With temporal micro-second
        Instant.of(ZonedDateTime.of(2020,8,25,13,20,23,123456,ZoneId.systemDefault()));
    }

    @Test(groups = { "instant-tests" })
    public void testInstantWithZonedDateTime() throws Exception{
        Instant.of(ZonedDateTime.now());
    }

    @Test(groups = { "instant-tests" }, expectedExceptions = DateTimeParseException.class)
    public void testInstantStringInputWithoutTimeZone() throws Exception {
        Instant.of("2018-12-31T20:00:08.112");
    }

    @Test(groups = { "instant-tests" }, expectedExceptions = DateTimeParseException.class)
    public void testInstantStringInputWithWrongTimeZone1() throws Exception {
        Instant.of("2018-12-31T20:00:00-24:00");
    }

    @Test(groups = { "instant-tests" }, expectedExceptions = DateTimeParseException.class)
    public void testInstantStringInputWithWrongTimeZone2() throws Exception {
        Instant.of("2018-12-31T20:00:00-4:00");
    }

    @Test(groups = { "instant-tests" }, expectedExceptions = DateTimeParseException.class)
    public void testInstantStringInputWithWrongSeperator() throws Exception {
        Instant.of("2018/12/31T20:00:00-04:00");
    }

    @Test(groups = { "instant-tests" }, expectedExceptions = DateTimeParseException.class)
    public void testInstantStringInputWithShortDateValue() throws Exception {
        Instant.of("2018-2-3T20:00:00-04:00");
    }

    @Test(groups = { "instant-tests" }, expectedExceptions = DateTimeParseException.class)
    public void testInstantStringInputWithWrongChars() throws Exception {
        Instant.of("2018-aa-bbT20:00:00-04:00");
    }

    @Test(groups = { "instant-tests" }, expectedExceptions = DateTimeParseException.class)
    public void testInstantStringInputWithTooMuchPrecision() throws Exception {
        Instant.of("2018-12-31T20:00:00.1234567890-04:00");
    }

    @Test(groups = { "instant-tests" }, expectedExceptions = NullPointerException.class)
    public void testInstantStringInputWithNull() throws Exception {
        Instant.of((String)null);
    }

    @Test(groups = { "instant-tests" }, expectedExceptions = NullPointerException.class)
    public void testInstantWithNullZonedDateTime() throws Exception {
        Instant.of((ZonedDateTime)null);
    }

    @Test(groups = { "instant-tests" })
    public void testInstantPrecise() throws Exception {
        assertTrue(Instant.of("2018-12-31T20:00:00.112-04:00").getValue()
                .isAfter(Instant.of(("2018-12-31T20:00:00.111-04:00")).getValue()));
    }

    @Test(groups = { "instant-tests" })
    public void testInstantTruncation() throws Exception {
        assertTrue(Instant.of("2018-12-31T20:00:00.112000-04:00").getValue()
                .equals(Instant.of(("2018-12-31T20:00:00.112000999-04:00")).getValue()));
    }

    @Test(groups = { "date-tests" })
    public void testDateWithValidStringInputs() throws Exception{
        // Full date
        assertFalse(Date.of("2018-02-31").isPartial());
        // Partial date with month
        assertTrue(Date.of("2018-12").isPartial());
        // Partial date with year only
        assertTrue(Date.of("2018").isPartial());
    }

    @Test(groups = { "date-tests" })
    public void testDateWithValidTemporalAccessor() throws Exception{
        // Full date
        assertFalse(Date.of(LocalDate.now()).isPartial());
        // Partial date with month
        assertTrue(Date.of(YearMonth.now()).isPartial());
     // Partial date with year only
        assertTrue(Date.of(Year.now()).isPartial());
    }

    @Test(groups = { "date-tests" }, expectedExceptions = DateTimeParseException.class)
    public void testDateStringWithWrongSeperator() throws Exception{
        Date.of("2018/12/31");
    }

    @Test(groups = { "date-tests" }, expectedExceptions = DateTimeParseException.class)
    public void testDateStringWithShortValues() throws Exception{
        Date.of("2018-2-3");
    }
    @Test(groups = { "date-tests" }, expectedExceptions = DateTimeParseException.class)
    public void testDateStringWithIlegalChars() throws Exception{
        Date.of("20aa-12-31");
    }

    @Test(groups = { "date-tests" }, expectedExceptions = NullPointerException.class)
    public void testDateWithNullString() throws Exception{
        Date.of((String)null);
    }

    @Test(groups = { "date-tests" }, expectedExceptions = NullPointerException.class)
    public void testDateWithNullTemporalAccessor() throws Exception{
        Date.of((TemporalAccessor)null);
    }

    @Test(groups = { "datetime-tests" })
    public void testDateTimeWithValidInputs() throws Exception{
        assertFalse(DateTime.now().isPartial());
        assertFalse(DateTime.now(ZoneOffset.UTC).isPartial());
        // Valid TemporalAccessor values
        assertFalse(DateTime.of(ZonedDateTime.of(2020,8,25,13,20,23,123456789,ZoneId.systemDefault())).isPartial());
        assertFalse(DateTime.of(ZonedDateTime.now()).isPartial());
        assertFalse(DateTime.of(ZonedDateTime.now(ZoneOffset.UTC)).isPartial());
        assertTrue(DateTime.of(LocalDate.now()).isPartial());
        assertTrue(DateTime.of(YearMonth.now()).isPartial());
        assertTrue(DateTime.of(Year.now()).isPartial());
        // Valid string values
        assertFalse(DateTime.of("2018-12-31T20:00:00.123456789-04:00").isPartial());
        assertFalse(DateTime.of("2018-12-31T20:00:00.112000-04:00").isPartial());
        assertFalse(DateTime.of("2018-12-31T20:00:00-04:00").isPartial());
        assertFalse(DateTime.of("2018-12-31T20:00:00.112-04:00").isPartial());
        assertTrue(DateTime.of("2018-12-31").isPartial());
        assertTrue(DateTime.of("2018-12").isPartial());
        assertTrue(DateTime.of("2018").isPartial());
    }

    @Test(groups = { "datetime-tests" }, expectedExceptions = DateTimeParseException.class)
    public void testDateTimeStringInputWithoutTimeZone() throws Exception {
        DateTime.of("2018-12-31T20:00:08.112");
    }

    @Test(groups = { "datetime-tests" }, expectedExceptions = DateTimeParseException.class)
    public void testDateTimeStringInputWithWrongTimeZone1() throws Exception {
        DateTime.of("2018-12-31T20:00:00-24:00");
    }

    @Test(groups = { "instant-tests" }, expectedExceptions = DateTimeParseException.class)
    public void testDateTimeStringInputWithWrongTimeZone2() throws Exception {
        DateTime.of("2018-12-31T20:00:00-4:00");
    }

    @Test(groups = { "datetime-tests" }, expectedExceptions = DateTimeParseException.class)
    public void testDateTimeStringInputWithWrongSeperator() throws Exception {
        DateTime.of("2018/12/31T20:00:00-04:00");
    }

    @Test(groups = { "datetime-tests" }, expectedExceptions = DateTimeParseException.class)
    public void testDateTimeStringInputWithShortDateValue() throws Exception {
        DateTime.of("2018-2-3T20:00:00-04:00");
    }

    @Test(groups = { "datetime-tests" }, expectedExceptions = DateTimeParseException.class)
    public void testDateTimeStringInputWithIlegalChars() throws Exception {
        DateTime.of("2018-aa-bbT20:00:00-04:00");
    }

    @Test(groups = { "datetime-tests" }, expectedExceptions = NullPointerException.class)
    public void testDateTimeWithNullString() throws Exception {
        DateTime.of((String)null);
    }

    @Test(groups = { "datetime-tests" }, expectedExceptions = NullPointerException.class)
    public void testDateTimeWithNullTemporalAccessor() throws Exception {
        DateTime.of((TemporalAccessor)null);
    }

    @Test(groups = { "datetime-tests" }, expectedExceptions = DateTimeParseException.class)
    public void testDateTimeStringInputWithShortDateValue2() throws Exception {
        DateTime.of("2018-2-3");
    }

    @Test(groups = { "datetime-tests" }, expectedExceptions = DateTimeParseException.class)
    public void testDateTimeStringInputWithShortDateValue3() throws Exception {
        DateTime.of("2018-2");
    }

    @Test(groups = { "datetime-tests" }, expectedExceptions = DateTimeParseException.class)
    public void testDateTimeStringInputWithShortDateValue4() throws Exception {
        DateTime.of("18");
    }

    @Test(groups = { "datetime-tests" }, expectedExceptions = DateTimeParseException.class)
    public void testDateTimeStringInputWithTooMuchPrecision() throws Exception {
        DateTime.of("2018-12-31T20:00:00.1234567890-04:00");
    }

    @Test(groups = { "datetime-tests" })
    public void testDateTimePrecise() throws Exception {
        assertTrue(java.time.Instant.from(DateTime.of("2018-12-31T20:00:00.112-04:00").getValue())
                .isAfter(java.time.Instant.from(DateTime.of("2018-12-31T20:00:00.111-04:00").getValue())));
    }

    @Test(groups = { "datetime-tests" })
    public void testDateTimeTruncation() throws Exception {
        assertTrue(java.time.Instant.from(DateTime.of("2018-12-31T20:00:00.112000-04:00").getValue())
                .equals(java.time.Instant.from(DateTime.of("2018-12-31T20:00:00.112000999-04:00").getValue())));
    }

     @Test(groups = { "time-tests" })
    public void testTimeWithValidInputs() throws Exception{
        // localTime
        Time.of(LocalTime.now());
        Time.of(LocalTime.MIDNIGHT);
        Time.of(LocalTime.now(ZoneId.of("UTC+2")));
        Time.of(LocalTime.of(13,20,23,123456789));
        // Valid string values
        Time.of("20:00:00.112000000");
        Time.of("20:00:00.112000");
        Time.of("20:00:00.112");
        Time.of("20:00:00");
    }

    @Test(groups = { "time-tests" }, expectedExceptions = DateTimeParseException.class)
    public void testTimeStringInputWithShortDateValue() throws Exception {
        Time.of("20:00");
    }

    @Test(groups = { "time-tests" }, expectedExceptions = DateTimeParseException.class)
    public void testTimeStringInputWithShortDateValue2() throws Exception {
        Time.of("20");
    }

    @Test(groups = { "time-tests" }, expectedExceptions = DateTimeParseException.class)
    public void testTimeStringInputWithShortDateValue3() throws Exception {
        Time.of("8:00:00");
    }

    @Test(groups = { "time-tests" }, expectedExceptions = DateTimeParseException.class)
    public void testTimeStringInputWithIllegalChars() throws Exception {
        Time.of("20:FF:00");
    }

    @Test(groups = { "time-tests" }, expectedExceptions = DateTimeParseException.class)
    public void testTimeStringInputWithTooMuchPrecision() throws Exception {
        Time.of("20:00:00.1234567890");
    }

    @Test(groups = { "time-tests" }, expectedExceptions = NullPointerException.class)
    public void testTimeWithNullString() throws Exception {
        Time.of((String)null);
    }

    @Test(groups = { "time-tests" }, expectedExceptions = NullPointerException.class)
    public void testTimeWithNullLocalTime() throws Exception {
        Time.of((LocalTime)null);
    }

    @Test(groups = { "time-tests" })
    public void testTimePrecise() throws Exception {
        assertTrue(Time.of("20:00:00.112000").getValue().isAfter(Time.of("20:00:00").getValue()));
    }

    @Test(groups = { "time-tests" })
    public void testTimeTruncation() throws Exception {
        assertTrue(Time.of("20:00:00.112000").getValue().equals(Time.of("20:00:00.112000999").getValue()));
    }

}
