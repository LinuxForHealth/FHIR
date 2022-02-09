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

@System("http://hl7.org/fhir/concept-map-equivalence")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ConceptMapEquivalence extends Code {
    /**
     * Related To
     * 
     * <p>The concepts are related to each other, and have at least some overlap in meaning, but the exact relationship is 
     * not known.
     */
    public static final ConceptMapEquivalence RELATEDTO = ConceptMapEquivalence.builder().value(Value.RELATEDTO).build();

    /**
     * Equivalent
     * 
     * <p>The definitions of the concepts mean the same thing (including when structural implications of meaning are 
     * considered) (i.e. extensionally identical).
     */
    public static final ConceptMapEquivalence EQUIVALENT = ConceptMapEquivalence.builder().value(Value.EQUIVALENT).build();

    /**
     * Equal
     * 
     * <p>The definitions of the concepts are exactly the same (i.e. only grammatical differences) and structural 
     * implications of meaning are identical or irrelevant (i.e. intentionally identical).
     */
    public static final ConceptMapEquivalence EQUAL = ConceptMapEquivalence.builder().value(Value.EQUAL).build();

    /**
     * Wider
     * 
     * <p>The target mapping is wider in meaning than the source concept.
     */
    public static final ConceptMapEquivalence WIDER = ConceptMapEquivalence.builder().value(Value.WIDER).build();

    /**
     * Subsumes
     * 
     * <p>The target mapping subsumes the meaning of the source concept (e.g. the source is-a target).
     */
    public static final ConceptMapEquivalence SUBSUMES = ConceptMapEquivalence.builder().value(Value.SUBSUMES).build();

    /**
     * Narrower
     * 
     * <p>The target mapping is narrower in meaning than the source concept. The sense in which the mapping is narrower SHALL 
     * be described in the comments in this case, and applications should be careful when attempting to use these mappings 
     * operationally.
     */
    public static final ConceptMapEquivalence NARROWER = ConceptMapEquivalence.builder().value(Value.NARROWER).build();

    /**
     * Specializes
     * 
     * <p>The target mapping specializes the meaning of the source concept (e.g. the target is-a source).
     */
    public static final ConceptMapEquivalence SPECIALIZES = ConceptMapEquivalence.builder().value(Value.SPECIALIZES).build();

    /**
     * Inexact
     * 
     * <p>The target mapping overlaps with the source concept, but both source and target cover additional meaning, or the 
     * definitions are imprecise and it is uncertain whether they have the same boundaries to their meaning. The sense in 
     * which the mapping is inexact SHALL be described in the comments in this case, and applications should be careful when 
     * attempting to use these mappings operationally.
     */
    public static final ConceptMapEquivalence INEXACT = ConceptMapEquivalence.builder().value(Value.INEXACT).build();

    /**
     * Unmatched
     * 
     * <p>There is no match for this concept in the target code system.
     */
    public static final ConceptMapEquivalence UNMATCHED = ConceptMapEquivalence.builder().value(Value.UNMATCHED).build();

    /**
     * Disjoint
     * 
     * <p>This is an explicit assertion that there is no mapping between the source and target concept.
     */
    public static final ConceptMapEquivalence DISJOINT = ConceptMapEquivalence.builder().value(Value.DISJOINT).build();

    private volatile int hashCode;

    private ConceptMapEquivalence(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this ConceptMapEquivalence as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating ConceptMapEquivalence objects from a passed enum value.
     */
    public static ConceptMapEquivalence of(Value value) {
        switch (value) {
        case RELATEDTO:
            return RELATEDTO;
        case EQUIVALENT:
            return EQUIVALENT;
        case EQUAL:
            return EQUAL;
        case WIDER:
            return WIDER;
        case SUBSUMES:
            return SUBSUMES;
        case NARROWER:
            return NARROWER;
        case SPECIALIZES:
            return SPECIALIZES;
        case INEXACT:
            return INEXACT;
        case UNMATCHED:
            return UNMATCHED;
        case DISJOINT:
            return DISJOINT;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating ConceptMapEquivalence objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static ConceptMapEquivalence of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating ConceptMapEquivalence objects from a passed string value.
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
     * Inherited factory method for creating ConceptMapEquivalence objects from a passed string value.
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
        ConceptMapEquivalence other = (ConceptMapEquivalence) obj;
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
         *     An enum constant for ConceptMapEquivalence
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public ConceptMapEquivalence build() {
            ConceptMapEquivalence conceptMapEquivalence = new ConceptMapEquivalence(this);
            if (validating) {
                validate(conceptMapEquivalence);
            }
            return conceptMapEquivalence;
        }

        protected void validate(ConceptMapEquivalence conceptMapEquivalence) {
            super.validate(conceptMapEquivalence);
        }

        protected Builder from(ConceptMapEquivalence conceptMapEquivalence) {
            super.from(conceptMapEquivalence);
            return this;
        }
    }

    public enum Value {
        /**
         * Related To
         * 
         * <p>The concepts are related to each other, and have at least some overlap in meaning, but the exact relationship is 
         * not known.
         */
        RELATEDTO("relatedto"),

        /**
         * Equivalent
         * 
         * <p>The definitions of the concepts mean the same thing (including when structural implications of meaning are 
         * considered) (i.e. extensionally identical).
         */
        EQUIVALENT("equivalent"),

        /**
         * Equal
         * 
         * <p>The definitions of the concepts are exactly the same (i.e. only grammatical differences) and structural 
         * implications of meaning are identical or irrelevant (i.e. intentionally identical).
         */
        EQUAL("equal"),

        /**
         * Wider
         * 
         * <p>The target mapping is wider in meaning than the source concept.
         */
        WIDER("wider"),

        /**
         * Subsumes
         * 
         * <p>The target mapping subsumes the meaning of the source concept (e.g. the source is-a target).
         */
        SUBSUMES("subsumes"),

        /**
         * Narrower
         * 
         * <p>The target mapping is narrower in meaning than the source concept. The sense in which the mapping is narrower SHALL 
         * be described in the comments in this case, and applications should be careful when attempting to use these mappings 
         * operationally.
         */
        NARROWER("narrower"),

        /**
         * Specializes
         * 
         * <p>The target mapping specializes the meaning of the source concept (e.g. the target is-a source).
         */
        SPECIALIZES("specializes"),

        /**
         * Inexact
         * 
         * <p>The target mapping overlaps with the source concept, but both source and target cover additional meaning, or the 
         * definitions are imprecise and it is uncertain whether they have the same boundaries to their meaning. The sense in 
         * which the mapping is inexact SHALL be described in the comments in this case, and applications should be careful when 
         * attempting to use these mappings operationally.
         */
        INEXACT("inexact"),

        /**
         * Unmatched
         * 
         * <p>There is no match for this concept in the target code system.
         */
        UNMATCHED("unmatched"),

        /**
         * Disjoint
         * 
         * <p>This is an explicit assertion that there is no mapping between the source and target concept.
         */
        DISJOINT("disjoint");

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
         * Factory method for creating ConceptMapEquivalence.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding ConceptMapEquivalence.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "relatedto":
                return RELATEDTO;
            case "equivalent":
                return EQUIVALENT;
            case "equal":
                return EQUAL;
            case "wider":
                return WIDER;
            case "subsumes":
                return SUBSUMES;
            case "narrower":
                return NARROWER;
            case "specializes":
                return SPECIALIZES;
            case "inexact":
                return INEXACT;
            case "unmatched":
                return UNMATCHED;
            case "disjoint":
                return DISJOINT;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
