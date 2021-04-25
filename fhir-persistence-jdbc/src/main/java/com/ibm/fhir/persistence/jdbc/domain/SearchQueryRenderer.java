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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.database.utils.query.Operator;
import com.ibm.fhir.database.utils.query.Select;
import com.ibm.fhir.database.utils.query.SelectAdapter;
import com.ibm.fhir.database.utils.query.WhereAdapter;
import com.ibm.fhir.database.utils.query.WhereFragment;
import com.ibm.fhir.database.utils.query.expression.StringExpNodeVisitor;
import com.ibm.fhir.database.utils.query.node.ExpNode;
import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.dao.api.JDBCIdentityCache;
import com.ibm.fhir.persistence.jdbc.util.NewUriModifierUtil;
import com.ibm.fhir.persistence.jdbc.util.QuerySegmentAggregator;
import com.ibm.fhir.persistence.jdbc.util.SqlParameterEncoder;
import com.ibm.fhir.persistence.jdbc.util.type.NewDateParmBehaviorUtil;
import com.ibm.fhir.persistence.jdbc.util.type.NewLastUpdatedParmBehaviorUtil;
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
import com.ibm.fhir.search.parameters.InclusionParameter;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.parameters.QueryParameterValue;
import com.ibm.fhir.search.util.SearchUtil;
import com.ibm.fhir.term.util.CodeSystemSupport;
import com.ibm.fhir.term.util.ValueSetSupport;

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
        return new QueryData(select, aliasIndex, rootResourceType);
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
        return new QueryData(select, aliasIndex, rootResourceType);
    }

    @Override
    public QueryData includeRoot(String rootResourceType) {
        final int aliasIndex = 0;
        final String xxLogicalResources = resourceLogicalResources(rootResourceType);
        final String xxResources = resourceResources(rootResourceType);
        final String lrAliasName = "LR0";
        final String rAliasName = "R0";

        // The core data query joining together the logical resources table
        // with the resources table for the current Query
        // parameters are bolted on as exists statements in the WHERE clause
        // Note that this query does not specify the current version of the
        // resource - that is the responsibility of the parameter filter (which
        // allows us to support versioned references for _include as described
        // in the spec).
        SelectAdapter select = Select.select("R0.RESOURCE_ID", "R0.LOGICAL_RESOURCE_ID", "R0.VERSION_ID", "R0.LAST_UPDATED", "R0.IS_DELETED", "R0.DATA", "LR0.LOGICAL_ID");
        select.from(xxLogicalResources, alias(lrAliasName))
            .innerJoin(xxResources, alias(rAliasName), on(lrAliasName, "LOGICAL_RESOURCE_ID").eq(rAliasName, "LOGICAL_RESOURCE_ID"))
            .where(lrAliasName, IS_DELETED).eq().literal("N");
        return new QueryData(select, aliasIndex, rootResourceType);
    }

    @Override
    public QueryData addTokenParam(QueryData queryData, String resourceType, QueryParameter queryParm) throws FHIRPersistenceException {
        // Attach an exists clause to the query
        final String parameterName = queryParm.getCode();
        final int aliasIndex = queryData.getParameterAlias() + 1;
        final SelectAdapter query = queryData.getQuery();
        SelectAdapter exists = Select.select("1");

        final Operator operator = getOperator(queryParm);
        if (operator == Operator.EQ) {
            // Previously we joined to _TOKEN_VALUES_V, but this caused performance issues due to poor
            // cardinality estimation (PostgreSQL) so to get around this, we grab the common_token_value_id
            // values first, then use these to join directly to _RESOURCE_TOKEN_REFS
            final String xxTokenValues = resourceType + "_RESOURCE_TOKEN_REFS";
            final String paramAlias = "P" + aliasIndex;
            final String lrAlias = "LR" + (aliasIndex-1);
            exists.from(xxTokenValues, alias(paramAlias))
            .where(paramAlias, "LOGICAL_RESOURCE_ID").eq(lrAlias, "LOGICAL_RESOURCE_ID") // correlate with the main query
            .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(parameterName))
            ;

            // add the filter predicate to the exists where clause
            ExpNode filter = getTokenFilter(queryParm, paramAlias).getExpression();
            exists.from().where().and(filter);
        } else {
            // A more complex for of token comparison. We can't predict how many matches
            // we'll get for the token-values, so we go old-school and just implement it
            // within the one join instead of fetching the COMMON_TOKEN_VALUE_IDs.
            final String xxTokenValues = resourceType + "_TOKEN_VALUES_V";
            final String paramAlias = "P" + aliasIndex;
            final String lrAlias = "LR" + (aliasIndex-1);
            exists.from(xxTokenValues, alias(paramAlias))
            .where(paramAlias, "LOGICAL_RESOURCE_ID").eq(lrAlias, "LOGICAL_RESOURCE_ID") // correlate with the main query
            .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(parameterName))
            ;

            // add the filter predicate to the exists where clause
            ExpNode filter = getComplexTokenFilter(queryParm, paramAlias).getExpression();
            exists.from().where().and(filter);
        }

        // Add the exists sub-query we just built to the where clause of the main query
        if (queryParm.getModifier() == Modifier.NOT || queryParm.getModifier() == Modifier.NOT_IN) {
            query.from().where().and().notExists(exists.build());
        } else {
            query.from().where().and().exists(exists.build());
        }
        // Return the exists sub-query in case there's a need to recurse lower
        return new QueryData(exists, aliasIndex, resourceType);
    }

    /**
     * Get the filter predicate for the given token query parameter
     * @param paramExists the exists select statement to which we need to AND the filter predicate
     * @param queryParm the token query parameter
     * @param paramAlias the alias used for the token values table
     * @throws FHIRPersistenceException
     */
    protected WhereFragment getTokenFilter(QueryParameter queryParm, String paramAlias) throws FHIRPersistenceException {
        final String parameterName = queryParm.getCode();

        WhereFragment where = new WhereFragment();
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("getTokenFilter: '" + parameterName + "'");
        }

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

            if (codeSystem == null || codeSystem.isEmpty() || codeSystem.equals("*")) {
                // Do not filter against code-system, which means we may have more than one matching
                // common_token_value_id value. For performance-reasons we join against the
                // xx_RESOURCE_TOKEN_REFS directly, which means we need a list of matching
                // COMMON_TOKEN_VALUE_ID values to specify in an in-list
                List<Long> ctvList = this.identityCache.getCommonTokenValueIdList(tokenValue);
                logger.fine("common_token_values('" + tokenValue + "') := " + ctvList.toString());
                if (ctvList.isEmpty()) {
                    // use -1...resulting in no data
                    where.col(paramAlias, "COMMON_TOKEN_VALUE_ID").eq(-1L);
                } else if (ctvList.size() == 1) {
                    where.col(paramAlias, "COMMON_TOKEN_VALUE_ID").eq(ctvList.get(0));
                } else {
                    where.col(paramAlias, "COMMON_TOKEN_VALUE_ID").inLiteralLong(ctvList);
                }
            } else {
                // should map to a single common_token_value_id, if it exists
                final Long commonTokenValueId = identityCache.getCommonTokenValueId(codeSystem, tokenValue);
                logger.fine("common_token_value_id('" + codeSystem + "', '" + tokenValue + "') := " + commonTokenValueId);
                where.col(paramAlias, "COMMON_TOKEN_VALUE_ID").eq(nullCheck(commonTokenValueId));
            }
        }
        where.rightParen();

        return where;
    }

    /**
     * Get the filter predicate for the given token query parameter. This variant
     * handles cases where operator is not a simple EQUALS.
     * @param paramExists the exists select statement to which we need to AND the filter predicate
     * @param queryParm the token query parameter
     * @param paramAlias the alias used for the token values table
     * @throws FHIRPersistenceException
     */
    protected WhereFragment getComplexTokenFilter(QueryParameter queryParm, String paramAlias) throws FHIRPersistenceException {
        final String parameterName = queryParm.getCode();
        final Operator operator = getOperator(queryParm);
        WhereFragment where = new WhereFragment();
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("getComplexTokenFilter: '" + parameterName + "'");
        }

        boolean first = true;
        where.leftParen();

        // Append the suffix for :text modifier
        String queryParmCode = queryParm.getCode();
        if (Modifier.TEXT.equals(queryParm.getModifier())) {
            queryParmCode += SearchConstants.TEXT_MODIFIER_SUFFIX;
        }

        for (QueryParameterValue value : queryParm.getValues()) {
            // If multiple values are present, we need to OR them together.
            if (first) {
                first = false;
            } else {
                where.or();
            }
            final String codeSystem = value.getValueSystem();
            final String tokenValue = value.getValueCode();

            // The expression may be complex, and we may need to OR them together. To avoid any
            // precedence drama, we simply wrap everything in parens just to be safe
            where.leftParen();

            if (Modifier.IN.equals(queryParm.getModifier()) || Modifier.NOT_IN.equals(queryParm.getModifier()) ||
                    Modifier.ABOVE.equals(queryParm.getModifier()) || Modifier.BELOW.equals(queryParm.getModifier())) {
                populateCodesSubSegment(where, queryParm.getModifier(), value, paramAlias);
            } else {
                // Include code
                where.col(paramAlias, "TOKEN_VALUE").operator(operator);
                if (operator == Operator.LIKE) {
                    // Must escape special wildcard characters _ and % in the parameter value string.
                    String textSearchString = SqlParameterEncoder.encode(value.getValueCode())
                            .replace(PERCENT_WILDCARD, ESCAPE_PERCENT)
                            .replace(UNDERSCORE_WILDCARD, ESCAPE_UNDERSCORE)
                            .replace("+", "++")+ PERCENT_WILDCARD;
                    where.bind(SearchUtil.normalizeForSearch(textSearchString)).escape("+");

                } else {
                    where.bind(SqlParameterEncoder.encode(value.getValueCode()));
                }

                // Include system if present.
                if (value.getValueSystem() != null && !value.getValueSystem().isEmpty()) {
                    Long commonTokenValueId = null;
                    if (operator == Operator.NE) {
                        where.or();
                    } else {
                        where.and();

                        // use #1929 optimization if we can
                        commonTokenValueId = identityCache.getCommonTokenValueId(value.getValueSystem(), value.getValueCode());
                    }

                    if (commonTokenValueId != null) {
                        // #1929 improves cardinality estimation
                        // resulting in far better execution plans for many search queries. Because COMMON_TOKEN_VALUE_ID
                        // is the primary key for the common_token_values table, we don't need the CODE_SYSTEM_ID = ? predicate.
                        where.col(paramAlias, COMMON_TOKEN_VALUE_ID).eq(commonTokenValueId);
                    } else {
                        // common token value not found so we can't use the optimization. Filter the code-system-id
                        // instead, which ends up being the logical equivalent.
                        Integer codeSystemId = identityCache.getCodeSystemId(value.getValueSystem());
                        where.col(paramAlias, CODE_SYSTEM_ID).operator(operator).literal(nullCheck(codeSystemId));
                    }
                }
            }

            where.rightParen();
        }

        where.rightParen();

        return where;
    }

    /**
     * Builds an SQL segment which populates an IN clause with codes for a token search parameter
     * specifying the :in, :not-in, :above, or :below modifier.
     *
     * @param whereClauseSegment  - the segment to which the sub-segment will be added
     * @param modifier            - the search parameter modifier (:in | :not-in | :above | :below)
     * @param parameterValue      - the search parameter value - a ValueSet URL or a CodeSystem URL + code
     * @param parameterTableAlias - the alias for the parameter table e.g. CPx
     * @throws FHIRPersistenceException
     */
    private void populateCodesSubSegment(WhereFragment whereClauseSegment, Modifier modifier,
            QueryParameterValue parameterValue, String parameterTableAlias) throws FHIRPersistenceException {

        boolean codeSystemProcessed = false;

        // Get the codes to populate the IN clause.
        // Note: validation of the value set or the code system + code specified in parameterValue
        // was done when the search parameter was parsed, so does not need to be done here.
        Map<String, Set<String>> codeSetMap = null;
        if (Modifier.IN.equals(modifier) || Modifier.NOT_IN.equals(modifier)) {
            codeSetMap = ValueSetSupport.getCodeSetMap(ValueSetSupport.getValueSet(parameterValue.getValueCode()));
        } else if (Modifier.ABOVE.equals(modifier) || Modifier.BELOW.equals(modifier)) {
            CodeSystem codeSystem = CodeSystemSupport.getCodeSystem(parameterValue.getValueSystem());
            Code code = Code.builder().value(parameterValue.getValueCode()).build();
            Set<String> codes;
            if (Modifier.ABOVE.equals(modifier)) {
                codes = CodeSystemSupport.getAncestorsAndSelf(codeSystem, code);
            } else {
                codes = CodeSystemSupport.getDescendantsAndSelf(codeSystem, code);
            }
            codeSetMap = Collections.singletonMap(parameterValue.getValueSystem(), codes);
        }

        // Build the SQL
        for (String codeSetUrl : codeSetMap.keySet()) {
            Set<String> codes = codeSetMap.get(codeSetUrl);
            if (codes != null) {
                // Strip version from canonical codeSet URL. We don't store version in TOKEN_VALUES
                // table so will just ignore it.
                int index = codeSetUrl.lastIndexOf("|");
                if (index != -1) {
                    codeSetUrl = codeSetUrl.substring(0, index);
                }

                if (codeSystemProcessed) {
                    whereClauseSegment.or();
                } else {
                    codeSystemProcessed = true;
                }

                // TODO: switch to use COMMON_TOKEN_VALUES support -dependent on issue #2184

                // <parameterTableAlias>.TOKEN_VALUE IN (...)
                whereClauseSegment.col(parameterTableAlias, TOKEN_VALUE).in(new ArrayList<>(codes));

                // AND <parameterTableAlias>.CODE_SYSTEM_ID = {n}
                whereClauseSegment.and().col(parameterTableAlias, CODE_SYSTEM_ID).eq(nullCheck(identityCache.getCodeSystemId(codeSetUrl)));
            }
        }
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

        // Add the (non-trivial) filter predicate for string parameters
        ExpNode filter = getStringFilter(queryParm, paramAlias).getExpression();
        exists.from().where().and(filter);

        // Add the exists to the where clause of the main query which already has a predicate
        // so we need to AND the exists
        queryData.getQuery().from().where().and().exists(exists.build());

        return new QueryData(exists, aliasIndex, resourceType);
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
            logger.fine("getStringFilter: " + parameterName + ", op=" + operator.name() + ", modifier=" + queryParm.getModifier());
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
                    searchValue = SearchUtil.normalizeForSearch(searchValue);
                    whereFragment.col(paramAlias, STR_VALUE_LCASE).like(bind(searchValue)).escape("+");
                } else {
                    // If there is not a CONTAINS modifier on the query parm, construct
                    // a 'starts with' search value.
                    String searchValue = tempSearchValue + PERCENT_WILDCARD;

                    // Specific processing for
                    if (queryParm.getModifier() != null && queryParm.getType() == Type.URI) {
                        if (queryParm.getModifier() == Modifier.BELOW) {
                            searchValue = tempSearchValue + "/" + PERCENT_WILDCARD;

                            whereFragment.leftParen()
                            .col(paramAlias, STR_VALUE).eq(bind(tempSearchValue))
                            .or(paramAlias, STR_VALUE).like(bind(searchValue)).escape("+")
                            .rightParen();

                        } else if (queryParm.getModifier() == Modifier.ABOVE) {
                            NewUriModifierUtil.generateAboveValuesQuery(whereFragment, paramAlias, STR_VALUE, searchValue, operator);
                        } else {
                            // neither above nor below, so an exact match for URI
                            whereFragment.col(paramAlias, STR_VALUE).eq(bind(searchValue));
                        }
                    } else {
                        // Simple STARTS WITH
                        searchValue = SearchUtil.normalizeForSearch(searchValue);
                        logger.fine("LIKE: " + searchValue);
                        whereFragment.col(paramAlias, STR_VALUE_LCASE).like(bind(searchValue)).escape("+");
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
                    .col(paramAlias, STR_VALUE).eq(bind(tempSearchValue))
                    .or(paramAlias, STR_VALUE).like(bind(searchValue)).escape("+")
                    .rightParen();

                } else if (queryParm.getModifier() == Modifier.ABOVE) {
                    String searchValue = SqlParameterEncoder.encode(value.getValueString());
                    NewUriModifierUtil.generateAboveValuesQuery(whereFragment, paramAlias, STR_VALUE, searchValue, operator);
                } else {
                    // neither above nor below, so an exact match for URI
                    String searchValue = SqlParameterEncoder.encode(value.getValueString());
                    whereFragment.col(paramAlias, STR_VALUE).eq(bind(searchValue));
                }
            } else if (operator == Operator.EQ) {
                // Exact match
                String searchValue = SqlParameterEncoder.encode(value.getValueString());
                whereFragment.col(paramAlias, STR_VALUE).eq(bind(searchValue));
            } else {
                // For anything other than an exact match, we search against the STR_VALUE_LCASE column in the
                // Resource's string values table.
                // Also, the search value is "normalized"; it has accents removed and is lower-cased. This enables a
                // case-insensitive, accent-insensitive search.
                // Build this piece: pX.str_value_lcase {operator} search-attribute-value
                String searchValue = SqlParameterEncoder.encode(value.getValueString());
                searchValue = SearchUtil.normalizeForSearch(searchValue);
                whereFragment.col(paramAlias, STR_VALUE_LCASE).operator(operator).bind(searchValue);
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
    public QueryData addMissingParam(QueryData queryData, QueryParameter queryParm, boolean isMissing) throws FHIRPersistenceException {
        // TODO. Simple implementation to get started
        // note that there's no filter here to look for a specific value. We simply want to know
        // whether or not the parameter exists for a given resource
        final String parameterName = queryParm.getCode();
        final int aliasIndex = queryData.getParameterAlias() + 1;
        final String resourceType = queryData.getResourceType();
        final String paramTableName = paramValuesTableName(resourceType, queryParm.getType());
        final String lrAlias = getLRAlias(aliasIndex-1);
        final String paramAlias = "P" + aliasIndex;

        SelectAdapter exists = Select.select("1");
        exists.from(paramTableName, alias(paramAlias))
                .where(paramAlias, "LOGICAL_RESOURCE_ID").eq(lrAlias, "LOGICAL_RESOURCE_ID") // correlate with the main query
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
        return new QueryData(exists, aliasIndex, resourceType);
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
            name.append("_RESOURCE_TOKEN_REFS"); // bypass the xx_TOKEN_VALUES_V for performance reasons
            break;
        case COMPOSITE:
            name.append("_LOGICAL_RESOURCES");
            break;
        }
        return name.toString();
    }

    @Override
    public QueryData addChained(QueryData queryData, QueryParameter currentParm) throws FHIRPersistenceException {
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
        final String sourceResourceType = queryData.getResourceType();
        final SelectAdapter currentSubQuery = queryData.getQuery();
        final int aliasIndex = queryData.getParameterAlias() + 1;
        final String targetResourceType = currentParm.getModifierResourceTypeName();
        final String tokenValues = sourceResourceType + "_TOKEN_VALUES_V"; // because we need TOKEN_VALUE
        final String xxLogicalResources = targetResourceType + "_LOGICAL_RESOURCES";
        final String paramAlias = "P" + aliasIndex;
        final String lrAlias = "LR" + aliasIndex;
        final String parentAlias = "LR" + (queryData.getParameterAlias());
        final Integer codeSystemIdForTargetResourceType = getCodeSystemId(targetResourceType);
        SelectAdapter exists = Select.select("1");
        exists.from(tokenValues, alias(paramAlias))
              .innerJoin(xxLogicalResources, alias(lrAlias),
                    on(lrAlias, "LOGICAL_ID").eq(paramAlias, "TOKEN_VALUE")
                    .and(lrAlias, "VERSION_ID").eq().coalesce(col(paramAlias, "REF_VERSION_ID"), col(lrAlias, "VERSION_ID"))
                    .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(currentParm.getCode()))
                    .and(paramAlias, "CODE_SYSTEM_ID").eq(nullCheck(codeSystemIdForTargetResourceType))
                    .and(lrAlias, "IS_DELETED").eq().literal("N")
                  )
              .where(paramAlias, "LOGICAL_RESOURCE_ID").eq(parentAlias, "LOGICAL_RESOURCE_ID") // correlate with the parent query
              ;

        // Add an exists clause to the where clause of the current query
        currentSubQuery.from().where().and().exists(exists.build());

        // Return the exists sub-select so that it can be used as the basis for the next
        // link in the chain
        return new QueryData(exists, aliasIndex, targetResourceType);
    }

    @Override
    public QueryData addReverseChained(QueryData queryData, QueryParameter currentParm) throws FHIRPersistenceException {
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
        final String refResourceType = queryData.getResourceType();
        final SelectAdapter currentSubQuery = queryData.getQuery();
        final int aliasIndex = queryData.getParameterAlias() + 1;
        final String resourceTypeName = currentParm.getModifierResourceTypeName();
        final String tokenValues = resourceTypeName + "_TOKEN_VALUES_V";
        final String xxLogicalResources = resourceTypeName + "_LOGICAL_RESOURCES";
        final String paramAlias = "P" + aliasIndex;
        final String lrAlias = "LR" + aliasIndex;
        final String parentAlias = "LR" + (aliasIndex-1);
        final Integer codeSystemIdForRefResourceType = getCodeSystemId(refResourceType);

        SelectAdapter exists = Select.select("1");
        exists.from(xxLogicalResources, alias(lrAlias))
              .innerJoin(tokenValues, alias(paramAlias),
                    on(lrAlias, "LOGICAL_RESOURCE_ID").eq(paramAlias, "LOGICAL_RESOURCE_ID")
                    .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(currentParm.getCode()))
                    .and(paramAlias, "CODE_SYSTEM_ID").eq(nullCheck(codeSystemIdForRefResourceType)))
              .where(parentAlias, "LOGICAL_ID").eq(paramAlias, "TOKEN_VALUE") // correlate with the main query
                .and(parentAlias, "VERSION_ID").eq().coalesce(col(paramAlias, "REF_VERSION_ID"), col(parentAlias, "VERSION_ID"))
              ;

        // Add an exists clause to the where clause of the current query
        currentSubQuery.from().where().and().exists(exists.build());

        // Return the exists sub-select so that it can be used as the basis for the next
        // link in the chain
        return new QueryData(exists, queryData.getParameterAlias()+1, resourceTypeName);
    }

    @Override
    public void addFilter(QueryData queryData, QueryParameter currentParm) throws FHIRPersistenceException {
        final SelectAdapter currentSubQuery = queryData.getQuery();
        final String code = currentParm.getCode();
        final String parentAlias = getLRAlias(queryData.getParameterAlias());

        if ("_id".equals(code)) {
            addIdFilter(queryData, currentParm);
        } else if ("_lastUpdated".equals(code)) {
            // Compute the _lastUpdated filter predicate for the given query parameter
            NewLastUpdatedParmBehaviorUtil util = new NewLastUpdatedParmBehaviorUtil(parentAlias);
            WhereFragment filter = new WhereFragment();
            util.executeBehavior(filter, currentParm);

            // Add the filter predicate to the where clause of the base query
            currentSubQuery.from().where().and(filter.getExpression());
        } else {
            // A simple filter added as an exists clause to the current query
            // AND EXISTS (SELECT 1
            //               FROM fhirdata.Patient_STR_VALUES AS P3                 -- 'Patient string parameters'
            //              WHERE P3.LOGICAL_RESOURCE_ID = LR2.LOGICAL_RESOURCE_ID  -- 'correlate to parent'
            //                AND P3.PARAMETER_NAME_ID = 123                        -- 'name parameter'
            //                AND P3.STR_VALUE = 'Jones')                           -- 'name filter'
            final int aliasIndex = queryData.getParameterAlias() + 1;
            final String paramTable = paramValuesTableName(queryData.getResourceType(), currentParm.getType());
            final String paramAlias = getParamAlias(aliasIndex);
            SelectAdapter exists = Select.select("1");
            exists.from(paramTable, alias(paramAlias))
                .where(paramAlias, "LOGICAL_RESOURCE_ID").eq(parentAlias, "LOGICAL_RESOURCE_ID")
                .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(currentParm.getCode()))
                .and(paramFilter(currentParm, paramAlias).getExpression());


            // Add an exists clause to the where clause of the current query
            if (currentParm.getModifier() == Modifier.NOT) {
                currentSubQuery.from().where().and().notExists(exists.build());
            } else {
                currentSubQuery.from().where().and().exists(exists.build());
            }
        }
    }

    /**
     * Get a simple filter predicate which can be used in the WHERE clause of a search query.
     * This is used at the "leaf level" of parameter processing, where the queryParm relates
     * to a single parameter (i.e. it is the caller's responsibility to handle chaining and
     * other more complex behavior.
     * @param queryData
     * @param queryParm
     * @return
     * @throws FHIRPersistenceException
     */
    protected WhereFragment getFilterPredicate(QueryData queryData, QueryParameter queryParm) throws FHIRPersistenceException {
        WhereFragment filter = new WhereFragment();

        final String code = queryParm.getCode();
        final String parentAlias = getLRAlias(queryData.getParameterAlias());

        if ("_id".equals(code)) {
            List<String> values = queryParm.getValues().stream().map(p -> p.getValueCode()).collect(Collectors.toList());
            if (values.size() == 1) {
                filter.col(parentAlias, "LOGICAL_ID").eq().bind(values.get(0));
            } else if (values.size() > 1) {
                // the values are converted to bind-markers, so this is secure
                filter.col(parentAlias, "LOGICAL_ID").in(values);
            } else {
                throw new FHIRPersistenceException("_id parameter value list is empty");
            }
        } else if ("_lastUpdated".equals(code)) {
            // Compute the _lastUpdated filter predicate for the given query parameter
            NewLastUpdatedParmBehaviorUtil util = new NewLastUpdatedParmBehaviorUtil(parentAlias);
            util.executeBehavior(filter, queryParm);
        } else {
            // A simple filter added as an exists clause to the current query
            // AND EXISTS (SELECT 1
            //               FROM fhirdata.Patient_STR_VALUES AS P3                 -- 'Patient string parameters'
            //              WHERE P3.LOGICAL_RESOURCE_ID = LR2.LOGICAL_RESOURCE_ID  -- 'correlate to parent'
            //                AND P3.PARAMETER_NAME_ID = 123                        -- 'name parameter'
            //                AND P3.STR_VALUE = 'Jones')                           -- 'name filter'
            final int aliasIndex = queryData.getParameterAlias() + 1;
            final String paramTable = paramValuesTableName(queryData.getResourceType(), queryParm.getType());
            final String paramAlias = getParamAlias(aliasIndex);
            SelectAdapter exists = Select.select("1");
            exists.from(paramTable, alias(paramAlias))
                .where(paramAlias, "LOGICAL_RESOURCE_ID").eq(parentAlias, "LOGICAL_RESOURCE_ID")
                .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(code))
                .and(paramFilter(queryParm, paramAlias).getExpression());
            filter.exists(exists.build());
        }

        return filter;
    }

    /**
     * Add a filter on the LOGICAL_ID for the given query parameter values
     * @param queryData
     */
    protected void addIdFilter(QueryData queryData, QueryParameter queryParm) throws FHIRPersistenceException {
        final SelectAdapter currentSubQuery = queryData.getQuery();
        final String parentAlias = getLRAlias(queryData.getParameterAlias());
        List<String> values = queryParm.getValues().stream().map(p -> p.getValueCode()).collect(Collectors.toList());
        if (values.size() == 1) {
            currentSubQuery.from().where().and(parentAlias, "LOGICAL_ID").eq().bind(values.get(0));
        } else if (values.size() > 1) {
            // the values are converted to bind-markers, so this is secure
            currentSubQuery.from().where().and(parentAlias, "LOGICAL_ID").in(values);
        } else {
            throw new FHIRPersistenceException("_id parameter value list is empty");
        }
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
        return new QueryData(exists, aliasIndex, resourceType);
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
        return new QueryData(exists, aliasIndex, resourceType);
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
        return new QueryData(exists, aliasIndex, resourceType);
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
        return new QueryData(exists, aliasIndex, resourceType);
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
        return new QueryData(exists, aliasIndex, resourceType);

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
            if (operator == Operator.EQ) {
                if (targetResourceType != null) {
                    // targetResourceType is treated as the code-system for references
                    // #1929 improves cardinality estimation
                    // resulting in far better execution plans for many search queries. Because COMMON_TOKEN_VALUE_ID
                    // is the primary key for the common_token_values table, we don't need the CODE_SYSTEM_ID = ? predicate.
                    Long commonTokenValueId = this.identityCache.getCommonTokenValueId(targetResourceType, searchValue);
                    whereClause.col(paramAlias, COMMON_TOKEN_VALUE_ID).eq(nullCheck(commonTokenValueId)); // use literal
                } else {
                    // grab the list of all matching common_token_value_id values
                    List<Long> ctvList = this.identityCache.getCommonTokenValueIdList(searchValue);
                    if (ctvList.isEmpty()) {
                        // use -1...resulting in no data
                        whereClause.col(paramAlias, "COMMON_TOKEN_VALUE_ID").eq(-1L);
                    } else if (ctvList.size() == 1) {
                        whereClause.col(paramAlias, "COMMON_TOKEN_VALUE_ID").eq(ctvList.get(0));
                    } else {
                        whereClause.col(paramAlias, "COMMON_TOKEN_VALUE_ID").inLiteralLong(ctvList);
                    }
                }
            } else {
                // inequality, so can't use discrete common_token_value_ids
                whereClause.col(paramAlias, TOKEN_VALUE).operator(operator).bind(searchValue);

                // add the [optional] condition for the resource type if we have one
                if (targetResourceType != null) {
                    // For better performance, use a literal for the resource type code-system-id, not a parameter marker
                    Integer codeSystemIdForResourceType = getCodeSystemId(targetResourceType);
                    whereClause.and(paramAlias, CODE_SYSTEM_ID).eq(nullCheck(codeSystemIdForResourceType));
                }
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
     * Use -1 as a simple substitute for null literal ids because we know -1 will never exist
     * as a value in the database (for fields populated by sequence values).
     * @param value
     * @return
     */
    private long nullCheck(Long value) {
        return value == null ? -1L : value;
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
    public QueryData addCompositeParam(QueryData queryData, QueryParameter queryParm) throws FHIRPersistenceException {
        final int aliasIndex = queryData.getParameterAlias() + 1;
        final String lrAlias = "LR" + (aliasIndex-1);

        // Each value gets its own EXISTS clause which we combine together
        // with OR. The whole thing needs to be wrapped in parens to ensure
        // the correct precedence.
        // AND ( EXISTS (...) OR EXISTS (...)
        final WhereAdapter where = queryData.getQuery().from().where();
        where.and().leftParen();
        boolean first = true;

        // Each query parm value gets its own EXISTS OR'd together
        for (QueryParameterValue compositeValue : queryParm.getValues()) {
            SelectAdapter exists = Select.select("1");

            List<QueryParameter> components = compositeValue.getComponent();
            for (int componentNum = 1; componentNum <= components.size(); componentNum++) {
                QueryParameter component = components.get(componentNum - 1);

                addParamTableToCompositeExists(exists, queryData.getResourceType(), lrAlias,
                    component, componentNum, true);
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
     * Build the composite join by adding the parameter table for the given
     * component number in the composite definition.
     * @param exists
     * @param resourceType
     * @param lrAlias
     * @param component
     * @param componentNum
     * @param addParamFilter
     * @throws FHIRPersistenceException
     */
    private void addParamTableToCompositeExists(SelectAdapter exists, String resourceType, String lrAlias,
        QueryParameter component, int componentNum, boolean addParamFilter) throws FHIRPersistenceException {
        String valuesTable = QuerySegmentAggregator.tableName(resourceType, component).strip();
        String componentTableAlias = "comp" + componentNum;
        String parameterName = component.getCode();

        if (componentNum == 1) {
            exists.from(valuesTable, alias(componentTableAlias))
            .where(componentTableAlias, "LOGICAL_RESOURCE_ID").eq(lrAlias, "LOGICAL_RESOURCE_ID") // correlate with the main query
            .and(componentTableAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(parameterName));

            if (addParamFilter) {
                exists.from().where().and(paramFilter(component, componentTableAlias).getExpression());
            }
        } else {
            // Join to the first parameter table
            final String firstTableAlias = "comp1";
            exists.from().innerJoin(valuesTable, alias(componentTableAlias),
                on(componentTableAlias, "LOGICAL_RESOURCE_ID").eq(firstTableAlias, "LOGICAL_RESOURCE_ID")
                .and(componentTableAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(parameterName))
                .and(componentTableAlias, "COMPOSITE_ID").eq(firstTableAlias, "COMPOSITE_ID"));

            // Parameter filter is skipped if this is coming from a missing/not missing search
            if (addParamFilter) {
                exists.from().where().and(paramFilter(component, componentTableAlias).getExpression());
            }
        }
    }

    @Override
    public QueryData addCompositeParam(QueryData queryData, QueryParameter queryParm, boolean isMissing) throws FHIRPersistenceException {
        final int aliasIndex = queryData.getParameterAlias() + 1;
        final String lrAlias = "LR" + (aliasIndex-1);

        // Each value gets its own EXISTS clause which we combine together
        // with OR. The whole thing needs to be wrapped in parens to ensure
        // the correct precedence.
        // AND ( EXISTS (...) OR EXISTS (...)
        final WhereAdapter where = queryData.getQuery().from().where();
        where.and().leftParen();
        boolean first = true;

        // Each query parm value gets its own EXISTS OR'd together
        for (QueryParameterValue compositeValue : queryParm.getValues()) {
            SelectAdapter exists = Select.select("1");

            List<QueryParameter> components = compositeValue.getComponent();
            for (int componentNum = 1; componentNum <= components.size(); componentNum++) {
                QueryParameter component = components.get(componentNum - 1);
                addParamTableToCompositeExists(exists, queryData.getResourceType(), lrAlias,
                    component, componentNum, false); // do not add param filter expression
            }

            // Add the exists sub-query we just built to the where clause of the main query
            if (first) {
                first = false;
            } else {
                where.or();
            }

            if (isMissing) {
                // parameter should be missing, i.e. not exist
                where.notExists(exists.build());
            } else {
                // parameter should be not missing...i.e. it exists
                where.exists(exists.build());
            }
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

    @Override
    public QueryData addInclusionParam(QueryData queryData, String resourceType, QueryParameter queryParm) throws FHIRPersistenceException {
        final WhereAdapter where = queryData.getQuery().from().where();
        where.and().leftParen();

        // TODO...chained stuff...but check out if this is still a requirement for R4
        QueryParameter currentParm = queryParm;
        while (currentParm != null) {
            // Add an exists clause for the given parameter
            WhereFragment filter = getFilterPredicate(queryData, queryParm);
            where.filter(filter.getExpression());

            currentParm = currentParm.getNextParameter();
            if (currentParm != null) {
                where.or();
            }
        }

        where.rightParen();
        return queryData;
    }

    @Override
    public QueryData addIncludeFilter(QueryData queryData, InclusionParameter inclusionParm, List<Long> logicalResourceIds) throws FHIRPersistenceException {
        // Add an exists filter matching the list of given ids
        /*
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
         *
         */
        // Versioned reference support. From the spec:
        // > If a resource has a reference that is versioned and _include is performed,
        // > the specified version SHOULD be provided.

        final String joinResourceType = inclusionParm.getJoinResourceType();
        final String targetResourceType = inclusionParm.getSearchParameterTargetType();
        final int aliasIndex = queryData.getParameterAlias() + 1;
        final SelectAdapter query = queryData.getQuery();
        final String tokenValues = joinResourceType + "_TOKEN_VALUES_V";
        final String paramAlias = getParamAlias(aliasIndex);
        final String parentLRAlias = getLRAlias(aliasIndex-1);
        final String parentRAlias = "R" + (aliasIndex-1);
        SelectAdapter exists = Select.select("1");
        exists.from(tokenValues, alias(paramAlias))
            .where(parentLRAlias, "LOGICAL_ID").eq(paramAlias, "TOKEN_VALUE")
            .and().coalesce(col(paramAlias, "REF_VERSION_ID"), col(parentLRAlias, "VERSION_ID")).eq(parentRAlias, "VERSION_ID")
            .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(inclusionParm.getSearchParameter()))
            .and(paramAlias, "CODE_SYSTEM_ID").eq(getCodeSystemId(targetResourceType))
            .and(paramAlias, "LOGICAL_RESOURCE_ID").inLiteralLong(logicalResourceIds)
            ;

        // Add the exists sub-query we just built to the where clause of the main query
        query.from().where().and().exists(exists.build());

        return queryData;
    }

    @Override
    public QueryData addRevIncludeFilter(QueryData queryData, InclusionParameter inclusionParm, List<Long> logicalResourceIds) throws FHIRPersistenceException {
        /*  old query
        *   EXISTS (SELECT 1 FROM
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
        */
        final String joinResourceType = inclusionParm.getJoinResourceType();
        final String targetResourceType = inclusionParm.getSearchParameterTargetType();
        final int aliasIndex = queryData.getParameterAlias() + 1;
        final SelectAdapter query = queryData.getQuery();
        final String tokenValues = joinResourceType + "_TOKEN_VALUES_V";
        final String targetLR = targetResourceType + "_LOGICAL_RESOURCES";
        final String paramAlias = getParamAlias(aliasIndex);
        final String lrAlias = getLRAlias(aliasIndex);
        final String parentLRAlias = getLRAlias(aliasIndex-1);
        SelectAdapter exists = Select.select("1");
        exists.from(tokenValues, alias(paramAlias))
            .innerJoin(targetLR, alias(lrAlias), on(lrAlias, "LOGICAL_ID").eq(paramAlias, "TOKEN_VALUE")
                .and().coalesce(col(paramAlias, "REF_VERSION_ID"), col(lrAlias, "VERSION_ID")).eq(lrAlias, "VERSION_ID")
                .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(inclusionParm.getSearchParameter()))
                .and(paramAlias, "CODE_SYSTEM_ID").eq(getCodeSystemId(targetResourceType))
                .and(lrAlias, "LOGICAL_RESOURCE_ID").inLiteralLong(logicalResourceIds)
                .and(lrAlias, "IS_DELETED").eq().literal("N"))
            .where(parentLRAlias, "LOGICAL_RESOURCE_ID").eq(paramAlias, "LOGICAL_RESOURCE_ID") // correlate with the main query
            ;

        // Add the exists sub-query we just built to the where clause of the main query
        query.from().where().and().exists(exists.build());

        return queryData;
    }
}