/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path;

import static com.ibm.fhir.path.util.FHIRPathUtil.getTemporal;
import static com.ibm.fhir.path.util.FHIRPathUtil.getTemporalAccessor;
import static com.ibm.fhir.path.util.FHIRPathUtil.getTemporalAmount;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAmount;
import java.util.Collection;
import java.util.Objects;

import com.ibm.fhir.path.visitor.FHIRPathNodeVisitor;

/**
 * A {@FHIRPathTemporalValue} node that wraps a {@link TemporalAccessor} date value
 */
public class FHIRPathDateValue extends FHIRPathAbstractNode implements FHIRPathTemporalValue {
    private static final DateTimeFormatter DATE_PARSER_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("yyyy")
            .optionalStart()
                .appendPattern("-MM")
                .optionalStart()
                    .appendPattern("-dd")
                .optionalEnd()
            .optionalEnd()
            .toFormatter();

    private final TemporalAccessor date;
    private final Temporal temporal;
    
    protected FHIRPathDateValue(Builder builder) {
        super(builder);
        date = builder.date;
        temporal = getTemporal(date);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDateValue() {
        return true;
    }
    
    /**
     * Indicates whether the date value wrapped by this FHIRPathDateValue node is partial
     * 
     * @return
     *     true if the date value wrapped by this FHIRPathDateValue node is partial, otherwise false
     */
    public boolean isPartial() {
        return !(date instanceof LocalDate);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public TemporalAccessor temporalAccessor() {
        return date;
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
     * {@inheritDoc}
     */
    @Override
    public Temporal temporal() {
        return temporal;
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
        return FHIRPathDateValue.builder(DATE_PARSER_FORMATTER.parseBest(text, LocalDate::from, YearMonth::from, Year::from)).build();
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
        return FHIRPathDateValue.builder(date).build();
    }
    
    /**
     * Static factory method for creating named FHIRPathDateValue instances from a {@link TemporalAccessor} date value
     * 
     * @param name
     *     the name
     * @param dateTime
     *     the {@link TemporalAccessor} date value
     * @return
     *     a new named FHIRPathDateValue instance
     */
    public static FHIRPathDateValue dateValue(String name, TemporalAccessor date) {
        return FHIRPathDateValue.builder(date).name(name).build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Builder toBuilder() {
        return new Builder(type, date);
    }
    
    /**
     * Static factory method for creating builder instances from a {@link TemporalAccessor} date value
     * 
     * @param date
     *     the {@link TemporalAccessor} date value
     * @return
     *     a new builder for building FHIRPathDateValue instances
     */
    public static Builder builder(TemporalAccessor date) {
        return new Builder(FHIRPathType.SYSTEM_DATE, date);
    }
    
    public static class Builder extends FHIRPathAbstractNode.Builder {
        private final TemporalAccessor date;
        
        private Builder(FHIRPathType type, TemporalAccessor dateTime) {
            super(type);
            this.date = dateTime;
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public Builder name(String name) {
            return (Builder) super.name(name);
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public Builder path(String path) {
            return (Builder) super.path(path);
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public Builder value(FHIRPathSystemValue value) {
            return this;
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public Builder children(FHIRPathNode... children) {
            return this;
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public Builder children(Collection<FHIRPathNode> children) {
            return this;
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
    
    /**
     * {@inheritDoc}
     */
    @Override
    public FHIRPathDateValue add(FHIRPathQuantityValue quantityValue) {
        Temporal temporal = getTemporal(date);
        TemporalAmount temporalAmount = getTemporalAmount(quantityValue);
        return dateValue(getTemporalAccessor(temporal.plus(temporalAmount), date.getClass()));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public FHIRPathDateValue subtract(FHIRPathQuantityValue quantityValue) {
        Temporal temporal = getTemporal(date);
        TemporalAmount temporalAmount = getTemporalAmount(quantityValue);
        return dateValue(getTemporalAccessor(temporal.minus(temporalAmount), date.getClass()));
    }
    
    /**
     * Indicates whether this FHIRPathDateValue is comparable to the parameter
     * 
     * @param other
     *     the other {@link FHIRPathNode}
     * @return
     *     true if the parameter or its primitive value is a {@link FHIRPathTemporalValue}, otherwise false
     */
    @Override
    public boolean isComparableTo(FHIRPathNode other) {
        if (other instanceof FHIRPathTemporalValue || other.getValue() instanceof FHIRPathTemporalValue) {
            FHIRPathTemporalValue temporalValue = (other instanceof FHIRPathTemporalValue) ? 
                    (FHIRPathTemporalValue) other : (FHIRPathTemporalValue) other.getValue();
            return date.getClass().equals(temporalValue.temporalAccessor().getClass());
        }
        return false;
    }

    /**
     * Compare the date value wrapped by this FHIRPathDateValue node to the parameter
     * 
     * @param other
     *     the other {@link FHIRPathNode}
     * @return
     *     0 if the date value wrapped by this FHIRPathDateValue node is equal to the parameter; a positive value if this FHIRPathDateValue is after the parameter; and
     *     a negative value if this FHIRPathDateValue is before the parameter
     */
    @Override
    public int compareTo(FHIRPathNode other) {
        if (!isComparableTo(other)) {
            throw new IllegalArgumentException();
        }
        FHIRPathTemporalValue temporalValue = (other instanceof FHIRPathTemporalValue) ? 
                (FHIRPathTemporalValue) other : (FHIRPathTemporalValue) other.getValue();
        return compareTo(temporalValue.temporalAccessor());
    }

    private int compareTo(TemporalAccessor temporalAccessor) {
        if (date instanceof Year && temporalAccessor instanceof Year) {
            return ((Year) date).compareTo((Year) temporalAccessor);
        }
        if (date instanceof YearMonth && temporalAccessor instanceof YearMonth) {
            return ((YearMonth) date).compareTo((YearMonth) temporalAccessor);
        }
        return ((LocalDate) date).compareTo((LocalDate) temporalAccessor);
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
        if (!isComparableTo(other)) {
            return false;
        }
        FHIRPathTemporalValue temporalValue = (other instanceof FHIRPathTemporalValue) ? 
                (FHIRPathTemporalValue) other : (FHIRPathTemporalValue) other.getValue();
        return Objects.equals(date, temporalValue.temporalAccessor());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(date);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return DATE_PARSER_FORMATTER.format(date);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(FHIRPathNodeVisitor visitor) {
        visitor.visit(this);
    }
}
