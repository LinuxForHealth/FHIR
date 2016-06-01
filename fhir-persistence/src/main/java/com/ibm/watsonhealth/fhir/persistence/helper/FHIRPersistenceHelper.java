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
public class FHIRPersistenceHelper implements PersistenceHelper {
    private static final Logger log = Logger.getLogger(FHIRPersistenceHelper.class.getName());
    protected static final String JNDI_NAME_FHIR_PERSISTENCE_FACTORY = "com.ibm.watsonhealth.fhir.persistence.factory";

    public FHIRPersistenceHelper() {
        log.finest("In FHIRPersistenceHelper() ctor. handle=" + FHIRUtilities.getObjectHandle(this));
        log.finest(FHIRUtilities.getCurrentStacktrace());
    }
    
    /**
     * Retrieves the name of the factory class that should be instantiated for use by the server.
     * @param factoryJndiName Name of the JNDI resource that contains the {@link FHIRPersistenceFactory} class name.
     * @return Name of the factory class that will be loaded.
     */
    protected String retrieveFactoryClassName(String factoryJndiName) throws FHIRPersistenceException {
        String factoryClassName = null;

        try {
            // Retrieve the name of the impl class via JNDI.
            InitialContext ctx = new InitialContext();
            factoryClassName = (String) ctx.lookup(factoryJndiName);
            log.fine("Found JNDI entry for key: " + factoryJndiName + ", value=" + factoryClassName);
        } catch (Throwable t) {
            throw new FHIRPersistenceException("Unable to find jndi entry: " + factoryJndiName);
        }

        return factoryClassName;
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.persistence.helper.PersistenceHelper#getFHIRPersistenceImplementation()
     */
    @Override
    public FHIRPersistence getFHIRPersistenceImplementation() throws FHIRPersistenceException {
        return getFHIRPersistenceImplementation(JNDI_NAME_FHIR_PERSISTENCE_FACTORY);
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.persistence.helper.PersistenceHelper#getFHIRPersistenceImplementation(java.lang.String)
     */
    @Override
    public FHIRPersistence getFHIRPersistenceImplementation(String factoryJndiName) throws FHIRPersistenceException {
        log.entering(this.getClass().getName(), "getFHIRPersistenceImplementation");

        try {
            String factoryClassName = retrieveFactoryClassName(factoryJndiName);
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
