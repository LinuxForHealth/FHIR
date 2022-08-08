/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.test.util;

import static org.linuxforhealth.fhir.persistence.jdbc.JDBCConstants.PARAMETER_TABLE_ALIAS;
import static org.linuxforhealth.fhir.persistence.jdbc.test.util.ParmBehaviorUtilTestHelper.assertExpectedSQL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.database.utils.query.WhereFragment;
import org.linuxforhealth.fhir.exception.FHIRException;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.persistence.jdbc.util.type.NewLocationParmBehaviorUtil;
import org.linuxforhealth.fhir.search.exception.FHIRSearchException;
import org.linuxforhealth.fhir.search.location.NearLocationHandler;
import org.linuxforhealth.fhir.search.location.bounding.Bounding;
import org.linuxforhealth.fhir.search.location.bounding.BoundingBox;
import org.linuxforhealth.fhir.search.location.bounding.BoundingMissing;
import org.linuxforhealth.fhir.search.location.bounding.BoundingRadius;

public class LocationParmBehaviorUtilTest {
    //---------------------------------------------------------------------------------------------------------
    // Supporting Methods:
    @BeforeClass
    public void before() throws FHIRException {
        FHIRRequestContext.get().setTenantId("behavior");
    }

    @AfterClass
    public void after() throws FHIRException {
        FHIRRequestContext.get().setTenantId("default");
    }

    private void runTest(List<Object> expectedBindVariables, String expectedSql, Bounding bounding)
            throws FHIRPersistenceException {
        WhereFragment actualWhereClauseSegment = new WhereFragment();

        NewLocationParmBehaviorUtil util = new NewLocationParmBehaviorUtil();
        if (bounding instanceof BoundingBox) {
            util.buildQueryForBoundingBox(actualWhereClauseSegment, (BoundingBox)bounding, PARAMETER_TABLE_ALIAS);
        } else if (bounding instanceof BoundingRadius) {
            util.buildQueryForBoundingRadius(actualWhereClauseSegment, PARAMETER_TABLE_ALIAS, (BoundingRadius)bounding);
        } else {
            util.buildQueryForBoundingMissing(actualWhereClauseSegment, (BoundingMissing)bounding);
        }
        assertExpectedSQL(actualWhereClauseSegment, expectedSql, expectedBindVariables);
    }

    private void runTestBoundingList(List<Object> expectedBindVariables, String expectedSql,
            List<Bounding> boundingAreas) throws FHIRPersistenceException {
        WhereFragment actualWhereClauseSegment = new WhereFragment();
        NewLocationParmBehaviorUtil util = new NewLocationParmBehaviorUtil();
        util.buildLocationSearchQuery(actualWhereClauseSegment, boundingAreas, PARAMETER_TABLE_ALIAS);
        assertExpectedSQL(actualWhereClauseSegment, expectedSql, expectedBindVariables);
    }
    //---------------------------------------------------------------------------------------------------------

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
                .minLatitude(-10.0)
                .maxLatitude(10.0)
                .minLongitude(-20.0)
                .maxLongitude(20.0)
                .build();
        runTest(expectedBindVariables, expectedSql, boundingBox);
    }

    @Test
    public void testBoundingRadius() throws FHIRPersistenceException, FHIRSearchException {
        BoundingBox boundingBoxForRadius = new NearLocationHandler().createBoundingBox(10, 20, 4, "km");

        List<Object> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(boundingBoxForRadius.minLatitude);
        expectedBindVariables.add(boundingBoxForRadius.maxLatitude);
        expectedBindVariables.add(boundingBoxForRadius.minLongitude);
        expectedBindVariables.add(boundingBoxForRadius.maxLongitude);
        expectedBindVariables.add(Math.toRadians(10.0));
        expectedBindVariables.add(Math.toRadians(10.0));
        expectedBindVariables.add(Math.toRadians(20.0));
        expectedBindVariables.add(4.0);

        String expectedSql =
                "(pX.LATITUDE_VALUE >= ? AND pX.LATITUDE_VALUE <= ? AND pX.LONGITUDE_VALUE >= ? AND pX.LONGITUDE_VALUE <= ?)"
                + " AND " +
                "(ACOS(SIN(?) * SIN(RADIANS(pX.LATITUDE_VALUE)) + COS(?) * COS(RADIANS(pX.LATITUDE_VALUE)) * COS(? - RADIANS(pX.LONGITUDE_VALUE))) * 6371 <= ?)";

        BoundingRadius boundingRadius = BoundingRadius.builder().latitude(10.0).longitude(20.0).radius(4.0).build();
        runTest(expectedBindVariables, expectedSql, boundingRadius);
    }

    @Test
    public void testBoundingList() throws FHIRPersistenceException, FHIRSearchException {
        BoundingBox boundingBoxForRadius = new NearLocationHandler().createBoundingBox(10, 21, 4, "km");

        BoundingRadius boundingRadius = BoundingRadius.builder().latitude(10.0).longitude(21.0).radius(4.0).build();
        BoundingBox boundingBox = BoundingBox.builder()
                .maxLatitude(11.0)
                .minLatitude(-10.0)
                .maxLongitude(20.0)
                .minLongitude(-20.0)
                .build();

        List<Bounding> boundingAreas = Arrays.asList(boundingRadius, boundingBox);

        List<Object> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(boundingBoxForRadius.minLatitude);
        expectedBindVariables.add(boundingBoxForRadius.maxLatitude);
        expectedBindVariables.add(boundingBoxForRadius.minLongitude);
        expectedBindVariables.add(boundingBoxForRadius.maxLongitude);
        expectedBindVariables.add(Math.toRadians(10.0));
        expectedBindVariables.add(Math.toRadians(10.0));
        expectedBindVariables.add(Math.toRadians(21.0));
        expectedBindVariables.add(new Double(4.0));
        expectedBindVariables.add(-10.0);
        expectedBindVariables.add(11.0);
        expectedBindVariables.add(-20.0);
        expectedBindVariables.add(20.0);

        String expectedSql =
                "((pX.LATITUDE_VALUE >= ? AND pX.LATITUDE_VALUE <= ? AND pX.LONGITUDE_VALUE >= ? AND pX.LONGITUDE_VALUE <= ?) AND "
                + "(ACOS(SIN(?) * SIN(RADIANS(pX.LATITUDE_VALUE)) + COS(?) * COS(RADIANS(pX.LATITUDE_VALUE)) * COS(? - RADIANS(pX.LONGITUDE_VALUE))) * 6371 <= ?)"
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