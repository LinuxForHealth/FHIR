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

@System("http://hl7.org/fhir/restful-interaction")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class TypeRestfulInteraction extends Code {
    public static final TypeRestfulInteraction READ = TypeRestfulInteraction.builder().value(ValueSet.READ).build();

    public static final TypeRestfulInteraction VREAD = TypeRestfulInteraction.builder().value(ValueSet.VREAD).build();

    public static final TypeRestfulInteraction UPDATE = TypeRestfulInteraction.builder().value(ValueSet.UPDATE).build();

    public static final TypeRestfulInteraction PATCH = TypeRestfulInteraction.builder().value(ValueSet.PATCH).build();

    public static final TypeRestfulInteraction DELETE = TypeRestfulInteraction.builder().value(ValueSet.DELETE).build();

    public static final TypeRestfulInteraction HISTORY_INSTANCE = TypeRestfulInteraction.builder().value(ValueSet.HISTORY_INSTANCE).build();

    public static final TypeRestfulInteraction HISTORY_TYPE = TypeRestfulInteraction.builder().value(ValueSet.HISTORY_TYPE).build();

    public static final TypeRestfulInteraction CREATE = TypeRestfulInteraction.builder().value(ValueSet.CREATE).build();

    public static final TypeRestfulInteraction SEARCH_TYPE = TypeRestfulInteraction.builder().value(ValueSet.SEARCH_TYPE).build();

    private volatile int hashCode;

    private TypeRestfulInteraction(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    /**
     * Factory method for creating TypeRestfulInteraction objects from a passed enum value.
     */
    public static TypeRestfulInteraction of(ValueSet value) {
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
        return of(ValueSet.from(value));
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
        return of(ValueSet.from(value));
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
        public TypeRestfulInteraction build() {
            return new TypeRestfulInteraction(this);
        }
    }

    public enum ValueSet {
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

        ValueSet(java.lang.String value) {
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
         * Factory method for creating TypeRestfulInteraction.ValueSet values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @throws IllegalArgumentException
         *     If the passed string cannot be parsed into an allowed code value
         */
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
