/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.impl;


/**
 * DTO representing a reference to an external resource
 */
public class ExternalResourceRef extends ResourceRef {

    // The system name for the external reference
    private final String externalSystemName;
    
    // The value string of the external reference
    private final String externalRefValue;
    
    public ExternalResourceRef(long fromLogicalResourceId, int parameterNameId, String externalSystemName, String externalRefValue) {
        super(fromLogicalResourceId, parameterNameId);
        this.externalSystemName = externalSystemName;
        this.externalRefValue = externalRefValue;
    }

    /**
     * @return the externalSystemName
     */
    public String getExternalSystemName() {
        return externalSystemName;
    }

    /**
     * @return the externalRefValue
     */
    public String getExternalRefValue() {
        return externalRefValue;
    }
}
