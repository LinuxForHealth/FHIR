/*
 * (C) Copyright IBM Corp. 2019, 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.path;

import static org.linuxforhealth.fhir.model.type.String.string;

import java.math.BigDecimal;
import java.util.Collection;

import org.linuxforhealth.fhir.model.type.Age;
import org.linuxforhealth.fhir.model.type.Count;
import org.linuxforhealth.fhir.model.type.Decimal;
import org.linuxforhealth.fhir.model.type.Distance;
import org.linuxforhealth.fhir.model.type.Duration;
import org.linuxforhealth.fhir.model.type.MoneyQuantity;
import org.linuxforhealth.fhir.model.type.Quantity;
import org.linuxforhealth.fhir.model.type.SimpleQuantity;

/**
 * A {@link FHIRPathElementNode} that wraps a {@link Quantity}
 */
public class FHIRPathQuantityNode extends FHIRPathElementNode {
    private final Quantity quantity;
    private final String quantitySystem;
    private final String quantityCode;
    private final String quantityUnit;
    private final BigDecimal quantityValue;
    
    /**
     * Enum for all the sub types of Quantity data type
     */
    public enum QuantitySubType {
        DURATION,
        DISTANCE,
        AGE,
        COUNT,
        MONEYQUANTITY,
        SIMPLEQUANTITY
    }

    protected FHIRPathQuantityNode(Builder builder) {
        super(builder);
        quantity = builder.quantity;
        quantitySystem = getQuantitySystem(quantity);
        quantityCode = getQuantityCode(quantity);
        quantityUnit = getQuantityUnit(quantity);
        quantityValue = getQuantityValue(quantity);
    }
    
    /**
     * Method to return the QuantitySubType wrapped by this FHIRPathQuantityNode.
     * @return the QuantitySubType, if the sub type wrapped by this FHIRPathQuantityNode is valid, null otherwise.
     */
    public QuantitySubType getQuantitySubType() {
        if (quantity == null) {
            return null;
        }
        if (quantity instanceof Duration) {
            return QuantitySubType.DURATION;
        } else if (quantity instanceof Distance) {
            return QuantitySubType.DISTANCE;
        } else if (quantity instanceof Age) {
            return QuantitySubType.AGE;
        } else if (quantity instanceof Count) {
            return QuantitySubType.COUNT;
        } else if (quantity instanceof MoneyQuantity) {
            return QuantitySubType.MONEYQUANTITY;
        } else if (quantity instanceof SimpleQuantity) {
            return QuantitySubType.SIMPLEQUANTITY;
        }
        return null;
    }
    

    /**
     * The quantity wrapped by this FHIRPathQuantityNode
     *
     * @return
     *     the quantity wrapped by this FHIRPathQuantityNode
     */
    public Quantity quantity() {
        return quantity;
    }

    /**
     * The system of the quantity wrapped by this FHIRPathQuantityNode
     *
     * @return
     *     the system of the quantity wrapped by this FHIRPathQuantityNode
     */
    public String getQuantitySystem() {
        return quantitySystem;
    }

    /**
     * The code of the quantity wrapped by this FHIRPathQuantityNode
     *
     * @return
     *     the code of the quantity wrapped by this FHIRPathQuantityNode
     */
    public String getQuantityCode() {
        return quantityCode;
    }

    /**
     * The unit of the quantity wrapped by this FHIRPathQuantityNode
     *
     * @return
     *     the unit of the quantity wrapped by this FHIRPathQuantityNode
     */
    public String getQuantityUnit() {
        return quantityUnit;
    }
    
    /**
     * @implSpec This method is introduced to return the quantity sub type if a valid quantity sub type is
     *      wrapped by this FHIRPathQuantityNode. 
     *      If the type of FHIRPathQuantityNode is quantity, then return quantity type.
     *      If the type of FHIRPathQuantityNode is any of the sub type of quantity, then return the quantity sub type.
     *      If the type of FHIRPathQuantityNode is invalid then throw IllegalStateException 
     */
    @Override
    public FHIRPathType type() {
        QuantitySubType quantitySubType = getQuantitySubType();
        if (quantitySubType == null) {
            return super.type();
        }
        switch (quantitySubType) {
        case AGE:
            return FHIRPathType.FHIR_AGE;
        case COUNT:
            return FHIRPathType.FHIR_COUNT;
        case DISTANCE:
            return FHIRPathType.FHIR_DISTANCE;
        case DURATION:
            return FHIRPathType.FHIR_DURATION;
        case MONEYQUANTITY:
            return FHIRPathType.FHIR_MONEY_QUANTITY;
        case SIMPLEQUANTITY:
            return FHIRPathType.FHIR_SIMPLE_QUANTITY;
        default:
            throw new IllegalStateException("Invalid quantity sub type: " + quantitySubType);
        }
    }
    

