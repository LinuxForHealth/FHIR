/*
 * (C) Copyright IBM Corp. 2019, 2021
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

@System("http://hl7.org/fhir/task-intent")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class TaskIntent extends Code {
    /**
     * Unknown
     * 
     * <p>The intent is not known. When dealing with Task, it's not always known (or relevant) how the task was initiated - i.
     * e. whether it was proposed, planned, ordered or just done spontaneously.
     */
    public static final TaskIntent UNKNOWN = TaskIntent.builder().value(ValueSet.UNKNOWN).build();

    public static final TaskIntent PROPOSAL = TaskIntent.builder().value(ValueSet.PROPOSAL).build();

    public static final TaskIntent PLAN = TaskIntent.builder().value(ValueSet.PLAN).build();

    public static final TaskIntent ORDER = TaskIntent.builder().value(ValueSet.ORDER).build();

    public static final TaskIntent ORIGINAL_ORDER = TaskIntent.builder().value(ValueSet.ORIGINAL_ORDER).build();

    public static final TaskIntent REFLEX_ORDER = TaskIntent.builder().value(ValueSet.REFLEX_ORDER).build();

    public static final TaskIntent FILLER_ORDER = TaskIntent.builder().value(ValueSet.FILLER_ORDER).build();

    public static final TaskIntent INSTANCE_ORDER = TaskIntent.builder().value(ValueSet.INSTANCE_ORDER).build();

    public static final TaskIntent OPTION = TaskIntent.builder().value(ValueSet.OPTION).build();

    private volatile int hashCode;

    private TaskIntent(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    /**
     * Factory method for creating TaskIntent objects from a passed enum value.
     */
    public static TaskIntent of(ValueSet value) {
        switch (value) {
        case UNKNOWN:
            return UNKNOWN;
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

    /**
     * Factory method for creating TaskIntent objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static TaskIntent of(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    /**
     * Inherited factory method for creating TaskIntent objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static String string(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    /**
     * Inherited factory method for creating TaskIntent objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
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
        TaskIntent other = (TaskIntent) obj;
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
        public TaskIntent build() {
            return new TaskIntent(this);
        }
    }

    public enum ValueSet {
        /**
         * Unknown
         * 
         * <p>The intent is not known. When dealing with Task, it's not always known (or relevant) how the task was initiated - i.
         * e. whether it was proposed, planned, ordered or just done spontaneously.
         */
        UNKNOWN("unknown"),

        PROPOSAL("proposal"),

        PLAN("plan"),

        ORDER("order"),

        ORIGINAL_ORDER("original-order"),

        REFLEX_ORDER("reflex-order"),

        FILLER_ORDER("filler-order"),

        INSTANCE_ORDER("instance-order"),

        OPTION("option");

        private final java.lang.String value;

        ValueSet(java.lang.String value) {
            this.value = value;
        }

        /**
         * @return
         *     The java.lang.String value of the code represented by this enum
         */
        public java.lang.String value() {
            return value;
        }

        /**
         * Factory method for creating TaskIntent.ValueSet values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @throws IllegalArgumentException
         *     If the passed string cannot be parsed into an allowed code value
         */
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
