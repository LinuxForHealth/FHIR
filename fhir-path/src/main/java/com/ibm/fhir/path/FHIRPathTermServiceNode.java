/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path;

import com.ibm.fhir.path.visitor.FHIRPathNodeVisitor;
import com.ibm.fhir.term.service.FHIRTermService;

/**
 * A singleton {@link FHIRPathNode} that wraps a {@link FHIRTermService} instance
 */
public class FHIRPathTermServiceNode extends FHIRPathAbstractNode {
    private FHIRPathTermServiceNode(Builder builder) {
        super(builder);
    }

    @Override
    public boolean isTermServiceNode() {
        return true;
    }

    public static FHIRPathTermServiceNode termServiceNode() {
        return FHIRPathTermServiceNode.builder().build();
    }

    @Override
    public Builder toBuilder() {
        throw new UnsupportedOperationException();
    }

    private static Builder builder() {
        return new Builder(FHIRPathType.FHIR_TERM_SERVICE);
    }

    private static class Builder extends FHIRPathAbstractNode.Builder {
        private Builder(FHIRPathType type) {
            super(type);
        }

        @Override
        public FHIRPathTermServiceNode build() {
            return new FHIRPathTermServiceNode(this);
        }
    }

    @Override
    public int compareTo(FHIRPathNode o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void accept(FHIRPathNodeVisitor visitor) {
        throw new UnsupportedOperationException();
    }
}