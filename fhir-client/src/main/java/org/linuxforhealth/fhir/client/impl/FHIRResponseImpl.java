/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.client.impl;

import java.net.URI;
import java.time.Instant;
import java.util.Date;

import jakarta.json.JsonObject;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.linuxforhealth.fhir.client.FHIRResponse;
import org.linuxforhealth.fhir.model.resource.Resource;

/**
 * An implementation of the FHIRResponse interface which exposes the results
 * of a FHIR REST API operation.
 */
public class FHIRResponseImpl implements FHIRResponse {
    private Response response;

    protected FHIRResponseImpl() {
    }

    public FHIRResponseImpl(Response response) {
        this.response = response;
    }

    /* (non-Javadoc)
     * @see org.linuxforhealth.fhir.client.FHIRResponse#getStatusCode()
     */
    @Override
    public int getStatus() {
        return response.getStatus();
    }

    /* (non-Javadoc)
     * @see org.linuxforhealth.fhir.client.FHIRResponse#getLocationURI()
     */
    @Override
    public URI getLocationURI() throws Exception {
        URI location = response.getLocation();
        if (location == null) {
            String s = response.getHeaderString("Content-Location");
            if (s != null && !s.isEmpty()) {
                location = new URI(s);
            }
        }
        return location;
    }

    /* (non-Javadoc)
     * @see org.linuxforhealth.fhir.client.FHIRResponse#getLocation()
     */
    @Override
    public String getLocation() throws Exception {
        URI location = response.getLocation();
        return (location != null ? location.toString() : null);
    }

    /* (non-Javadoc)
     * @see org.linuxforhealth.fhir.client.FHIRResponse#getLastModified()
     */
    @Override
    public Instant getLastModified() throws Exception {
        Instant lastModified = null;
        Date s = response.getLastModified();

        if (s != null) {
            lastModified = s.toInstant();
        }
        return lastModified;
    }

    /* (non-Javadoc)
     * @see org.linuxforhealth.fhir.client.FHIRResponse#getETag()
     */
    @Override
    public String getETag() throws Exception {
        return response.getHeaderString("ETag");
    }

    /* (non-Javadoc)
     * @see org.linuxforhealth.fhir.client.FHIRResponse#getResource(java.lang.Class)
     */
    @Override
    public <T> T getResource(Class<T> type) throws Exception {
        if (Resource.class.isAssignableFrom(type) || JsonObject.class.equals(type)) {
            return response.readEntity(type);
        } else {
            throw new IllegalArgumentException("The 'type' parameter must be either '" + JsonObject.class.getName() + ".class' or a class derived from '" + Resource.class.getName() + ".class'.");
        }
    }

    /* (non-Javadoc)
     * @see org.linuxforhealth.fhir.client.FHIRResponse#getResponse()
     */
    @Override
    public Response getResponse() throws Exception {
        return response;
    }

    /* (non-Javadoc)
     * @see org.linuxforhealth.fhir.client.FHIRResponse#parseLocation()
     */
    @Override
    public String[] parseLocation(String location) throws Exception {
        String[] result = null;
        if (location == null) {
            throw new NullPointerException("The 'location' parameter was specified as null.");
        }

        String[] tokens = location.split("/");
        // Check if we should expect 4 tokens or only 2.
        if (location.contains("_history")) {
            if (tokens.length >= 4) {
                result = new String[3];
                result[0] = tokens[tokens.length - 4];
                result[1] = tokens[tokens.length - 3];
                result[2] = tokens[tokens.length - 1];
            } else {
                throw new IllegalArgumentException("Incorrect location value specified: " + location);
            }
        } else {
            if (tokens.length >= 2) {
                result = new String[2];
                result[0] = tokens[tokens.length - 2];
                result[1] = tokens[tokens.length - 1];
            } else {
                throw new IllegalArgumentException("Incorrect location value specified: " + location);
            }
        }
        return result;
    }

    /* (non-Javadoc)
     * @see org.linuxforhealth.fhir.client.FHIRResponse#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        // To check for the presence of a resource in the response, we can't fully trust
        // the Content-Length response header or the JAX-RS Response.hasEntity() method.
        // The following scenarios are possible:
        // 1) Response.hasEntity() could return true even if Content-Length = 0
        // 2) Content-Length might be missing
        // Return true if either Response.hasEntity() returns false,
        // OR the Content-Length header is specified as "0".
        String contentLengthStr = response.getHeaderString(HttpHeaders.CONTENT_LENGTH);
        int contentLength = -1;
        if (contentLengthStr != null && !contentLengthStr.isEmpty()) {
            contentLength = Integer.valueOf(contentLengthStr).intValue();
        }
        return (!response.hasEntity() || contentLength == 0);
    }
}
