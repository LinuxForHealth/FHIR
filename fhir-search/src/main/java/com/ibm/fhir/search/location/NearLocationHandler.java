/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.location;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.exception.SearchExceptionUtil;
import com.ibm.fhir.search.location.area.BoundedBox;
import com.ibm.fhir.search.location.uom.LocationUnit;
import com.ibm.fhir.search.util.SearchUtil;

public class NearLocationHandler {
    private static final String CLASSNAME = NearLocationHandler.class.getName();
    private static final Logger logger = Logger.getLogger(CLASSNAME);

    // Circumference of the earth in miles / 360. 
    private static final Double LONGITUDE_FACTOR = LocationUnit.getUnitToMetersFactor("mi") * 24901 / 360;
    // North to South Length is split <a href="https://www.timeanddate.com/worldclock/distances.html?n=468">Distance To</a>
    private static final Double LATITUDE_FACTOR = LocationUnit.getUnitToMetersFactor("mi") * 12430 / 180;

    public NearLocationHandler() {
        // No Operation
    }

    /**
     * generate the bounded boxes used in the query.
     * 
     * @param longitude double value between -90 and 90
     * @param latitude  double value between 180 and -190
     * @param size      a double value that should be found on Earth.
     * @param uom       the unit of measure which aligns to the LocationUnit
     *                  abbreviation
     * @return
     * @throws FHIRSearchException
     */
    public List<BoundedBox> buildBoundedBoxFromParameterValues(String longitude, String latitude, String size,
            String uom)
            throws FHIRSearchException {
        // Convert the longitude, latitude. 
        Double lon = Double.parseDouble(longitude);
        Double lat = Double.parseDouble(latitude);

        // Divide the size in half and convert size and UOM
        // The result is a normalizedBoundary in meters. 
        Double boundary = Double.parseDouble(size) / 2.0;
        checkValidBoundary(boundary);
        Double factor = LocationUnit.getUnitToMetersFactor(uom);
        Double normalizedBoundary = boundary * factor;

        // At this point, there are 1,2 or 4 BoundedBox. There are never 3, 0 or more than 4. 
        return buildBoundedBoxFromMetres(lat, lon, normalizedBoundary);
    }

    /**
     * build boxes
     * 
     * @param lat
     * @param lon
     * @param normalizedBoundary normalized to meters (unit-of-measure)
     * @throws FHIRSearchException
     */
    public List<BoundedBox> buildBoundedBoxFromMetres(Double lat, Double lon, Double boundary)
            throws FHIRSearchException {
        List<BoundedBox> boxes = new ArrayList<>();

        //        Double left, Double right, Double up, Double down

        // Convert the normalized boundary into meridians and limit to a maximum
        Double lonBox = checkAndLimitMaximumLongitude(boundary / LONGITUDE_FACTOR);
        Double latBox = checkAndLimitMaximumLatitude(boundary / LATITUDE_FACTOR);

        // if greater than 180, it must be greater than 90, and therefore the maximum box. 
        // Round to maximum 180 or 90
        Double left = lonBox + lon;
        Double right = lon - lonBox;
        Double up = latBox + lat;
        Double down = lat - latBox;

        // The math gets very complicated when the area is expanded beyond 90/-90.
        checkOverUnderNinety(up, down);

        // Check to see if the Boundary Exceeds what we want. 
        // If the area is the whole earth, we're just going to go ahead and use -180 to 180, -90 to -90
        // if less than zero,  then use negative branch
        // else use the positive branch 
        // round if greater to the value. 

        if (left < 0.0 && right > 0.0) {
            // We know it's spanned OVER the origin. 
            System.out.println("SPLIT VERITCAL");
        } else if (left > 90) {

        } else if (right > 90) {
            System.out.println("RIGHT");
        }

        if (down < 0.0 && up > 0.0) {
            // Split Horizontal
            System.out.println("HORIZONTAL");
        }

        //Math.signum(boundary) != Math.signum(original)
        return boxes;
    }

    /*
     * =============================================================================
     * = The following are checks for valid conditions =============================
     * =============================================================================
     */

    /**
     * check if the location/area spans over the pole (either one).
     * If it does, just frankly, it's hard to do the math properly.
     * 
     * @param up
     * @param down
     * @throws FHIRSearchException
     */
    public void checkOverUnderNinety(Double up, Double down) throws FHIRSearchException {
        if (up >= 90.0 || down <= -90.0) {
            throw SearchExceptionUtil.buildNewInvalidSearchException("negative value sent as size");
        }
    }

    /**
     * force the latitude to a minimum
     * 
     * @param value
     * @return
     */
    public Double checkAndLimitMaximumLatitude(Double value) {
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
    public Double checkAndLimitMaximumLongitude(Double value) {
        if (value > 180.0) {
            value = 180.0d;
        } else if (value < -180.0) {
            value = -180.0d;
        }
        return value;
    }

    /**
     * checks if the value is valid
     * 
     * @param val
     * @throws FHIRSearchException
     */
    public void checkValidBoundary(Double val) throws FHIRSearchException {
        if (Math.signum(val) < 0) {
            throw SearchExceptionUtil.buildNewInvalidSearchException("negative value sent as size");
        }
    }
}