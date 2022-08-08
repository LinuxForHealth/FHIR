/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.type.code;

import org.linuxforhealth.fhir.model.annotation.System;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.String;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

@System("http://hl7.org/fhir/restful-interaction")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class SystemRestfulInteraction extends Code {
    public static final SystemRestfulInteraction TRANSACTION = SystemRestfulInteraction.builder().value(Value.TRANSACTION).build();

    public static final SystemRestfulInteraction BATCH = SystemRestfulInteraction.builder().value(Value.BATCH).build();

    public static final SystemRestfulInteraction SEARCH_SYSTEM = SystemRestfulInteraction.builder().value(Value.SEARCH_SYSTEM).build();

    public static final SystemRestfulInteraction HISTORY_SYSTEM = SystemRestfulInteraction.builder().value(Value.HISTORY_SYSTEM).build();

    private volatile int hashCode;

    private SystemRestfulInteraction(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this SystemRestfulInteraction as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating SystemRestfulInteraction objects from a passed enum value.
     */
    public static SystemRestfulInteraction of(Value value) {
        switch (value) {
        case TRANSACTION:
            return TRANSACTION;
        case BATCH:
            return BATCH;
        case SEARCH_SYSTEM:
            return SEARCH_SYSTEM;
        case HISTORY_SYSTEM:
            return HISTORY_SYSTEM;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating SystemRestfulInteraction objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static SystemRestfulInteraction of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating SystemRestfulInteraction objects from a passed string value.
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
     * Inherited factory method for creating SystemRestfulInteraction objects from a passed string value.
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
        SystemRestfulInteraction other = (SystemRestfulInteraction) obj;
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
         *     An enum constant for SystemRestfulInteraction
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public SystemRestfulInteraction build() {
            SystemRestfulInteraction systemRestfulInteraction = new SystemRestfulInteraction(this);
            if (validating) {
                validate(systemRestfulInteraction);
            }
            return systemRestfulInteraction;
        }

        protected void validate(SystemRestfulInteraction systemRestfulInteraction) {
            super.validate(systemRestfulInteraction);
        }

        protected Builder from(SystemRestfulInteraction systemRestfulInteraction) {
            super.from(systemRestfulInteraction);
            return this;
        }
    }

    public enum Value {
        TRANSACTION("transaction"),

        BATCH("batch"),

        SEARCH_SYSTEM("search-system"),

        HISTORY_SYSTEM("history-system");

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
         * Factory method for creating SystemRestfulInteraction.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding SystemRestfulInteraction.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "transaction":
                return TRANSACTION;
            case "batch":
                return BATCH;
            case "search-system":
                return SEARCH_SYSTEM;
            case "history-system":
                return HISTORY_SYSTEM;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
