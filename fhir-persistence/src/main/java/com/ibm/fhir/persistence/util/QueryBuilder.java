/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.util;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.search.context.FHIRSearchContext;

/**
 * Defines methods for for building persistence layer queries.
 * @param <T>
 */
public interface QueryBuilder<T> {
    
    /**
     * Build and return query for the passed resource type and search parameters.
     * @param resourceType - A FHIR Resource subclass.
     * @param searchParameters - A search context that contains a List of search parameters to be used for constructing the query.
     * @return T - An instance of <T> representing the constructed query.
     * @throws FHIRPersistenceException - Thrown for any non-recoverable failure that occurs during query construction.
     * @throws Exception 
     */
    T buildQuery(Class<?> resourceType, FHIRSearchContext searchContext) throws FHIRPersistenceException, Exception;
}
