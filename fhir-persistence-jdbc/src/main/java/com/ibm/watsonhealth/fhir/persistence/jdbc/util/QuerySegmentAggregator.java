/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.ibm.watsonhealth.fhir.model.Location;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.FHIRDbDAO;
import com.ibm.watsonhealth.fhir.persistence.util.AbstractQueryBuilder;
import com.ibm.watsonhealth.fhir.search.Parameter;

/**
 * This class assists the JDBCNormalizedQueryBuilder. Its purpose is to aggregate SQL query segments together to produce a well-formed FHIR Resource query or 
 * FHIR Resource count query. 
 * 
 * @author markd
 *
 */
class QuerySegmentAggregator {
	
	private static final String CLASSNAME = QuerySegmentAggregator.class.getName();
	private static final Logger log = java.util.logging.Logger.getLogger(CLASSNAME);
	
	private static final String SELECT_ROOT = "SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID, R.LAST_UPDATED, R.IS_DELETED, R.DATA, LR.LOGICAL_ID ";
	private static final String SELECT_COUNT_ROOT = "SELECT COUNT(R.RESOURCE_ID) ";
	private static final String FROM_CLAUSE_ROOT = "FROM {0}_RESOURCES R, {0}_LOGICAL_RESOURCES LR ";
	private static final String WHERE_CLAUSE_ROOT = "WHERE R.RESOURCE_ID = LR.CURRENT_RESOURCE_ID AND R.IS_DELETED <> 'Y'";
	private static final String PARAMETER_TABLE_VAR = "P";
	protected static final String PARAMETER_TABLE_ALIAS = "pX.";
		
	private Class<? extends Resource> resourceType;
	private List<SqlQueryData> querySegments;
	private List<Parameter> searchQueryParameters;
	private int offset;
	private int pageSize;
	private FHIRDbDAO dao;
	

	/**
	 * Constructs a new QueryBuilderHelper
	 * @param resourceType - The type of FHIR Resource to be searched for.
	 * @param offset - The beginning index of the first search result.
	 * @param pageSize - The max number of requested search results.
	 */
	protected QuerySegmentAggregator(Class<? extends Resource> resourceType, int offset, int pageSize, FHIRDbDAO dao) {
		super();
		this.resourceType = resourceType;
		this.offset = offset;
		this.pageSize = pageSize;
		this.dao = dao;
		this.querySegments = new ArrayList<>();
		this.searchQueryParameters = new ArrayList<>();
		 
	}
	
	/**
	 * Adds a query segment, which is a where clause segment corresponding to the passed query Parameter and its encapsulated search values.
	 * @param querySegment A piece of a SQL WHERE clause 
	 * @param queryParm - The corresponding query parameter
	 */
	protected void addQueryData(SqlQueryData querySegment,Parameter queryParm) {
		final String METHODNAME = "addQueryData";
		log.entering(CLASSNAME, METHODNAME);
		
		this.querySegments.add(querySegment);
		this.searchQueryParameters.add(queryParm);
		
		log.exiting(CLASSNAME, METHODNAME);
 		
	}
	
	/**
	 * Builds a complete SQL count Query based upon the encapsulated query segments and bind variables.
	 * A simple example query produced by this method:
	 * 
	 * SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID, R.LAST_UPDATED, R.IS_DELETED, R.DATA, LR.LOGICAL_ID FROM
	 *	 PATIENT_RESOURCES R, PATIENT_LOGICAL_RESOURCES LR, PATIENT_STR_VALUES P1 WHERE  
	 *	 R.RESOURCE_ID = LR.CURRENT_RESOURCE_ID AND
	 *	 P1.RESOURCE_ID = R.RESOURCE_ID AND
	 *	 (P1.PARAMETER_NAME_ID = 4 AND
	 *	 P1.STR_VALUE LIKE ? ESCAPE '+')
	 *   ORDER BY r.RESOURCE_ID ASC OFFSET 0 ROWS FETCH NEXT 10 ROWS ONLY
	 * 
	 * @return SqlQueryData - contains the complete SQL query string and any associated bind variables.
	 * @throws Exception 
	 */
	protected SqlQueryData buildQuery() throws Exception {
		final String METHODNAME = "buildQuery";
		log.entering(CLASSNAME, METHODNAME);
		
		StringBuilder queryString = new StringBuilder();
		SqlQueryData queryData;
		List<Object> allBindVariables = new ArrayList<>();
		
		queryString.append(SELECT_ROOT);
				
		queryString.append(this.buildFromClause());
		
		queryString.append(this.buildWhereClause());
		
		// Add default ordering
		queryString.append(" ORDER BY R.RESOURCE_ID ASC ");
		
		// Add pagination clauses
		if(this.dao.isDb2Database()) {
			queryString.append(" LIMIT ").append(this.pageSize).append(" OFFSET ").append(this.offset);
			
		}
		else {
			queryString.append(" OFFSET ").append(this.offset).append(" ROWS")
				       .append(" FETCH NEXT ").append(this.pageSize).append(" ROWS ONLY");
		}
		
		for (SqlQueryData querySegment : this.querySegments) {
			allBindVariables.addAll(querySegment.getBindVariables());
		}
				
		queryData = new SqlQueryData(queryString.toString(), allBindVariables);
		log.exiting(CLASSNAME, METHODNAME, queryData);
		return queryData;
	}
	
