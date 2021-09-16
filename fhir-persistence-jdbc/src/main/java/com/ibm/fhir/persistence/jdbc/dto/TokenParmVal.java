/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dto;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.JDBCConstants;

/**
 * This class defines the Data Transfer Object representing a row in the X_TOKEN_VALUES tables.
 */
public class TokenParmVal extends ExtractedParameterValue {

    private String valueSystem;
    private String valueCode;

    /**
     * Public constructor
     */
    public TokenParmVal() {
        super();
    }

    @Override
    public String toString() {
        // to aid debugging
        return getResourceType() + "[" + getName() + ", " + getValueSystem() + ", " + getValueCode() + "]";
    }

    public String getValueSystem() {
        if (valueSystem == null) {
            return JDBCConstants.DEFAULT_TOKEN_SYSTEM;
        }
        return valueSystem;
    }

    public void setValueSystem(String valueSystem) {
        this.valueSystem = valueSystem;
    }

    public String getValueCode() {
        return valueCode;
    }

    public void setValueCode(String valueCode) {
        this.valueCode = valueCode;
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
        TokenParmVal other = (TokenParmVal) o;
        int retVal;

        String thisValueSystem = this.getValueSystem();
        String otherValueSystem = other.getValueSystem();
        if (thisValueSystem != null || otherValueSystem != null) {
            if (thisValueSystem == null) {
                return -1;
            } else if (otherValueSystem == null) {
                return 1;
            }
            retVal = thisValueSystem.compareTo(otherValueSystem);
            if (retVal != 0) {
                return retVal;
            }
        }
        String thisValueCode = this.getValueCode();
        String otherValueCode = other.getValueCode();
        if (thisValueCode != null || otherValueCode != null) {
            if (thisValueCode == null) {
                return -1;
            } else if (otherValueCode == null) {
                return 1;
            }
            retVal = thisValueCode.compareTo(otherValueCode);
            if (retVal != 0) {
                return retVal;
            }
        }

        return 0;
    }
}
