/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.audit.logging.beans;

/**
 * This class defines the Data section of the FHIR server AuditLogEntry.
 * @author markd
 *
 */
public class Data {
	
	private String id;
	private String versionId;

	public Data() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public Data withId(String id) {
		this.id = id;
		return this;
	}

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	
	public Data withVersionId(String versionId) {
		this.versionId = versionId;
		return this;
	}

}
