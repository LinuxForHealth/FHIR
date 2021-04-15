/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.domain;

import static com.ibm.fhir.database.utils.query.expression.ExpressionUtils.alias;
import static com.ibm.fhir.database.utils.query.expression.ExpressionUtils.bind;
import static com.ibm.fhir.database.utils.query.expression.ExpressionUtils.col;
import static com.ibm.fhir.database.utils.query.expression.ExpressionUtils.on;
import static com.ibm.fhir.database.utils.query.expression.ExpressionUtils.string;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.CODE_SYSTEM_ID;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.COMMON_TOKEN_VALUE_ID;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.EQ;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.ESCAPE_PERCENT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.ESCAPE_UNDERSCORE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.IS_DELETED;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.PERCENT_WILDCARD;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.TOKEN_VALUE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.UNDERSCORE_WILDCARD;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants._LOGICAL_RESOURCES;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants._RESOURCES;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.query.Operator;
import com.ibm.fhir.database.utils.query.Select;
import com.ibm.fhir.database.utils.query.SelectAdapter;
import com.ibm.fhir.database.utils.query.WhereAdapter;
import com.ibm.fhir.database.utils.query.WhereFragment;
import com.ibm.fhir.database.utils.query.expression.StringExpNodeVisitor;
import com.ibm.fhir.database.utils.query.node.ExpNode;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.dao.api.JDBCIdentityCache;
import com.ibm.fhir.persistence.jdbc.util.NewUriModifierUtil;
import com.ibm.fhir.persistence.jdbc.util.QuerySegmentAggregator;
import com.ibm.fhir.persistence.jdbc.util.SqlParameterEncoder;
import com.ibm.fhir.persistence.jdbc.util.type.NewDateParmBehaviorUtil;
import com.ibm.fhir.persistence.jdbc.util.type.NewLocationParmBehaviorUtil;
import com.ibm.fhir.persistence.jdbc.util.type.NewNumberParmBehaviorUtil;
import com.ibm.fhir.persistence.jdbc.util.type.NewQuantityParmBehaviorUtil;
import com.ibm.fhir.persistence.jdbc.util.type.OperatorUtil;
import com.ibm.fhir.search.SearchConstants;
import com.ibm.fhir.search.SearchConstants.Modifier;
import com.ibm.fhir.search.SearchConstants.Type;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.location.NearLocationHandler;
import com.ibm.fhir.search.location.bounding.Bounding;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.parameters.QueryParameterValue;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * Used to render the domain model into a physical, executable query
 * modeled as a Select statement. The domain model knows about resources
 * and parameters. This class is used to translate the logical structure
 * of the query into a physical one, using the correct table names, join
 * predicates and filter expressions.
 */
public class SearchQueryRenderer implements SearchQueryVisitor<QueryData> {
    private static final Logger logger = Logger.getLogger(SearchQueryRenderer.class.getName());
    private final static String STR_VALUE = "STR_VALUE";
    private final static String STR_VALUE_LCASE = "STR_VALUE_LCASE";


    private final JDBCIdentityCache identityCache;

    // pagination page number
    private final int rowOffset;

    // pagination page size
    private final int rowsPerPage;

    /**
     * Public constructor
     * @param identityCache
     */
    public SearchQueryRenderer(JDBCIdentityCache identityCache,
        int rowOffset, int rowsPerPage) {
        this.identityCache = identityCache;
        this.rowOffset = rowOffset;
        this.rowsPerPage = rowsPerPage;
    }

    /**
     * Get the table name for the xx_logical_resources table where xx is the
     * resource type name
     * @param resourceType
     * @return the table name
     */
    private String resourceLogicalResources(String resourceType) {
        return resourceType + _LOGICAL_RESOURCES;
    }

    /**
     * Get the table name for the xx_resources table where xx is the resource type name
     * @param resourceType
     * @return
     */
    private String resourceResources(String resourceType) {
        return resourceType + _RESOURCES;
    }

    /**
     * Get the id for the given parameter name (cache lookup)
     * @param parameterName
     * @return
     */
    private int getParameterNameId(String parameterName) throws FHIRPersistenceException {
        return this.identityCache.getParameterNameId(parameterName);
    }

    /**
     * Get the id for the given code system name (cache lookup)
     * @param codeSystemName
     * @return
     * @throws FHIRPersistenceException
     */
    private int getCodeSystemId(String codeSystemName) throws FHIRPersistenceException {
        return this.identityCache.getCodeSystemId(codeSystemName);
    }

    @Override
    public QueryData countRoot(String rootResourceType) {
        final int aliasIndex = 0;
        final String xxLogicalResources = resourceLogicalResources(rootResourceType);
        final String lrAliasName = "LR" + aliasIndex;

        // The basic count query from the xx_LOGICAL_RESOURCES table. Query
        // parameters are bolted on as exists statements in the WHERE clause.
        // No need to join with xx_RESOURCES, because we only need to count
        // undeleted logical resources, not individual resource versions
        SelectAdapter select = Select.select("COUNT(*)");
        select.from(xxLogicalResources, alias(lrAliasName))
            .where(lrAliasName, IS_DELETED).eq(string("N"));
        return new QueryData(select, aliasIndex);
    }

