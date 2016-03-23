/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.ibm.watsonhealth.fhir.provider.FHIRProvider;
import com.ibm.watsonhealth.fhir.server.resources.FHIRResource;

public class FHIRApplication extends Application {
    private Set<Object> singletons = null;
    private Set<Class<?>> classes = null;

    public FHIRApplication() {
    }

    @Override
    public Set<Class<?>> getClasses() {
        if (classes == null) {
            classes = new HashSet<Class<?>>();
            classes.add(FHIRResource.class);
        }
        return classes;
    }

    @Override
    public Set<Object> getSingletons() {
        if (singletons == null) {
            singletons = new HashSet<Object>();
            singletons.add(new FHIRProvider());
        }
        return singletons;
    }
}
