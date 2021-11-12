/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.audit;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;

import com.ibm.fhir.audit.AuditLogEventType;
import com.ibm.fhir.audit.AuditLogService;
import com.ibm.fhir.audit.AuditLogServiceFactory;
import com.ibm.fhir.audit.beans.ApiParameters;
import com.ibm.fhir.audit.beans.AuditLogEntry;
import com.ibm.fhir.audit.beans.Batch;
import com.ibm.fhir.audit.beans.Context;
import com.ibm.fhir.audit.beans.Data;
import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.core.FHIRUtilities;
import com.ibm.fhir.core.util.handler.IPHandler;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.util.FHIRUtil;

/**
 * This class provides convenience methods for FHIR Java Batch Jobs
 * that access and manipulate FHIR Data, thus needing to write FHIR audit log entries.
 */
public class BulkAuditLogger {

    /*
     * Identifies the component when writing to the IBM FHIR Server
     */
    private static final String COMPONENT_ID = "fhir-bulkdata-server";

    private static final String CLASSNAME = BulkAuditLogger.class.getName();
    private static final Logger log = java.util.logging.Logger.getLogger(CLASSNAME);

    private static final IPHandler componentIpHandler = new IPHandler();

    private AuditLogService svc = AuditLogServiceFactory.getService();

    public BulkAuditLogger() {
        // No Operation
    }

    public boolean shouldLog() {
        // Wraps common code for logging
        if (log.isLoggable(Level.FINER)) {
            log.finer("Bulk Data Audit Log is '" + svc.isEnabled() + "'");
        }
        return svc.isEnabled();
    }

    private void log(AuditLogEventType eventType, String action, String description, Resource oldResource, Resource newResource, Date startTime, Date endTime,
            Response.Status responseStatus, String queryString, Long totalSearch, String location, String users) throws Exception {
        log(eventType, action, description, oldResource, newResource, startTime, endTime, responseStatus, queryString, totalSearch, location, users, null);
    }

    /*
     * This method is common code in the audit logging.
     */
    private void log(AuditLogEventType eventType, String action, String description, Resource oldResource, Resource newResource, Date startTime, Date endTime,
        Response.Status responseStatus, String queryString, Long totalSearch, String location, String users, String resource) throws Exception {

        AuditLogEntry entry = createAuditLogEntry(eventType, newResource, startTime, endTime, responseStatus, location, users);
        entry.getContext().setAction(action);
        entry.setDescription(description);

        String resourceName = null;
        if (newResource != null) {
            resourceName = newResource.getClass().getSimpleName();
        } else {
            resourceName = resource;
        }

        entry.getContext().setResourceName(resourceName);

        // Only add if it's a search
        if (AuditLogEventType.FHIR_SEARCH.equals(eventType)) {
            entry.getContext().setQueryParameters(queryString);
            entry.getContext()
                .setBatch(Batch.builder()
                    .resourcesRead(totalSearch).build());
        }

        svc.logEntry(entry);
    }

    /*
     * Creates an audit log entry with attributes common to all the bulkdata jobs.
     *
     * @param eventType the type of event
     * @param resource The Resource object.
     * @param startTime
     * The start time of the request execution.
     * @param endTime
     * The end time of the request execution.
     * @param responseStatus
     * The response status.
     * @param location
     * the destination or source for the export or import
     * @param users
     * the principals that initiated the request
     *
     * @return AuditLogEntry an audit log entry.
     */
    private AuditLogEntry createAuditLogEntry(AuditLogEventType eventType, Resource resource, Date startTime, Date endTime, Response.Status responseStatus,
            String location, String users) {
        final String METHODNAME = "createAuditLogEntry";
        log.entering(CLASSNAME, METHODNAME);

        String componentIp = componentIpHandler.getIpAddress();

        String tenantId = FHIRRequestContext.get().getTenantId();
        String timestamp = FHIRUtilities.formatTimestamp(new Date(System.currentTimeMillis()));

        AuditLogEntry entry = new AuditLogEntry(COMPONENT_ID, eventType.value(), timestamp, componentIp, tenantId);

        entry.setContext(new Context());

        // Start Time and End Time of the Event
        entry.getContext().setStartTime(FHIRUtilities.formatTimestamp(startTime));
        entry.getContext().setEndTime(FHIRUtilities.formatTimestamp(endTime));

        // Set the User List
        entry.setUserName(users);

        // Set the Batch Request as the Source or Destination
        entry.setLocation(location);

        // Uses the FHIRRestServletFilter to pass the OriginalRequestUri to the backend.
        entry.getContext().setApiParameters(ApiParameters.builder()
                                                .request(FHIRRequestContext.get().getOriginalRequestUri())
                                                .status(responseStatus.getStatusCode())
                                                .build());

        // For Export, the code assembles minimal data, and never includes the ORIGINAL data due to limitation in a
        // messaging platform.
        if (resource != null) {
            // Set the Resource Type
            entry.getContext().setData(Data.builder().resourceType(resource.getClass().getSimpleName()).build());

            // Set the ID for the given Resource
            if (resource.getId() != null) {
                entry.getContext().getData().setId(resource.getId());
            }

            // Set the Version ID for the Resource-ID
            if (resource.getMeta() != null && resource.getMeta().getVersionId() != null) {
                entry.getContext().getData().setVersionId(resource.getMeta().getVersionId().getValue());
            }
        }

        // Adds custom patient identifier to the auditLogEntry.
        String patientIdExtUrl = FHIRConfigHelper.getStringProperty(FHIRConfiguration.PROPERTY_AUDIT_PATIENT_ID_EXTURL, null);
        entry.setPatientId(FHIRUtil.getExtensionStringValue(resource, patientIdExtUrl));

        // Assigns the FHIRRequestContext a Unique ID.
        // This ThreadLocal id is valid per partition-and-execution.
        entry.getContext().setRequestUniqueId(FHIRRequestContext.get().getRequestUniqueId());

        log.exiting(CLASSNAME, METHODNAME);
        return entry;
    }

