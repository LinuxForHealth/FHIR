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

@System("http://hl7.org/fhir/action-condition-kind")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ActionConditionKind extends Code {
    /**
     * Applicability
     * 
     * <p>The condition describes whether or not a given action is applicable.
     */
    public static final ActionConditionKind APPLICABILITY = ActionConditionKind.builder().value(Value.APPLICABILITY).build();

    /**
     * Start
     * 
     * <p>The condition is a starting condition for the action.
     */
    public static final ActionConditionKind START = ActionConditionKind.builder().value(Value.START).build();

    /**
     * Stop
     * 
     * <p>The condition is a stop, or exit condition for the action.
     */
    public static final ActionConditionKind STOP = ActionConditionKind.builder().value(Value.STOP).build();

    private volatile int hashCode;

    private ActionConditionKind(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this ActionConditionKind as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating ActionConditionKind objects from a passed enum value.
     */
    public static ActionConditionKind of(Value value) {
        switch (value) {
        case APPLICABILITY:
            return APPLICABILITY;
        case START:
            return START;
        case STOP:
            return STOP;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating ActionConditionKind objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static ActionConditionKind of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating ActionConditionKind objects from a passed string value.
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
     * Inherited factory method for creating ActionConditionKind objects from a passed string value.
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
        ActionConditionKind other = (ActionConditionKind) obj;
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
         *     An enum constant for ActionConditionKind
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public ActionConditionKind build() {
            ActionConditionKind actionConditionKind = new ActionConditionKind(this);
            if (validating) {
                validate(actionConditionKind);
            }
            return actionConditionKind;
        }

        protected void validate(ActionConditionKind actionConditionKind) {
            super.validate(actionConditionKind);
        }

        protected Builder from(ActionConditionKind actionConditionKind) {
            super.from(actionConditionKind);
            return this;
        }
    }

    public enum Value {
        /**
         * Applicability
         * 
         * <p>The condition describes whether or not a given action is applicable.
         */
        APPLICABILITY("applicability"),

        /**
         * Start
         * 
         * <p>The condition is a starting condition for the action.
         */
        START("start"),

        /**
         * Stop
         * 
         * <p>The condition is a stop, or exit condition for the action.
         */
        STOP("stop");

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
         * Factory method for creating ActionConditionKind.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding ActionConditionKind.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "applicability":
                return APPLICABILITY;
            case "start":
                return START;
            case "stop":
                return STOP;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
