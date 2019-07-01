/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

public class QuestionnaireItemType extends Code {
    /**
     * Group
     */
    public static final QuestionnaireItemType GROUP = QuestionnaireItemType.of(ValueSet.GROUP);

    /**
     * Display
     */
    public static final QuestionnaireItemType DISPLAY = QuestionnaireItemType.of(ValueSet.DISPLAY);

    /**
     * Question
     */
    public static final QuestionnaireItemType QUESTION = QuestionnaireItemType.of(ValueSet.QUESTION);

    /**
     * Boolean
     */
    public static final QuestionnaireItemType BOOLEAN = QuestionnaireItemType.of(ValueSet.BOOLEAN);

    /**
     * Decimal
     */
    public static final QuestionnaireItemType DECIMAL = QuestionnaireItemType.of(ValueSet.DECIMAL);

    /**
     * Integer
     */
    public static final QuestionnaireItemType INTEGER = QuestionnaireItemType.of(ValueSet.INTEGER);

    /**
     * Date
     */
    public static final QuestionnaireItemType DATE = QuestionnaireItemType.of(ValueSet.DATE);

    /**
     * Date Time
     */
    public static final QuestionnaireItemType DATE_TIME = QuestionnaireItemType.of(ValueSet.DATE_TIME);

    /**
     * Time
     */
    public static final QuestionnaireItemType TIME = QuestionnaireItemType.of(ValueSet.TIME);

    /**
     * String
     */
    public static final QuestionnaireItemType STRING = QuestionnaireItemType.of(ValueSet.STRING);

    /**
     * Text
     */
    public static final QuestionnaireItemType TEXT = QuestionnaireItemType.of(ValueSet.TEXT);

    /**
     * Url
     */
    public static final QuestionnaireItemType URL = QuestionnaireItemType.of(ValueSet.URL);

    /**
     * Choice
     */
    public static final QuestionnaireItemType CHOICE = QuestionnaireItemType.of(ValueSet.CHOICE);

    /**
     * Open Choice
     */
    public static final QuestionnaireItemType OPEN_CHOICE = QuestionnaireItemType.of(ValueSet.OPEN_CHOICE);

    /**
     * Attachment
     */
    public static final QuestionnaireItemType ATTACHMENT = QuestionnaireItemType.of(ValueSet.ATTACHMENT);

    /**
     * Reference
     */
    public static final QuestionnaireItemType REFERENCE = QuestionnaireItemType.of(ValueSet.REFERENCE);

    /**
     * Quantity
     */
    public static final QuestionnaireItemType QUANTITY = QuestionnaireItemType.of(ValueSet.QUANTITY);

    private volatile int hashCode;

    private QuestionnaireItemType(Builder builder) {
        super(builder);
    }

    public static QuestionnaireItemType of(java.lang.String value) {
        return QuestionnaireItemType.builder().value(value).build();
    }

    public static QuestionnaireItemType of(ValueSet value) {
        return QuestionnaireItemType.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return QuestionnaireItemType.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return QuestionnaireItemType.builder().value(value).build();
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
        QuestionnaireItemType other = (QuestionnaireItemType) obj;
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
        builder.id = id;
        builder.extension.addAll(extension);
        builder.value = value;
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
        public QuestionnaireItemType build() {
            return new QuestionnaireItemType(this);
        }
    }

    public enum ValueSet {
        /**
         * Group
         */
        GROUP("group"),

        /**
         * Display
         */
        DISPLAY("display"),

        /**
         * Question
         */
        QUESTION("question"),

        /**
         * Boolean
         */
        BOOLEAN("boolean"),

        /**
         * Decimal
         */
        DECIMAL("decimal"),

        /**
         * Integer
         */
        INTEGER("integer"),

        /**
         * Date
         */
        DATE("date"),

        /**
         * Date Time
         */
        DATE_TIME("dateTime"),

        /**
         * Time
         */
        TIME("time"),

        /**
         * String
         */
        STRING("string"),

        /**
         * Text
         */
        TEXT("text"),

        /**
         * Url
         */
        URL("url"),

        /**
         * Choice
         */
        CHOICE("choice"),

        /**
         * Open Choice
         */
        OPEN_CHOICE("open-choice"),

        /**
         * Attachment
         */
        ATTACHMENT("attachment"),

        /**
         * Reference
         */
        REFERENCE("reference"),

        /**
         * Quantity
         */
        QUANTITY("quantity");

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
