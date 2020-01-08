/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.location.bounding;

import java.util.Arrays;
import java.util.List;

import com.ibm.fhir.search.location.util.LocationUtil;

/**
 * Bounding Radius takes a point as defined in radians.
 * Latitude and Longtitude and the radius on the arc.
 * The radius on the arc must be in meters.
 */
public class BoundingRadius extends Bounding {

    private Double latitude;
    private Double longitude;
    private Double radius;

    public Double getLatitude() {
        return latitude;
    }

    protected void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    protected void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getRadius() {
        return radius;
    }

    protected void setRadius(Double radius) {
        this.radius = radius;
    }

    @Override
    public void validate() {
        if (LocationUtil.checkNull(latitude) || !LocationUtil.checkLatValid(latitude)) {
            throw new IllegalArgumentException("Null or Invalid number for the latitude");
        }

        if (LocationUtil.checkNull(longitude) || !LocationUtil.checkLonValid(longitude)) {
            throw new IllegalArgumentException("Null or Invalid number for the longitude");
        }
    }

    public List<Double> getDataPoints() {
        return Arrays.asList(latitude, longitude, radius);
    }

    public BoundingType getType() {
        return BoundingType.RADIUS;
    }

    /**
     * Builds the Bounding Radius.
     */
    public static class Builder {
        private BoundingRadius r = new BoundingRadius();

        public Builder latitude(Double lat) {
            r.setLatitude(lat);
            return this;
        }

        public Builder longitude(Double lon) {
            r.setLongitude(lon);
            return this;
        }

        public Builder radius(Double radius) {
            r.setRadius(radius);
            return this;
        }

        public BoundingRadius build() {
            r.validate();
            return r;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "BoundingRadius [latitude=" + latitude + ", longitude=" + longitude + ", instance=" + super.instance()
                + "]";
    }
}