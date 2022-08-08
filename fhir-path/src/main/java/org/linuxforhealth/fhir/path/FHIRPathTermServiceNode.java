/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.path;

import org.linuxforhealth.fhir.path.visitor.FHIRPathNodeVisitor;

/**
 * A special {@link FHIRPathNode} implementation used for the %terminologies external constant
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