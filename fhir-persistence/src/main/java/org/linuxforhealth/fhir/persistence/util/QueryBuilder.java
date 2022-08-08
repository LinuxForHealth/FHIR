/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.util;

import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.search.context.FHIRSearchContext;

/**
 * Defines methods for for building persistence layer queries.
 *
 * @param <T>
 */
@Deprecated
public interface QueryBuilder<T> {

    /**
     * Build and return query for the passed resource type and search parameters.
     *
     * @param resourceType A FHIR Resource subclass.
     * @param searchContext A search context that contains a List of search parameters to be used for constructing the query.
     * @return An instance of T representing the constructed query.
     * @throws FHIRPersistenceException thrown for any non-recoverable failure that occurs during query construction.
     * @throws Exception
     */
    T buildQuery(Class<?> resourceType, FHIRSearchContext searchContext) throws FHIRPersistenceException, Exception;
}
