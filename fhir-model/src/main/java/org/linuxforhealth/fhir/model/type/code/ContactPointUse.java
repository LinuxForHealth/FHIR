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

@System("http://hl7.org/fhir/contact-point-use")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class ContactPointUse extends Code {
    /**
     * Home
     * 
     * <p>A communication contact point at a home; attempted contacts for business purposes might intrude privacy and chances 
     * are one will contact family or other household members instead of the person one wishes to call. Typically used with 
     * urgent cases, or if no other contacts are available.
     */
    public static final ContactPointUse HOME = ContactPointUse.builder().value(Value.HOME).build();

    /**
     * Work
     * 
     * <p>An office contact point. First choice for business related contacts during business hours.
     */
    public static final ContactPointUse WORK = ContactPointUse.builder().value(Value.WORK).build();

    /**
     * Temp
     * 
     * <p>A temporary contact point. The period can provide more detailed information.
     */
    public static final ContactPointUse TEMP = ContactPointUse.builder().value(Value.TEMP).build();

    /**
     * Old
     * 
     * <p>This contact point is no longer in use (or was never correct, but retained for records).
     */
    public static final ContactPointUse OLD = ContactPointUse.builder().value(Value.OLD).build();

    /**
     * Mobile
     * 
     * <p>A telecommunication device that moves and stays with its owner. May have characteristics of all other use codes, 
     * suitable for urgent matters, not the first choice for routine business.
     */
    public static final ContactPointUse MOBILE = ContactPointUse.builder().value(Value.MOBILE).build();

    private volatile int hashCode;

    private ContactPointUse(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this ContactPointUse as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating ContactPointUse objects from a passed enum value.
     */
    public static ContactPointUse of(Value value) {
        switch (value) {
        case HOME:
            return HOME;
        case WORK:
            return WORK;
        case TEMP:
            return TEMP;
        case OLD:
            return OLD;
        case MOBILE:
            return MOBILE;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating ContactPointUse objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static ContactPointUse of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating ContactPointUse objects from a passed string value.
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
     * Inherited factory method for creating ContactPointUse objects from a passed string value.
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
        ContactPointUse other = (ContactPointUse) obj;
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
         *     An enum constant for ContactPointUse
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public ContactPointUse build() {
            ContactPointUse contactPointUse = new ContactPointUse(this);
            if (validating) {
                validate(contactPointUse);
            }
            return contactPointUse;
        }

        protected void validate(ContactPointUse contactPointUse) {
            super.validate(contactPointUse);
        }

        protected Builder from(ContactPointUse contactPointUse) {
            super.from(contactPointUse);
            return this;
        }
    }

    public enum Value {
        /**
         * Home
         * 
         * <p>A communication contact point at a home; attempted contacts for business purposes might intrude privacy and chances 
         * are one will contact family or other household members instead of the person one wishes to call. Typically used with 
         * urgent cases, or if no other contacts are available.
         */
        HOME("home"),

        /**
         * Work
         * 
         * <p>An office contact point. First choice for business related contacts during business hours.
         */
        WORK("work"),

        /**
         * Temp
         * 
         * <p>A temporary contact point. The period can provide more detailed information.
         */
        TEMP("temp"),

        /**
         * Old
         * 
         * <p>This contact point is no longer in use (or was never correct, but retained for records).
         */
        OLD("old"),

        /**
         * Mobile
         * 
         * <p>A telecommunication device that moves and stays with its owner. May have characteristics of all other use codes, 
         * suitable for urgent matters, not the first choice for routine business.
         */
        MOBILE("mobile");

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
         * Factory method for creating ContactPointUse.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding ContactPointUse.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "home":
                return HOME;
            case "work":
                return WORK;
            case "temp":
                return TEMP;
            case "old":
                return OLD;
            case "mobile":
                return MOBILE;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
