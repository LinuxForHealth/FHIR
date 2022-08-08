/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.scout;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.persistence.FHIRPersistence;
import org.linuxforhealth.fhir.persistence.FHIRPersistenceFactory;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceNotSupportedException;

/**
 * Factory for creating FHIRPersistence instances using Cassandra as the underlying
 * datastore.
 */
public class FHIRPersistenceScoutFactory implements FHIRPersistenceFactory {
    private static final Logger logger = Logger.getLogger(FHIRPersistenceScoutFactory.class.getName());
    
    @Override
    public FHIRPersistence getInstance() throws FHIRPersistenceException {
        try {
            return new FHIRPersistenceScoutImpl();
        }
        catch (FHIRPersistenceNotSupportedException e) {
            throw e;
        }
        catch (Throwable t) {
            final String msg = "Unexpected exception while creating SCOUT persistence layer";
            logger.log(Level.SEVERE, msg, t);
            throw new FHIRPersistenceException("FHIRPersistenceScoutFactory#getInstance()", t);
        }
    }
}
