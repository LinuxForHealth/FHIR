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

@System("http://hl7.org/fhir/document-mode")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class DocumentMode extends Code {
    /**
     * Producer
     * 
     * <p>The application produces documents of the specified type.
     */
    public static final DocumentMode PRODUCER = DocumentMode.builder().value(Value.PRODUCER).build();

    /**
     * Consumer
     * 
     * <p>The application consumes documents of the specified type.
     */
    public static final DocumentMode CONSUMER = DocumentMode.builder().value(Value.CONSUMER).build();

    private volatile int hashCode;

    private DocumentMode(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this DocumentMode as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating DocumentMode objects from a passed enum value.
     */
    public static DocumentMode of(Value value) {
        switch (value) {
        case PRODUCER:
            return PRODUCER;
        case CONSUMER:
            return CONSUMER;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating DocumentMode objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static DocumentMode of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating DocumentMode objects from a passed string value.
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
     * Inherited factory method for creating DocumentMode objects from a passed string value.
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
        DocumentMode other = (DocumentMode) obj;
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
         *     An enum constant for DocumentMode
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public DocumentMode build() {
            DocumentMode documentMode = new DocumentMode(this);
            if (validating) {
                validate(documentMode);
            }
            return documentMode;
        }

        protected void validate(DocumentMode documentMode) {
            super.validate(documentMode);
        }

        protected Builder from(DocumentMode documentMode) {
            super.from(documentMode);
            return this;
        }
    }

    public enum Value {
        /**
         * Producer
         * 
         * <p>The application produces documents of the specified type.
         */
        PRODUCER("producer"),

        /**
         * Consumer
         * 
         * <p>The application consumes documents of the specified type.
         */
        CONSUMER("consumer");

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
         * Factory method for creating DocumentMode.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding DocumentMode.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "producer":
                return PRODUCER;
            case "consumer":
                return CONSUMER;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
