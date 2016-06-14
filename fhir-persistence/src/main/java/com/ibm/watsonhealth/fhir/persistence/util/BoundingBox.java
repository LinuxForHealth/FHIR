/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.util;

public class BoundingBox {
    public double minLatitude;
    public double maxLatitude;
    public double minLongitude;
    public double maxLongitude;

    public BoundingBox(double minLatitude, double maxLatitude, double minLongitude, double maxLongitude) {
        this.minLatitude = minLatitude;
        this.maxLatitude = maxLatitude;
        this.minLongitude = minLongitude;
        this.maxLongitude = maxLongitude;
    }

    public double getMinLatitude() {
        return minLatitude;
    }

    public double getMaxLatitude() {
        return maxLatitude;
    }

    public double getMinLongitude() {
        return minLongitude;
    }

    public double getMaxLongitude() {
        return maxLongitude;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("minLatitude: ");
        buffer.append(minLatitude);
        buffer.append(", maxLatitude: ");
        buffer.append(maxLatitude);
        buffer.append(", minLongitude: ");
        buffer.append(minLongitude);
        buffer.append(", maxLongitude: ");
        buffer.append(maxLongitude);
        return buffer.toString();
    }
}
