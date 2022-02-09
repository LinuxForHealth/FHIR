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

@System("http://hl7.org/fhir/item-type")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class QuestionnaireItemType extends Code {
    /**
     * Group
     * 
     * <p>An item with no direct answer but should have at least one child item.
     */
    public static final QuestionnaireItemType GROUP = QuestionnaireItemType.builder().value(Value.GROUP).build();

    /**
     * Display
     * 
     * <p>Text for display that will not capture an answer or have child items.
     */
    public static final QuestionnaireItemType DISPLAY = QuestionnaireItemType.builder().value(Value.DISPLAY).build();

    /**
     * Question
     * 
     * <p>An item that defines a specific answer to be captured, and which may have child items. (the answer provided in the 
     * QuestionnaireResponse should be of the defined datatype).
     */
    public static final QuestionnaireItemType QUESTION = QuestionnaireItemType.builder().value(Value.QUESTION).build();

    /**
     * Boolean
     * 
     * <p>Question with a yes/no answer (valueBoolean).
     */
    public static final QuestionnaireItemType BOOLEAN = QuestionnaireItemType.builder().value(Value.BOOLEAN).build();

    /**
     * Decimal
     * 
     * <p>Question with is a real number answer (valueDecimal).
     */
    public static final QuestionnaireItemType DECIMAL = QuestionnaireItemType.builder().value(Value.DECIMAL).build();

    /**
     * Integer
     * 
     * <p>Question with an integer answer (valueInteger).
     */
    public static final QuestionnaireItemType INTEGER = QuestionnaireItemType.builder().value(Value.INTEGER).build();

    /**
     * Date
     * 
     * <p>Question with a date answer (valueDate).
     */
    public static final QuestionnaireItemType DATE = QuestionnaireItemType.builder().value(Value.DATE).build();

    /**
     * Date Time
     * 
     * <p>Question with a date and time answer (valueDateTime).
     */
    public static final QuestionnaireItemType DATE_TIME = QuestionnaireItemType.builder().value(Value.DATE_TIME).build();

    /**
     * Time
     * 
     * <p>Question with a time (hour:minute:second) answer independent of date. (valueTime).
     */
    public static final QuestionnaireItemType TIME = QuestionnaireItemType.builder().value(Value.TIME).build();

    /**
     * String
     * 
     * <p>Question with a short (few words to short sentence) free-text entry answer (valueString).
     */
    public static final QuestionnaireItemType STRING = QuestionnaireItemType.builder().value(Value.STRING).build();

    /**
     * Text
     * 
     * <p>Question with a long (potentially multi-paragraph) free-text entry answer (valueString).
     */
    public static final QuestionnaireItemType TEXT = QuestionnaireItemType.builder().value(Value.TEXT).build();

    /**
     * Url
     * 
     * <p>Question with a URL (website, FTP site, etc.) answer (valueUri).
     */
    public static final QuestionnaireItemType URL = QuestionnaireItemType.builder().value(Value.URL).build();

    /**
     * Choice
     * 
     * <p>Question with a Coding drawn from a list of possible answers (specified in either the answerOption property, or via 
     * the valueset referenced in the answerValueSet property) as an answer (valueCoding).
     */
    public static final QuestionnaireItemType CHOICE = QuestionnaireItemType.builder().value(Value.CHOICE).build();

    /**
     * Open Choice
     * 
     * <p>Answer is a Coding drawn from a list of possible answers (as with the choice type) or a free-text entry in a string 
     * (valueCoding or valueString).
     */
    public static final QuestionnaireItemType OPEN_CHOICE = QuestionnaireItemType.builder().value(Value.OPEN_CHOICE).build();

    /**
     * Attachment
     * 
     * <p>Question with binary content such as an image, PDF, etc. as an answer (valueAttachment).
     */
    public static final QuestionnaireItemType ATTACHMENT = QuestionnaireItemType.builder().value(Value.ATTACHMENT).build();

    /**
     * Reference
     * 
     * <p>Question with a reference to another resource (practitioner, organization, etc.) as an answer (valueReference).
     */
    public static final QuestionnaireItemType REFERENCE = QuestionnaireItemType.builder().value(Value.REFERENCE).build();

    /**
     * Quantity
     * 
     * <p>Question with a combination of a numeric value and unit, potentially with a comparator (&lt;, &gt;, etc.) as an 
     * answer. (valueQuantity) There is an extension 'http://hl7.org/fhir/StructureDefinition/questionnaire-unit' that can be 
     * used to define what unit should be captured (or the unit that has a ucum conversion from the provided unit).
     */
    public static final QuestionnaireItemType QUANTITY = QuestionnaireItemType.builder().value(Value.QUANTITY).build();

    private volatile int hashCode;

    private QuestionnaireItemType(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this QuestionnaireItemType as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating QuestionnaireItemType objects from a passed enum value.
     */
    public static QuestionnaireItemType of(Value value) {
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

    /**
     * Factory method for creating QuestionnaireItemType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static QuestionnaireItemType of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating QuestionnaireItemType objects from a passed string value.
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
     * Inherited factory method for creating QuestionnaireItemType objects from a passed string value.
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
         *     An enum constant for QuestionnaireItemType
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public QuestionnaireItemType build() {
            QuestionnaireItemType questionnaireItemType = new QuestionnaireItemType(this);
            if (validating) {
                validate(questionnaireItemType);
            }
            return questionnaireItemType;
        }

        protected void validate(QuestionnaireItemType questionnaireItemType) {
            super.validate(questionnaireItemType);
        }

        protected Builder from(QuestionnaireItemType questionnaireItemType) {
            super.from(questionnaireItemType);
            return this;
        }
    }

    public enum Value {
        /**
         * Group
         * 
         * <p>An item with no direct answer but should have at least one child item.
         */
        GROUP("group"),

        /**
         * Display
         * 
         * <p>Text for display that will not capture an answer or have child items.
         */
        DISPLAY("display"),

        /**
         * Question
         * 
         * <p>An item that defines a specific answer to be captured, and which may have child items. (the answer provided in the 
         * QuestionnaireResponse should be of the defined datatype).
         */
        QUESTION("question"),

        /**
         * Boolean
         * 
         * <p>Question with a yes/no answer (valueBoolean).
         */
        BOOLEAN("boolean"),

        /**
         * Decimal
         * 
         * <p>Question with is a real number answer (valueDecimal).
         */
        DECIMAL("decimal"),

        /**
         * Integer
         * 
         * <p>Question with an integer answer (valueInteger).
         */
        INTEGER("integer"),

        /**
         * Date
         * 
         * <p>Question with a date answer (valueDate).
         */
        DATE("date"),

        /**
         * Date Time
         * 
         * <p>Question with a date and time answer (valueDateTime).
         */
        DATE_TIME("dateTime"),

        /**
         * Time
         * 
         * <p>Question with a time (hour:minute:second) answer independent of date. (valueTime).
         */
        TIME("time"),

        /**
         * String
         * 
         * <p>Question with a short (few words to short sentence) free-text entry answer (valueString).
         */
        STRING("string"),

        /**
         * Text
         * 
         * <p>Question with a long (potentially multi-paragraph) free-text entry answer (valueString).
         */
        TEXT("text"),

        /**
         * Url
         * 
         * <p>Question with a URL (website, FTP site, etc.) answer (valueUri).
         */
        URL("url"),

        /**
         * Choice
         * 
         * <p>Question with a Coding drawn from a list of possible answers (specified in either the answerOption property, or via 
         * the valueset referenced in the answerValueSet property) as an answer (valueCoding).
         */
        CHOICE("choice"),

        /**
         * Open Choice
         * 
         * <p>Answer is a Coding drawn from a list of possible answers (as with the choice type) or a free-text entry in a string 
         * (valueCoding or valueString).
         */
        OPEN_CHOICE("open-choice"),

        /**
         * Attachment
         * 
         * <p>Question with binary content such as an image, PDF, etc. as an answer (valueAttachment).
         */
        ATTACHMENT("attachment"),

        /**
         * Reference
         * 
         * <p>Question with a reference to another resource (practitioner, organization, etc.) as an answer (valueReference).
         */
        REFERENCE("reference"),

        /**
         * Quantity
         * 
         * <p>Question with a combination of a numeric value and unit, potentially with a comparator (&lt;, &gt;, etc.) as an 
         * answer. (valueQuantity) There is an extension 'http://hl7.org/fhir/StructureDefinition/questionnaire-unit' that can be 
         * used to define what unit should be captured (or the unit that has a ucum conversion from the provided unit).
         */
        QUANTITY("quantity");

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
         * Factory method for creating QuestionnaireItemType.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding QuestionnaireItemType.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "group":
                return GROUP;
            case "display":
                return DISPLAY;
            case "question":
                return QUESTION;
            case "boolean":
                return BOOLEAN;
            case "decimal":
                return DECIMAL;
            case "integer":
                return INTEGER;
            case "date":
                return DATE;
            case "dateTime":
                return DATE_TIME;
            case "time":
                return TIME;
            case "string":
                return STRING;
            case "text":
                return TEXT;
            case "url":
                return URL;
            case "choice":
                return CHOICE;
            case "open-choice":
                return OPEN_CHOICE;
            case "attachment":
                return ATTACHMENT;
            case "reference":
                return REFERENCE;
            case "quantity":
                return QUANTITY;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
