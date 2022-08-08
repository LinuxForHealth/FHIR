/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.search.location.bounding;

import java.util.Arrays;
import java.util.List;

import org.linuxforhealth.fhir.search.location.util.LocationUtil;

/**
 * The maximum number of BoundedBoxes for a single coordinate is 4.<br>
 * lat = 90,-90, long = 180, -180 with any boundary the result is 4 Bounded Boxes.<br>
 * <br>
 * The bounded boxes must adhere to the following constraints: <br>
 * There are four points.
 * Latitude: 90 to 0 to -90 <br>
 * Longitude: 180 to 0 -180 <br>
 */
public class BoundingBox extends Bounding {
    public Double minLatitude;
    public Double maxLatitude;
    public Double minLongitude;
    public Double maxLongitude;

    public BoundingBox() {
        // No Operation
    }

    public Double getMinLatitude() {
        return minLatitude;
    }

    public Double getMaxLatitude() {
        return maxLatitude;
    }

    public Double getMinLongitude() {
        return minLongitude;
    }

    public Double getMaxLongitude() {
        return maxLongitude;
    }

    protected void setMinLatitude(Double minLatitude) {
        this.minLatitude = minLatitude;
    }

    protected void setMaxLatitude(Double maxLatitude) {
        this.maxLatitude = maxLatitude;
    }

    protected void setMinLongitude(Double minLongitude) {
        this.minLongitude = minLongitude;
    }

    protected void setMaxLongitude(Double maxLongitude) {
        this.maxLongitude = maxLongitude;
    }

    public void validate() {
        if (LocationUtil.checkNull(minLatitude) || LocationUtil.checkNull(maxLatitude)
                || !LocationUtil.checkLatValid(minLatitude)
                || !LocationUtil.checkLatValid(maxLatitude)) {
            throw new IllegalArgumentException("Null or Invalid number for the latitude");
        }

        if (LocationUtil.checkNull(minLongitude) || LocationUtil.checkNull(maxLongitude)
                || !LocationUtil.checkLonValid(minLongitude)
                || !LocationUtil.checkLonValid(maxLongitude)) {
            throw new IllegalArgumentException("Null or Invalid number for the longitude");
        }
    }

    public List<Double> getDataPoints() {
        return Arrays.asList(minLatitude, minLongitude, maxLatitude, maxLongitude);
    }

    public BoundingType getType() {
        return BoundingType.BOX;
    }

    /**
     * Builds the BoundingBox.
     */
    public static class Builder {
        private BoundingBox box = new BoundingBox();

        public Builder minLatitude(Double lat) {
            box.setMinLatitude(lat);
            return this;
        }

        public Builder maxLatitude(Double lat) {
            box.setMaxLatitude(lat);
            return this;
        }

        public Builder minLongitude(Double lon) {
            box.setMinLongitude(lon);
            return this;
        }

        public Builder maxLongitude(Double lon) {
            box.setMaxLongitude(lon);
            return this;
        }

        public BoundingBox build() {
            box.validate();
            return box;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "BoundingBox [minLatitude=" + minLatitude + ", maxLatitude=" + maxLatitude + ", minLongitude="
                + minLongitude + ", maxLongitude=" + maxLongitude + ", instance=" + super.instance() +"]";
    }
}