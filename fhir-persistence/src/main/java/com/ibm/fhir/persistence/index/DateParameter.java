/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.index;

import java.time.Instant;

/**
 * A date search parameter value
 */
public class DateParameter extends SearchParameterValue {
    private Instant valueDateStart;
    private Instant valueDateEnd;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Date[");
        addDescription(result);
        result.append(",");
        result.append(valueDateStart);
        result.append(",");
        result.append(valueDateEnd);
        result.append("]");
        return result.toString();
    }

    /**
     * @return the valueDateStart
     */
    public Instant getValueDateStart() {
        return valueDateStart;
    }
    
    /**
     * @param valueDateStart the valueDateStart to set
     */
    public void setValueDateStart(Instant valueDateStart) {
        this.valueDateStart = valueDateStart;
    }
    
    /**
     * @return the valueDateEnd
     */
    public Instant getValueDateEnd() {
        return valueDateEnd;
    }
    
    /**
     * @param valueDateEnd the valueDateEnd to set
     */
    public void setValueDateEnd(Instant valueDateEnd) {
        this.valueDateEnd = valueDateEnd;
    }

}
