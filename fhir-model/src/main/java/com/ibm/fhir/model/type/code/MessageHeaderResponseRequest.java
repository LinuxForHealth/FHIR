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

@System("http://hl7.org/fhir/messageheader-response-request")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class MessageHeaderResponseRequest extends Code {
    /**
     * Always
     * 
     * <p>initiator expects a response for this message.
     */
    public static final MessageHeaderResponseRequest ALWAYS = MessageHeaderResponseRequest.builder().value(Value.ALWAYS).build();

    /**
     * Error/reject conditions only
     * 
     * <p>initiator expects a response only if in error.
     */
    public static final MessageHeaderResponseRequest ON_ERROR = MessageHeaderResponseRequest.builder().value(Value.ON_ERROR).build();

    /**
     * Never
     * 
     * <p>initiator does not expect a response.
     */
    public static final MessageHeaderResponseRequest NEVER = MessageHeaderResponseRequest.builder().value(Value.NEVER).build();

    /**
     * Successful completion only
     * 
     * <p>initiator expects a response only if successful.
     */
    public static final MessageHeaderResponseRequest ON_SUCCESS = MessageHeaderResponseRequest.builder().value(Value.ON_SUCCESS).build();

    private volatile int hashCode;

    private MessageHeaderResponseRequest(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this MessageHeaderResponseRequest as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating MessageHeaderResponseRequest objects from a passed enum value.
     */
    public static MessageHeaderResponseRequest of(Value value) {
        switch (value) {
        case ALWAYS:
            return ALWAYS;
        case ON_ERROR:
            return ON_ERROR;
        case NEVER:
            return NEVER;
        case ON_SUCCESS:
            return ON_SUCCESS;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating MessageHeaderResponseRequest objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static MessageHeaderResponseRequest of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating MessageHeaderResponseRequest objects from a passed string value.
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
     * Inherited factory method for creating MessageHeaderResponseRequest objects from a passed string value.
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
        MessageHeaderResponseRequest other = (MessageHeaderResponseRequest) obj;
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
         *     An enum constant for MessageHeaderResponseRequest
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public MessageHeaderResponseRequest build() {
            MessageHeaderResponseRequest messageHeaderResponseRequest = new MessageHeaderResponseRequest(this);
            if (validating) {
                validate(messageHeaderResponseRequest);
            }
            return messageHeaderResponseRequest;
        }

        protected void validate(MessageHeaderResponseRequest messageHeaderResponseRequest) {
            super.validate(messageHeaderResponseRequest);
        }

        protected Builder from(MessageHeaderResponseRequest messageHeaderResponseRequest) {
            super.from(messageHeaderResponseRequest);
            return this;
        }
    }

    public enum Value {
        /**
         * Always
         * 
         * <p>initiator expects a response for this message.
         */
        ALWAYS("always"),

        /**
         * Error/reject conditions only
         * 
         * <p>initiator expects a response only if in error.
         */
        ON_ERROR("on-error"),

        /**
         * Never
         * 
         * <p>initiator does not expect a response.
         */
        NEVER("never"),

        /**
         * Successful completion only
         * 
         * <p>initiator expects a response only if successful.
         */
        ON_SUCCESS("on-success");

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
         * Factory method for creating MessageHeaderResponseRequest.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding MessageHeaderResponseRequest.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "always":
                return ALWAYS;
            case "on-error":
                return ON_ERROR;
            case "never":
                return NEVER;
            case "on-success":
                return ON_SUCCESS;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
