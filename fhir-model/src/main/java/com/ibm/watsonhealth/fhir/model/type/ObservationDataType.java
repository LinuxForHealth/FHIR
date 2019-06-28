/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class ObservationDataType extends Code {
    /**
     * Quantity
     */
    public static final ObservationDataType QUANTITY = ObservationDataType.of(ValueSet.QUANTITY);

    /**
     * CodeableConcept
     */
    public static final ObservationDataType CODEABLE_CONCEPT = ObservationDataType.of(ValueSet.CODEABLE_CONCEPT);

    /**
     * string
     */
    public static final ObservationDataType STRING = ObservationDataType.of(ValueSet.STRING);

    /**
     * boolean
     */
    public static final ObservationDataType BOOLEAN = ObservationDataType.of(ValueSet.BOOLEAN);

    /**
     * integer
     */
    public static final ObservationDataType INTEGER = ObservationDataType.of(ValueSet.INTEGER);

    /**
     * Range
     */
    public static final ObservationDataType RANGE = ObservationDataType.of(ValueSet.RANGE);

    /**
     * Ratio
     */
    public static final ObservationDataType RATIO = ObservationDataType.of(ValueSet.RATIO);

    /**
     * SampledData
     */
    public static final ObservationDataType SAMPLED_DATA = ObservationDataType.of(ValueSet.SAMPLED_DATA);

    /**
     * time
     */
    public static final ObservationDataType TIME = ObservationDataType.of(ValueSet.TIME);

    /**
     * dateTime
     */
    public static final ObservationDataType DATE_TIME = ObservationDataType.of(ValueSet.DATE_TIME);

    /**
     * Period
     */
    public static final ObservationDataType PERIOD = ObservationDataType.of(ValueSet.PERIOD);

    private ObservationDataType(Builder builder) {
        super(builder);
    }

    public static ObservationDataType of(java.lang.String value) {
        return ObservationDataType.builder().value(value).build();
    }

    public static ObservationDataType of(ValueSet value) {
        return ObservationDataType.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return ObservationDataType.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return ObservationDataType.builder().value(value).build();
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
        public ObservationDataType build() {
            return new ObservationDataType(this);
        }
    }

    public enum ValueSet {
        /**
         * Quantity
         */
        QUANTITY("Quantity"),

        /**
         * CodeableConcept
         */
        CODEABLE_CONCEPT("CodeableConcept"),

        /**
         * string
         */
        STRING("string"),

        /**
         * boolean
         */
        BOOLEAN("boolean"),

        /**
         * integer
         */
        INTEGER("integer"),

        /**
         * Range
         */
        RANGE("Range"),

        /**
         * Ratio
         */
        RATIO("Ratio"),

        /**
         * SampledData
         */
        SAMPLED_DATA("SampledData"),

        /**
         * time
         */
        TIME("time"),

        /**
         * dateTime
         */
        DATE_TIME("dateTime"),

        /**
         * Period
         */
        PERIOD("Period");

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
