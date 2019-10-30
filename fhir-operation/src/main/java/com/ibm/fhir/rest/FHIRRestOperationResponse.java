/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.rest;

import java.net.URI;

import javax.ws.rs.core.Response;

import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Resource;

/**
 * This class is used to represent a response returned by the FHIR resource helper methods.
 */
public class FHIRRestOperationResponse {
    private Response.Status status;
    private URI locationURI;
    private Resource resource;
    private Resource prevResource;
    private OperationOutcome operationOutcome;
    
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
}
