/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.context;

import com.ibm.fhir.persistence.payload.PayloadPersistenceResponse;
import com.ibm.fhir.search.context.FHIRSearchContext;

/**
 * This interface is used to provide request context-related information
 * to the FHIR Server persistence layer.
 */
public interface FHIRPersistenceContext {

    /**
     * Returns the FHIRPersistenceEvent instance for the current request.
     * This contains information about the security context, HTTP headers,
     * request URI information, etc.
     */
    FHIRPersistenceEvent getPersistenceEvent();

    /**
     * Returns the FHIRHistoryContext instance associated with the current request.
     * This will be null if the current request is not a 'history' operation.
     */
    FHIRHistoryContext getHistoryContext();

    /**
     * Returns the FHIRSearchContext instance associated with the current request.
     * This will be null if the current request is not a 'search' operation.
     */
    FHIRSearchContext getSearchContext();

    /**
     * Indicates whether the persistence layer should include "deleted" resources in the operation response.
     */
    boolean includeDeleted();
    
    /**
     * Get the encoded ifNoneMatch value which is interpreted as follows:
     * <pre>
     *    null: create-on-update proceeds as normal
     *       0: create-on-update returns 304 if resource exists, 201 if created
     * </pre>
     * @return the value from the If-None-Match header in the PUT request
     */
    Integer getIfNoneMatch();
    
    /**
     * Get the payload persistence response 
     * @return
     */
    PayloadPersistenceResponse getOffloadResponse();

    /**
     * Get the key used for sharding used by the distributed schema variant. If
     * the tenant is not configured for distribution, the value will be null
     * @return any Short value in [Short.MIN_VALUE, Short.MAX_VALUE] or null
     */
    Short getShardKey();
}