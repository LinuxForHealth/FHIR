/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util;

import static com.ibm.fhir.persistence.jdbc.JDBCConstants.COMBINED_RESULTS;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.FROM;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.JOIN;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.ON;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.PARAMETER_TABLE_ALIAS;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.UNION;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.persistence.jdbc.dao.api.ParameterDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.ResourceDAO;
import com.ibm.fhir.persistence.jdbc.util.type.DateParmBehaviorUtil;
import com.ibm.fhir.search.SearchConstants.Modifier;
import com.ibm.fhir.search.SearchConstants.Type;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.parameters.QueryParameterValue;

/**
 * This class assists the JDBCQueryBuilder. Its purpose is to aggregate SQL query segments together to produce a well-formed FHIR Resource query or 
 * FHIR Resource count query. 
 */
public class QuerySegmentAggregator {
    private static final String CLASSNAME = QuerySegmentAggregator.class.getName();
    private static final Logger log = java.util.logging.Logger.getLogger(CLASSNAME);
    
    protected static final String SELECT_ROOT = "SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID, R.LAST_UPDATED, R.IS_DELETED, R.DATA, LR.LOGICAL_ID ";
    protected static final String SYSTEM_LEVEL_SELECT_ROOT = "SELECT RESOURCE_ID, LOGICAL_RESOURCE_ID, VERSION_ID, LAST_UPDATED, IS_DELETED, DATA, LOGICAL_ID ";
    protected static final String SYSTEM_LEVEL_SUBSELECT_ROOT = SELECT_ROOT;
    protected static final String SELECT_COUNT_ROOT = "SELECT COUNT(R.RESOURCE_ID) ";
    protected static final String SYSTEM_LEVEL_SELECT_COUNT_ROOT = "SELECT COUNT(RESOURCE_ID) ";
    protected static final String SYSTEM_LEVEL_SUBSELECT_COUNT_ROOT = " SELECT R.RESOURCE_ID ";
    protected static final String WHERE_CLAUSE_ROOT = "WHERE R.IS_DELETED <> 'Y'";
    protected static final String DEFAULT_ORDERING = " ORDER BY R.RESOURCE_ID ASC ";

    protected static final Set<String> SKIP_WHERE = new HashSet<>(Arrays.asList("_id"));
    
    protected Class<?> resourceType;
    
    // Used for whole system search on multiple resource types.
    private List<String> resourceTypes = null;

    /**
     * querySegments and searchQueryParameters are used as parallel arrays
     * and should be added to/removed together. 
     */
    protected List<SqlQueryData> querySegments;
    protected List<QueryParameter> searchQueryParameters;
    
