/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.path;

import java.util.Collection;
import java.util.stream.Stream;

import org.linuxforhealth.fhir.path.visitor.FHIRPathNodeVisitor;

/**
 * In FHIRPath, data are represented as a tree of labeled nodes, where each node may optionally have a
 * child system value and child nodes. Nodes need not have a unique label, and leaf nodes must have a child system value.
 */
public interface FHIRPathNode extends Comparable<FHIRPathNode> {
    /**
     * The name (label) of this FHIRPathNode
     *
     * @return
     *     the name of this FHIRPathNode if exists, otherwise null
     * @implNote
     *     if this node exists in a {@link FHIRPathTree}, then it will have a name that matches the element name or type name (for root nodes)
     */
    String name();

    /**
     * The path of this FHIRPathNode
     *
     * @return
     *     the path of this FHIRPathNode if exists, otherwise null
     * @implNote
     *     if this FHIRPathNode exists in a {@link FHIRPathTree}, then it will have the path that represents its location in the tree, otherwise null
     */
    String path();

    /**
     * The type of this FHIRPathNode
     *
     * @return
     *     the type of this FHIRPathNode
     */
    FHIRPathType type();

    /**
     * Indicates whether this FHIRPathNode has a child system value
     *
     * @return
     *     true if this FHIRPathNode has a child system value, otherwise false
     */
    boolean hasValue();

    /**
     * The child system value of this FHIRPathNode
     *
     * @return
     *     the child system value of this FHIRPathNode, otherwise null
     */
    FHIRPathSystemValue getValue();

    /**
     * The child nodes of this FHIRPathNode
     *
     * @return
     *     a collection of child nodes for this FHIRPathNode if exists, otherwise empty collection
     */
    Collection<FHIRPathNode> children();

    /**
     * A stream-based view of this FHIRPathNode and all of its descendants
     *
     * @return
     *     A stream containing this FHIRPathNode and all of its descendants
     */
    Stream<FHIRPathNode> stream();

    /**
     * A collection of descendant nodes of this FHIRPathNode
     *
     * @return
     *     a collection of descendant nodes of this FHIRPathNode if exists, otherwise empty collection
     */
    Collection<FHIRPathNode> descendants();

    /**
     * Indicates whether this FHIRPathNode is comparable to the parameter
     *
     * @param other
     *     the other FHIRPathNode
     * @return
     *     true if this FHIRPathNode is comparable to the parameter, otherwise false
     */
    default boolean isComparableTo(FHIRPathNode other) {
        return false;
    }

    /**
     * Indicates whether this FHIRPathNode is type compatible with the parameter
     *
     * @param <T>
     *     FHIRPathNode or one of its implementations
     * @param nodeType
     *     the nodeType we are checking against
     * @return
     *     true if this FHIRPathNode is type compatible with {@code nodeType}, otherwise false
     */
    <T extends FHIRPathNode> boolean is(Class<T> nodeType);

    /**
     * Cast this FHIRPathNode to the type specified in the parameter
     *
     * @param <T>
     *     FHIRPathNode or one of its implementations
     * @param nodeType
     *     the nodeType we are casting to
     * @return
     *     this FHIRPathNode cast to the type specified in the parameter
     * @throws
     *     {@link ClassCastException if this FHIRPathNode is not type compatible with the parameter
     */
    <T extends FHIRPathNode> T as(Class<T> nodeType);

    /**
     * Indicates whether this FHIRPathNode is type compatible with {@link FHIRPathElementNode}
     *
     * @return
     *     true if this FHIRPathNode is type compatible with {@link FHIRPathElementNode}, otherwise false
     */
    default boolean isElementNode() {
        return false;
    }

    /**
     * Indicates whether this FHIRPathNode is type compatible with {@link FHIRPathResourceNode}
     *
     * @return
     *     true if this FHIRPathNode is type compatible with {@link FHIRPathResourceNode}, otherwise false
     */
    default boolean isResourceNode() {
        return false;
    }

    /**
     * Indicates whether this FHIRPathNode is type compatible with {@link FHIRPathSystemValue}
     *
     * @return
     *     true if this FHIRPathNode is type compatible with {@link FHIRPathSystemValue}, otherwise false
     */
    default boolean isSystemValue() {
        return false;
    }

