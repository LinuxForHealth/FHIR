/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.remote.index.api;

import java.util.List;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * Our interface for handling messages received by the consumer. Used
 * to decouple the Kafka consumer from the database persistence logic
 */
public interface IMessageHandler {

    void process(List<String> messages) throws FHIRPersistenceException;

    /**
     * Close any resources held by the handler
     */
    void close();
}
