/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.model.resource.Location;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.persistence.jdbc.dao.api.ParameterDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.ResourceDAO;
import com.ibm.fhir.persistence.util.AbstractQueryBuilder;
import com.ibm.fhir.search.SearchConstants.Modifier;
import com.ibm.fhir.search.parameters.Parameter;

/**
 * This class assists the JDBCQueryBuilder. Its purpose is to aggregate SQL query segments together to produce a well-formed FHIR Resource query or 
 * FHIR Resource count query. 
 */
class QuerySegmentAggregator {
    private static final String CLASSNAME = QuerySegmentAggregator.class.getName();
    private static final Logger log = java.util.logging.Logger.getLogger(CLASSNAME);

    protected static final String SELECT_ROOT = "SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID, R.LAST_UPDATED, R.IS_DELETED, R.DATA, LR.LOGICAL_ID ";
    protected static final String SYSTEM_LEVEL_SELECT_ROOT = "SELECT RESOURCE_ID, LOGICAL_RESOURCE_ID, VERSION_ID, LAST_UPDATED, IS_DELETED, DATA, LOGICAL_ID ";
    protected static final String SYSTEM_LEVEL_SUBSELECT_ROOT = SELECT_ROOT;
    private static final String SELECT_COUNT_ROOT = "SELECT COUNT(R.RESOURCE_ID) ";
    private static final String SYSTEM_LEVEL_SELECT_COUNT_ROOT = "SELECT COUNT(RESOURCE_ID) ";
    private static final String SYSTEM_LEVEL_SUBSELECT_COUNT_ROOT = " SELECT R.RESOURCE_ID ";

    protected static final String WHERE_CLAUSE_ROOT = "WHERE R.IS_DELETED <> 'Y'";
    protected static final String PARAMETER_TABLE_ALIAS = "pX";
    private static final String FROM = " FROM ";
    private static final String UNION = " UNION ALL ";
    protected static final String ON = " ON ";
    private static final String AND = " AND ";
    protected static final String COMBINED_RESULTS = " COMBINED_RESULTS";
    private static final String DEFAULT_ORDERING = " ORDER BY R.RESOURCE_ID ASC ";

    private static final Set<String> SKIP_WHERE = new HashSet<>(Arrays.asList("_id"));
    
    protected Class<?> resourceType;
    
    // Used for whole system search on multiple resource types.
    private List<String> resourceTypes = null;

    /**
     * querySegments and searchQueryParameters are used as parallel arrays
     * and should be added to/removed together. 
     */
    protected List<SqlQueryData> querySegments;
    protected List<Parameter> searchQueryParameters;
    
    // used for special treatment of _id and _lastUpdated
    protected Parameter queryParamId = null;
    protected Parameter queryParamLastUpdated = null;
    
    private int offset;
    private int pageSize;
    protected ParameterDAO parameterDao;
    protected ResourceDAO resourceDao;

    /**
     * Constructs a new QueryBuilderHelper
     * @param resourceType - The type of FHIR Resource to be searched for.
     * @param offset - The beginning index of the first search result.
     * @param pageSize - The max number of requested search results.
     */
    protected QuerySegmentAggregator(Class<?> resourceType, int offset, int pageSize, 
                                    ParameterDAO parameterDao, ResourceDAO resourceDao) {
        super();
        this.resourceType = resourceType;
        this.offset = offset;
        this.pageSize = pageSize;
        this.parameterDao = parameterDao;
        this.resourceDao = resourceDao;
        this.querySegments = new ArrayList<>();
        this.searchQueryParameters = new ArrayList<>();
         
    }
    
    public void setResourceTypes(List<String> resourceTypes) {
        this.resourceTypes = resourceTypes;
    }
    
    /**
     * Adds a query segment, which is a where clause segment corresponding to the passed query Parameter and its encapsulated search values.
     * @param querySegment A piece of a SQL WHERE clause 
     * @param queryParm - The corresponding query parameter
     */
    protected void addQueryData(SqlQueryData querySegment,Parameter queryParm) {
        final String METHODNAME = "addQueryData";
        log.entering(CLASSNAME, METHODNAME);
        
        //parallel arrays
        this.querySegments.add(querySegment);
        
        String name = queryParm.getCode();
        if("_id".compareTo(name)==0) {
            queryParamId = queryParm;
        } else if("_lastUpdated".compareTo(name)==0) {
            queryParamLastUpdated = queryParm;
        } else { 
            // Only add the query parameter one time, if it's not _id or _lastUpdated.
            this.searchQueryParameters.add(queryParm);
        }

        log.exiting(CLASSNAME, METHODNAME);
         
    }
    
