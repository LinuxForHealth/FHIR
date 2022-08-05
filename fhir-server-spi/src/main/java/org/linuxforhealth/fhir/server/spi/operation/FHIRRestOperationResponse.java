/*
 * (C) Copyright IBM Corp. 2017, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.spi.operation;

import java.net.URI;

import javax.ws.rs.core.Response;

import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.persistence.payload.PayloadPersistenceResponse;

/**
 * This class is used to represent a response returned by the FHIR resource helper methods.
 */
public class FHIRRestOperationResponse {
    private Response.Status status;
    private URI locationURI;
    private Resource resource;
    private Resource prevResource;
    private OperationOutcome operationOutcome;
    private boolean deleted;

    // For delete we need to return the version of the deletion marker
    private int versionForETag;

    // Flag to indicate the request is complete and can be returned as-is
    private boolean completed;

    // A nested response we may get when offloading payload storage (e.g. in COS, Cassandra)
    private PayloadPersistenceResponse storePayloadResponse;

    // The id of the resource, which could be new in the case of create
    private String resourceId;

    public FHIRRestOperationResponse() {
    }

    public FHIRRestOperationResponse(Response.Status status, URI locationURI, Resource resource) {
        setStatus(status);
        setLocationURI(locationURI);
        setResource(resource);
    }
    public FHIRRestOperationResponse(Response.Status status, URI locationURI, OperationOutcome operationOutcome) {
        setStatus(status);
        setLocationURI(locationURI);
        setOperationOutcome(operationOutcome);
    }

    public FHIRRestOperationResponse(Resource resource, String resourceId, PayloadPersistenceResponse storePayloadResponse) {
        this.resource = resource;
        this.resourceId = resourceId;
        this.setStorePayloadResponse(storePayloadResponse);
    }

    public Response.Status getStatus() {
        return status;
    }
    public void setStatus(Response.Status status) {
        this.status = status;
    }
    public URI getLocationURI() {
        return locationURI;
    }
    public void setLocationURI(URI locationURI) {
        this.locationURI = locationURI;
    }
    public Resource getResource() {
        return resource;
    }
    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Resource getPrevResource() {
        return prevResource;
    }

    public void setPrevResource(Resource prevResource) {
        this.prevResource = prevResource;
    }

    public OperationOutcome getOperationOutcome() {
        return operationOutcome;
    }

    public void setOperationOutcome(OperationOutcome operationOutcome) {
        this.operationOutcome = operationOutcome;
    }

    /**
     * Getter for the resourceId
     * @return
     */
    public String getResourceId() {
        return this.resourceId;
    }

    /**
     * @return the deleted
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * @param deleted the deleted to set
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * @return the completed
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * @param completed the completed to set
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    /**
     * @return the storePayloadResponse
     */
    public PayloadPersistenceResponse getStorePayloadResponse() {
        return storePayloadResponse;
    }

    /**
     * @param storePayloadResponse the storePayloadResponse to set
     */
    public void setStorePayloadResponse(PayloadPersistenceResponse storePayloadResponse) {
        this.storePayloadResponse = storePayloadResponse;
    }

    /**
     * @return the versionForETag
     */
    public int getVersionForETag() {
        return versionForETag;
    }

    /**
     * @param versionForETag the versionForETag to set
     */
    public void setVersionForETag(int versionForETag) {
        this.versionForETag = versionForETag;
    }
}