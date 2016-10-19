/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.audit.logging.beans;

import com.google.gson.annotations.SerializedName;

/**
 * This class encapsulates the data for a FHIR server audit log entry.
 * @author markd
 *
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
	
	private String timestamp;
	
	private String description;
	
	@SerializedName("user_name")
	private String userName;
	
	private Context context;
	
	@SerializedName("config_data")
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

}
