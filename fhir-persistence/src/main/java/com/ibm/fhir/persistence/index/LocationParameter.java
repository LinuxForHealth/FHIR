/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.index;


/**
 * A LatLng location search parameter
 */
public class LocationParameter extends SearchParameterValue {
    private Double valueLatitude;
    private Double valueLongitude;
    
    /**
     * @return the valueLatitude
     */
    public Double getValueLatitude() {
        return valueLatitude;
    }
    
    /**
     * @param valueLatitude the valueLatitude to set
     */
    public void setValueLatitude(Double valueLatitude) {
        this.valueLatitude = valueLatitude;
    }
    
    /**
     * @return the valueLongitude
     */
    public Double getValueLongitude() {
        return valueLongitude;
    }
    
    /**
     * @param valueLongitude the valueLongitude to set
     */
    public void setValueLongitude(Double valueLongitude) {
        this.valueLongitude = valueLongitude;
    }

}
