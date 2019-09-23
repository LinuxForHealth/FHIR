/*
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util;

import static com.ibm.fhir.persistence.jdbc.util.JDBCNormalizedQueryBuilder.CODE_SYSTEM_ID;
import static com.ibm.fhir.persistence.jdbc.util.JDBCNormalizedQueryBuilder.DATE_VALUE;
import static com.ibm.fhir.persistence.jdbc.util.JDBCNormalizedQueryBuilder.NUMBER_VALUE;
import static com.ibm.fhir.persistence.jdbc.util.JDBCNormalizedQueryBuilder.QUANTITY_VALUE;
import static com.ibm.fhir.persistence.jdbc.util.JDBCNormalizedQueryBuilder.STR_VALUE;
import static com.ibm.fhir.persistence.jdbc.util.JDBCNormalizedQueryBuilder.TOKEN_VALUE;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceNotSupportedException;
import com.ibm.fhir.persistence.jdbc.dao.api.ParameterNormalizedDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.ResourceNormalizedDAO;
import com.ibm.fhir.search.SearchConstants.SortDirection;
import com.ibm.fhir.search.parameters.SortParameter;

/**
 * This class assists the JDBCNormalizedQueryBuilder. It extends the QuerySegmentAggregator to build a FHIR Resource query
 * that produces sorted search results.
 * 
 * @author markd
 *
 */
public class SortedQuerySegmentAggregator extends QuerySegmentAggregator {
    private static final String CLASSNAME = SortedQuerySegmentAggregator.class.getName();
    private static final Logger log = java.util.logging.Logger.getLogger(CLASSNAME);
    
    private static final String SORT_PARAMETER_ALIAS = "S";
        
    private List<SortParameter> sortParameters;

    /**
     * Constructs a new SortedQuerySegmentAggregator
     * @param resourceType - The type of FHIR Resource to be searched for.
     * @param offset - The beginning index of the first search result.
     * @param pageSize - The max number of requested search results.
     * @param FHIRDBDAO - A basic FHIR DB Data Access Object
     * @param sortParms - A list of SortParameters
     */
    protected SortedQuerySegmentAggregator(Class<?> resourceType, int offset, int pageSize, ParameterNormalizedDAO parameterDao, 
                                            ResourceNormalizedDAO resourceDao, List<SortParameter> sortParms) {
        super(resourceType, offset, pageSize, parameterDao, resourceDao);
        this.sortParameters = sortParms;
         
    }
    
