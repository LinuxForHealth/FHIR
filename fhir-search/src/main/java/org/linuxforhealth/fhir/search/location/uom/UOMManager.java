/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.search.location.uom;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.linuxforhealth.fhir.search.location.uom.standard.ImperialUnits;
import org.linuxforhealth.fhir.search.location.uom.standard.MetricUnits;
import org.linuxforhealth.fhir.search.location.uom.standard.StatuteLengthUnits;

/**
 * LocationUnit maps the unit of measure to a specific length (one meter).
 */
public final class UOMManager {
    private static final Map<String, Double> UNIT_TO_METER =
            Collections.unmodifiableMap(new HashMap<String, Double>() {

                private static final long serialVersionUID = -9812444709851684l;

                {
                    // Imperial Units
                    put(ImperialUnits.INCH.value(), calculateImperial(0.025399980000000003));
                    put(ImperialUnits.INCH_CI.value(), calculateImperial(0.025399980000000003));
                    put(ImperialUnits.FOOT.value(), calculateImperial(0.30479976000000003));
                    put(ImperialUnits.FOOT_CI.value(), calculateImperial(0.30479976000000003));
                    put(ImperialUnits.ROD.value(), calculateImperial(5.02919604));
                    put(ImperialUnits.ROD_CI.value(), calculateImperial(5.02919604));
                    put(ImperialUnits.SURVEYORS_CHAIN.value(), calculateImperial(20.11678416));
                    put(ImperialUnits.SURVEYORS_CHAIN_CI.value(), calculateImperial(20.11678416));
                    put(ImperialUnits.SURVEYORS_LINK.value(), calculateImperial(0.2011678416));
                    put(ImperialUnits.SURVEYORS_LINK_CI.value(), calculateImperial(0.2011678416));
                    put(ImperialUnits.FATHOM.value(), calculateImperial(1.82879856));
                    put(ImperialUnits.FATHOM_CI.value(), calculateImperial(1.82879856));
                    put(ImperialUnits.PACE.value(), calculateImperial(0.7619994000000001));
                    put(ImperialUnits.PACE_CI.value(), calculateImperial(0.7619994000000001));
                    put(ImperialUnits.YARD.value(), calculateImperial(0.91439928));
                    put(ImperialUnits.YARD_CI.value(), calculateImperial(0.91439928));
                    put(ImperialUnits.MILE.value(), calculateImperial(1609.3427328000002));
                    put(ImperialUnits.MILE_CI.value(), calculateImperial(1609.3427328000002));
                    put(ImperialUnits.NAUTICAL_MILE.value(), calculateImperial(1853.1825408000002));
                    put(ImperialUnits.NAUTICAL_MILE_CI.value(), calculateImperial(1853.1825408000002));

                    // US StatuteLengths Units
                    put(StatuteLengthUnits.FOOT.value(), calculateStatute(1.0));
                    put(StatuteLengthUnits.FOOT_CI.value(), calculateStatute(1.0));
                    put(StatuteLengthUnits.YARD.value(), calculateStatute(3.0));
                    put(StatuteLengthUnits.YARD_CI.value(), calculateStatute(3.0));
                    put(StatuteLengthUnits.INCH.value(), calculateStatute(0.08333333333333333));
                    put(StatuteLengthUnits.INCH_CI.value(), calculateStatute(0.08333333333333333));
                    put(StatuteLengthUnits.ROD.value(), calculateStatute(16.5));
                    put(StatuteLengthUnits.ROD_CI.value(), calculateStatute(16.5));
                    put(StatuteLengthUnits.SURVEYORS_CHAIN.value(), calculateStatute(66.0));
                    put(StatuteLengthUnits.SURVEYORS_CHAIN_CI.value(), calculateStatute(66.0));
                    put(StatuteLengthUnits.SURVEYORS_LINK.value(), calculateStatute(0.66));
                    put(StatuteLengthUnits.SURVEYORS_LINK_CI.value(), calculateStatute(0.66));
                    put(StatuteLengthUnits.ENGINEERS_CHAIN.value(), calculateStatute(100.0));
                    put(StatuteLengthUnits.ENGINEERS_CHAIN_CI.value(), calculateStatute(100.0));
                    put(StatuteLengthUnits.ENGINEERS_LINK.value(), calculateStatute(100.0));
                    put(StatuteLengthUnits.ENGINEERS_LINK_CI.value(), calculateStatute(100.0));
                    put(StatuteLengthUnits.FATHOM.value(), calculateStatute(6.0));
                    put(StatuteLengthUnits.FATHOM_CI.value(), calculateStatute(6.0));
                    put(StatuteLengthUnits.FURLONG.value(), calculateStatute(660.0));
                    put(StatuteLengthUnits.FURLONG_CI.value(), calculateStatute(660.0));
                    put(StatuteLengthUnits.MILE.value(), calculateStatute(5280.0));
                    put(StatuteLengthUnits.MILE_CI.value(), calculateStatute(5280.0));
                    put(StatuteLengthUnits.MIL.value(), calculateStatute(8.333333333333333E-5));
                    put(StatuteLengthUnits.MIL_CI.value(), calculateStatute(8.333333333333333E-5));

                    // Base Metric 
                    put(MetricUnits.YOTTAMETRE.value(), calculateMetric(24.0));
                    put(MetricUnits.YOTTAMETRE_CI.value(), calculateMetric(24.0));
                    put(MetricUnits.ZETTAMETRE.value(), calculateMetric(21.0));
                    put(MetricUnits.ZETTAMETRE_CI.value(), calculateMetric(21.0));
                    put(MetricUnits.EXAMETRE.value(), calculateMetric(18.0));
                    put(MetricUnits.EXAMETRE_CI.value(), calculateMetric(18.0));
                    put(MetricUnits.PETAMETRE.value(), calculateMetric(15.0));
                    put(MetricUnits.PETAMETRE_CI.value(), calculateMetric(15.0));
                    put(MetricUnits.TERAMETRE.value(), calculateMetric(12.0));
                    put(MetricUnits.TERAMETRE_CI.value(), calculateMetric(12.0));
                    put(MetricUnits.GIGAMETRE.value(), calculateMetric(9.0));
                    put(MetricUnits.GIGAMETRE_CI.value(), calculateMetric(9.0));
                    put(MetricUnits.MEGAMETRE.value(), calculateMetric(6.0));
                    put(MetricUnits.MEGAMETRE_CI.value(), calculateMetric(6.0));
                    put(MetricUnits.KILOMETRE.value(), calculateMetric(3.0));
                    put(MetricUnits.KILOMETRE_CI.value(), calculateMetric(3.0));
                    put(MetricUnits.HECTOMETRE.value(), calculateMetric(2.0));
                    put(MetricUnits.HECTOMETRE_CI.value(), calculateMetric(2.0));
                    put(MetricUnits.DECAMETRE.value(), calculateMetric(1.0));
                    put(MetricUnits.DECAMETRE_CM.value(), calculateMetric(1.0));
                    put(MetricUnits.METRE.value(), calculateMetric(0.0));
                    put(MetricUnits.METRE_CI.value(), calculateMetric(0.0));
                    put(MetricUnits.YOCTOMETRE.value(), calculateMetric(-24.0));
                    put(MetricUnits.YOCTOMETRE_CI.value(), calculateMetric(-24.0));
                    put(MetricUnits.ZEPTOMETRE.value(), calculateMetric(-21.0));
                    put(MetricUnits.ZEPTOMETRE_CI.value(), calculateMetric(-21.0));
                    put(MetricUnits.ATTOMETRE.value(), calculateMetric(-18.0));
                    put(MetricUnits.ATTOMETRE_CI.value(), calculateMetric(-18.0));
                    put(MetricUnits.FEMTOMETRE.value(), calculateMetric(-15.0));
                    put(MetricUnits.FEMTOMETRE_CI.value(), calculateMetric(-15.0));
                    put(MetricUnits.PICOMETRE.value(), calculateMetric(-12.0));
                    put(MetricUnits.PICOMETRE_CI.value(), calculateMetric(-12.0));
                    put(MetricUnits.NANOMETRE.value(), calculateMetric(-9.0));
                    put(MetricUnits.NANOMETRE_CI.value(), calculateMetric(-9.0));
                    put(MetricUnits.MICROMETRE.value(), calculateMetric(-6.0));
                    put(MetricUnits.MICROMETRE_CI.value(), calculateMetric(-6.0));
                    put(MetricUnits.MILLIMETRE.value(), calculateMetric(-3.0));
                    put(MetricUnits.MILLIMETRE_CI.value(), calculateMetric(-3.0));
                    put(MetricUnits.CENTIMETRE.value(), calculateMetric(-2.0));
                    put(MetricUnits.CENTIMETRE_CI.value(), calculateMetric(-2.0));
                    put(MetricUnits.DECIMETRE.value(), calculateMetric(-1.0));
                    put(MetricUnits.DECIMETRE_CI.value(), calculateMetric(-1.0));

                    // Support for Variants not in UCUM
                    put(MetricUnits.KILOMETRE_VARIANT_KMS.value(), calculateMetric(3.0));
                    put(MetricUnits.KILOMETRE_VARIANT_KILOMETER.value(), calculateMetric(3.0));
                    put(MetricUnits.KILOMETRE_VARIANT_KILOMETERS.value(), calculateMetric(3.0));
                    put(MetricUnits.KILOMETRE_VARIANT_KILOMETRE.value(), calculateMetric(3.0));
                    put(MetricUnits.KILOMETRE_VARIANT_KILOMETRES.value(), calculateMetric(3.0));
                    put(MetricUnits.KILOMETRE_VARIANT_KMS_CI.value(), calculateMetric(3.0));
                    put(MetricUnits.KILOMETRE_VARIANT_KILOMETER_CI.value(), calculateMetric(3.0));
                    put(MetricUnits.KILOMETRE_VARIANT_KILOMETERS_CI.value(), calculateMetric(3.0));
                    put(MetricUnits.KILOMETRE_VARIANT_KILOMETRE_CI.value(), calculateMetric(3.0));
                    put(MetricUnits.KILOMETRE_VARIANT_KILOMETRES_CI.value(), calculateMetric(3.0));

                    put(MetricUnits.METRE_VARIANT_MS.value(), calculateMetric(0.0));
                    put(MetricUnits.METRE_VARIANT_METER.value(), calculateMetric(0.0));
                    put(MetricUnits.METRE_VARIANT_METERS.value(), calculateMetric(0.0));
                    put(MetricUnits.METRE_VARIANT_METRE.value(), calculateMetric(0.0));
                    put(MetricUnits.METRE_VARIANT_METRES.value(), calculateMetric(0.0));
                    put(MetricUnits.METRE_VARIANT_MS_CI.value(), calculateMetric(0.0));
                    put(MetricUnits.METRE_VARIANT_METER_CI.value(), calculateMetric(0.0));
                    put(MetricUnits.METRE_VARIANT_METERS_CI.value(), calculateMetric(0.0));
                    put(MetricUnits.METRE_VARIANT_METRE_CI.value(), calculateMetric(0.0));
                    put(MetricUnits.METRE_VARIANT_METRES_CI.value(), calculateMetric(0.0));

                    put(StatuteLengthUnits.MILE_VARIANT_MI.value(), calculateStatute(5280.0));
                    put(StatuteLengthUnits.MILE_VARIANT_MIS.value(), calculateStatute(5280.0));
                    put(StatuteLengthUnits.MILE_VARIANT_MILE.value(), calculateStatute(5280.0));
                    put(StatuteLengthUnits.MILE_VARIANT_MILES.value(), calculateStatute(5280.0));
                    put(StatuteLengthUnits.MILE_VARIANT_MI_CI.value(), calculateStatute(5280.0));
                    put(StatuteLengthUnits.MILE_VARIANT_MIS_CI.value(), calculateStatute(5280.0));
                    put(StatuteLengthUnits.MILE_VARIANT_MILE_CI.value(), calculateStatute(5280.0));
                    put(StatuteLengthUnits.MILE_VARIANT_MILES_CI.value(), calculateStatute(5280.0));

                    put(StatuteLengthUnits.FOOT_VARIANT_FT.value(), calculateStatute(1.0));
                    put(StatuteLengthUnits.FOOT_VARIANT_FTS.value(), calculateStatute(1.0));
                    put(StatuteLengthUnits.FOOT_VARIANT_FOOT.value(), calculateStatute(1.0));
                    put(StatuteLengthUnits.FOOT_VARIANT_FEET.value(), calculateStatute(1.0));
                    put(StatuteLengthUnits.FOOT_VARIANT_FT_CI.value(), calculateStatute(1.0));
                    put(StatuteLengthUnits.FOOT_VARIANT_FTS_CI.value(), calculateStatute(1.0));
                    put(StatuteLengthUnits.FOOT_VARIANT_FOOT_CI.value(), calculateStatute(1.0));
                    put(StatuteLengthUnits.FOOT_VARIANT_FEET_CI.value(), calculateStatute(1.0));
                }
            });