    /**
     * Builds a complete SQL Query based upon the encapsulated query segments and bind variables.
     * @return SqlQueryData - contains the complete SQL query string and any associated bind variables.
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
        }
        else {
            queryString.append(SELECT_ROOT);
            queryString.append(this.buildFromClause());
            queryString.append(this.buildWhereClause(null));

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
     *   Builds a complete SQL count query based upon the encapsulated query segments and bind variables.
     * @return SqlQueryData - contains the complete SQL count query string and any associated bind variables.
     * @throws Exception 
     */
    protected SqlQueryData buildCountQuery() throws Exception {
        final String METHODNAME = "buildCountQuery";
        log.entering(CLASSNAME, METHODNAME);
        
        StringBuilder queryString = new StringBuilder();
        SqlQueryData queryData;
        List<Object> allBindVariables = new ArrayList<>();
        
        if (this.isSystemLevelSearch()) {
            queryData = this.buildSystemLevelQuery(SYSTEM_LEVEL_SELECT_COUNT_ROOT, SYSTEM_LEVEL_SUBSELECT_COUNT_ROOT, false);
        }
        else {
            queryString.append(SELECT_COUNT_ROOT);
                    
            queryString.append(this.buildFromClause());
            
            queryString.append(this.buildWhereClause(null));
            
            for (SqlQueryData querySegment : this.querySegments) {
                allBindVariables.addAll(querySegment.getBindVariables());
            }
            queryData = new SqlQueryData(queryString.toString(), allBindVariables);
        }
        
        log.exiting(CLASSNAME, METHODNAME, queryData);
        return queryData;
        
    }
    
    /**
     * Build a system level query or count query, based upon the encapsulated query segments and bind variables and
     * the passed select-root strings.
     * A FHIR system level query spans multiple resource types, and therefore spans multiple tables in the database. 
     * @param selectRoot - The text of the outer SELECT ('SELECT' to 'FROM')
     * @param subSelectRoot - The text of the inner SELECT root to use in each sub-select
     * @param addFinalClauses - Indicates whether or not ordering and pagination clauses should be generated.
     * @return SqlQueryData - contains the complete SQL query string and any associated bind variables.
     * @throws Exception
     */
    protected SqlQueryData buildSystemLevelQuery(String selectRoot, String subSelectRoot, boolean addFinalClauses) 
                                                    throws Exception {
        final String METHODNAME = "buildSystemLevelQuery";
        log.entering(CLASSNAME, METHODNAME);
        
        StringBuilder queryString = new StringBuilder();
        SqlQueryData queryData;
        List<Object> allBindVariables = new ArrayList<>();
        
        String tempFromClause;
        boolean resourceTypeProcessed = false;

        queryString.append(selectRoot).append(FROM).append("(");

        // Processes through EACH register parameter extracting the integer value
        Map<String, Integer> resourceNameMap = resourceDao.readAllResourceTypeNames();
        
        for(Map.Entry<String,Integer> resourceEntry : resourceNameMap.entrySet()) {
            String resourceTypeName =  resourceEntry.getKey();
            // Only search the required resource types if any.
            if (this.resourceTypes != null && !this.resourceTypes.contains(resourceTypeName)) {
                continue;
            }
            
            tempFromClause = this.buildFromClause(resourceTypeName);
            
            // Skip the UNION on the first, and change to indicate
            // subsequent resourceTypes are to be unioned. 
            if (resourceTypeProcessed) {
                queryString.append(UNION);
            }
            resourceTypeProcessed = true;
            
            queryString.append(subSelectRoot).append(tempFromClause);

            tempFromClause = this.buildWhereClause(resourceTypeName);
            queryString.append(tempFromClause);

            for (SqlQueryData querySegment : this.querySegments) {
                allBindVariables.addAll(querySegment.getBindVariables());
            }
        }
        
        // End the Combined Results
        queryString.append(")").append(COMBINED_RESULTS);
        
        // Add Ordering and Pagination
        if (addFinalClauses) {
            queryString.append(" ORDER BY RESOURCE_ID ASC ");
            this.addPaginationClauses(queryString);
        }
        
        queryData = new SqlQueryData(queryString.toString(), allBindVariables);
        
        log.exiting(CLASSNAME, METHODNAME, queryData);
        return queryData;
    }
    
