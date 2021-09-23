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

@System("http://hl7.org/fhir/action-grouping-behavior")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ActionGroupingBehavior extends Code {
    /**
     * Visual Group
     * 
     * <p>Any group marked with this behavior should be displayed as a visual group to the end user.
     */
    public static final ActionGroupingBehavior VISUAL_GROUP = ActionGroupingBehavior.builder().value(Value.VISUAL_GROUP).build();

    /**
     * Logical Group
     * 
     * <p>A group with this behavior logically groups its sub-elements, and may be shown as a visual group to the end user, 
     * but it is not required to do so.
     */
    public static final ActionGroupingBehavior LOGICAL_GROUP = ActionGroupingBehavior.builder().value(Value.LOGICAL_GROUP).build();

    /**
     * Sentence Group
     * 
     * <p>A group of related alternative actions is a sentence group if the target referenced by the action is the same in 
     * all the actions and each action simply constitutes a different variation on how to specify the details for the target. 
     * For example, two actions that could be in a SentenceGroup are "aspirin, 500 mg, 2 times per day" and "aspirin, 300 mg, 
     * 3 times per day". In both cases, aspirin is the target referenced by the action, and the two actions represent 
     * different options for how aspirin might be ordered for the patient. Note that a SentenceGroup would almost always have 
     * an associated selection behavior of "AtMostOne", unless it's a required action, in which case, it would be 
     * "ExactlyOne".
     */
    public static final ActionGroupingBehavior SENTENCE_GROUP = ActionGroupingBehavior.builder().value(Value.SENTENCE_GROUP).build();

    private volatile int hashCode;

    private ActionGroupingBehavior(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this ActionGroupingBehavior as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating ActionGroupingBehavior objects from a passed enum value.
     */
    public static ActionGroupingBehavior of(Value value) {
        switch (value) {
        case VISUAL_GROUP:
            return VISUAL_GROUP;
        case LOGICAL_GROUP:
            return LOGICAL_GROUP;
        case SENTENCE_GROUP:
            return SENTENCE_GROUP;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating ActionGroupingBehavior objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static ActionGroupingBehavior of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating ActionGroupingBehavior objects from a passed string value.
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
     * Inherited factory method for creating ActionGroupingBehavior objects from a passed string value.
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
        ActionGroupingBehavior other = (ActionGroupingBehavior) obj;
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
         *     An enum constant for ActionGroupingBehavior
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public ActionGroupingBehavior build() {
            ActionGroupingBehavior actionGroupingBehavior = new ActionGroupingBehavior(this);
            if (validating) {
                validate(actionGroupingBehavior);
            }
            return actionGroupingBehavior;
        }

        protected void validate(ActionGroupingBehavior actionGroupingBehavior) {
            super.validate(actionGroupingBehavior);
        }

        protected Builder from(ActionGroupingBehavior actionGroupingBehavior) {
            super.from(actionGroupingBehavior);
            return this;
        }
    }

    public enum Value {
        /**
         * Visual Group
         * 
         * <p>Any group marked with this behavior should be displayed as a visual group to the end user.
         */
        VISUAL_GROUP("visual-group"),

        /**
         * Logical Group
         * 
         * <p>A group with this behavior logically groups its sub-elements, and may be shown as a visual group to the end user, 
         * but it is not required to do so.
         */
        LOGICAL_GROUP("logical-group"),

        /**
         * Sentence Group
         * 
         * <p>A group of related alternative actions is a sentence group if the target referenced by the action is the same in 
         * all the actions and each action simply constitutes a different variation on how to specify the details for the target. 
         * For example, two actions that could be in a SentenceGroup are "aspirin, 500 mg, 2 times per day" and "aspirin, 300 mg, 
         * 3 times per day". In both cases, aspirin is the target referenced by the action, and the two actions represent 
         * different options for how aspirin might be ordered for the patient. Note that a SentenceGroup would almost always have 
         * an associated selection behavior of "AtMostOne", unless it's a required action, in which case, it would be 
         * "ExactlyOne".
         */
        SENTENCE_GROUP("sentence-group");

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
         * Factory method for creating ActionGroupingBehavior.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding ActionGroupingBehavior.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "visual-group":
                return VISUAL_GROUP;
            case "logical-group":
                return LOGICAL_GROUP;
            case "sentence-group":
                return SENTENCE_GROUP;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
