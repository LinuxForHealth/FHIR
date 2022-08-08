/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.notification;

import org.linuxforhealth.fhir.model.resource.Resource;

public class FHIRNotificationEvent {
    private String lastUpdated = null;
    private String location = null;
    private String operationType = null;
    private String resourceId = null;
    private String tenantId = null;
    private String datasourceId = null;
    private Resource resource = null;

    public FHIRNotificationEvent() {
        // No Operation
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

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(String datasourceId) {
        this.datasourceId = datasourceId;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FHIRNotificationEvent["
                + "operation=" + getOperationType()
                + ", resourceId=" + getResourceId()
                + ", location=" + getLocation()
                + ", lastUpdated=" + getLastUpdated()
                + ", datasourceId=" + getDatasourceId()
                + ", tenantId=" + getTenantId()
                + "]");
        return sb.toString();
    }
}