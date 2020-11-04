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

@System("http://hl7.org/fhir/contact-point-system")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ContactPointSystem extends Code {
    /**
     * Phone
     * 
     * <p>The value is a telephone number used for voice calls. Use of full international numbers starting with + is 
     * recommended to enable automatic dialing support but not required.
     */
    public static final ContactPointSystem PHONE = ContactPointSystem.builder().value(ValueSet.PHONE).build();

    /**
     * Fax
     * 
     * <p>The value is a fax machine. Use of full international numbers starting with + is recommended to enable automatic 
     * dialing support but not required.
     */
    public static final ContactPointSystem FAX = ContactPointSystem.builder().value(ValueSet.FAX).build();

    /**
     * Email
     * 
     * <p>The value is an email address.
     */
    public static final ContactPointSystem EMAIL = ContactPointSystem.builder().value(ValueSet.EMAIL).build();

    /**
     * Pager
     * 
     * <p>The value is a pager number. These may be local pager numbers that are only usable on a particular pager system.
     */
    public static final ContactPointSystem PAGER = ContactPointSystem.builder().value(ValueSet.PAGER).build();

    /**
     * URL
     * 
     * <p>A contact that is not a phone, fax, pager or email address and is expressed as a URL. This is intended for various 
     * institutional or personal contacts including web sites, blogs, Skype, Twitter, Facebook, etc. Do not use for email 
     * addresses.
     */
    public static final ContactPointSystem URL = ContactPointSystem.builder().value(ValueSet.URL).build();

    /**
     * SMS
     * 
     * <p>A contact that can be used for sending an sms message (e.g. mobile phones, some landlines).
     */
    public static final ContactPointSystem SMS = ContactPointSystem.builder().value(ValueSet.SMS).build();

    /**
     * Other
     * 
     * <p>A contact that is not a phone, fax, page or email address and is not expressible as a URL. E.g. Internal mail 
     * address. This SHOULD NOT be used for contacts that are expressible as a URL (e.g. Skype, Twitter, Facebook, etc.) 
     * Extensions may be used to distinguish "other" contact types.
     */
    public static final ContactPointSystem OTHER = ContactPointSystem.builder().value(ValueSet.OTHER).build();

    private volatile int hashCode;

    private ContactPointSystem(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    /**
     * Factory method for creating ContactPointSystem objects from a passed enum value.
     */
    public static ContactPointSystem of(ValueSet value) {
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
        return of(ValueSet.from(value));
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
        return of(ValueSet.from(value));
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
        public ContactPointSystem build() {
            return new ContactPointSystem(this);
        }
    }

    public enum ValueSet {
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
         * Factory method for creating ContactPointSystem.ValueSet values from a passed string value.
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
