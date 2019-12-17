/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.location.util;

import java.util.List;

import com.ibm.fhir.model.resource.Location;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.exception.SearchExceptionUtil;
import com.ibm.fhir.search.location.NearLocationHandler;
import com.ibm.fhir.search.parameters.QueryParameter;

/**
 * Common Location related functions.
 */
public class LocationUtil {

    private LocationUtil() {
        // No Operation
    }

    /**
     * force the latitude to a minimum
     * 
     * @param value
     * @return
     */
    public static Double checkAndLimitMaximumLatitude(Double value) {
        if (value > 90) {
            value = 90.0d;
        } else if (value < -90) {
            value = -90.0d;
        }
        return value;
    }

    /**
     * force the longitude to a minimum
     * 
     * @param value
     * @return
     */
    public static Double checkAndLimitMaximumLongitude(Double value) {
        if (value > 180.0) {
            value = 180.0d;
        } else if (value < -180.0) {
            value = -180.0d;
        }
        return value;
    }

    /**
     * Finds the index of the 'near' parameter in the passed list of search
     * parameters. If not found, -1 is returned.
     * 
     * @param searchParameters
     * @return int - The index of the 'near' parameter in the passed List.
     */
    public static int findNearParameterIndex(List<QueryParameter> searchParameters) {
        int nearParameterIndex = -1;
        for (int i = 0; i < searchParameters.size(); i++) {
            if (NearLocationHandler.NEAR.equals(searchParameters.get(i).getCode())) {
                nearParameterIndex = i;
                break;
            }
        }
        return nearParameterIndex;
    }

    /**
     * Check if it's a location and 'near'
     * <br>
     * 
     * @param resourceType
     * @param queryParm
     * @return
     */
    public static boolean isLocation(Class<?> resourceType, QueryParameter queryParm) {
        // near-distance was deprecated. 
        return Location.class.equals(resourceType) &&
                NearLocationHandler.NEAR.equals(queryParm.getCode());
    }

    /**
     * if null, return true.
     * 
     * @param val
     * @return
     */
    public static boolean checkNull(Double val) {
        return val == null;
    }

    /**
     * common constraint for valid latitude and longitude.
     * 
     * @param lat
     * @return
     */
    public static boolean checkLatValid(Double lat) {
        return lat >= -90 && lat <= 90.0;
    }

    /**
     * common constraint for valid latitude and longitude.
     * 
     * @param lon
     * @return
     */
    public static boolean checkLonValid(Double lon) {
        return lon >= -180 && lon <= 180.0;
    }

    /**
     * check if the location/area spans over the pole (either one).
     * If it does, just frankly, it's hard to do the math properly.
     * 
     * @param up
     * @param down
     * @throws FHIRSearchException
     */
    public static void checkOverUnderNinety(Double up, Double down) throws FHIRSearchException {
        if (!checkLatValid(up) || !checkLatValid(down)) {
            throw SearchExceptionUtil.buildNewInvalidSearchException("improper latitude sent - must be < 90 or > -90");
        }
    }
}