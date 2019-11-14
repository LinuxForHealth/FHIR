/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util;

import static com.ibm.fhir.persistence.jdbc.JDBCConstants.BIND_VAR;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.model.resource.Location;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.persistence.jdbc.JDBCConstants.JDBCOperator;
import com.ibm.fhir.persistence.jdbc.dao.api.ParameterDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.ResourceDAO;
import com.ibm.fhir.persistence.util.AbstractQueryBuilder;
import com.ibm.fhir.search.SearchConstants.Modifier;
import com.ibm.fhir.search.SearchConstants.Prefix;
import com.ibm.fhir.search.parameters.Parameter;
import com.ibm.fhir.search.parameters.ParameterValue;

/**
 * This class assists the JDBCQueryBuilder. Its purpose is to aggregate SQL
 * query segments together to produce a well-formed FHIR Resource query or
 * FHIR Resource count query.
 */
class QuerySegmentAggregator {

    private static final String CLASSNAME = QuerySegmentAggregator.class.getName();
    private static final Logger log = java.util.logging.Logger.getLogger(CLASSNAME);

    protected static final String SELECT_ROOT =
            "SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID, R.LAST_UPDATED, R.IS_DELETED, R.DATA, LR.LOGICAL_ID ";
    protected static final String SYSTEM_LEVEL_SELECT_ROOT =
            "SELECT RESOURCE_ID, LOGICAL_RESOURCE_ID, VERSION_ID, LAST_UPDATED, IS_DELETED, DATA, LOGICAL_ID ";
    protected static final String SYSTEM_LEVEL_SUBSELECT_ROOT = SELECT_ROOT;
    private static final String SELECT_COUNT_ROOT = "SELECT COUNT(R.RESOURCE_ID) ";
    private static final String SYSTEM_LEVEL_SELECT_COUNT_ROOT = "SELECT COUNT(RESOURCE_ID) ";
    private static final String SYSTEM_LEVEL_SUBSELECT_COUNT_ROOT = " SELECT R.RESOURCE_ID ";

    // The FROM_CLAUSE_ROOT is used in two classes - this, InclusionQuerySegmentAggregator
    // The {0},{1} is used so the table is able to be replaced by a derived table.  
    protected static final String FROM_CLAUSE_ROOT =
            "FROM {0} R JOIN {1} LR ON R.LOGICAL_RESOURCE_ID=LR.LOGICAL_RESOURCE_ID AND R.RESOURCE_ID = LR.CURRENT_RESOURCE_ID ";
    protected static final String WHERE_CLAUSE_ROOT = "WHERE R.IS_DELETED <> 'Y'";
    protected static final String PARAMETER_TABLE_ALIAS = "pX";
    private static final String FROM = " FROM ";
    private static final String UNION = " UNION ALL ";
    protected static final String ON = " ON ";
    private static final String AND = " AND ";
    protected static final String COMBINED_RESULTS = " COMBINED_RESULTS";
    private static final String DEFAULT_ORDERING = " ORDER BY R.RESOURCE_ID ASC ";

    private static final Set<String> SKIP_WHERE = new HashSet<>(Arrays.asList("_id", "_lastUpdated"));

    protected Class<?> resourceType;

    /**
     * querySegments and searchQueryParameters are used as parallel arrays
     * and should be added to/removed together.
     */
    protected List<SqlQueryData> querySegments = new ArrayList<>();
    protected List<Parameter> searchQueryParameters = new ArrayList<>();

    protected Parameter queryParamId = null;
    protected Parameter queryParamLastUpdated = null;

    private int offset;
    private int pageSize;
    protected ParameterDAO parameterDao;
    protected ResourceDAO resourceDao;

    /**
     * Constructs a new QueryBuilderHelper
     * 
     * @param resourceType - The type of FHIR Resource to be searched for.
     * @param offset       - The beginning index of the first search result.
     * @param pageSize     - The max number of requested search results.
     */
    protected QuerySegmentAggregator(Class<?> resourceType, int offset, int pageSize,
            ParameterDAO parameterDao, ResourceDAO resourceDao) {
        super();
        this.resourceType          = resourceType;
        this.offset                = offset;
        this.pageSize              = pageSize;
        this.parameterDao          = parameterDao;
        this.resourceDao           = resourceDao;
        this.querySegments         = new ArrayList<>();
        this.searchQueryParameters = new ArrayList<>();

    }

