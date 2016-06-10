/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.util;

/**
 * Object to hold and transport bounding box points
 * 
 *
 */
public class BoundingBox {
	
	public double minLatitude =0.0;
	public double maxLatitude =0.0;
	public double minLongitude =0.0;
	public double maxLongitude =0.0;
	public double getMinLatitude() {
		return minLatitude;
	}
	public void setMinLatitude(double minLatitude) {
		this.minLatitude = minLatitude;
	}
	public double getMaxLatitude() {
		return maxLatitude;
	}
	public void setMaxLatitude(double maxLatitude) {
		this.maxLatitude = maxLatitude;
	}
	public double getMinLongitude() {
		return minLongitude;
	}
	public void setMinLongitude(double minLongitude) {
		this.minLongitude = minLongitude;
	}
	public double getMaxLongitude() {
		return maxLongitude;
	}
	public void setMaxLongitude(double maxLongitude) {
		this.maxLongitude = maxLongitude;
	}
	
	public String toString() {
		
		return "minLatitude : " + this.minLatitude + " :maxLatitude: " + this.maxLatitude + " :minLongitude: " + this.minLongitude + " :maxLongitude: " + this.maxLongitude;
	}
	
}
