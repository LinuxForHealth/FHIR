/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

import com.ibm.watsonhealth.fhir.core.FHIRUtilities;
import com.ibm.watsonhealth.fhir.model.Code;
import com.ibm.watsonhealth.fhir.model.Location;
import com.ibm.watsonhealth.fhir.model.Range;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.model.SearchParameter;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.FHIRDbDAO;
import com.ibm.watsonhealth.fhir.persistence.jdbc.util.JDBCQueryBuilder.JDBCOperator;
import com.ibm.watsonhealth.fhir.persistence.util.AbstractQueryBuilder;
import com.ibm.watsonhealth.fhir.persistence.util.BoundingBox;
import com.ibm.watsonhealth.fhir.search.Parameter;
import com.ibm.watsonhealth.fhir.search.Parameter.Modifier;
import com.ibm.watsonhealth.fhir.search.ParameterValue;
import com.ibm.watsonhealth.fhir.search.ParameterValue.Prefix;
import com.ibm.watsonhealth.fhir.search.context.FHIRSearchContext;
import com.ibm.watsonhealth.fhir.search.util.SearchUtil;

/**
 * This is the JDBC implementation of a query builder for the JDBC persistence layer.
 * Queries are built in SQL.
 * @author markd
 *
 */
public class JDBCQueryBuilder extends AbstractQueryBuilder<String, JDBCOperator> {
	
	private static final Logger log = java.util.logging.Logger.getLogger(JDBCQueryBuilder.class.getName());
	private static final String CLASSNAME = JDBCQueryBuilder.class.getName();
	
	// Constants used in SQL query string construction
	protected static final String LEFT_PAREN = "(";
	protected static final String RIGHT_PAREN = ")";
	protected static final String QUOTE = "'";
	protected static final String COMMA = ",";
	protected static final String DOT = ".";
	protected static final String EQUALS = "=";
	protected static final String WHERE = " WHERE ";
	private static final String PERCENT_WILDCARD = "%";
	private static final String UNDERSCORE_WILDCARD = "_";
	private static final String ESCAPE_CHAR = "+";
	private static final String ESCAPE_UNDERSCORE = ESCAPE_CHAR + "_";
	private static final String ESCAPE_PERCENT = ESCAPE_CHAR + PERCENT_WILDCARD;
	private static final String ESCAPE_EXPR = " ESCAPE '" + ESCAPE_CHAR + "'";
	protected static final String PARAMETERS_TABLE_ALIAS = "pX.";
	protected static final String DEFAULT_ORDER_BY = " ORDER BY r.id ASC";
	protected static final String NAME = "name";
	protected static final String VALUE_STRING = "value_string";
	protected static final String VALUE_NUMBER = "value_number";
	protected static final String VALUE_NUMBER_LOW = "value_number_low";
	protected static final String VALUE_NUMBER_HIGH = "value_number_high";
	protected static final String VALUE_SYSTEM = "value_system";
	protected static final String VALUE_CODE = "value_code";
	protected static final String VALUE_DATE = "value_date";
	protected static final String VALUE_DATE_START = "value_date_start";
	protected static final String VALUE_DATE_END = "value_date_end";
	protected static final String VALUE_LONGITUDE = "value_longitude";
	protected static final String VALUE_LATITUDE = "value_latitude";
	protected static final String RESOURCE_TYPE = "resource_type";
	protected static final String SELECT_ROOT = "SELECT r.id, r.data, r.last_updated, r.logical_id, r.resource_type, r.version_id FROM Resource r";
	protected static final String SELECT_COUNT_ROOT = "SELECT COUNT(*) FROM Resource r";
	protected static final String JOIN_CLAUSE_ROOT = " JOIN Parameter p%d ON p%d.resource_id=r.id";
	
	private FHIRDbDAO fhirDbDao;
	
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

	public JDBCQueryBuilder(FHIRDbDAO dao) {
		super();
		this.fhirDbDao = dao;
	}
	
	/**
	 * Builds a query that returns the count of the search results that would be found by applying the search
	 * parameters contained within the passed search context.
	 * @param resourceType - The type of resource being searched for.
	 * @param searchContext - The search context containing the search parameters.
	 * @return String - A count query SQL string
	 * @throws Exception 
	 */
	public String buildCountQuery(Class<? extends Resource> resourceType, FHIRSearchContext searchContext)
			throws Exception {
		final String METHODNAME = "buildCountQuery";
		log.entering(CLASSNAME, METHODNAME, new Object[] {resourceType.getSimpleName(), searchContext.getSearchParameters()});
		
		String sqlQueryString = null;
		String sqlCountQueryString = null;
				
		sqlQueryString = this.buildQuery(resourceType, searchContext);
		if (sqlQueryString != null) {
			sqlQueryString = this.removeDefaultOrderBy(sqlQueryString);
			sqlCountQueryString = sqlQueryString.replace(SELECT_ROOT, SELECT_COUNT_ROOT);
		}
				
		log.exiting(CLASSNAME, METHODNAME, sqlCountQueryString);
		return sqlCountQueryString;
	}