    /**
     * Adds a query segment, which is a where clause segment corresponding to the
     * passed query Parameter and its encapsulated search values.
     * 
     * @param querySegment A piece of a SQL WHERE clause
     * @param queryParm    - The corresponding query parameter
     */
    protected void addQueryData(SqlQueryData querySegment, Parameter queryParm) {
        final String METHODNAME = "addQueryData";
        log.entering(CLASSNAME, METHODNAME);

        //parallel arrays
        this.querySegments.add(querySegment);

        String name = queryParm.getName();
        if ("_id".compareTo(name) == 0) {
            queryParamId = queryParm;
        } else if ("_lastUpdated".compareTo(name) == 0) {
            queryParamLastUpdated = queryParm;
        } else {
            this.searchQueryParameters.add(queryParm);
        }

        log.exiting(CLASSNAME, METHODNAME);
    }

    /**
     * Builds a complete SQL Query based upon the encapsulated query segments and
     * bind variables.
     * 
     * @return SqlQueryData - contains the complete SQL query string and any
     *         associated bind variables.
     * @throws Exception
     */
    protected SqlQueryData buildQuery() throws Exception {
        final String METHODNAME = "buildQuery";
        log.entering(CLASSNAME, METHODNAME);

        StringBuilder queryString = new StringBuilder();
        SqlQueryData queryData;
        List<Object> allBindVariables = new ArrayList<>();

        if (this.isSystemLevelSearch()) {
            queryData = this.buildSystemLevelQuery(SYSTEM_LEVEL_SELECT_ROOT, SYSTEM_LEVEL_SUBSELECT_ROOT, true);
        } else {
            queryString.append(SELECT_ROOT);

            queryString.append(this.buildFromClause());

            queryString.append(this.buildWhereClause());

            for (SqlQueryData querySegment : this.querySegments) {
                allBindVariables.addAll(querySegment.getBindVariables());
            }
            // Add default ordering
            queryString.append(DEFAULT_ORDERING);
            this.addPaginationClauses(queryString);
            queryData = new SqlQueryData(queryString.toString(), allBindVariables);
        }

        log.exiting(CLASSNAME, METHODNAME, queryData);
        return queryData;
    }

    /**
     * Builds a complete SQL count query based upon the encapsulated query segments
     * and bind variables.
     * 
     * @return SqlQueryData - contains the complete SQL count query string and any
     *         associated bind variables.
     * @throws Exception
     */
    protected SqlQueryData buildCountQuery() throws Exception {
        final String METHODNAME = "buildCountQuery";
        log.entering(CLASSNAME, METHODNAME);

        StringBuilder queryString = new StringBuilder();
        SqlQueryData queryData;
        List<Object> allBindVariables = new ArrayList<>();

        if (this.isSystemLevelSearch()) {
            queryData =
                    this.buildSystemLevelQuery(SYSTEM_LEVEL_SELECT_COUNT_ROOT, SYSTEM_LEVEL_SUBSELECT_COUNT_ROOT,
                            false);
        } else {
            queryString.append(SELECT_COUNT_ROOT);

            queryString.append(this.buildFromClause());

            queryString.append(this.buildWhereClause());

            for (SqlQueryData querySegment : this.querySegments) {
                allBindVariables.addAll(querySegment.getBindVariables());
            }
            queryData = new SqlQueryData(queryString.toString(), allBindVariables);
        }

        log.exiting(CLASSNAME, METHODNAME, queryData);
        return queryData;

    }

