/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.domain;

import static com.ibm.fhir.database.utils.query.expression.ExpressionSupport.alias;
import static com.ibm.fhir.database.utils.query.expression.ExpressionSupport.bind;
import static com.ibm.fhir.database.utils.query.expression.ExpressionSupport.col;
import static com.ibm.fhir.database.utils.query.expression.ExpressionSupport.on;
import static com.ibm.fhir.database.utils.query.expression.ExpressionSupport.string;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.CODE_SYSTEM_ID;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.COMMON_TOKEN_VALUE_ID;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.DATE_START;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.EQ;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.ESCAPE_PERCENT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.ESCAPE_UNDERSCORE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.IS_DELETED;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LEFT_PAREN;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.MAX;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.MIN;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.NUMBER_VALUE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.PERCENT_WILDCARD;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.QUANTITY_VALUE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.RIGHT_PAREN;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.TOKEN_VALUE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.UNDERSCORE_WILDCARD;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants._LOGICAL_RESOURCES;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants._RESOURCES;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.database.utils.common.DataDefinitionUtil;
import com.ibm.fhir.database.utils.query.Operator;
import com.ibm.fhir.database.utils.query.Select;
import com.ibm.fhir.database.utils.query.SelectAdapter;
import com.ibm.fhir.database.utils.query.WhereAdapter;
import com.ibm.fhir.database.utils.query.WhereFragment;
import com.ibm.fhir.database.utils.query.expression.ColumnExpNodeVisitor;
import com.ibm.fhir.database.utils.query.expression.StringExpNodeVisitor;
import com.ibm.fhir.database.utils.query.node.ExpNode;
import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceNotSupportedException;
import com.ibm.fhir.persistence.jdbc.dao.api.JDBCIdentityCache;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceProfileRec;
import com.ibm.fhir.persistence.jdbc.util.CanonicalSupport;
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
import com.ibm.fhir.search.sort.Sort.Direction;
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
    private static final String CLASSNAME = SearchQueryRenderer.class.getName();
    private static final Logger logger = Logger.getLogger(CLASSNAME);

    private final static String STR_VALUE = "STR_VALUE";
    private final static String STR_VALUE_LCASE = "STR_VALUE_LCASE";

    // A cache providing access to various database reference ids
    private final JDBCIdentityCache identityCache;

    // pagination page number
    private final int rowOffset;

    // pagination page size
    private final int rowsPerPage;

    // Counter so we can allocate unique alias names
    private int paramCounter = 0;

    /**
     * Public constructor
     * @param identityCache
     * @param rowOffset
     * @param rowsPerPage
     */
    public SearchQueryRenderer(JDBCIdentityCache identityCache,
        int rowOffset, int rowsPerPage) {
        this.identityCache = identityCache;
        this.rowOffset = rowOffset;
        this.rowsPerPage = rowsPerPage;
    }

    /**
     * Get the next index number to use as a parameter table alias
     * @return
     */
    protected int getNextAliasIndex() {
        return ++paramCounter;
    }

    /**
     * Get the table name for the xx_logical_resources table where xx is the
     * resource type name
     * @param resourceType
     * @return the table name
     */
    protected String resourceLogicalResources(String resourceType) {
        return resourceType + _LOGICAL_RESOURCES;
    }

    /**
     * Get the table name for the xx_resources table where xx is the resource type name
     * @param resourceType
     * @return
     */
    protected String resourceResources(String resourceType) {
        return resourceType + _RESOURCES;
    }

    /**
     * Get the id for the given parameter name (cache lookup)
     * @param parameterName
     * @return
     */
    protected int getParameterNameId(String parameterName) throws FHIRPersistenceException {
        return this.identityCache.getParameterNameId(parameterName);
    }

    /**
     * Get the common token value id matching the unique tuple {system, code}
     * @param system
     * @param code
     * @return
     * @throws FHIRPersistenceException
     */
    protected Long getCommonTokenValueId(String system, String code) throws FHIRPersistenceException {
        return this.identityCache.getCommonTokenValueId(system, code);
    }

    /**
     * Get a list of common token values matching the given code
     * @param code
     * @return
     * @throws FHIRPersistenceException
     */
    protected List<Long> getCommonTokenValueIdList(String code) throws FHIRPersistenceException {
        return this.identityCache.getCommonTokenValueIdList(code);
    }

    /**
     * Get the id for the given code system name (cache lookup)
     * @param codeSystemName
     * @return
     * @throws FHIRPersistenceException
     */
    protected int getCodeSystemId(String codeSystemName) throws FHIRPersistenceException {
        return this.identityCache.getCodeSystemId(codeSystemName);
    }

    protected int getCanonicalId(String canonicalValue) throws FHIRPersistenceException {
        return this.identityCache.getCanonicalId(canonicalValue);
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
        /*
          SELECT COUNT(*)
            FROM Patient_LOGICAL_RESOURCES AS LR0
           WHERE LR0.IS_DELETED = 'N'
             AND EXISTS (
          SELECT 1
            FROM Patient_LOGICAL_RESOURCES AS LR1
      INNER JOIN Patient_STR_VALUES AS P2 ON P2.LOGICAL_RESOURCE_ID = LR1.LOGICAL_RESOURCE_ID
             AND P2.PARAMETER_NAME_ID = 1246
             AND (P2.STR_VALUE = ?)
           WHERE LR1.IS_DELETED = 'N'
             AND LR1.LOGICAL_RESOURCE_ID = LR0.LOGICAL_RESOURCE_ID)
         */
        SelectAdapter select = Select.select("COUNT(*)");
        select.from(xxLogicalResources, alias(lrAliasName))
            .where(lrAliasName, IS_DELETED).eq(string("N"));
        return new QueryData(select, lrAliasName, null, rootResourceType, 0);
    }

    @Override
    public QueryData dataRoot(String rootResourceType) {
        /*
        // The data root query is formed as an inner select statement which we
        // then inner join to the xx_RESOURCES table as a final step. This is
        // crucial to enable the optimizer to generate the correct plan.
        // The final query looks something like this:
              SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID, R.LAST_UPDATED, R.IS_DELETED, R.DATA, LR.LOGICAL_ID
                FROM (
              SELECT LR0.LOGICAL_RESOURCE_ID, LR0.LOGICAL_ID, LR0.CURRENT_RESOURCE_ID
                FROM Patient_LOGICAL_RESOURCES AS LR0
               WHERE LR0.IS_DELETED = 'N'
                 AND EXISTS (
              SELECT 1
                FROM Patient_LOGICAL_RESOURCES AS LR1
          INNER JOIN Patient_STR_VALUES AS P2 ON P2.LOGICAL_RESOURCE_ID = LR1.LOGICAL_RESOURCE_ID
                 AND P2.PARAMETER_NAME_ID = 1246
                 AND (P2.STR_VALUE = ?)
               WHERE LR1.IS_DELETED = 'N'
                 AND LR1.LOGICAL_RESOURCE_ID = LR0.LOGICAL_RESOURCE_ID)) AS LR
          INNER JOIN Patient_RESOURCES AS R ON LR.CURRENT_RESOURCE_ID = R.RESOURCE_ID
            ORDER BY LR.LOGICAL_RESOURCE_ID
         FETCH FIRST 10 ROWS ONLY
        */
        final String xxLogicalResources = resourceLogicalResources(rootResourceType);
        final String lrAliasName = "LR0";

        // The core data query joining together the logical resources table. Query
        // parameters are bolted on as exists statements in the WHERE clause. The final
        // query is constructed when joinResources is called.
        SelectAdapter select = Select.select("LR0.LOGICAL_RESOURCE_ID", "LR0.LOGICAL_ID", "LR0.CURRENT_RESOURCE_ID");
        select.from(xxLogicalResources, alias(lrAliasName))
            .where(lrAliasName, IS_DELETED).eq().literal("N");
        return new QueryData(select, lrAliasName, null, rootResourceType, 0);
    }

    @Override
    public QueryData getParameterBaseQuery(QueryData parent) {
        final int aliasIndex = getNextAliasIndex();
        final String xxLogicalResources = parent.getResourceType() + "_LOGICAL_RESOURCES";
        final String lrAlias = "LR" + aliasIndex;
        final String parentLRAlias = parent.getLRAlias();

        // SELECT 1 FROM xx_LOGICAL_RESOURCES LRn
        //    INNER JOIN ...
        //    INNER JOIN ...
        //         WHERE LRn.IS_DELETED = 'N'
        //           AND LRn.LOGICAL_RESOURCE_ID = LRp.LOGICAL_RESOURCE_ID
        SelectAdapter exists = Select.select("1");
        exists.from(xxLogicalResources, alias(lrAlias))
         .where(lrAlias, "IS_DELETED").eq().literal("N") // TODO remove either from here or parent
         .and(lrAlias, "LOGICAL_RESOURCE_ID").eq(parentLRAlias, "LOGICAL_RESOURCE_ID"); // correlate to parent query

        // Add this exists to the parent query
        parent.getQuery().from().where().and().exists(exists.build());

        // This bit is important to understanding how this works. We return the
        // sub-query here, not the main query. The sub-query is returned because
        // it is the query to which we attach all the parameter table joins
        return new QueryData(exists, lrAlias, null, parent.getResourceType(), 0);
    }

    @Override
    public QueryData joinResources(QueryData queryData) {
        final SelectAdapter logicalResources = queryData.getQuery();
        final String xxResources = resourceResources(queryData.getResourceType());
        final String lrAliasName = "LR";
        SelectAdapter select = Select.select("R.RESOURCE_ID", "R.LOGICAL_RESOURCE_ID", "R.VERSION_ID", "R.LAST_UPDATED", "R.IS_DELETED", "R.DATA", "LR.LOGICAL_ID");
        select.from(logicalResources.build(), alias(lrAliasName))
            .innerJoin(xxResources, alias("R"), on(lrAliasName, "CURRENT_RESOURCE_ID").eq("R", "RESOURCE_ID"));

        // The final query still needs ordering/pagination to be applied
        return new QueryData(select, lrAliasName, null, queryData.getResourceType(), queryData.getChainDepth());
    }

    @Override
    public QueryData includeRoot(String rootResourceType) {

        /* Final query should like this:
        SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID, R.LAST_UPDATED, R.IS_DELETED, R.DATA, LR.LOGICAL_ID
                FROM (
              SELECT LR0.LOGICAL_RESOURCE_ID, LR0.LOGICAL_ID, LR0.CURRENT_RESOURCE_ID
                FROM Patient_LOGICAL_RESOURCES AS LR0
               WHERE LR0.IS_DELETED = 'N'
                 AND EXISTS (
              SELECT 1
                FROM Patient_LOGICAL_RESOURCES AS LR1
          INNER JOIN Patient_STR_VALUES AS P2 ON P2.LOGICAL_RESOURCE_ID = LR1.LOGICAL_RESOURCE_ID
                 AND P2.PARAMETER_NAME_ID = 1246
                 AND (P2.STR_VALUE = ?)
               WHERE LR1.IS_DELETED = 'N'
                 AND LR1.LOGICAL_RESOURCE_ID = LR0.LOGICAL_RESOURCE_ID)) AS LR
          INNER JOIN Patient_RESOURCES AS R ON LR.CURRENT_RESOURCE_ID = R.RESOURCE_ID
            ORDER BY LR.LOGICAL_RESOURCE_ID
         FETCH FIRST 10 ROWS ONLY
         */

        // The root query is just the inner distinct piece. The overall query is built by wrapInclude
        final boolean distinct = true;
        SelectAdapter select = Select.select(distinct, "R0.RESOURCE_ID", "R0.LOGICAL_RESOURCE_ID", "R0.VERSION_ID", "R0.LAST_UPDATED", "R0.IS_DELETED", "LR0.LOGICAL_ID");
        return new QueryData(select, null, null, rootResourceType, 0);
    }

    @Override
    public QueryData wrapInclude(QueryData query) {
        // Need to join the RESOURCES table again to get the DATA column after the DISTINCT.
        final String lrAlias = "LR";
        final String rAlias = "R";
        final String rTable = query.getResourceType() + "_RESOURCES";
        SelectAdapter select = Select.select("LR.RESOURCE_ID", "LR.LOGICAL_RESOURCE_ID", "LR.VERSION_ID", "LR.LAST_UPDATED", "LR.IS_DELETED", "R.DATA", "LR.LOGICAL_ID");
        select.from(query.getQuery().build(), alias(lrAlias))
            .innerJoin(rTable, alias(rAlias), on(lrAlias, "RESOURCE_ID").eq(rAlias, "RESOURCE_ID"));
        return new QueryData(select, lrAlias, null, query.getResourceType(), 0);
    }

    @Override
    public QueryData sortRoot(String rootResourceType) {
        final String xxLogicalResources = resourceLogicalResources(rootResourceType);
        final String lrAliasName = "LR0";

        // The core data query joining together the logical resources table. Query
        // parameters are bolted on as exists statements in the WHERE clause. The final
        // query is constructed when joinResources is called.
        SelectAdapter select = Select.select("LR0.CURRENT_RESOURCE_ID");
        select.from(xxLogicalResources, alias(lrAliasName))
            .where(lrAliasName, IS_DELETED).eq().literal("N");

        // We need to group the sort parameters to address any duplicates
        select.from().groupBy("LR0.CURRENT_RESOURCE_ID");
        return new QueryData(select, lrAliasName, null, rootResourceType, 0);
    }

    /**
     * Get the filter predicate for the given token query parameter.
     * @param queryParm the token query parameter
     * @param paramAlias the alias used for the token values table
     * @throws FHIRPersistenceException
     */
    protected WhereFragment getTokenFilter(QueryParameter queryParm, String paramAlias) throws FHIRPersistenceException {
        final Operator operator = getOperator(queryParm, EQ);
        WhereFragment where = new WhereFragment();

        boolean first = true;
        where.leftParen();

        // Append the suffix for :text modifier
        String parameterName = queryParm.getCode();
        if (Modifier.TEXT.equals(queryParm.getModifier())) {
            parameterName += SearchConstants.TEXT_MODIFIER_SUFFIX;
        }

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("getTokenFilter: '" + parameterName + "'" + ", Operator: " + operator + ", modifier: " + queryParm.getModifier());
        }

        for (QueryParameterValue value : queryParm.getValues()) {
            // If multiple values are present, we need to OR them together.
            if (first) {
                first = false;
            } else {
                where.or();
            }

            // The expression may be complex, and we may need to OR them together. To avoid any
            // precedence drama, we simply wrap everything in parens just to be safe
            where.leftParen();

            if (Modifier.IN.equals(queryParm.getModifier()) || Modifier.NOT_IN.equals(queryParm.getModifier()) ||
                    Modifier.ABOVE.equals(queryParm.getModifier()) || Modifier.BELOW.equals(queryParm.getModifier())) {
                populateCodesSubSegment(where, queryParm.getModifier(), value, paramAlias);
            } else {
                final String system = value.getValueSystem() != null && !value.getValueSystem().isEmpty() ? value.getValueSystem() : null;
                final String code = value.getValueCode() != null ? value.getValueCode() : null; // empty code is a valid value

                // Determine code normalization based on code system case-sensitivity
                String normalizedCode = null;
                if (code != null) {
                    if (system != null) {
                        boolean codeSystemIsCaseSensitive = CodeSystemSupport.isCaseSensitive(system);
                        normalizedCode = SqlParameterEncoder.encode(codeSystemIsCaseSensitive ?
                                            code : SearchUtil.normalizeForSearch(code));
                    } else {
                        normalizedCode = SqlParameterEncoder.encode(SearchUtil.normalizeForSearch(code));
                    }
                }

                // Include code
                if (operator == Operator.EQ && code != null) {
                    if (system == null || system.equals("*")) {
                        // Even though we don't have a system, we can still use a list of
                        // common_token_value_ids matching the value-code, allowing a similar optimization
                        Set<Long> ctvs = new HashSet<>();
                        fetchCommonTokenValues(ctvs, SqlParameterEncoder.encode(code));
                        fetchCommonTokenValues(ctvs, SqlParameterEncoder.encode(SearchUtil.normalizeForSearch(code)));
                        addCommonTokenValueIdFilter(where, paramAlias, ctvs);
                    } else {
                        Long commonTokenValueId = identityCache.getCommonTokenValueId(system, normalizedCode);
                        where.col(paramAlias, COMMON_TOKEN_VALUE_ID).eq(nullCheck(commonTokenValueId));
                    }
                } else {
                    // Traditional approach, using a join to xx_TOKEN_VALUES_V

                    // Include code if present
                    if (code != null) {
                        where.col(paramAlias, TOKEN_VALUE).operator(operator);
                        if (operator == Operator.LIKE) {
                            // Must escape special wildcard characters _ and % in the parameter value string.
                            String textSearchString = normalizedCode
                                    .replace(PERCENT_WILDCARD, ESCAPE_PERCENT)
                                    .replace(UNDERSCORE_WILDCARD, ESCAPE_UNDERSCORE)
                                    .replace("+", "++")+ PERCENT_WILDCARD;
                            where.bind(SearchUtil.normalizeForSearch(textSearchString)).escape("+");

                        } else {
                            where.bind(normalizedCode);
                        }
                    }

                    // Include system if present
                    if (system != null) {
                        if (code != null) {
                            where.and();
                        }

                        // Filter on the code system for the given parameter
                        Integer codeSystemId = identityCache.getCodeSystemId(system);
                        where.col(paramAlias, CODE_SYSTEM_ID).eq().literal(nullCheck(codeSystemId));
                    }
                }
            }

            where.rightParen();
        }

        where.rightParen();

        return where;
    }

    /**
     * Adds a filter predicate for COMMON_TOKEN_VALUE_ID. Fetches the list of possible matches (there's no code-system,
     * so there could be multiple). If no match, then -1 is used to make sure the row isn't produced. If there is a
     * single match, the predicate is COMMON_TOKEN_VALUE_ID = {n}. If there are multiple matches, the predicate is
     * COMMON_TOKEN_VALUE_ID IN (1, 2, 3, ...).
     * The query uses literal values not bind variables on purpose (better performance).
     * @param where
     * @param paramAlias
     * @param searchValue
     * @throws FHIRPersistenceException
     */
    private void addCommonTokenValueIdFilter(WhereFragment where, String paramAlias, String searchValue) throws FHIRPersistenceException {
        // grab the list of all matching common_token_value_id values
        Set<Long> ctvs = new HashSet<>();
        fetchCommonTokenValues(ctvs, searchValue);

        // and add a filter expression paramAlias IN (...) for the values
        addCommonTokenValueIdFilter(where, paramAlias, ctvs);
    }

    /**
     * Add all common_token_value_id matching the given searchValue to the ctvs set.
     * @param ctvs
     * @param searchValue
     * @throws FHIRPersistenceException
     */
    private void fetchCommonTokenValues(Set<Long> ctvs, String searchValue) throws FHIRPersistenceException {
        List<Long> ctvList = this.identityCache.getCommonTokenValueIdList(searchValue);
        ctvs.addAll(ctvList);
    }

    /**
     * Adds a filter predicate for COMMON_TOKEN_VALUE_ID. If tje ctvs list is empty, then -1 is used to make
     * sure the row isn't produced. If there is a single match, the predicate is COMMON_TOKEN_VALUE_ID = {n}.
     * If there are multiple matches, the predicate is COMMON_TOKEN_VALUE_ID IN (1, 2, 3, ...).
     * The query uses literal values not bind variables on purpose (better performance).
     * @param where
     * @param paramAlias
     * @param ctvs
     * @throws FHIRPersistenceException
     */
    private void addCommonTokenValueIdFilter(WhereFragment where, String paramAlias, Collection<Long> ctvs) throws FHIRPersistenceException {
        final List<Long> ctvList = new ArrayList<>(ctvs);
        if (ctvList.isEmpty()) {
            // use -1...resulting in no data
            where.col(paramAlias, "COMMON_TOKEN_VALUE_ID").eq(-1L);
        } else if (ctvList.size() == 1) {
            where.col(paramAlias, "COMMON_TOKEN_VALUE_ID").eq(ctvList.get(0));
        } else {
            where.col(paramAlias, "COMMON_TOKEN_VALUE_ID").inLiteralLong(ctvList);
        }
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
    public QueryData addSorting(QueryData queryData, String lrAlias) {
        final String lrLogicalResourceId = DataDefinitionUtil.getQualifiedName(lrAlias, "LOGICAL_RESOURCE_ID");
        queryData.getQuery().from().orderBy(lrLogicalResourceId);
        return queryData;
    }

    @Override
    public QueryData addPagination(QueryData queryData) {
        queryData.getQuery().pagination(rowOffset, rowsPerPage);
        return queryData;
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

    /**
     * Get the column name to use for the given paramType
     * @param paramType
     * @return
     */
    public String paramValuesColumnName(Type paramType) {
        final String result;
        switch (paramType) {
        case URI:
        case STRING:
            result = "STR_VALUES";
            break;
        case NUMBER:
            result = "NUMBER_VALUE";
            break;
        case QUANTITY:
            result = "QUANTITY_VALUE";
            break;
        case DATE:
            result = "DATE_VALUE";
            break;
        case SPECIAL:
            result = "LATLNG_VALUES";
            break;
        case REFERENCE:
        case TOKEN:
            result = "TOKEN_VALUE";
            break;
        case COMPOSITE:
            result = null;
            break;
        default:
            result = null;
        }
        return result;
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
        final String parentAlias = queryData.getLRAlias();

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
            final int aliasIndex = getNextAliasIndex();
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
     * @param queryParm
     */
    protected void addIdFilter(QueryData queryData, QueryParameter queryParm) throws FHIRPersistenceException {
        final SelectAdapter currentSubQuery = queryData.getQuery();
        final String parentAlias = queryData.getLRAlias();
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

    /**
     * Get a filter predicate for the given number query parameter
     * @param queryParm
     * @param paramAlias
     */
    protected WhereFragment getNumberFilter(QueryParameter queryParm, String paramAlias) throws FHIRPersistenceException {
        WhereFragment where = new WhereFragment();
        NewNumberParmBehaviorUtil.executeBehavior(where, queryParm, paramAlias);
        return where;
    }

    /**
     * Add a filter predicate to the given exists sub-query
     * @param queryParm
     * @param paramAlias
     */
    protected WhereFragment getQuantityFilter(QueryParameter queryParm, String paramAlias) throws FHIRPersistenceException {
        WhereFragment where = new WhereFragment();
        NewQuantityParmBehaviorUtil behaviorUtil = new NewQuantityParmBehaviorUtil(this.identityCache);
        behaviorUtil.executeBehavior(where, queryParm, paramAlias);
        return where;
    }

    /**
     * Add a filter predicate to the given exists sub-query
     * @param queryParm
     * @param paramAlias
     */
    protected WhereFragment getDateFilter(QueryParameter queryParm, String paramAlias) {
        WhereFragment where = new WhereFragment();
        NewDateParmBehaviorUtil util = new NewDateParmBehaviorUtil();
        util.executeBehavior(where, queryParm, paramAlias);
        return where;
    }

    /**
     * Add a filter predicate to the given exists sub-query
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
    protected String getParamAlias(int aliasIndex) {
        return "P" + aliasIndex;
    }

    /**
     * Get the string to use as a logical resource alias for the given aliasIndex value
     * @param aliasIndex
     * @return
     */
    protected String getLRAlias(int aliasIndex) {
        return "LR" + aliasIndex;
    }

    /**
     * Compute the token parameter table name we want to use to join with. This method
     * inspects the content of the given filter {@link ExpNode}. If the filter contains
     * a reference to the TOKEN_VALUE column, the returned table name will be based
     * on xx_TOKEN_VALUES_V, otherwise it will be based on xx_RESOURCE_TOKEN_REFS. The
     * latter is preferable because it eliminates an unnecessary join, improves cardinality
     * estimation and (usually) results in a better execution plan.
     * @param filter
     * @param resourceType
     * @param paramAlias
     * @return
     */
    protected String getTokenParamTable(ExpNode filter, String resourceType, String paramAlias) {
        ColumnExpNodeVisitor visitor = new ColumnExpNodeVisitor(); // gathers all columns used in the filter expression
        Set<String> columns = filter.visit(visitor);
        boolean usesTokenValue = columns.contains(DataDefinitionUtil.getQualifiedName(paramAlias, TOKEN_VALUE)) ||
                                    columns.contains(DataDefinitionUtil.getQualifiedName(paramAlias, CODE_SYSTEM_ID));

        final String xxTokenValues;
        if (usesTokenValue) {
            // can't optimize because we filter on TOKEN_VALUE
            xxTokenValues = resourceType + "_TOKEN_VALUES_V";
        } else {
            // only filters on COMMON_TOKEN_VALUE_ID so we can optimize
            xxTokenValues = resourceType + "_RESOURCE_TOKEN_REFS";
        }
        return xxTokenValues;
    }

    /**
     * Create a filter predicate for the given reference query parameter
     * @param queryParm
     * @param paramAlias
     * @throws FHIRPersistenceException
     */
    protected WhereFragment getReferenceFilter(QueryParameter queryParm, String paramAlias) throws FHIRPersistenceException {
        WhereFragment whereClause = new WhereFragment();
        whereClause.leftParen();
        Operator operator = getOperator(queryParm, EQ);

        boolean parmValueProcessed = false;
        for (QueryParameterValue value : queryParm.getValues()) {
            // If multiple values are present, we need to OR them together.
            if (parmValueProcessed) {
                whereClause.or();
            } else {
                parmValueProcessed = true;
            }

            String targetResourceType = null;
            if (Modifier.IDENTIFIER.equals(queryParm.getModifier())) {
                // Determine code system case-sensitivity
                boolean codeSystemIsCaseSensitive = false;
                if (value.getValueSystem() != null && !value.getValueSystem().isEmpty()) {

                    // Normalize code if code system is not case-sensitive. Otherwise leave code as is.
                    codeSystemIsCaseSensitive = CodeSystemSupport.isCaseSensitive(value.getValueSystem());
                    final String searchValue = SqlParameterEncoder.encode(codeSystemIsCaseSensitive ?
                            value.getValueCode() : SearchUtil.normalizeForSearch(value.getValueCode()));

                    // We have a code-system and a code so we must have a common_token_value if the tuple exists
                    Long commonTokenValueId = getCommonTokenValueId(value.getValueSystem(), searchValue);
                    whereClause.col(paramAlias, COMMON_TOKEN_VALUE_ID).eq(nullCheck(commonTokenValueId)); // use literal
                } else {
                    // No code system specified, search against both normalized code and unmodified code.
                    // Build equivalent of: pX.token_value IN (search-attribute-value, normalized-search-sttribute-value)
                    final String normalizedValue = SearchUtil.normalizeForSearch(value.getValueCode());
                    Set<Long> ctvs = new HashSet<>();
                    fetchCommonTokenValues(ctvs, value.getValueCode());
                    fetchCommonTokenValues(ctvs, normalizedValue);
                    addCommonTokenValueIdFilter(whereClause, paramAlias, ctvs);
                }
            } else {
                String searchValue = SqlParameterEncoder.encode(value.getValueString());

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
                        addCommonTokenValueIdFilter(whereClause, paramAlias, searchValue);
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
    protected int nullCheck(Integer value) {
        return value == null ? -1 : value;
    }

    /**
     * Use -1 as a simple substitute for null literal ids because we know -1 will never exist
     * as a value in the database (for fields populated by sequence values).
     * @param value
     * @return
     */
    protected long nullCheck(Long value) {
        return value == null ? -1L : value;
    }

    /**
     * Get the operator we need to use for matching values for this parameter
     * @param queryParameter
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

    /**
     * Get the filter predicate expression for the given query parameter taking into account its type,
     * modifiers etc.
     * @param paramTableAlias
     * @param queryParm
     * @return a valid expression
     */
    protected WhereFragment paramFilter(QueryParameter queryParm, String paramTableAlias) throws FHIRPersistenceException {
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
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("buildLocationQuerySegment no longer needed");
            }
        }

        return null;
    }

    @Override
    public QueryData addInclusionParam(QueryData queryData, String resourceType, QueryParameter queryParm) throws FHIRPersistenceException {

        QueryParameter currentParm = queryParm;
        if (queryParm.getNextParameter() == null) {
            // just a single inclusion parameter, so we can optimize and treat as a simple join
            // to the main parameter filter block
            addFilter(queryData, queryParm);
        } else {
            // Attach a series of exists clauses to the parameter query block
            final WhereAdapter where = queryData.getQuery().from().where();
            where.and().leftParen();
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
        }
        return queryData;
    }

    @Override
    public QueryData addIncludeFilter(QueryData queryData, InclusionParameter inclusionParm, List<Long> logicalResourceIds) throws FHIRPersistenceException {
        // Build the entire join for the include query (everything after the FROM)
        // Versioned reference support. From the spec:
        // > If a resource has a reference that is versioned and _include is performed,
        // > the specified version SHOULD be provided.
        /*
SELECT R0.RESOURCE_ID, R0.LOGICAL_RESOURCE_ID, R0.VERSION_ID, R0.LAST_UPDATED, R0.IS_DELETED, R0.DATA, LR0.LOGICAL_ID
        FROM fhirdata.ExplanationOfBenefit_TOKEN_VALUES_V AS P1
  INNER JOIN fhirdata.Claim_LOGICAL_RESOURCES AS LR0
          ON LR0.LOGICAL_ID = P1.TOKEN_VALUE
         AND P1.PARAMETER_NAME_ID = 9263
         AND P1.CODE_SYSTEM_ID = 341729359
         AND P1.LOGICAL_RESOURCE_ID IN (135010606,135010540,135010498,135010412,135010428)
  INNER JOIN fhirdata.Claim_RESOURCES AS R0
          ON LR0.LOGICAL_RESOURCE_ID = R0.LOGICAL_RESOURCE_ID
         AND COALESCE(P1.REF_VERSION_ID,LR0.VERSION_ID) = R0.VERSION_ID
         AND R0.IS_DELETED = 'N'
         *
         */

        final String joinResourceType = inclusionParm.getJoinResourceType();
        final String targetResourceType = inclusionParm.getSearchParameterTargetType();
        final int aliasIndex = getNextAliasIndex();
        final String tokenValues = joinResourceType + "_TOKEN_VALUES_V";
        final String xxLogicalResources = targetResourceType + "_LOGICAL_RESOURCES";
        final String xxResources = targetResourceType + "_RESOURCES";
        final String paramAlias = getParamAlias(aliasIndex);
        final String lrAlias = "LR0";
        final String rAlias = "R0";

        SelectAdapter select = queryData.getQuery();
        select.from(tokenValues, alias(paramAlias))
        .innerJoin(xxLogicalResources, alias(lrAlias),
            on(lrAlias, "LOGICAL_ID").eq(paramAlias, "TOKEN_VALUE")
            .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(inclusionParm.getSearchParameter()))
            .and(paramAlias, "CODE_SYSTEM_ID").eq(getCodeSystemId(targetResourceType))
            .and(paramAlias, "LOGICAL_RESOURCE_ID").inLiteralLong(logicalResourceIds)
            )
        .innerJoin(xxResources, alias(rAlias),
            on(lrAlias, "LOGICAL_RESOURCE_ID").eq(rAlias, "LOGICAL_RESOURCE_ID")
            .and().coalesce(col(paramAlias, "REF_VERSION_ID"), col(lrAlias, "VERSION_ID")).eq(rAlias, "VERSION_ID")
            .and(rAlias, IS_DELETED).eq().literal("N")
            )
            ;

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
        final int aliasIndex = getNextAliasIndex();
        final SelectAdapter query = queryData.getQuery();
        final String tokenValues = joinResourceType + "_TOKEN_VALUES_V";
        final String targetLR = targetResourceType + "_LOGICAL_RESOURCES";
        final String parentLR = joinResourceType +"_LOGICAL_RESOURCES";
        final String parentR = joinResourceType + "_RESOURCES";
        final String paramAlias = getParamAlias(aliasIndex);
        final String parentLRAlias = "LR0";
        final String rAlias = "R0";

        final String lrAlias = "LR" + aliasIndex;

        // parentLR <- token_values <- logical_resources IN (123,456)
        query.from(parentLR, alias(parentLRAlias))
            .innerJoin(tokenValues, alias(paramAlias),
                on(parentLRAlias, "LOGICAL_RESOURCE_ID").eq(paramAlias, "LOGICAL_RESOURCE_ID")
                .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(inclusionParm.getSearchParameter()))
                .and(paramAlias, "CODE_SYSTEM_ID").eq(getCodeSystemId(targetResourceType))
                )
            .innerJoin(targetLR, alias(lrAlias),
                on(lrAlias, "LOGICAL_ID").eq(paramAlias, "TOKEN_VALUE")
                .and().coalesce(col(paramAlias, "REF_VERSION_ID"), col(lrAlias, "VERSION_ID")).eq(lrAlias, "VERSION_ID")
                .and(lrAlias, "LOGICAL_RESOURCE_ID").inLiteralLong(logicalResourceIds)
                .and(lrAlias, "IS_DELETED").eq().literal("N")
                )
            .innerJoin(parentR, alias(rAlias),
                on(parentLRAlias, "CURRENT_RESOURCE_ID").eq(rAlias, "RESOURCE_ID")
                )
            ;

        return queryData;
    }

    @Override
    public QueryData addTokenParam(QueryData queryData, String resourceType, QueryParameter queryParm) throws FHIRPersistenceException {
        // Add a join to the query. The NOT/NOT_IN modifiers are trickier because
        // they need to be handled as a NOT EXISTS clause.
        final int aliasIndex = getNextAliasIndex();
        final SelectAdapter query = queryData.getQuery();
        final String paramAlias = "P" + aliasIndex;
        final String lrAlias = queryData.getLRAlias(); // join to LR at the same query level
        final ExpNode filter;
        filter = getTokenFilter(queryParm, paramAlias).getExpression();
        // which table we join against depends on the fields used by the filter expression
        final String xxTokenValues = getTokenParamTable(filter, resourceType, paramAlias);

        String parameterName = queryParm.getCode();
        // Append the suffix for :text modifier
        if (Modifier.TEXT.equals(queryParm.getModifier())) {
            parameterName += SearchConstants.TEXT_MODIFIER_SUFFIX;
        }

        if (queryParm.getModifier() == Modifier.NOT || queryParm.getModifier() == Modifier.NOT_IN) {
            // Use a nested NOT EXISTS (...) instead of a simple join
            SelectAdapter exists = Select.select("1");
            exists.from(xxTokenValues, alias(paramAlias))
            .where(paramAlias, "LOGICAL_RESOURCE_ID").eq(lrAlias, "LOGICAL_RESOURCE_ID") // correlate with the main query
            .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(parameterName));

            // add the filter predicate to the exists where clause
            exists.from().where().and(filter);
            query.from().where().and().notExists(exists.build());
        } else {
            // Attach the parameter table to the single parameter exists join
            query.from().innerJoin(xxTokenValues, alias(paramAlias), on(paramAlias, "LOGICAL_RESOURCE_ID").eq(lrAlias, "LOGICAL_RESOURCE_ID")
                .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(parameterName))
                .and(filter));
        }

        // We're not changing the level, so we return the same queryData we were given
        return queryData;
    }

    @Override
    public QueryData addTagParam(QueryData queryData, String resourceType, QueryParameter queryParm) throws FHIRPersistenceException {
        // Add a join to the query. The NOT/NOT_IN modifiers are trickier because
        // they need to be handled as a NOT EXISTS clause.
        final int aliasIndex = getNextAliasIndex();
        final SelectAdapter query = queryData.getQuery();
        final String paramAlias = "P" + aliasIndex;
        final String lrAlias = queryData.getLRAlias(); // join to LR at the same query level

        // :text treated like a normal token
        if (Modifier.TEXT.equals(queryParm.getModifier())) {
            return addTokenParam(queryData, resourceType, queryParm);
        }


        // TAGs are stored in their own parameter table and therefore don't need a parameter_name_id
        // but otherwise are just like any other token
        final ExpNode filter = getTokenFilter(queryParm, paramAlias).getExpression();
        final String xxTags = resourceType + "_TAGS";

        String parameterName = queryParm.getCode();

        if (queryParm.getModifier() == Modifier.NOT || queryParm.getModifier() == Modifier.NOT_IN) {
            // Use a nested NOT EXISTS (...) instead of a simple join
            SelectAdapter exists = Select.select("1");
            exists.from(xxTags, alias(paramAlias))
            .where(paramAlias, "LOGICAL_RESOURCE_ID").eq(lrAlias, "LOGICAL_RESOURCE_ID") // correlate with the main query
            .and(filter);

            // Add as a not exists to the main query
            query.from().where().and().notExists(exists.build());
        } else {
            // Attach the parameter table to the single parameter exists join
            query.from().innerJoin(xxTags, alias(paramAlias), on(paramAlias, "LOGICAL_RESOURCE_ID").eq(lrAlias, "LOGICAL_RESOURCE_ID")
                .and(filter));
        }

        // We're not changing the level, so we return the same queryData we were given
        return queryData;
    }

    @Override
    public QueryData addStringParam(QueryData queryData, String resourceType, QueryParameter queryParm) throws FHIRPersistenceException {
        // Join to the string parameter table
        // Attach an exists clause to filter the result based on the string query parameter definition
        final int aliasIndex = getNextAliasIndex();
        final String lrAlias = queryData.getLRAlias();
        final String paramTableName = resourceType + "_STR_VALUES";
        final String paramAlias = getParamAlias(aliasIndex);
        final String parameterName = queryParm.getCode();

        // Add the (non-trivial) filter predicate for string parameters
        ExpNode filter = getStringFilter(queryParm, paramAlias).getExpression();

        SelectAdapter query = queryData.getQuery();
        query.from().innerJoin(paramTableName, alias(paramAlias), on(paramAlias, "LOGICAL_RESOURCE_ID").eq(lrAlias, "LOGICAL_RESOURCE_ID")
            .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(parameterName))
            .and(filter));

        return queryData;
    }

    @Override
    public QueryData addCanonicalParam(QueryData queryData, String resourceType, QueryParameter queryParm) throws FHIRPersistenceException {
        // Join to the canonical parameter table...which in this case means the xx_profiles table
        // because current _profile is the only search parameter to be defined as a canonical
        final int aliasIndex = getNextAliasIndex();
        final String lrAlias = queryData.getLRAlias();
        final String paramTableName = resourceType + "_PROFILES";
        final String paramAlias = getParamAlias(aliasIndex);
        final String parameterName = queryParm.getCode();
        if (!"_profile".equals(parameterName)) {
            throw new FHIRPersistenceException("Only _profile is supported as a canonical");
        }

        // Build the filter predicate for the canonical values, handling the parse
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

            // Reuse the same CanonicalSupport code used for param extraction to parse the search value
            ResourceProfileRec rpc = CanonicalSupport.makeResourceProfileRec(-1, resourceType, -1, -1, value.getValueString(), false);
            int canonicalId = getCanonicalId(rpc.getCanonicalValue());
            whereFragment.col(paramAlias, "CANONICAL_ID").eq(canonicalId);

            // TODO double-check semantics of ABOVE and BELOW in this context
            if (rpc.getVersion() != null && !rpc.getVersion().isEmpty()) {
                if (queryParm.getModifier() == Modifier.ABOVE) {
                    whereFragment.and(paramAlias, "VERSION").gte().bind(rpc.getVersion());
                } else if (queryParm.getModifier() == Modifier.BELOW) {
                    whereFragment.and(paramAlias, "VERSION").lt().bind(rpc.getVersion());
                } else {
                    whereFragment.and(paramAlias, "VERSION").eq().bind(rpc.getVersion());
                }
            }

            if (rpc.getFragment() != null && !rpc.getFragment().isEmpty()) {
                whereFragment.and(paramAlias, "FRAGMENT").eq().bind(rpc.getFragment());
            }
        }

        whereFragment.rightParen();

        SelectAdapter query = queryData.getQuery();
        query.from().innerJoin(paramTableName, alias(paramAlias), on(paramAlias, "LOGICAL_RESOURCE_ID").eq(lrAlias, "LOGICAL_RESOURCE_ID")
            .and(whereFragment.getExpression()));

        return queryData;
    }

    @Override
    public QueryData addMissingParam(QueryData queryData, QueryParameter queryParm, boolean isMissing) throws FHIRPersistenceException {
        // note that there's no filter here to look for a specific value. We simply want to know
        // whether or not the parameter exists for a given resource
        final String parameterName = queryParm.getCode();
        final int aliasIndex = getNextAliasIndex();
        final String resourceType = queryData.getResourceType();
        final String paramTableName = paramValuesTableName(resourceType, queryParm.getType());
        final String lrAlias = queryData.getLRAlias();
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
        return queryData;
    }

    @Override
    public QueryData addChained(QueryData queryData, QueryParameter currentParm) throws FHIRPersistenceException {
        logger.entering(CLASSNAME, "addChained");
        // In this variant, each chained element is added as join to the current statement. We still need
        // to add the EXISTS clause when depth == 0 (the first element in the chain)

        // AND EXISTS (SELECT 1
        //               FROM fhirdata.Observation_TOKEN_VALUES_V AS P1        -- Observation references to
        //         INNER JOIN fhirdata.Device_LOGICAL_RESOURCES AS LR1         -- Device
        //                 ON LR1.LOGICAL_ID = P1.TOKEN_VALUE                  -- Device.LOGICAL_ID = Observation.device
        //                AND P1.PARAMETER_NAME_ID = 1234                      -- Observation.device reference param
        //                AND P1.CODE_SYSTEM_ID = 4321                         -- code-system for Device
        //                AND LR1.IS_DELETED = 'N'                             -- referenced Device is not deleted
        //              WHERE P1.LOGICAL_RESOURCE_ID = LR0.LOGICAL_RESOURCE_ID -- correlate parameter to parent

        final String sourceResourceType = queryData.getResourceType();
        final SelectAdapter currentSubQuery = queryData.getQuery();
        final int aliasIndex = getNextAliasIndex();
        final String targetResourceType = currentParm.getModifierResourceTypeName();
        final String tokenValues = sourceResourceType + "_TOKEN_VALUES_V"; // because we need TOKEN_VALUE
        final String xxLogicalResources = targetResourceType + "_LOGICAL_RESOURCES";
        final String paramAlias = "P" + aliasIndex;
        final String lrAlias = "LR" + aliasIndex;
        final String lrPrevAlias = queryData.getLRAlias();
        final Integer codeSystemIdForTargetResourceType = getCodeSystemId(targetResourceType);

        // Add this chain element as a join to the current query. For forward chaining,
        // we need to join logical-resources and token-values
        currentSubQuery.from()
            .innerJoin(tokenValues, alias(paramAlias), on(paramAlias, "LOGICAL_RESOURCE_ID").eq(lrPrevAlias, "LOGICAL_RESOURCE_ID"))
            .innerJoin(xxLogicalResources, alias(lrAlias),
                on(lrAlias, "LOGICAL_ID").eq(paramAlias, "TOKEN_VALUE")
                .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(currentParm.getCode()))
                .and(paramAlias, "CODE_SYSTEM_ID").eq(nullCheck(codeSystemIdForTargetResourceType))
                .and(lrAlias, "IS_DELETED").eq().literal("N")
              );


        logger.exiting(CLASSNAME, "addChained");
        // Return details of the aliases needed for future chain elements
        return new QueryData(currentSubQuery, lrAlias, paramAlias, targetResourceType, queryData.getChainDepth()+1);
    }

    @Override
    public void addFilter(QueryData queryData, QueryParameter currentParm) throws FHIRPersistenceException {
        // A variant where we just use a simple join instead of an exists (sub-select) to implement
        // the parameter filter.
        logger.fine("chainDepth: " + queryData.getChainDepth());
        final SelectAdapter currentSubQuery = queryData.getQuery();
        final String code = currentParm.getCode();
        final String lrAlias = queryData.getLRAlias();

        if ("_id".equals(code)) {
            addIdFilter(queryData, currentParm);
        } else if ("_lastUpdated".equals(code)) {
            // Compute the _lastUpdated filter predicate for the given query parameter
            NewLastUpdatedParmBehaviorUtil util = new NewLastUpdatedParmBehaviorUtil(lrAlias);
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
            final int aliasIndex = getNextAliasIndex();
            final String paramTable = paramValuesTableName(queryData.getResourceType(), currentParm.getType());
            final String paramAlias = getParamAlias(aliasIndex);

            WhereFragment pf = paramFilter(currentParm, paramAlias);
            if (currentParm.getModifier() == Modifier.NOT) {
                // Needs to be handled as a NOT EXISTS correlated subquery
                SelectAdapter exists = Select.select("1");
                exists.from(paramTable, alias(paramAlias))
                .where(paramAlias, "LOGICAL_RESOURCE_ID").eq(lrAlias, "LOGICAL_RESOURCE_ID") // correlate to parent query
                .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(currentParm.getCode()))
                .and(pf.getExpression());

                // Add the sub-query as a NOT EXISTS filter to the main query
                currentSubQuery.from().where().and().notExists(exists.build());
            } else {
                // Filter the query by adding a join
                currentSubQuery.from()
                .innerJoin(paramTable, alias(paramAlias),
                    on(paramAlias, "LOGICAL_RESOURCE_ID").eq(lrAlias, "LOGICAL_RESOURCE_ID")
                    .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(currentParm.getCode()))
                    .and(pf.getExpression()));
            }
        }
    }

    @Override
    public QueryData addReverseChained(QueryData queryData, QueryParameter currentParm) throws FHIRPersistenceException {
        logger.entering(CLASSNAME, "addReverseChained");
        // For reverse chaining, we connect the token-value (reference)
        // back to the parent query LOGICAL_ID and an xx_LOGICAL_RESOURCES
        // to provide the LOGICAL_ID as the target for future chain elements
        // INNER JOIN fhirdata.Observation_TOKEN_VALUES_V AS P1
        //        AND LR0.LOGICAL_ID = P1.TOKEN_VALUE   -- 'Patient.LOGICAL_ID = Observation.patient'
        //        AND LR0.VERSION_ID = COALESCE(P1.REF_VERSION_ID, LR0.VERSION_ID)
        //        AND P1.PARAMETER_NAME_ID = 1246       -- 'Observation.patient'
        //        AND P1.CODE_SYSTEM_ID = 6             -- 'code system for Patient references'
        // INNER JOIN fhirdata.Observation_LOGICAL_RESOURCES LR1
        //         ON LR1.LOGICAL_RESOURCE_ID = P1.LOGICAL_RESOURCE_ID
        final String refResourceType = queryData.getResourceType();
        final SelectAdapter currentSubQuery = queryData.getQuery();
        final int aliasIndex = getNextAliasIndex();
        final String resourceTypeName = currentParm.getModifierResourceTypeName();
        final String tokenValues = resourceTypeName + "_TOKEN_VALUES_V";
        final String xxLogicalResources = resourceTypeName + "_LOGICAL_RESOURCES";
        final String paramAlias = "P" + aliasIndex;
        final String lrAlias = "LR" + aliasIndex;
        final String lrPrevAlias = queryData.getLRAlias();
        final Integer codeSystemIdForRefResourceType = getCodeSystemId(refResourceType);

        currentSubQuery.from()
              .innerJoin(tokenValues, alias(paramAlias),
                    on(lrPrevAlias, "LOGICAL_ID").eq(paramAlias, "TOKEN_VALUE") // correlate with the main query
                    .and(lrPrevAlias, "VERSION_ID").eq().coalesce(col(paramAlias, "REF_VERSION_ID"), col(lrPrevAlias, "VERSION_ID"))
                    .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(currentParm.getCode()))
                    .and(paramAlias, "CODE_SYSTEM_ID").eq(nullCheck(codeSystemIdForRefResourceType))
                    )
              .innerJoin(xxLogicalResources, alias(lrAlias),
                  on(lrAlias, "LOGICAL_RESOURCE_ID").eq(paramAlias, "LOGICAL_RESOURCE_ID"))
              ;


        // Return a new QueryData with the aliases configured to use by the next element in the chain
        logger.exiting(CLASSNAME, "addReverseChained");
        return new QueryData(currentSubQuery, lrAlias, paramAlias, resourceTypeName, queryData.getChainDepth()+1);
    }


    @Override
    public QueryData addNumberParam(QueryData queryData, String resourceType, QueryParameter queryParm) throws FHIRPersistenceException {
        // Attach an exists clause to the query
        final String parameterName = queryParm.getCode();
        final int aliasIndex = getNextAliasIndex();
        final SelectAdapter query = queryData.getQuery();
        final String paramTableName = resourceType + "_NUMBER_VALUES";
        final String paramAlias = "P" + aliasIndex;
        final String lrAlias = queryData.getLRAlias();

        ExpNode filter = getNumberFilter(queryParm, paramAlias).getExpression();

        query.from().innerJoin(paramTableName, alias(paramAlias), on(paramAlias, "LOGICAL_RESOURCE_ID").eq(lrAlias, "LOGICAL_RESOURCE_ID")
            .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(parameterName))
            .and(filter));

        return queryData;
    }

    @Override
    public QueryData addQuantityParam(QueryData queryData, String resourceType, QueryParameter queryParm) throws FHIRPersistenceException {
        final String parameterName = queryParm.getCode();
        final int aliasIndex = getNextAliasIndex();
        final SelectAdapter query = queryData.getQuery();
        final String paramTableName = resourceType + "_QUANTITY_VALUES";
        final String paramAlias = "P" + aliasIndex;
        final String lrAlias = queryData.getLRAlias();

        ExpNode filter = getQuantityFilter(queryParm, paramAlias).getExpression();

        query.from().innerJoin(paramTableName, alias(paramAlias), on(paramAlias, "LOGICAL_RESOURCE_ID").eq(lrAlias, "LOGICAL_RESOURCE_ID")
            .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(parameterName))
            .and(filter));

        return queryData;
    }

    @Override
    public QueryData addDateParam(QueryData queryData, String resourceType, QueryParameter queryParm) throws FHIRPersistenceException {
        final String parameterName = queryParm.getCode();
        final int aliasIndex = getNextAliasIndex();
        final SelectAdapter query = queryData.getQuery();
        final String paramTableName = resourceType + "_DATE_VALUES";
        final String paramAlias = "P" + aliasIndex;
        final String lrAlias = queryData.getLRAlias();
        ExpNode filter = getDateFilter(queryParm, paramAlias).getExpression();
        query.from().innerJoin(paramTableName, alias(paramAlias), on(paramAlias, "LOGICAL_RESOURCE_ID").eq(lrAlias, "LOGICAL_RESOURCE_ID")
            .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(parameterName))
            .and(filter));

        return queryData;
    }

    @Override
    public QueryData addLocationParam(QueryData queryData, String resourceType, QueryParameter queryParm) throws FHIRPersistenceException {
        final String parameterName = queryParm.getCode();
        final int aliasIndex = getNextAliasIndex();
        final SelectAdapter query = queryData.getQuery();
        final String paramTableName = resourceType + "_LATLNG_VALUES";
        final String paramAlias = "P" + aliasIndex;
        final String lrAlias = queryData.getLRAlias();
        ExpNode filter = getLocationFilter(queryParm, paramAlias).getExpression();
        query.from().innerJoin(paramTableName, alias(paramAlias), on(paramAlias, "LOGICAL_RESOURCE_ID").eq(lrAlias, "LOGICAL_RESOURCE_ID")
            .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(parameterName))
            .and(filter));

        return queryData;
    }

    @Override
    public QueryData addReferenceParam(QueryData queryData, String resourceType, QueryParameter queryParm) throws FHIRPersistenceException {
        final int aliasIndex = getNextAliasIndex();
        final SelectAdapter query = queryData.getQuery();
        final String paramAlias = "P" + aliasIndex;
        final String lrAlias = queryData.getLRAlias();

        // Grab the filter expression first. We can then inspect the expression to
        // look for use of the TOKEN_VALUE column. If use of this column isn't found,
        // we can apply an optimization by joining against the RESOURCE_TOKEN_REFS
        // table directly.
        ExpNode filter = getReferenceFilter(queryParm, paramAlias).getExpression();
        final String paramTableName = getTokenParamTable(filter, resourceType, paramAlias);

        // Append the suffix for :identifier modifier
        String queryParmCode = queryParm.getCode();
        if (Modifier.IDENTIFIER.equals(queryParm.getModifier())) {
            queryParmCode += SearchConstants.IDENTIFIER_MODIFIER_SUFFIX;
        }

        query.from().innerJoin(paramTableName, alias(paramAlias), on(paramAlias, "LOGICAL_RESOURCE_ID").eq(lrAlias, "LOGICAL_RESOURCE_ID")
            .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(queryParmCode))
            .and(filter));

        return queryData;
    }

    @Override
    public QueryData addCompositeParam(QueryData queryData, QueryParameter queryParm) throws FHIRPersistenceException {
        final String lrAlias = queryData.getLRAlias();

        final WhereAdapter where = queryData.getQuery().from().where();

        // Each query parm value gets its own EXISTS OR'd together
        if (queryParm.getValues().size() == 1) {
            // Simple optimization. Only one composite value, so add
            // as inner joins to the core parameter exists query
            QueryParameterValue compositeValue = queryParm.getValues().get(0);
            List<QueryParameter> components = compositeValue.getComponent();
            int firstAliasIndex = -1;
            for (int componentNum = 1; componentNum <= components.size(); componentNum++) {
                QueryParameter component = components.get(componentNum - 1);
                int aliasIndex = addCompositeParamTable(queryData.getQuery(), queryData.getResourceType(), lrAlias, component, componentNum, firstAliasIndex);

                if (componentNum == 1) {
                    // Remember the alias we use for the first component so we can join subsequent
                    // component tables to the first
                    firstAliasIndex = aliasIndex;
                }
            }
        } else {
            // Each value gets its own EXISTS clause which we combine together
            // with OR. The whole thing needs to be wrapped in parens to ensure
            // the correct precedence.
            // AND ( EXISTS (...) OR EXISTS (...) )
            where.and().leftParen();
            boolean first = true;
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
        }

        // The only thing we can return which makes any sense is the original query
        return queryData;
    }

    /**
     * Add a parameter table filter for a composite parameter
     * @param query
     * @param resourceType
     * @param lrAlias
     * @param component
     * @param componentNum
     * @param firstAliasIndex
     * @return the parameter alias, so we can find the first composite param table alias
     * @throws FHIRPersistenceException
     */
    private int addCompositeParamTable(SelectAdapter query, String resourceType, String lrAlias, QueryParameter component, int componentNum,
        int firstAliasIndex) throws FHIRPersistenceException {
        final int aliasIndex = getNextAliasIndex();
        String paramTableAlias = "P" + aliasIndex;
        String parameterName = component.getCode();

        // Grab the parameter filter expression first so that we can see if it's safe to apply
        // the COMMON_TOKEN_VALUES_ID optimization
        final ExpNode filter = paramFilter(component, paramTableAlias).getExpression();
        final String valuesTable;
        if (component.getType() == Type.TOKEN && filter != null) {
            // optimize token parameter joins if the expression lets us
            valuesTable = getTokenParamTable(filter, resourceType, paramTableAlias);
        } else {
            valuesTable = QuerySegmentAggregator.tableName(resourceType, component).trim();
        }

        if (componentNum == 1) {
            query.from().innerJoin(valuesTable, alias(paramTableAlias),
                on(paramTableAlias, "LOGICAL_RESOURCE_ID").eq(lrAlias, "LOGICAL_RESOURCE_ID")
                .and(paramTableAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(parameterName))
                .and(filter));

        } else {
            // also join to the first parameter table
            final String firstTableAlias = "P" + firstAliasIndex;
            query.from().innerJoin(valuesTable, alias(paramTableAlias),
                on(paramTableAlias, "LOGICAL_RESOURCE_ID").eq(firstTableAlias, "LOGICAL_RESOURCE_ID")
                .and(paramTableAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(parameterName))
                .and(paramTableAlias, "COMPOSITE_ID").eq(firstTableAlias, "COMPOSITE_ID")
                .and(filter));
        }
        return aliasIndex;
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

        String componentTableAlias = "comp" + componentNum;
        String parameterName = component.getCode();

        // Grab the parameter filter expression first so that we can see if it's safe to apply
        // the COMMON_TOKEN_VALUES_ID optimization
        final ExpNode filter;
        if (addParamFilter) {
            filter = paramFilter(component, componentTableAlias).getExpression();
        } else {
            filter = null;
        }
        final String valuesTable;
        if (component.getType() == Type.TOKEN && filter != null) {
            // optimize token parameter joins if the expression lets us
            valuesTable = getTokenParamTable(filter, resourceType, componentTableAlias);
        } else {
            valuesTable = QuerySegmentAggregator.tableName(resourceType, component).trim();
        }

        if (componentNum == 1) {
            exists.from(valuesTable, alias(componentTableAlias))
            .where(componentTableAlias, "LOGICAL_RESOURCE_ID").eq(lrAlias, "LOGICAL_RESOURCE_ID") // correlate with the main query
            .and(componentTableAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(parameterName));

            // Parameter filter is skipped if this is coming from a missing/not missing search
            if (addParamFilter) {
                exists.from().where().and(filter);
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
                exists.from().where().and(filter);
            }
        }
    }

    @Override
    public QueryData addCompositeParam(QueryData queryData, QueryParameter queryParm, boolean isMissing) throws FHIRPersistenceException {
        final String lrAlias = queryData.getLRAlias();

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

    @Override
    public void addSortParam(QueryData queryData, String code, Type type, Direction direction) throws FHIRPersistenceException {
        // Each sort parameter gets added as parameter table which is outer-joined to the
        // core data query
        SelectAdapter query = queryData.getQuery();
        final int aliasIndex = getNextAliasIndex();
        final String paramAlias = getParamAlias(aliasIndex);

        addAggregateAndOrderByExpressions(queryData, code, type, direction, paramAlias);

        // Now add the parameter table as an outer join
        final String paramTable = getSortParameterTableName(queryData.getResourceType(), type);
        final String lrAlias = queryData.getLRAlias();

        query.from()
            .leftOuterJoin(paramTable, alias(paramAlias),
                on(paramAlias, "LOGICAL_RESOURCE_ID").eq(lrAlias, "LOGICAL_RESOURCE_ID")
                .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(code)));
   }

    /**
     * Returns the name of the database table corresponding to the type of the
     * passed sort parameter.
     *
     * @param sortParm A valid SortParameter
     * @return String - A database table name
     * @throws FHIRPersistenceException
     */
    protected String getSortParameterTableName(String resourceType, Type type) throws FHIRPersistenceException {
        final String METHODNAME = "getSortParameterTableName";
        logger.entering(CLASSNAME, METHODNAME);

        StringBuilder sortParameterTableName = new StringBuilder();
        sortParameterTableName.append(resourceType).append("_");

        switch (type) {
        case URI:
        case STRING:
            sortParameterTableName.append("STR_VALUES");
            break;
        case DATE:
            sortParameterTableName.append("DATE_VALUES");
            break;
        case REFERENCE:
        case TOKEN:
            sortParameterTableName.append("TOKEN_VALUES_V");
            break;
        case NUMBER:
            sortParameterTableName.append("NUMBER_VALUES");
            break;
        case QUANTITY:
            sortParameterTableName.append("QUANTITY_VALUES");
            break;
        default:
            throw new FHIRPersistenceNotSupportedException("Parm type not supported: " + type.value());
        }

        logger.exiting(CLASSNAME, METHODNAME);
        return sortParameterTableName.toString();
    }

    /**
     * Add the min/max aggregate and sort expressions to the SORT query
     * @param queryData
     * @param code
     * @param type
     * @param direction
     * @param parmAlias
     * @throws FHIRPersistenceException
     */
    private void addAggregateAndOrderByExpressions(QueryData queryData, String code, Type type, Direction direction, String parmAlias)
            throws FHIRPersistenceException {
        final String METHODNAME = "addAggregateAndOrderByExpressions";
        logger.entering(CLASSNAME, METHODNAME);

        SelectAdapter query = queryData.getQuery();
        List<String> valueAttributeNames;

        valueAttributeNames = this.getValueAttributeNames(type);
        for (String attributeName : valueAttributeNames) {
            StringBuilder expression = new StringBuilder();
            final String dirExp;
            if (direction == Direction.INCREASING) {
                expression.append(MIN);
                dirExp = "ASC";
            } else {
                expression.append(MAX);
                dirExp = "DESC";
            }
            expression.append(LEFT_PAREN);

            if (SearchConstants.LAST_UPDATED.equals(code)) {
                expression.append(queryData.getLRAlias() + ".LAST_UPDATED");
            } else {
                expression.append(parmAlias).append(".").append(attributeName);
            }
            expression.append(RIGHT_PAREN);

            // add the aggregate column expression to the select list clause
            query.addColumn(null, expression.toString(), null);

            // Add the column to the order by clause
            expression.append(" ").append(dirExp).append(" NULLS LAST");
            query.from().orderBy(expression.toString());
        }

        logger.exiting(CLASSNAME, METHODNAME);
    }

    /**
     * Returns the names of the Parameter attributes containing the values
     * corresponding to the passed sort parameter type.
     * @param type
     * @throws FHIRPersistenceException
     */
    private List<String> getValueAttributeNames(Type type) throws FHIRPersistenceException {
        final String METHODNAME = "getValueAttributeName";
        logger.entering(CLASSNAME, METHODNAME);

        List<String> attributeNames = new ArrayList<>();
        switch (type) {
        case STRING:
            attributeNames.add(STR_VALUE);
            break;
        case REFERENCE:
            attributeNames.add(TOKEN_VALUE);
            break;
        case DATE:
            attributeNames.add(DATE_START);
            break;
        case TOKEN:
            attributeNames.add(TOKEN_VALUE);
            break;
        case NUMBER:
            attributeNames.add(NUMBER_VALUE);
            break;
        case QUANTITY:
            attributeNames.add(QUANTITY_VALUE);
            break;
        case URI:
            attributeNames.add(STR_VALUE);
            break;
        default:
            throw new FHIRPersistenceNotSupportedException("Parm type not supported: " + type.value());
        }

        logger.exiting(CLASSNAME, METHODNAME);
        return attributeNames;
    }
}