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
 *
 */
public class IncludeExtension implements SearchExtension {
    private final InclusionParameter inclusionParm;

    private final List<Long> ids;

    /**
     * Public constructor
     * @param inclusionParm
     * @param ids
     */
    public IncludeExtension(InclusionParameter inclusionParm, List<Long> ids) {
        this.inclusionParm = inclusionParm;
        this.ids = ids;
    }

    @Override
    public <T> T visit(T query, SearchQueryVisitor<T> visitor) throws FHIRPersistenceException {
        return visitor.addIncludeFilter(query, inclusionParm, ids);
    }
}