/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.audit.logging.beans;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

/**
 * This class defines the Data section of the FHIR server AuditLogEntry.
 * @author markd
 *
 */
@JsonPropertyOrder({"resource_type", "id", "version_id" })
public class Data {
	
    @JsonProperty("resource_type")
    private String resourceType;
    
	@JsonProperty("id")
    private String id;
	
	@JsonProperty("version_id")
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

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }
    
    public Data withResourceType(String resourceType) {
        this.resourceType = resourceType;
        return this;
    }

}
