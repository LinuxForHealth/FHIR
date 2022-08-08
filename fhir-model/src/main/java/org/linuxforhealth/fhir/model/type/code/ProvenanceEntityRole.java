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

@System("http://hl7.org/fhir/provenance-entity-role")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class ProvenanceEntityRole extends Code {
    /**
     * Derivation
     * 
     * <p>A transformation of an entity into another, an update of an entity resulting in a new one, or the construction of a 
     * new entity based on a pre-existing entity.
     */
    public static final ProvenanceEntityRole DERIVATION = ProvenanceEntityRole.builder().value(Value.DERIVATION).build();

    /**
     * Revision
     * 
     * <p>A derivation for which the resulting entity is a revised version of some original.
     */
    public static final ProvenanceEntityRole REVISION = ProvenanceEntityRole.builder().value(Value.REVISION).build();

    /**
     * Quotation
     * 
     * <p>The repeat of (some or all of) an entity, such as text or image, by someone who might or might not be its original 
     * author.
     */
    public static final ProvenanceEntityRole QUOTATION = ProvenanceEntityRole.builder().value(Value.QUOTATION).build();

    /**
     * Source
     * 
     * <p>A primary source for a topic refers to something produced by some agent with direct experience and knowledge about 
     * the topic, at the time of the topic's study, without benefit from hindsight.
     */
    public static final ProvenanceEntityRole SOURCE = ProvenanceEntityRole.builder().value(Value.SOURCE).build();

    /**
     * Removal
     * 
     * <p>A derivation for which the entity is removed from accessibility usually through the use of the Delete operation.
     */
    public static final ProvenanceEntityRole REMOVAL = ProvenanceEntityRole.builder().value(Value.REMOVAL).build();

    private volatile int hashCode;

    private ProvenanceEntityRole(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this ProvenanceEntityRole as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating ProvenanceEntityRole objects from a passed enum value.
     */
    public static ProvenanceEntityRole of(Value value) {
        switch (value) {
        case DERIVATION:
            return DERIVATION;
        case REVISION:
            return REVISION;
        case QUOTATION:
            return QUOTATION;
        case SOURCE:
            return SOURCE;
        case REMOVAL:
            return REMOVAL;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating ProvenanceEntityRole objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static ProvenanceEntityRole of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating ProvenanceEntityRole objects from a passed string value.
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
     * Inherited factory method for creating ProvenanceEntityRole objects from a passed string value.
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
        ProvenanceEntityRole other = (ProvenanceEntityRole) obj;
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
         *     An enum constant for ProvenanceEntityRole
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public ProvenanceEntityRole build() {
            ProvenanceEntityRole provenanceEntityRole = new ProvenanceEntityRole(this);
            if (validating) {
                validate(provenanceEntityRole);
            }
            return provenanceEntityRole;
        }

        protected void validate(ProvenanceEntityRole provenanceEntityRole) {
            super.validate(provenanceEntityRole);
        }

        protected Builder from(ProvenanceEntityRole provenanceEntityRole) {
            super.from(provenanceEntityRole);
            return this;
        }
    }

    public enum Value {
        /**
         * Derivation
         * 
         * <p>A transformation of an entity into another, an update of an entity resulting in a new one, or the construction of a 
         * new entity based on a pre-existing entity.
         */
        DERIVATION("derivation"),

        /**
         * Revision
         * 
         * <p>A derivation for which the resulting entity is a revised version of some original.
         */
        REVISION("revision"),

        /**
         * Quotation
         * 
         * <p>The repeat of (some or all of) an entity, such as text or image, by someone who might or might not be its original 
         * author.
         */
        QUOTATION("quotation"),

        /**
         * Source
         * 
         * <p>A primary source for a topic refers to something produced by some agent with direct experience and knowledge about 
         * the topic, at the time of the topic's study, without benefit from hindsight.
         */
        SOURCE("source"),

        /**
         * Removal
         * 
         * <p>A derivation for which the entity is removed from accessibility usually through the use of the Delete operation.
         */
        REMOVAL("removal");

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
         * Factory method for creating ProvenanceEntityRole.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding ProvenanceEntityRole.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "derivation":
                return DERIVATION;
            case "revision":
                return REVISION;
            case "quotation":
                return QUOTATION;
            case "source":
                return SOURCE;
            case "removal":
                return REMOVAL;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
