/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.location;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.location.uom.UOMManager;

public class NearLocationHandlerTest {


    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testLocationNegativeSizeBoundary() throws FHIRSearchException {
        NearLocationHandler handler = new NearLocationHandler();
 //       handler.checkValidBoundary(Double.parseDouble("-2.0"));
    }

    @Test(expectedExceptions = {})
    public void testLocationPositiveSizeBoundary() throws FHIRSearchException {
        NearLocationHandler handler = new NearLocationHandler();
  //      handler.checkValidBoundary(Double.parseDouble("2.0"));
        assertTrue(true);
    }
    
    
    @Test(expectedExceptions = {})
    public void testBoundaryGeneration() throws FHIRSearchException {
        NearLocationHandler handler = new NearLocationHandler();
 //       handler.checkValidBoundary(Double.parseDouble("2.0"));
        assertTrue(true);
    }

    @Test
    public void testLocationBs() throws FHIRSearchException {
        NearLocationHandler handler = new NearLocationHandler();
//        handler.buildBoundedBoxFromParameterValues("0.0", "0.0", "69", "mi");
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
//        handler.buildBoundedBoxFromParameterValues("0.0", "0.0", "69", "mi");
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
//        handler.buildBoundedBoxFromParameterValues("0.0", "0.0", "69", "mi");
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
        //handler.buildBoundedBoxFromParameterValues("0.0", "0.0", "69", "mi");
    }
    
    @Test
    public void test() {
        double latitude = 50.0; 
        double longitude = 0.0; 
        double distance = Math.pow(NearLocationHandler.EARTH_RADIUS_KILOMETERS,2) * Math.PI;
        System.out.println(distance);
        double minLatitude = Math.max(-90.0, latitude - (distance / NearLocationHandler.EARTH_RADIUS_KILOMETERS) * (180.0 / Math.PI));
        double maxLatitude = Math.min(90.0,latitude + (distance / NearLocationHandler.EARTH_RADIUS_KILOMETERS) * (180.0 / Math.PI));
        double minLongitude =
                longitude - ((distance / NearLocationHandler.EARTH_RADIUS_KILOMETERS) * (180.0 / Math.PI))
                        / Math.cos(latitude * (180.0 / Math.PI));
        double maxLongitude =
                longitude + ((distance / NearLocationHandler.EARTH_RADIUS_KILOMETERS) * (180.0 / Math.PI))
                        / Math.cos(latitude * (180.0 / Math.PI));
        System.out.println("             " + maxLongitude + "        ");
        System.out.println(minLatitude + "        " + maxLatitude);
        System.out.println("             " + minLongitude + "        ");
        
        
        double d = distance / NearLocationHandler.EARTH_RADIUS_KILOMETERS;
        double LAT = 2 * NearLocationHandler.EARTH_RADIUS_KILOMETERS * Math.PI;
        System.out.println(d);
    }

}