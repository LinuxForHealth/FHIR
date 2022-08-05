/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.path;

import java.util.Collection;
import java.util.Objects;

import org.linuxforhealth.fhir.model.type.Element;
import org.linuxforhealth.fhir.path.visitor.FHIRPathNodeVisitor;

/**
 * A {@link FHIRPathNode} that wraps an {@link Element}
 */
public class FHIRPathElementNode extends FHIRPathAbstractNode {
    protected final Element element;
    protected final FHIRPathTree tree;

    protected FHIRPathElementNode(Builder builder) {
        super(builder);
        this.element = builder.element;
        this.tree = builder.tree;
    }

    /**
     * The {@link Element} wrapped by this FHIRPathElementNode
     *
     * @return
     *     the {@link Element} wrapped by this FHIRPathElementNode
     */
    public Element element() {
        return element;
    }

    /**
     * The {@link FHIRPathTree} that contains this FHIRPathElementNode
     *
     * @return
     *     the {@link FHIRPathTree} that contains this FHIRPathElementNode, otherwise null
     */
    public FHIRPathTree getTree() {
        return tree;
    }

    @Override
    public boolean isElementNode() {
        return true;
    }

    /**
     * Indicates whether this FHIRPathElementNode is type compatible with {@link FHIRPathQuantityNode}
     *
     * @return
     *     true if this FHIRPathElementNode is type compatible with {@link FHIRPathQuantityNode}, otherwise false
     */
    public boolean isQuantityNode() {
        return false;
    }

    /**
     * Cast this FHIRPathElementNode to a {@link FHIRPathQuantityNode}
     *
     * @return
     *     this FHIRPathElementNode as a {@link FHIRPathQuantityNode}
     * @throws
     *     {@link ClassCastException} if this FHIRPathElementNode is not type compatible with {@link FHIRPathQuantityNode}
     */
    public FHIRPathQuantityNode asQuantityNode() {
        return as(FHIRPathQuantityNode.class);
    }

    /**
     * Static factory method for creating FHIRPathElementNode instances from an {@link Element}
     *
     * @param element
     *     the element
     * @return
     *     a new FHIRPathElementNode instance
     */
    public static FHIRPathElementNode elementNode(Element element) {
        return FHIRPathElementNode.builder(element).build();
    }

    /**
     * Static factory method for creating named FHIRPathElementNode instances from an {@link Element}
     *
     * @param name
     *     the name
     * @param element
     *     the element
     * @return
     *     a new named FHIRPathElementNode instance
     */
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

    /**
     * Static factory method for creating builder instances from an {@link Element}
     *
     * @param element
     *     the element
     * @return
     *     a new builder for building FHIRPathElementNode instances
     */
    public static Builder builder(Element element) {
        return new Builder(FHIRPathType.from(element.getClass()), element);
    }

    public static class Builder extends FHIRPathAbstractNode.Builder {
        protected final Element element;
        protected FHIRPathTree tree;

        protected Builder(FHIRPathType type, Element element) {
            super(type);
            this.element = element;
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
        public Builder value(FHIRPathSystemValue value) {
            return (Builder) super.value(value);
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
         * Build a FHIRPathElementNode using this builder
         *
         * @return
         *     a new FHIRPathElementNode instance
         */
        @Override
        public FHIRPathElementNode build() {
            return new FHIRPathElementNode(this);
        }
    }

    /**
     * Indicates whether this FHIRPathElementNode has a primitive value and is comparable to the parameter
     *
     * @param other
     *     the other {@link FHIRPathNode}
     * @return
     *     true if this FHIRPathElementNode has a primitive value and is comparable to the primitive value of the parameter, otherwise false
     */
    @Override
    public boolean isComparableTo(FHIRPathNode other) {
        if (hasValue()) {
            if (other instanceof FHIRPathSystemValue) {
                return getValue().isComparableTo(other);
            }
            if (other.hasValue()) {
                return getValue().isComparableTo(other.getValue());
            }
        }
        return false;
    }

    /**
     * Compare the element wrapped by this FHIRPathElementNode to the parameter
     *
     * @param other
     *     the other {@link FHIRPathNode}
     * @return
     *     the result of comparing the primitive value of this FHIRPathElementNode to the primitive value of the parameter
     */
    @Override
    public int compareTo(FHIRPathNode other) {
        if (!isComparableTo(other)) {
            throw new IllegalArgumentException();
        }
        if (other instanceof FHIRPathSystemValue) {
            return getValue().compareTo(other);
        }
        return getValue().compareTo(other.getValue());
    }

    /**
     * Indicates whether this FHIRPathElementNode is equal to the parameter
     *
     * @param obj
     *     the other {@link Object}
     * @return
     *     true if the primitive value of this FHIRPathElementNode is equal to the primitive value of the parameter or the {@link Element}
     *     wrapped by this FHIRPathElementNode is equal to the {@link Element} wrapped by the parameter, otherwise false
     */
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
            if (other instanceof FHIRPathSystemValue) {
                return getValue().equals(other);
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

    @Override
    public String toString() {
        if (hasValue()) {
            return "FHIRPathElementNode: [type: " + type() + ", value: " + value.toString() + "]";
        }
        return super.toString();
    }

    @Override
    public void accept(FHIRPathNodeVisitor visitor) {
        visitor.visit(this);
    }
}
