/*
 * (C) Copyright IBM Corp. 2018, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util;

import static com.ibm.fhir.persistence.jdbc.JDBCConstants.AND;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.DEFAULT_ORDERING;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.FROM;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.JOIN;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LEFT_PAREN;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.ON;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.RIGHT_PAREN;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.WHERE;

import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Logger;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.connection.QueryHints;
import com.ibm.fhir.persistence.jdbc.dao.api.JDBCIdentityCache;
import com.ibm.fhir.persistence.jdbc.dao.api.ParameterDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.ResourceDAO;
import com.ibm.fhir.search.SearchConstants;
import com.ibm.fhir.search.parameters.InclusionParameter;

/**
 * This class assists the JDBCQueryBuilder. It extends the
 * QuerySegmentAggregator to build a FHIR Resource query
 * that processes _include and _revinclude search result parameters. Using the
 * query segments built by the query builder,
 * this class augments those query segments to result in a query that includes
 * other resources as determined by
 * InclusionParameter objects.
 */
@Deprecated
public class InclusionQuerySegmentAggregator extends QuerySegmentAggregator {
    private static final String CLASSNAME = InclusionQuerySegmentAggregator.class.getName();
    private static final Logger log = java.util.logging.Logger.getLogger(CLASSNAME);

    private static final String SELECT_ROOT =
            "SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID, R.LAST_UPDATED, R.IS_DELETED, R.DATA, R1.LOGICAL_ID";

    private final JDBCIdentityCache identityCache;

    protected InclusionQuerySegmentAggregator(Class<?> resourceType, int offset, int pageSize,
            ParameterDAO parameterDao, ResourceDAO resourceDao, QueryHints queryHints, JDBCIdentityCache identityCache) {
        super(resourceType, offset, pageSize, parameterDao, resourceDao, queryHints);
        this.identityCache = identityCache;
    }

    public SqlQueryData buildIncludeQuery(InclusionParameter inclusionParm, Set<String> ids, String inclusionType) throws Exception {
        final String METHODNAME = "buildIncludeQuery";
        log.entering(CLASSNAME, METHODNAME);

        SqlQueryData queryData;

        // @formatter:off
        //
        // SELECT
        //   R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID, R.LAST_UPDATED, R.IS_DELETED, R.DATA, R1.LOGICAL_ID
        // FROM
        //   <resourceType>_RESOURCES R
        //
        // @formatter:on
        StringBuilder queryString = new StringBuilder(SELECT_ROOT)
                .append(FROM)
                .append(SearchConstants.INCLUDE.equals(inclusionType) ?
                        inclusionParm.getSearchParameterTargetType() :
                        inclusionParm.getJoinResourceType())
                .append("_RESOURCES R");

        if (SearchConstants.INCLUDE.equals(inclusionType)) {
            processIncludeJoins(queryString, inclusionParm, ids);
        } else {
            processRevIncludeJoins(queryString, inclusionParm, ids);
        }

        // @formatter:off
        //
        // ORDER BY
        //   LOGICAL_RESOURCE_ID ASC OFFSET 0 ROWS FETCH NEXT 1001 ROWS ONLY
        //
        // @formatter:on
        queryString.append(DEFAULT_ORDERING);
        addPaginationClauses(queryString);
        addOptimizerHint(queryString);

        queryData = new SqlQueryData(queryString.toString(), new ArrayList<>());

        log.exiting(CLASSNAME, METHODNAME, queryData);
        return queryData;
    }

    /**
     * Formats the JOIN clauses for an include query.
     *
     * <pre>
     * JOIN (
     *   SELECT
     *     DISTINCT R.RESOURCE_ID, LR.LOGICAL_ID
     *   FROM
     *     <joinResourceType>_TOKEN_VALUES_V P1
     *     JOIN <targetResourceType>_LOGICAL_RESOURCES LR
     *       ON P1.TOKEN_VALUE = LR.LOGICAL_ID
     *     JOIN <targetResourceType>_RESOURCES R
     *       ON LR.LOGICAL_RESOURCE_ID = R.LOGICAL_RESOURCE_ID
     *      AND COALESCE(P1.REF_VERSION_ID, LR.VERSION_ID) = R.VERSION_ID
     *      AND R.IS_DELETED = 'N'
     *   WHERE
     *     P1.PARAMETER_NAME_ID = {n}
     *     AND P1.CODE_SYSTEM_ID = {n}
     *     AND P1.LOGICAL_RESOURCE_ID IN (<list-of-logical-resource_ids>)
     * ) AS R1 ON R.RESOURCE_ID = R1.RESOURCE_ID AND R.IS_DELETED = 'N'
     * </pre>
     *
     * @param queryString
     *              The non-null StringBuilder
     * @param inclusionParm
     *              The inclusion parameter being processed
     * @param ids
     *              The list of logical resource IDs
     * @throws FHIRPersistenceException
     */
    protected void processIncludeJoins(StringBuilder queryString, InclusionParameter inclusionParm, Set<String> ids)
            throws FHIRPersistenceException {
        queryString.append(JOIN).append(LEFT_PAREN)
            .append("SELECT DISTINCT R.RESOURCE_ID, LR.LOGICAL_ID")
            .append(FROM)
            .append(inclusionParm.getJoinResourceType()).append("_TOKEN_VALUES_V P1")
            .append(JOIN)
            .append(inclusionParm.getSearchParameterTargetType()).append("_LOGICAL_RESOURCES LR")
            .append(ON)
            .append("P1.TOKEN_VALUE = LR.LOGICAL_ID")
            .append(JOIN)
            .append(inclusionParm.getSearchParameterTargetType()).append("_RESOURCES R")
            .append(ON)
            .append("LR.LOGICAL_RESOURCE_ID = R.LOGICAL_RESOURCE_ID")
            .append(AND)
            .append("COALESCE(P1.REF_VERSION_ID, LR.VERSION_ID) = R.VERSION_ID")
            .append(AND)
            .append("R.IS_DELETED = 'N'")
            .append(WHERE)
            .append("P1.PARAMETER_NAME_ID=").append(this.getParameterNameId(inclusionParm.getSearchParameter()))
            .append(AND)
            .append("P1.CODE_SYSTEM_ID=").append(getCodeSystemId(inclusionParm.getSearchParameterTargetType()))
            .append(AND)
            .append("P1.LOGICAL_RESOURCE_ID IN (").append(String.join(",",ids)).append(RIGHT_PAREN)
            .append(") AS R1 ON R.RESOURCE_ID = R1.RESOURCE_ID AND R.IS_DELETED = 'N'");
    }

