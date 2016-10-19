/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.audit.logging.beans;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;



/**
 * This class defines the Context section of the FHIR server AuditLogEntry.
 * @author markd
 *
 */
public class Context {
	
	private Data data;
	
	private String action;
	
	private String query;
	
	@SerializedName("value_old")
	private JsonObject valueOld;
	
	@SerializedName("value_new")
	private JsonObject valueNew;
	
	@SerializedName("reason_of_action")
	private String reasonOfAction;
	
	private String status;
	
	@SerializedName("start_time")
	private String startTime;
	
	@SerializedName("end_time")
	private String endTime;
	
	@SerializedName("api_parameters")
	private ApiParameters apiParameters;
	
	private Batch batch;


	public Context() {
		super();
	}


	public Data getData() {
		return data;
	}


	public void setData(Data data) {
		this.data = data;
	}


	public String getAction() {
		return action;
	}


	public void setAction(String action) {
		this.action = action;
	}


	public String getQuery() {
		return query;
	}


	public void setQuery(String query) {
		this.query = query;
	}


	public JsonObject getValueOld() {
		return valueOld;
	}


	public void setValueOld(JsonObject valueOld) {
		this.valueOld = valueOld;
	}


	public JsonObject getValueNew() {
		return valueNew;
	}


	public void setValueNew(JsonObject valueNew) {
		this.valueNew = valueNew;
	}


	public String getReasonOfAction() {
		return reasonOfAction;
	}


	public void setReasonOfAction(String reasonOfAction) {
		this.reasonOfAction = reasonOfAction;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getStartTime() {
		return startTime;
	}


	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}


	public String getEndTime() {
		return endTime;
	}


	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}


	public ApiParameters getApiParameters() {
		return apiParameters;
	}


	public void setApiParameters(ApiParameters apiParameters) {
		this.apiParameters = apiParameters;
	}


	public Batch getBatch() {
		return batch;
	}


	public void setBatch(Batch batch) {
		this.batch = batch;
	}

}
