/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util;

import static com.ibm.fhir.persistence.jdbc.JDBCConstants.AND;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.AS;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.BIND_VAR;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.CODE_SYSTEM_ID;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.COMMA;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.COMMON_TOKEN_VALUE_ID;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.CURRENT_RESOURCE_ID;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.DOT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.EQ;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.ESCAPE_EXPR;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.ESCAPE_PERCENT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.ESCAPE_UNDERSCORE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.EXISTS;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.FROM;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.IN;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.IS_DELETED_NO;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.JOIN;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LEFT_PAREN;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LIKE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LOGICAL_ID;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LOGICAL_RESOURCE_ID;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.NE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.NOT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.ON;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.OR;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.PARAMETER_NAME_ID;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.PARAMETER_TABLE_ALIAS;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.PARAMETER_TABLE_NAME_PLACEHOLDER;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.PERCENT_WILDCARD;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.RESOURCE_ID;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.RIGHT_PAREN;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.SELECT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.SPACE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.STR_VALUE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.STR_VALUE_LCASE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.TOKEN_VALUE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.UNDERSCORE_WILDCARD;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.WHERE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants._LOGICAL_RESOURCES;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants._RESOURCES;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.modifierOperatorMap;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.model.resource.Location;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceNotSupportedException;
import com.ibm.fhir.persistence.jdbc.connection.QueryHints;
import com.ibm.fhir.persistence.jdbc.dao.api.JDBCIdentityCache;
import com.ibm.fhir.persistence.jdbc.dao.api.ParameterDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.ResourceDAO;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;
import com.ibm.fhir.persistence.jdbc.util.type.DateParmBehaviorUtil;
import com.ibm.fhir.persistence.jdbc.util.type.LastUpdatedParmBehaviorUtil;
import com.ibm.fhir.persistence.jdbc.util.type.LocationParmBehaviorUtil;
import com.ibm.fhir.persistence.jdbc.util.type.NumberParmBehaviorUtil;
import com.ibm.fhir.persistence.jdbc.util.type.QuantityParmBehaviorUtil;
import com.ibm.fhir.persistence.util.AbstractQueryBuilder;
import com.ibm.fhir.search.SearchConstants;
import com.ibm.fhir.search.SearchConstants.Modifier;
import com.ibm.fhir.search.SearchConstants.Type;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.location.NearLocationHandler;
import com.ibm.fhir.search.location.bounding.Bounding;
import com.ibm.fhir.search.location.util.LocationUtil;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.parameters.QueryParameterValue;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * This is the JDBC implementation of a query builder for the IBM FHIR Server
 * JDBC persistence layer schema.
 * Queries are built in SQL.
 * <br>
 * <br>
 * For the new R4 schema, the search parameter tables (e.g.
 * {@code <resourceType>_STR_VALUES}) are
 * joined to their corresponding <resourceType>_LOGICAL_RESOURCES tables on
 * LOGICAL_RESOURCE_ID.
 * This is because the search parameters are not versioned, and are associated
 * with
 * the logical resource, not the resource version.
 * <br>
 * Useful column reference:<br>
 *
 * <pre>
 * ------------------------
 * RESOURCE_TYPE_NAME    the formal name of the resource type e.g. 'Patient'
 * RESOURCE_TYPE_ID      FK to the RESOURCE_TYPES table
 * LOGICAL_ID            the VARCHAR holding the logical-id of the resource. Unique for a given resource type
 * LOGICAL_RESOURCE_ID   the database BIGINT
 * CURRENT_RESOURCE_ID   the unique BIGINT id of the latest resource version for the logical resource
 * VERSION_ID            INT resource version number incrementing by 1
 * RESOURCE_ID           the PK of the version-specific resource. Now only used as the target for CURRENT_RESOURCE_ID
 * </pre>
 */
public class JDBCQueryBuilder extends AbstractQueryBuilder<SqlQueryData> {
    private static final Logger log = java.util.logging.Logger.getLogger(JDBCQueryBuilder.class.getName());
    private static final String CLASSNAME = JDBCQueryBuilder.class.getName();

    private final ParameterDAO parameterDao;
    private final ResourceDAO resourceDao;

    // For id lookups
    private final JDBCIdentityCache identityCache;

    // Hints to use for certain queries
    private final QueryHints queryHints;

    // Table aliases
    private static final String LR = "LR";

    // Table alias prefixes
    private static final String CR = "CR";
    private static final String CLR = "CLR";
    private static final String CP = "CP";

    /**
     * Public constructor
     * @param parameterDao
     * @param resourceDao
     * @param queryHints
     * @param identityCache
     */
    public JDBCQueryBuilder(ParameterDAO parameterDao, ResourceDAO resourceDao, QueryHints queryHints, JDBCIdentityCache identityCache) {
        this.parameterDao = parameterDao;
        this.resourceDao  = resourceDao;
        this.queryHints = queryHints;
        this.identityCache = identityCache;
    }

    /**
     * Builds a query that returns the count of the search results that would be
     * found by applying the search parameters
     * contained within the passed search context.
     *
     * @param resourceType
     *                      - The type of resource being searched for.
     * @param searchContext
     *                      - The search context containing the search parameters.
     * @return String - A count query SQL string
     * @throws Exception
     */
    public SqlQueryData buildCountQuery(Class<?> resourceType, FHIRSearchContext searchContext) throws Exception {
        final String METHODNAME = "buildCountQuery";
        log.entering(CLASSNAME, METHODNAME,
                new Object[] { resourceType.getSimpleName(), searchContext.getSearchParameters() });

        QuerySegmentAggregator helper;
        SqlQueryData query = null;

        helper = this.buildQueryCommon(resourceType, searchContext);
        if (helper != null) {
            query = helper.buildCountQuery();
        }

        log.exiting(CLASSNAME, METHODNAME);
        return query;
    }

    @Override
    public SqlQueryData buildQuery(Class<?> resourceType, FHIRSearchContext searchContext) throws Exception {
        final String METHODNAME = "buildQuery";
        log.entering(CLASSNAME, METHODNAME,
                new Object[] { resourceType.getSimpleName(), searchContext.getSearchParameters() });

        SqlQueryData query = null;
        QuerySegmentAggregator helper;

        helper = this.buildQueryCommon(resourceType, searchContext);
        if (helper != null) {
            query = helper.buildQuery();
        }

        log.exiting(CLASSNAME, METHODNAME);
        return query;
    }

    /**
     * Contains logic common to the building of both 'count' resource queries and
     * 'regular' resource queries.
     *
     * @param resourceType
     *                      The type of FHIR resource being searched for.
     * @param searchContext
     *                      The search context containing search parameters.
     * @return QuerySegmentAggregator - A query builder helper containing processed
     *         query segments.
     * @throws Exception
     */
    private QuerySegmentAggregator buildQueryCommon(Class<?> resourceType, FHIRSearchContext searchContext)
            throws Exception {
        final String METHODNAME = "buildQueryCommon";
        log.entering(CLASSNAME, METHODNAME,
                new Object[] { resourceType.getSimpleName(), searchContext.getSearchParameters() });

        SqlQueryData querySegment;
        int nearParameterIndex;
        List<QueryParameter> searchParameters = searchContext.getSearchParameters();

        // Forces _id and _lastUpdated to come before all other parameters, which is good for this bit here
        // zero is used to for all other cases.
        searchParameters.sort(new Comparator<QueryParameter>() {
            @Override
            public int compare(QueryParameter leftParameter, QueryParameter rightParameter) {
                int result = 0;
                if (QuerySegmentAggregator.ID.equals(leftParameter.getCode())) {
                    result = -100;
                } else if (LastUpdatedParmBehaviorUtil.LAST_UPDATED.equals(leftParameter.getCode())) {
                    result = -90;
                }
                return result;
            }

        });

        int pageSize = searchContext.getPageSize();
        int offset = (searchContext.getPageNumber() - 1) * pageSize;
        QuerySegmentAggregator helper;
        boolean isValidQuery = true;

        helper =
                QuerySegmentAggregatorFactory.buildQuerySegmentAggregator(resourceType, offset, pageSize,
                        this.parameterDao, this.resourceDao, searchContext, this.queryHints, this.identityCache);

        // Special logic for handling LocationPosition queries. These queries have interdependencies between
        // a couple of related input query parameters
        if (Location.class.equals(resourceType)) {
            querySegment = this.processLocationPosition(searchParameters, PARAMETER_TABLE_ALIAS);
            if (querySegment != null) {
                nearParameterIndex = LocationUtil.findNearParameterIndex(searchParameters);
                helper.addQueryData(querySegment, searchParameters.get(nearParameterIndex));
            }
            // If there are Location-position parameters but a querySegment was not built,
            // the query would be invalid. Note that valid parameters could be found in the following
            // for loop.
            else if (!searchParameters.isEmpty()) {
                isValidQuery = false;
            }
        }

        // For each search parm, build a query parm that will satisfy the search.
        for (QueryParameter queryParameter : searchParameters) {
            querySegment = this.buildQueryParm(resourceType, queryParameter, PARAMETER_TABLE_ALIAS, LR, false);
            if (querySegment != null) {
                helper.addQueryData(querySegment, queryParameter);
                isValidQuery = true;
            }
        }
        if (!isValidQuery) {
            helper = null;
        }
        log.exiting(CLASSNAME, METHODNAME);
        return helper;

    }

    protected String getOperator(QueryParameter queryParm) {
        final String METHODNAME = "getOperator(QueryParameter)";
        log.entering(CLASSNAME, METHODNAME, queryParm.getModifier());

        String operator = LIKE;
        Modifier modifier = queryParm.getModifier();

        // In the case where a URI, we need specific behavior/manipulation
        // so that URI defaults to EQ, unless... BELOW
        if (Type.URI.equals(queryParm.getType())) {
            if (modifier != null && Modifier.BELOW.equals(modifier)) {
                operator = LIKE;
            } else {
                operator = EQ;
            }
        } else if (modifier != null) {
            operator = modifierOperatorMap.get(modifier);
        }

        if (operator == null) {
            operator = LIKE;
        }

        log.exiting(CLASSNAME, METHODNAME, operator);
        return operator;
    }

    /**
     * Map the Modifier in the passed Parameter to a supported query operator. If
     * the mapping results in the default
     * operator, override the default operator with the passed operator if the
     * passed operator is not null.
     *
     * @param queryParm
     *                        - A valid query Parameter.
     * @param defaultOverride
     *                        - An operator that should override the default
     *                        operator.
     * @return A supported operator.
     */
    protected String getOperator(QueryParameter queryParm, String defaultOverride) {
        final String METHODNAME = "getOperator(Parameter, String)";
        log.entering(CLASSNAME, METHODNAME, queryParm.getModifier());

        String operator = defaultOverride;
        Modifier modifier = queryParm.getModifier();

        if (modifier != null) {
            operator = modifierOperatorMap.get(modifier);
        }

        if (operator == null) {
            if (defaultOverride != null) {
                operator = defaultOverride;
            } else {
                operator = LIKE;
            }
        }

        log.exiting(CLASSNAME, METHODNAME, operator);
        return operator;
    }

