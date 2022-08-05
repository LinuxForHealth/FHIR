/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.domain;

/**
 * A domain model of the query used to count the number of resources
 * matching a particular search context. The purpose of this class
 * is to model the general structure of the query we need to perform
 * the search, but not worry about the details of exactly which tables
 * are required and how they should be joined. This abstraction lets
 * us hide details of the schema from the complex search code.
 *
 * This also makes it easier to implement different queries and or
 * join strategies if we find one thing works better than another.
 *
 * The new query builder avoids the DISTINCT clause (previously required
 * because of duplicate parameter matches). Instead, parameters are
 * matched using an EXISTS clause, which avoids any issues related
 * to duplicates, and thus the DISTINCT is no longer needed.
 */
public class SearchCountQuery extends SearchQuery {

    /**
     * Public constructor
     * @param rootResourceType
     */
    public SearchCountQuery(String rootResourceType) {
        super(rootResourceType);
    }

    @Override
    public <T> T getRoot(SearchQueryVisitor<T> visitor) {
        return visitor.countRoot(getRootResourceType());
    }
}