/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class Status extends Code {
    /**
     * Attested
     */
    public static final Status ATTESTED = Status.of(ValueSet.ATTESTED);

    /**
     * Validated
     */
    public static final Status VALIDATED = Status.of(ValueSet.VALIDATED);

    /**
     * In process
     */
    public static final Status IN_PROCESS = Status.of(ValueSet.IN_PROCESS);

    /**
     * Requires revalidation
     */
    public static final Status REQ_REVALID = Status.of(ValueSet.REQ_REVALID);

    /**
     * Validation failed
     */
    public static final Status VAL_FAIL = Status.of(ValueSet.VAL_FAIL);

    /**
     * Re-Validation failed
     */
    public static final Status REVAL_FAIL = Status.of(ValueSet.REVAL_FAIL);

    private Status(Builder builder) {
        super(builder);
    }

    public static Status of(java.lang.String value) {
        return Status.builder().value(value).build();
    }

    public static Status of(ValueSet value) {
        return Status.builder().value(value).build();
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
        public Status build() {
            return new Status(this);
        }
    }

    public enum ValueSet {
        /**
         * Attested
         */
        ATTESTED("attested"),

        /**
         * Validated
         */
        VALIDATED("validated"),

        /**
         * In process
         */
        IN_PROCESS("in-process"),

        /**
         * Requires revalidation
         */
        REQ_REVALID("req-revalid"),

        /**
         * Validation failed
         */
        VAL_FAIL("val-fail"),

        /**
         * Re-Validation failed
         */
        REVAL_FAIL("reval-fail");

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
