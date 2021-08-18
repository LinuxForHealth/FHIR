/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.domain;

import java.util.List;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.search.SearchConstants.Type;
import com.ibm.fhir.search.parameters.InclusionParameter;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.sort.Sort.Direction;

/**
 * Used by the {@link SearchQuery} domain model to render the model
 * into another form (such as a Select statement).
 */
public interface SearchQueryVisitor<T> {

    /**
     * The root query (select statement) for a count query
     * @param rootResourceType
     * @return
     */
    T countRoot(String rootResourceType);

    /**
     * The root query (select statement) for the data query
     * @param rootResourceType
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
     * Get the join to which we want to attach all the parameter tables.
     * This makes it easier to build different styles of joins
     * @param queryData
     * @return
     */
    T getParameterBaseQuery(T queryData);

    /**
     * The root query (select statement) for the include query. This query is different
     * than the data root because of the need to support version references for _include
     * searches. For this, we join:
     *   xx_RESOURCES.LOGICAL_RESOURCE_ID = xx_LOGICAL_RESOURCES.LOGICAL_RESOURCE_ID
     * and allow the filter to specify the version_id constraint. However, we still need
     * to assert that the resource has not been deleted, so we keep:
     *   xx_LOGICAL_RESOURCES.IS_DELETED = 'N'
     * @param rootResourceType
     * @return
     */
    T includeRoot(String rootResourceType);

    /**
     * @param query
     * @return
     */
    T wrapInclude(T query);

    /**
     * The root of the FHIR search sort query
     * @param rootResourceType
     * @return
     */
    T sortRoot(String rootResourceType);

    /**
     * The root of the FHIR whole-system filter search query
     * @return
     */
    T wholeSystemFilterRoot();

    /**
     * The root of the FHIR whole-system data search query
     * @param rootResourceType
     * @return
     */
    T wholeSystemDataRoot(String rootResourceType);

    /**
     * The wrapper for whole-system search
     * @param queries
     * @param isCountQuery
     * @return
     */
    T wrapWholeSystem(List<T> queries, boolean isCountQuery);
        
    /**
     * Filter the query using the given parameter id and token value
     * @param query
     * @param resourceType
     * @param queryParm
     * @return
     * @throws FHIRPersistenceException
     */
    T addTokenParam(T query, String resourceType, QueryParameter queryParm) throws FHIRPersistenceException;

    /**
     * Filter the query using the given tag query parameter
     * @param query
     * @param resourceType
     * @param queryParm
     * @return
     * @throws FHIRPersistenceException
     */
    T addTagParam(T query, String resourceType, QueryParameter queryParm) throws FHIRPersistenceException;

    /**
     * Filter the query using the given security query parameter
     * @param query
     * @param resourceType
     * @param queryParm
     * @return
     * @throws FHIRPersistenceException
     */
    T addSecurityParam(T query, String resourceType, QueryParameter queryParm) throws FHIRPersistenceException;

    /**
     * Filter the query using the given string parameter
     * @param query
     * @param resourceType
     * @param queryParm
     * @return
     */
    T addStringParam(T query, String resourceType, QueryParameter queryParm) throws FHIRPersistenceException;

    /**
     * Filter the query using the given canonical parameter
     * @param query
     * @param resourceType
     * @param queryParm
     * @return
     */
    T addCanonicalParam(T query, String resourceType, QueryParameter queryParm) throws FHIRPersistenceException;

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
     * @param queryParm
     * @param isMissing true if the condition should be that the parameter does not exist
     * @return
     */
    T addMissingParam(T query, QueryParameter queryParm, boolean isMissing) throws FHIRPersistenceException;

    /**
     * Add a composite query parameter filter to the query
     * @param query
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
     * @param lrAlias
     * @return
     */
    T addSorting(T query, String lrAlias);

    /**
     * Add sorting (order by) for whole-system search to the query
     * @param query
     * @param sortParms
     * @param lrAlias
     * @return
     */
    T addWholeSystemSorting(T query, List<DomainSortParameter> sortParms, String lrAlias);

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
     * @return
     */
    T addChained(T currentSubQuery, QueryParameter currentParm) throws FHIRPersistenceException;

    /**
     * Add a reverse chain subquery element as part of a chained parameter search
     * @param currentSubQuery
     * @param currentParm
     * @return
     */
    T addReverseChained(T currentSubQuery, QueryParameter currentParm) throws FHIRPersistenceException;

    /**
     * Add a filter predicate to the given chained sub-query element. This must be
     * the last element of the chain.
     * @param currentSubQuery
     * @param resourceType
     * @param currentParm
     */
    void addFilter(T currentSubQuery, String resourceType, QueryParameter currentParm) throws FHIRPersistenceException;

    /**
     * @param queryData
     * @param queryParameters
     * @return
     */
    T addLocationPosition(T queryData, List<QueryParameter> queryParameters) throws FHIRPersistenceException;

    /**
     * @param query
     * @param inclusionParm
     * @param logicalResourceIds
     * @return
     */
    T addIncludeFilter(T query, InclusionParameter inclusionParm, List<Long> logicalResourceIds) throws FHIRPersistenceException;

    /**
     * @param query
     * @param inclusionParm
     * @param logicalResourceIds
     * @return
     */
    T addRevIncludeFilter(T query, InclusionParameter inclusionParm, List<Long> logicalResourceIds) throws FHIRPersistenceException;

    /**
     * @param query
     * @param resourceType
     * @param logicalResourceIds
     * @return
     */
    T addWholeSystemDataFilter(T query, String resourceType, List<Long> logicalResourceIds) throws FHIRPersistenceException;

    /**
     * @param query
     * @param resourceTypeIds
     * @return
     */
    T addWholeSystemResourceTypeFilter(T query, List<Integer> resourceTypeIds) throws FHIRPersistenceException;

    /**
     * Add the given sort parameter to the sort query
     * @param queryData
     * @param code
     * @param type
     * @param direction
     */
    void addSortParam(T queryData, String code, Type type, Direction direction) throws FHIRPersistenceException;
}