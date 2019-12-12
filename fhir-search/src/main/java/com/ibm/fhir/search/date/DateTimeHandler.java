/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.date;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.TimeZone;

import com.ibm.fhir.search.SearchConstants.Prefix;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.exception.SearchExceptionUtil;
import com.ibm.fhir.search.parameters.ParameterValue;

/**
 * Date Time Handler<br>
 * <a href=
 * "https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#patterns">Date
 * Time Formatter</a>
 */
public class DateTimeHandler {
    /*
     * to normalize the times and dates we store all times and dates as UTC.
     * This ensures a consistent, and accurate approach when extracting the times
     * and dates.
     * <br>
     * If there is a need to override, the DEFAULT_ZONE can be used in combination
     * with a FHIR Configuration property, the property defaults to UTC, and may be
     * overridden.
     */
    private static final String DEFAULT_ZONE = "UTC";

    // used for adjustInto calls to obtain usable Zulu instants from Year, YearMonth, LocalDate
    private static final String REFERENCE_DATE_STRING = "2018-01-01T00:00:00.000000";
    private static final LocalDateTime REFERENCE_DATE = LocalDateTime.parse(REFERENCE_DATE_STRING);

    // The approach we follow is to use 6 pts of precision.  
    private static final long TICK = 1000l;

    /*
     * The date parameter format is yyyy-mm-ddThh:mm:ss[Z|(+|-)hh:mm] (the standard
     * XML format). <br>
     * <br>
     * 1 - Any degree of precision can be provided. <br>
     * 2 - SHALL: Populated from LEFT to Right
     * 3 - SHALL: Minutes be present if an hour is present
     * 4 - SHOULD: provide a time zone if the time part is present
     * 5 - MAY: Time consists of hours and minutes with no seconds:
     */
    // @formatter:off
    private static final DateTimeFormatter DATE_TIME_PARSER_FORMATTER =
            new DateTimeFormatterBuilder()
                    .appendPattern("yyyy")
                    .optionalStart()
                        .appendLiteral('-')
                        .appendPattern("MM")
                        .optionalStart()
                            .appendLiteral('-')
                            .appendPattern("dd")
                            .optionalStart()
                                .appendLiteral("T")
                                .optionalStart()
                                .appendPattern("HH")
                                .optionalStart()
                                    .appendLiteral(':')
                                    .appendPattern("mm")
                                    .optionalStart()
                                        .appendLiteral(':')
                                        .appendPattern("ss")
                                        .optionalStart()
                                            .appendFraction(ChronoField.MICRO_OF_SECOND, 0, 6, true)
                                        .optionalEnd()
                                    .optionalEnd()
                                .optionalEnd()
                                .optionalStart()
                                    .appendLiteral('Z')
                                    .optionalStart()
                                        .appendZoneOrOffsetId()
                                    .optionalEnd()
                                .optionalEnd()
                            .optionalEnd()
                            .optionalEnd()
                        .optionalEnd()
                    .optionalEnd()
                    .toFormatter();
    // @formatter:on

    public static Timestamp generateTimestamp(Instant inst) {
        TimeZone.setDefault(TimeZone.getTimeZone(DEFAULT_ZONE));
        return Timestamp.from(inst);
    }

    /**
     * Calculates a lower bound absolutely.
     * No matter if the time is AFTER the current time.
     * 
     * @param now
     * @param cur
     * @return
     */
    public static Instant generateLowerBoundApproximation(Instant now, Instant cur) {
        long nanoGap = ChronoUnit.NANOS.between(cur, now);
        return cur.minus(Duration.ofNanos(Math.abs(nanoGap)).dividedBy(10));
    }

    /**
     * generate lower bounds
     * 
     * @param prefix
     * @param value
     * @param originalString
     * @return
     */
    public static Instant generateLowerBound(Prefix prefix, TemporalAccessor value, String originalString) {
        Instant response;

        if (prefix != null && Prefix.AP.compareTo(prefix) == 0) {
            Instant cur = generateValue(value, originalString);
            Instant now = java.time.Instant.now();
            response = generateLowerBoundApproximation(now, cur);
        } else {
            response = generateValue(value, originalString).minus(TICK, ChronoUnit.NANOS);
        }
        return response;
    }

    public static Instant generateValue(TemporalAccessor value, String originalString) {
        return generateValue(value);
    }

