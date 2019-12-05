/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.location.uom;

import com.ibm.fhir.search.exception.SearchExceptionUtil;

/**
 * <a href="https://en.wikipedia.org/wiki/Angstrom">ANGSTROM</a>
 */
public enum OtherUnits {
    ANGSTROM("Å");

    private String value = null;

    OtherUnits(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static OtherUnits fromValue(String value) {
        for (OtherUnits unit : OtherUnits.values()) {
            if (unit.value.equalsIgnoreCase(value)) {
                return unit;
            }
        }
        throw SearchExceptionUtil.buildNewIllegalArgumentException(value);
    }
}
