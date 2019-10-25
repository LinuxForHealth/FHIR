/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;

import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.DateTime;

/**
 * These utilities support the building of a FHIR 
 * Query with complicates timezones and partial times. 
 */
public class QueryBuilderUtil {
    
    // used for adjustInto calls to obtain usable Zulu instants from Year, YearMonth, LocalDate
    private static final String REFERENCE_DATE_STRING = "2018-01-01T00:00:00.000000";
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
        
        if (ta instanceof ZonedDateTime) {
            // early exit
            return ((ZonedDateTime) ta).toInstant();
        }
        
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
        java.time.Instant result;
        java.time.LocalDateTime local;
        
        if (ta instanceof ZonedDateTime) {
            result = ((ZonedDateTime) ta).toInstant();
            if (!hasMicroseconds(result)) {
                result = addOne(result);
            }
        } else if (ta instanceof java.time.Instant) {
            result = (Instant) ta;
            if (!hasMicroseconds(result)) {
                result = addOne(result);
            }
        } else {
            if (ta instanceof Year) {
                Year year = ((Year)ta).plusYears(1);
                local = REFERENCE_DATE.with(ChronoField.YEAR, year.getValue());
            }
            else if (ta instanceof YearMonth) {
                YearMonth ym = ((YearMonth)ta).plusMonths(1);
                local = REFERENCE_DATE.with(ChronoField.YEAR, ym.getYear());
                local = local.with(ChronoField.MONTH_OF_YEAR, ym.getMonthValue());
            }
            else if (ta instanceof LocalDate) {
                // as long as we follow this order, we should never end up with an invalid date-time
                LocalDate ld = ((LocalDate) ta).plusDays(1);
                local = REFERENCE_DATE.with(ChronoField.YEAR, ld.getYear());
                local = local.with(ChronoField.MONTH_OF_YEAR, ld.getMonthValue());
                local = local.with(ChronoField.DAY_OF_MONTH, ld.getDayOfMonth());
            }
            else {
                throw new IllegalArgumentException("Invalid partial TemporalAccessor: " + ta.getClass().getName());
            }
            
            result = ZonedDateTime.of(local, ZoneOffset.UTC).toInstant();
        }
        
        return result;
    }

    private static java.time.Instant addOne(java.time.Instant instant) {
        if (instant.get(ChronoField.MILLI_OF_SECOND) > 0) {
            instant = instant.plusMillis(1);
        } else {
            instant = instant.plusSeconds(1);
        }
        return instant;
    }
    
    /**
     * A rough heuristic to guess whether we got an exact instant (down to the millisecond) or something we should treat as an implicit range.
     * <p>
     * There is no "correct" way to determine this at the moment since the search parameter comes in as a DateTime and
     * there is no way to distinguish input like 0001-01-01T01:01:01.1 from 0001-01-01T01:01:01.100000.
     */
    public static boolean hasMicroseconds(TemporalAccessor ta) {
        int micro_of_milli = ta.get(ChronoField.MICRO_OF_SECOND) % 1000;
        if (micro_of_milli > 0) {
            return true;
        }
        return false;
    }
}
