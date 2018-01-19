/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.util;

import static com.ibm.watsonhealth.fhir.persistence.jdbc.util.QuerySegmentAggregator.PARAMETER_TABLE_ALIAS;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

import com.ibm.watsonhealth.fhir.core.FHIRUtilities;
import com.ibm.watsonhealth.fhir.model.Code;
import com.ibm.watsonhealth.fhir.model.DateTime;
import com.ibm.watsonhealth.fhir.model.Instant;
import com.ibm.watsonhealth.fhir.model.Location;
import com.ibm.watsonhealth.fhir.model.Period;
import com.ibm.watsonhealth.fhir.model.Range;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.model.SearchParameter;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.ParameterNormalizedDAO;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.ResourceNormalizedDAO;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.util.JDBCNormalizedQueryBuilder.JDBCOperator;
import com.ibm.watsonhealth.fhir.persistence.util.AbstractQueryBuilder;
import com.ibm.watsonhealth.fhir.persistence.util.BoundingBox;
import com.ibm.watsonhealth.fhir.search.Parameter;
import com.ibm.watsonhealth.fhir.search.Parameter.Modifier;
import com.ibm.watsonhealth.fhir.search.ParameterValue;
import com.ibm.watsonhealth.fhir.search.ParameterValue.Prefix;
import com.ibm.watsonhealth.fhir.search.context.FHIRSearchContext;
import com.ibm.watsonhealth.fhir.search.util.SearchUtil;

/**
 * This is the JDBC implementation of a query builder for the 'normalized' schema of the JDBC persistence layer.
 * Queries are built in SQL.
 * @author markd
 *
 */
public class JDBCNormalizedQueryBuilder extends AbstractQueryBuilder<SqlQueryData, JDBCOperator> {
	
	private static final Logger log = java.util.logging.Logger.getLogger(JDBCNormalizedQueryBuilder.class.getName());
	private static final String CLASSNAME = JDBCNormalizedQueryBuilder.class.getName();
	
	// Constants used in SQL query string construction
	private static final String LEFT_PAREN = "(";
	private static final String RIGHT_PAREN = ")";
	private static final String AND = " AND ";
	private static final String BIND_VAR = "?";
	private static final String PERCENT_WILDCARD = "%";
	private static final String UNDERSCORE_WILDCARD = "_";
	private static final String ESCAPE_CHAR = "+";
	private static final String ESCAPE_UNDERSCORE = ESCAPE_CHAR + "_";
	private static final String ESCAPE_PERCENT = ESCAPE_CHAR + PERCENT_WILDCARD;
	private static final String ESCAPE_EXPR = " ESCAPE '" + ESCAPE_CHAR + "'";
    protected static final String STR_VALUE = "STR_VALUE";
    protected static final String STR_VALUE_LCASE = "STR_VALUE_LCASE";
    protected static final String TOKEN_VALUE = "TOKEN_VALUE";
    protected static final String CODE_SYSTEM_ID = "CODE_SYSTEM_ID";
    protected static final String CODE = "CODE";
    protected static final String NUMBER_VALUE = "NUMBER_VALUE";
    protected static final String QUANTITY_VALUE = "QUANTITY_VALUE";
    protected static final String QUANTITY_VALUE_LOW = "QUANTITY_VALUE_LOW";
    protected static final String QUANTITY_VALUE_HIGH = "QUANTITY_VALUE_HIGH";
    protected static final String DATE_VALUE = "DATE_VALUE";
    protected static final String DATE_START = "DATE_START";
    protected static final String DATE_END = "DATE_END";
    protected static final String LATITUDE_VALUE = "LATITUDE_VALUE";
    protected static final String LONGITUDE_VALUE = "LONGITUDE_VALUE";
    
    
    
	/**
	 * An enumeration of SQL query operators.
	 */
	public static enum JDBCOperator {
		EQ(" = "), 
		LIKE(" LIKE "), 
		IN(" IN "), 
		LT(" < "), 
		LTE(" <= "),
		GT(" > "), 
		GTE(" >= "),
		NE(" <> "), 
		OR(" OR "),
		AND(" AND ");
		
		private String value = null;
		
		JDBCOperator(String value) {
			this.value = value;
		}
		
		public String value() {
			return value;
		}
		
		public static JDBCOperator fromValue(String value) {
			for (JDBCOperator operator : JDBCOperator.values()) {
				if (operator.value.equalsIgnoreCase(value)) {
					return operator;
				}
			}
			throw new IllegalArgumentException("No constant with value " + value + " found.");
		}
	}
	
	/**
	 * Maps Parameter modifiers to SQL operators.
	 */
	private static HashMap<Modifier, JDBCOperator> modifierMap;
	/**
	 * Maps Parameter value prefix operators to SQL operators.
	 */
	private static HashMap<Prefix, JDBCOperator> prefixOperatorMap;
	
	static {
		modifierMap = new HashMap<>(); 
		modifierMap.put(Modifier.ABOVE, JDBCOperator.GT);
		modifierMap.put(Modifier.BELOW, JDBCOperator.LT);
		modifierMap.put(Modifier.CONTAINS, JDBCOperator.LIKE);
		modifierMap.put(Modifier.EXACT, JDBCOperator.EQ);
		modifierMap.put(Modifier.NOT, JDBCOperator.NE);
				
		prefixOperatorMap = new HashMap<>();
		prefixOperatorMap.put(Prefix.EQ, JDBCOperator.EQ);
		prefixOperatorMap.put(Prefix.GE, JDBCOperator.GTE);
		prefixOperatorMap.put(Prefix.GT, JDBCOperator.GT);
		prefixOperatorMap.put(Prefix.LE, JDBCOperator.LTE);
		prefixOperatorMap.put(Prefix.LT, JDBCOperator.LT);
		prefixOperatorMap.put(Prefix.NE, JDBCOperator.NE);
			
	}
	
	private Class<? extends Resource> resourceType = null;
	
	private ParameterNormalizedDAO parameterDao;
	private ResourceNormalizedDAO resourceDao;
	
	public static final boolean isRangeSearch(Class<? extends Resource> resourceType, Parameter queryParm) throws Exception {
		return (SearchUtil.getValueTypes(resourceType, queryParm.getName()).contains(Range.class));
	}
	
	public static final boolean isDateSearch(Class<? extends Resource> resourceType, Parameter queryParm) throws Exception {
		Set<Class<?>> valueTypes = SearchUtil.getValueTypes(resourceType, queryParm.getName());
		return valueTypes.contains(com.ibm.watsonhealth.fhir.model.Date.class) ||
			   valueTypes.contains(DateTime.class) ||
			   valueTypes.contains(Instant.class);
	}
	
	public static final boolean isDateRangeSearch(Class<? extends Resource> resourceType, Parameter queryParm) throws Exception  {
		return  SearchUtil.getValueTypes(resourceType, queryParm.getName()).contains(Period.class);
	}
	
	
	public JDBCNormalizedQueryBuilder(ParameterNormalizedDAO parameterDao, ResourceNormalizedDAO resourceDao) {
		super();
		this.parameterDao = parameterDao;
		this.resourceDao = resourceDao;
	}
	
