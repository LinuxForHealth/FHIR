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

@System("http://hl7.org/fhir/clinical-use-issue-type")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ClinicalUseIssueType extends Code {
    /**
     * Indication
     * 
     * <p>A reason for giving the medicaton.
     */
    public static final ClinicalUseIssueType INDICATION = ClinicalUseIssueType.builder().value(Value.INDICATION).build();

    /**
     * Contraindication
     * 
     * <p>A reason for not giving the medicaition.
     */
    public static final ClinicalUseIssueType CONTRAINDICATION = ClinicalUseIssueType.builder().value(Value.CONTRAINDICATION).build();

    /**
     * Interaction
     * 
     * <p>Interactions between the medication and other substances.
     */
    public static final ClinicalUseIssueType INTERACTION = ClinicalUseIssueType.builder().value(Value.INTERACTION).build();

    /**
     * Undesirable Effect
     * 
     * <p>Side effects or adverse effects associated with the medication.
     */
    public static final ClinicalUseIssueType UNDESIRABLE_EFFECT = ClinicalUseIssueType.builder().value(Value.UNDESIRABLE_EFFECT).build();

    /**
     * Warning
     * 
     * <p>A general warning or issue that is not specifically one of the other types.
     */
    public static final ClinicalUseIssueType WARNING = ClinicalUseIssueType.builder().value(Value.WARNING).build();

    private volatile int hashCode;

    private ClinicalUseIssueType(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this ClinicalUseIssueType as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating ClinicalUseIssueType objects from a passed enum value.
     */
    public static ClinicalUseIssueType of(Value value) {
        switch (value) {
        case INDICATION:
            return INDICATION;
        case CONTRAINDICATION:
            return CONTRAINDICATION;
        case INTERACTION:
            return INTERACTION;
        case UNDESIRABLE_EFFECT:
            return UNDESIRABLE_EFFECT;
        case WARNING:
            return WARNING;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating ClinicalUseIssueType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static ClinicalUseIssueType of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating ClinicalUseIssueType objects from a passed string value.
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
     * Inherited factory method for creating ClinicalUseIssueType objects from a passed string value.
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
        ClinicalUseIssueType other = (ClinicalUseIssueType) obj;
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
         *     An enum constant for ClinicalUseIssueType
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public ClinicalUseIssueType build() {
            ClinicalUseIssueType clinicalUseIssueType = new ClinicalUseIssueType(this);
            if (validating) {
                validate(clinicalUseIssueType);
            }
            return clinicalUseIssueType;
        }

        protected void validate(ClinicalUseIssueType clinicalUseIssueType) {
            super.validate(clinicalUseIssueType);
        }

        protected Builder from(ClinicalUseIssueType clinicalUseIssueType) {
            super.from(clinicalUseIssueType);
            return this;
        }
    }

    public enum Value {
        /**
         * Indication
         * 
         * <p>A reason for giving the medicaton.
         */
        INDICATION("indication"),

        /**
         * Contraindication
         * 
         * <p>A reason for not giving the medicaition.
         */
        CONTRAINDICATION("contraindication"),

        /**
         * Interaction
         * 
         * <p>Interactions between the medication and other substances.
         */
        INTERACTION("interaction"),

        /**
         * Undesirable Effect
         * 
         * <p>Side effects or adverse effects associated with the medication.
         */
        UNDESIRABLE_EFFECT("undesirable-effect"),

        /**
         * Warning
         * 
         * <p>A general warning or issue that is not specifically one of the other types.
         */
        WARNING("warning");

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
         * Factory method for creating ClinicalUseIssueType.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding ClinicalUseIssueType.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "indication":
                return INDICATION;
            case "contraindication":
                return CONTRAINDICATION;
            case "interaction":
                return INTERACTION;
            case "undesirable-effect":
                return UNDESIRABLE_EFFECT;
            case "warning":
                return WARNING;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
