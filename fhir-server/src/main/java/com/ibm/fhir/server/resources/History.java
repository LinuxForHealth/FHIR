/*
 * (C) Copyright IBM Corp. 2016, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.resources;

import static com.ibm.fhir.server.util.IssueTypeToHttpStatusMapper.issueListToStatus;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.server.util.FHIRRestHelper;
import com.ibm.fhir.server.util.RestAuditLogger;

@Path("/")
@Consumes({ FHIRMediaType.APPLICATION_FHIR_JSON, MediaType.APPLICATION_JSON,
        FHIRMediaType.APPLICATION_FHIR_XML, MediaType.APPLICATION_XML })
@Produces({ FHIRMediaType.APPLICATION_FHIR_JSON, MediaType.APPLICATION_JSON,
        FHIRMediaType.APPLICATION_FHIR_XML, MediaType.APPLICATION_XML })
@RolesAllowed("FHIRUsers")
@RequestScoped
public class History extends FHIRResource {
    private static final Logger log = java.util.logging.Logger.getLogger(History.class.getName());

    public History() throws Exception {
        super();
    }

    @GET
    @Path("{type}/{id}/_history")
    public Response history(@PathParam("type") String type, @PathParam("id") String id) {
        log.entering(this.getClass().getName(), "history(String,String)");
        Date startTime = new Date();
        Response.Status status = null;
        Bundle bundle = null;

        try {
            checkInitComplete();
            checkType(type);

            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl(), getSearchHelper(), getFhirVersion());
            bundle = helper.doHistory(type, id, uriInfo.getQueryParameters(), getRequestUri());
            status = Status.OK;
            return Response.status(status).entity(bundle).build();
        } catch (FHIROperationException e) {
            status = issueListToStatus(e.getIssues());
            return exceptionResponse(e, status);
        } catch (Exception e) {
            status = Status.INTERNAL_SERVER_ERROR;
            return exceptionResponse(e, status);
        } finally {
            try {
                RestAuditLogger.logHistory(httpServletRequest, bundle,
                        startTime, new Date(), status);
            } catch (Exception e) {
                log.log(Level.SEVERE, AUDIT_LOGGING_ERR_MSG, e);
            }

            log.exiting(this.getClass().getName(), "history(String,String)");
        }
    }

    @GET
    @Path("_history")
    public Response systemHistory() {
        log.entering(this.getClass().getName(), "systemHistory()");
        Date startTime = new Date();
        Response.Status status = null;
        Bundle bundle = null;

        try {
            checkInitComplete();

            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl(), getSearchHelper(), getFhirVersion());
            bundle = helper.doHistory(uriInfo.getQueryParameters(), getRequestUri());
            status = Status.OK;
            return Response.status(status).entity(bundle).build();
        } catch (FHIROperationException e) {
            status = issueListToStatus(e.getIssues());
            return exceptionResponse(e, status);
        } catch (Exception e) {
            status = Status.INTERNAL_SERVER_ERROR;
            return exceptionResponse(e, status);
        } finally {
            try {
                RestAuditLogger.logHistory(httpServletRequest, bundle,
                        startTime, new Date(), status);
            } catch (Exception e) {
                log.log(Level.SEVERE, AUDIT_LOGGING_ERR_MSG, e);
            }

            log.exiting(this.getClass().getName(), "systemHistory()");
        }
    }

    @GET
    @Path("{type}/_history")
    public Response typeHistory(@PathParam("type") String type) {
        log.entering(this.getClass().getName(), "systemHistory(String)");
        Date startTime = new Date();
        Response.Status status = null;
        Bundle bundle = null;

        try {
            checkInitComplete();
            checkType(type);

            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl(), getSearchHelper(), getFhirVersion());
            bundle = helper.doHistory(uriInfo.getQueryParameters(), getRequestUri(), type);
            status = Status.OK;
            return Response.status(status).entity(bundle).build();
        } catch (FHIROperationException e) {
            status = issueListToStatus(e.getIssues());
            return exceptionResponse(e, status);
        } catch (Exception e) {
            status = Status.INTERNAL_SERVER_ERROR;
            return exceptionResponse(e, status);
        } finally {
            try {
                RestAuditLogger.logHistory(httpServletRequest, bundle,
                        startTime, new Date(), status);
            } catch (Exception e) {
                log.log(Level.SEVERE, AUDIT_LOGGING_ERR_MSG, e);
            }

            log.exiting(this.getClass().getName(), "systemHistory(String)");
        }
    }
}
