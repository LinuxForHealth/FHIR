/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path;

/**
 * An interface that represents FHIRPath system data types
 */
public interface FHIRPathSystemValue extends FHIRPathNode {
    /**
     * @return false
     * @implSpec Always false because FHIRPathSystemValues are always leaf nodes.
     */
    @Override
    default boolean hasValue() {
        return false;
    }

    /**
     * @return null
     * @implSpec FHIRPathSystemValues are always leaf nodes and so this method is not applicable
     *          to instances of this interface. To get the primitive value, cast to a specific SystemValue
     *          type and use the appropriate getter.
     * @see #isBooleanValue()
     * @see #isStringValue()
     * @see #isQuantityValue()
     * @see #isNumberValue()
     * @see #isTemporalValue()
     * @see #asBooleanValue()
     * @see #asStringValue()
     * @see #asQuantityValue()
     * @see #asNumberValue()
     * @see #asTemporalValue()
     */
    @Override
    default FHIRPathSystemValue getValue() {
        return null;
    }

    /**
     * Always true
     */
    @Override
    default boolean isSystemValue() {
        return true;
    }

    /**
     * Indicates whether this FHIRPathSystemValue is type compatible with {@link FHIRPathBooleanValue}
     *
     * @return
     *     true if this FHIRPathSystemValue is type compatible with {@link FHIRPathBooleanValue}, otherwise false
     */
    default boolean isBooleanValue() {
        return false;
    }

    /**
     * Indicates whether this FHIRPathSystemValue is type compatible with {@link FHIRPathStringValue}
     *
     * @return
     *     true if this FHIRPathSystemValue is type compatible with {@link FHIRPathStringValue}, otherwise false
     */
    default boolean isStringValue() {
        return false;
    }

    /**
     * Indicates whether this FHIRPathSystemValue is type compatible with {@link FHIRPathQuantityValue}
     *
     * @return
     *     true if this FHIRPathSystemValue is type compatible with {@link FHIRPathQuantityValue}, otherwise false
     */
    default boolean isQuantityValue() {
        return false;
    }

    /**
     * Indicates whether this FHIRPathSystemValue is type compatible with {@link FHIRPathNumberValue}
     *
     * @return
     *     true if this FHIRPathSystemValue is type compatible with {@link FHIRPathNumberValue}, otherwise false
     */
    default boolean isNumberValue() {
        return false;
    }

    /**
     * Indicates whether this FHIRPathSystemValue is type compatible with {@link FHIRPathTemporalValue}
     *
     * @return
     *     true if this FHIRPathSystemValue is type compatible with {@link FHIRPathTemporalValue}, otherwise false
     */
    default boolean isTemporalValue() {
        return false;
    }

    /**
     * Cast this FHIRPathSystemValue to a {@link FHIRPathBooleanValue}
     *
     * @return
     *     this FHIRPathSystemValue as a {@link FHIRPathBooleanValue}
     * @throws
     *     {@link ClassCastException} if this FHIRPathSystemValue is not type compatible with {@link FHIRPathBooleanValue}
     */
    default FHIRPathBooleanValue asBooleanValue() {
        return as(FHIRPathBooleanValue.class);
    }

    /**
     * Cast this FHIRPathSystemValue to a {@link FHIRPathStringValue}
     *
     * @return
     *     this FHIRPathSystemValue as a {@link FHIRPathStringValue}
     * @throws
     *     {@link ClassCastException} if this FHIRPathSystemValue is not type compatible with {@link FHIRPathStringValue}
     */
    default FHIRPathStringValue asStringValue() {
        return as(FHIRPathStringValue.class);
    }

    /**
     * Cast this FHIRPathSystemValue to a {@link FHIRPathQuantityValue}
     *
     * @return
     *     this FHIRPathSystemValue as a {@link FHIRPathQuantityValue}
     * @throws
     *     {@link ClassCastException} if this FHIRPathSystemValue is not type compatible with {@link FHIRPathQuantityValue}
     */
    default FHIRPathQuantityValue asQuantityValue() {
        return as(FHIRPathQuantityValue.class);
    }

   /**
    * Cast this FHIRPathSystemValue to a {@link FHIRPathNumberValue}
    *
    * @return
    *     this FHIRPathSystemValue as a {@link FHIRPathNumberValue}
    * @throws
    *     {@link ClassCastException} if this FHIRPathSystemValue is not type compatible with {@link FHIRPathNumberValue}
    */
    default FHIRPathNumberValue asNumberValue() {
        return as(FHIRPathNumberValue.class);
    }

    /**
     * Cast this FHIRPathSystemValue to a {@link FHIRPathTemporalValue}
     *
     * @return
     *     this FHIRPathSystemValue as a {@link FHIRPathTemporalValue}
     * @throws
     *     {@link ClassCastException} if this FHIRPathSystemValue is not type compatible with {@link FHIRPathTemporalValue}
     */
    default FHIRPathTemporalValue asTemporalValue() {
        return as(FHIRPathTemporalValue.class);
    }
}
