/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.ws.rs.core.Application;

import com.ibm.watsonhealth.fhir.provider.FHIRProvider;
import com.ibm.watsonhealth.fhir.server.resources.FHIRResource;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;

public class FHIRApplication extends Application {
    private static final Logger log = Logger.getLogger(FHIRApplication.class.getName());

    private Set<Object> singletons = null;
    private Set<Class<?>> classes = null;

    public FHIRApplication() {
        log.finest("In FHIRApplication ctor.");
        
        // Configure Swagger's scanner so that it can scan our resource classes,
        // and build the required swagger definition.
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setTitle("FHIR REST API");
        beanConfig.setDescription("IBM Watson Health Cloud FHIR Server");
        beanConfig.setVersion("0.1");
        beanConfig.setBasePath("/fhir-server/api");
        beanConfig.setResourcePackage(FHIRResource.class.getPackage().getName());
        beanConfig.setScan(true);
    }

    @Override
    public Set<Class<?>> getClasses() {
        log.entering(this.getClass().getName(), "getClasses");
        try {
            if (classes == null) {
                classes = new HashSet<Class<?>>();
                classes.add(FHIRResource.class);
                
                // Swagger-related resources.
                classes.add(ApiListingResource.class);
                classes.add(SwaggerSerializers.class);
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
            }
            return singletons;
        } finally {
            log.exiting(this.getClass().getName(), "getSingletons");
        }
    }
}
