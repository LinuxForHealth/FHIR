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
 * this class fetches the resource data for whole system searches.
 */
public class SearchWholeSystemDataQuery extends SearchQuery {
    
    // The database resource_type_id matching the resourceType of this sub-query
    final int resourceTypeId;

    /**
     * Public constructor
     * @param resourceType
     */
    public SearchWholeSystemDataQuery(String resourceType, int resourceTypeId) {
        super(resourceType);
        this.resourceTypeId = resourceTypeId;
    }

    @Override
    public <T> T getRoot(SearchQueryVisitor<T> visitor) {
        return visitor.wholeSystemDataRoot(getRootResourceType(), this.resourceTypeId);
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
        query = visitor.joinResources(query, INCLUDE_RESOURCE_TYPE_ID);

        return query;
    }
}