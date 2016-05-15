/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.interceptor;

/**
 * This interface describes a persistence interceptor.
 * Persistence interceptors are invoked by the FHIR Server to allow
 * users to inject business logic into the REST API processing flow.
 * To make use of this interceptor, develop a class that implements this interface,
 * then store your implementation class name in a file called
 * META-INF/services/com.ibm.watsonhealth.fhir.persistence.FHIRPersistenceInterceptor within
 * your jar file.
 */
public interface FHIRPersistenceInterceptor {
    
    /**
     * This method is called during the processing of a 'create' REST API invocation, 
     * immediately before the new resource is stored by the persistence layer.
     * @param event information about the 'create' event
     * @throws FHIRPersistenceInterceptorException
     */
    void beforeCreate(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException;
    
    /**
     * This method is called during the processing of a 'create' REST API invocation, 
     * immediately after the new resource has been stored by the persistence layer.
     * @param event information about the 'create' event
     * @throws FHIRPersistenceInterceptorException
     */
    void afterCreate(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException;

    /**
     * This method is called during the processing of an 'update' REST API invocation, 
     * immediately before the updated resource is stored by the persistence layer.
     * @param event information about the 'update' event
     * @throws FHIRPersistenceInterceptorException
     */
    void beforeUpdate(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException;
    
    /**
     * This method is called during the processing of an 'update' REST API invocation, 
     * immediately after the updated resource has been stored by the persistence layer.
     * @param event information about the 'update' event
     * @throws FHIRPersistenceInterceptorException
     */
    void afterUpdate(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException;
}