	/* (non-Javadoc)
	 * @see com.ibm.watsonhealth.fhir.persistence.util.QueryBuilder#buildQuery(java.lang.Class, com.ibm.watsonhealth.fhir.search.context.FHIRSearchContext)
	 */
	@Override
	public String buildQuery(Class<? extends Resource> resourceType, FHIRSearchContext searchContext)
			throws Exception {
		final String METHODNAME = "buildQuery";
		log.entering(CLASSNAME, METHODNAME, new Object[] {resourceType.getSimpleName(), searchContext.getSearchParameters()});
		
        this.resourceType = resourceType;
        List<Parameter> searchParameters = searchContext.getSearchParameters();
       	StringBuilder sqlQuery = new StringBuilder();
		String sqlQueryString = null;
		String queryParm;
		List<String> queryParms;
		int numInputParms;
		boolean parmAdded = false;
		String whereClauseRoot;
		String whereClauseParm;
		String indexedTableAlias;
		
		numInputParms = searchParameters.size();
		queryParms = new ArrayList<>();
		 
		
		// Special logic for handling LocationPosition queries. These queries have interdependencies between
		// a couple of related input query parameters
		if (Location.class.equals(resourceType)) {
			queryParm = this.processLocationPosition(searchParameters);
			if (queryParm != null) {
				queryParms.add(queryParm);
				parmAdded = true;
			}
		}
		
		// For each search parm, build a query parm that will satisfy the search. 
		for (Parameter queryParameter : searchParameters) {
			queryParm = this.buildQueryParm(resourceType, queryParameter);
			if (queryParm != null) {
				queryParms.add(queryParm);
				parmAdded = true;
			}
		}
		
		// If at least one query segment was built, or if no query parms were passed in, create a
		// query segment to match on resourceType.
		if (parmAdded || numInputParms == 0) {
			sqlQuery.append(SELECT_ROOT);
			whereClauseRoot = this.buildWhereClauseRoot(resourceType, queryParms);
			if (queryParms.size() > 0) {
				
				// Build up JOIN clause
				for (int i = 0; i < queryParms.size(); i++) {
					sqlQuery.append(new Formatter().format(JOIN_CLAUSE_ROOT, i+1, i+1));
				}
								
				// Build up WHERE clause
				sqlQuery.append(whereClauseRoot);
				for (int i = 0; i < queryParms.size(); i++) {
					whereClauseParm = queryParms.get(i);
					indexedTableAlias = "p" + (i+1) + ".";
					whereClauseParm = whereClauseParm.replaceAll(PARAMETERS_TABLE_ALIAS, indexedTableAlias);
					// Only tack on an 'AND' to the end of the query buffer if it does not end in 'WHERE'
					if (! (sqlQuery.lastIndexOf(WHERE) == sqlQuery.length() - WHERE.length())) {
						sqlQuery.append(JDBCOperator.AND.value());
					}
					sqlQuery.append(LEFT_PAREN).append(whereClauseParm).append(RIGHT_PAREN);
				}
			}
			else {
				sqlQuery.append(whereClauseRoot);
			}
		}
		if (sqlQuery.length() > 0) {
			sqlQuery.append(" ORDER BY r.id ASC");
			this.addPaginationClauses(sqlQuery, searchContext);
			sqlQueryString = sqlQuery.toString();
		}
		log.exiting(CLASSNAME, METHODNAME,sqlQueryString);
		return sqlQueryString;
	}
	
	/**
	 * Builds a query segment for the passed query parameter.
	 * Acquires the segment from the superclass method, then augments that segment with:
	 * 'PX.resourceType = '{the passed resource type}'
	 * @param resourceType - A valid FHIR Resource type
	 * @param queryParm - A Parameter object describing the name, value and type of search parm.
	 * @return String - An object representing the selector query segment for the passed search parm.
	 * @throws Exception 
	 */
	@Override
	protected String buildQueryParm(Class<? extends Resource> resourceType, Parameter queryParm) 
			throws Exception {
		final String METHODNAME = "buildQueryParm";
		log.entering(CLASSNAME, METHODNAME, queryParm.toString());
		
		String databaseQueryParm = null;
		StringBuilder modifiedQueryParm = null;
		String returnQueryParm = null;
		
		
		try {
			databaseQueryParm = super.buildQueryParm(resourceType, queryParm);
			if (databaseQueryParm != null) {
				returnQueryParm = databaseQueryParm;
				if (!Resource.class.equals(resourceType)) {
					// Add this piece: pX.resourceType = 'resource-type'
					modifiedQueryParm = new StringBuilder();
					modifiedQueryParm.append(PARAMETERS_TABLE_ALIAS).append(RESOURCE_TYPE).append(JDBCOperator.EQ.value())
									 .append(QUOTE).append(resourceType.getSimpleName()).append(QUOTE).append(JDBCOperator.AND.value)
									 .append(LEFT_PAREN).append(databaseQueryParm).append(RIGHT_PAREN);
					returnQueryParm = modifiedQueryParm.toString();
				}
			}
		}
		finally {
			log.exiting(CLASSNAME, METHODNAME, new Object[] {returnQueryParm});
		}
		return returnQueryParm;
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
	protected String processStringParm(Parameter queryParm) {
		final String METHODNAME = "processStringParm";
		log.entering(CLASSNAME, METHODNAME, queryParm.toString());
		
		StringBuilder whereClauseSegment = new StringBuilder();
		JDBCOperator operator = this.getOperator(queryParm);
		boolean parmValueProcessed = false;
		String searchValue, tempSearchValue;
		boolean appendEscape;
		
		// Build this piece: p1.name = 'search-attribute-name' AND (
		this.populateNameQuerySegment(whereClauseSegment, queryParm.getName());
		whereClauseSegment.append(JDBCOperator.AND.value())
		  				  .append(LEFT_PAREN);
		  				  
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
			// Build this piece: p1.valueString {operator} 'search-attribute-value'
			whereClauseSegment.append(PARAMETERS_TABLE_ALIAS).append(VALUE_STRING).append(operator.value())
							  .append(QUOTE).append(searchValue).append(QUOTE);
			// Build this piece: ESCAPE '+'
			if (appendEscape) {
				whereClauseSegment.append(ESCAPE_EXPR);
			}
			parmValueProcessed = true;
		}
		whereClauseSegment.append(RIGHT_PAREN);
								
		log.exiting(CLASSNAME, METHODNAME, whereClauseSegment.toString());
		return whereClauseSegment.toString();
	}

