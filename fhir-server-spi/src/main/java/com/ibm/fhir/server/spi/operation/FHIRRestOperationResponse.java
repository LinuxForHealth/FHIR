/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.spi.operation;

import java.net.URI;
import java.util.concurrent.Future;

import javax.ws.rs.core.Response;

import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.persistence.payload.PayloadKey;

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

    // Flag to indicate the request is complete and can be returned as-is
    private boolean completed;

    // A nested response we may get when offloading payload storage (e.g. in COS, Cassandra)
    private Future<PayloadKey> storePayloadResponse;

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

    public FHIRRestOperationResponse(Resource resource, String resourceId, Future<PayloadKey> storePayloadResponse) {
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
    public Future<PayloadKey> getStorePayloadResponse() {
        return storePayloadResponse;
    }

    /**
     * @param storePayloadResponse the storePayloadResponse to set
     */
    public void setStorePayloadResponse(Future<PayloadKey> storePayloadResponse) {
        this.storePayloadResponse = storePayloadResponse;
    }
}
