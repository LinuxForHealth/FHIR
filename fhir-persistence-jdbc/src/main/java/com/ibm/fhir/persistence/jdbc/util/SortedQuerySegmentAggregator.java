/*
 * (C) Copyright IBM Corp. 2017, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util;

import static com.ibm.fhir.persistence.jdbc.JDBCConstants.AND;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.ASCENDING;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.CODE_SYSTEM_ID;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.COMMA;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.COMMA_CHAR;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.DATE_START;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.DESCENDING;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.DOT_CHAR;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LEFT_PAREN;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.MAX;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.MIN;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.NUMBER_VALUE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.ON;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.ORDER_BY;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.QUANTITY_VALUE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.RIGHT_PAREN;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.SPACE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.STR_VALUE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.TOKEN_VALUE;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceNotSupportedException;
import com.ibm.fhir.persistence.jdbc.dao.api.ParameterDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.ResourceDAO;
import com.ibm.fhir.persistence.jdbc.util.type.LastUpdatedParmBehaviorUtil;
import com.ibm.fhir.search.parameters.SortParameter;
import com.ibm.fhir.search.sort.Sort;

/**
 * This class assists the JDBCQueryBuilder. It extends the
 * QuerySegmentAggregator to build a FHIR Resource query
 * that produces sorted search results.
 */
public class SortedQuerySegmentAggregator extends QuerySegmentAggregator {
    private static final String CLASSNAME = SortedQuerySegmentAggregator.class.getName();
    private static final Logger log = java.util.logging.Logger.getLogger(CLASSNAME);

    public static final String GROUP_BY = " GROUP BY R.RESOURCE_ID ";
    private static final String SORT_PARAMETER_ALIAS = "S";

    private List<SortParameter> sortParameters;

    /**
     * Constructs a new SortedQuerySegmentAggregator
     * 
     * @param resourceType - The type of FHIR Resource to be searched for.
     * @param offset       - The beginning index of the first search result.
     * @param pageSize     - The max number of requested search results.
     * @param ResourceDAO  - A FHIR DB Data Access Object
     * @param sortParms    - A list of SortParameters
     */
    protected SortedQuerySegmentAggregator(Class<?> resourceType, int offset, int pageSize, ParameterDAO parameterDao,
            ResourceDAO resourceDao, List<SortParameter> sortParms) {
        super(resourceType, offset, pageSize, parameterDao, resourceDao);
        this.sortParameters = sortParms;
    }

