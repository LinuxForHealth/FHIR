/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.dto;

import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;

/**
 * This class defines the Data Transfer Object representing a row in the X_LATLNG_VALUES tables.
 */
public class LocationParmVal extends ExtractedParameterValue {

    private Double valueLatitude;
    private Double valueLongitude;

    /**
     * Public constructor
     */
    public LocationParmVal() {
        super();
    }

    public Double getValueLatitude() {
        return valueLatitude;
    }

    public void setValueLatitude(Double valueLatitude) {
        this.valueLatitude = valueLatitude;
    }

    public Double getValueLongitude() {
        return valueLongitude;
    }

    public void setValueLongitude(Double valueLongitude) {
        this.valueLongitude = valueLongitude;
    }

    /**
     * We know our type, so we can call the correct method on the visitor
     */
    @Override
    public void accept(ExtractedParameterValueVisitor visitor) throws FHIRPersistenceException {
        visitor.visit(this);
    }

    @Override
    protected int compareToInner(ExtractedParameterValue o) {
        LocationParmVal other = (LocationParmVal) o;
        int retVal;

        Double thisValueLatitude = this.getValueLatitude();
        Double otherValueLatitude = other.getValueLatitude();
        if (thisValueLatitude != null || otherValueLatitude != null) {
            if (thisValueLatitude == null) {
                return -1;
            } else if (otherValueLatitude == null) {
                return 1;
            }
            retVal = thisValueLatitude.compareTo(otherValueLatitude);
            if (retVal != 0) {
                return retVal;
            }
        }

        Double thisValueLongitude = this.getValueLongitude();
        Double otherValueLongitude = other.getValueLongitude();
        if (thisValueLongitude != null || otherValueLongitude != null) {
            if (thisValueLongitude == null) {
                return -1;
            } else if (otherValueLongitude == null) {
                return 1;
            }
            retVal = thisValueLongitude.compareTo(otherValueLongitude);
            if (retVal != 0) {
                return retVal;
            }
        }

        return 0;
    }
}