    /**
     * common code to generate instant value.
     * 
     * @param value
     * @return
     */
    public static Instant generateValue(TemporalAccessor value) {
        Instant response;
        if (value instanceof java.time.Year) {
            // YEAR - 1
            Year year = (Year) value;
            LocalDateTime local = REFERENCE_DATE.with(ChronoField.YEAR, year.getValue());
            response = ZonedDateTime.of(local, ZoneOffset.UTC).toInstant();
        } else if (value instanceof YearMonth) {
            // Month - 1
            // Grab the values for Year/Month Value
            YearMonth ym = (YearMonth) value;
            LocalDateTime local = REFERENCE_DATE.with(ChronoField.YEAR, ym.getYear());
            local    = local.with(ChronoField.MONTH_OF_YEAR, ym.getMonthValue());
            response =
                    ZonedDateTime.of(local, ZoneOffset.UTC).toInstant();
        } else if (value instanceof LocalDate) {
            // LocalDate - YYYY-MM-DD
            LocalDate ld = (LocalDate) value;
            LocalDateTime local = REFERENCE_DATE.with(ChronoField.YEAR, ld.getYear());
            local    = local.with(ChronoField.MONTH_OF_YEAR, ld.getMonthValue());
            local    = local.with(ChronoField.DAY_OF_MONTH, ld.getDayOfMonth());
            response =
                    ZonedDateTime.of(local, ZoneOffset.UTC).toInstant();
        } else if (value instanceof LocalDateTime) {
            // LocalDate - YYYY-MM-DD HH
            LocalDateTime local = (LocalDateTime) value;
            response =
                    ZonedDateTime.of(local, ZoneOffset.UTC).toInstant();
        } else if (value instanceof ZonedDateTime) {
            ZonedDateTime zdt = (ZonedDateTime) value;
            response = zdt.toInstant();
        } else {
            Instant cur = java.time.Instant.from(value);
            response = cur.minus(Duration.ofNanos(TICK));
        }
        return response;
    }

