/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class GuidanceResponseStatus extends Code {
    /**
     * Success
     */
    public static final GuidanceResponseStatus SUCCESS = GuidanceResponseStatus.of(ValueSet.SUCCESS);

    /**
     * Data Requested
     */
    public static final GuidanceResponseStatus DATA_REQUESTED = GuidanceResponseStatus.of(ValueSet.DATA_REQUESTED);

    /**
     * Data Required
     */
    public static final GuidanceResponseStatus DATA_REQUIRED = GuidanceResponseStatus.of(ValueSet.DATA_REQUIRED);

    /**
     * In Progress
     */
    public static final GuidanceResponseStatus IN_PROGRESS = GuidanceResponseStatus.of(ValueSet.IN_PROGRESS);

    /**
     * Failure
     */
    public static final GuidanceResponseStatus FAILURE = GuidanceResponseStatus.of(ValueSet.FAILURE);

    /**
     * Entered In Error
     */
    public static final GuidanceResponseStatus ENTERED_IN_ERROR = GuidanceResponseStatus.of(ValueSet.ENTERED_IN_ERROR);

    private GuidanceResponseStatus(Builder builder) {
        super(builder);
    }

    public static GuidanceResponseStatus of(java.lang.String value) {
        return GuidanceResponseStatus.builder().value(value).build();
    }

    public static GuidanceResponseStatus of(ValueSet value) {
        return GuidanceResponseStatus.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return GuidanceResponseStatus.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return GuidanceResponseStatus.builder().value(value).build();
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
        public GuidanceResponseStatus build() {
            return new GuidanceResponseStatus(this);
        }
    }

    public enum ValueSet {
        /**
         * Success
         */
        SUCCESS("success"),

        /**
         * Data Requested
         */
        DATA_REQUESTED("data-requested"),

        /**
         * Data Required
         */
        DATA_REQUIRED("data-required"),

        /**
         * In Progress
         */
        IN_PROGRESS("in-progress"),

        /**
         * Failure
         */
        FAILURE("failure"),

        /**
         * Entered In Error
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
