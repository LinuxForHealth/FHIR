/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path;

import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;

/**
 * A {@link FHIRPathSystemValue} that wraps a temporal value
 */
public interface FHIRPathTemporalValue extends FHIRPathSystemValue {
    @Override
    default boolean isTemporalValue() {
        return true;
    }

    /**
     * Indicates whether this FHIRPathTemporalValue is type compatible with {@link FHIRPathDateValue}
     *
     * @return
     *     true if this FHIRPathTemporalValue is type compatible with {@link FHIRPathDateValue}, otherwise false
     */
    default boolean isDateValue() {
        return false;
    }

    /**
     * Indicates whether this FHIRPathTemporalValue is type compatible with {@link FHIRPathDateTimeValue}
     *
     * @return
     *     true if this FHIRPathTemporalValue is type compatible with {@link FHIRPathDateTimeValue}, otherwise false
     */
    default boolean isDateTimeValue() {
        return false;
    }

    /**
     * Indicates whether this FHIRPathTemporalValue is type compatible with {@link FHIRPathTimeValue}
     *
     * @return
     *     true if this FHIRPathTemporalValue is type compatible with {@link FHIRPathDateValue}, otherwise false
     */
    default boolean isTimeValue() {
        return false;
    }

    /**
     * The {@link TemporalAcessor} value wrapped by this FHIRPathTemporalValue
     *
     * @return
     *     the {@link TemporalAccessor} value wrapped by this FHIRPathTemporalValue
     */
    TemporalAccessor temporalAccessor();

    /**
     * The {@link Temporal} value wrapped by this FHIRPathTemporalValue
     *
     * @return
     *     the {@link Temporal} value wrapped by this FHIRPathTemporalValue
     */
    Temporal temporal();

    /**
     * The precision of this FHIRPathTemporalValue
     *
     * @return
     *     the precision of this FHIRPathTemporalValue
     */
    ChronoField precision();

    /**
     * The text that this FHIRPathTemporalValue was parsed from (if applicable)
     *
     * @return
     *     the text that this FHIRPathTemporvalValue was parsed from (if applicable), otherwise null
     */
    String getText();

    /**
     * Indicates whether the specified field is supported by this FHIRPathTemporalValue
     *
     * @param field
     *     the field
     * @return
     *     true if the specified field is supported by this FHIRPathTemporalValue, otherwise false
     */
    boolean isSupported(ChronoField field);

    /**
     * Indicates whether the date/time value wrapped by this FHIRPathTemporalValue node is partial
     *
     * @return
     *     true if the date/time value wrapped by this FHIRPathTemporalValue node is partial, otherwise false
     */
    boolean isPartial();

    /**
     * Cast this FHIRPathTemporalValue to a {@link FHIRPathDateValue}
     *
     * @return
     *     this FHIRPathTemporalValue as a {@link FHIRPathDateValue}
     * @throws
     *     {@link ClassCastException} if this FHIRPathTemporalValue is not type compatible with {@link FHIRPathDateValue}
     */
    default FHIRPathDateValue asDateValue() {
        return as(FHIRPathDateValue.class);
    }

    /**
     * Cast this FHIRPathTemporalValue to a {@link FHIRPathDateTimeValue}
     *
     * @return
     *     this FHIRPathTemporalValue as a {@link FHIRPathDateTimeValue}
     * @throws
     *     {@link ClassCastException} if this FHIRPathTemporalValue is not type compatible with {@link FHIRPathDateTimeValue}
     */
    default FHIRPathDateTimeValue asDateTimeValue() {
        return as(FHIRPathDateTimeValue.class);
    }

    /**
     * Cast this FHIRPathTemporalValue to a {@link FHIRPathTimeValue}
     *
     * @return
     *     this FHIRPathTemporalValue as a {@link FHIRPathTimeValue}
     * @throws
     *     {@link ClassCastException} if this FHIRPathTemporalValue is not type compatible with {@link FHIRPathTimeValue}
     */
    default FHIRPathTimeValue asTimeValue() {
        return as(FHIRPathTimeValue.class);
    }

    /**
     * Add a quantity value to this FHIRPathTemporalValue
     *
     * @param quantityValue
     *     the quantity value to add
     * @return
     *     the result of adding a quantityValue to this FHIRPathTemporalValue
     */
    FHIRPathTemporalValue add(FHIRPathQuantityValue quantityValue);

    /**
     * Subtract a quantity value from this FHIRPathTemporalValue
     *
     * @param quantityValue
     *     the quantity value to subtract
     * @return
     *     the result of subtracting a quantityValue from this FHIRPathTemporalValue
     */
    FHIRPathTemporalValue subtract(FHIRPathQuantityValue quantityValue);
}