	/* (non-Javadoc)
	 * @see com.ibm.watsonhealth.fhir.persistence.util.AbstractQueryBuilder#processReferenceParm(com.ibm.watsonhealth.fhir.search.Parameter)
	 */
	@Override
	protected String processReferenceParm(Parameter queryParm) throws Exception {
		final String METHODNAME = "processReferenceParm";
		log.entering(CLASSNAME, METHODNAME, queryParm.toString());
		
		StringBuilder whereClauseSegment = new StringBuilder();
		boolean parmValueProcessed = false;
		JDBCOperator operator = this.getOperator(queryParm, JDBCOperator.EQ);
		String searchValue;
		
		// Build this piece: p1.name = 'search-attribute-name' AND (
		this.populateNameQuerySegment(whereClauseSegment, queryParm.getName());
		whereClauseSegment.append(JDBCOperator.AND.value());
		 
		whereClauseSegment.append(LEFT_PAREN);
		for (ParameterValue value : queryParm.getValues()) {
			// Handle query parm representing this name/value pair construct:
			// {name} = {resource-type/resource-id}
			searchValue = SQLParameterEncoder.encode(value.getValueString());
			
			// Handle query parm representing this name/value pair construct:
			// {name}:{Resource Type} = {resource-id}
			if (queryParm.getModifier() != null && queryParm.getModifier().equals(Modifier.TYPE)) {
				searchValue = queryParm.getModifierResourceTypeName() + "/" + SQLParameterEncoder.encode(value.getValueString());
			} else if (!isAbsoluteURL(searchValue)) {
			    SearchParameter definition = SearchUtil.getSearchParameter(resourceType, queryParm.getName());
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
			// Build this piece: p1.valueString {operator} 'search-attribute-value'
			whereClauseSegment.append(PARAMETERS_TABLE_ALIAS).append(VALUE_STRING).append(operator.value())
							  .append(QUOTE).append(searchValue).append(QUOTE);
			parmValueProcessed = true;
		}
		whereClauseSegment.append(RIGHT_PAREN);
				
		log.exiting(CLASSNAME, METHODNAME, whereClauseSegment.toString());
		return whereClauseSegment.toString();
	}
	
	/**
	 * Contains special logic for handling chained reference search parameters.
	 * @see https://www.hl7.org/fhir/search.html#reference (section 2.1.1.4.13)
	 * Nested sub-selects are built to realize the chaining logic required. Here is a sample chained query for an
	 * Observation given this search parameter: device:Device.patient.family=Monella
	 * SELECT r FROM Resource r JOIN Parameter p1 ON p1.resource_id=r.id WHERE r.resource_ype = 'Observation' AND 
	 * r.version_id = (SELECT MAX(r2.version_id) FROM Resource r2 WHERE r2.logical_id = r.logical_id) AND 
	 * (p1.name = 'device' AND 
	 * (p1.value_string IN 
	 * 		(SELECT 'Device' || '/' || CR1.logical_id FROM Resource CR1 JOIN Parameter CP1 ON CP1.resource_id=CR1.id WHERE CR1.resource_type='Device' AND 
	 *	          CP1.name = 'patient' AND CP1.value_string IN 
	 *				(SELECT 'Patient || '/' || CR2.logical_id FROM Resource CR2 JOIN Parameter CP2 ON CP2.resource_id=CR2.id WHERE CR2.resource_type='Patient' AND 
	 *					CP2.name='family' AND CP2.value_string='Monella'))))
	 *
	 * @param queryParm - A Parameter representing a chained query.
	 * @return String - The query segment for a chained parameter reference search.
	 */
	@Override
	protected String processChainedReferenceParm(Parameter queryParm) {
		final String METHODNAME = "processChainedReferenceParm";
		log.entering(CLASSNAME, METHODNAME, queryParm.toString());
		
		final String CR = "CR";
		final String CP = "CP";
		Parameter currentParm;
		int refParmIndex = 0;
		String chainedResourceVar = null;
		String chainedParmVar = null;
		String resourceTypeName;
		StringBuilder whereClauseSegment = new StringBuilder();
		
		currentParm = queryParm;
		while(currentParm != null) {
			if (currentParm.getNextParameter() != null) {
				if (refParmIndex == 0) {
					// Must build this first piece using px placeholder table alias, which will be replaced with a 
					// generated value in the buildQuery() method.
					// Build this piece: px.name = 'search-attribute-name' AND (px.value_string IN
					this.populateNameQuerySegment(whereClauseSegment, currentParm.getName());
					whereClauseSegment.append(JDBCOperator.AND.value());
					whereClauseSegment.append(LEFT_PAREN);
					whereClauseSegment.append(PARAMETERS_TABLE_ALIAS).append(VALUE_STRING).append(JDBCOperator.IN.value());
				}
				else {
					// Build this piece: CPx.name = 'search-attribute-name' AND CPx.value_string IN 
					whereClauseSegment.append(chainedParmVar).append(DOT).append(NAME)
				  	  				  .append(JDBCOperator.EQ.value())
				  	  				  .append(QUOTE).append(currentParm.getName()).append(QUOTE);
					whereClauseSegment.append(JDBCOperator.AND.value());
					whereClauseSegment.append(chainedParmVar).append(DOT).append(VALUE_STRING).append(JDBCOperator.IN.value());
				}
			 
				refParmIndex++;
				chainedResourceVar = CR + refParmIndex;
				chainedParmVar = CP + refParmIndex;
				// Build this piece: (SELECT 'resource-type-name' || '/' || CRx.logical_id  
				// Note that * is a wildcard for any resource type.
				if (currentParm.getModifierResourceTypeName().equals("*")) {
					resourceTypeName = chainedResourceVar + DOT + "resource_type";
				}
				else {
					resourceTypeName = QUOTE + currentParm.getModifierResourceTypeName() + QUOTE;
				}
				whereClauseSegment.append(LEFT_PAREN).append("SELECT ")
								  .append(resourceTypeName)
								  .append(" || ").append("'/'").append(" || ")
								  .append(chainedResourceVar).append(DOT).append("logical_id");
				
				// Build this piece: FROM Resource CRx JOIN Parameter CPx ON CPx.resource_id=CRx.id WHERE
				whereClauseSegment.append(" FROM Resource ").append(chainedResourceVar).append(" JOIN Parameter ")
							      .append(chainedParmVar).append(" ON ").append(chainedParmVar).append(DOT).append("resource_id=")
							      .append(chainedResourceVar).append(DOT).append("id").append(WHERE);
								  
				// Build this piece: CRx.resource_type='resource-type-name' AND
				// Note that if the resource type is a wildcard, we omit this piece of the where clause so that we do not
				// restrict the search by resourceType.
				if (!currentParm.getModifierResourceTypeName().equals("*")) {
					whereClauseSegment.append(chainedResourceVar).append(DOT).append("resource_type").append(EQUALS)
							      	  .append(QUOTE).append(currentParm.getModifierResourceTypeName()).append(QUOTE)
							          .append(JDBCOperator.AND.value());
				}
			 
			}
			else {
				// This logic processes the LAST parameter in the chain.
				// Build this piece: CPx.name = 'paramenter-name' AND CPx.value_string = 'parameter-value'
				whereClauseSegment.append(chainedParmVar).append(DOT).append("name").append(EQUALS)
								  .append(QUOTE).append(currentParm.getName()).append(QUOTE)
								  .append(JDBCOperator.AND.value())
								  .append(chainedParmVar).append(DOT).append(VALUE_STRING).append(EQUALS)
								  .append(QUOTE).append(currentParm.getValues().get(0).getValueString()).append(QUOTE);
			}
			currentParm = currentParm.getNextParameter();
		}
		 
		// Finally, ensure the correct number of right parens are inserted to balance the where clause segment.
		int rightParensRequired = queryParm.getChain().size() + 1;
		for (int i = 0; i < rightParensRequired; i++) 
		{
			whereClauseSegment.append(RIGHT_PAREN);
		}
		log.exiting(CLASSNAME, METHODNAME, whereClauseSegment.toString());
		return whereClauseSegment.toString();
	}
	
	@Override
	protected String processInclusionCriteria(Parameter queryParm) throws FHIRPersistenceException {
		final String METHODNAME = "processInclusionCriteria";
		log.entering(CLASSNAME, METHODNAME, queryParm.toString());
		
		StringBuilder whereClauseSegment = new StringBuilder();
		JDBCOperator operator = JDBCOperator.EQ;
		Parameter currentParm;
		String currentParmValue; 
		
		currentParm = queryParm;
		while(currentParm != null) {
			if (currentParm.getValues() == null || currentParm.getValues().isEmpty()) {
				throw new FHIRPersistenceException("No Paramter values found when processing inclusion criteria.", Response.Status.INTERNAL_SERVER_ERROR);
			}
			// Handle the special case of chained inclusion criteria.
			if (currentParm.getName().contains(".")) {
				whereClauseSegment.append(LEFT_PAREN);
				whereClauseSegment.append(this.processChainedInclusionCriteria(currentParm));
				whereClauseSegment.append(RIGHT_PAREN);
			}
			else {
				currentParmValue = currentParm.getValues().get(0).getValueString();
				// Build this piece: (p1.name = 'search-attribute-name' AND 
				whereClauseSegment.append(LEFT_PAREN);
				this.populateNameQuerySegment(whereClauseSegment, currentParm.getName());
				whereClauseSegment.append(JDBCOperator.AND.value());
				// Build this piece: p1.value_string = 'search-attribute-value')
				whereClauseSegment.append(PARAMETERS_TABLE_ALIAS).append(VALUE_STRING).append(operator.value())
								  .append(QUOTE).append(currentParmValue).append(QUOTE).append(RIGHT_PAREN);
			}
			
			currentParm = currentParm.getNextParameter();
			// If more than one parameter is in the chain, OR them together.
			if (currentParm != null) {
				whereClauseSegment.append(JDBCOperator.OR.value());
			}
		}
		 
		log.exiting(CLASSNAME, METHODNAME, whereClauseSegment.toString());
		return whereClauseSegment.toString();
	}
	
	/**
	 * This method handles the special case of chained inclusion criteria. 
	 * Using data extracted from the passed query parameter, a new Parameter chain is built to represent the chained inclusion criteria.
	 * That new Parameter is then passed to the inherited processChainedReferenceParamter() method to generate the required where clause segment.
	 * @see https://www.hl7.org/fhir/compartments.html
	 * @param queryParm - A Parameter representing chained inclusion criterion.
	 * @return String - the where clause segment for a chained inclusion criterion.
	 */
	private String processChainedInclusionCriteria(Parameter queryParm) {
		final String METHODNAME = "processChainedInclusionCriteria";
		log.entering(CLASSNAME, METHODNAME, queryParm.toString());
		
		String whereClauseSegment;
		Parameter rootParameter = null;
		
		// Transform the passed query parm into a chained parameter representation.
		rootParameter = SearchUtil.parseChainedInclusionCriteria(queryParm);
		// Call method to process the Parameter built by this method as a chained parameter.
		whereClauseSegment = this.processChainedReferenceParm(rootParameter);
		
		log.exiting(CLASSNAME, METHODNAME);
		return whereClauseSegment;
		
	}

	/* (non-Javadoc)
	 * @see com.ibm.watsonhealth.fhir.persistence.util.AbstractQueryBuilder#processDateParm(com.ibm.watsonhealth.fhir.search.Parameter)
	 */
	@Override
	protected String processDateParm(Parameter queryParm) {
		final String METHODNAME = "processDateParm";
		log.entering(CLASSNAME, METHODNAME, queryParm.toString());
		
		StringBuilder whereClauseSegment = new StringBuilder();
		JDBCOperator operator;
		boolean parmValueProcessed = false;
		XMLGregorianCalendar calendar;
		Date date, start, end;
		Duration duration;
				
		// Build this piece: p1.name = 'search-attribute-name' AND (
		this.populateNameQuerySegment(whereClauseSegment, queryParm.getName());
		whereClauseSegment.append(JDBCOperator.AND.value())
		  				  .append(LEFT_PAREN);
		  				  
		for (ParameterValue value : queryParm.getValues()) {
			operator = this.getPrefixOperator(value);
			// If multiple values are present, we need to OR them together.
			if (parmValueProcessed) {
				whereClauseSegment.append(JDBCOperator.OR.value());
			}
			whereClauseSegment.append(LEFT_PAREN);
			// NOTE: The valueDate is cloned so subsequent calendar adjustments do not affect the original valueDate object.
			calendar = (XMLGregorianCalendar)value.getValueDate().clone();
			date = calendar.toGregorianCalendar().getTime();
			operator = getPrefixOperator(value);
			// If the dateTime value is fully specified, go ahead and build a where clause segment for it.
			if (FHIRUtilities.isDateTime(calendar)) {
				whereClauseSegment.append(PARAMETERS_TABLE_ALIAS).append(VALUE_DATE).append(operator.value())
							      .append(QUOTE).append(FHIRUtilities.formatTimestamp(date)).append(QUOTE);
			}
			else if (FHIRUtilities.isPartialDate(calendar)) { 
				duration = FHIRUtilities.createDuration(calendar);
				FHIRUtilities.setDefaults(calendar);
				start = calendar.toGregorianCalendar().getTime();
				calendar.add(duration);
				end = calendar.toGregorianCalendar().getTime();
				// For a partial dateTime and an EQ operator, a duration is calculated and a where segment is generated to cover a range.
				// For example, if the dateTime is specified down to the day, a range where segment is generated to cover that day.
				if (operator.equals(JDBCOperator.EQ)) { 
					whereClauseSegment.append(PARAMETERS_TABLE_ALIAS).append(VALUE_DATE)
									  .append(JDBCOperator.GTE.value()).append(QUOTE).append(FHIRUtilities.formatTimestamp(start)).append(QUOTE)
									  .append(JDBCOperator.AND.value())
									  .append(PARAMETERS_TABLE_ALIAS).append(VALUE_DATE)
									  .append(JDBCOperator.LT.value()).append(QUOTE).append(FHIRUtilities.formatTimestamp(end)).append(QUOTE);
				}
				else {
					whereClauseSegment.append(PARAMETERS_TABLE_ALIAS).append(VALUE_DATE)
									  .append(operator.value())
									  .append(QUOTE).append(FHIRUtilities.formatTimestamp(start)).append(QUOTE);
				}
				date = start;
			}
			// Add where clause segment to search for date range. 
			whereClauseSegment.append(JDBCOperator.OR.value())
							  .append(LEFT_PAREN).append(PARAMETERS_TABLE_ALIAS).append(VALUE_DATE_START)
							  .append(JDBCOperator.LTE.value())
							  .append(QUOTE).append(FHIRUtilities.formatTimestamp(date)).append(QUOTE)
							  .append(JDBCOperator.AND.value())
							  .append(PARAMETERS_TABLE_ALIAS).append(VALUE_DATE_END)
							  .append(JDBCOperator.GTE.value())
							  .append(QUOTE).append(FHIRUtilities.formatTimestamp(date)).append(QUOTE).append(RIGHT_PAREN);
			
			whereClauseSegment.append(RIGHT_PAREN);
			parmValueProcessed = true;
		}
		whereClauseSegment.append(RIGHT_PAREN);
		 								
		log.exiting(CLASSNAME, METHODNAME, whereClauseSegment.toString());
		return whereClauseSegment.toString();
	}

	/* (non-Javadoc)
	 * @see com.ibm.watsonhealth.fhir.persistence.util.AbstractQueryBuilder#processTokenParm(com.ibm.watsonhealth.fhir.search.Parameter)
	 */
	@Override
	protected String processTokenParm(Parameter queryParm) {
		final String METHODNAME = "processTokenParm";
		log.entering(CLASSNAME, METHODNAME, queryParm.toString());
		
		StringBuilder whereClauseSegment = new StringBuilder();
		JDBCOperator operator = this.getOperator(queryParm, JDBCOperator.EQ);
		boolean parmValueProcessed = false;
		
		// Build this piece: p1.name = 'search-attribute-name' AND (
		this.populateNameQuerySegment(whereClauseSegment, queryParm.getName());
		whereClauseSegment.append(JDBCOperator.AND.value())
		  				  .append(LEFT_PAREN);
		  				  
		for (ParameterValue value : queryParm.getValues()) {
			// If multiple values are present, we need to OR them together.
			if (parmValueProcessed) {
				whereClauseSegment.append(JDBCOperator.OR.value());
			}
			
			whereClauseSegment.append(LEFT_PAREN);
				
			//Include code  
		 	whereClauseSegment.append(PARAMETERS_TABLE_ALIAS).append(VALUE_CODE)
							  .append(operator.value())
							  .append(QUOTE).append(SQLParameterEncoder.encode(value.getValueCode())).append(QUOTE);
						
			//Include system if present.
			if (value.getValueSystem() != null && !value.getValueSystem().isEmpty()) {
				if (operator.equals(JDBCOperator.NE)) {
					whereClauseSegment.append(JDBCOperator.OR.value());
				}
				else {
					whereClauseSegment.append(JDBCOperator.AND.value());
				}
				
			   whereClauseSegment.append(PARAMETERS_TABLE_ALIAS).append(VALUE_SYSTEM)
			  .append(operator.value())
			  .append(QUOTE).append(SQLParameterEncoder.encode(value.getValueSystem())).append(QUOTE);
			}
			whereClauseSegment.append(RIGHT_PAREN);
			parmValueProcessed = true;
		}
		whereClauseSegment.append(RIGHT_PAREN);
		 								
		log.exiting(CLASSNAME, METHODNAME, whereClauseSegment.toString());
		return whereClauseSegment.toString();
	}

	/* (non-Javadoc)
	 * @see com.ibm.watsonhealth.fhir.persistence.util.AbstractQueryBuilder#processNumberParm(com.ibm.watsonhealth.fhir.search.Parameter)
	 */
	@Override
	protected String processNumberParm(Parameter queryParm) {
		final String METHODNAME = "processNumberParm";
		log.entering(CLASSNAME, METHODNAME, queryParm.toString());
		
		StringBuilder whereClauseSegment = new StringBuilder();
		JDBCOperator operator;
		boolean parmValueProcessed = false;
				
		// Build this piece: p1.name = 'search-attribute-name' AND (
		this.populateNameQuerySegment(whereClauseSegment, queryParm.getName());
		whereClauseSegment.append(JDBCOperator.AND.value())
		  				  .append(LEFT_PAREN);
		  				  
		for (ParameterValue value : queryParm.getValues()) {
			operator = this.getPrefixOperator(value);
			// If multiple values are present, we need to OR them together.
			if (parmValueProcessed) {
				whereClauseSegment.append(JDBCOperator.OR.value());
			}
			// Build this piece: p1.value_string {operator} search-attribute-value
			whereClauseSegment.append(PARAMETERS_TABLE_ALIAS).append(VALUE_NUMBER).append(operator.value())
							  .append(value.getValueNumber());
			parmValueProcessed = true;
		}
		whereClauseSegment.append(RIGHT_PAREN);
								
		log.exiting(CLASSNAME, METHODNAME, whereClauseSegment.toString());
		return whereClauseSegment.toString();
	}

	/* (non-Javadoc)
	 * @see com.ibm.watsonhealth.fhir.persistence.util.AbstractQueryBuilder#processQuantityParm(java.lang.Class, com.ibm.watsonhealth.fhir.search.Parameter)
	 */
	@Override
	protected String processQuantityParm(Class<? extends Resource> resourceType, Parameter queryParm) throws Exception {
		final String METHODNAME = "processQuantityParm";
		log.entering(CLASSNAME, METHODNAME, queryParm.toString());
		
		StringBuilder whereClauseSegment = new StringBuilder();
		JDBCOperator operator;
		boolean parmValueProcessed = false;
		
		
		// Build this piece: p1.name = 'search-attribute-name' AND (
		this.populateNameQuerySegment(whereClauseSegment, queryParm.getName());
		whereClauseSegment.append(JDBCOperator.AND.value())
		  				  .append(LEFT_PAREN);
		  				  
		for (ParameterValue value : queryParm.getValues()) {
			operator = this.getPrefixOperator(value);
			// If multiple values are present, we need to OR them together.
			if (parmValueProcessed) {
				whereClauseSegment.append(JDBCOperator.OR.value());
			}
			whereClauseSegment.append(LEFT_PAREN);
			
			// If the target data type of the query is a Range, we need to build a piece of the where clause that looks like this:
			// pX.value_number_low <= {search-attribute-value} AND pX.value_number_high >= {search-attribute-value}
			if (SearchUtil.getValueTypes(resourceType, queryParm.getName()).contains(Range.class)) {
				whereClauseSegment.append(PARAMETERS_TABLE_ALIAS).append(VALUE_NUMBER_LOW)
								  .append(JDBCOperator.LTE.value())
								  .append(value.getValueNumber())
								  .append(JDBCOperator.AND.value())
								  .append(PARAMETERS_TABLE_ALIAS).append(VALUE_NUMBER_HIGH)
								  .append(JDBCOperator.GTE.value())
								  .append(value.getValueNumber());
			}
			else {
				// Build this piece: p1.value_string {operator} search-attribute-value
				whereClauseSegment.append(PARAMETERS_TABLE_ALIAS).append(VALUE_NUMBER).append(operator.value())
								  .append(value.getValueNumber());
			}
			
			//Include system if present.
			if (value.getValueSystem() != null && !value.getValueSystem().isEmpty()) {
				whereClauseSegment.append(JDBCOperator.AND.value())
								  .append(PARAMETERS_TABLE_ALIAS).append(VALUE_SYSTEM)
								  .append(JDBCOperator.EQ.value())
								  .append(QUOTE).append(SQLParameterEncoder.encode(value.getValueSystem())).append(QUOTE);
			}
			
			//Include code if present.
			if (value.getValueCode() != null && !value.getValueCode().isEmpty()) {
				whereClauseSegment.append(JDBCOperator.AND.value())
								  .append(PARAMETERS_TABLE_ALIAS).append(VALUE_CODE)
								  .append(JDBCOperator.EQ.value())
								  .append(QUOTE).append(SQLParameterEncoder.encode(value.getValueCode())).append(QUOTE);
			}
							
			whereClauseSegment.append(RIGHT_PAREN);
			parmValueProcessed = true;
		}
		whereClauseSegment.append(RIGHT_PAREN);
		 								
		log.exiting(CLASSNAME, METHODNAME, whereClauseSegment.toString());
		return whereClauseSegment.toString();
	}

	/**
	 * Populates the passed StringBuilder with the parameter name where clause segment, for example:
	 * px.name = 'address'
	 * @param buffer StringBuilder - the buffer to be populated with the name where clause segment
	 * @param queryParmName - The name of a query parameter.
	 */
	private void populateNameQuerySegment(StringBuilder buffer, String queryParmName) {
		final String METHODNAME = "populateNameQuerySegment";
		log.entering(CLASSNAME, METHODNAME, queryParmName);
		
		// Build this piece: p1.name = 'search-attribute-name' AND (
		buffer.append(PARAMETERS_TABLE_ALIAS).append(NAME)
		  	  .append(JDBCOperator.EQ.value())
		  	  .append(QUOTE).append(queryParmName).append(QUOTE);
		  				  
		log.exiting(CLASSNAME, METHODNAME);
		
	}

	/**
	 * Builds a query segment for the passed parameter name using the geospatial data contained with the passed BoundingBox
	 * @param parmName - The name of the search parameter
	 * @param boundingBox - Container for the geospatial data needed to construct the query segment.
	 * @return JsonObject - The query segment necessary for searching locations that are inside the bounding box.
	 */
	@Override
	protected String buildLocationQuerySegment(String parmName, BoundingBox boundingBox) {
		final String METHODNAME = "buildLocationQuerySegment";
		log.entering(CLASSNAME, METHODNAME, parmName);
		
		StringBuilder whereClauseSegment = new StringBuilder();
				
		// Build this piece: p1.name = 'search-attribute-name' AND (
		this.populateNameQuerySegment(whereClauseSegment, parmName);
		
		// Now build the piece that compares the BoundingBox longitude and latitude values
		// to the persisted longitude and latitude parameters.
		whereClauseSegment.append(JDBCOperator.AND.value())
		  				  .append(LEFT_PAREN)
		  				  .append(PARAMETERS_TABLE_ALIAS).append(VALUE_LONGITUDE)
		  				  .append(JDBCOperator.LTE.value())
		  				  .append(boundingBox.getMaxLongitude())
		  				  .append(JDBCOperator.AND.value())
		  				  .append(PARAMETERS_TABLE_ALIAS).append(VALUE_LONGITUDE)
		  				  .append(JDBCOperator.GTE.value())
		  				  .append(boundingBox.getMinLongitude())
		  				  .append(JDBCOperator.AND.value())
		  				  .append(PARAMETERS_TABLE_ALIAS).append(VALUE_LATITUDE)
		  				  .append(JDBCOperator.LTE.value())
		  				  .append(boundingBox.getMaxLatitude())
		  				  .append(JDBCOperator.AND.value())
		  				  .append(PARAMETERS_TABLE_ALIAS).append(VALUE_LATITUDE)
		  				  .append(JDBCOperator.GTE.value())
		  				  .append(boundingBox.getMinLatitude())
		  				  .append(RIGHT_PAREN);
				
		log.exiting(CLASSNAME, METHODNAME, whereClauseSegment.toString());
		return whereClauseSegment.toString();
	}
	
	/**
	 * Removes the default ORDER BY clause from the passed query string.
	 * @param sqlQueryString A valid SQL query string
	 * @return The query string with default ordering removed.
	 */
	protected String removeDefaultOrderBy(String sqlQueryString) {
		final String METHODNAME = "removeDefaultOrderBy";
		log.entering(CLASSNAME, METHODNAME);
		
		String noOrderByQuery = sqlQueryString;
		if (sqlQueryString.contains(DEFAULT_ORDER_BY)) {
			noOrderByQuery = sqlQueryString.substring(0, sqlQueryString.indexOf(DEFAULT_ORDER_BY));
		}
		log.exiting(CLASSNAME, METHODNAME);
		return noOrderByQuery;
				
	}
	
	/**
	 * Builds and returns the beginning of the query WHERE clause, based on the passed resource type and 
	 * the presence of query parameters.
	 * @param resourceType - The type of Resource being queried.
	 * @param queryParms - The query parameters.
	 * @return
	 */
	private String buildWhereClauseRoot(Class<? extends Resource> resourceType, List<String> queryParms) {
		final String METHODNAME = "buildWhereClauseRoot";
		log.entering(CLASSNAME, METHODNAME);
		
		StringBuilder builder = new StringBuilder();
		if (!Resource.class.equals(resourceType)) {
			builder.append(WHERE);
			builder.append(" r.resource_type = '");
		    builder.append(resourceType.getSimpleName());
		    builder.append("' AND");
		    builder.append(" r.version_id = (SELECT MAX(r2.version_id) FROM Resource r2 WHERE r2.logical_id = r.logical_id)");
			
		}
		if (!queryParms.isEmpty() && builder.length() == 0) {
			builder.append(WHERE);
		}
		
		 
		log.exiting(CLASSNAME, METHODNAME);
		return builder.toString();
	}
	
	/**
	 * Add the necessary SQL clauses that determine the index of the first row to be returned and
	 * the total number of rows to be returned.
	 * @param sqlQuery - A buffer containing complete SQL query (without the pagination clauses).
	 * @param searchContext - The search context.
	 * @throws Exception 
	 */
	protected void addPaginationClauses(StringBuilder sqlQuery, FHIRSearchContext searchContext) throws Exception {
		final String METHODNAME = "addPaginationClauses";
		log.entering(CLASSNAME, METHODNAME);
		
		int pageSize = searchContext.getPageSize();
		int offset = (searchContext.getPageNumber() - 1) * pageSize;
				
		if(this.fhirDbDao.isDb2Database()) {
			sqlQuery.append(" LIMIT ").append(pageSize).append(" OFFSET ").append(offset);
			
		}
		else {
			sqlQuery.append(" OFFSET ").append(offset).append(" ROWS")
				       .append(" FETCH NEXT ").append(pageSize).append(" ROWS ONLY");
		}
		
		log.exiting(CLASSNAME, METHODNAME);
		
	}

}
