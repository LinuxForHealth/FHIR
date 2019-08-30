/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.server;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.ws.rs.RuntimeType;
import javax.ws.rs.core.Application;

import com.ibm.watson.health.fhir.provider.FHIRJsonPatchProvider;
import com.ibm.watson.health.fhir.provider.FHIRJsonProvider;
import com.ibm.watson.health.fhir.provider.FHIRProvider;
import com.ibm.watson.health.fhir.server.resources.FHIRResource;

public class FHIRApplication extends Application {
    private static final Logger log = Logger.getLogger(FHIRApplication.class.getName());

    private Set<Object> singletons = null;
    private Set<Class<?>> classes = null;

    public FHIRApplication() {
        log.entering(this.getClass().getName(), "ctor");
        
        FHIRBuildIdentifier buildInfo = new FHIRBuildIdentifier();
        log.info("FHIR Server version " + buildInfo.getBuildVersion() + " build id '" + buildInfo.getBuildId() + "' starting.");
        
        log.exiting(this.getClass().getName(), "ctor");
    }

    @Override
    public Set<Class<?>> getClasses() {
        log.entering(this.getClass().getName(), "getClasses");
        try {
            if (classes == null) {
                classes = new HashSet<Class<?>>();
                classes.add(FHIRResource.class);
            }
            return classes;
        } finally {
            log.exiting(this.getClass().getName(), "getClasses");
        }
    }

    @Override
    public Set<Object> getSingletons() {
        log.entering(this.getClass().getName(), "getSingletons");
        try {
            if (singletons == null) {
                singletons = new HashSet<Object>();
                singletons.add(new FHIRProvider());
                singletons.add(new FHIRJsonProvider());
                singletons.add(new FHIRJsonPatchProvider(RuntimeType.SERVER));
            }
            return singletons;
        } finally {
            log.exiting(this.getClass().getName(), "getSingletons");
        }
    }
}
