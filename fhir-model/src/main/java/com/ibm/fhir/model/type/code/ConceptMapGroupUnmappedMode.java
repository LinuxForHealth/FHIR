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

@System("http://hl7.org/fhir/conceptmap-unmapped-mode")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ConceptMapGroupUnmappedMode extends Code {
    /**
     * Provided Code
     * 
     * <p>Use the code as provided in the $translate request.
     */
    public static final ConceptMapGroupUnmappedMode PROVIDED = ConceptMapGroupUnmappedMode.builder().value(Value.PROVIDED).build();

    /**
     * Fixed Code
     * 
     * <p>Use the code explicitly provided in the group.unmapped.
     */
    public static final ConceptMapGroupUnmappedMode FIXED = ConceptMapGroupUnmappedMode.builder().value(Value.FIXED).build();

    /**
     * Other Map
     * 
     * <p>Use the map identified by the canonical URL in the url element.
     */
    public static final ConceptMapGroupUnmappedMode OTHER_MAP = ConceptMapGroupUnmappedMode.builder().value(Value.OTHER_MAP).build();

    private volatile int hashCode;

    private ConceptMapGroupUnmappedMode(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this ConceptMapGroupUnmappedMode as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating ConceptMapGroupUnmappedMode objects from a passed enum value.
     */
    public static ConceptMapGroupUnmappedMode of(Value value) {
        switch (value) {
        case PROVIDED:
            return PROVIDED;
        case FIXED:
            return FIXED;
        case OTHER_MAP:
            return OTHER_MAP;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating ConceptMapGroupUnmappedMode objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static ConceptMapGroupUnmappedMode of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating ConceptMapGroupUnmappedMode objects from a passed string value.
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
     * Inherited factory method for creating ConceptMapGroupUnmappedMode objects from a passed string value.
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
        ConceptMapGroupUnmappedMode other = (ConceptMapGroupUnmappedMode) obj;
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
         *     An enum constant for ConceptMapGroupUnmappedMode
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public ConceptMapGroupUnmappedMode build() {
            ConceptMapGroupUnmappedMode conceptMapGroupUnmappedMode = new ConceptMapGroupUnmappedMode(this);
            if (validating) {
                validate(conceptMapGroupUnmappedMode);
            }
            return conceptMapGroupUnmappedMode;
        }

        protected void validate(ConceptMapGroupUnmappedMode conceptMapGroupUnmappedMode) {
            super.validate(conceptMapGroupUnmappedMode);
        }

        protected Builder from(ConceptMapGroupUnmappedMode conceptMapGroupUnmappedMode) {
            super.from(conceptMapGroupUnmappedMode);
            return this;
        }
    }

    public enum Value {
        /**
         * Provided Code
         * 
         * <p>Use the code as provided in the $translate request.
         */
        PROVIDED("provided"),

        /**
         * Fixed Code
         * 
         * <p>Use the code explicitly provided in the group.unmapped.
         */
        FIXED("fixed"),

        /**
         * Other Map
         * 
         * <p>Use the map identified by the canonical URL in the url element.
         */
        OTHER_MAP("other-map");

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
         * Factory method for creating ConceptMapGroupUnmappedMode.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding ConceptMapGroupUnmappedMode.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "provided":
                return PROVIDED;
            case "fixed":
                return FIXED;
            case "other-map":
                return OTHER_MAP;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