	/**
	 * Builds a query that returns the count of the search results that would be found by applying the search
	 * parameters contained within the passed search context.
	 * @param resourceType - The type of resource being searched for.
	 * @param searchContext - The search context containing the search parameters.
	 * @return String - A count query SQL string
	 * @throws Exception 
	 */
	public SqlQueryData buildCountQuery(Class<? extends Resource> resourceType, FHIRSearchContext searchContext)
			throws Exception {
		final String METHODNAME = "buildCountQuery";
		log.entering(CLASSNAME, METHODNAME, new Object[] {resourceType.getSimpleName(), searchContext.getSearchParameters()});
		
		QuerySegmentAggregator helper;
		SqlQueryData query = null;
				
		helper = this.buildQueryCommon(resourceType, searchContext);
		if (helper != null) {
			query = helper.buildCountQuery();
        }
		
		log.exiting(CLASSNAME, METHODNAME);
		return query;
	}

	/* (non-Javadoc)
	 * @see com.ibm.watsonhealth.fhir.persistence.util.QueryBuilder#buildQuery(java.lang.Class, com.ibm.watsonhealth.fhir.search.context.FHIRSearchContext)
	 */
	@Override
	public SqlQueryData buildQuery(Class<? extends Resource> resourceType, FHIRSearchContext searchContext) throws Exception {
		final String METHODNAME = "buildQuery";
		log.entering(CLASSNAME, METHODNAME, new Object[] {resourceType.getSimpleName(), searchContext.getSearchParameters()});
		
		SqlQueryData query = null;
		QuerySegmentAggregator helper;
		
		helper = this.buildQueryCommon(resourceType, searchContext);
        if (helper != null) {
        	query = helper.buildQuery();
        }
				
		log.exiting(CLASSNAME, METHODNAME);
		return query;
	}
	
