/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import com.ibm.watsonhealth.fhir.model.Location;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceNotSupportedException;
import com.ibm.watsonhealth.fhir.search.Parameter;
import com.ibm.watsonhealth.fhir.search.Parameter.Modifier;
import com.ibm.watsonhealth.fhir.search.ParameterValue;

/**
 * This class defines a reusable method structure and common functionality for a FHIR peristence query builder.
 * @author markd
 *
 */
public abstract class AbstractQueryBuilder<T1, T2>  implements QueryBuilder<T1> {
	
	private static final Logger log = java.util.logging.Logger.getLogger(AbstractQueryBuilder.class.getName());
	private static final String CLASSNAME = AbstractQueryBuilder.class.getName();
	
	// Constants used in token (data type LocationPosition) searches
	public static final String LATITUDE = "-latitude";
	public static final String LONGITUDE = "-longitude";
	public static final String NEAR = "near";
	public static final String NEAR_DISTANCE = "near-distance";
	public static final double DEFAULT_DISTANCE = 5.0;
	public static final String DEFAULT_UNIT = "km";

	
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
	
	public static boolean isAbsoluteURL(String s) {
	    try {
	        new URL(s);
	        return true;
	    } catch (MalformedURLException e) {}
	    return false;
	}
	
	/**
	 * Builds a query segment for the passed query parameter.
	 * @param resourceType - A valid FHIR Resource type
	 * @param queryParm - A Parameter object describing the name, value and type of search parm.
	 * @return T1 - An object representing the selector query segment for the passed search parm.
	 * @throws Exception 
	 */
	protected T1 buildQueryParm(Class<? extends Resource> resourceType, Parameter queryParm, String tableAlias) 
			throws Exception {
		final String METHODNAME = "buildQueryParm";
		log.entering(CLASSNAME, METHODNAME, queryParm.toString());
		
		T1 databaseQueryParm = null;
		Parameter.Type type;
		
		try {
			// NOTE: The special logic needed to process NEAR and NEAR_DISTANCE query parms for the Location resource type is
			// found in method processLocationPosition(). This method will not handle those.
			if (! (Location.class.equals(resourceType) && 
				(queryParm.getName().equals(NEAR) || queryParm.getName().equals(NEAR_DISTANCE)))) {
				
				type = queryParm.getType();
				switch(type) {
				case STRING:    databaseQueryParm = this.processStringParm(queryParm, tableAlias);
					    break;
				case REFERENCE: if (queryParm.isChained()) {
									databaseQueryParm = this.processChainedReferenceParm(queryParm);
								}
								else if (queryParm.isInclusionCriteria()) {
									databaseQueryParm = this.processInclusionCriteria(queryParm);
								}
								else {
									databaseQueryParm = this.processReferenceParm(resourceType, queryParm, tableAlias);
								}
						break;
				case DATE:      databaseQueryParm = this.processDateParm(resourceType, queryParm, tableAlias);
				        break;
				case TOKEN:     databaseQueryParm = this.processTokenParm(queryParm, tableAlias);
						break;
				case NUMBER:    databaseQueryParm = this.processNumberParm(queryParm, tableAlias);
						break;
				case QUANTITY:  databaseQueryParm = this.processQuantityParm(resourceType, queryParm, tableAlias);
						break;
				case URI:  		databaseQueryParm = this.processUriParm(queryParm, tableAlias);
						break;
				
				default: throw new FHIRPersistenceNotSupportedException("Parm type not yet supported: " + type.value());
				}
			}
		}
		finally {
			log.exiting(CLASSNAME, METHODNAME, new Object[] {databaseQueryParm});
		}
		return databaseQueryParm;
		}
	
	/**
	 * Map the Modifier in the passed Parameter to a supported query operator.
	 * @param queryParm - A valid query Parameter.
	 * @return T2 - A supported operator.
	 */
	protected abstract T2 getOperator(Parameter queryParm);
	
	/**
	 * Map the Modifier in the passed Parameter to a supported query operator. 
	 * If the mapping results in the default operator, override the default operator with the passed operator 
	 * if the passed operator is not null.
	 * @param queryParm - A valid query Parameter.
	 * @param defaultOverride - An operator that should override the default operator.
	 * @return T2 - A supported operator.
	 */
	protected abstract T2 getOperator(Parameter queryParm, T2 defaultOverride);
		
	/**
	 * Map the Prefix in the passed ParameterValue to a supported query operator.
	 * @param queryParmValue - A valid query ParameterValue.
	 * @return T2 - A supported operator.
	 */
	protected abstract T2 getPrefixOperator(ParameterValue queryParmValue);
	
	/**
	 * Creates a query segment for a String type parameter.
	 * @param queryParm - The query parameter. 
	 * @return T1 - An object containing query segment. 
	 */
	protected abstract T1 processStringParm(Parameter queryParm, String tableAlias) throws FHIRPersistenceException;
	
	/**
	 * Creates a query segment for a Reference type parameter.
	 * @param queryParm - The query parameter. 
	 * @return T1 - An object containing query segment. 
	 * @throws Exception 
	 */
	protected abstract T1 processReferenceParm(Class<? extends Resource> resourceType, Parameter queryParm, String tableAlias) throws Exception;
	
	/**
	 * Contains special logic for handling chained reference search parameters.
	 * @see https://www.hl7.org/fhir/search.html#reference (section 2.1.1.4.13)
	 * @param queryParm - The query parameter.
	 * @return T1 - An object containing a query segment. 
	 * @throws FHIRPersistenceException
	 */
	protected abstract T1 processChainedReferenceParm(Parameter queryParm) throws Exception;
	
