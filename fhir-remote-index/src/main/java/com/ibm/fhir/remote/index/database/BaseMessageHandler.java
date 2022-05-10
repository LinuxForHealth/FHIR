/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.remote.index.database;

import java.util.List;

import com.google.gson.Gson;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
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
 * Base for the Kafka message handler to load message data into
 * a database via JDBC.
 */
public abstract class BaseMessageHandler implements IMessageHandler {

    @Override
    public void process(List<String> messages) throws FHIRPersistenceException {
        try {
            for (String payload: messages) {
                RemoteIndexMessage message = unmarshall(payload);
                process(message);
            }
            pushBatch();
        } catch (Throwable t) {
            setRollbackOnly();
            throw t;
        } finally {
            endTransaction();
        }
    }

    /**
     * Mark the transaction for rollback
     */
    protected abstract void setRollbackOnly();

    /**
     * Push any data we've accumulated from processing messages.
     */
    protected abstract void pushBatch() throws FHIRPersistenceException;

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
    private void process(RemoteIndexMessage message) throws FHIRPersistenceException {
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
    protected abstract void process(String tenantId, String resourceType, String logicalId, long logicalResourceId, LocationParameter p) throws FHIRPersistenceException;

    /**
     * @param tenantId
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param p
     */
    protected abstract void process(String tenantId, String resourceType, String logicalId, long logicalResourceId, TokenParameter p) throws FHIRPersistenceException;

    /**
     * @param tenantId
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param p
     */
    protected abstract void process(String tenantId, String resourceType, String logicalId, long logicalResourceId, QuantityParameter p) throws FHIRPersistenceException;

    /**
     * @param tenantId
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param p
     */
    protected abstract void process(String tenantId, String resourceType, String logicalId, long logicalResourceId, NumberParameter p) throws FHIRPersistenceException;

    /**
     * @param tenantId
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param p
     */
    protected abstract void process(String tenantId, String resourceType, String logicalId, long logicalResourceId, DateParameter p) throws FHIRPersistenceException;

    /**
     * @param tenantId
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param p
     */
    protected abstract void process(String tenantId, String resourceType, String logicalId, long logicalResourceId, StringParameter p) throws FHIRPersistenceException;

    protected abstract void endTransaction() throws FHIRPersistenceException;
}
