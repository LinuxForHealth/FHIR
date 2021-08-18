/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.domain;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.search.parameters.SortParameter;

/**
 * Decorates SortParameter for use with the new query builder domain model. There's
 * no interface for SortParameter, so it's not true decoration, of course.
 */
public class DomainSortParameter {

    // The SortParameter we are wrapping
    private final SortParameter sortParameter;

    /**
     * Public constructor
     * @param sp
     */
    public DomainSortParameter(SortParameter sp) {
        this.sortParameter = sp;
    }

    /**
     * Get the sort parameter.
     * @return sort parameter
     */
    public SortParameter getSortParameter() {
        return this.sortParameter;
    }

    /**
     * Visitor to apply the sort parameter to the query builder represented by the visitor
     * @param queryData
     * @param visitor
     */
    public <T> void visit(T queryData, SearchQueryVisitor<T> visitor) throws FHIRPersistenceException {
        visitor.addSortParam(queryData, sortParameter.getCode(), sortParameter.getType(), sortParameter.getDirection());
    }
}