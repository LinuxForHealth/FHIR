/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.audit.logging.beans;

import com.fasterxml.jackson.annotation.*;

/**
 * This class defines the Batch parameters section of the FHIR server AuditLogEntry.
 * @author markd
 *
 */
public class Batch {
    
    private String status;
    
    @JsonProperty("resources_read")
    private Long resourcesRead;
    
    @JsonProperty("resources_created")
    private Long resourcesCreated;
    
    @JsonProperty("resources_updated")
    private Long resourcesUpdated;
    
    public Batch() {
        super();
    }


    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }


    public Long getResourcesRead() {
        return resourcesRead;
    }

    
    public void setResourcesRead(Long recsRead) {
        this.resourcesRead = recsRead;
    }
    
    public Batch withResourcesRead(Long recsRead) {
        this.resourcesRead = recsRead;
        return this;
    }
    
    public Long getResourcesCreated() {
        return resourcesCreated;
    }


    public void setResourcesCreated(Long recsCreated) {
        this.resourcesCreated = recsCreated;
    }
    
    public Batch withResourcesCreated(Long recsCreated) {
        this.resourcesCreated = recsCreated;
        return this;
    }


    public Long getResourcesUpdated() {
        return resourcesUpdated;
    }


    public void setResourcesUpdated(Long resourcesUpdated) {
        this.resourcesUpdated = resourcesUpdated;
    }
    
    public Batch withResourcesUpdated(Long resourcesUpdated) {
        this.resourcesUpdated = resourcesUpdated;
        return this;
    }

}
