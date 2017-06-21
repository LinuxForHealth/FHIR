/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
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
	private static final String SYSTEM_LEVEL_SELECT_ROOT = "SELECT RESOURCE_ID, LOGICAL_RESOURCE_ID, VERSION_ID, LAST_UPDATED, IS_DELETED, DATA, LOGICAL_ID ";
	private static final String SYSTEM_LEVEL_SUBSELECT_ROOT = SELECT_ROOT;
	private static final String SELECT_COUNT_ROOT = "SELECT COUNT(R.RESOURCE_ID) ";
	private static final String SYSTEM_LEVEL_SELECT_COUNT_ROOT = "SELECT COUNT(RESOURCE_ID) ";
	private static final String SYSTEM_LEVEL_SUBSELECT_COUNT_ROOT = " SELECT R.RESOURCE_ID ";
	private static final String FROM_CLAUSE_ROOT = "FROM {0}_RESOURCES R JOIN {0}_LOGICAL_RESOURCES LR ON R.LOGICAL_RESOURCE_ID=LR.LOGICAL_RESOURCE_ID ";
	private static final String WHERE_CLAUSE_ROOT = "WHERE R.RESOURCE_ID = LR.CURRENT_RESOURCE_ID AND R.IS_DELETED <> 'Y'";
	private static final String PARAMETER_TABLE_VAR = "P";
	protected static final String PARAMETER_TABLE_ALIAS = "pX.";
	private static final String FROM = " FROM ";
	private static final String UNION = " UNION ALL ";
	private static final String ON = " ON ";
	private static final String JOIN = " JOIN ";
	private static final String COMBINED_RESULTS = " COMBINED_RESULTS";
		
	protected Class<? extends Resource> resourceType;
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
	 * Builds a complete SQL Query based upon the encapsulated query segments and bind variables.
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
		
		if (this.isSystemLevelSearch()) {
			queryData = this.buildSystemLevelQuery(SYSTEM_LEVEL_SELECT_ROOT, SYSTEM_LEVEL_SUBSELECT_ROOT, true);
		}
		else {
			queryString.append(SELECT_ROOT);
					
			queryString.append(this.buildFromClause());
			
			queryString.append(this.buildWhereClause());
			
			for (SqlQueryData querySegment : this.querySegments) {
				allBindVariables.addAll(querySegment.getBindVariables());
			}
			// Add default ordering
			queryString.append(" ORDER BY R.RESOURCE_ID ASC ");
			this.addPaginationClauses(queryString);		
			queryData = new SqlQueryData(queryString.toString(), allBindVariables);
		}
		
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
		
		if (this.isSystemLevelSearch()) {
			queryData = this.buildSystemLevelQuery(SYSTEM_LEVEL_SELECT_COUNT_ROOT, SYSTEM_LEVEL_SUBSELECT_COUNT_ROOT, false);
		}
		else {
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
	 * Build a system level query or count query, based upon the encapsulated query segments and bind variables and
	 * the passed select-root strings.
	 * A FHIR system level query spans multiple resource types, and therefore spans multiple tables in the database. 
	 * Here is an example of a system level query, assuming that only 3 different resource types have been persisted
	 * in the database:
	 * SELECT RESOURCE_ID, LOGICAL_RESOURCE_ID, VERSION_ID, LAST_UPDATED, IS_DELETED, DATA, LOGICAL_ID FROM 
	 *  (SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID, R.LAST_UPDATED, R.IS_DELETED, R.DATA, LR.LOGICAL_ID FROM 
	 *    RiskAssessment_RESOURCES R, RiskAssessment_LOGICAL_RESOURCES LR , RiskAssessment_DATE_VALUES P1 WHERE 
	 *    R.RESOURCE_ID = LR.CURRENT_RESOURCE_ID AND R.IS_DELETED <> 'Y' AND P1.RESOURCE_ID = R.RESOURCE_ID AND 
	 *    (P1.PARAMETER_NAME_ID=3 AND ((P1.DATE_VALUE = '2017-06-15 21:30:58.251')))
	 *  UNION ALL 
     *  SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID, R.LAST_UPDATED, R.IS_DELETED, R.DATA, LR.LOGICAL_ID FROM 
	 *   Group_RESOURCES R, Group_LOGICAL_RESOURCES LR , Group_DATE_VALUES P1 WHERE 
     *   R.RESOURCE_ID = LR.CURRENT_RESOURCE_ID AND R.IS_DELETED <> 'Y' AND P1.RESOURCE_ID = R.RESOURCE_ID AND 
     *  (P1.PARAMETER_NAME_ID=3 AND ((P1.DATE_VALUE = '2017-06-15 21:30:58.251'))) 
	 *  UNION ALL  
     *  SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID, R.LAST_UPDATED, R.IS_DELETED, R.DATA, LR.LOGICAL_ID FROM 
     *   Questionnaire_RESOURCES R, Questionnaire_LOGICAL_RESOURCES LR , Questionnaire_DATE_VALUES P1 WHERE
     *   R.RESOURCE_ID = LR.CURRENT_RESOURCE_ID AND R.IS_DELETED <> 'Y' AND P1.RESOURCE_ID = R.RESOURCE_ID AND 
     *   (P1.PARAMETER_NAME_ID=3 AND ((P1.DATE_VALUE = '2017-06-15 21:30:58.251')))) COMBINED_RESULTS; 
	 * 
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
		Collection<Integer> resourceTypeIds;
		String tempFromClause;
		String resourceTypeName;
		boolean resourceTypeProcessed = false;
		
		queryString.append(selectRoot).append(FROM).append("(");
		resourceTypeIds = ResourceTypesCache.getAllResourceTypeIds();
		for(Integer resourceTypeId : resourceTypeIds) {
			resourceTypeName = ResourceTypesCache.getResourceTypeName(resourceTypeId) + "_";
			tempFromClause = this.buildFromClause();
			tempFromClause = tempFromClause.replaceAll("Resource_", resourceTypeName);
			if (resourceTypeProcessed) {
				queryString.append(UNION);
			}
			queryString.append(subSelectRoot).append(tempFromClause);
			resourceTypeProcessed = true;
			queryString.append(this.buildWhereClause());
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
	 * Builds the FROM clause for the SQL query being generated. The appropriate Resource and Parameter table names are included 
	 * along with an alias for each table.
	 * @return A String containing the FROM clause
	 * @throws Exception 
	 */
	protected String buildFromClause() throws Exception {
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
				case STRING :   fromClause.append(JOIN).append(resourceTypeName).append("_STR_VALUES ").append(PARAMETER_TABLE_VAR).append(parameterTableAliasIndex);
					 break;
				case NUMBER :   fromClause.append(JOIN).append(resourceTypeName).append("_NUMBER_VALUES ").append(PARAMETER_TABLE_VAR).append(parameterTableAliasIndex); 
					 break;
				case QUANTITY : fromClause.append(JOIN).append(resourceTypeName).append("_QUANTITY_VALUES ").append(PARAMETER_TABLE_VAR).append(parameterTableAliasIndex);
					 break;
				case DATE :     fromClause.append(JOIN).append(resourceTypeName).append("_DATE_VALUES ").append(PARAMETER_TABLE_VAR).append(parameterTableAliasIndex);
				 	 break;
				case TOKEN :    if (isLocationQuery) {
									fromClause.append(JOIN).append(resourceTypeName).append("_LATLNG_VALUES ").append(PARAMETER_TABLE_VAR).append(parameterTableAliasIndex);
								}
								else {
									fromClause.append(JOIN).append(resourceTypeName).append("_TOKEN_VALUES ").append(PARAMETER_TABLE_VAR).append(parameterTableAliasIndex);
								}
					 break;
			}
			fromClause.append(ON).append(PARAMETER_TABLE_VAR).append(parameterTableAliasIndex).append(".RESOURCE_ID=R.RESOURCE_ID");
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
	
	/**
	 * 
	 * @return true if this instance represents a FHIR system level search
	 */
	private boolean isSystemLevelSearch() {
		return Resource.class.equals(this.resourceType);
	}
	
	/**
	 * Adds the appropriate pagination clauses to the passed query string buffer, based on the type
	 * of database we're running against.
	 * @param queryString A query string buffer.
	 * @throws Exception
	 */
	private void addPaginationClauses(StringBuilder queryString) throws Exception {
		
		if(this.dao.isDb2Database()) {
			queryString.append(" LIMIT ").append(this.pageSize).append(" OFFSET ").append(this.offset);
		}
		else {
			queryString.append(" OFFSET ").append(this.offset).append(" ROWS")
				       .append(" FETCH NEXT ").append(this.pageSize).append(" ROWS ONLY");
		}
	}

}
