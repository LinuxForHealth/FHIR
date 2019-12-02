/*
 * (C) Copyright IBM Corp. 2019
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
    
    @Override
    public boolean isDateValue() {
        return true;
    }
    
    public boolean isPartial() {
        return !(date instanceof LocalDate);
    }
    
    @Override
    public TemporalAccessor temporalAccessor() {
        return date;
    }
    
    public TemporalAccessor date() {
        return date;
    }
    
    @Override
    public Temporal temporal() {
        return temporal;
    }
    
    public static FHIRPathDateValue dateValue(String date) {
        return FHIRPathDateValue.builder(DATE_PARSER_FORMATTER.parseBest(date, LocalDate::from, YearMonth::from, Year::from)).build();
    }
    
    public static FHIRPathDateValue dateValue(TemporalAccessor date) {
        return FHIRPathDateValue.builder(date).build();
    }
    
    public static FHIRPathDateValue dateValue(String name, TemporalAccessor date) {
        return FHIRPathDateValue.builder(date).name(name).build();
    }

    @Override
    public Builder toBuilder() {
        return new Builder(type, date);
    }
    
    public static Builder builder(TemporalAccessor date) {
        return new Builder(FHIRPathType.SYSTEM_DATE, date);
    }
    
    public static class Builder extends FHIRPathAbstractNode.Builder {
        private final TemporalAccessor date;
        
        private Builder(FHIRPathType type, TemporalAccessor dateTime) {
            super(type);
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
        public Builder value(FHIRPathSystemValue value) {
            return this;
        }
        
        @Override
        public Builder children(FHIRPathNode... children) {
            return this;
        }
        
        @Override
        public Builder children(Collection<FHIRPathNode> children) {
            return this;
        }

        @Override
        public FHIRPathDateValue build() {
            return new FHIRPathDateValue(this);
        }
    }
    
    @Override
    public FHIRPathDateValue add(FHIRPathQuantityValue quantityValue) {
        Temporal temporal = getTemporal(date);
        TemporalAmount temporalAmount = getTemporalAmount(quantityValue);
        return dateValue(getTemporalAccessor(temporal.plus(temporalAmount), date.getClass()));
    }
    
    @Override
    public FHIRPathDateValue subtract(FHIRPathQuantityValue quantityValue) {
        Temporal temporal = getTemporal(date);
        TemporalAmount temporalAmount = getTemporalAmount(quantityValue);
        return dateValue(getTemporalAccessor(temporal.minus(temporalAmount), date.getClass()));
    }
    
    @Override
    public boolean isComparableTo(FHIRPathNode other) {
        if (other instanceof FHIRPathTemporalValue || other.getValue() instanceof FHIRPathTemporalValue) {
            FHIRPathTemporalValue temporalValue = (other instanceof FHIRPathTemporalValue) ? 
                    (FHIRPathTemporalValue) other : (FHIRPathTemporalValue) other.getValue();
            return date.getClass().equals(temporalValue.temporalAccessor().getClass());
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

    @Override
    public int hashCode() {
        return Objects.hashCode(date);
    }

    @Override
    public String toString() {
        return DATE_PARSER_FORMATTER.format(date);
    }
    
    @Override
    public void accept(FHIRPathNodeVisitor visitor) {
        visitor.visit(this);
    }
}
