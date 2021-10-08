/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.davinci.hrex.test;

import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.CacheControl;
import jakarta.ws.rs.core.EntityTag;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.Link.Builder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.Response.Status.Family;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.Variant;
import jakarta.ws.rs.core.Variant.VariantListBuilder;
import jakarta.ws.rs.ext.RuntimeDelegate;

/**
 * Test Delegate to avoid stacktrace when running Unit Tests
 */
public class TestRuntimeDelegate extends RuntimeDelegate {

    @Override
    public ResponseBuilder createResponseBuilder() {
        return new ResponseBuilder() {

            private int status = 200;
            private Object entity = null;

            @Override
            public Response build() {
                return new Response() {

                    @Override
                    public int getStatus() {
                        return status;
                    }

                    @Override
                    public Object getEntity() {
                        return entity;
                    }

                    @Override
                    public StatusType getStatusInfo() {
                        return new StatusType() {

                            @Override
                            public int getStatusCode() {
                                return status;
                            }

                            @Override
                            public Family getFamily() {
                                return Status.Family.familyOf(status);
                            }

                            @Override
                            public String getReasonPhrase() {
                                return "Test Delegate";
                            }

                        };
                    }

                    @Override
                    public <T> T readEntity(Class<T> entityType) {

                        throw new AssertionError("Unexpected");
                    }

                    @Override
                    public <T> T readEntity(GenericType<T> entityType) {

                        throw new AssertionError("Unexpected");
                    }

                    @Override
                    public <T> T readEntity(Class<T> entityType, Annotation[] annotations) {

                        throw new AssertionError("Unexpected");
                    }

                    @Override
                    public <T> T readEntity(GenericType<T> entityType, Annotation[] annotations) {

                        throw new AssertionError("Unexpected");
                    }

                    @Override
                    public boolean hasEntity() {

                        return false;
                    }

                    @Override
                    public boolean bufferEntity() {

                        return false;
                    }

                    @Override
                    public void close() {

                    }

                    @Override
                    public MediaType getMediaType() {

                        throw new AssertionError("Unexpected");
                    }

                    @Override
                    public Locale getLanguage() {

                        throw new AssertionError("Unexpected");
                    }

                    @Override
                    public int getLength() {

                        return 0;
                    }

                    @Override
                    public Set<String> getAllowedMethods() {

                        throw new AssertionError("Unexpected");
                    }

                    @Override
                    public Map<String, NewCookie> getCookies() {

                        throw new AssertionError("Unexpected");
                    }

                    @Override
                    public EntityTag getEntityTag() {

                        throw new AssertionError("Unexpected");
                    }

                    @Override
                    public Date getDate() {

                        throw new AssertionError("Unexpected");
                    }

                    @Override
                    public Date getLastModified() {

                        throw new AssertionError("Unexpected");
                    }

                    @Override
                    public URI getLocation() {

                        throw new AssertionError("Unexpected");
                    }

                    @Override
                    public Set<Link> getLinks() {

                        throw new AssertionError("Unexpected");
                    }

                    @Override
                    public boolean hasLink(String relation) {

                        return false;
                    }

                    @Override
                    public Link getLink(String relation) {

                        throw new AssertionError("Unexpected");
                    }

                    @Override
                    public Builder getLinkBuilder(String relation) {

                        throw new AssertionError("Unexpected");
                    }

                    @Override
                    public MultivaluedMap<String, Object> getMetadata() {

                        throw new AssertionError("Unexpected");
                    }

                    @Override
                    public MultivaluedMap<String, String> getStringHeaders() {

                        throw new AssertionError("Unexpected");
                    }

                    @Override
                    public String getHeaderString(String name) {

                        throw new AssertionError("Unexpected");
                    }

                };
            }

            @Override
            public ResponseBuilder status(int status) {
                this.status = status;
                return this;
            }

            @Override
            public ResponseBuilder entity(Object entity) {
                this.entity = entity;
                return this;
            }

            @Override
            public ResponseBuilder status(int status, String reasonPhrase) {
                throw new AssertionError("Unexpected");
            }

            @Override
            public ResponseBuilder clone() {
                throw new AssertionError("Unexpected");
            }

            @Override
            public ResponseBuilder entity(Object entity, Annotation[] annotations) {
                throw new AssertionError("Unexpected");
            }

            @Override
            public ResponseBuilder allow(String... methods) {
                throw new AssertionError("Unexpected");
            }

            @Override
            public ResponseBuilder allow(Set<String> methods) {
                throw new AssertionError("Unexpected");
            }

            @Override
            public ResponseBuilder cacheControl(CacheControl cacheControl) {
                throw new AssertionError("Unexpected");
            }

            @Override
            public ResponseBuilder encoding(String encoding) {
                throw new AssertionError("Unexpected");
            }

            @Override
            public ResponseBuilder header(String name, Object value) {
                throw new AssertionError("Unexpected");
            }

            @Override
            public ResponseBuilder replaceAll(MultivaluedMap<String, Object> headers) {
                throw new AssertionError("Unexpected");
            }

            @Override
            public ResponseBuilder language(String language) {
                throw new AssertionError("Unexpected");
            }

            @Override
            public ResponseBuilder language(Locale language) {
                throw new AssertionError("Unexpected");
            }

            @Override
            public ResponseBuilder type(MediaType type) {
                throw new AssertionError("Unexpected");
            }

            @Override
            public ResponseBuilder type(String type) {
                throw new AssertionError("Unexpected");
            }

            @Override
            public ResponseBuilder variant(Variant variant) {
                throw new AssertionError("Unexpected");
            }

            @Override
            public ResponseBuilder contentLocation(URI location) {
                throw new AssertionError("Unexpected");
            }

            @Override
            public ResponseBuilder cookie(NewCookie... cookies) {
                throw new AssertionError("Unexpected");
            }

            @Override
            public ResponseBuilder expires(Date expires) {

                throw new AssertionError("Unexpected");
            }

            @Override
            public ResponseBuilder lastModified(Date lastModified) {

                throw new AssertionError("Unexpected");
            }

            @Override
            public ResponseBuilder location(URI location) {

                throw new AssertionError("Unexpected");
            }

            @Override
            public ResponseBuilder tag(EntityTag tag) {

                throw new AssertionError("Unexpected");
            }

            @Override
            public ResponseBuilder tag(String tag) {

                throw new AssertionError("Unexpected");
            }

            @Override
            public ResponseBuilder variants(Variant... variants) {

                throw new AssertionError("Unexpected");
            }

            @Override
            public ResponseBuilder variants(List<Variant> variants) {

                throw new AssertionError("Unexpected");
            }

            @Override
            public ResponseBuilder links(Link... links) {

                throw new AssertionError("Unexpected");
            }

            @Override
            public ResponseBuilder link(URI uri, String rel) {

                throw new AssertionError("Unexpected");
            }

            @Override
            public ResponseBuilder link(String uri, String rel) {

                throw new AssertionError("Unexpected");
            }

        };
    }

    @Override
    public UriBuilder createUriBuilder() {

        throw new AssertionError("Unexpected");
    }

    @Override
    public VariantListBuilder createVariantListBuilder() {

        throw new AssertionError("Unexpected");
    }

    @Override
    public <T> T createEndpoint(Application application, Class<T> endpointType) throws IllegalArgumentException, UnsupportedOperationException {

        throw new AssertionError("Unexpected");
    }

    @Override
    public <T> HeaderDelegate<T> createHeaderDelegate(Class<T> type) throws IllegalArgumentException {

        throw new AssertionError("Unexpected");
    }

    @Override
    public Builder createLinkBuilder() {

        throw new AssertionError("Unexpected");
    }

}
