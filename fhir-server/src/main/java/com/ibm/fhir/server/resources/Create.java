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
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.Response.Status;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.core.HTTPReturnPreference;
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
public class Create extends FHIRResource {
    private static final Logger log = java.util.logging.Logger.getLogger(Create.class.getName());

    /**
     * This HL7-defined extension header supports "conditional create", allowing a client to create a new resource only if some equivalent
     * resource does not already exist on the server.
     * The client defines what equivalence means in this case by supplying a FHIR search query using an HL7 defined extension header "If-None-Exist" as shown:
     * <pre>
     * If-None-Exist: [search parameters]
     * </pre>
     * The header value matches the FHIR search syntax (what would be in the URL following the "?").
     */
    private static final String HEADERNAME_IF_NONE_EXIST = "If-None-Exist";

    public Create() throws Exception {
        super();
    }

    @POST
    @Path("{type}")
    public Response create(@PathParam("type") String type, Resource resource, @HeaderParam(HEADERNAME_IF_NONE_EXIST) String ifNoneExist) {
        log.entering(this.getClass().getName(), "create(String,Resource)");
        Date startTime = new Date();
        Response.Status status = null;
        FHIRRestOperationResponse ior = null;

        try {
            checkInitComplete();
            checkType(type);

            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl());
            ior = helper.doCreate(type, resource, ifNoneExist);

            ResponseBuilder response =
                    Response.created(toUri(getAbsoluteUri(getRequestBaseUri(type), ior.getLocationURI().toString())));
            resource = ior.getResource();

            HTTPReturnPreference returnPreference = FHIRRequestContext.get().getReturnPreference();
            if (resource != null && HTTPReturnPreference.REPRESENTATION == returnPreference) {
                response.entity(resource);
            } else if (ior.getOperationOutcome() != null && HTTPReturnPreference.OPERATION_OUTCOME == returnPreference) {
                response.entity(ior.getOperationOutcome());
            }
            response = addHeaders(response, resource);
            status = ior.getStatus();
            response.status(status);

            return response.build();
        } catch (FHIROperationException e) {
            status = issueListToStatus(e.getIssues());
            return exceptionResponse(e, status);
        } catch (Exception e) {
            status = Status.INTERNAL_SERVER_ERROR;
            return exceptionResponse(e, status);
        } finally {
            try {
                RestAuditLogger.logCreate(httpServletRequest,
                        ior != null ? ior.getResource() : null,
                        startTime, new Date(), status);
            } catch (Exception e) {
                log.log(Level.SEVERE, AUDIT_LOGGING_ERR_MSG, e);
            }

            log.exiting(this.getClass().getName(), "create(String,Resource)");
        }
    }
}
