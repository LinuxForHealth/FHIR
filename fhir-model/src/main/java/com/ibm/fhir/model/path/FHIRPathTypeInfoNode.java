/*
 * (C) Copyright IBM Corp. 2019
 * 
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.path;

import java.util.Objects;

import com.ibm.fhir.model.path.visitor.FHIRPathNodeVisitor;

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
    
    public TypeInfo typeInfo() {
        return typeInfo;
    }
    
    public static FHIRPathTypeInfoNode typeInfoNode(TypeInfo typeInfo) {
        return new Builder(FHIRPathType.from(typeInfo.getClass()), typeInfo).build();
    }
    
    private static class Builder extends FHIRPathAbstractNode.Builder {
        private final TypeInfo typeInfo;
        
        private Builder(FHIRPathType type, TypeInfo typeInfo) {
            super(type);
            this.typeInfo = typeInfo;
            if (typeInfo.getNamespace() != null) {
                children.add(FHIRPathStringValue.stringValue("namespace", typeInfo.getNamespace()));
            }
            if (typeInfo.getName() != null) {
                children.add(FHIRPathStringValue.stringValue("name", typeInfo.getName()));
            }
        }

        @Override
        public FHIRPathTypeInfoNode build() {
            return new FHIRPathTypeInfoNode(this);
        }
    }

    @Override
    public void accept(FHIRPathNodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int compareTo(FHIRPathNode o) {
        throw new UnsupportedOperationException();
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
    public Builder toBuilder() {
        throw new UnsupportedOperationException();
    }
}