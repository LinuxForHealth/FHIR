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

@System("http://hl7.org/fhir/identity-assuranceLevel")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class IdentityAssuranceLevel extends Code {
    /**
     * Level 1
     * 
     * <p>Little or no confidence in the asserted identity's accuracy.
     */
    public static final IdentityAssuranceLevel LEVEL1 = IdentityAssuranceLevel.builder().value(Value.LEVEL1).build();

    /**
     * Level 2
     * 
     * <p>Some confidence in the asserted identity's accuracy.
     */
    public static final IdentityAssuranceLevel LEVEL2 = IdentityAssuranceLevel.builder().value(Value.LEVEL2).build();

    /**
     * Level 3
     * 
     * <p>High confidence in the asserted identity's accuracy.
     */
    public static final IdentityAssuranceLevel LEVEL3 = IdentityAssuranceLevel.builder().value(Value.LEVEL3).build();

    /**
     * Level 4
     * 
     * <p>Very high confidence in the asserted identity's accuracy.
     */
    public static final IdentityAssuranceLevel LEVEL4 = IdentityAssuranceLevel.builder().value(Value.LEVEL4).build();

    private volatile int hashCode;

    private IdentityAssuranceLevel(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this IdentityAssuranceLevel as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating IdentityAssuranceLevel objects from a passed enum value.
     */
    public static IdentityAssuranceLevel of(Value value) {
        switch (value) {
        case LEVEL1:
            return LEVEL1;
        case LEVEL2:
            return LEVEL2;
        case LEVEL3:
            return LEVEL3;
        case LEVEL4:
            return LEVEL4;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating IdentityAssuranceLevel objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static IdentityAssuranceLevel of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating IdentityAssuranceLevel objects from a passed string value.
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
     * Inherited factory method for creating IdentityAssuranceLevel objects from a passed string value.
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
        IdentityAssuranceLevel other = (IdentityAssuranceLevel) obj;
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
         *     An enum constant for IdentityAssuranceLevel
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public IdentityAssuranceLevel build() {
            IdentityAssuranceLevel identityAssuranceLevel = new IdentityAssuranceLevel(this);
            if (validating) {
                validate(identityAssuranceLevel);
            }
            return identityAssuranceLevel;
        }

        protected void validate(IdentityAssuranceLevel identityAssuranceLevel) {
            super.validate(identityAssuranceLevel);
        }

        protected Builder from(IdentityAssuranceLevel identityAssuranceLevel) {
            super.from(identityAssuranceLevel);
            return this;
        }
    }

    public enum Value {
        /**
         * Level 1
         * 
         * <p>Little or no confidence in the asserted identity's accuracy.
         */
        LEVEL1("level1"),

        /**
         * Level 2
         * 
         * <p>Some confidence in the asserted identity's accuracy.
         */
        LEVEL2("level2"),

        /**
         * Level 3
         * 
         * <p>High confidence in the asserted identity's accuracy.
         */
        LEVEL3("level3"),

        /**
         * Level 4
         * 
         * <p>Very high confidence in the asserted identity's accuracy.
         */
        LEVEL4("level4");

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
         * Factory method for creating IdentityAssuranceLevel.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding IdentityAssuranceLevel.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "level1":
                return LEVEL1;
            case "level2":
                return LEVEL2;
            case "level3":
                return LEVEL3;
            case "level4":
                return LEVEL4;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
