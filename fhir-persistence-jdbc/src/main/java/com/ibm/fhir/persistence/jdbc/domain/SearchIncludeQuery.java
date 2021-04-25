/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.domain;

/**
 * Domain model of the FHIR search context representing the query used
 * to perform the search operation in the database. The query built by
 * this class fetches the data for the _include phase of searches.
 */
public class SearchIncludeQuery extends SearchDataQuery {

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
}