    /**
     * The {@link BigDecimal} value of the quantity wrapped by this FHIRPathQuantityNode
     *
     * @return
     *     the {@link BigDecimal} value of the quantity wrapped by this FHIRPathQuantityNode
     */
    public BigDecimal getQuantityValue() {
        return quantityValue;
    }

    private String getQuantitySystem(Quantity quantity) {
        if (quantity.getSystem() != null) {
            return quantity.getSystem().getValue();
        }
        return null;
    }

    private String getQuantityCode(Quantity quantity) {
        if (quantity.getCode() != null) {
            return quantity.getCode().getValue();
        }
        return null;
    }

    private String getQuantityUnit(Quantity quantity) {
        if (quantity.getUnit() != null) {
            return quantity.getUnit().getValue();
        }
        return null;
    }

    private BigDecimal getQuantityValue(Quantity quantity) {
        if (quantity.getValue() != null) {
            return quantity.getValue().getValue();
        }
        return null;
    }

    @Override
    public boolean isQuantityNode() {
        return true;
    }

    /**
     * Static factory method for creating builder instances from a {@link Quantity} value
     *
     * @param quantity
     *     the {@link Quantity} value
     * @return
     *     a new builder for building FHIRPathQuantityNode instances
     */
    public static Builder builder(Quantity quantity) {
        return new Builder(FHIRPathType.FHIR_QUANTITY, quantity);
    }

    public static class Builder extends FHIRPathElementNode.Builder {
        private final Quantity quantity;

        protected Builder(FHIRPathType type, Quantity quantity) {
            super(type, quantity);
            this.quantity = quantity;
        }

        @Override
        public Builder name(java.lang.String name) {
            return (Builder) super.name(name);
        }

        @Override
        public Builder path(String path) {
            return (Builder) super.path(path);
        }

        @Override
        public Builder value(FHIRPathSystemValue value) {
            return (Builder) super.value(value);
        }

        @Override
        public Builder children(FHIRPathNode... children) {
            return (Builder) super.children(children);
        }

        @Override
        public Builder children(Collection<FHIRPathNode> children) {
            return (Builder) super.children(children);
        }

        /**
         * Build a FHIRPathQuantityNode instance using this builder
         *
         * @return
         *     a new FHIRPathQuantityNode instance
         */
        @Override
        public FHIRPathQuantityNode build() {
            return new FHIRPathQuantityNode(this);
        }
    }

    /**
     * Add this FHIRPathQuantityNode to another FHIRPathQuantityNode
     *
     * @param node
     *     the other FHIRPathQuantityNode
     * @return
     *     the result of adding this FHIRPathQuantityNode to another FHIRPathQuantityNode
     */
    public FHIRPathQuantityNode add(FHIRPathQuantityNode node) {
        Quantity quantity = Quantity.builder()
                .value(Decimal.of(getQuantityValue().add(node.getQuantityValue())))
                .unit(string(getQuantityUnit()))
                .build();
        return FHIRPathQuantityNode.builder(quantity).build();
    }

    /**
     * Subtract another FHIRPathQuantityNode from this FHIRPathQuantityNode
     *
     * @param node
     *     the other FHIRPathQuantityNode
     * @return
     *     the result of subtracting another FHIRPathQuantityNode from this FHIRPathQuantityNode
     */
    public FHIRPathQuantityNode subtract(FHIRPathQuantityNode node) {
        Quantity quantity = Quantity.builder()
                .value(Decimal.of(getQuantityValue().subtract(node.getQuantityValue())))
                .unit(string(getQuantityUnit()))
                .build();
        return FHIRPathQuantityNode.builder(quantity).build();
    }

    /**
     * Indicates whether this FHIRPathQuantityNode is comparable to the parameter
     *
     * @return
     *     true if the parameter or its primitive value is a FHIRPathQuantityNode, a {@link FHIRPathQuantityValue} or a {@FHIRPathNumberValue}, otherwise false
     */
    @Override
    public boolean isComparableTo(FHIRPathNode other) {
        if (hasValue()) {
            if (other instanceof FHIRPathQuantityValue || other.getValue() instanceof FHIRPathQuantityValue) {
                FHIRPathQuantityValue quantityValue = (other instanceof FHIRPathQuantityValue) ? (FHIRPathQuantityValue) other : (FHIRPathQuantityValue) other.getValue();
                return getValue().isComparableTo(quantityValue);
            }
            if (other instanceof FHIRPathNumberValue || other.getValue() instanceof FHIRPathNumberValue) {
                FHIRPathNumberValue numberValue = (other instanceof FHIRPathNumberValue) ? (FHIRPathNumberValue) other : (FHIRPathNumberValue) other.getValue();
                return getValue().isComparableTo(numberValue);
            }
        }
        if (other instanceof FHIRPathQuantityNode) {
            return isComparableTo((FHIRPathQuantityNode) other);
        }
        if (other instanceof FHIRPathNumberValue || other.getValue() instanceof FHIRPathNumberValue) {
            return getQuantityValue() != null;
        }
        return false;
    }

