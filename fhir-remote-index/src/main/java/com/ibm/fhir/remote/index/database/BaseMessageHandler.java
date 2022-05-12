/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.remote.index.database;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.index.DateParameter;
import com.ibm.fhir.persistence.index.LocationParameter;
import com.ibm.fhir.persistence.index.NumberParameter;
import com.ibm.fhir.persistence.index.ProfileParameter;
import com.ibm.fhir.persistence.index.QuantityParameter;
import com.ibm.fhir.persistence.index.RemoteIndexMessage;
import com.ibm.fhir.persistence.index.SearchParametersTransport;
import com.ibm.fhir.persistence.index.SecurityParameter;
import com.ibm.fhir.persistence.index.StringParameter;
import com.ibm.fhir.persistence.index.TagParameter;
import com.ibm.fhir.persistence.index.TokenParameter;
import com.ibm.fhir.remote.index.api.IMessageHandler;


/**
 * Base for the Kafka message handler to load message data into
 * a database via JDBC.
 */
public abstract class BaseMessageHandler implements IMessageHandler {
    private final Logger logger = Logger.getLogger(BaseMessageHandler.class.getName());
    private static final int MIN_SUPPORTED_MESSAGE_VERSION = 1;

    @Override
    public void process(List<String> messages) throws FHIRPersistenceException {
        try {
            startBatch();
            for (String payload: messages) {
                if (logger.isLoggable(Level.FINEST)) {
                    logger.finest("Processing message payload: " + payload);
                }
                RemoteIndexMessage message = unmarshall(payload);
                if (message != null) {
                    if (message.getMessageVersion() >= MIN_SUPPORTED_MESSAGE_VERSION) {
                        process(message);
                    } else {
                        logger.warning("Message version [" + message.getMessageVersion() + "] not supported, ignoring payload=[" + payload + "]");
                    }
                }
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
     * Called before we start processing a new batch of messages
     */
    protected abstract void startBatch();

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
        try {
            return gson.fromJson(jsonPayload, RemoteIndexMessage.class);
        } catch (Throwable t) {
            // We need to sink this error to avoid poison messages from 
            // blocking the queues.
            // TODO. Perhaps push this to a dedicated error topic
            logger.severe("Not a RemoteIndexMessage. Ignoring: '" + jsonPayload + "'");
        }
        return null;
    }
    /**
     * Process the data 
     * @param message
     */
    private void process(RemoteIndexMessage message) throws FHIRPersistenceException {
        SearchParametersTransport params = message.getData();
        if (params.getStringValues() != null) {
            for (StringParameter p: params.getStringValues()) {
                process(message.getTenantId(), params.getRequestShard(), params.getResourceType(), params.getLogicalId(), params.getLogicalResourceId(), p);
            }
        }

        if (params.getDateValues() != null) {
            for (DateParameter p: params.getDateValues()) {
                process(message.getTenantId(), params.getRequestShard(), params.getResourceType(), params.getLogicalId(), params.getLogicalResourceId(), p);
            }
        }

        if (params.getNumberValues() != null) {
            for (NumberParameter p: params.getNumberValues()) {
                process(message.getTenantId(), params.getRequestShard(), params.getResourceType(), params.getLogicalId(), params.getLogicalResourceId(), p);
            }
        }

        if (params.getQuantityValues() != null) {
            for (QuantityParameter p: params.getQuantityValues()) {
                process(message.getTenantId(), params.getRequestShard(), params.getResourceType(), params.getLogicalId(), params.getLogicalResourceId(), p);                
            }
        }

        if (params.getTokenValues() != null) {
            for (TokenParameter p: params.getTokenValues()) {
                process(message.getTenantId(), params.getRequestShard(), params.getResourceType(), params.getLogicalId(), params.getLogicalResourceId(), p);
            }
        }

        if (params.getLocationValues() != null) {
            for (LocationParameter p: params.getLocationValues()) {
                process(message.getTenantId(), params.getRequestShard(), params.getResourceType(), params.getLogicalId(), params.getLogicalResourceId(), p);
            }
        }

        if (params.getTagValues() != null) {
            for (TagParameter p: params.getTagValues()) {
                process(message.getTenantId(), params.getRequestShard(), params.getResourceType(), params.getLogicalId(), params.getLogicalResourceId(), p);
            }
        }

        if (params.getProfileValues() != null) {
            for (ProfileParameter p: params.getProfileValues()) {
                process(message.getTenantId(), params.getRequestShard(), params.getResourceType(), params.getLogicalId(), params.getLogicalResourceId(), p);
            }
        }

        if (params.getSecurityValues() != null) {
            for (SecurityParameter p: params.getSecurityValues()) {
                process(message.getTenantId(), params.getRequestShard(), params.getResourceType(), params.getLogicalId(), params.getLogicalResourceId(), p);
            }
        }
    }

    /**
     * @param tenantId
     * @param requestShard
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param p
     */
    protected abstract void process(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, LocationParameter p) throws FHIRPersistenceException;

    /**
     * @param tenantId
     * @param requestShard
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param p
     */
    protected abstract void process(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, TokenParameter p) throws FHIRPersistenceException;

    /**
     * 
     * @param tenantId
     * @param requestShard
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param p
     * @throws FHIRPersistenceException
     */
    protected abstract void process(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, TagParameter p) throws FHIRPersistenceException;

    /**
     * 
     * @param tenantId
     * @param requestShard
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param p
     * @throws FHIRPersistenceException
     */
    protected abstract void process(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, ProfileParameter p) throws FHIRPersistenceException;

    /**
     * 
     * @param tenantId
     * @param requestShard
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param p
     * @throws FHIRPersistenceException
     */
    protected abstract void process(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, SecurityParameter p) throws FHIRPersistenceException;

    /**
     * @param tenantId
     * @param requestShard
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param p
     */
    protected abstract void process(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, QuantityParameter p) throws FHIRPersistenceException;

    /**
     * @param tenantId
     * @param requestShard
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param p
     */
    protected abstract void process(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, NumberParameter p) throws FHIRPersistenceException;

    /**
     * @param tenantId
     * @param requestShard
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param p
     */
    protected abstract void process(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, DateParameter p) throws FHIRPersistenceException;

    /**
     * @param tenantId
     * @param requestShard
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param p
     */
    protected abstract void process(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, StringParameter p) throws FHIRPersistenceException;

    /**
     * Tell the persistence layer to commit the current transaction, or perform a rollback
     * if setRollbackOnly() has been called
     * @throws FHIRPersistenceException
     */
    protected abstract void endTransaction() throws FHIRPersistenceException;
}
