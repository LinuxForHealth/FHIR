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

@System("http://hl7.org/fhir/search-xpath-usage")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class XPathUsageType extends Code {
    /**
     * Normal
     * 
     * <p>The search parameter is derived directly from the selected nodes based on the type definitions.
     */
    public static final XPathUsageType NORMAL = XPathUsageType.builder().value(Value.NORMAL).build();

    /**
     * Phonetic
     * 
     * <p>The search parameter is derived by a phonetic transform from the selected nodes.
     */
    public static final XPathUsageType PHONETIC = XPathUsageType.builder().value(Value.PHONETIC).build();

    /**
     * Nearby
     * 
     * <p>The search parameter is based on a spatial transform of the selected nodes.
     */
    public static final XPathUsageType NEARBY = XPathUsageType.builder().value(Value.NEARBY).build();

    /**
     * Distance
     * 
     * <p>The search parameter is based on a spatial transform of the selected nodes, using physical distance from the middle.
     */
    public static final XPathUsageType DISTANCE = XPathUsageType.builder().value(Value.DISTANCE).build();

    /**
     * Other
     * 
     * <p>The interpretation of the xpath statement is unknown (and can't be automated).
     */
    public static final XPathUsageType OTHER = XPathUsageType.builder().value(Value.OTHER).build();

    private volatile int hashCode;

    private XPathUsageType(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this XPathUsageType as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating XPathUsageType objects from a passed enum value.
     */
    public static XPathUsageType of(Value value) {
        switch (value) {
        case NORMAL:
            return NORMAL;
        case PHONETIC:
            return PHONETIC;
        case NEARBY:
            return NEARBY;
        case DISTANCE:
            return DISTANCE;
        case OTHER:
            return OTHER;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating XPathUsageType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static XPathUsageType of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating XPathUsageType objects from a passed string value.
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
     * Inherited factory method for creating XPathUsageType objects from a passed string value.
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
        XPathUsageType other = (XPathUsageType) obj;
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
         *     An enum constant for XPathUsageType
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public XPathUsageType build() {
            XPathUsageType xPathUsageType = new XPathUsageType(this);
            if (validating) {
                validate(xPathUsageType);
            }
            return xPathUsageType;
        }

        protected void validate(XPathUsageType xPathUsageType) {
            super.validate(xPathUsageType);
        }

        protected Builder from(XPathUsageType xPathUsageType) {
            super.from(xPathUsageType);
            return this;
        }
    }

    public enum Value {
        /**
         * Normal
         * 
         * <p>The search parameter is derived directly from the selected nodes based on the type definitions.
         */
        NORMAL("normal"),

        /**
         * Phonetic
         * 
         * <p>The search parameter is derived by a phonetic transform from the selected nodes.
         */
        PHONETIC("phonetic"),

        /**
         * Nearby
         * 
         * <p>The search parameter is based on a spatial transform of the selected nodes.
         */
        NEARBY("nearby"),

        /**
         * Distance
         * 
         * <p>The search parameter is based on a spatial transform of the selected nodes, using physical distance from the middle.
         */
        DISTANCE("distance"),

        /**
         * Other
         * 
         * <p>The interpretation of the xpath statement is unknown (and can't be automated).
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
         * Factory method for creating XPathUsageType.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding XPathUsageType.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "normal":
                return NORMAL;
            case "phonetic":
                return PHONETIC;
            case "nearby":
                return NEARBY;
            case "distance":
                return DISTANCE;
            case "other":
                return OTHER;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
