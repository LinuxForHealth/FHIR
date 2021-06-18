/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dto;

import java.sql.Timestamp;
import java.util.Objects;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.util.ParameterHashUtil;

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
    public String getHash(ParameterHashUtil parameterHashUtil) {
        StringBuilder sb = new StringBuilder();
        sb.append(Objects.toString(valueDateStart, ""));
        sb.append("|").append(Objects.toString(valueDateEnd, ""));
        return parameterHashUtil.getNameValueHash(getHashHeader(), sb.toString());
    }

    @Override
    public String toString() {
        return "DateParmVal [resourceType=" + getResourceType() + ", name=" + getName()
                + ", valueDateStart=" + valueDateStart + ", valueDateEnd=" + valueDateEnd + "]";
    }
}