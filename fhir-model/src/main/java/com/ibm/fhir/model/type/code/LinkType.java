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

@System("http://hl7.org/fhir/link-type")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class LinkType extends Code {
    /**
     * Replaced-by
     * 
     * <p>The patient resource containing this link must no longer be used. The link points forward to another patient 
     * resource that must be used in lieu of the patient resource that contains this link.
     */
    public static final LinkType REPLACED_BY = LinkType.builder().value(Value.REPLACED_BY).build();

    /**
     * Replaces
     * 
     * <p>The patient resource containing this link is the current active patient record. The link points back to an inactive 
     * patient resource that has been merged into this resource, and should be consulted to retrieve additional referenced 
     * information.
     */
    public static final LinkType REPLACES = LinkType.builder().value(Value.REPLACES).build();

    /**
     * Refer
     * 
     * <p>The patient resource containing this link is in use and valid but not considered the main source of information 
     * about a patient. The link points forward to another patient resource that should be consulted to retrieve additional 
     * patient information.
     */
    public static final LinkType REFER = LinkType.builder().value(Value.REFER).build();

    /**
     * See also
     * 
     * <p>The patient resource containing this link is in use and valid, but points to another patient resource that is known 
     * to contain data about the same person. Data in this resource might overlap or contradict information found in the 
     * other patient resource. This link does not indicate any relative importance of the resources concerned, and both 
     * should be regarded as equally valid.
     */
    public static final LinkType SEEALSO = LinkType.builder().value(Value.SEEALSO).build();

    private volatile int hashCode;

    private LinkType(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this LinkType as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating LinkType objects from a passed enum value.
     */
    public static LinkType of(Value value) {
        switch (value) {
        case REPLACED_BY:
            return REPLACED_BY;
        case REPLACES:
            return REPLACES;
        case REFER:
            return REFER;
        case SEEALSO:
            return SEEALSO;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating LinkType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static LinkType of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating LinkType objects from a passed string value.
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
     * Inherited factory method for creating LinkType objects from a passed string value.
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
        LinkType other = (LinkType) obj;
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
         *     An enum constant for LinkType
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public LinkType build() {
            LinkType linkType = new LinkType(this);
            if (validating) {
                validate(linkType);
            }
            return linkType;
        }

        protected void validate(LinkType linkType) {
            super.validate(linkType);
        }

        protected Builder from(LinkType linkType) {
            super.from(linkType);
            return this;
        }
    }

    public enum Value {
        /**
         * Replaced-by
         * 
         * <p>The patient resource containing this link must no longer be used. The link points forward to another patient 
         * resource that must be used in lieu of the patient resource that contains this link.
         */
        REPLACED_BY("replaced-by"),

        /**
         * Replaces
         * 
         * <p>The patient resource containing this link is the current active patient record. The link points back to an inactive 
         * patient resource that has been merged into this resource, and should be consulted to retrieve additional referenced 
         * information.
         */
        REPLACES("replaces"),

        /**
         * Refer
         * 
         * <p>The patient resource containing this link is in use and valid but not considered the main source of information 
         * about a patient. The link points forward to another patient resource that should be consulted to retrieve additional 
         * patient information.
         */
        REFER("refer"),

        /**
         * See also
         * 
         * <p>The patient resource containing this link is in use and valid, but points to another patient resource that is known 
         * to contain data about the same person. Data in this resource might overlap or contradict information found in the 
         * other patient resource. This link does not indicate any relative importance of the resources concerned, and both 
         * should be regarded as equally valid.
         */
        SEEALSO("seealso");

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
         * Factory method for creating LinkType.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding LinkType.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "replaced-by":
                return REPLACED_BY;
            case "replaces":
                return REPLACES;
            case "refer":
                return REFER;
            case "seealso":
                return SEEALSO;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
