/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.audit.beans;

/**
 * This class encapsulates the data for a FHIR server audit log entry.
 */
public class AuditLogEntry {

    private String componentId;

    private String componentIp;

    private String tenantId;

    private String location;

    private String eventType;

    private String timestamp;

    private String description;

    private String userName;

    private String clientCertCn;

    private String clientCertIssuerOu;

    private String correlationId;

    private String patientId;

    private Context context;

    private ConfigData configData;

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

    public ConfigData getConfigData() {
        return configData;
    }

    public void setConfigData(ConfigData configData) {
        this.configData = configData;
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