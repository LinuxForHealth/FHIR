/*
 * (C) Copyright IBM Corp. 2016, 2021
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

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.core.FHIRConstants;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.core.HTTPReturnPreference;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.persistence.exception.FHIRPersistenceIfNoneMatchException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceResourceNotFoundException;
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
public class Update extends FHIRResource {
    private static final Logger log = java.util.logging.Logger.getLogger(Update.class.getName());
    private static final Integer IF_NONE_MATCH_ZERO = Integer.valueOf(0);
    private static final Integer IF_NONE_MATCH_NULL = null;

    public Update() throws Exception {
        super();
    }

    @PUT
    @Path("{type}/{id}")
    public Response update(@PathParam("type") String type, @PathParam("id") String id, Resource resource,
            @HeaderParam(HttpHeaders.IF_MATCH) String ifMatch,
            @HeaderParam(FHIRConstants.UPDATE_IF_MODIFIED_HEADER) boolean onlyIfModified,
            @HeaderParam(HttpHeaders.IF_NONE_MATCH) String ifNoneMatchHdr) {
        log.entering(this.getClass().getName(), "update(String,String,Resource)");
        Date startTime = new Date();
        Response.Status status = null;
        FHIRRestOperationResponse ior = null;

        try {
            checkInitComplete();
            checkType(type);
            Integer ifNoneMatch = encodeIfNoneMatch(ifNoneMatchHdr);

            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl());
            ior = helper.doUpdate(type, id, resource, ifMatch, null, onlyIfModified, ifNoneMatch);

            ResponseBuilder response = Response.ok()
                    .location(toUri(getAbsoluteUri(getRequestBaseUri(type), ior.getLocationURI().toString())));
            status = ior.getStatus();
            response.status(status);

            Resource updatedResource = ior.getResource();
            HTTPReturnPreference returnPreference = FHIRRequestContext.get().getReturnPreference();
            if (updatedResource != null && HTTPReturnPreference.REPRESENTATION == returnPreference) {
                response.entity(updatedResource);
            } else if (ior.getOperationOutcome() != null && HTTPReturnPreference.OPERATION_OUTCOME == returnPreference) {
                response.entity(ior.getOperationOutcome());
            }

            if (updatedResource != null) {
                response = addHeaders(response, updatedResource);
            } else {
                // For IfNoneMatch returning a 304 Not Modified, we may not have the current resource,
                // but we can set the response headers we need using the location URI
                response = addHeaders(response, ior.getLocationURI());
            }
            return response.build();
        } catch (FHIRPersistenceResourceNotFoundException e) {
            // By default, NOT_FOUND is mapped to HTTP 404, so explicitly set it to HTTP 405
            status = Status.METHOD_NOT_ALLOWED;
            return exceptionResponse(e, status);
        } catch (FHIRPersistenceIfNoneMatchException e) {
            // IfNoneMatch being treated as an error (default behavior)
            status = Status.PRECONDITION_FAILED;
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
            checkType(type);

            String searchQueryString = httpServletRequest.getQueryString();
            if (searchQueryString == null || searchQueryString.isEmpty()) {
                String msg =
                        "Cannot PUT to resource type endpoint unless a search query string is provided for a conditional update.";
                throw buildRestException(msg, IssueType.INVALID);
            }

            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl());
            ior = helper.doUpdate(type, null, resource, ifMatch, searchQueryString, onlyIfModified, IF_NONE_MATCH_NULL);

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

    /**
     * Encode the header value to a simple integer which makes it a lot easier
     * and safer to pass down to the persistence layer.
     * @param value
     * @return
     */
    private Integer encodeIfNoneMatch(String value) throws FHIROperationException {
        if (value == null || value.isEmpty()) {
            return null;
        }

        if ("*".equals(value)) {
            // FHIR resource version numbers start at 1, so we use 0 to represent
            // all values "*"
            return IF_NONE_MATCH_ZERO;
        }

        // Values of the form W/"1" where 1 is the version number would result
        // in less-than-intuitive behavior and so are not supported at this time
        if (value.length() > 4 && value.startsWith("W/\"") && value.endsWith("\"")) {
            // the bit in the middle should be an integer
            value = value.substring(3, value.length()-1);
            try {
                Integer.parseInt(value); // just check it's an int for now
            } catch (NumberFormatException x) {
                throw buildRestException("Invalid If-None-Match value", IssueType.INVALID);
            }
            throw buildRestException("If-None-Match with specific version not supported", IssueType.INVALID);
        } else {
            throw buildRestException("Invalid If-None-Match value", IssueType.INVALID);
        }
    }
}