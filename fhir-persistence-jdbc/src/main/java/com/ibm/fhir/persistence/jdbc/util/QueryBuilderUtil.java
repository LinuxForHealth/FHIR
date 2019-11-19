/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util;

import java.sql.Timestamp;
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
     * @param inclusive whether to include the last instant of the implied period or not; 
     *        for example (12:00:00,12:00:01) vs (12:00:00,12:00:00.999999)
     * @return
     */
    public static java.time.Instant getEnd(DateTime dateTime, boolean inclusive) {
        return getEnd(dateTime.getValue(), inclusive);
    }

    public static java.time.Instant getStart(DateTime dateTime) {
        return getInstantFromPartial(dateTime.getValue());
    }

    public static java.time.Instant getStart(Date date) {
        return getInstantFromPartial(date.getValue());
    }

    public static java.time.Instant getEnd(Date date, boolean inclusive) {
        return getEnd(date.getValue(), inclusive);
    }

    public static java.time.Instant getInstantFromPartial(TemporalAccessor ta) {
        java.time.LocalDateTime result;
        if (ta == null) {
            return null;
        }
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

    public static java.time.Instant getEnd(TemporalAccessor ta, boolean inclusive) {
        java.time.Instant result;
        if (ta == null) {
            return null;
        }
        if (ta instanceof ZonedDateTime) {
            if (!hasSubSeconds(ta)) {
                ta = addOneMinuteOrSecond((ZonedDateTime) ta);
                ta = minusOneMicroSecond((ZonedDateTime) ta);
            }
            result = ((ZonedDateTime) ta).toInstant();
        } else if (ta instanceof java.time.Instant) {
            if (!hasSubSeconds(ta)) {
                ta = addOneMinuteOrSecond(((java.time.Instant) ta).atZone(ZoneOffset.UTC));
                ta = minusOneMicroSecond((ZonedDateTime) ta);
                ta = ((ZonedDateTime) ta).toInstant();
            }
            result = (java.time.Instant) ta;
        } else {
            java.time.LocalDateTime local;

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

            result = minusOneMicroSecond(ZonedDateTime.of(local, ZoneOffset.UTC)).toInstant();
        }

        return result;
    }

    private static ZonedDateTime addOneMinuteOrSecond(ZonedDateTime dateTime) {
        if (dateTime.get(ChronoField.SECOND_OF_MINUTE) > 0) {
            dateTime = dateTime.plusSeconds(1);
        } else {
            dateTime = dateTime.plusSeconds(60);
        }
        return dateTime;
    }
    private static ZonedDateTime minusOneMicroSecond(ZonedDateTime dateTime) {
        return dateTime.minusNanos(1000);
    }

    /**
     * Convert a period's end timestamp from an exclusive end timestamp to an inclusive one
     *
     * @param exlusiveEndTime
     * @return inclusiveEndTime
     */
    public static Timestamp minusNanoseconds(Timestamp exlusiveEndTime) {
        // Our current schema uses the db2/derby default of 6 decimal places (1000 nanoseconds) for fractional seconds.
        return Timestamp.from(exlusiveEndTime.toInstant().minusNanos(1000));
    }

    /**
     * Whether the temporal accessor has fractional seconds
     */
    public static boolean hasSubSeconds(TemporalAccessor ta) {
        if (ta.isSupported(ChronoField.MILLI_OF_SECOND) && ta.get(ChronoField.NANO_OF_SECOND) > 0) {
            return true;
        }
        return false;
    }
}
