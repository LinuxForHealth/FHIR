/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.connection;

import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.persistence.FHIRPersistenceTransaction;

/**
 * Factory/strategy to instantiate FHIRTestTransactionHandler implementations
 * of the {@link FHIRPersistenceTransaction} interface. Typically used
 * to support unit-tests
 */
@Deprecated
public class FHIRTestTransactionFactory implements FHIRTransactionFactory {

    private final IConnectionProvider connectionProvider;
    
    /**
     * Public constructor
     * @param cp the {@link IConnectionProvider}
     */
    public FHIRTestTransactionFactory(IConnectionProvider cp) {
        this.connectionProvider = cp;
    }
    
    @Override
    public FHIRPersistenceTransaction create() {
        return new FHIRTestTransactionAdapter(connectionProvider, null);
    }

}
