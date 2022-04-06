/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.flow.impl;

import com.ibm.fhir.flow.api.ResourceIdentifierVersion;
import com.ibm.fhir.model.resource.Resource;

/**
 * The result from reading the resource from the upstream system
 */
public class FlowFetchResult {
    private int status;
    private Resource resource;
    private ResourceIdentifierVersion location;

    /**
     * Public constructor
     * 
     * @param location
     */
    public FlowFetchResult(ResourceIdentifierVersion location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return location.toString();
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }
    
    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }
    
    /**
     * @return the resource
     */
    public Resource getResource() {
        return resource;
    }
    
    /**
     * @param resource the resource to set
     */
    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
