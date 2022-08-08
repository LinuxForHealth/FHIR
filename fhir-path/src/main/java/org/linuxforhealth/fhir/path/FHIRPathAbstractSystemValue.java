/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.path;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Stream;

/**
 * Abstract base class for all {@link FHIRPathSystemValue} implementations
 *
 * <p>{@link FHIRPathSystemValue} is a terminal {@link FHIRPathNode} and implementations will never have a child system value, children, or descendants.
 */
public abstract class FHIRPathAbstractSystemValue extends FHIRPathAbstractNode implements FHIRPathSystemValue {
    protected FHIRPathAbstractSystemValue(Builder builder) {
        super(builder);
    }

    /**
     * This method always returns an empty {@link Collection}
     *
     * @return
     *     an empty {@link Collection}
     */
    @Override
    public final Collection<FHIRPathNode> children() {
        return Collections.emptyList();
    }

    /**
     * This method always returns an empty {@link Stream}
     *
     * @return
     *     an empty {@link Stream}
     */
    @Override
    public final Stream<FHIRPathNode> stream() {
        return Stream.empty();
    }

    /**
     * This method always returns an empty {@link Collection}
     *
     * @return
     *     an empty {@link Stream}
     */
    @Override
    public final Collection<FHIRPathNode> descendants() {
        return Collections.emptyList();
    }

    @Override
    public abstract Builder toBuilder();

    public static abstract class Builder extends FHIRPathAbstractNode.Builder {
        protected Builder(FHIRPathType type) {
            super(type);
        }

        @Override
        public Builder name(String name) {
            return (Builder) super.name(name);
        }

        @Override
        public Builder path(String path) {
            return (Builder) super.path(path);
        }

        /**
         * This builder method is not supported - implementations will never have a child system value
         *
         * @throws
         *     UnsupportedOperationException
         */
        @Override
        public final Builder value(FHIRPathSystemValue value) {
            throw new UnsupportedOperationException("Builder method not supported");
        }

        /**
         * This builder method is not supported - implementations will never have children
         *
         * @throws
         *     UnsupportedOperationException
         */
        @Override
        public final Builder children(FHIRPathNode... children) {
            throw new UnsupportedOperationException("Builder method not supported");
        }

        /**
         * This builder method is not supported - implementations will never have descendants
         *
         * @throws
         *     UnsupportedOperationException
         */
        @Override
        public final Builder children(Collection<FHIRPathNode> children) {
            throw new UnsupportedOperationException("Builder method not supported");
        }

        @Override
        public abstract FHIRPathSystemValue build();
    }
}
