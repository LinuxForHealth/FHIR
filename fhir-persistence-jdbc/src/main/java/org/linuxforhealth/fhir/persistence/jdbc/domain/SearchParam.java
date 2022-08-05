/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.domain;

import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.search.parameters.QueryParameter;

/**
 * Represents a search parameter in the search domain model
 */
public abstract class SearchParam {
    private final String rootResourceType;

    private final String name;

    private final QueryParameter queryParameter;

    protected SearchParam(String rootResourceType, String name, QueryParameter queryParameter) {
        this.rootResourceType = rootResourceType;
        this.name = name;
        this.queryParameter = queryParameter;
    }

    /**
     * Getter for the parameter's name
     * @return
     */
    protected String getName() {
        return this.name;
    }

    /**
     * Getter for the root resource type of the query
     * @return
     */
    protected String getRootResourceType() {
        return this.rootResourceType;
    }

    /**
     * Getter for the {@link QueryParameter} we've wrapped
     * @return
     */
    protected QueryParameter getQueryParameter() {
        return this.queryParameter;
    }

    public abstract <T> T visit(T query, SearchQueryVisitor<T> visitor) throws FHIRPersistenceException;

}