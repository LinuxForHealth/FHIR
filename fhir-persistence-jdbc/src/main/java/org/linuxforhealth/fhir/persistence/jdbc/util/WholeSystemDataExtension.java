/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.util;

import java.util.List;

import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.persistence.jdbc.domain.SearchExtension;
import org.linuxforhealth.fhir.persistence.jdbc.domain.SearchQueryVisitor;

/**
 * A SearchExtension used to add whole-system data search filters to the
 * whole-system data query.
 */
public class WholeSystemDataExtension implements SearchExtension {
    // The resource type being processed
    private final String resourceType;

    // The list of LOGICAL_RESOURCE_ID values to filter the query
    private final List<Long> logicalResourceIds;

    /**
     * Public constructor
     * @param resourceType
     * @param logicalResourcesIds
     */
    public WholeSystemDataExtension(String resourceType, List<Long> logicalResourceIds) {
        this.resourceType = resourceType;
        this.logicalResourceIds = logicalResourceIds;
    }

    @Override
    public <T> T visit(T query, SearchQueryVisitor<T> visitor) throws FHIRPersistenceException {
        return visitor.addWholeSystemDataFilter(query, resourceType, logicalResourceIds);
    }
}