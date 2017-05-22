/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.util;

import static com.ibm.watsonhealth.fhir.persistence.jdbc.util.QuerySegmentAggregator.PARAMETER_TABLE_ALIAS;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

import com.ibm.watsonhealth.fhir.core.FHIRUtilities;
import com.ibm.watsonhealth.fhir.model.Code;
import com.ibm.watsonhealth.fhir.model.DateTime;
import com.ibm.watsonhealth.fhir.model.Period;
import com.ibm.watsonhealth.fhir.model.Range;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.model.SearchParameter;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceNotSupportedException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.ParameterNormalizedDAO;
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
    private static final String STR_VALUE = "STR_VALUE";
    private static final String STR_VALUE_LCASE = "STR_VALUE_LCASE";
    private static final String TOKEN_VALUE = "TOKEN_VALUE";
    private static final String CODE_SYSTEM_ID = "CODE_SYSTEM_ID";
    private static final String CODE = "CODE";
    private static final String NUMBER_VALUE = "NUMBER_VALUE";
    private static final String QUANTITY_VALUE = "QUANTITY_VALUE";
    private static final String QUANTITY_VALUE_LOW = "QUANTITY_VALUE_LOW";
    private static final String QUANTITY_VALUE_HIGH = "QUANTITY_VALUE_HIGH";
    private static final String DATE_VALUE = "DATE_VALUE";
    private static final String DATE_START = "DATE_START";
    private static final String DATE_END = "DATE_END";
    
		 
	
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
	
	public static final boolean isRangeSearch(Class<? extends Resource> resourceType, Parameter queryParm) throws Exception {
		return (SearchUtil.getValueTypes(resourceType, queryParm.getName()).contains(Range.class));
	}
	
	public static final boolean isDateSearch(Class<? extends Resource> resourceType, Parameter queryParm) throws Exception {
		return (SearchUtil.getValueTypes(resourceType, queryParm.getName()).contains(com.ibm.watsonhealth.fhir.model.Date.class) ||
                SearchUtil.getValueTypes(resourceType, queryParm.getName()).contains(DateTime.class));
	}
	
	public static final boolean isDateRangeSearch(Class<? extends Resource> resourceType, Parameter queryParm) throws Exception  {
		return  SearchUtil.getValueTypes(resourceType, queryParm.getName()).contains(Period.class);
	}
	
	
	public JDBCNormalizedQueryBuilder(ParameterNormalizedDAO parameterDao) {
		super();
		this.parameterDao = parameterDao;
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
				
		helper = this.buildQueryCommon(resourceType, searchContext);
		
		log.exiting(CLASSNAME, METHODNAME);
		return helper.buildCountQuery();
	}

	/* (non-Javadoc)
	 * @see com.ibm.watsonhealth.fhir.persistence.util.QueryBuilder#buildQuery(java.lang.Class, com.ibm.watsonhealth.fhir.search.context.FHIRSearchContext)
	 */
	@Override
	public SqlQueryData buildQuery(Class<? extends Resource> resourceType, FHIRSearchContext searchContext)
			throws Exception {
		final String METHODNAME = "buildQuery";
		log.entering(CLASSNAME, METHODNAME, new Object[] {resourceType.getSimpleName(), searchContext.getSearchParameters()});
		
        QuerySegmentAggregator helper = this.buildQueryCommon(resourceType, searchContext);
				
		log.exiting(CLASSNAME, METHODNAME);
		//return sqlQueryString;
		return helper.buildQuery();
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
		List<Parameter> searchParameters = searchContext.getSearchParameters();
		int pageSize = searchContext.getPageSize();
		int offset = (searchContext.getPageNumber() - 1) * pageSize;
		QuerySegmentAggregator helper = new QuerySegmentAggregator(resourceType, offset, pageSize, this.parameterDao);
		
		this.resourceType = resourceType;
		
		/*
		// Special logic for handling LocationPosition queries. These queries have interdependencies between
		// a couple of related input query parameters
		if (Location.class.equals(resourceType)) {
			queryParm = this.processLocationPosition(searchParameters);
			if (queryParm != null) {
				queryParms.add(queryParm);
				parmAdded = true;
			}
		}  */
		
		// For each search parm, build a query parm that will satisfy the search. 
		for (Parameter queryParameter : searchParameters) {
			querySegment = this.buildQueryParm(resourceType, queryParameter);
			helper.addQueryData(querySegment, queryParameter);
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
		this.populateNameIdSubSegment(whereClauseSegment, queryParm, PARAMETER_TABLE_ALIAS);
		
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
		this.populateNameIdSubSegment(whereClauseSegment, queryParm, PARAMETER_TABLE_ALIAS);
		
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

	@Override
	protected SqlQueryData processChainedReferenceParm(Parameter queryParm) throws FHIRPersistenceException {
		throw new FHIRPersistenceNotSupportedException("Chained parameter searches not supported at this time.");
	}

	@Override
	protected SqlQueryData processInclusionCriteria(Parameter queryParm) throws FHIRPersistenceException {
		throw new FHIRPersistenceNotSupportedException("Compartment searches not supported at this time.");
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
		 
		log.fine("isDateSearch=" + isDateSearch + "  isDateRangeSearch=" + isDateRangeSearch);
							
		// Build this piece of the segment:
		//(P1.PARAMETER_NAME_ID = x AND
		this.populateNameIdSubSegment(whereClauseSegment, queryParm, PARAMETER_TABLE_ALIAS);
		  	
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
		
		// Build this piece of the segment:
		// (P1.PARAMETER_NAME_ID = x AND
		this.populateNameIdSubSegment(whereClauseSegment, queryParm, PARAMETER_TABLE_ALIAS);
		
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
			    bindVariables.add(CodeSystemsCache.getCodeSystemId(value.getValueSystem(), this.parameterDao));
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
		this.populateNameIdSubSegment(whereClauseSegment, queryParm, PARAMETER_TABLE_ALIAS);
		  	
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
		this.populateNameIdSubSegment(whereClauseSegment, queryParm, PARAMETER_TABLE_ALIAS);
		  
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
				systemId = CodeSystemsCache.getCodeSystemId(value.getValueSystem(), this.parameterDao);
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
	protected SqlQueryData buildLocationQuerySegment(String parmName, BoundingBox boundingBox) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Populates the parameter name sub-segment of the passed where clause segment.
	 * @param whereClauseSegment
	 * @param queryParm
	 * @throws FHIRPersistenceDBConnectException
	 * @throws FHIRPersistenceDataAccessException
	 */
	private void populateNameIdSubSegment(StringBuilder whereClauseSegment, Parameter queryParm, String parameterTableAlias) throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException {
		final String METHODNAME = "populateNameIdSubSegment";
		log.entering(CLASSNAME, METHODNAME, queryParm.toString());
		
		int parameterNameId;
		
		// Build this piece of the segment:
		//(P1.PARAMETER_NAME_ID = x AND
		parameterNameId = ParameterNamesCache.getParameterNameId(queryParm.getName(), this.parameterDao);
		whereClauseSegment.append(LEFT_PAREN);
		whereClauseSegment.append(parameterTableAlias).append("PARAMETER_NAME_ID=").append(parameterNameId);
		
		log.exiting(CLASSNAME, METHODNAME);
	}

}
