/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.location;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.model.resource.Location;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.location.bounding.Bounding;
import com.ibm.fhir.search.location.bounding.BoundingBox;
import com.ibm.fhir.search.location.bounding.BoundingType;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * Test the BoundingBox
 * Verified using https://www.geodatasource.com/distance-calculator
 */
@Test
public class NearLocationHandlerBoundingBoxTest {
    @BeforeClass
    public void setup() {
        FHIRConfiguration.setConfigHome("src/test/resources");
    }

    @BeforeMethod
    public void startMethod(Method method) throws FHIRException {
        // Configure the request context for our search tests
        FHIRRequestContext context = FHIRRequestContext.get();

        //Facilitate the switching of tenant configurations based on method name
        String tenant = "default";
        String methodName = method.getName();
        if (methodName.contains("_tenant_")) {
            int idx = methodName.indexOf("_tenant_") + "_tenant_".length();
            tenant = methodName.substring(idx);
        }
        context.setTenantId(tenant);

        context.setOriginalRequestUri("https://localhost:9443/fhir-server/api/v4");
    }

    @AfterMethod
    public void clearThreadLocal() {
        FHIRRequestContext.remove();
    }
    @Test
    public void testLocationBoundaryExample() throws FHIRSearchException {
        double latitude = 40;
        double longitude = -40;
        double distance = 1000.0;
        String unit = "km";
        NearLocationHandler handler = new NearLocationHandler();
        BoundingBox boundingBox = handler.createBoundingBox(latitude, longitude, distance, unit);
        assertNotNull(boundingBox);
        assertApproxEquals(boundingBox.getMinLatitude(), Double.valueOf("30.98662700557291"), 9);
        assertApproxEquals(boundingBox.getMinLongitude(), Double.valueOf("-51.7266732000822"), 9);
        assertApproxEquals(boundingBox.getMaxLatitude(), Double.valueOf("49.01337299442709"), 9);
        assertApproxEquals(boundingBox.getMaxLongitude(), Double.valueOf("-28.2733267999178"), 9);
        assertEquals(boundingBox.getType(), BoundingType.BOX);

        // The resulting diagnoal is ~2807.48 km
        // The side of the square is 1986 km.
        // The error is roughly .7%
    }

    @Test
    public void testLocationBoundaryOrigin() throws FHIRSearchException {
        double latitude = 0;
        double longitude = 0;
        double distance = 10.0;
        String unit = "km";
        NearLocationHandler handler = new NearLocationHandler();
        BoundingBox boundingBox = handler.createBoundingBox(latitude, longitude, distance, unit);
        assertNotNull(boundingBox);
        assertApproxEquals(boundingBox.getMinLatitude(), Double.valueOf("-0.090133729"), 9);
        assertApproxEquals(boundingBox.getMinLongitude(), Double.valueOf("-0.089831528"), 9);
        assertApproxEquals(boundingBox.getMaxLatitude(), Double.valueOf("0.090133729"), 9);
        assertApproxEquals(boundingBox.getMaxLongitude(), Double.valueOf("0.089831528"), 9);
        assertEquals(boundingBox.getType(), BoundingType.BOX);
    }

    @Test
    public void testLocationBoundaryOriginNoDistance() throws FHIRSearchException {
        double latitude = 0;
        double longitude = 0;
        double distance = 0.0;
        String unit = "km";
        NearLocationHandler handler = new NearLocationHandler();
        BoundingBox boundingBox = handler.createBoundingBox(latitude, longitude, distance, unit);
        assertNotNull(boundingBox);
        assertEquals(boundingBox.getMinLatitude(), Double.valueOf("0.0"));
        assertEquals(boundingBox.getMinLongitude(), Double.valueOf("0.0"));
        assertEquals(boundingBox.getMaxLatitude(), Double.valueOf("0.0"));
        assertEquals(boundingBox.getMaxLongitude(), Double.valueOf("0.0"));
        assertEquals(boundingBox.getType(), BoundingType.BOX);
    }

