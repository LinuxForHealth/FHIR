/*
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc;

import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.FHIRPersistenceFactory;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.impl.FHIRPersistenceJDBCImpl;

/**
 * Factory which serves up instances of the JDBC persistence implementation.
 */
public class FHIRPersistenceJDBCFactory implements FHIRPersistenceFactory {
    @Override
    public FHIRPersistence getInstance() throws FHIRPersistenceException {
        try {
            return new FHIRPersistenceJDBCImpl();
        } catch (Exception e) {
            throw new FHIRPersistenceException("Unexpected exception while creating JDBC persistence layer: ", e); 
        }
    }
}
