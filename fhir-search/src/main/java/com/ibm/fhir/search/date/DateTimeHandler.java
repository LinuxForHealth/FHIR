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
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.logging.Logger;

import com.ibm.fhir.search.SearchConstants.Prefix;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.exception.SearchExceptionUtil;
import com.ibm.fhir.search.parameters.QueryParameterValue;

/**
 * Date Time Handler<br>
 * <a href=
 * "https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#patterns">Date
 * Time Formatter</a>
 */
public class DateTimeHandler {
    private static final Logger logger = Logger.getLogger(DateTimeHandler.class.getName());

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
                                    .appendPattern("XXX")
                                .optionalEnd()
                            .optionalEnd()
                            .optionalEnd()
                        .optionalEnd()
                    .optionalEnd()
                    .toFormatter();
    // @formatter:on

    public static Timestamp generateTimestamp(Instant inst) {
        return Timestamp.from(ZonedDateTime.ofInstant(inst, ZoneId.of("UTC")).toInstant());
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
            response = generateValue(value, originalString);
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
            response = ZonedDateTime.of(local, ZoneOffset.UTC).toInstant();
        } else if (value instanceof LocalDate) {
            // LocalDate - YYYY-MM-DD
            LocalDate ld = (LocalDate) value;
            LocalDateTime local = REFERENCE_DATE.with(ChronoField.YEAR, ld.getYear());
            local    = local.with(ChronoField.MONTH_OF_YEAR, ld.getMonthValue());
            local    = local.with(ChronoField.DAY_OF_MONTH, ld.getDayOfMonth());
            response = ZonedDateTime.of(local, ZoneOffset.UTC).toInstant();
        } else if (value instanceof LocalDateTime) {
            // LocalDate - YYYY-MM-DD HH
            LocalDateTime local = (LocalDateTime) value;
            response = ZonedDateTime.of(local, ZoneOffset.UTC).toInstant();
        } else if (value instanceof ZonedDateTime) {
            ZonedDateTime zdt = (ZonedDateTime) value;
            response = zdt.toInstant();
        } else {
            response = java.time.Instant.from(value);
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

        if (value instanceof java.time.Year) {
            // YEAR + 1
            Year year = (Year) value;
            year = year.plus(1, ChronoUnit.YEARS);
            LocalDateTime local = REFERENCE_DATE.with(ChronoField.YEAR, year.getValue());
            response =
                    ZonedDateTime.of(local, ZoneOffset.UTC).toInstant().minus(TICK,
                            ChronoUnit.NANOS);
        } else if (value instanceof YearMonth) {
            // Grab the values for Year/Month Value
            YearMonth ym = (YearMonth) value;
            ym = ym.plusMonths(1);
            LocalDateTime local = REFERENCE_DATE.with(ChronoField.YEAR, ym.getYear());
            local    = local.with(ChronoField.MONTH_OF_YEAR, ym.getMonthValue());
            response =
                    ZonedDateTime.of(local, ZoneOffset.UTC).toInstant().minus(TICK,
                            ChronoUnit.NANOS);
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
        } else if (value instanceof LocalDateTime) {
            // LocalDate - YYYY-MM-DD
            LocalDateTime local = (LocalDateTime) value;
            long precision = originalString.chars().filter(ch -> ch == ':' || ch == '.').count();
            if (precision == 0) {
                // HH - No Colon 
                local = local.plus(1, ChronoUnit.HOURS).minus(TICK, ChronoUnit.NANOS);
            } else if (precision == 1) {
                // HH:MM - First Colon 
                local = local.plus(1, ChronoUnit.MINUTES).minus(TICK, ChronoUnit.NANOS);
            } else if (precision == 2) {
                // HH:MM:SS - Second Colon
                local = local.truncatedTo(ChronoUnit.SECONDS).plus(1, ChronoUnit.SECONDS).minus(TICK, ChronoUnit.NANOS);
            }

            // ELSE -> HH:MM:SS.XXXXX
            // The point is treated as exact.
            response = ZonedDateTime.of(local, ZoneOffset.UTC).toInstant();

        } else if (value instanceof ZonedDateTime) {
            ZonedDateTime zdt = (ZonedDateTime) value;
            long precision = originalString.chars().filter(ch -> ch == ':' || ch == '.' || ch == 'Z' ).count();
            // Shift by 1 as the Zone includes a semicolon. 
            if (precision == 1) {
                // HH - First Colon (Zone is colon)
                // 2019-12-11T00+05:00
                zdt = zdt.plus(1, ChronoUnit.HOURS).minus(TICK, ChronoUnit.NANOS);
            } else if (precision == 2) {
                // HH:MM - Second Colon 
                // 2019-12-11T00:00+05:00
                zdt = zdt.plus(1, ChronoUnit.MINUTES).minus(TICK, ChronoUnit.NANOS);
            } else if (precision == 3) {
                // HH:MM:SS - Third Colon
                // 2019-12-11T00:00:00+05:00
                zdt = zdt.truncatedTo(ChronoUnit.SECONDS).plus(1, ChronoUnit.SECONDS).minus(TICK, ChronoUnit.NANOS);
            } else if (precision == 4) { 
                // Nanoseconds
                // 2019-12-11T00:00:00.000000+05:00
                zdt = zdt.truncatedTo(ChronoUnit.MILLIS).plus(1, ChronoUnit.MILLIS).minus(TICK, ChronoUnit.NANOS);
            }

            // ELSE -> HH:MM:SS.XXXXX - treat precise.
            response = zdt.toInstant();
        }

        if (prefix != null && Prefix.AP.compareTo(prefix) == 0 && response != null) {
            // Take the ChronoUnits into consideration with +/- 10%
            // And now we're at the upper bound of a range, and taking 10% from there. 
            response = generateUpperBoundApproximation(Instant.now(), response);
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
    public static void parse(Prefix prefix, QueryParameterValue parameterValue, String v) throws FHIRSearchException {
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

    /**
     * parses quiet the value into a set of bounds/value and adds to parameter
     * value.
     * 
     * @param value
     * @return
     */
    public static TemporalAccessor parseQuiet(String value) {
        try {
            return DATE_TIME_PARSER_FORMATTER.withResolverStyle(ResolverStyle.SMART).parseBest(value,
                    ZonedDateTime::from, LocalDateTime::from, LocalDate::from, YearMonth::from, Year::from);
        } catch (java.time.format.DateTimeParseException dtpe) {
            logger.fine("Error parsing a quiet value " + dtpe.toString());
            return null;
        }
    }
}