/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.util;

import java.io.StringWriter;
import java.security.Principal;
import java.util.Date;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ibm.watsonhealth.fhir.core.FHIRUtilities;
import com.ibm.watsonhealth.fhir.core.audit.logging.api.AuditLogEventType;
import com.ibm.watsonhealth.fhir.core.audit.logging.api.AuditLogService;
import com.ibm.watsonhealth.fhir.core.audit.logging.api.AuditLogServiceFactory;
import com.ibm.watsonhealth.fhir.core.audit.logging.beans.ApiParameters;
import com.ibm.watsonhealth.fhir.core.audit.logging.beans.AuditLogEntry;
import com.ibm.watsonhealth.fhir.core.audit.logging.beans.Context;
import com.ibm.watsonhealth.fhir.core.audit.logging.beans.Data;
import com.ibm.watsonhealth.fhir.core.audit.logging.beans.Database;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil.Format;

/**
 * This class provides convenience methods for FHIR Rest services that need to write FHIR audit log entries.
 * @author markd
 *
 */
public class RestAuditLogger {
	private static final String CLASSNAME = RestAuditLogger.class.getName();
	private static final Logger log = java.util.logging.Logger.getLogger(CLASSNAME);
	
	/**
	 * Builds an audit log entry for a 'create' REST service invocation.
	 * @param user - The user who initiated the request.
	 * @param resourceType - The type of FHIR Resource being created.
	 * @param resource - The Resource object being created.
	 * @param startTime - The start time of the create request execution.
	 * @param endTime - The end time of the create request execution.
	 * @param responseStatus - The response status.
	 */
	public static void logCreate(Principal user, String resourceType, Resource resource, Date startTime, Date endTime, Response.Status responseStatus) {
		final String METHODNAME = "logCreate";
		log.entering(CLASSNAME, METHODNAME);
		
		AuditLogService auditLogSvc = AuditLogServiceFactory.getService();
		AuditLogEntry entry = initAuditLogEntry(user, resourceType, resource, startTime, endTime, responseStatus);
				
		entry.getContext().setAction("C");
		if (Response.Status.CREATED.equals(responseStatus)) {
			entry.getContext().setData(new Data().withId(resource.getId().getValue()));
			try {
				entry.getContext().setValueNew(convertToJsonObject(resource));
			}
			catch(JAXBException e) {
				log.severe("Failure converting Resource to JsonObject: " + e.getMessage());
			}
		}
				
		auditLogSvc.logEntry(entry);
		
		log.exiting(CLASSNAME, METHODNAME);
	}
	
	/**
	 * Builds an audit log entry for an 'update' REST service invocation.
	 * @param user - The user who initiated the request.
	 * @param resourceType - The type of FHIR Resource being updated.
	 * @param oldResource - The previous version of the Resource, before it was updated.
	 * @param newResource - The updated version of the Resource.
	 * @param startTime - The start time of the update request execution.
	 * @param endTime - The end time of the update request execution.
	 * @param responseStatus - The response status.
	 */
	public static void logUpdate(Principal user, String resourceType, Resource oldResource, Resource updatedResource, Date startTime, Date endTime, 
								 Response.Status responseStatus) {
		final String METHODNAME = "logCreate";
		log.entering(CLASSNAME, METHODNAME);
		
		AuditLogService auditLogSvc = AuditLogServiceFactory.getService();
		AuditLogEntry entry = initAuditLogEntry(user, resourceType, updatedResource, startTime, endTime, responseStatus);
		
		entry.getContext().setAction("U");
		if (Response.Status.OK.equals(responseStatus)) {
			entry.getContext().setData(new Data().withId(updatedResource.getId().getValue()));
			try {
				entry.getContext().setValueOld(convertToJsonObject(oldResource));
				entry.getContext().setValueNew(convertToJsonObject(updatedResource));
			}
			catch(JAXBException e) {
				log.severe("Failure converting Resource to JsonObject: " + e.getMessage());
			}
		}
				
		auditLogSvc.logEntry(entry);
		
		log.exiting(CLASSNAME, METHODNAME);
	}
	
	/**
	 * Creates and intializes a new audit log entry, with attributes common to all REST services.
	 * @param user - The user who initiated the request.
	 * @param resourceType - The type of FHIR Resource.
	 * @param resource - The Resource object.
	 * @param startTime - The start time of the request execution.
	 * @param endTime - The end time of the request execution.
	 * @param responseStatus - The response status.
	 * @return AuditLogEntry - an intiailized audit log entry.
	 */
	private static AuditLogEntry initAuditLogEntry(Principal user, String resourceType, Resource resource, 
											     Date startTime, Date endTime, Response.Status responseStatus) {
		
		final String METHODNAME = "initAuditLogEntry";
		log.entering(CLASSNAME, METHODNAME);
		
		AuditLogService auditLogSvc = AuditLogServiceFactory.getService();
		AuditLogEntry entry = auditLogSvc.initLogEntry(AuditLogEventType.DATA_ACCESS);
		entry.setUserName(user.getName());
		entry.setContext(new Context());
		entry.getContext().setApiParameters(new ApiParameters().withRequest(resourceType));
		entry.getContext().setDb(acquireDbData());
		entry.getContext().setStatus(responseStatus.toString());
		entry.getContext().setStartTime(FHIRUtilities.formatTimestamp(startTime));
		entry.getContext().setEndTime(FHIRUtilities.formatTimestamp(endTime));
		if (resource.getId() != null) {
			entry.getContext().setData(new Data().withId(resource.getId().getValue()));
		}
				
		log.exiting(CLASSNAME, METHODNAME);
		return entry;
		
		
	}
	
	/**
	 * 
	 * @return Database - Metadata describing the database in use by the FHIR server.
	 */
	private static Database acquireDbData() {
		
		Database db = new Database();
		// TODO need to acquire dbname and hostname. Can get dbname via jndi lookups using FHIRServerUtils.getJNDIValue()
		// Not sure about how to get hostname.
		
		return db;
		
	}

	/**
	 * Converts the passed FHIR Resource object to a GSON Json object
	 * @param resource - The Resource to be converted
	 * @return JSONObject - The JSONObject equivalent of the passed Resource
	 * @throws JAXBException
	 */
	private static JsonObject convertToJsonObject(Resource resource) throws JAXBException {
		
		final String METHODNAME = "convertToJsonObject(Resource)";
		log.entering(CLASSNAME, METHODNAME);
		
		// serialize FHIR Resource object to String
		StringWriter writer = new StringWriter();
		FHIRUtil.write(resource, Format.JSON, writer);
		String jsonString = writer.toString();
		
		// convert String into GSON JsonObject
		Gson gson = new Gson();
		JsonElement jsonElement = gson.fromJson(jsonString, JsonElement.class);
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		
		log.exiting(CLASSNAME, METHODNAME);
		return jsonObject;
		
	}

}
