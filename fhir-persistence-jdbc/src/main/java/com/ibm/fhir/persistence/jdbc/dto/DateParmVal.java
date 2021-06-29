/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dto;

import java.sql.Timestamp;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * This class defines the Data Transfer Object representing a row in the X_DATE_VALUES tables.
 */
public class DateParmVal extends ExtractedParameterValue {

    private Timestamp valueDateStart;
    private Timestamp valueDateEnd;

    public enum TimeType {
        YEAR,
        YEAR_MONTH,
        LOCAL_DATE,
        ZONE_DATE,
        UNKNOWN,
        DEFAULT
    }

    public DateParmVal() {
        super();
    }

    public Timestamp getValueDateStart() {
        return valueDateStart;
    }

    public void setValueDateStart(Timestamp valueDateStart) {
        this.valueDateStart = valueDateStart;
    }

    public Timestamp getValueDateEnd() {
        return valueDateEnd;
    }

    public void setValueDateEnd(Timestamp valueDateEnd) {
        this.valueDateEnd = valueDateEnd;
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
        DateParmVal other = (DateParmVal) o;
        int retVal;

        Timestamp thisValueDateStart = this.getValueDateStart();
        Timestamp otherValueDateStart = other.getValueDateStart();
        if (thisValueDateStart != null || otherValueDateStart != null) {
            if (thisValueDateStart == null) {
                return -1;
            } else if (otherValueDateStart == null) {
                return 1;
            }
            retVal = thisValueDateStart.compareTo(otherValueDateStart);
            if (retVal != 0) {
                return retVal;
            }
        }

        Timestamp thisValueDateEnd = this.getValueDateEnd();
        Timestamp otherValueDateEnd = other.getValueDateEnd();
        if (thisValueDateEnd != null || otherValueDateEnd != null) {
            if (thisValueDateEnd == null) {
                return -1;
            } else if (otherValueDateEnd == null) {
                return 1;
            }
            retVal = thisValueDateEnd.compareTo(otherValueDateEnd);
            if (retVal != 0) {
                return retVal;
            }
        }

        return 0;
    }

    @Override
    public String toString() {
        return "DateParmVal [resourceType=" + getResourceType() + ", name=" + getName()
                + ", valueDateStart=" + valueDateStart + ", valueDateEnd=" + valueDateEnd + "]";
    }
}