    @Test
    public void testLocationBoundaryNorthPole() throws FHIRSearchException {
        double latitude = 90;
        double longitude = 0;
        double distance = 1.0;
        String unit = "km";
        NearLocationHandler handler = new NearLocationHandler();
        BoundingBox boundingBox = handler.createBoundingBox(latitude, longitude, distance, unit);
        assertNotNull(boundingBox);
        // At the high latitudes it's going to cover most of the area.
        assertApproxEquals(boundingBox.getMinLatitude(), Double.valueOf("89.990986627"), 9);
        assertEquals(boundingBox.getMinLongitude(), Double.valueOf("-180.0"));
        assertEquals(boundingBox.getMaxLatitude(), Double.valueOf("90.0"));
        assertEquals(boundingBox.getMaxLongitude(), Double.valueOf("180.0"));
        assertEquals(boundingBox.getType(), BoundingType.BOX);
    }

    @Test
    public void testLocationBoundarySouthPole() throws FHIRSearchException {
        double latitude = -90;
        double longitude = 0;
        double distance = 1.0;
        String unit = "km";
        NearLocationHandler handler = new NearLocationHandler();
        BoundingBox boundingBox = handler.createBoundingBox(latitude, longitude, distance, unit);
        assertNotNull(boundingBox);
        // At the low latitudes it's going to cover most of the area.
        assertEquals(boundingBox.getMinLatitude(), Double.valueOf("-90.0"));
        assertEquals(boundingBox.getMinLongitude(), Double.valueOf("-180.0"));
        assertApproxEquals(boundingBox.getMaxLatitude(), Double.valueOf("-89.990986627"), 9);
        assertEquals(boundingBox.getMaxLongitude(), Double.valueOf("180.0"));
        assertEquals(boundingBox.getType(), BoundingType.BOX);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testLocationBoundaryBadUnit() throws FHIRSearchException {
        double latitude = -90;
        double longitude = 0;
        double distance = 1.0;
        String unit = "FUDGE";
        NearLocationHandler handler = new NearLocationHandler();
        handler.createBoundingBox(latitude, longitude, distance, unit);
    }

    @Test
    public void testLocationBoundaryPositionsFromParameters_tenant_near() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>(1);
        queryParms.put("near", Collections.singletonList("-90.0|0.0|1.0|km"));
        FHIRSearchContext ctx = SearchUtil.parseQueryParameters(Location.class, queryParms, true, true);
        NearLocationHandler handler = new NearLocationHandler();
        List<Bounding> bounding = handler.generateLocationPositionsFromParameters(ctx.getSearchParameters());
        assertNotNull(bounding);
        assertFalse(bounding.isEmpty(), "generateLocationPositionsFromParameters came back empty for tenant " + FHIRRequestContext.get().getTenantId());

        BoundingBox boundingBox = (BoundingBox) bounding.get(0);
        // At the low latitudes it's going to cover most of the area.
        assertEquals(boundingBox.getMinLatitude(), Double.valueOf("-90.0"));
        assertEquals(boundingBox.getMinLongitude(), Double.valueOf("-180.0"));
        assertApproxEquals(boundingBox.getMaxLatitude(), Double.valueOf("-89.990986627"), 9);
        assertEquals(boundingBox.getMaxLongitude(), Double.valueOf("180.0"));
        assertEquals(boundingBox.getType(), BoundingType.BOX);
    }

    private void assertApproxEquals(double x, double y, int numberOfDecimalsToCheck) {
        assertEquals(truncate(x, numberOfDecimalsToCheck), truncate(y, numberOfDecimalsToCheck));
    }

    // from https://stackoverflow.com/a/21468258/161022
    private static BigDecimal truncate(double x, int numberOfDecimals)
    {
        if ( x > 0) {
            return new BigDecimal(String.valueOf(x)).setScale(numberOfDecimals, BigDecimal.ROUND_FLOOR);
        } else {
            return new BigDecimal(String.valueOf(x)).setScale(numberOfDecimals, BigDecimal.ROUND_CEILING);
        }
    }
}