/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.spi.interceptor;

import com.ibm.fhir.persistence.context.FHIRPersistenceEvent;
import com.ibm.fhir.server.spi.operation.FHIROperationContext;

/**
 * This interface describes a persistence interceptor. Persistence interceptors are invoked by the FHIR Server to allow
 * users to inject business logic into the REST API processing flow. To make use of this interceptor, develop a class
 * that implements this interface, then store your implementation class name in a file called
 * META-INF/services/com.ibm.fhir.persistence.FHIRPersistenceInterceptor within your jar file.
 */
public interface FHIRPersistenceInterceptor {
    /**
     * This method is called during the processing of a 'create' REST API invocation, immediately before the new
     * resource is stored by the persistence layer.
     *
     * @param event
     *            information about the 'create' event
     * @throws FHIRPersistenceInterceptorException
     */
    default void beforeCreate(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
    }

    /**
     * This method is called during the processing of a 'create' REST API invocation, immediately after the new resource
     * has been stored by the persistence layer.
     *
     * @param event
     *            information about the 'create' event
     * @throws FHIRPersistenceInterceptorException
     */
    default void afterCreate(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
    }

    /**
     * This method is called during the processing of an 'update' REST API invocation, immediately before the updated
     * resource is stored by the persistence layer.
     *
     * @param event
     *            information about the 'update' event
     * @throws FHIRPersistenceInterceptorException
     */
    default void beforeUpdate(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
    }

    /**
     * This method is called during the processing of an 'update' REST API invocation, immediately after the updated
     * resource has been stored by the persistence layer.
     *
     * @param event
     *            information about the 'update' event
     * @throws FHIRPersistenceInterceptorException
     */
    default void afterUpdate(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
    }

    /**
     * This method is called during the processing of an 'patch' REST API invocation, immediately before the updated
     * resource is stored by the persistence layer.
     *
     * @param event
     *            information about the 'patch' event
     * @throws FHIRPersistenceInterceptorException
     */
    default void beforePatch(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
    }

    /**
     * This method is called during the processing of an 'patch' REST API invocation, immediately after the updated
     * resource has been stored by the persistence layer.
     *
     * @param event
     *            information about the 'patch' event
     * @throws FHIRPersistenceInterceptorException
     */
    default void afterPatch(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
    }

    /**
     * This method is called during the processing of a 'delete' REST API invocation, immediately before the
     * resource is deleted by the persistence layer.
     *
     * @param event
     *            information about the 'delete' event
     * @throws FHIRPersistenceInterceptorException
     */
    default void beforeDelete(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
    }

    /**
     * This method is called during the processing of a 'delete' REST API invocation, immediately after the
     * resource has been deleted by the persistence layer.
     *
     * @param event
     *            information about the 'delete' event
     * @throws FHIRPersistenceInterceptorException
     */
    default void afterDelete(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
    }

    /**
     * This method is called during the processing of a 'read' REST API invocation, immediately before the resource is
     * read by the persistence layer.
     *
     * @param event
     *            information about the 'read' event
     * @throws FHIRPersistenceInterceptorException
     */
    default void beforeRead(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
    }

    /**
     * This method is called during the processing of a 'read' REST API invocation, immediately after the resource has
     * been read by the persistence layer.
     *
     * @param event
     *            information about the 'read' event
     * @throws FHIRPersistenceInterceptorException
     */
    default void afterRead(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
    }

    /**
     * This method is called during the processing of a 'vread' (versioned read) REST API invocation, immediately before
     * the resource is read by the persistence layer.
     *
     * @param event
     *            information about the 'vread' event
     * @throws FHIRPersistenceInterceptorException
     */
    default void beforeVread(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
    }

    /**
     * This method is called during the processing of a 'vread' REST API invocation, immediately after the resource has
     * been read by the persistence layer.
     *
     * @param event
     *            information about the 'vread' event
     * @throws FHIRPersistenceInterceptorException
     */
    default void afterVread(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
    }

    /**
     * This method is called during the processing of a 'history' REST API invocation, immediately before the resource's
     * history is read by the persistence layer.
     *
     * @param event
     *            information about the 'history' event
     * @throws FHIRPersistenceInterceptorException
     */
    default void beforeHistory(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
    }

    /**
     * This method is called during the processing of a 'history' REST API invocation, immediately after the resource's
     * history has been read by the persistence layer.
     *
     * @param event
     *            information about the 'history' event
     * @throws FHIRPersistenceInterceptorException
     */
    default void afterHistory(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
    }

    /**
     * This method is called during the processing of a 'search' REST API invocation, immediately before the search is
     * performed by the persistence layer.
     *
     * @param event
     *            information about the 'search' event
     * @throws FHIRPersistenceInterceptorException
     */
    default void beforeSearch(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
    }

    /**
     * This method is called during the processing of a 'search' REST API invocation, immediately after the search has
     * been performed by the persistence layer.
     *
     * @param event
     *            information about the 'search' event
     * @throws FHIRPersistenceInterceptorException
     */
    default void afterSearch(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
    }

    /**
     * This method is called during the invocation of a 'custom operation', immediately before the operation logic
     * is executed.
     *
     * @param context
     *            information about the 'invoke' event
     * @throws FHIRPersistenceInterceptorException
     */
    default void beforeInvoke(FHIROperationContext event) throws FHIRPersistenceInterceptorException {
    }

    /**
     * This method is called during the invocation of a 'custom operation', immediately after the operation logic
     * is executed.
     *
     * @param context
     *            information about the 'invoke' event
     * @throws FHIRPersistenceInterceptorException
     */
    default void afterInvoke(FHIROperationContext event) throws FHIRPersistenceInterceptorException {
    }
}
