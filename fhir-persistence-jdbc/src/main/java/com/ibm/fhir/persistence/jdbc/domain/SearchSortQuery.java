/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.domain;

import java.util.ArrayList;
import java.util.List;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * Domain model of the FHIR search context representing the query used
 * to perform the search sort operation in the database. This query
 * provides a list of ids ordered by the requested search parameters
 * and these ids are then used in a subsequent fetch query
 */
public class SearchSortQuery extends SearchQuery {
    final List<DomainSortParameter> sortParameters = new ArrayList<>();

    /**
     * Public constructor
     * @param resourceType
     */
    public SearchSortQuery(String resourceType) {
        super(resourceType);
    }

    /**
     * Add the given sort parameter sp to the sortParameters list.
     * @param dsp
     */
    public void add(DomainSortParameter dsp) {
        this.sortParameters.add(dsp);
    }

    @Override
    public <T> T getRoot(SearchQueryVisitor<T> visitor) {
        // The sortRoot is just the filtered join (similar to the count query) - there's
        // no need to join the xx_RESOURCES table
        // SELECT LR.CURRENT_RESOURCE_ID
        //   FROM ( ) AS LR
        return visitor.sortRoot(getRootResourceType());
    }

    @Override
    public <T> T visit(SearchQueryVisitor<T> visitor) throws FHIRPersistenceException {

        // Special case for the sort query because we don't want to wrap
        // and join the xx_RESOURCES table...it is already part of the join.
        T query = getRoot(visitor);

        T parameterBase = visitor.getParameterBaseQuery(query);
        visitSearchParams(parameterBase, visitor);

        // Now add the aggregation fields and group by clause
        for (DomainSortParameter dsp: this.sortParameters) {
            dsp.visit(query, visitor);
        }

        query = visitor.addPagination(query);

        return query;
    }
}