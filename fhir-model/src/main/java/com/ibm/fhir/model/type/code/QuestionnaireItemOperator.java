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

@System("http://hl7.org/fhir/questionnaire-enable-operator")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class QuestionnaireItemOperator extends Code {
    /**
     * Exists
     */
    public static final QuestionnaireItemOperator EXISTS = QuestionnaireItemOperator.builder().value(ValueSet.EXISTS).build();

    /**
     * Equals
     */
    public static final QuestionnaireItemOperator EQUALS = QuestionnaireItemOperator.builder().value(ValueSet.EQUALS).build();

    /**
     * Not Equals
     */
    public static final QuestionnaireItemOperator NOT_EQUALS = QuestionnaireItemOperator.builder().value(ValueSet.NOT_EQUALS).build();

    /**
     * Greater Than
     */
    public static final QuestionnaireItemOperator GREATER_THAN = QuestionnaireItemOperator.builder().value(ValueSet.GREATER_THAN).build();

    /**
     * Less Than
     */
    public static final QuestionnaireItemOperator LESS_THAN = QuestionnaireItemOperator.builder().value(ValueSet.LESS_THAN).build();

    /**
     * Greater or Equals
     */
    public static final QuestionnaireItemOperator GREATER_OR_EQUALS = QuestionnaireItemOperator.builder().value(ValueSet.GREATER_OR_EQUALS).build();

    /**
     * Less or Equals
     */
    public static final QuestionnaireItemOperator LESS_OR_EQUALS = QuestionnaireItemOperator.builder().value(ValueSet.LESS_OR_EQUALS).build();

    private volatile int hashCode;

    private QuestionnaireItemOperator(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    public static QuestionnaireItemOperator of(ValueSet value) {
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

    public static QuestionnaireItemOperator of(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    public static String string(java.lang.String value) {
        return of(ValueSet.from(value));
    }

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
        public QuestionnaireItemOperator build() {
            return new QuestionnaireItemOperator(this);
        }
    }

    public enum ValueSet {
        /**
         * Exists
         */
        EXISTS("exists"),

        /**
         * Equals
         */
        EQUALS("="),

        /**
         * Not Equals
         */
        NOT_EQUALS("!="),

        /**
         * Greater Than
         */
        GREATER_THAN(">"),

        /**
         * Less Than
         */
        LESS_THAN("<"),

        /**
         * Greater or Equals
         */
        GREATER_OR_EQUALS(">="),

        /**
         * Less or Equals
         */
        LESS_OR_EQUALS("<=");

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
