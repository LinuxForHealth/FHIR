/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.helper;

import java.util.logging.Logger;

import javax.naming.InitialContext;

import com.ibm.watsonhealth.fhir.core.FHIRUtilities;
import com.ibm.watsonhealth.fhir.persistence.FHIRPersistence;
import com.ibm.watsonhealth.fhir.persistence.FHIRPersistenceFactory;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;

/**
 * This class serves as a helper for obtaining the correct persistence implementation to be used by the FHIR REST API
 * layer.
 */
public class FHIRPersistenceHelper {
    private static final Logger log = Logger.getLogger(FHIRPersistenceHelper.class.getName());
    private static final String JNDI_NAME_FHIR_PERSISTENCE_FACTORY = "com.ibm.watsonhealth.fhir.persistence.factory";

    public FHIRPersistenceHelper() {
        log.finest("In FHIRPersistenceHelper() ctor. handle=" + FHIRUtilities.getObjectHandle(this));
        log.finest(FHIRUtilities.getCurrentStacktrace());
    }

    /**
     * Retrieves the name of the factory class that should be instantiated for use by the server.
     */
    private String retrieveFactoryClassName() throws FHIRPersistenceException {
        String factoryClassName = null;

        try {
            // Retrieve the name of the impl class via JNDI.
            InitialContext ctx = new InitialContext();
            factoryClassName = (String) ctx.lookup(JNDI_NAME_FHIR_PERSISTENCE_FACTORY);
            log.fine("Found JNDI entry for key: " + JNDI_NAME_FHIR_PERSISTENCE_FACTORY + ", value=" + factoryClassName);
        } catch (Throwable t) {
            throw new FHIRPersistenceException("Unable to find jndi entry: " + JNDI_NAME_FHIR_PERSISTENCE_FACTORY);
        }

        return factoryClassName;
    }

    /**
     * Returns an appropriate FHIRPersistance implementation according to the current configuration.
     * @throws FHIRPersistenceException 
     */
    public FHIRPersistence getFHIRPersistenceImplementation() throws FHIRPersistenceException {
        log.entering(this.getClass().getName(), "getFHIRPersistenceImplementation");

        try {
            String factoryClassName = retrieveFactoryClassName();
            log.fine("Using FHIR persistence factory class name: " + factoryClassName);

            Class<?> factoryClass = null;
            try {
                factoryClass = Class.forName(factoryClassName);
            } catch (ClassNotFoundException e) {
                String msg = "An error occurred while trying to load FHIR persistence factory class '" + factoryClassName + "'";
                log.severe(msg + ": " + e);
                throw new FHIRPersistenceException(msg, e);
            }
            
            // Make sure the class we loaded is in fact a FHIRPersistenceFactory class.
            FHIRPersistenceFactory factory = null;
            if (FHIRPersistenceFactory.class.isAssignableFrom(factoryClass)) {
                try {
                    factory = (FHIRPersistenceFactory) factoryClass.newInstance();
                } catch (IllegalAccessException | InstantiationException e) {
                    String msg = "An error occurred while trying to instantiate FHIR persistence factory class '" + factoryClassName + "'";
                    log.severe(msg + ": " + e);
                    throw new FHIRPersistenceException(msg, e);
                }
            } else {
                String msg = "Configured FHIR persistence factory class '" + factoryClass.getName() + "' does not implement FHIRPersistenceFactory interface.";
                log.severe(msg);
                throw new FHIRPersistenceException(msg);
            }

            // Call the factory and return the implementation instance.
            return factory.getInstance();
        } finally {
            log.exiting(this.getClass().getName(), "getFHIRPersistenceImplementation");
        }
    }
}