    /**
     * Builds the FROM clause for the SQL query being generated. The appropriate Resource and Parameter table names are included 
     * along with an alias for each table.
     * @return A String containing the FROM clause
     */
    protected String buildFromClause() {
        return buildFromClause(this.resourceType.getSimpleName());
    }
    
    /**
     * enables calls to buildFromClause to avoid redoing string replacements. 
     * 
     * @param simpleName
     * @return
     */
    protected String buildFromClause(String simpleName) {
        final String METHODNAME = "buildFromClauseWithString";
        log.entering(CLASSNAME, METHODNAME);

        StringBuilder fromClause = new StringBuilder();
        fromClause.append("FROM ");
        processFromClauseForId(fromClause, simpleName);
        fromClause.append(" LR JOIN ");
        fromClause.append(simpleName);
        fromClause.append("_RESOURCES");
        fromClause.append(" R ON R.LOGICAL_RESOURCE_ID=LR.LOGICAL_RESOURCE_ID AND R.RESOURCE_ID = LR.CURRENT_RESOURCE_ID ");

        log.exiting(CLASSNAME, METHODNAME);
        return fromClause.toString();
        
    }
    
    /*
     * Processes the From Clause for _id, as _id is contained in the LOGICAL_RESOURCES 
     * else, return a default table name
     * 
     * @param fromClause the non-null StringBuilder
     * @param target is the Target Type for the search
     */
    private void processFromClauseForId(StringBuilder fromClause, String target) {
        
        /*
         * The not null path uses a DERIVED TABLE. 
         * ILR refers to the intermediate Logical resource table and is just a convenient name.
         * 
         * The IN clause is effective here to drive a smaller table in the subsequent LEFT INNER JOINS
         * off the derived tables. 
         * <pre>
         * ( SELECT * FROM BASIC_LOGICAL_RESOURCES ILR WHERE ILR.LOGICAL_ID IN ( ? ? )) 
         * </pre>
         */
        
        if(queryParamId != null) {
            // ID, then special handling. 
            fromClause.append("( SELECT * FROM ");
            fromClause.append(target);
            fromClause.append("_LOGICAL_RESOURCES");
            fromClause.append(" ILR WHERE ILR.LOGICAL_ID IN ( ");
                        
            fromClause.append(queryParamId.getValues().stream().map(param -> "?" ).collect(Collectors.joining(", ")));
            fromClause.append(" )) ");
        } else {
            // Not ID, then go to the default. 
            fromClause.append(target);
            fromClause.append("_LOGICAL_RESOURCES");
        }
    }
    
    /**
     * Builds the WHERE clause for the query being generated. This method aggregates the contained query segments, and ties those segments back
     * to the appropriate parameter table alias.
     * 
     * @param overrideType if not null, then it's the default type used in the building of the where clause. 
     * @return
     */
    protected String buildWhereClause(String overrideType) {
        final String METHODNAME = "buildWhereClause";
        log.entering(CLASSNAME, METHODNAME);
        boolean isLocationQuery;
        
        // Override the Type is null, then use the default type here. 
        if(overrideType == null) {
            overrideType = this.resourceType.getSimpleName();
        }

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
                String code = param.getCode();
                if (!SKIP_WHERE.contains(code)) {

                    whereClauseSegment = querySegment.getQueryString();
                    if (Modifier.MISSING.equals(param.getModifier())) {
                        whereClause.append(AND).append(whereClauseSegment);
                    } else {

                        whereClause.append(AND).append("R.LOGICAL_RESOURCE_ID IN (SELECT LOGICAL_RESOURCE_ID FROM ");
                        whereClause.append(overrideType);
                        isLocationQuery =
                                Location.class.equals(this.resourceType)
                                        && param.getCode().equals(AbstractQueryBuilder.NEAR);
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
     * 
     * @return true if this instance represents a FHIR system level search
     */
    protected boolean isSystemLevelSearch() {
        return Resource.class.equals(this.resourceType);
    }
    
    /**
     * Adds the appropriate pagination clauses to the passed query string buffer, based on the type
     * of database we're running against.
     * @param queryString A query string buffer.
     * @throws Exception
     */
    protected void addPaginationClauses(StringBuilder queryString) throws Exception {
        
        if(this.parameterDao.isDb2Database()) {
            queryString.append(" LIMIT ").append(this.pageSize).append(" OFFSET ").append(this.offset);
        }
        else {
            queryString.append(" OFFSET ").append(this.offset).append(" ROWS")
                       .append(" FETCH NEXT ").append(this.pageSize).append(" ROWS ONLY");
        }
    }
}
