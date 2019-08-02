/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ibm.watsonhealth.fhir.model.visitor.Visitable;

public abstract class FHIRPathAbstractNode implements FHIRPathNode {
    protected final String name;
    protected final String path;
    protected final FHIRPathType type;
    protected final FHIRPathPrimitiveValue value;
    protected final Collection<FHIRPathNode> children;
    
    protected FHIRPathAbstractNode(Builder builder) {
        name = builder.name;
        path = builder.path;
        type = builder.type;
        value = builder.value;
        children = Collections.unmodifiableCollection(builder.children);
    }
    
    @Override
    public String name() {
        return name;
    }
    
    @Override
    public String path() {
        return path;
    }
    
    @Override
    public FHIRPathType type() {
        return type;
    }
    
    @Override
    public boolean hasValue() {
        return value != null;
    }
    
    @Override
    public FHIRPathPrimitiveValue getValue() {
        return value;
    }
    
    @Override
    public Collection<FHIRPathNode> children() {
        return children;
    }
    
    @Override
    public Collection<FHIRPathNode> descendants() {
        return stream().skip(1).collect(Collectors.toList());
    }
    
    @Override
    public Stream<FHIRPathNode> stream() {
        return Stream.concat(Stream.of(this), children().stream().flatMap(FHIRPathNode::stream));
    }
    
    @Override
    public Visitable visitable() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final <T extends FHIRPathNode> boolean is(Class<T> nodeType) {
        return nodeType.isInstance(this);
    }
    
    @Override
    public final <T extends FHIRPathNode> T as(Class<T> nodeType) {
        return nodeType.cast(this);
    }
    
    public abstract Builder toBuilder();
    
    public static abstract class Builder implements FHIRPathNode.Builder {
        // required
        protected final FHIRPathType type;
        
        // optional
        protected String name;
        protected String path;
        protected FHIRPathPrimitiveValue value;
        protected Collection<FHIRPathNode> children = new ArrayList<>();
        
        protected Builder(FHIRPathType type) {
            super();
            this.type = type;
        }
        
        @Override
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        @Override
        public Builder path(String path) {
            this.path = path;
            return this;
        }
        
        @Override
        public Builder value(FHIRPathPrimitiveValue value) {
            children.remove(this.value);
            this.value = value;
            children.add(this.value);
            return this;
        }
        
        @Override
        public Builder children(FHIRPathNode... children) {
            for (FHIRPathNode child : children) {
                this.children.add(child);
            }
            return this;
        }
        
        @Override
        public Builder children(Collection<FHIRPathNode> children) {
            this.children.addAll(children);
            return this;
        }

        @Override
        public abstract FHIRPathNode build();
    }
}
