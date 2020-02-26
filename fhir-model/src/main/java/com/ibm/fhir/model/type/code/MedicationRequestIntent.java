/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type.code;

import com.ibm.fhir.model.annotation.System;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.String;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

@System("http://hl7.org/fhir/CodeSystem/medicationrequest-intent")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class MedicationRequestIntent extends Code {
    /**
     * Proposal
     */
    public static final MedicationRequestIntent PROPOSAL = MedicationRequestIntent.builder().value(ValueSet.PROPOSAL).build();

    /**
     * Plan
     */
    public static final MedicationRequestIntent PLAN = MedicationRequestIntent.builder().value(ValueSet.PLAN).build();

    /**
     * Order
     */
    public static final MedicationRequestIntent ORDER = MedicationRequestIntent.builder().value(ValueSet.ORDER).build();

    /**
     * Original Order
     */
    public static final MedicationRequestIntent ORIGINAL_ORDER = MedicationRequestIntent.builder().value(ValueSet.ORIGINAL_ORDER).build();

    /**
     * Reflex Order
     */
    public static final MedicationRequestIntent REFLEX_ORDER = MedicationRequestIntent.builder().value(ValueSet.REFLEX_ORDER).build();

    /**
     * Filler Order
     */
    public static final MedicationRequestIntent FILLER_ORDER = MedicationRequestIntent.builder().value(ValueSet.FILLER_ORDER).build();

    /**
     * Instance Order
     */
    public static final MedicationRequestIntent INSTANCE_ORDER = MedicationRequestIntent.builder().value(ValueSet.INSTANCE_ORDER).build();

    /**
     * Option
     */
    public static final MedicationRequestIntent OPTION = MedicationRequestIntent.builder().value(ValueSet.OPTION).build();

    private volatile int hashCode;

    private MedicationRequestIntent(Builder builder) {
        super(builder);
    }

    public static MedicationRequestIntent of(ValueSet value) {
        switch (value) {
        case PROPOSAL:
            return PROPOSAL;
        case PLAN:
            return PLAN;
        case ORDER:
            return ORDER;
        case ORIGINAL_ORDER:
            return ORIGINAL_ORDER;
        case REFLEX_ORDER:
            return REFLEX_ORDER;
        case FILLER_ORDER:
            return FILLER_ORDER;
        case INSTANCE_ORDER:
            return INSTANCE_ORDER;
        case OPTION:
            return OPTION;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    public static MedicationRequestIntent of(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    public static String string(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    public static Code code(java.lang.String value) {
        return of(ValueSet.from(value));
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
        builder.id(id);
        builder.extension(extension);
        builder.value(value);
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
            return (value != null) ? (Builder) super.value(ValueSet.from(value).value()) : this;
        }

        public Builder value(ValueSet value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
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
