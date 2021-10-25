/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.context;

import java.util.HashMap;
import java.util.Map;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.search.context.FHIRSearchContext;

/**
 * This class represents an event fired by the FHIR persistence interceptor framework.
 */
public class FHIRPersistenceEvent {
    /**
     * This property is of type FHIRPersistence and is the handle to the persistence layer implementation
     * being used by the FHIR Server while processing the current request.
     * Persistence interceptors can use this handle to invoke persistence operations.
     */
    public static final String PROPNAME_PERSISTENCE_IMPL = "PERSISTENCE_IMPL";

    /**
     * This property is of type String and contains the location URI that can be used to
     * retrieve the resource via a GET request.
     */
    public static final String PROPNAME_RESOURCE_LOCATION_URI = "LOCATION_URI";

    /**
     * This property is of type String and contains the resource type associated with a
     * create, update, read, vread, history, search, or delete operation.
     * For other operations, this property will be null.
     */
    public static final String PROPNAME_RESOURCE_TYPE = "RESOURCE_TYPE";

    /**
     * This property is of type String and contains the resource id associated with an
     * update, read, vread, history, or delete operation.
     * For update and delete it may be null (i.e. for conditional updates/deletes)
     * For other operations, this property will be null.
     */
    public static final String PROPNAME_RESOURCE_ID = "RESOURCE_ID";

    /**
     * This property is of type String and contains the version id associated with a
     * vread operation.   For other operations, this property will be null.
     */
    public static final String PROPNAME_VERSION_ID = "VERSION_ID";

    /**
     * This property holds the FHIRPatch instance associated with the request.
     */
    public static final String PROPNAME_PATCH = "PATCH";

    /**
     * This property is of type FHIRSearchContext and is the search context
     * associated with a search request, but it may be null.
     * For other operations, this property will be null.
     */
    public static final String PROPNAME_SEARCH_CONTEXT_IMPL = "SEARCH_CONTEXT_IMPL";

    private Resource fhirResource;
    private Resource prevFhirResource = null;
    private boolean  prevFhirResourceSet = false;
    private Map<String, Object> properties;

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
     * Returns the "previous" resource associated with the REST API request that triggered
     * the interceptor invocation.  This field is set only for an "update" operation and represents
     * the existing version of the resource prior to the new resource being stored.
     */
    public Resource getPrevFhirResource() {
        return prevFhirResource;
    }

    /**
     * Sets the "previous" resource associated with an "update" request.
     *
     * @param prevFhirResource the existing most recent version of the resource
     * prior to the update operation being processed.
     *
     */
    public void setPrevFhirResource(Resource prevFhirResource) {
        this.prevFhirResource = prevFhirResource;
        this.prevFhirResourceSet = true;
    }

    /**
     * This method returns true if and only if the "previous resource" field has in fact
     * been set.   This flag exists so that we can differentiate between these two scenarios:
     * <ul>
     * <li>The "previous resource" field is explicitly set to null.</li>
     * <li>The "previous resource" field is not explicitly set at all.</li>
     * </ul>
     *
     * @return true if the "previous resource" field is set
     * (including the situation where it is set to null); false otherwise
     */
    public boolean isPrevFhirResourceSet() {
        return prevFhirResourceSet;
    }

    /**
     * Returns the resource type associated with the FHIR REST API request that triggered the
     * interceptor invocation.   This will be non-null for a
     * create, update, read, vread, history, search, or delete operation.
     */
    public String getFhirResourceType() {
        return (String) getProperty(PROPNAME_RESOURCE_TYPE);
    }

    /**
     * Returns the resource id associated with the FHIR REST API request that triggered the
     * interceptor invocation.   This will be non-null for a read, vread, history, or non-conditional update/delete operation.
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
        return (this.getFhirResourceType() != null ? ModelSupport.isResourceType(getFhirResourceType()) : false);
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
     * Retrieves the set of properties associated with the event that triggered
     * the interceptor invocation.
     */
    public Map<String, Object> getProperties() {
        if (properties == null) {
            properties = new HashMap<String, Object>();
        }
        return properties;
    }

    /**
     * Returns the FHIRSearchContext instance currently being used by the FHIR REST API layer
     * to process the current request.
     */
    public FHIRSearchContext getSearchContextImpl() {
        return (FHIRSearchContext) getProperty(PROPNAME_SEARCH_CONTEXT_IMPL);
    }

}
