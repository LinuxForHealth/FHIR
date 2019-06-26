/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class AuditEventOutcome extends Code {
    /**
     * Success
     */
    public static final AuditEventOutcome OUTCOME_0 = AuditEventOutcome.of(ValueSet.OUTCOME_0);

    /**
     * Minor failure
     */
    public static final AuditEventOutcome OUTCOME_4 = AuditEventOutcome.of(ValueSet.OUTCOME_4);

    /**
     * Serious failure
     */
    public static final AuditEventOutcome OUTCOME_8 = AuditEventOutcome.of(ValueSet.OUTCOME_8);

    /**
     * Major failure
     */
    public static final AuditEventOutcome OUTCOME_12 = AuditEventOutcome.of(ValueSet.OUTCOME_12);

    private AuditEventOutcome(Builder builder) {
        super(builder);
    }

    public static AuditEventOutcome of(java.lang.String value) {
        return AuditEventOutcome.builder().value(value).build();
    }

    public static AuditEventOutcome of(ValueSet value) {
        return AuditEventOutcome.builder().value(value).build();
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
        public AuditEventOutcome build() {
            return new AuditEventOutcome(this);
        }
    }

    public enum ValueSet {
        /**
         * Success
         */
        OUTCOME_0("0"),

        /**
         * Minor failure
         */
        OUTCOME_4("4"),

        /**
         * Serious failure
         */
        OUTCOME_8("8"),

        /**
         * Major failure
         */
        OUTCOME_12("12");

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
