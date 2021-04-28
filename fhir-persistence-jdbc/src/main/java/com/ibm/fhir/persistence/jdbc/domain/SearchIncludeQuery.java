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
 * this class fetches the data for the _include phase of searches.
 */
public class SearchIncludeQuery extends SearchQuery {

    /**
     * Public constructor
     * @param resourceType
     */
    public SearchIncludeQuery(String resourceType) {
        super(resourceType);
    }

    @Override
    public <T> T getRoot(SearchQueryVisitor<T> visitor) {
        return visitor.includeRoot(getRootResourceType());
    }


    @Override
    public <T> T visit(SearchQueryVisitor<T> visitor) throws FHIRPersistenceException {

        // Special case for the include query because we don't want to wrap
        // and join the xx_RESOURCES table...it is already part of the join.
        T query = super.visit(visitor);

        // now attach the requisite ordering and pagination clauses
        query = visitor.addSorting(query, "LR0");
        query = visitor.addPagination(query);

        return query;
    }
}