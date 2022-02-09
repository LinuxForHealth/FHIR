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

@System("http://hl7.org/fhir/narrative-status")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class NarrativeStatus extends Code {
    /**
     * Generated
     * 
     * <p>The contents of the narrative are entirely generated from the core elements in the content.
     */
    public static final NarrativeStatus GENERATED = NarrativeStatus.builder().value(Value.GENERATED).build();

    /**
     * Extensions
     * 
     * <p>The contents of the narrative are entirely generated from the core elements in the content and some of the content 
     * is generated from extensions. The narrative SHALL reflect the impact of all modifier extensions.
     */
    public static final NarrativeStatus EXTENSIONS = NarrativeStatus.builder().value(Value.EXTENSIONS).build();

    /**
     * Additional
     * 
     * <p>The contents of the narrative may contain additional information not found in the structured data. Note that there 
     * is no computable way to determine what the extra information is, other than by human inspection.
     */
    public static final NarrativeStatus ADDITIONAL = NarrativeStatus.builder().value(Value.ADDITIONAL).build();

    /**
     * Empty
     * 
     * <p>The contents of the narrative are some equivalent of "No human-readable text provided in this case".
     */
    public static final NarrativeStatus EMPTY = NarrativeStatus.builder().value(Value.EMPTY).build();

    private volatile int hashCode;

    private NarrativeStatus(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this NarrativeStatus as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating NarrativeStatus objects from a passed enum value.
     */
    public static NarrativeStatus of(Value value) {
        switch (value) {
        case GENERATED:
            return GENERATED;
        case EXTENSIONS:
            return EXTENSIONS;
        case ADDITIONAL:
            return ADDITIONAL;
        case EMPTY:
            return EMPTY;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating NarrativeStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static NarrativeStatus of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating NarrativeStatus objects from a passed string value.
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
     * Inherited factory method for creating NarrativeStatus objects from a passed string value.
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
        NarrativeStatus other = (NarrativeStatus) obj;
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
         *     An enum constant for NarrativeStatus
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public NarrativeStatus build() {
            NarrativeStatus narrativeStatus = new NarrativeStatus(this);
            if (validating) {
                validate(narrativeStatus);
            }
            return narrativeStatus;
        }

        protected void validate(NarrativeStatus narrativeStatus) {
            super.validate(narrativeStatus);
        }

        protected Builder from(NarrativeStatus narrativeStatus) {
            super.from(narrativeStatus);
            return this;
        }
    }

    public enum Value {
        /**
         * Generated
         * 
         * <p>The contents of the narrative are entirely generated from the core elements in the content.
         */
        GENERATED("generated"),

        /**
         * Extensions
         * 
         * <p>The contents of the narrative are entirely generated from the core elements in the content and some of the content 
         * is generated from extensions. The narrative SHALL reflect the impact of all modifier extensions.
         */
        EXTENSIONS("extensions"),

        /**
         * Additional
         * 
         * <p>The contents of the narrative may contain additional information not found in the structured data. Note that there 
         * is no computable way to determine what the extra information is, other than by human inspection.
         */
        ADDITIONAL("additional"),

        /**
         * Empty
         * 
         * <p>The contents of the narrative are some equivalent of "No human-readable text provided in this case".
         */
        EMPTY("empty");

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
         * Factory method for creating NarrativeStatus.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding NarrativeStatus.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "generated":
                return GENERATED;
            case "extensions":
                return EXTENSIONS;
            case "additional":
                return ADDITIONAL;
            case "empty":
                return EMPTY;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
