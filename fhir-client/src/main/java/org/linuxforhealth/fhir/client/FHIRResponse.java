/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.client;

import java.net.URI;
import java.time.Instant;

import javax.ws.rs.core.Response;

/**
 * This interface represents a response received from a FHIR REST API invocation.
 */
public interface FHIRResponse {
    
    /**
     * This method is used to retrieve the numeric HTTP status code associated with 
     * a FHIR REST API response.
     * @return the HTTP status code
     */
    int getStatus();
    
    /**
     * This method is used to retrieve the value of the Location response HTTP header as a String.
     * @return the Location response header value as a String
     * @throws Exception
     */
    String getLocation() throws Exception;
    
    /**
     * This method is used to retrieve the value of the Location response HTTP header as a URI.
     * @return the Location response header value as a URI
     * @throws Exception
     */
    URI getLocationURI() throws Exception;
    
    /**
     * This method is used to retrieve the Last-Modified response HTTP header.
     * @return
     * @throws Exception
     */
    Instant getLastModified() throws Exception;

    /**
     * This method is used to retrieve the value of the ETag response HTTP header.
     * @return the ETag response header value
     * @throws Exception
     */
    String getETag() throws Exception;
    
    /**
     * This method is used to retrieve the resource returned in a FHIR REST API response.
     * @param type this should be a <code>java.lang.Class</code> value which indicates the
     * type of return value expected
     * @return
     * @throws Exception
     */
    <T> T getResource(Class<T> type) throws Exception;

    /**
     * Returns the underlying JAX-RS 2.0 Response object associated with this FHIRResponse.
     * @return the JAX-RS Response
     * @throws Exception
     */
    Response getResponse() throws Exception;
    
    /**
     * This function will parse the specified location value into its constituent parts and return
     * a String array containing the individual parts.
     * The resulting String array returned by this function will contain the following values:
     * <ul>
     * <li>Index 0 - the resource type
     * <li>Index 1 - the resource id
     * <li>Index 2 - the version id (optional)
     * </ul>
     * 
     * Examples:
     * <ol>
     * <li>If you call <code>parseLocation</code> with the value 
     * "http://localhost:9080/fhir-server/api/v4/Patient/23/_history/2", then you should
     * receive this String array:
     * <ul>
     * <li>[0] - "Patient"
     * <li>[1] - "23"
     * <li>[2] - "2"
     * </ul>
     * 
     * <li>If you call <code>parseLocation</code> with the value 
     * "http://localhost:9080/fhir-server/api/v4/Observation/38", then you should
     * receive this String array:
     * <ul>
     * <li>[0] - "Observation"
     * <li>[1] - "38"
     * </ul>
     * 
     * @param locationString a resource's location URI as a string; this will typically be the
     * value returned by the getLocation() method.
     * 
     * @return a String array containing the individual parts of the location string or null 
     * if the specified location value could not be parsed or was passed in as null.
     * 
     * @throws Exception
     */
    String[] parseLocation(String locationString) throws Exception;
    
    /**
     * Returns whether the response contains a FHIR Resource entity.
     * @return true if the response body is empty, otherwise false 
     */
    boolean isEmpty();
}
