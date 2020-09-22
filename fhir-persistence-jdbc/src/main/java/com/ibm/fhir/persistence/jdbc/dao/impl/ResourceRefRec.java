/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.impl;


/**
 * DTO base representing a reference from a resource to another resource (local or external)
 */
public class ResourceRefRec {

    // The id of the parameter name representing this relationship
    private final int parameterNameId;

    // The type name/id of this resource
    private final String resourceType;
    private final long resourceTypeId;
    
    // The logical id of this resource and its normalized database id (when we have it)
    private final String logicalId;
    private long logicalResourceId = -1;

    /**
     * Public constructor
     * @param parameterNameId
     * @param resourceType
     * @param resourceTypeId
     * @param logicalId
     */
    public ResourceRefRec(int parameterNameId, String resourceType, long resourceTypeId, String logicalId) {
        this.parameterNameId = parameterNameId;
        this.resourceType = resourceType;
        this.resourceTypeId = resourceTypeId;
        this.logicalId = logicalId;
    }

    /**
     * @return the parameterNameId
     */
    public int getParameterNameId() {
        return parameterNameId;
    }
    

    /**
     * @return the resourceType
     */
    public String getResourceType() {
        return resourceType;
    }

    /**
     * @return the resourceTypeId
     */
    public long getResourceTypeId() {
        return resourceTypeId;
    }

    /**
     * @return the logicalId
     */
    public String getLogicalId() {
        return logicalId;
    }

    /**
     * @return the logicalResourceId
     */
    public long getLogicalResourceId() {
        return logicalResourceId;
    }

    /**
     * @param logicalResourceId the logicalResourceId to set
     */
    public void setLogicalResourceId(long logicalResourceId) {
        this.logicalResourceId = logicalResourceId;
    }

}
