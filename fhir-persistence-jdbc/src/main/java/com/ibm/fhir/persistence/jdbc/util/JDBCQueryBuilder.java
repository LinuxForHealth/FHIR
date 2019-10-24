/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util;

import static com.ibm.fhir.persistence.jdbc.JDBCConstants.AND;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.BIND_VAR;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.CODE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.CODE_SYSTEM_ID;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.DATE_END;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.DATE_START;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.DATE_VALUE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.DOT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.ESCAPE_EXPR;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.ESCAPE_PERCENT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.ESCAPE_UNDERSCORE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LATITUDE_VALUE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LEFT_PAREN;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LONGITUDE_VALUE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.NUMBER_VALUE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.PARAMETERS_TABLE_ALIAS;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.PERCENT_WILDCARD;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.QUANTITY_VALUE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.QUANTITY_VALUE_HIGH;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.QUANTITY_VALUE_LOW;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.RIGHT_PAREN;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.STR_VALUE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.STR_VALUE_LCASE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.TOKEN_VALUE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.UNDERSCORE_WILDCARD;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.WHERE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.modifierMap;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.prefixOperatorMap;
import static com.ibm.fhir.persistence.jdbc.util.QuerySegmentAggregator.PARAMETER_TABLE_ALIAS;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.model.resource.Location;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceNotSupportedException;
import com.ibm.fhir.persistence.jdbc.JDBCConstants.JDBCOperator;
import com.ibm.fhir.persistence.jdbc.dao.api.ParameterDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.ResourceDAO;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;
import com.ibm.fhir.persistence.util.AbstractQueryBuilder;
import com.ibm.fhir.persistence.util.BoundingBox;
import com.ibm.fhir.search.SearchConstants.Modifier;
import com.ibm.fhir.search.SearchConstants.Prefix;
import com.ibm.fhir.search.SearchConstants.Type;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.parameters.Parameter;
import com.ibm.fhir.search.parameters.ParameterValue;
import com.ibm.fhir.search.util.SearchUtil;
import com.ibm.fhir.search.valuetypes.ValueTypesFactory;

/**
 * This is the JDBC implementation of a query builder for the IBM FHIR Server JDBC persistence layer schema.
 * Queries are built in SQL.
 * <br><br>
 * For the new R4 schema, the search parameter tables (e.g. <resourceType>_STR_VALUES) are
 * joined to their corresponding <resourceType>_LOGICAL_RESOURCES tables on LOGICAL_RESOURCE_ID.
 * This is because the search parameters are not versioned, and are associated with
 * the logical resource, not the resource version.
 * <br>
 * Useful column reference:<br>
 * <code>
 * ------------------------
 * RESOURCE_TYPE_NAME    the formal name of the resource type e.g. 'Patient'
 * RESOURCE_TYPE_ID      FK to the RESOURCE_TYPES table
 * LOGICAL_ID            the VARCHAR holding the logical-id of the resource. Unique for a given resource type
 * LOGICAL_RESOURCE_ID   the database BIGINT
 * CURRENT_RESOURCE_ID   the unique BIGINT id of the latest resource version for the logical resource
 * VERSION_ID            INT resource version number incrementing by 1
 * RESOURCE_ID           the PK of the version-specific resource. Now only used as the target for CURRENT_RESOURCE_ID
 * </code>
 */
public class JDBCQueryBuilder extends AbstractQueryBuilder<SqlQueryData, JDBCOperator> {
    private static final Logger log = java.util.logging.Logger.getLogger(JDBCQueryBuilder.class.getName());
    private static final String CLASSNAME = JDBCQueryBuilder.class.getName();

    private ParameterDAO parameterDao;
    private ResourceDAO resourceDao;

    public static final boolean isIntegerSearch(Class<?> resourceType, Parameter queryParm) throws Exception {
        return ValueTypesFactory.getValueTypesProcessor().isIntegerSearch(resourceType, queryParm);
    }

    public static final boolean isRangeSearch(Class<?> resourceType, Parameter queryParm) throws Exception {
        return ValueTypesFactory.getValueTypesProcessor().isRangeSearch(resourceType, queryParm);
    }

    public static final boolean isDateSearch(Class<?> resourceType, Parameter queryParm) throws Exception {
        return ValueTypesFactory.getValueTypesProcessor().isDateSearch(resourceType, queryParm);
    }

    public static final boolean isDateRangeSearch(Class<?> resourceType, Parameter queryParm) throws Exception {
        return ValueTypesFactory.getValueTypesProcessor().isDateRangeSearch(resourceType, queryParm);
    }

    public JDBCQueryBuilder(ParameterDAO parameterDao, ResourceDAO resourceDao) {
        this.parameterDao = parameterDao;
        this.resourceDao = resourceDao;
    }

