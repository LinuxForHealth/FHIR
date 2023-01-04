/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.server.spi.operation;


/**
 * This class is used to represent a Resource context information.
 */
public class FHIRResourceContext {

    private String resourceType;

    private String id;

    private String versionId;

    

    /**
     * @param resourceType
     * @param id
     * @param versionId
     */
    public FHIRResourceContext(String resourceType, String id, String versionId) {
        super();
        this.resourceType = resourceType;
        this.id = id;
        this.versionId = versionId;
    }



    /**
     * @return the resourceType
     */
    public String getResourceType() {
        return resourceType;
    }

    
    /**
     * @param resourceType the resourceType to set
     */
    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    
    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    
    /**
     * @return the versionId
     */
    public String getVersionId() {
        return versionId;
    }

    
    /**
     * @param versionId the versionId to set
     */
    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }
}
