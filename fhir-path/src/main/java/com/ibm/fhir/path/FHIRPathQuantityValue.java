/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path;

import java.math.BigDecimal;
import java.util.Objects;

import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.path.visitor.FHIRPathNodeVisitor;

/**
 * A {@link FHIRPathSystemValue} that wraps a {@link BigDecimal} value and {@link String} unit
 */
public class FHIRPathQuantityValue extends FHIRPathAbstractSystemValue {
    private final BigDecimal value;
    private final String unit;

    protected FHIRPathQuantityValue(Builder builder) {
        super(builder);
        this.value = builder.value;
        this.unit = builder.unit;
    }

    @Override
    public boolean isQuantityValue() {
        return true;
    }

    /**
     * The {@link BigDecimal} value wrapped by this FHIRPathQuantityValue
     *
     * @return
     *     the {@link BigDecimal} value wrapped by this FHIRPathQuantityValue
     */
    public BigDecimal value() {
        return value;
    }

    /**
     * The {@link String} unit wrapped by this FHIRPathQuantityValue
     *
     * @return
     *     the {@link String} unit wrapped by this FHIRPathQuantityValue
     */
    public String unit() {
        return unit;
    }

    /**
     * Static factory method for creating FHIRPathQuantityValue instances from a {@link Quantity} value
     *
     * @param quantity
     *     the {@link Quantity} value
     * @return
     *     a new FHIRPathQuantityValue instance
     */
    public static FHIRPathQuantityValue quantityValue(Quantity quantity) {
        if (quantity.getValue() != null &&
            quantity.getValue().getValue() != null &&
            quantity.getSystem() != null &&
            "http://unitsofmeasure.org".equals(quantity.getSystem().getValue()) &&
            quantity.getCode() != null &&
            quantity.getCode().getValue() != null) {
            BigDecimal value = quantity.getValue().getValue();
            String unit = getUnit(quantity.getCode().getValue());
            return quantityValue(value, unit);
        }
        return null;
    }

    private static String getUnit(String code) {
        switch (code) {
        case "a":
            return "year";
        case "mo":
            return "month";
        case "d":
            return "day";
        case "h":
            return "hour";
        case "min":
            return "minute";
        case "s":
            return "second";
        }
        return code;
    }

    /**
     * Static factory method for creating FHIRPathQuantityValue instances from a {@link BigDecimal} value and {@link String} unit
     *
     * @param value
     *     the {@link BigDecimal} value
     * @param unit
     *     the {@link String} unit
     * @return
     *     a new FHIRPathQuantityValue instance
     */
    public static FHIRPathQuantityValue quantityValue(BigDecimal value, String unit) {
        return FHIRPathQuantityValue.builder(value, unit).build();
    }

    @Override
    public Builder toBuilder() {
        return new Builder(type, value, unit);
    }

    /**
     * Static factory method for creating builder instances from a {@link BigDecimal} value and {@link String} unit
     *
     * @param value
     *     the {@link BigDecimal} value
     * @param unit
     *     the {@link String} unit
     * @return
     *     a new builder for building FHIRPathQuantityValue instances
     */
    public static Builder builder(BigDecimal value, String unit) {
        return new Builder(FHIRPathType.SYSTEM_QUANTITY, value, unit);
    }

    public static class Builder extends FHIRPathAbstractSystemValue.Builder {
        private final BigDecimal value;
        private final String unit;

        private Builder(FHIRPathType type, BigDecimal value, String unit) {
            super(type);
            this.value = value;
            this.unit = unit;
        }

        @Override
        public Builder name(String name) {
            return (Builder) super.name(name);
        }

        @Override
        public Builder path(String path) {
            return (Builder) super.path(path);
        }

        /**
         * Build a FHIRPathQuantityValue instance using this builder
         *
         * @return
         *     a new FHIRPathQuantityValue instance
         */
        @Override
        public FHIRPathQuantityValue build() {
            return new FHIRPathQuantityValue(this);
        }
    }

