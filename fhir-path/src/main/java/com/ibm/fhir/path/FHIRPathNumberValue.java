/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path;

import java.math.BigDecimal;

/**
 * An interface that represents a {@link FHIRPathSystemValue} that wraps a number value
 */
public interface FHIRPathNumberValue extends FHIRPathSystemValue {
    @Override
    default boolean isNumberValue() {
        return true;
    }

    /**
     * Indicates whether this FHIRPathNumberValue is a {@link FHIRPathDecimalValue}
     *
     * @return
     *     true if this FHIRPathNumberValue is a {@link FHIRPathDecimalValue}
     */
    default boolean isDecimalValue() {
        return false;
    }

    /**
     * Indicates whether this FHIRPathNumberValue is a {@link FHIRPathIntegerValue}
     *
     * @return
     *     true if this FHIRPathNumberValue is a {@link FHIRPathIntegerValue}
     */
    default boolean isIntegerValue() {
        return false;
    }

    /**
     * The {@link BigDecimal} value wrapped by this FHIRPathNumberValue
     *
     * @return
     *     the {@link BigDecimal} value wrapped by this FHIRPathNumberValue
     */
    BigDecimal decimal();

    /**
     * The {@link Integer} value wrapped by this FHIRPathNumberValue
     *
     * @return
     *     the {@link Integer} value wrapped by this FHIRPathNumberValue
     */
    Integer integer();

    /**
     * Cast this FHIRPathNumberValue to a {@link FHIRPathDecimalValue}
     *
     * @return
     *     this FHIRPathNumberValue as a {@link FHIRPathDecimalValue}
     * @throws
     *     {@link ClassCastException} if this FHIRPathNumberValue is not type compatible with {@link FHIRPathDecimalValue}
     */
    default FHIRPathDecimalValue asDecimalValue() {
        return as(FHIRPathDecimalValue.class);
    }

    /**
     * Cast this FHIRPathNumberValue to a {@link FHIRPathIntegerValue}
     *
     * @return
     *     this FHIRPathNumberValue as a {@link FHIRPathIntegerValue}
     * @throws
     *     {@link ClassCastException} if this FHIRPathNumberValue is not type compatible with {@link FHIRPathIntegerValue}
     */
    default FHIRPathIntegerValue asIntegerValue() {
        return as(FHIRPathIntegerValue.class);
    }

    /**
     * The {@link Number} value wrapped by this FHIRPathNumberValue
     *
     * @return
     *     the {@link Number} value wrapped by this FHIRPathNumberValue
     */
    default Number number() {
        if (isDecimalValue()) {
            return asDecimalValue().decimal();
        }
        return asIntegerValue().integer();
    }

    /**
     * Add this FHIRPathNumberValue to another FHIRPathNumber value
     *
     * @param value
     *     the other FHIRPathNumber value
     * @return
     *     the result of adding this FHIRPathNumberValue to another FHIRPathNumberValue
     */
    FHIRPathNumberValue add(FHIRPathNumberValue value);

    /**
     * Subtract another FHIRPathNumberValue from this FHIRPathNumberValue
     *
     * @param value
     *     the other FHIRPathNumberValue
     * @return
     *     the result of subtracting another FHIRPathNumberValue from this FHIRPathNumberValue
     */
    FHIRPathNumberValue subtract(FHIRPathNumberValue value);

    /**
     * Multiply this FHIRPathNumberValue by another FHIRPathNumberValue
     *
     * @param value
     *     the other FHIRPathNumberValue
     * @return
     *     the result of multiplying this FHIRPathNumberValue by another FHIRPathNumberValue
     */
    FHIRPathNumberValue multiply(FHIRPathNumberValue value);

    /**
     * Divide this FHIRPathNumberValue by another FHIRPathNumberValue
     *
     * @param value
     *     the other FHIRPathNumberValue
     * @return
     *     the result of dividing this FHIRPathNumberValue by another FHIRPathNumberValue
     */
    FHIRPathNumberValue divide(FHIRPathNumberValue value);

    /**
     * Divide this FHIRPathNumberValue by another FHIRPathNumberValue and convert the result to an integer
     *
     * @param value
     *     the other FHIRPathNumberValue
     * @return
     *     the result of dividing this FHIRPathNumberValue by another FHIRPathNumberValue and converting the result to an integer
     */
    FHIRPathNumberValue div(FHIRPathNumberValue value);

    /**
     * Compute the remainder resulting from the integer division of this FHIRPathNumberValue by another FHIRPathNumberValue
     *
     * @param value
     *     the other FHIRPathNumberValue
     * @return
     *     the integer remainder resulting from the division of this FHIRPathNumberValue by another FHIRPathNumberValue
     */
    FHIRPathNumberValue mod(FHIRPathNumberValue value);

    /**
     * Negate this FHIRPathNumberValue
     *
     * @return
     *     the result of negating this FHIRPathNumber
     */
    FHIRPathNumberValue negate();

    /**
     * The inverse of negate (for symmetry)
     *
     * @return
     *     this FHIRPathNumberValue
     */
    FHIRPathNumberValue plus();

    /**
     * Indicates whether this FHIRPathNumberValue is comparable to the parameter
     *
     * @return
     *     true if the parameter or its primitive value is a FHIRPathNumberValue, {@link FHIRPathQuantityNode} or {@link FHIRPathQuantityValue} otherwise false
     */
    @Override
    default boolean isComparableTo(FHIRPathNode other) {
        if (other instanceof FHIRPathQuantityNode) {
            FHIRPathQuantityNode quantityNode = (FHIRPathQuantityNode) other;
            return quantityNode.hasValue() || quantityNode.getQuantityValue() != null;
        }
        return other instanceof FHIRPathQuantityValue ||
                other instanceof FHIRPathNumberValue ||
                other.getValue() instanceof FHIRPathNumberValue;
    }

    /**
     * Compare the number value wrapped by this FHIRPathNumberValue node to the parameter
     *
     * @param other
     *     the other {@link FHIRPathNode}
     * @return
     *     0 if the number value wrapped by this FHIRPathNumberValue node is equal to the parameter; a positive value if this FHIRPathNumberValue is greater than the parameter; and
     *     a negative value if this FHIRPathDateValue is less than the parameter
     */
    @Override
    default int compareTo(FHIRPathNode other) {
        if (!isComparableTo(other)) {
            throw new IllegalArgumentException();
        }
        if (other instanceof FHIRPathQuantityNode) {
            FHIRPathQuantityNode quantityNode = (FHIRPathQuantityNode) other;
            if (quantityNode.hasValue()) {
                FHIRPathQuantityValue quantityValue = (FHIRPathQuantityValue) quantityNode.getValue();
                return decimal().compareTo(quantityValue.value());
            }
            return decimal().compareTo(quantityNode.getQuantityValue());
        }
        if (other instanceof FHIRPathQuantityValue) {
            return decimal().compareTo(((FHIRPathQuantityValue) other).value());
        }
        FHIRPathNumberValue value = (other instanceof FHIRPathNumberValue) ? (FHIRPathNumberValue) other : (FHIRPathNumberValue) other.getValue();
        return decimal().compareTo(value.decimal());
    }
}
