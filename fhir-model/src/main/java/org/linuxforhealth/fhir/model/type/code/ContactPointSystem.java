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

@System("http://hl7.org/fhir/contact-point-system")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class ContactPointSystem extends Code {
    /**
     * Phone
     * 
     * <p>The value is a telephone number used for voice calls. Use of full international numbers starting with + is 
     * recommended to enable automatic dialing support but not required.
     */
    public static final ContactPointSystem PHONE = ContactPointSystem.builder().value(Value.PHONE).build();

    /**
     * Fax
     * 
     * <p>The value is a fax machine. Use of full international numbers starting with + is recommended to enable automatic 
     * dialing support but not required.
     */
    public static final ContactPointSystem FAX = ContactPointSystem.builder().value(Value.FAX).build();

    /**
     * Email
     * 
     * <p>The value is an email address.
     */
    public static final ContactPointSystem EMAIL = ContactPointSystem.builder().value(Value.EMAIL).build();

    /**
     * Pager
     * 
     * <p>The value is a pager number. These may be local pager numbers that are only usable on a particular pager system.
     */
    public static final ContactPointSystem PAGER = ContactPointSystem.builder().value(Value.PAGER).build();

    /**
     * URL
     * 
     * <p>A contact that is not a phone, fax, pager or email address and is expressed as a URL. This is intended for various 
     * institutional or personal contacts including web sites, blogs, Skype, Twitter, Facebook, etc. Do not use for email 
     * addresses.
     */
    public static final ContactPointSystem URL = ContactPointSystem.builder().value(Value.URL).build();

    /**
     * SMS
     * 
     * <p>A contact that can be used for sending an sms message (e.g. mobile phones, some landlines).
     */
    public static final ContactPointSystem SMS = ContactPointSystem.builder().value(Value.SMS).build();

    /**
     * Other
     * 
     * <p>A contact that is not a phone, fax, page or email address and is not expressible as a URL. E.g. Internal mail 
     * address. This SHOULD NOT be used for contacts that are expressible as a URL (e.g. Skype, Twitter, Facebook, etc.) 
     * Extensions may be used to distinguish "other" contact types.
     */
    public static final ContactPointSystem OTHER = ContactPointSystem.builder().value(Value.OTHER).build();

    private volatile int hashCode;

    private ContactPointSystem(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this ContactPointSystem as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating ContactPointSystem objects from a passed enum value.
     */
    public static ContactPointSystem of(Value value) {
        switch (value) {
        case PHONE:
            return PHONE;
        case FAX:
            return FAX;
        case EMAIL:
            return EMAIL;
        case PAGER:
            return PAGER;
        case URL:
            return URL;
        case SMS:
            return SMS;
        case OTHER:
            return OTHER;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating ContactPointSystem objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static ContactPointSystem of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating ContactPointSystem objects from a passed string value.
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
     * Inherited factory method for creating ContactPointSystem objects from a passed string value.
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
        ContactPointSystem other = (ContactPointSystem) obj;
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
         *     An enum constant for ContactPointSystem
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public ContactPointSystem build() {
            ContactPointSystem contactPointSystem = new ContactPointSystem(this);
            if (validating) {
                validate(contactPointSystem);
            }
            return contactPointSystem;
        }

        protected void validate(ContactPointSystem contactPointSystem) {
            super.validate(contactPointSystem);
        }

        protected Builder from(ContactPointSystem contactPointSystem) {
            super.from(contactPointSystem);
            return this;
        }
    }

    public enum Value {
        /**
         * Phone
         * 
         * <p>The value is a telephone number used for voice calls. Use of full international numbers starting with + is 
         * recommended to enable automatic dialing support but not required.
         */
        PHONE("phone"),

        /**
         * Fax
         * 
         * <p>The value is a fax machine. Use of full international numbers starting with + is recommended to enable automatic 
         * dialing support but not required.
         */
        FAX("fax"),

        /**
         * Email
         * 
         * <p>The value is an email address.
         */
        EMAIL("email"),

        /**
         * Pager
         * 
         * <p>The value is a pager number. These may be local pager numbers that are only usable on a particular pager system.
         */
        PAGER("pager"),

        /**
         * URL
         * 
         * <p>A contact that is not a phone, fax, pager or email address and is expressed as a URL. This is intended for various 
         * institutional or personal contacts including web sites, blogs, Skype, Twitter, Facebook, etc. Do not use for email 
         * addresses.
         */
        URL("url"),

        /**
         * SMS
         * 
         * <p>A contact that can be used for sending an sms message (e.g. mobile phones, some landlines).
         */
        SMS("sms"),

        /**
         * Other
         * 
         * <p>A contact that is not a phone, fax, page or email address and is not expressible as a URL. E.g. Internal mail 
         * address. This SHOULD NOT be used for contacts that are expressible as a URL (e.g. Skype, Twitter, Facebook, etc.) 
         * Extensions may be used to distinguish "other" contact types.
         */
        OTHER("other");

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
         * Factory method for creating ContactPointSystem.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding ContactPointSystem.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "phone":
                return PHONE;
            case "fax":
                return FAX;
            case "email":
                return EMAIL;
            case "pager":
                return PAGER;
            case "url":
                return URL;
            case "sms":
                return SMS;
            case "other":
                return OTHER;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
