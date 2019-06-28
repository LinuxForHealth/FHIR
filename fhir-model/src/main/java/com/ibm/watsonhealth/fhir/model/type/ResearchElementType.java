/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class ResearchElementType extends Code {
    /**
     * Population
     */
    public static final ResearchElementType POPULATION = ResearchElementType.of(ValueSet.POPULATION);

    /**
     * Exposure
     */
    public static final ResearchElementType EXPOSURE = ResearchElementType.of(ValueSet.EXPOSURE);

    /**
     * Outcome
     */
    public static final ResearchElementType OUTCOME = ResearchElementType.of(ValueSet.OUTCOME);

    private ResearchElementType(Builder builder) {
        super(builder);
    }

    public static ResearchElementType of(java.lang.String value) {
        return ResearchElementType.builder().value(value).build();
    }

    public static ResearchElementType of(ValueSet value) {
        return ResearchElementType.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return ResearchElementType.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return ResearchElementType.builder().value(value).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.id = id;
        builder.extension.addAll(extension);
        builder.value = value;
        return builder;
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
        public ResearchElementType build() {
            return new ResearchElementType(this);
        }
    }

    public enum ValueSet {
        /**
         * Population
         */
        POPULATION("population"),

        /**
         * Exposure
         */
        EXPOSURE("exposure"),

        /**
         * Outcome
         */
        OUTCOME("outcome");

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
