/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.jbatch.bulkdata.audit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;

import com.ibm.fhir.audit.logging.api.AuditLogService;
import com.ibm.fhir.audit.logging.api.AuditLogServiceFactory;
import com.ibm.fhir.audit.logging.beans.ApiParameters;
import com.ibm.fhir.audit.logging.beans.AuditLogEntry;
import com.ibm.fhir.audit.logging.beans.AuditLogEventType;
import com.ibm.fhir.audit.logging.beans.Batch;
import com.ibm.fhir.audit.logging.beans.Context;
import com.ibm.fhir.audit.logging.beans.Data;
import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.core.FHIRUtilities;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.operation.bulkdata.model.type.JobParameter;

/**
 * This class provides convenience methods for IBM FHIR Server
 * Bulk Data services that need to write FHIR audit log entries.
 */
public class BulkDataAuditLogger {

    private static final String CLASSNAME = BulkDataAuditLogger.class.getName();
    private static final Logger log = java.util.logging.Logger.getLogger(CLASSNAME);

    private static final BulkDataAuditLogger auditLogger = new BulkDataAuditLogger();

    private BulkDataAuditLogger() {
        // No Operation
    }

    /**
     * The single instance of the bulkdata audit logger
     *
     * @return the logger
     */
    public static BulkDataAuditLogger getInstance() {
        return auditLogger;
    }

    /**
     * Builds an audit log entry for a Bulk Data 'import' operation
     *
     * @param jobParameter
     *            - the job parameters
     * @param resource
     *            - The resource that is created and update on the system.
     * @param startTime
     *            - The start time of the update request execution.
     * @param endTime
     *            - The end time of the update request execution.
     * @param responseStatus
     *            - the response status.
     * @throws Exception
     */
    public void logBulkDataImport(JobParameter jobParameter, Resource resource, Date startTime, Date endTime, Response.Status responseStatus)
        throws Exception {
        final String METHODNAME = "logBulkDataImport";
        log.entering(CLASSNAME, METHODNAME);

        AuditLogService auditLogSvc = AuditLogServiceFactory.getService();
        AuditLogEntry entry = initLogEntry(AuditLogEventType.FHIR_OPERATION_BULKDATA_IMPORT);
        populateAuditLogEntry(entry, jobParameter, resource, startTime, endTime, responseStatus);

        entry.getContext().setAction(AuditLogEventType.FHIR_OPERATION_BULKDATA_IMPORT.value());
        entry.setDescription("BulkData - FHIR Create or Update request");

        auditLogSvc.logEntry(entry);
        log.exiting(CLASSNAME, METHODNAME);
    }

    /**
     * Builds an audit log entry for a Bulk Data 'export' operation
     *
     * @param jobParameter
     *            - the job parameters
     * @param total
     *            - the total number of resources exported
     * @param startTime
     *            - The start time of the update request execution.
     * @param endTime
     *            - The end time of the update request execution.
     * @param responseStatus
     *            - the response status.
     * @throws Exception
     */
    public void logBulkDataExport(JobParameter jobParameter, Long total, Date startTime, Date endTime, Response.Status responseStatus) throws Exception {
        final String METHODNAME = "logBulkDataExport";
        log.entering(CLASSNAME, METHODNAME);

        AuditLogService auditLogSvc = AuditLogServiceFactory.getService();
        AuditLogEntry entry = initLogEntry(AuditLogEventType.FHIR_OPERATION_BULKDATA_EXPORT);
        populateAuditLogEntry(entry, jobParameter, null, startTime, endTime, responseStatus);
        long totalExport = (total != null) ? total : 0;

        entry.getContext().setBatch(Batch.builder().resourcesRead(totalExport).build());
        entry.getContext().setAction(AuditLogEventType.FHIR_OPERATION_BULKDATA_EXPORT.value());
        entry.setDescription("BulkData - FHIR Export - Job");

        auditLogSvc.logEntry(entry);
        log.exiting(CLASSNAME, METHODNAME);
    }

