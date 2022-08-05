/*
 * (C) Copyright IBM Corp. 2019,2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.search.location;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.Arrays;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.search.location.bounding.BoundingBox;
import org.linuxforhealth.fhir.search.location.bounding.BoundingRadius;
import org.linuxforhealth.fhir.search.location.bounding.BoundingType;

public class BoundingTest {

    @Test
    public void testBoundingBox() {
        BoundingBox bounding =
                BoundingBox.builder().maxLatitude(10.0).minLatitude(-10.0).maxLongitude(20.0).minLongitude(-10.0)
                        .build();
        assertNotNull(bounding);
        assertEquals(bounding.getMinLatitude(), Double.valueOf("-10.0"));
        assertEquals(bounding.getMinLongitude(), Double.valueOf("-10.0"));
        assertEquals(bounding.getMaxLatitude(), Double.valueOf("10.0"));
        assertEquals(bounding.getMaxLongitude(), Double.valueOf("20.0"));
        assertEquals(bounding.toString(),
                "BoundingBox [minLatitude=-10.0, maxLatitude=10.0, minLongitude=-10.0, maxLongitude=20.0, instance=0]");
        assertEquals(bounding.getDataPoints(), Arrays.asList(-10.0, -10.0, 10.0, 20.0));
        assertEquals(bounding.getType(), BoundingType.BOX);
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testBadBoundsNullMaxLat() {
        BoundingBox.builder().maxLatitude(null).minLatitude(-10.0).maxLongitude(20.0).minLongitude(-10.0).build();
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testBadBoundsBadMaxLat() {
        BoundingBox.builder().maxLatitude(-190.0).minLatitude(-10.0).maxLongitude(20.0).minLongitude(-10.0).build();
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testBadBoundsGoodBadMaxLat() {
        BoundingBox.builder().maxLatitude(10.0).minLatitude(null).maxLongitude(20.0).minLongitude(-10.0).build();
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testBadBoundsNullMinLat() {
        BoundingBox.builder().maxLatitude(10.0).minLatitude(-900.0).maxLongitude(20.0).minLongitude(-10.0).build();
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testBadBoundsNullMaxLong() {
        BoundingBox.builder().maxLatitude(10.0).minLatitude(10.0).maxLongitude(null).minLongitude(-10.0).build();
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testBadBoundsBadMaxLong() {
        BoundingBox.builder().maxLatitude(-190.0).minLatitude(10.0).maxLongitude(200.0).minLongitude(-10.0).build();
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testBadBoundsGoodNullMinLong() {
        BoundingBox.builder().maxLatitude(10.0).minLatitude(10.0).maxLongitude(-9.0).minLongitude(null).build();
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testBadBoundsGoodMaxBadMinLong() {
        BoundingBox.builder().maxLatitude(10.0).minLatitude(10.0).maxLongitude(20.0).minLongitude(-100000.0).build();
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testBadBoundsGoodMaxBadMinLongMin() {
        BoundingBox.builder().maxLatitude(10.0).minLatitude(10.0).maxLongitude(20.0).minLongitude(100000.0).build();
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testBadBoundsGoodMaxBadMinLongMax() {
        BoundingBox.builder().maxLatitude(10.0).minLatitude(10.0).maxLongitude(100000.0).minLongitude(1.0).build();
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testBadBoundsRadiusLatNull() {
        BoundingRadius.builder().latitude(null).longitude(-1.0).radius(-10.0).build();
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testBadBoundsRadiusLatTooHigh() {
        BoundingRadius.builder().latitude(90.1).longitude(-1.0).radius(-10.0).build();
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testBadBoundsRadiusLatTooLow() {
        BoundingRadius.builder().latitude(-90.1).longitude(-1.0).radius(-10.0).build();
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testBadBoundsRadiusLongtNull() {
        BoundingRadius.builder().latitude(10.0).longitude(null).radius(-10.0).build();
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testBadBoundsRadiusLongTooHigh() {
        BoundingRadius.builder().latitude(9.0).longitude(-181.0).radius(-10.0).build();
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testBadBoundsRadiusLongTooLow() {
        BoundingRadius.builder().latitude(-9.0).longitude(-181.0).radius(-10.0).build();
    }

    @Test
    public void testBoundingRadius() {
        BoundingRadius bounding = BoundingRadius.builder().latitude(10.0).longitude(20.0).radius(4.0).build();
        assertNotNull(bounding);
        assertEquals(bounding.getLatitude(), Double.valueOf("10.0"));
        assertEquals(bounding.getLongitude(), Double.valueOf("20.0"));
        assertEquals(bounding.getRadius(), Double.valueOf("4.0"));
        assertEquals(bounding.toString(), "BoundingRadius [latitude=10.0, longitude=20.0, instance=0]");
        assertEquals(bounding.getDataPoints(), Arrays.asList(10.0, 20.0, 4.0));
        assertEquals(bounding.getType(), BoundingType.RADIUS);
    }
}