/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.remote.index.database;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.ibm.fhir.database.utils.thread.ThreadHandler;
import com.ibm.fhir.persistence.exception.FHIRPersistenceDataAccessException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.index.DateParameter;
import com.ibm.fhir.persistence.index.LocationParameter;
import com.ibm.fhir.persistence.index.NumberParameter;
import com.ibm.fhir.persistence.index.ProfileParameter;
import com.ibm.fhir.persistence.index.QuantityParameter;
import com.ibm.fhir.persistence.index.ReferenceParameter;
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

    // If we fail 10 times due to deadlocks, then something is seriously wrong
    private static final int MAX_TX_ATTEMPTS = 10;
    private SecureRandom random = new SecureRandom();

    private final long maxReadyWaitMs;
    /**
     * Protected constructor
     * @param maxReadyWaitMs the max time in ms to wait for the upstream transaction to make the data ready
     */
    protected BaseMessageHandler(long maxReadyWaitMs) {
        this.maxReadyWaitMs = maxReadyWaitMs;
    }

    @Override
    public void process(List<String> messages) throws FHIRPersistenceException {
        List<RemoteIndexMessage> unmarshalled = new ArrayList<>(messages.size());
        for (String payload: messages) {
            if (logger.isLoggable(Level.FINEST)) {
                logger.finest("Processing message payload: " + payload);
            }
            RemoteIndexMessage message = unmarshall(payload);
            if (message != null) {
                if (message.getMessageVersion() >= MIN_SUPPORTED_MESSAGE_VERSION) {
                    unmarshalled.add(message);
                } else {
                    logger.warning("Message version [" + message.getMessageVersion() + "] not supported, ignoring payload=[" + payload + "]");
                }
            }
        }
        processWithRetry(unmarshalled);
    }

    /**
     * Process the batch of messages with support for retries in the case
     * of a retryable error such as a database deadlock
     * 
     * @param messages
     * @throws FHIRPersistenceException
     */
    private void processWithRetry(List<RemoteIndexMessage> messages) throws FHIRPersistenceException {
        int attempt = 1;
        do {
            try {
                if (attempt > 1) {
                    // introduce a random delay before we re-attempt to process the batch. This
                    // may help to avoid subsequent deadlocks if there are multiple transactions
                    // involved
                    final long delay = random.nextInt(10) * 1000l;
                    logger.fine(() -> "Deadlock retry backoff ms: " + delay);
                    ThreadHandler.safeSleep(delay);
                }
                startBatch();
                processMessages(messages);
                pushBatch();

                attempt = MAX_TX_ATTEMPTS; // exit our do...while
            } catch (FHIRPersistenceDataAccessException x) {
                setRollbackOnly();
                // see if this is a retryable error
                if (x.isTransactionRetryable() && attempt++ < MAX_TX_ATTEMPTS) {
                    logger.warning("tx failed, but retry permitted: " + x.getMessage());
                    resetBatch(); // clear up any cruft from the previous attempt
                } else {
                    throw x;
                }
            } finally {
                endTransaction();
            }
        } while (attempt < MAX_TX_ATTEMPTS);
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
     * Reset the state of the handler following a failure so that the batch can
     * be retried
     */
    protected abstract void resetBatch();

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
     * Process the list of messages
     * @param messages
     * @throws FHIRPersistenceException
     */
    private void processMessages(List<RemoteIndexMessage> messages) throws FHIRPersistenceException {
        // We need to do a quick scan of all the messages to make sure that
        // the logical resource records for each already exist. If prepare
        // returns false, it means one of two things:
        // 1. we received the message before the server transaction committed
        // 2. the server transaction failed/rolled back, so we'll never be ready
        long timeoutTime = System.nanoTime() + this.maxReadyWaitMs * 1000000;

        // Messages which match the current version info in the database
        List<RemoteIndexMessage> okToProcess = new ArrayList<>();

        // resources which don't yet exist of their version is older than the message
        List<RemoteIndexMessage> notReady = new ArrayList<>();

        // make at least one attempt
        do {
            if (okToProcess.size() > 0) {
                okToProcess.clear(); // reset ready for next prepare call
            }
            if (notReady.size() > 0) {
                notReady.clear(); // reset ready for next prepare call
            }

            // Ask the handle to check which messages match the database
            // and are therefore ready to be processed
            checkReady(messages, okToProcess, notReady);

            // If the ready check fails just sleep for a bit because we need
            // to wait until the upstream transaction commits. This means we
            // may need to keep waiting for a long time which unfortunately
            // stalls processing this partition
            if (notReady.size() > 0) {
                long snoozeMs = Math.min(1000l, (timeoutTime - System.nanoTime()) / 1000000);
                // short sleep to wait for the upstream transaction to complete
                if (snoozeMs > 0) {
                    ThreadHandler.safeSleep(snoozeMs);
                }
            }
        } while (notReady.size() > 0 && System.nanoTime() < timeoutTime);

        // okToProcess contains those messages for which we see the upstream transaction
        // has committed.
        for (RemoteIndexMessage message: okToProcess) {
            process(message);
        }

        // Make a note of which messages we were unable to process because the upstream
        // transaction did not commit before our maxReadyWaitMs timeout
        for (RemoteIndexMessage message: notReady) {
            logger.warning("Timed out waiting for upstream transaction to commit data for: " + message.toString());
        }
    }

    /**
     * Check to see if the database is ready to process the messages
     * @param  IN: messages to check
     * @param OUT: okToMessages the messages matching the current database
     * @param OUT: notReady the messages for which the upstream transaction has yet to commit
     */
    protected abstract void checkReady(List<RemoteIndexMessage> messages, List<RemoteIndexMessage> okToProcess, List<RemoteIndexMessage> notReady) throws FHIRPersistenceException;

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

        if (params.getRefValues() != null) {
            for (ReferenceParameter p: params.getRefValues()) {
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
     * 
     * @param tenantId
     * @param requestShard
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param p
     * @throws FHIRPersistenceException
     */
    protected abstract void process(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, ReferenceParameter p) throws FHIRPersistenceException;

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
