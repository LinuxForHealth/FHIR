/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.index;

import java.time.Instant;
import java.util.Objects;

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

    @Override
    public int hashCode() {
        // yeah I know I could include the super hashCode in the list of values,
        // but this avoids unnecessary boxing
        return 31 * super.hashCode() + Objects.hash(valueDateStart, valueDateEnd);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DateParameter) {
            DateParameter that = (DateParameter)obj;
            return super.equals(obj)
                    && Objects.equals(this.valueDateStart, that.valueDateStart)
                    && Objects.equals(this.valueDateEnd, that.valueDateEnd);
        }
        return false;
    }
}
