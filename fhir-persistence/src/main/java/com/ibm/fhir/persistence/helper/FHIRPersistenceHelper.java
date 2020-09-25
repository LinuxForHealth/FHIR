/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.helper;

import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_PERSISTENCE_FACTORY;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.FHIRPersistenceFactory;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * This class serves as a helper for obtaining the correct persistence implementation to be used by the FHIR REST API
 * layer.
 */
public class FHIRPersistenceHelper implements PersistenceHelper {
    private static final Logger log = Logger.getLogger(FHIRPersistenceHelper.class.getName());

    protected PropertyGroup fhirConfig = null;

    // Issue #1366. Keep one instance of each type of persistence factory instead of instantiating it every time
    private final ConcurrentHashMap<String, FHIRPersistenceFactory> persistenceFactoryCache = new ConcurrentHashMap<>();
    
    public FHIRPersistenceHelper() {
        log.entering(this.getClass().getName(), "FHIRPersistenceHelper ctor");
        try {
            fhirConfig = FHIRConfiguration.getInstance().loadConfiguration();
        } catch (Throwable t) {
            String msg = "Unexpected error while retrieving configuration.";
            log.severe(msg + " " + t);
            throw new RuntimeException(msg, t);
        } finally {
            log.exiting(this.getClass().getName(), "FHIRPersistenceHelper ctor");
        }
    }
    
    /**
     * Retrieves the name of the factory class that should be instantiated for use by the server.
     * @param factoryPropertyName name of the property that contains the {@link FHIRPersistenceFactory} class name.
     * @return Name of the factory class that will be loaded.
     */
    protected String retrieveFactoryClassName(String factoryPropertyName) throws FHIRPersistenceException {
        try {
            String factoryClassName = fhirConfig.getStringProperty(factoryPropertyName);
            if (factoryClassName == null) {
                throw new FHIRPersistenceException("Unable to find configuration property: " + factoryPropertyName);
            } else {
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Retrieved persistence factory property '" + factoryPropertyName + "': " + factoryClassName);
                }
            }
            return factoryClassName;
        } catch (Throwable t) {
            throw new FHIRPersistenceException("Unexpected error while retrieving configuration.", t);
        }
    }

    @Override
    public FHIRPersistence getFHIRPersistenceImplementation() throws FHIRPersistenceException {
        return getFHIRPersistenceImplementation(PROPERTY_PERSISTENCE_FACTORY);
    }

    @Override
    public FHIRPersistence getFHIRPersistenceImplementation(String factoryPropertyName) throws FHIRPersistenceException {
        log.entering(this.getClass().getName(), "getFHIRPersistenceImplementation");

        try {
            String factoryClassName = retrieveFactoryClassName(factoryPropertyName);
            if (log.isLoggable(Level.FINE)) {
                log.fine("Using FHIR persistence factory class name: " + factoryClassName);
            }
            
            
            FHIRPersistenceFactory factory = persistenceFactoryCache.get(factoryClassName);
            if (factory == null) {

                Class<?> factoryClass = null;
                try {
                    factoryClass = Class.forName(factoryClassName);
                } catch (ClassNotFoundException e) {
                    String msg = "An error occurred while trying to load FHIR persistence factory class '" + factoryClassName + "'";
                    log.severe(msg + ": " + e);
                    throw new FHIRPersistenceException(msg, e);
                }
                
                // Make sure the class we loaded is in fact a FHIRPersistenceFactory class.
                if (FHIRPersistenceFactory.class.isAssignableFrom(factoryClass)) {
                    try {
                        factory = (FHIRPersistenceFactory) factoryClass.getDeclaredConstructor().newInstance();

                        // There's a small chance we may initially create more than one factory instance, but
                        // we only cache the first one, so others will be quickly forgotten
                        FHIRPersistenceFactory other = persistenceFactoryCache.putIfAbsent(factoryClassName, factory);
                        if (other != null) {
                            // solves a small race condition - we want the one already cached
                            factory = other; 
                        }
                    } catch (Throwable t) {
                        String msg = "An error occurred while trying to instantiate FHIR persistence factory class '" + factoryClassName + "'";
                        log.severe(msg + ": " + t);
                        throw new FHIRPersistenceException(msg, t);
                    }
                } else {
                    String msg = "Configured FHIR persistence factory class '" + factoryClass.getName() + "' does not implement FHIRPersistenceFactory interface.";
                    log.severe(msg);
                    throw new FHIRPersistenceException(msg);
                }
            }
            // Call the factory and return the implementation instance.
            return factory.getInstance();
        } finally {
            log.exiting(this.getClass().getName(), "getFHIRPersistenceImplementation");
        }
    }
}
