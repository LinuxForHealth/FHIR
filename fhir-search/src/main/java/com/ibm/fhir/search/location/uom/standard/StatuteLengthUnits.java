/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.location.uom.standard;

import com.ibm.fhir.search.exception.SearchExceptionUtil;

/**
 * <a href="http://unitsofmeasure.org/ucum.html">U.S. “survey” lengths (also called "statute" lengths) Units</a>
 * <br>
 * The factors are in FT. 
 */
public enum StatuteLengthUnits {
    FOOT("ft_us", 1.0),
    FOOT_CI("FT_US", 1.0),
    // Support for common variants that are not in UCUM
    FOOT_VARIANT_FT("ft", 1.0),
    FOOT_VARIANT_FTS("fts", 1.0),
    FOOT_VARIANT_FOOT("foot", 1.0),
    FOOT_VARIANT_FEET("feet", 1.0),
    FOOT_VARIANT_FT_CI("FT", 1.0),
    FOOT_VARIANT_FTS_CI("FTS", 1.0),
    FOOT_VARIANT_FOOT_CI("FOOT", 1.0),
    FOOT_VARIANT_FEET_CI("FEET", 1.0),

    YARD("yd_us", 3.0),
    YARD_CI("YD_US", 3.0),

    INCH("in_us", 1.0 / 12.0),
    INCH_CI("IN_US", 1.0 / 12.0),
    
    ROD("rd_us", 16.5),
    ROD_CI("RD_US", 16.5),

    SURVEYORS_CHAIN("ch_us", 4 * 16.5),
    SURVEYORS_CHAIN_CI("CH_US", 4 * 16.5),

    SURVEYORS_LINK("lk_us", 4 * 16.5 / 100.0),
    SURVEYORS_LINK_CI("LK_US", 4 * 16.5 / 100.0),

    ENGINEERS_CHAIN("rch_us", 100.0),
    ENGINEERS_CHAIN_CI("RCH_US", 100.0),

    ENGINEERS_LINK("rlk_us", 100.0),
    ENGINEERS_LINK_CI("RLK_US", 100.0),

    FATHOM("fth_us", 6.0),
    FATHOM_CI("FTH_US", 6.0),

    FURLONG("fur_us", 40.0 * 16.5),
    FURLONG_CI("FUR_US", 40.0 * 16.5),

    MILE("mi_us", 8 * 40 * 16.5),
    MILE_CI("MI_US", 8 * 40 * 16.5),
    // Support for common variants that are not in UCUM
    MILE_VARIANT_MI("mi", 8 * 40 * 16.5),
    MILE_VARIANT_MIS("mis", 8 * 40 * 16.5),
    MILE_VARIANT_MILE("mile", 8 * 40 * 16.5),
    MILE_VARIANT_MILES("miles", 8 * 40 * 16.5),
    MILE_VARIANT_MI_CI("MI", 8 * 40 * 16.5),
    MILE_VARIANT_MIS_CI("MIS", 8 * 40 * 16.5),
    MILE_VARIANT_MILE_CI("MILE", 8 * 40 * 16.5),
    MILE_VARIANT_MILES_CI("MILES", 8 * 40 * 16.5),

    MIL("mil_us", 1E-3 * 1.0 / 12.0), 
    MIL_CI("MIL_US", 1E-3 * 1.0 / 12.0);

    private String value = null;
    private Double factor = null;

    StatuteLengthUnits(String value, Double factor) {
        this.value = value;
        this.factor = factor;
    }

    public String value() {
        return value;
    }
    
    public Double factor() {
        return factor;
    }

    public static StatuteLengthUnits fromValue(String value) {
        for (StatuteLengthUnits unit : StatuteLengthUnits.values()) {
            if (unit.value().equalsIgnoreCase(value)) {
                return unit;
            }
        }
        throw SearchExceptionUtil.buildNewIllegalArgumentException(value);
    }
}