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

@System("http://hl7.org/fhir/variable-handling")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class EvidenceVariableHandling extends Code {
    /**
     * continuous variable
     * 
     * <p>A continuous variable is one for which, within the limits the variable ranges, any value is possible (from STATO 
     * http://purl.obolibrary.org/obo/STATO_0000251).
     */
    public static final EvidenceVariableHandling CONTINUOUS = EvidenceVariableHandling.builder().value(Value.CONTINUOUS).build();

    /**
     * dichotomous variable
     * 
     * <p>A dichotomous variable is a categorical variable which is defined to have only 2 categories or possible values 
     * (from STATO http://purl.obolibrary.org/obo/STATO_0000090).
     */
    public static final EvidenceVariableHandling DICHOTOMOUS = EvidenceVariableHandling.builder().value(Value.DICHOTOMOUS).build();

    /**
     * ordinal variable
     * 
     * <p>An ordinal variable is a categorical variable where the discrete possible values are ordered or correspond to an 
     * implicit ranking (from STATO http://purl.obolibrary.org/obo/STATO_0000228).
     */
    public static final EvidenceVariableHandling ORDINAL = EvidenceVariableHandling.builder().value(Value.ORDINAL).build();

    /**
     * polychotomous variable
     * 
     * <p>A polychotomous variable is a categorical variable which is defined to have minimally 2 categories or possible 
     * values. (from STATO http://purl.obolibrary.org/obo/STATO_0000087). Suggestion to limit code use to situations when 
     * neither dichotomous nor ordinal variables apply.
     */
    public static final EvidenceVariableHandling POLYCHOTOMOUS = EvidenceVariableHandling.builder().value(Value.POLYCHOTOMOUS).build();

    private volatile int hashCode;

    private EvidenceVariableHandling(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this EvidenceVariableHandling as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating EvidenceVariableHandling objects from a passed enum value.
     */
    public static EvidenceVariableHandling of(Value value) {
        switch (value) {
        case CONTINUOUS:
            return CONTINUOUS;
        case DICHOTOMOUS:
            return DICHOTOMOUS;
        case ORDINAL:
            return ORDINAL;
        case POLYCHOTOMOUS:
            return POLYCHOTOMOUS;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating EvidenceVariableHandling objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static EvidenceVariableHandling of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating EvidenceVariableHandling objects from a passed string value.
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
     * Inherited factory method for creating EvidenceVariableHandling objects from a passed string value.
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
        EvidenceVariableHandling other = (EvidenceVariableHandling) obj;
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
         *     An enum constant for EvidenceVariableHandling
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public EvidenceVariableHandling build() {
            EvidenceVariableHandling evidenceVariableHandling = new EvidenceVariableHandling(this);
            if (validating) {
                validate(evidenceVariableHandling);
            }
            return evidenceVariableHandling;
        }

        protected void validate(EvidenceVariableHandling evidenceVariableHandling) {
            super.validate(evidenceVariableHandling);
        }

        protected Builder from(EvidenceVariableHandling evidenceVariableHandling) {
            super.from(evidenceVariableHandling);
            return this;
        }
    }

    public enum Value {
        /**
         * continuous variable
         * 
         * <p>A continuous variable is one for which, within the limits the variable ranges, any value is possible (from STATO 
         * http://purl.obolibrary.org/obo/STATO_0000251).
         */
        CONTINUOUS("continuous"),

        /**
         * dichotomous variable
         * 
         * <p>A dichotomous variable is a categorical variable which is defined to have only 2 categories or possible values 
         * (from STATO http://purl.obolibrary.org/obo/STATO_0000090).
         */
        DICHOTOMOUS("dichotomous"),

        /**
         * ordinal variable
         * 
         * <p>An ordinal variable is a categorical variable where the discrete possible values are ordered or correspond to an 
         * implicit ranking (from STATO http://purl.obolibrary.org/obo/STATO_0000228).
         */
        ORDINAL("ordinal"),

        /**
         * polychotomous variable
         * 
         * <p>A polychotomous variable is a categorical variable which is defined to have minimally 2 categories or possible 
         * values. (from STATO http://purl.obolibrary.org/obo/STATO_0000087). Suggestion to limit code use to situations when 
         * neither dichotomous nor ordinal variables apply.
         */
        POLYCHOTOMOUS("polychotomous");

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
         * Factory method for creating EvidenceVariableHandling.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding EvidenceVariableHandling.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "continuous":
                return CONTINUOUS;
            case "dichotomous":
                return DICHOTOMOUS;
            case "ordinal":
                return ORDINAL;
            case "polychotomous":
                return POLYCHOTOMOUS;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
