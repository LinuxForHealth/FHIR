/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.connection;

import com.ibm.fhir.persistence.FHIRPersistenceTransaction;

/**
 * Factory/strategy for creating instances of FHIRPersistenceTransaction
 */
public interface FHIRTransactionFactory {
    
    /**
     * Create a new instance of {@link FHIRPersistenceTransaction}
     * @return
     */
    FHIRPersistenceTransaction create();
}
