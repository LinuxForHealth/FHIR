/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path;

import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Collection;
import java.util.Objects;

public class FHIRPathDateTimeValue extends FHIRPathAbstractNode implements FHIRPathPrimitiveValue {
    private final TemporalAccessor dateTime;
    
    protected FHIRPathDateTimeValue(Builder builder) {
        super(builder);
        dateTime = builder.dateTime;
    }
    
    @Override
    public boolean isDateTimeValue() {
        return true;
    }
    
    public boolean isPartial() {
        return !(dateTime instanceof ZonedDateTime);
    }
    
    public TemporalAccessor dateTime() {
        return dateTime;
    }
    
    public static FHIRPathDateTimeValue dateTimeValue(TemporalAccessor dateTime) {
        return FHIRPathDateTimeValue.builder(dateTime).build();
    }
    
    public static FHIRPathDateTimeValue dateTimeValue(String name, TemporalAccessor dateTime) {
        return FHIRPathDateTimeValue.builder(dateTime).name(name).build();
    }

    @Override
    public Builder toBuilder() {
        return new Builder(type, dateTime);
    }
    
    public static Builder builder(TemporalAccessor dateTime) {
        return new Builder(FHIRPathType.SYSTEM_DATE_TIME, dateTime);
    }
    
    public static class Builder extends FHIRPathAbstractNode.Builder {
        private final TemporalAccessor dateTime;
        
        private Builder(FHIRPathType type, TemporalAccessor dateTime) {
            super(type);
            this.dateTime = dateTime;
        }
        
        @Override
        public Builder name(String name) {
            return (Builder) super.name(name);
        }
        
        @Override
        public Builder value(FHIRPathPrimitiveValue value) {
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
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        FHIRPathDateTimeValue other = (FHIRPathDateTimeValue) obj;
        return Objects.equals(dateTime, other.dateTime());
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(dateTime);
    }
}
