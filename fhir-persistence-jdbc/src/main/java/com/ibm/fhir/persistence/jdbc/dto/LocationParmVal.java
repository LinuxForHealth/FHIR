/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dto;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * This class defines the Data Transfer Object representing a row in the X_LATLNG_VALUES tables.
 */
public class LocationParmVal extends ExtractedParameterValue {

    private Double valueLongitude;
    private Double valueLatitude;

    /**
     * Public constructor
     */
    public LocationParmVal() {
        super();
    }

    public Double getValueLongitude() {
        return valueLongitude;
    }

    public void setValueLongitude(Double valueLongitude) {
        this.valueLongitude = valueLongitude;
    }

    public Double getValueLatitude() {
        return valueLatitude;
    }

    public void setValueLatitude(Double valueLatitude) {
        this.valueLatitude = valueLatitude;
    }

    /**
     * We know our type, so we can call the correct method on the visitor
     */
    public void accept(ExtractedParameterValueVisitor visitor) throws FHIRPersistenceException {
        visitor.visit(this);
    }
}