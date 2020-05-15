/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.service;

import java.util.Iterator;
import java.util.ServiceLoader;

import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.term.service.provider.DefaultTermServiceProvider;
import com.ibm.fhir.term.spi.FHIRTermServiceProvider;

public class FHIRTermService {
    private static final FHIRTermService INSTANCE = new FHIRTermService();

    private final FHIRTermServiceProvider provider;

    private FHIRTermService() {
        provider = loadProvider();
    }

    public ValueSet expand(ValueSet valueSet) {
        return provider.expand(valueSet);
    }

    public static FHIRTermService getInstance() {
        return INSTANCE;
    }

    private FHIRTermServiceProvider loadProvider() {
        Iterator<FHIRTermServiceProvider> iterator = ServiceLoader.load(FHIRTermServiceProvider.class).iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return new DefaultTermServiceProvider();
    }
}
