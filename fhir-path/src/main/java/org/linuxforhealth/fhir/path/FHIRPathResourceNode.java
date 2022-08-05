/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.path;

import java.util.Collection;
import java.util.Objects;

import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.path.visitor.FHIRPathNodeVisitor;

/**
 * A {@link FHIRPathNode} that wraps a {@link Resource}
 */
public class FHIRPathResourceNode extends FHIRPathAbstractNode {
    private final Resource resource;
    private final FHIRPathTree tree;

    private FHIRPathResourceNode(Builder builder) {
        super(builder);
        this.resource = builder.resource;
        this.tree = builder.tree;
    }

    @Override
    public boolean isResourceNode() {
        return true;
    }

    /**
     * The {@link Resource} wrapped by this FHIRPathResourceNode
     *
     * @return
     *     the {@link Resource} wrapped by this FHIRPathResourceNode
     */
    public Resource resource() {
        return resource;
    }

    /**
     * The {@link FHIRPathTree} that contains this FHIRPathResourceNode
     *
     * @return
     *     the {@link FHIRPathTree} that contains this FHIRPathResourceNode, otherwise null
     */
    public FHIRPathTree getTree() {
        return tree;
    }

    /**
     * Static factory method for creating FHIRPathResourceNode instances from a {@link Resource}
     *
     * @param resource
     *     the resource
     * @return
     *     a new FHIRPathResource instance
     */
    public static FHIRPathResourceNode resourceNode(Resource resource) {
        return FHIRPathResourceNode.builder(resource).build();
    }

    /**
     * Static factory method for creating named FHIRPathResourceNode instances from a {@link Resource}
     *
     * @param name
     *     the name
     * @param resource
     *     the resource
     * @return
     *     a new FHIRPathResourceNode instance
     */
    public static FHIRPathResourceNode resourceNode(String name, Resource resource) {
        return FHIRPathResourceNode.builder(resource).name(name).build();
    }

    /**
     * Static factory method for creating FHIRPathResourceNode instances from a {@link FHIRPathType}
     *
     * @param type
     *     the type
     * @return
     *     a new FHIRPathResourceNode instance
     */
    public static FHIRPathResourceNode resourceNode(FHIRPathType type) {
        return new Builder(type, null).path(type.getName()).build();
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder(type, resource);
        builder.name = name;
        builder.value = value;
        builder.children = children;
        return builder;
    }

    /**
     * Static factory method for creating builder instances from a {@link Resource}
     *
     * @param resource
     *     the resource
     * @return
     *     a new builder for building FHIRPathResource instances
     */
    public static Builder builder(Resource resource) {
        return new Builder(FHIRPathType.from(resource.getClass()), resource);
    }

    public static class Builder extends FHIRPathAbstractNode.Builder {
        private final Resource resource;
        private FHIRPathTree tree;

        protected Builder(FHIRPathType type, Resource resource) {
            super(type);
            this.resource = resource;
        }

        public Builder tree(FHIRPathTree tree) {
            this.tree = tree;
            return this;
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
        public Builder children(FHIRPathNode... children) {
            return (Builder) super.children(children);
        }

        @Override
        public Builder children(Collection<FHIRPathNode> children) {
            return (Builder) super.children(children);
        }

        /**
         * Build a FHIRPathResourceNode using this builder
         *
         * @return
         *     a new FHIRPathResourceNode instance
         */
        @Override
        public FHIRPathResourceNode build() {
            return new FHIRPathResourceNode(this);
        }
    }

    /**
     * This method is not supported for this FHIRPathResourceNode
     */
    @Override
    public int compareTo(FHIRPathNode node) {
        throw new UnsupportedOperationException();
    }

    /**
     * Indicates whether this FHIRPathResourceNode is equal to the parameter
     *
     * @param obj
     *     the other {@link Object}
     * @return
     *     true if the {@link Resource} wrapped by this FHIRResourceNode is equal to the {@link Resource} wrapped by the parameter,
     *     otherwise false
     */
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
        FHIRPathResourceNode other = (FHIRPathResourceNode) obj;
        return Objects.equals(resource, other.resource());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(resource);
    }

    @Override
    public void accept(FHIRPathNodeVisitor visitor) {
        visitor.visit(this);
    }
}
