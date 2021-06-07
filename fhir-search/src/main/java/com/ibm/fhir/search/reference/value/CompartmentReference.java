/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.reference.value;

import java.util.Objects;

/**
 * Represents a reference to a resource compartment extracted by SearchUtil
 */
public class CompartmentReference {

    private final String parameterName;

    private final String referenceResourceType;

    private final String referenceResourceValue;

    /**
     * Public constructor
     * @param parameterName
     * @param referenceResourceType
     * @param referenceResourceValue
     */
    public CompartmentReference(String parameterName, String referenceResourceType, String referenceResourceValue) {
        Objects.requireNonNull(parameterName, "parameterName");
        Objects.requireNonNull(referenceResourceType, "referenceResourceType");
        Objects.requireNonNull(referenceResourceValue, "referenceResourceValue");
        this.parameterName = parameterName;
        this.referenceResourceType = referenceResourceType;
        this.referenceResourceValue = referenceResourceValue;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        return parameterName.hashCode() + prime * (referenceResourceType.hashCode() + prime * referenceResourceValue.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CompartmentReference) {
            CompartmentReference that = (CompartmentReference)obj;
            return this.parameterName.equals(that.parameterName)
                && this.referenceResourceType.equals(that.referenceResourceType)
                && this.referenceResourceValue.equals(that.referenceResourceValue);
        } else {
            return false;
        }
    }


    /**
     * @return the parameterName
     */
    public String getParameterName() {
        return parameterName;
    }


    /**
     * @return the referenceResourceType
     */
    public String getReferenceResourceType() {
        return referenceResourceType;
    }


    /**
     * @return the referenceResourceValue
     */
    public String getReferenceResourceValue() {
        return referenceResourceValue;
    }
}