    /**
     * Build a system level query or count query, based upon the encapsulated query
     * segments and bind variables and
     * the passed select-root strings.
     * A FHIR system level query spans multiple resource types, and therefore spans
     * multiple tables in the database.
     * 
     * @param selectRoot      - The text of the outer SELECT ('SELECT' to 'FROM')
     * @param subSelectRoot   - The text of the inner SELECT root to use in each
     *                        sub-select
     * @param addFinalClauses - Indicates whether or not ordering and pagination
     *                        clauses should be generated.
     * @return SqlQueryData - contains the complete SQL query string and any
     *         associated bind variables.
     * @throws Exception
     */
    protected SqlQueryData buildSystemLevelQuery(String selectRoot, String subSelectRoot, boolean addFinalClauses)
            throws Exception {
        final String METHODNAME = "buildSystemLevelQuery";
        log.entering(CLASSNAME, METHODNAME);

        StringBuilder queryString = new StringBuilder();
        SqlQueryData queryData;
        List<Object> allBindVariables = new ArrayList<>();
        Collection<Integer> resourceTypeIds;
        String tempFromClause;
        String resourceTypeName;
        boolean resourceTypeProcessed = false;
        Map<String, Integer> resourceNameIdMap = null;
        Map<Integer, String> resourceIdNameMap = null;

        queryString.append(selectRoot).append(FROM).append("(");

        resourceNameIdMap = this.resourceDao.readAllResourceTypeNames();
        resourceTypeIds   = resourceNameIdMap.values();
        resourceIdNameMap =
                resourceNameIdMap.entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

        for (Integer resourceTypeId : resourceTypeIds) {

            resourceTypeName = resourceIdNameMap.get(resourceTypeId) + "_";

            tempFromClause   = this.buildFromClause();
            tempFromClause   = tempFromClause.replaceAll("Resource_", resourceTypeName);
            if (resourceTypeProcessed) {
                queryString.append(UNION);
            }
            queryString.append(subSelectRoot).append(tempFromClause);
            resourceTypeProcessed = true;

            tempFromClause        = this.buildWhereClause();
            tempFromClause        = tempFromClause.replaceAll("Resource_", resourceTypeName);
            queryString.append(tempFromClause);

            for (SqlQueryData querySegment : this.querySegments) {
                allBindVariables.addAll(querySegment.getBindVariables());
            }
        }
        queryString.append(")").append(COMBINED_RESULTS);
        if (addFinalClauses) {
            queryString.append(" ORDER BY RESOURCE_ID ASC ");
            this.addPaginationClauses(queryString);
        }

        queryData = new SqlQueryData(queryString.toString(), allBindVariables);

        log.exiting(CLASSNAME, METHODNAME, queryData);
        return queryData;
    }

    /**
     * Builds the FROM clause for the SQL query being generated. The appropriate
     * Resource and Parameter table names are included
     * along with an alias for each table.
     * 
     * @return A String containing the FROM clause
     * @throws Exception
     */
    protected String buildFromClause() throws Exception {
        final String METHODNAME = "buildFromClause";
        log.entering(CLASSNAME, METHODNAME);

        StringBuilder fromClause = new StringBuilder();

        // IF AND ONLY if _id or _lastupdated is true. 
        fromClause.append(buildInterimFromForIdAndLastUpdated(this.resourceType.getSimpleName()));
        fromClause.append(" ");

        log.exiting(CLASSNAME, METHODNAME);
        return fromClause.toString();

    }

    /** 
     * build interim from clause creates a DERIVED table when necessary. 
     * The derived table is part of the LEFT INNER JOIN filtering down on the resources as the query is converted
     * to a Query plan. 
     * 
     * @param tableType
     * @return a combination of derived tables and JOINs. 
     */
    protected String buildInterimFromForIdAndLastUpdated(String tableType) {
        // Create the Parameters are used in the format
        // <code>{0}_RESOURCES </code><code> {1}_LOGICAL_RESOURCES</code>
        String resourceTable =  tableType + "_RESOURCES";
        String logicalResourceTable = tableType + "_LOGICAL_RESOURCES";

        // If this is not null, we need to do some changes to the parameter (and the various values 1..* contained)
        // The order of these subsequent TWO blocks is IMPORTANT
        
        // For the LOGICAL_ID the IN operator is used, as the value is a STRING
        // The string value is treated as an exact match. 
        if (queryParamId != null) {
            StringBuilder logicalResourceTableBuilder = new StringBuilder();
            logicalResourceTableBuilder.append("( SELECT * FROM ");
            logicalResourceTableBuilder.append(logicalResourceTable);
            logicalResourceTableBuilder.append(" ILR WHERE ILR.LOGICAL_ID IN ( ");
            
            int count = queryParamId.getValues().size();
            for(int i = 0; i < count; i++){
                if ( i != 0) {
                    logicalResourceTableBuilder.append(",");
                }
                
                logicalResourceTableBuilder.append("? ");
                
                searchQueryParameters.add(0, queryParamId);
            }
            logicalResourceTableBuilder.append(")) ");

            if (log.isLoggable(Level.FINEST)) {
                log.finest(logicalResourceTableBuilder.toString());
            }
            logicalResourceTable = logicalResourceTableBuilder.toString();
        }

        // The Date is compared in many different ways. 
        if (queryParamLastUpdated  != null) {
            StringBuilder resourceTableBuilder = new StringBuilder();
            resourceTableBuilder.append("( SELECT * FROM ");
            resourceTableBuilder.append(resourceTable);
            resourceTableBuilder.append(" IR WHERE ");
            
            int count = queryParamLastUpdated.getValues().size();
            List<ParameterValue> values = queryParamLastUpdated.getValues();
            for(int i = 0; i < count; i++) {
                if ( i != 0) {
                    resourceTableBuilder.append(" OR ");
                }
                
                // Get the Operand, and use it to switch between various behaviors. 
                ParameterValue pValue = values.get(i);
                Prefix operand = pValue.getPrefix();    
                if (operand == null) {
                    operand = Prefix.EQ;
                } 
                
                handleComparisionOperators(queryParamLastUpdated, operand, resourceTableBuilder);
                
            }
            
            resourceTableBuilder.append(") ");
            if (log.isLoggable(Level.FINEST)) {
                log.finest(resourceTableBuilder.toString());
            }
            resourceTable = resourceTableBuilder.toString();
        }

        return MessageFormat.format(FROM_CLAUSE_ROOT, resourceTable, logicalResourceTable);
    }
    
