/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.location;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.Test;

import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.location.area.BoundedBox;
import com.ibm.fhir.search.location.uom.LocationUnit;

public class NearLocationHandlerTest {
    @Test
    public void testLocationUnitFound() {
        assertEquals(LocationUnit.getUnitToMetersFactor("mi"), 1609.344);
    }
    
    @Test
    public void testLocationUnitNotFound() {
        assertNull(LocationUnit.getUnitToMetersFactor("zzzzzzz"));
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testLocationNegativeSizeBoundary() throws FHIRSearchException {
        NearLocationHandler handler = new NearLocationHandler();
        handler.checkValidBoundary(Double.parseDouble("-2.0"));
    }

    @Test(expectedExceptions = {})
    public void testLocationPositiveSizeBoundary() throws FHIRSearchException {
        NearLocationHandler handler = new NearLocationHandler();
        handler.checkValidBoundary(Double.parseDouble("2.0"));
        assertTrue(true);
    }
    
    
    @Test(expectedExceptions = {})
    public void testBoundaryGeneration() throws FHIRSearchException {
        NearLocationHandler handler = new NearLocationHandler();
        handler.checkValidBoundary(Double.parseDouble("2.0"));
        assertTrue(true);
    }

    @Test
    public void testLocationBs() throws FHIRSearchException {
        NearLocationHandler handler = new NearLocationHandler();
        handler.buildBoundedBoxFromParameterValues("0.0", "0.0", "69", "mi");
    }

    /**
     * Ocean South of Gulf Of Guinea
     * 
     * @throws FHIRSearchException
     */
    @Test
    public void testZeroZero() throws FHIRSearchException {
        // Bounding Box at 0.0 , 0.0
        NearLocationHandler handler = new NearLocationHandler();
        handler.buildBoundedBoxFromParameterValues("0.0", "0.0", "69", "mi");
    }

    /**
     * The poles are the tough ones.
     * 
     * @throws FHIRSearchException
     */
    @Test
    public void testNorthPole() throws FHIRSearchException {
        // Bounding Box at 0.0 , 0.0
        NearLocationHandler handler = new NearLocationHandler();
        handler.buildBoundedBoxFromParameterValues("0.0", "0.0", "69", "mi");
    }

    /**
     * The poles are the tough ones.
     * 
     * @throws FHIRSearchException
     */
    @Test
    public void testSouthPole() throws FHIRSearchException {
        // Bounding Box at 0.0 , 0.0
        NearLocationHandler handler = new NearLocationHandler();
        handler.buildBoundedBoxFromParameterValues("0.0", "0.0", "69", "mi");
    }

}