    /**
     * Builds a complete SQL Query based upon the encapsulated query segments and bind variables. This query
     * contains the necessary clauses to support sorted search results.
     * A simple example query produced by this method:
     * 
     * SELECT R.RESOURCE_ID,MIN(S1.STR_VALUE) FROM 
     * Patient_RESOURCES R JOIN 
     * Patient_LOGICAL_RESOURCES LR ON R.LOGICAL_RESOURCE_ID=LR.LOGICAL_RESOURCE_ID  JOIN 
     * Patient_TOKEN_VALUES P1 ON P1.RESOURCE_ID=R.RESOURCE_ID  
     * LEFT OUTER JOIN Patient_STR_VALUES S1 ON (S1.PARAMETER_NAME_ID=50 AND S1.RESOURCE_ID = R.RESOURCE_ID) WHERE 
     * R.RESOURCE_ID = LR.CURRENT_RESOURCE_ID AND 
     * R.IS_DELETED <> 'Y' AND 
     * P1.RESOURCE_ID = R.RESOURCE_ID AND 
     * (P1.PARAMETER_NAME_ID=196 AND ((P1.TOKEN_VALUE = false))) 
     * GROUP BY R.RESOURCE_ID  
     * ORDER BY MIN(S1.STR_VALUE) asc NULLS LAST 
     * OFFSET 0 ROWS FETCH NEXT 100 ROWS ONLY;
     * 
     * @return SqlQueryData - contains the complete SQL query string and any associated bind variables.
     * @throws Exception 
     */
    @Override
    public SqlQueryData buildQuery() throws Exception {
        final String METHODNAME = "buildQuery";
        log.entering(CLASSNAME, METHODNAME);
        
        StringBuilder sqlSortQuery = new StringBuilder();
        SqlQueryData queryData;
        List<Object> allBindVariables = new ArrayList<>();
        StringBuilder sysLvlQueryString;
                        
        if (this.sortParameters == null || this.sortParameters.isEmpty()) {
            throw new FHIRPersistenceException("No sort parameters found.");
        }
        
        // Gather up all bind variables from the query segments
        for (SqlQueryData querySegment : this.querySegments) {
            allBindVariables.addAll(querySegment.getBindVariables());
        }
        
        // For system level search, the sort parameters are limited to a couple of columns in the *_resources  
        // and *_logical_resources tables. Execute the following special logic for sorted system-level-searches.
        if (this.isSystemLevelSearch()) {
            // Build query without order-by and pagination clauses.
            queryData = this.buildSystemLevelQuery(SYSTEM_LEVEL_SELECT_ROOT, SYSTEM_LEVEL_SUBSELECT_ROOT, false);
            sysLvlQueryString = new StringBuilder(queryData.getQueryString());
            // Add in order-by clause.
            sysLvlQueryString.append(this.buildSysLvlOrderByClause());
            // Add pagination clauses.
            this.addPaginationClauses(sysLvlQueryString);
            queryData = new SqlQueryData(sysLvlQueryString.toString(), queryData.getBindVariables());
        }
        else {
            // Build SELECT clause
            sqlSortQuery.append(this.buildSelectClause());
            
            // Build basic FROM clause
            sqlSortQuery.append(this.buildFromClause());
            
            // Build LEFT OUTER JOIN clause
            sqlSortQuery.append(this.buildSortJoinClause());
            
            // Build the WHERE clause
            sqlSortQuery.append(this.buildWhereClause());
            
            // Build GROUP BY clause
            sqlSortQuery.append(this.buildGroupByClause());
            
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
     * SELECT R.RESOURCE_ID,MIN(S1.STR_VALUE) FROM 
     * 
     * @throws FHIRPersistenceException
     */
    private String buildSelectClause()     throws FHIRPersistenceException {
        final String METHODNAME = "buildSelectClause";
        log.entering(CLASSNAME, METHODNAME);
        
        StringBuilder selectBuffer = new StringBuilder();
        
        selectBuffer.append("SELECT R.RESOURCE_ID");
        
        // Build MIN and/or MAX clauses
        for (int i = 0; i < this.sortParameters.size(); i++) {
            selectBuffer.append(",");
            selectBuffer.append(this.buildAggregateExpression(this.sortParameters.get(i), i+1, false));
        }
        
        selectBuffer.append(" ");
        //selectBuffer.append(" FROM ").append(this.resourceType.getSimpleName()).append("_RESOURCES R ");
            
        log.exiting(CLASSNAME, METHODNAME);
        return selectBuffer.toString();
    }

    /**
     * Builds the required MIN or MAX aggregate expressions for the passed sort parameter. 
     * @param sortParm A valid sort parameter.
     * @param sortParmIndex An integer representing the position of the sort parameter in a collection of sort parameters.
     * @param useInOrderByClause A flag indicating whether or not the returned aggregate expression is to be used in an ORDER BY clause.
     * @return
     * @throws FHIRPersistenceException
     */
    private String buildAggregateExpression(SortParameter sortParm, int sortParmIndex, boolean useInOrderByClause) throws FHIRPersistenceException {
        final String METHODNAME = "buildAggregateExpression";
        log.entering(CLASSNAME, METHODNAME);
        
        StringBuilder expression = new StringBuilder();
        List<String> valueAttributeNames;
                
        valueAttributeNames = this.getValueAttributeNames(sortParm);
        boolean nameProcessed = false;
        for(String attributeName : valueAttributeNames) {
            if (nameProcessed) {
                expression.append(", ");
            }
            if (sortParm.getDirection().equals(SortDirection.ASCENDING)) {
                expression.append("MIN");
            }
            else {
                expression.append("MAX");
            }
            expression.append("(");
            expression.append(SORT_PARAMETER_ALIAS).append(sortParmIndex).append(".");
            expression.append(attributeName);
            expression.append(")");
            if (useInOrderByClause) {
                expression.append(" ").append(sortParm.getDirection().value()) 
                    .append(" NULLS LAST");
            }
            nameProcessed = true;
        }
                
        log.exiting(CLASSNAME, METHODNAME);
        return expression.toString();
    }

    /**
     * Returns the names of the Parameter attributes containing the values corresponding to the passed sort parameter.
     * @throws FHIRPersistenceException
     */
    private List<String> getValueAttributeNames(SortParameter sortParm) throws FHIRPersistenceException {
        final String METHODNAME = "getValueAttributeName";
        log.entering(CLASSNAME, METHODNAME);
        
        List<String> attributeNames = new ArrayList<>();
        switch(sortParm.getType()) {
            case STRING:    attributeNames.add(STR_VALUE);
                            break;
            case REFERENCE: attributeNames.add(STR_VALUE);
                            break;
            case DATE:      attributeNames.add(DATE_VALUE);
                            break;
            case TOKEN:     attributeNames.add(CODE_SYSTEM_ID); //TODO This is probably wrong
                            attributeNames.add(TOKEN_VALUE);
                            break;
            case NUMBER:    attributeNames.add(NUMBER_VALUE);
                            break;
            case QUANTITY:  attributeNames.add(QUANTITY_VALUE);
                            break;
            case URI:          attributeNames.add(STR_VALUE);
                            break;
            default: throw new FHIRPersistenceNotSupportedException("Parm type not supported: " + sortParm.getType().value());
        }
                
        log.exiting(CLASSNAME, METHODNAME);
        return attributeNames;
        
    }

    /**
     * Builds the LEFT OUTER JOIN clauses necessary to return sorted Resource ids. 
     * For example:
     * JOIN r.parameters p1 
     * LEFT OUTER JOIN Patient_STR_VALUES S1 ON (S1.PARAMETER_NAME_ID=50 AND S1.LOGICAL_RESOURCE_ID = R.LOGICAL_RESOURCE_ID)  
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
        for (SortParameter sortParm: this.sortParameters) {
            sortParameterNameId = ParameterNamesCache.getParameterNameId(sortParm.getName());
            if (sortParameterNameId == null) {
                // Only read...don't try and create the parameter name if it doesn't exist
                sortParameterNameId = this.parameterDao.readParameterNameId(sortParm.getName());
                if (sortParameterNameId != null) {
                    this.parameterDao.addParameterNamesCacheCandidate(sortParm.getName(), sortParameterNameId);
                }
                else {
                    sortParameterNameId = -1; // so we don't break the query syntax
                }
            }
            
            // Note...the PARAMETER_NAME_ID=xxx is provided as a literal because this helps
            // the query optimizer significantly with index range scan cardinality estimation
            joinBuffer.append(" LEFT OUTER JOIN ").append(this.getSortParameterTableName(sortParm)).append(" ")
                      .append(SORT_PARAMETER_ALIAS).append(sortParmIndex)
                      .append(ON)
                      .append("(")
                      .append(SORT_PARAMETER_ALIAS).append(sortParmIndex).append(".PARAMETER_NAME_ID=").append(sortParameterNameId)
                      .append(" AND ")
                      .append(SORT_PARAMETER_ALIAS).append(sortParmIndex).append(".LOGICAL_RESOURCE_ID = R.LOGICAL_RESOURCE_ID")
                      .append(") ");
                    
            sortParmIndex++;
        }
                    
        log.exiting(CLASSNAME, METHODNAME);
        return joinBuffer.toString();
    }
    
    /**
     * Returns the name of the database table corresponding to the type of the passed sort parameter.
     * @param sortParm A valid SortParameter
     * @return String - A database table name
     * @throws FHIRPersistenceException
     */
    private String getSortParameterTableName(SortParameter sortParm) throws FHIRPersistenceException {
        final String METHODNAME = "getSortParameterTableName";
        log.entering(CLASSNAME, METHODNAME);
        
        StringBuilder sortParameterTableName = new StringBuilder();
        sortParameterTableName.append(this.resourceType.getSimpleName()).append("_");
        
        switch(sortParm.getType()) {
            case REFERENCE:
            case URI:
            case STRING:    sortParameterTableName.append("STR_VALUES");
                            break;
            case DATE:      sortParameterTableName.append("DATE_VALUES");
                            break;
            case TOKEN:     sortParameterTableName.append("TOKEN_VALUES");
                            break;
            case NUMBER:    sortParameterTableName.append("NUMBER_VALUES");
                            break;
            case QUANTITY:  sortParameterTableName.append("QUANTITY_VALUES");
                            break;
            
            default: throw new FHIRPersistenceNotSupportedException("Parm type not supported: " + sortParm.getType().value());
        }
                
        log.exiting(CLASSNAME, METHODNAME);
        return sortParameterTableName.toString();
    }

    /**
     * Builds the GROUP BY clause necessary to return sorted Resource ids. 
     * @throws FHIRPersistenceException
     */
    private String buildGroupByClause() {
        final String METHODNAME = "buildGroupByClause";
        log.entering(CLASSNAME, METHODNAME);
        
        String groupBy = " GROUP BY R.RESOURCE_ID ";
            
        log.exiting(CLASSNAME, METHODNAME);
        return groupBy;
    }

    /**
     * Builds the ORDER BY clause necessary to return sorted Resource ids. 
     * For example:
     * ORDER BY MIN(S1.STR_VALUE) asc NULLS LAST,MAX(S2.CODE_SYSTEM_ID) desc NULLS LAST, MAX(S2.TOKEN_VALUE) desc NULLS LAST 
     * 
     * @throws FHIRPersistenceException
     */
    private String buildOrderByClause() throws FHIRPersistenceException {
        final String METHODNAME = "buildOrderByClause";
        log.entering(CLASSNAME, METHODNAME);
        
        StringBuilder orderByBuffer = new StringBuilder();
        
        orderByBuffer.append(" ORDER BY "); 
        // Build MIN and/or MAX clauses
        for (int i = 0; i < this.sortParameters.size(); i++) {
            if (i > 0) {
                orderByBuffer.append(",");
            }
            orderByBuffer.append(this.buildAggregateExpression(this.sortParameters.get(i), i+1, true));
        }
            
        log.exiting(CLASSNAME, METHODNAME);
        return orderByBuffer.toString();
    }
    
    /**
     * This method builds a special ORDER BY clause for use only with system-level queries. 
     * Only 2 parameters are supported for sorting on system-level searches: _id and _lastUpdated.
     * @return An ORDER-BY clause for use exclusively with system-level searches.
     * @throws FHIRPersistenceException
     */
    private String buildSysLvlOrderByClause() throws FHIRPersistenceException {
        final String METHODNAME = "buildSysLvlOrderByClause";
        log.entering(CLASSNAME, METHODNAME);
        
        StringBuilder orderByBuffer = new StringBuilder();
        SortParameter sortParm;
        
        orderByBuffer.append(" ORDER BY "); 
        for (int i = 0; i < this.sortParameters.size(); i++) {
            if (i > 0) {
                orderByBuffer.append(",");
            }
            sortParm = this.sortParameters.get(i);
            if (sortParm.getName().equals("_id")) {
                orderByBuffer.append("LOGICAL_ID ");
            }
            else if (sortParm.getName().equals("_lastUpdated")) {
                orderByBuffer.append("LAST_UPDATED ");
            }
            else {
                throw new FHIRPersistenceNotSupportedException(sortParm.getName() + " is an unsupported sort parameter for " +
                            "system level search.");
            }
            orderByBuffer.append(sortParm.getDirection().value());
        }
            
        log.exiting(CLASSNAME, METHODNAME);
        return orderByBuffer.toString();
    }

}
