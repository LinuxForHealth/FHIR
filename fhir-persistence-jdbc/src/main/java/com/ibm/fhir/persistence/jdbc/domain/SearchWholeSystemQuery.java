/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.domain;

import java.util.ArrayList;
import java.util.List;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * A domain model of the query used to retrieve the count or data
 * for a particular whole-system search.
 */
public class SearchWholeSystemQuery extends SearchQuery {
    
    // Set of domain models for resource types used in this whole-system search
    List<SearchQuery> domainModels;

    // Flag indicating if a count query or a data query
    boolean isCountQuery;

    // Is pagination required?
    boolean addPagination = true;

    // Sort parameters
    final List<DomainSortParameter> sortParameters = new ArrayList<>();

    /**
     * Public constructor
     * @param domainModels
     * @param isCountQuery
     * @param addPagination
     */
    public SearchWholeSystemQuery(List<SearchQuery> domainModels, boolean isCountQuery, boolean addPagination) {
        super(Resource.class.getSimpleName());
        this.domainModels = domainModels;
        this.isCountQuery = isCountQuery;
        this.addPagination = addPagination;
    }

    /**
     * Add the given sort parameter to the sortParameters list.
     * @param dsp
     */
    public void add(DomainSortParameter dsp) {
        this.sortParameters.add(dsp);
    }

    @Override
    public <T> T getRoot(SearchQueryVisitor<T> visitor) {
        return null;
    }

    @Override
    public <T> T visit(SearchQueryVisitor<T> visitor) throws FHIRPersistenceException {

        // Get the core data queries
        List<T> queries = new ArrayList<>();
        for (SearchQuery domainModel : domainModels) {
            queries.add(domainModel.visit(visitor));
        }

        // Get the overall query
        T query = visitor.wrapWholeSystem(queries, isCountQuery);
        
        // Add sorting and pagination
        if (!isCountQuery) {
            query = visitor.addWholeSystemSorting(query, sortParameters, "COMBINED_RESULTS");
            if (addPagination) {
                query = visitor.addPagination(query);
            }
        }

        return query;
    }
}