    private boolean isComparableTo(FHIRPathQuantityNode other) {
        return getQuantityValue() != null && other.getQuantityValue() != null &&
            (
                (
                    getQuantitySystem() != null && other.getQuantitySystem() != null &&
                    getQuantitySystem().equals(other.getQuantitySystem()) &&
                    getQuantityCode() != null && other.getQuantityCode() != null &&
                    getQuantityCode().equals(other.getQuantityCode())
                )
                ||
                (
                    getQuantityUnit() != null && other.getQuantityUnit() != null &&
                    getQuantityUnit().equals(other.getQuantityUnit())
                )
            );
    }

    /**
     * Compare the quantity value wrapped by this FHIRPathQuantityNode to the parameter
     *
     * @param other
     *     the other {@link FHIRPathNode}
     * @return
     *     0 if the quantity value wrapped by this FHIRPathQuantity is equal to the parameter; a positive value if this FHIRPathQuantityNode is greater than the parameter; and
     *     a negative value if this FHIRPathQuantityNode is less than the parameter
     */
    @Override
    public int compareTo(FHIRPathNode other) {
        if (!isComparableTo(other)) {
            throw new IllegalArgumentException();
        }
        if (hasValue()) {
            if (other instanceof FHIRPathQuantityValue || other.getValue() instanceof FHIRPathQuantityValue) {
                FHIRPathQuantityValue quantityValue = (other instanceof FHIRPathQuantityValue) ? (FHIRPathQuantityValue) other : (FHIRPathQuantityValue) other.getValue();
                return getValue().compareTo(quantityValue);
            }
            if (other instanceof FHIRPathNumberValue || other.getValue() instanceof FHIRPathNumberValue) {
                FHIRPathNumberValue numberValue = (other instanceof FHIRPathNumberValue) ? (FHIRPathNumberValue) other : (FHIRPathNumberValue) other.getValue();
                return getValue().compareTo(numberValue);
            }
        }
        if (other instanceof FHIRPathQuantityNode) {
            return getQuantityValue().compareTo(((FHIRPathQuantityNode) other).getQuantityValue());
        }
        FHIRPathNumberValue numberValue = (other instanceof FHIRPathNumberValue) ? (FHIRPathNumberValue) other : (FHIRPathNumberValue) other.getValue();
        return getQuantityValue().compareTo(numberValue.decimal());
    }

    /**
     * Indicates whether the quantity value wrapped by this FHIRPathQuantityNode is equal the parameter (or its primitive value)
     *
     * @param obj
     *     the other {@link Object}
     * @return
     *     true if the quantity value wrapped by this FHIRPathQuantityNode node is equal the parameter (or its primitive value), otherwise false
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
        if (hasValue()) {
            if (other instanceof FHIRPathQuantityValue || other.getValue() instanceof FHIRPathQuantityValue) {
                FHIRPathQuantityValue quantityValue = (other instanceof FHIRPathQuantityValue) ? (FHIRPathQuantityValue) other : (FHIRPathQuantityValue) other.getValue();
                return getValue().compareTo(quantityValue) == 0;
            }
            if (other instanceof FHIRPathNumberValue || other.getValue() instanceof FHIRPathNumberValue) {
                FHIRPathNumberValue numberValue = (other instanceof FHIRPathNumberValue) ? (FHIRPathNumberValue) other : (FHIRPathNumberValue) other.getValue();
                return getValue().compareTo(numberValue) == 0;
            }
        }
        if (other instanceof FHIRPathQuantityNode) {
            return getQuantityValue().compareTo(((FHIRPathQuantityNode) other).getQuantityValue()) == 0;
        }
        FHIRPathNumberValue numberValue = (other instanceof FHIRPathNumberValue) ? (FHIRPathNumberValue) other : (FHIRPathNumberValue) other.getValue();
        return getQuantityValue().compareTo(numberValue.decimal()) == 0;
    }

    @Override
    public String toString() {
        if (hasValue()) {
            return getValue().toString();
        }
        StringBuilder sb = new StringBuilder();
        BigDecimal quantityValue = getQuantityValue();
        String quantityUnit = getQuantityUnit();
        sb.append(quantityValue != null ? quantityValue.toPlainString() : "<no value>");
        sb.append(" ");
        sb.append("'");
        sb.append(quantityUnit != null ? quantityUnit : "<no unit>");
        sb.append("'");
        return sb.toString();
    }
}
