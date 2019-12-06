/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.location;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.search.location.bounding.BoundingBox;
import com.ibm.fhir.search.location.uom.UOMManager;
import com.ibm.fhir.search.parameters.Parameter;
import com.ibm.fhir.search.parameters.ParameterValue;

/** 
 * 
 * 
 */
public class NearLocationHandler {
    private static final String CLASSNAME = NearLocationHandler.class.getName();
    private static final Logger logger = Logger.getLogger(CLASSNAME);

    public static final double EARTH_RADIUS_KILOMETERS = 6371.0; // earth radius in kilometers

    // Constants used in token (data type LocationPosition) searches
    public static final String LATITUDE = "-latitude";
    public static final String LONGITUDE = "-longitude";
    public static final String NEAR = "near";
    public static final double DEFAULT_DISTANCE = 5.0;
    public static final String DEFAULT_UNIT = "km";

    public NearLocationHandler() {
        // No Operation
    }

    /**
     * Method to build a bounding box given a latitude, longitude and distance
     * 
     * @param latitude
     * @param longitude
     * @param distance
     * @param unit
     * @return
     */
    public BoundingBox createBoundingBox(double latitude, double longitude, double distance, String unit) {
        logger.entering(NearLocationHandler.class.getName(), "createBoundingBox");

        if (logger.isLoggable(Level.FINE)) {
            logger.fine(
                    "distance   :" + distance + ":unit:" + unit + ":latitude :" + latitude + ":longitude:" + longitude);
        }
        try {
            Double convertedDistance = UOMManager.convertUnitsToKiloMeters(unit, distance);
            if (convertedDistance == null) {
                throw new IllegalArgumentException(
                        "Invalid unit: '" + unit + "'. Must a UOM length.");
            }

            // Since we're centering the box, we divide in half.
            double box = distance / 2;

            // build bounding box points
            // The max/min ensures we don't loop infinitely over the pole, and are not taking silly. 
            // latitude parameters when calculated with the distance.

            double minLatitude = Math.max(-90.0, latitude - (box / EARTH_RADIUS_KILOMETERS) * (180.0 / Math.PI));
            double maxLatitude = Math.min(90.0, latitude + (box / EARTH_RADIUS_KILOMETERS) * (180.0 / Math.PI));
            double minLongitude =
                    longitude - ((box / EARTH_RADIUS_KILOMETERS) * (180.0 / Math.PI))
                            / Math.cos(latitude * (180.0 / Math.PI));
            double maxLongitude =
                    longitude + ((box / EARTH_RADIUS_KILOMETERS) * (180.0 / Math.PI))
                            / Math.cos(latitude * (180.0 / Math.PI));

            BoundingBox boundingBox =
                    BoundingBox.builder().minLatitude(minLatitude).maxLatitude(maxLatitude).minLongitude(minLongitude)
                            .maxLongitude(maxLongitude).build();

            if (logger.isLoggable(Level.FINE)) {
                logger.fine("distance: [" + convertedDistance + "] km, original unit: [" + unit + "]");
                logger.fine("bounding box: [" + boundingBox + "]");
            }

            return boundingBox;
        } finally {
            logger.exiting(NearLocationHandler.class.getName(), "createBoundingBox");
        }
    }

    /**
     * generates location positions for processing from parameters.
     * 
     * @param queryParameters
     * @return
     */
    public List<BoundingBox> generateLocationPositionsFromParameters(List<Parameter> queryParameters) {
        List<BoundingBox> boundingBoxes = new ArrayList<>();
        // We are only interested in the near and near-distance parameters.
        // Extract the following data elements: latitude, longitude, distance, distance unit
        Iterator<Parameter> queryParms = queryParameters.iterator();
        while (queryParms.hasNext()) {
            Parameter queryParm = queryParms.next();
            for (ParameterValue value : queryParm.getValues()) {
                if (NEAR.equals(queryParm.getCode())) {
                    double latitude = 0.0;
                    double longitude = 0.0;
                    double distance = DEFAULT_DISTANCE;
                    String unit = DEFAULT_UNIT;
                    if (value.getValueSystem() != null) {

                        latitude = Double.parseDouble(value.getValueSystem());
                    }
                    if (value.getValueCode() != null) {
                        longitude = Double.parseDouble(value.getValueCode());
                    }
                    boundingBoxes.add(createBoundingBox(latitude, longitude, distance, unit));
                }
            }
        }
        return boundingBoxes;
    }
}