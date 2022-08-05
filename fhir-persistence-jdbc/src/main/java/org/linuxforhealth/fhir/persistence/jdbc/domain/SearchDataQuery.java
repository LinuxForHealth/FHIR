/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.domain;

import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;

/**
 * Domain model of the FHIR search context representing the query used
 * to perform the search operation in the database. The query built by
 * this class fetches the data and handles pagination
 */
public class SearchDataQuery extends SearchQuery {
    // Is sorting required?
    boolean addSorting = true;
    
    // Is pagination required?
    boolean addPagination = true;
    
    int resourceTypeId = -1;
    

    /**
     * Public constructor
     * @param resourceType
     */
    public SearchDataQuery(String resourceType) {
        super(resourceType);
    }

    /**
     * Public constructor
     * @param resourceType
     * @param addSorting
     * @param addPagination
     */
    public SearchDataQuery(String resourceType, boolean addSorting, boolean addPagination, int resourceTypeId) {
        super(resourceType);
        this.resourceTypeId = resourceTypeId;
        this.addSorting = addSorting;
        this.addPagination = addPagination;
    }

    @Override
    public <T> T getRoot(SearchQueryVisitor<T> visitor) {
        return visitor.dataRoot(getRootResourceType(), resourceTypeId);
    }

    @Override
    public <T> T visit(SearchQueryVisitor<T> visitor) throws FHIRPersistenceException {

        // get the core data query
        T query = super.visit(visitor);

        // Join the core logical resource selection to the resource versions table
        final boolean includeResourceTypeId = this.resourceTypeId >= 0;
        query = visitor.joinResources(query, includeResourceTypeId);

        // now attach the requisite ordering and pagination clauses
        if (addSorting) {
            query = visitor.addSorting(query, "LR");
        }
        if (addPagination) {
            query = visitor.addPagination(query);
        }

        return query;
    }
}