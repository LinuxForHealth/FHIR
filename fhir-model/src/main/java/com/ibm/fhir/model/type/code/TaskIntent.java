/*
 * (C) Copyright IBM Corp. 2019, 2022
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
    public static final TaskIntent UNKNOWN = TaskIntent.builder().value(Value.UNKNOWN).build();

    public static final TaskIntent PROPOSAL = TaskIntent.builder().value(Value.PROPOSAL).build();

    public static final TaskIntent PLAN = TaskIntent.builder().value(Value.PLAN).build();

    public static final TaskIntent ORDER = TaskIntent.builder().value(Value.ORDER).build();

    public static final TaskIntent ORIGINAL_ORDER = TaskIntent.builder().value(Value.ORIGINAL_ORDER).build();

    public static final TaskIntent REFLEX_ORDER = TaskIntent.builder().value(Value.REFLEX_ORDER).build();

    public static final TaskIntent FILLER_ORDER = TaskIntent.builder().value(Value.FILLER_ORDER).build();

    public static final TaskIntent INSTANCE_ORDER = TaskIntent.builder().value(Value.INSTANCE_ORDER).build();

    public static final TaskIntent OPTION = TaskIntent.builder().value(Value.OPTION).build();

    private volatile int hashCode;

    private TaskIntent(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this TaskIntent as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating TaskIntent objects from a passed enum value.
     */
    public static TaskIntent of(Value value) {
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
        return of(Value.from(value));
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
        return of(Value.from(value));
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
        return of(Value.from(value));
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
        return new Builder().from(this);
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
            return (value != null) ? (Builder) super.value(Value.from(value).value()) : this;
        }

        /**
         * Primitive value for code
         * 
         * @param value
         *     An enum constant for TaskIntent
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public TaskIntent build() {
            TaskIntent taskIntent = new TaskIntent(this);
            if (validating) {
                validate(taskIntent);
            }
            return taskIntent;
        }

        protected void validate(TaskIntent taskIntent) {
            super.validate(taskIntent);
        }

        protected Builder from(TaskIntent taskIntent) {
            super.from(taskIntent);
            return this;
        }
    }

    public enum Value {
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

        Value(java.lang.String value) {
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
         * Factory method for creating TaskIntent.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding TaskIntent.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "unknown":
                return UNKNOWN;
            case "proposal":
                return PROPOSAL;
            case "plan":
                return PLAN;
            case "order":
                return ORDER;
            case "original-order":
                return ORIGINAL_ORDER;
            case "reflex-order":
                return REFLEX_ORDER;
            case "filler-order":
                return FILLER_ORDER;
            case "instance-order":
                return INSTANCE_ORDER;
            case "option":
                return OPTION;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