    /**
     * Builds a complete SQL Query based upon the encapsulated query segments and
     * bind variables. This query
     * contains the necessary clauses to support sorted search results.
     * <p>
     * A simple example query produced by this method:
     * 
     * <pre>
     * SELECT R.RESOURCE_ID,MIN(S1.STR_VALUE) FROM Patient_RESOURCES R 
     *   JOIN Patient_LOGICAL_RESOURCES LR ON R.LOGICAL_RESOURCE_ID=LR.LOGICAL_RESOURCE_ID
     *   JOIN Patient_TOKEN_VALUES P1 ON P1.RESOURCE_ID=R.RESOURCE_ID  
     *   LEFT OUTER JOIN Patient_STR_VALUES S1 ON (S1.PARAMETER_NAME_ID=50 AND S1.RESOURCE_ID = R.RESOURCE_ID)
     *   WHERE R.RESOURCE_ID = LR.CURRENT_RESOURCE_ID AND 
     *         R.IS_DELETED <> 'Y' AND 
     *         P1.RESOURCE_ID = R.RESOURCE_ID AND 
     *         (P1.PARAMETER_NAME_ID=196 AND ((P1.TOKEN_VALUE = false))) 
     * GROUP BY R.RESOURCE_ID  
     * ORDER BY MIN(S1.STR_VALUE) asc NULLS LAST 
     * OFFSET 0 ROWS FETCH NEXT 100 ROWS ONLY;
     * </pre>
     * 
     * @return SqlQueryData - contains the complete SQL query string and any
     *         associated bind variables.
     * @throws Exception
     */
    @Override
    public SqlQueryData buildQuery() throws Exception {
        final String METHODNAME = "buildQuery";
        log.entering(CLASSNAME, METHODNAME);

        if (this.sortParameters == null || this.sortParameters.isEmpty()) {
            throw new FHIRPersistenceException("No sort parameters found.");
        }

        // For system level search, the sort parameters are limited to a couple of columns in the *_resources  
        // and *_logical_resources tables. Execute the following special logic for sorted system-level-searches.
        SqlQueryData queryData;
        if (this.isSystemLevelSearch()) {
            // Build query without order-by and pagination clauses.
            queryData =
                    this.buildSystemLevelQuery(SYSTEM_LEVEL_SELECT_ROOT, SYSTEM_LEVEL_SUBSELECT_ROOT, false);
            StringBuilder sysLvlQueryString = new StringBuilder(queryData.getQueryString());
            // Add in order-by clause.
            sysLvlQueryString.append(this.buildSysLvlOrderByClause());
            // Add pagination clauses.
            this.addPaginationClauses(sysLvlQueryString);
            queryData = new SqlQueryData(sysLvlQueryString.toString(), queryData.getBindVariables());
        } else {
            StringBuilder sqlSortQuery = new StringBuilder();
            // Build SELECT clause
            sqlSortQuery.append(this.buildSelectClause());

            // Build FROM clause
            buildFromClause(sqlSortQuery, resourceType.getSimpleName());

            // Gather up all bind variables from the query segments
            // An important step here is to add _id and _lastUpdated
            List<Object> allBindVariables = new ArrayList<>();
            allBindVariables.addAll(idsObjects);
            allBindVariables.addAll(lastUpdatedObjects);
            for (SqlQueryData querySegment : this.querySegments) {
                allBindVariables.addAll(querySegment.getBindVariables());
            }

            // Build the WHERE clause...this needs to appear before the outer join part
            buildWhereClause(sqlSortQuery, null);
            
            // Build LEFT OUTER JOIN clause
            sqlSortQuery.append(this.buildSortJoinClause());


            // Build GROUP BY clause
            sqlSortQuery.append(GROUP_BY);

            // Build ORDER BY clause
            sqlSortQuery.append(this.buildOrderByClause());

            // Add in clauses to support pagination
            this.addPaginationClauses(sqlSortQuery);

            queryData = new SqlQueryData(sqlSortQuery.toString(), allBindVariables);
        }

        log.exiting(CLASSNAME, METHODNAME, queryData);
        return queryData;
    }

    /**
     * Builds the SELECT clause necessary to return sorted Resource ids.
     * For example:
     * 
     * <pre>
     * SELECT R.RESOURCE_ID,MIN(S1.STR_VALUE) FROM
     * </pre>
     * 
     * @throws FHIRPersistenceException
     */
    private String buildSelectClause() throws FHIRPersistenceException {
        final String METHODNAME = "buildSelectClause";
        log.entering(CLASSNAME, METHODNAME);

        StringBuilder selectBuffer = new StringBuilder();
        selectBuffer.append("SELECT R.RESOURCE_ID");

        // Build MIN and/or MAX clauses
        for (int i = 0; i < this.sortParameters.size(); i++) {
            selectBuffer.append(COMMA_CHAR);
            selectBuffer.append(this.buildAggregateExpression(this.sortParameters.get(i), i + 1, false));
        }
        selectBuffer.append(SPACE);

        log.exiting(CLASSNAME, METHODNAME);
        return selectBuffer.toString();
    }

    /**
     * Builds the required MIN or MAX aggregate expressions for the passed sort
     * parameter.
     * 
     * @param sortParm           A valid sort parameter.
     * @param sortParmIndex      An integer representing the position of the sort
     *                           parameter in a collection of sort parameters.
     * @param useInOrderByClause A flag indicating whether or not the returned
     *                           aggregate expression is to be used in an ORDER BY
     *                           clause.
     * @return
     * @throws FHIRPersistenceException
     */
    private String buildAggregateExpression(SortParameter sortParm, int sortParmIndex, boolean useInOrderByClause)
            throws FHIRPersistenceException {
        final String METHODNAME = "buildAggregateExpression";
        log.entering(CLASSNAME, METHODNAME);

        StringBuilder expression = new StringBuilder();
        List<String> valueAttributeNames;

        valueAttributeNames = this.getValueAttributeNames(sortParm);
        boolean nameProcessed = false;
        for (String attributeName : valueAttributeNames) {
            if (nameProcessed) {
                expression.append(COMMA);
            }
            if (Sort.Direction.INCREASING.equals(sortParm.getDirection())) {
                expression.append(MIN);
            } else {
                expression.append(MAX);
            }
            expression.append(LEFT_PAREN);
            expression.append(SORT_PARAMETER_ALIAS).append(sortParmIndex).append(DOT_CHAR);
            expression.append(attributeName);
            expression.append(RIGHT_PAREN);
            if (useInOrderByClause) {
                expression.append(SPACE);
                // Choose the DIRECTION
                switch (sortParm.getDirection()) {
                case INCREASING:
                    expression.append(ASCENDING);
                    break;
                default:
                    expression.append(DESCENDING);
                    break;
                }
                expression.append(" NULLS LAST");
            }
            nameProcessed = true;
        }

        log.exiting(CLASSNAME, METHODNAME);
        return expression.toString();
    }