    @Override
    public QueryData dataRoot(String rootResourceType) {
        final int aliasIndex = 0;
        final String xxLogicalResources = resourceLogicalResources(rootResourceType);
        final String xxResources = resourceResources(rootResourceType);
        final String lrAliasName = "LR0";

        // The core data query joining together the logical resources table
        // with the resources table for the current Query
        // parameters are bolted on as exists statements in the WHERE clause
        SelectAdapter select = Select.select("R.RESOURCE_ID", "R.LOGICAL_RESOURCE_ID", "R.VERSION_ID", "R.LAST_UPDATED", "R.IS_DELETED", "R.DATA", "LR0.LOGICAL_ID");
        select.from(xxLogicalResources, alias(lrAliasName))
            .innerJoin(xxResources, alias("R"), on(lrAliasName, "CURRENT_RESOURCE_ID").eq("R", "RESOURCE_ID"))
            .where(lrAliasName, IS_DELETED).eq().literal("N");
        return new QueryData(select, aliasIndex);
    }

    @Override
    public QueryData addTokenParam(QueryData queryData, String resourceType, QueryParameter queryParm) throws FHIRPersistenceException {
        // Attach an exists clause to the query
        final String parameterName = queryParm.getCode();
        final int aliasIndex = queryData.getParameterAlias() + 1;
        final SelectAdapter query = queryData.getQuery();
        final String xxTokenValues = resourceType + "_TOKEN_VALUES_V";
        final String paramAlias = "P" + aliasIndex;
        final String lrAlias = "LR" + (aliasIndex-1);
        SelectAdapter exists = Select.select("1");
        exists.from(xxTokenValues, alias(paramAlias))
                .where(paramAlias, "LOGICAL_RESOURCE_ID").eq(lrAlias, "LOGICAL_RESOURCE_ID") // correlate with the main query
                .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(parameterName))
                ;

        // add the filter predicate to the exists where clause
        ExpNode filter = getTokenFilter(queryParm, paramAlias).getExpression();
        exists.from().where().and(filter);

        // Add the exists sub-query we just built to the where clause of the main query
        query.from().where().and().exists(exists.build());

