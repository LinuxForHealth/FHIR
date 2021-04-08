/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.query;

import com.ibm.fhir.persistence.jdbc.statement.FHIRQuery;

/**
 * The domain model for a FHIR search query. This is constructed from the
 * search context and models the logical query structure but doesn't know
 * the details of how best to join tables. Those details come when this
 * model is used to render an executable query using an IQueryBuilder.
 *
 * This separation of concerns is intended to isolate the business logic
 * related to FHIR search functions from the underlying joins we need to
 * perform. This separation makes it easier to make schema changes without
 * breaking that business logic code (too much). It should also make things
 * a lot easier to unit-test.
 */
public class SearchQueryModel {

    // The resource type used as the base of the search. Null for whole-system searches
    private final String primaryResourceType;

    /**
     * Public constructor
     * @param primaryResourceType
     */
    public SearchQueryModel(String primaryResourceType) {
        this.primaryResourceType = primaryResourceType;
    }

    /**
     * Build the query using the given {@link IQueryBuilder} as a helper. The
     * queryBuilder knows about the physical tables and how to join them to
     * satisfy the search logic represented by this model.
     * @param queryBuilder
     * @return
     */
    public FHIRQuery build(IQueryBuilder queryBuilder) {
        FHIRQuery result = null;
        return result;
    }
}