    private UOMManager() {
        // No Operation
    }

    private static double calculateImperial(double factor) {
        return factor;
    }

    private static double calculateMetric(double factor) {
        return Math.pow(10, factor);
    }
    
    private static double calculateStatute(double factor) {
        return factor * .3048;
    }

    /**
     * based on the unit abbreviation, returns the factor
     * 
     * @param unit the string representing the unit-of-measure abbreviation. 
     * @return the factor if the factor exists, else null. 
     */
    public static final Double getUnitToMetersFactor(final String unit) {
        Double factor = UNIT_TO_METER.get(unit);
        // Supports UCUM with [mi_us] or mi_us
        if(factor == null && unit != null && 
                unit.startsWith("[") && unit.endsWith("]")) {
            factor = UNIT_TO_METER.get(unit.substring(1,unit.length() -1));
        }
        return factor;
    }
    
    /**
     * Method to convert unit into meters
     * 
     * @param unit
     * @param miles distance in miles
     * @return distance in kilometers
     */
    public static Double convertUnitsToMeters(String unit, double value) {
        Double result = getUnitToMetersFactor(unit);
        if(result != null) {
            result *= value;
        }
        return result;
    }
    
    /**
     * Method to convert unit into kilometers
     * 
     * @param unit
     * @param miles distance in miles
     * @return distance in kilometers
     */
    public static Double convertUnitsToKiloMeters(String unit, double value) {
        Double result = getUnitToMetersFactor(unit);
        if(result != null) {
            result *= value / 1000;
        }
        return result;
    }
}