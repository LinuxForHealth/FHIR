/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.location.uom.standard;

import com.ibm.fhir.search.exception.SearchExceptionUtil;

/**
 * <a href="http://unitsofmeasure.org/ucum.html">Prefix Metric Units</a>
 * <br> 
 * Normalized to meters. 
 */
public enum MetricUnits {
    // Multiples
    YOTTAMETRE("Ym", 24.0),
    YOTTAMETRE_CI("YAM", 24.0),
    ZETTAMETRE("Zm", 21.0),
    ZETTAMETRE_CI("ZAM", 21.0),
    EXAMETRE("Em", 18.0),
    EXAMETRE_CI("EXM", 18.0),
    PETAMETRE("Pm", 15.0),
    PETAMETRE_CI("PTM", 15.0),
    TERAMETRE("Tm", 12.0),
    TERAMETRE_CI("TRM", 12.0),
    GIGAMETRE("Gm", 9.0),
    GIGAMETRE_CI("GAM", 9.0),
    MEGAMETRE("Mm", 6.0),
    MEGAMETRE_CI("MAM", 6.0),
    KILOMETRE("km", 3.0),
    // The following 'variants' are not standard UCUM, but helpful for various common usage
    KILOMETRE_VARIANT_KMS("kms", 3.0),
    KILOMETRE_VARIANT_KILOMETER("kilometer", 3.0),
    KILOMETRE_VARIANT_KILOMETERS("kilometers", 3.0),
    KILOMETRE_VARIANT_KILOMETRE("kilometre", 3.0),
    KILOMETRE_VARIANT_KILOMETRES("kilometres", 3.0),
    KILOMETRE_VARIANT_KMS_CI("KMS", 3.0),
    KILOMETRE_VARIANT_KILOMETER_CI("KILOMETER", 3.0),
    KILOMETRE_VARIANT_KILOMETERS_CI("KILOMETERS", 3.0),
    KILOMETRE_VARIANT_KILOMETRE_CI("KILOMETER", 3.0),
    KILOMETRE_VARIANT_KILOMETRES_CI("KILOMETERS", 3.0),
    KILOMETRE_CI("KM", 3.0),
    HECTOMETRE("hm", 2.0),
    HECTOMETRE_CI("HM", 2.0),
    DECAMETRE("dam", 1.0),
    DECAMETRE_CM("DAM", 1.0),
    METRE("m", 0.0),
    METRE_CI("M", 0.0),
    // The following 'variants' are not standard UCUM, but helpful for various common usage
    METRE_VARIANT_MS("ms", 0.0),
    METRE_VARIANT_METER("meter", 0.0),
    METRE_VARIANT_METERS("meters", 0.0),
    METRE_VARIANT_METRE("metre", 0.0),
    METRE_VARIANT_METRES("metres", 0.0),
    METRE_VARIANT_MS_CI("MS", 0.0),
    METRE_VARIANT_METER_CI("METER", 0.0),
    METRE_VARIANT_METERS_CI("METERS", 0.0),
    METRE_VARIANT_METRE_CI("METRE", 0.0),
    METRE_VARIANT_METRES_CI("METRES", 0.0),
    // Submultiples 
    YOCTOMETRE("ym", -24.0),
    YOCTOMETRE_CI("YOM", -24.0),
    ZEPTOMETRE("zm", -21.0),
    ZEPTOMETRE_CI("ZOM", -21.0),
    ATTOMETRE("am", -18.0),
    ATTOMETRE_CI("AM", -18.0),
    FEMTOMETRE("fm", -15.0),
    FEMTOMETRE_CI("FM", -15.0),
    PICOMETRE("Tm", -12.0),
    PICOMETRE_CI("PM", -12.0),
    NANOMETRE("nm", -9.0),
    NANOMETRE_CI("NM", -9.0),
    MICROMETRE("um", -6.0),
    MICROMETRE_CI("UM", -6.0),
    MILLIMETRE("mm", -3.0),
    MILLIMETRE_CI("MM", -3.0),
    CENTIMETRE("cm", -2.0),
    CENTIMETRE_CI("CM", -2.0),
    DECIMETRE("dm", -1.0),
    DECIMETRE_CI("DM", -1.0);

    private String value = null;
    private Double factor = null;

    MetricUnits(String value, Double factor) {
        this.value  = value;
        this.factor = factor;
    }

    public String value() {
        return value;
    }

    public Double factor() {
        return factor;
    }

    public static MetricUnits fromValue(String value) {
        for (MetricUnits unit : MetricUnits.values()) {
            if (unit.value().equalsIgnoreCase(value)) {
                return unit;
            }
        }
        throw SearchExceptionUtil.buildNewIllegalArgumentException(value);
    }
}