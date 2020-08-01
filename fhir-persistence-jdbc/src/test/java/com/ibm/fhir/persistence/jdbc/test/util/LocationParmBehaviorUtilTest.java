/*
 * (C) Copyright IBM Corp. 2019,2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.util;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.JDBCConstants;
import com.ibm.fhir.persistence.jdbc.util.type.LocationParmBehaviorUtil;
import com.ibm.fhir.search.location.bounding.Bounding;
import com.ibm.fhir.search.location.bounding.BoundingBox;
import com.ibm.fhir.search.location.bounding.BoundingMissing;
import com.ibm.fhir.search.location.bounding.BoundingRadius;

public class LocationParmBehaviorUtilTest {
    private static final Logger log = java.util.logging.Logger.getLogger(LocationParmBehaviorUtilTest.class.getName());
    private static final Level LOG_LEVEL = Level.FINE;

    private void runTestBoundingBox(List<Object> expectedBindVariables, String expectedSql,
            BoundingBox boundingBox)
            throws FHIRPersistenceException {
        StringBuilder actualWhereClauseSegment = new StringBuilder();
        List<Object> actualBindVariables = new ArrayList<>();

        LocationParmBehaviorUtil util = new LocationParmBehaviorUtil();
        util.buildQueryForBoundingBox(actualWhereClauseSegment, actualBindVariables, boundingBox, JDBCConstants.PARAMETER_TABLE_ALIAS);

        if (log.isLoggable(LOG_LEVEL)) {
            log.info("whereClauseSegment -> " + actualWhereClauseSegment.toString());
            log.info("bind variables -> " + actualBindVariables);
        }
        assertEquals(actualWhereClauseSegment.toString(), expectedSql);

        for (Object o : expectedBindVariables) {
            actualBindVariables.remove(o);
        }

        if (log.isLoggable(LOG_LEVEL)) {
            log.info("leftover - bind variables -> " + actualBindVariables);
        }
        assertEquals(actualBindVariables.size(), 0);
    }

    private void runTestBoundingRadius(List<Object> expectedBindVariables, String expectedSql,
            BoundingRadius boundingRadius)
            throws FHIRPersistenceException {
        StringBuilder actualWhereClauseSegment = new StringBuilder();
        List<Object> actualBindVariables = new ArrayList<>();

        LocationParmBehaviorUtil util = new LocationParmBehaviorUtil();
        util.buildQueryForBoundingRadius(actualWhereClauseSegment, actualBindVariables, boundingRadius);

        if (log.isLoggable(LOG_LEVEL)) {
            log.info("whereClauseSegment -> " + actualWhereClauseSegment.toString());
            log.info("bind variables -> " + actualBindVariables);
        }
        assertEquals(actualWhereClauseSegment.toString(), expectedSql);

        for (Object o : expectedBindVariables) {
            actualBindVariables.remove(o);
        }

        if (log.isLoggable(LOG_LEVEL)) {
            log.info("leftover - bind variables -> " + actualBindVariables);
        }
        assertEquals(actualBindVariables.size(), 0);
    }

    private void runTestBoundingMissing(String expectedSql, BoundingMissing boundingMissing)
            throws FHIRPersistenceException {
        StringBuilder actualWhereClauseSegment = new StringBuilder();

        StringBuilder actualParameterClauseSegment = new StringBuilder();
        actualParameterClauseSegment.append("(P1.PARAMETER_NAME_ID = x AND");

        LocationParmBehaviorUtil util = new LocationParmBehaviorUtil();
        util.buildQueryForBoundingMissing(actualParameterClauseSegment.toString(), actualWhereClauseSegment,
                boundingMissing);

        if (log.isLoggable(LOG_LEVEL)) {
            log.info("whereClauseSegment -> " + actualWhereClauseSegment.toString());
        }
        assertEquals(actualWhereClauseSegment.toString(), expectedSql);
    }

    private void runTestBoundingList(List<Object> expectedBindVariables, String expectedSql,
            List<Bounding> boundingAreas)
            throws FHIRPersistenceException {
        StringBuilder actualWhereClauseSegment = new StringBuilder();
        List<Object> actualBindVariables = new ArrayList<>();

        StringBuilder actualParameterClauseSegment = new StringBuilder();
        actualParameterClauseSegment.append("(P1.PARAMETER_NAME_ID = x AND");

        LocationParmBehaviorUtil util = new LocationParmBehaviorUtil();
        util.buildLocationSearchQuery(actualParameterClauseSegment.toString(), actualWhereClauseSegment,
                actualBindVariables, boundingAreas, JDBCConstants.PARAMETER_TABLE_ALIAS);

        if (log.isLoggable(LOG_LEVEL)) {
            log.info("whereClauseSegment -> " + actualWhereClauseSegment.toString());
            log.info("bind variables -> " + actualBindVariables);
        }
        assertEquals(actualWhereClauseSegment.toString(), expectedSql);

        for (Object o : expectedBindVariables) {
            actualBindVariables.remove(o);
        }

        if (log.isLoggable(LOG_LEVEL)) {
            log.info("leftover - bind variables -> " + actualBindVariables);
        }
        assertEquals(actualBindVariables.size(), 0);
    }

    @BeforeClass
    public static void before() throws FHIRException {
        FHIRRequestContext.get().setTenantId("behavior");
    }

    @Test(expectedExceptions = {})
    public void testBoundingBox() throws FHIRPersistenceException {
        List<Object> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(20.0);
        expectedBindVariables.add(-20.0);
        expectedBindVariables.add(10.0);
        expectedBindVariables.add(-10.0);

        String expectedSql =
                "(pX.LATITUDE_VALUE >= ? AND pX.LATITUDE_VALUE <= ? AND pX.LONGITUDE_VALUE >= ? AND pX.LONGITUDE_VALUE <= ?)";

        BoundingBox boundingBox =
                BoundingBox.builder().maxLatitude(10.0).minLatitude(-10.0).maxLongitude(20.0).minLongitude(-20.0)
                        .build();
        runTestBoundingBox(expectedBindVariables, expectedSql, boundingBox);
    }

    @Test(expectedExceptions = {})
    public void testBoundingRadius() throws FHIRPersistenceException {
        List<Object> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(10.0);
        expectedBindVariables.add(10.0);
        expectedBindVariables.add(20.0);
        expectedBindVariables.add(20.0);
        expectedBindVariables.add(10.0);
        expectedBindVariables.add(10.0);
        expectedBindVariables.add(4.0);

        String expectedSql =
                "(pX.LATITUDE_VALUE <= ? AND pX.LATITUDE_VALUE >= ? AND pX.LONGITUDE_VALUE <= ? AND pX.LONGITUDE_VALUE >= ? AND ACOS(SIN(?) * SIN(pX.LATITUDE_VALUE) + COS(?) * COS(pX.LATITUDE_VALUE) * COS(pX.LONGITUDE_VALUE))<= ?";

        BoundingRadius boundingRadius = BoundingRadius.builder().latitude(10.0).longitude(20.0).radius(4.0).build();
        runTestBoundingRadius(expectedBindVariables, expectedSql, boundingRadius);
    }

    @Test
    public void testBoundingList() throws FHIRPersistenceException {
        BoundingRadius boundingRadius = BoundingRadius.builder().latitude(10.0).longitude(21.0).radius(4.0).build();
        BoundingBox boundingBox =
                BoundingBox.builder().maxLatitude(11.0).minLatitude(-10.0).maxLongitude(20.0).minLongitude(-20.0)
                        .build();

        List<Bounding> boundingAreas = Arrays.asList(boundingRadius, boundingBox);

        List<Object> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(new Double(10.0));
        expectedBindVariables.add(new Double(10.0));
        expectedBindVariables.add(new Double(21.0));
        expectedBindVariables.add(new Double(21.0));
        expectedBindVariables.add(new Double(10.0));
        expectedBindVariables.add(new Double(10.0));
        expectedBindVariables.add(new Double(4.0));
        expectedBindVariables.add(new Double(20.0));
        expectedBindVariables.add(new Double(-20.0));
        expectedBindVariables.add(new Double(11.0));
        expectedBindVariables.add(new Double(-10.0));

        String expectedSql =
                "(P1.PARAMETER_NAME_ID = x AND AND  (pX.LATITUDE_VALUE <= ? AND pX.LATITUDE_VALUE >= ? AND pX.LONGITUDE_VALUE <= ? AND pX.LONGITUDE_VALUE >= ? AND ACOS(SIN(?) * SIN(pX.LATITUDE_VALUE) + COS(?) * COS(pX.LATITUDE_VALUE) * COS(pX.LONGITUDE_VALUE))<= ? OR (pX.LATITUDE_VALUE >= ? AND pX.LATITUDE_VALUE <= ? AND pX.LONGITUDE_VALUE >= ? AND pX.LONGITUDE_VALUE <= ?))";

        runTestBoundingList(expectedBindVariables, expectedSql, boundingAreas);
    }

    @Test
    public void testBoundingMultipleParmsList() throws FHIRPersistenceException {
        BoundingBox boundingBox1 =
                BoundingBox.builder().maxLatitude(11.0).minLatitude(-10.0).maxLongitude(20.0).minLongitude(-20.0)
                        .build();
        boundingBox1.setInstance(0);
        BoundingBox boundingBox2 =
                BoundingBox.builder().maxLatitude(11.0).minLatitude(-10.0).maxLongitude(20.0).minLongitude(-20.0)
                        .build();
        boundingBox2.setInstance(0);
        BoundingBox boundingBox3 =
                BoundingBox.builder().maxLatitude(11.0).minLatitude(-10.0).maxLongitude(20.0).minLongitude(-20.0)
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
                "(P1.PARAMETER_NAME_ID = x AND AND  (pX.LATITUDE_VALUE >= ? AND pX.LATITUDE_VALUE <= ? AND pX.LONGITUDE_VALUE >= ? AND pX.LONGITUDE_VALUE <= ?) OR (pX.LATITUDE_VALUE >= ? AND pX.LATITUDE_VALUE <= ? AND pX.LONGITUDE_VALUE >= ? AND pX.LONGITUDE_VALUE <= ?)) AND ((P1.PARAMETER_NAME_ID = x AND AND  (pX.LATITUDE_VALUE >= ? AND pX.LATITUDE_VALUE <= ? AND pX.LONGITUDE_VALUE >= ? AND pX.LONGITUDE_VALUE <= ?)))";

        runTestBoundingList(expectedBindVariables, expectedSql, boundingAreas);
    }

    @Test(expectedExceptions = {})
    public void testBoundingMissing() throws FHIRPersistenceException {
        String expectedSql = "";
        BoundingMissing boundingMissing = new BoundingMissing();
        runTestBoundingMissing(expectedSql, boundingMissing);
    }
}