	/**
	 * Contains special logic for handling Compartment based searches.
	 * @see https://www.hl7.org/fhir/compartments.html
	 * @param queryParm - The query parameter.
	 * @return T1 - An object containing a query segment. 
	 * @throws FHIRPersistenceException
	 */
	protected abstract T1 processInclusionCriteria(Parameter queryParm) throws Exception;
	
	/**
	 * Creates a query segment for a Date type parameter.
	 * @param queryParm - The query parameter. 
	 * @return T1 - An object containing query segment. 
	 * @throws Exception 
	 */
	protected abstract T1 processDateParm(Class<? extends Resource> resourceType, Parameter queryParm, String tableAlias) throws Exception;
	
	/**
	 * Creates a query segment for a Token type parameter.
	 * @param queryParm - The query parameter. 
	 * @return T1 - An object containing query segment. 
	 */
	protected abstract T1 processTokenParm(Parameter queryParm, String tableAlias) throws FHIRPersistenceException;
	
	/**
	 * Creates a query segment for a Number type parameter.
	 * @param queryParm - The query parameter. 
	 * @return T1 - An object containing query segment. 
	 * @throws FHIRPersistenceException 
	 */
	protected abstract T1 processNumberParm(Parameter queryParm, String tableAlias) throws FHIRPersistenceException;
	
	/**
	 * Creates a query segment for a Quantity type parameter.
	 * @param queryParm - The query parameter. 
	 * @return T1 - An object containing query segment. 
	 * @throws Exception 
	 */
	protected abstract T1 processQuantityParm(Class<? extends Resource> resourceType, Parameter queryParm, String tableAlias) throws Exception;
	
	/**
	 * Creates a query segment for a URI type parameter.
	 * @param queryParm - The query parameter. 
	 * @return T1 - An object containing query segment. 
	 * @throws FHIRPersistenceException 
	 */
	protected T1 processUriParm(Parameter queryParm, String tableAlias) throws FHIRPersistenceException {
		final String METHODNAME = "processUriParm";
		log.entering(CLASSNAME, METHODNAME, queryParm.toString());
		
		T1 parmRoot;
		Parameter myQueryParm;
				
		myQueryParm = queryParm;
		Modifier queryParmModifier = queryParm.getModifier();
		// A BELOW modifier has the same behavior as a "starts with" String search parm. 
		if (queryParmModifier != null && queryParmModifier.equals(Modifier.BELOW)) {
			 myQueryParm = new Parameter(queryParm.getType(), queryParm.getName(), null,
					 			queryParm.getModifierResourceTypeName(), queryParm.getValues());
		}
		parmRoot = this.processStringParm(myQueryParm, tableAlias);
								
						
		log.exiting(CLASSNAME, METHODNAME, parmRoot.toString());
		return parmRoot;
	}
	
	/**
	 * This method executes special logic for a Token type query that maps to a LocationPosition data type.
	 * @param queryParameters The entire collection of query input parameters
	 * @return T1 - A query segment related to a LocationPosition
	 * @throws FHIRPersistenceException 
	 */
	protected T1 processLocationPosition(List<Parameter> queryParameters) throws FHIRPersistenceException {
		final String METHODNAME = "processLocationPosition";
		log.entering(CLASSNAME, METHODNAME);
		
		double latitude = 0.0;;
        double longitude = 0.0;
        double distance = DEFAULT_DISTANCE;
        String unit = DEFAULT_UNIT;
        BoundingBox boundingBox;
        Parameter queryParm;
        boolean nearFound = false;
        T1 parmRoot = null;
        
        // We are only interested in the near and near-distance parameters.
        // Extract the following data elements: latitude, longitude, distance, distance unit
        Iterator<Parameter> queryParms = queryParameters.iterator();
        while (queryParms.hasNext()) {
           	queryParm = queryParms.next();
        	for (ParameterValue value : queryParm.getValues()) {
    			if (queryParm.getName().equals(NEAR)) {
	        		if (value.getValueSystem() != null) {
	        			latitude = Double.parseDouble(value.getValueSystem());
	        		}
	        		if (value.getValueCode() != null) {
	        			longitude = Double.parseDouble(value.getValueCode());
	        		}
	        		nearFound = true;
	        	}
    			else if (queryParm.getName().equals(NEAR_DISTANCE)) {
	        		if (value.getValueSystem() != null) {
	                    distance = Double.parseDouble(value.getValueSystem());
	                }
	                if (value.getValueCode() != null) {
	                    unit = value.getValueCode();
	                }
	            }
        	}
        }
        if (nearFound) {
        	boundingBox = FHIRPersistenceUtil.createBoundingBox(latitude, longitude, distance, unit);
        	parmRoot = this.buildLocationQuerySegment(NEAR,boundingBox);
        }
                
        log.exiting(CLASSNAME, METHODNAME);
		return parmRoot;
	}
	
	/**
	 * Builds a query segment for the passed parameter name using the geospatial data contained with the passed BoundingBox
	 * @param parmName - The name of the search parameter
	 * @param boundingBox - Container for the geospatial data needed to construct the query segment.
	 * @return T1 - The query segment necessary for searching locations that are inside the bounding box.
	 */
	protected abstract T1 buildLocationQuerySegment(String parmName, BoundingBox boundingBox) throws FHIRPersistenceException;

}
