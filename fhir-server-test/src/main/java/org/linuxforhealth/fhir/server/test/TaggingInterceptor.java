/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.server.test;

import java.util.ArrayList;
import java.util.List;

import org.linuxforhealth.fhir.model.resource.Bundle;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.Coding;
import org.linuxforhealth.fhir.model.type.Meta;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.persistence.context.FHIRPersistenceEvent;
import org.linuxforhealth.fhir.server.spi.interceptor.FHIRPersistenceInterceptor;
import org.linuxforhealth.fhir.server.spi.interceptor.FHIRPersistenceInterceptorException;

/**
 * A sample persistence interceptor that adds a "before" tag to each resource before it gets persisted and an "after" tag when it is read.
 */
public class TaggingInterceptor implements FHIRPersistenceInterceptor {
    private final Coding BEFORE_TAG = Coding.builder()
            .system(Uri.of("http://example.com/test"))
            .code(Code.of("before"))
            .build();
    private final Coding AFTER_TAG = Coding.builder()
            .system(Uri.of("http://example.com/test"))
            .code(Code.of("after"))
            .build();

    @Override
    public void beforeCreate(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        event.setFhirResource(addTag(event.getFhirResource(), BEFORE_TAG));
    }

    @Override
    public void beforeUpdate(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        event.setFhirResource(addTag(event.getFhirResource(), BEFORE_TAG));
    }

    @Override
    public void beforePatch(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        event.setFhirResource(addTag(event.getFhirResource(), BEFORE_TAG));
    }

    @Override
    public void afterRead(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        event.setFhirResource(addTag(event.getFhirResource(), AFTER_TAG));
    }

    @Override
    public void afterVread(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        event.setFhirResource(addTag(event.getFhirResource(), AFTER_TAG));
    }

    @Override
    public void afterHistory(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        if (event.getFhirResource().is(Bundle.class)) {
            Bundle bundle = event.getFhirResource().as(Bundle.class);

            List<Bundle.Entry> updatedEntries = new ArrayList<>();

            for (Bundle.Entry entry : bundle.getEntry()) {
                Resource updatedResource = addTag(entry.getResource(), AFTER_TAG);
                Bundle.Entry updatedEntry = entry.toBuilder()
                        .resource(updatedResource)
                        .build();
                updatedEntries.add(updatedEntry);
            }

            Bundle updatedBundle = bundle.toBuilder()
                    .entry(updatedEntries)
                    .build();

            event.setFhirResource(updatedBundle);
        }
    }

    @Override
    public void afterSearch(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        if (event.getFhirResource().is(Bundle.class)) {
            Bundle bundle = event.getFhirResource().as(Bundle.class);

            List<Bundle.Entry> updatedEntries = new ArrayList<>();

            for (Bundle.Entry entry : bundle.getEntry()) {
                Resource updatedResource = addTag(entry.getResource(), AFTER_TAG);
                Bundle.Entry updatedEntry = entry.toBuilder()
                        .resource(updatedResource)
                        .build();
                updatedEntries.add(updatedEntry);
            }

            Bundle updatedBundle = bundle.toBuilder()
                    .entry(updatedEntries)
                    .build();

            event.setFhirResource(updatedBundle);
        }
    }

    private Resource addTag(Resource r, Coding tag) {
        boolean hasMeta = r.getMeta() != null;
        if (hasMeta && r.getMeta().getTag().contains(tag)) {
            return r;
        }
        Meta.Builder metaBuilder = hasMeta ? r.getMeta().toBuilder() : Meta.builder();
        return r.toBuilder().meta(metaBuilder.tag(tag).build()).build();
    }
}
