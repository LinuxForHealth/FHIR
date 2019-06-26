/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class QuestionnaireItemOperator extends Code {
    /**
     * Exists
     */
    public static final QuestionnaireItemOperator EXISTS = QuestionnaireItemOperator.of(ValueSet.EXISTS);

    /**
     * Equals
     */
    public static final QuestionnaireItemOperator EQUALS = QuestionnaireItemOperator.of(ValueSet.EQUALS);

    /**
     * Not Equals
     */
    public static final QuestionnaireItemOperator NOT_EQUALS = QuestionnaireItemOperator.of(ValueSet.NOT_EQUALS);

    /**
     * Greater Than
     */
    public static final QuestionnaireItemOperator GREATER_THAN = QuestionnaireItemOperator.of(ValueSet.GREATER_THAN);

    /**
     * Less Than
     */
    public static final QuestionnaireItemOperator LESS_THAN = QuestionnaireItemOperator.of(ValueSet.LESS_THAN);

    /**
     * Greater or Equals
     */
    public static final QuestionnaireItemOperator GREATER_OR_EQUALS = QuestionnaireItemOperator.of(ValueSet.GREATER_OR_EQUALS);

    /**
     * Less or Equals
     */
    public static final QuestionnaireItemOperator LESS_OR_EQUALS = QuestionnaireItemOperator.of(ValueSet.LESS_OR_EQUALS);

    private QuestionnaireItemOperator(Builder builder) {
        super(builder);
    }

    public static QuestionnaireItemOperator of(java.lang.String value) {
        return QuestionnaireItemOperator.builder().value(value).build();
    }

    public static QuestionnaireItemOperator of(ValueSet value) {
        return QuestionnaireItemOperator.builder().value(value).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.id = id;
        builder.extension.addAll(extension);
        builder.value = value;
        return builder;
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
