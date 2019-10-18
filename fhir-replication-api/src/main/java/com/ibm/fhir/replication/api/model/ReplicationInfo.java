/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.replication.api.model;

import java.util.Date;

public class ReplicationInfo {
    
    private String sourceKey;
    private String serviceId;
    private Date creationTime;
    private int versionId;
    private String txCorrelationId;
    private String changedBy;
    private String correlationToken;
    private String tenantId;
    private String reason;
    private String event;
    private String resourceType;
    private String siteId;
    private String studyId;
    private String patientId;
    private String resourceName;
    
    private String resourceRef;
    private String previousResourceRef;
    
    public String getSourceKey() {
        return sourceKey;
    }
    public void setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey;
    }
    public String getServiceId() {
        return serviceId;
    }
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
    public Date getCreationTime() {
        return creationTime;
    }
    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }
    public int getVersionId() {
        return versionId;
    }
    public void setVersionId(int versionId) {
        this.versionId = versionId;
    }
    public String getTxCorrelationId() {
        return txCorrelationId;
    }
    public void setTxCorrelationId(String txCorrelationId) {
        this.txCorrelationId = txCorrelationId;
    }
    public String getChangedBy() {
        return changedBy;
    }
    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }
    public String getCorrelationToken() {
        return correlationToken;
    }
    public void setCorrelationToken(String correlationToken) {
        this.correlationToken = correlationToken;
    }
    public String getTenantId() {
        return tenantId;
    }
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    public String getEvent() {
        return event;
    }
    public void setEvent(String event) {
        this.event = event;
    }
    public String getResourceType() {
        return resourceType;
    }
    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }
    public String getSiteId() {
        return siteId;
    }
    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }
    public String getStudyId() {
        return studyId;
    }
    public void setStudyId(String studyId) {
        this.studyId = studyId;
    }
    public String getPatientId() {
        return patientId;
    }
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
    public String getResourceRef() {
        return resourceRef;
    }
    public void setResourceRef(String resourceRef) {
        this.resourceRef = resourceRef;
    }
    public String getPreviousResourceRef() {
        return previousResourceRef;
    }
    public void setPreviousResourceRef(String previousResourceRef) {
        this.previousResourceRef = previousResourceRef;
    }
    public String getResourceName() {
        return resourceName;
    }
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
}
