/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.remote.index.database;

import java.util.List;

import com.google.gson.Gson;
import com.ibm.fhir.persistence.index.DateParameter;
import com.ibm.fhir.persistence.index.LocationParameter;
import com.ibm.fhir.persistence.index.NumberParameter;
import com.ibm.fhir.persistence.index.QuantityParameter;
import com.ibm.fhir.persistence.index.RemoteIndexMessage;
import com.ibm.fhir.persistence.index.SearchParametersTransport;
import com.ibm.fhir.persistence.index.StringParameter;
import com.ibm.fhir.persistence.index.TokenParameter;
import com.ibm.fhir.remote.index.api.IMessageHandler;


/**
 *
 */
public abstract class BaseMessageHandler implements IMessageHandler {

    @Override
    public void process(List<String> messages) {
        for (String payload: messages) {
            RemoteIndexMessage message = unmarshall(payload);
            process(message);
        }
        pushBatch();
        commit();
    }

    /**
     * Push any data we've accumulated from processing messages.
     */
    protected abstract void pushBatch();

    /**
     * Unmarshall the json payload string into a RemoteIndexMessage
     * @param payload
     * @return
     */
    private RemoteIndexMessage unmarshall(String jsonPayload) {
        Gson gson = new Gson();
        return gson.fromJson(jsonPayload, RemoteIndexMessage.class);
    }
    /**
     * Process the data 
     * @param message
     */
    private void process(RemoteIndexMessage message) {
        SearchParametersTransport params = message.getData();
        if (params.getStringValues() != null) {
            for (StringParameter p: params.getStringValues()) {
                process(message.getTenantId(), params.getResourceType(), params.getLogicalId(), params.getLogicalResourceId(), p);
            }
        }

        if (params.getDateValues() != null) {
            for (DateParameter p: params.getDateValues()) {
                process(message.getTenantId(), params.getResourceType(), params.getLogicalId(), params.getLogicalResourceId(), p);
            }
        }

        if (params.getNumberValues() != null) {
            for (NumberParameter p: params.getNumberValues()) {
                process(message.getTenantId(), params.getResourceType(), params.getLogicalId(), params.getLogicalResourceId(), p);
            }
        }

        if (params.getQuantityValues() != null) {
            for (QuantityParameter p: params.getQuantityValues()) {
                process(message.getTenantId(), params.getResourceType(), params.getLogicalId(), params.getLogicalResourceId(), p);                
            }
        }

        if (params.getTokenValues() != null) {
            for (TokenParameter p: params.getTokenValues()) {
                process(message.getTenantId(), params.getResourceType(), params.getLogicalId(), params.getLogicalResourceId(), p);
            }
        }

        if (params.getLocationValues() != null) {
            for (LocationParameter p: params.getLocationValues()) {
                process(message.getTenantId(), params.getResourceType(), params.getLogicalId(), params.getLogicalResourceId(), p);
            }
        }
    }

    /**
     * @param tenantId
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param p
     */
    protected abstract void process(String tenantId, String resourceType, String logicalId, long logicalResourceId, LocationParameter p);

    /**
     * @param tenantId
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param p
     */
    protected abstract void process(String tenantId, String resourceType, String logicalId, long logicalResourceId, TokenParameter p);

    /**
     * @param tenantId
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param p
     */
    protected abstract void process(String tenantId, String resourceType, String logicalId, long logicalResourceId, QuantityParameter p);

    /**
     * @param tenantId
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param p
     */
    protected abstract void process(String tenantId, String resourceType, String logicalId, long logicalResourceId, NumberParameter p);

    /**
     * @param tenantId
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param p
     */
    protected abstract void process(String tenantId, String resourceType, String logicalId, long logicalResourceId, DateParameter p);

    /**
     * @param tenantId
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param p
     */
    protected abstract void process(String tenantId, String resourceType, String logicalId, long logicalResourceId, StringParameter p);

    private void commit() {
        
    }
}