    /**
     * Indicates whether this FHIRPathNode is type compatible with {@link FHIRPathTypeInfoNode}
     *
     * @return
     *     true if this FHIRPathNode is type compatible with {@link FHIRPathTypeInfoNode}, otherwise false
     */
    default boolean isTypeInfoNode() {
        return false;
    }

    /**
     * Indicates whether this FHIRPathNode is type compatible with {@link FHIRPathTermServiceNode}
     *
     * @return
     *     true if this FHIRPathNode is type compatible with {@link FHIRPathTermServiceNode}, otherwise false
     */
    default boolean isTermServiceNode() {
        return false;
    }

    /**
     * Cast this FHIRPathNode to a {@link FHIRPathElementNode}
     *
     * @return
     *     this FHIRPathNode as a {@link FHIRPathElementNode}
     * @throws
     *     {@link ClassCastException} if this FHIRPathNode is not type compatible with {@link FHIRPathElementNode}
     */
    default FHIRPathElementNode asElementNode() {
        return as(FHIRPathElementNode.class);
    }

    /**
     * Cast this FHIRPathNode to a {@link FHIRPathResourceNode}
     *
     * @return
     *     this FHIRPathNode as a {@link FHIRPathResourceNode}
     * @throws
     *     {@link ClassCastException} if this FHIRPathNode is not type compatible with {@link FHIRPathResourceNode}
     */
    default FHIRPathResourceNode asResourceNode() {
        return as(FHIRPathResourceNode.class);
    }

    /**
     * Cast this FHIRPathNode to a {@link FHIRPathSystemValue}
     *
     * @return
     *     this FHIRPathNode as a {@link FHIRPathSystemValue}
     * @throws
     *     {@link ClassCastException} if this FHIRPathNode is not type compatible with {@link FHIRPathSystemValue}
     */
    default FHIRPathSystemValue asSystemValue() {
        return as(FHIRPathSystemValue.class);
    }

    /**
     * Cast this FHIRPathNode to a {@link FHIRPathTypeInfoNode}
     *
     * @return
     *     this FHIRPathNode as a {@link FHIRPathTypeInfoNode}
     * @throws
     *     {@link ClassCastException} if this FHIRPathNode is not type compatible with {@link FHIRPathTypeInfoNode}
     */
    default FHIRPathTypeInfoNode asTypeInfoNode() {
        return as(FHIRPathTypeInfoNode.class);
    }

    /**
     * Cast this FHIRPathNode to a {@link FHIRPathTermServiceNode}
     *
     * @return
     *     this FHIRPathNode as a {@link FHIRPathTermServiceNode}
     * @throws
     *     {@link ClassCastException} if this FHIRPathNode is not type compatible with {@link FHIRPathTermServiceNode}
     */
    default FHIRPathTermServiceNode asTermServiceNode() {
        return as(FHIRPathTermServiceNode.class);
    }

    /**
     * A builder interface for building FHIRPathNode instances
     */
    interface Builder {
        /**
         * The name of the FHIRPathNode
         *
         * @param name
         *     the name of the FHIRPathNode
         * @return
         *     A reference to this builder instance
         */
        Builder name(String name);

        /**
         * The path of the FHIRPathNode
         *
         * @param path
         *     the path of the FHIRPathNode
         * @return
         *     A reference to this builder instance
         */
        Builder path(String path);

        /**
         * The child system value of the FHIRPathNode
         *
         * @param name
         *     the child system value of the FHIRPathNode
         * @return
         *     A reference to this builder instance
         */
        Builder value(FHIRPathSystemValue value);

        /**
         * Child nodes of the FHIRPathNode
         *
         * @param name
         *     child nodes of the FHIRPathNode
         * @return
         *     A reference to this builder instance
         */
        Builder children(FHIRPathNode... children);

        /**
         * Child nodes of the FHIRPathNode
         *
         * @param name
         *     child nodes of the FHIRPathNode
         * @return
         *     A reference to this builder instance
         */
        Builder children(Collection<FHIRPathNode> children);

        /**
         * Build a FHIRPathNode using this builder
         *
         * @return
         *     a new FHIRPathNode instance
         */
        FHIRPathNode build();
    }

    /**
     * A method for accepting a {@link FHIRPathNodeVisitor}
     *
     * @param visitor
     *     the {@link FHIRPathNodeVisitor} that this FHIRPathNode is accepting
     */
    void accept(FHIRPathNodeVisitor visitor);
}
