/*
 * (C) Copyright IBM Corp. 2016, 2022
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
import com.ibm.fhir.search.util.SearchHelper;

/**
 * This class serves as a helper for obtaining the correct persistence implementation to be used by the FHIR REST API
 * layer.
 */
public class FHIRPersistenceHelper implements PersistenceHelper {
    private static final Logger log = Logger.getLogger(FHIRPersistenceHelper.class.getName());

    protected final PropertyGroup fhirConfig;
    protected final SearchHelper searchHelper;

    // Issue #1366. Keep one instance of each type of persistence factory instead of instantiating it every time
    private final ConcurrentHashMap<String, FHIRPersistenceFactory> persistenceFactoryCache = new ConcurrentHashMap<>();

    public FHIRPersistenceHelper(SearchHelper searchHelper) {
        log.entering(this.getClass().getName(), "FHIRPersistenceHelper ctor");
        try {
            this.fhirConfig = FHIRConfiguration.getInstance().loadConfiguration();
            this.searchHelper = searchHelper;
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

            FHIRPersistenceFactory factory = persistenceFactoryCache.computeIfAbsent(factoryClassName, FHIRPersistenceHelper::newInstance );

            if (factory != null) {
                // Call the factory and return the implementation instance.
                return factory.getInstance(searchHelper);
            } else {
                throw new FHIRPersistenceException(PROPERTY_PERSISTENCE_FACTORY + " is configured incorrectly");
            }
        } catch (FHIRPersistenceException fpe) {
            throw fpe;
        } catch (Throwable t) {
            throw new FHIRPersistenceException(PROPERTY_PERSISTENCE_FACTORY + " is configured incorrectly", t);
        } finally {
            log.exiting(this.getClass().getName(), "getFHIRPersistenceImplementation");
        }
    }

    /**
     * Create a new instance of the class
     * @param factoryClassName
     * @return
     */
    private static FHIRPersistenceFactory newInstance(String factoryClassName) {
        Class<?> factoryClass = null;
        try {
            factoryClass = Class.forName(factoryClassName);
        } catch (ClassNotFoundException e) {
            String msg = "An error occurred while trying to load FHIR persistence factory class '" + factoryClassName + "'";
            log.severe(msg + ": " + e);

            throw new IllegalStateException("Failed to load persistence implementation class");
        }

        try {
            if (FHIRPersistenceFactory.class.isAssignableFrom(factoryClass)) {
                return (FHIRPersistenceFactory) factoryClass.getDeclaredConstructor().newInstance();
            } else {
                throw new IllegalArgumentException("FHIRPersistenceFactory is not assignable from " + factoryClassName);
            }
        } catch (Throwable t) {
            log.log(Level.SEVERE, "Failed creating new instance of " + factoryClassName, t);
            throw new IllegalStateException("Failed to create persistence implementation class");
        }
    }
}
