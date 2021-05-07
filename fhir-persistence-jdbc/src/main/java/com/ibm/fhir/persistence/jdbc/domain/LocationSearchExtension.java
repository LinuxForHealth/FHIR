/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.domain;

import java.util.List;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.search.parameters.QueryParameter;

/**
 * A search extension for the NEAR location logic
 */
public class LocationSearchExtension implements SearchExtension {

    // The list of query parameters to process for location search
    private final List<QueryParameter> queryParameters;

    /**
     * Public constructor
     * @param queryParameters
     */
    public LocationSearchExtension(List<QueryParameter> queryParameters) {
        // No need to copy this...we trust the list won't be changing
        this.queryParameters = queryParameters;
    }

    @Override
    public <T> T visit(T queryData, SearchQueryVisitor<T> visitor) throws FHIRPersistenceException {
        // ask the visitor to apply the near location position logic to the query
        return visitor.addLocationPosition(queryData, queryParameters);
    }
}