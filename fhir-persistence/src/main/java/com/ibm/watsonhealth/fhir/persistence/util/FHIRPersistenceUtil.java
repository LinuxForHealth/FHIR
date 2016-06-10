/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.datatype.XMLGregorianCalendar;

import com.ibm.watsonhealth.fhir.core.FHIRUtilities;
import com.ibm.watsonhealth.fhir.model.Instant;
import com.ibm.watsonhealth.fhir.model.Location;
import com.ibm.watsonhealth.fhir.model.ObjectFactory;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRHistoryContext;
import com.ibm.watsonhealth.fhir.persistence.context.impl.FHIRHistoryContextImpl;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;

public class FHIRPersistenceUtil {
	private static final Logger log = Logger.getLogger(FHIRPersistenceUtil.class.getName());

	private static final ObjectFactory objectFactory = new ObjectFactory();

	// Parse history parameters into a FHIRHistoryContext
	public static FHIRHistoryContext parseHistoryParameters(Map<String, List<String>> queryParameters) throws FHIRPersistenceException {
		if (log.isLoggable(Level.FINE)) {
			log.entering(FHIRPersistenceUtil.class.getName(), "parseHistoryParameters");
		}
		FHIRHistoryContext context = new FHIRHistoryContextImpl();
		try {
			for (String name : queryParameters.keySet()) {
				List<String> values = queryParameters.get(name);
				String first = values.get(0);
				if ("_page".equals(name)) {
					int pageNumber = Integer.parseInt(first);
					context.setPageNumber(pageNumber);
				} else if ("_count".equals(name)) {
					int pageSize = Integer.parseInt(first);
					context.setPageSize(pageSize);
				} else if ("_since".equals(name)) {
					XMLGregorianCalendar calendar = FHIRUtilities.parseDateTime(first, false);
					if (FHIRUtilities.isDateTime(calendar)) {
						calendar = calendar.normalize();
						Instant since = objectFactory.createInstant().withValue(calendar);
						context.setSince(since);
					} else {
						throw new FHIRPersistenceException("The '_since' parameter must be a fully specified ISO 8601 date/time");
					}
				} else if ("_format".equals(name)) {
					// safely ignore
					continue;
				} else {
					throw new FHIRPersistenceException("Unrecognized history parameter: '" + name + "'");
				}
			}
		} catch (FHIRPersistenceException e) {
			throw e;
		} catch (Exception e) {
			throw new FHIRPersistenceException("Error parsing history parameters", e);
		} finally {
			if (log.isLoggable(Level.FINE)) {
				log.exiting(FHIRPersistenceUtil.class.getName(), "parseHistoryParameters");
			}
		}
		return context;
	}

	public static boolean isLocationResourceType(Class<? extends Resource> resourceType){
		if (log.isLoggable(Level.FINE)) {
			log.entering(FHIRPersistenceUtil.class.getName(), "isLocationResourceType");
		}
		try{
			if(resourceType.equals(Location.class)){
				return true;
			}

			return false;
		}finally{
			if (log.isLoggable(Level.FINE)) {
				log.exiting(FHIRPersistenceUtil.class.getName(), "isLocationResourceType");
			}
		}

	}

	/**
	 * Method to build bounding box
	 * @param locationList
	 * @return
	 */
	public  static Map <String, Double> createBoundingBox(List<Map <String, String>> locationList){
		if (log.isLoggable(Level.FINE)) {
			log.entering(FHIRPersistenceUtil.class.getName(), "createBoundingBox");
		}
		Map <String, Double> boundingPoints = new HashMap<String, Double>();
		try{
			Double latitude=0.0, longitude=0.0, minLongitude, maxLongitude, minLatitude, maxLatitude;
			Double distance = 5.0; //Default 5 KM
			String unit = "kilometers" ;
			//extract out values 
			for(Map <String, String> m : locationList){
				if(m.get("name").equals("near")){
					//system = lat
					//code = long
					latitude = Double.parseDouble( m.get("system"));
					longitude = Double.parseDouble( m.get("code"));		
				}else if(m.get("name").equals("near-distance")){
					if(m.get("code") != null){
						if(m.get("system") != null){
							distance = Double.parseDouble( m.get("system"));
						}
						if( m.get("code") != null){
							//if value is not null then only try to set it up so we can levarage default values
							unit = m.get("code");
						}
					}
				}
			}

			if(unit.contentEquals("km")){
				unit = "kilometers"; //default is kilometers
			}

			if (unit.equalsIgnoreCase("miles")){
				distance = covertMilesToKilometers(distance);
			}

			log.fine("distance   :" + distance + ":unit:" + unit  );
			//earth radius 
			double earthRadius = 6371.0;
			//build bounding box points 
			minLatitude = latitude + (-distance / earthRadius) * (180.0 / Math.PI);
			maxLatitude = latitude + (distance / earthRadius) * (180.0 / Math.PI);
			minLongitude = longitude + ((-distance / earthRadius) * (180.0 / Math.PI)) / Math.cos(latitude * (180.0 / Math.PI));
			maxLongitude = longitude + ((distance / earthRadius) * (180.0 / Math.PI)) / Math.cos(latitude * (180.0 / Math.PI));

			boundingPoints.put("minLatitude", minLatitude);
			boundingPoints.put("maxLatitude",  maxLatitude);
			boundingPoints.put("minLongitude", minLongitude);
			boundingPoints.put("maxLongitude", maxLongitude);

			log.fine("boundingPoints   :" + boundingPoints  );
			return boundingPoints ;
		} finally {
			if (log.isLoggable(Level.FINE)) {
				log.exiting(FHIRPersistenceUtil.class.getName(), "createBoundingBox");
			}
		}

	}


	/**
	 * Method to convert miles into kilometers 
	 * @param miles
	 * @return
	 */

	public static double covertMilesToKilometers(double miles){
		if (log.isLoggable(Level.FINE)) {
			log.entering(FHIRPersistenceUtil.class.getName(), "covertMilesToKilometers");
		}
		try{
			return  miles * 1.609344;
		}finally{
			if (log.isLoggable(Level.FINE)) {
				log.exiting(FHIRPersistenceUtil.class.getName(), "covertMilesToKilometers");
			}
		}

	}
}
