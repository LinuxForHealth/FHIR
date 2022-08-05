/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.dao.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.persistence.jdbc.TransactionData;

/**
 * Hold data accumulated during a transaction which we want to persist just
 * prior to commit. Because multiple datasources could be involved in a 
 * distributed transaction, we need to hold TransactionData for each.
 * Note that because all of this relates to a single transaction, this
 * also means that it's a single thread, so there are no concurrency issues.
 */
public class TransactionDataImpl<T extends TransactionData> implements TransactionData {
    private static final Logger logger = Logger.getLogger(TransactionDataImpl.class.getName());
    
    // The map holding TransactionData instances for each datasource id
    private final Map<String,T> datasourceMap = new HashMap<>();
    
    // The function used to create an instance of TransactionData for a new datasource
    private final Function<String, T> mappingFunction;
    
    /**
     * Public constructor
     * @param mappingFunction Function to create new TransactionData instances if they don't yet exist
     */
    public TransactionDataImpl(Function<String, T> mappingFunction) {
        this.mappingFunction = mappingFunction;
    }

    @Override
    public void persist() {
        // order isn't important, and the contract does not permit
        // exceptions to be thrown, making this easy
        long start = System.nanoTime();
        datasourceMap.values().forEach(td -> td.persist());
        
        if (logger.isLoggable(Level.FINE)) {
            long end = System.nanoTime();
            double elapsed = (end - start) / 1e9;
            logger.fine(String.format("persisted transaction data [took %5.3f s]", elapsed));
        }
    }

    /**
     * Get the TransactionData for the given datasourceName, creating a new instance
     * one currently doesn't exist
     * @param datasourceName
     * @return
     */
    public T findOrCreate(String datasourceName) {
        return datasourceMap.computeIfAbsent(datasourceName, mappingFunction);
    }
}