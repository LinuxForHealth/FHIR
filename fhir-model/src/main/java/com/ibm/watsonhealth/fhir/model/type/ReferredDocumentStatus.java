/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class ReferredDocumentStatus extends Code {
    /**
     * Preliminary
     */
    public static final ReferredDocumentStatus PRELIMINARY = ReferredDocumentStatus.of(ValueSet.PRELIMINARY);

    /**
     * Final
     */
    public static final ReferredDocumentStatus FINAL = ReferredDocumentStatus.of(ValueSet.FINAL);

    /**
     * Amended
     */
    public static final ReferredDocumentStatus AMENDED = ReferredDocumentStatus.of(ValueSet.AMENDED);

    /**
     * Entered in Error
     */
    public static final ReferredDocumentStatus ENTERED_IN_ERROR = ReferredDocumentStatus.of(ValueSet.ENTERED_IN_ERROR);

    private ReferredDocumentStatus(Builder builder) {
        super(builder);
    }

    public static ReferredDocumentStatus of(java.lang.String value) {
        return ReferredDocumentStatus.builder().value(value).build();
    }

    public static ReferredDocumentStatus of(ValueSet value) {
        return ReferredDocumentStatus.builder().value(value).build();
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
        public ReferredDocumentStatus build() {
            return new ReferredDocumentStatus(this);
        }
    }

    public enum ValueSet {
        /**
         * Preliminary
         */
        PRELIMINARY("preliminary"),

        /**
         * Final
         */
        FINAL("final"),

        /**
         * Amended
         */
        AMENDED("amended"),

        /**
         * Entered in Error
         */
        ENTERED_IN_ERROR("entered-in-error");

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
