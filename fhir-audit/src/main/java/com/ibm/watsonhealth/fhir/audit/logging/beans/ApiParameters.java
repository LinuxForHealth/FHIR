/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.audit.logging.beans;

import com.fasterxml.jackson.annotation.*;

/**
 * This class defines the ApiParameters section of the FHIR server AuditLogEntry.
 * @author markd
 *
 */
public class ApiParameters {
    
    @JsonProperty("request")
    private String request;
    
    @JsonProperty("response_status")
    private int status;

    
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


    public int getStatus() {
        return status;
    }


    public void setStatus(int status) {
        this.status = status;
    }
    
    public ApiParameters withStatus(int status) {
        this.status = status;
        return this;
    }

}
