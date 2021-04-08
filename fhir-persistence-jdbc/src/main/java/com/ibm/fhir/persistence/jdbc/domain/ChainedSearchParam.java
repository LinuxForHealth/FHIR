/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.domain;

import com.ibm.fhir.search.parameters.QueryParameter;

/**
 * A chained search parameter
 */
public class ChainedSearchParam extends SearchParam {

    /**
     * Public constructor
     * @param name
     */
    public ChainedSearchParam(String rootResourceType, String name, QueryParameter queryParameter) {
        super(rootResourceType, name, queryParameter);
    }

    @Override
    public <T> T visit(T query, SearchQueryVisitor<T> visitor) {
        // return visitor.addTokenParam(query, resourceType, parameterNameId, commonTokenValueId);
        return null;
    }
}