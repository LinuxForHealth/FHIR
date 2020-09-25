/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.impl;


/**
 * A DTO representing an external resource reference. This object is initialized
 * with names which are resolved to ids when passed through the cache. The idea
 * is to reduce the number of times we have to iterate over the list of records
 */
public class ExternalResourceReferenceRec extends ResourceRefRec {

    // The external system name and its normalized database id (when we have it)
    private final String externalSystemName;
    private int externalSystemNameId = -1;

    // The external ref value and its normalized database id (when we have it)
    private final String externalRefValue;
    private long externalRefValueId = -1;

    public ExternalResourceReferenceRec(int parameterNameId, String resourceType, long resourceTypeId, long logicalResourceId,
        String externalSystemName, String externalRefValue) {
        super(parameterNameId, resourceType, resourceTypeId, logicalResourceId);
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
     * @return the externalSystemNameId
     */
    public int getExternalSystemNameId() {
        return externalSystemNameId;
    }

    /**
     * @param externalSystemNameId the externalSystemNameId to set
     */
    public void setExternalSystemNameId(int externalSystemNameId) {
        this.externalSystemNameId = externalSystemNameId;
    }

    /**
     * @return the externalRefValue
     */
    public String getExternalRefValue() {
        return externalRefValue;
    }

    /**
     * @return the externalRefValueId
     */
    public long getExternalRefValueId() {
        return externalRefValueId;
    }

    /**
     * @param externalRefValueId the externalRefValueId to set
     */
    public void setExternalRefValueId(long externalRefValueId) {
        this.externalRefValueId = externalRefValueId;
    }
}
