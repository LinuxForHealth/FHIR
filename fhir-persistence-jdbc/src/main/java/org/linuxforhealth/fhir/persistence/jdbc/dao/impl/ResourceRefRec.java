/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.dao.impl;


/**
 * DTO base representing a reference from a resource to another resource (local or external)
 */
public class ResourceRefRec {

    // The id of the parameter name representing this relationship
    private final String parameterName;
    
    // The database id from PARAMETER_NAMES. Set after the parameter is looked up
    private int parameterNameId = -1;

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
    public ResourceRefRec(String parameterName, String resourceType, long resourceTypeId, long logicalResourceId) {
        this.parameterName = parameterName;
        this.resourceType = resourceType;
        this.resourceTypeId = resourceTypeId;
        this.logicalResourceId = logicalResourceId;
    }
    
    /**
     * Getter for parameterName
     * @return
     */
    public String getParameterName() {
        return this.parameterName;
    }
    
    /**
     * Setter for the parameterNameId
     * @param id the database parameter_name_id from PARAMETER_NAMES
     */
    public void setParameterNameId(int id) {
        this.parameterNameId = id;
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
