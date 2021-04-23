/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.domain;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.search.parameters.QueryParameter;

/**
 * A string search parameter
 */
public class CompositeSearchParam extends SearchParam {
    // private static final Logger logger = Logger.getLogger(StringSearchParam.class.getName());

    /**
     * Public constructor
     * @param name
     * @param queryParameter the search query parameter being wrapped
     */
    public CompositeSearchParam(String rootResourceType, String name, QueryParameter queryParameter) {
        super(rootResourceType, name, queryParameter);
    }

    @Override
    public <T> T visit(T queryData, SearchQueryVisitor<T> visitor) throws FHIRPersistenceException {
        return visitor.addCompositeParam(queryData, getQueryParameter());
    }
}
