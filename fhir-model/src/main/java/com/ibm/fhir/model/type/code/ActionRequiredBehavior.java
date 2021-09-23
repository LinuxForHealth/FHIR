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

@System("http://hl7.org/fhir/action-required-behavior")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ActionRequiredBehavior extends Code {
    /**
     * Must
     * 
     * <p>An action with this behavior must be included in the actions processed by the end user; the end user SHALL NOT 
     * choose not to include this action.
     */
    public static final ActionRequiredBehavior MUST = ActionRequiredBehavior.builder().value(Value.MUST).build();

    /**
     * Could
     * 
     * <p>An action with this behavior may be included in the set of actions processed by the end user.
     */
    public static final ActionRequiredBehavior COULD = ActionRequiredBehavior.builder().value(Value.COULD).build();

    /**
     * Must Unless Documented
     * 
     * <p>An action with this behavior must be included in the set of actions processed by the end user, unless the end user 
     * provides documentation as to why the action was not included.
     */
    public static final ActionRequiredBehavior MUST_UNLESS_DOCUMENTED = ActionRequiredBehavior.builder().value(Value.MUST_UNLESS_DOCUMENTED).build();

    private volatile int hashCode;

    private ActionRequiredBehavior(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this ActionRequiredBehavior as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating ActionRequiredBehavior objects from a passed enum value.
     */
    public static ActionRequiredBehavior of(Value value) {
        switch (value) {
        case MUST:
            return MUST;
        case COULD:
            return COULD;
        case MUST_UNLESS_DOCUMENTED:
            return MUST_UNLESS_DOCUMENTED;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating ActionRequiredBehavior objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static ActionRequiredBehavior of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating ActionRequiredBehavior objects from a passed string value.
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
     * Inherited factory method for creating ActionRequiredBehavior objects from a passed string value.
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
        ActionRequiredBehavior other = (ActionRequiredBehavior) obj;
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
         *     An enum constant for ActionRequiredBehavior
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public ActionRequiredBehavior build() {
            ActionRequiredBehavior actionRequiredBehavior = new ActionRequiredBehavior(this);
            if (validating) {
                validate(actionRequiredBehavior);
            }
            return actionRequiredBehavior;
        }

        protected void validate(ActionRequiredBehavior actionRequiredBehavior) {
            super.validate(actionRequiredBehavior);
        }

        protected Builder from(ActionRequiredBehavior actionRequiredBehavior) {
            super.from(actionRequiredBehavior);
            return this;
        }
    }

    public enum Value {
        /**
         * Must
         * 
         * <p>An action with this behavior must be included in the actions processed by the end user; the end user SHALL NOT 
         * choose not to include this action.
         */
        MUST("must"),

        /**
         * Could
         * 
         * <p>An action with this behavior may be included in the set of actions processed by the end user.
         */
        COULD("could"),

        /**
         * Must Unless Documented
         * 
         * <p>An action with this behavior must be included in the set of actions processed by the end user, unless the end user 
         * provides documentation as to why the action was not included.
         */
        MUST_UNLESS_DOCUMENTED("must-unless-documented");

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
         * Factory method for creating ActionRequiredBehavior.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding ActionRequiredBehavior.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "must":
                return MUST;
            case "could":
                return COULD;
            case "must-unless-documented":
                return MUST_UNLESS_DOCUMENTED;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
