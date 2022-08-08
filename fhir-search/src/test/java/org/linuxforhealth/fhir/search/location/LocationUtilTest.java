/*
 * (C) Copyright IBM Corp. 2019,2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.search.location;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.resource.Location;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.search.exception.FHIRSearchException;
import org.linuxforhealth.fhir.search.location.util.LocationUtil;
import org.linuxforhealth.fhir.search.parameters.QueryParameter;

public class LocationUtilTest {

    @Test
    public void testBounding() {
        assertTrue(LocationUtil.checkLatValid(10.0));
        assertTrue(LocationUtil.checkLatValid(-10.0));
    }

    @Test
    public void testBoundingLatNegativeOutOfRange() {
        assertFalse(LocationUtil.checkLatValid(-90.1));
    }

    @Test
    public void testBoundingLatPositiveOutOfRange() {
        assertFalse(LocationUtil.checkLatValid(90.1));
    }

    @Test
    public void testBoundingLonNegativeOutOfRange() {
        assertTrue(LocationUtil.checkLonValid(-90.1));
        assertTrue(LocationUtil.checkLonValid(-180.0));
        assertFalse(LocationUtil.checkLonValid(-180.1));
    }

    @Test
    public void testBoundingLonPositiveOutOfRange() {
        assertTrue(LocationUtil.checkLonValid(90.1));
        assertTrue(LocationUtil.checkLonValid(180.0));
        assertFalse(LocationUtil.checkLonValid(180.1));
    }

    @Test
    public void testBoundingNull() {
        assertTrue(LocationUtil.checkNull(null));
        assertFalse(LocationUtil.checkNull(180.0));
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testCheckOverUnderNinetyUp() throws FHIRSearchException {
        LocationUtil.checkOverUnderNinety(910.0, 900.0);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testCheckOverUnderNinetyBad() throws FHIRSearchException {
        LocationUtil.checkOverUnderNinety(-910.0, 900.0);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testCheckOverUnderNinetyDown() throws FHIRSearchException {
        LocationUtil.checkOverUnderNinety(10.0, -900.0);
    }

    @Test
    public void testCheckOverUnderNinetyUpDown() throws FHIRSearchException {
        LocationUtil.checkOverUnderNinety(10.0, 10.0);
        assertTrue(true);
    }

    @Test
    public void testIsLocation() {
        Class<?> notLocation = Patient.class;
        Class<?> location = Location.class;
        QueryParameter notNearParm = new QueryParameter(null, "not-near", null, null);
        QueryParameter nearParm = new QueryParameter(null, "near", null, null);

        assertFalse(LocationUtil.isLocation(notLocation, notNearParm));
        assertTrue(LocationUtil.isLocation(location, nearParm));
        assertFalse(LocationUtil.isLocation(location, notNearParm));
        assertFalse(LocationUtil.isLocation(notLocation, notNearParm));
    }

    @Test
    public void testFindNearParameterIndex() {
        QueryParameter notNearParm = new QueryParameter(null, "not-near", null, null);
        QueryParameter notParm = new QueryParameter(null, "not", null, null);
        QueryParameter nearParm = new QueryParameter(null, "near", null, null);
        assertEquals(LocationUtil.findNearParameterIndex(Collections.emptyList()), -1);
        assertEquals(LocationUtil.findNearParameterIndex(Arrays.asList(notNearParm)), -1);
        assertEquals(LocationUtil.findNearParameterIndex(Arrays.asList(notNearParm, notParm)), -1);
        assertEquals(LocationUtil.findNearParameterIndex(Arrays.asList(notNearParm, notParm, nearParm)), 2);
    }

    @Test
    public void testCheckAndLimitMaximumLatitude() {
        assertEquals(LocationUtil.checkAndLimitMaximumLatitude(990.0), Double.valueOf("90.0"));
        assertEquals(LocationUtil.checkAndLimitMaximumLatitude(-990.0), Double.valueOf("-90.0"));
        assertEquals(LocationUtil.checkAndLimitMaximumLatitude(-9.0), Double.valueOf("-9.0"));
        assertEquals(LocationUtil.checkAndLimitMaximumLatitude(9.0), Double.valueOf("9.0"));
    }

    @Test
    public void testCheckAndLimitMaximumLongitude() {
        assertEquals(LocationUtil.checkAndLimitMaximumLongitude(990.0), Double.valueOf("180.0"));
        assertEquals(LocationUtil.checkAndLimitMaximumLongitude(-990.0), Double.valueOf("-180.0"));
        assertEquals(LocationUtil.checkAndLimitMaximumLongitude(9.0), Double.valueOf("9.0"));
        assertEquals(LocationUtil.checkAndLimitMaximumLongitude(-9.0), Double.valueOf("-9.0"));
    }
}