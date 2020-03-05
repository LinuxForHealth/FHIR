/*
 * (C) Copyright IBM Corp. 2019, 2020
 * 
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class FHIRPathAbstractNode implements FHIRPathNode {
    protected final String name;
    protected final String path;
    protected final FHIRPathType type;
    protected final FHIRPathSystemValue value;
    protected final Collection<FHIRPathNode> children;
    
    protected FHIRPathAbstractNode(Builder builder) {
        name = builder.name;
        path = builder.path;
        type = Objects.requireNonNull(builder.type);
        value = builder.value;
        children = Collections.unmodifiableCollection(builder.children);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String name() {
        return name;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String path() {
        return path;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public FHIRPathType type() {
        return type;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasValue() {
        return value != null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public FHIRPathSystemValue getValue() {
        return value;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<FHIRPathNode> children() {
        return children;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<FHIRPathNode> descendants() {
        return stream().skip(1).collect(Collectors.toList());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<FHIRPathNode> stream() {
        return Stream.concat(Stream.of(this), children().stream().flatMap(FHIRPathNode::stream));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public final <T extends FHIRPathNode> boolean is(Class<T> nodeType) {
        return nodeType.isInstance(this);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public final <T extends FHIRPathNode> T as(Class<T> nodeType) {
        return nodeType.cast(this);
    }
    
    /**
     * Convert this {@link FHIRPathNode} instance into a {@link FHIRPathNode.Builder} instance
     * 
     * @return
     *     a new {@link FHIRPathNode.Builder} instance containing the fields from this {@link FHIRPathNode} instance
     */
    public abstract Builder toBuilder();
    
    public static abstract class Builder implements FHIRPathNode.Builder {
        // required
        protected final FHIRPathType type;
        
        // optional
        protected String name;
        protected String path;
        protected FHIRPathSystemValue value;
        protected Collection<FHIRPathNode> children = new ArrayList<>();
        
        protected Builder(FHIRPathType type) {
            super();
            this.type = type;
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public Builder path(String path) {
            this.path = path;
            return this;
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public Builder value(FHIRPathSystemValue value) {
            children.remove(this.value);
            this.value = value;
            children.add(this.value);
            return this;
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public Builder children(FHIRPathNode... children) {
            for (FHIRPathNode child : children) {
                this.children.add(child);
            }
            return this;
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public Builder children(Collection<FHIRPathNode> children) {
            this.children.addAll(children);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public abstract FHIRPathNode build();
    }
}