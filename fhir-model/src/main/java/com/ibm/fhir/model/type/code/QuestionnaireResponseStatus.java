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

@System("http://hl7.org/fhir/questionnaire-answers-status")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class QuestionnaireResponseStatus extends Code {
    /**
     * In Progress
     * 
     * <p>This QuestionnaireResponse has been partially filled out with answers but changes or additions are still expected 
     * to be made to it.
     */
    public static final QuestionnaireResponseStatus IN_PROGRESS = QuestionnaireResponseStatus.builder().value(ValueSet.IN_PROGRESS).build();

    /**
     * Completed
     * 
     * <p>This QuestionnaireResponse has been filled out with answers and the current content is regarded as definitive.
     */
    public static final QuestionnaireResponseStatus COMPLETED = QuestionnaireResponseStatus.builder().value(ValueSet.COMPLETED).build();

    /**
     * Amended
     * 
     * <p>This QuestionnaireResponse has been filled out with answers, then marked as complete, yet changes or additions have 
     * been made to it afterwards.
     */
    public static final QuestionnaireResponseStatus AMENDED = QuestionnaireResponseStatus.builder().value(ValueSet.AMENDED).build();

    /**
     * Entered in Error
     * 
     * <p>This QuestionnaireResponse was entered in error and voided.
     */
    public static final QuestionnaireResponseStatus ENTERED_IN_ERROR = QuestionnaireResponseStatus.builder().value(ValueSet.ENTERED_IN_ERROR).build();

    /**
     * Stopped
     * 
     * <p>This QuestionnaireResponse has been partially filled out with answers but has been abandoned. It is unknown whether 
     * changes or additions are expected to be made to it.
     */
    public static final QuestionnaireResponseStatus STOPPED = QuestionnaireResponseStatus.builder().value(ValueSet.STOPPED).build();

    private volatile int hashCode;

    private QuestionnaireResponseStatus(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    /**
     * Factory method for creating QuestionnaireResponseStatus objects from a passed enum value.
     */
    public static QuestionnaireResponseStatus of(ValueSet value) {
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
        return of(ValueSet.from(value));
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
        return of(ValueSet.from(value));
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
        public QuestionnaireResponseStatus build() {
            return new QuestionnaireResponseStatus(this);
        }
    }

    public enum ValueSet {
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
         * Factory method for creating QuestionnaireResponseStatus.ValueSet values from a passed string value.
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
