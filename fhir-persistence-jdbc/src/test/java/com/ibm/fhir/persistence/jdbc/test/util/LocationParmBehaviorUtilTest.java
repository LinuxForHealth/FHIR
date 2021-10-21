/*
 * (C) Copyright IBM Corp. 2019,2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.util;

import static com.ibm.fhir.persistence.jdbc.JDBCConstants.PARAMETER_TABLE_ALIAS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.database.utils.query.WhereFragment;
import com.ibm.fhir.database.utils.query.expression.StringExpNodeVisitor;
import com.ibm.fhir.database.utils.query.node.BindMarkerNode;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.util.type.NewLocationParmBehaviorUtil;
import com.ibm.fhir.search.location.bounding.Bounding;
import com.ibm.fhir.search.location.bounding.BoundingBox;
import com.ibm.fhir.search.location.bounding.BoundingMissing;
import com.ibm.fhir.search.location.bounding.BoundingRadius;

public class LocationParmBehaviorUtilTest {
    private static final Logger log = java.util.logging.Logger.getLogger(LocationParmBehaviorUtilTest.class.getName());
    private static final Level LOG_LEVEL = Level.INFO;

    private void runTest(List<Object> expectedBindVariables, String expectedSql, Bounding bounding)
            throws FHIRPersistenceException {
        if (log.isLoggable(LOG_LEVEL)) {
            log.info("Expected Bind Variables -> " + expectedBindVariables);
        }
        WhereFragment actualWhereClauseSegment = new WhereFragment();

        NewLocationParmBehaviorUtil util = new NewLocationParmBehaviorUtil();
        if (bounding instanceof BoundingBox) {
            util.buildQueryForBoundingBox(actualWhereClauseSegment, (BoundingBox)bounding, PARAMETER_TABLE_ALIAS);
        } else if (bounding instanceof BoundingRadius) {
            util.buildQueryForBoundingRadius(actualWhereClauseSegment, PARAMETER_TABLE_ALIAS, (BoundingRadius)bounding);
        } else {
            util.buildQueryForBoundingMissing(actualWhereClauseSegment, (BoundingMissing)bounding);
        }
        List<BindMarkerNode> collectBindMarkersInto = new ArrayList<>();
        StringExpNodeVisitor visitor = new StringExpNodeVisitor(null, collectBindMarkersInto, false);
        final String actualWhereClauseString = actualWhereClauseSegment.getExpression().visit(visitor);

        if (log.isLoggable(LOG_LEVEL)) {
            log.info("whereClauseSegment -> " + actualWhereClauseString);
            log.info("bind variables -> " + collectBindMarkersInto.stream()
                    .map(b -> b.toValueString("~"))
                    .collect(Collectors.toList()));
        }
        assertEquals(actualWhereClauseString, expectedSql);
        assertEquals(collectBindMarkersInto.size(), expectedBindVariables.size());

        for (int i=0; i<expectedBindVariables.size(); i++) {
            Object expectedValue = expectedBindVariables.get(i);
            BindMarkerNode bindMarker = collectBindMarkersInto.get(i);

            if (!bindMarker.checkTypeAndValue(expectedValue)) {
                StringBuilder msg = new StringBuilder();
                msg.append("BIND[").append(i).append("] ")
                    .append("EXPECTED=").append(expectedValue)
                    .append("; ACTUAL=").append(bindMarker.toValueString("~"));
                fail(msg.toString());
            }
        }
    }

    private void runTestBoundingList(List<Object> expectedBindVariables, String expectedSql,
            List<Bounding> boundingAreas) throws FHIRPersistenceException {
        if (log.isLoggable(LOG_LEVEL)) {
            log.info("Expected Bind Variables -> " + expectedBindVariables);
        }
        WhereFragment actualWhereClauseSegment = new WhereFragment();

        NewLocationParmBehaviorUtil util = new NewLocationParmBehaviorUtil();
        util.buildLocationSearchQuery(actualWhereClauseSegment, boundingAreas, PARAMETER_TABLE_ALIAS);
        List<BindMarkerNode> collectBindMarkersInto = new ArrayList<>();
        StringExpNodeVisitor visitor = new StringExpNodeVisitor(null, collectBindMarkersInto, false);
        final String actualWhereClauseString = actualWhereClauseSegment.getExpression().visit(visitor);

        if (log.isLoggable(LOG_LEVEL)) {
            log.info("whereClauseSegment -> " + actualWhereClauseString);
            log.info("bind variables -> " + collectBindMarkersInto.stream()
                    .map(b -> b.toValueString("~"))
                    .collect(Collectors.toList()));
        }
        assertEquals(actualWhereClauseString, expectedSql);
        assertEquals(collectBindMarkersInto.size(), expectedBindVariables.size());

        for (int i=0; i<expectedBindVariables.size(); i++) {
            Object expectedValue = expectedBindVariables.get(i);
            BindMarkerNode bindMarker = collectBindMarkersInto.get(i);

            if (!bindMarker.checkTypeAndValue(expectedValue)) {
                StringBuilder msg = new StringBuilder();
                msg.append("BIND[").append(i).append("] ")
                    .append("EXPECTED=").append(expectedValue)
                    .append("; ACTUAL=").append(bindMarker.toValueString("~"));
                fail(msg.toString());
            }
        }
    }

    @BeforeClass
    public static void before() throws FHIRException {
        FHIRRequestContext.get().setTenantId("behavior");
    }

    @Test
    public void testBoundingBox() throws FHIRPersistenceException {
        List<Object> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(-10.0);
        expectedBindVariables.add(10.0);
        expectedBindVariables.add(-20.0);
        expectedBindVariables.add(20.0);

        String expectedSql =
                "(pX.LATITUDE_VALUE >= ? AND pX.LATITUDE_VALUE <= ? AND pX.LONGITUDE_VALUE >= ? AND pX.LONGITUDE_VALUE <= ?)";

        BoundingBox boundingBox = BoundingBox.builder()
                .maxLatitude(10.0)
                .minLatitude(-10.0)
                .maxLongitude(20.0)
                .minLongitude(-20.0)
                .build();
        runTest(expectedBindVariables, expectedSql, boundingBox);
    }

    @Test
    public void testBoundingRadius() throws FHIRPersistenceException {
        List<Object> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(Math.toRadians(10.0));
        expectedBindVariables.add(Math.toRadians(10.0));
        expectedBindVariables.add(Math.toRadians(20.0));
        expectedBindVariables.add(4.0);

        String expectedSql =
                "(ACOS(SIN(?) * SIN(RADIANS(pX.LATITUDE_VALUE)) + COS(?) * COS(RADIANS(pX.LATITUDE_VALUE)) * COS(? - RADIANS(pX.LONGITUDE_VALUE))) * 6371 <= ?)";

        BoundingRadius boundingRadius = BoundingRadius.builder().latitude(10.0).longitude(20.0).radius(4.0).build();
        runTest(expectedBindVariables, expectedSql, boundingRadius);
    }

    @Test
    public void testBoundingList() throws FHIRPersistenceException {
        BoundingRadius boundingRadius = BoundingRadius.builder().latitude(10.0).longitude(21.0).radius(4.0).build();
        BoundingBox boundingBox = BoundingBox.builder()
                .maxLatitude(11.0)
                .minLatitude(-10.0)
                .maxLongitude(20.0)
                .minLongitude(-20.0)
                .build();

        List<Bounding> boundingAreas = Arrays.asList(boundingRadius, boundingBox);

        List<Object> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(Math.toRadians(10.0));
        expectedBindVariables.add(Math.toRadians(10.0));
        expectedBindVariables.add(Math.toRadians(21.0));
        expectedBindVariables.add(new Double(4.0));
        expectedBindVariables.add(-10.0);
        expectedBindVariables.add(11.0);
        expectedBindVariables.add(-20.0);
        expectedBindVariables.add(20.0);

        String expectedSql =
                "((ACOS(SIN(?) * SIN(RADIANS(pX.LATITUDE_VALUE)) + COS(?) * COS(RADIANS(pX.LATITUDE_VALUE)) * COS(? - RADIANS(pX.LONGITUDE_VALUE))) * 6371 <= ?)"
                + " OR "
                + "(pX.LATITUDE_VALUE >= ? AND pX.LATITUDE_VALUE <= ? AND pX.LONGITUDE_VALUE >= ? AND pX.LONGITUDE_VALUE <= ?))";

        runTestBoundingList(expectedBindVariables, expectedSql, boundingAreas);
    }

    @Test
    public void testBoundingMultipleParmsList() throws FHIRPersistenceException {
        BoundingBox boundingBox1 = BoundingBox.builder()
                .maxLatitude(11.0)
                .minLatitude(-10.0)
                .maxLongitude(20.0)
                .minLongitude(-20.0)
                .build();
        boundingBox1.setInstance(0);

        BoundingBox boundingBox2 = BoundingBox.builder()
                .maxLatitude(11.0)
                .minLatitude(-10.0)
                .maxLongitude(20.0)
                .minLongitude(-20.0)
                .build();
        boundingBox2.setInstance(0);

        BoundingBox boundingBox3 = BoundingBox.builder()
                .maxLatitude(11.0)
                .minLatitude(-10.0)
                .maxLongitude(20.0)
                .minLongitude(-20.0)
                .build();
        boundingBox3.setInstance(1);

        BoundingMissing boundingBox4 = new BoundingMissing();
        boundingBox4.setInstance(2);

        List<Bounding> boundingAreas = Arrays.asList(boundingBox1, boundingBox2, boundingBox3, boundingBox4);

        List<Object> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(new Double(-10.0));
        expectedBindVariables.add(new Double(11.0));
        expectedBindVariables.add(new Double(-20.0));
        expectedBindVariables.add(new Double(20.0));

        expectedBindVariables.add(new Double(-10.0));
        expectedBindVariables.add(new Double(11.0));
        expectedBindVariables.add(new Double(-20.0));
        expectedBindVariables.add(new Double(20.0));

        expectedBindVariables.add(new Double(-10.0));
        expectedBindVariables.add(new Double(11.0));
        expectedBindVariables.add(new Double(-20.0));
        expectedBindVariables.add(new Double(20.0));

        String expectedSql =
                "((pX.LATITUDE_VALUE >= ? AND pX.LATITUDE_VALUE <= ? AND pX.LONGITUDE_VALUE >= ? AND pX.LONGITUDE_VALUE <= ?) OR "
                + "(pX.LATITUDE_VALUE >= ? AND pX.LATITUDE_VALUE <= ? AND pX.LONGITUDE_VALUE >= ? AND pX.LONGITUDE_VALUE <= ?))"
                + " AND "
                + "((pX.LATITUDE_VALUE >= ? AND pX.LATITUDE_VALUE <= ? AND pX.LONGITUDE_VALUE >= ? AND pX.LONGITUDE_VALUE <= ?))";

        runTestBoundingList(expectedBindVariables, expectedSql, boundingAreas);
    }

    // the new query builder doesn't currently support generating empty WhereFragments
    @Test(expectedExceptions = IllegalStateException.class)
    public void testBoundingMissing() throws FHIRPersistenceException {
        String expectedSql = "";
        BoundingMissing boundingMissing = new BoundingMissing();
        runTest(Collections.emptyList(), expectedSql, boundingMissing);
    }
}