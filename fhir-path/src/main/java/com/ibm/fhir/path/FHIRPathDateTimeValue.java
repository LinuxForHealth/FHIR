/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path;

import static com.ibm.fhir.path.util.FHIRPathUtil.getPrecision;
import static com.ibm.fhir.path.util.FHIRPathUtil.getTemporalAccessor;
import static com.ibm.fhir.path.util.FHIRPathUtil.getTemporalAmount;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAmount;
import java.util.Objects;

import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.path.visitor.FHIRPathNodeVisitor;

/**
 * A {@link FHIRPathTemporalValue} node that wraps a {@link TemporalAccessor} date/time value
 */
public class FHIRPathDateTimeValue extends FHIRPathAbstractTemporalValue {
    public static final DateTimeFormatter PARSER_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("yyyy")
            .optionalStart()
                .appendPattern("-MM")
                .optionalStart()
                    .appendPattern("-dd")
                .optionalEnd()
            .optionalEnd()
            .appendLiteral("T")
            .optionalStart()
                .appendPattern("HH")
                .optionalStart()
                    .appendPattern(":mm")
                    .optionalStart()
                        .appendPattern(":ss")
                        .optionalStart()
                            .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
                        .optionalEnd()
                    .optionalEnd()
                .optionalEnd()
                .optionalStart()
                    .appendPattern("XXX")
                .optionalEnd()
            .optionalEnd()
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter();

    private final TemporalAccessor dateTime;

    protected FHIRPathDateTimeValue(Builder builder) {
        super(builder);
        dateTime = builder.dateTime;
    }

    @Override
    public boolean isDateTimeValue() {
        return true;
    }

    @Override
    public boolean isPartial() {
        return !ChronoField.OFFSET_SECONDS.equals(precision);
    }

    /**
     * The date/time value wrapped by this FHIRPathDateTimeValue node
     *
     * @return
     *     the date/time value wrapped by this FHIRPathDateTimeValue node
     */
    public TemporalAccessor dateTime() {
        return dateTime;
    }

    /**
     * Static factory method for creating FHIRPathDateTimeValue instances from a {@link String} value
     *
     * @param text
     *     the {@link String} value that is parsed into a {@link TemporalAccessor} date/time
     * @return
     *     a new FHIRPathDateTimeValue instance
     */
    public static FHIRPathDateTimeValue dateTimeValue(String text) {
        TemporalAccessor dateTime = ModelSupport.truncateTime(
            text.contains("T") ?
                PARSER_FORMATTER.parseBest(text, ZonedDateTime::from, LocalDateTime::from, LocalDate::from, YearMonth::from, Year::from) :
                FHIRPathDateValue.PARSER_FORMATTER.parseBest(text, LocalDate::from, YearMonth::from, Year::from),
            ChronoUnit.MICROS);
        ChronoField precision = getPrecision(dateTime, text);
        return FHIRPathDateTimeValue.builder(dateTime, precision).text(text).build();
    }

    /**
     * Static factory method for creating FHIRPathDateTimeValue instances from a {@link TemporalAccessor} date/time value
     *
     * @param dateTime
     *     the {@link TemporalAccessor} date/time value
     * @return
     *     a new FHIRPathDateTimeValue instance
     */
    public static FHIRPathDateTimeValue dateTimeValue(TemporalAccessor dateTime) {
        return FHIRPathDateTimeValue.builder(dateTime, getPrecision(dateTime)).build();
    }

    /**
     * Static factory method for creating named FHIRPathDateTimeValue instances from a {@link TemporalAccessor} date/time value
     *
     * @param path
     *     the path of the FHIRPathNode
     * @param name
     *     the name
     * @param dateTime
     *     the {@link TemporalAccessor} date/time value
     * @return
     *     a new named FHIRPathDateTimeValue instance
     */
    public static FHIRPathDateTimeValue dateTimeValue(String path, String name, TemporalAccessor dateTime) {
        return FHIRPathDateTimeValue.builder(dateTime, getPrecision(dateTime)).name(name).path(path).build();
    }

    @Override
    public Builder toBuilder() {
        return new Builder(type, dateTime, precision);
    }

    /**
     * Static factory method for creating builder instances from a {@link TemporalAccess} date/time value with
     * a specified precision
     *
     * @param dateTime
     *     the {@link TemporalAccessor} date/time value
     * @param precision
     *     the precision
     * @return
     *     a new builder for building FHIRPathDateTimeValue instances
     */
    public static Builder builder(TemporalAccessor dateTime, ChronoField precision) {
        return new Builder(FHIRPathType.SYSTEM_DATE_TIME, dateTime, precision);
    }

    public static class Builder extends FHIRPathAbstractTemporalValue.Builder {
        private final TemporalAccessor dateTime;

        private Builder(FHIRPathType type, TemporalAccessor dateTime, ChronoField precision) {
            super(type, dateTime, precision);
            this.dateTime = dateTime;
        }

        @Override
        public Builder name(String name) {
            return (Builder) super.name(name);
        }

        @Override
        public Builder path(String path) {
            return (Builder) super.path(path);
        }

        @Override
        public Builder text(String text) {
            return (Builder) super.text(text);
        }

        /**
         * Build a FHIRPathDateTimeValue instance using this builder
         *
         * @return
         *     a new FHIRPathDateTimeValue instance
         */
        @Override
        public FHIRPathDateTimeValue build() {
            return new FHIRPathDateTimeValue(this);
        }
    }

    @Override
    public FHIRPathDateTimeValue add(FHIRPathQuantityValue quantityValue) {
        TemporalAmount temporalAmount = getTemporalAmount(quantityValue);
        return dateTimeValue(getTemporalAccessor(temporal.plus(temporalAmount), dateTime.getClass()));
    }

    @Override
    public FHIRPathDateTimeValue subtract(FHIRPathQuantityValue quantityValue) {
        TemporalAmount temporalAmount = getTemporalAmount(quantityValue);
        return dateTimeValue(getTemporalAccessor(temporal.minus(temporalAmount), dateTime.getClass()));
    }

    /**
     * Indicates whether the date/time value wrapped by this FHIRPathDateTimeValue node is equal the parameter (or its primitive value)
     *
     * @param obj
     *     the other {@link Object}
     * @return
     *     true if the date/time value wrapped by this FHIRPathDateTimeValue node is equal the parameter (or its primitive value), otherwise false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof FHIRPathNode)) {
            return false;
        }
        FHIRPathNode other = (FHIRPathNode) obj;
        if (other instanceof FHIRPathTemporalValue || other.getValue() instanceof FHIRPathTemporalValue) {
            FHIRPathTemporalValue temporalValue = (other instanceof FHIRPathTemporalValue) ?
                    (FHIRPathTemporalValue) other : (FHIRPathTemporalValue) other.getValue();
            if (dateTime instanceof ZonedDateTime && temporalValue.temporalAccessor() instanceof ZonedDateTime) {
                return compareTo((ZonedDateTime) dateTime, (ZonedDateTime) temporalValue.temporalAccessor()) == 0;
            }
            return Objects.equals(dateTime, temporalValue.temporalAccessor()) &&
                    Objects.equals(precision, temporalValue.precision());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime, precision);
    }

    @Override
    public String toString() {
        return (text != null) ? text : PARSER_FORMATTER.format(dateTime);
    }

    @Override
    public void accept(FHIRPathNodeVisitor visitor) {
        visitor.visit(this);
    }
}
