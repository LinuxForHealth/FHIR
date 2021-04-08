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
 *
 */
public abstract class SearchQuery {

    private final String rootResourceType;
    private final List<SearchParam> searchParams = new ArrayList<>();

    /**
     * Public constructor
     * @param rootResourceType
     */
    public SearchQuery(String rootResourceType) {
        this.rootResourceType = rootResourceType;
    }

    /**
     * Getter for the root resource type for this search query
     * @return
     */
    public String getRootResourceType() {
        return this.rootResourceType;
    }

    /**
     * Add the search parameter to the model
     * @param sp
     */
    public void add(SearchParam sp) {
        this.searchParams.add(sp);
    }

    /**
     * Get the root query and attach the parameter filters to it
     * @param <T>
     * @param visitor
     * @return
     */
    public <T> T visit(SearchQueryVisitor<T> visitor) throws FHIRPersistenceException {
        T query = getRoot(visitor);
        for (SearchParam sp: this.searchParams) {
            query = sp.visit(query, visitor);
        }
        return query;
    }


    /**
     * Process the logical query definition through the visitor
     * @param <T>
     * @param visitor
     * @return
     */
    public abstract <T> T getRoot(SearchQueryVisitor<T> visitor);

}