/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.audit.logging.beans;

import com.fasterxml.jackson.annotation.*;



/**
 * This class defines the Context section of the FHIR server AuditLogEntry.
 * @author markd
 *
 */
@JsonPropertyOrder({"request_unique_id", "action", "operation_name", "purpose", "resource_name", "start_time", "end_time", "api_parameters", "query", "data", "batch" })
public class Context {
    
    @JsonProperty("request_unique_id")
    private String requestUniqueId;
    
    @JsonProperty("data")
    private Data data;
    
    @JsonProperty("action")
    private String action;
    
    @JsonProperty("operation_name")
    private String operationName;
    
    @JsonProperty("query_parameters")
    private String queryParameters;
    
    @JsonProperty("purpose")
    private String purpose;
    
    @JsonProperty("resource_name")
    private String resourceName;
    
    @JsonProperty("start_time")
    private String startTime;
    
    @JsonProperty("end_time")
    private String endTime;
    
    @JsonProperty("api_parameters")
    private ApiParameters apiParameters;
    
    @JsonProperty("batch")
    private Batch batch;


    public Context() {
        super();
    }


    public Context(Context fromObj) {
        super();
        this.action = fromObj.action;
        this.apiParameters = fromObj.apiParameters;
        this.batch = fromObj.batch;
        this.data = fromObj.data;
        this.endTime = fromObj.endTime;
        this.operationName = fromObj.operationName;
        this.purpose = fromObj.purpose;
        this.queryParameters = fromObj.queryParameters;
        this.requestUniqueId = fromObj.requestUniqueId;
        this.resourceName = fromObj.resourceName;
        this.startTime = fromObj.startTime;
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


    public String getQueryParameters() {
        return queryParameters;
    }


    public void setQueryParameters(String queryParms) {
        this.queryParameters = queryParms;
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


    public String getPurpose() {
        return purpose;
    }


    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }


    public String getOperationName() {
        return operationName;
    }


    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }


    public String getRequestUniqueId() {
        return requestUniqueId;
    }


    public void setRequestUniqueId(String requestUniqueId) {
        this.requestUniqueId = requestUniqueId;
    }


    public String getResourceName() {
        return resourceName;
    }


    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

}