    /**
     * Builds a query that returns the count of the search results that would be found by applying the search parameters
     * contained within the passed search context.
     * 
     * @param resourceType
     *            - The type of resource being searched for.
     * @param searchContext
     *            - The search context containing the search parameters.
     * @return String - A count query SQL string
     * @throws Exception
     */
    public SqlQueryData buildCountQuery(Class<?> resourceType, FHIRSearchContext searchContext) throws Exception {
        final String METHODNAME = "buildCountQuery";
        log.entering(CLASSNAME, METHODNAME, new Object[] { resourceType.getSimpleName(), searchContext.getSearchParameters() });

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
        log.entering(CLASSNAME, METHODNAME, new Object[] { resourceType.getSimpleName(), searchContext.getSearchParameters() });

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
     * Contains logic common to the building of both 'count' resource queries and 'regular' resource queries.
     * 
     * @param resourceType
     *            The type of FHIR resource being searched for.
     * @param searchContext
     *            The search context containing search parameters.
     * @return QuerySegmentAggregator - A query builder helper containing processed query segments.
     * @throws Exception
     */
    private QuerySegmentAggregator buildQueryCommon(Class<?> resourceType, FHIRSearchContext searchContext) throws Exception {
        final String METHODNAME = "buildQueryCommon";
        log.entering(CLASSNAME, METHODNAME, new Object[] { resourceType.getSimpleName(), searchContext.getSearchParameters() });

        SqlQueryData querySegment;
        int nearParameterIndex;
        List<Parameter> searchParameters = searchContext.getSearchParameters();
        int pageSize = searchContext.getPageSize();
        int offset = (searchContext.getPageNumber() - 1) * pageSize;
        QuerySegmentAggregator helper;
        boolean isValidQuery = true;

        helper = QuerySegmentAggregatorFactory.buildQuerySegmentAggregator(resourceType, offset, pageSize, this.parameterDao, this.resourceDao, searchContext);

        // Special logic for handling LocationPosition queries. These queries have interdependencies between
        // a couple of related input query parameters
        if (Location.class.equals(resourceType)) {
            querySegment = this.processLocationPosition(searchParameters);
            if (querySegment != null) {
                nearParameterIndex = this.findNearParameterIndex(searchParameters);
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
        for (Parameter queryParameter : searchParameters) {
            querySegment = this.buildQueryParm(resourceType, queryParameter, PARAMETER_TABLE_ALIAS);
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

    @Override
    protected JDBCOperator getOperator(Parameter queryParm) {
        final String METHODNAME = "getOperator(Parameter)";
        log.entering(CLASSNAME, METHODNAME, queryParm.getModifier());

        JDBCOperator operator = null;
        Modifier modifier = queryParm.getModifier();

        if (modifier != null) {
            operator = modifierMap.get(modifier);
        }
        if (operator == null) {
            operator = JDBCOperator.LIKE;
        }
        log.exiting(CLASSNAME, METHODNAME, operator.value());
        return operator;
    }

    /**
     * Map the Modifier in the passed Parameter to a supported query operator. If the mapping results in the default
     * operator, override the default operator with the passed operator if the passed operator is not null.
     * 
     * @param queryParm
     *            - A valid query Parameter.
     * @param defaultOverride
     *            - An operator that should override the default operator.
     * @return T2 - A supported operator.
     */
    @Override
    protected JDBCOperator getOperator(Parameter queryParm, JDBCOperator defaultOverride) {
        final String METHODNAME = "getOperator(Parameter, JDBCOperator)";
        log.entering(CLASSNAME, METHODNAME, queryParm.getModifier());

        JDBCOperator operator = null;
        Modifier modifier = queryParm.getModifier();

        if (modifier != null) {
            operator = modifierMap.get(modifier);
        }
        if (operator == null) {
            if (defaultOverride != null) {
                operator = defaultOverride;
            } else {
                operator = JDBCOperator.LIKE;
            }
        }
        log.exiting(CLASSNAME, METHODNAME, operator.value());
        return operator;
    }

    @Override
    protected JDBCOperator getPrefixOperator(ParameterValue queryParmValue) {
        final String METHODNAME = "getOperator(ParameterValue)";
        log.entering(CLASSNAME, METHODNAME, queryParmValue.getPrefix());

        Prefix prefix = queryParmValue.getPrefix();
        JDBCOperator operator = null;

        if (prefix != null) {
            operator = prefixOperatorMap.get(prefix);
        }
        if (operator == null) {
            operator = JDBCOperator.EQ;
        }

        log.exiting(CLASSNAME, METHODNAME, operator.value());
        return operator;
    }

    @Override
    protected SqlQueryData processStringParm(Parameter queryParm) throws FHIRPersistenceException {
        return processStringParm(queryParm, PARAMETERS_TABLE_ALIAS);
    }
    
    private SqlQueryData processStringParm(Parameter queryParm, String tableAlias) throws FHIRPersistenceException {
        final String METHODNAME = "processStringParm";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());

        StringBuilder whereClauseSegment = new StringBuilder();
        JDBCOperator operator = this.getOperator(queryParm);
        boolean parmValueProcessed = false;
        String searchValue, tempSearchValue;
        boolean appendEscape;
        SqlQueryData queryData;
        List<Object> bindVariables = new ArrayList<>();

        // Build this piece of the segment:
        // (P1.PARAMETER_NAME_ID = x AND
        this.populateNameIdSubSegment(whereClauseSegment, queryParm.getName(), tableAlias);

        whereClauseSegment.append(AND).append(LEFT_PAREN);
        for (ParameterValue value : queryParm.getValues()) {
            appendEscape = false;
            if (operator.equals(JDBCOperator.LIKE)) {
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
                }
                appendEscape = true;
            } else {
                searchValue = SqlParameterEncoder.encode(value.getValueString());
            }

            // If multiple values are present, we need to OR them together.
            if (parmValueProcessed) {
                whereClauseSegment.append(JDBCOperator.OR.value());
            }

            if (operator.equals(JDBCOperator.EQ) || Type.URI.equals(queryParm.getType())) {
                // For an exact match, we search against the STR_VALUE column in the Resource's string values table.
                // Build this piece: pX.str_value = search-attribute-value
                whereClauseSegment.append(tableAlias + DOT).append(STR_VALUE).append(operator.value()).append(BIND_VAR);
            } else {
                // For anything other than an exact match, we search against the STR_VALUE_LCASE column in the
                // Resource's string values table.
                // Also, the search value is "normalized"; it has accents removed and is lower-cased. This enables a
                // case-insensitive, accent-insensitive search.
                // Build this piece: pX.str_value_lcase {operator} search-attribute-value
                whereClauseSegment.append(tableAlias + DOT).append(STR_VALUE_LCASE).append(operator.value()).append(BIND_VAR);
                searchValue = SearchUtil.normalizeForSearch(searchValue);
            }
            bindVariables.add(searchValue);
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
    protected SqlQueryData processReferenceParm(Class<?> resourceType, Parameter queryParm) throws Exception {
        return processReferenceParm(resourceType, queryParm, PARAMETERS_TABLE_ALIAS);
    }
    
    private SqlQueryData processReferenceParm(Class<?> resourceType, Parameter queryParm, String tableAlias) throws Exception {
        final String METHODNAME = "processReferenceParm";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());

        StringBuilder whereClauseSegment = new StringBuilder();
        JDBCOperator operator = this.getOperator(queryParm, JDBCOperator.EQ);
        boolean parmValueProcessed = false;
        String searchValue;
        SqlQueryData queryData;
        List<Object> bindVariables = new ArrayList<>();

        // Build this piece of the segment:
        // (P1.PARAMETER_NAME_ID = x AND
        this.populateNameIdSubSegment(whereClauseSegment, queryParm.getName(), tableAlias);

        whereClauseSegment.append(AND).append(LEFT_PAREN);
        for (ParameterValue value : queryParm.getValues()) {
            // Handle query parm representing this name/value pair construct:
            // {name} = {resource-type/resource-id}
            searchValue = SqlParameterEncoder.encode(value.getValueString());

            // Handle query parm representing this name/value pair construct:
            // {name}:{Resource Type} = {resource-id}
            if (queryParm.getModifier() != null && queryParm.getModifier().equals(Modifier.TYPE)) {
                searchValue = queryParm.getModifierResourceTypeName() + "/" + SqlParameterEncoder.encode(value.getValueString());
            } else if (!isAbsoluteURL(searchValue)) {
                SearchParameter definition = SearchUtil.getSearchParameter(resourceType, queryParm.getName());
                if (definition != null) {
                    List<? extends Code> targets = definition.getTarget();
                    if (targets.size() == 1) {
                        Code target = targets.get(0);
                        String targetResourceTypeName = target.getValue();
                        if (!searchValue.startsWith(targetResourceTypeName + "/")) {
                            searchValue = targetResourceTypeName + "/" + searchValue;
                        }
                    }
                } else {
                    log.finer("Couldn't find search parameter named " + queryParm.getName() + " for resource of type " + resourceType);
                }
            }

            // If multiple values are present, we need to OR them together.
            if (parmValueProcessed) {
                whereClauseSegment.append(JDBCOperator.OR.value());
            }
            // Build this piece: pX.str_value {operator} search-attribute-value
            whereClauseSegment.append(tableAlias + DOT).append(STR_VALUE).append(operator.value()).append(BIND_VAR);
            bindVariables.add(searchValue);
            parmValueProcessed = true;
        }
        whereClauseSegment.append(RIGHT_PAREN).append(RIGHT_PAREN);

        queryData = new SqlQueryData(whereClauseSegment.toString(), bindVariables);
        log.exiting(CLASSNAME, METHODNAME);
        return queryData;
    }

    /**
     * Contains special logic for handling chained reference search parameters.
     * 
     * @see https://www.hl7.org/fhir/search.html#reference (section 2.1.1.4.13) Nested sub-selects are built to realize
     *      the chaining logic required. Here is a sample chained query for an Observation given this search parameter:
     *      device:Device.patient.family=Monella
     *
     *      SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID, R.LAST_UPDATED, R.IS_DELETED, R.DATA,
     *      LR.LOGICAL_ID FROM Observation_RESOURCES R, Observation_LOGICAL_RESOURCES LR , Observation_STR_VALUES P1
     *      WHERE R.RESOURCE_ID = LR.CURRENT_RESOURCE_ID AND R.IS_DELETED <> 'Y' AND P1.LOGICAL_RESOURCE_ID = R.LOGICAL_RESOURCE_ID AND
     *      P1.PARAMETER_NAME_ID = 107 AND (p1.STR_VALUE IN (SELECT 'Device' || '/' || CLR1.LOGICAL_ID FROM
     *      Device_RESOURCES CR1, Device_LOGICAL_RESOURCES CLR1, Device_STR_VALUES CP1 WHERE CR1.RESOURCE_ID =
     *      CLR1.CURRENT_RESOURCE_ID AND CR1.IS_DELETED <> 'Y' AND CP1.LOGICAL_RESOURCE_ID = CR1.LOGICAL_RESOURCE_ID AND
     *      CP1.PARAMETER_NAME_ID = 17 AND CP1.STR_VALUE IN (SELECT 'Patient' || '/' || CLR2.LOGICAL_ID FROM
     *      Patient_RESOURCES CR2, Patient_LOGICAL_RESOURCES CLR2, Patient_STR_VALUES CP2 WHERE CR2.RESOURCE_ID =
     *      CLR2.CURRENT_RESOURCE_ID AND CR2.IS_DELETED <> 'Y' AND CP2.LOGICAL_RESOURCE_ID = CR2.LOGICAL_RESOURCE_ID AND
     *      CP2.PARAMETER_NAME_ID = 5 AND CP2.STR_VALUE = 'Monella')));
     *
     * @param queryParm
     *            - A Parameter representing a chained query.
     * @return SqlQueryData - The query segment for a chained parameter reference search.
     * @throws Exception
     */
    @Override
    protected SqlQueryData processChainedReferenceParm(Parameter queryParm) throws Exception {
        final String METHODNAME = "processChainedReferenceParm";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());

        final String CR = "CR";
        final String CLR = "CLR";
        final String CP = "CP";
        Parameter currentParm;
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
            Parameter nextParameter = currentParm.getNextParameter();
            if (nextParameter != null) {
                Type nextParmaterType = nextParameter.getType();
                if (refParmIndex == 0) {
                    // Must build this first piece using px placeholder table alias, which will be replaced with a
                    // generated value in the buildQuery() method.
                    // Build this piece:P1.PARAMETER_NAME_ID = x AND (p1.STR_VALUE IN
                    this.populateNameIdSubSegment(whereClauseSegment, currentParm.getName(), PARAMETER_TABLE_ALIAS);
                    whereClauseSegment.append(JDBCOperator.AND.value());
                    whereClauseSegment.append(LEFT_PAREN);
                    whereClauseSegment.append(PARAMETER_TABLE_ALIAS + DOT).append(STR_VALUE).append(JDBCOperator.IN.value());
                } else {
                    // Build this piece: CP1.PARAMETER_NAME_ID = x AND CP1.STR_VALUE IN
                    appendMidChainParm(whereClauseSegment, currentParm, chainedParmVar);
                }

                refParmIndex++;
                chainedResourceVar = CR + refParmIndex;
                chainedLogicalResourceVar = CLR + refParmIndex;
                chainedParmVar = CP + refParmIndex;

                // The * is a wildcard for any resource type. This occurs only in the case where a reference parameter
                // chain was built to represent a compartment search with chained inclusion criteria that includes a
                // wildcard.
                //
                // For this situation, a separate method is called, and further processing of the chain by this method
                // is halted.
                if (currentParm.getModifierResourceTypeName().equals("*")) {
                    this.processWildcardChainedRefParm(currentParm, chainedResourceVar, chainedLogicalResourceVar, chainedParmVar, whereClauseSegment, bindVariables);
                    break;
                }
                resourceTypeName = currentParm.getModifierResourceTypeName();
                // Build this piece: (SELECT 'resource-type-name' || '/' || CLRx.LOGICAL_ID ...
                whereClauseSegment.append(LEFT_PAREN);
                appendInnerSelect(whereClauseSegment, currentParm, nextParmaterType, resourceTypeName, chainedResourceVar, chainedLogicalResourceVar, chainedParmVar);
            } else {
                // This logic processes the LAST parameter in the chain.
                // Build this piece: CPx.PARAMETER_NAME_ID = x AND CPx.STR_VALUE = ?
                Class<?> chainedResourceType = ModelSupport.getResourceType(resourceTypeName);
                SqlQueryData sqlQueryData = buildQueryParm(chainedResourceType, currentParm, chainedParmVar);
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

    private void appendMidChainParm(StringBuilder whereClauseSegment, Parameter currentParm, String chainedParmVar)
        throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException, FHIRPersistenceException {
        Integer parameterNameId = ParameterNamesCache.getParameterNameId(currentParm.getName());
        if (parameterNameId == null) {
            parameterNameId = this.parameterDao.readParameterNameId(currentParm.getName());
            if (parameterNameId != null) {
                this.parameterDao.addParameterNamesCacheCandidate(currentParm.getName(), parameterNameId);
            }
        }
        whereClauseSegment.append(chainedParmVar).append(".").append("PARAMETER_NAME_ID").append(JDBCOperator.EQ.value()).append(parameterNameId).append(JDBCOperator.AND.value()).append(chainedParmVar).append(".").append(STR_VALUE).append(JDBCOperator.IN.value());
    }

    private void appendInnerSelect(StringBuilder whereClauseSegment, Parameter currentParm, Type nextParmaterType, String resourceTypeName,
        String chainedResourceVar, String chainedLogicalResourceVar, String chainedParmVar) {
        String chainedResourceTableAlias = chainedResourceVar + ".";
        String chainedLogicalResourceTableAlias = chainedLogicalResourceVar + ".";
        String chainedParmTableAlias = chainedParmVar + ".";

        // Build this piece: SELECT 'resource-type-name' || '/' || CLRx.LOGICAL_ID
        whereClauseSegment.append("SELECT ").append("'" + resourceTypeName
                + "'").append(" || ").append("'/'").append(" || ").append(chainedLogicalResourceTableAlias).append("LOGICAL_ID");

        // Build this piece: FROM Device_RESOURCES CR1, Device_LOGICAL_RESOURCES CLR1, Device_STR_VALUES CP1 WHERE
        whereClauseSegment.append(" FROM ").append(resourceTypeName).append("_RESOURCES ").append(chainedResourceVar).append(", ").append(resourceTypeName).append("_LOGICAL_RESOURCES ").append(chainedLogicalResourceVar).append(", ");

        switch (nextParmaterType) {
        case DATE:
            whereClauseSegment.append(resourceTypeName + "_DATE_VALUES ");
            break;
        case NUMBER:
            whereClauseSegment.append(resourceTypeName + "_NUMBER_VALUES ");
            break;
        case QUANTITY:
            whereClauseSegment.append(resourceTypeName + "_QUANTITY_VALUES ");
            break;
        case TOKEN:
            whereClauseSegment.append(resourceTypeName + "_TOKEN_VALUES ");
            break;
        case REFERENCE:
        case URI:
        case STRING:
        default:
            whereClauseSegment.append(resourceTypeName + "_STR_VALUES ");
            break;
        }

        whereClauseSegment.append(chainedParmVar).append(" WHERE ");

        // Build this piece: CR1.RESOURCE_ID = CLR1.CURRENT_RESOURCE_ID AND CR1.IS_DELETED <> 'Y' AND CP1.LOGICAL_RESOURCE_ID =
        // CLR1.LOGICAL_RESOURCE_ID AND
        whereClauseSegment.append(chainedResourceTableAlias).append("RESOURCE_ID = ").append(chainedLogicalResourceTableAlias).append("CURRENT_RESOURCE_ID").append(AND).append(chainedResourceTableAlias).append("IS_DELETED").append(" <> 'Y'").append(AND).append(chainedParmTableAlias).append("LOGICAL_RESOURCE_ID = ").append(chainedResourceTableAlias).append("LOGICAL_RESOURCE_ID").append(AND);
    }

    /**
     * This method handles the processing of a wildcard chained reference parameter. The wildcard represents ALL FHIR
     * resource types stored in the FHIR database.
     * 
     * @throws Exception
     */
    private void processWildcardChainedRefParm(Parameter currentParm, String chainedResourceVar, String chainedLogicalResourceVar, String chainedParmVar,
        StringBuilder whereClauseSegment, List<Object> bindVariables) throws Exception {
        final String METHODNAME = "processChainedReferenceParm";
        log.entering(CLASSNAME, METHODNAME, currentParm.toString());

        String resourceTypeName;
        Collection<Integer> resourceTypeIds;
        Parameter lastParm;
        boolean selectGenerated = false;
        Map<String, Integer> resourceNameIdMap = null;
        Map<Integer, String> resourceIdNameMap = null;

        lastParm = currentParm.getNextParameter();

        // Acquire ALL Resource Type Ids
        resourceNameIdMap = resourceDao.readAllResourceTypeNames();
        resourceTypeIds = resourceNameIdMap.values();
        resourceIdNameMap = resourceNameIdMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

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

            appendInnerSelect(whereClauseSegment, currentParm, lastParm.getType(), resourceTypeName, chainedResourceVar, chainedLogicalResourceVar, chainedParmVar);

            // This logic processes the LAST parameter in the chain.
            // Build this piece: CPx.PARAMETER_NAME_ID = x AND CPx.STR_VALUE = ?
            Class<?> chainedResourceType = ModelSupport.getResourceType(resourceTypeName);
            SqlQueryData sqlQueryData = buildQueryParm(chainedResourceType, lastParm, chainedParmVar);
            whereClauseSegment.append(sqlQueryData.getQueryString());
            bindVariables.addAll(sqlQueryData.getBindVariables());

            selectGenerated = true;
        }

        log.exiting(CLASSNAME, METHODNAME, whereClauseSegment.toString());

    }

    /**
     * This method is the entry point for processing inclusion criteria, which define resources that are part of a
     * comparment-based search.
     * 
     * @throws Exception
     * @see compartments.json for the specificaiton of compartments, resources contained in each compartment, and the
     *      criteria that must be for a resource to be included in a compartment. Example inclusion criteria for
     *      AuditEvent in the Patient compartment: { "name": "AuditEvent", "inclusionCriteria": ["patient", This is a
     *      simple attribute inclusion criterion "participant.patient:Device", This is a chained inclusion criterion
     *      "participant.patient:RelatedPerson", This is a chained inclusion criterion "reference.patient:*"] This is a
     *      chained inclusion criterion with wildcard. The wildcard means "any resource type". }
     *
     *      Here is a sample generated query for this inclusion criteria: --PARAMETER_NAME_ID 13 = 'participant'
     *      --PARAMETER_NAME_ID 14 = 'patient' --PARAMETER_NAME_ID 16 = 'reference'
     *
     *      SELECT COUNT(R.RESOURCE_ID) FROM AuditEvent_RESOURCES R, AuditEvent_LOGICAL_RESOURCES LR ,
     *      AuditEvent_STR_VALUES P1 WHERE R.RESOURCE_ID = LR.CURRENT_RESOURCE_ID AND R.IS_DELETED <> 'Y' AND
     *      P1.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID AND ((P1.PARAMETER_NAME_ID=14 AND P1.STR_VALUE = ?) OR
     *      ((P1.PARAMETER_NAME_ID=13 AND (P1.STR_VALUE IN (SELECT 'Device' || '/' || CLR1.LOGICAL_ID FROM
     *      Device_RESOURCES CR1, Device_LOGICAL_RESOURCES CLR1, Device_STR_VALUES CP1 WHERE CR1.RESOURCE_ID =
     *      CLR1.CURRENT_RESOURCE_ID AND CR1.IS_DELETED <> 'Y' AND CP1.LOGICAL_RESOURCE_ID = CLR1.LOGICAL_RESOURCE_ID AND
     *      CP1.PARAMETER_NAME_ID=14 AND CP1.STR_VALUE = ?)))) OR ((P1.PARAMETER_NAME_ID=13 AND (P1.STR_VALUE IN (SELECT
     *      'RelatedPerson' || '/' || CLR1.LOGICAL_ID FROM RelatedPerson_RESOURCES CR1, RelatedPerson_LOGICAL_RESOURCES
     *      CLR1, RelatedPerson_STR_VALUES CP1 WHERE CR1.RESOURCE_ID = CLR1.CURRENT_RESOURCE_ID AND CR1.IS_DELETED <>
     *      'Y' AND CP1.LOGICAL_RESOURCE_ID = CLR1.LOGICAL_RESOURCE_ID AND CP1.PARAMETER_NAME_ID=14 AND CP1.STR_VALUE = ?)))) OR
     *      ((P1.PARAMETER_NAME_ID=16 AND (P1.STR_VALUE IN (SELECT 'AuditEvent' || '/' || CLR1.LOGICAL_ID FROM
     *      auditevent_RESOURCES CR1, auditevent_LOGICAL_RESOURCES CLR1, auditevent_STR_VALUES CP1 WHERE CR1.RESOURCE_ID
     *      = CLR1.CURRENT_RESOURCE_ID AND CR1.IS_DELETED <> 'Y' AND CP1.LOGICAL_RESOURCE_ID = CLR1.LOGICAL_RESOURCE_ID AND
     *      CP1.PARAMETER_NAME_ID=14 AND CP1.STR_VALUE = ? UNION SELECT 'Device' || '/' || CLR1.LOGICAL_ID FROM
     *      device_RESOURCES CR1, device_LOGICAL_RESOURCES CLR1, device_STR_VALUES CP1 WHERE CR1.RESOURCE_ID =
     *      CLR1.CURRENT_RESOURCE_ID AND CR1.IS_DELETED <> 'Y' AND CP1.LOGICAL_RESOURCE_ID = CLR1.LOGICAL_RESOURCE_ID AND
     *      CP1.PARAMETER_NAME_ID=14 AND CP1.STR_VALUE = ?)))));
     */
    @Override
    protected SqlQueryData processInclusionCriteria(Parameter queryParm) throws Exception {
        final String METHODNAME = "processInclusionCriteria";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());

        StringBuilder whereClauseSegment = new StringBuilder();
        JDBCOperator operator = JDBCOperator.EQ;
        Parameter currentParm;
        String currentParmValue;
        List<Object> bindVariables = new ArrayList<>();
        SqlQueryData queryData;
        SqlQueryData chainedIncQueryData;

        currentParm = queryParm;
        whereClauseSegment.append(LEFT_PAREN);
        while (currentParm != null) {
            if (currentParm.getValues() == null || currentParm.getValues().isEmpty()) {
                throw new FHIRPersistenceException("No Paramter values found when processing inclusion criteria.");
            }
            // Handle the special case of chained inclusion criteria.
            if (currentParm.getName().contains(".")) {
                whereClauseSegment.append(LEFT_PAREN);
                chainedIncQueryData = this.processChainedInclusionCriteria(currentParm);
                whereClauseSegment.append(chainedIncQueryData.getQueryString());
                bindVariables.addAll(chainedIncQueryData.getBindVariables());
                whereClauseSegment.append(RIGHT_PAREN);
            } else {
                currentParmValue = currentParm.getValues().get(0).getValueString();
                // Build this piece:
                // (pX.PARAMETER_NAME_ID = x AND
                this.populateNameIdSubSegment(whereClauseSegment, currentParm.getName(), PARAMETER_TABLE_ALIAS);
                whereClauseSegment.append(JDBCOperator.AND.value());
                // Build this piece: pX.str_value = search-attribute-value
                whereClauseSegment.append(PARAMETER_TABLE_ALIAS + DOT).append(STR_VALUE).append(operator.value()).append(BIND_VAR);
                whereClauseSegment.append(RIGHT_PAREN);
                bindVariables.add(currentParmValue);
            }

            currentParm = currentParm.getNextParameter();
            // If more than one parameter is in the chain, OR them together.
            if (currentParm != null) {
                whereClauseSegment.append(JDBCOperator.OR.value());
            }
        }
        whereClauseSegment.append(RIGHT_PAREN);
        queryData = new SqlQueryData(whereClauseSegment.toString(), bindVariables);
        log.exiting(CLASSNAME, METHODNAME, whereClauseSegment.toString());
        return queryData;
    }
    
    @Override
    protected SqlQueryData processDateParm(Class<?> resourceType, Parameter queryParm) throws Exception {
        return processDateParm(resourceType, queryParm, PARAMETERS_TABLE_ALIAS);
    }

    private SqlQueryData processDateParm(Class<?> resourceType, Parameter queryParm, String tableAlias) throws Exception {
        final String METHODNAME = "processDateParm";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());

        StringBuilder whereClauseSegment = new StringBuilder();
        JDBCOperator operator;
        boolean parmValueProcessed = false;
        Date date, start = null, end = null;
        SqlQueryData queryData;
        List<Object> bindVariables = new ArrayList<>();
        boolean isDateSearch = isDateSearch(resourceType, queryParm);
        boolean isDateRangeSearch = isDateRangeSearch(resourceType, queryParm);

        if (log.isLoggable(Level.FINE)) {
            log.fine("isDateSearch=" + isDateSearch + "  isDateRangeSearch=" + isDateRangeSearch);
        }
        if (!isDateSearch && !isDateRangeSearch) {
            throw new FHIRPersistenceException("Cannot process query parameter '" + queryParm.getName() + "' as a date.").withIssue(
                    Issue.builder()
                         .code(IssueType.INVALID)
                         .severity(IssueSeverity.WARNING)
                         .build()
                    );
        }

        // Build this piece of the segment:
        // (P1.PARAMETER_NAME_ID = x AND
        this.populateNameIdSubSegment(whereClauseSegment, queryParm.getName(), tableAlias);

        whereClauseSegment.append(AND).append(LEFT_PAREN);
        for (ParameterValue value : queryParm.getValues()) {
            operator = getPrefixOperator(value);
            // If multiple values are present, we need to OR them together.
            if (parmValueProcessed) {
                whereClauseSegment.append(JDBCOperator.OR.value()).append(LEFT_PAREN);
            }

            // TODO: clean up handling of date parameters in the query predicates
            java.time.Instant inst = QueryBuilderUtil.getInstant(value.getValueDate());
            date = Date.from(inst);
            // If the dateTime value is fully specified, go ahead and build a where clause segment for it.
            if (!value.getValueDate().isPartial()) {
                start = date;
                end = date;
                if (isDateSearch) {
                    whereClauseSegment.append(LEFT_PAREN);
                    whereClauseSegment.append(tableAlias + DOT).append(DATE_VALUE).append(operator.value()).append(BIND_VAR);
                    bindVariables.add(date);
                    whereClauseSegment.append(RIGHT_PAREN);
                }
            } else {
                // For a partial dateTime and an EQ operator, a duration is calculated and a where segment is generated
                // to cover a range.
                // For example, if the dateTime is specified down to the day, a range where segment is generated to
                // cover that day.

                start = date;
                end = Date.from(QueryBuilderUtil.getEnd(value.getValueDate()));

                if (isDateSearch) {
                    whereClauseSegment.append(LEFT_PAREN);
                    if (operator.equals(JDBCOperator.EQ)) {
                        // TODO: can we combine this with the dateRange logic below?
                        // The main difference is here we use DATE_VALUE instead of DATE_START/DATE_END
                        // because the DATE_VALUE is precise and has no implicit range
                        whereClauseSegment.append(tableAlias
                                + DOT).append(DATE_VALUE).append(JDBCOperator.GTE.value()).append(BIND_VAR).append(JDBCOperator.AND.value()).append(tableAlias
                                        + DOT).append(DATE_VALUE).append(JDBCOperator.LT.value()).append(BIND_VAR);
                        bindVariables.add(start);
                        bindVariables.add(end);
                    } else {
                        whereClauseSegment.append(tableAlias + DOT).append(DATE_VALUE).append(operator.value()).append(BIND_VAR);
                        bindVariables.add(start);
                    }
                    whereClauseSegment.append(RIGHT_PAREN);
                }
            }
            if (isDateRangeSearch) {
                if (isDateSearch) {
                    whereClauseSegment.append(JDBCOperator.OR.value());
                }
                whereClauseSegment.append(LEFT_PAREN);
                if (value.getPrefix() == null) {
                    handleDateRangeComparison(tableAlias, whereClauseSegment, start, end, bindVariables, Prefix.EQ);
                } else {
                    handleDateRangeComparison(tableAlias, whereClauseSegment, start, end, bindVariables, value.getPrefix());
                }
                whereClauseSegment.append(RIGHT_PAREN);
            }
            whereClauseSegment.append(RIGHT_PAREN);
            parmValueProcessed = true;
        }
        whereClauseSegment.append(RIGHT_PAREN);

        queryData = new SqlQueryData(whereClauseSegment.toString(), bindVariables);
        log.exiting(CLASSNAME, METHODNAME);
        return queryData;
    }

    /**
     * Append the condition and bind the variables according to the semantics of the passed prefix
     * 
     * @param tableAlias
     * @param whereClauseSegment
     * @param start
     * @param end
     * @param bindVariables
     * @param prefix
     */
    private void handleDateRangeComparison(String tableAlias, StringBuilder whereClauseSegment, Date start, Date end, List<Object> bindVariables,
        Prefix prefix) {
        switch (prefix) {
        case EB:
            // the range of the search value does not overlap with the range of the target value,
            // and the range above the search value contains the range of the target value
            whereClauseSegment.append(tableAlias + DOT).append(DATE_END).append(JDBCOperator.LT.value()).append(BIND_VAR);
            bindVariables.add(start);
            break;
        case SA:
            // the range of the search value does not overlap with the range of the target value,
            // and the range below the search value contains the range of the target value
            whereClauseSegment.append(tableAlias + DOT).append(DATE_START).append(JDBCOperator.GT.value()).append(BIND_VAR);
            bindVariables.add(end);
            break;
        case GE:
            // the range above the search value intersects (i.e. overlaps) with the range of the target value,
            // or the range of the search value fully contains the range of the target value
            whereClauseSegment.append(tableAlias + DOT).append(DATE_END).append(JDBCOperator.GTE.value()).append(BIND_VAR);
            bindVariables.add(start);
            break;
        case GT:
            // the range above the search value intersects (i.e. overlaps) with the range of the target value
            whereClauseSegment.append(tableAlias + DOT).append(DATE_END).append(JDBCOperator.GT.value()).append(BIND_VAR);
            bindVariables.add(start);
            break;
        case LE:
            // the range below the search value intersects (i.e. overlaps) with the range of the target value
            // or the range of the search value fully contains the range of the target value
            whereClauseSegment.append(tableAlias + DOT).append(DATE_START).append(JDBCOperator.LTE.value()).append(BIND_VAR);
            bindVariables.add(end);
            break;
        case LT:
            // the range below the search value intersects (i.e. overlaps) with the range of the target value
            whereClauseSegment.append(tableAlias + DOT).append(DATE_START).append(JDBCOperator.LT.value()).append(BIND_VAR);
            bindVariables.add(end);
            break;
        case AP:
            // the range of the search value overlaps with the range of the target value

            // 1. search range fully contains the target period
            whereClauseSegment.append(LEFT_PAREN);
            whereClauseSegment.append(tableAlias + DOT).append(DATE_START).append(JDBCOperator.GTE.value()).append(BIND_VAR);
            whereClauseSegment.append(JDBCOperator.AND.value());
            whereClauseSegment.append(tableAlias + DOT).append(DATE_END).append(JDBCOperator.LT.value()).append(BIND_VAR);
            bindVariables.add(start);
            bindVariables.add(end);
            whereClauseSegment.append(RIGHT_PAREN);

            whereClauseSegment.append(JDBCOperator.OR.value());
            // 2. search range begins during the target period
            whereClauseSegment.append(LEFT_PAREN);
            whereClauseSegment.append(tableAlias + DOT).append(DATE_START).append(JDBCOperator.LTE.value()).append(BIND_VAR);
            whereClauseSegment.append(JDBCOperator.AND.value());
            whereClauseSegment.append(tableAlias + DOT).append(DATE_END).append(JDBCOperator.GTE.value()).append(BIND_VAR);
            bindVariables.add(start);
            bindVariables.add(start);
            whereClauseSegment.append(RIGHT_PAREN);

            whereClauseSegment.append(JDBCOperator.OR.value());
            // 3. search range ends during the target period
            whereClauseSegment.append(LEFT_PAREN);
            whereClauseSegment.append(tableAlias + DOT).append(DATE_START)
                // strictly less than because the implicit end of the search range is exclusive
                .append(JDBCOperator.LT.value()).append(BIND_VAR);
            whereClauseSegment.append(JDBCOperator.AND.value());
            whereClauseSegment.append(tableAlias + DOT).append(DATE_END).append(JDBCOperator.GTE.value()).append(BIND_VAR);
            bindVariables.add(end);
            bindVariables.add(end);
            whereClauseSegment.append(RIGHT_PAREN);

            break;
        case NE:
            // the range of the search value does not fully contain the range of the target value
            whereClauseSegment.append(tableAlias).append(DOT).append(DATE_START).append(JDBCOperator.LT.value()).append(BIND_VAR);
            whereClauseSegment.append(JDBCOperator.OR.value());
            whereClauseSegment.append(tableAlias).append(DOT).append(DATE_END).append(JDBCOperator.GTE.value()).append(BIND_VAR);
            bindVariables.add(start);
            bindVariables.add(end);
            break;
        case EQ:
        default:
            // the range of the search value fully contains the range of the target value
            whereClauseSegment.append(tableAlias).append(DOT).append(DATE_START).append(JDBCOperator.GTE.value()).append(BIND_VAR);
            whereClauseSegment.append(JDBCOperator.AND.value());
            whereClauseSegment.append(tableAlias).append(DOT).append(DATE_END).append(JDBCOperator.LT.value()).append(BIND_VAR);
            bindVariables.add(start);
            bindVariables.add(end);
            break;
        }
    }

    @Override
    protected SqlQueryData processTokenParm(Parameter queryParm) throws FHIRPersistenceException {
        return processTokenParm(queryParm, PARAMETERS_TABLE_ALIAS);
    }
    
    private SqlQueryData processTokenParm(Parameter queryParm, String tableAlias) throws FHIRPersistenceException {
        final String METHODNAME = "processTokenParm";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());

        StringBuilder whereClauseSegment = new StringBuilder();
        JDBCOperator operator = this.getOperator(queryParm, JDBCOperator.EQ);
        boolean parmValueProcessed = false;
        SqlQueryData queryData;
        List<Object> bindVariables = new ArrayList<>();
        Integer codeSystemId;

        // Build this piece of the segment:
        // (P1.PARAMETER_NAME_ID = x AND
        this.populateNameIdSubSegment(whereClauseSegment, queryParm.getName(), tableAlias);

        whereClauseSegment.append(AND).append(LEFT_PAREN);
        for (ParameterValue value : queryParm.getValues()) {
            // If multiple values are present, we need to OR them together.
            if (parmValueProcessed) {
                whereClauseSegment.append(JDBCOperator.OR.value());
            }

            whereClauseSegment.append(LEFT_PAREN);
            // Include code
            whereClauseSegment.append(tableAlias + DOT).append(TOKEN_VALUE).append(operator.value()).append(BIND_VAR);
            bindVariables.add(SqlParameterEncoder.encode(value.getValueCode()));

            // Include system if present.
            if (value.getValueSystem() != null && !value.getValueSystem().isEmpty()) {
                if (operator.equals(JDBCOperator.NE)) {
                    whereClauseSegment.append(JDBCOperator.OR.value());
                } else {
                    whereClauseSegment.append(JDBCOperator.AND.value());
                }
                whereClauseSegment.append(tableAlias + DOT).append(CODE_SYSTEM_ID).append(operator.value()).append(BIND_VAR);
                codeSystemId = CodeSystemsCache.getCodeSystemId(value.getValueSystem());
                if (codeSystemId == null) {
                    codeSystemId = this.parameterDao.readCodeSystemId(value.getValueSystem());
                    if (codeSystemId != null) {
                        this.parameterDao.addCodeSystemsCacheCandidate(value.getValueSystem(), codeSystemId);
                    }
                }
                // must be able to handle nulls
                bindVariables.add(codeSystemId);
            }
            whereClauseSegment.append(RIGHT_PAREN);
            parmValueProcessed = true;
        }

        whereClauseSegment.append(RIGHT_PAREN).append(RIGHT_PAREN);
        queryData = new SqlQueryData(whereClauseSegment.toString(), bindVariables);

        log.exiting(CLASSNAME, METHODNAME);
        return queryData;
    }

    @Override
    protected SqlQueryData processNumberParm(Class<?> resourceType, Parameter queryParm) throws FHIRPersistenceException {
        return processNumberParm(resourceType, queryParm, PARAMETERS_TABLE_ALIAS);
    }
    
    private SqlQueryData processNumberParm(Class<?> resourceType, Parameter queryParm, String tableAlias) throws FHIRPersistenceException {
        final String METHODNAME = "processNumberParm";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());

        StringBuilder whereClauseSegment = new StringBuilder();
        JDBCOperator operator;
        boolean parmValueProcessed = false;
        List<Object> bindVariables = new ArrayList<>();
        SqlQueryData queryData;

        // Build this piece of the segment:
        // (P1.PARAMETER_NAME_ID = x AND
        this.populateNameIdSubSegment(whereClauseSegment, queryParm.getName(), tableAlias);

        whereClauseSegment.append(AND).append(LEFT_PAREN);
        for (ParameterValue value : queryParm.getValues()) {
            if (value.getPrefix() == Prefix.EB || value.getPrefix() == Prefix.SA) {
                boolean isIntegerSearch = false;
                try {
                    isIntegerSearch = ValueTypesFactory.getValueTypesProcessor().isIntegerSearch(resourceType, queryParm);
                } catch (FHIRSearchException e) {
                    log.log(Level.INFO, "Caught exception while checking the value types for parameter '" + queryParm.getName() + "'; continuing...", e);
                    // do nothing
                }
                if (isIntegerSearch) {
                    throw new FHIRPersistenceException("Search prefixes '" + Prefix.EB.value() + "' and '" + Prefix.SA.value()
                            + "' are not supported for integer searches.");
                }
            }
            operator = this.getPrefixOperator(value);
            // If multiple values are present, we need to OR them together.
            if (parmValueProcessed) {
                whereClauseSegment.append(JDBCOperator.OR.value());
            }
            // Build this piece: p1.value_string {operator} search-attribute-value
            whereClauseSegment.append(tableAlias + DOT).append(NUMBER_VALUE).append(operator.value()).append(BIND_VAR);
            bindVariables.add(value.getValueNumber());
            parmValueProcessed = true;
        }
        whereClauseSegment.append(RIGHT_PAREN).append(RIGHT_PAREN);
        queryData = new SqlQueryData(whereClauseSegment.toString(), bindVariables);

        log.exiting(CLASSNAME, METHODNAME, whereClauseSegment.toString());
        return queryData;
    }

