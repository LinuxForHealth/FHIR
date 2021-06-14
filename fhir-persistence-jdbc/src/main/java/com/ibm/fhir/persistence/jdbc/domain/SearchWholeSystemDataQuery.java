/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.domain;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * Domain model of the FHIR search context representing the query used
 * to perform the search operation in the database. The query built by
 * this class fetches the resource data for whole system searches.
 */
public class SearchWholeSystemDataQuery extends SearchQuery {

    /**
     * Public constructor
     * @param resourceType
     */
    public SearchWholeSystemDataQuery(String resourceType) {
        super(resourceType);
    }

    @Override
    public <T> T getRoot(SearchQueryVisitor<T> visitor) {
        return visitor.wholeSystemDataRoot(getRootResourceType());
    }

    @Override
    public <T> T visit(SearchQueryVisitor<T> visitor) throws FHIRPersistenceException {

        // get the core data query
        // NOTE: we don't call super.visit() here because it would add an
        // unnecessary EXISTS clause to the query when processing parameters.
        // We never have parameters so don't need that processing.
        T query = getRoot(visitor);

        // Pre-process the whole-system data extension
        visitExtensions(query, visitor);

        // Join the core logical resource selection to the resource versions table
        query = visitor.joinResources(query);

        return query;
    }
}