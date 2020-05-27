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

@System("http://hl7.org/fhir/concept-subsumption-outcome")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ConceptSubsumptionOutcome extends Code {
    /**
     * Equivalent
     * 
     * <p>The two concepts are equivalent (have the same properties).
     */
    public static final ConceptSubsumptionOutcome EQUIVALENT = ConceptSubsumptionOutcome.builder().value(ValueSet.EQUIVALENT).build();

    /**
     * Subsumes
     * 
     * <p>Coding/code "A" subsumes Coding/code "B" (e.g. B has all the properties A has, and some of it's own).
     */
    public static final ConceptSubsumptionOutcome SUBSUMES = ConceptSubsumptionOutcome.builder().value(ValueSet.SUBSUMES).build();

    /**
     * Subsumed-By
     * 
     * <p>Coding/code "A" is subsumed by Coding/code "B" (e.g. A has all the properties B has, and some of it's own).
     */
    public static final ConceptSubsumptionOutcome SUBSUMED_BY = ConceptSubsumptionOutcome.builder().value(ValueSet.SUBSUMED_BY).build();

    /**
     * Not-Subsumed
     * 
     * <p>Coding/code "A" and Coding/code "B" are disjoint (e.g. each has propeties that the other doesn't have).
     */
    public static final ConceptSubsumptionOutcome NOT_SUBSUMED = ConceptSubsumptionOutcome.builder().value(ValueSet.NOT_SUBSUMED).build();

    private volatile int hashCode;

    private ConceptSubsumptionOutcome(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    public static ConceptSubsumptionOutcome of(ValueSet value) {
        switch (value) {
        case EQUIVALENT:
            return EQUIVALENT;
        case SUBSUMES:
            return SUBSUMES;
        case SUBSUMED_BY:
            return SUBSUMED_BY;
        case NOT_SUBSUMED:
            return NOT_SUBSUMED;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    public static ConceptSubsumptionOutcome of(java.lang.String value) {
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
        ConceptSubsumptionOutcome other = (ConceptSubsumptionOutcome) obj;
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
        public ConceptSubsumptionOutcome build() {
            return new ConceptSubsumptionOutcome(this);
        }
    }

    public enum ValueSet {
        /**
         * Equivalent
         * 
         * <p>The two concepts are equivalent (have the same properties).
         */
        EQUIVALENT("equivalent"),

        /**
         * Subsumes
         * 
         * <p>Coding/code "A" subsumes Coding/code "B" (e.g. B has all the properties A has, and some of it's own).
         */
        SUBSUMES("subsumes"),

        /**
         * Subsumed-By
         * 
         * <p>Coding/code "A" is subsumed by Coding/code "B" (e.g. A has all the properties B has, and some of it's own).
         */
        SUBSUMED_BY("subsumed-by"),

        /**
         * Not-Subsumed
         * 
         * <p>Coding/code "A" and Coding/code "B" are disjoint (e.g. each has propeties that the other doesn't have).
         */
        NOT_SUBSUMED("not-subsumed");

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
