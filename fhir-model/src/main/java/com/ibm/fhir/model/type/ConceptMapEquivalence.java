/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ConceptMapEquivalence extends Code {
    /**
     * Related To
     */
    public static final ConceptMapEquivalence RELATEDTO = ConceptMapEquivalence.of(ValueSet.RELATEDTO);

    /**
     * Equivalent
     */
    public static final ConceptMapEquivalence EQUIVALENT = ConceptMapEquivalence.of(ValueSet.EQUIVALENT);

    /**
     * Equal
     */
    public static final ConceptMapEquivalence EQUAL = ConceptMapEquivalence.of(ValueSet.EQUAL);

    /**
     * Wider
     */
    public static final ConceptMapEquivalence WIDER = ConceptMapEquivalence.of(ValueSet.WIDER);

    /**
     * Subsumes
     */
    public static final ConceptMapEquivalence SUBSUMES = ConceptMapEquivalence.of(ValueSet.SUBSUMES);

    /**
     * Narrower
     */
    public static final ConceptMapEquivalence NARROWER = ConceptMapEquivalence.of(ValueSet.NARROWER);

    /**
     * Specializes
     */
    public static final ConceptMapEquivalence SPECIALIZES = ConceptMapEquivalence.of(ValueSet.SPECIALIZES);

    /**
     * Inexact
     */
    public static final ConceptMapEquivalence INEXACT = ConceptMapEquivalence.of(ValueSet.INEXACT);

    /**
     * Unmatched
     */
    public static final ConceptMapEquivalence UNMATCHED = ConceptMapEquivalence.of(ValueSet.UNMATCHED);

    /**
     * Disjoint
     */
    public static final ConceptMapEquivalence DISJOINT = ConceptMapEquivalence.of(ValueSet.DISJOINT);

    private volatile int hashCode;

    private ConceptMapEquivalence(Builder builder) {
        super(builder);
    }

    public static ConceptMapEquivalence of(java.lang.String value) {
        return ConceptMapEquivalence.builder().value(value).build();
    }

    public static ConceptMapEquivalence of(ValueSet value) {
        return ConceptMapEquivalence.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return ConceptMapEquivalence.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return ConceptMapEquivalence.builder().value(value).build();
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
        builder.id = id;
        builder.extension.addAll(extension);
        builder.value = value;
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
            return (Builder) super.value(ValueSet.from(value).value());
        }

        public Builder value(ValueSet value) {
            return (Builder) super.value(value.value());
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
