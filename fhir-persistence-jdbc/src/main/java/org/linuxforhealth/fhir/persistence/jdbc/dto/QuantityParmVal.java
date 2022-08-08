/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.dto;

import java.math.BigDecimal;

import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.persistence.jdbc.JDBCConstants;

/**
 * This class defines the Data Transfer Object representing a row in the X_QUANTITY_VALUES tables.
 */
public class QuantityParmVal extends ExtractedParameterValue {

    private BigDecimal valueNumber;
    private BigDecimal valueNumberLow;
    private BigDecimal valueNumberHigh;
    private String valueSystem;
    private String valueCode;

    /**
     * Public constructor
     */
    public QuantityParmVal() {
        super();
    }

    public BigDecimal getValueNumber() {
        return valueNumber;
    }

    public void setValueNumber(BigDecimal valueNumber) {
        this.valueNumber = valueNumber;
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
        // X_QUANTITY_VALUES tables have non-nullable CODE column, so use empty string for no code
        if (valueCode == null) {
            return "";
        }
        return valueCode;
    }

    public void setValueCode(String valueCode) {
        this.valueCode = valueCode;
    }

    public BigDecimal getValueNumberLow() {
        return valueNumberLow;
    }

    public void setValueNumberLow(BigDecimal valueNumberLow) {
        this.valueNumberLow = valueNumberLow;
    }

    public BigDecimal getValueNumberHigh() {
        return valueNumberHigh;
    }

    public void setValueNumberHigh(BigDecimal valueNumberHigh) {
        this.valueNumberHigh = valueNumberHigh;
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
        QuantityParmVal other = (QuantityParmVal) o;
        int retVal;

        BigDecimal thisValueNumber = this.getValueNumber();
        BigDecimal otherValueNumber = other.getValueNumber();
        if (thisValueNumber != null || otherValueNumber != null) {
            if (thisValueNumber == null) {
                return -1;
            } else if (otherValueNumber == null) {
                return 1;
            }
            retVal = thisValueNumber.compareTo(otherValueNumber);
            if (retVal != 0) {
                return retVal;
            }
        }

        BigDecimal thisValueNumberLow = this.getValueNumberLow();
        BigDecimal otherValueNumberLow = other.getValueNumberLow();
        if (thisValueNumberLow != null || otherValueNumberLow != null) {
            if (thisValueNumberLow == null) {
                return -1;
            } else if (otherValueNumberLow == null) {
                return 1;
            }
            retVal = thisValueNumberLow.compareTo(otherValueNumberLow);
            if (retVal != 0) {
                return retVal;
            }
        }

        BigDecimal thisValueNumberHigh = this.getValueNumberHigh();
        BigDecimal otherValueNumberHigh = other.getValueNumberHigh();
        if (thisValueNumberHigh != null || otherValueNumberHigh != null) {
            if (thisValueNumberHigh == null) {
                return -1;
            } else if (otherValueNumberHigh == null) {
                return 1;
            }
            retVal = thisValueNumberHigh.compareTo(otherValueNumberHigh);
            if (retVal != 0) {
                return retVal;
            }
        }

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