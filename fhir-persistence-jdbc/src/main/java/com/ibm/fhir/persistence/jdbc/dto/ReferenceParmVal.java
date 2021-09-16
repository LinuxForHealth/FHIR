/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dto;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.search.SearchConstants.Type;
import com.ibm.fhir.search.util.ReferenceValue;
import com.ibm.fhir.search.util.ReferenceValue.ReferenceType;

/**
 * DTO representing external and local reference parameters
 */
public class ReferenceParmVal extends ExtractedParameterValue {

    // The value of the reference after it has been processed to determine target resource type, version etc.
    private ReferenceValue refValue;

    /**
     * Public constructor
     */
    public ReferenceParmVal() {
        super();
    }

    /**
     * Get the refValue
     * @return
     */
    public ReferenceValue getRefValue() {
        return this.refValue;
    }

    /**
     * Set the refValue
     * @param refValue
     */
    public void setRefValue(ReferenceValue refValue) {
        this.refValue = refValue;
    }

    public Type getType() {
        return Type.REFERENCE;
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
        ReferenceParmVal other = (ReferenceParmVal) o;
        int retVal;

        ReferenceValue thisRefValue = this.getRefValue();
        ReferenceValue otherRefValue = other.getRefValue();
        if (thisRefValue != null || otherRefValue != null) {
            if (thisRefValue == null) {
                return -1;
            } else if (otherRefValue == null) {
                return 1;
            }

            String thisTargetResourceType = thisRefValue.getTargetResourceType();
            String otherTargetResourceType = otherRefValue.getTargetResourceType();
            if (thisTargetResourceType != null || otherTargetResourceType != null) {
                if (thisTargetResourceType == null) {
                    return -1;
                } else if (otherTargetResourceType == null) {
                    return 1;
                }
                retVal = thisTargetResourceType.compareTo(otherTargetResourceType);
                if (retVal != 0) {
                    return retVal;
                }
            }

            String thisValue = thisRefValue.getValue();
            String otherValue = otherRefValue.getValue();
            if (thisValue != null || otherValue != null) {
                if (thisValue == null) {
                    return -1;
                } else if (otherValue == null) {
                    return 1;
                }
                retVal = thisValue.compareTo(otherValue);
                if (retVal != 0) {
                    return retVal;
                }
            }

            ReferenceType thisType = thisRefValue.getType();
            ReferenceType otherType = otherRefValue.getType();
            if (thisType != null || otherType != null) {
                if (thisType == null) {
                    return -1;
                } else if (otherType == null) {
                    return 1;
                }
                retVal = thisType.compareTo(otherType);
                if (retVal != 0) {
                    return retVal;
                }
            }

            Integer thisVersion = thisRefValue.getVersion();
            Integer otherVersion = otherRefValue.getVersion();
            if (thisVersion != null || otherVersion != null) {
                if (thisVersion == null) {
                    return -1;
                } else if (otherVersion == null) {
                    return 1;
                }
                retVal = thisVersion.compareTo(otherVersion);
                if (retVal != 0) {
                    return retVal;
                }
            }
        }

        return 0;
    }
}