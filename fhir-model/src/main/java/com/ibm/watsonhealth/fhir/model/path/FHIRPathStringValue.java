/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path;

import java.util.Collection;
import java.util.Objects;

public class FHIRPathStringValue extends FHIRPathAbstractNode implements FHIRPathPrimitiveValue {
    private final String string;
    
    protected FHIRPathStringValue(Builder builder) {
        super(builder);
        string = builder.string;
    }
    
    @Override
    public boolean isStringValue() {
        return true;
    }
    
    public String string() {
        return string;
    }
    
    public FHIRPathStringValue concat(FHIRPathStringValue node) {
        return stringValue(string.concat(node.string()));
    }
    
    public static FHIRPathStringValue stringValue(String string) {
        return FHIRPathStringValue.builder(string).build();
    }
    
    public static FHIRPathStringValue stringValue(String name, String string) {
        return FHIRPathStringValue.builder(string).name(name).build();
    }

    @Override
    public Builder toBuilder() {
        return new Builder(type, string);
    }
    
    public static Builder builder(String string) {
        return new Builder(FHIRPathType.SYSTEM_STRING, string);
    }
    
    public static class Builder extends FHIRPathAbstractNode.Builder {
        private final String string;
        
        private Builder(FHIRPathType type, String string) {
            super(type);
            this.string = string;
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
        public FHIRPathStringValue build() {
            return new FHIRPathStringValue(this);
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
        FHIRPathStringValue other = (FHIRPathStringValue) obj;
        return Objects.equals(string, other.string());
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(string);
    }
}
