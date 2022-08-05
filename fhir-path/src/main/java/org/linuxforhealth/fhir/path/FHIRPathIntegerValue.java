/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.path;

import static org.linuxforhealth.fhir.path.FHIRPathDecimalValue.decimalValue;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Objects;

import org.linuxforhealth.fhir.path.visitor.FHIRPathNodeVisitor;

/**
 * A {@link FHIRPathNumberValue} node that wraps an {@link Integer} value
 */
public class FHIRPathIntegerValue extends FHIRPathAbstractSystemValue implements FHIRPathNumberValue {
    private final Integer integer;
    private final BigDecimal decimal;

    protected FHIRPathIntegerValue(Builder builder) {
        super(builder);
        integer = builder.integer;
        // promote this integer to BigDecimal
        decimal = new BigDecimal(integer.toString());
    }

    @Override
    public boolean isIntegerValue() {
        return true;
    }

    @Override
    public BigDecimal decimal() {
        return decimal;
    }

    @Override
    public Integer integer() {
        return integer;
    }

    /**
     * Static factory method for creating FHIRPathIntegerValue instances from an {@link Integer} value
     *
     * @param integer
     *     the {@link Integer} value
     * @return
     *     a new FHIRPathIntegerValue instance
     */
    public static FHIRPathIntegerValue integerValue(Integer integer) {
        return FHIRPathIntegerValue.builder(integer).build();
    }

    /**
     * Static factory method for creating named FHIRPathIntegerValue instances from an {@link Integer} value
     *
     * @param path
     *     the path of the FHIRPathNode
     * @param name
     *     the name
     * @param integer
     *     the {@link Integer} value
     * @return
     *     a new named FHIRPathIntegerValue instance
     */
    public static FHIRPathIntegerValue integerValue(String path, String name, Integer integer) {
        return FHIRPathIntegerValue.builder(integer).name(name).path(path).build();
    }

    @Override
    public Builder toBuilder() {
        return new Builder(type, integer);
    }

    /**
     * Static factory method for creating builder instances from an {@link Integer} value
     *
     * @param decimal
     *     the {@link Integer} value
     * @return
     *     a new builder for building FHIRPathIntegerValue instances
     */
    public static Builder builder(Integer integer) {
        return new Builder(FHIRPathType.SYSTEM_INTEGER, integer);
    }

    public static class Builder extends FHIRPathAbstractSystemValue.Builder {
        private final Integer integer;

        private Builder(FHIRPathType type, Integer integer) {
            super(type);
            this.integer = integer;
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
         * Build a FHIRPathIntegerValue instance using this builder
         *
         * @return
         *     a new FHIRPathIntegerValue instance
         */
        @Override
        public FHIRPathIntegerValue build() {
            return new FHIRPathIntegerValue(this);
        }
    }

    @Override
    public FHIRPathNumberValue add(FHIRPathNumberValue value) {
        if (value.isDecimalValue()) {
            return decimalValue(decimal.add(value.decimal()));
        }
        return integerValue(integer + value.integer());
    }

    @Override
    public FHIRPathNumberValue subtract(FHIRPathNumberValue value) {
        if (value.isDecimalValue()) {
            return decimalValue(decimal.subtract(value.decimal()));
        }
        return integerValue(integer - value.integer());
    }

    @Override
    public FHIRPathNumberValue multiply(FHIRPathNumberValue value) {
        if (value.isDecimalValue()) {
            return decimalValue(decimal.multiply(value.decimal()));
        }
        return integerValue(integer * value.integer());
    }

    @Override
    public FHIRPathNumberValue divide(FHIRPathNumberValue value) {
        return decimalValue(decimal.divide(value.decimal(), MathContext.DECIMAL64));
    }

    @Override
    public FHIRPathNumberValue div(FHIRPathNumberValue value) {
        if (value.isDecimalValue()) {
            return decimalValue(decimal.divideToIntegralValue(value.decimal()));
        }
        return integerValue(integer / value.integer());
    }

    @Override
    public FHIRPathNumberValue mod(FHIRPathNumberValue value) {
        if (value.isDecimalValue()) {
            return decimalValue(decimal.remainder(value.decimal()));
        }
        return integerValue(integer % value.integer());
    }

    @Override
    public FHIRPathNumberValue negate() {
        return integerValue(-integer);
    }

    @Override
    public FHIRPathNumberValue plus() {
        return this;
    }

    /**
     * Indicates whether the integer value wrapped by this FHIRPathIntegerValue node is equal the parameter (or its primitive value)
     *
     * @param obj
     *     the other {@link Object}
     * @return
     *     true if the integer value wrapped by this FHIRPathIntegerValue node is equal the parameter (or its primitive value), otherwise false
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
        return compareTo(other) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(integer);
    }

    @Override
    public String toString() {
        return integer.toString();
    }

    @Override
    public void accept(FHIRPathNodeVisitor visitor) {
        visitor.visit(this);
    }
}