    @Override
    protected SqlQueryData processQuantityParm(Class<?> resourceType, Parameter queryParm) throws Exception {
        return processQuantityParm(resourceType, queryParm, PARAMETERS_TABLE_ALIAS);
    }
    
    private SqlQueryData processQuantityParm(Class<?> resourceType, Parameter queryParm, String tableAlias) throws Exception {
        final String METHODNAME = "processQuantityParm";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());

        StringBuilder whereClauseSegment = new StringBuilder();
        JDBCOperator operator;
        boolean parmValueProcessed = false;
        List<Object> bindVariables = new ArrayList<>();
        Integer systemId;
        SqlQueryData queryData;

        // Build this piece of the segment:
        // (P1.PARAMETER_NAME_ID = x AND
        this.populateNameIdSubSegment(whereClauseSegment, queryParm.getName(), tableAlias);

        whereClauseSegment.append(AND).append(LEFT_PAREN);
        for (ParameterValue value : queryParm.getValues()) {
            operator = this.getPrefixOperator(value);
            // If multiple values are present, we need to OR them together.
            if (parmValueProcessed) {
                whereClauseSegment.append(JDBCOperator.OR.value());
            }
            whereClauseSegment.append(LEFT_PAREN);

            // If the target data type of the query is a Range, we need to build a piece of the where clause that looks
            // like this:
            // pX.value_number_low <= {search-attribute-value} AND pX.value_number_high >= {search-attribute-value}
            if (isRangeSearch(resourceType, queryParm)) {

                if (value.getPrefix() == null) {
                    handleQuantityRangeComparison(tableAlias, whereClauseSegment, value.getValueNumber(), value.getValueNumber(), bindVariables, Prefix.EQ);
                } else {
                    handleQuantityRangeComparison(tableAlias, whereClauseSegment, value.getValueNumber(), value.getValueNumber(), bindVariables, value.getPrefix());
                }
            } else {
                // Build this piece: p1.value_string {operator} search-attribute-value
                whereClauseSegment.append(tableAlias + DOT).append(QUANTITY_VALUE).append(operator.value()).append(BIND_VAR);
                bindVariables.add(value.getValueNumber());
            }

            // Include system if present.
            if (value.getValueSystem() != null && !value.getValueSystem().isEmpty()) {
                systemId = CodeSystemsCache.getCodeSystemId(value.getValueSystem());
                if (systemId == null) {
                    systemId = this.parameterDao.readCodeSystemId(value.getValueSystem());
                    if (systemId != null) {
                        this.parameterDao.addCodeSystemsCacheCandidate(value.getValueSystem(), systemId);
                    }
                }
                whereClauseSegment.append(JDBCOperator.AND.value()).append(tableAlias
                        + DOT).append(CODE_SYSTEM_ID).append(JDBCOperator.EQ.value()).append(BIND_VAR);
                bindVariables.add(systemId);
            }

            // Include code if present.
            if (value.getValueCode() != null && !value.getValueCode().isEmpty()) {
                whereClauseSegment.append(JDBCOperator.AND.value()).append(tableAlias + DOT).append(CODE).append(JDBCOperator.EQ.value()).append(BIND_VAR);
                bindVariables.add(value.getValueCode());
            }

            whereClauseSegment.append(RIGHT_PAREN);
            parmValueProcessed = true;
        }
        whereClauseSegment.append(RIGHT_PAREN).append(RIGHT_PAREN);
        queryData = new SqlQueryData(whereClauseSegment.toString(), bindVariables);