    /**
     * Builds a query segment for the passed query parameter.
     *
     * @param resourceType - A valid FHIR Resource type
     * @param queryParm    - A Parameter object describing the name, value and type
     *                     of search parm
     * @param paramTableAlias   - An alias for the parameter table for which this query parameter
     *                     applies
     * @param logicalRsrcTableAlias   - An alias for the logical resource table for which this query parameter
     *                     applies
     * @param endOfChain   - true if query parameter is at the end of a chain, otherwise false
     * @return SqlQueryData - An object representing the selector query segment for
     *         the passed search parm.
     * @throws Exception
     * @implNote This is just like
     *           {@link com.ibm.fhir.persistence.util.AbstractQueryBuilder.buildQueryParm(QueryParameter,
     *           String)}
     *           except this one takes a paramTableAlias, rsrcTableAlias, and endOfChain
     */
    protected SqlQueryData buildQueryParm(Class<?> resourceType, QueryParameter queryParm, String paramTableAlias, String logicalRsrcTableAlias, boolean endOfChain)
            throws Exception {
        final String METHODNAME = "buildQueryParm";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());

        SqlQueryData databaseQueryParm = null;
        Type type;

        try {
            if (Modifier.MISSING.equals(queryParm.getModifier())) {
                return this.processMissingParm(resourceType, queryParm, paramTableAlias, logicalRsrcTableAlias, endOfChain);
            }
            // NOTE: The special logic needed to process NEAR query parms for the Location resource type is
            // found in method processLocationPosition(). This method will not handle those.
            if (!LocationUtil.isLocation(resourceType, queryParm)) {
                type = queryParm.getType();
                switch (type) {
                case STRING:
                    databaseQueryParm = this.processStringParm(queryParm, paramTableAlias);
                    break;
                case REFERENCE:
                    if (queryParm.isReverseChained()) {
                        databaseQueryParm = this.processReverseChainedReferenceParm(resourceType, queryParm);
                    } else if (queryParm.isChained()) {
                        databaseQueryParm = this.processChainedReferenceParm(queryParm);
                    } else if (queryParm.isInclusionCriteria()) {
                        databaseQueryParm = this.processInclusionCriteria(queryParm);
                    } else {
                        databaseQueryParm = this.processReferenceParm(resourceType, queryParm, paramTableAlias);
                    }
                    break;
                case DATE:
                    databaseQueryParm = this.processDateParm(resourceType, queryParm, paramTableAlias);
                    break;
                case TOKEN:
                    databaseQueryParm = this.processTokenParm(resourceType, queryParm, paramTableAlias, logicalRsrcTableAlias, endOfChain);
                    break;
                case NUMBER:
                    databaseQueryParm = this.processNumberParm(resourceType, queryParm, paramTableAlias);
                    break;
                case QUANTITY:
                    databaseQueryParm = this.processQuantityParm(resourceType, queryParm, paramTableAlias);
                    break;
                case URI:
                    databaseQueryParm = this.processUriParm(queryParm, paramTableAlias);
                    break;
                case COMPOSITE:
                    databaseQueryParm = this.processCompositeParm(resourceType, queryParm, paramTableAlias, logicalRsrcTableAlias);
                    break;
                default:
                    throw new FHIRPersistenceNotSupportedException("Parm type not yet supported: " + type.value());
                }
            } else {
                NearLocationHandler handler = new NearLocationHandler();
                List<Bounding> boundingAreas;
                try {
                    boundingAreas = handler.generateLocationPositionsFromParameters(Arrays.asList(queryParm));
                } catch (FHIRSearchException e) {
                    throw new FHIRPersistenceException("input parameter is invalid bounding area, bad prefix, or bad units", e);
                }
                databaseQueryParm = this.buildLocationQuerySegment(NearLocationHandler.NEAR, boundingAreas, paramTableAlias);
            }
        } finally {
            log.exiting(CLASSNAME, METHODNAME, new Object[] { databaseQueryParm });
        }
        return databaseQueryParm;
    }

    @Override
    protected SqlQueryData processStringParm(QueryParameter queryParm) throws FHIRPersistenceException {
        return processStringParm(queryParm, PARAMETER_TABLE_ALIAS);
    }

    private SqlQueryData processStringParm(QueryParameter queryParm, String tableAlias)
            throws FHIRPersistenceException {
        final String METHODNAME = "processStringParm";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());

        StringBuilder whereClauseSegment = new StringBuilder();
        String operator = this.getOperator(queryParm);
        boolean parmValueProcessed = false;
        String searchValue, tempSearchValue;
        boolean appendEscape;
        SqlQueryData queryData;
        List<Object> bindVariables = new ArrayList<>();

        // Build this piece of the segment:
        // (P1.PARAMETER_NAME_ID = x AND
        this.populateNameIdSubSegment(whereClauseSegment, queryParm.getCode(), tableAlias);

        whereClauseSegment.append(AND).append(LEFT_PAREN);
        for (QueryParameterValue value : queryParm.getValues()) {
            List<String> values = new ArrayList<>();

            appendEscape = false;
            if (LIKE.equals(operator)) {
                // Must escape special wildcard characters _ and % in the parameter value string.
                tempSearchValue =
                        SqlParameterEncoder.encode(value.getValueString()
                                .replace(PERCENT_WILDCARD, ESCAPE_PERCENT)
                                .replace(UNDERSCORE_WILDCARD, ESCAPE_UNDERSCORE));
                if (Modifier.CONTAINS.equals(queryParm.getModifier())) {
                    searchValue = PERCENT_WILDCARD + tempSearchValue + PERCENT_WILDCARD;
                } else {
                    // If there is not a CONTAINS modifier on the query parm, construct
                    // a 'starts with' search value.
                    searchValue = tempSearchValue + PERCENT_WILDCARD;

                    // Specific processing for
                    if (Type.URI.compareTo(queryParm.getType()) == 0
                            && queryParm.getModifier() != null
                            && Modifier.BELOW.compareTo(queryParm.getModifier()) == 0) {
                        searchValue = tempSearchValue + "/" + PERCENT_WILDCARD;
                        values.add(tempSearchValue);
                        values.add(searchValue);
                    }
                }
                appendEscape = true;
            } else {
                searchValue = SqlParameterEncoder.encode(value.getValueString());
            }

            // If multiple values are present, we need to OR them together.
            if (parmValueProcessed) {
                whereClauseSegment.append(OR);
            }

            if (EQ.equals(operator) || Type.URI.equals(queryParm.getType())) {
                // For an exact match, we search against the STR_VALUE column in the Resource's string values table.
                // Build this piece: pX.str_value = search-attribute-value

                if (queryParm.getModifier() != null && Type.URI.equals(queryParm.getType())) {
                    if (Modifier.ABOVE.compareTo(queryParm.getModifier()) == 0) {
                        values =
                                UriModifierUtil.generateAboveValuesQuery(searchValue, whereClauseSegment,
                                        tableAlias + DOT + STR_VALUE);
                    } else if (Modifier.BELOW.compareTo(queryParm.getModifier()) == 0) {
                        UriModifierUtil.generateBelowValuesQuery(whereClauseSegment, tableAlias + DOT + STR_VALUE);
                    }
                }

                if (values.isEmpty()) {
                    // In every other case... use whatever operator comes through at this point
                    whereClauseSegment.append(tableAlias + DOT).append(STR_VALUE)
                            .append(operator).append(BIND_VAR);
                }
            } else {
                // For anything other than an exact match, we search against the STR_VALUE_LCASE column in the
                // Resource's string values table.
                // Also, the search value is "normalized"; it has accents removed and is lower-cased. This enables a
                // case-insensitive, accent-insensitive search.
                // Build this piece: pX.str_value_lcase {operator} search-attribute-value
                whereClauseSegment.append(tableAlias + DOT).append(STR_VALUE_LCASE).append(operator).append(BIND_VAR);
                searchValue = SearchUtil.normalizeForSearch(searchValue);
            }

            if (values.isEmpty()) {
                bindVariables.add(searchValue);
            } else {
                bindVariables.addAll(values);
            }

            // Build this piece: ESCAPE '+'
            if (appendEscape) {
                whereClauseSegment.append(ESCAPE_EXPR);
            }
            parmValueProcessed = true;
        }
        whereClauseSegment.append(RIGHT_PAREN).append(RIGHT_PAREN);

        queryData = new SqlQueryData(whereClauseSegment.toString(), bindVariables);
        log.exiting(CLASSNAME, METHODNAME);
        return queryData;
    }

    @Override
    protected SqlQueryData processReferenceParm(Class<?> resourceType, QueryParameter queryParm) throws Exception {
        return processReferenceParm(resourceType, queryParm, PARAMETER_TABLE_ALIAS);
    }

    /**
     * Get the code system id, reading from the database if necessary
     * @param codeSystemValue
     * @return
     * @throws FHIRPersistenceException
     */
    private Integer getCodeSystemId(String codeSystemValue) throws FHIRPersistenceException {
        return identityCache.getCodeSystemId(codeSystemValue);
    }

    private SqlQueryData processReferenceParm(Class<?> resourceType, QueryParameter queryParm, String tableAlias)
            throws Exception {
        final String METHODNAME = "processReferenceParm";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());

        StringBuilder whereClauseSegment = new StringBuilder();
        String operator = getOperator(queryParm, EQ);

        String searchValue;
        SqlQueryData queryData;
        List<Object> bindVariables = new ArrayList<>();

        // Build this piece of the segment:
        // (P1.PARAMETER_NAME_ID = x AND
        this.populateNameIdSubSegment(whereClauseSegment, queryParm.getCode(), tableAlias);

        whereClauseSegment.append(AND).append(LEFT_PAREN);

        boolean parmValueProcessed = false;
        for (QueryParameterValue value : queryParm.getValues()) {
            String targetResourceType = null;
            searchValue = SqlParameterEncoder.encode(value.getValueString());

            // Make sure we split out the resource type if it is included in the search value
            String[] parts = value.getValueString().split("/");
            if (parts.length == 2) {
                targetResourceType = parts[0];
                searchValue = parts[1];
            }

            // Handle query parm representing this name/value pair construct:
            // <code>{name}:{Resource Type} = {resource-id}</code>
            if (queryParm.getModifier() != null && queryParm.getModifier().equals(Modifier.TYPE)) {
                if (!SearchConstants.Type.REFERENCE.equals(queryParm.getType())) {
                    // Not a Reference
                    searchValue =
                            queryParm.getModifierResourceTypeName() + "/"
                                    + SqlParameterEncoder.encode(value.getValueString());
                } else {
                    // This is a Reference type.
                    if (parts.length != 2) {
                        // fallback to get the target resource type using the modifier
                        targetResourceType = queryParm.getModifierResourceTypeName();
                    }
                }
            }

            // If multiple values are present, we need to OR them together.
            if (parmValueProcessed) {
                whereClauseSegment.append(OR);
            } else {
                parmValueProcessed = true;
            }

            // If the predicate includes a code-system it will resolve to a single value from
            // common_token_values. It helps the query optimizer if we include this additional
            // filter because it can make better cardinality estimates.
            Long commonTokenValueId = null;
            if (EQ.equals(operator) && targetResourceType != null) {
                // targetResourceType is treated as the code-system for references
                commonTokenValueId = getCommonTokenValueId(targetResourceType, searchValue);
            }

            // Build this piece: pX.token_value {operator} search-attribute-value [ AND pX.code_system_id = <n> ]
            whereClauseSegment.append(tableAlias).append(DOT).append(TOKEN_VALUE).append(operator).append(BIND_VAR);
            bindVariables.add(searchValue);

            // add the [optional] condition for the resource type if we have one
            if (commonTokenValueId != null) {
                // #1929 improves cardinality estimation
                // resulting in far better execution plans for many search queries. Because COMMON_TOKEN_VALUE_ID
                // is the primary key for the common_token_values table, we don't need the CODE_SYSTEM_ID = ? predicate.
                whereClauseSegment.append(AND).append(tableAlias).append(DOT).append(COMMON_TOKEN_VALUE_ID).append(EQ)
                    .append(commonTokenValueId);
            } else if (targetResourceType != null) {
                // For better performance, use a literal for the resource type code-system-id, not a parameter marker
                Integer codeSystemIdForResourceType = getCodeSystemId(targetResourceType);
                whereClauseSegment.append(AND).append(tableAlias).append(DOT).append(CODE_SYSTEM_ID).append(EQ).append(nullCheck(codeSystemIdForResourceType));
            }
        }
        whereClauseSegment.append(RIGHT_PAREN).append(RIGHT_PAREN);

        queryData = new SqlQueryData(whereClauseSegment.toString(), bindVariables);
        log.exiting(CLASSNAME, METHODNAME);
        return queryData;
    }

    /**
     * Get the common_token_value_id values matching the given code-system/token_value
     * @return
     */
    private Long getCommonTokenValueId(String codeSystem, String tokenValue) {
        Long result = identityCache.getCommonTokenValueId(codeSystem, tokenValue);

        return result;
    }

    /**
     * Contains special logic for handling chained reference search parameters.
     * <p>
     * Nested sub-selects are built to realize the chaining logic required. Here is
     * a sample chained query for an Observation given this search parameter:
     * {@code device:Device.patient.family=Monella}
     *
     * <pre>
     * SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID, R.LAST_UPDATED, R.IS_DELETED, R.DATA, LR.LOGICAL_ID
     * FROM Observation_LOGICAL_RESOURCES LR
     * JOIN Observation_RESOURCES R ON R.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID AND R.RESOURCE_ID = LR.CURRENT_RESOURCE_ID AND R.IS_DELETED = 'N'
     * JOIN (SELECT DISTINCT LOGICAL_RESOURCE_ID FROM Observation_STR_VALUES
     * WHERE(P1.PARAMETER_NAME_ID = 107 AND (p1.STR_VALUE IN
     *    (SELECT 'Device' || '/' || CLR1.LOGICAL_ID FROM Device_RESOURCES CR1, Device_LOGICAL_RESOURCES CLR1, Device_STR_VALUES CP1 WHERE
     *        CR1.RESOURCE_ID = CLR1.CURRENT_RESOURCE_ID AND CR1.IS_DELETED = 'N' AND CP1.RESOURCE_ID = CR1.RESOURCE_ID AND
     *          CP1.PARAMETER_NAME_ID = 17 AND CP1.STR_VALUE IN
     *                 (SELECT 'Patient' || '/' || CLR2.LOGICAL_ID FROM Patient_RESOURCES CR2, Patient_LOGICAL_RESOURCES CLR2, Patient_STR_VALUES CP2 WHERE
     *                     CR2.RESOURCE_ID = CLR2.CURRENT_RESOURCE_ID AND CR2.IS_DELETED = 'N' AND CP2.RESOURCE_ID = CR2.RESOURCE_ID AND
     *                     CP2.PARAMETER_NAME_ID = 5 AND CP2.STR_VALUE = 'Monella')))
     * TMP0 ON TMP0.LOGICAL_RESOURCE_ID = R.LOGICAL_RESOURCE_ID;
     * </pre>
     *
     * @see https://www.hl7.org/fhir/search.html#reference (section 2.1.1.4.13)
     * @param queryParm
     *                  - A Parameter representing a chained query.
     * @return SqlQueryData - The query segment for a chained parameter reference
     *         search.
     * @throws Exception
     */
    @Override
    protected SqlQueryData processChainedReferenceParm(QueryParameter queryParm) throws Exception {
        final String METHODNAME = "processChainedReferenceParm";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());

        QueryParameter currentParm;
        int refParmIndex = 0;
        String chainedResourceVar = null;
        String chainedLogicalResourceVar = null;
        String chainedParmVar = null;
        String resourceTypeName = null;
        StringBuilder whereClauseSegment = new StringBuilder();
        List<Object> bindVariables = new ArrayList<>();
        SqlQueryData queryData;

        currentParm = queryParm;
        while (currentParm != null) {
            QueryParameter nextParameter = currentParm.getNextParameter();
            if (nextParameter != null) {
                if (refParmIndex == 0) {
                    // Must build this first piece using px placeholder table alias, which will be replaced with a
                    // generated value in the buildQuery() method. The CODE_SYSTEM_ID filter is added for issue #1366
                    // due to the normalization of token values
                    // Build this piece:P1.PARAMETER_NAME_ID = x AND AND P1.CODE_SYSTEM_ID = x AND (p1.TOKEN_VALUE IN
                    this.populateNameIdSubSegment(whereClauseSegment, currentParm.getCode(), PARAMETER_TABLE_ALIAS);

                    // The resource type of the reference is encoded as the code system associated with the token value
                    // so we need to add a filter to ensure we don't match logical-ids for other resource types
                    // Note if the match is for any resource, we simply don't filter on the resource type
                    final String codeSystemName = currentParm.getModifierResourceTypeName();
                    if (codeSystemName != null && !codeSystemName.equals("*")) {
                        Integer codeSystemId = identityCache.getCodeSystemId(codeSystemName);
                        whereClauseSegment.append(AND).append(PARAMETER_TABLE_ALIAS).append(DOT).append(CODE_SYSTEM_ID).append(EQ)
                                .append(nullCheck(codeSystemId));
                    }

                    whereClauseSegment.append(AND);
                    whereClauseSegment.append(LEFT_PAREN);
                    whereClauseSegment.append(PARAMETER_TABLE_ALIAS).append(DOT).append(TOKEN_VALUE).append(IN);
                } else {
                    // Build this piece: CP1.PARAMETER_NAME_ID = x AND CP1.TOKEN_VALUE IN
                    appendMidChainParm(whereClauseSegment, currentParm, chainedParmVar);
                }

                refParmIndex++;
                chainedResourceVar        = CR + refParmIndex;
                chainedLogicalResourceVar = CLR + refParmIndex;
                chainedParmVar            = CP + refParmIndex;

                // The * is a wildcard for any resource type. This occurs only in the case where a reference parameter
                // chain was built to represent a compartment search with chained inclusion criteria that includes a
                // wildcard.
                //
                // For this situation, a separate method is called, and further processing of the chain by this method
                // is halted.
                if (currentParm.getModifierResourceTypeName().equals("*")) {
                    this.processWildcardChainedRefParm(currentParm, chainedResourceVar, chainedLogicalResourceVar,
                            chainedParmVar, whereClauseSegment, bindVariables);
                    break;
                }
                resourceTypeName = currentParm.getModifierResourceTypeName();
                // Build this piece: (SELECT 'resource-type-name' || '/' || CLRx.LOGICAL_ID ...
                // since #1366, we no longer need to prepend the resource-type-name
                whereClauseSegment.append(LEFT_PAREN);
                appendInnerSelect(whereClauseSegment, currentParm, resourceTypeName,
                        chainedResourceVar, chainedLogicalResourceVar, chainedParmVar);
            } else {
                // This logic processes the LAST parameter in the chain.
                // Build this piece: CPx.PARAMETER_NAME_ID = x AND CPx.TOKEN_VALUE = ?
                // TODO do we need to filter the code-system here too?
                if (chainedParmVar == null) {
                    chainedParmVar = CP + 1;
                }
                Class<?> chainedResourceType = ModelSupport.getResourceType(resourceTypeName);

                String code = currentParm.getCode();
                SqlQueryData sqlQueryData;
                if ("_id".equals(code)) {
                    // The code '_id' is only going to be the end of the change as it is a base element.
                    // We know at this point this is an '_id' and at the tail of the parameter chain
                    sqlQueryData = buildChainedIdClause(currentParm, chainedParmVar);
                } else if ("_lastUpdated".equals(code)) {
                    // Build the rest: (LAST_UPDATED <operator> ?)
                    LastUpdatedParmBehaviorUtil util = new LastUpdatedParmBehaviorUtil();
                    StringBuilder lastUpdatedWhereClause = new StringBuilder();
                    util.executeBehavior(lastUpdatedWhereClause, currentParm);

                    // issue-2011 LAST_UPDATED now in both XXX_resources and XXX_logical_resources so we need an alias
                    sqlQueryData = new SqlQueryData(lastUpdatedWhereClause.toString()
                        .replaceAll(LastUpdatedParmBehaviorUtil.LAST_UPDATED_COLUMN_NAME,
                            chainedResourceVar + DOT + LastUpdatedParmBehaviorUtil.LAST_UPDATED_COLUMN_NAME), util.getBindVariables());
                } else {
                    sqlQueryData = buildQueryParm(chainedResourceType, currentParm, chainedParmVar, chainedLogicalResourceVar, true);
                }

                if (log.isLoggable(Level.FINE)) {
                    log.fine("chained sqlQueryData[" + chainedParmVar + "] = " + sqlQueryData.getQueryString());
                }
                whereClauseSegment.append(sqlQueryData.getQueryString());
                bindVariables.addAll(sqlQueryData.getBindVariables());
            }
            currentParm = currentParm.getNextParameter();
        }

        // Finally, ensure the correct number of right parens are inserted to balance the where clause segment.
        int rightParensRequired = queryParm.getChain().size() + 2;
        for (int i = 0; i < rightParensRequired; i++) {
            whereClauseSegment.append(RIGHT_PAREN);
        }

        queryData = new SqlQueryData(whereClauseSegment.toString(), bindVariables);
        log.exiting(CLASSNAME, METHODNAME, whereClauseSegment.toString());
        return queryData;
    }

    /*
     * Builds the specific handling for exact matches on _id.
     * The procedure here is SIMILAR to that of QuerySegmentAggregator.processFromClauseForId
     *
     * Results in a query like: CP1.LOGICAL_ID IN (?)
     */
    private SqlQueryData buildChainedIdClause(QueryParameter currentParm, String chainedParmVar) {
        StringBuilder whereClauseSegment = new StringBuilder();
        List<Object> bindVariables = new ArrayList<>();

        whereClauseSegment
            .append(chainedParmVar.replace("CP", "CLR")).append(DOT).append(LOGICAL_ID).append(" IN (");

        List<QueryParameterValue> vals = currentParm.getValues();
        boolean add = false;
        for (QueryParameterValue v : vals) {
            if (add) {
                whereClauseSegment.append(COMMA);
            } else {
                add = true;
            }
            whereClauseSegment.append(BIND_VAR);
            bindVariables.add(SqlParameterEncoder.encode(v.getValueCode()));
        }
        whereClauseSegment.append(") ");

        return new SqlQueryData(whereClauseSegment.toString(), bindVariables);
    }

    private void appendMidChainParm(StringBuilder whereClauseSegment, QueryParameter currentParm, String chainedParmVar)
            throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException, FHIRPersistenceException {
        Integer parameterNameId = identityCache.getParameterNameId(currentParm.getCode());
        whereClauseSegment.append(chainedParmVar).append(DOT).append(PARAMETER_NAME_ID).append(EQ)
                .append(parameterNameId);

        final String codeSystemName = currentParm.getModifierResourceTypeName();
        if (codeSystemName != null && !codeSystemName.equals("*")) {
            Integer codeSystemId = identityCache.getCodeSystemId(codeSystemName);
            whereClauseSegment.append(AND).append(chainedParmVar).append(DOT).append(CODE_SYSTEM_ID).append(EQ)
                    .append(nullCheck(codeSystemId));
        }

        whereClauseSegment.append(AND).append(chainedParmVar).append(DOT).append(TOKEN_VALUE).append(IN);
    }

    private void appendInnerSelect(StringBuilder whereClauseSegment, QueryParameter currentParm,
            String resourceTypeName, String chainedResourceVar, String chainedLogicalResourceVar, String chainedParmVar) {
        String chainedResourceTableAlias = chainedResourceVar + DOT;
        String chainedLogicalResourceTableAlias = chainedLogicalResourceVar + DOT;
        String chainedParmTableAlias = chainedParmVar + DOT;

        // Build this piece: SELECT 'resource-type-name' || '/' || CLRx.LOGICAL_ID
        // Note since #1366, we no longer need to prepend the resourceTypeName
        whereClauseSegment.append(SELECT).append(chainedLogicalResourceTableAlias).append(LOGICAL_ID);

        QueryParameter nextParameter = currentParm.getNextParameter();

        // Build this piece: FROM Device_LOGICAL_RESOURCES CLR1
        whereClauseSegment.append(FROM)
            .append(resourceTypeName).append(_LOGICAL_RESOURCES).append(SPACE).append(chainedLogicalResourceVar);

        if (Type.COMPOSITE.equals(nextParameter.getType())) {
            // If next parameter is composite, just join RESOURCES table:
            // JOIN Device_RESOURCES CR1 ON CR1.RESOURCE_ID = CLR1.CURRENT_RESOURCE_ID AND CR1.IS_DELETED = 'N'
            whereClauseSegment.append(JOIN)
                .append(resourceTypeName).append(_RESOURCES).append(SPACE).append(chainedResourceVar)
                .append(ON)
                .append(chainedResourceTableAlias).append(RESOURCE_ID).append(EQ)
                .append(chainedLogicalResourceTableAlias).append(CURRENT_RESOURCE_ID)
                .append(AND)
                .append(chainedResourceTableAlias).append(IS_DELETED_NO);
            // If composite parameter has :missing modifier, add WHERE
            if (Modifier.MISSING.equals(nextParameter.getModifier())) {
                whereClauseSegment.append(WHERE);
            }
        } else {
            // Build this piece: , Device_RESOURCES CR1
            whereClauseSegment.append(COMMA).append(resourceTypeName).append(_RESOURCES).append(SPACE).append(chainedResourceVar);

            // If we're dealing with anything other than id, then proceed to add the parameters table.
            if (nextParameter != null && !"_id".equals(nextParameter.getCode())) {
                whereClauseSegment.append(COMMA)
                    .append(QuerySegmentAggregator.tableName(resourceTypeName, nextParameter)).append(chainedParmVar);
            }

            whereClauseSegment.append(WHERE);

            // CR1.RESOURCE_ID = CLR1.CURRENT_RESOURCE_ID AND CR1.IS_DELETED = 'N' AND
            whereClauseSegment.append(chainedResourceTableAlias).append(RESOURCE_ID).append(EQ)
                .append(chainedLogicalResourceTableAlias).append(CURRENT_RESOURCE_ID)
                .append(AND)
                .append(chainedResourceTableAlias).append(IS_DELETED_NO)
                .append(AND);

            // CP1.LOGICAL_RESOURCE_ID = CLR1.LOGICAL_RESOURCE_ID AND
            if (nextParameter != null && !"_id".equals(nextParameter.getCode())) {
                whereClauseSegment.append(chainedParmTableAlias).append(LOGICAL_RESOURCE_ID).append(EQ)
                    .append(chainedResourceTableAlias).append(LOGICAL_RESOURCE_ID)
                    .append(AND);
            }
        }
    }

    /**
     * This method handles the processing of a wildcard chained reference parameter.
     * The wildcard represents ALL FHIR
     * resource types stored in the FHIR database.
     *
     * @throws Exception
     */
    private void processWildcardChainedRefParm(QueryParameter currentParm, String chainedResourceVar,
            String chainedLogicalResourceVar, String chainedParmVar,
            StringBuilder whereClauseSegment, List<Object> bindVariables) throws Exception {
        final String METHODNAME = "processWildcardChainedRefParm";
        log.entering(CLASSNAME, METHODNAME, currentParm.toString());

        String resourceTypeName;
        Collection<Integer> resourceTypeIds;
        QueryParameter lastParm;
        boolean selectGenerated = false;
        Map<String, Integer> resourceNameIdMap = null;
        Map<Integer, String> resourceIdNameMap = null;

        lastParm          = currentParm.getNextParameter();

        // Acquire ALL Resource Type Ids
        resourceNameIdMap = resourceDao.readAllResourceTypeNames();
        resourceTypeIds   = resourceNameIdMap.values();
        resourceIdNameMap =
                resourceNameIdMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

        // Build a sub-SELECT for each resource type, and put them together in a UNION.
        for (Integer resourceTypeId : resourceTypeIds) {
            if (selectGenerated) {
                whereClauseSegment.append(" UNION ");
            }
            // Build this piece: (SELECT 'resource-type-name' || '/' || CLRx.LOGICAL_ID ...
            resourceTypeName = resourceIdNameMap.get(resourceTypeId);

            if (!selectGenerated) {
                whereClauseSegment.append(LEFT_PAREN);
            }

            appendInnerSelect(whereClauseSegment, currentParm, resourceTypeName, chainedResourceVar,
                    chainedLogicalResourceVar, chainedParmVar);

            // This logic processes the LAST parameter in the chain.
            // Build this piece: CPx.PARAMETER_NAME_ID = x AND CPx.STR_VALUE = ?
            Class<?> chainedResourceType = ModelSupport.getResourceType(resourceTypeName);
            SqlQueryData sqlQueryData = buildQueryParm(chainedResourceType, lastParm, chainedParmVar, chainedLogicalResourceVar, false);
            whereClauseSegment.append(sqlQueryData.getQueryString());
            bindVariables.addAll(sqlQueryData.getBindVariables());

            selectGenerated = true;
        }

        log.exiting(CLASSNAME, METHODNAME, whereClauseSegment.toString());

    }

    /**
     * This method is the entry point for processing inclusion criteria, which
     * define resources that are part of a compartment-based search.
     * Example inclusion criteria for AuditEvent in the Patient compartment:
     *
     * TODO: revisit this method in light of https://jira.hl7.org/browse/FHIR-15906 (removal of chained inclusion criteria)
     * TODO: consider leaving the chained inclusion in light of https://jira.hl7.org/browse/FHIR-17358
     * <pre>
     * {
     *   "name": "AuditEvent",
     *   "inclusionCriteria": [
     *     "patient",                              This is a simple attribute inclusion criterion
     *     "participant.patient:Device",           This is a chained inclusion criterion
     *     "participant.patient:RelatedPerson",    This is a chained inclusion criterion
     *     "reference.patient:*"                   This is a chained inclusion criterion with wildcard. The wildcard means "any resource type".
     *   ]
     * }
     * </pre>
     * <p>
     * Here is a sample generated query for this inclusion criteria:
     * <li>PARAMETER_NAME_ID 13 = 'participant'
     * <li>PARAMETER_NAME_ID 14 = 'patient'
     * <li>PARAMETER_NAME_ID 16 = 'reference'
     *
     * <pre>
     *    SELECT COUNT(R.RESOURCE_ID) FROM
     *    AuditEvent_RESOURCES R, AuditEvent_LOGICAL_RESOURCES LR , AuditEvent_STR_VALUES P1 WHERE
     *    R.RESOURCE_ID = LR.CURRENT_RESOURCE_ID AND
     *    R.IS_DELETED = 'N' AND
     *    P1.RESOURCE_ID = R.RESOURCE_ID AND
     *    ((P1.PARAMETER_NAME_ID=14 AND P1.STR_VALUE = ?) OR
     *     ((P1.PARAMETER_NAME_ID=13 AND
     *      (P1.STR_VALUE IN
     *        (SELECT 'Device' || '/' || CLR1.LOGICAL_ID FROM
     *            Device_RESOURCES CR1, Device_LOGICAL_RESOURCES CLR1, Device_STR_VALUES CP1 WHERE
     *            CR1.RESOURCE_ID = CLR1.CURRENT_RESOURCE_ID AND
     *            CR1.IS_DELETED = 'N' AND
     *            CP1.RESOURCE_ID = CR1.RESOURCE_ID AND
     *            CP1.PARAMETER_NAME_ID=14 AND CP1.STR_VALUE = ?)))) OR
     *    ((P1.PARAMETER_NAME_ID=13 AND
     *     (P1.STR_VALUE IN
     *        (SELECT 'RelatedPerson' || '/' || CLR1.LOGICAL_ID FROM
     *            RelatedPerson_RESOURCES CR1, RelatedPerson_LOGICAL_RESOURCES CLR1, RelatedPerson_STR_VALUES CP1 WHERE
     *            CR1.RESOURCE_ID = CLR1.CURRENT_RESOURCE_ID AND
     *            CR1.IS_DELETED = 'N' AND
     *            CP1.RESOURCE_ID = CR1.RESOURCE_ID AND
     *            CP1.PARAMETER_NAME_ID=14 AND CP1.STR_VALUE = ?)))) OR
     *     ((P1.PARAMETER_NAME_ID=16 AND
     *      (P1.STR_VALUE IN
     *        (SELECT 'AuditEvent' || '/' || CLR1.LOGICAL_ID FROM
     *            auditevent_RESOURCES CR1, auditevent_LOGICAL_RESOURCES CLR1, auditevent_STR_VALUES CP1 WHERE
     *            CR1.RESOURCE_ID = CLR1.CURRENT_RESOURCE_ID AND
     *            CR1.IS_DELETED = 'N' AND
     *            CP1.RESOURCE_ID = CR1.RESOURCE_ID AND
     *            CP1.PARAMETER_NAME_ID=14 AND CP1.STR_VALUE = ?
     *            UNION
     *            SELECT 'Device' || '/' || CLR1.LOGICAL_ID FROM
     *                device_RESOURCES CR1, device_LOGICAL_RESOURCES CLR1, device_STR_VALUES CP1 WHERE
     *                CR1.RESOURCE_ID = CLR1.CURRENT_RESOURCE_ID AND
     *                CR1.IS_DELETED = 'N' AND
     *                CP1.RESOURCE_ID = CR1.RESOURCE_ID AND
     *                CP1.PARAMETER_NAME_ID=14 AND CP1.STR_VALUE = ?)))));
     * </pre>
     *
     * @throws Exception
     * @see compartments.json for the specificaiton of compartments, resources
     *      contained in each compartment, and the
     *      criteria for a resource to be included in a compartment.
     */
    @Override
    protected SqlQueryData processInclusionCriteria(QueryParameter queryParm) throws Exception {
        final String METHODNAME = "processInclusionCriteria";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());

        StringBuilder whereClauseSegment = new StringBuilder();
        QueryParameter currentParm;
        List<String> currentParmValues;
        List<Object> bindVariables = new ArrayList<>();
        SqlQueryData queryData;
        SqlQueryData chainedIncQueryData;

        currentParm = queryParm;
        whereClauseSegment.append(LEFT_PAREN);
        while (currentParm != null) {
            if (currentParm.getValues() == null || currentParm.getValues().isEmpty()) {
                throw new FHIRPersistenceException("No Paramter values found when processing inclusion criteria.");
            }
            currentParmValues = new ArrayList<>();
            String resourceTypeName = null;

            // Handle the special case of chained inclusion criteria.
            if (currentParm.getCode().contains(DOT)) {
                whereClauseSegment.append(LEFT_PAREN);
                chainedIncQueryData = this.processChainedInclusionCriteria(currentParm);
                whereClauseSegment.append(chainedIncQueryData.getQueryString());
                bindVariables.addAll(chainedIncQueryData.getBindVariables());
                whereClauseSegment.append(RIGHT_PAREN);
            } else {
                for (QueryParameterValue value : currentParm.getValues()) {
                    String valueString = value.getValueString();

                    // split the resource type name out (since issue #1366)
                    String[] parts = valueString.split("/");
                    if (parts.length == 2) {
                        if (resourceTypeName == null) {
                            resourceTypeName = parts[0];
                        } else if (!resourceTypeName.equals(parts[0])){
                            log.warning("Resource type name must be consistent across inclusion criteria values " +
                                    "[" + resourceTypeName + "," + parts[0] + "]");
                        }
                        currentParmValues.add(parts[1]);
                    } else {
                        log.warning("Unexpected inclusion criteria value: '" + valueString + "'. " +
                                  "Inclusion criteria should always be of the form <compartmentName>/<id>");
                        currentParmValues.add(valueString);
                    }
                }

                // Build this piece:
                // (pX.PARAMETER_NAME_ID = x AND
                this.populateNameIdSubSegment(whereClauseSegment, currentParm.getCode(), PARAMETER_TABLE_ALIAS);
                whereClauseSegment.append(AND);

                boolean fallback = false;
                Set<String> commonTokenValueIds = new HashSet<>();
                for (String currentParmValue : currentParmValues) {
                    Long commonTokenValueId = getCommonTokenValueId(resourceTypeName, currentParmValue);
                    if (commonTokenValueId == null) {
                        // Can't use the common_token_value_id optimization, so do it the old way.
                        log.warning("Unable to obtain common token value id for " + resourceTypeName + "/" + currentParmValue);
                        fallback = true;
                        break;
                    } else {
                        commonTokenValueIds.add(commonTokenValueId.toString());
                    }
                }

                if (fallback) {
                    // Can't use the common_token_value_id optimization, so do it the old way.
                    // pX.token_value IN (val1, val2, ...) [ AND pX.code_system_id = n ]
                    whereClauseSegment.append(PARAMETER_TABLE_ALIAS).append(DOT).append(TOKEN_VALUE)
                            .append(IN).append(LEFT_PAREN)
                            .append(String.join(", ", Collections.nCopies(currentParmValues.size(), BIND_VAR)))
                            .append(RIGHT_PAREN);
                    Integer codeSystemIdForResourceType = getCodeSystemId(resourceTypeName);
                    whereClauseSegment.append(AND).append(PARAMETER_TABLE_ALIAS).append(DOT)
                    .append(CODE_SYSTEM_ID).append(EQ).append(nullCheck(codeSystemIdForResourceType));
                    for (String currentParmValue : currentParmValues) {
                        bindVariables.add(currentParmValue);
                    }
                } else {
                    // #1929 improves cardinality estimation
                    // resulting in far better execution plans for many search queries. Because COMMON_TOKEN_VALUE_ID
                    // is the primary key for the common_token_values table, we don't need the CODE_SYSTEM_ID = ? predicate.
                    whereClauseSegment.append(PARAMETER_TABLE_ALIAS).append(DOT).append(COMMON_TOKEN_VALUE_ID)
                        .append(IN).append(LEFT_PAREN)
                        .append(String.join(", ", String.join(",", commonTokenValueIds)))
                        .append(RIGHT_PAREN);
                }

                whereClauseSegment.append(RIGHT_PAREN);
            }

            currentParm = currentParm.getNextParameter();
            // If more than one parameter is in the chain, OR them together.
            if (currentParm != null) {
                whereClauseSegment.append(OR);
            }
        }
        whereClauseSegment.append(RIGHT_PAREN);
        queryData = new SqlQueryData(whereClauseSegment.toString(), bindVariables);
        log.exiting(CLASSNAME, METHODNAME, whereClauseSegment.toString());
        return queryData;
    }

    @Override
    protected SqlQueryData processDateParm(Class<?> resourceType, QueryParameter queryParm) throws Exception {
        return processDateParm(resourceType, queryParm, PARAMETER_TABLE_ALIAS);
    }

    private SqlQueryData processDateParm(Class<?> resourceType, QueryParameter queryParm, String tableAlias)
            throws Exception {
        final String METHODNAME = "processDateParm";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());

        StringBuilder whereClauseSegment = new StringBuilder();

        // Build this piece of the segment:
        // (P1.PARAMETER_NAME_ID = x AND
        this.populateNameIdSubSegment(whereClauseSegment, queryParm.getCode(), tableAlias);

        List<Timestamp> bindVariables = new ArrayList<>();
        DateParmBehaviorUtil behaviorUtil = new DateParmBehaviorUtil();
        behaviorUtil.executeBehavior(whereClauseSegment, queryParm, bindVariables, tableAlias);

        SqlQueryData queryData = new SqlQueryData(whereClauseSegment.toString(), bindVariables);
        log.exiting(CLASSNAME, METHODNAME);
        return queryData;
    }

    @Override
    protected SqlQueryData processTokenParm(Class<?> resourceType, QueryParameter queryParm) throws FHIRPersistenceException {
        return processTokenParm(resourceType, queryParm, PARAMETER_TABLE_ALIAS, LR, false);
    }

    private SqlQueryData processTokenParm(Class<?> resourceType, QueryParameter queryParm, String paramTableAlias, String logicalRsrcTableAlias, boolean endOfChain) throws FHIRPersistenceException {
        final String METHODNAME = "processTokenParm";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());

        StringBuilder whereClauseSegment = new StringBuilder();
        String operator = this.getOperator(queryParm, EQ);
        boolean parmValueProcessed = false;
        SqlQueryData queryData;
        List<Object> bindVariables = new ArrayList<>();
        String tableAlias = paramTableAlias;

        String code = queryParm.getCode();
        if (!QuerySegmentAggregator.ID.equals(code)) {

            // Only generate NOT EXISTS subquery if :not modifier is within chained query;
            // when :not modifier is within non-chained query QuerySegmentAggregator.buildWhereClause generates the NOT EXISTS subquery
            boolean surroundWithNotExistsSubquery = Modifier.NOT.equals(queryParm.getModifier()) && endOfChain;
            if (surroundWithNotExistsSubquery) {
                whereClauseSegment.append(NOT).append(EXISTS);

                // PARAMETER_TABLE_NAME_PLACEHOLDER is replaced by the actual table name for the resource type by QuerySegmentAggregator.buildWhereClause(...)
                String valuesTable = !ModelSupport.isAbstract(resourceType) ? QuerySegmentAggregator.tableName(resourceType.getSimpleName(), queryParm) : PARAMETER_TABLE_NAME_PLACEHOLDER;
                tableAlias = paramTableAlias + "_param0";
                whereClauseSegment.append("(SELECT 1 FROM " + valuesTable + AS + tableAlias + WHERE);
            }

            // Build this piece of the segment:
            // (P1.PARAMETER_NAME_ID = x AND
            this.populateNameIdSubSegment(whereClauseSegment, queryParm.getCode(), tableAlias);

            whereClauseSegment.append(AND).append(LEFT_PAREN);
            for (QueryParameterValue value : queryParm.getValues()) {
                // If multiple values are present, we need to OR them together.
                if (parmValueProcessed) {
                    whereClauseSegment.append(OR);
                }

                whereClauseSegment.append(LEFT_PAREN);
                // Include code
                whereClauseSegment.append(tableAlias + DOT).append(TOKEN_VALUE).append(operator).append(BIND_VAR);
                bindVariables.add(SqlParameterEncoder.encode(value.getValueCode()));

                // Include system if present.
                if (value.getValueSystem() != null && !value.getValueSystem().isEmpty()) {
                    Long commonTokenValueId = null;
                    if (NE.equals(operator)) {
                        whereClauseSegment.append(OR);
                    } else {
                        whereClauseSegment.append(AND);

                        // use #1929 optimization if we can
                        commonTokenValueId = getCommonTokenValueId(value.getValueSystem(), value.getValueCode());
                    }

                    if (commonTokenValueId != null) {
                        // #1929 improves cardinality estimation
                        // resulting in far better execution plans for many search queries. Because COMMON_TOKEN_VALUE_ID
                        // is the primary key for the common_token_values table, we don't need the CODE_SYSTEM_ID = ? predicate.
                        whereClauseSegment.append(tableAlias).append(DOT).append(COMMON_TOKEN_VALUE_ID).append(EQ)
                            .append(commonTokenValueId);
                    } else {
                        // common token value not found so we can't use the optimization. Filter the code-system-id
                        // instead, which ends up being the logical equivalent.
                        Integer codeSystemId = identityCache.getCodeSystemId(value.getValueSystem());
                        whereClauseSegment.append(tableAlias).append(DOT).append(CODE_SYSTEM_ID).append(operator)
                        .append(nullCheck(codeSystemId));
                    }
                }
                whereClauseSegment.append(RIGHT_PAREN);
                parmValueProcessed = true;
            }

            whereClauseSegment.append(RIGHT_PAREN).append(RIGHT_PAREN);

            if (surroundWithNotExistsSubquery) {
                whereClauseSegment.append(AND).append(tableAlias).append(".LOGICAL_RESOURCE_ID = ").append(logicalRsrcTableAlias).append(".LOGICAL_RESOURCE_ID");
                whereClauseSegment.append(RIGHT_PAREN);
            }
        }
        queryData = new SqlQueryData(whereClauseSegment.toString(), bindVariables);

        log.exiting(CLASSNAME, METHODNAME);
        return queryData;
    }

    @Override
    protected SqlQueryData processNumberParm(Class<?> resourceType, QueryParameter queryParm)
            throws FHIRPersistenceException {
        return processNumberParm(resourceType, queryParm, PARAMETER_TABLE_ALIAS);
    }

    private SqlQueryData processNumberParm(Class<?> resourceType, QueryParameter queryParm, String tableAlias)
            throws FHIRPersistenceException {
        final String METHODNAME = "processNumberParm";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());

        StringBuilder whereClauseSegment = new StringBuilder();
        List<Object> bindVariables = new ArrayList<>();

        // Build this piece of the segment:
        // (P1.PARAMETER_NAME_ID = x AND
        this.populateNameIdSubSegment(whereClauseSegment, queryParm.getCode(), tableAlias);

        // Calls to the NumberParmBehaviorUtil which encapsulates the precision
        // selection criteria.
        NumberParmBehaviorUtil.executeBehavior(whereClauseSegment, queryParm, bindVariables, resourceType, tableAlias,
                this);
        SqlQueryData queryData = new SqlQueryData(whereClauseSegment.toString(), bindVariables);

        log.exiting(CLASSNAME, METHODNAME, whereClauseSegment.toString());
        return queryData;
    }

    @Override
    protected SqlQueryData processQuantityParm(Class<?> resourceType, QueryParameter queryParm) throws Exception {
        return processQuantityParm(resourceType, queryParm, PARAMETER_TABLE_ALIAS);
    }

    private SqlQueryData processQuantityParm(Class<?> resourceType, QueryParameter queryParm, String tableAlias)
            throws Exception {
        final String METHODNAME = "processQuantityParm";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());

        StringBuilder whereClauseSegment = new StringBuilder();
        List<Object> bindVariables = new ArrayList<>();

        // Build this piece of the segment:
        // (P1.PARAMETER_NAME_ID = x AND
        this.populateNameIdSubSegment(whereClauseSegment, queryParm.getCode(), tableAlias);

        // Calls to the QuantityParmBehaviorUtil which encapsulates the precision
        // selection criteria.
        QuantityParmBehaviorUtil behaviorUtil = new QuantityParmBehaviorUtil();
        behaviorUtil.executeBehavior(whereClauseSegment, queryParm, bindVariables, tableAlias, parameterDao);
        SqlQueryData queryData = new SqlQueryData(whereClauseSegment.toString(), bindVariables);

        log.exiting(CLASSNAME, METHODNAME, whereClauseSegment.toString());
        return queryData;
    }

    @Override
    protected SqlQueryData processUriParm(QueryParameter queryParm) throws FHIRPersistenceException {
        return processUriParm(queryParm, PARAMETER_TABLE_ALIAS);
    }

    /**
     * Creates a query segment for a URI type parameter.
     *
     * @param queryParm  - The query parameter
     * @param tableAlias - An alias for the table to query
     * @return SqlQueryData - An object containing query segment
     * @throws FHIRPersistenceException
     */
    protected SqlQueryData processUriParm(QueryParameter queryParm, String tableAlias) throws FHIRPersistenceException {
        final String METHODNAME = "processUriParm";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());
        SqlQueryData parmRoot = this.processStringParm(queryParm, tableAlias);
        log.exiting(CLASSNAME, METHODNAME, parmRoot.toString());
        return parmRoot;
    }

    @Override
    protected SqlQueryData processCompositeParm(Class<?> resourceType, QueryParameter queryParm)
            throws FHIRPersistenceException {
        return processCompositeParm(resourceType, queryParm, PARAMETER_TABLE_ALIAS, LR);
    }

    /**
     * Creates a query segment for a COMPOSITE type parameter.
     *
     * @param queryParm  - The query parameter
     * @param paramTableAlias - An alias for the parameter table to query
     * @param logicalResourceTableAlias - An alias for the logical resource table to query
     * @return SqlQueryData - An object containing query segment
     * @throws FHIRPersistenceException
     */
    private SqlQueryData processCompositeParm(Class<?> resourceType, QueryParameter queryParm, String paramTableAlias, String logicalResourceTableAlias)
            throws FHIRPersistenceException {
        final String METHODNAME = "processCompositeParm";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());

        StringBuilder joinSegment = new StringBuilder();
        SqlQueryData queryData;
        List<Object> bindVariables = new ArrayList<>();

        try {
            if (queryParm.getValues().size() > 1) {
                // Build as WHERE clause using EXISTS
                SqlQueryData compositeQueryData = populateCompositeSelectSubSegment(queryParm, resourceType, paramTableAlias, logicalResourceTableAlias);
                joinSegment.append(" WHERE (EXISTS ").append(compositeQueryData.getQueryString()).append(RIGHT_PAREN);
                bindVariables.addAll(compositeQueryData.getBindVariables());
            } else {
                // Build as JOINS
                QueryParameterValue compositeValue = queryParm.getValues().get(0);
                List<QueryParameter> components = compositeValue.getComponent();
                for (int componentNum = 1; componentNum <= components.size(); componentNum++) {
                    QueryParameter component = components.get(componentNum - 1);
                    String componentTableAlias = paramTableAlias + "_p" + componentNum;

                    // Build JOIN clause
                    joinSegment.append(JOIN).append(QuerySegmentAggregator.tableName(resourceType.getSimpleName(), component))
                                .append(AS).append(componentTableAlias).append(ON);

                    SqlQueryData subQueryData = buildQueryParm(resourceType, component, componentTableAlias, logicalResourceTableAlias, false);
                    joinSegment.append(subQueryData.getQueryString());
                    bindVariables.addAll(subQueryData.getBindVariables());

                    joinSegment.append(AND).append(logicalResourceTableAlias).append(".LOGICAL_RESOURCE_ID = ").append(componentTableAlias).append(".LOGICAL_RESOURCE_ID");
                    if (componentNum > 1) {
                        joinSegment.append(AND).append(componentTableAlias).append(".COMPOSITE_ID = " + paramTableAlias + "_p1.COMPOSITE_ID");
                    }
                }
            }
        } catch (Exception e) {
            throw new FHIRPersistenceException("Error while creating subquery for parameter '" + queryParm.getCode() + "'", e);
        }

        queryData = new SqlQueryData(joinSegment.toString(), bindVariables);
        log.exiting(CLASSNAME, METHODNAME);
        return queryData;
    }

    @Override
    protected SqlQueryData buildLocationQuerySegment(String parmName, List<Bounding> boundingAreas, String paramTableAlias)
            throws FHIRPersistenceException {
        final String METHODNAME = "buildLocationQuerySegment";
        log.entering(CLASSNAME, METHODNAME, parmName);

        StringBuilder whereClauseSegment = new StringBuilder();
        List<Object> bindVariables = new ArrayList<>();

        StringBuilder populateNameIdSubSegment = new StringBuilder();
        this.populateNameIdSubSegment(populateNameIdSubSegment, parmName, paramTableAlias);

        LocationParmBehaviorUtil behaviorUtil = new LocationParmBehaviorUtil();
        behaviorUtil.buildLocationSearchQuery(populateNameIdSubSegment.toString(), whereClauseSegment, bindVariables, boundingAreas, paramTableAlias);

        SqlQueryData queryData = new SqlQueryData(whereClauseSegment.toString(), bindVariables);
        log.exiting(CLASSNAME, METHODNAME, whereClauseSegment.toString());
        return queryData;
    }

    /**
     * Populates the parameter name sub-segment of the passed where clause segment.
     *
     * @param whereClauseSegment
     * @param queryParmName
     * @param parameterTableAlias the alias for the parameter table e.g. pX
     * @param resourceTypeName the resource type of the reference being followed
     * @throws FHIRPersistenceException
     */
    private void populateNameIdSubSegment(StringBuilder whereClauseSegment, String queryParmName,
            String parameterTableAlias) throws FHIRPersistenceException {
        final String METHODNAME = "populateNameIdSubSegment";
        log.entering(CLASSNAME, METHODNAME, queryParmName);

        Integer parameterNameId;

        // Build this piece of the segment:
        // (P1.PARAMETER_NAME_ID = x
        parameterNameId = identityCache.getParameterNameId(queryParmName);

        whereClauseSegment.append(LEFT_PAREN);
        whereClauseSegment.append(parameterTableAlias + DOT).append(PARAMETER_NAME_ID).append(EQ)
                .append(nullCheck(parameterNameId));

        log.exiting(CLASSNAME, METHODNAME);
    }

    /**
     * Use -1 in place of a null literal, otherwise return the literal value
     *
     * @param n
     * @return
     */
    private String nullCheck(Integer n) {
        return n == null ? "-1" : n.toString();
    }

    /**
     * This method handles the special case of chained inclusion criteria. Using
     * data extracted from the passed query
     * parameter, a new Parameter chain is built to represent the chained inclusion
     * criteria. That new Parameter is then
     * passed to the inherited processChainedReferenceParamter() method to generate
     * the required where clause segment.
     *
     * @see https://www.hl7.org/fhir/compartments.html
     * @param queryParm
     *                  - A Parameter representing chained inclusion criterion.
     * @return SqlQueryData - the where clause segment and bind variables for a
     *         chained inclusion criterion.
     * @throws Exception
     */
    private SqlQueryData processChainedInclusionCriteria(QueryParameter queryParm) throws Exception {
        final String METHODNAME = "processChainedInclusionCriteria";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());

        SqlQueryData queryData;
        QueryParameter rootParameter = null;

        // Transform the passed query parm into a chained parameter representation.
        rootParameter = SearchUtil.parseChainedInclusionCriteria(queryParm);
        // Call method to process the Parameter built by this method as a chained parameter.
        queryData     = this.processChainedReferenceParm(rootParameter);

        log.exiting(CLASSNAME, METHODNAME);
        return queryData;

    }

    private SqlQueryData processMissingParm(Class<?> resourceType, QueryParameter queryParm, String paramTableAlias, String logicalRsrcTableAlias, boolean endOfChain)
            throws FHIRPersistenceException {
        final String METHODNAME = "processMissingParm";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());

        // boolean to track whether the user has requested the resources missing this parameter (true) or not missing it
        // (false)
        Boolean missing = null;
        for (QueryParameterValue parameterValue : queryParm.getValues()) {
            if (missing == null) {
                missing = Boolean.parseBoolean(parameterValue.getValueCode());
            } else {
                // multiple values would be very unusual, but I suppose we should handle it like an "or"
                if (missing != Boolean.parseBoolean(parameterValue.getValueCode())) {
                    // user has requested both missing and not missing values for this field which makes no sense
                    log.warning("Processing query with conflicting values for query param with 'missing' modifier");
                    // TODO: What does returning null do here? We should handle this better.
                    return null;
                }
            }
        }

        StringBuilder whereClauseSegment = new StringBuilder();
        if (!endOfChain) {
            whereClauseSegment.append(WHERE);
        }

        // Build this piece of the segment. Use EXISTS instead of SELECT DISTINCT for much better performance
        // missing:     NOT EXISTS (SELECT 1 FROM pTABLE_NAME_PLACEHOLDER AS pX WHERE pX.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID)
        // not missing:     EXISTS (SELECT 1 FROM pTABLE_NAME_PLACEHOLDER AS pX WHERE pX.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID)
        if (missing == null || missing) {
            whereClauseSegment.append(NOT);
        }
        whereClauseSegment.append(EXISTS);

        List<Object> bindVariables = new ArrayList<>();
        if (Type.COMPOSITE.equals(queryParm.getType())) {
            SqlQueryData compositeQueryData = populateCompositeSelectSubSegment(queryParm, resourceType, paramTableAlias, logicalRsrcTableAlias);
            whereClauseSegment.append(compositeQueryData.getQueryString());
            bindVariables.addAll(compositeQueryData.getBindVariables());
        } else {
            // PARAMETER_TABLE_NAME_PLACEHOLDER is replaced by the actual table name for the resource type by QuerySegmentAggregator.buildWhereClause(...)
            String valuesTable = !ModelSupport.isAbstract(resourceType) ? QuerySegmentAggregator.tableName(resourceType.getSimpleName(), queryParm) : PARAMETER_TABLE_NAME_PLACEHOLDER;
            String subqueryTableAlias =  endOfChain ? (paramTableAlias + "_param0") : paramTableAlias;
            whereClauseSegment.append("(SELECT 1 FROM " + valuesTable + AS + subqueryTableAlias + WHERE);
            this.populateNameIdSubSegment(whereClauseSegment, queryParm.getCode(), subqueryTableAlias);
            whereClauseSegment.append(AND).append(subqueryTableAlias).append(".LOGICAL_RESOURCE_ID = ").append(logicalRsrcTableAlias).append(".LOGICAL_RESOURCE_ID"); // correlate the [NOT] EXISTS subquery
            whereClauseSegment.append(RIGHT_PAREN).append(RIGHT_PAREN);
        }

        SqlQueryData queryData = new SqlQueryData(whereClauseSegment.toString(), bindVariables);
        log.exiting(CLASSNAME, METHODNAME);
        return queryData;
    }

    /**
     * Contains special logic for handling reverse chained reference search parameters.
     * <p>
     * A select statement is built to realize the reverse chaining logic required. Here is a sample
     * reverse chained query for a Patient given this search parameter: _has:Observation:patient:code=1234
     *
     * <pre>
     * SELECT
     *   CLR0.LOGICAL_ID
     * FROM
     *   Patient_LOGICAL_RESOURCES AS CLR0
     *   JOIN Patient_RESOURCES AS CR0 ON CR0.RESOURCE_ID = CLR0.CURRENT_RESOURCE_ID AND CR0.IS_DELETED = 'N'
     * WHERE
     *   EXISTS (
     *     SELECT
     *       1
     *     FROM
     *       Observation_TOKEN_VALUES_V AS CP1
     *       JOIN Observation_LOGICAL_RESOURCES AS CLR1 ON CLR1.LOGICAL_RESOURCE_ID = CP1.LOGICAL_RESOURCE_ID
     *       JOIN Observation_RESOURCES AS CR1 ON CR1.RESOURCE_ID = CLR1.CURRENT_RESOURCE_ID AND CR1.IS_DELETED = 'N'
     *       JOIN Observation_TOKEN_VALUES_V AS CP2 ON CP2.LOGICAL_RESOURCE_ID = CLR1.LOGICAL_RESOURCE_ID
     *       AND (
     *         CP2.PARAMETER_NAME_ID = 1073
     *         AND ((CP2.TOKEN_VALUE = ?))
     *       )
     *     WHERE
     *       COALESCE(CP1.REF_VERSION_ID, CR0.VERSION_ID) = CR0.VERSION_ID
     *       AND CP1.TOKEN_VALUE = CLR0.LOGICAL_ID
     *       AND CP1.PARAMETER_NAME_ID = 1274
     *       AND CP1.CODE_SYSTEM_ID = 20004
     *   )
     * </pre>
     *
     * @see https://www.hl7.org/fhir/search.html#has
     * @param resourceType
     *                  - The resource type being searched.
     * @param queryParm
     *                  - A Parameter representing a reverse chained query.
     * @return SqlQueryData
     *                  - The query segment for a reverse chained parameter reference search.
     * @throws Exception
     */
    @Override
    protected SqlQueryData processReverseChainedReferenceParm(Class<?> resourceType, QueryParameter queryParm) throws Exception {
        final String METHODNAME = "processReverseChainedReferenceParm";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());

        String prevChainedResourceVar = null;
        String prevChainedLogicalResourceVar = null;
        String chainedResourceVar = null;
        String chainedLogicalResourceVar = null;
        String chainedParmVar = null;
        String nextChainedResourceVar = null;
        String nextChainedLogicalResourceVar = null;
        String nextChainedParmVar = null;
        QueryParameter previousParm = null;;
        int parmIndex = 0;
        int lastParmIndex = queryParm.getChain().size();
        boolean chainedParmProcessed = false;
        StringBuilder selectSegments = new StringBuilder();
        StringBuilder whereClauseSegments = new StringBuilder();
        List<Object> bindVariables = new ArrayList<>();

        // Loop through the chained query parameters in order
        List<QueryParameter> queryParms = queryParm.getChain();
        queryParms.add(0, queryParm);
        for (QueryParameter currentParm : queryParms) {

            prevChainedResourceVar        = CR + parmIndex;
            prevChainedLogicalResourceVar = CLR + parmIndex;
            chainedResourceVar        = CR + (parmIndex + 1);
            chainedLogicalResourceVar = CLR + (parmIndex + 1);
            chainedParmVar            = CP + (parmIndex + 1);
            nextChainedResourceVar        = CR + (parmIndex + 2);
            nextChainedLogicalResourceVar = CLR + (parmIndex + 2);
            nextChainedParmVar        = CP + (parmIndex + 2);
            StringBuilder whereClauseSegment = new StringBuilder();

            if (parmIndex == 0) {
                // Build outer select:
                // @formatter:off
                //   SELECT CLR0.LOGICAL_ID
                //     FROM <resource-type>_LOGICAL_RESOURCES AS CLR0
                //     JOIN <resource-type>_RESOURCES AS CR0
                //       ON CR0.RESOURCE_ID = CLR0.CURRENT_RESOURCE_ID AND CR0.IS_DELETED = 'N'
                //     WHERE
                // @formatter:on
                selectSegments.append(SELECT).append(prevChainedLogicalResourceVar).append(DOT).append(LOGICAL_ID)
                                .append(FROM).append(resourceType.getSimpleName()).append(_LOGICAL_RESOURCES).append(AS).append(prevChainedLogicalResourceVar)
                                .append(JOIN).append(resourceType.getSimpleName()).append(_RESOURCES).append(AS).append(prevChainedResourceVar)
                                .append(ON).append(prevChainedResourceVar).append(DOT).append(RESOURCE_ID).append(EQ)
                                .append(prevChainedLogicalResourceVar).append(DOT).append(CURRENT_RESOURCE_ID)
                                .append(AND).append(prevChainedResourceVar).append(DOT ).append(IS_DELETED_NO)
                                .append(WHERE);
            }

            if (parmIndex < lastParmIndex) {
                if (currentParm.isReverseChained()) {
                    // Build inner select joins:
                    // @formatter:off
                    //   EXISTS (SELECT 1
                    //     FROM <modifierTypeResourceName>_TOKEN_VALUES_V AS CPx
                    //     JOIN <modifierTypeResourceName>_LOGICAL_RESOURCES AS CLRx
                    //       ON CLRx.LOGICAL_RESOURCE_ID = CPx.LOGICAL_RESOURCE_ID
                    //     JOIN <modifierTypeResourceName>_RESOURCES AS CRx
                    //       ON CRx.RESOURCE_ID = CLRx.CURRENT_RESOURCE_ID AND CRx.IS_DELETED = 'N'
                    // @formatter:on
                    selectSegments.append(EXISTS).append(LEFT_PAREN).append("SELECT 1")
                                    .append(FROM).append(currentParm.getModifierResourceTypeName()).append("_TOKEN_VALUES_V").append(AS).append(chainedParmVar)
                                    .append(JOIN).append(currentParm.getModifierResourceTypeName()).append(_LOGICAL_RESOURCES).append(AS).append(chainedLogicalResourceVar)
                                    .append(ON).append(chainedLogicalResourceVar).append(DOT).append(LOGICAL_RESOURCE_ID).append(EQ)
                                    .append(chainedParmVar).append(DOT).append(LOGICAL_RESOURCE_ID)
                                    .append(JOIN).append(currentParm.getModifierResourceTypeName()).append(_RESOURCES).append(AS).append(chainedResourceVar)
                                    .append(ON).append(chainedResourceVar).append(DOT).append(RESOURCE_ID).append(EQ)
                                    .append(chainedLogicalResourceVar).append(DOT).append(CURRENT_RESOURCE_ID)
                                    .append(AND).append(chainedResourceVar).append(DOT).append(IS_DELETED_NO);

                    String referencedResourceType = null;
                    if (parmIndex == 0) {
                        referencedResourceType = resourceType.getSimpleName();
                    } else {
                        referencedResourceType = previousParm.getModifierResourceTypeName();
                    }
                    if (parmIndex < lastParmIndex - 1 && currentParm.getNextParameter().isReverseChained()) {
                        // Build inner select where clause:
                        // @formatter:off
                        //   WHERE
                        //     COALESCE(CPx.REF_VERSION_ID, CR<x-1>.VERSION_ID) = CR<x-1>.VERSION_ID
                        //     AND CPx.TOKEN_VALUE = CLR<x-1>.LOGICAL_ID
                        //     AND CPx.PARAMETER_NAME_ID = <parm-name-id>
                        //     AND CPx.CODE_SYSTEM_ID = <code-system-id>
                        //     AND
                        // @formatter:on
                        selectSegments.append(WHERE).append("COALESCE(").append(chainedParmVar).append(DOT).append("REF_VERSION_ID, ")
                                        .append(prevChainedResourceVar).append(DOT).append("VERSION_ID)").append(EQ)
                                        .append(prevChainedResourceVar).append(DOT).append("VERSION_ID").append(AND)
                                        .append(chainedParmVar).append(DOT).append(TOKEN_VALUE).append(EQ)
                                        .append(prevChainedLogicalResourceVar).append(DOT).append(LOGICAL_ID).append(AND);
                        populateReferenceNameAndCodeSystemIdSubSegment(selectSegments, currentParm.getCode(), referencedResourceType, chainedParmVar);
                        selectSegments.append(AND);
                    } else {
                        // Build final inner select where clause:
                        // @formatter:off
                        //   WHERE
                        //     COALESCE(CPx.REF_VERSION_ID, CR<x-1>.VERSION_ID) = CR<x-1>.VERSION_ID
                        //     AND CPx.TOKEN_VALUE = CLR<x-1>.LOGICAL_ID
                        //     AND CPx.PARAMETER_NAME_ID = <parm-name-id>
                        //     AND CPx.CODE_SYSTEM_ID = <code-system-id>
                        // @formatter:on
                        whereClauseSegment.append(WHERE).append("COALESCE(").append(chainedParmVar).append(DOT).append("REF_VERSION_ID, ")
                                            .append(prevChainedResourceVar).append(DOT).append("VERSION_ID)").append(EQ)
                                            .append(prevChainedResourceVar).append(DOT).append("VERSION_ID").append(AND)
                                            .append(chainedParmVar).append(DOT).append(TOKEN_VALUE).append(EQ)
                                            .append(prevChainedLogicalResourceVar).append(DOT).append(LOGICAL_ID).append(AND);
                        populateReferenceNameAndCodeSystemIdSubSegment(whereClauseSegment, currentParm.getCode(), referencedResourceType, chainedParmVar);
                    }

                    // Add closing right paren for EXISTS
                    whereClauseSegment.append(RIGHT_PAREN);
                } else if (currentParm.isChained()) {
                    // Build chained query
                    if (!chainedParmProcessed) {
                        // Build initial chain join and select:
                        //   SELECT CPx.LOGICAL_RESOURCE_ID FROM <modifierTypeResourceName>_TOKEN_VALUES_V AS CPx WHERE
                        selectSegments.append(JOIN ).append(LEFT_PAREN)
                                        .append(SELECT).append(chainedParmVar).append(DOT).append(LOGICAL_RESOURCE_ID).append(FROM)
                                        .append(previousParm.getModifierResourceTypeName()).append("_TOKEN_VALUES_V AS ").append(chainedParmVar)
                                        .append(WHERE);
                    }

                    // Build this piece: CPx.PARAMETER_NAME_ID = <code-id> AND CPx.STR_VALUE IN
                    appendMidChainParm(selectSegments, currentParm, chainedParmVar);

                    // Build this piece: (SELECT 'resource-type-name' || '/' || CLR<x+1>.LOGICAL_ID ...
                    selectSegments.append(LEFT_PAREN);
                    appendInnerSelect(selectSegments, currentParm, currentParm.getModifierResourceTypeName(),
                        nextChainedResourceVar, nextChainedLogicalResourceVar, nextChainedParmVar);
                    whereClauseSegment.append(RIGHT_PAREN);

                    if (!chainedParmProcessed) {
                        chainedParmProcessed = true;

                        // Builds ON clause for join: ) AS CPx ON CPx.LOGICAL_RESOURCE_ID = CLR<x-1>.LOGICAL_RESOURCE_ID
                        whereClauseSegment.append(RIGHT_PAREN).append(AS).append(chainedParmVar).append(ON)
                                            .append(chainedParmVar).append(DOT).append(LOGICAL_RESOURCE_ID).append(EQ)
                                            .append(prevChainedLogicalResourceVar).append(DOT).append(LOGICAL_RESOURCE_ID);
                    }
                }
            } else if (parmIndex == lastParmIndex) {
                // This logic processes the LAST parameter in the chain.
                SqlQueryData sqlQueryData;
                if ("_id".equals(currentParm.getCode())) {
                    if (!chainedParmProcessed) {
                        // Build this join:
                        // @formatter:off
                        //   JOIN <modifierTypeResourceName>_LOGICAL_RESOURCES AS CLRx
                        //     ON CLRx.LOGICAL_RESOURCE_ID = CLR<x-1>.LOGICAL_RESOURCE_ID
                        //     AND
                        // @formatter:on
                        whereClauseSegment.append(JOIN).append(previousParm.getModifierResourceTypeName()).append(_LOGICAL_RESOURCES)
                                            .append(AS).append(chainedLogicalResourceVar).append(ON)
                                            .append(chainedLogicalResourceVar).append(DOT).append(LOGICAL_RESOURCE_ID)
                                            .append(EQ).append(prevChainedLogicalResourceVar).append(DOT).append(LOGICAL_RESOURCE_ID).append(AND);
                    }
                    // Build the rest: CLRx.LOGICAL_ID IN (?)
                    sqlQueryData = buildChainedIdClause(currentParm, chainedParmVar);
                } else if ("_lastUpdated".equals(currentParm.getCode())) {
                    if (!chainedParmProcessed) {
                        // Build this join:
                        // @formatter:off
                        //   JOIN <modifierTypeResourceName>_RESOURCES AS CRx
                        //     ON CRx.LOGICAL_RESOURCE_ID = CLR<x-1>.LOGICAL_RESOURCE_ID
                        //     AND
                        // @formatter:on
                        whereClauseSegment.append(JOIN).append(previousParm.getModifierResourceTypeName()).append(_RESOURCES)
                                            .append(AS).append(chainedResourceVar).append(ON)
                                            .append(chainedResourceVar).append(DOT).append(LOGICAL_RESOURCE_ID)
                                            .append(EQ).append(prevChainedLogicalResourceVar).append(DOT).append(LOGICAL_RESOURCE_ID).append(AND);
                    }
                    // Build the rest: (LAST_UPDATED <operator> ?)
                    LastUpdatedParmBehaviorUtil util = new LastUpdatedParmBehaviorUtil();
                    StringBuilder lastUpdatedWhereClause = new StringBuilder();
                    util.executeBehavior(lastUpdatedWhereClause, currentParm);
                    sqlQueryData = new SqlQueryData(lastUpdatedWhereClause.toString()
                        .replaceAll(LastUpdatedParmBehaviorUtil.LAST_UPDATED_COLUMN_NAME,
                            chainedResourceVar + DOT + LastUpdatedParmBehaviorUtil.LAST_UPDATED_COLUMN_NAME),
                        util.getBindVariables());
                } else {
                    if (!chainedParmProcessed && !Type.COMPOSITE.equals(currentParm.getType())) {
                        // Build this join:
                        // @formatter:off
                        //   JOIN <modifierTypeResourceName>_<type>_VALUES AS CPx
                        //     ON CPx.LOGICAL_RESOURCE_ID = CLR<x-1>.LOGICAL_RESOURCE_ID
                        //     AND
                        // @formatter:on
                        whereClauseSegment.append(JOIN).append(QuerySegmentAggregator.tableName(previousParm.getModifierResourceTypeName(), currentParm))
                                            .append(AS).append(chainedParmVar).append(ON).append(chainedParmVar).append(DOT).append(LOGICAL_RESOURCE_ID)
                                            .append(EQ).append(prevChainedLogicalResourceVar).append(DOT).append(LOGICAL_RESOURCE_ID).append(AND);
                    }
                    // Build the rest: (CPx.PARAMETER_NAME_ID=<code-id> AND (CPx.<type>_VALUE=<valueCode>))
                    sqlQueryData = buildQueryParm(ModelSupport.getResourceType(previousParm.getModifierResourceTypeName()), currentParm, chainedParmVar, prevChainedLogicalResourceVar, true);
                }

                if (log.isLoggable(Level.FINE)) {
                    log.fine("chained sqlQueryData[" + chainedParmVar + "] = " + sqlQueryData.getQueryString());
                }
                whereClauseSegment.append(sqlQueryData.getQueryString());
                bindVariables.addAll(sqlQueryData.getBindVariables());
           }

            // Insert where clause segment in whole
            whereClauseSegments.insert(0, whereClauseSegment.toString());

            previousParm = currentParm;
            parmIndex++;
        }

        SqlQueryData queryData = new SqlQueryData(whereClauseSegments.insert(0, selectSegments.toString()).toString(), bindVariables);
        log.exiting(CLASSNAME, METHODNAME, queryData.getQueryString());
        return queryData;
    }

    /**
     * Populates the reference parameter name ID and code system ID sub-segment of the passed where clause segment.
     *
     * @param whereClauseSegment - the segment to which the sub-segment will be added
     * @param queryParmName - the search parameter name
     * @param resourceTypeName - the resource type of the reference being followed
     * @param parameterTableAlias - the alias for the parameter table e.g. CPx
     * @throws FHIRPersistenceException
     */
    private void populateReferenceNameAndCodeSystemIdSubSegment(StringBuilder whereClauseSegment, String queryParmName,
            String resourceTypeName, String parameterTableAlias) throws FHIRPersistenceException {
        final String METHODNAME = "populateReferenceNameAndCodeSystemIdSubSegment";
        log.entering(CLASSNAME, METHODNAME, queryParmName);

        Integer parameterNameId = identityCache.getParameterNameId(queryParmName);
        Integer codeSystemId = getCodeSystemId(resourceTypeName);

        // Build the segment:
        // CPx.PARAMETER_NAME_ID = <parameter-name-id> AND CPx.CODE_SYSTEM_ID = <code-system_id>
        whereClauseSegment.append(parameterTableAlias).append(DOT).append(PARAMETER_NAME_ID).append(EQ).append(nullCheck(parameterNameId))
                            .append(AND).append(parameterTableAlias).append(DOT).append(CODE_SYSTEM_ID).append(EQ).append(nullCheck(codeSystemId));

        log.exiting(CLASSNAME, METHODNAME);
    }

    /**
     * Populates the composite select statement(s) for OR'd or :missing composite searches.
     *
     * @param queryParm - the composite query parameter
     * @param resourceType - the search resource type
     * @param parameterTableAlias - the alias for the parameter table e.g. CPx
     * @param logicalResourceTableAlias - the alias for the logical resource table e.g. LR
     * @return SqlQueryData the populated composite select statement(s)
     * @throws FHIRPersistenceException
     */
    private SqlQueryData populateCompositeSelectSubSegment(QueryParameter queryParm, Class<?> resourceType, String paramTableAlias,
        String logicalResourceTableAlias) throws FHIRPersistenceException {
        final String METHODNAME = "populateCompositeSelectSubSegment";
        log.entering(CLASSNAME, METHODNAME, queryParm.getCode());

        SqlQueryData queryData;
        List<Object> bindVariables = new ArrayList<>();
        StringBuilder whereClauseSegment = new StringBuilder();
        boolean missing = Modifier.MISSING.equals(queryParm.getModifier());
        String valueSeparator = "";

        try {
            for (QueryParameterValue compositeValue : queryParm.getValues()) {
                whereClauseSegment.append(valueSeparator).append("(SELECT 1 FROM ");
                List<QueryParameter> components = compositeValue.getComponent();

                // Loop through components to build FROM clause and WHERE clause
                StringBuilder fromClauseSegment = new StringBuilder();
                StringBuilder innerWhereClauseSegment = new StringBuilder();
                for (int componentNum = 1; componentNum <= components.size(); componentNum++) {
                    QueryParameter component = components.get(componentNum - 1);
                    String valuesTable = QuerySegmentAggregator.tableName(resourceType.getSimpleName(), component);
                    String componentTableAlias = paramTableAlias + "_p" + componentNum;

                    // Build table in FROM clause for this component:
                    // Observation_STR_VALUES param1
                    if (componentNum > 1) {
                        fromClauseSegment.append(COMMA);
                    }
                    fromClauseSegment.append(valuesTable).append(" ").append(componentTableAlias);

                    // Build WHERE clause predicates for this component
                    if (componentNum > 1) {
                        innerWhereClauseSegment.append(AND);
                    }
                    innerWhereClauseSegment.append(componentTableAlias).append(".LOGICAL_RESOURCE_ID = ")
                                            .append(logicalResourceTableAlias).append(".LOGICAL_RESOURCE_ID").append(AND);
                    if (missing) {
                        this.populateNameIdSubSegment(innerWhereClauseSegment, component.getCode(), componentTableAlias);
                        innerWhereClauseSegment.append(RIGHT_PAREN);
                    } else {
                        SqlQueryData subQueryData = buildQueryParm(resourceType, component, componentTableAlias, logicalResourceTableAlias, false);
                        innerWhereClauseSegment.append(subQueryData.getQueryString());
                        bindVariables.addAll(subQueryData.getBindVariables());
                    }
                    if (componentNum > 1) {
                        innerWhereClauseSegment.append(AND).append(componentTableAlias).append(".COMPOSITE_ID = " + paramTableAlias + "_p1.COMPOSITE_ID");
                    }
                }
                whereClauseSegment.append(fromClauseSegment.toString()).append(WHERE).append(innerWhereClauseSegment.toString()).append(RIGHT_PAREN);
                valueSeparator = " OR EXISTS ";
            }
        } catch (Exception e) {
            throw new FHIRPersistenceException("Error while creating subquery for parameter '" + queryParm.getCode() + "'", e);
        }

        queryData = new SqlQueryData(whereClauseSegment.toString(), bindVariables);
        log.exiting(CLASSNAME, METHODNAME);
        return queryData;
    }

}
