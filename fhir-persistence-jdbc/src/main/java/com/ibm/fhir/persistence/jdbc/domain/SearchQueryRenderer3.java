/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.domain;

import static com.ibm.fhir.database.utils.query.expression.ExpressionUtils.alias;
import static com.ibm.fhir.database.utils.query.expression.ExpressionUtils.col;
import static com.ibm.fhir.database.utils.query.expression.ExpressionUtils.on;

import java.util.List;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.query.Operator;
import com.ibm.fhir.database.utils.query.Select;
import com.ibm.fhir.database.utils.query.SelectAdapter;
import com.ibm.fhir.database.utils.query.WhereAdapter;
import com.ibm.fhir.database.utils.query.WhereFragment;
import com.ibm.fhir.database.utils.query.node.ExpNode;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.dao.api.JDBCIdentityCache;
import com.ibm.fhir.persistence.jdbc.util.QuerySegmentAggregator;
import com.ibm.fhir.search.SearchConstants.Modifier;
import com.ibm.fhir.search.SearchConstants.Type;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.parameters.QueryParameterValue;

/**
 * This version of the query renderer uses a single EXISTS statement
 * with all the parameter joins contained within a single sub-select
 * bound together using inner-joins. This approach appears to give
 * the optimizer the best opportunity to produce a reasonable plan,
 * particular in the case of compartment searches.
 */
public class SearchQueryRenderer3 extends SearchQueryRenderer {
    private static final String CLASSNAME = SearchQueryRenderer.class.getName();
    private static final Logger logger = Logger.getLogger(CLASSNAME);

    private final static String STR_VALUE = "STR_VALUE";
    private final static String STR_VALUE_LCASE = "STR_VALUE_LCASE";

    /**
     * Public constructor
     * @param identityCache
     */
    public SearchQueryRenderer3(JDBCIdentityCache identityCache,
        int rowOffset, int rowsPerPage) {
        super(identityCache, rowOffset, rowsPerPage);
    }

    /**
     * In this version of the query builder, parameter tables are all grouped together
     * under a single exists clause, which we start building here.
     * @param queryData
     */
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

        // We are starting a sub-query, so future params need to add themselves to this
        return new QueryData(exists, lrAlias, null, parent.getResourceType(), 0);
    }

    @Override
    public QueryData addTokenParam(QueryData queryData, String resourceType, QueryParameter queryParm) throws FHIRPersistenceException {
        // Add a join to the query. The NOT/NOT_IN modifiers are tricker because
        // they need to be handled as a NOT EXISTS clause.
        logger.fine("adding token parm: " + queryParm);
        final String parameterName = queryParm.getCode();
        final int aliasIndex = getNextAliasIndex();
        final SelectAdapter query = queryData.getQuery();
        final Operator operator = getOperator(queryParm);
        final String paramAlias = "P" + aliasIndex;
        final String lrAlias = queryData.getLRAlias(); // join to LR at the same query level
        final ExpNode filter;
        if (operator == Operator.EQ) {
            filter = getTokenFilter(queryParm, paramAlias).getExpression();
        } else {
            filter = getComplexTokenFilter(queryParm, paramAlias).getExpression();
        }
        // which table we join against depends on the fields used by the filter expression
        final String xxTokenValues = getTokenParamTable(filter, resourceType, paramAlias);

        if (queryParm.getModifier() == Modifier.NOT || queryParm.getModifier() == Modifier.NOT_IN) {
            // Use a nested NOT EXISTS (...) instead of a simple join
            SelectAdapter exists = Select.select("1");

            if (operator == Operator.EQ) {
                exists.from(xxTokenValues, alias(paramAlias))
                .where(paramAlias, "LOGICAL_RESOURCE_ID").eq(lrAlias, "LOGICAL_RESOURCE_ID") // correlate with the main query
                .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(parameterName));
            } else {
                exists.from(xxTokenValues, alias(paramAlias))
                .where(paramAlias, "LOGICAL_RESOURCE_ID").eq(lrAlias, "LOGICAL_RESOURCE_ID") // correlate with the main query
                .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(parameterName));
            }
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
        // Join to the string parameter table
        // Attach an exists clause to filter the result based on the string query parameter definition
        logger.fine("adding string parm: " + queryParm);
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
    public QueryData addMissingParam(QueryData queryData, QueryParameter queryParm, boolean isMissing) throws FHIRPersistenceException {
        // note that there's no filter here to look for a specific value. We simply want to know
        // whether or not the parameter exists for a given resource
        final String parameterName = queryParm.getCode();
        final int aliasIndex = getNextAliasIndex();
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
            super.addFilter(queryData, currentParm);
        } else if ("_lastUpdated".equals(code)) {
            super.addFilter(queryData, currentParm);
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

            WhereFragment pf;
            if (currentParm.getModifier() == Modifier.NOT) {
                pf = new WhereFragment().not(paramFilter(currentParm, paramAlias).getExpression());
            } else {
                pf = paramFilter(currentParm, paramAlias);
            }
            currentSubQuery.from()
                .innerJoin(paramTable, alias(paramAlias),
                    on(paramAlias, "LOGICAL_RESOURCE_ID").eq(lrAlias, "LOGICAL_RESOURCE_ID")
                    .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(currentParm.getCode()))
                    .and(pf.getExpression()));
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
                    on(lrAlias, "LOGICAL_RESOURCE_ID").eq(paramAlias, "LOGICAL_RESOURCE_ID")
                    .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(currentParm.getCode()))
                    .and(paramAlias, "CODE_SYSTEM_ID").eq(nullCheck(codeSystemIdForRefResourceType))
                    .and(lrPrevAlias, "LOGICAL_ID").eq(paramAlias, "TOKEN_VALUE") // correlate with the main query
                    .and(lrPrevAlias, "VERSION_ID").eq().coalesce(col(paramAlias, "REF_VERSION_ID"), col(lrPrevAlias, "VERSION_ID")))
              .innerJoin(xxLogicalResources, alias(lrAlias), on(lrAlias, "LOGICAL_RESOURCE_ID").eq(paramAlias, "LOGICAL_RESOURCE_ID"))
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
        logger.fine("adding reference parm: " + queryParm);
        final String parameterName = queryParm.getCode();
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

        query.from().innerJoin(paramTableName, alias(paramAlias), on(paramAlias, "LOGICAL_RESOURCE_ID").eq(lrAlias, "LOGICAL_RESOURCE_ID")
            .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(parameterName))
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
                // AND ( EXISTS (...) OR EXISTS (...) )  <== close the paren
                where.rightParen();
            }
        }

        // The only thing we can return which makes any sense is the original query
        return queryData;
    }

    /**
     * @param where
     * @param resourceType
     * @param lrAlias
     * @param component
     * @param componentNum
     * @param firstAliasIndex
     * @return
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
        final int aliasIndex = getNextAliasIndex();
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
}