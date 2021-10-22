/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.location;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.search.SearchConstants;
import com.ibm.fhir.search.SearchConstants.Modifier;
import com.ibm.fhir.search.SearchConstants.Prefix;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.exception.SearchExceptionUtil;
import com.ibm.fhir.search.location.bounding.Bounding;
import com.ibm.fhir.search.location.bounding.BoundingBox;
import com.ibm.fhir.search.location.bounding.BoundingMissing;
import com.ibm.fhir.search.location.bounding.BoundingRadius;
import com.ibm.fhir.search.location.uom.UOMManager;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.parameters.QueryParameterValue;

/**
 * <a href="https://www.hl7.org/fhir/r4/location.html#search"> FHIR Search:
 * Special Location - Positional Searching</a>
 * <br>
 * Custom processing of the NEAR Location into Bounding Boxes or Bounding
 * Radius.
 */
public class NearLocationHandler {
    private static final String CLASSNAME = NearLocationHandler.class.getName();
    private static final Logger logger = Logger.getLogger(CLASSNAME);

    // Radix for the Earth:
    private static final double RADIUS_MERIDIAN = 6378.137;
    private static final double RADIUS_EQUATORIAL = 6356.7523;

    // Constants used in token (data type LocationPosition) searches
    public static final String LATITUDE = "-latitude";
    public static final String LONGITUDE = "-longitude";
    public static final String NEAR = "near";
    public static final double DEFAULT_DISTANCE = 5.0;
    public static final String DEFAULT_UNIT = "km";

    // Turns on the use of the bounding raidus, if it's configured.
    boolean boundingRadius = false;

    public NearLocationHandler() {
        try {
            PropertyGroup fhirConfig = FHIRConfiguration.getInstance().loadConfiguration();
            this.boundingRadius = fhirConfig.getBooleanProperty(FHIRConfiguration.PROPERTY_SEARCH_BOUNDING_AREA_RADIUS_TYPE,
                    false);
        } catch (Exception e) {
            logger.fine("Issue loading the fhir configuration, defaulting to BoundingBox");
        }
    }

