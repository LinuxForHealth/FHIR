/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path;

import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAccessor;
import java.util.Collection;
import java.util.Objects;

public class FHIRPathTimeValue extends FHIRPathAbstractNode implements FHIRPathPrimitiveValue {
    private final TemporalAccessor time;
    
    protected FHIRPathTimeValue(Builder builder) {
        super(builder);
        time = builder.time;
    }
    
    @Override
    public boolean isTimeValue() {
        return true;
    }
    
    public boolean hasTimeZone() {
        return getTimeZone() != null;
    }
    
    public ZoneOffset getTimeZone() {
        if (time instanceof OffsetTime) {
            return ((OffsetTime) time).getOffset();
        }
        return null;
    }
    
    public TemporalAccessor time() {
        return time;
    }
    
    public static FHIRPathTimeValue timeValue(LocalTime time) {
        return FHIRPathTimeValue.builder(time).build();
    }
    
    public static FHIRPathTimeValue timeValue(OffsetTime time) {
        return FHIRPathTimeValue.builder(time).build();
    }
    
    public static FHIRPathTimeValue timeValue(String name, LocalTime time) {
        return FHIRPathTimeValue.builder(time).name(name).build();
    }
    
    public static FHIRPathTimeValue timeValue(String name, OffsetTime time) {
        return FHIRPathTimeValue.builder(time).name(name).build();
    }

    @Override
    public Builder toBuilder() {
        return new Builder(type, time);
    }
    
    public static Builder builder(TemporalAccessor time) {
        return new Builder(FHIRPathType.SYSTEM_TIME, time);
    }
    
    public static class Builder extends FHIRPathAbstractNode.Builder {
        private final TemporalAccessor time;
        
        private Builder(FHIRPathType type, TemporalAccessor time) {
            super(type);
            this.time = time;
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
        public Builder children(FHIRPathNode.Builder builder) {
            return this;
        }

        @Override
        public FHIRPathTimeValue build() {
            return new FHIRPathTimeValue(this);
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
        FHIRPathTimeValue other = (FHIRPathTimeValue) obj;
        return Objects.equals(time, other.time());
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(time);
    }
}
