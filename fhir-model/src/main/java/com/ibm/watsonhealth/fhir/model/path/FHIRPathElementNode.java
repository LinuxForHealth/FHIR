/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path;

import java.util.Collection;
import java.util.Objects;

import com.ibm.watsonhealth.fhir.model.type.Element;

public class FHIRPathElementNode extends FHIRPathAbstractNode {
    private final Element element;
    
    private FHIRPathElementNode(Builder builder) {
        super(builder);
        this.element = builder.element;
    }
    
    public Element element() {
        return element;
    }
    
    @Override
    public boolean isElementNode() {
        return true;
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
        private final Element element;

        protected Builder(FHIRPathType type, Element element) {
            super(type);
            this.element = element;
        }
        
        public Builder name(String name) {
            return (Builder) super.name(name);
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
        FHIRPathElementNode other = (FHIRPathElementNode) obj;
        return Objects.equals(element, other.element());
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(element);
    }
    
    public String toString() {
        if (hasValue()) {
            return value.toString();
        }
        return super.toString();
    }
}
