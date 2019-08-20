/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path;

import java.util.Collection;
import java.util.Objects;

public class FHIRPathBooleanValue extends FHIRPathAbstractNode implements FHIRPathPrimitiveValue {
    public static final FHIRPathBooleanValue TRUE = FHIRPathBooleanValue.booleanValue(true);
    public static final FHIRPathBooleanValue FALSE = FHIRPathBooleanValue.booleanValue(false);
    
    private final Boolean _boolean;
    
    protected FHIRPathBooleanValue(Builder builder) {
        super(builder);
        _boolean = Objects.requireNonNull(builder._boolean);
    }
    
    @Override
    public boolean isBooleanValue() {
        return true;
    }
    
    public Boolean _boolean() {
        return _boolean;
    }
    
    public boolean isTrue() {
        return Boolean.TRUE.equals(_boolean);
    }
    
    public boolean isFalse() {
        return Boolean.FALSE.equals(_boolean);
    }
    
    public static FHIRPathBooleanValue booleanValue(Boolean _boolean) {
        return FHIRPathBooleanValue.builder(_boolean).build();
    }
    
    public static FHIRPathBooleanValue booleanValue(String name, Boolean _boolean) {
        return FHIRPathBooleanValue.builder(_boolean).name(name).build();
    }

    @Override
    public Builder toBuilder() {
        return new Builder(type, _boolean);
    }
    
    public static Builder builder(Boolean _boolean) {
        return new Builder(FHIRPathType.SYSTEM_BOOLEAN, _boolean);
    }
    
    public static class Builder extends FHIRPathAbstractNode.Builder {
        private final Boolean _boolean;
        
        private Builder(FHIRPathType type, Boolean _boolean) {
            super(type);
            this._boolean = _boolean;
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
        public FHIRPathBooleanValue build() {
            return new FHIRPathBooleanValue(this);
        }
    }
    
    public FHIRPathBooleanValue or(FHIRPathBooleanValue value) {
        return (_boolean || value._boolean) ? TRUE : FALSE; 
    }
    
    public FHIRPathBooleanValue xor(FHIRPathBooleanValue value) {
        return ((_boolean || value._boolean()) && !(_boolean && value._boolean())) ? TRUE : FALSE;
    }
    
    public FHIRPathBooleanValue and(FHIRPathBooleanValue value) {
        return (_boolean && value._boolean()) ? TRUE : FALSE;
    }
    
    public FHIRPathBooleanValue implies(FHIRPathBooleanValue value) {
        return (!_boolean || value._boolean()) ? TRUE : FALSE;
    }
    
    public FHIRPathBooleanValue not() {
        return _boolean ? FALSE : TRUE;
    }
    
    @Override
    public boolean isComparableTo(FHIRPathNode other) {
        return other instanceof FHIRPathBooleanValue || 
                other.getValue() instanceof FHIRPathBooleanValue;
    }

    @Override
    public int compareTo(FHIRPathNode other) {
        if (!isComparableTo(other)) {
            throw new IllegalArgumentException();
        }
        if (other instanceof FHIRPathBooleanValue) {
            return _boolean.compareTo(((FHIRPathBooleanValue) other)._boolean());
        }
        return _boolean.compareTo(((FHIRPathBooleanValue) other.getValue())._boolean());
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
        if (other instanceof FHIRPathBooleanValue) {
            return Objects.equals(_boolean, ((FHIRPathBooleanValue) other)._boolean());
        }
        if (other.getValue() instanceof FHIRPathBooleanValue) {
            return Objects.equals(_boolean, ((FHIRPathBooleanValue) other.getValue())._boolean());
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(_boolean);
    }
    
    @Override
    public String toString() {
        return _boolean ? "'true'" : "'false'";
    }

    @Override
    public <T> void accept(T param, FHIRPathNodeVisitor<T> visitor) {
        visitor.visit(param, this);
    }
}
