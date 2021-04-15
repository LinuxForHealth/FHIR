/*
 * (C) Copyright IBM Corp. 2019,2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util.type;

import static com.ibm.fhir.database.utils.query.expression.ExpressionUtils.bind;
import static com.ibm.fhir.database.utils.query.expression.ExpressionUtils.col;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LATITUDE_VALUE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LONGITUDE_VALUE;

import java.util.List;
import java.util.stream.Collectors;

import com.ibm.fhir.database.utils.query.WhereFragment;
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

    public NewLocationParmBehaviorUtil() {
        // No Operation
    }

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
            for (int i = 0; i < processed; i++) {
                whereClauseSegment.rightParen();
            }
        }
    }

    public void buildQueryForBoundingMissing(WhereFragment whereClauseSegment,
            BoundingMissing missingArea) {
        // No Operation - the main logic is contained in the process Missing parameter
    }

    /**
     * build query for bounding box.
     *
     * @param whereClauseSegment
     * @param bindVariables
     * @param boundingBox
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
     * @param bindVariables
     * @param boundingBox
     */
    public void buildQueryForBoundingRadius(WhereFragment whereClauseSegment, String paramAlias,
            BoundingRadius boundingRadius) {
        // This section of code is based on code from http://janmatuschek.de/LatitudeLongitudeBoundingCoordinates
        // ACOS(SIN(boundingRadiusLatitude) * SIN(LATITUDE_VALUE) + COS(boundingRadiusLatitude) * COS(LATITUDE_VALUE) * COS(LONGITUDE_VALUE)
        WhereFragment arcRadius = new WhereFragment();
        arcRadius
            .sin(bind(boundingRadius.getLatitude()))
            .mult()
            .sin(col(paramAlias, LATITUDE_VALUE))
            .add()
            .cos(bind(boundingRadius.getLatitude()))
            .mult()
            .cos(col(paramAlias, LATITUDE_VALUE))
            .mult()
            .acos(col(paramAlias, LONGITUDE_VALUE));

        whereClauseSegment.leftParen()
        .col(paramAlias, LATITUDE_VALUE).lte().bind(boundingRadius.getLatitude())
        .and()
        .col(paramAlias, LATITUDE_VALUE).gte().bind(boundingRadius.getLatitude())
        .and()
        .col(paramAlias, LONGITUDE_VALUE).lte().bind(boundingRadius.getLongitude())
        .and()
        .col(paramAlias, LONGITUDE_VALUE).gte().bind(boundingRadius.getLongitude())
        // Check the ARC Radius
        .and().acos(arcRadius.getExpression()).lte().bind(boundingRadius.getRadius());
        whereClauseSegment.rightParen();
    }
}