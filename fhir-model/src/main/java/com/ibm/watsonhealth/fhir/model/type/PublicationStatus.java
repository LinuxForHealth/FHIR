/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class PublicationStatus extends Code {
    /**
     * Draft
     */
    public static final PublicationStatus DRAFT = PublicationStatus.of(ValueSet.DRAFT);

    /**
     * Active
     */
    public static final PublicationStatus ACTIVE = PublicationStatus.of(ValueSet.ACTIVE);

    /**
     * Retired
     */
    public static final PublicationStatus RETIRED = PublicationStatus.of(ValueSet.RETIRED);

    /**
     * Unknown
     */
    public static final PublicationStatus UNKNOWN = PublicationStatus.of(ValueSet.UNKNOWN);

    private PublicationStatus(Builder builder) {
        super(builder);
    }

    public static PublicationStatus of(java.lang.String value) {
        return PublicationStatus.builder().value(value).build();
    }

    public static PublicationStatus of(ValueSet value) {
        return PublicationStatus.builder().value(value).build();
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
        public PublicationStatus build() {
            return new PublicationStatus(this);
        }
    }

    public enum ValueSet {
        /**
         * Draft
         */
        DRAFT("draft"),

        /**
         * Active
         */
        ACTIVE("active"),

        /**
         * Retired
         */
        RETIRED("retired"),

        /**
         * Unknown
         */
        UNKNOWN("unknown");

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
