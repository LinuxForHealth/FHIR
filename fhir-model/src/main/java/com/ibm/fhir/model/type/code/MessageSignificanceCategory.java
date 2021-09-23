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

@System("http://hl7.org/fhir/message-significance-category")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class MessageSignificanceCategory extends Code {
    /**
     * Consequence
     * 
     * <p>The message represents/requests a change that should not be processed more than once; e.g., making a booking for an 
     * appointment.
     */
    public static final MessageSignificanceCategory CONSEQUENCE = MessageSignificanceCategory.builder().value(Value.CONSEQUENCE).build();

    /**
     * Currency
     * 
     * <p>The message represents a response to query for current information. Retrospective processing is wrong and/or 
     * wasteful.
     */
    public static final MessageSignificanceCategory CURRENCY = MessageSignificanceCategory.builder().value(Value.CURRENCY).build();

    /**
     * Notification
     * 
     * <p>The content is not necessarily intended to be current, and it can be reprocessed, though there may be version 
     * issues created by processing old notifications.
     */
    public static final MessageSignificanceCategory NOTIFICATION = MessageSignificanceCategory.builder().value(Value.NOTIFICATION).build();

    private volatile int hashCode;

    private MessageSignificanceCategory(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this MessageSignificanceCategory as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating MessageSignificanceCategory objects from a passed enum value.
     */
    public static MessageSignificanceCategory of(Value value) {
        switch (value) {
        case CONSEQUENCE:
            return CONSEQUENCE;
        case CURRENCY:
            return CURRENCY;
        case NOTIFICATION:
            return NOTIFICATION;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating MessageSignificanceCategory objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static MessageSignificanceCategory of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating MessageSignificanceCategory objects from a passed string value.
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
     * Inherited factory method for creating MessageSignificanceCategory objects from a passed string value.
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
        MessageSignificanceCategory other = (MessageSignificanceCategory) obj;
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
         *     An enum constant for MessageSignificanceCategory
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public MessageSignificanceCategory build() {
            MessageSignificanceCategory messageSignificanceCategory = new MessageSignificanceCategory(this);
            if (validating) {
                validate(messageSignificanceCategory);
            }
            return messageSignificanceCategory;
        }

        protected void validate(MessageSignificanceCategory messageSignificanceCategory) {
            super.validate(messageSignificanceCategory);
        }

        protected Builder from(MessageSignificanceCategory messageSignificanceCategory) {
            super.from(messageSignificanceCategory);
            return this;
        }
    }

    public enum Value {
        /**
         * Consequence
         * 
         * <p>The message represents/requests a change that should not be processed more than once; e.g., making a booking for an 
         * appointment.
         */
        CONSEQUENCE("consequence"),

        /**
         * Currency
         * 
         * <p>The message represents a response to query for current information. Retrospective processing is wrong and/or 
         * wasteful.
         */
        CURRENCY("currency"),

        /**
         * Notification
         * 
         * <p>The content is not necessarily intended to be current, and it can be reprocessed, though there may be version 
         * issues created by processing old notifications.
         */
        NOTIFICATION("notification");

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
         * Factory method for creating MessageSignificanceCategory.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding MessageSignificanceCategory.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "consequence":
                return CONSEQUENCE;
            case "currency":
                return CURRENCY;
            case "notification":
                return NOTIFICATION;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
