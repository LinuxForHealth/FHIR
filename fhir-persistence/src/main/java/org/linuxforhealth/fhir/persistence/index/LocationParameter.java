/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.index;

import java.util.Objects;

/**
 * A LatLng location search parameter
 */
public class LocationParameter extends SearchParameterValue {
    private Double valueLatitude;
    private Double valueLongitude;
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Location[");
        addDescription(result);
        result.append(",");
        result.append(valueLatitude);
        result.append(",");
        result.append(valueLongitude);
        result.append("]");
        return result.toString();
    }

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

    @Override
    public int hashCode() {
        // yeah I know I could include the super hashCode in the list of values,
        // but this avoids unnecessary boxing
        return 31 * super.hashCode() + Objects.hash(valueLatitude, valueLongitude);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LocationParameter) {
            LocationParameter that = (LocationParameter)obj;
            return super.equals(obj)
                    && Objects.equals(this.valueLatitude, that.valueLatitude)
                    && Objects.equals(this.valueLongitude, that.valueLongitude);
        }
        return false;
    }
}
