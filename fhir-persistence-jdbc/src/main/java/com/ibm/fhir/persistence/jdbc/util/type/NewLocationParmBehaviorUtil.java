/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util.type;

import static com.ibm.fhir.database.utils.query.expression.ExpressionSupport.bind;
import static com.ibm.fhir.database.utils.query.expression.ExpressionSupport.col;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LATITUDE_VALUE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LONGITUDE_VALUE;

import java.util.List;
import java.util.stream.Collectors;

import com.ibm.fhir.database.utils.query.WhereFragment;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.location.NearLocationHandler;
import com.ibm.fhir.search.location.bounding.Bounding;
import com.ibm.fhir.search.location.bounding.BoundingBox;
import com.ibm.fhir.search.location.bounding.BoundingMissing;
import com.ibm.fhir.search.location.bounding.BoundingRadius;
import com.ibm.fhir.search.location.bounding.BoundingType;

/**
 * Location Behavior Util generates SQL and loads the variables into bind
 * variables.
 */
public class NewLocationParmBehaviorUtil {
    // the radius of the earth in km (using a spherical approximation)
    private static int AVERAGE_RADIUS_OF_EARTH = 6371;

    /**
     * build location search query based on the bounding areas.
     *
     * @param whereClauseSegment
     * @param bindVariables
     * @param boundingAreas
     */
    public void buildLocationSearchQuery(WhereFragment whereClauseSegment,
            List<Bounding> boundingAreas, String paramTableAlias) {
        int instance = 0;

        boolean first = true;
        int processed = 0;
        // Strips out the MISSING bounds.
        for (Bounding area : boundingAreas.stream()
                .filter(area -> !BoundingType.MISSING.equals(area.getType())).collect(Collectors.toList())) {
            if (instance == area.instance()) {
                processed++;
                if (instance > 0) {
                    whereClauseSegment.rightParen().and().leftParen();
                } else {
                    whereClauseSegment.leftParen();
                }

                instance++;
                first = true;
            }

            if (!first) {
                // If this is not the first, we want to make this a co-joined set of conditions.
                whereClauseSegment.or();
            } else {
                first = false;
            }

            // Switch between the various types of bounding and queries.
            switch (area.getType()) {
            case RADIUS:
                buildQueryForBoundingRadius(whereClauseSegment, paramTableAlias, (BoundingRadius)area);
                break;
            case MISSING:
                buildQueryForBoundingMissing(whereClauseSegment, (BoundingMissing) area);
                break;
            case BOX:
            default:
                buildQueryForBoundingBox(whereClauseSegment, (BoundingBox) area, paramTableAlias);
                break;
            }
        }

        if (processed > 0) {
            whereClauseSegment.rightParen();
        }
    }

    /**
     * Process missing area. No longer performed here, but in another missing
     * clause.
     * @param whereClauseSegment
     * @param missingArea
     */
    @Deprecated
    public void buildQueryForBoundingMissing(WhereFragment whereClauseSegment,
            BoundingMissing missingArea) {
        // No Operation - the main logic is contained in the process Missing parameter
    }

    /**
     * build query for bounding box.
     *
     * @param whereClauseSegment
     * @param boundingBox
     * @param paramTableAlias
     */
    public void buildQueryForBoundingBox(WhereFragment whereClauseSegment,
            BoundingBox boundingBox, String paramTableAlias) {

        // Now build the piece that compares the BoundingBox longitude and latitude values
        // to the persisted longitude and latitude parameters.
        whereClauseSegment.leftParen()
            // LAT <= ? --- LAT >= MIN_LAT
            .col(paramTableAlias, LATITUDE_VALUE).gte().bind(boundingBox.getMinLatitude())
            // LAT <= ? --- LAT <= MAX_LAT
            .and()
            .col(paramTableAlias, LATITUDE_VALUE).lte().bind(boundingBox.getMaxLatitude())
            // LON <= ? --- LON >= MIN_LON
            .and()
            .col(paramTableAlias, LONGITUDE_VALUE).gte().bind(boundingBox.getMinLongitude())
            // LON <= ? --- LON <= MAX_LON
            .and()
            .col(paramTableAlias, LONGITUDE_VALUE).lte().bind(boundingBox.getMaxLongitude())
            .rightParen();
    }

    /**
     * build query for bounding radius.
     *
     * @param whereClauseSegment
     * @param paramAlias
     * @param boundingBox
     */
    public void buildQueryForBoundingRadius(WhereFragment whereClauseSegment, String paramAlias,
            BoundingRadius boundingRadius) {

        // First, emit our bounding box query to scope the points down to just those in the general vicinity
        BoundingBox boundingBox;
        try {
            boundingBox = new NearLocationHandler()
                    .createBoundingBox(boundingRadius.getLatitude(), boundingRadius.getLongitude(), boundingRadius.getRadius(), "km");
        } catch (FHIRSearchException e) {
            // createBoundingBox throws if the units are invalid, but we've already normalized them to km by this point
            throw new IllegalStateException("Unexpected exception while computing the bounding box for radius search", e);
        }
        buildQueryForBoundingBox(whereClauseSegment, boundingBox, paramAlias);
        whereClauseSegment.and();

        // Then further refine it by having the db calculate the distance bewtween the locations
        // This section of code is based on code from http://janmatuschek.de/LatitudeLongitudeBoundingCoordinates
        // ACOS(SIN(boundingRadiusLatitude) * SIN(LATITUDE_VALUE) + COS(boundingRadiusLatitude) * COS(LATITUDE_VALUE) * COS(boundingRadiusLongitude - LONGITUDE_VALUE) * R

        double queryLatitudeInRadians = Math.toRadians(boundingRadius.getLatitude());
        double queryLongitudeInRadians = Math.toRadians(boundingRadius.getLongitude());

        // First, build the fragments for the longitudinal difference (in radians) and the radian of the latitude in the db
        WhereFragment longitudeDiff = new WhereFragment().bind(queryLongitudeInRadians).sub().radians(col(paramAlias, LONGITUDE_VALUE));
        WhereFragment radianLatitude = new WhereFragment().radians(col(paramAlias, LATITUDE_VALUE));

        // Then, use those to build the expression that gets passed to ACOS
        WhereFragment arcRadius = new WhereFragment()
            .sin(bind(queryLatitudeInRadians))
            .mult()
            .sin(radianLatitude.getExpression())
            .add()
            .cos(bind(queryLatitudeInRadians))
            .mult()
            .cos(radianLatitude.getExpression())
            .mult()
            .cos(longitudeDiff.getExpression());

        // Finally, put it all together
        whereClauseSegment.leftParen()
            .acos(arcRadius.getExpression()).mult().literal(AVERAGE_RADIUS_OF_EARTH)
            .lte()
            .bind(boundingRadius.getRadius())
            .rightParen();
    }
}