/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class DocumentConfidentiality extends Code {
    public static final DocumentConfidentiality U = DocumentConfidentiality.of(ValueSet.U);

    public static final DocumentConfidentiality L = DocumentConfidentiality.of(ValueSet.L);

    public static final DocumentConfidentiality M = DocumentConfidentiality.of(ValueSet.M);

    public static final DocumentConfidentiality N = DocumentConfidentiality.of(ValueSet.N);

    public static final DocumentConfidentiality R = DocumentConfidentiality.of(ValueSet.R);

    public static final DocumentConfidentiality V = DocumentConfidentiality.of(ValueSet.V);

    private DocumentConfidentiality(Builder builder) {
        super(builder);
    }

    public static DocumentConfidentiality of(java.lang.String value) {
        return DocumentConfidentiality.builder().value(value).build();
    }

    public static DocumentConfidentiality of(ValueSet value) {
        return DocumentConfidentiality.builder().value(value).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.id = id;
        builder.extension.addAll(extension);
        builder.value = value;
        return builder;
    }

    public static class Builder extends Code.Builder {
        private Builder() {
            super();
        }

        @Override
        public Builder id(java.lang.String id) {
            return (Builder) super.id(id);
        }

        @Override
        public Builder extension(Extension... extension) {
            return (Builder) super.extension(extension);
        }

        @Override
        public Builder extension(Collection<Extension> extension) {
            return (Builder) super.extension(extension);
        }

        @Override
        public Builder value(java.lang.String value) {
            return (Builder) super.value(ValueSet.from(value).value());
        }

        public Builder value(ValueSet value) {
            return (Builder) super.value(value.value());
        }

        @Override
        public DocumentConfidentiality build() {
            return new DocumentConfidentiality(this);
        }
    }

    public enum ValueSet {
        U("U"),

        L("L"),

        M("M"),

        N("N"),

        R("R"),

        V("V");

        private final java.lang.String value;

        ValueSet(java.lang.String value) {
            this.value = value;
        }

        public java.lang.String value() {
            return value;
        }

        public static ValueSet from(java.lang.String value) {
            for (ValueSet c : ValueSet.values()) {
                if (c.value.equals(value)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(value);
        }
    }
}
