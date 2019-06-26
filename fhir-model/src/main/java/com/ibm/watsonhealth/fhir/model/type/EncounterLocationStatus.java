/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class EncounterLocationStatus extends Code {
    /**
     * Planned
     */
    public static final EncounterLocationStatus PLANNED = EncounterLocationStatus.of(ValueSet.PLANNED);

    /**
     * Active
     */
    public static final EncounterLocationStatus ACTIVE = EncounterLocationStatus.of(ValueSet.ACTIVE);

    /**
     * Reserved
     */
    public static final EncounterLocationStatus RESERVED = EncounterLocationStatus.of(ValueSet.RESERVED);

    /**
     * Completed
     */
    public static final EncounterLocationStatus COMPLETED = EncounterLocationStatus.of(ValueSet.COMPLETED);

    private EncounterLocationStatus(Builder builder) {
        super(builder);
    }

    public static EncounterLocationStatus of(java.lang.String value) {
        return EncounterLocationStatus.builder().value(value).build();
    }

    public static EncounterLocationStatus of(ValueSet value) {
        return EncounterLocationStatus.builder().value(value).build();
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
        public EncounterLocationStatus build() {
            return new EncounterLocationStatus(this);
        }
    }

    public enum ValueSet {
        /**
         * Planned
         */
        PLANNED("planned"),

        /**
         * Active
         */
        ACTIVE("active"),

        /**
         * Reserved
         */
        RESERVED("reserved"),

        /**
         * Completed
         */
        COMPLETED("completed");

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
