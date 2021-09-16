/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dto;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * This class defines the Data Transfer Object representing a row in the X_STR_VALUES tables.
 */
public class StringParmVal extends ExtractedParameterValue {

    // The string value of this extracted parameter
    private String valueString;

    /**
     * Public constructor
     */
    public StringParmVal() {
        super();
    }

    public String getValueString() {
        return valueString;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
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
        StringParmVal other = (StringParmVal) o;
        int retVal;

        String thisValueString = this.getValueString();
        String otherValueString = other.getValueString();
        if (thisValueString != null || otherValueString != null) {
            if (thisValueString == null) {
                return -1;
            } else if (otherValueString == null) {
                return 1;
            }
            retVal = thisValueString.compareTo(otherValueString);
            if (retVal != 0) {
                return retVal;
            }
        }

        return 0;
    }
}