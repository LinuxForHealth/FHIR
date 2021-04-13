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
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.EQ;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.ESCAPE_PERCENT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.ESCAPE_UNDERSCORE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.GT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.GTE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.IS_DELETED;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LIKE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LTE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.NE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.PERCENT_WILDCARD;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.UNDERSCORE_WILDCARD;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants._LOGICAL_RESOURCES;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants._RESOURCES;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.modifierOperatorMap;

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
import com.ibm.fhir.persistence.jdbc.util.SqlParameterEncoder;
import com.ibm.fhir.persistence.jdbc.util.type.NewDateParmBehaviorUtil;
import com.ibm.fhir.persistence.jdbc.util.type.NewNumberParmBehaviorUtil;
import com.ibm.fhir.persistence.jdbc.util.type.NewQuantityParmBehaviorUtil;
import com.ibm.fhir.search.SearchConstants.Modifier;
import com.ibm.fhir.search.SearchConstants.Type;
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
        SelectAdapter select = Select.select("R.RESOURCE_ID", "R.LOGICAL_RESOURCE_ID", "R.VERSION_ID", "R.LAST_UPDATED", "R.IS_DELETED", "R.DATA", "LR.LOGICAL_ID");
        select.from(xxLogicalResources, alias(lrAliasName))
            .innerJoin(xxResources, alias("R"), on("LR", "CURRENT_RESOURCE_ID").eq("R", "RESOURCE_ID"))
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
        addTokenFilter(exists, queryParm, paramAlias);

        // Add the exists sub-query we just built to the where clause of the main query
        query.from().where().and().exists(exists.build());

        // Return the exists sub-query in case there's a need to recurse lower
        return new QueryData(exists, aliasIndex);
    }

    /**
     * Add the filter predicate for the given token query parameter
     * @param paramExists the exists select statement to which we need to AND the filter predicate
     * @param queryParm the token query parameter
     * @param paramAlias the alias used for the token values table
     * @throws FHIRPersistenceException
     */
    protected void addTokenFilter(SelectAdapter paramExists, QueryParameter queryParm, String paramAlias) throws FHIRPersistenceException {
        WhereAdapter where = paramExists.from().where();
        boolean first = true;
        where.and().leftParen();
        for (QueryParameterValue pv: queryParm.getValues()) {
            if (first) {
                first = false;
            } else {
                where.or();
            }
            final String codeSystem = pv.getValueSystem();
            final String tokenValue = pv.getValueCode();
            Long commonTokenValueId = identityCache.getCommonTokenValueId(codeSystem, tokenValue);
            if (commonTokenValueId != null) {
                // Optimization where we have a single value. Use the literal (not a bind variable)
                // to give the optimizer more info
                where.and(paramAlias, "COMMON_TOKEN_VALUE_ID").eq(commonTokenValueId);
            } else {
                // add bind variables for the code system and token-value
                where.and(paramAlias, "CODE_SYSTEM").eq().bind(codeSystem);
                where.and(paramAlias, "TOKEN_VALUE").eq().bind(tokenValue);
            }
        }
        where.rightParen();
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
        addStringFilter(exists, queryParm, paramAlias);

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
    protected void addStringFilter(SelectAdapter paramExists, QueryParameter queryParm, String paramAlias) throws FHIRPersistenceException {
        // Process the values from the queryParameter to produce
        // the predicates we need to pass to the visitor (which is
        // responsible for building the full query).
        final Operator operator = getOperator(queryParm);
        final String parameterName = queryParm.getCode();

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("addStringParam: " + parameterName + ", op=" + operator.name() + ", modifier=" + queryParm.getModifier());
        }
        WhereFragment whereFragment = new WhereFragment();

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


        // Now we've constructed the predicate, attach it to the exists where clause
        final ExpNode filter = whereFragment.getExpression();

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("filter[" + parameterName + "] := " + StringExpNodeVisitor.stringify(filter));
        }

        paramExists.from().where().and(filter);
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
        queryData.getQuery().from().orderBy("LR.LOGICAL_RESOURCE_ID");
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
        addNumberFilter(exists, queryParm, paramAlias);

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
    protected void addNumberFilter(SelectAdapter exists, QueryParameter queryParm, String paramAlias) throws FHIRPersistenceException {
        WhereAdapter where = exists.from().where();
        NewNumberParmBehaviorUtil.executeBehavior(where, queryParm, paramAlias);
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
        addQuantityFilter(exists, queryParm, paramAlias);

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
    protected void addQuantityFilter(SelectAdapter exists, QueryParameter queryParm, String paramAlias) throws FHIRPersistenceException {
        NewQuantityParmBehaviorUtil behaviorUtil = new NewQuantityParmBehaviorUtil(this.identityCache);
        WhereAdapter where = exists.from().where();
        behaviorUtil.executeBehavior(where, queryParm, paramAlias);
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
        addDateFilter(exists, queryParm, paramAlias);

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
    protected void addDateFilter(SelectAdapter exists, QueryParameter queryParm, String paramAlias) {
        NewDateParmBehaviorUtil util = new NewDateParmBehaviorUtil();
        util.executeBehavior(exists, queryParm, paramAlias);
    }

    @Override
    public QueryData addLocationParam(QueryData queryData, String resourceType, QueryParameter queryParm) throws FHIRPersistenceException {
        return null;
    }

    /**
     * Add a filter predicate to the given exists sub-query
     * @param exists
     * @param queryParm
     * @param paramAlias
     */
    protected void addLocationFilter(SelectAdapter exists, QueryParameter queryParm, String paramAlias) {
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

    /**
     * Get the operator we need to use for matching values for this parameter
     * @return
     */
    protected Operator getOperator(QueryParameter queryParameter) {
        String operator = LIKE;
        Modifier modifier = queryParameter.getModifier();

        // In the case where a URI, we need specific behavior/manipulation
        // so that URI defaults to EQ, unless... BELOW
        if (Type.URI.equals(queryParameter.getType())) {
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

        return convert(operator);
    }

    /**
     * Convert the operator string value to its enum equivalent
     * @param op
     * @return
     */
    private Operator convert(String op) {
        final Operator result;
        switch (op) {
        case LIKE:
            result = Operator.LIKE;
            break;
        case EQ:
            result = Operator.EQ;
            break;
        case NE:
            result = Operator.NE;
            break;
        case LT:
            result = Operator.LT;
            break;
        case LTE:
            result = Operator.LTE;
            break;
        case GT:
            result = Operator.GT;
            break;
        case GTE:
            result = Operator.GTE;
            break;
        default:
            throw new IllegalArgumentException("Operator not supported: " + op);
        }

        return result;
    }
}