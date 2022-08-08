/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.operation.erase.audit;

import static org.linuxforhealth.fhir.model.type.String.string;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.linuxforhealth.fhir.audit.AuditLogEventType;
import org.linuxforhealth.fhir.audit.AuditLogService;
import org.linuxforhealth.fhir.audit.AuditLogServiceFactory;
import org.linuxforhealth.fhir.audit.beans.AuditLogEntry;
import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.model.resource.OperationOutcome.Issue;
import org.linuxforhealth.fhir.model.resource.Parameters;
import org.linuxforhealth.fhir.model.resource.Parameters.Parameter;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.type.code.IssueType;
import org.linuxforhealth.fhir.persistence.erase.EraseDTO;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationContext;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationUtil;
import org.linuxforhealth.fhir.server.util.RestAuditLogger;

/**
 * Generates Audit Log Messages for the Erase Operation
 */
public class EraseOperationAuditLogger extends RestAuditLogger {

    private static final String CLASSNAME = EraseOperationAuditLogger.class.getName();
    private static final Logger log = java.util.logging.Logger.getLogger(CLASSNAME);

    // The start time of the read request execution.
    private Date startTime = new Date();

    // The HttpServletRequest representation of the REST request.
    private HttpServletRequest httpServletRequest = null;

    /**
     * Creates an audit logger for Erase Operation
     * @param operationContext
     */
    public EraseOperationAuditLogger(FHIROperationContext operationContext) {
        httpServletRequest = (HttpServletRequest) operationContext.getProperty(FHIROperationContext.PROPNAME_HTTP_REQUEST);
    }

    /**
     * runs the audit over the returned records
     *
     * @param response
     * @param eraseDto
     * @throws FHIROperationException
     */
    public void audit(Parameters response, EraseDTO eraseDto) throws FHIROperationException {
        logEraseOperation(getAuditLogService(), response, Response.Status.OK, eraseDto.getReason(), eraseDto.getPatient());
    }

    /**
     * runs the audit when there is an error in a partial erase.
     *
     * @param request
     * @param eraseDto
     * @throws FHIROperationException
     */
    public void error(Parameters request, FHIROperationException e, EraseDTO eraseDto) throws FHIROperationException {
        String reference = eraseDto.generateReference();

        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name(string("resource")).value(string(reference)).build());
        parameters.add(Parameter.builder().name(string("partial")).value(org.linuxforhealth.fhir.model.type.Boolean.TRUE).build());

        if (e.getIssues() != null && !e.getIssues().isEmpty()) {
            int i = 0;
            for (Issue issue : e.getIssues()) {
                if (issue.getDetails() != null && issue.getDetails().getText() != null) {
                    String details = issue.getDetails().getText().getValue();
                    parameters.add(Parameter.builder().name(string("issue" + i++)).value(string(details)).build());
                }
            }
        }

        logEraseOperation(getAuditLogService(),
                        request.toBuilder()
                                   .parameter(parameters)
                                   .build(),
                        Response.Status.INTERNAL_SERVER_ERROR,
                        eraseDto.getReason(),
                        eraseDto.getPatient());
    }

    /**
     * returns the audit log service
     * @return
     */
    private AuditLogService getAuditLogService() {
        return AuditLogServiceFactory.getService();
    }

    /**
     * Build and Submit an audit log entry for a '$erase' REST service invocation.
     *
     * @param auditLogSvc
     * @param resource
     *  The Resource object being read.
     * @param responseStatus
     *  The response status.
     * @param reason the reason
     * @param patient the patient it's related
     * @throws Exception
     */
    public void logEraseOperation(AuditLogService auditLogSvc, Resource resource, Response.Status responseStatus, String reason, String patient) throws FHIROperationException {
        /*
         * @implNote intent is that we can check auditLogSvc behavior when it is enabled
         */
        final String METHODNAME = "logEraseOperation";
        log.entering(CLASSNAME, METHODNAME);

        // The end time of the read request execution.
        Date endTime = new Date();

        if (auditLogSvc.isEnabled()) {
            AuditLogEntry entry = initLogEntry(AuditLogEventType.FHIR_OPERATION);
            populateAuditLogEntry(entry, httpServletRequest, resource, startTime, endTime, responseStatus);

            // The erase is always a Delete, which is "D"
            entry.getContext().setAction("D");

            StringBuilder builder = new StringBuilder("FHIR Hard Delete ($erase) request");
            builder.append(" for reason '")
                    .append(reason)
                    .append("'");

            // Only if the patient exists, let's add it to the description
            if (patient != null) {
                builder.append(" for patient '")
                    .append(patient)
                    .append("'");
            }

            entry.setDescription(builder.toString());

            try {
                auditLogSvc.logEntry(entry);
            } catch (Exception e) {
                // We wrap it so we can use the Operations framework.
                throw FHIROperationUtil.buildExceptionWithIssue("Error while logging entry", IssueType.EXCEPTION, e);
            }
        }
        log.exiting(CLASSNAME, METHODNAME);
    }
}