    /**
     * Formats the JOIN clauses for a revinclude query.
     *
     * <pre>
     * JOIN (
     *   SELECT
     *     DISTINCT LR.CURRENT_RESOURCE_ID, LR.LOGICAL_ID
     *   FROM
     *     (
     *       SELECT
     *         LOGICAL_ID, VERSION_ID
     *       FROM
     *         <targetResourceType>_LOGICAL_RESOURCES LR
     *       WHERE
     *         LR.LOGICAL_RESOURCE_ID IN (<list-of-logical-resource_ids>)
     *     ) REFS
     *     JOIN <joinResourceType>_TOKEN_VALUES_V P1
     *       ON REFS.LOGICAL_ID = P1.TOKEN_VALUE
     *      AND COALESCE(P1.REF_VERSION_ID, REFS.VERSION_ID) = REFS.VERSION_ID
     *      AND P1.PARAMETER_NAME_ID = {n}
     *      AND P1.CODE_SYSTEM_ID = {n}
     *     JOIN <targetResourceType>_LOGICAL_RESOURCES LR
     *       ON P1.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID
     *      AND LR.IS_DELETED = 'N'
     * ) AS R1 ON R.RESOURCE_ID = R1.CURRENT_RESOURCE_ID
     * </pre>
     *
     * @param queryString
     *              The non-null StringBuilder
     * @param inclusionParm
     *              The inclusion parameter being processed
     * @param ids
     *              The list of logical resource IDs
     * @throws FHIRPersistenceException
     */
    protected void processRevIncludeJoins(StringBuilder queryString, InclusionParameter inclusionParm, Set<String> ids)
            throws FHIRPersistenceException {
        queryString.append(JOIN).append(LEFT_PAREN)
            .append("SELECT DISTINCT LR.CURRENT_RESOURCE_ID, LR.LOGICAL_ID")
            .append(FROM)
            .append(LEFT_PAREN).append("SELECT LOGICAL_ID, VERSION_ID")
            .append(FROM)
            .append(inclusionParm.getSearchParameterTargetType()).append("_LOGICAL_RESOURCES LR")
            .append(WHERE)
            .append("LR.LOGICAL_RESOURCE_ID IN (").append(String.join(",",ids)).append(RIGHT_PAREN)
            .append(RIGHT_PAREN).append(" REFS")
            .append(JOIN)
            .append(inclusionParm.getJoinResourceType()).append("_TOKEN_VALUES_V P1")
            .append(ON)
            .append("REFS.LOGICAL_ID = P1.TOKEN_VALUE")
            .append(AND)
            .append("COALESCE(P1.REF_VERSION_ID, REFS.VERSION_ID) = REFS.VERSION_ID")
            .append(AND)
            .append("P1.PARAMETER_NAME_ID=").append(this.getParameterNameId(inclusionParm.getSearchParameter()))
            .append(AND)
            .append("P1.CODE_SYSTEM_ID=").append(getCodeSystemId(inclusionParm.getSearchParameterTargetType()))
            .append(JOIN)
            .append(inclusionParm.getJoinResourceType()).append("_LOGICAL_RESOURCES LR")
            .append(ON)
            .append("P1.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID")
            .append(AND)
            .append("LR.IS_DELETED = 'N'")
            .append(") AS R1 ON R.RESOURCE_ID = R1.CURRENT_RESOURCE_ID");
    }

    /**
     * Returns the integer id that corresponds to the passed search parameter name.
     *
     * @param searchParameterName
     * @return Integer
     * @throws FHIRPersistenceException
     */
    private int getParameterNameId(String searchParameterName) throws FHIRPersistenceException {
        final String METHODNAME = "getParameterNameId";
        log.entering(CLASSNAME, METHODNAME);

        Integer parameterNameId = identityCache.getParameterNameId(searchParameterName);
        if (parameterNameId == null) {
            parameterNameId = -1; // need a value to keep query syntax valid
        }

        log.exiting(CLASSNAME, METHODNAME);
        return parameterNameId;
    }

    private int getCodeSystemId(String codeSystemName) throws FHIRPersistenceException {
        Integer result = identityCache.getCodeSystemId(codeSystemName);
        if (result == null) {
            result = -1;
        }
        return result;
    }

}