/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path;

import static com.ibm.fhir.path.util.FHIRPathUtil.getPrecision;
import static com.ibm.fhir.path.util.FHIRPathUtil.getTemporalAmount;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.Objects;

import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.path.visitor.FHIRPathNodeVisitor;

/**
 * A {@link FHIRPathTemporalValue} node that wraps a {@link LocalTime} value
 */
public class FHIRPathTimeValue extends FHIRPathAbstractTemporalValue {
    public static final DateTimeFormatter PARSER_FORMATTER = new DateTimeFormatterBuilder()
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
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter();

    private final LocalTime time;

    protected FHIRPathTimeValue(Builder builder) {
        super(builder);
        time = builder.time;
    }

    @Override
    public boolean isTimeValue() {
        return true;
    }

    @Override
    public boolean isPartial() {
        return !ChronoField.MICRO_OF_SECOND.equals(precision);
    }

    /**
     * The {@link LocalTime} value that is wrapped by this FHIRPathTimeValue
     *
     * @return
     *     the {@link LocalTime} value that is wrapped by this FHIRPathTimeValue
     */
    public LocalTime time() {
        return time;
    }

    /**
     * Static factory method for creating FHIRPathTimeValue instances from a {@link String} value
     *
     * @param text
     *     the text that is parsed into a {@link LocalTime} value
     * @return
     *     a new FHIRPathTimeValue instance
     */
    public static FHIRPathTimeValue timeValue(String text) {
        LocalTime time = ModelSupport.truncateTime(LocalTime.parse(text, PARSER_FORMATTER), ChronoUnit.MICROS);
        ChronoField precision = getPrecision(time, text);
        return FHIRPathTimeValue.builder(time, precision).text(text).build();
    }

    /**
     * Static factory method for creating FHIRPathTimeValue instances from a {@link LocalTime} value
     *
     * @param time
     *     the {@link LocalTime} value
     * @return
     *     a new FHIRPathTimeValue instance
     */
    public static FHIRPathTimeValue timeValue(LocalTime time) {
        return FHIRPathTimeValue.builder(time, getPrecision(time)).build();
    }

    /**
     * Static factory method for creating named FHIRPathTimeValue instances from a {@link LocalTime} value
     *
     * @param path
     *     the path of the FHIRPathNode
     * @param name
     *     the name
     * @param time
     *     the {@link LocalTime} value
     * @return
     *     a new named FHIRPathTimeValue instance
     */
    public static FHIRPathTimeValue timeValue(String path, String name, LocalTime time) {
        return FHIRPathTimeValue.builder(time, getPrecision(time)).name(name).path(path).build();
    }

    @Override
    public Builder toBuilder() {
        return new Builder(type, time, precision);
    }

    /**
     * Static factory method for creating builder instances from a {@link LocalTime} value and a precision
     *
     * @param time
     *     the {@link LocalTime} value
     * @param precision
     *     the precision
     * @return
     *     a new builder for building FHIRPathTimeValue instances
     */
    public static Builder builder(LocalTime time, ChronoField precision) {
        return new Builder(FHIRPathType.SYSTEM_TIME, time, precision);
    }

    public static class Builder extends FHIRPathAbstractTemporalValue.Builder {
        private final LocalTime time;

        private Builder(FHIRPathType type, LocalTime time, ChronoField precision) {
            super(type, time, precision);
            this.time = time;
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
         * Build a FHIRPathTimeValue instance using this builder
         *
         * @return
         *     a new FHIRPathTimeValue instance
         */
        @Override
        public FHIRPathTimeValue build() {
            return new FHIRPathTimeValue(this);
        }
    }

    @Override
    public FHIRPathTimeValue add(FHIRPathQuantityValue quantityValue) {
        TemporalAmount temporalAmount = getTemporalAmount(quantityValue);
        return timeValue(LocalTime.from(temporal.plus(temporalAmount)));
    }

    @Override
    public FHIRPathTimeValue subtract(FHIRPathQuantityValue quantityValue) {
        TemporalAmount temporalAmount = getTemporalAmount(quantityValue);
        return timeValue(LocalTime.from(temporal.minus(temporalAmount)));
    }

    /**
     * Indicates whether the {@link LocalTime} value wrapped by this FHIRPathTimeValue node is equal the parameter (or its primitive value)
     *
     * @param obj
     *     the other {@link Object}
     * @return
     *     true if the date/time value wrapped by this FHIRPathTimeValue node is equal the parameter (or its primitive value), otherwise false
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
            return Objects.equals(time, temporalValue.temporalAccessor()) &&
                    Objects.equals(precision, temporalValue.precision());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, precision);
    }

    @Override
    public String toString() {
        return (text != null) ? text : PARSER_FORMATTER.format(time);
    }

    @Override
    public void accept(FHIRPathNodeVisitor visitor) {
        visitor.visit(this);
    }
}
