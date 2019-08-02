/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path;

import java.util.Collection;
import java.util.Objects;

import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.visitor.Visitable;

public class FHIRPathElementNode extends FHIRPathAbstractNode {
    protected final Element element;
    
    protected FHIRPathElementNode(Builder builder) {
        super(builder);
        this.element = builder.element;
    }
    
    public Element element() {
        return element;
    }
    
    @Override
    public Visitable visitable() {
        return element;
    }
    
    @Override
    public boolean isElementNode() {
        return true;
    }
    
    public boolean isQuantityNode() {
        return false;
    }
    
    public FHIRPathQuantityNode asQuantityNode() {
        return as(FHIRPathQuantityNode.class);
    }
    
    public static FHIRPathElementNode elementNode(Element element) {
        return FHIRPathElementNode.builder(element).build();
    }
    
    public static FHIRPathElementNode elementNode(String name, Element element) {
        return FHIRPathElementNode.builder(element).name(name).build();
    }
    
    @Override
    public Builder toBuilder() {
        Builder builder = new Builder(type, element);
        builder.name = name;
        builder.value = value;
        builder.children = children;
        return builder;
    }

    public static Builder builder(Element element) {
        return new Builder(FHIRPathType.from(element.getClass()), element);
    }
    
    public static class Builder extends FHIRPathAbstractNode.Builder {
        protected final Element element;

        protected Builder(FHIRPathType type, Element element) {
            super(type);
            this.element = element;
        }
        
        public Builder name(String name) {
            return (Builder) super.name(name);
        }
        
        @Override
        public Builder path(String path) {
            return (Builder) super.path(path);
        }
        
        public Builder value(FHIRPathPrimitiveValue value) {
            return (Builder) super.value(value);
        }
        
        public Builder children(FHIRPathNode... children) {
            return (Builder) super.children(children);
        }
        
        public Builder children(Collection<FHIRPathNode> children) {
            return (Builder) super.children(children);
        }

        @Override
        public FHIRPathElementNode build() {
            return new FHIRPathElementNode(this);
        }
    }
    
    @Override
    public boolean isComparableTo(FHIRPathNode other) {
        if (hasValue()) {
            if (other instanceof FHIRPathPrimitiveValue) {
                return getValue().isComparableTo(other);
            }
            if (other.hasValue()) {
                return getValue().isComparableTo(other.getValue());
            }
        }
        return false;
    }
    
    @Override
    public int compareTo(FHIRPathNode other) {
        if (!isComparableTo(other)) {
            throw new IllegalArgumentException();
        }
        if (other instanceof FHIRPathPrimitiveValue) {
            return getValue().compareTo(other);
        }
        return getValue().compareTo(other.getValue());
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
        if (hasValue()) {
            if (other instanceof FHIRPathPrimitiveValue) {
                return getValue().equals((FHIRPathPrimitiveValue) other);
            }
            if (other.hasValue()) {
                return getValue().equals(other.getValue());
            }
        }
        if (other instanceof FHIRPathElementNode) {
            return Objects.equals(element, ((FHIRPathElementNode) other).element());
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(element);
    }
    
    public String toString() {
        if (hasValue()) {
            return "FHIRPathElementNode: [type: " + type() + ", value: " + value.toString() + "]";
        }
        return super.toString();
    }
}
