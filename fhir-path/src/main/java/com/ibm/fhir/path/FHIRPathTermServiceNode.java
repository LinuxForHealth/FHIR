/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path;

import java.util.Objects;

import com.ibm.fhir.path.visitor.FHIRPathNodeVisitor;
import com.ibm.fhir.term.service.FHIRTermService;

/**
 * A singleton {@link FHIRPathNode} that wraps a {@link FHIRTermService} instance
 */
public class FHIRPathTermServiceNode extends FHIRPathAbstractNode {
    public static final FHIRPathTermServiceNode INSTANCE = FHIRPathTermServiceNode.builder(FHIRTermService.getInstance()).build();

    private final FHIRTermService service;

    private FHIRPathTermServiceNode(Builder builder) {
        super(builder);
        this.service = Objects.requireNonNull(builder.service);
    }

    @Override
    public boolean isTermServiceNode() {
        return true;
    }

    public FHIRTermService service() {
        return service;
    }

    @Override
    public Builder toBuilder() {
        throw new UnsupportedOperationException();
    }

    private static Builder builder(FHIRTermService service) {
        return new Builder(FHIRPathType.FHIR_TERM_SERVICE, service);
    }

    private static class Builder extends FHIRPathAbstractNode.Builder {
        private final FHIRTermService service;

        private Builder(FHIRPathType type, FHIRTermService service) {
            super(type);
            this.service = service;
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