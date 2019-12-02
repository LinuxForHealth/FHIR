/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path;

import static com.ibm.fhir.path.util.FHIRPathUtil.getTemporal;
import static com.ibm.fhir.path.util.FHIRPathUtil.getTemporalAccessor;
import static com.ibm.fhir.path.util.FHIRPathUtil.getTemporalAmount;
import static com.ibm.fhir.path.util.FHIRPathUtil.getTimePrecision;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAmount;
import java.util.Collection;
import java.util.Objects;

import com.ibm.fhir.path.util.FHIRPathUtil.TimePrecision;
import com.ibm.fhir.path.visitor.FHIRPathNodeVisitor;

public class FHIRPathDateTimeValue extends FHIRPathAbstractNode implements FHIRPathTemporalValue {
    private static final DateTimeFormatter DATE_TIME_PARSER_FORMATTER = new DateTimeFormatterBuilder()
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
                            .appendFraction(ChronoField.MICRO_OF_SECOND, 0, 6, true)
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
    private final TimePrecision timePrecision;
    private final Temporal temporal;
    
    protected FHIRPathDateTimeValue(Builder builder) {
        super(builder);
        dateTime = builder.dateTime;
        timePrecision = builder.timePrecision;
        temporal = getTemporal(dateTime);
    }
    
    @Override
    public boolean isDateTimeValue() {
        return true;
    }
    
    public boolean isPartial() {
        return !(dateTime instanceof ZonedDateTime);
    }
    
    @Override
    public TemporalAccessor temporalAccessor() {
        return dateTime;
    }
    
    public TemporalAccessor dateTime() {
        return dateTime;
    }
    
    @Override
    public TimePrecision timePrecision() {
        return timePrecision;
    }
    
    @Override
    public Temporal temporal() {
        return temporal;
    }
    
    public static FHIRPathDateTimeValue dateTimeValue(String text) {
        return FHIRPathDateTimeValue.builder(DATE_TIME_PARSER_FORMATTER.parseBest(text, ZonedDateTime::from, LocalDateTime::from, LocalDate::from, YearMonth::from, Year::from), getTimePrecision(text)).build();
    }
    
    public static FHIRPathDateTimeValue dateTimeValue(TemporalAccessor dateTime) {
        return FHIRPathDateTimeValue.builder(dateTime, getTimePrecision(dateTime)).build();
    }
    
    public static FHIRPathDateTimeValue dateTimeValue(String name, TemporalAccessor dateTime) {
        return FHIRPathDateTimeValue.builder(dateTime, getTimePrecision(dateTime)).name(name).build();
    }

    @Override
    public Builder toBuilder() {
        return new Builder(type, dateTime, timePrecision);
    }
    
    public static Builder builder(TemporalAccessor dateTime, TimePrecision timePrecision) {
        return new Builder(FHIRPathType.SYSTEM_DATE_TIME, dateTime, timePrecision);
    }
    
    public static class Builder extends FHIRPathAbstractNode.Builder {
        private final TemporalAccessor dateTime;
        private final TimePrecision timePrecision;
        
        private Builder(FHIRPathType type, TemporalAccessor dateTime, TimePrecision timePrecision) {
            super(type);
            this.dateTime = dateTime;
            this.timePrecision = timePrecision;
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
        public FHIRPathDateTimeValue build() {
            return new FHIRPathDateTimeValue(this);
        }
    }
    
    @Override
    public FHIRPathDateTimeValue add(FHIRPathQuantityValue quantityValue) {
        Temporal temporal = getTemporal(dateTime);
        TemporalAmount temporalAmount = getTemporalAmount(quantityValue);
        return dateTimeValue(getTemporalAccessor(temporal.plus(temporalAmount), dateTime.getClass()));
    }
    
    @Override
    public FHIRPathDateTimeValue subtract(FHIRPathQuantityValue quantityValue) {
        Temporal temporal = getTemporal(dateTime);
        TemporalAmount temporalAmount = getTemporalAmount(quantityValue);
        return dateTimeValue(getTemporalAccessor(temporal.minus(temporalAmount), dateTime.getClass()));
    }
    
    @Override
    public boolean isComparableTo(FHIRPathNode other) {
        if (other instanceof FHIRPathTemporalValue || other.getValue() instanceof FHIRPathTemporalValue) {
            FHIRPathTemporalValue temporalValue = (other instanceof FHIRPathTemporalValue) ? 
                    (FHIRPathTemporalValue) other : (FHIRPathTemporalValue) other.getValue();
            return dateTime.getClass().equals(temporalValue.temporalAccessor().getClass()) && 
                    timePrecision.equals(temporalValue.timePrecision());
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
        if (dateTime instanceof Year && temporalAccessor instanceof Year) {
            return ((Year) dateTime).compareTo((Year) temporalAccessor);
        }
        if (dateTime instanceof YearMonth && temporalAccessor instanceof YearMonth) {
            return ((YearMonth) dateTime).compareTo((YearMonth) temporalAccessor);
        }
        if (dateTime instanceof LocalDate && temporalAccessor instanceof LocalDate) {
            return ((LocalDate) dateTime).compareTo((LocalDate) temporalAccessor);
        }
        if (dateTime instanceof LocalDateTime && temporalAccessor instanceof LocalDateTime) {
            return ((LocalDateTime) dateTime).compareTo((LocalDateTime) temporalAccessor);
        }
        return compareTo((ZonedDateTime) temporalAccessor);
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
        TemporalAccessor temporalAccessor = temporalValue.temporalAccessor();
        if (temporalAccessor instanceof ZonedDateTime) {
            return compareTo(temporalAccessor) == 0;
        } else {
            return Objects.equals(dateTime, temporalAccessor);
        }        
    }
    
    private int compareTo(ZonedDateTime other) {
        ZonedDateTime zdt = (ZonedDateTime) dateTime;
        if (zdt.isBefore(other)) {
            return -1;
        } else if (zdt.isAfter(other)) {
            return 1;
        }
        return 0;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(dateTime);
    }

    @Override
    public String toString() {
        return DATE_TIME_PARSER_FORMATTER.format(dateTime);
    }
    
    @Override
    public void accept(FHIRPathNodeVisitor visitor) {
        visitor.visit(this);
    }
}