    // used for special treatment of List<Parameters> of _id and _lastUpdated
    protected List<Parameter> queryParamIds = new ArrayList<>();
    protected List<Parameter> queryParamLastUpdateds = new ArrayList<>();
    
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
    protected void addQueryData(SqlQueryData querySegment,QueryParameter queryParm) {
        final String METHODNAME = "addQueryData";
        log.entering(CLASSNAME, METHODNAME);
        
        //parallel arrays
        this.querySegments.add(querySegment);
        
        String code = queryParm.getCode();
        if("_id".compareTo(code)==0) {
            queryParamIds.add(queryParm);
        }
        if(DateParmBehaviorUtil.LAST_UPDATED.compareTo(code)==0) {
            queryParamLastUpdateds.add(queryParm);
        }
        else { 
            // Only add the query parameter one time, if it's not _id
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
     * Builds a complete SQL count query based upon the encapsulated query segments and bind variables.
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
        fromClause.append(FROM);
        processFromClauseForId(fromClause, simpleName);
        fromClause.append(" LR JOIN ");
        processFromClauseForLastUpdated(fromClause, simpleName);
        fromClause.append(" R ON R.LOGICAL_RESOURCE_ID=LR.LOGICAL_RESOURCE_ID AND R.RESOURCE_ID = LR.CURRENT_RESOURCE_ID AND R.IS_DELETED <> 'Y' ");

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
        
        if (!queryParamIds.isEmpty()) {
            // ID, then special handling. 
            fromClause.append("( SELECT * FROM ");
            fromClause.append(target);
            fromClause.append("_LOGICAL_RESOURCES");
            fromClause.append(" ILR WHERE ILR.LOGICAL_ID IN ( ");
            //TODO fromClause.append(queryParamId.getValues().stream().map(param -> "?" ).collect(Collectors.joining(", ")));
            fromClause.append(" )) ");
        } else {
            // Not ID, then go to the default. 
            fromClause.append(target);
            fromClause.append("_LOGICAL_RESOURCES");
        }
    }
    
    private void processFromClauseForLastUpdated(StringBuilder fromClause, String target) {
        if(!queryParamLastUpdateds.isEmpty()) {
            // Start the Drived Table
            fromClause.append("( SELECT * FROM ");
            fromClause.append(target);
            fromClause.append("_RESOURCES IR ");

            // Process the Condtional
            fromClause.append("WHERE ILR.LOGICAL_ID IN ( ");
            //fromClause.append(queryParamId.getValues().stream().map(param -> "?" ).collect(Collectors.joining(", ")));
            
            // Close out the Derived Tables
            fromClause.append(" )) ");
        } else {
            // Not _lastUpdated, then go to the default. 
            fromClause.append(target);
            fromClause.append("_RESOURCES");
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
        
        // Override the Type is null, then use the default type here. 
        if(overrideType == null) {
            overrideType = this.resourceType.getSimpleName();
        }

        StringBuilder whereClause = new StringBuilder();
        String whereClauseSegment;

        for (int i = 0; i < this.querySegments.size(); i++) {
            SqlQueryData querySegment = this.querySegments.get(i);
            QueryParameter param = this.searchQueryParameters.get(i);

            // Being bold here... this part should NEVER get a NPE. 
            // The parameter would not be parsed and passed successfully,
            // the NPE would have occurred earlier in the stack. 
            String code = param.getCode();
            if (!SKIP_WHERE.contains(code)) {

                if (Modifier.MISSING.equals(param.getModifier())) {
                    whereClauseSegment = querySegment.getQueryString().replaceAll(PARAMETER_TABLE_ALIAS + "\\.", "");
                    whereClause.append(whereClauseSegment);
                } else {
                    if (!Type.COMPOSITE.equals(param.getType())) {
                        whereClauseSegment = querySegment.getQueryString().replaceAll(PARAMETER_TABLE_ALIAS + "\\.", "");

                        whereClause.append(JOIN).append("(SELECT DISTINCT LOGICAL_RESOURCE_ID FROM ");
                        whereClause.append(tableName(overrideType, param));
                    } else {
                        // add an alias for the composite table
                        String compositeAlias = "comp" + (i+1);
                        whereClauseSegment = querySegment.getQueryString().replaceAll(PARAMETER_TABLE_ALIAS + "\\.", compositeAlias + ".");

                        whereClause.append(JOIN).append("(SELECT DISTINCT " + compositeAlias + ".LOGICAL_RESOURCE_ID FROM ");
                        whereClause.append(tableName(overrideType, param))
                                   .append(compositeAlias);

                        if (param.getValues() != null && !param.getValues().isEmpty()) {
                            // Assumption:  all the values should have the same number of components and the same types
                            QueryParameterValue queryParameterValue = param.getValues().get(0);
                            List<QueryParameter> components = queryParameterValue.getComponent();
                            for (int componentNum = 1; componentNum <= components.size(); componentNum++) {
                                String alias = compositeAlias + "_p" + componentNum;
                                QueryParameter component = components.get(componentNum - 1);
                                whereClause.append(JOIN + tableName(overrideType, component) + alias)
                                    .append(ON)
                                    .append(compositeAlias + ".COMP" + componentNum + abbr(component) )
                                    .append("=")
                                    .append(alias + ".ROW_ID");
                                whereClauseSegment = whereClauseSegment.replaceAll(
                                        PARAMETER_TABLE_ALIAS + "_p" + componentNum + "\\.", alias + ".");
                            }
                        }
                    }
                    whereClause.append(" WHERE ").append(whereClauseSegment).append(") ");
                    String tmpTableName = overrideType + i;
                    whereClause.append(tmpTableName).append(ON).append(tmpTableName).append(".LOGICAL_RESOURCE_ID = R.LOGICAL_RESOURCE_ID");
                }
            } // end if SKIP
        } // end for

        log.exiting(CLASSNAME, METHODNAME);
        return whereClause.toString();
    }

    public static String tableName(String resourceType, QueryParameter param) {
        StringBuilder name = new StringBuilder(resourceType);
        switch (param.getType()) {
        case URI:
        case REFERENCE:
        case STRING:
        case NUMBER:
        case QUANTITY:
        case DATE:
        case TOKEN:
        case SPECIAL:
            name.append(abbr(param) + "_VALUES ");
            break;
        case COMPOSITE:
            name.append("_COMPOSITES ");
            break;
        }
        return name.toString();
    }
    
    public static String abbr(QueryParameter param) {
        switch (param.getType()) {
        case URI:
        case REFERENCE:
        case STRING:
            return "_STR";
        case NUMBER:
            return "_NUMBER";
        case QUANTITY:
            return "_QUANTITY";
        case DATE:
            return "_DATE";
        case TOKEN:
            return "_TOKEN";
        case SPECIAL:
            return "_LATLNG";
        case COMPOSITE:
        default:
            throw new IllegalArgumentException("There is no abbreviation for parameter values table of type " + param.getType());
        }
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
