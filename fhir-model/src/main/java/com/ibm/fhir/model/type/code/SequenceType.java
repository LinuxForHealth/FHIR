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

@System("http://hl7.org/fhir/sequence-type")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class SequenceType extends Code {
    /**
     * AA Sequence
     * 
     * <p>Amino acid sequence.
     */
    public static final SequenceType AA = SequenceType.builder().value(Value.AA).build();

    /**
     * DNA Sequence
     * 
     * <p>DNA Sequence.
     */
    public static final SequenceType DNA = SequenceType.builder().value(Value.DNA).build();

    /**
     * RNA Sequence
     * 
     * <p>RNA Sequence.
     */
    public static final SequenceType RNA = SequenceType.builder().value(Value.RNA).build();

    private volatile int hashCode;

    private SequenceType(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this SequenceType as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating SequenceType objects from a passed enum value.
     */
    public static SequenceType of(Value value) {
        switch (value) {
        case AA:
            return AA;
        case DNA:
            return DNA;
        case RNA:
            return RNA;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating SequenceType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static SequenceType of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating SequenceType objects from a passed string value.
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
     * Inherited factory method for creating SequenceType objects from a passed string value.
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
        SequenceType other = (SequenceType) obj;
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
         *     An enum constant for SequenceType
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public SequenceType build() {
            SequenceType sequenceType = new SequenceType(this);
            if (validating) {
                validate(sequenceType);
            }
            return sequenceType;
        }

        protected void validate(SequenceType sequenceType) {
            super.validate(sequenceType);
        }

        protected Builder from(SequenceType sequenceType) {
            super.from(sequenceType);
            return this;
        }
    }

    public enum Value {
        /**
         * AA Sequence
         * 
         * <p>Amino acid sequence.
         */
        AA("aa"),

        /**
         * DNA Sequence
         * 
         * <p>DNA Sequence.
         */
        DNA("dna"),

        /**
         * RNA Sequence
         * 
         * <p>RNA Sequence.
         */
        RNA("rna");

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
         * Factory method for creating SequenceType.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding SequenceType.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "aa":
                return AA;
            case "dna":
                return DNA;
            case "rna":
                return RNA;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
