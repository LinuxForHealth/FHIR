/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type.code;

import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.String;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

@Generated("com.ibm.fhir.tools.CodeGenerator")
public class QuestionnaireResponseStatus extends Code {
    /**
     * In Progress
     */
    public static final QuestionnaireResponseStatus IN_PROGRESS = QuestionnaireResponseStatus.of(ValueSet.IN_PROGRESS);

    /**
     * Completed
     */
    public static final QuestionnaireResponseStatus COMPLETED = QuestionnaireResponseStatus.of(ValueSet.COMPLETED);

    /**
     * Amended
     */
    public static final QuestionnaireResponseStatus AMENDED = QuestionnaireResponseStatus.of(ValueSet.AMENDED);

    /**
     * Entered in Error
     */
    public static final QuestionnaireResponseStatus ENTERED_IN_ERROR = QuestionnaireResponseStatus.of(ValueSet.ENTERED_IN_ERROR);

    /**
     * Stopped
     */
    public static final QuestionnaireResponseStatus STOPPED = QuestionnaireResponseStatus.of(ValueSet.STOPPED);

    private volatile int hashCode;

    private QuestionnaireResponseStatus(Builder builder) {
        super(builder);
    }

    public static QuestionnaireResponseStatus of(java.lang.String value) {
        return QuestionnaireResponseStatus.builder().value(value).build();
    }

    public static QuestionnaireResponseStatus of(ValueSet value) {
        return QuestionnaireResponseStatus.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return QuestionnaireResponseStatus.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return QuestionnaireResponseStatus.builder().value(value).build();
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
            return (Builder) super.value(ValueSet.from(value).value());
        }

        public Builder value(ValueSet value) {
            return (Builder) super.value(value.value());
        }

        @Override
        public QuestionnaireResponseStatus build() {
            return new QuestionnaireResponseStatus(this);
        }
    }

    public enum ValueSet {
        /**
         * In Progress
         */
        IN_PROGRESS("in-progress"),

        /**
         * Completed
         */
        COMPLETED("completed"),

        /**
         * Amended
         */
        AMENDED("amended"),

        /**
         * Entered in Error
         */
        ENTERED_IN_ERROR("entered-in-error"),

        /**
         * Stopped
         */
        STOPPED("stopped");

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
