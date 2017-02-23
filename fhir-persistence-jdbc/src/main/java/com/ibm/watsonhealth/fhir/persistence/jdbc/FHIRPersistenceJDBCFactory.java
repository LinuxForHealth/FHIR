/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc;

import java.util.logging.Logger;

import com.ibm.watsonhealth.fhir.persistence.FHIRPersistence;
import com.ibm.watsonhealth.fhir.persistence.FHIRPersistenceFactory;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.impl.FHIRPersistenceJDBCImpl;

/**
 * Factory which serves up instances of the JDBC persistence implementation.
 */
public class FHIRPersistenceJDBCFactory implements FHIRPersistenceFactory {
    private static final Logger log = Logger.getLogger(FHIRPersistenceJDBCFactory.class.getName());

    /*
     * (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.persistence.FHIRPersistenceFactory#getInstance()
     */
    @Override
    public FHIRPersistence getInstance() throws FHIRPersistenceException {
        try {
            return new FHIRPersistenceJDBCImpl();
        } 
        catch (Throwable t) {
            String msg = "Unexpected exception while creating JDBC persistence layer: ";
            log.severe(msg + t);
            throw new FHIRPersistenceException(msg, t);
        }
    }
}
