/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.domain;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.search.parameters.QueryParameter;

/**
 * A token search parameter
 */
public class TokenSearchParam extends SearchParam {

    /**
     * Public constructor
     * @param rootResourceName
     * @param name
     * @param queryParameter
     */
    public TokenSearchParam(String rootResourceName, String name, QueryParameter queryParameter) {
        super(rootResourceName, name, queryParameter);
    }

    @Override
    public <T> T visit(T queryData, SearchQueryVisitor<T> visitor) throws FHIRPersistenceException {
        QueryParameter queryParm = getQueryParameter();
        return visitor.addTokenParam(queryData, getRootResourceType(), queryParm);
    }
}