    /**
     * generate upper bounds
     * 
     * @param prefix
     * @param value
     * @param originalString
     * @return
     */
    public static Instant generateUpperBound(Prefix prefix, TemporalAccessor value, String originalString) {
        Instant response = null;

        ChronoUnit unit = ChronoUnit.NANOS;
        if (value instanceof java.time.Year) {
            // YEAR + 1
            Year year = (Year) value;
            year = year.plus(1, ChronoUnit.YEARS);
            LocalDateTime local = REFERENCE_DATE.with(ChronoField.YEAR, year.getValue());
            response =
                    ZonedDateTime.of(local, ZoneOffset.UTC).toInstant().minus(TICK,
                            unit);

            unit     = ChronoUnit.YEARS;
        } else if (value instanceof YearMonth) {
            // Grab the values for Year/Month Value
            YearMonth ym = (YearMonth) value;
            ym = ym.plusMonths(1);
            LocalDateTime local = REFERENCE_DATE.with(ChronoField.YEAR, ym.getYear());
            local    = local.with(ChronoField.MONTH_OF_YEAR, ym.getMonthValue());
            response =
                    ZonedDateTime.of(local, ZoneOffset.UTC).toInstant().minus(TICK,
                            ChronoUnit.NANOS);
            unit     = ChronoUnit.MONTHS;
        } else if (value instanceof LocalDate) {
            // LocalDate - YYYY-MM-DD
            LocalDate ld = (LocalDate) value;
            ld = ld.plusDays(1);
            LocalDateTime local = REFERENCE_DATE.with(ChronoField.YEAR, ld.getYear());
            local    = local.with(ChronoField.MONTH_OF_YEAR, ld.getMonthValue());
            local    = local.with(ChronoField.DAY_OF_MONTH, ld.getDayOfMonth());
            response =
                    ZonedDateTime.of(local, ZoneOffset.UTC).toInstant().minus(TICK,
                            ChronoUnit.NANOS);

            unit     = ChronoUnit.DAYS;
        } else if (value instanceof LocalDateTime) {
            // LocalDate - YYYY-MM-DD
            LocalDateTime local = (LocalDateTime) value;
            long precision = originalString.chars().filter(ch -> ch == ':' || ch == '.').count();
            if (precision == 0) {
                // HH - No Colon 
                local =
                        local.plus(1, ChronoUnit.HOURS).minus(TICK,
                                ChronoUnit.NANOS);

                unit  = ChronoUnit.HOURS;
            } else if (precision == 1) {
                // HH:MM - First Colon 
                local =
                        local.plus(1, ChronoUnit.MINUTES).minus(TICK,
                                ChronoUnit.NANOS);

                unit  = ChronoUnit.MINUTES;
            } else if (precision == 2) {
                // HH:MM:SS - Second Colon
                local =
                        local.plus(1, ChronoUnit.SECONDS).minus(TICK,
                                ChronoUnit.NANOS);
                unit  = ChronoUnit.SECONDS;
            }

            // ELSE -> HH:MM:SS.XXXXX - First Period
            // We're at a precise point here, no more infinite precision. 
            response =
                    ZonedDateTime.of(local, ZoneOffset.UTC).toInstant();
        } else if (value instanceof ZonedDateTime) {
            ZonedDateTime zdt = (ZonedDateTime) value;
            long precision = originalString.chars().filter(ch -> ch == ':' || ch == '.').count();
            // Shift by 1 as the Zone includes a semicolon. 
            if (precision == 1) {
                // HH - No Colon 
                zdt  =
                        zdt.plus(1, ChronoUnit.HOURS).minus(TICK,
                                ChronoUnit.NANOS);
                unit = ChronoUnit.HOURS;
            } else if (precision == 2) {
                // HH:MM - First Colon 
                zdt  = zdt.plus(1, ChronoUnit.MINUTES).minus(TICK, ChronoUnit.NANOS);
                unit = ChronoUnit.MINUTES;
            } else if (precision == 3) {
                // HH:MM:SS - Second Colon
                zdt  = zdt.plus(1, ChronoUnit.SECONDS).minus(TICK, ChronoUnit.NANOS);
                unit = ChronoUnit.SECONDS;
            }

            // ELSE -> HH:MM:SS.XXXXX - First Period
            // We're at a precise point here, no more infinite precision. 
            response =
                    zdt.toInstant();
        } 

        //else {
        //    Instant cur = java.time.Instant.from(value);
        //    response = cur.minus(Duration.ofNanos(TICK));
        //}

        if (prefix != null && Prefix.AP.compareTo(prefix) == 0 && response != null) {
            // Take the ChronoUnits into consideration with +/- 10%
            // And now we're at the upper bound of a range, and taking 10% from there. 
            response = generateUpperBoundApproximation(Instant.now(),response);
        }

        return response;
    }

    /**
     * Calculates a lower bound absolutely.
     * No matter if the time is AFTER the current time.
     * 
     * @param now
     * @param cur
     * @return
     */
    public static Instant generateUpperBoundApproximation(Instant now, Instant cur) {
        long nanoGap = ChronoUnit.NANOS.between(cur, now);
        return cur.plus(Duration.ofNanos(Math.abs(nanoGap)).dividedBy(10));
    }

    /**
     * Serializer encapsulates the logic to serialize to string.
     */
    public static String serialize(TemporalAccessor value) {
        return value.toString();
    }

    /**
     * parses the value into a set of bounds/value and adds to parameter value.
     * 
     * @param prefix
     * @param parameterValue
     * @param v
     * @throws FHIRSearchException
     */
    public static void parse(Prefix prefix, ParameterValue parameterValue, String v) throws FHIRSearchException {
        TemporalAccessor value = parse(v);
        parameterValue.setValueDate(generateValue(value, v));
        parameterValue.setValueDateLowerBound(generateLowerBound(prefix, value, v));
        parameterValue.setValueDateUpperBound(generateUpperBound(prefix, value, v));
    }

    /**
     * parses the value into a set of bounds/value and adds to parameter value.
     * 
     * @param value
     * @return
     * @throws FHIRSearchException
     */
    public static TemporalAccessor parse(String value) throws FHIRSearchException {
        try {
            return DATE_TIME_PARSER_FORMATTER.withResolverStyle(ResolverStyle.SMART).parseBest(value,
                    ZonedDateTime::from, LocalDateTime::from, LocalDate::from, YearMonth::from, Year::from);
        } catch (java.time.format.DateTimeParseException dtpe) {
            throw SearchExceptionUtil.buildNewDateTimeFormatException(dtpe);
        }
    }
}