    /**
     * Builds an audit log entry for a 'create' in a bulkdata service invocation.
     *
     * @param newResource
     *            The Resource object being created.
     * @param startTime
     *            The start time of the create request execution.
     * @param endTime
     *            The end time of the create request execution.
     * @param responseStatus
     *            The response status.
     * @param location
     *            the destination or source for the export or import
     * @param users
     *            the principals that initiated the request
     * @throws Exception
     */
    public void logCreateOnImport(Resource newResource, Date startTime, Date endTime, Response.Status responseStatus, String location, String users)
            throws Exception {
        final String METHODNAME = "logCreateOnImport";
        log.entering(CLASSNAME, METHODNAME);
        if (shouldLog()) {
            log(AuditLogEventType.FHIR_CREATE, "C", "FHIR BulkData Create request", null, newResource, startTime, endTime, responseStatus, null, null, location, users);
        }
        log.exiting(CLASSNAME, METHODNAME);
    }

    /**
     * Builds an audit log entry for a 'validate' in a bulkdata service invocation.
     *
     * @param newResource
     *            The Resource object being created.
     * @param startTime
     *            The start time of the create request execution.
     * @param endTime
     *            The end time of the create request execution.
     * @param responseStatus
     *            The response status.
     * @param location
     *            the destination or source for the export or import
     * @param users
     *            the principals that initiated the request
     * @throws Exception
     */
    public void logValidateOnImport(Resource newResource, Date startTime, Date endTime, Response.Status responseStatus, String location, String users)
            throws Exception {
        final String METHODNAME = "logValidateOnImport";
        log.entering(CLASSNAME, METHODNAME);
        if (shouldLog()) {
            log(AuditLogEventType.FHIR_OPERATION, "R", "FHIR BulkData Validate on Import request", null, newResource, startTime, endTime, responseStatus, null, null, location, users);
        }
        log.exiting(CLASSNAME, METHODNAME);
    }

    /**
     * Builds an audit log entry for an 'update' in a bulkdata service invocation.
     *
     * @param oldResource
     *            The previous version of the Resource, before it was updated.
     * @param updatedResource
     *            The updated version of the Resource.
     * @param startTime
     *            The start time of the update request execution.
     * @param endTime
     *            The end time of the update request execution.
     * @param responseStatus
     *            The response status.
     * @param location
     *            the destination or source for the export or import
     * @param users
     *            the principals that initiated the request
     * @throws Exception
     */
    public void logUpdateOnImport(Resource oldResource, Resource updatedResource, Date startTime, Date endTime, Response.Status responseStatus, String location,
            String users) throws Exception {
        final String METHODNAME = "logUpdateOnImport";
        log.entering(CLASSNAME, METHODNAME);
        if (shouldLog()) {
            if (Response.Status.CREATED.equals(responseStatus)) {
                logCreateOnImport(updatedResource, startTime, endTime, responseStatus, location, users);
            } else {
                // Right now, we don't log or treat the oldResource. The signature is left for the commonality with the REST
                // Audit Logger.
                log(AuditLogEventType.FHIR_UPDATE, "U", "FHIR BulkData Update request", null, updatedResource, startTime, endTime, responseStatus, null, null, location, users);
            }
        }
        log.exiting(CLASSNAME, METHODNAME);
    }

