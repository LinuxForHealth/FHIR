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

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
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
import com.ibm.watsonhealth.fhir.core.audit.logging.beans.Batch;
import com.ibm.watsonhealth.fhir.core.audit.logging.beans.Context;
import com.ibm.watsonhealth.fhir.core.audit.logging.beans.Data;
import com.ibm.watsonhealth.fhir.core.audit.logging.beans.Database;
import com.ibm.watsonhealth.fhir.model.Bundle;
import com.ibm.watsonhealth.fhir.model.BundleEntry;
import com.ibm.watsonhealth.fhir.model.HTTPVerb;
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
	 * @param requestUrl - The request URL.
	 * @param resource - The Resource object being created.
	 * @param startTime - The start time of the create request execution.
	 * @param endTime - The end time of the create request execution.
	 * @param responseStatus - The response status.
	 */
	public static void logCreate(Principal user, UriInfo requestUrl, Resource resource, Date startTime, Date endTime, Response.Status responseStatus) {
		final String METHODNAME = "logCreate";
		log.entering(CLASSNAME, METHODNAME);
		
		AuditLogService auditLogSvc = AuditLogServiceFactory.getService();
		AuditLogEntry entry = auditLogSvc.initLogEntry(AuditLogEventType.FHIR_CREATE);
		populateAuditLogEntry(entry, user, requestUrl, resource, startTime, endTime, responseStatus);
				
		entry.getContext().setAction("C");
		if (Response.Status.CREATED.equals(responseStatus)) {
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
	 * @param requestUrl - The request URL.
	 * @param oldResource - The previous version of the Resource, before it was updated.
	 * @param newResource - The updated version of the Resource.
	 * @param startTime - The start time of the update request execution.
	 * @param endTime - The end time of the update request execution.
	 * @param responseStatus - The response status.
	 */
	public static void logUpdate(Principal user, UriInfo requestUrl, Resource oldResource, Resource updatedResource, Date startTime, Date endTime, 
								 Response.Status responseStatus) {
		final String METHODNAME = "logCreate";
		log.entering(CLASSNAME, METHODNAME);
		
		AuditLogService auditLogSvc = AuditLogServiceFactory.getService();
		AuditLogEntry entry = auditLogSvc.initLogEntry(AuditLogEventType.FHIR_UPDATE);
		populateAuditLogEntry(entry, user, requestUrl, updatedResource, startTime, endTime, responseStatus);
		
		entry.getContext().setAction("U");
		if (Response.Status.OK.equals(responseStatus)) {
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
	 * Builds an audit log entry for a 'read' REST service invocation.
	 * @param user - The user who initiated the request.
	 * @param requestUrl - The request URL.
	 * @param resource - The Resource object being read.
	 * @param startTime - The start time of the read request execution.
	 * @param endTime - The end time of the read request execution.
	 * @param responseStatus - The response status.
	 */
	public static void logRead(Principal user, UriInfo requestUrl, Resource resource, Date startTime, Date endTime, Response.Status responseStatus) {
		final String METHODNAME = "logRead";
		log.entering(CLASSNAME, METHODNAME);
		
		AuditLogService auditLogSvc = AuditLogServiceFactory.getService();
		AuditLogEntry entry = auditLogSvc.initLogEntry(AuditLogEventType.FHIR_READ);
		populateAuditLogEntry(entry, user, requestUrl, resource, startTime, endTime, responseStatus);
				
		entry.getContext().setAction("R");
						
		auditLogSvc.logEntry(entry);
		log.exiting(CLASSNAME, METHODNAME);
	}
	
	/**
	 * Builds an audit log entry for a 'version-read' REST service invocation.
	 * @param user - The user who initiated the request.
	 * @param requestUrl - The request URL.
	 * @param resource - The Resource object being read.
	 * @param startTime - The start time of the read request execution.
	 * @param endTime - The end time of the read request execution.
	 * @param responseStatus - The response status.
	 */
	public static void logVersionRead(Principal user, UriInfo requestUrl, Resource resource, Date startTime, Date endTime, Response.Status responseStatus) {
		final String METHODNAME = "logVersionRead";
		log.entering(CLASSNAME, METHODNAME);
		
		AuditLogService auditLogSvc = AuditLogServiceFactory.getService();
		AuditLogEntry entry = auditLogSvc.initLogEntry(AuditLogEventType.FHIR_VREAD);
		populateAuditLogEntry(entry, user, requestUrl, resource, startTime, endTime, responseStatus);
				
		entry.getContext().setAction("R");
						
		auditLogSvc.logEntry(entry);
		log.exiting(CLASSNAME, METHODNAME);
	}
	
	/**
	 * Builds an audit log entry for a 'history' REST service invocation.
	 * @param user - The user who initiated the request.
	 * @param requestUrl - The request URL.
	 * @param bundle - The Bundle that is returned to the REST service caller.
	 * @param startTime - The start time of the bundle request execution.
	 * @param endTime - The end time of the bundle request execution.
	 * @param responseStatus - The response status.
	 */
	public static void logHistory(Principal user, UriInfo requestUrl, Bundle bundle, Date startTime, Date endTime, Response.Status responseStatus) {
		final String METHODNAME = "logHistory";
		log.entering(CLASSNAME, METHODNAME);
		
		AuditLogService auditLogSvc = AuditLogServiceFactory.getService();
		AuditLogEntry entry = auditLogSvc.initLogEntry(AuditLogEventType.FHIR_HISTORY);
			
		
		populateAuditLogEntry(entry, user, requestUrl, null, startTime, endTime, responseStatus);
		if (bundle != null && bundle.getEntry() != null && !bundle.getEntry().isEmpty()) {
			entry.getContext().setBatch(new Batch().withResourcesRead(bundle.getTotal().getValue().longValue()));
		}
		entry.getContext().setAction("R");
						
		auditLogSvc.logEntry(entry);
		log.exiting(CLASSNAME, METHODNAME);
	}
	
	/**
	 * Builds an audit log entry for a 'bundle' REST service invocation.
	 * @param user - The user who initiated the request.
	 * @param requestUrl - The request URL.
	 * @param bundle - The Bundle that is returned to the REST service caller.
	 * @param startTime - The start time of the bundle request execution.
	 * @param endTime - The end time of the bundle request execution.
	 * @param responseStatus - The response status.
	 */
	public static void logBundle(Principal user, UriInfo requestUrl, Bundle bundle, Date startTime, Date endTime, Response.Status responseStatus) {
		final String METHODNAME = "logBundle";
		log.entering(CLASSNAME, METHODNAME);
		
		AuditLogService auditLogSvc = AuditLogServiceFactory.getService();
		AuditLogEntry entry = auditLogSvc.initLogEntry(AuditLogEventType.FHIR_BUNDLE);
		long readCount = 0;
		long createCount = 0;
		long updateCount = 0;
		HTTPVerb requestMethod;
					
		populateAuditLogEntry(entry, user, requestUrl, null, startTime, endTime, responseStatus);
		if (bundle != null && bundle.getEntry() != null && !bundle.getEntry().isEmpty()) {
			for (BundleEntry bundleEntry : bundle.getEntry()) {
				if (bundleEntry.getRequest() != null && bundleEntry.getRequest().getMethod() != null) {
					requestMethod = bundleEntry.getRequest().getMethod();
					switch (requestMethod.getValue())  {
					case GET:
						readCount++;
						break;
					case POST:
						createCount++;
						break;
					case PUT:
						updateCount++;
						break;
					default:
						break;
					
					}
				}
			}
			entry.getContext().setBatch(new Batch()
								.withResourcesCreated(createCount)
								.withResourcesRead(readCount)
								.withResourcesUpdated(updateCount));
		}
		
		auditLogSvc.logEntry(entry);
		log.exiting(CLASSNAME, METHODNAME);
	}
	
	/**
	 * Builds an audit log entry for a 'search' REST service invocation.
	 * @param user - The user who initiated the request.
	 * @param requestUrl - The request URL.
	 * @param queryParms - The query parameters passed to the search REST service.
	 * @param bundle - The Bundle that is returned to the REST service caller.
	 * @param startTime - The start time of the bundle request execution.
	 * @param endTime - The end time of the bundle request execution.
	 * @param responseStatus - The response status.
	 */
	public static void logSearch(Principal user, UriInfo requestUrl, MultivaluedMap<String, String> queryParms, Bundle bundle, Date startTime, Date endTime, Response.Status responseStatus) {
		final String METHODNAME = "logHistory";
		log.entering(CLASSNAME, METHODNAME);
		
		AuditLogService auditLogSvc = AuditLogServiceFactory.getService();
		AuditLogEntry entry = auditLogSvc.initLogEntry(AuditLogEventType.FHIR_SEARCH);
		populateAuditLogEntry(entry, user, requestUrl, null, startTime, endTime, responseStatus);
		
		if (queryParms != null && !queryParms.isEmpty()) {
			entry.getContext().setQuery(queryParms.toString());
		}
		if (bundle != null && bundle.getEntry() != null && !bundle.getEntry().isEmpty()) {
			entry.getContext().setBatch(new Batch().withResourcesRead(bundle.getTotal().getValue().longValue()));
		}
		entry.getContext().setAction("R");
						
		auditLogSvc.logEntry(entry);
		log.exiting(CLASSNAME, METHODNAME);
	}
	
	/**
	 * Populates the passed audit log entry, with attributes common to all REST services.
	 * @param entry - The AuditLogEntry to be populated.
	 * @param user - The user who initiated the request.
	 * @param requestUrl - The request URL.
	 * @param resource - The Resource object.
	 * @param startTime - The start time of the request execution.
	 * @param endTime - The end time of the request execution.
	 * @param responseStatus - The response status.
	 * @return AuditLogEntry - an initialized audit log entry.
	 */
	private static AuditLogEntry populateAuditLogEntry(AuditLogEntry entry, Principal user, UriInfo requestUrl, Resource resource, 
											     Date startTime, Date endTime, Response.Status responseStatus) {
		
		final String METHODNAME = "populateAuditLogEntry";
		log.entering(CLASSNAME, METHODNAME);
		
		entry.setUserName(user.getName());
		entry.setContext(new Context());
		entry.getContext().setApiParameters(new ApiParameters().withRequest(requestUrl.getAbsolutePath().toString()));
		entry.getContext().setDb(acquireDbData());
		entry.getContext().setStatus(responseStatus.toString());
		entry.getContext().setStartTime(FHIRUtilities.formatTimestamp(startTime));
		entry.getContext().setEndTime(FHIRUtilities.formatTimestamp(endTime));
		if (resource != null && resource.getId() != null) {
			entry.getContext().setData(new Data().withId(resource.getId().getValue()));
			if (resource.getMeta() != null && resource.getMeta().getVersionId() != null) {
				entry.getContext().getData().setVersionId(resource.getMeta().getVersionId().getValue());
			}
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
