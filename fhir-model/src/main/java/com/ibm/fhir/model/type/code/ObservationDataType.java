/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type.code;

import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.String;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ObservationDataType extends Code {
    /**
     * Quantity
     */
    public static final ObservationDataType QUANTITY = ObservationDataType.builder().value(ValueSet.QUANTITY).build();

    /**
     * CodeableConcept
     */
    public static final ObservationDataType CODEABLE_CONCEPT = ObservationDataType.builder().value(ValueSet.CODEABLE_CONCEPT).build();

    /**
     * string
     */
    public static final ObservationDataType STRING = ObservationDataType.builder().value(ValueSet.STRING).build();

    /**
     * boolean
     */
    public static final ObservationDataType BOOLEAN = ObservationDataType.builder().value(ValueSet.BOOLEAN).build();

    /**
     * integer
     */
    public static final ObservationDataType INTEGER = ObservationDataType.builder().value(ValueSet.INTEGER).build();

    /**
     * Range
     */
    public static final ObservationDataType RANGE = ObservationDataType.builder().value(ValueSet.RANGE).build();

    /**
     * Ratio
     */
    public static final ObservationDataType RATIO = ObservationDataType.builder().value(ValueSet.RATIO).build();

    /**
     * SampledData
     */
    public static final ObservationDataType SAMPLED_DATA = ObservationDataType.builder().value(ValueSet.SAMPLED_DATA).build();

    /**
     * time
     */
    public static final ObservationDataType TIME = ObservationDataType.builder().value(ValueSet.TIME).build();

    /**
     * dateTime
     */
    public static final ObservationDataType DATE_TIME = ObservationDataType.builder().value(ValueSet.DATE_TIME).build();

    /**
     * Period
     */
    public static final ObservationDataType PERIOD = ObservationDataType.builder().value(ValueSet.PERIOD).build();

    private volatile int hashCode;

    private ObservationDataType(Builder builder) {
        super(builder);
    }

    public static ObservationDataType of(ValueSet value) {
        switch (value) {
        case QUANTITY:
            return QUANTITY;
        case CODEABLE_CONCEPT:
            return CODEABLE_CONCEPT;
        case STRING:
            return STRING;
        case BOOLEAN:
            return BOOLEAN;
        case INTEGER:
            return INTEGER;
        case RANGE:
            return RANGE;
        case RATIO:
            return RATIO;
        case SAMPLED_DATA:
            return SAMPLED_DATA;
        case TIME:
            return TIME;
        case DATE_TIME:
            return DATE_TIME;
        case PERIOD:
            return PERIOD;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    public static ObservationDataType of(java.lang.String value) {
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
        ObservationDataType other = (ObservationDataType) obj;
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
