/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util.type;

import static com.ibm.fhir.persistence.jdbc.JDBCConstants.BIND_VAR;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.DOT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LATITUDE_VALUE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LEFT_PAREN;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LONGITUDE_VALUE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.RIGHT_PAREN;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.PARAMETER_TABLE_ALIAS;

import java.util.List;

import com.ibm.fhir.persistence.jdbc.JDBCConstants;
import com.ibm.fhir.persistence.jdbc.JDBCConstants.JDBCOperator;
import com.ibm.fhir.search.location.bounding.Bounding;
import com.ibm.fhir.search.location.bounding.BoundingBox;
import com.ibm.fhir.search.location.bounding.BoundingRadius;

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
    public void buildLocationSearchQuery(StringBuilder whereClauseSegment, List<Object> bindVariables,
            List<Bounding> boundingAreas) {
        // Clause: AND (
        whereClauseSegment.append(JDBCOperator.AND.value())
                .append(LEFT_PAREN);

        boolean first = true;
        for (Bounding area : boundingAreas) {
            // If this is not the first, we want to make this a co-joined set of conditions.
            // ) OR (
            if (!first) {
                whereClauseSegment
                        .append(RIGHT_PAREN)
                        .append(JDBCOperator.OR.value())
                        .append(LEFT_PAREN);
            } else {
                first = false;
            }

            // Switch between the various types of bounding and queries.
            switch (area.getType()) {
            case RADIUS:
                buildQueryForBoundingRadius(whereClauseSegment, bindVariables,
                        (BoundingRadius) area);
                break;
            case BOX:
            default:
                buildQueryForBoundingBox(whereClauseSegment, bindVariables, (BoundingBox) area);
                break;
            }
        }
        whereClauseSegment.append(RIGHT_PAREN);
    }

    /**
     * build query for bounding box.
     * 
     * @param whereClauseSegment
     * @param bindVariables
     * @param boundingBox
     */
    public void buildQueryForBoundingBox(StringBuilder whereClauseSegment, List<Object> bindVariables,
            BoundingBox boundingBox) {
        // Now build the piece that compares the BoundingBox longitude and latitude values
        // to the persisted longitude and latitude parameters.
        whereClauseSegment
                .append(JDBCConstants.SPACE)
                // LAT <= ? --- LAT >= MIN_LAT
                .append(PARAMETER_TABLE_ALIAS).append(DOT).append(LATITUDE_VALUE).append(JDBCOperator.GTE.value())
                .append(BIND_VAR)
                // LAT <= ? --- LAT <= MAX_LAT
                .append(JDBCOperator.AND.value())
                .append(PARAMETER_TABLE_ALIAS).append(DOT).append(LATITUDE_VALUE).append(JDBCOperator.LTE.value())
                .append(BIND_VAR)
                // LON <= ? --- LON >= MIN_LON
                .append(JDBCOperator.AND.value())
                .append(PARAMETER_TABLE_ALIAS).append(DOT).append(LONGITUDE_VALUE).append(JDBCOperator.GTE.value())
                .append(BIND_VAR)
                // LON <= ? --- LON <= MAX_LON
                .append(JDBCOperator.AND.value())
                .append(PARAMETER_TABLE_ALIAS).append(DOT).append(LONGITUDE_VALUE).append(JDBCOperator.LTE.value())
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
                .append(JDBCConstants.SPACE)
                .append(PARAMETER_TABLE_ALIAS).append(DOT).append(LATITUDE_VALUE).append(JDBCOperator.LTE.value())
                .append(BIND_VAR)
                .append(JDBCOperator.AND.value())
                .append(PARAMETER_TABLE_ALIAS).append(DOT).append(LATITUDE_VALUE).append(JDBCOperator.GTE.value())
                .append(BIND_VAR)
                .append(JDBCOperator.AND.value())
                .append(PARAMETER_TABLE_ALIAS).append(DOT).append(LONGITUDE_VALUE).append(JDBCOperator.LTE.value())
                .append(BIND_VAR)
                .append(JDBCOperator.AND.value())
                .append(PARAMETER_TABLE_ALIAS).append(DOT).append(LONGITUDE_VALUE).append(JDBCOperator.GTE.value())
                .append(BIND_VAR)
                // Check the ARC Radius
                .append(JDBCOperator.AND.value())
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