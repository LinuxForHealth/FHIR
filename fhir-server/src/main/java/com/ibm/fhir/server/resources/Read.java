/*
 * (C) Copyright IBM Corp. 2016, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.resources;

import static com.ibm.fhir.server.util.IssueTypeToHttpStatusMapper.issueListToStatus;

import java.time.Instant;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.Resource;
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
public class Read extends FHIRResource {
    private static final Logger log = java.util.logging.Logger.getLogger(Read.class.getName());

    public Read() throws Exception {
        super();
    }

    @GET
    @Path("{type}/{id}")
    public Response read(@PathParam("type") String type, @PathParam("id") String id,
            @HeaderParam(HttpHeaders.IF_NONE_MATCH) String ifNoneMatch) throws Exception {
        log.entering(this.getClass().getName(), "read(String,String)");
        Date startTime = new Date();
        Response.Status status = null;
        FHIRRestOperationResponse ior = null;

        try {
            checkInitComplete();
            checkType(type);

            MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
            long modifiedSince = parseIfModifiedSince();

            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl(), getSearchHelper());
            Resource resource = helper.doRead(type, id, true, queryParameters).getResource();
            int version2Match = -1;
            // Support ETag value with or without " (and W/)
            // e.g:  1, "1", W/1, W/"1" (the first format is used by TouchStone)
            if (ifNoneMatch != null) {
                ifNoneMatch = ifNoneMatch.replaceAll("\"", "").replaceAll("W/", "").trim();
                if (!ifNoneMatch.isEmpty()) {
                    try {
                        version2Match = Integer.parseInt(ifNoneMatch);
                    }
                    catch (NumberFormatException e)
                    {
                        // ignore invalid version
                        version2Match = -1;
                    }
                }
            }
            Instant modifiedTime2Compare = null;
            if (modifiedSince > 0 ) {
                modifiedTime2Compare = Instant.ofEpochMilli(modifiedSince);
            }

            boolean isModified = true;
            // check if-not-match first
            if (version2Match != -1) {
                if (version2Match == Integer.parseInt(resource.getMeta().getVersionId().getValue())) {
                    isModified = false;
                }
            }
            // then check if-modified-since
            if(isModified && modifiedTime2Compare != null) {
                if (resource.getMeta().getLastUpdated().getValue().toInstant().isBefore(modifiedTime2Compare)) {
                    isModified = false;
                }
            }

            ResponseBuilder response;
            if (isModified) {
                status = Status.OK;
                response = Response.ok().entity(resource);
                response = addETagAndLastModifiedHeaders(response, resource);
            } else {
                status = Status.NOT_MODIFIED;
                response = Response.status(Response.Status.NOT_MODIFIED);
            }
            return response.build();
        } catch (FHIROperationException e) {
            status = issueListToStatus(e.getIssues());
            return exceptionResponse(e, status);
        } catch (Exception e) {
            status = Status.INTERNAL_SERVER_ERROR;
            return exceptionResponse(e, status);
        } finally {
            try {
                RestAuditLogger.logRead(httpServletRequest,
                        ior != null ? ior.getResource() : null,
                        startTime, new Date(), status);
            } catch (Exception e) {
                log.log(Level.SEVERE, AUDIT_LOGGING_ERR_MSG, e);
            }

            log.exiting(this.getClass().getName(), "read(String,String)");
        }
    }
}
