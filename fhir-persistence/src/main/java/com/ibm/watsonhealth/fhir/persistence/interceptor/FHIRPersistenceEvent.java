/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.interceptor;

import java.util.Map;

import com.ibm.watsonhealth.fhir.model.Resource;

/**
 * This class represents an event fired by the FHIR persistence interceptor framework.
 */
public class FHIRPersistenceEvent {

    /**
     * This property is of type javax.ws.rs.core.UriInfo and contains Application and Request
     * URI information associated with the REST API request for which the interceptor is being invoked.
     */
    public static final String PROPNAME_URI_INFO = "URI_INFO";
    
    /**
     * This property is of type javax.ws.rs.core.HttpHeaders and contains the set of HTTP headers
     * associated with the REST API request for which the interceptor is being invoked.
     */
    public static final String PROPNAME_HTTP_HEADERS = "HTTP_HEADERS";

    /**
     * This property is of type javax.ws.rs.core.SecurityContext and contains security-related information
     * associated with the REST API request for which the interceptor is being invoked.
     */
    public static final String PROPNAME_SECURITY_CONTEXT = "SECURITY_CONTEXT";
    
    Resource fhirResource;
    Map<String, Object> properties;
    
    /**
     * Default ctor.
     */
    public FHIRPersistenceEvent() {
    }
    
    /**
     * Ctor which accepts the FHIR resource and a collection of properties.
     * @param fhirResource the FHIR resource associated with the event
     * @param properties the set of properties associated with the event.
     */
    public FHIRPersistenceEvent(Resource fhirResource, Map<String, Object> properties) {
        this.fhirResource = fhirResource;
        this.properties = properties;
    }

    /**
     * Returns the FHIR resource associated with the interceptor invocation.
     */
    public Resource getFhirResource() {
        return fhirResource;
    }

    /**
     * Retrieves the named property from the set of properties available to the interceptor.
     * @param propertyName the name of the property to retrieve.
     */
    public Object getProperty(String propertyName) {
        return (properties != null ? properties.get(propertyName) : null);
    }
}
