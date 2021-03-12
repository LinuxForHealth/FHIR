/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.graph.util;

import static com.ibm.fhir.model.util.ModelSupport.FHIR_BOOLEAN;
import static com.ibm.fhir.model.util.ModelSupport.FHIR_INTEGER;
import static com.ibm.fhir.model.util.ModelSupport.FHIR_STRING;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;

import org.slf4j.LoggerFactory;

import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Element;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public class FHIRTermGraphUtil {
    private FHIRTermGraphUtil() { }

    /**
     * Convert the given element value to an object value that is compatible with the graph schema.
     *
     * @param value
     *     the element value
     * @return
     *     an object value that is compatible with the graph schema
     */
    public static Object toObject(Element value) {
        if (value.is(FHIR_BOOLEAN)) {
            return value.as(FHIR_BOOLEAN).getValue();
        }
        if (value.is(Code.class)) {
            return value.as(Code.class).getValue();
        }
        if (value.is(DateTime.class)) {
            return DateTime.PARSER_FORMATTER.format(value.as(DateTime.class).getValue());
        }
        if (value.is(Decimal.class)) {
            return value.as(Decimal.class).getValue().doubleValue();
        }
        if (value.is(FHIR_INTEGER)) {
            return value.as(FHIR_INTEGER).getValue();
        }
        if (value.is(FHIR_STRING)) {
            return value.as(FHIR_STRING).getValue();
        }
        throw new IllegalArgumentException();
    }

    /**
     * Normalize the string by making it case and accent insensitive.
     *
     * @param value
     *     the string value to normalized
     * @return
     *     the normalized string value
     */
    public static String normalize(String value) {
        if (value != null) {
            return Normalizer.normalize(value, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
        }
        return null;
    }

    /**
     * Sets the root logger level for the logback classic root logger. See:
     * <a href="https://docs.janusgraph.org/basics/common-questions/#debug-level-logging-slows-execution">JanusGraph common questions</a>
     * for more information.
     *
     * @param level
     *     the level
     */
    public static void setRootLoggerLevel(Level level) {
        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(level);
    }

    /**
     * Convert the {@link DateTime} value to a Long value that is compatible with the graph schema.
     *
     * @param dateTime
     *     the dateTime value
     * @return
     *     the Long equivalent value (milliseconds from the epoch)
     */
    public static Long toLong(DateTime dateTime) {
        TemporalAccessor value = dateTime.getValue();
        if (value instanceof ZonedDateTime) {
            ZonedDateTime zonedDateTime = (ZonedDateTime) value;
            return zonedDateTime.toInstant().toEpochMilli();
        }
        if (value instanceof LocalDate) {
            LocalDate localDate = (LocalDate) value;
            return localDate.atStartOfDay().atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
        }
        if (value instanceof YearMonth) {
            YearMonth yearMonth = (YearMonth) value;
            return yearMonth.atDay(1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
        }
        if (value instanceof Year) {
            Year year = (Year) value;
            return year.atMonth(1).atDay(1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
        }
        throw new IllegalArgumentException();
    }
}
