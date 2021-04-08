/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.domain;

import com.ibm.fhir.database.utils.query.node.ExpNode;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * Used by the {@link SearchQuery} domain model to render the model
 * into another form (such as a Select statement.
 */
public interface SearchQueryVisitor<T> {

    /**
     * The root query (select statement) for a count query
     * @param rootResourceType
     * @param columns
     * @return
     */
    T countRoot(String rootResourceType);

    /**
     * The root query (select statement) for the data query
     * @param rootResourceType
     * @param columns
     * @return
     */
    T dataRoot(String rootResourceType);

    /**
     * Filter the query using the given parameter id and token value
     * @param query
     * @param parameterNameId
     * @param parameterValue
     * @return
     */
    T addTokenParam(T query, String resourceType, int parameterNameId, Long commonTokenValueId) throws FHIRPersistenceException;

    /**
     * Filter the query using the given parameter id and string value
     * @param query
     * @param parameterNameId
     * @param strValue
     * @return
     */
    T addStringParam(T query, String resourceType, String parameterName, ExpNode filter) throws FHIRPersistenceException;

    /**
     * Add sorting (order by) to the query
     * @param query
     * @return
     */
    T addSorting(T query);

    /**
     * Add pagination (LIMIT/OFFSET) to the query
     * @param query
     * @return
     */
    T addPagination(T query);
}