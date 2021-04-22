/*
 * (C) Copyright IBM Corp. 2016, 2020
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
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.jwt.JsonWebToken;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.core.FHIRConstants;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.core.HTTPReturnPreference;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.persistence.exception.FHIRPersistenceResourceNotFoundException;
import com.ibm.fhir.server.operation.spi.FHIRRestOperationResponse;
import com.ibm.fhir.server.util.FHIRRestHelper;
import com.ibm.fhir.server.util.RestAuditLogger;

@Path("/")
@Consumes({ FHIRMediaType.APPLICATION_FHIR_JSON, MediaType.APPLICATION_JSON,
        FHIRMediaType.APPLICATION_FHIR_XML, MediaType.APPLICATION_XML })
@Produces({ FHIRMediaType.APPLICATION_FHIR_JSON, MediaType.APPLICATION_JSON,
        FHIRMediaType.APPLICATION_FHIR_XML, MediaType.APPLICATION_XML })
@RolesAllowed("FHIRUsers")
@RequestScoped
public class Update extends FHIRResource {
    private static final Logger log = java.util.logging.Logger.getLogger(Update.class.getName());

    // The JWT of the current caller. Since this is a request scoped resource, the
    // JWT will be injected for each JAX-RS request. The injection is performed by
    // the mpJwt feature.
    @Inject
    private JsonWebToken jwt;

    public Update() throws Exception {
        super();
    }

    @PUT
    @Path("{type}/{id}")
    public Response update(@PathParam("type") String type, @PathParam("id") String id, Resource resource,
            @HeaderParam(HttpHeaders.IF_MATCH) String ifMatch, @HeaderParam(FHIRConstants.UPDATE_IF_MODIFIED_HEADER) boolean onlyIfModified) {
        log.entering(this.getClass().getName(), "update(String,String,Resource)");
        Date startTime = new Date();
        Response.Status status = null;
        FHIRRestOperationResponse ior = null;

        try {
            checkInitComplete();

            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl());
            ior = helper.doUpdate(type, id, resource, ifMatch, null, null, onlyIfModified);

            ResponseBuilder response =
                    Response.ok().location(toUri(getAbsoluteUri(getRequestBaseUri(type), ior.getLocationURI().toString())));
            status = ior.getStatus();
            response.status(status);

            Resource updatedResource = ior.getResource();
            HTTPReturnPreference returnPreference = FHIRRequestContext.get().getReturnPreference();
            if (updatedResource != null && HTTPReturnPreference.REPRESENTATION == returnPreference) {
                response.entity(updatedResource);
            } else if (ior.getOperationOutcome() != null && HTTPReturnPreference.OPERATION_OUTCOME == returnPreference) {
                response.entity(ior.getOperationOutcome());
            }
            response = addHeaders(response, updatedResource);
            return response.build();
        } catch (FHIRPersistenceResourceNotFoundException e) {
            // By default, NOT_FOUND is mapped to HTTP 404, so explicitly set it to HTTP 405
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
                RestAuditLogger.logUpdate(httpServletRequest,
                        ior != null ? ior.getPrevResource() : null,
                        ior != null ? ior.getResource() : null,
                        startTime, new Date(), status);
            } catch (Exception e) {
                log.log(Level.SEVERE, AUDIT_LOGGING_ERR_MSG, e);
            }

            log.exiting(this.getClass().getName(), "update(String,String,Resource)");
        }
    }

    @PUT
    @Path("{type}")
    public Response conditionalUpdate(@PathParam("type") String type, Resource resource, @HeaderParam(HttpHeaders.IF_MATCH) String ifMatch,
            @HeaderParam(FHIRConstants.UPDATE_IF_MODIFIED_HEADER) boolean onlyIfModified) {
        log.entering(this.getClass().getName(), "conditionalUpdate(String,Resource)");
        Date startTime = new Date();
        Response.Status status = null;
        FHIRRestOperationResponse ior = null;

        try {
            checkInitComplete();

            String searchQueryString = httpServletRequest.getQueryString();
            if (searchQueryString == null || searchQueryString.isEmpty()) {
                String msg =
                        "Cannot PUT to resource type endpoint unless a search query string is provided for a conditional update.";
                throw buildRestException(msg, IssueType.INVALID);
            }

            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl());
            ior = helper.doUpdate(type, null, resource, ifMatch, searchQueryString, null, onlyIfModified);

            ResponseBuilder response =
                    Response.ok().location(toUri(getAbsoluteUri(getRequestBaseUri(type), ior.getLocationURI().toString())));
            status = ior.getStatus();
            response.status(status);

            Resource updatedResource = ior.getResource();
            if (updatedResource != null && HTTPReturnPreference.REPRESENTATION == FHIRRequestContext.get().getReturnPreference()) {
                response.entity(updatedResource);
            } else if (ior.getOperationOutcome() != null && HTTPReturnPreference.OPERATION_OUTCOME == FHIRRequestContext.get().getReturnPreference()) {
                response.entity(ior.getOperationOutcome());
            }
            response = addHeaders(response, updatedResource);

            return response.build();
        } catch (FHIRPersistenceResourceNotFoundException e) {
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
                RestAuditLogger.logUpdate(httpServletRequest,
                        ior != null ? ior.getPrevResource() : null,
                        ior != null ? ior.getResource() : null,
                        startTime, new Date(), status);
            } catch (Exception e) {
                log.log(Level.SEVERE, AUDIT_LOGGING_ERR_MSG, e);
            }

            log.exiting(this.getClass().getName(), "conditionalUpdate(String,Resource)");
        }
    }
}
