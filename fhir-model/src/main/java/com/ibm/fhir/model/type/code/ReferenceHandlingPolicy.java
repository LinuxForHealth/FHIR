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

@System("http://hl7.org/fhir/reference-handling-policy")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ReferenceHandlingPolicy extends Code {
    /**
     * Literal References
     * 
     * <p>The server supports and populates Literal references (i.e. using Reference.reference) where they are known (this 
     * code does not guarantee that all references are literal; see 'enforced').
     */
    public static final ReferenceHandlingPolicy LITERAL = ReferenceHandlingPolicy.builder().value(Value.LITERAL).build();

    /**
     * Logical References
     * 
     * <p>The server allows logical references (i.e. using Reference.identifier).
     */
    public static final ReferenceHandlingPolicy LOGICAL = ReferenceHandlingPolicy.builder().value(Value.LOGICAL).build();

    /**
     * Resolves References
     * 
     * <p>The server will attempt to resolve logical references to literal references - i.e. converting Reference.identifier 
     * to Reference.reference (if resolution fails, the server may still accept resources; see logical).
     */
    public static final ReferenceHandlingPolicy RESOLVES = ReferenceHandlingPolicy.builder().value(Value.RESOLVES).build();

    /**
     * Reference Integrity Enforced
     * 
     * <p>The server enforces that references have integrity - e.g. it ensures that references can always be resolved. This 
     * is typically the case for clinical record systems, but often not the case for middleware/proxy systems.
     */
    public static final ReferenceHandlingPolicy ENFORCED = ReferenceHandlingPolicy.builder().value(Value.ENFORCED).build();

    /**
     * Local References Only
     * 
     * <p>The server does not support references that point to other servers.
     */
    public static final ReferenceHandlingPolicy LOCAL = ReferenceHandlingPolicy.builder().value(Value.LOCAL).build();

    private volatile int hashCode;

    private ReferenceHandlingPolicy(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this ReferenceHandlingPolicy as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating ReferenceHandlingPolicy objects from a passed enum value.
     */
    public static ReferenceHandlingPolicy of(Value value) {
        switch (value) {
        case LITERAL:
            return LITERAL;
        case LOGICAL:
            return LOGICAL;
        case RESOLVES:
            return RESOLVES;
        case ENFORCED:
            return ENFORCED;
        case LOCAL:
            return LOCAL;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating ReferenceHandlingPolicy objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static ReferenceHandlingPolicy of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating ReferenceHandlingPolicy objects from a passed string value.
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
     * Inherited factory method for creating ReferenceHandlingPolicy objects from a passed string value.
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
        ReferenceHandlingPolicy other = (ReferenceHandlingPolicy) obj;
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
         *     An enum constant for ReferenceHandlingPolicy
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public ReferenceHandlingPolicy build() {
            ReferenceHandlingPolicy referenceHandlingPolicy = new ReferenceHandlingPolicy(this);
            if (validating) {
                validate(referenceHandlingPolicy);
            }
            return referenceHandlingPolicy;
        }

        protected void validate(ReferenceHandlingPolicy referenceHandlingPolicy) {
            super.validate(referenceHandlingPolicy);
        }

        protected Builder from(ReferenceHandlingPolicy referenceHandlingPolicy) {
            super.from(referenceHandlingPolicy);
            return this;
        }
    }

    public enum Value {
        /**
         * Literal References
         * 
         * <p>The server supports and populates Literal references (i.e. using Reference.reference) where they are known (this 
         * code does not guarantee that all references are literal; see 'enforced').
         */
        LITERAL("literal"),

        /**
         * Logical References
         * 
         * <p>The server allows logical references (i.e. using Reference.identifier).
         */
        LOGICAL("logical"),

        /**
         * Resolves References
         * 
         * <p>The server will attempt to resolve logical references to literal references - i.e. converting Reference.identifier 
         * to Reference.reference (if resolution fails, the server may still accept resources; see logical).
         */
        RESOLVES("resolves"),

        /**
         * Reference Integrity Enforced
         * 
         * <p>The server enforces that references have integrity - e.g. it ensures that references can always be resolved. This 
         * is typically the case for clinical record systems, but often not the case for middleware/proxy systems.
         */
        ENFORCED("enforced"),

        /**
         * Local References Only
         * 
         * <p>The server does not support references that point to other servers.
         */
        LOCAL("local");

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
         * Factory method for creating ReferenceHandlingPolicy.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding ReferenceHandlingPolicy.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "literal":
                return LITERAL;
            case "logical":
                return LOGICAL;
            case "resolves":
                return RESOLVES;
            case "enforced":
                return ENFORCED;
            case "local":
                return LOCAL;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