	/**
	 * Contains logic common to the building of 'regular' resource queries and 'count' resource queries.
	 * @param resourceType The type of FHIR resource being searched for.
	 * @param searchContext The search context containing search parameters.
	 * @return QuerySegmentAggregator - A query builder helper containing processed query segments.
	 * @throws Exception
	 */
	private QuerySegmentAggregator buildQueryCommon (Class<? extends Resource> resourceType, FHIRSearchContext searchContext) throws Exception {
		final String METHODNAME = "buildQueryCommon";
		log.entering(CLASSNAME, METHODNAME, new Object[] {resourceType.getSimpleName(), searchContext.getSearchParameters()});
		
		SqlQueryData querySegment;
		int nearParameterIndex;
		List<Parameter> searchParameters = searchContext.getSearchParameters();
		int pageSize = searchContext.getPageSize();
		int offset = (searchContext.getPageNumber() - 1) * pageSize;
		QuerySegmentAggregator helper;
		boolean isValidQuery = true;
		
		this.resourceType = resourceType;
		if (searchContext.getSortParameters() == null || searchContext.getSortParameters().isEmpty()) {
			helper = new QuerySegmentAggregator(resourceType, offset, pageSize, this.parameterDao, this.resourceDao);
		}
		else {
			helper = new SortedQuerySegmentAggregator(resourceType, offset, pageSize, this.parameterDao, 
			                this.resourceDao, searchContext.getSortParameters());
		}
		
		
		// Special logic for handling LocationPosition queries. These queries have interdependencies between
		// a couple of related input query parameters
		if (Location.class.equals(resourceType)) {
			querySegment = this.processLocationPosition(searchParameters);
			if (querySegment != null) {
				nearParameterIndex = this.findNearParameterIndex(searchParameters);
				helper.addQueryData(querySegment, searchParameters.get(nearParameterIndex));
			}
			// If there are Location-position parameters but a querySegment was not built, 
			// the query would be invalid. Note that valid parameters could be found in the following
			// for loop.
			else if (!searchParameters.isEmpty()) {
				isValidQuery = false;
			}
		}  
		
		// For each search parm, build a query parm that will satisfy the search. 
		for (Parameter queryParameter : searchParameters) {
			querySegment = this.buildQueryParm(resourceType, queryParameter);
			if (querySegment != null) {
				helper.addQueryData(querySegment, queryParameter);
				isValidQuery = true;
			}
		}
		if (!isValidQuery) {
			helper = null;
		}
		log.exiting(CLASSNAME, METHODNAME);
		return helper;
		
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.watsonhealth.fhir.persistence.util.AbstractQueryBuilder#getOperator(com.ibm.watsonhealth.fhir.search.Parameter)
	 */
	@Override
	protected JDBCOperator getOperator(Parameter queryParm) {
		final String METHODNAME = "getOperator(Parameter)";
		log.entering(CLASSNAME, METHODNAME, queryParm.getModifier());
		
		JDBCOperator operator = null;
		Modifier modifier = queryParm.getModifier();
		
		if (modifier != null) {
			operator = modifierMap.get(modifier);
		}
		if (operator == null) {
			operator = JDBCOperator.LIKE;
		}
		log.exiting(CLASSNAME, METHODNAME, operator.value());
		return operator;
	}
	
	/**
	 * Map the Modifier in the passed Parameter to a supported query operator. 
	 * If the mapping results in the default operator, override the default operator with the passed operator 
	 * if the passed operator is not null.
	 * @param queryParm - A valid query Parameter.
	 * @param defaultOverride - An operator that should override the default operator.
	 * @return T2 - A supported operator.
	 */
	@Override
	protected JDBCOperator getOperator(Parameter queryParm, JDBCOperator defaultOverride) {
		final String METHODNAME = "getOperator(Parameter, JDBCOperator)";
		log.entering(CLASSNAME, METHODNAME, queryParm.getModifier());
		
		JDBCOperator operator = null;
		Modifier modifier = queryParm.getModifier();
		
		if (modifier != null) {
			operator = modifierMap.get(modifier);
		}
		if (operator == null) {
			if (defaultOverride != null) {
				operator = defaultOverride;
			}
			else {
				operator = JDBCOperator.LIKE;
			}
		}
		log.exiting(CLASSNAME, METHODNAME, operator.value());
		return operator;
	}

	/* (non-Javadoc)
	 * @see com.ibm.watsonhealth.fhir.persistence.util.AbstractQueryBuilder#getPrefixOperator(com.ibm.watsonhealth.fhir.search.ParameterValue)
	 */
	@Override
	protected JDBCOperator getPrefixOperator(ParameterValue queryParmValue) {
		final String METHODNAME = "getOperator(ParameterValue)";
		log.entering(CLASSNAME, METHODNAME, queryParmValue.getPrefix());
		
		Prefix prefix = queryParmValue.getPrefix();
		JDBCOperator operator = null;
		
		if (prefix != null) {
			operator = prefixOperatorMap.get(prefix);
		}
		if (operator == null) {
			operator = JDBCOperator.EQ;
		}
		
		log.exiting(CLASSNAME, METHODNAME, operator.value());
		return operator;
	}

	/* (non-Javadoc)
	 * @see com.ibm.watsonhealth.fhir.persistence.util.AbstractQueryBuilder#processStringParm(com.ibm.watsonhealth.fhir.search.Parameter)
	 */
	@Override
	protected SqlQueryData processStringParm(Parameter queryParm) throws FHIRPersistenceException { 
		final String METHODNAME = "processStringParm";
		log.entering(CLASSNAME, METHODNAME, queryParm.toString());
		
		StringBuilder whereClauseSegment = new StringBuilder();
		JDBCOperator operator = this.getOperator(queryParm);
		boolean parmValueProcessed = false;
		String searchValue, tempSearchValue;
		boolean appendEscape;
		SqlQueryData queryData;
		List<Object> bindVariables = new ArrayList<>();
		
		// Build this piece of the segment:
		// (P1.PARAMETER_NAME_ID = x AND
		this.populateNameIdSubSegment(whereClauseSegment, queryParm.getName(), PARAMETER_TABLE_ALIAS);
		
		whereClauseSegment.append(AND).append(LEFT_PAREN);
		for (ParameterValue value : queryParm.getValues()) {
			appendEscape = false;
			if (operator.equals(JDBCOperator.LIKE)) {
				// Must escape special wildcard characters _ and % in the parameter value string.
				tempSearchValue = SQLParameterEncoder.encode(value.getValueString().replace(PERCENT_WILDCARD, ESCAPE_PERCENT)
									.replace(UNDERSCORE_WILDCARD, ESCAPE_UNDERSCORE));
				if (Modifier.CONTAINS.equals(queryParm.getModifier())) {
					searchValue = PERCENT_WILDCARD + tempSearchValue + PERCENT_WILDCARD;
				}
				else {
					// If there is not a CONTAINS modifier on the query parm, construct
					// a 'starts with' search value.
					searchValue = tempSearchValue + PERCENT_WILDCARD;
				}
				appendEscape = true;
			}
			else {
				searchValue = SQLParameterEncoder.encode(value.getValueString());
			}
			// If multiple values are present, we need to OR them together.
			if (parmValueProcessed) {
				whereClauseSegment.append(JDBCOperator.OR.value());
			}
			if (operator.equals(JDBCOperator.EQ)) {
				// For an exact match, we search against the STR_VALUE column in the Resource's string values table.
				// Build this piece: pX.str_value = search-attribute-value
				whereClauseSegment.append(PARAMETER_TABLE_ALIAS).append(STR_VALUE).append(operator.value()).append(BIND_VAR);
			}
			else {
				// For anything other than an  exact match, we search against the STR_VALUE_LCASE column in the Resource's string values table.
				// Also, the search value is "normalized"; it has accents removed and is lower-cased. This enables a case-insensitve, accent-insesnsitive search.
				// Build this piece: pX.str_value_lcase {operator} search-attribute-value
				whereClauseSegment.append(PARAMETER_TABLE_ALIAS).append(STR_VALUE_LCASE).append(operator.value()).append(BIND_VAR);
				searchValue = SearchUtil.normalizeForSearch(searchValue);
			}
			bindVariables.add(searchValue);
			// Build this piece: ESCAPE '+'
			if (appendEscape) {
				whereClauseSegment.append(ESCAPE_EXPR);
			}
			parmValueProcessed = true;
		}
		whereClauseSegment.append(RIGHT_PAREN).append(RIGHT_PAREN);
		
		queryData = new SqlQueryData(whereClauseSegment.toString(), bindVariables);
		log.exiting(CLASSNAME, METHODNAME);
		return queryData;
	}

	@Override
	protected SqlQueryData processReferenceParm(Parameter queryParm) throws Exception {
		final String METHODNAME = "processReferenceParm";
		log.entering(CLASSNAME, METHODNAME, queryParm.toString());
		
		StringBuilder whereClauseSegment = new StringBuilder();
		JDBCOperator operator = this.getOperator(queryParm);
		boolean parmValueProcessed = false;
		String searchValue;
		SqlQueryData queryData;
		List<Object> bindVariables = new ArrayList<>();
		
		// Build this piece of the segment:
		// (P1.PARAMETER_NAME_ID = x AND
		this.populateNameIdSubSegment(whereClauseSegment, queryParm.getName(), PARAMETER_TABLE_ALIAS);
		
		whereClauseSegment.append(AND).append(LEFT_PAREN);
		for (ParameterValue value : queryParm.getValues()) {
			// Handle query parm representing this name/value pair construct:
			// {name} = {resource-type/resource-id}
			searchValue = SQLParameterEncoder.encode(value.getValueString());
			
			// Handle query parm representing this name/value pair construct:
			// {name}:{Resource Type} = {resource-id}
			if (queryParm.getModifier() != null && queryParm.getModifier().equals(Modifier.TYPE)) {
				searchValue = queryParm.getModifierResourceTypeName() + "/" + SQLParameterEncoder.encode(value.getValueString());
			} else if (!isAbsoluteURL(searchValue)) {
			    SearchParameter definition = SearchUtil.getSearchParameter(this.resourceType, queryParm.getName());
			    List<Code> targets = definition.getTarget();
			    if (targets.size() == 1) {
			        Code target = targets.get(0);
			        String targetResourceTypeName = target.getValue();
			        if (!searchValue.startsWith(targetResourceTypeName + "/")) {
			            searchValue = targetResourceTypeName + "/" + searchValue;
			        }
			    }
			}
			
			// If multiple values are present, we need to OR them together.
			if (parmValueProcessed) {
				whereClauseSegment.append(JDBCOperator.OR.value());
			}
			// Build this piece: pX.str_value {operator} search-attribute-value
			whereClauseSegment.append(PARAMETER_TABLE_ALIAS).append(STR_VALUE).append(operator.value()).append(BIND_VAR);
			bindVariables.add(searchValue);
			parmValueProcessed = true;
		}
		whereClauseSegment.append(RIGHT_PAREN).append(RIGHT_PAREN);
		
		queryData = new SqlQueryData(whereClauseSegment.toString(), bindVariables);
		log.exiting(CLASSNAME, METHODNAME);
		return queryData;
	}

	/**
	 * Contains special logic for handling chained reference search parameters.
	 * @see https://www.hl7.org/fhir/search.html#reference (section 2.1.1.4.13)
	 * Nested sub-selects are built to realize the chaining logic required. Here is a sample chained query for an
	 * Observation given this search parameter: device:Device.patient.family=Monella
	 * 
	 * SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID, R.LAST_UPDATED, R.IS_DELETED, R.DATA, LR.LOGICAL_ID FROM 
	 * Observation_RESOURCES R, Observation_LOGICAL_RESOURCES LR , Observation_STR_VALUES P1 WHERE R.RESOURCE_ID = LR.CURRENT_RESOURCE_ID AND R.IS_DELETED <> 'Y' AND 
	 * P1.RESOURCE_ID = R.RESOURCE_ID AND 
	 * P1.PARAMETER_NAME_ID = 107 AND 
	 * (p1.STR_VALUE IN 
	 *    (SELECT 'Device' || '/' || CLR1.LOGICAL_ID FROM Device_RESOURCES CR1, Device_LOGICAL_RESOURCES CLR1, Device_STR_VALUES CP1 WHERE
	 *		CR1.RESOURCE_ID = CLR1.CURRENT_RESOURCE_ID AND CR1.IS_DELETED <> 'Y' AND CP1.RESOURCE_ID = CR1.RESOURCE_ID AND
	 *          CP1.PARAMETER_NAME_ID = 17 AND CP1.STR_VALUE IN
	 *             	(SELECT 'Patient' || '/' || CLR2.LOGICAL_ID FROM Patient_RESOURCES CR2, Patient_LOGICAL_RESOURCES CLR2, Patient_STR_VALUES CP2 WHERE
	 *             		CR2.RESOURCE_ID = CLR2.CURRENT_RESOURCE_ID AND CR2.IS_DELETED <> 'Y' AND CP2.RESOURCE_ID = CR2.RESOURCE_ID AND
	 *             		CP2.PARAMETER_NAME_ID = 5 AND CP2.STR_VALUE = 'Monella')));
	 *
	 * @param queryParm - A Parameter representing a chained query.
	 * @return SqlQueryData - The query segment for a chained parameter reference search.
	 */
	@Override
	protected SqlQueryData processChainedReferenceParm(Parameter queryParm) throws FHIRPersistenceException {
		final String METHODNAME = "processChainedReferenceParm";
		log.entering(CLASSNAME, METHODNAME, queryParm.toString());
		
		final String CR = "CR";
		final String CLR = "CLR";
		final String CP = "CP";
		Parameter currentParm;
		int refParmIndex = 0;
		String chainedResourceVar = null;
		String chainedLogicalResourceVar = null;
		String chainedParmVar = null;
		String resourceTypeName = null;
		StringBuilder whereClauseSegment = new StringBuilder();
		List<Object> bindVariables = new ArrayList<>();
		SqlQueryData queryData;
		Integer parameterNameId;
		
		currentParm = queryParm;
		while(currentParm != null) {
			if (currentParm.getNextParameter() != null) {
				if (refParmIndex == 0) {
					// Must build this first piece using px placeholder table alias, which will be replaced with a 
					// generated value in the buildQuery() method.
					// Build this piece:P1.PARAMETER_NAME_ID = x AND (p1.STR_VALUE IN 
					this.populateNameIdSubSegment(whereClauseSegment, currentParm.getName(), PARAMETER_TABLE_ALIAS);
					whereClauseSegment.append(JDBCOperator.AND.value());
					whereClauseSegment.append(LEFT_PAREN);
					whereClauseSegment.append(PARAMETER_TABLE_ALIAS).append(STR_VALUE).append(JDBCOperator.IN.value());
				}
				else {
					// Build this piece: CP1.PARAMETER_NAME_ID = x AND CP1.STR_VALUE IN
				    parameterNameId = ParameterNamesCache.getParameterNameId(currentParm.getName());
			        if (parameterNameId == null) {
			            parameterNameId = this.parameterDao.readParameterNameId(currentParm.getName());
			            this.parameterDao.getNewParameterNameIds().put(currentParm.getName(), parameterNameId);
			        }
					whereClauseSegment.append(chainedParmVar).append(".").append("PARAMETER_NAME_ID")
				  	  				  .append(JDBCOperator.EQ.value())
				  	  				  .append(parameterNameId)
				  	                  .append(JDBCOperator.AND.value())
					                  .append(chainedParmVar).append(".").append(STR_VALUE).append(JDBCOperator.IN.value());
				}
			 
				refParmIndex++;
				chainedResourceVar = CR + refParmIndex;
				chainedLogicalResourceVar = CLR + refParmIndex;
				chainedParmVar = CP + refParmIndex;
				
				// The * is a wildcard for any resource type. This occurs only in the case where a reference parameter chain
				// was built to represent a compartment search with chained inclusion criteria that includes a wildcard.
				// For this situation, a separate method is called, and further processing of the chain by this method is halted.
				if (currentParm.getModifierResourceTypeName().equals("*")) {
					this.processWildcardChainedRefParm(currentParm, chainedResourceVar, chainedLogicalResourceVar, chainedParmVar, whereClauseSegment, bindVariables);
					break;
				}
				// Build this piece: (SELECT 'resource-type-name' || '/' || CLRx.LOGICAL_ID 
				resourceTypeName = "'" + currentParm.getModifierResourceTypeName() + "'";
				whereClauseSegment.append(LEFT_PAREN).append("SELECT ")
								  .append(resourceTypeName)
								  .append(" || ").append("'/'").append(" || ")
								  .append(chainedLogicalResourceVar).append(".").append("LOGICAL_ID");
				
				// Build this piece: FROM Device_RESOURCES CR1, Device_LOGICAL_RESOURCES CLR1, Device_STR_VALUES CP1 WHERE
				whereClauseSegment.append(" FROM ")
								  .append(currentParm.getModifierResourceTypeName()).append("_RESOURCES ")
								  .append(chainedResourceVar).append(", ")
								  .append(currentParm.getModifierResourceTypeName()).append("_LOGICAL_RESOURCES ")
								  .append(chainedLogicalResourceVar).append(", ")
								  .append(currentParm.getModifierResourceTypeName()).append("_STR_VALUES ")
								  .append(chainedParmVar)
								  .append(" WHERE ");
				// Build this piece: CR1.RESOURCE_ID = CLR1.CURRENT_RESOURCE_ID AND CR1.IS_DELETED <> 'Y' AND CP1.RESOURCE_ID = CR1.RESOURCE_ID AND
				whereClauseSegment.append(chainedResourceVar).append(".RESOURCE_ID = ")
								  .append(chainedLogicalResourceVar).append(".").append("CURRENT_RESOURCE_ID").append(AND)
								  .append(chainedResourceVar).append(".IS_DELETED").append(" <> 'Y'").append(AND)
								  .append(chainedParmVar).append(".RESOURCE_ID = ").append(chainedResourceVar).append(".RESOURCE_ID").append(AND);
			}
			else {
				// This logic processes the LAST parameter in the chain.
				// Build this piece: CPx.PARAMETER_NAME_ID = x AND CPx.STR_VALUE = ?
			    parameterNameId = ParameterNamesCache.getParameterNameId(currentParm.getName());
                if (parameterNameId == null) {
                    parameterNameId = this.parameterDao.readParameterNameId(currentParm.getName());
                    this.parameterDao.getNewParameterNameIds().put(currentParm.getName(), parameterNameId);
                }
				whereClauseSegment.append(chainedParmVar).append(".PARAMETER_NAME_ID=")
								  .append(parameterNameId).append(AND)
								  .append(chainedParmVar).append(".").append(STR_VALUE).append(" = ?");
				bindVariables.add(currentParm.getValues().get(0).getValueString());
			}
			currentParm = currentParm.getNextParameter();
		}
		 
		// Finally, ensure the correct number of right parens are inserted to balance the where clause segment.
		int rightParensRequired = queryParm.getChain().size() + 2;
		for (int i = 0; i < rightParensRequired; i++) 
		{
			whereClauseSegment.append(RIGHT_PAREN);
		}
		
		queryData = new SqlQueryData(whereClauseSegment.toString(), bindVariables);
		log.exiting(CLASSNAME, METHODNAME, whereClauseSegment.toString());
		return queryData;
	}

	/**
	 * This method handles the processing of a wildcard chained reference parameter. The wildcard represents ALL FHIR resource types stored 
	 * in the FHIR database.
	 */
	private void processWildcardChainedRefParm(Parameter currentParm, String chainedResourceVar,
			String chainedLogicalResourceVar, String chainedParmVar, StringBuilder whereClauseSegment, List<Object> bindVariables) throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException {
		final String METHODNAME = "processChainedReferenceParm";
		log.entering(CLASSNAME, METHODNAME, currentParm.toString());
		
		String resourceTypeName;
		Collection<Integer> resourceTypeIds;
		Parameter lastParm;
		boolean selectGenerated = false;
		Integer parameterNameId;
		Map<String, Integer> resourceNameIdMap = null;
		Map<Integer, String> resourceIdNameMap = null;
						
		lastParm = currentParm.getNextParameter();
		
		// Acquire ALL Resource Type Ids
		if (ResourceTypesCache.isEnabled()) {
		    resourceTypeIds = ResourceTypesCache.getAllResourceTypeIds();
		}
		else {
		    resourceNameIdMap = resourceDao.readAllResourceTypeNames();
		    resourceTypeIds = resourceNameIdMap.values();
		    resourceIdNameMap = resourceNameIdMap.entrySet().stream()
		                       .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
		}
		
		// Build a sub-SELECT for each resource type, and put them together in a UNION.
		for (Integer resourceTypeId : resourceTypeIds) {
			if (selectGenerated) {
				whereClauseSegment.append(" UNION ");
			}
			// Build this piece: (SELECT 'resource-type-name' || '/' || CLRx.LOGICAL_ID 
			if (ResourceTypesCache.isEnabled()) {
			    resourceTypeName = ResourceTypesCache.getResourceTypeName(resourceTypeId);
			}
			else {
			    resourceTypeName =  resourceIdNameMap.get(resourceTypeId);
			}
			if (!selectGenerated) {
				whereClauseSegment.append(LEFT_PAREN);
			}
			whereClauseSegment.append("SELECT ")
							  .append("'").append(resourceTypeName).append("'")
							  .append(" || ").append("'/'").append(" || ")
							  .append(chainedLogicalResourceVar).append(".").append("LOGICAL_ID");
			
			// Build this piece: FROM Device_RESOURCES CR1, Device_LOGICAL_RESOURCES CLR1, Device_STR_VALUES CP1 WHERE
			whereClauseSegment.append(" FROM ")
							  .append(resourceTypeName).append("_RESOURCES ")
							  .append(chainedResourceVar).append(", ")
							  .append(resourceTypeName).append("_LOGICAL_RESOURCES ")
							  .append(chainedLogicalResourceVar).append(", ")
							  .append(resourceTypeName).append("_STR_VALUES ")
							  .append(chainedParmVar)
							  .append(" WHERE ");
			// Build this piece: CR1.RESOURCE_ID = CLR1.CURRENT_RESOURCE_ID AND CR1.IS_DELETED <> 'Y' AND CP1.RESOURCE_ID = CR1.RESOURCE_ID AND
			whereClauseSegment.append(chainedResourceVar).append(".RESOURCE_ID = ")
							  .append(chainedLogicalResourceVar).append(".").append("CURRENT_RESOURCE_ID").append(AND)
							  .append(chainedResourceVar).append(".IS_DELETED").append(" <> 'Y'").append(AND)
							  .append(chainedParmVar).append(".RESOURCE_ID = ").append(chainedResourceVar).append(".RESOURCE_ID").append(AND);
			
			// This logic processes the LAST parameter in the chain.
			// Build this piece: CPx.PARAMETER_NAME_ID = x AND CPx.STR_VALUE = ?
			parameterNameId = ParameterNamesCache.getParameterNameId(lastParm.getName());
            if (parameterNameId == null) {
                parameterNameId = this.parameterDao.readParameterNameId(lastParm.getName());
                this.parameterDao.getNewParameterNameIds().put(lastParm.getName(), parameterNameId);
            }
			whereClauseSegment.append(chainedParmVar).append(".PARAMETER_NAME_ID=")
							  .append(parameterNameId).append(AND)
							  .append(chainedParmVar).append(".").append(STR_VALUE).append(" = ?");
			bindVariables.add(lastParm.getValues().get(0).getValueString());
				
			selectGenerated = true;
		}
		
		log.exiting(CLASSNAME, METHODNAME, whereClauseSegment.toString());
		
	}

	/**
	 * This method is the entry point for processing inclusion criteria, which define resources that are part of a comparment-based search.
	 * @see compartments.json for the specificaiton of compartments, resources contained in each compartment, and the criteria that must be for 
	 * a resource to be included in a compartment. 
	 * Example inclusion criteria for AuditEvent in the Patient compartment:
	 * {
	 *		"name": "AuditEvent",
	 *		"inclusionCriteria": ["patient",          This is a simple attribute inclusion criterion
	 *		"participant.patient:Device",             This is a chained inclusion criterion
	 *		"participant.patient:RelatedPerson",      This is a chained inclusion criterion
	 *		"reference.patient:*"]                    This is a chained inclusion criterion with wildcard. The wildcard means "any resource type".
		} 
	 *
	 * Here is a sample generated query for this inclusion criteria:
	 * --PARAMETER_NAME_ID 13 = 'participant'
	 * --PARAMETER_NAME_ID 14 = 'patient'
	 * --PARAMETER_NAME_ID 16 = 'reference'
	 *	
	 *	SELECT COUNT(R.RESOURCE_ID) FROM 
	 *	AuditEvent_RESOURCES R, AuditEvent_LOGICAL_RESOURCES LR , AuditEvent_STR_VALUES P1 WHERE 
	 *	R.RESOURCE_ID = LR.CURRENT_RESOURCE_ID AND 
	 *	R.IS_DELETED <> 'Y' AND 
	 *	P1.RESOURCE_ID = R.RESOURCE_ID AND 
	 *	((P1.PARAMETER_NAME_ID=14 AND P1.STR_VALUE = ?) OR 
	 *	 ((P1.PARAMETER_NAME_ID=13 AND 
	 *	  (P1.STR_VALUE IN 
	 *		(SELECT 'Device' || '/' || CLR1.LOGICAL_ID FROM 
	 *			Device_RESOURCES CR1, Device_LOGICAL_RESOURCES CLR1, Device_STR_VALUES CP1 WHERE 
	 *			CR1.RESOURCE_ID = CLR1.CURRENT_RESOURCE_ID AND 
	 *			CR1.IS_DELETED <> 'Y' AND 
	 *			CP1.RESOURCE_ID = CR1.RESOURCE_ID AND 
	 *			CP1.PARAMETER_NAME_ID=14 AND CP1.STR_VALUE = ?)))) OR 
	 *	((P1.PARAMETER_NAME_ID=13 AND 
	 *	 (P1.STR_VALUE IN 
	 *		(SELECT 'RelatedPerson' || '/' || CLR1.LOGICAL_ID FROM 
	 *			RelatedPerson_RESOURCES CR1, RelatedPerson_LOGICAL_RESOURCES CLR1, RelatedPerson_STR_VALUES CP1 WHERE 
	 *			CR1.RESOURCE_ID = CLR1.CURRENT_RESOURCE_ID AND 
	 *			CR1.IS_DELETED <> 'Y' AND 
	 *			CP1.RESOURCE_ID = CR1.RESOURCE_ID AND 
	 *			CP1.PARAMETER_NAME_ID=14 AND CP1.STR_VALUE = ?)))) OR 
	 *	 ((P1.PARAMETER_NAME_ID=16 AND 
	 *	  (P1.STR_VALUE IN 
	 *		(SELECT 'AuditEvent' || '/' || CLR1.LOGICAL_ID FROM 
	 *			auditevent_RESOURCES CR1, auditevent_LOGICAL_RESOURCES CLR1, auditevent_STR_VALUES CP1 WHERE 
	 *			CR1.RESOURCE_ID = CLR1.CURRENT_RESOURCE_ID AND 
	 *			CR1.IS_DELETED <> 'Y' AND 
	 *			CP1.RESOURCE_ID = CR1.RESOURCE_ID AND 
	 *			CP1.PARAMETER_NAME_ID=14 AND CP1.STR_VALUE = ? 
	 *			UNION 
	 *			SELECT 'Device' || '/' || CLR1.LOGICAL_ID FROM 
	 *				device_RESOURCES CR1, device_LOGICAL_RESOURCES CLR1, device_STR_VALUES CP1 WHERE 
	 *				CR1.RESOURCE_ID = CLR1.CURRENT_RESOURCE_ID AND 
	 *				CR1.IS_DELETED <> 'Y' AND 
	 *				CP1.RESOURCE_ID = CR1.RESOURCE_ID AND 
	 *				CP1.PARAMETER_NAME_ID=14 AND CP1.STR_VALUE = ?)))));
	 */
	@Override
	protected SqlQueryData processInclusionCriteria(Parameter queryParm) throws FHIRPersistenceException {
		final String METHODNAME = "processInclusionCriteria";
		log.entering(CLASSNAME, METHODNAME, queryParm.toString());
		
		StringBuilder whereClauseSegment = new StringBuilder();
		JDBCOperator operator = JDBCOperator.EQ;
		Parameter currentParm;
		String currentParmValue; 
		List<Object> bindVariables = new ArrayList<>();
		SqlQueryData queryData;
		SqlQueryData chainedIncQueryData;
		
		currentParm = queryParm;
		whereClauseSegment.append(LEFT_PAREN);
		while(currentParm != null) {
			if (currentParm.getValues() == null || currentParm.getValues().isEmpty()) {
				throw new FHIRPersistenceException("No Paramter values found when processing inclusion criteria.");
			}
			// Handle the special case of chained inclusion criteria.
			if (currentParm.getName().contains(".")) {
				whereClauseSegment.append(LEFT_PAREN);
				chainedIncQueryData = this.processChainedInclusionCriteria(currentParm);
				whereClauseSegment.append(chainedIncQueryData.getQueryString());
				bindVariables.addAll(chainedIncQueryData.getBindVariables());
				whereClauseSegment.append(RIGHT_PAREN);
			}
			else {
				currentParmValue = currentParm.getValues().get(0).getValueString();
				// Build this piece:
				// (pX.PARAMETER_NAME_ID = x AND
				this.populateNameIdSubSegment(whereClauseSegment, currentParm.getName(), PARAMETER_TABLE_ALIAS);
				whereClauseSegment.append(JDBCOperator.AND.value());
				// Build this piece: pX.str_value = search-attribute-value
				whereClauseSegment.append(PARAMETER_TABLE_ALIAS).append(STR_VALUE).append(operator.value()).append(BIND_VAR);
				whereClauseSegment.append(RIGHT_PAREN);
				bindVariables.add(currentParmValue);
			}
			
			currentParm = currentParm.getNextParameter();
			// If more than one parameter is in the chain, OR them together.
			if (currentParm != null) {
				whereClauseSegment.append(JDBCOperator.OR.value());
			}
		}
		whereClauseSegment.append(RIGHT_PAREN);
		queryData = new SqlQueryData(whereClauseSegment.toString(), bindVariables);
		log.exiting(CLASSNAME, METHODNAME, whereClauseSegment.toString());
		return queryData;
	}

	@Override
	protected SqlQueryData processDateParm(Parameter queryParm) throws Exception {
		final String METHODNAME = "processDateParm";
		log.entering(CLASSNAME, METHODNAME, queryParm.toString());
		
		StringBuilder whereClauseSegment = new StringBuilder();
		JDBCOperator operator;
		boolean parmValueProcessed = false;
		XMLGregorianCalendar calendar;
		Date date, start, end;
		Duration duration;
		SqlQueryData queryData;
		List<Object> bindVariables = new ArrayList<>();
		boolean isDateSearch = isDateSearch(resourceType, queryParm);
		boolean isDateRangeSearch  = isDateRangeSearch(resourceType, queryParm);
		
		if (log.isLoggable(Level.FINE)) {
			log.fine("isDateSearch=" + isDateSearch + "  isDateRangeSearch=" + isDateRangeSearch);
		}
							
		// Build this piece of the segment:
		//(P1.PARAMETER_NAME_ID = x AND
		this.populateNameIdSubSegment(whereClauseSegment, queryParm.getName(), PARAMETER_TABLE_ALIAS);
		  	
		whereClauseSegment.append(AND).append(LEFT_PAREN);
		for (ParameterValue value : queryParm.getValues()) {
			operator = this.getPrefixOperator(value);
			// If multiple values are present, we need to OR them together.
			if (parmValueProcessed) {
				whereClauseSegment.append(JDBCOperator.OR.value()).append(LEFT_PAREN);
			}
			
			// NOTE: The valueDate is cloned so subsequent calendar adjustments do not affect the original valueDate object.
			calendar = (XMLGregorianCalendar)value.getValueDate().clone();
			date = calendar.toGregorianCalendar().getTime();
			operator = getPrefixOperator(value);
			// If the dateTime value is fully specified, go ahead and build a where clause segment for it.
			if (FHIRUtilities.isDateTime(calendar)) {
				if (isDateSearch) {
					whereClauseSegment.append(LEFT_PAREN);
					whereClauseSegment.append(PARAMETER_TABLE_ALIAS).append(DATE_VALUE).append(operator.value())
							      	  .append(BIND_VAR);
					bindVariables.add(FHIRUtilities.formatTimestamp(date));
					whereClauseSegment.append(RIGHT_PAREN);
				}
			}
			else if (FHIRUtilities.isPartialDate(calendar)) { 
				// For a partial dateTime and an EQ operator, a duration is calculated and a where segment is generated to cover a range.
				// For example, if the dateTime is specified down to the day, a range where segment is generated to cover that day.
				duration = FHIRUtilities.createDuration(calendar);
				FHIRUtilities.setDefaults(calendar);
				start = calendar.toGregorianCalendar().getTime();
				calendar.add(duration);
				end = calendar.toGregorianCalendar().getTime();
				
				if (isDateSearch) {
					whereClauseSegment.append(LEFT_PAREN);
					if (operator.equals(JDBCOperator.EQ)) { 
						whereClauseSegment.append(PARAMETER_TABLE_ALIAS).append(DATE_VALUE)
										  .append(JDBCOperator.GTE.value()).append(BIND_VAR)
										  .append(JDBCOperator.AND.value())
										  .append(PARAMETER_TABLE_ALIAS).append(DATE_VALUE)
										  .append(JDBCOperator.LT.value()).append(BIND_VAR);
						bindVariables.add(FHIRUtilities.formatTimestamp(start));
						bindVariables.add(FHIRUtilities.formatTimestamp(end));
					}
					else {
						whereClauseSegment.append(PARAMETER_TABLE_ALIAS).append(DATE_VALUE)
										  .append(operator.value())
										  .append(BIND_VAR);
						bindVariables.add(FHIRUtilities.formatTimestamp(start));
					}
					whereClauseSegment.append(RIGHT_PAREN);
				}
				date = start;
			}
			if (isDateRangeSearch) {
				if (isDateSearch) {
					whereClauseSegment.append(JDBCOperator.OR.value());
				}
				whereClauseSegment.append(LEFT_PAREN)
				 				  .append(PARAMETER_TABLE_ALIAS).append(DATE_START)
								  .append(JDBCOperator.LTE.value())
								  .append(BIND_VAR)
								  .append(JDBCOperator.AND.value())
								  .append(PARAMETER_TABLE_ALIAS).append(DATE_END)
								  .append(JDBCOperator.GTE.value())
								  .append(BIND_VAR)
								  .append(RIGHT_PAREN);
				bindVariables.add(FHIRUtilities.formatTimestamp(date));
				bindVariables.add(FHIRUtilities.formatTimestamp(date));
			}
			whereClauseSegment.append(RIGHT_PAREN);
			parmValueProcessed = true;
		}
		whereClauseSegment.append(RIGHT_PAREN);
		
		queryData = new SqlQueryData(whereClauseSegment.toString(), bindVariables);
		log.exiting(CLASSNAME, METHODNAME);
		return queryData;
	}

	@Override
	protected SqlQueryData processTokenParm(Parameter queryParm) throws FHIRPersistenceException {
		final String METHODNAME = "processTokenParm";
		log.entering(CLASSNAME, METHODNAME, queryParm.toString());
		
		StringBuilder whereClauseSegment = new StringBuilder();
		JDBCOperator operator = this.getOperator(queryParm, JDBCOperator.EQ);
		boolean parmValueProcessed = false;
		SqlQueryData queryData;
		List<Object> bindVariables = new ArrayList<>();
		Integer codeSystemId;
		
		// Build this piece of the segment:
		// (P1.PARAMETER_NAME_ID = x AND
		this.populateNameIdSubSegment(whereClauseSegment, queryParm.getName(), PARAMETER_TABLE_ALIAS);
		
		whereClauseSegment.append(AND).append(LEFT_PAREN);
		for (ParameterValue value : queryParm.getValues()) {
			// If multiple values are present, we need to OR them together.
			if (parmValueProcessed) {
				whereClauseSegment.append(JDBCOperator.OR.value());
			}
			
			whereClauseSegment.append(LEFT_PAREN);
			//Include code  
		 	whereClauseSegment.append(PARAMETER_TABLE_ALIAS).append(TOKEN_VALUE)
							  .append(operator.value())
							  .append(BIND_VAR);
		 	bindVariables.add(SQLParameterEncoder.encode(value.getValueCode()));
						
			//Include system if present.
			if (value.getValueSystem() != null && !value.getValueSystem().isEmpty()) {
				if (operator.equals(JDBCOperator.NE)) {
					whereClauseSegment.append(JDBCOperator.OR.value());
				}
				else {
					whereClauseSegment.append(JDBCOperator.AND.value());
				}
				whereClauseSegment.append(PARAMETER_TABLE_ALIAS).append(CODE_SYSTEM_ID)
			   		.append(operator.value()).append(BIND_VAR);
				codeSystemId = CodeSystemsCache.getCodeSystemId(value.getValueSystem());
                if (codeSystemId == null) {
                    codeSystemId = this.parameterDao.readCodeSystemId(value.getValueSystem());
                    this.parameterDao.getNewCodeSystemIds().put(value.getValueSystem(), codeSystemId);
                }
                bindVariables.add(codeSystemId);
			}
			whereClauseSegment.append(RIGHT_PAREN);
			parmValueProcessed = true;
		}
		
		whereClauseSegment.append(RIGHT_PAREN).append(RIGHT_PAREN);
		queryData = new SqlQueryData(whereClauseSegment.toString(), bindVariables);
		
		log.exiting(CLASSNAME, METHODNAME);
		return queryData;
	}

	@Override
	protected SqlQueryData processNumberParm(Parameter queryParm) throws FHIRPersistenceException {
		final String METHODNAME = "processNumberParm";
		log.entering(CLASSNAME, METHODNAME, queryParm.toString());
		
		StringBuilder whereClauseSegment = new StringBuilder();
		JDBCOperator operator;
		boolean parmValueProcessed = false;
		List<Object> bindVariables = new ArrayList<>();
		SqlQueryData queryData;
				
		// Build this piece of the segment:
		// (P1.PARAMETER_NAME_ID = x AND
		this.populateNameIdSubSegment(whereClauseSegment, queryParm.getName(), PARAMETER_TABLE_ALIAS);
		  	
		whereClauseSegment.append(AND).append(LEFT_PAREN);
		for (ParameterValue value : queryParm.getValues()) {
			operator = this.getPrefixOperator(value);
			// If multiple values are present, we need to OR them together.
			if (parmValueProcessed) {
				whereClauseSegment.append(JDBCOperator.OR.value());
			}
			// Build this piece: p1.value_string {operator} search-attribute-value
			whereClauseSegment.append(PARAMETER_TABLE_ALIAS).append(NUMBER_VALUE).append(operator.value())
							  .append(BIND_VAR);
			bindVariables.add(value.getValueNumber());
			parmValueProcessed = true;
		}
		whereClauseSegment.append(RIGHT_PAREN).append(RIGHT_PAREN);
		queryData = new SqlQueryData(whereClauseSegment.toString(), bindVariables);
								
		log.exiting(CLASSNAME, METHODNAME, whereClauseSegment.toString());
		return queryData;
	}

	@Override
	protected SqlQueryData processQuantityParm(Class<? extends Resource> resourceType, Parameter queryParm) throws Exception {
		final String METHODNAME = "processQuantityParm";
		log.entering(CLASSNAME, METHODNAME, queryParm.toString());
		
		StringBuilder whereClauseSegment = new StringBuilder();
		JDBCOperator operator;
		boolean parmValueProcessed = false;
		List<Object> bindVariables = new ArrayList<>();
		Integer systemId;
		SqlQueryData queryData;
		
		
		// Build this piece of the segment:
		// (P1.PARAMETER_NAME_ID = x AND
		this.populateNameIdSubSegment(whereClauseSegment, queryParm.getName(), PARAMETER_TABLE_ALIAS);
		  
		whereClauseSegment.append(AND).append(LEFT_PAREN);
		for (ParameterValue value : queryParm.getValues()) {
			operator = this.getPrefixOperator(value);
			// If multiple values are present, we need to OR them together.
			if (parmValueProcessed) {
				whereClauseSegment.append(JDBCOperator.OR.value());
			}
			whereClauseSegment.append(LEFT_PAREN);
			
			// If the target data type of the query is a Range, we need to build a piece of the where clause that looks like this:
			// pX.value_number_low <= {search-attribute-value} AND pX.value_number_high >= {search-attribute-value}
			if (isRangeSearch(resourceType, queryParm)) {
				whereClauseSegment.append(PARAMETER_TABLE_ALIAS).append(QUANTITY_VALUE_LOW)
								  .append(JDBCOperator.LTE.value())
								  .append(BIND_VAR)
								  .append(JDBCOperator.AND.value())
								  .append(PARAMETER_TABLE_ALIAS).append(QUANTITY_VALUE_HIGH)
								  .append(JDBCOperator.GTE.value())
								  .append(BIND_VAR);
				bindVariables.add(value.getValueNumber());
				bindVariables.add(value.getValueNumber());
			}
			else {
				// Build this piece: p1.value_string {operator} search-attribute-value
				whereClauseSegment.append(PARAMETER_TABLE_ALIAS).append(QUANTITY_VALUE).append(operator.value())
								  .append(BIND_VAR);
				bindVariables.add(value.getValueNumber());
			}
			
			//Include system if present.
			if (value.getValueSystem() != null && !value.getValueSystem().isEmpty()) {
			    systemId = CodeSystemsCache.getCodeSystemId(value.getValueSystem());
                if (systemId == null) {
                    systemId = this.parameterDao.readCodeSystemId(value.getValueSystem());
                    this.parameterDao.getNewCodeSystemIds().put(value.getValueSystem(), systemId);
                }
                whereClauseSegment.append(JDBCOperator.AND.value())
                                  .append(PARAMETER_TABLE_ALIAS).append(CODE_SYSTEM_ID)
                                  .append(JDBCOperator.EQ.value())
                                  .append(BIND_VAR);
                bindVariables.add(systemId);
			}
			
			//Include code if present.
			if (value.getValueCode() != null && !value.getValueCode().isEmpty()) {
				whereClauseSegment.append(JDBCOperator.AND.value())
								  .append(PARAMETER_TABLE_ALIAS).append(CODE)
								  .append(JDBCOperator.EQ.value())
								  .append(BIND_VAR);
				bindVariables.add(value.getValueCode());
			}
							
			whereClauseSegment.append(RIGHT_PAREN);
			parmValueProcessed = true;
		}
		whereClauseSegment.append(RIGHT_PAREN).append(RIGHT_PAREN);
		queryData = new SqlQueryData(whereClauseSegment.toString(), bindVariables);
		 								
		log.exiting(CLASSNAME, METHODNAME, whereClauseSegment.toString());
		return queryData;
	}

	@Override
	protected SqlQueryData buildLocationQuerySegment(String parmName, BoundingBox boundingBox) throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException {
		final String METHODNAME = "buildLocationQuerySegment";
		log.entering(CLASSNAME, METHODNAME, parmName);
		
		StringBuilder whereClauseSegment = new StringBuilder();
		List<Object> bindVariables = new ArrayList<>();
		SqlQueryData queryData;
				
		// Build this piece of the segment:
		// (P1.PARAMETER_NAME_ID = x AND
		this.populateNameIdSubSegment(whereClauseSegment, parmName, PARAMETER_TABLE_ALIAS);
		
		// Now build the piece that compares the BoundingBox longitude and latitude values
		// to the persisted longitude and latitude parameters.
		whereClauseSegment.append(JDBCOperator.AND.value())
		  				  .append(LEFT_PAREN)
		  				  .append(PARAMETER_TABLE_ALIAS).append(LONGITUDE_VALUE)
		  				  .append(JDBCOperator.LTE.value())
		  				  .append(BIND_VAR)
		  				  .append(JDBCOperator.AND.value())
		  				  .append(PARAMETER_TABLE_ALIAS).append(LONGITUDE_VALUE)
		  				  .append(JDBCOperator.GTE.value())
		  				  .append(BIND_VAR)
		  				  .append(JDBCOperator.AND.value())
		  				  .append(PARAMETER_TABLE_ALIAS).append(LATITUDE_VALUE)
		  				  .append(JDBCOperator.LTE.value())
		  				  .append(BIND_VAR)
		  				  .append(JDBCOperator.AND.value())
		  				  .append(PARAMETER_TABLE_ALIAS).append(LATITUDE_VALUE)
		  				  .append(JDBCOperator.GTE.value())
		  				  .append(BIND_VAR)
		  				  .append(RIGHT_PAREN).append(RIGHT_PAREN);
		bindVariables.add(boundingBox.getMaxLongitude());
		bindVariables.add(boundingBox.getMinLongitude());
		bindVariables.add(boundingBox.getMaxLatitude());
		bindVariables.add(boundingBox.getMinLatitude());
		
		queryData = new SqlQueryData(whereClauseSegment.toString(), bindVariables);
		log.exiting(CLASSNAME, METHODNAME, whereClauseSegment.toString());
		return queryData;
	}
	
	/**
	 * Populates the parameter name sub-segment of the passed where clause segment.
	 * @param whereClauseSegment
	 * @param queryParmName
	 * @throws FHIRPersistenceDBConnectException
	 * @throws FHIRPersistenceDataAccessException
	 */
	private void populateNameIdSubSegment(StringBuilder whereClauseSegment, String queryParmName, String parameterTableAlias) throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException {
		final String METHODNAME = "populateNameIdSubSegment";
		log.entering(CLASSNAME, METHODNAME, queryParmName);
		
		Integer parameterNameId;
		
		// Build this piece of the segment:
		//(P1.PARAMETER_NAME_ID = x 
		parameterNameId = ParameterNamesCache.getParameterNameId(queryParmName);
        if (parameterNameId == null) {
            parameterNameId = this.parameterDao.readParameterNameId(queryParmName);
            this.parameterDao.getNewParameterNameIds().put(queryParmName, parameterNameId);
        }
		whereClauseSegment.append(LEFT_PAREN);
		whereClauseSegment.append(parameterTableAlias).append("PARAMETER_NAME_ID=").append(parameterNameId);
		
		log.exiting(CLASSNAME, METHODNAME);
	}
	
	/**
	 * Finds the index of the 'near' parameter in the passed list of search parameters.
	 * If not found, -1 is returned.
	 * @param searchParameters
	 * @return int - The index of the 'near' parameter in the passed List.
	 */
	private int findNearParameterIndex(List<Parameter> searchParameters) {
		
		int nearParameterIndex = -1;
		
		for (int i = 0; i < searchParameters.size(); i++) {
			if (searchParameters.get(i).getName().equals(NEAR)) {
				nearParameterIndex = i;
				break;
			}
		}
		return nearParameterIndex;
	}

	/**
	 * This method handles the special case of chained inclusion criteria. 
	 * Using data extracted from the passed query parameter, a new Parameter chain is built to represent the chained inclusion criteria.
	 * That new Parameter is then passed to the inherited processChainedReferenceParamter() method to generate the required where clause segment.
	 * @see https://www.hl7.org/fhir/compartments.html
	 * @param queryParm - A Parameter representing chained inclusion criterion.
	 * @return SqlQueryData - the where clause segment and bind variables for a chained inclusion criterion.
	 * @throws FHIRPersistenceException 
	 */
	private SqlQueryData processChainedInclusionCriteria(Parameter queryParm) throws FHIRPersistenceException {
		final String METHODNAME = "processChainedInclusionCriteria";
		log.entering(CLASSNAME, METHODNAME, queryParm.toString());
		
		SqlQueryData queryData;
		Parameter rootParameter = null;
		
		// Transform the passed query parm into a chained parameter representation.
		rootParameter = SearchUtil.parseChainedInclusionCriteria(queryParm);
		// Call method to process the Parameter built by this method as a chained parameter.
		queryData = this.processChainedReferenceParm(rootParameter);
		
		log.exiting(CLASSNAME, METHODNAME);
		return queryData;
		
	}

}
