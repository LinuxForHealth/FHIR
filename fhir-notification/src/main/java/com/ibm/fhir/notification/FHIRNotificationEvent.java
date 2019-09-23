/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.notification;

import javax.ws.rs.core.HttpHeaders;

import com.ibm.fhir.model.resource.Resource;

public class FHIRNotificationEvent {
    private String lastUpdated = null;
    private String location = null;
    private String operationType = null;
    private String resourceId = null;
    private Resource resource = null;
    private HttpHeaders httpHeaders = null;

    public FHIRNotificationEvent() {
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FHIRNotificationEvent["
                + "operation=" + getOperationType()
                + ", resourceId=" + getResourceId()
                + ", location=" + getLocation()
                + ", lastUpdated=" + getLastUpdated()
                + ", httpHeaders=" + getHttpHeaders()
                + "]");
        return sb.toString();
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public HttpHeaders getHttpHeaders() {
        return this.httpHeaders;
    }

    public void setHttpHeaders(HttpHeaders httpHeaders) {
        this.httpHeaders = httpHeaders;
    }
}
