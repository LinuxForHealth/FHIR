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

@System("http://hl7.org/fhir/name-use")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class NameUse extends Code {
    /**
     * Usual
     * 
     * <p>Known as/conventional/the one you normally use.
     */
    public static final NameUse USUAL = NameUse.builder().value(Value.USUAL).build();

    /**
     * Official
     * 
     * <p>The formal name as registered in an official (government) registry, but which name might not be commonly used. May 
     * be called "legal name".
     */
    public static final NameUse OFFICIAL = NameUse.builder().value(Value.OFFICIAL).build();

    /**
     * Temp
     * 
     * <p>A temporary name. Name.period can provide more detailed information. This may also be used for temporary names 
     * assigned at birth or in emergency situations.
     */
    public static final NameUse TEMP = NameUse.builder().value(Value.TEMP).build();

    /**
     * Nickname
     * 
     * <p>A name that is used to address the person in an informal manner, but is not part of their formal or usual name.
     */
    public static final NameUse NICKNAME = NameUse.builder().value(Value.NICKNAME).build();

    /**
     * Anonymous
     * 
     * <p>Anonymous assigned name, alias, or pseudonym (used to protect a person's identity for privacy reasons).
     */
    public static final NameUse ANONYMOUS = NameUse.builder().value(Value.ANONYMOUS).build();

    /**
     * Old
     * 
     * <p>This name is no longer in use (or was never correct, but retained for records).
     */
    public static final NameUse OLD = NameUse.builder().value(Value.OLD).build();

    /**
     * Name changed for Marriage
     * 
     * <p>A name used prior to changing name because of marriage. This name use is for use by applications that collect and 
     * store names that were used prior to a marriage. Marriage naming customs vary greatly around the world, and are 
     * constantly changing. This term is not gender specific. The use of this term does not imply any particular history for 
     * a person's name.
     */
    public static final NameUse MAIDEN = NameUse.builder().value(Value.MAIDEN).build();

    private volatile int hashCode;

    private NameUse(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this NameUse as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating NameUse objects from a passed enum value.
     */
    public static NameUse of(Value value) {
        switch (value) {
        case USUAL:
            return USUAL;
        case OFFICIAL:
            return OFFICIAL;
        case TEMP:
            return TEMP;
        case NICKNAME:
            return NICKNAME;
        case ANONYMOUS:
            return ANONYMOUS;
        case OLD:
            return OLD;
        case MAIDEN:
            return MAIDEN;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating NameUse objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static NameUse of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating NameUse objects from a passed string value.
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
     * Inherited factory method for creating NameUse objects from a passed string value.
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
        NameUse other = (NameUse) obj;
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
         *     An enum constant for NameUse
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public NameUse build() {
            NameUse nameUse = new NameUse(this);
            if (validating) {
                validate(nameUse);
            }
            return nameUse;
        }

        protected void validate(NameUse nameUse) {
            super.validate(nameUse);
        }

        protected Builder from(NameUse nameUse) {
            super.from(nameUse);
            return this;
        }
    }

    public enum Value {
        /**
         * Usual
         * 
         * <p>Known as/conventional/the one you normally use.
         */
        USUAL("usual"),

        /**
         * Official
         * 
         * <p>The formal name as registered in an official (government) registry, but which name might not be commonly used. May 
         * be called "legal name".
         */
        OFFICIAL("official"),

        /**
         * Temp
         * 
         * <p>A temporary name. Name.period can provide more detailed information. This may also be used for temporary names 
         * assigned at birth or in emergency situations.
         */
        TEMP("temp"),

        /**
         * Nickname
         * 
         * <p>A name that is used to address the person in an informal manner, but is not part of their formal or usual name.
         */
        NICKNAME("nickname"),

        /**
         * Anonymous
         * 
         * <p>Anonymous assigned name, alias, or pseudonym (used to protect a person's identity for privacy reasons).
         */
        ANONYMOUS("anonymous"),

        /**
         * Old
         * 
         * <p>This name is no longer in use (or was never correct, but retained for records).
         */
        OLD("old"),

        /**
         * Name changed for Marriage
         * 
         * <p>A name used prior to changing name because of marriage. This name use is for use by applications that collect and 
         * store names that were used prior to a marriage. Marriage naming customs vary greatly around the world, and are 
         * constantly changing. This term is not gender specific. The use of this term does not imply any particular history for 
         * a person's name.
         */
        MAIDEN("maiden");

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
         * Factory method for creating NameUse.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding NameUse.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "usual":
                return USUAL;
            case "official":
                return OFFICIAL;
            case "temp":
                return TEMP;
            case "nickname":
                return NICKNAME;
            case "anonymous":
                return ANONYMOUS;
            case "old":
                return OLD;
            case "maiden":
                return MAIDEN;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
