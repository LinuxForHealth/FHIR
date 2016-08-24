/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.audit.logging.beans;

/**
 * This class defines the ApiParameters section of the FHIR server AuditLogEntry.
 * @author markd
 *
 */
public class ApiParameters {
	
	private String request;
	private String response;

	
	public ApiParameters() {
		super();
	}


	public String getRequest() {
		return request;
	}


	public void setRequest(String request) {
		this.request = request;
	}
	
	public ApiParameters withRequest(String request) {
		this.request = request;
		return this;
	}


	public String getResponse() {
		return response;
	}


	public void setResponse(String response) {
		this.response = response;
	}
	
	public ApiParameters withResponse(String response) {
		this.response = response;
		return this;
	}

}
