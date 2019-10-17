/*
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc;

import java.util.logging.Logger;

import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.FHIRPersistenceFactory;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.impl.FHIRPersistenceJDBCNormalizedImpl;

/**
 * Factory which serves up instances of the JDBC persistence implementation.
 */
public class FHIRPersistenceJDBCFactory implements FHIRPersistenceFactory {
    private static final Logger log = Logger.getLogger(FHIRPersistenceJDBCFactory.class.getName());

    /*
     * (non-Javadoc)
     * @see com.ibm.fhir.persistence.FHIRPersistenceFactory#getInstance()
     */
    @Override
    public FHIRPersistence getInstance() throws FHIRPersistenceException {
        try {
            return new FHIRPersistenceJDBCNormalizedImpl();
        } catch (Exception e) {
            throw new FHIRPersistenceException("Unexpected exception while creating JDBC persistence layer: ", e); 
        }
    }
}
