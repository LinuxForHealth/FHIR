/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.location;

import org.testng.annotations.Test;

public class BoundaryHandlerTest {
    @Test
    public void test() {
        double latitude = 50.0;
        double longitude = 110.0;

        double distance = 10000;
        System.out.println(distance);
        double minLatitude =
                Math.max(-90.0,
                        latitude - (distance / NearLocationHandler.EARTH_RADIUS_KILOMETERS) * (180.0 / Math.PI));
        double maxLatitude =
                Math.min(90.0, latitude + (distance / NearLocationHandler.EARTH_RADIUS_KILOMETERS) * (180.0 / Math.PI));
        double minLongitude =
                longitude - ((distance / NearLocationHandler.EARTH_RADIUS_KILOMETERS) * (180.0 / Math.PI))
                        / Math.cos(latitude * (180.0 / Math.PI));
        double maxLongitude =
                longitude + ((distance / NearLocationHandler.EARTH_RADIUS_KILOMETERS) * (180.0 / Math.PI))
                        / Math.cos(latitude * (180.0 / Math.PI));

        printLatLon(minLatitude, maxLatitude, minLongitude, maxLongitude);

        double d = distance / NearLocationHandler.EARTH_RADIUS_KILOMETERS;
        double LAT = 2 * NearLocationHandler.EARTH_RADIUS_KILOMETERS * Math.PI;
        System.out.println(d);
    }

    public void buildValidCoordinate() {
        double latitude = 50.0;
        double longitude = 110.0;
        double distance = 100;
        
        double minLatitude = 0.0;
        double maxLatitude = 0.0;
        double minLongitude = 0.0;
        double maxLongitude = 0.0;
        
        
        

        printLatLon(minLatitude, maxLatitude, minLongitude, maxLongitude);
    }

    public Double latitude(double latitude, double distance) {
        return latitude - (distance);
    }

    public void printLatLon(Double minLatitude, Double maxLatitude, Double minLongitude, Double maxLongitude) {
        System.out.println("-----------------------------");
        System.out.println("      N[" + Math.round(maxLatitude) + "]        ");
        System.out.println("W[" + Math.round(maxLongitude) + "]       E[" + Math.round(minLongitude) + "]");
        System.out.println("      S[" + Math.round(minLatitude) + "]        ");
        System.out.println("------------------------------");
    }
}