    public void handleComparisionOperators(Parameter queryParamLastUpdated, Prefix operand, StringBuilder whereClauseSegment) {
        whereClauseSegment.append("(  IR.LAST_UPDATED ");
        
        searchQueryParameters.add(0, queryParamLastUpdated);
        
        switch (operand) {
        case EB:
            // the value for the parameter in the resource **ends before** the provided value
            whereClauseSegment.append(JDBCOperator.LT.value()).append(BIND_VAR);
            break;
        case SA:
            // the value for the parameter in the resource **starts after** the provided value
            whereClauseSegment.append(JDBCOperator.GT.value()).append(BIND_VAR);
            break;
        case GE:
            // the value for the parameter in the resource is greater or equal to the provided value
            whereClauseSegment.append(JDBCOperator.GTE.value()).append(BIND_VAR);
            break;
        case GT:
            // the value for the parameter in the resource is greater than the provided value
            whereClauseSegment.append(JDBCOperator.GT.value()).append(BIND_VAR);
            break;
        case LE:
            // the value for the parameter in the resource is less or equal to the provided value
            whereClauseSegment.append(JDBCOperator.LTE.value()).append(BIND_VAR);
            break;
        case LT:
            // the value for the parameter in the resource is less than the provided value
            whereClauseSegment.append(JDBCOperator.LT.value()).append(BIND_VAR);
            break;
        case AP:
            // the value for the parameter in the resource is approximately the same to the provided value.
            // Note that the recommended value for the approximation is 10% of the stated value (or for a date, 
            // 10% of the gap between now and the date), but systems may choose other values where appropriate

//            // 1. search range fully contains the target period
//            whereClauseSegment.append(LEFT_PAREN);
//            whereClauseSegment.append(tableAlias + DOT).append(DATE_START).append(JDBCOperator.GTE.value()).append(BIND_VAR);
//            whereClauseSegment.append(JDBCOperator.AND.value());
//            whereClauseSegment.append(tableAlias + DOT).append(DATE_END).append(JDBCOperator.LT.value()).append(BIND_VAR);
//            whereClauseSegment.append(RIGHT_PAREN);
//
//            whereClauseSegment.append(JDBCOperator.OR.value());
//            // 2. search range begins during the target period
//            whereClauseSegment.append(LEFT_PAREN);
//            whereClauseSegment.append(tableAlias + DOT).append(DATE_START).append(JDBCOperator.LTE.value()).append(BIND_VAR);
//            whereClauseSegment.append(JDBCOperator.AND.value());
//            whereClauseSegment.append(tableAlias + DOT).append(DATE_END).append(JDBCOperator.GTE.value()).append(BIND_VAR);
//            whereClauseSegment.append(RIGHT_PAREN);
//
//            whereClauseSegment.append(JDBCOperator.OR.value());
//            // 3. search range ends during the target period
//            whereClauseSegment.append(LEFT_PAREN);
//            whereClauseSegment.append(tableAlias + DOT).append(DATE_START)
//                // strictly less than because the implicit end of the search range is exclusive
//                .append(JDBCOperator.LT.value()).append(BIND_VAR);
//            whereClauseSegment.append(JDBCOperator.AND.value());
//            whereClauseSegment.append(tableAlias + DOT).append(DATE_END).append(JDBCOperator.GTE.value()).append(BIND_VAR);
//            whereClauseSegment.append(RIGHT_PAREN);
            break;
        case NE:
            // the range of the search value does not fully contain the range of the target value
            whereClauseSegment.append(JDBCOperator.LT.value()).append(BIND_VAR);
            whereClauseSegment.append(")");
            whereClauseSegment.append(JDBCOperator.OR.value());
            whereClauseSegment.append("(  IR.LAST_UPDATED ");
            whereClauseSegment.append(JDBCOperator.GTE.value()).append("'");
            whereClauseSegment.append(translateDateTime(queryParamLastUpdated.getValues().get(0)));
            whereClauseSegment.append("'");
            break;
        case EQ:
        default:
            // the value for the parameter in the resource is equal to the provided value
            // GTE
            whereClauseSegment.append(JDBCOperator.EQ.value()).append(BIND_VAR);
            break;
        }
        whereClauseSegment.append(" )");
    }
    
