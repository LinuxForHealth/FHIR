/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.params.api;

import java.util.List;

import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;

/**
 * Our interface for handling messages received by the consumer. Used
 * to decouple the Kafka consumer from the database persistence logic
 */
public interface IMessageHandler {

    /**
     * Ask the handler to process the list of messages.
     * @param messages
     * @throws FHIRPersistenceException
     */
    void process(List<String> messages) throws FHIRPersistenceException;

    /**
     * Close any resources held by the handler
     */
    void close();
}
