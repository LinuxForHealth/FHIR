/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class AdministrativeGender extends Code {
    /**
     * Male
     */
    public static final AdministrativeGender MALE = AdministrativeGender.of(ValueSet.MALE);

    /**
     * Female
     */
    public static final AdministrativeGender FEMALE = AdministrativeGender.of(ValueSet.FEMALE);

    /**
     * Other
     */
    public static final AdministrativeGender OTHER = AdministrativeGender.of(ValueSet.OTHER);

    /**
     * Unknown
     */
    public static final AdministrativeGender UNKNOWN = AdministrativeGender.of(ValueSet.UNKNOWN);

    private AdministrativeGender(Builder builder) {
        super(builder);
    }

    public static AdministrativeGender of(java.lang.String value) {
        return AdministrativeGender.builder().value(value).build();
    }

    public static AdministrativeGender of(ValueSet value) {
        return AdministrativeGender.builder().value(value).build();
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
        public AdministrativeGender build() {
            return new AdministrativeGender(this);
        }
    }

    public enum ValueSet {
        /**
         * Male
         */
        MALE("male"),

        /**
         * Female
         */
        FEMALE("female"),

        /**
         * Other
         */
        OTHER("other"),

        /**
         * Unknown
         */
        UNKNOWN("unknown");

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
