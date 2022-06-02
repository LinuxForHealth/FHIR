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
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAmount;
import java.util.Objects;

import com.ibm.fhir.path.visitor.FHIRPathNodeVisitor;

/**
 * A {@FHIRPathTemporalValue} node that wraps a {@link TemporalAccessor} date value
 */
public class FHIRPathDateValue extends FHIRPathAbstractTemporalValue {
    public static final DateTimeFormatter PARSER_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("yyyy")
            .optionalStart()
                .appendPattern("-MM")
                .optionalStart()
                    .appendPattern("-dd")
                .optionalEnd()
            .optionalEnd()
            .toFormatter();

    private final TemporalAccessor date;

    protected FHIRPathDateValue(Builder builder) {
        super(builder);
        date = builder.date;
    }

    @Override
    public boolean isDateValue() {
        return true;
    }

    @Override
    public boolean isPartial() {
        return !ChronoField.DAY_OF_MONTH.equals(precision);
    }

    /**
     * The date value wrapped by this FHIRPathDateValue node
     *
     * @return
     *     the date value wrapped by this FHIRPathDateValue node
     */
    public TemporalAccessor date() {
        return date;
    }

    /**
     * Static factory method for creating FHIRPathDateValue instances from a {@link String} value
     *
     * @param text
     *     the text that is parsed into a {@link TemporalAccessor} date value
     * @return
     *     a new FHIRPathDateValue instance
     */
    public static FHIRPathDateValue dateValue(String text) {
        TemporalAccessor date = PARSER_FORMATTER.parseBest(text, LocalDate::from, YearMonth::from, Year::from);
        ChronoField precision = getPrecision(date);
        return FHIRPathDateValue.builder(date, precision).text(text).build();
    }

    /**
     * Static factory method for creating FHIRPathDateValue instances from a {@link TemporalAccessor} date value
     *
     * @param dateTime
     *     the {@link TemporalAccessor} date value
     * @return
     *     a new FHIRPathDateValue instance
     */
    public static FHIRPathDateValue dateValue(TemporalAccessor date) {
        return FHIRPathDateValue.builder(date, getPrecision(date)).build();
    }

    /**
     * Static factory method for creating named FHIRPathDateValue instances from a {@link TemporalAccessor} date value
     *
     * @param path
     *     the path of the FHIRPathNode
     * @param name
     *     the name
     * @param dateTime
     *     the {@link TemporalAccessor} date value
     * @return
     *     a new named FHIRPathDateValue instance
     */
    public static FHIRPathDateValue dateValue(String path, String name, TemporalAccessor date) {
        return FHIRPathDateValue.builder(date, getPrecision(date)).name(name).path(path).build();
    }

    @Override
    public Builder toBuilder() {
        return new Builder(type, date, precision);
    }

    /**
     * Static factory method for creating builder instances from a {@link TemporalAccessor} date value and a precision
     *
     * @param date
     *     the {@link TemporalAccessor} date value
     * @param precision
     *     the precision
     * @return
     *     a new builder for building FHIRPathDateValue instances
     */
    public static Builder builder(TemporalAccessor date, ChronoField precision) {
        return new Builder(FHIRPathType.SYSTEM_DATE, date, precision);
    }

    public static class Builder extends FHIRPathAbstractTemporalValue.Builder {
        private final TemporalAccessor date;

        private Builder(FHIRPathType type, TemporalAccessor dateTime, ChronoField precision) {
            super(type, dateTime, precision);
            this.date = dateTime;
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
         * Build a FHIRPathDateValue instance using this builder
         *
         * @return
         *     a new FHIRPathDateValue instance
         */
        @Override
        public FHIRPathDateValue build() {
            return new FHIRPathDateValue(this);
        }
    }

    @Override
    public FHIRPathDateValue add(FHIRPathQuantityValue quantityValue) {
        TemporalAmount temporalAmount = getTemporalAmount(quantityValue);
        return dateValue(getTemporalAccessor(temporal.plus(temporalAmount), date.getClass()));
    }

    @Override
    public FHIRPathDateValue subtract(FHIRPathQuantityValue quantityValue) {
        TemporalAmount temporalAmount = getTemporalAmount(quantityValue);
        return dateValue(getTemporalAccessor(temporal.minus(temporalAmount), date.getClass()));
    }

    /**
     * Indicates whether the date value wrapped by this FHIRPathDateValue node is equal the parameter (or its primitive value)
     *
     * @param obj
     *     the other {@link Object}
     * @return
     *     true if the date value wrapped by this FHIRPathDateValue node is equal the parameter (or its primitive value), otherwise false
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
            return Objects.equals(date, temporalValue.temporalAccessor()) &&
                    Objects.equals(precision, temporalValue.precision());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, precision);
    }

    @Override
    public String toString() {
        return (text != null) ? text : PARSER_FORMATTER.format(date);
    }

    @Override
    public void accept(FHIRPathNodeVisitor visitor) {
        visitor.visit(this);
    }
}