    /**
     * Returns the names of the Parameter attributes containing the values
     * corresponding to the passed sort parameter.
     * 
     * @throws FHIRPersistenceException
     */
    private List<String> getValueAttributeNames(SortParameter sortParm) throws FHIRPersistenceException {
        final String METHODNAME = "getValueAttributeName";
        log.entering(CLASSNAME, METHODNAME);

        List<String> attributeNames = new ArrayList<>();
        switch (sortParm.getType()) {
        case STRING:
            attributeNames.add(STR_VALUE);
            break;
        case REFERENCE:
            attributeNames.add(STR_VALUE);
            break;
        case DATE:
            attributeNames.add(DATE_START);
            break;
        case TOKEN:
            attributeNames.add(CODE_SYSTEM_ID); //TODO This is probably wrong
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
            throw new FHIRPersistenceNotSupportedException("Parm type not supported: " + sortParm.getType().value());
        }

        log.exiting(CLASSNAME, METHODNAME);
        return attributeNames;
    }

    /**
     * Builds the LEFT OUTER JOIN clauses necessary to return sorted Resource ids.
     * For example:
     * JOIN r.parameters p1
     * LEFT OUTER JOIN Patient_STR_VALUES S1 ON (S1.PARAMETER_NAME_ID=50 AND
     * S1.LOGICAL_RESOURCE_ID = R.LOGICAL_RESOURCE_ID)
     * 
     * @throws FHIRPersistenceException
     */
    private String buildSortJoinClause() throws FHIRPersistenceException {
        final String METHODNAME = "buildSortJoinClause";
        log.entering(CLASSNAME, METHODNAME);

        StringBuilder joinBuffer = new StringBuilder();
        Integer sortParameterNameId;

        // Build the LEFT OUTER JOINs needed to access the required sort parameters.
        int sortParmIndex = 1;
        for (SortParameter sortParm : this.sortParameters) {
            sortParameterNameId = ParameterNamesCache.getParameterNameId(sortParm.getCode());
            if (sortParameterNameId == null) {
                // Only read...don't try and create the parameter name if it doesn't exist
                sortParameterNameId = this.parameterDao.readParameterNameId(sortParm.getCode());
                if (sortParameterNameId != null) {
                    this.parameterDao.addParameterNamesCacheCandidate(sortParm.getCode(), sortParameterNameId);
                } else {
                    sortParameterNameId = -1; // so we don't break the query syntax
                }
            }

            // Note...the PARAMETER_NAME_ID=xxx is provided as a literal because this helps
            // the query optimizer significantly with index range scan cardinality estimation
            joinBuffer.append(" LEFT OUTER JOIN ").append(this.getSortParameterTableName(sortParm)).append(SPACE)
                    .append(SORT_PARAMETER_ALIAS).append(sortParmIndex)
                    .append(ON)
                    .append(LEFT_PAREN)
                    .append(SORT_PARAMETER_ALIAS).append(sortParmIndex).append(".PARAMETER_NAME_ID=")
                    .append(sortParameterNameId)
                    .append(AND)
                    .append(SORT_PARAMETER_ALIAS).append(sortParmIndex)
                    .append(".LOGICAL_RESOURCE_ID = R.LOGICAL_RESOURCE_ID")
                    .append(RIGHT_PAREN).append(SPACE);

            sortParmIndex++;
        }

        log.exiting(CLASSNAME, METHODNAME);
        return joinBuffer.toString();
    }

