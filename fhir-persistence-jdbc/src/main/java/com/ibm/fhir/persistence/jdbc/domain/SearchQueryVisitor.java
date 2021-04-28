/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.domain;

import java.util.List;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.search.parameters.InclusionParameter;
import com.ibm.fhir.search.parameters.QueryParameter;

/**
 * Used by the {@link SearchQuery} domain model to render the model
 * into another form (such as a Select statement.
 *
 * TODO. Might be useful to change the structure slightly and have
 * various methods return a statement (e.g. the exists clause) which
 * can then be passed to another method for aggregation into the
 * larger query. This may be useful in promoting reuse, where the
 * same subquery structure is used in more than one context.
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
     * Finish the data query by wrapping the root and joining the resources
     * table
     * @param queryData
     * @return
     */
    T joinResources(T queryData);

    /**
     * The root query (select statement) for the include query. This query is different
     * than the data root because of the need to support version references for _include
     * searches. For this, we join:
     *   xx_RESOURCES.LOGICAL_RESOURCE_ID = xx_LOGICAL_RESOURCES.LOGICAL_RESOURCE_ID
     * and allow the filter to specify the version_id constraint. However, we still need
     * to assert that the resource has not been deleted, so we keep:
     *   xx_LOGICAL_RESOURCES.IS_DELETED = 'N'
     * @param rootResourceType
     * @param columns
     * @return
     */
    T includeRoot(String rootResourceType);

    /**
     * Filter the query using the given parameter id and token value
     * @param query
     * @param parameterNameId
     * @param parameterValue
     * @return
     */
    T addTokenParam(T query, String resourceType, QueryParameter queryParm) throws FHIRPersistenceException;

    /**
     * Filter the query using the given string parameter
     * @param query
     * @param parameterNameId
     * @param strValue
     * @return
     */
    T addStringParam(T query, String resourceType, QueryParameter queryParm) throws FHIRPersistenceException;

    /**
     * Filter the query using the given number parameter
     * @param queryData
     * @param resourceType
     * @param queryParm
     * @return
     * @throws FHIRPersistenceException
     */
    public T addNumberParam(T queryData, String resourceType, QueryParameter queryParm) throws FHIRPersistenceException;

    /**
     * Filter the query using the given quantity parameter
     * @param queryData
     * @param resourceType
     * @param queryParm
     * @return
     * @throws FHIRPersistenceException
     */
    public T addQuantityParam(T queryData, String resourceType, QueryParameter queryParm) throws FHIRPersistenceException;

    /**
     * Filter the query using the given date parameter
     * @param queryData
     * @param resourceType
     * @param queryParm
     * @return
     * @throws FHIRPersistenceException
     */
    public T addDateParam(T queryData, String resourceType, QueryParameter queryParm) throws FHIRPersistenceException;

    /**
     * Filter the query using the given reference parameter
     * @param queryData
     * @param resourceType
     * @param queryParm
     * @return
     * @throws FHIRPersistenceException
     */
    public T addReferenceParam(T queryData, String resourceType, QueryParameter queryParm) throws FHIRPersistenceException;

    /**
     * Filter the query using the given location (lat/lng) param
     * @param queryData
     * @param resourceType
     * @param queryParm
     * @return
     * @throws FHIRPersistenceException
     */
    public T addLocationParam(T queryData, String resourceType, QueryParameter queryParm) throws FHIRPersistenceException;

    /**
     * Add a missing (NOT EXISTS) parameter clause to the query
     * @param query
     * @param rootResourceType
     * @param code
     * @param isMissing true if the condition should be that the parameter does not exist
     * @return
     */
    T addMissingParam(T query, QueryParameter queryParm, boolean isMissing) throws FHIRPersistenceException;

    /**
     * Add a composite query parameter filter to the query
     * @param query
     * @param resourceType
     * @param queryParm
     * @return
     * @throws FHIRPersistenceException
     */
    T addCompositeParam(T query, QueryParameter queryParm) throws FHIRPersistenceException;

    /**
     * Add a composite query which only tests missing/not missing, not the
     * actual parameter value
     * @param query
     * @param queryParm
     * @param isMissing
     * @return
     * @throws FHIRPersistenceException
     */
    T addCompositeParam(T query, QueryParameter queryParm, boolean isMissing) throws FHIRPersistenceException;

    /**
     * Special case to handle inclusion related to compartment-based searches
     * @param query
     * @param resourceType
     * @param queryParm
     * @return
     * @throws FHIRPersistenceException
     */
    T addInclusionParam(T query, String resourceType, QueryParameter queryParm) throws FHIRPersistenceException;

    /**
     * Add sorting (order by) to the query
     * @param query
     * @return
     */
    T addSorting(T query, String lrAlias);

    /**
     * Add pagination (LIMIT/OFFSET) to the query
     * @param query
     * @return
     */
    T addPagination(T query);

    /**
     * Add a chain subquery element as part of a chained parameter search
     * @param currentSubQuery
     * @param currentParm
     * @param aliasIndex
     * @return
     */
    T addChained(T currentSubQuery, QueryParameter currentParm) throws FHIRPersistenceException;

    /**
     * Add a reverse chain subquery element as part of a chained parameter search
     * @param currentSubQuery
     * @param currentParm
     * @param aliasIndex
     * @return
     */
    T addReverseChained(T currentSubQuery, QueryParameter currentParm) throws FHIRPersistenceException;

    /**
     * Add a filter predicate to the given chained sub-query element. This must be
     * the last element of the chain.
     * @param currentSubQuery
     * @param currentParm
     * @param aliasIndex
     */
    void addFilter(T currentSubQuery, QueryParameter currentParm) throws FHIRPersistenceException;

    /**
     * @param queryData
     * @param queryParameters
     * @return
     */
    T addLocationPosition(T queryData, List<QueryParameter> queryParameters) throws FHIRPersistenceException;

    /**
     * @param query
     * @param inclusionParm
     * @param ids
     * @return
     */
    T addIncludeFilter(T query, InclusionParameter inclusionParm, List<Long> logicalResourceids) throws FHIRPersistenceException;

    /**
     * @param query
     * @param inclusionParm
     * @param ids
     * @return
     */
    T addRevIncludeFilter(T query, InclusionParameter inclusionParm, List<Long> logicalResourceIds) throws FHIRPersistenceException;
}