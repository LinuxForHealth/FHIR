/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class ParticipantStatus extends Code {
    /**
     * Accepted
     */
    public static final ParticipantStatus ACCEPTED = ParticipantStatus.of(ValueSet.ACCEPTED);

    /**
     * Declined
     */
    public static final ParticipantStatus DECLINED = ParticipantStatus.of(ValueSet.DECLINED);

    /**
     * Tentative
     */
    public static final ParticipantStatus TENTATIVE = ParticipantStatus.of(ValueSet.TENTATIVE);

    /**
     * Needs Action
     */
    public static final ParticipantStatus NEEDS_ACTION = ParticipantStatus.of(ValueSet.NEEDS_ACTION);

    private ParticipantStatus(Builder builder) {
        super(builder);
    }

    public static ParticipantStatus of(java.lang.String value) {
        return ParticipantStatus.builder().value(value).build();
    }

    public static ParticipantStatus of(ValueSet value) {
        return ParticipantStatus.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return ParticipantStatus.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return ParticipantStatus.builder().value(value).build();
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
        public ParticipantStatus build() {
            return new ParticipantStatus(this);
        }
    }

    public enum ValueSet {
        /**
         * Accepted
         */
        ACCEPTED("accepted"),

        /**
         * Declined
         */
        DECLINED("declined"),

        /**
         * Tentative
         */
        TENTATIVE("tentative"),

        /**
         * Needs Action
         */
        NEEDS_ACTION("needs-action");

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
