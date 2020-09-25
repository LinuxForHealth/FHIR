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
    
    // The logical resource id of the resource owning the references
    private final long logicalResourceId;

    /**
     * Public constructor
     * @param parameterNameId
     * @param resourceType
     * @param resourceTypeId
     * @param logicalId
     */
    public ResourceRefRec(int parameterNameId, String resourceType, long resourceTypeId, long logicalResourceId) {
        this.parameterNameId = parameterNameId;
        this.resourceType = resourceType;
        this.resourceTypeId = resourceTypeId;
        this.logicalResourceId = logicalResourceId;
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
     * @return the logicalResourceId
     */
    public long getLogicalResourceId() {
        return logicalResourceId;
    }
}