	/**
	 *   Builds a complete SQL count query based upon the encapsulated query segments and bind variables.
	 *   A simple example query produced by this method:
	 *   
	 * 	 SELECT COUNT(R.RESOURCE_ID)FROM
	 *	 PATIENT_RESOURCES R, PATIENT_LOGICAL_RESOURCES LR, PATIENT_STR_VALUES P1 WHERE  
	 *	 R.RESOURCE_ID = LR.CURRENT_RESOURCE_ID AND
	 *	 (P1.RESOURCE_ID = R.RESOURCE_ID AND
	 *	 (P1.PARAMETER_NAME_ID = 4 AND
	 *	 P1.STR_VALUE LIKE ? ESCAPE '+'))
	 * 
	 * @return SqlQueryData - contains the complete SQL count query string and any associated bind variables.
	 * @throws Exception 
	 */
	protected SqlQueryData buildCountQuery() throws Exception {
		final String METHODNAME = "buildCountQuery";
		log.entering(CLASSNAME, METHODNAME);
		
		StringBuilder queryString = new StringBuilder();
		SqlQueryData queryData;
		List<Object> allBindVariables = new ArrayList<>();
		
		queryString.append(SELECT_COUNT_ROOT);
				
		queryString.append(this.buildFromClause());
		
		queryString.append(this.buildWhereClause());
		
		for (SqlQueryData querySegment : this.querySegments) {
			allBindVariables.addAll(querySegment.getBindVariables());
		}
		queryData = new SqlQueryData(queryString.toString(), allBindVariables);
		
		log.exiting(CLASSNAME, METHODNAME, queryData);
		return queryData;
		
	}
	
	/**
	 * Builds the FROM clause for the SQL query being generated. The appropriate Resource and Parameter table names are included 
	 * along with an alias for each table.
	 * @return A String containing the FROM clause
	 * @throws Exception 
	 */
	private String buildFromClause() throws Exception {
		final String METHODNAME = "buildFromClause";
		log.entering(CLASSNAME, METHODNAME);
		
		boolean isLocationQuery;
		int parameterTableAliasIndex = 1;
		StringBuilder fromClause = new StringBuilder();
		String resourceTypeName = this.resourceType.getSimpleName();
		fromClause.append(MessageFormat.format(FROM_CLAUSE_ROOT, this.resourceType.getSimpleName()));
		
		for (Parameter searchQueryParm : this.searchQueryParameters) {
			isLocationQuery = Location.class.equals(this.resourceType) && searchQueryParm.getName().equals(AbstractQueryBuilder.NEAR);
			switch(searchQueryParm.getType()) {
				case URI :
				case REFERENCE : 
				case STRING :   fromClause.append(", ").append(resourceTypeName).append("_STR_VALUES ").append(PARAMETER_TABLE_VAR).append(parameterTableAliasIndex);
					 break;
				case NUMBER :   fromClause.append(", ").append(resourceTypeName).append("_NUMBER_VALUES ").append(PARAMETER_TABLE_VAR).append(parameterTableAliasIndex); 
					 break;
				case QUANTITY : fromClause.append(", ").append(resourceTypeName).append("_QUANTITY_VALUES ").append(PARAMETER_TABLE_VAR).append(parameterTableAliasIndex);
					 break;
				case DATE :     fromClause.append(", ").append(resourceTypeName).append("_DATE_VALUES ").append(PARAMETER_TABLE_VAR).append(parameterTableAliasIndex);
				 	 break;
				case TOKEN :    if (isLocationQuery) {
									fromClause.append(", ").append(resourceTypeName).append("_LATLNG_VALUES ").append(PARAMETER_TABLE_VAR).append(parameterTableAliasIndex);
								}
								else {
									fromClause.append(", ").append(resourceTypeName).append("_TOKEN_VALUES ").append(PARAMETER_TABLE_VAR).append(parameterTableAliasIndex);
								}
					 break;
			}
			parameterTableAliasIndex++;
		}
		fromClause.append(" ");
			
		log.exiting(CLASSNAME, METHODNAME);
		return fromClause.toString();
		
	}
	
	/**
	 * Builds the WHERE clause for the query being generated. This method aggregates the contained query segments, and ties those segments back
	 * to the appropriate parameter table alias.
	 * @return
	 */
	private String buildWhereClause() {
		final String METHODNAME = "buildWhereClause";
		log.entering(CLASSNAME, METHODNAME);
		
		int parameterTableAliasIndex = 1;
		StringBuilder whereClause = new StringBuilder();
		String resolvedTableAlias;
		String whereClauseSegment;
		boolean querySegmentProcessed = false;
				 		
		whereClause.append(WHERE_CLAUSE_ROOT);
		if (!this.querySegments.isEmpty()) {
			whereClause.append(" AND ");
		 	for(SqlQueryData querySegment : this.querySegments) {
		 		if (querySegmentProcessed) {
					whereClause.append(" AND ");
				}
				whereClauseSegment = querySegment.getQueryString();
				resolvedTableAlias = PARAMETER_TABLE_VAR + parameterTableAliasIndex + ".";
				whereClause.append(PARAMETER_TABLE_VAR).append(parameterTableAliasIndex).append(".").append("RESOURCE_ID = R.RESOURCE_ID AND ");
				whereClauseSegment = whereClauseSegment.replaceAll(PARAMETER_TABLE_ALIAS, resolvedTableAlias);
				whereClause.append(whereClauseSegment);
				querySegmentProcessed = true;
				parameterTableAliasIndex++;
			}
		}
		
		log.exiting(CLASSNAME, METHODNAME);
		return whereClause.toString();
	}

}
