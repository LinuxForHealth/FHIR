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

@System("http://hl7.org/fhir/action-precheck-behavior")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ActionPrecheckBehavior extends Code {
    /**
     * Yes
     * 
     * <p>An action with this behavior is one of the most frequent action that is, or should be, included by an end user, for 
     * the particular context in which the action occurs. The system displaying the action to the end user should consider 
     * "pre-checking" such an action as a convenience for the user.
     */
    public static final ActionPrecheckBehavior YES = ActionPrecheckBehavior.builder().value(Value.YES).build();

    /**
     * No
     * 
     * <p>An action with this behavior is one of the less frequent actions included by the end user, for the particular 
     * context in which the action occurs. The system displaying the actions to the end user would typically not "pre-check" 
     * such an action.
     */
    public static final ActionPrecheckBehavior NO = ActionPrecheckBehavior.builder().value(Value.NO).build();

    private volatile int hashCode;

    private ActionPrecheckBehavior(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this ActionPrecheckBehavior as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating ActionPrecheckBehavior objects from a passed enum value.
     */
    public static ActionPrecheckBehavior of(Value value) {
        switch (value) {
        case YES:
            return YES;
        case NO:
            return NO;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating ActionPrecheckBehavior objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static ActionPrecheckBehavior of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating ActionPrecheckBehavior objects from a passed string value.
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
     * Inherited factory method for creating ActionPrecheckBehavior objects from a passed string value.
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
        ActionPrecheckBehavior other = (ActionPrecheckBehavior) obj;
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
         *     An enum constant for ActionPrecheckBehavior
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public ActionPrecheckBehavior build() {
            ActionPrecheckBehavior actionPrecheckBehavior = new ActionPrecheckBehavior(this);
            if (validating) {
                validate(actionPrecheckBehavior);
            }
            return actionPrecheckBehavior;
        }

        protected void validate(ActionPrecheckBehavior actionPrecheckBehavior) {
            super.validate(actionPrecheckBehavior);
        }

        protected Builder from(ActionPrecheckBehavior actionPrecheckBehavior) {
            super.from(actionPrecheckBehavior);
            return this;
        }
    }

    public enum Value {
        /**
         * Yes
         * 
         * <p>An action with this behavior is one of the most frequent action that is, or should be, included by an end user, for 
         * the particular context in which the action occurs. The system displaying the action to the end user should consider 
         * "pre-checking" such an action as a convenience for the user.
         */
        YES("yes"),

        /**
         * No
         * 
         * <p>An action with this behavior is one of the less frequent actions included by the end user, for the particular 
         * context in which the action occurs. The system displaying the actions to the end user would typically not "pre-check" 
         * such an action.
         */
        NO("no");

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
         * Factory method for creating ActionPrecheckBehavior.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding ActionPrecheckBehavior.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "yes":
                return YES;
            case "no":
                return NO;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
