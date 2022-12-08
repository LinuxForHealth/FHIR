/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.bulkdata.application;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.ws.rs.core.Application;

import org.linuxforhealth.fhir.bulkdata.resources.HealthCheck;

public class FHIRBulkDataApplication extends Application {
    private static final Logger log = Logger.getLogger(FHIRBulkDataApplication.class.getName());

    private Set<Class<?>> classes = null;
    private Set<Object> singletons = null;

    public FHIRBulkDataApplication() {
        
    }

    @Override
    public Set<Class<?>> getClasses() {
        log.entering(this.getClass().getName(), "getClasses");
        System.out.println("##################### init bulk data rest api's");
        try {
            if (classes == null) {
                classes = new HashSet<Class<?>>();
                classes.add(HealthCheck.class);
            }
            return classes;
        } finally {
            log.exiting(this.getClass().getName(), "getClasses");
        }
    }
    
    
}
