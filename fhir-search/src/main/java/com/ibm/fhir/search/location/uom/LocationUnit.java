/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.location.uom;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * LocationUnit maps the unit of measure to a specific length (one meter).
 */
public final class LocationUnit {
    private static final Map<String, Double> UNIT_TO_METER =
            Collections.unmodifiableMap(new HashMap<String, Double>() {

                private static final long serialVersionUID = -9812444709851684l;

                {
                    // Imperial Units
                    put(ImperialUnits.THOU.value(), calculateImperial(0.0000254d));
                    put(ImperialUnits.INCH.value(), calculateImperial(0.0254d));
                    put(ImperialUnits.FOOT.value(), calculateImperial(0.3048d));
                    put(ImperialUnits.YARD.value(), calculateImperial(0.9144d));
                    put(ImperialUnits.CHAIN.value(), calculateImperial(20.1168d));
                    put(ImperialUnits.FURLONG.value(), calculateImperial(201.168d));
                    put(ImperialUnits.MILE.value(), calculateImperial(1609.344d));
                    put(ImperialUnits.LEAGUE.value(), calculateImperial(4828.032d));
                    put(ImperialUnits.FATHOM.value(), calculateImperial(1.852d));
                    put(ImperialUnits.CABLE.value(), calculateImperial(185.2d));
                    put(ImperialUnits.NAUTICAL_MILE.value(), calculateImperial(0.0000254d));
                    put(ImperialUnits.LINK.value(), calculateImperial(0.201168d));
                    put(ImperialUnits.ROD.value(), calculateImperial(5.0292d));

                    // Base Metric Unit
                    put(MetricUnits.METRE.value(), calculateMetric(0));

                    // Metric Submultiple Unit
                    put(MetricUnits.DECIMETRE.value(), calculateMetric(-1));
                    put(MetricUnits.CENTIMETRE.value(), calculateMetric(-2));
                    put(MetricUnits.MILLIMETRE.value(), calculateMetric(-3));
                    put(MetricUnits.MICROMETRE.value(), calculateMetric(-6));
                    put(MetricUnits.NANOMETRE.value(), calculateMetric(-9));
                    put(MetricUnits.PICOMETRE.value(), calculateMetric(-12));
                    put(MetricUnits.FEMTOMETRE.value(), calculateMetric(-15));
                    put(MetricUnits.ATTOMETRE.value(), calculateMetric(-18));
                    put(MetricUnits.ZEPTOMETRE.value(), calculateMetric(-21));
                    put(MetricUnits.YOCTOMETRE.value(), calculateMetric(-24));

                    // Metric Multiple Unit
                    put(MetricUnits.DECAMETRE.value(), calculateMetric(1));
                    put(MetricUnits.HECTOMETRE.value(), calculateMetric(2));
                    put(MetricUnits.KILOMETRE.value(), calculateMetric(3));
                    put(MetricUnits.MEGAMETRE.value(), calculateMetric(6));
                    put(MetricUnits.GIGAMETRE.value(), calculateMetric(9));
                    put(MetricUnits.TERAMETRE.value(), calculateMetric(12));
                    put(MetricUnits.PETAMETRE.value(), calculateMetric(15));
                    put(MetricUnits.EXAMETRE.value(), calculateMetric(18));
                    put(MetricUnits.ZETTAMETRE.value(), calculateMetric(21));
                    put(MetricUnits.YOTTAMETRE.value(), calculateMetric(24));

                    // Other Units (10^-10 metres)
                    put(OtherUnits.ANGSTROM.value(), calculateMetric(-10));
                }
            });

    private LocationUnit() {
        // No Operation
    }

    private static double calculateImperial(double factor) {
        return factor * calculateMetric(0);
    }

    private static double calculateMetric(int factor) {
        return Math.pow(10, factor);
    }

    /**
     * based on the unit abbreviation, returns the factor 
     * @param unit
     * @return
     */
    public static final Double getUnitToMetersFactor(String unit) {
        return UNIT_TO_METER.get(unit);
    }
}