/*
 * (C) Copyright IBM Corp. 2019,2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util.type;

import static com.ibm.fhir.persistence.jdbc.JDBCConstants.AND;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.BIND_VAR;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.DOT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.GTE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LATITUDE_VALUE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LEFT_PAREN;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LONGITUDE_VALUE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LTE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.OR;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.PARAMETER_TABLE_ALIAS;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.RIGHT_PAREN;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.SPACE;

import java.util.List;
import java.util.stream.Collectors;

import com.ibm.fhir.search.location.bounding.Bounding;
import com.ibm.fhir.search.location.bounding.BoundingBox;
import com.ibm.fhir.search.location.bounding.BoundingMissing;
import com.ibm.fhir.search.location.bounding.BoundingRadius;
import com.ibm.fhir.search.location.bounding.BoundingType;

/**
 * Location Behavior Util generates SQL and loads the variables into bind
 * variables.
 */
public class LocationParmBehaviorUtil {

    public LocationParmBehaviorUtil() {
        // No Operation
    }

    /**
     * build location search query based on the bounding areas.
     * 
     * @param whereClauseSegment
     * @param bindVariables
     * @param boundingAreas
     */
    public void buildLocationSearchQuery(String populateNameIdSubSegment, StringBuilder whereClauseSegment,
            List<Object> bindVariables, List<Bounding> boundingAreas, String paramTableAlias) {
        int instance = 0;

        boolean first = true;
        int processed = 0;
        // Strips out the MISSING bounds. 
        for (Bounding area : boundingAreas.stream()
                .filter(area -> !BoundingType.MISSING.equals(area.getType())).collect(Collectors.toList())) {
            if (instance == area.instance()) {
                processed++;
                if (instance > 0) {
                    whereClauseSegment.append(RIGHT_PAREN)
                            .append(AND)
                            .append(LEFT_PAREN);
                }

                // Build this piece of the segment:
                // (P1.PARAMETER_NAME_ID = x AND (
                whereClauseSegment
                        .append(populateNameIdSubSegment).append(AND).append(SPACE);
                instance++;
                first = true;
            }

            if (!first) {
                // If this is not the first, we want to make this a co-joined set of conditions.
                // ) OR (
                whereClauseSegment.append(OR);
            } else {
                first = false;
            }

            // Switch between the various types of bounding and queries.
            switch (area.getType()) {
            case RADIUS:
                buildQueryForBoundingRadius(whereClauseSegment, bindVariables,
                        (BoundingRadius) area);
                break;
            case MISSING:
                buildQueryForBoundingMissing(populateNameIdSubSegment, whereClauseSegment, (BoundingMissing) area);
                break;
            case BOX:
            default:
                buildQueryForBoundingBox(whereClauseSegment, bindVariables, (BoundingBox) area, paramTableAlias);
                break;
            }
        }

        if (processed > 0) {
            for (int i = 0; i < processed; i++) {
                whereClauseSegment.append(RIGHT_PAREN);
            }
        }
    }

    public void buildQueryForBoundingMissing(String populateNameIdSubSegment, StringBuilder whereClauseSegment,
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
    public void buildQueryForBoundingBox(StringBuilder whereClauseSegment, List<Object> bindVariables,
            BoundingBox boundingBox, String paramTableAlias) {
        // Now build the piece that compares the BoundingBox longitude and latitude values
        // to the persisted longitude and latitude parameters.
        whereClauseSegment
                .append(LEFT_PAREN)
                // LAT <= ? --- LAT >= MIN_LAT
                .append(paramTableAlias).append(DOT).append(LATITUDE_VALUE).append(GTE)
                .append(BIND_VAR)
                // LAT <= ? --- LAT <= MAX_LAT
                .append(AND)
                .append(paramTableAlias).append(DOT).append(LATITUDE_VALUE).append(LTE)
                .append(BIND_VAR)
                // LON <= ? --- LON >= MIN_LON
                .append(AND)
                .append(paramTableAlias).append(DOT).append(LONGITUDE_VALUE).append(GTE)
                .append(BIND_VAR)
                // LON <= ? --- LON <= MAX_LON
                .append(AND)
                .append(paramTableAlias).append(DOT).append(LONGITUDE_VALUE).append(LTE)
                .append(BIND_VAR)
                .append(RIGHT_PAREN);

        // The following order is important. 
        bindVariables.add(boundingBox.getMinLatitude());
        bindVariables.add(boundingBox.getMaxLatitude());
        bindVariables.add(boundingBox.getMinLongitude());
        bindVariables.add(boundingBox.getMaxLongitude());
    }

    /**
     * build query for bounding radius.
     * 
     * @param whereClauseSegment
     * @param bindVariables
     * @param boundingBox
     */
    public void buildQueryForBoundingRadius(StringBuilder whereClauseSegment, List<Object> bindVariables,
            BoundingRadius boundingRadius) {
        // This section of code is based on code from http://janmatuschek.de/LatitudeLongitudeBoundingCoordinates
        whereClauseSegment
                .append(LEFT_PAREN)
                .append(PARAMETER_TABLE_ALIAS).append(DOT).append(LATITUDE_VALUE).append(LTE)
                .append(BIND_VAR)
                .append(AND)
                .append(PARAMETER_TABLE_ALIAS).append(DOT).append(LATITUDE_VALUE).append(GTE)
                .append(BIND_VAR)
                .append(AND)
                .append(PARAMETER_TABLE_ALIAS).append(DOT).append(LONGITUDE_VALUE).append(LTE)
                .append(BIND_VAR)
                .append(AND)
                .append(PARAMETER_TABLE_ALIAS).append(DOT).append(LONGITUDE_VALUE).append(GTE)
                .append(BIND_VAR)
                // Check the ARC Radius
                .append(AND)
                .append("ACOS(SIN(?) * SIN(")
                .append(PARAMETER_TABLE_ALIAS).append(DOT).append(LATITUDE_VALUE)
                .append(") + COS(?) * COS(")
                .append(PARAMETER_TABLE_ALIAS).append(DOT).append(LATITUDE_VALUE)
                .append(") * COS(")
                .append(PARAMETER_TABLE_ALIAS).append(DOT).append(LONGITUDE_VALUE)
                .append(RIGHT_PAREN)
                .append(RIGHT_PAREN)
                .append("<= ?");

        // The following order is important.
        bindVariables.add(boundingRadius.getLatitude());
        bindVariables.add(boundingRadius.getLatitude());
        bindVariables.add(boundingRadius.getLongitude());
        bindVariables.add(boundingRadius.getLongitude());
        bindVariables.add(boundingRadius.getLatitude());
        bindVariables.add(boundingRadius.getLatitude());
        bindVariables.add(boundingRadius.getRadius());
    }
}