    /**
     * Add this FHIRPathQuantityValue to another FHIRPathQuantityValue
     *
     * @param quantityValue
     *     the other FHIRPathQuantityValue
     * @return
     *     the result of adding this FHIRPathQuantityValue to another FHIRPathQuantityValue
     */
    public FHIRPathQuantityValue add(FHIRPathQuantityValue quantityValue) {
        return FHIRPathQuantityValue.quantityValue(value.add(quantityValue.value()), unit);
    }

    /**
     * Subtract another FHIRPathQuantityValue from this FHIRPathQuantityValue
     *
     * @param quantityValue
     *     the other FHIRPathQuantityValue
     * @return
     *     the result of subtracting another FHIRPathQuantityValue from this FHIRPathQuantityValue
     */
    public FHIRPathQuantityValue subtract(FHIRPathQuantityValue quantityValue) {
        return FHIRPathQuantityValue.quantityValue(value.subtract(quantityValue.value()), unit);
    }

    /**
     * Indicates whether this FHIRPathQuantityValue is comparable to the parameter
     *
     * @return
     *     true if the parameter or its primitive value is a FHIRPathQuantityValue or a {@FHIRPathNumberValue}, otherwise false
     */
    @Override
    public boolean isComparableTo(FHIRPathNode other) {
        if (other instanceof FHIRPathQuantityValue || other.getValue() instanceof FHIRPathQuantityValue) {
            FHIRPathQuantityValue quantityValue = (other instanceof FHIRPathQuantityValue) ? (FHIRPathQuantityValue) other : (FHIRPathQuantityValue) other.getValue();
            return unit.equals(quantityValue.unit());
        }
        return (other instanceof FHIRPathNumberValue) ||
                (other.getValue() instanceof FHIRPathNumberValue);
    }

    /**
     * Compare the quantity value wrapped by this FHIRPathQuantityValue to the parameter
     *
     * @param other
     *     the other {@link FHIRPathNode}
     * @return
     *     0 if the quantity value wrapped by this FHIRPathQuantityValue is equal to the parameter; a positive value if this FHIRPathQuantityValue is greater than the parameter; and
     *     a negative value if this FHIRPathQuantityValue is less than the parameter
     */
    @Override
    public int compareTo(FHIRPathNode other) {
        if (!isComparableTo(other)) {
            throw new IllegalArgumentException();
        }
        if (other instanceof FHIRPathQuantityValue || other.getValue() instanceof FHIRPathQuantityValue) {
            FHIRPathQuantityValue quantityValue = (other instanceof FHIRPathQuantityValue) ? (FHIRPathQuantityValue) other : (FHIRPathQuantityValue) other.getValue();
            return value.compareTo(quantityValue.value());
        }
        FHIRPathNumberValue numberValue = (other instanceof FHIRPathNumberValue) ? (FHIRPathNumberValue) other : (FHIRPathNumberValue) other.getValue();
        return value.compareTo(numberValue.decimal());
    }

    /**
     * Indicates whether the {@link BigDecimal} value and {@link String} unit wrapped by this FHIRPathQuantityValue is equal the parameter (or its primitive value)
     *
     * @param obj
     *     the other {@link Object}
     * @return
     *     true if the {@link BigDecimal} value and {@link String} unit wrapped by this FHIRPathQuantityValue node is equal the parameter (or its primitive value), otherwise false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof FHIRPathNode)) {
            return false;
        }
        FHIRPathNode other = (FHIRPathNode) obj;
        if (!isComparableTo(other)) {
            return false;
        }
        if (other instanceof FHIRPathQuantityValue || other instanceof FHIRPathNumberValue) {
            return compareTo(other) == 0;
        }
        return compareTo(other.getValue()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, unit);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(value.toPlainString()).append(" '").append(unit).append("'");
        return sb.toString();
    }

    @Override
    public void accept(FHIRPathNodeVisitor visitor) {
        visitor.visit(this);
    }
}
