/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.persistence.FHIRPersistence;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRReplicationContext;

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
     * This property is of type FHIRPersistence and is the handle to the persistence layer implementation
     * being used by the FHIR Server while processing the current request.
     * Persistence interceptors can use this handle to invoke persistence operations.
     */
    public static final String PROPNAME_PERSISTENCE_IMPL = "PERSISTENCE_IMPL";
    
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
    
    /**
     * This property is of type String and contains the location URI that can be used to 
     * retrieve the resource via a GET request.
     */
    public static final String PROPNAME_RESOURCE_LOCATION_URI = "LOCATION_URI";
    
    /**
     * This property is of type String and contains the resource type associated with a
     * read, vread, history or search operation.   For other operations, this property will be null.
     */
    public static final String PROPNAME_RESOURCE_TYPE = "RESOURCE_TYPE";
    
    /**
     * This property is of type String and contains the resource id associated with a
     * read, vread, or history operation.   For other operations, this property will be null.
     */
    public static final String PROPNAME_RESOURCE_ID = "RESOURCE_ID";
    
    /**
     * This property is of type String and contains the version id associated with a
     * vread operation.   For other operations, this property will be null.
     */
    public static final String PROPNAME_VERSION_ID = "VERSION_ID";

    /**
     * This property is of type String and contains the correlation id associated with 
     * the transaction associated with the persistence event (if applicable).
     * This property will be set by the FHIR REST engine when it is processing the requests within 
     * a transaction bundle.   All requests processed within a transaction bundle will share the same 
     * transaction correlation id value.   For operations not involving a transaction bundle, 
     * this property will be null.
     */
    public static final String PROPNAME_TXN_CORRELATION_ID = "TXN_CORRELATION_ID";

    /**
     * This property is of type String and contains the request correlation id associated with 
     * the persistence event (if applicable).
     * This property will be set by the FHIR REST engine when it is processing the requests within 
     * a bundle (either batch or transaction).   All requests processed within a bundle will share the same 
     * request correlation id value.   For operations not involving a bundle, this property will be null.
     */
    public static final String PROPNAME_REQUEST_CORRELATION_ID = "REQUEST_CORRELATION_ID";
    
    /**
     * This property holds the ReplicationInfo instance associated with the request.
     * The ReplicationInfo object holds information that is used by the persistence layer
     * to create an entry in the replication log for a create, update, or delete operation.
     */
    public static final String PROPNAME_REPLICATION_INFO = "REPLICATION_INFO";
    
    /**
     * This property holds the ReplicationContext instance associated with the request.
     * The ReplicationContext object is passed to the persistence layer by the replication
     * consumer component in order to store a replicated resource.
     */
    public static final String PROPNAME_REPLICATION_CONTEXT = "REPLICATION_CONTEXT";
    
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
     * Returns the resource associated with the REST API request that triggered the 
     * interceptor invocation.  This will be non-null before and after a create or update operation,
     * and will be non-null after a read, vread, history or search operation.
     */
    public Resource getFhirResource() {
        return fhirResource;
    }
    
    /**
     * Sets the specific resource in 'this'.
     * Interceptor implementations should *not* call this method.  This method is reserved for use by the FHIR Server.
     */
    public void setFhirResource(Resource resource) {
        this.fhirResource = resource;
    }
    
    /**
     * Returns the resource type associated with the FHIR REST API request that triggered the
     * interceptor invocation.   This will be non-null for a create, update, read, vread, history or search operation.
     */
    public String getFhirResourceType() {
        return (String) getProperty(PROPNAME_RESOURCE_TYPE);
    }
    
    /**
     * Returns the resource id associated with the FHIR REST API request that triggered the
     * interceptor invocation.   This will be non-null for an update, read, vread or history operation.
     */
    public String getFhirResourceId() {
        return (String) getProperty(PROPNAME_RESOURCE_ID);
    }
    
    /**
     * Returns the version id associated with the FHIR REST API request that triggered the 
     * interceptor invocation.  This will be non-null for a vread operation.
     */
    public String getFhirVersionId() {
        return (String) getProperty(PROPNAME_VERSION_ID);
    }
    
    /**
     * Returns true if and only if the resource type value contained in the persistence event 
     * represents a standard FHIR resource type.
     */
    public boolean isStandardResourceType() {
        return (this.getFhirResourceType() != null ? FHIRUtil.isStandardResourceType(getFhirResourceType()) : false);
    }
    
    /**
     * Returns the HttpHeaders instance associated with the FHIR REST API request that triggered the
     * interceptor invocation.
     * Note that this HttpHeaders instance is only valid within the scope of the REST API request.
     */
    public HttpHeaders getHttpHeaders() {
        return (HttpHeaders) getProperty(PROPNAME_HTTP_HEADERS);
    }
    
    /**
     * Returns the SecurityContext instance associated with the FHIR REST API request that triggered the
     * interceptor invocation.
     * Note that this SecurityContext instance is only valid within the scope of the REST API request.
     */
    public SecurityContext getSecurityContext() {
        return (SecurityContext) getProperty(PROPNAME_SECURITY_CONTEXT);
    }
    
    /**
     * Returns the UriInfo instance associated with the FHIR REST API request that triggered the
     * interceptor invocation.  
     * Note that this UriInfo instance is only valid within the scope of the REST API request.
     */
    public UriInfo getUriInfo() {
        return (UriInfo) getProperty(PROPNAME_URI_INFO);
    }
    
    /**
     * Returns the transaction correlation id associated with the current persistence event.
     * If the persistence event is not associated with a transaction bundle, then this value will be null.
     */
    public String getTransactionCorrelationId() {
        return (String) getProperty(PROPNAME_TXN_CORRELATION_ID);
    }
    
    /**
     * Returns the request correlation id associated with the current persistence event.
     * If the persistence event is not associated with a bundle, then this value will be null.
     */
    public String getRequestCorrelationId() {
        return (String) getProperty(PROPNAME_REQUEST_CORRELATION_ID);
    }
    
    /**
     * Returns the FHIRReplicationContext object associated with the current persistence event,
     * or null if the persistence event is not for a replicated resource.
     */
    public FHIRReplicationContext getReplicationContext() {
        return (FHIRReplicationContext) getProperty(PROPNAME_REPLICATION_CONTEXT);
    }
    
    /**
     * Returns the FHIRPersistence instance currently being used by the FHIR REST API layer
     * to process the current request.
     */
    public FHIRPersistence getPersistenceImpl() {
        return (FHIRPersistence) getProperty(PROPNAME_PERSISTENCE_IMPL);
    }
    
    /**
     * Retrieves the named property from the set of properties available to the interceptor.
     * @param propertyName the name of the property to retrieve.
     */
    public Object getProperty(String propertyName) {
        return (properties != null ? properties.get(propertyName) : null);
    }
    
    /**
     * Retrieves the set of properties associated with the FHIR REST API request that triggered
     * the interceptor invocation.
     */
    public Map<String, Object> getProperties() {
        if (properties == null) {
            properties = new HashMap<String, Object>();
        }
        return properties;
    }
}
