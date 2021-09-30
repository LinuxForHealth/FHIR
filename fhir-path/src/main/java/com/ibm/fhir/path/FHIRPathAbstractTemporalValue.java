/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path;

import static com.ibm.fhir.path.util.FHIRPathUtil.getTemporal;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.ibm.fhir.path.visitor.FHIRPathNodeVisitor;

public abstract class FHIRPathAbstractTemporalValue extends FHIRPathAbstractSystemValue implements FHIRPathTemporalValue {
    private static final List<ChronoField> FIELDS = Arrays.asList(
            ChronoField.YEAR,
            ChronoField.MONTH_OF_YEAR,
            ChronoField.DAY_OF_MONTH,
            ChronoField.HOUR_OF_DAY,
            ChronoField.MINUTE_OF_HOUR,
            ChronoField.SECOND_OF_MINUTE,
            ChronoField.MICRO_OF_SECOND,
            ChronoField.OFFSET_SECONDS);

    private static final Map<ChronoField, Integer> FIELD_INDEX_MAP = buildFieldIndexMap();

    protected final TemporalAccessor temporalAccessor;
    protected final ChronoField precision;
    protected final Temporal temporal;
    protected final String text;

    protected FHIRPathAbstractTemporalValue(Builder builder) {
        super(builder);
        temporalAccessor = Objects.requireNonNull(builder.temporalAccessor);
        precision = Objects.requireNonNull(builder.precision);
        temporal = getTemporal(temporalAccessor);
        text = builder.text;
    }

    @Override
    public TemporalAccessor temporalAccessor() {
        return temporalAccessor;
    }

    @Override
    public Temporal temporal() {
        return temporal;
    }

    @Override
    public ChronoField precision() {
        return precision;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public boolean isSupported(ChronoField field) {
        if (!FIELD_INDEX_MAP.containsKey(field)) {
            return false;
        }
        return fieldIndex(precision) >= fieldIndex(field);
    }

    @Override
    public abstract boolean isPartial();

    @Override
    public abstract FHIRPathTemporalValue add(FHIRPathQuantityValue quantityValue);

    @Override
    public abstract FHIRPathTemporalValue subtract(FHIRPathQuantityValue quantityValue);

    @Override
    public abstract Builder toBuilder();

    public static abstract class Builder extends FHIRPathAbstractSystemValue.Builder {
        protected final TemporalAccessor temporalAccessor;
        protected final ChronoField precision;
        protected String text;

        protected Builder(FHIRPathType type, TemporalAccessor temporalAccessor, ChronoField precision) {
            super(type);
            this.temporalAccessor = temporalAccessor;
            this.precision = precision;
        }

        @Override
        public Builder name(String name) {
            return (Builder) super.name(name);
        }

        @Override
        public Builder path(String path) {
            return (Builder) super.path(path);
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        @Override
        public abstract FHIRPathTemporalValue build();
    }

    @Override
    public boolean isComparableTo(FHIRPathNode other) {
        if (other instanceof FHIRPathTemporalValue || other.getValue() instanceof FHIRPathTemporalValue) {
            FHIRPathTemporalValue temporalValue = (other instanceof FHIRPathTemporalValue) ?
                    (FHIRPathTemporalValue) other : (FHIRPathTemporalValue) other.getValue();

            if ((temporalAccessor instanceof LocalTime && !(temporalValue.temporalAccessor() instanceof LocalTime))
                    || (!(temporalAccessor instanceof LocalTime) && temporalValue.temporalAccessor() instanceof LocalTime)) {
                return false;
            }

            int startIndex = 0;

            if (temporalAccessor instanceof LocalTime && temporalValue.temporalAccessor() instanceof LocalTime) {
                startIndex = fieldIndex(ChronoField.HOUR_OF_DAY);
            }

            for (int i = startIndex; i < FIELDS.size(); i++) {
                ChronoField field = FIELDS.get(i);

                if (!isSupported(field) || !temporalValue.isSupported(field)) {
                    return false;
                }

                int result = compareTo(temporalAccessor, temporalValue.temporalAccessor(), field);

                if (result != 0) {
                    return true;
                }

                if (precision.equals(temporalValue.precision()) && precision.equals(field)) {
                    break;
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public int compareTo(FHIRPathNode other) {
        if (!isComparableTo(other)) {
            throw new IllegalArgumentException();
        }

        FHIRPathTemporalValue temporalValue = (other instanceof FHIRPathTemporalValue) ?
                (FHIRPathTemporalValue) other : (FHIRPathTemporalValue) other.getValue();

        if (temporalAccessor instanceof ZonedDateTime && temporalValue.temporalAccessor() instanceof ZonedDateTime) {
            return compareTo((ZonedDateTime) temporalAccessor, (ZonedDateTime) temporalValue.temporalAccessor());
        }

        int startIndex = 0;

        if (temporalAccessor instanceof LocalTime && temporalValue.temporalAccessor() instanceof LocalTime) {
            startIndex = fieldIndex(ChronoField.HOUR_OF_DAY);
        }

        int result = 0;
        for (int i = startIndex; i < FIELDS.size(); i++) {
            ChronoField field = FIELDS.get(i);

            result = compareTo(temporalAccessor, temporalValue.temporalAccessor(), field);

            if (result != 0) {
                return result;
            }

            if (precision.equals(temporalValue.precision()) && precision.equals(field)) {
                break;
            }
        }

        return result;
    }

    @Override
    public abstract void accept(FHIRPathNodeVisitor visitor);

    protected int compareTo(ZonedDateTime left, ZonedDateTime right) {
        if (left.isBefore(right)) {
            return -1;
        } else if (left.isAfter(right)) {
            return 1;
        }
        return 0;
    }

    private int compareTo(TemporalAccessor left, TemporalAccessor right, ChronoField field) {
        return left.get(field) - right.get(field);
    }

    private int fieldIndex(ChronoField field) {
        return FIELD_INDEX_MAP.get(field);
    }

    private static Map<ChronoField, Integer> buildFieldIndexMap() {
        Map<ChronoField, Integer> fieldIndexMap = new LinkedHashMap<>();
        int index = 0;
        for (ChronoField field : FIELDS) {
            fieldIndexMap.put(field, index++);
        }
        return Collections.unmodifiableMap(fieldIndexMap);
    }
}
