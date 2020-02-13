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

@System("http://hl7.org/fhir/concept-map-equivalence")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ConceptMapEquivalence extends Code {
    /**
     * Related To
     */
    public static final ConceptMapEquivalence RELATEDTO = ConceptMapEquivalence.builder().value(ValueSet.RELATEDTO).build();

    /**
     * Equivalent
     */
    public static final ConceptMapEquivalence EQUIVALENT = ConceptMapEquivalence.builder().value(ValueSet.EQUIVALENT).build();

    /**
     * Equal
     */
    public static final ConceptMapEquivalence EQUAL = ConceptMapEquivalence.builder().value(ValueSet.EQUAL).build();

    /**
     * Wider
     */
    public static final ConceptMapEquivalence WIDER = ConceptMapEquivalence.builder().value(ValueSet.WIDER).build();

    /**
     * Subsumes
     */
    public static final ConceptMapEquivalence SUBSUMES = ConceptMapEquivalence.builder().value(ValueSet.SUBSUMES).build();

    /**
     * Narrower
     */
    public static final ConceptMapEquivalence NARROWER = ConceptMapEquivalence.builder().value(ValueSet.NARROWER).build();

    /**
     * Specializes
     */
    public static final ConceptMapEquivalence SPECIALIZES = ConceptMapEquivalence.builder().value(ValueSet.SPECIALIZES).build();

    /**
     * Inexact
     */
    public static final ConceptMapEquivalence INEXACT = ConceptMapEquivalence.builder().value(ValueSet.INEXACT).build();

    /**
     * Unmatched
     */
    public static final ConceptMapEquivalence UNMATCHED = ConceptMapEquivalence.builder().value(ValueSet.UNMATCHED).build();

    /**
     * Disjoint
     */
    public static final ConceptMapEquivalence DISJOINT = ConceptMapEquivalence.builder().value(ValueSet.DISJOINT).build();

    private volatile int hashCode;

    private ConceptMapEquivalence(Builder builder) {
        super(builder);
    }

    public static ConceptMapEquivalence of(ValueSet value) {
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

    public static ConceptMapEquivalence of(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    public static String string(java.lang.String value) {
        return of(ValueSet.from(value));
    }

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
        public ConceptMapEquivalence build() {
            return new ConceptMapEquivalence(this);
        }
    }

    public enum ValueSet {
        /**
         * Related To
         */
        RELATEDTO("relatedto"),

        /**
         * Equivalent
         */
        EQUIVALENT("equivalent"),

        /**
         * Equal
         */
        EQUAL("equal"),

        /**
         * Wider
         */
        WIDER("wider"),

        /**
         * Subsumes
         */
        SUBSUMES("subsumes"),

        /**
         * Narrower
         */
        NARROWER("narrower"),

        /**
         * Specializes
         */
        SPECIALIZES("specializes"),

        /**
         * Inexact
         */
        INEXACT("inexact"),

        /**
         * Unmatched
         */
        UNMATCHED("unmatched"),

        /**
         * Disjoint
         */
        DISJOINT("disjoint");

        private final java.lang.String value;

        ValueSet(java.lang.String value) {
            this.value = value;
        }

        public java.lang.String value() {
            return value;
        }

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
