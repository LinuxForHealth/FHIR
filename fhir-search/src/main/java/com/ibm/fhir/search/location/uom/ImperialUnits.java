/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.location.uom;

import com.ibm.fhir.search.exception.SearchExceptionUtil;

/**
 * <a href="https://en.wikipedia.org/wiki/Imperial_units">Imperial Units</a>
 */
public enum ImperialUnits {
    THOU("th"),
    INCH("in"),
    FOOT("ft"),
    YARD("yd"),
    CHAIN("ch"),
    FURLONG("fur"),
    MILE("mi"),
    LEAGUE("lea"),
    FATHOM("ftm"),
    CABLE("cable"),
    NAUTICAL_MILE("nmi"), 
    LINK("link"), 
    ROD("rod");

    private String value = null;

    ImperialUnits(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static ImperialUnits fromValue(String value) {
        for (ImperialUnits unit : ImperialUnits.values()) {
            if (unit.value.equalsIgnoreCase(value)) {
                return unit;
            }
        }
        throw SearchExceptionUtil.buildNewIllegalArgumentException(value);
    }
}