    /**
     * build a bounding box given a latitude, longitude and distance.
     * <br>
     * WGS84 format
     * [latitude]|[longitude]|[distance]|[units]
     * <br>
     *
     * @param latitude
     * @param longitude
     * @param distance
     * @param unit
     * @return
     * @throws FHIRSearchException
     */
    public BoundingBox createBoundingBox(double latitude, double longitude, double distance, String unit)
            throws FHIRSearchException {
        logger.entering(NearLocationHandler.class.getName(), "createBoundingBox");

        if (logger.isLoggable(Level.FINE)) {
            logger.fine(
                    "distance:[" + distance + "] unit:[" + unit + "] latitude:[" + latitude + "] longitude:["
                            + longitude + "]");
        }
        try {
            Double convertedDistance = UOMManager.convertUnitsToKiloMeters(unit, distance);
            if (convertedDistance == null) {
                throw new FHIRSearchException(
                        "Invalid unit: '" + unit + "'. Must a UOM length.");
            }

            // Initially set to zero, until we determine the distance is greater than 0.
            double minLatitude = latitude;
            double maxLatitude = latitude;
            double minLongitude = longitude;
            double maxLongitude = longitude;

            // If distance is not zero, we're going for boxed match.
            if (distance != 0) {
                // Convert to Radians to do the ARC calculation
                // Based on https://stackoverflow.com/a/238558/1873438
                // Verified at https://www.movable-type.co.uk/scripts/latlong.html
                double latRad = Math.toRadians(latitude);
                double lonRad = Math.toRadians(longitude);

                // build bounding box points
                // The max/min ensures we don't loop infinitely over the pole, and are not taking silly.
                // latitude parameters when calculated with the distance.
                double latMin = latRad - distance / RADIUS_EQUATORIAL;
                double latMax = latRad + distance / RADIUS_EQUATORIAL;
                double lonMin = lonRad - distance / (RADIUS_MERIDIAN * Math.cos(latRad));
                double lonMax = lonRad + distance / (RADIUS_MERIDIAN * Math.cos(latRad));

                // Convert back to degrees, and minimize the box.
                minLatitude  = Math.max(-90, Math.toDegrees(latMin));
                maxLatitude  = Math.min(90, Math.toDegrees(latMax));
                minLongitude = Math.max(-180, Math.toDegrees(lonMin));
                maxLongitude = Math.min(180, Math.toDegrees(lonMax));
            }

            BoundingBox boundingBox = BoundingBox.builder()
                    .minLatitude(minLatitude)
                    .maxLatitude(maxLatitude)
                    .minLongitude(minLongitude)
                    .maxLongitude(maxLongitude)
                    .build();

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
     * @throws FHIRSearchException
     */
    public List<Bounding> generateLocationPositionsFromParameters(List<QueryParameter> queryParameters)
            throws FHIRSearchException {
        List<Bounding> boundingAreas = new ArrayList<>();
        // We are only interested in the near parameter.
        // Extract the following data elements: latitude, longitude, distance, distance unit
        int instance = 0;
        for (QueryParameter queryParm : queryParameters.stream().collect(Collectors.toList())) {
            // Only process the NEAR values IFF it's actually 'near'
            if (NEAR.equals(queryParm.getCode())) {
                Modifier modifier = queryParm.getModifier();
                if (modifier != null && Modifier.MISSING.equals(modifier)) {
                    BoundingMissing missing = new BoundingMissing();
                    missing.setInstance(instance);

                    Boolean miss = null;
                    for (QueryParameterValue value : queryParm.getValues()) {
                        if (miss != null && miss != Boolean.parseBoolean(value.getValueCode())) {
                            // user has requested both missing and not missing values for this field which makes no sense
                            logger.warning(
                                    "Processing query with conflicting values for query param with 'missing' modifier");
                        } else {
                            miss = Boolean.parseBoolean(value.getValueCode());
                        }
                    }
                    missing.setMissing(miss);
                    boundingAreas.add(missing);
                } else {
                    for (QueryParameterValue value : queryParm.getValues()) {
                        // Make sure that the prefixes are properly defined.
                        Prefix prefix = value.getPrefix();
                        if (prefix != null && Prefix.EQ.compareTo(prefix) != 0) {
                            throw new FHIRSearchException(
                                    "Only prefixes allowed for near search are [default/empty, EQ].");
                        }

                        double latitude;
                        double longitude;
                        double distance = DEFAULT_DISTANCE;
                        String unit = DEFAULT_UNIT;

                        try {
                            // [latitude]|[longitude]|[distance]|[units]
                            // -83.694810|42.256500|11.20|km
                            String[] components =
                                    value.getValueString().split(SearchConstants.BACKSLASH_NEGATIVE_LOOKBEHIND + "\\|");

                            // If less than 2, it's going to generate an NPE (caught in the outer exception).
                            latitude  = Double.parseDouble(components[0]);
                            longitude = Double.parseDouble(components[1]);

                            // Distance is included
                            if (components.length > 2) {
                                distance = Double.parseDouble(components[2]);
                            }

                            // The user has set the units value.
                            if (components.length > 3) {
                                unit = components[3];
                            }
                        } catch (NumberFormatException | NullPointerException e) {
                            throw SearchExceptionUtil
                                    .buildNewInvalidSearchException("Invalid parameters for the 'near' search");
                        }

                        // Switch between the types bounding radius and boxes.
                        Bounding bounding;
                        if (boundingRadius) {
                            bounding = createBoundingRadius(latitude, longitude, distance, unit);
                        } else {
                            bounding = createBoundingBox(latitude, longitude, distance, unit);
                        }

                        // Identifies which Query parameter
                        bounding.setInstance(instance);
                        boundingAreas.add(bounding);
                    }
                }

                // Increment the 'near'
                instance++;
            }
        }
        return boundingAreas;
    }

    /**
     * create bounding radius.
     *
     * @param latitude
     * @param longitude
     * @param distance
     * @param unit
     * @return
     * @throws FHIRSearchException
     */
    public Bounding createBoundingRadius(double latitude, double longitude, double distance, String unit)
            throws FHIRSearchException {
        logger.entering(NearLocationHandler.class.getName(), "createBoundingRadius");

        if (logger.isLoggable(Level.FINE)) {
            logger.fine(
                    "distance:[" + distance + "] unit:[" + unit + "] latitude:[" + latitude + "] longitude:["
                            + longitude + "]");
        }
        try {
            Double convertedDistance = UOMManager.convertUnitsToKiloMeters(unit, distance);
            if (convertedDistance == null) {
                throw new FHIRSearchException(
                        "Invalid unit: '" + unit + "'. Must a UOM length.");
            }

            BoundingRadius boundingRadiusObj = BoundingRadius.builder()
                    .latitude(latitude)
                    .longitude(longitude)
                    .radius(convertedDistance)
                    .build();

            if (logger.isLoggable(Level.FINE)) {
                logger.fine("distance: [" + convertedDistance + "] km, original unit: [" + unit + "]");
                logger.fine("bounding radius: [" + boundingRadiusObj + "]");
            }

            return boundingRadiusObj;
        } finally {
            logger.exiting(NearLocationHandler.class.getName(), "createBoundingRadius");
        }
    }

    /**
     * overrides the bounding functionality.
     *
     * @param boundingRadius
     */
    public void setBounding(boolean boundingRadius) {
        this.boundingRadius = boundingRadius;
    }
}