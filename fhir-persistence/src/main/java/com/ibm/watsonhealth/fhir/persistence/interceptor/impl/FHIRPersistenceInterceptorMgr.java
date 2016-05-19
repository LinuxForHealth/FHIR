/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.interceptor.impl;

import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.watsonhealth.fhir.core.FHIRUtilities;
import com.ibm.watsonhealth.fhir.persistence.interceptor.FHIRPersistenceEvent;
import com.ibm.watsonhealth.fhir.persistence.interceptor.FHIRPersistenceInterceptor;
import com.ibm.watsonhealth.fhir.persistence.interceptor.FHIRPersistenceInterceptorException;

/**
 * This class implements the FHIR persistence interceptor framework. This framework allows users to inject business
 * logic into the REST API request processing code path at various points.
 * 
 * Interceptors are discovered using the jdk's ServiceProvider class.
 * 
 * To register an interceptor implementation, develop a class that implements the FHIRPersistenceInterceptor interface,
 * and then insert your implementation class name into a file called
 * META-INF/services/com.ibm.watsonhealth.fhir.persistence.FHIRPersistenceInterceptor and store that file in your jar.
 * These "interceptor" jars should be stored in a common place defined by the FHIR Server.
 */
public class FHIRPersistenceInterceptorMgr {
    private static final Logger log = Logger.getLogger(FHIRPersistenceInterceptorMgr.class.getName());

    private static FHIRPersistenceInterceptorMgr instance = null;

    // Our list of discovered interceptors.
    ServiceLoader<FHIRPersistenceInterceptor> interceptors;

    public static synchronized FHIRPersistenceInterceptorMgr getInstance() {
        if (instance == null) {
            instance = new FHIRPersistenceInterceptorMgr();
        }
        return instance;
    }

    public FHIRPersistenceInterceptorMgr() {
        // Discover all implementations of our interceptor interface.
        interceptors = ServiceLoader.load(FHIRPersistenceInterceptor.class);
        if (log.isLoggable(Level.FINE)) {
            Iterator<FHIRPersistenceInterceptor> iter = interceptors.iterator();
            if (iter.hasNext()) {
                log.fine("Discovered the following persistence interceptors:");
                while (iter.hasNext()) {
                    FHIRPersistenceInterceptor interceptor = iter.next();
                    log.fine(">>> " + interceptor.getClass().getName() + '@' + FHIRUtilities.getObjectHandle(interceptor));
                }
            } else {
                log.fine("No persistence interceptors found...");
            }
        }
    }

    /**
     * The following methods will invoke the respective interceptor methods on each registered interceptor.
     */
    public void fireBeforeCreateEvent(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        Iterator<FHIRPersistenceInterceptor> iter = interceptors.iterator();
        while (iter.hasNext()) {
            FHIRPersistenceInterceptor interceptor = iter.next();
            interceptor.beforeCreate(event);
        }
    }

    public void fireAfterCreateEvent(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        Iterator<FHIRPersistenceInterceptor> iter = interceptors.iterator();
        while (iter.hasNext()) {
            FHIRPersistenceInterceptor interceptor = iter.next();
            interceptor.afterCreate(event);
        }
    }

    public void fireBeforeUpdateEvent(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        Iterator<FHIRPersistenceInterceptor> iter = interceptors.iterator();
        while (iter.hasNext()) {
            FHIRPersistenceInterceptor interceptor = iter.next();
            interceptor.beforeUpdate(event);
        }
    }

    public void fireAfterUpdateEvent(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        Iterator<FHIRPersistenceInterceptor> iter = interceptors.iterator();
        while (iter.hasNext()) {
            FHIRPersistenceInterceptor interceptor = iter.next();
            interceptor.afterUpdate(event);
        }
    }
}