    /**
     * Builds an audit log entry for a Bulk Data search as part of the operation
     *
     * @param jobParameter
     *            - the job parameters
     * @param properties
     *            - the properties related to this search
     * @param resource
     *            - The resource that is created and update on the system.
     * @param startTime
     *            - The start time of the update request execution.
     * @param endTime
     *            - The end time of the update request execution.
     * @param responseStatus
     *            - the response status.
     * @throws Exception
     */
    public void logBulkDataSearch(JobParameter jobParameter, Bundle bundle, Date startTime, Date endTime,
        Response.Status responseStatus) throws Exception {
        final String METHODNAME = "logBulkDataSearch";
        log.entering(CLASSNAME, METHODNAME);

        AuditLogService auditLogSvc = AuditLogServiceFactory.getService();
        AuditLogEntry entry = initLogEntry(AuditLogEventType.FHIR_OPERATION_BULKDATA_SEARCH);
        populateAuditLogEntry(entry, jobParameter, null, startTime, endTime, responseStatus);
        long totalSearch = 0;

        // Log the Job Parameters that are used in the Search


        // Log the returned _total in the response
        if (bundle.getTotal() != null) {
            totalSearch = bundle.getTotal().getValue().longValue();
        }
        entry.getContext().setBatch(Batch.builder().resourcesRead(totalSearch).build());

        //
        entry.getContext().setAction(AuditLogEventType.FHIR_OPERATION_BULKDATA_SEARCH.value());
        entry.setDescription("BulkData - FHIR Search request");

        auditLogSvc.logEntry(entry);
        log.exiting(CLASSNAME, METHODNAME);
    }

    /*
     * Populates the passed audit log entry, with attributes common to all REST services.
     * @param entry
     * - The AuditLogEntry to be populated.
     * @param request
     * - The HttpServletRequest representation of the REST request.
     * @param resource
     * - The Resource object.
     * @param startTime
     * - The start time of the request execution.
     * @param endTime
     * - The end time of the request execution.
     * @param responseStatus
     * - The response status.
     * @return AuditLogEntry - an initialized audit log entry.
     */
    private AuditLogEntry populateAuditLogEntry(
        AuditLogEntry entry, JobParameter jobParameter, Resource resource,
        Date startTime, Date endTime, Response.Status responseStatus) {
        final String METHODNAME = "populateAuditLogEntry";
        log.entering(CLASSNAME, METHODNAME);

        StringBuffer requestUrl;
        String patientIdExtUrl;
        List<String> userList = new ArrayList<>();

        entry.setLocation(new StringBuilder().append(request.getRemoteAddr()).append("/").append(request.getRemoteHost()).toString());
        entry.setContext(new Context());
        requestUrl = request.getRequestURL();
        if (request.getQueryString() != null) {
            requestUrl.append("?");
            requestUrl.append(request.getQueryString());
        }
        entry.getContext().setApiParameters(ApiParameters.builder().request(requestUrl.toString()).status(responseStatus.getStatusCode()).build());
        entry.getContext().setStartTime(FHIRUtilities.formatTimestamp(startTime));
        entry.getContext().setEndTime(FHIRUtilities.formatTimestamp(endTime));
        if (resource != null) {
            entry.getContext().setData(Data.builder().resourceType(resource.getClass().getSimpleName()).build());
            if (resource.getId() != null) {
                entry.getContext().getData().setId(resource.getId());
            }
            if (resource.getMeta() != null && resource.getMeta().getVersionId() != null) {
                entry.getContext().getData().setVersionId(resource.getMeta().getVersionId().getValue());
            }
        }

        // Get Client Cert CN as on Behalf of
        // Get Client Cert Issuer OU as on behalf ofc
        entry.setClientCertCn(request.getHeader(HEADER_CLIENT_CERT_CN));
        entry.setClientCertIssuerOu(request.getHeader(HEADER_CLIENT_CERT_ISSUER_OU));
        entry.setCorrelationId(request.getHeader(HEADER_CORRELATION_ID));

        patientIdExtUrl = FHIRConfigHelper.getStringProperty(FHIRConfiguration.PROPERTY_AUDIT_PATIENT_ID_EXTURL, null);
        entry.setPatientId(FHIRUtil.getExtensionStringValue(resource, patientIdExtUrl));
        entry.getContext().setRequestUniqueId(FHIRRequestContext.get().getRequestUniqueId());

        log.exiting(CLASSNAME, METHODNAME);
        return entry;
    }

    /*
     * Builds and returns an AuditLogEntry with the minimum required fields populated.
     * @param eventType
     * - A valid type of audit log event
     * @return AuditLogEntry with the minimum required fields populated.
     */
    private AuditLogEntry initLogEntry(AuditLogEventType eventType) {
        final String METHODNAME = "initLogEntry";
        log.entering(CLASSNAME, METHODNAME);

        String timestamp;
        String componentIp = null;
        AuditLogEntry logEntry;
        String tenantId;

        tenantId = FHIRRequestContext.get().getTenantId();
        timestamp = FHIRUtilities.formatTimestamp(new Date(System.currentTimeMillis()));

        // TODO: Get the Component ID from the FHIR Operation

        logEntry = new AuditLogEntry("fhir-server-bulk-data", eventType.value(), timestamp, componentIp, tenantId);
        log.exiting(CLASSNAME, METHODNAME);
        return logEntry;
    }
}