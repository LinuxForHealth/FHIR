/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.search.location.uom.standard;

import org.linuxforhealth.fhir.search.exception.SearchExceptionUtil;

/**
 * <a href="http://unitsofmeasure.org/ucum.html">British Imperial lengths</a>
 * <br>
 * The factors are normalized to meters.
 */
public enum ImperialUnits {
    INCH("in_br", 2.539998 / 100),
    INCH_CI("IN_BR", 2.539998 / 100),

    FOOT("ft_br", 2.539998 / 100 * 12),
    FOOT_CI("FT_BR", 2.539998 / 100 * 12),

    ROD("rd_br", 2.539998 / 100 * 12 * 16.5),
    ROD_CI("RD_BR", 2.539998 / 100 * 12 * 16.5),

    SURVEYORS_CHAIN("ch_br", 2.539998 / 100 * 12 * 16.5 * 4),
    SURVEYORS_CHAIN_CI("CH_BR", 2.539998 / 100 * 12 * 16.5 * 4),

    SURVEYORS_LINK("lk_br", 2.539998 / 100 * 12 * 16.5 * 4 / 100),
    SURVEYORS_LINK_CI("LK_BR", 2.539998 / 100 * 12 * 16.5 * 4 / 100),

    FATHOM("fth_br", 2.539998 / 100 * 12 * 6),
    FATHOM_CI("FTH_BR", 2.539998 / 100 * 12 * 6),

    PACE("pc_br", 2.539998 / 100 * 12 * 2.5),
    PACE_CI("PC_BR", 2.539998 / 100 * 12 * 2.5),
    
    YARD("yd_br", 2.539998 / 100 * 12 * 3),
    YARD_CI("YD_BR", 2.539998 / 100 * 12 * 3),

    MILE("mi_br", 2.539998 / 100 * 12 * 5280),
    MILE_CI("MI_BR", 2.539998 / 100 * 12 * 5280),

    NAUTICAL_MILE("nmi_br", 2.539998 / 100 * 12 * 6080), 
    NAUTICAL_MILE_CI("NMI_BR", 2.539998 / 100 * 12 * 6080);


    private String value = null;
    private Double factor = null;

    ImperialUnits(String value, Double factor) {
        this.value = value;
        this.factor = factor;
    }

    public String value() {
        return value;
    }
    
    public Double factor() {
        return factor;
    }

    public static ImperialUnits fromValue(String value) {
        for (ImperialUnits unit : ImperialUnits.values()) {
            if (unit.value().equalsIgnoreCase(value)) {
                return unit;
            }
        }
        throw SearchExceptionUtil.buildNewIllegalArgumentException(value);
    }
}