/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.path;

import java.util.Objects;

import org.linuxforhealth.fhir.path.visitor.FHIRPathNodeVisitor;

/**
 * A {@link FHIRPathNode} that wraps a {@link TypeInfo}
 */
public class FHIRPathTypeInfoNode extends FHIRPathAbstractNode {
    private final TypeInfo typeInfo;

    protected FHIRPathTypeInfoNode(Builder builder) {
        super(builder);
        this.typeInfo = Objects.requireNonNull(builder.typeInfo);
    }

    @Override
    public boolean isTypeInfoNode() {
        return true;
    }

    /**
     * The {@link TypeInfo} wrapped by this FHIRPathTypeInfoNode
     *
     * @return
     *     the {@link TypeInfo} wrapped by this FHIRPathTypeInfoNode
     */
    public TypeInfo typeInfo() {
        return typeInfo;
    }

    /**
     * Static factory method for creating {@link TypeInfo} instances from a {@link TypeInfo}
     *
     * @param typeInfo
     *     the typeInfo
     * @return
     *     a new FHIRPathTypeInfoNode instance
     */
    public static FHIRPathTypeInfoNode typeInfoNode(TypeInfo typeInfo) {
        return new Builder(FHIRPathType.from(typeInfo.getClass()), typeInfo).build();
    }

    /**
     * This toBuilder is not supported for this FHIRPathTypeInfoNode
     */
    @Override
    public Builder toBuilder() {
        throw new UnsupportedOperationException();
    }

    private static class Builder extends FHIRPathAbstractNode.Builder {
        private final TypeInfo typeInfo;

        private Builder(FHIRPathType type, TypeInfo typeInfo) {
            super(type);
            this.typeInfo = typeInfo;
            if (typeInfo.getNamespace() != null) {
                children.add(FHIRPathStringValue.stringValue(null, "namespace", typeInfo.getNamespace()));
            }
            if (typeInfo.getName() != null) {
                children.add(FHIRPathStringValue.stringValue(null, "name", typeInfo.getName()));
            }
        }

        /**
         * Build a FHIRPathTypeInfoNode instance using this builder
         *
         * @return
         *     a new FHIRPathTypeInfoNode instance
         */
        @Override
        public FHIRPathTypeInfoNode build() {
            return new FHIRPathTypeInfoNode(this);
        }
    }

    /**
     * The compareTo operation is not supported for this FHIRPathTypeInfoNode
     */
    @Override
    public int compareTo(FHIRPathNode o) {
        throw new UnsupportedOperationException();
    }

    /**
     * Indicates whether the {@link TypeInfo} wrapped by this FHIRPathTypeInfoNode is equal the parameter
     *
     * @param obj
     *     the other {@link Object}
     * @return
     *     true if the {@link TypeInfo} wrapped by this FHIRPathTypeInfoNode node is equal to the parameter, otherwise false
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
        FHIRPathTypeInfoNode other = (FHIRPathTypeInfoNode) obj;
        return Objects.equals(typeInfo, other.typeInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(typeInfo);
    }

    @Override
    public String toString() {
        return typeInfo.toString();
    }

    @Override
    public void accept(FHIRPathNodeVisitor visitor) {
        visitor.visit(this);
    }
}
