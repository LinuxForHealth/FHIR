/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.audit.logging.beans;

import com.google.gson.annotations.SerializedName;

/**
 * This class encapsulates the data for a FHIR server audit log entry.
 */
public class AuditLogEntry {

    @SerializedName("component_id")
    private String componentId;

    @SerializedName("component_ip")
    private String componentIp;

    @SerializedName("tenant_id")
    private String tenantId;

    private String location;

    @SerializedName("event_type")
    private String eventType;

    @SerializedName("timestamp")
    private String timestamp;

    @SerializedName("description")
    private String description;

    @SerializedName("user_name")
    private String userName;

    @SerializedName("client_cert_cn")
    private String clientCertCn;

    @SerializedName("client_cert_issuer_ou")
    private String clientCertIssuerOu;

    @SerializedName("correlation_id")
    private String correlationId;

    @SerializedName("patient_id")
    private String patientId;

    @SerializedName("context")
    private Context context;

    public AuditLogEntry(String componentId, String eventType, String timestamp, String componentIp, String tenantId) {
        super();
        this.setComponentId(componentId);
        this.setEventType(eventType);
        this.setTimestamp(timestamp);
        this.setComponentIp(componentIp);
        this.setTenantId(tenantId);
    }

    public String getComponentId() {
        return componentId;
    }

    private void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getComponentIp() {
        return componentIp;
    }

    private void setComponentIp(String componentIp) {
        this.componentIp = componentIp;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEventType() {
        return eventType;
    }

    private void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    private void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getClientCertCn() {
        return clientCertCn;
    }

    public void setClientCertCn(String clientCertCn) {
        this.clientCertCn = clientCertCn;
    }

    public String getClientCertIssuerOu() {
        return clientCertIssuerOu;
    }

    public void setClientCertIssuerOu(String clientCertIssuerOu) {
        this.clientCertIssuerOu = clientCertIssuerOu;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
}