    /**
     * Builds an audit log entry for an 'update' skipped in a bulkdata service invocation.
     *
     * @param resource
     *            The updated version of the Resource.
     * @param startTime
     *            The start time of the update request execution.
     * @param endTime
     *            The end time of the update request execution.
     * @param responseStatus
     *            The response status.
     * @param location
     *            the destination or source for the export or import
     * @param users
     *            the principals that initiated the request
     */
    public void logUpdateOnImportSkipped(Resource resource, Date startTime, Date endTime, Response.Status responseStatus, String location,
            String users) throws Exception {
        final String METHODNAME = "logUpdateOnImport";
        log.entering(CLASSNAME, METHODNAME);
        if (shouldLog()) {
            log(AuditLogEventType.FHIR_UPDATE, "U", "FHIR BulkData Update Resource Skipped request", null, resource, startTime, endTime, responseStatus, null, null, location, users);
        }
        log.exiting(CLASSNAME, METHODNAME);
    }

    /**
     * Builds an audit log entry for a 'read' in a bulkdata service invocation.
     *
     * @param resource
     *            The Resource object being read.
     * @param startTime
     *            The start time of the read request execution.
     * @param endTime
     *            The end time of the read request execution.
     * @param responseStatus
     *            The response status.
     * @param location
     *            the destination or source for the export or import
     * @param users
     *            the principals that initiated the request
     * @throws Exception
     */
    public void logReadOnExport(Resource resource, Date startTime, Date endTime, Response.Status responseStatus, String location, String users)
            throws Exception {
        final String METHODNAME = "logReadOnExport";
        log.entering(CLASSNAME, METHODNAME);
        if (shouldLog()) {
            // Right now, we don't log or treat the oldResource. The signature is left for the commonality with the REST
            // Audit Logger.
            log(AuditLogEventType.FHIR_READ, "R", "FHIR BulkData Read request", null, resource, startTime, endTime, responseStatus, null, null, location, users);
        }
        log.exiting(CLASSNAME, METHODNAME);
    }

    /**
     * Builds an audit log entry for a 'search' in a bulkdata service invocation.
     *
     * @param resource the resource
     * @param queryParms
     *            The query parameters passed to the search service.
     * @param totalSearch
     *            The total retrieved
     * @param startTime
     *            The start time of the bundle request execution.
     * @param endTime
     *            The end time of the bundle request execution.
     * @param responseStatus
     *            The response status.
     * @param location
     *            the destination or source for the export or import
     * @param users
     *            the principals that initiated the request
     * @throws Exception
     */
    public void logSearchOnExport(String resource, Map<String, List<String>> queryParms, int totalSearch, Date startTime, Date endTime, Response.Status responseStatus,
            String location, String users) throws Exception {
        final String METHODNAME = "logSearchOnExport";
        log.entering(CLASSNAME, METHODNAME);
        if (shouldLog()) {
            String queryString = null;
            if (queryParms != null && !queryParms.isEmpty()) {
                queryString = queryParms.toString();
            }

            // Right now, we don't log or treat the oldResource or newResource. The signature is left for the
            // commonality with the REST Audit Logger.
            log(AuditLogEventType.FHIR_SEARCH, "R", "FHIR BulkData Search request", null, null, startTime, endTime, responseStatus, queryString, (long) totalSearch, location, users, resource);
        }
        log.exiting(CLASSNAME, METHODNAME);
    }

    /**
     * Builds an audit log entry for a 'search' in a bulkdata service invocation.
     *
     * @param resource the resource
     * @param queryParm
     *            The query parameter passed to the search service.
     * @param totalSearch
     *            The total retrieved
     * @param startTime
     *            The start time of the bundle request execution.
     * @param endTime
     *            The end time of the bundle request execution.
     * @param responseStatus
     *            The response status.
     * @param location
     *            the destination or source for the export or import
     * @param users
     *            the principals that initiated the request
     * @throws Exception
     */
    public void logFastOnExport(String resource, String queryParm, int totalSearch, Date startTime, Date endTime, Response.Status responseStatus,
            String location, String users) throws Exception {
        final String METHODNAME = "logSearchOnExport";
        log.entering(CLASSNAME, METHODNAME);
        if (shouldLog()) {
            // Right now, we don't log or treat the oldResource or newResource. The signature is left for the
            // commonality with the REST Audit Logger.
            log(AuditLogEventType.FHIR_SEARCH, "R", "FHIR BulkData Fast request", null, null, startTime, endTime, responseStatus, queryParm, (long) totalSearch, location, users, resource);
        }
        log.exiting(CLASSNAME, METHODNAME);
    }
}
