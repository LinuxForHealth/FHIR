/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.client.impl;

import java.net.URI;

import javax.json.JsonObject;
import javax.ws.rs.core.Response;
import javax.xml.datatype.XMLGregorianCalendar;

import com.ibm.watsonhealth.fhir.client.FHIRResponse;
import com.ibm.watsonhealth.fhir.model.Resource;

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
     * @see com.ibm.watsonhealth.fhir.client.FHIRResponse#getStatusCode()
     */
    @Override
    public int getStatus() {
        return response.getStatus();
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRResponse#getLocationURI()
     */
    @Override
    public URI getLocationURI() throws Exception {
        return response.getLocation();
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRResponse#getLocation()
     */
    @Override
    public String getLocation() throws Exception {
        return (response.getLocation() != null ? response.getLocation().toString() : null);
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRResponse#getLastModified()
     */
    @Override
    public XMLGregorianCalendar getLastModified() throws Exception {
        // TODO finish this!
        return null;
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRResponse#getETag()
     */
    @Override
    public String getETag() throws Exception {
        return response.getHeaderString("ETag");
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRResponse#getResource(java.lang.Class)
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
     * @see com.ibm.watsonhealth.fhir.client.FHIRResponse#getResponse()
     */
    @Override
    public Response getResponse() throws Exception {
        return response;
    }
}
