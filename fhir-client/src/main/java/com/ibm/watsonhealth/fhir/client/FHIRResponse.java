/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.client;

import java.net.URI;

import javax.ws.rs.core.Response;
import javax.xml.datatype.XMLGregorianCalendar;

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
    XMLGregorianCalendar getLastModified() throws Exception;

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
}
