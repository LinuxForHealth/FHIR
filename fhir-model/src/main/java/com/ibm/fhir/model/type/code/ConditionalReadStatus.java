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

@System("http://hl7.org/fhir/conditional-read-status")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ConditionalReadStatus extends Code {
    /**
     * Not Supported
     * 
     * <p>No support for conditional reads.
     */
    public static final ConditionalReadStatus NOT_SUPPORTED = ConditionalReadStatus.builder().value(ValueSet.NOT_SUPPORTED).build();

    /**
     * If-Modified-Since
     * 
     * <p>Conditional reads are supported, but only with the If-Modified-Since HTTP Header.
     */
    public static final ConditionalReadStatus MODIFIED_SINCE = ConditionalReadStatus.builder().value(ValueSet.MODIFIED_SINCE).build();

    /**
     * If-None-Match
     * 
     * <p>Conditional reads are supported, but only with the If-None-Match HTTP Header.
     */
    public static final ConditionalReadStatus NOT_MATCH = ConditionalReadStatus.builder().value(ValueSet.NOT_MATCH).build();

    /**
     * Full Support
     * 
     * <p>Conditional reads are supported, with both If-Modified-Since and If-None-Match HTTP Headers.
     */
    public static final ConditionalReadStatus FULL_SUPPORT = ConditionalReadStatus.builder().value(ValueSet.FULL_SUPPORT).build();

    private volatile int hashCode;

    private ConditionalReadStatus(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    /**
     * Factory method for creating ConditionalReadStatus objects from a passed enum value.
     */
    public static ConditionalReadStatus of(ValueSet value) {
        switch (value) {
        case NOT_SUPPORTED:
            return NOT_SUPPORTED;
        case MODIFIED_SINCE:
            return MODIFIED_SINCE;
        case NOT_MATCH:
            return NOT_MATCH;
        case FULL_SUPPORT:
            return FULL_SUPPORT;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating ConditionalReadStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static ConditionalReadStatus of(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    /**
     * Inherited factory method for creating ConditionalReadStatus objects from a passed string value.
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
     * Inherited factory method for creating ConditionalReadStatus objects from a passed string value.
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
        ConditionalReadStatus other = (ConditionalReadStatus) obj;
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
        public ConditionalReadStatus build() {
            return new ConditionalReadStatus(this);
        }
    }

    public enum ValueSet {
        /**
         * Not Supported
         * 
         * <p>No support for conditional reads.
         */
        NOT_SUPPORTED("not-supported"),

        /**
         * If-Modified-Since
         * 
         * <p>Conditional reads are supported, but only with the If-Modified-Since HTTP Header.
         */
        MODIFIED_SINCE("modified-since"),

        /**
         * If-None-Match
         * 
         * <p>Conditional reads are supported, but only with the If-None-Match HTTP Header.
         */
        NOT_MATCH("not-match"),

        /**
         * Full Support
         * 
         * <p>Conditional reads are supported, with both If-Modified-Since and If-None-Match HTTP Headers.
         */
        FULL_SUPPORT("full-support");

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
         * Factory method for creating ConditionalReadStatus.ValueSet values from a passed string value.
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
