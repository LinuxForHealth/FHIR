/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.location;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.ibm.fhir.search.location.uom.ImperialUnits;
import com.ibm.fhir.search.location.uom.LocationUnit;
import com.ibm.fhir.search.location.uom.MetricUnits;
import com.ibm.fhir.search.location.uom.OtherUnits;

public class LocationUnitTest {

    @Test
    public void testLocations() {
        assertEquals(LocationUnit.getUnitToMetersFactor("km"), 1000.0);
    }
    
    @Test
    public void testImperialUnits() {
        assertEquals(ImperialUnits.fromValue("ft"), ImperialUnits.FOOT);
    }
    
    
    @Test(expectedExceptions = {IllegalArgumentException.class} )
    public void testImperialUnitsExceptions() {
        assertEquals(ImperialUnits.fromValue("funny"), ImperialUnits.FOOT);
    }
    
    @Test
    public void testOtherUnits() {
        assertEquals(OtherUnits.fromValue("Å"), OtherUnits.ANGSTROM);
    }
    
    @Test(expectedExceptions = {IllegalArgumentException.class} )
    public void testOtherUnitsExceptions() {
        assertEquals(OtherUnits.fromValue("FRED"), OtherUnits.ANGSTROM);
    }
    
    @Test
    public void testMetricUnits() {
        assertEquals(MetricUnits.fromValue("Mm"), MetricUnits.MEGAMETRE);
    }
    
    @Test(expectedExceptions = {IllegalArgumentException.class} )
    public void testMetricUnitsExceptions() {
        assertEquals(MetricUnits.fromValue("FRED"), MetricUnits.MEGAMETRE);
    }

}