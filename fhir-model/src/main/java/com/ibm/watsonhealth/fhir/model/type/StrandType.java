/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class StrandType extends Code {
    /**
     * Watson strand of referenceSeq
     */
    public static final StrandType WATSON = StrandType.of(ValueSet.WATSON);

    /**
     * Crick strand of referenceSeq
     */
    public static final StrandType CRICK = StrandType.of(ValueSet.CRICK);

    private StrandType(Builder builder) {
        super(builder);
    }

    public static StrandType of(java.lang.String value) {
        return StrandType.builder().value(value).build();
    }

    public static StrandType of(ValueSet value) {
        return StrandType.builder().value(value).build();
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
        public StrandType build() {
            return new StrandType(this);
        }
    }

    public enum ValueSet {
        /**
         * Watson strand of referenceSeq
         */
        WATSON("watson"),

        /**
         * Crick strand of referenceSeq
         */
        CRICK("crick");

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
