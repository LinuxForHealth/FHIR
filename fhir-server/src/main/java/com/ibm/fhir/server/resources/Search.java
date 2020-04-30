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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.jwt.JsonWebToken;

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
public class Search extends FHIRResource {
    private static final Logger log = java.util.logging.Logger.getLogger(Search.class.getName());

    // The JWT of the current caller. Since this is a request scoped resource, the
    // JWT will be injected for each JAX-RS request. The injection is performed by
    // the mpJwt feature.
    @Inject
    private JsonWebToken jwt;

    public Search() throws Exception {
        super();
    }

    @GET
    @Path("{type}")
    public Response search(@PathParam("type") String type) {
        log.entering(this.getClass().getName(), "search(String)");
        Date startTime = new Date();
        Response.Status status = null;
        MultivaluedMap<String, String> queryParameters = null;
        Bundle bundle = null;

        try {
            checkInitComplete();

            queryParameters = uriInfo.getQueryParameters();
            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl());
            bundle = helper.doSearch(type, null, null, queryParameters, getRequestUri(), null, null);
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
                RestAuditLogger.logSearch(httpServletRequest, queryParameters, bundle,
                        startTime, new Date(), status);
            } catch (Exception e) {
                log.log(Level.SEVERE, AUDIT_LOGGING_ERR_MSG, e);
            }

            log.exiting(this.getClass().getName(), "search(String)");
        }
    }

    @GET
    @Path("{compartment}/{compartmentId}/{type}")
    public Response searchCompartment(@PathParam("compartment") String compartment,
            @PathParam("compartmentId") String compartmentId, @PathParam("type") String type) {
        log.entering(this.getClass().getName(), "search(String,String,String)");
        Date startTime = new Date();
        Response.Status status = null;
        MultivaluedMap<String, String> queryParameters = null;
        Bundle bundle = null;

        try {
            checkInitComplete();

            queryParameters = uriInfo.getQueryParameters();
            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl());
            bundle = helper.doSearch(type, compartment, compartmentId, queryParameters, getRequestUri(), null, null);
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
                RestAuditLogger.logSearch(httpServletRequest, queryParameters, bundle,
                        startTime, new Date(), status);
            } catch (Exception e) {
                log.log(Level.SEVERE, AUDIT_LOGGING_ERR_MSG, e);
            }

            log.exiting(this.getClass().getName(), "search(String,String,String)");
        }
    }

    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Path("{type}/_search")
    public Response _search(@PathParam("type") String type) {
        log.entering(this.getClass().getName(), "_search(String)");
        Date startTime = new Date();
        Response.Status status = null;
        MultivaluedMap<String, String> queryParameters = null;
        Bundle bundle = null;

        try {
            checkInitComplete();

            queryParameters = uriInfo.getQueryParameters();
            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl());
            bundle = helper.doSearch(type, null, null, queryParameters, getRequestUri(), null, null);
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
                RestAuditLogger.logSearch(httpServletRequest, queryParameters, bundle,
                        startTime, new Date(), status);
            } catch (Exception e) {
                log.log(Level.SEVERE, AUDIT_LOGGING_ERR_MSG, e);
            }

            log.exiting(this.getClass().getName(), "_search(String)");
        }
    }

    @GET
    @Path("/")
    public Response searchAllGet() {
        return doSearchAll();
    }

    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Path("_search")
    public Response searchAllPost() {
        return doSearchAll();
    }

    private Response doSearchAll() {
        log.entering(this.getClass().getName(), "doSearchAll");
        Date startTime = new Date();
        Response.Status status = null;
        MultivaluedMap<String, String> queryParameters = null;
        Bundle bundle = null;

        try {
            checkInitComplete();

            queryParameters = uriInfo.getQueryParameters();
            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl());
            bundle = helper.doSearch("Resource", null, null, queryParameters, getRequestUri(), null, null);
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
                RestAuditLogger.logSearch(httpServletRequest, queryParameters, bundle,
                        startTime, new Date(), status);
            } catch (Exception e) {
                log.log(Level.SEVERE, AUDIT_LOGGING_ERR_MSG, e);
            }

            log.exiting(this.getClass().getName(), "doSearchAll");
        }
    }
}
