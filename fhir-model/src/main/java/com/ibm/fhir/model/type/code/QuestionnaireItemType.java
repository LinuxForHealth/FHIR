/*
 * (C) Copyright IBM Corp. 2019, 2020
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
public class QuestionnaireItemType extends Code {
    /**
     * Group
     */
    public static final QuestionnaireItemType GROUP = QuestionnaireItemType.builder().value(ValueSet.GROUP).build();

    /**
     * Display
     */
    public static final QuestionnaireItemType DISPLAY = QuestionnaireItemType.builder().value(ValueSet.DISPLAY).build();

    /**
     * Question
     */
    public static final QuestionnaireItemType QUESTION = QuestionnaireItemType.builder().value(ValueSet.QUESTION).build();

    /**
     * Boolean
     */
    public static final QuestionnaireItemType BOOLEAN = QuestionnaireItemType.builder().value(ValueSet.BOOLEAN).build();

    /**
     * Decimal
     */
    public static final QuestionnaireItemType DECIMAL = QuestionnaireItemType.builder().value(ValueSet.DECIMAL).build();

    /**
     * Integer
     */
    public static final QuestionnaireItemType INTEGER = QuestionnaireItemType.builder().value(ValueSet.INTEGER).build();

    /**
     * Date
     */
    public static final QuestionnaireItemType DATE = QuestionnaireItemType.builder().value(ValueSet.DATE).build();

    /**
     * Date Time
     */
    public static final QuestionnaireItemType DATE_TIME = QuestionnaireItemType.builder().value(ValueSet.DATE_TIME).build();

    /**
     * Time
     */
    public static final QuestionnaireItemType TIME = QuestionnaireItemType.builder().value(ValueSet.TIME).build();

    /**
     * String
     */
    public static final QuestionnaireItemType STRING = QuestionnaireItemType.builder().value(ValueSet.STRING).build();

    /**
     * Text
     */
    public static final QuestionnaireItemType TEXT = QuestionnaireItemType.builder().value(ValueSet.TEXT).build();

    /**
     * Url
     */
    public static final QuestionnaireItemType URL = QuestionnaireItemType.builder().value(ValueSet.URL).build();

    /**
     * Choice
     */
    public static final QuestionnaireItemType CHOICE = QuestionnaireItemType.builder().value(ValueSet.CHOICE).build();

    /**
     * Open Choice
     */
    public static final QuestionnaireItemType OPEN_CHOICE = QuestionnaireItemType.builder().value(ValueSet.OPEN_CHOICE).build();

    /**
     * Attachment
     */
    public static final QuestionnaireItemType ATTACHMENT = QuestionnaireItemType.builder().value(ValueSet.ATTACHMENT).build();

    /**
     * Reference
     */
    public static final QuestionnaireItemType REFERENCE = QuestionnaireItemType.builder().value(ValueSet.REFERENCE).build();

    /**
     * Quantity
     */
    public static final QuestionnaireItemType QUANTITY = QuestionnaireItemType.builder().value(ValueSet.QUANTITY).build();

    private volatile int hashCode;

    private QuestionnaireItemType(Builder builder) {
        super(builder);
    }

    public static QuestionnaireItemType of(ValueSet value) {
        switch (value) {
        case GROUP:
            return GROUP;
        case DISPLAY:
            return DISPLAY;
        case QUESTION:
            return QUESTION;
        case BOOLEAN:
            return BOOLEAN;
        case DECIMAL:
            return DECIMAL;
        case INTEGER:
            return INTEGER;
        case DATE:
            return DATE;
        case DATE_TIME:
            return DATE_TIME;
        case TIME:
            return TIME;
        case STRING:
            return STRING;
        case TEXT:
            return TEXT;
        case URL:
            return URL;
        case CHOICE:
            return CHOICE;
        case OPEN_CHOICE:
            return OPEN_CHOICE;
        case ATTACHMENT:
            return ATTACHMENT;
        case REFERENCE:
            return REFERENCE;
        case QUANTITY:
            return QUANTITY;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    public static QuestionnaireItemType of(java.lang.String value) {
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
