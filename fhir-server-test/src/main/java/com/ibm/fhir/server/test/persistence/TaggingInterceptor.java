/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.server.test.persistence;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.persistence.interceptor.FHIRPersistenceEvent;
import com.ibm.fhir.persistence.interceptor.FHIRPersistenceInterceptor;
import com.ibm.fhir.persistence.interceptor.FHIRPersistenceInterceptorException;

/**
 * A sample persistence interceptor for testing resource modification
 */
public class TaggingInterceptor implements FHIRPersistenceInterceptor {
    private final Coding tag;

    /**
     * Construct an interceptor that adds the passed tag to each resource
     */
    public TaggingInterceptor(Coding tag) {
        this.tag = tag;
    }

    @Override
    public void beforeCreate(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        event.setFhirResource(addTag(event.getFhirResource()));
    }

    @Override
    public void beforeUpdate(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        event.setFhirResource(addTag(event.getFhirResource()));
    }

    @Override
    public void beforePatch(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        event.setFhirResource(addTag(event.getFhirResource()));
    }

    private Resource addTag(Resource r) {
        boolean hasMeta = r.getMeta() != null;
        if (hasMeta && r.getMeta().getTag().contains(tag)) {
            return r;
        }
        Meta.Builder metaBuilder = hasMeta ? r.getMeta().toBuilder() : Meta.builder();
        return r.toBuilder().meta(metaBuilder.tag(tag).build()).build();
    }
}
