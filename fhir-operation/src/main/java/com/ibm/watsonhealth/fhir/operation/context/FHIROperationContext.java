/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.operation.context;

import java.util.HashMap;
import java.util.Map;

/**
 * This class holds context information for custom operation invocations.
 */
public class FHIROperationContext {
    public static enum Type { SYSTEM, RESOURCE_TYPE, INSTANCE }

    /**
     * This property is of type String and represents the base URI string (e.g.
     * "https://localhost:9443/fhir-server/api/v1") associated with the request.
     */
    public static final String PROPNAME_REQUEST_BASE_URI = "REQUEST_BASE_URI";

    /**
     * This property is of type String and represents the value of the Location header.
     */
    public static final String PROPNAME_LOCATION_URI = "LOCATION_URI";
    
    /**
     * This property is of type FHIRResourceHelper and is the handle to the 
     * FHIR REST API layer.   This interface provides methods to perform various
     * operations on FHIR resources (e.g. create, update, read, etc.)
     */
    public static final String PROPNAME_RESOURCE_HELPER = "RESOURCE_HELPER";
    
    /**
     * This property is of type FHIRPersistence and is the handle to the persistence layer implementation
     * being used by the FHIR Server while processing the current request.
     * It is recommended that custom operation implementors use the RESOURCE_HELPER property instead of
     * this property unless you have been advised otherwise.
     */
    public static final String PROPNAME_PERSISTENCE_IMPL = "PERSISTENCE_IMPL";
    
    /**
     * This property is of type javax.ws.rs.core.UriInfo and contains Application and Request
     * URI information associated with the REST API request for which the interceptor is being invoked.
     */
    public static final String PROPNAME_URI_INFO = "URI_INFO";
    
    /**
     * This property is of type javax.ws.rs.core.HttpHeaders and contains the set of HTTP headers
     * associated with the REST API request.
     */
    public static final String PROPNAME_HTTP_HEADERS = "HTTP_HEADERS";
    
    /**
     * This property is of type javax.ws.rs.core.SecurityContext and contains security-related information
     * associated with the REST API request for which the interceptor is being invoked.
     */
    public static final String PROPNAME_SECURITY_CONTEXT = "SECURITY_CONTEXT";
    

    private Type type = null;
    private Map<String, Object> properties = null;
    
    private FHIROperationContext(Type type) {
        if (type == null) {
            throw new IllegalArgumentException("Context type cannot be null");
        }
        this.type = type;
        properties = new HashMap<String, Object>();
    }
    
    public Type getType() {
        return type;
    }
    
    public void setProperty(String name, Object value) {
        properties.put(name, value);
    }
    
    public Object getProperty(String name) {
        return properties.get(name);
    }
    
    public static FHIROperationContext createSystemOperationContext() {
        return new FHIROperationContext(Type.SYSTEM);
    }
    
    public static FHIROperationContext createResourceTypeOperationContext() {
        return new FHIROperationContext(Type.RESOURCE_TYPE);
    }
    
    public static FHIROperationContext createInstanceOperationContext() {
        return new FHIROperationContext(Type.INSTANCE);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName()).append(" [");
        sb.append("type=" + type.name());
        sb.append(", properties={").append((properties != null ? properties.toString() : "<null>")).append("}");
        sb.append("]");
        
        return sb.toString();
    }
}
