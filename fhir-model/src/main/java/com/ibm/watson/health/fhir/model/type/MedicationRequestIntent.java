/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

public class MedicationRequestIntent extends Code {
    /**
     * Proposal
     */
    public static final MedicationRequestIntent PROPOSAL = MedicationRequestIntent.of(ValueSet.PROPOSAL);

    /**
     * Plan
     */
    public static final MedicationRequestIntent PLAN = MedicationRequestIntent.of(ValueSet.PLAN);

    /**
     * Order
     */
    public static final MedicationRequestIntent ORDER = MedicationRequestIntent.of(ValueSet.ORDER);

    /**
     * Original Order
     */
    public static final MedicationRequestIntent ORIGINAL_ORDER = MedicationRequestIntent.of(ValueSet.ORIGINAL_ORDER);

    /**
     * Reflex Order
     */
    public static final MedicationRequestIntent REFLEX_ORDER = MedicationRequestIntent.of(ValueSet.REFLEX_ORDER);

    /**
     * Filler Order
     */
    public static final MedicationRequestIntent FILLER_ORDER = MedicationRequestIntent.of(ValueSet.FILLER_ORDER);

    /**
     * Instance Order
     */
    public static final MedicationRequestIntent INSTANCE_ORDER = MedicationRequestIntent.of(ValueSet.INSTANCE_ORDER);

    /**
     * Option
     */
    public static final MedicationRequestIntent OPTION = MedicationRequestIntent.of(ValueSet.OPTION);

    private volatile int hashCode;

    private MedicationRequestIntent(Builder builder) {
        super(builder);
    }

    public static MedicationRequestIntent of(java.lang.String value) {
        return MedicationRequestIntent.builder().value(value).build();
    }

    public static MedicationRequestIntent of(ValueSet value) {
        return MedicationRequestIntent.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return MedicationRequestIntent.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return MedicationRequestIntent.builder().value(value).build();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MedicationRequestIntent other = (MedicationRequestIntent) obj;
        return Objects.equals(id, other.id) && Objects.equals(extension, other.extension) && Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, extension, value);
            hashCode = result;
        }
        return result;
    }

    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.id = id;
        builder.extension.addAll(extension);
        builder.value = value;
        return builder;
    }

    public static Builder builder() {
        return new Builder();
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
        public MedicationRequestIntent build() {
            return new MedicationRequestIntent(this);
        }
    }

    public enum ValueSet {
        /**
         * Proposal
         */
        PROPOSAL("proposal"),

        /**
         * Plan
         */
        PLAN("plan"),

        /**
         * Order
         */
        ORDER("order"),

        /**
         * Original Order
         */
        ORIGINAL_ORDER("original-order"),

        /**
         * Reflex Order
         */
        REFLEX_ORDER("reflex-order"),

        /**
         * Filler Order
         */
        FILLER_ORDER("filler-order"),

        /**
         * Instance Order
         */
        INSTANCE_ORDER("instance-order"),

        /**
         * Option
         */
        OPTION("option");

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