        log.exiting(CLASSNAME, METHODNAME, whereClauseSegment.toString());
        return queryData;
    }

    /**
     * Append the condition and bind the variables according to the semantics of the passed prefix
     * 
     * @param tableAlias
     * @param whereClauseSegment
     * @param start
     * @param end
     * @param bindVariables
     * @param prefix
     */
    private void handleQuantityRangeComparison(String tableAlias, StringBuilder whereClauseSegment, BigDecimal start, BigDecimal end,
        List<Object> bindVariables, Prefix prefix) {
        switch (prefix) {
        case EB:
            // the range of the search value does not overlap with the range of the target value,
            // and the range above the search value contains the range of the target value
            whereClauseSegment.append(tableAlias + DOT).append(QUANTITY_VALUE_HIGH).append(JDBCOperator.LT.value()).append(BIND_VAR);
            bindVariables.add(start);
            break;
        case SA:
            // the range of the search value does not overlap with the range of the target value,
            // and the range below the search value contains the range of the target value
            whereClauseSegment.append(tableAlias + DOT).append(QUANTITY_VALUE_LOW).append(JDBCOperator.GT.value()).append(BIND_VAR);
            bindVariables.add(end);
            break;
        case GE:
            // the range above the search value intersects (i.e. overlaps) with the range of the target value,
            // or the range of the search value fully contains the range of the target value
            whereClauseSegment.append(tableAlias + DOT).append(QUANTITY_VALUE_HIGH).append(JDBCOperator.GTE.value()).append(BIND_VAR);
            bindVariables.add(start);
            break;
        case GT:
            // the range above the search value intersects (i.e. overlaps) with the range of the target value
            whereClauseSegment.append(tableAlias + DOT).append(QUANTITY_VALUE_HIGH).append(JDBCOperator.GT.value()).append(BIND_VAR);
            bindVariables.add(start);
            break;
        case LE:
            // the range below the search value intersects (i.e. overlaps) with the range of the target value
            // or the range of the search value fully contains the range of the target value
            whereClauseSegment.append(tableAlias + DOT).append(QUANTITY_VALUE_LOW).append(JDBCOperator.LTE.value()).append(BIND_VAR);
            bindVariables.add(end);
            break;
        case LT:
            // the range below the search value intersects (i.e. overlaps) with the range of the target value
            whereClauseSegment.append(tableAlias + DOT).append(QUANTITY_VALUE_LOW).append(JDBCOperator.LT.value()).append(BIND_VAR);
            bindVariables.add(end);
            break;
        case AP:
            // the range of the search value overlaps with the range of the target value

            // 1. search range fully contains the target period
            whereClauseSegment.append(LEFT_PAREN);
            whereClauseSegment.append(tableAlias + DOT).append(QUANTITY_VALUE_LOW).append(JDBCOperator.GTE.value()).append(BIND_VAR);
            whereClauseSegment.append(JDBCOperator.AND.value());
            whereClauseSegment.append(tableAlias + DOT).append(QUANTITY_VALUE_HIGH).append(JDBCOperator.LT.value()).append(BIND_VAR);
            bindVariables.add(start);
            bindVariables.add(end);
            whereClauseSegment.append(RIGHT_PAREN);

            whereClauseSegment.append(JDBCOperator.OR.value());
            // 2. search range begins during the target period
            whereClauseSegment.append(LEFT_PAREN);
            whereClauseSegment.append(tableAlias + DOT).append(QUANTITY_VALUE_LOW).append(JDBCOperator.LTE.value()).append(BIND_VAR);
            whereClauseSegment.append(JDBCOperator.AND.value());
            whereClauseSegment.append(tableAlias + DOT).append(QUANTITY_VALUE_HIGH).append(JDBCOperator.GTE.value()).append(BIND_VAR);
            bindVariables.add(start);
            bindVariables.add(start);
            whereClauseSegment.append(RIGHT_PAREN);

            whereClauseSegment.append(JDBCOperator.OR.value());
            // 3. search range ends during the target period
            whereClauseSegment.append(LEFT_PAREN);
            whereClauseSegment.append(tableAlias + DOT).append(QUANTITY_VALUE_LOW)
                // strictly less than because the implicit end of the search range is exclusive
                .append(JDBCOperator.LT.value()).append(BIND_VAR);
            whereClauseSegment.append(JDBCOperator.AND.value());
            whereClauseSegment.append(tableAlias + DOT).append(QUANTITY_VALUE_HIGH).append(JDBCOperator.GTE.value()).append(BIND_VAR);
            bindVariables.add(end);
            bindVariables.add(end);
            whereClauseSegment.append(RIGHT_PAREN);

            break;
        case NE:
            // the range of the search value does not fully contain the range of the target value
            whereClauseSegment.append(tableAlias + DOT).append(QUANTITY_VALUE_LOW).append(JDBCOperator.LT.value()).append(BIND_VAR);
            whereClauseSegment.append(JDBCOperator.OR.value());
            whereClauseSegment.append(tableAlias + DOT).append(QUANTITY_VALUE_HIGH).append(JDBCOperator.GTE.value()).append(BIND_VAR);
            bindVariables.add(start);
            bindVariables.add(end);
            break;
        case EQ:
        default:
            // the range of the search value fully contains the range of the target value
            whereClauseSegment.append(tableAlias + DOT).append(QUANTITY_VALUE_LOW).append(JDBCOperator.GTE.value()).append(BIND_VAR);
            whereClauseSegment.append(JDBCOperator.AND.value());
            whereClauseSegment.append(tableAlias + DOT).append(QUANTITY_VALUE_HIGH).append(JDBCOperator.LT.value()).append(BIND_VAR);
            bindVariables.add(start);
            bindVariables.add(end);
            break;
        }
    }

    @Override
    protected SqlQueryData buildLocationQuerySegment(String parmName, BoundingBox boundingBox) throws FHIRPersistenceException {
        final String METHODNAME = "buildLocationQuerySegment";
        log.entering(CLASSNAME, METHODNAME, parmName);

        StringBuilder whereClauseSegment = new StringBuilder();
        List<Object> bindVariables = new ArrayList<>();
        SqlQueryData queryData;

        // Build this piece of the segment:
        // (P1.PARAMETER_NAME_ID = x AND
        this.populateNameIdSubSegment(whereClauseSegment, parmName, PARAMETER_TABLE_ALIAS);

        // Now build the piece that compares the BoundingBox longitude and latitude values
        // to the persisted longitude and latitude parameters.
        whereClauseSegment.append(JDBCOperator.AND.value()).append(LEFT_PAREN).append(PARAMETER_TABLE_ALIAS
                + DOT).append(LONGITUDE_VALUE).append(JDBCOperator.LTE.value()).append(BIND_VAR).append(JDBCOperator.AND.value()).append(PARAMETER_TABLE_ALIAS
                        + DOT).append(LONGITUDE_VALUE).append(JDBCOperator.GTE.value()).append(BIND_VAR).append(JDBCOperator.AND.value()).append(PARAMETER_TABLE_ALIAS
                                + DOT).append(LATITUDE_VALUE).append(JDBCOperator.LTE.value()).append(BIND_VAR).append(JDBCOperator.AND.value()).append(PARAMETER_TABLE_ALIAS
                                        + DOT).append(LATITUDE_VALUE).append(JDBCOperator.GTE.value()).append(BIND_VAR).append(RIGHT_PAREN).append(RIGHT_PAREN);
        bindVariables.add(boundingBox.getMaxLongitude());
        bindVariables.add(boundingBox.getMinLongitude());
        bindVariables.add(boundingBox.getMaxLatitude());
        bindVariables.add(boundingBox.getMinLatitude());

        queryData = new SqlQueryData(whereClauseSegment.toString(), bindVariables);
        log.exiting(CLASSNAME, METHODNAME, whereClauseSegment.toString());
        return queryData;
    }

    /**
     * Populates the parameter name sub-segment of the passed where clause segment.
     * 
     * @param whereClauseSegment
     * @param queryParmName
     * @throws FHIRPersistenceException
     */
    private void populateNameIdSubSegment(StringBuilder whereClauseSegment, String queryParmName, String parameterTableAlias) throws FHIRPersistenceException {
        final String METHODNAME = "populateNameIdSubSegment";
        log.entering(CLASSNAME, METHODNAME, queryParmName);

        Integer parameterNameId;

        // Build this piece of the segment:
        // (P1.PARAMETER_NAME_ID = x
        parameterNameId = ParameterNamesCache.getParameterNameId(queryParmName);
        if (parameterNameId == null) {
            // only try to read, not create
            parameterNameId = this.parameterDao.readParameterNameId(queryParmName);

            if (parameterNameId != null) {
                this.parameterDao.addParameterNamesCacheCandidate(queryParmName, parameterNameId);
            }
        }
        whereClauseSegment.append(LEFT_PAREN);
        whereClauseSegment.append(parameterTableAlias + DOT).append("PARAMETER_NAME_ID=").append(nullCheck(parameterNameId));

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
     * Finds the index of the 'near' parameter in the passed list of search parameters. If not found, -1 is returned.
     * 
     * @param searchParameters
     * @return int - The index of the 'near' parameter in the passed List.
     */
    private int findNearParameterIndex(List<Parameter> searchParameters) {

        int nearParameterIndex = -1;

        for (int i = 0; i < searchParameters.size(); i++) {
            if (searchParameters.get(i).getName().equals(NEAR)) {
                nearParameterIndex = i;
                break;
            }
        }
        return nearParameterIndex;
    }

    /**
     * This method handles the special case of chained inclusion criteria. Using data extracted from the passed query
     * parameter, a new Parameter chain is built to represent the chained inclusion criteria. That new Parameter is then
     * passed to the inherited processChainedReferenceParamter() method to generate the required where clause segment.
     * 
     * @see https://www.hl7.org/fhir/compartments.html
     * @param queryParm
     *            - A Parameter representing chained inclusion criterion.
     * @return SqlQueryData - the where clause segment and bind variables for a chained inclusion criterion.
     * @throws Exception
     */
    private SqlQueryData processChainedInclusionCriteria(Parameter queryParm) throws Exception {
        final String METHODNAME = "processChainedInclusionCriteria";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());

        SqlQueryData queryData;
        Parameter rootParameter = null;

        // Transform the passed query parm into a chained parameter representation.
        rootParameter = SearchUtil.parseChainedInclusionCriteria(queryParm);
        // Call method to process the Parameter built by this method as a chained parameter.
        queryData = this.processChainedReferenceParm(rootParameter);

        log.exiting(CLASSNAME, METHODNAME);
        return queryData;

    }

    private SqlQueryData processMissingParm(Class<?> resourceType, Parameter queryParm, String tableAlias) throws FHIRPersistenceException {
        final String METHODNAME = "processStringParm";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());

        // boolean to track whether the user has requested the resources missing this parameter (true) or not missing it
        // (false)
        Boolean missing = null;
        for (ParameterValue parameterValue : queryParm.getValues()) {
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

        // WHERE R.RESOURCE_ID = LR.CURRENT_RESOURCE_ID because we're not using the value from the parameters table
        whereClauseSegment.append("R.RESOURCE_ID = LR.CURRENT_RESOURCE_ID AND ");

        // if (missing != null && !missing) {
        // // Build this piece of the segment:
        // // (P1.PARAMETER_NAME_ID = x AND P1.logical_resource_id = LR.logical_resource_id)
        // this.populateNameIdSubSegment(whereClauseSegment, queryParm.getName(), tableAlias);
        // whereClauseSegment.append(AND).append(tableAlias + DOT + "LOGICAL_RESOURCE_ID = LR.LOGICL_RESOURCE_ID");
        // whereClauseSegment.append(RIGHT_PAREN);
        // } else {
        StringBuilder valuesTable = new StringBuilder(resourceType.getSimpleName());
        switch (queryParm.getType()) {
        case URI:
        case REFERENCE:
        case STRING:
            valuesTable.append("_STR_VALUES");
            break;
        case NUMBER:
            valuesTable.append("_NUMBER_VALUES");
            break;
        case QUANTITY:
            valuesTable.append("_QUANTITY_VALUES");
            break;
        case DATE:
            valuesTable.append("_DATE_VALUES");
            break;
        case TOKEN:
            valuesTable.append("_TOKEN_VALUES");
            break;
        default:
            break;

        }

        if (missing == null || missing) {
            whereClauseSegment.append("NOT ");
        }
        whereClauseSegment.append("EXISTS ").append("(SELECT 1 FROM " + valuesTable + WHERE);

        // Build this piece of the segment:
        // (P1.PARAMETER_NAME_ID = x AND P1.logical_resource_id = R.logical_resource_id))
        this.populateNameIdSubSegment(whereClauseSegment, queryParm.getName(), valuesTable.toString());
        whereClauseSegment.append(AND).append(valuesTable + DOT + "LOGICAL_RESOURCE_ID = R.LOGICAL_RESOURCE_ID");
        whereClauseSegment.append(RIGHT_PAREN).append(RIGHT_PAREN);
        // }

        List<Object> bindVariables = new ArrayList<>();
        SqlQueryData queryData = new SqlQueryData(whereClauseSegment.toString(), bindVariables);
        log.exiting(CLASSNAME, METHODNAME);
        return queryData;
    }

    /**
     * Builds a query segment for the passed query parameter.
     * @param resourceType - A valid FHIR Resource type
     * @param queryParm - A Parameter object describing the name, value and type of search parm.
     * @return T1 - An object representing the selector query segment for the passed search parm.
     * @throws Exception 
     */
    protected SqlQueryData buildQueryParm(Class<?> resourceType, Parameter queryParm, String tableAlias) throws Exception {
        final String METHODNAME = "buildQueryParm";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());
        
        SqlQueryData databaseQueryParm = null;
        Type type;
        
        try {
            if (Modifier.MISSING.equals(queryParm.getModifier())) {
                return this.processMissingParm(resourceType, queryParm, tableAlias);
            }
            // NOTE: The special logic needed to process NEAR and NEAR_DISTANCE query parms for the Location resource type is
            // found in method processLocationPosition(). This method will not handle those.
            if (! (Location.class.equals(resourceType) && 
                (queryParm.getName().equals(NEAR) || queryParm.getName().equals(NEAR_DISTANCE)))) {
                
                type = queryParm.getType();
                switch(type) {
                case STRING:    databaseQueryParm = this.processStringParm(queryParm, tableAlias);
                        break;
                case REFERENCE: if (queryParm.isChained()) {
                                    databaseQueryParm = this.processChainedReferenceParm(queryParm);
                                }
                                else if (queryParm.isInclusionCriteria()) {
                                    databaseQueryParm = this.processInclusionCriteria(queryParm);
                                }
                                else {
                                    databaseQueryParm = this.processReferenceParm(resourceType, queryParm, tableAlias);
                                }
                        break;
                case DATE:      databaseQueryParm = this.processDateParm(resourceType, queryParm, tableAlias);
                        break;
                case TOKEN:     databaseQueryParm = this.processTokenParm(queryParm, tableAlias);
                        break;
                case NUMBER:    databaseQueryParm = this.processNumberParm(resourceType, queryParm, tableAlias);
                        break;
                case QUANTITY:  databaseQueryParm = this.processQuantityParm(resourceType, queryParm, tableAlias);
                        break;
                case URI:       databaseQueryParm = this.processUriParm(queryParm, tableAlias);
                        break;
                
                default: throw new FHIRPersistenceNotSupportedException("Parm type not yet supported: " + type.value());
                }
            }
        }
        finally {
            log.exiting(CLASSNAME, METHODNAME, new Object[] {databaseQueryParm});
        }
        return databaseQueryParm;
    }

    @Override
    protected SqlQueryData processUriParm(Parameter queryParm) throws FHIRPersistenceException {
        return processUriParm(queryParm, PARAMETERS_TABLE_ALIAS);
    }

    /**
     * Creates a query segment for a URI type parameter.
     * @param queryParm - The query parameter. 
     * @param tableAlias - An alias for the table to query
     * @return T1 - An object containing query segment. 
     * @throws FHIRPersistenceException 
     */
    protected SqlQueryData processUriParm(Parameter queryParm, String tableAlias) throws FHIRPersistenceException {
        final String METHODNAME = "processUriParm";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());
        
        SqlQueryData parmRoot;
        Parameter myQueryParm;
                
        myQueryParm = queryParm;
        Modifier queryParmModifier = queryParm.getModifier();
        // A BELOW modifier has the same behavior as a "starts with" String search parm. 
        if (queryParmModifier != null && queryParmModifier.equals(Modifier.BELOW)) {
             myQueryParm = new Parameter(queryParm.getType(), queryParm.getName(), null,
                                queryParm.getModifierResourceTypeName(), queryParm.getValues());
        }
        parmRoot = this.processStringParm(myQueryParm, tableAlias);
                        
        log.exiting(CLASSNAME, METHODNAME, parmRoot.toString());
        return parmRoot;
    }

}
