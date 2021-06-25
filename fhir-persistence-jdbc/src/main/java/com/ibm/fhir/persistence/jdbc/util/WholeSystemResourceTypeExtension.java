/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util;

import java.util.List;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.domain.SearchExtension;
import com.ibm.fhir.persistence.jdbc.domain.SearchQueryVisitor;

/**
 * A SearchExtension used to add resource type id filters to the
 * whole-system count and data filter queries when the _type parameter
 * is specified.
 */
public class WholeSystemResourceTypeExtension implements SearchExtension {
    // The list of RESOURCE_TYPE_ID values to filter the query
    private final List<Integer> resourceTypeIds;

    /**
     * Public constructor
     * @param resourceTypeIds
     */
    public WholeSystemResourceTypeExtension(List<Integer> resourceTypeIds) {
        this.resourceTypeIds = resourceTypeIds;
    }

    @Override
    public <T> T visit(T query, SearchQueryVisitor<T> visitor) throws FHIRPersistenceException {
        return visitor.addWholeSystemResourceTypeFilter(query, resourceTypeIds);
    }
}