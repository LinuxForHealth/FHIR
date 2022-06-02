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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.server.util.FHIRRestHelper;
import com.ibm.fhir.server.util.RestAuditLogger;

@Path("/")
@Produces({ FHIRMediaType.APPLICATION_FHIR_JSON, MediaType.APPLICATION_JSON,
        FHIRMediaType.APPLICATION_FHIR_XML, MediaType.APPLICATION_XML })
@RolesAllowed("FHIRUsers")
@RequestScoped
public class Search extends FHIRResource {
    private static final Logger log = java.util.logging.Logger.getLogger(Search.class.getName());

    public Search() throws Exception {
        super();
    }

    @GET
    @Path("{type}")
    public Response searchGet(@PathParam("type") String type) {
        return doSearch(type);
    }

    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Path("{type}/_search")
    public Response searchPost(@PathParam("type") String type) {
        return doSearch(type);
    }

    private Response doSearch(String type) {
        log.entering(this.getClass().getName(), "doSearch");
        Date startTime = new Date();
        Response.Status status = null;
        MultivaluedMap<String, String> queryParameters = null;
        Bundle bundle = null;

        try {
            checkInitComplete();
            checkType(type);

            queryParameters = uriInfo.getQueryParameters();
            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl(), getSearchHelper(), getFhirVersion());
            bundle = helper.doSearch(type, null, null, queryParameters, getRequestUri());

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

            log.exiting(this.getClass().getName(), "doSearch");
        }
    }

    @GET
    @Path("{compartment}/{compartmentId}/{type}")
    public Response searchCompartmentGet(@PathParam("compartment") String compartment,
            @PathParam("compartmentId") String compartmentId, @PathParam("type") String type) {
        return doSearchCompartment(compartment, compartmentId, type);
    }

    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Path("{compartment}/{compartmentId}/{type}/_search")
    public Response searchCompartmentPost(@PathParam("compartment") String compartment,
            @PathParam("compartmentId") String compartmentId, @PathParam("type") String type) {
        return doSearchCompartment(compartment, compartmentId, type);
    }

    private Response doSearchCompartment(String compartment, String compartmentId, String type) {
        log.entering(this.getClass().getName(), "doSearchCompartment");
        Date startTime = new Date();
        Response.Status status = null;
        MultivaluedMap<String, String> queryParameters = null;
        Bundle bundle = null;

        try {
            checkInitComplete();
            checkType(type);

            queryParameters = uriInfo.getQueryParameters();
            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl(), getSearchHelper(), getFhirVersion());
            bundle = helper.doSearch(type, compartment, compartmentId, queryParameters, getRequestUri());

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

            log.exiting(this.getClass().getName(), "doSearchCompartment");
        }
    }

    @GET
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
            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl(), getSearchHelper(), getFhirVersion());
            bundle = helper.doSearch("Resource", null, null, queryParameters, getRequestUri());

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
