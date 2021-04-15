/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.domain;

import com.ibm.fhir.database.utils.query.SelectAdapter;

/**
 * A simple wrapper used by the {@link SearchQueryRenderer} hold the current
 * query - {@link SelectAdapter} - and the parameterAlias which needs to be
 * incremented each time we add another parameter table or sub-query.
 * Immutable.
 */
public class QueryData {
    // The query being wrapped
    private final SelectAdapter query;

    // The current parameterAlias.
    private final int parameterAlias;

    // The resource type associated with this part of the query
    private final String resourceType;

    /**
     * Public constructor
     * @param query
     * @param parameterAlias
     */
    public QueryData(SelectAdapter query, int parameterAlias, String resourceType) {
        this.query = query;
        this.parameterAlias = parameterAlias;
        this.resourceType = resourceType;
    }

    /**
     * @return the query
     */
    public SelectAdapter getQuery() {
        return query;
    }

    /**
     * @return the parameterAlias
     */
    public int getParameterAlias() {
        return parameterAlias;
    }

    /**
     * @return the resourceType
     */
    public String getResourceType() {
        return resourceType;
    }
}
