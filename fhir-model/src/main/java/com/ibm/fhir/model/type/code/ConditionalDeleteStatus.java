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

@System("http://hl7.org/fhir/conditional-delete-status")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ConditionalDeleteStatus extends Code {
    /**
     * Not Supported
     * 
     * <p>No support for conditional deletes.
     */
    public static final ConditionalDeleteStatus NOT_SUPPORTED = ConditionalDeleteStatus.builder().value(ValueSet.NOT_SUPPORTED).build();

    /**
     * Single Deletes Supported
     * 
     * <p>Conditional deletes are supported, but only single resources at a time.
     */
    public static final ConditionalDeleteStatus SINGLE = ConditionalDeleteStatus.builder().value(ValueSet.SINGLE).build();

    /**
     * Multiple Deletes Supported
     * 
     * <p>Conditional deletes are supported, and multiple resources can be deleted in a single interaction.
     */
    public static final ConditionalDeleteStatus MULTIPLE = ConditionalDeleteStatus.builder().value(ValueSet.MULTIPLE).build();

    private volatile int hashCode;

    private ConditionalDeleteStatus(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    /**
     * Factory method for creating ConditionalDeleteStatus objects from a passed enum value.
     */
    public static ConditionalDeleteStatus of(ValueSet value) {
        switch (value) {
        case NOT_SUPPORTED:
            return NOT_SUPPORTED;
        case SINGLE:
            return SINGLE;
        case MULTIPLE:
            return MULTIPLE;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating ConditionalDeleteStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static ConditionalDeleteStatus of(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    /**
     * Inherited factory method for creating ConditionalDeleteStatus objects from a passed string value.
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
     * Inherited factory method for creating ConditionalDeleteStatus objects from a passed string value.
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
        ConditionalDeleteStatus other = (ConditionalDeleteStatus) obj;
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
        public ConditionalDeleteStatus build() {
            return new ConditionalDeleteStatus(this);
        }
    }

    public enum ValueSet {
        /**
         * Not Supported
         * 
         * <p>No support for conditional deletes.
         */
        NOT_SUPPORTED("not-supported"),

        /**
         * Single Deletes Supported
         * 
         * <p>Conditional deletes are supported, but only single resources at a time.
         */
        SINGLE("single"),

        /**
         * Multiple Deletes Supported
         * 
         * <p>Conditional deletes are supported, and multiple resources can be deleted in a single interaction.
         */
        MULTIPLE("multiple");

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
         * Factory method for creating ConditionalDeleteStatus.ValueSet values from a passed string value.
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