    public String translateDateTime(ParameterValue parameter){
        DateTime dateTime = parameter.getValueDate();
        
        java.time.Instant time;
        if (dateTime.isPartial()) {
            time = QueryBuilderUtil.getInstantFromPartial(dateTime.getValue());
        } else {
            // fully specified time including zone, so we can interpret as an instant
            time = java.time.Instant.from(dateTime.getValue());
        }
        
        return Timestamp.from(time).toString();
    }

    /**
     * Builds the WHERE clause for the query being generated. This method aggregates
     * the contained query segments, and ties those segments back
     * to the appropriate parameter table alias.
     * 
     * @return
     */
    protected String buildWhereClause() {
        final String METHODNAME = "buildWhereClause";
        log.entering(CLASSNAME, METHODNAME);
        boolean isLocationQuery;

        StringBuilder whereClause = new StringBuilder();
        String whereClauseSegment;

        whereClause.append(WHERE_CLAUSE_ROOT);
        if (!this.querySegments.isEmpty()) {
            for (int i = 0; i < this.querySegments.size(); i++) {
                SqlQueryData querySegment = this.querySegments.get(i);
                Parameter param = this.searchQueryParameters.get(i);

                // Being bold here... this part should NEVER get a NPE. 
                // The parameter would not be parsed and passed successfully,
                // the NPE would have occurred earlier in the stack. 
                String name = param.getName();
                if (!SKIP_WHERE.contains(name)) {

                    whereClauseSegment = querySegment.getQueryString();
                    if (Modifier.MISSING.equals(param.getModifier())) {
                        whereClause.append(AND).append(whereClauseSegment);
                    } else {

                        whereClause.append(AND).append("R.LOGICAL_RESOURCE_ID IN (SELECT LOGICAL_RESOURCE_ID FROM ");
                        whereClause.append(this.resourceType.getSimpleName());
                        isLocationQuery =
                                Location.class.equals(this.resourceType)
                                        && param.getName().equals(AbstractQueryBuilder.NEAR);
                        switch (param.getType()) {
                        case URI:
                        case REFERENCE:
                        case STRING:
                            whereClause.append("_STR_VALUES ");
                            break;
                        case NUMBER:
                            whereClause.append("_NUMBER_VALUES ");
                            break;
                        case QUANTITY:
                            whereClause.append("_QUANTITY_VALUES ");
                            break;
                        case DATE:
                            whereClause.append("_DATE_VALUES ");
                            break;
                        case TOKEN:
                            if (isLocationQuery) {
                                whereClause.append("_LATLNG_VALUES ");
                            } else {
                                whereClause.append("_TOKEN_VALUES ");
                            }
                            break;
                        }
                        whereClauseSegment = whereClauseSegment.replaceAll(PARAMETER_TABLE_ALIAS + ".", "");
                        whereClause.append(" WHERE ").append(whereClauseSegment).append(")");
                    }
                }
            }
        }

        log.exiting(CLASSNAME, METHODNAME);
        return whereClause.toString();
    }

    /**
     * @return true if this instance represents a FHIR system level search
     */
    protected boolean isSystemLevelSearch() {
        return Resource.class.equals(this.resourceType);
    }

    /**
     * Adds the appropriate pagination clauses to the passed query string buffer,
     * based on the type
     * of database we're running against.
     * 
     * @param queryString A query string buffer.
     * @throws Exception
     */
    protected void addPaginationClauses(StringBuilder queryString) throws Exception {

        if (this.parameterDao.isDb2Database()) {
            queryString.append(" LIMIT ").append(this.pageSize).append(" OFFSET ").append(this.offset);
        } else {
            queryString.append(" OFFSET ").append(this.offset).append(" ROWS")
                    .append(" FETCH NEXT ").append(this.pageSize).append(" ROWS ONLY");
        }
    }
}