    /**
     * Returns the name of the database table corresponding to the type of the
     * passed sort parameter.
     * 
     * @param sortParm A valid SortParameter
     * @return String - A database table name
     * @throws FHIRPersistenceException
     */
    private String getSortParameterTableName(SortParameter sortParm) throws FHIRPersistenceException {
        final String METHODNAME = "getSortParameterTableName";
        log.entering(CLASSNAME, METHODNAME);

        StringBuilder sortParameterTableName = new StringBuilder();
        sortParameterTableName.append(this.resourceType.getSimpleName()).append("_");

        switch (sortParm.getType()) {
        case REFERENCE:
        case URI:
        case STRING:
            sortParameterTableName.append("STR_VALUES");
            break;
        case DATE:
            sortParameterTableName.append("DATE_VALUES");
            break;
        case TOKEN:
            sortParameterTableName.append("TOKEN_VALUES");
            break;
        case NUMBER:
            sortParameterTableName.append("NUMBER_VALUES");
            break;
        case QUANTITY:
            sortParameterTableName.append("QUANTITY_VALUES");
            break;
        default:
            throw new FHIRPersistenceNotSupportedException("Parm type not supported: " + sortParm.getType().value());
        }

        log.exiting(CLASSNAME, METHODNAME);
        return sortParameterTableName.toString();
    }

    /**
     * Builds the ORDER BY clause necessary to return sorted Resource ids.
     * For example:
     * 
     * <pre>
     * ORDER BY MIN(S1.STR_VALUE) asc NULLS LAST,MAX(S2.CODE_SYSTEM_ID) desc NULLS LAST, MAX(S2.TOKEN_VALUE) desc NULLS LAST
     * </pre>
     * 
     * @throws FHIRPersistenceException
     */
    private String buildOrderByClause() throws FHIRPersistenceException {
        final String METHODNAME = "buildOrderByClause";
        log.entering(CLASSNAME, METHODNAME);

        StringBuilder orderByBuffer = new StringBuilder();
        orderByBuffer.append(ORDER_BY);

        // Build MIN and/or MAX clauses
        for (int i = 0; i < this.sortParameters.size(); i++) {
            if (i > 0) {
                orderByBuffer.append(COMMA_CHAR);
            }
            orderByBuffer.append(this.buildAggregateExpression(this.sortParameters.get(i), i + 1, true));
        }

        log.exiting(CLASSNAME, METHODNAME);
        return orderByBuffer.toString();
    }

    /**
     * This method builds a special ORDER BY clause for use only with system-level
     * queries.
     * Only 2 parameters are supported for sorting on system-level searches: _id and
     * _lastUpdated.
     * 
     * @return An ORDER-BY clause for use exclusively with system-level searches.
     * @throws FHIRPersistenceException
     */
    private String buildSysLvlOrderByClause() throws FHIRPersistenceException {
        final String METHODNAME = "buildSysLvlOrderByClause";
        log.entering(CLASSNAME, METHODNAME);

        StringBuilder orderByBuffer = new StringBuilder();
        orderByBuffer.append(ORDER_BY);

        boolean first = true;
        for (SortParameter sortParm : sortParameters) {
            if (!first) {
                orderByBuffer.append(COMMA_CHAR);
            } else {
                first = false;
            }

            // System level searches are on specific columns only _id and _lastUpdated.
            String code = sortParm.getCode();
            if (ID.equals(code)) {
                orderByBuffer.append(ID_COLUMN_NAME);
            } else if (LastUpdatedParmBehaviorUtil.LAST_UPDATED.equals(code)) {
                orderByBuffer.append(LastUpdatedParmBehaviorUtil.LAST_UPDATED_COLUMN_NAME).append(SPACE);
            } else {
                throw new FHIRPersistenceNotSupportedException(
                        "'" + code + "' is an unsupported sort parameter for system level search.");
            }

            // Choose the DIRECTION
            switch (sortParm.getDirection()) {
            case INCREASING:
                orderByBuffer.append(ASCENDING);
                break;
            default:
                orderByBuffer.append(DESCENDING);
                break;
            }
        }

        log.exiting(CLASSNAME, METHODNAME);
        return orderByBuffer.toString();
    }
}