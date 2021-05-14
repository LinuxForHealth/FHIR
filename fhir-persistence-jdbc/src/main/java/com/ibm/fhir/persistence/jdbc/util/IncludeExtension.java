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
import com.ibm.fhir.search.parameters.InclusionParameter;

/**
 * A SearchExtension used to add InclusionParameter filters to the
 * include query.
 */
public class IncludeExtension implements SearchExtension {
    // The parameter being processed
    private final InclusionParameter inclusionParm;

    // The list of LOGICAL_RESOURCE_ID values to filter the query
    private final List<Long> logicalResourceIds;

    /**
     * Public constructor
     * @param inclusionParm
     * @param logicalResourcesIds
     */
    public IncludeExtension(InclusionParameter inclusionParm, List<Long> logicalResourceIds) {
        this.inclusionParm = inclusionParm;
        this.logicalResourceIds = logicalResourceIds;
    }

    @Override
    public <T> T visit(T query, SearchQueryVisitor<T> visitor) throws FHIRPersistenceException {
        return visitor.addIncludeFilter(query, inclusionParm, logicalResourceIds);
    }
}