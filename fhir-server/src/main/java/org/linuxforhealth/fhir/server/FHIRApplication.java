/*
 * (C) Copyright IBM Corp. 2016, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.ws.rs.RuntimeType;
import javax.ws.rs.core.Application;

import org.linuxforhealth.fhir.config.FHIRConfigHelper;
import org.linuxforhealth.fhir.config.FHIRConfiguration;
import org.linuxforhealth.fhir.provider.FHIRJsonPatchProvider;
import org.linuxforhealth.fhir.provider.FHIRJsonProvider;
import org.linuxforhealth.fhir.provider.FHIRProvider;
import org.linuxforhealth.fhir.server.resources.Batch;
import org.linuxforhealth.fhir.server.resources.Capabilities;
import org.linuxforhealth.fhir.server.resources.Create;
import org.linuxforhealth.fhir.server.resources.Delete;
import org.linuxforhealth.fhir.server.resources.History;
import org.linuxforhealth.fhir.server.resources.Operation;
import org.linuxforhealth.fhir.server.resources.Patch;
import org.linuxforhealth.fhir.server.resources.Read;
import org.linuxforhealth.fhir.server.resources.Search;
import org.linuxforhealth.fhir.server.resources.Update;
import org.linuxforhealth.fhir.server.resources.VRead;
import org.linuxforhealth.fhir.server.resources.WellKnown;
import org.linuxforhealth.fhir.server.resources.filters.FHIRVersionRequestFilter;
import org.linuxforhealth.fhir.server.resources.filters.OriginalRequestFilter;

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
                classes.add(Batch.class);
                classes.add(Capabilities.class);
                classes.add(Create.class);
                classes.add(Delete.class);
                classes.add(History.class);
                classes.add(Operation.class);
                classes.add(Patch.class);
                classes.add(Read.class);
                classes.add(Search.class);
                classes.add(Update.class);
                classes.add(VRead.class);
                classes.add(FHIRVersionRequestFilter.class);
                classes.add(OriginalRequestFilter.class);
                if (FHIRConfigHelper.getBooleanProperty(FHIRConfiguration.PROPERTY_SECURITY_OAUTH_SMART_ENABLED, false)) {
                    classes.add(WellKnown.class);
                }
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
                singletons.add(new FHIRProvider(RuntimeType.SERVER));
                singletons.add(new FHIRJsonProvider(RuntimeType.SERVER));
                singletons.add(new FHIRJsonPatchProvider(RuntimeType.SERVER));
            }
            return singletons;
        } finally {
            log.exiting(this.getClass().getName(), "getSingletons");
        }
    }
}