        // Return the exists sub-query in case there's a need to recurse lower
        return new QueryData(exists, aliasIndex);
    }

    /**
     * Get the filter predicate for the given token query parameter
     * @param paramExists the exists select statement to which we need to AND the filter predicate
     * @param queryParm the token query parameter
     * @param paramAlias the alias used for the token values table
     * @throws FHIRPersistenceException
     */
    protected WhereFragment getTokenFilter(QueryParameter queryParm, String paramAlias) throws FHIRPersistenceException {
        WhereFragment where = new WhereFragment();
        boolean first = true;
        where.leftParen();
        for (QueryParameterValue pv: queryParm.getValues()) {
            if (first) {
                first = false;
            } else {
                where.or();
            }
            final String codeSystem = pv.getValueSystem();
            final String tokenValue = pv.getValueCode();
            Long commonTokenValueId = identityCache.getCommonTokenValueId(codeSystem, tokenValue);
            if (commonTokenValueId != null && !codeSystem.equals("*")) {
                // Optimization where we have a single value. Use the literal (not a bind variable)
                // to give the optimizer more info
                where.and(paramAlias, "COMMON_TOKEN_VALUE_ID").eq(commonTokenValueId);
            } else {
                // add bind variables for the code system and token-value
                if (!codeSystem.equals("*")) {
                    where.and(paramAlias, "CODE_SYSTEM").eq().bind(codeSystem);
                }
                where.and(paramAlias, "TOKEN_VALUE").eq().bind(tokenValue);
            }
        }
        where.rightParen();

        return where;
    }

    /**
     * Add AND EXISTS (filterSubselect) to the WHERE clause of the given query
     * @param query
     * @param resourceType
     * @param parameterName
     * @param aliasIndex
     * @param filter
     */
    @Override
    public QueryData addStringParam(QueryData queryData, String resourceType, QueryParameter queryParm) throws FHIRPersistenceException {
        // Attach an exists clause to filter the result based on the string query parameter definition
        final int aliasIndex = queryData.getParameterAlias() + 1;
        final String lrAlias = getLRAlias(aliasIndex-1);
        final String strValues = resourceType + "_STR_VALUES";
        final String paramAlias = getParamAlias(aliasIndex);
        final String parameterName = queryParm.getCode();
        SelectAdapter exists = Select.select("1");
        exists.from(strValues, alias(paramAlias))
                .where(paramAlias, "LOGICAL_RESOURCE_ID").eq(lrAlias, "LOGICAL_RESOURCE_ID") // correlate with the main query
                .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(parameterName))
                ;

        Select filterQuery = exists.build();
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("string param EXISTS: " + filterQuery.toDebugString());
        }

        // Add the (non-trivial) filter predicate for string parameters
        ExpNode filter = getStringFilter(queryParm, paramAlias).getExpression();
        exists.from().where().and(filter);

        // Add the exists to the where clause of the main query which already has a predicate
        // so we need to AND the exists
        queryData.getQuery().from().where().and().exists(filterQuery);

        return new QueryData(exists, aliasIndex);
    }


    /**
     * Add a filter expression to the given parameter sub-query (which is used as an EXISTS clause)
     * @param paramExists the query statement to which we need to add the filter predicate
     * @param queryParm the query parameter for which we need to compute and add the filter predicate
     * @param paramAlias the alias for the query parameter table
     * @return
     * @throws FHIRPersistenceException
     */
    protected WhereFragment getStringFilter(QueryParameter queryParm, String paramAlias) throws FHIRPersistenceException {

        // Process the values from the queryParameter to produce
        // the predicates we need to pass to the visitor (which is
        // responsible for building the full query).
        final Operator operator = getOperator(queryParm);
        final String parameterName = queryParm.getCode();

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("addStringParam: " + parameterName + ", op=" + operator.name() + ", modifier=" + queryParm.getModifier());
        }
        WhereFragment whereFragment = new WhereFragment();
        whereFragment.leftParen();

        boolean multiple = false;
        for (QueryParameterValue value : queryParm.getValues()) {
            // Concatenate multiple matches with an OR
            if (multiple) {
                whereFragment.or();
            } else {
                multiple = true;
            }
            if (operator == Operator.LIKE) {
                // Must escape special wildcard characters _ and % in the parameter value string.
                String tempSearchValue =
                        SqlParameterEncoder.encode(value.getValueString()
                                .replace(PERCENT_WILDCARD, ESCAPE_PERCENT)
                                .replace(UNDERSCORE_WILDCARD, ESCAPE_UNDERSCORE));

                if (Modifier.CONTAINS.equals(queryParm.getModifier())) {
                    String searchValue = PERCENT_WILDCARD + tempSearchValue + PERCENT_WILDCARD;
                    whereFragment.col(STR_VALUE_LCASE).like(bind(searchValue)).escape("+");
                } else {
                    // If there is not a CONTAINS modifier on the query parm, construct
                    // a 'starts with' search value.
                    String searchValue = tempSearchValue + PERCENT_WILDCARD;

                    // Specific processing for
                    if (queryParm.getModifier() != null && queryParm.getType() == Type.URI) {
                        if (queryParm.getModifier() == Modifier.BELOW) {
                            searchValue = tempSearchValue + "/" + PERCENT_WILDCARD;

                            whereFragment.leftParen()
                            .col(STR_VALUE).eq(bind(tempSearchValue))
                            .or(STR_VALUE).like(bind(searchValue)).escape("+")
                            .rightParen();

                        } else if (queryParm.getModifier() == Modifier.ABOVE) {
                            NewUriModifierUtil.generateAboveValuesQuery(whereFragment, STR_VALUE, searchValue);
                        } else {
                            // neither above nor below, so an exact match for URI
                            whereFragment.col(STR_VALUE).eq(bind(searchValue));
                        }
                    } else {
                        // Simple STARTS WITH
                        whereFragment.col(STR_VALUE).like(bind(searchValue)).escape("+");
                    }
                }
            } else if (queryParm.getType() == Type.URI) {
                // need to handle above/below modifier
                if (queryParm.getModifier() == Modifier.BELOW) {
                    String tempSearchValue =
                            SqlParameterEncoder.encode(value.getValueString()
                                    .replace(PERCENT_WILDCARD, ESCAPE_PERCENT)
                                    .replace(UNDERSCORE_WILDCARD, ESCAPE_UNDERSCORE));

                    String searchValue = tempSearchValue + "/" + PERCENT_WILDCARD;

                    whereFragment.leftParen()
                    .col(STR_VALUE).eq(bind(tempSearchValue))
                    .or(STR_VALUE).like(bind(searchValue)).escape("+")
                    .rightParen();

                } else if (queryParm.getModifier() == Modifier.ABOVE) {
                    String searchValue = SqlParameterEncoder.encode(value.getValueString());
                    NewUriModifierUtil.generateAboveValuesQuery(whereFragment, STR_VALUE, searchValue);
                } else {
                    // neither above nor below, so an exact match for URI
                    String searchValue = SqlParameterEncoder.encode(value.getValueString());
                    whereFragment.col(STR_VALUE).eq(bind(searchValue));
                }
            } else if (operator == Operator.EQ) {
                // Exact match
                String searchValue = SqlParameterEncoder.encode(value.getValueString());
                whereFragment.col(STR_VALUE).eq(bind(searchValue));
            } else {
                // For anything other than an exact match, we search against the STR_VALUE_LCASE column in the
                // Resource's string values table.
                // Also, the search value is "normalized"; it has accents removed and is lower-cased. This enables a
                // case-insensitive, accent-insensitive search.
                // Build this piece: pX.str_value_lcase {operator} search-attribute-value
                String searchValue = SqlParameterEncoder.encode(value.getValueString());
                searchValue = SearchUtil.normalizeForSearch(searchValue);
                whereFragment.col(STR_VALUE_LCASE).operator(operator).bind(searchValue);
                addEscapeIfRequired(whereFragment, operator);
            }
        }

        whereFragment.rightParen();
        final ExpNode filter = whereFragment.getExpression();

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("string filter[" + parameterName + "] := " + StringExpNodeVisitor.stringify(filter));
        }

        return whereFragment;
    }

    /**
     * Add the ESCAPE modified to the LIKE clause if needed
     * @param wf
     * @param op
     */
    private void addEscapeIfRequired(WhereFragment wf, Operator op) {
        if (op == Operator.LIKE) {
            wf.escape("+"); // adds ESCAPE '+'
        }
    }


    @Override
    public QueryData addSorting(QueryData queryData) {
        queryData.getQuery().from().orderBy("LR0.LOGICAL_RESOURCE_ID");
        return queryData;
    }

    @Override
    public QueryData addPagination(QueryData queryData) {
        queryData.getQuery().pagination(rowOffset, rowsPerPage);
        return queryData;
    }

    @Override
    public QueryData addMissingParam(QueryData queryData, String resourceType, QueryParameter queryParm, boolean isMissing) throws FHIRPersistenceException {
        // TODO. Simple implementation to get started
        // note that there's no filter here to look for a specific value. We simply want to know
        // whether or not the parameter exists for a given resource
        final String parameterName = queryParm.getCode();
        final int aliasIndex = queryData.getParameterAlias() + 1;
        final String paramTableName = paramValuesTableName(resourceType, queryParm.getType());
        final String paramAlias = "P" + aliasIndex;
        SelectAdapter exists = Select.select("1");
        exists.from(paramTableName, alias(paramAlias))
                .where(paramAlias, "LOGICAL_RESOURCE_ID").eq("LR", "LOGICAL_RESOURCE_ID") // correlate with the main query
                .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(parameterName))
                ;

        // Add the exists to the where clause of the main query which already has a predicate
        // so we need to AND the exists
        SelectAdapter query = queryData.getQuery();
        if (isMissing) {
            // parameter should be missing, i.e. not exist
            query.from().where().and().notExists(exists.build());
        } else {
            // parameter should be not missing...i.e. it exists
            query.from().where().and().exists(exists.build());
        }
        return new QueryData(exists, aliasIndex);
    }

    /**
     * Get the parameter values table name (e.g. Patient_STR_VALUES) for the
     * given resource and parameter type. Note that this is now different from
     * the original QuerySegmentAggregator implementation - it does not differentiate
     * on chaining...that is left up to the building logic.
     * @param resourceType
     * @param paramType
     * @return
     */
    public String paramValuesTableName(String resourceType, Type paramType) {
        StringBuilder name = new StringBuilder(resourceType);
        switch (paramType) {
        case URI:
        case STRING:
            name.append("_STR_VALUES");
            break;
        case NUMBER:
            name.append("_NUMBER_VALUES");
            break;
        case QUANTITY:
            name.append("_QUANTITY_VALUES");
            break;
        case DATE:
            name.append("_DATE_VALUES");
            break;
        case SPECIAL:
            name.append("_LATLNG_VALUES");
            break;
        case REFERENCE:
        case TOKEN:
            name.append("_TOKEN_VALUES_V"); // uses view to hide new issue #1366 schema
            break;
        case COMPOSITE:
            name.append("_LOGICAL_RESOURCES");
            break;
        }
        return name.toString();
    }

    @Override
    public QueryData addChained(QueryData queryData, QueryParameter currentParm, String sourceResourceType) throws FHIRPersistenceException {
        // Each chained element is added as a nested EXISTS clause which joins the reference parameter
        // (stored as a token-value) with the target XX_LOGICAL_RESOURCES table.
        // AND EXISTS (SELECT 1
        //               FROM fhirdata.Observation_TOKEN_VALUES_V AS P1        -- Observation references to
        //         INNER JOIN fhirdata.Device_LOGICAL_RESOURCES AS LR1         -- Device
        //                 ON LR1.LOGICAL_ID = P1.TOKEN_VALUE                  -- Device.LOGICAL_ID = Observation.device
        //                AND LR1.VERSION_ID = COALESCE(P1.REF_VERSION_ID, LR1.VERSION_ID)
        //                AND P1.PARAMETER_NAME_ID = 1234                      -- Observation.device reference param
        //                AND P1.CODE_SYSTEM_ID = 4321                         -- code-system for Device
        //                AND LR1.IS_DELETED = 'N'                             -- referenced Device is not deleted
        //              WHERE P1.LOGICAL_RESOURCE_ID = LR0.LOGICAL_RESOURCE_ID -- correlate parameter to parent
        final SelectAdapter currentSubQuery = queryData.getQuery();
        final int aliasIndex = queryData.getParameterAlias() + 1;
        final String targetResourceType = currentParm.getModifierResourceTypeName();
        final String tokenValues = sourceResourceType + "_TOKEN_VALUES_V";
        final String xxLogicalResources = targetResourceType + "_LOGICAL_RESOURCES";
        final String paramAlias = "P" + aliasIndex;
        final String lrAlias = "LR" + aliasIndex;
        final String parentAlias = "LR" + (queryData.getParameterAlias());
        SelectAdapter exists = Select.select("1");
        exists.from(tokenValues, alias(paramAlias))
              .innerJoin(xxLogicalResources, alias(lrAlias),
                    on(lrAlias, "LOGICAL_ID").eq(paramAlias, "TOKEN_VALUE")
                    .and(lrAlias, "VERSION_ID").eq().coalesce(col(paramAlias, "REF_VERSION_ID"), col(parentAlias, "VERSION_ID"))
                    .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(currentParm.getCode()))
                    .and(paramAlias, "CODE_SYSTEM_ID").eq(getCodeSystemId(targetResourceType))
                    .and(lrAlias, "IS_DELETED").eq().literal("N")
                    )
              .where(paramAlias, "LOGICAL_RESOURCE_ID").eq(parentAlias, "LOGICAL_RESOURCE_ID") // correlate with the parent query
              ;

        // Add an exists clause to the where clause of the current query
        currentSubQuery.from().where().and().exists(exists.build());

        // Return the exists sub-select so that it can be used as the basis for the next
        // link in the chain
        return new QueryData(exists, aliasIndex);
    }

    @Override
    public QueryData addReverseChained(QueryData queryData, QueryParameter currentParm, String refResourceType) throws FHIRPersistenceException {
        // For reverse chaining, we write an exists with correlated sub-query
        // by connecting the token-value (reference) back to the parent query LOGICAL_ID.
        // We also include a local xx_LOGICAL_RESOURCES table, in case the next
        // chain element is a reverse chain and it needs a LOGICAL_ID value to connect to
        // AND EXISTS (SELECT 1
        //       FROM fhirdata.Observation_LOGICAL_RESOURCES LR1
        // INNER JOIN fhirdata.Observation_TOKEN_VALUES_V AS P1
        //         ON LR1.LOGICAL_RESOURCE_ID = P1.LOGICAL_RESOURCE_ID
        //        AND P1.PARAMETER_NAME_ID = 1246       -- 'Observation.patient'
        //        AND P1.CODE_SYSTEM_ID = 6             -- 'code system for Patient references'
        //      WHERE LR0.LOGICAL_ID = P1.TOKEN_VALUE   -- 'Patient.LOGICAL_ID = Observation.patient'
        //        AND LR0.VERSION_ID = COALESCE(P1.REF_VERSION_ID, LR0.VERSION_ID)
        final SelectAdapter currentSubQuery = queryData.getQuery();
        final int aliasIndex = queryData.getParameterAlias() + 1;
        final String resourceTypeName = currentParm.getModifierResourceTypeName();
        final String tokenValues = resourceTypeName + "_TOKEN_VALUES_V";
        final String xxLogicalResources = resourceTypeName + "_LOGICAL_RESOURCES";
        final String paramAlias = "P" + aliasIndex;
        final String lrAlias = "LR" + aliasIndex;
        final String parentAlias = "LR" + (aliasIndex-1);
        SelectAdapter exists = Select.select("1");
        exists.from(xxLogicalResources, alias(lrAlias))
              .innerJoin(tokenValues, alias(paramAlias),
                    on(lrAlias, "LOGICAL_RESOURCE_ID").eq(paramAlias, "LOGICAL_RESOURCE_ID")
                    .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(currentParm.getCode()))
                    .and(paramAlias, "CODE_SYSTEM_ID").eq(getCodeSystemId(refResourceType)))
              .where(parentAlias, "LOGICAL_ID").eq(paramAlias, "TOKEN_VALUE") // correlate with the main query
                .and(parentAlias, "VERSION_ID").eq().coalesce(col(paramAlias, "REF_VERSION_ID"), col(parentAlias, "VERSION_ID"))
              ;

        // Add an exists clause to the where clause of the current query
        currentSubQuery.from().where().and().exists(exists.build());

        // Return the exists sub-select so that it can be used as the basis for the next
        // link in the chain
        return new QueryData(exists, queryData.getParameterAlias()+1);
    }

    @Override
    public void addFilter(QueryData queryData, QueryParameter currentParm) throws FHIRPersistenceException {
        // TODO...still needed?
        SelectAdapter currentSubQuery = queryData.getQuery();
        // A simple filter added as an exists clause to the current query
        // AND EXISTS (SELECT 1
        //               FROM fhirdata.Patient_STR_VALUES AS P3                 -- 'Patient string parameters'
        //              WHERE P3.LOGICAL_RESOURCE_ID = LR2.LOGICAL_RESOURCE_ID  -- 'correlate to parent'
        //                AND P3.PARAMETER_NAME_ID = 123                        -- 'name parameter'
        //                AND P3.STR_VALUE = 'Jones')                           -- 'name filter'
        final int aliasIndex = queryData.getParameterAlias() + 1;
        final String paramTable = paramValuesTableName(currentParm.getModifierResourceTypeName(), currentParm.getType());
        final String paramAlias = getParamAlias(aliasIndex);
        final String parentAlias = getLRAlias(aliasIndex-1);
        SelectAdapter exists = Select.select("1");
        exists.from(paramTable, alias(paramAlias))
            .where(paramAlias, "LOGICAL_RESOURCE_ID").eq(parentAlias, "LOGICAL_RESOURCE_ID")
            .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(currentParm.getCode()))
            ;

        QueryData filter = new QueryData(exists, aliasIndex);
        addParamFilterValue(filter, currentParm);

        // Add an exists clause to the where clause of the current query
        currentSubQuery.from().where().and().exists(exists.build());
    }

    @Override
    public QueryData addNumberParam(QueryData queryData, String resourceType, QueryParameter queryParm) throws FHIRPersistenceException {
        // Attach an exists clause to the query
        final String parameterName = queryParm.getCode();
        final int aliasIndex = queryData.getParameterAlias() + 1;
        final SelectAdapter query = queryData.getQuery();
        final String numberValues = resourceType + "_NUMBER_VALUES";
        final String paramAlias = "P" + aliasIndex;
        final String lrAlias = "LR" + (aliasIndex-1);
        SelectAdapter exists = Select.select("1");
        exists.from(numberValues, alias(paramAlias))
                .where(paramAlias, "LOGICAL_RESOURCE_ID").eq(lrAlias, "LOGICAL_RESOURCE_ID") // correlate with the main query
                .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(parameterName))
                ;

        // add the filter predicate to the exists where clause
        ExpNode filter = getNumberFilter(queryParm, paramAlias).getExpression();
        exists.from().where().and(filter);

        // Add the exists sub-query we just built to the where clause of the main query
        query.from().where().and().exists(exists.build());

        // Return the exists sub-query in case there's a need to recurse lower
        return new QueryData(exists, aliasIndex);
    }

    /**
     * Get a filter predicate for the given number query parameter
     * @param exists
     * @param queryParm
     * @param paramAlias
     */
    protected WhereFragment getNumberFilter(QueryParameter queryParm, String paramAlias) throws FHIRPersistenceException {
        WhereFragment where = new WhereFragment();
        NewNumberParmBehaviorUtil.executeBehavior(where, queryParm, paramAlias);
        return where;
    }

    @Override
    public QueryData addQuantityParam(QueryData queryData, String resourceType, QueryParameter queryParm) throws FHIRPersistenceException {
        // Attach an exists clause to the query
        final String parameterName = queryParm.getCode();
        final int aliasIndex = queryData.getParameterAlias() + 1;
        final SelectAdapter query = queryData.getQuery();
        final String numberValues = resourceType + "_QUANTITY_VALUES";
        final String paramAlias = "P" + aliasIndex;
        final String lrAlias = "LR" + (aliasIndex-1);
        SelectAdapter exists = Select.select("1");
        exists.from(numberValues, alias(paramAlias))
                .where(paramAlias, "LOGICAL_RESOURCE_ID").eq(lrAlias, "LOGICAL_RESOURCE_ID") // correlate with the main query
                .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(parameterName))
                ;

        // add the filter predicate to the exists where clause
        ExpNode filter = getQuantityFilter(queryParm, paramAlias).getExpression();
        exists.from().where().and(filter);

        // Add the exists sub-query we just built to the where clause of the main query
        query.from().where().and().exists(exists.build());

        // Return the exists sub-query in case there's a need to recurse lower
        return new QueryData(exists, aliasIndex);
    }

    /**
     * Add a filter predicate to the given exists sub-query
     * @param exists
     * @param queryParm
     * @param paramAlias
     */
    protected WhereFragment getQuantityFilter(QueryParameter queryParm, String paramAlias) throws FHIRPersistenceException {
        WhereFragment where = new WhereFragment();
        NewQuantityParmBehaviorUtil behaviorUtil = new NewQuantityParmBehaviorUtil(this.identityCache);
        behaviorUtil.executeBehavior(where, queryParm, paramAlias);
        return where;
    }

    @Override
    public QueryData addDateParam(QueryData queryData, String resourceType, QueryParameter queryParm) throws FHIRPersistenceException {
        final String parameterName = queryParm.getCode();
        final int aliasIndex = queryData.getParameterAlias() + 1;
        final SelectAdapter query = queryData.getQuery();
        final String dateValues = resourceType + "_DATE_VALUES";
        final String paramAlias = getParamAlias(aliasIndex);
        final String lrAlias = getLRAlias(aliasIndex-1);
        SelectAdapter exists = Select.select("1");
        exists.from(dateValues, alias(paramAlias))
                .where(paramAlias, "LOGICAL_RESOURCE_ID").eq(lrAlias, "LOGICAL_RESOURCE_ID") // correlate with the main query
                .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(parameterName))
                ;

        // add the filter predicate to the exists where clause
        ExpNode filter = getDateFilter(queryParm, paramAlias).getExpression();
        exists.from().where().and(filter);

        // Add the exists sub-query we just built to the where clause of the main query
        query.from().where().and().exists(exists.build());

        // Return the exists sub-query in case there's a need to recurse lower
        return new QueryData(exists, aliasIndex);
    }

    /**
     * Add a filter predicate to the given exists sub-query
     * @param exists
     * @param queryParm
     * @param paramAlias
     */
    protected WhereFragment getDateFilter(QueryParameter queryParm, String paramAlias) {
        WhereFragment where = new WhereFragment();
        NewDateParmBehaviorUtil util = new NewDateParmBehaviorUtil();
        util.executeBehavior(where, queryParm, paramAlias);
        return where;
    }

    @Override
    public QueryData addLocationParam(QueryData queryData, String resourceType, QueryParameter queryParm) throws FHIRPersistenceException {
        final String parameterName = queryParm.getCode();
        final int aliasIndex = queryData.getParameterAlias() + 1;
        final SelectAdapter query = queryData.getQuery();
        final String dateValues = resourceType + "_LATLNG_VALUES";
        final String paramAlias = getParamAlias(aliasIndex);
        final String lrAlias = getLRAlias(aliasIndex-1);
        final long parameterNameId = getParameterNameId(parameterName);
        SelectAdapter exists = Select.select("1");
        exists.from(dateValues, alias(paramAlias))
                .where(paramAlias, "LOGICAL_RESOURCE_ID").eq(lrAlias, "LOGICAL_RESOURCE_ID") // correlate with the main query
                .and(paramAlias, "PARAMETER_NAME_ID").eq(parameterNameId)
                ;

        // add the filter predicate to the exists where clause
        ExpNode filter = getLocationFilter(queryParm, paramAlias).getExpression();
        exists.from().where().and(filter);

        // Add the exists sub-query we just built to the where clause of the main query
        query.from().where().and().exists(exists.build());

        // Return the exists sub-query in case there's a need to recurse lower
        return new QueryData(exists, aliasIndex);
    }

    /**
     * Add a filter predicate to the given exists sub-query
     * @param exists
     * @param queryParm
     * @param paramAlias
     */
    protected WhereFragment getLocationFilter(QueryParameter queryParm, String paramAlias) throws FHIRPersistenceException {
        WhereFragment where = new WhereFragment();

        NearLocationHandler handler = new NearLocationHandler();
        List<Bounding> boundingAreas;
        try {
            boundingAreas = handler.generateLocationPositionsFromParameters(Arrays.asList(queryParm));
        } catch (FHIRSearchException e) {
            throw new FHIRPersistenceException("input parameter is invalid bounding area, bad prefix, or bad units", e);
        }

        NewLocationParmBehaviorUtil behaviorUtil = new NewLocationParmBehaviorUtil();
        behaviorUtil.buildLocationSearchQuery(where, boundingAreas, paramAlias);

        return where;
    }

    /**
     * Get the string to use as a parameter table alias for the given aliasIndex value
     * @param aliasIndex
     * @return
     */
    private String getParamAlias(int aliasIndex) {
        return "P" + aliasIndex;
    }

    /**
     * Get the string to use as a logical resource alias for the given aliasIndex value
     * @param aliasIndex
     * @return
     */
    private String getLRAlias(int aliasIndex) {
        return "LR" + aliasIndex;
    }

    /**
     * @param exists
     * @param currentParm
     * @param paramAlias
     */
    private void addParamFilterValue(QueryData exists, QueryParameter currentParm) {
        // Add the filter value for the parameter to the current exists statement. This
        // statement already contains a WHERE predicate, so we just need to AND the
        // expression we need.
        String col;
        switch (currentParm.getType()) {
        case URI:
        case STRING:
            col = "STR_VALUE";
            break;
        case NUMBER:
            col = "NUMBER_VALUE";
            break;
        case QUANTITY:
            col = "QUANTITY_LOW";
            break;
        case DATE:
            col = "DATE_VALUE";
            break;
        case SPECIAL:
            col = "LATLNG_VALUES";
            break;
        case REFERENCE:
        case TOKEN:
            col = "TOKEN_VALUE";
            break;
        case COMPOSITE:
            col = null;
            break;
        }
    }


    @Override
    public QueryData addReferenceParam(QueryData queryData, String resourceType, QueryParameter queryParm) throws FHIRPersistenceException {
        // TODO align with addTokenParam
        // Attach an exists clause to the query
        final String parameterName = queryParm.getCode();
        final int aliasIndex = queryData.getParameterAlias() + 1;
        final SelectAdapter query = queryData.getQuery();
        final String xxTokenValues = resourceType + "_TOKEN_VALUES_V";
        final String paramAlias = "P" + aliasIndex;
        final String lrAlias = "LR" + (aliasIndex-1);
        SelectAdapter exists = Select.select("1");
        exists.from(xxTokenValues, alias(paramAlias))
                .where(paramAlias, "LOGICAL_RESOURCE_ID").eq(lrAlias, "LOGICAL_RESOURCE_ID") // correlate with the main query
                .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(parameterName))
                ;

        // add the filter predicate to the exists where clause
        ExpNode filter = getReferenceFilter(queryParm, paramAlias).getExpression();
        exists.from().where().and(filter);

        // Add the exists sub-query we just built to the where clause of the main query
        query.from().where().and().exists(exists.build());

        // Return the exists sub-query in case there's a need to recurse lower
        return new QueryData(exists, aliasIndex);

    }

    /**
     * Add the filter predicate to the given sub-select
     * @param exists
     * @param queryParm
     * @param paramAlias
     * @throws FHIRPersistenceException
     */
    protected WhereFragment getReferenceFilter(QueryParameter queryParm, String paramAlias) throws FHIRPersistenceException {
        WhereFragment whereClause = new WhereFragment();
        whereClause.leftParen();
        Operator operator = getOperator(queryParm, EQ);

        String searchValue;

        // We no longer build the query directly here. Instead we just want to build a logical model
        // which can be easily translated into a query.
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
                whereClause.or();
            } else {
                parmValueProcessed = true;
            }

            // If the predicate includes a code-system it will resolve to a single value from
            // common_token_values. It helps the query optimizer if we include this additional
            // filter because it can make better cardinality estimates.
            Long commonTokenValueId = null;
            if (operator == Operator.EQ && targetResourceType != null) {
                // targetResourceType is treated as the code-system for references
                commonTokenValueId = this.identityCache.getCommonTokenValueId(targetResourceType, searchValue);
            }

            // Build this piece: pX.token_value {operator} search-attribute-value [ AND pX.code_system_id = <n> ]
            whereClause.col(paramAlias, TOKEN_VALUE).operator(operator).bind(searchValue);

            // add the [optional] condition for the resource type if we have one
            if (commonTokenValueId != null) {
                // #1929 improves cardinality estimation
                // resulting in far better execution plans for many search queries. Because COMMON_TOKEN_VALUE_ID
                // is the primary key for the common_token_values table, we don't need the CODE_SYSTEM_ID = ? predicate.
                whereClause.and().col(paramAlias, COMMON_TOKEN_VALUE_ID).eq(commonTokenValueId); // use literal
            } else if (targetResourceType != null) {
                // For better performance, use a literal for the resource type code-system-id, not a parameter marker
                Integer codeSystemIdForResourceType = getCodeSystemId(targetResourceType);
                whereClause.and(paramAlias, CODE_SYSTEM_ID).eq(nullCheck(codeSystemIdForResourceType));
            }
        }

        whereClause.rightParen();
        return whereClause;
    }

    /**
     * Use -1 as a simple substitute for null literal ids because we know -1 will never exist
     * as a value in the database (for fields populated by sequence values).
     * @param value
     * @return
     */
    private int nullCheck(Integer value) {
        return value == null ? -1 : value;
    }

    /**
     * Get the operator we need to use for matching values for this parameter
     * @return
     */
    protected Operator getOperator(QueryParameter queryParameter) {
        return OperatorUtil.getOperator(queryParameter);
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
    protected Operator getOperator(QueryParameter queryParm, String defaultOverride) {
        return OperatorUtil.getOperator(queryParm, defaultOverride);
    }

    @Override
    public QueryData addCompositeParam(QueryData queryData, String resourceType, QueryParameter queryParm) throws FHIRPersistenceException {
        final int aliasIndex = queryData.getParameterAlias() + 1;
        final SelectAdapter query = queryData.getQuery();
        final String lrAlias = "LR" + (aliasIndex-1);

        // Each value gets its own EXISTS clause which we combine together
        // with OR. The whole thing needs to be wrapped in parens to ensure
        // the correct precedence.
        // AND ( EXISTS (...) OR EXISTS (...)
        final WhereAdapter where = queryData.getQuery().from().where();
        where.and().leftParen();
        boolean first = true;
        String firstTableAlias = null;

        // Each query parm value gets its own EXISTS OR'd together
        for (QueryParameterValue compositeValue : queryParm.getValues()) {
            SelectAdapter exists = Select.select("1");

            List<QueryParameter> components = compositeValue.getComponent();
            for (int componentNum = 1; componentNum <= components.size(); componentNum++) {
                QueryParameter component = components.get(componentNum - 1);
                String valuesTable = QuerySegmentAggregator.tableName(resourceType, component);
                String componentTableAlias = "comp" + componentNum;
                String parameterName = component.getCode();

                if (componentNum == 1) {
                    exists.from(valuesTable, alias(componentTableAlias))
                    .where(componentTableAlias, "LOGICAL_RESOURCE_ID").eq(lrAlias, "LOGICAL_RESOURCE_ID") // correlate with the main query
                    .and(componentTableAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(parameterName))
                    .and(paramFilter(component, componentTableAlias).getExpression());
                    ;

                    // Capture the alias for the first table - used to join additional composite parameter tables
                    firstTableAlias = componentTableAlias;
                } else {
                    // Join to the first parameter table
                    exists.from().innerJoin(valuesTable, alias(componentTableAlias),
                        on(componentTableAlias, "LOGICAL_RESOURCE_ID").eq(firstTableAlias, "LOGICAL_RESOURCE_ID")
                        .and(componentTableAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(parameterName))
                        .and(componentTableAlias, "COMPOSITE_ID").eq(firstTableAlias, "COMPOSITE_ID")
                        .and(paramFilter(component, componentTableAlias).getExpression()));
                }
            }

            // Add the exists sub-query we just built to the where clause of the main query
            if (first) {
                first = false;
            } else {
                where.or();
            }
            where.exists(exists.build());
        }

        // AND ( EXISTS (...) OR EXISTS (...) )  <== close the paren
        where.rightParen();
        // The only thing we can return which makes any sense is the original query
        return queryData;
    }

    /**
     * Get the filter predicate expression for the given query parameter taking into account its type,
     * modifiers etc.
     * @param paramTableAlias
     * @param queryParm
     * @return a valid expression
     */
    private WhereFragment paramFilter(QueryParameter queryParm, String paramTableAlias) throws FHIRPersistenceException {
        final WhereFragment result;

        switch (queryParm.getType()) {
        case URI:
        case STRING:
            result = getStringFilter(queryParm, paramTableAlias);
            break;
        case NUMBER:
            result = getNumberFilter(queryParm, paramTableAlias);
            break;
        case QUANTITY:
            result = getQuantityFilter(queryParm, paramTableAlias);
            break;
        case DATE:
            result = getDateFilter(queryParm, paramTableAlias);
            break;
        case SPECIAL:
            result = getLocationFilter(queryParm, paramTableAlias);
            break;
        case REFERENCE:
            result = getReferenceFilter(queryParm, paramTableAlias);
            break;
        case TOKEN:
            result = getTokenFilter(queryParm, paramTableAlias);
            break;
        default:
            result = null;
            break;
        }

        if (result == null) {
            throw new FHIRPersistenceException("Nested composite parameters are not supported");
        }

        return result;
    }

    @Override
    public QueryData addLocationPosition(QueryData queryData, List<QueryParameter> queryParameters) throws FHIRPersistenceException {
        // Special handling for location position extension logic
        NearLocationHandler handler = new NearLocationHandler();
        List<Bounding> boundingAreas;
        try {
            boundingAreas = handler.generateLocationPositionsFromParameters(queryParameters);
        } catch (FHIRSearchException e) {
            throw new FHIRPersistenceException("input parameter is invalid bounding area, bad prefix, or bad units", e);
        }

        if (!boundingAreas.isEmpty()) {
            buildLocationQuerySegment(queryData.getQuery(), boundingAreas);
        }

        return null;
    }

    protected void buildLocationQuerySegment(SelectAdapter query, List<Bounding> boundingAreas) {
        // TODO
        throw new IllegalStateException("unsupported");
    }
}