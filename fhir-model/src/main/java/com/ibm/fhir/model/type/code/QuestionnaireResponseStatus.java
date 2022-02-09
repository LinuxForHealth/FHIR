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

@System("http://hl7.org/fhir/questionnaire-answers-status")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class QuestionnaireResponseStatus extends Code {
    /**
     * In Progress
     * 
     * <p>This QuestionnaireResponse has been partially filled out with answers but changes or additions are still expected 
     * to be made to it.
     */
    public static final QuestionnaireResponseStatus IN_PROGRESS = QuestionnaireResponseStatus.builder().value(Value.IN_PROGRESS).build();

    /**
     * Completed
     * 
     * <p>This QuestionnaireResponse has been filled out with answers and the current content is regarded as definitive.
     */
    public static final QuestionnaireResponseStatus COMPLETED = QuestionnaireResponseStatus.builder().value(Value.COMPLETED).build();

    /**
     * Amended
     * 
     * <p>This QuestionnaireResponse has been filled out with answers, then marked as complete, yet changes or additions have 
     * been made to it afterwards.
     */
    public static final QuestionnaireResponseStatus AMENDED = QuestionnaireResponseStatus.builder().value(Value.AMENDED).build();

    /**
     * Entered in Error
     * 
     * <p>This QuestionnaireResponse was entered in error and voided.
     */
    public static final QuestionnaireResponseStatus ENTERED_IN_ERROR = QuestionnaireResponseStatus.builder().value(Value.ENTERED_IN_ERROR).build();

    /**
     * Stopped
     * 
     * <p>This QuestionnaireResponse has been partially filled out with answers but has been abandoned. It is unknown whether 
     * changes or additions are expected to be made to it.
     */
    public static final QuestionnaireResponseStatus STOPPED = QuestionnaireResponseStatus.builder().value(Value.STOPPED).build();

    private volatile int hashCode;

    private QuestionnaireResponseStatus(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this QuestionnaireResponseStatus as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating QuestionnaireResponseStatus objects from a passed enum value.
     */
    public static QuestionnaireResponseStatus of(Value value) {
        switch (value) {
        case IN_PROGRESS:
            return IN_PROGRESS;
        case COMPLETED:
            return COMPLETED;
        case AMENDED:
            return AMENDED;
        case ENTERED_IN_ERROR:
            return ENTERED_IN_ERROR;
        case STOPPED:
            return STOPPED;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating QuestionnaireResponseStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static QuestionnaireResponseStatus of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating QuestionnaireResponseStatus objects from a passed string value.
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
     * Inherited factory method for creating QuestionnaireResponseStatus objects from a passed string value.
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
        QuestionnaireResponseStatus other = (QuestionnaireResponseStatus) obj;
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
         *     An enum constant for QuestionnaireResponseStatus
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public QuestionnaireResponseStatus build() {
            QuestionnaireResponseStatus questionnaireResponseStatus = new QuestionnaireResponseStatus(this);
            if (validating) {
                validate(questionnaireResponseStatus);
            }
            return questionnaireResponseStatus;
        }

        protected void validate(QuestionnaireResponseStatus questionnaireResponseStatus) {
            super.validate(questionnaireResponseStatus);
        }

        protected Builder from(QuestionnaireResponseStatus questionnaireResponseStatus) {
            super.from(questionnaireResponseStatus);
            return this;
        }
    }

    public enum Value {
        /**
         * In Progress
         * 
         * <p>This QuestionnaireResponse has been partially filled out with answers but changes or additions are still expected 
         * to be made to it.
         */
        IN_PROGRESS("in-progress"),

        /**
         * Completed
         * 
         * <p>This QuestionnaireResponse has been filled out with answers and the current content is regarded as definitive.
         */
        COMPLETED("completed"),

        /**
         * Amended
         * 
         * <p>This QuestionnaireResponse has been filled out with answers, then marked as complete, yet changes or additions have 
         * been made to it afterwards.
         */
        AMENDED("amended"),

        /**
         * Entered in Error
         * 
         * <p>This QuestionnaireResponse was entered in error and voided.
         */
        ENTERED_IN_ERROR("entered-in-error"),

        /**
         * Stopped
         * 
         * <p>This QuestionnaireResponse has been partially filled out with answers but has been abandoned. It is unknown whether 
         * changes or additions are expected to be made to it.
         */
        STOPPED("stopped");

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
         * Factory method for creating QuestionnaireResponseStatus.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding QuestionnaireResponseStatus.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "in-progress":
                return IN_PROGRESS;
            case "completed":
                return COMPLETED;
            case "amended":
                return AMENDED;
            case "entered-in-error":
                return ENTERED_IN_ERROR;
            case "stopped":
                return STOPPED;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
