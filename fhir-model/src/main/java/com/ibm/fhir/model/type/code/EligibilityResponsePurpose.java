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

@System("http://hl7.org/fhir/eligibilityresponse-purpose")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class EligibilityResponsePurpose extends Code {
    /**
     * Coverage auth-requirements
     * 
     * <p>The prior authorization requirements for the listed, or discovered if specified, converages for the categories of 
     * service and/or specifed biling codes are requested.
     */
    public static final EligibilityResponsePurpose AUTH_REQUIREMENTS = EligibilityResponsePurpose.builder().value(Value.AUTH_REQUIREMENTS).build();

    /**
     * Coverage benefits
     * 
     * <p>The plan benefits and optionally benefits consumed for the listed, or discovered if specified, converages are 
     * requested.
     */
    public static final EligibilityResponsePurpose BENEFITS = EligibilityResponsePurpose.builder().value(Value.BENEFITS).build();

    /**
     * Coverage Discovery
     * 
     * <p>The insurer is requested to report on any coverages which they are aware of in addition to any specifed.
     */
    public static final EligibilityResponsePurpose DISCOVERY = EligibilityResponsePurpose.builder().value(Value.DISCOVERY).build();

    /**
     * Coverage Validation
     * 
     * <p>A check that the specified coverages are in-force is requested.
     */
    public static final EligibilityResponsePurpose VALIDATION = EligibilityResponsePurpose.builder().value(Value.VALIDATION).build();

    private volatile int hashCode;

    private EligibilityResponsePurpose(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this EligibilityResponsePurpose as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating EligibilityResponsePurpose objects from a passed enum value.
     */
    public static EligibilityResponsePurpose of(Value value) {
        switch (value) {
        case AUTH_REQUIREMENTS:
            return AUTH_REQUIREMENTS;
        case BENEFITS:
            return BENEFITS;
        case DISCOVERY:
            return DISCOVERY;
        case VALIDATION:
            return VALIDATION;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating EligibilityResponsePurpose objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static EligibilityResponsePurpose of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating EligibilityResponsePurpose objects from a passed string value.
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
     * Inherited factory method for creating EligibilityResponsePurpose objects from a passed string value.
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
        EligibilityResponsePurpose other = (EligibilityResponsePurpose) obj;
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
         *     An enum constant for EligibilityResponsePurpose
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public EligibilityResponsePurpose build() {
            EligibilityResponsePurpose eligibilityResponsePurpose = new EligibilityResponsePurpose(this);
            if (validating) {
                validate(eligibilityResponsePurpose);
            }
            return eligibilityResponsePurpose;
        }

        protected void validate(EligibilityResponsePurpose eligibilityResponsePurpose) {
            super.validate(eligibilityResponsePurpose);
        }

        protected Builder from(EligibilityResponsePurpose eligibilityResponsePurpose) {
            super.from(eligibilityResponsePurpose);
            return this;
        }
    }

    public enum Value {
        /**
         * Coverage auth-requirements
         * 
         * <p>The prior authorization requirements for the listed, or discovered if specified, converages for the categories of 
         * service and/or specifed biling codes are requested.
         */
        AUTH_REQUIREMENTS("auth-requirements"),

        /**
         * Coverage benefits
         * 
         * <p>The plan benefits and optionally benefits consumed for the listed, or discovered if specified, converages are 
         * requested.
         */
        BENEFITS("benefits"),

        /**
         * Coverage Discovery
         * 
         * <p>The insurer is requested to report on any coverages which they are aware of in addition to any specifed.
         */
        DISCOVERY("discovery"),

        /**
         * Coverage Validation
         * 
         * <p>A check that the specified coverages are in-force is requested.
         */
        VALIDATION("validation");

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
         * Factory method for creating EligibilityResponsePurpose.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding EligibilityResponsePurpose.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "auth-requirements":
                return AUTH_REQUIREMENTS;
            case "benefits":
                return BENEFITS;
            case "discovery":
                return DISCOVERY;
            case "validation":
                return VALIDATION;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
