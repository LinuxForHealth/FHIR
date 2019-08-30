/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.persistence.jdbc.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;

import com.ibm.watson.health.fhir.model.type.Date;
import com.ibm.watson.health.fhir.model.type.DateTime;

/**
 * @author rarnold
 *
 */
public class QueryBuilderUtil {
    // used for adjustInto calls to obtain usable Zulu instants from Year, YearMonth, LocalDate
    private static final String REFERENCE_DATE_STRING = "2018-01-01T00:00:00";
    private static final LocalDateTime REFERENCE_DATE = LocalDateTime.parse(REFERENCE_DATE_STRING);

    /**
     * convert a fully qualified or partial {@link DateTime} to an Instant suitable for
     * use as a query parameter (i.e. eventual conversion into java.sql.Timestamp)
     * @param dateTime
     * @return
     */
    public static java.time.Instant getInstant(DateTime dateTime) {
        if (dateTime.isPartial()) {
            return getInstantFromPartial(dateTime.getValue());
        }
        else {
            // Direct conversion because the dateTime has all the fields (including timezone)
            return Instant.from(dateTime.getValue());
        }
    }
    
    /**
     * Compute the end time to use as a range filter based on the "partialness"
     * of the given dateTime field.
     * @param dateTime
     * @return
     */
    public static java.time.Instant getEnd(DateTime dateTime) {
        return getEnd(dateTime.getValue());
    }

    public static java.time.Instant getStart(DateTime dateTime) {
        return getInstantFromPartial(dateTime.getValue());
    }

    public static java.time.Instant getStart(Date date) {
        return getInstantFromPartial(date.getValue());
    }
    
    public static java.time.Instant getEnd(Date date) {
        return getEnd(date.getValue());
    }

    public static java.time.Instant getInstantFromPartial(TemporalAccessor ta) {
        java.time.LocalDateTime result;
        
        if (ta instanceof Year) {
            result = REFERENCE_DATE.with(ChronoField.YEAR, ((Year)ta).getValue());
        }
        else if (ta instanceof YearMonth) {
            result = REFERENCE_DATE.with(ChronoField.YEAR, ((YearMonth)ta).getYear());
            result = result.with(ChronoField.MONTH_OF_YEAR, ((YearMonth)ta).getMonthValue());
        }
        else if (ta instanceof LocalDate) {
            // as long as we follow this order, we should never end up with an invalid date-time
            result = REFERENCE_DATE.with(ChronoField.YEAR, ((LocalDate) ta).getYear());
            result = result.with(ChronoField.MONTH_OF_YEAR, ((LocalDate) ta).getMonthValue());
            result = result.with(ChronoField.DAY_OF_MONTH, ((LocalDate) ta).getDayOfMonth());
        }
        else {
            throw new IllegalArgumentException("Invalid partial TemporalAccessor: " + ta.getClass().getName());
        }
        
        return ZonedDateTime.of(result, ZoneOffset.UTC).toInstant();
    }
        
    public static java.time.Instant getEnd(TemporalAccessor ta) {
        java.time.LocalDateTime result;
        
        if (ta instanceof Year) {
            Year year = ((Year)ta).plusYears(1);
            result = REFERENCE_DATE.with(ChronoField.YEAR, year.getValue());
        }
        else if (ta instanceof YearMonth) {
            YearMonth ym = ((YearMonth)ta).plusMonths(1);
            result = REFERENCE_DATE.with(ChronoField.YEAR, ym.getYear());
            result = result.with(ChronoField.MONTH_OF_YEAR, ym.getMonthValue());
        }
        else if (ta instanceof LocalDate) {
            // as long as we follow this order, we should never end up with an invalid date-time
            LocalDate ld = ((LocalDate) ta).plusDays(1);
            result = REFERENCE_DATE.with(ChronoField.YEAR, ld.getYear());
            result = result.with(ChronoField.MONTH_OF_YEAR, ld.getMonthValue());
            result = result.with(ChronoField.DAY_OF_MONTH, ld.getDayOfMonth());
        }
        else {
            throw new IllegalArgumentException("Invalid partial TemporalAccessor: " + ta.getClass().getName());
        }
        
        return ZonedDateTime.of(result, ZoneOffset.UTC).toInstant();
        
    }
}
