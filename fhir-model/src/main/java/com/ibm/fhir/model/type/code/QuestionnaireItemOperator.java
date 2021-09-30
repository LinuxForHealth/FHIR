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

@System("http://hl7.org/fhir/questionnaire-enable-operator")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class QuestionnaireItemOperator extends Code {
    /**
     * Exists
     * 
     * <p>True if whether an answer exists is equal to the enableWhen answer (which must be a boolean).
     */
    public static final QuestionnaireItemOperator EXISTS = QuestionnaireItemOperator.builder().value(Value.EXISTS).build();

    /**
     * Equals
     * 
     * <p>True if whether at least one answer has a value that is equal to the enableWhen answer.
     */
    public static final QuestionnaireItemOperator EQUALS = QuestionnaireItemOperator.builder().value(Value.EQUALS).build();

    /**
     * Not Equals
     * 
     * <p>True if whether at least no answer has a value that is equal to the enableWhen answer.
     */
    public static final QuestionnaireItemOperator NOT_EQUALS = QuestionnaireItemOperator.builder().value(Value.NOT_EQUALS).build();

    /**
     * Greater Than
     * 
     * <p>True if whether at least no answer has a value that is greater than the enableWhen answer.
     */
    public static final QuestionnaireItemOperator GREATER_THAN = QuestionnaireItemOperator.builder().value(Value.GREATER_THAN).build();

    /**
     * Less Than
     * 
     * <p>True if whether at least no answer has a value that is less than the enableWhen answer.
     */
    public static final QuestionnaireItemOperator LESS_THAN = QuestionnaireItemOperator.builder().value(Value.LESS_THAN).build();

    /**
     * Greater or Equals
     * 
     * <p>True if whether at least no answer has a value that is greater or equal to the enableWhen answer.
     */
    public static final QuestionnaireItemOperator GREATER_OR_EQUALS = QuestionnaireItemOperator.builder().value(Value.GREATER_OR_EQUALS).build();

    /**
     * Less or Equals
     * 
     * <p>True if whether at least no answer has a value that is less or equal to the enableWhen answer.
     */
    public static final QuestionnaireItemOperator LESS_OR_EQUALS = QuestionnaireItemOperator.builder().value(Value.LESS_OR_EQUALS).build();

    private volatile int hashCode;

    private QuestionnaireItemOperator(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this QuestionnaireItemOperator as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating QuestionnaireItemOperator objects from a passed enum value.
     */
    public static QuestionnaireItemOperator of(Value value) {
        switch (value) {
        case EXISTS:
            return EXISTS;
        case EQUALS:
            return EQUALS;
        case NOT_EQUALS:
            return NOT_EQUALS;
        case GREATER_THAN:
            return GREATER_THAN;
        case LESS_THAN:
            return LESS_THAN;
        case GREATER_OR_EQUALS:
            return GREATER_OR_EQUALS;
        case LESS_OR_EQUALS:
            return LESS_OR_EQUALS;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating QuestionnaireItemOperator objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static QuestionnaireItemOperator of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating QuestionnaireItemOperator objects from a passed string value.
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
     * Inherited factory method for creating QuestionnaireItemOperator objects from a passed string value.
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
        QuestionnaireItemOperator other = (QuestionnaireItemOperator) obj;
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
         *     An enum constant for QuestionnaireItemOperator
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public QuestionnaireItemOperator build() {
            QuestionnaireItemOperator questionnaireItemOperator = new QuestionnaireItemOperator(this);
            if (validating) {
                validate(questionnaireItemOperator);
            }
            return questionnaireItemOperator;
        }

        protected void validate(QuestionnaireItemOperator questionnaireItemOperator) {
            super.validate(questionnaireItemOperator);
        }

        protected Builder from(QuestionnaireItemOperator questionnaireItemOperator) {
            super.from(questionnaireItemOperator);
            return this;
        }
    }

    public enum Value {
        /**
         * Exists
         * 
         * <p>True if whether an answer exists is equal to the enableWhen answer (which must be a boolean).
         */
        EXISTS("exists"),

        /**
         * Equals
         * 
         * <p>True if whether at least one answer has a value that is equal to the enableWhen answer.
         */
        EQUALS("="),

        /**
         * Not Equals
         * 
         * <p>True if whether at least no answer has a value that is equal to the enableWhen answer.
         */
        NOT_EQUALS("!="),

        /**
         * Greater Than
         * 
         * <p>True if whether at least no answer has a value that is greater than the enableWhen answer.
         */
        GREATER_THAN(">"),

        /**
         * Less Than
         * 
         * <p>True if whether at least no answer has a value that is less than the enableWhen answer.
         */
        LESS_THAN("<"),

        /**
         * Greater or Equals
         * 
         * <p>True if whether at least no answer has a value that is greater or equal to the enableWhen answer.
         */
        GREATER_OR_EQUALS(">="),

        /**
         * Less or Equals
         * 
         * <p>True if whether at least no answer has a value that is less or equal to the enableWhen answer.
         */
        LESS_OR_EQUALS("<=");

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
         * Factory method for creating QuestionnaireItemOperator.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding QuestionnaireItemOperator.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "exists":
                return EXISTS;
            case "=":
                return EQUALS;
            case "!=":
                return NOT_EQUALS;
            case ">":
                return GREATER_THAN;
            case "<":
                return LESS_THAN;
            case ">=":
                return GREATER_OR_EQUALS;
            case "<=":
                return LESS_OR_EQUALS;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
