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
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.persistence.exception.FHIRPersistenceNotSupportedException;
import com.ibm.fhir.server.spi.operation.FHIRRestOperationResponse;
import com.ibm.fhir.server.util.FHIRRestHelper;
import com.ibm.fhir.server.util.RestAuditLogger;

@Path("/")
@Consumes({ FHIRMediaType.APPLICATION_FHIR_JSON, MediaType.APPLICATION_JSON,
        FHIRMediaType.APPLICATION_FHIR_XML, MediaType.APPLICATION_XML })
@Produces({ FHIRMediaType.APPLICATION_FHIR_JSON, MediaType.APPLICATION_JSON,
        FHIRMediaType.APPLICATION_FHIR_XML, MediaType.APPLICATION_XML })
@RolesAllowed("FHIRUsers")
@RequestScoped
public class Delete extends FHIRResource {
    private static final Logger log = java.util.logging.Logger.getLogger(Delete.class.getName());

    public Delete() throws Exception {
        super();
    }

    @DELETE
    @Path("{type}/{id}")
    public Response delete(@PathParam("type") String type, @PathParam("id") String id) throws Exception {
        log.entering(this.getClass().getName(), "delete(String,String)");
        Date startTime = new Date();
        Response.Status status = null;
        FHIRRestOperationResponse ior = null;

        try {
            checkInitComplete();
            checkType(type);

            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl(), getFhirVersion());
            ior = helper.doDelete(type, id, null);
            status = ior.getStatus();
            return buildResponse(ior);
        } catch (FHIRPersistenceNotSupportedException e) {
            status = Status.METHOD_NOT_ALLOWED;
            return exceptionResponse(e, status);
        } catch (FHIROperationException e) {
            status = issueListToStatus(e.getIssues());
            return exceptionResponse(e, status);
        } catch (Exception e) {
            status = Status.INTERNAL_SERVER_ERROR;
            return exceptionResponse(e, status);
        } finally {
            try {
                RestAuditLogger.logDelete(httpServletRequest,
                        ior != null ? ior.getResource() : null,
                        startTime, new Date(), status);
            } catch (Exception e) {
                log.log(Level.SEVERE, AUDIT_LOGGING_ERR_MSG, e);
            }

            log.exiting(this.getClass().getName(), "delete(String,String)");
        }
    }

    @DELETE
    @Path("{type}")
    public Response conditionalDelete(@PathParam("type") String type) throws Exception {
        log.entering(this.getClass().getName(), "conditionalDelete(String)");
        Date startTime = new Date();
        Response.Status status = null;
        FHIRRestOperationResponse ior = null;

        try {
            checkInitComplete();
            checkType(type);

            String searchQueryString = httpServletRequest.getQueryString();
            if (searchQueryString == null || searchQueryString.isEmpty()) {
                String msg =
                        "A search query string is required for a conditional delete operation.";
                throw buildRestException(msg, IssueType.INVALID);
            }

            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl(), getFhirVersion());
            ior = helper.doDelete(type, null, searchQueryString);
            status = ior.getStatus();
            return buildResponse(ior);
        } catch (FHIRPersistenceNotSupportedException e) {
            status = Status.METHOD_NOT_ALLOWED;
            return exceptionResponse(e, status);
        } catch (FHIROperationException e) {
            status = issueListToStatus(e.getIssues());
            return exceptionResponse(e, status);
        } catch (Exception e) {
            status = Status.INTERNAL_SERVER_ERROR;
            return exceptionResponse(e, status);
        } finally {
            try {
                RestAuditLogger.logDelete(httpServletRequest,
                        ior != null ? ior.getResource() : null,
                        startTime, new Date(), status);
            } catch (Exception e) {
                log.log(Level.SEVERE, AUDIT_LOGGING_ERR_MSG, e);
            }

            log.exiting(this.getClass().getName(), "conditionalDelete(String)");
        }
    }

    private Response buildResponse(FHIRRestOperationResponse ior) {
        ResponseBuilder response = Response.status(ior.getStatus());

        if (ior.getOperationOutcome() != null) {
            response.entity(ior.getOperationOutcome());
        }

        if (ior.getResource() != null) {
            addHeaders(response, ior.getResource());
        } else if (ior.getVersionForETag() > 0) {
            addHeaders(response, ior.getVersionForETag());
        }
        return response.build();
    }
}
