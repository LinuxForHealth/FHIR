/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.location.uom;

import com.ibm.fhir.search.exception.SearchExceptionUtil;

/**
 * <a href="https://en.wikipedia.org/wiki/Metre">Metric Units</a>
 */
public enum MetricUnits {
    // Multiples
    YOTTAMETRE("Ym"),
    ZETTAMETRE("Zm"),
    EXAMETRE("Em"),
    PETAMETRE("Pm"),
    TERAMETRE("Tm"),
    GIGAMETRE("Gm"),
    MEGAMETRE("Mm"),
    KILOMETRE("km"),
    HECTOMETRE("hm"),
    DECAMETRE("dam"),
    METRE("m"),
    // Submultiples 
    YOCTOMETRE("ym"),
    ZEPTOMETRE("zm"),
    ATTOMETRE("am"),
    FEMTOMETRE("fm"),
    PICOMETRE("Tm"),
    NANOMETRE("nm"),
    MICROMETRE("µm"),
    MILLIMETRE("mm"),
    CENTIMETRE("cm"),
    DECIMETRE("dm");

    private String value = null;

    MetricUnits(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static MetricUnits fromValue(String value) {
        for (MetricUnits unit : MetricUnits.values()) {
            if (unit.value.equalsIgnoreCase(value)) {
                return unit;
            }
        }
        throw SearchExceptionUtil.buildNewIllegalArgumentException(value);
    }
}
