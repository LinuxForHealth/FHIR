/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceNotSupportedException;
import com.ibm.watsonhealth.fhir.search.Parameter;
import com.ibm.watsonhealth.fhir.search.ParameterValue;

/**
 * This class defines a reusable method structure and common functionality for a FHIR peristence query builder.
 * @author markd
 *
 */
public abstract class AbstractQueryBuilder<T1, T2>  implements QueryBuilder<T1> {
	
	private static final Logger log = java.util.logging.Logger.getLogger(AbstractQueryBuilder.class.getName());
	private static final String CLASSNAME = AbstractQueryBuilder.class.getName();

	
	public AbstractQueryBuilder() {
		super();
	}
	
	/**
	 * Examines the passed ParamaterValue, and checks to see if the value is a URL. If it is, the 
	 * {ResourceType/id} part of the URL path is extracted and returned. For example:
	 * If the input value is http://localhost:8080/fhir/Patient/123, then
	 * Patient/123 is returned. 
	 * @param parmValue A valid String parameter value that may or may not contain a URL.
	 * @return String - The last 2 segments of the URL path is returned if the passed parmValue is a URL; 
	 * otherwise, null is returned.
	 *   
	 */
	public static String extractReferenceFromUrl(String parmValue) {
		final String METHODNAME = "extractReferenceFromUrl";
		log.entering(CLASSNAME, METHODNAME, parmValue);
		
		String referenceValue = null;
		URL parmValueUrl;
		String urlString;
		String[] urlPath;
		try {
			parmValueUrl = new URL(parmValue);
			urlString = parmValueUrl.getPath();
			urlPath = urlString.split("/");
			referenceValue = urlPath[urlPath.length-2] + "/" + urlPath[urlPath.length-1];
		}
		catch(MalformedURLException e) {}
		
		log.exiting(CLASSNAME, METHODNAME);
		return referenceValue;
	}
	
	/**
	 * Builds a query segment for the passed query parameter.
	 * @param resourceType - A valid FHIR Resource type
	 * @param queryParm - A Parameter object describing the name, value and type of search parm.
	 * @return T1 - An object representing the selector query segment for the passed search parm.
	 * @throws FHIRPersistenceException
	 */
	protected T1 buildQueryParm(Class<? extends Resource> resourceType, Parameter queryParm) 
			throws FHIRPersistenceException {
		final String METHODNAME = "buildQueryParm";
		log.entering(CLASSNAME, METHODNAME, queryParm.toString());
		
		T1 jsonQueryParm = null;
		Parameter.Type type;
		
		
		try {
			type = queryParm.getType();
				
			switch(type) {
			case STRING:    jsonQueryParm = this.processStringParm(queryParm);
				    break;
			case REFERENCE: jsonQueryParm = this.processReferenceParm(queryParm);
					    break;
			case DATE:      jsonQueryParm = this.processDateParm(queryParm);
			        break;
			case TOKEN:     jsonQueryParm = this.processTokenParm(queryParm);
					break;
			case NUMBER:    jsonQueryParm = this.processNumberParm(queryParm);
					break;
			case QUANTITY:  jsonQueryParm = this.processQuantityParm(resourceType, queryParm);
					break;
			case URI:  		jsonQueryParm = this.processUriParm(queryParm);
					break;
			
			default: throw new FHIRPersistenceNotSupportedException("Parm type not yet supported: " + type.value());
			}
		}
		finally {
		log.exiting(CLASSNAME, METHODNAME, new Object[] {jsonQueryParm});
		}
		return jsonQueryParm;
		}
	
	/**
	 * Map the Modifier in the passed Parameter to a supported query operator.
	 * @param queryParm - A valid query Parameter.
	 * @return T2 - A supported operator.
	 */
	protected abstract T2 getOperator(Parameter queryParm);
		
	/**
	 * Map the Prefix in the passed ParameterValue to a supported query operator.
	 * @param queryParm - A valid query ParameterValue.
	 * @return T2 - A supported operator.
	 */
	protected abstract T2 getPrefixOperator(ParameterValue queryParm);
	
	/**
	 * Creates a query segment for a String type parameter.
	 * @param queryParm - The query parameter. 
	 * @return T1 - An object containing query segment. 
	 */
	protected abstract T1 processStringParm(Parameter queryParm);
	
	/**
	 * Creates a query segment for a Reference type parameter.
	 * @param queryParm - The query parameter. 
	 * @return T1 - An object containing query segment. 
	 */
	protected abstract T1 processReferenceParm(Parameter queryParm);
	
	/**
	 * Creates a query segment for a Date type parameter.
	 * @param queryParm - The query parameter. 
	 * @return T1 - An object containing query segment. 
	 */
	protected abstract T1 processDateParm(Parameter queryParm);
	
	/**
	 * Creates a query segment for a Token type parameter.
	 * @param queryParm - The query parameter. 
	 * @return T1 - An object containing query segment. 
	 */
	protected abstract T1 processTokenParm(Parameter queryParm);
	
	/**
	 * Creates a query segment for a Number type parameter.
	 * @param queryParm - The query parameter. 
	 * @return T1 - An object containing query segment. 
	 */
	protected abstract T1 processNumberParm(Parameter queryParm);
	
	/**
	 * Creates a query segment for a Quantity type parameter.
	 * @param queryParm - The query parameter. 
	 * @return T1 - An object containing query segment. 
	 */
	protected abstract T1 processQuantityParm(Class<? extends Resource> resourceType, Parameter queryParm);
	
	/**
	 * Creates a query segment for a URI type parameter.
	 * @param queryParm - The query parameter. 
	 * @return T1 - An object containing query segment. 
	 */
	protected abstract T1 processUriParm(Parameter queryParm);
	
	/**
	 * Creates a query segment for the Range data type, which is a kind of Quantity search.
	 * The query segment tests to see if each value associated with the query parm  is >= range-low AND <= range-high.
	 * @param queryParm - The query parameter. 
	 * @return T1 - An object containing a query segment. 
	 */
	protected abstract T1 processRange(Parameter queryParm);
	
	/**
	 * Builds and returns a query segment that searches on the target date contained within a date Period.
	 * @param queryParmName - The name of the passed Date type search parameter.
	 * @param parmValue - A date value associated with the passed Date parameter.
	 * @return T1 - An object containing a query segment
	 */
	protected abstract T1 processDatePeriod(String queryParmName, ParameterValue parmValue);
	 
	 /**
		 * This method executes special logic for a Token type query that maps to a LocationPosition data type.
		 * @param queryParameters The entire collection of query input parameters
		 * @return JsonObject - A query segment related to a LocationPosition
		 */
	protected abstract T1 processLocationPosition(List<Parameter> queryParameters);

}
