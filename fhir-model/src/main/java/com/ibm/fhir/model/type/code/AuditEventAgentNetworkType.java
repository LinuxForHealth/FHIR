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

@System("http://hl7.org/fhir/network-type")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class AuditEventAgentNetworkType extends Code {
    /**
     * Machine Name
     * 
     * <p>The machine name, including DNS name.
     */
    public static final AuditEventAgentNetworkType TYPE_1 = AuditEventAgentNetworkType.builder().value(ValueSet.TYPE_1).build();

    /**
     * IP Address
     * 
     * <p>The assigned Internet Protocol (IP) address.
     */
    public static final AuditEventAgentNetworkType TYPE_2 = AuditEventAgentNetworkType.builder().value(ValueSet.TYPE_2).build();

    /**
     * Telephone Number
     * 
     * <p>The assigned telephone number.
     */
    public static final AuditEventAgentNetworkType TYPE_3 = AuditEventAgentNetworkType.builder().value(ValueSet.TYPE_3).build();

    /**
     * Email address
     * 
     * <p>The assigned email address.
     */
    public static final AuditEventAgentNetworkType TYPE_4 = AuditEventAgentNetworkType.builder().value(ValueSet.TYPE_4).build();

    /**
     * URI
     * 
     * <p>URI (User directory, HTTP-PUT, ftp, etc.).
     */
    public static final AuditEventAgentNetworkType TYPE_5 = AuditEventAgentNetworkType.builder().value(ValueSet.TYPE_5).build();

    private volatile int hashCode;

    private AuditEventAgentNetworkType(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    /**
     * Factory method for creating AuditEventAgentNetworkType objects from a passed enum value.
     */
    public static AuditEventAgentNetworkType of(ValueSet value) {
        switch (value) {
        case TYPE_1:
            return TYPE_1;
        case TYPE_2:
            return TYPE_2;
        case TYPE_3:
            return TYPE_3;
        case TYPE_4:
            return TYPE_4;
        case TYPE_5:
            return TYPE_5;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating AuditEventAgentNetworkType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static AuditEventAgentNetworkType of(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    /**
     * Inherited factory method for creating AuditEventAgentNetworkType objects from a passed string value.
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
     * Inherited factory method for creating AuditEventAgentNetworkType objects from a passed string value.
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
        AuditEventAgentNetworkType other = (AuditEventAgentNetworkType) obj;
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
        public AuditEventAgentNetworkType build() {
            return new AuditEventAgentNetworkType(this);
        }
    }

    public enum ValueSet {
        /**
         * Machine Name
         * 
         * <p>The machine name, including DNS name.
         */
        TYPE_1("1"),

        /**
         * IP Address
         * 
         * <p>The assigned Internet Protocol (IP) address.
         */
        TYPE_2("2"),

        /**
         * Telephone Number
         * 
         * <p>The assigned telephone number.
         */
        TYPE_3("3"),

        /**
         * Email address
         * 
         * <p>The assigned email address.
         */
        TYPE_4("4"),

        /**
         * URI
         * 
         * <p>URI (User directory, HTTP-PUT, ftp, etc.).
         */
        TYPE_5("5");

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
         * Factory method for creating AuditEventAgentNetworkType.ValueSet values from a passed string value.
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
