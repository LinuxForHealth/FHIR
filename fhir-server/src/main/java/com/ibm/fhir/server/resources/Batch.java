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
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.core.FHIRConstants;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.server.exception.FHIRRestBundledRequestException;
import com.ibm.fhir.server.spi.operation.FHIROperationContext;
import com.ibm.fhir.server.util.FHIRRestHelper;
import com.ibm.fhir.server.util.RestAuditLogger;

@Path("/")
@Consumes({ FHIRMediaType.APPLICATION_FHIR_JSON, MediaType.APPLICATION_JSON,
        FHIRMediaType.APPLICATION_FHIR_XML, MediaType.APPLICATION_XML })
@Produces({ FHIRMediaType.APPLICATION_FHIR_JSON, MediaType.APPLICATION_JSON,
        FHIRMediaType.APPLICATION_FHIR_XML, MediaType.APPLICATION_XML })
@RolesAllowed({"FHIRUsers", "FHIROperationAdmin"})
@RequestScoped
public class Batch extends FHIRResource {
    private static final Logger log = java.util.logging.Logger.getLogger(Batch.class.getName());

    @Context
    protected HttpHeaders httpHeaders;

    @Context
    protected SecurityContext securityContext;

    public Batch() throws Exception {
        super();
    }

    @POST
    public Response bundle(Resource resource, @HeaderParam(FHIRConstants.UPDATE_IF_MODIFIED_HEADER) boolean updateOnlyIfModified) {
        log.entering(this.getClass().getName(), "bundle(Bundle)");
        Date startTime = new Date();
        Response.Status status = null;
        Bundle responseBundle = null;

        // Sets up the requestContext with extended properties.
        FHIRRequestContext requestContext = FHIRRequestContext.get();
        requestContext.setExtendedOperationProperties(FHIROperationContext.PROPNAME_URI_INFO, uriInfo);
        requestContext.setExtendedOperationProperties(FHIROperationContext.PROPNAME_HTTP_HEADERS, httpHeaders);
        requestContext.setExtendedOperationProperties(FHIROperationContext.PROPNAME_METHOD_TYPE, "POST" );
        requestContext.setExtendedOperationProperties(FHIROperationContext.PROPNAME_SECURITY_CONTEXT, securityContext);
        requestContext.setExtendedOperationProperties(FHIROperationContext.PROPNAME_HTTP_REQUEST, httpServletRequest);

        try {
            checkInitComplete();

            Bundle inputBundle = null;
            if (resource instanceof Bundle) {
                inputBundle = (Bundle) resource;
            } else {
                String msg = "A 'Bundle' resource type is required but a '"
                        + resource.getClass().getSimpleName() + "' resource type was sent.";
                throw buildRestException(msg, IssueType.INVALID);
            }

            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl(), getSearchHelper(), getFhirVersion());
            responseBundle = helper.doBundle(inputBundle, updateOnlyIfModified);
            status = Status.OK;
            return Response.ok(responseBundle).build();
        } catch (FHIRRestBundledRequestException e) {
            Response exceptionResponse = exceptionResponse(e);
            status = Response.Status.fromStatusCode(exceptionResponse.getStatus());
            return exceptionResponse;
        } catch (FHIROperationException e) {
            status = issueListToStatus(e.getIssues());
            return exceptionResponse(e, status);
        } catch (Exception e) {
            status = Status.INTERNAL_SERVER_ERROR;
            return exceptionResponse(e, status);
        } finally {
            try {
                RestAuditLogger.logBundle(httpServletRequest, (Bundle) resource, responseBundle, startTime, new Date(), status);
            } catch (Exception e) {
                log.log(Level.SEVERE, AUDIT_LOGGING_ERR_MSG, e);
            }

            log.exiting(this.getClass().getName(), "bundle(Bundle)");
        }
    }
}
