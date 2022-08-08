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
public class TypeRestfulInteraction extends Code {
    public static final TypeRestfulInteraction READ = TypeRestfulInteraction.builder().value(Value.READ).build();

    public static final TypeRestfulInteraction VREAD = TypeRestfulInteraction.builder().value(Value.VREAD).build();

    public static final TypeRestfulInteraction UPDATE = TypeRestfulInteraction.builder().value(Value.UPDATE).build();

    public static final TypeRestfulInteraction PATCH = TypeRestfulInteraction.builder().value(Value.PATCH).build();

    public static final TypeRestfulInteraction DELETE = TypeRestfulInteraction.builder().value(Value.DELETE).build();

    public static final TypeRestfulInteraction HISTORY_INSTANCE = TypeRestfulInteraction.builder().value(Value.HISTORY_INSTANCE).build();

    public static final TypeRestfulInteraction HISTORY_TYPE = TypeRestfulInteraction.builder().value(Value.HISTORY_TYPE).build();

    public static final TypeRestfulInteraction CREATE = TypeRestfulInteraction.builder().value(Value.CREATE).build();

    public static final TypeRestfulInteraction SEARCH_TYPE = TypeRestfulInteraction.builder().value(Value.SEARCH_TYPE).build();

    private volatile int hashCode;

    private TypeRestfulInteraction(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this TypeRestfulInteraction as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating TypeRestfulInteraction objects from a passed enum value.
     */
    public static TypeRestfulInteraction of(Value value) {
        switch (value) {
        case READ:
            return READ;
        case VREAD:
            return VREAD;
        case UPDATE:
            return UPDATE;
        case PATCH:
            return PATCH;
        case DELETE:
            return DELETE;
        case HISTORY_INSTANCE:
            return HISTORY_INSTANCE;
        case HISTORY_TYPE:
            return HISTORY_TYPE;
        case CREATE:
            return CREATE;
        case SEARCH_TYPE:
            return SEARCH_TYPE;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating TypeRestfulInteraction objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static TypeRestfulInteraction of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating TypeRestfulInteraction objects from a passed string value.
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
     * Inherited factory method for creating TypeRestfulInteraction objects from a passed string value.
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
        TypeRestfulInteraction other = (TypeRestfulInteraction) obj;
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
         *     An enum constant for TypeRestfulInteraction
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public TypeRestfulInteraction build() {
            TypeRestfulInteraction typeRestfulInteraction = new TypeRestfulInteraction(this);
            if (validating) {
                validate(typeRestfulInteraction);
            }
            return typeRestfulInteraction;
        }

        protected void validate(TypeRestfulInteraction typeRestfulInteraction) {
            super.validate(typeRestfulInteraction);
        }

        protected Builder from(TypeRestfulInteraction typeRestfulInteraction) {
            super.from(typeRestfulInteraction);
            return this;
        }
    }

    public enum Value {
        READ("read"),

        VREAD("vread"),

        UPDATE("update"),

        PATCH("patch"),

        DELETE("delete"),

        HISTORY_INSTANCE("history-instance"),

        HISTORY_TYPE("history-type"),

        CREATE("create"),

        SEARCH_TYPE("search-type");

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
         * Factory method for creating TypeRestfulInteraction.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding TypeRestfulInteraction.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "read":
                return READ;
            case "vread":
                return VREAD;
            case "update":
                return UPDATE;
            case "patch":
                return PATCH;
            case "delete":
                return DELETE;
            case "history-instance":
                return HISTORY_INSTANCE;
            case "history-type":
                return HISTORY_TYPE;
            case "create":
                return CREATE;
            case "search-type":
                return SEARCH_TYPE;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
