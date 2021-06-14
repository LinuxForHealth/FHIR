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
 * Domain model of the FHIR search context representing the query used
 * to perform the search operation in the database. The query built by
 * this class fetches the filter data for whole system searches, which
 * is a set of logical resource id + resource type.
 */
public class SearchWholeSystemFilterQuery extends SearchQuery {
    
    // Sort parameters
    final List<DomainSortParameter> sortParameters = new ArrayList<>();

    /**
     * Public constructor
     */
    public SearchWholeSystemFilterQuery() {
        super(Resource.class.getSimpleName());
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
        return visitor.wholeSystemFilterRoot();
    }

    @Override
    public <T> T visit(SearchQueryVisitor<T> visitor) throws FHIRPersistenceException {

        // get the core data query
        T query = super.visit(visitor);

        // now attach the requisite ordering and pagination clauses
        query = visitor.addWholeSystemSorting(query, sortParameters, "LR0");
        query = visitor.addPagination(query);

        return query;
    }
}