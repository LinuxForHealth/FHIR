/*
 * (C) Copyright IBM Corp. 2016, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.resources;

import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_OAUTH_AUTHURL;
import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_OAUTH_REGURL;
import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_OAUTH_TOKENURL;
import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_UPDATE_CREATE_ENABLED;
import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.server.util.IssueTypeToHttpStatusMapper.issueListToStatus;

import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.JsonArray;
import javax.json.JsonPatch;
import javax.json.JsonValue;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.core.HTTPReturnPreference;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.patch.FHIRJsonPatch;
import com.ibm.fhir.model.patch.FHIRPatch;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.CapabilityStatement;
import com.ibm.fhir.model.resource.CapabilityStatement.Rest;
import com.ibm.fhir.model.resource.CapabilityStatement.Rest.Resource.Interaction;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.Url;
import com.ibm.fhir.model.type.code.CapabilityStatementKind;
import com.ibm.fhir.model.type.code.ConditionalDeleteStatus;
import com.ibm.fhir.model.type.code.ConditionalReadStatus;
import com.ibm.fhir.model.type.code.FHIRVersion;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.model.type.code.ResourceType;
import com.ibm.fhir.model.type.code.RestfulCapabilityMode;
import com.ibm.fhir.model.type.code.SystemRestfulInteraction;
import com.ibm.fhir.model.type.code.TypeRestfulInteraction;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.operation.context.FHIROperationContext;
import com.ibm.fhir.path.patch.FHIRPathPatch;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceNotSupportedException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceResourceNotFoundException;
import com.ibm.fhir.persistence.helper.FHIRPersistenceHelper;
import com.ibm.fhir.persistence.helper.PersistenceHelper;
import com.ibm.fhir.rest.FHIRRestOperationResponse;
import com.ibm.fhir.search.util.SearchUtil;
import com.ibm.fhir.server.FHIRBuildIdentifier;
import com.ibm.fhir.server.annotation.PATCH;
import com.ibm.fhir.server.exception.FHIRRestBundledRequestException;
import com.ibm.fhir.server.listener.FHIRServletContextListener;
import com.ibm.fhir.server.util.FHIRRestHelper;
import com.ibm.fhir.server.util.RestAuditLogger;

@Path("/")
@Consumes({ FHIRMediaType.APPLICATION_FHIR_JSON, MediaType.APPLICATION_JSON,
        FHIRMediaType.APPLICATION_FHIR_XML, MediaType.APPLICATION_XML })
@Produces({ FHIRMediaType.APPLICATION_FHIR_JSON, MediaType.APPLICATION_JSON,
        FHIRMediaType.APPLICATION_FHIR_XML, MediaType.APPLICATION_XML })
public class FHIRResource {
    private static final Logger log =
            java.util.logging.Logger.getLogger(FHIRResource.class.getName());

    private static final String FHIR_SERVER_NAME = "IBM FHIR Server";
    private static final String FHIR_COPYRIGHT = "(C) Copyright IBM Corporation 2016, 2020";
    private static final String EXTENSION_URL = "http://ibm.com/fhir/extension";

    private static final String HEADERNAME_IF_NONE_EXIST = "If-None-Exist";
    private static final String HEADERNAME_IF_MODIFIED_SINCE = "If-Modified-Since";
    private static final String HEADERNAME_IF_NONE_MATCH = "If-None-Match";

    private static final String AUDIT_LOGGING_ERR_MSG = "An error occurred while writing the audit log message.";

    public static final DateTimeFormatter PARSER_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("EEE")
            .optionalStart()
            // ANSIC date time format for If-Modified-Since
            .appendPattern(" MMM dd HH:mm:ss yyyy")
            .optionalEnd()
            .optionalStart()
            // Touchstone date time format for If-Modified-Since
            .appendPattern(", dd-MMM-yy HH:mm:ss")
            .optionalEnd().toFormatter();

    private PersistenceHelper persistenceHelper = null;
    private FHIRPersistence persistence = null;

    @Context
    private ServletContext context;

    @Context
    private HttpServletRequest httpServletRequest;

    /**
     * UriInfo injected by the JAXRS framework.
     *
     * <p>Use {@link #getRequestUri()} instead to get the original request URI
     * when constructing URIs that will be sent back to the end user.
     */
    @Context
    private UriInfo uriInfo;

    @Context
    private HttpHeaders httpHeaders;

    @Context
    private SecurityContext securityContext;

    private PropertyGroup fhirConfig = null;

    /**
     * This method will do a quick check of the "initCompleted" flag in the servlet context. If the flag is FALSE, then
     * we'll throw an error to short-circuit the current in-progress REST API invocation.
     */
    private void checkInitComplete() throws FHIROperationException {
        Boolean fhirServerInitComplete =
                (Boolean) context.getAttribute(FHIRServletContextListener.FHIR_SERVER_INIT_COMPLETE);
        if (Boolean.FALSE.equals(fhirServerInitComplete)) {
            String msg =
                    "The FHIR Server web application cannot process requests because it did not initialize correctly";
            throw buildRestException(msg, IssueType.EXCEPTION);
        }
    }

    public FHIRResource() throws Exception {
        if (log.isLoggable(Level.FINEST)) {
            log.entering(this.getClass().getName(), "FHIRResource ctor");
        }
        try {
            fhirConfig = FHIRConfiguration.getInstance().loadConfiguration();
        } catch (Throwable t) {
            log.severe("Unexpected error during initialization: " + t);
            throw t;
        } finally {
            if (log.isLoggable(Level.FINEST)) {
                log.exiting(this.getClass().getName(), "FHIRResource ctor");
            }
        }
    }

    @GET
    @Path("metadata")
    public Response metadata() throws ClassNotFoundException {
        log.entering(this.getClass().getName(), "metadata()");
        Date startTime = new Date();
        String errMsg = "Caught exception while processing 'metadata' request.";

        try {
            checkInitComplete();

            CapabilityStatement capabilityStatement = getCapabilityStatement();
            RestAuditLogger.logMetadata(httpServletRequest, startTime, new Date(), Response.Status.OK);

            return Response.ok().entity(capabilityStatement).build();
        } catch (FHIROperationException e) {
            log.log(Level.SEVERE, errMsg, e);
            return exceptionResponse(e, issueListToStatus(e.getIssues()));
        } catch (Exception e) {
            log.log(Level.SEVERE, errMsg, e);
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "metadata()");
        }
    }

    @POST
    @Path("{type}")
    public Response create(@PathParam("type") String type, Resource resource) {
        log.entering(this.getClass().getName(), "create(String,Resource)");
        Date startTime = new Date();
        Response.Status status = null;
        FHIRRestOperationResponse ior = null;

        try {
            checkInitComplete();

            String ifNoneExist = httpHeaders.getHeaderString(HEADERNAME_IF_NONE_EXIST);

            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl());
            ior = helper.doCreate(type, resource, ifNoneExist, null);

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

    @PUT
    @Path("{type}/{id}")
    public Response update(@PathParam("type") String type, @PathParam("id") String id, Resource resource) {
        log.entering(this.getClass().getName(), "update(String,String,Resource)");
        Date startTime = new Date();
        Response.Status status = null;
        FHIRRestOperationResponse ior = null;

        try {
            checkInitComplete();

            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl());
            ior = helper.doUpdate(type, id, resource, httpHeaders.getHeaderString(HttpHeaders.IF_MATCH), null, null);

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
    public Response conditionalUpdate(@PathParam("type") String type, Resource resource) {
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
            ior = helper.doUpdate(type, null, resource, httpHeaders.getHeaderString(HttpHeaders.IF_MATCH), searchQueryString, null);

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

    @PATCH
    @Consumes({ FHIRMediaType.APPLICATION_JSON_PATCH })
    @Produces({ FHIRMediaType.APPLICATION_FHIR_JSON, MediaType.APPLICATION_JSON })
    @Path("{type}/{id}")
    public Response patch(@PathParam("type") String type, @PathParam("id") String id, JsonArray array) {
        log.entering(this.getClass().getName(), "patch(String,String,JsonArray)");
        Date startTime = new Date();
        Response.Status status = null;
        FHIRRestOperationResponse ior = null;

        try {
            checkInitComplete();

            FHIRPatch patch = createPatch(array);

            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl());
            ior = helper.doPatch(type, id, patch, httpHeaders.getHeaderString(HttpHeaders.IF_MATCH), null, null);

            status = ior.getStatus();
            ResponseBuilder response = Response.status(status)
                    .location(toUri(getAbsoluteUri(getRequestBaseUri(type), ior.getLocationURI().toString())));

            Resource resource = ior.getResource();
            if (resource != null && HTTPReturnPreference.REPRESENTATION == FHIRRequestContext.get().getReturnPreference()) {
                response.entity(resource);
            } else if (ior.getOperationOutcome() != null && HTTPReturnPreference.OPERATION_OUTCOME == FHIRRequestContext.get().getReturnPreference()) {
                response.entity(ior.getOperationOutcome());
            }
            response = addHeaders(response, resource);
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
                RestAuditLogger.logPatch(httpServletRequest,
                        ior != null ? ior.getPrevResource() : null,
                        ior != null ? ior.getResource() : null,
                        startTime, new Date(), status);
            } catch (Exception e) {
                log.log(Level.SEVERE, AUDIT_LOGGING_ERR_MSG, e);
            }

            log.exiting(this.getClass().getName(), "patch(String,String,JsonArray)");
        }
    }

    @PATCH
    @Path("{type}/{id}")
    public Response patch(@PathParam("type") String type, @PathParam("id") String id, Parameters parameters) {
        log.entering(this.getClass().getName(), "patch(String,String,Parameters)");
        Date startTime = new Date();
        Response.Status status = null;
        FHIRRestOperationResponse ior = null;

        try {
            checkInitComplete();

            FHIRPatch patch;
            try {
                patch = FHIRPathPatch.from(parameters);
            } catch(IllegalArgumentException e) {
                throw buildRestException(e.getMessage(), IssueType.INVALID);
            }

            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl());
            ior = helper.doPatch(type, id, patch, httpHeaders.getHeaderString(HttpHeaders.IF_MATCH), null, null);

            ResponseBuilder response =
                    Response.ok().location(toUri(getAbsoluteUri(getRequestBaseUri(type), ior.getLocationURI().toString())));
            status = ior.getStatus();
            response.status(status);

            Resource resource = ior.getResource();
            if (resource != null && HTTPReturnPreference.REPRESENTATION == FHIRRequestContext.get().getReturnPreference()) {
                response.entity(resource);
            } else if (ior.getOperationOutcome() != null &&
                       HTTPReturnPreference.OPERATION_OUTCOME == FHIRRequestContext.get().getReturnPreference()) {
                response.entity(ior.getOperationOutcome());
            }
            response = addHeaders(response, resource);
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
                RestAuditLogger.logPatch(httpServletRequest,
                        ior != null ? ior.getPrevResource() : null,
                        ior != null ? ior.getResource() : null,
                        startTime, new Date(), status);
            } catch (Exception e) {
                log.log(Level.SEVERE, AUDIT_LOGGING_ERR_MSG, e);
            }

            log.exiting(this.getClass().getName(), "patch(String,String,Parameters)");
        }
    }

    @PATCH
    @Consumes({ FHIRMediaType.APPLICATION_JSON_PATCH })
    @Produces({ FHIRMediaType.APPLICATION_FHIR_JSON, MediaType.APPLICATION_JSON })
    @Path("{type}")
    public Response conditionalPatch(@PathParam("type") String type, JsonArray array) {
        log.entering(this.getClass().getName(), "conditionalPatch(String,String,JsonArray)");
        Date startTime = new Date();
        Response.Status status = null;
        FHIRRestOperationResponse ior = null;

        try {
            checkInitComplete();

            FHIRPatch patch = createPatch(array);

            String searchQueryString = httpServletRequest.getQueryString();
            if (searchQueryString == null || searchQueryString.isEmpty()) {
                String msg =
                        "Cannot PATCH to resource type endpoint unless a search query string is provided for a conditional patch.";
                throw buildRestException(msg, IssueType.INVALID);
            }

            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl());
            ior = helper.doPatch(type, null, patch, httpHeaders.getHeaderString(HttpHeaders.IF_MATCH), searchQueryString, null);

            ResponseBuilder response =
                    Response.ok().location(toUri(getAbsoluteUri(getRequestBaseUri(type), ior.getLocationURI().toString())));
            status = ior.getStatus();
            response.status(status);

            Resource resource = ior.getResource();
            if (resource != null && HTTPReturnPreference.REPRESENTATION == FHIRRequestContext.get().getReturnPreference()) {
                response.entity(resource);
            } else if (ior.getOperationOutcome() != null && HTTPReturnPreference.OPERATION_OUTCOME == FHIRRequestContext.get().getReturnPreference()) {
                response.entity(ior.getOperationOutcome());
            }

            response = addHeaders(response, ior.getResource());

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
                RestAuditLogger.logPatch(httpServletRequest,
                        ior != null ? ior.getPrevResource() : null,
                        ior != null ? ior.getResource() : null,
                        startTime, new Date(), status);
            } catch (Exception e) {
                log.log(Level.SEVERE, AUDIT_LOGGING_ERR_MSG, e);
            }

            log.exiting(this.getClass().getName(), "conditionalPatch(String,String,JsonArray)");
        }
    }

    @PATCH
    @Path("{type}")
    public Response conditionalPatch(@PathParam("type") String type, Parameters parameters) {
        log.entering(this.getClass().getName(), "conditionalPatch(String,String,Parameters)");
        Date startTime = new Date();
        Response.Status status = null;
        FHIRRestOperationResponse ior = null;

        try {
            checkInitComplete();

            FHIRPatch patch;
            try {
                patch = FHIRPathPatch.from(parameters);
            } catch(IllegalArgumentException e) {
                throw buildRestException(e.getMessage(), IssueType.INVALID);
            }

            String searchQueryString = httpServletRequest.getQueryString();
            if (searchQueryString == null || searchQueryString.isEmpty()) {
                String msg =
                        "Cannot PATCH to resource type endpoint unless a search query string is provided for a conditional patch.";
                throw buildRestException(msg, IssueType.INVALID);
            }

            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl());
            ior = helper.doPatch(type, null, patch, httpHeaders.getHeaderString(HttpHeaders.IF_MATCH), searchQueryString, null);

            status = ior.getStatus();
            ResponseBuilder response = Response.status(status)
                    .location(toUri(getAbsoluteUri(getRequestBaseUri(type), ior.getLocationURI().toString())));

            Resource resource = ior.getResource();
            if (resource != null && HTTPReturnPreference.REPRESENTATION == FHIRRequestContext.get().getReturnPreference()) {
                response.entity(resource);
            } else if (ior.getOperationOutcome() != null && HTTPReturnPreference.OPERATION_OUTCOME == FHIRRequestContext.get().getReturnPreference()) {
                response.entity(ior.getOperationOutcome());
            }

            response = addHeaders(response, ior.getResource());

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
                RestAuditLogger.logPatch(httpServletRequest,
                        ior != null ? ior.getPrevResource() : null,
                        ior != null ? ior.getResource() : null,
                        startTime, new Date(), status);
            } catch (Exception e) {
                log.log(Level.SEVERE, AUDIT_LOGGING_ERR_MSG, e);
            }

            log.exiting(this.getClass().getName(), "conditionalPatch(String,String,Parameters)");
        }
    }

    private FHIRPatch createPatch(JsonArray array) throws FHIROperationException {
        try {
            FHIRPatch patch = FHIRPatch.patch(array);
            JsonPatch jsonPatch = patch.as(FHIRJsonPatch.class).getJsonPatch();
            for (JsonValue value : jsonPatch.toJsonArray()) {
                // validate path
                String path = value.asJsonObject().getString("path");
                if ("/id".equals(path) ||
                        "/meta/versionId".equals(path) ||
                        "/meta/lastUpdated".equals(path)) {
                    throw new IllegalArgumentException("Path: '" + path
                            + "' is not allowed in a patch operation.");
                }
            }
            return patch;
        } catch (Exception e) {
            String msg = "Invalid patch: " + e.getMessage();
            throw new FHIROperationException(msg, e)
                    .withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.INVALID));
        }
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

            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl());
            ior = helper.doDelete(type, id, null, null);

            status = Status.NO_CONTENT;
            ResponseBuilder response = Response.noContent();

            if (ior.getResource() != null) {
                response = addHeaders(response, ior.getResource());
            }
            return response.build();
        } catch (FHIRPersistenceResourceNotFoundException e) {
            // Overwrite the exception response status because we want NOT_FOUND to be success for delete
            status = Status.OK;
            return exceptionResponse(e, status);
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

            String searchQueryString = httpServletRequest.getQueryString();
            if (searchQueryString == null || searchQueryString.isEmpty()) {
                String msg =
                        "A search query string is required for a conditional delete operation.";
                throw buildRestException(msg, IssueType.INVALID);
            }

            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl());
            ior = helper.doDelete(type, null, searchQueryString, null);
            status = ior.getStatus();
            ResponseBuilder response = Response.status(status);
            if (ior.getOperationOutcome() != null) {
                response.entity(ior.getOperationOutcome());
            }
            if (ior.getResource() != null) {
                response = addHeaders(response, ior.getResource());
            }
            return response.build();
        } catch (FHIRPersistenceResourceNotFoundException e) {
            // Return 200 instead of 404 to pass TouchStone test
            status = Status.OK;
            return exceptionResponse(e, status);
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

    @GET
    @Path("{type}/{id}")
    public Response read(@PathParam("type") String type, @PathParam("id") String id) throws Exception {
        log.entering(this.getClass().getName(), "read(String,String)");
        Date startTime = new Date();
        Response.Status status = null;
        FHIRRestOperationResponse ior = null;

        try {
            checkInitComplete();
            MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
            long modifiedSince = parseIfModifiedSince();

            String ifNoneMatch = httpHeaders.getHeaderString(HEADERNAME_IF_NONE_MATCH);

            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl());
            Resource resource = helper.doRead(type, id, true, false, null, null, queryParameters);
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
                response = addHeaders(response, resource);
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

    @GET
    @Path("{type}/{id}/_history/{vid}")
    public Response vread(@PathParam("type") String type, @PathParam("id") String id, @PathParam("vid") String vid) {
        log.entering(this.getClass().getName(), "vread(String,String,String)");
        Date startTime = new Date();
        Response.Status status = null;
        FHIRRestOperationResponse ior = null;

        try {
            checkInitComplete();

            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl());
            Resource resource = helper.doVRead(type, id, vid, null);
            status = Status.OK;
            ResponseBuilder response = Response.ok().entity(resource);
            response = addHeaders(response, resource);
            return response.build();
        } catch (FHIROperationException e) {
            status = issueListToStatus(e.getIssues());
            return exceptionResponse(e, status);
        } catch (Exception e) {
            status = Status.INTERNAL_SERVER_ERROR;
            return exceptionResponse(e, status);
        } finally {
            try {
                RestAuditLogger.logVersionRead(httpServletRequest,
                        ior != null ? ior.getResource() : null,
                        startTime, new Date(), status);
            } catch (Exception e) {
                log.log(Level.SEVERE, AUDIT_LOGGING_ERR_MSG, e);
            }

            log.exiting(this.getClass().getName(), "vread(String,String,String)");
        }
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

            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl());
            bundle = helper.doHistory(type, id, uriInfo.getQueryParameters(), getRequestUri(), null);
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

    @GET
    @Path("${operationName}")
    public Response invoke(@PathParam("operationName") String operationName) {
        log.entering(this.getClass().getName(), "invoke(String)");
        Date startTime = new Date();
        Response.Status status = null;

        try {
            checkInitComplete();

            FHIROperationContext operationContext =
                    FHIROperationContext.createSystemOperationContext();
            operationContext.setProperty(FHIROperationContext.PROPNAME_URI_INFO, uriInfo);
            operationContext.setProperty(FHIROperationContext.PROPNAME_HTTP_HEADERS, httpHeaders);
            operationContext.setProperty(FHIROperationContext.PROPNAME_METHOD_TYPE, HttpMethod.GET );

            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl());
            Resource result = helper.doInvoke(operationContext, null, null, null, operationName,
                    null, uriInfo.getQueryParameters(), null);
            Response response = buildResponse(operationContext, null, result);
            status = Response.Status.fromStatusCode(response.getStatus());
            return response;
        } catch (FHIROperationException e) {
            status = issueListToStatus(e.getIssues());
            return exceptionResponse(e, status);
        } catch (Exception e) {
            status = Status.INTERNAL_SERVER_ERROR;
            return exceptionResponse(e, status);
        } finally {
            try {
                RestAuditLogger.logOperation(httpServletRequest, operationName, null, null, null,
                        startTime, new Date(), status);
            } catch (Exception e) {
                log.log(Level.SEVERE, AUDIT_LOGGING_ERR_MSG, e);
            }

            log.exiting(this.getClass().getName(), "invoke(String)");
        }
    }

    @POST
    @Path("${operationName}")
    public Response invoke(@PathParam("operationName") String operationName, Resource resource) {
        log.entering(this.getClass().getName(), "invoke(String,Resource)");
        Date startTime = new Date();
        Response.Status status = null;

        try {
            checkInitComplete();

            FHIROperationContext operationContext =
                    FHIROperationContext.createSystemOperationContext();
            operationContext.setProperty(FHIROperationContext.PROPNAME_URI_INFO, uriInfo);
            operationContext.setProperty(FHIROperationContext.PROPNAME_HTTP_HEADERS, httpHeaders);
            operationContext.setProperty(FHIROperationContext.PROPNAME_METHOD_TYPE, HttpMethod.POST );

            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl());
            Resource result = helper.doInvoke(operationContext, null, null, null, operationName,
                    resource, uriInfo.getQueryParameters(), null);
            Response response = buildResponse(operationContext, null, result);
            status = Response.Status.fromStatusCode(response.getStatus());
            return response;
        } catch (FHIROperationException e) {
            status = issueListToStatus(e.getIssues());
            return exceptionResponse(e, status);
        } catch (Exception e) {
            status = Status.INTERNAL_SERVER_ERROR;
            return exceptionResponse(e, status);
        } finally {
            try {
                RestAuditLogger.logOperation(httpServletRequest, operationName, null, null, null,
                        startTime, new Date(), status);
            } catch (Exception e) {
                log.log(Level.SEVERE, AUDIT_LOGGING_ERR_MSG, e);
            }

            log.exiting(this.getClass().getName(), "invoke(String,Resource)");
        }
    }

    @GET
    @Path("{resourceTypeName}/${operationName}")
    public Response invoke(@PathParam("resourceTypeName") String resourceTypeName,
            @PathParam("operationName") String operationName) {
        log.entering(this.getClass().getName(), "invoke(String,String)");
        Date startTime = new Date();
        Response.Status status = null;

        try {
            checkInitComplete();

            FHIROperationContext operationContext =
                    FHIROperationContext.createResourceTypeOperationContext();
            operationContext.setProperty(FHIROperationContext.PROPNAME_URI_INFO, uriInfo);
            operationContext.setProperty(FHIROperationContext.PROPNAME_HTTP_HEADERS, httpHeaders);
            operationContext.setProperty(FHIROperationContext.PROPNAME_METHOD_TYPE, HttpMethod.GET );

            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl());
            Resource result = helper.doInvoke(operationContext, resourceTypeName, null, null, operationName,
                    null, uriInfo.getQueryParameters(), null);
            Response response = buildResponse(operationContext, resourceTypeName, result);
            status = Response.Status.fromStatusCode(response.getStatus());
            return response;
        } catch (FHIROperationException e) {
            status = issueListToStatus(e.getIssues());
            return exceptionResponse(e, status);
        } catch (Exception e) {
            status = Status.INTERNAL_SERVER_ERROR;
            return exceptionResponse(e, status);
        } finally {
            try {
                RestAuditLogger.logOperation(httpServletRequest, operationName, resourceTypeName, null, null,
                        startTime, new Date(), status);
            } catch (Exception e) {
                log.log(Level.SEVERE, AUDIT_LOGGING_ERR_MSG, e);
            }

            log.exiting(this.getClass().getName(), "invoke(String,String)");
        }
    }

    @POST
    @Path("{resourceTypeName}/${operationName}")
    public Response invoke(@PathParam("resourceTypeName") String resourceTypeName,
            @PathParam("operationName") String operationName, Resource resource) {
        log.entering(this.getClass().getName(), "invoke(String,String,Resource)");
        Date startTime = new Date();
        Response.Status status = null;

        try {
            checkInitComplete();

            FHIROperationContext operationContext =
                    FHIROperationContext.createResourceTypeOperationContext();
            operationContext.setProperty(FHIROperationContext.PROPNAME_URI_INFO, uriInfo);
            operationContext.setProperty(FHIROperationContext.PROPNAME_HTTP_HEADERS, httpHeaders);
            operationContext.setProperty(FHIROperationContext.PROPNAME_METHOD_TYPE, HttpMethod.POST );

            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl());
            Resource result = helper.doInvoke(operationContext, resourceTypeName, null, null, operationName,
                    resource, uriInfo.getQueryParameters(), null);
            Response response = buildResponse(operationContext, resourceTypeName, result);
            status = Response.Status.fromStatusCode(response.getStatus());
            return response;
        } catch (FHIROperationException e) {
            // response 200 OK if no failure issue found.
            boolean isFailure = false;
            for (Issue issue : e.getIssues()) {
                if (FHIRUtil.isFailure(issue.getSeverity())) {
                    isFailure = true;
                    break;
                }
            }
            if (isFailure) {
                status = issueListToStatus(e.getIssues());
                return exceptionResponse(e, status);
            } else {
                status = Status.OK;
                return exceptionResponse(e, Response.Status.OK);
            }
        } catch (Exception e) {
            status = Status.INTERNAL_SERVER_ERROR;
            return exceptionResponse(e, status);
        } finally {
            try {
                RestAuditLogger.logOperation(httpServletRequest, operationName, resourceTypeName, null, null,
                        startTime, new Date(), status);
            } catch (Exception e) {
                log.log(Level.SEVERE, AUDIT_LOGGING_ERR_MSG, e);
            }

            log.exiting(this.getClass().getName(), "invoke(String,String,Resource)");
        }
    }

    @GET
    @Path("{resourceTypeName}/{logicalId}/${operationName}")
    public Response invoke(@PathParam("resourceTypeName") String resourceTypeName,
            @PathParam("logicalId") String logicalId,
            @PathParam("operationName") String operationName) {
        log.entering(this.getClass().getName(), "invoke(String,String,String)");
        Date startTime = new Date();
        Response.Status status = null;

        try {
            checkInitComplete();

            FHIROperationContext operationContext =
                    FHIROperationContext.createInstanceOperationContext();
            operationContext.setProperty(FHIROperationContext.PROPNAME_URI_INFO, uriInfo);
            operationContext.setProperty(FHIROperationContext.PROPNAME_HTTP_HEADERS, httpHeaders);
            operationContext.setProperty(FHIROperationContext.PROPNAME_METHOD_TYPE, HttpMethod.GET );

            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl());
            Resource result = helper.doInvoke(operationContext, resourceTypeName, logicalId, null, operationName,
                    null, uriInfo.getQueryParameters(), null);
            Response response = buildResponse(operationContext, resourceTypeName, result);
            status = Response.Status.fromStatusCode(response.getStatus());
            return response;
        } catch (FHIROperationException e) {
            status = issueListToStatus(e.getIssues());
            return exceptionResponse(e, status);
        } catch (Exception e) {
            status = Status.INTERNAL_SERVER_ERROR;
            return exceptionResponse(e, status);
        } finally {
            try {
                RestAuditLogger.logOperation(httpServletRequest, operationName, resourceTypeName, logicalId, null,
                        startTime, new Date(), status);
            } catch (Exception e) {
                log.log(Level.SEVERE, AUDIT_LOGGING_ERR_MSG, e);
            }

            log.exiting(this.getClass().getName(), "invoke(String,String,String)");
        }
    }

    @POST
    @Path("{resourceTypeName}/{logicalId}/${operationName}")
    public Response invoke(@PathParam("resourceTypeName") String resourceTypeName,
            @PathParam("logicalId") String logicalId,
            @PathParam("operationName") String operationName, Resource resource) {
        log.entering(this.getClass().getName(), "invoke(String,String,String,Resource)");
        Date startTime = new Date();
        Response.Status status = null;

        try {
            checkInitComplete();

            FHIROperationContext operationContext =
                    FHIROperationContext.createInstanceOperationContext();
            operationContext.setProperty(FHIROperationContext.PROPNAME_URI_INFO, uriInfo);
            operationContext.setProperty(FHIROperationContext.PROPNAME_HTTP_HEADERS, httpHeaders);
            operationContext.setProperty(FHIROperationContext.PROPNAME_METHOD_TYPE, HttpMethod.POST);

            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl());
            Resource result = helper.doInvoke(operationContext, resourceTypeName, logicalId, null, operationName,
                    resource, uriInfo.getQueryParameters(), null);
            Response response = buildResponse(operationContext, resourceTypeName, result);
            status = Response.Status.fromStatusCode(response.getStatus());
            return response;
        } catch (FHIROperationException e) {
            status = issueListToStatus(e.getIssues());
            return exceptionResponse(e, status);
        } catch (Exception e) {
            status = Status.INTERNAL_SERVER_ERROR;
            return exceptionResponse(e, status);
        } finally {
            try {
                RestAuditLogger.logOperation(httpServletRequest, operationName, resourceTypeName, logicalId, null,
                        startTime, new Date(), status);
            } catch (Exception e) {
                log.log(Level.SEVERE, AUDIT_LOGGING_ERR_MSG, e);
            }

            log.exiting(this.getClass().getName(), "invoke(String,String,String,Resource)");
        }
    }

    @GET
    @Path("{resourceTypeName}/{logicalId}/_history/{versionId}/${operationName}")
    public Response invoke(@PathParam("resourceTypeName") String resourceTypeName,
            @PathParam("logicalId") String logicalId,
            @PathParam("versionId") String versionId,
            @PathParam("operationName") String operationName) {
        log.entering(this.getClass().getName(), "invoke(String,String,String,String)");
        Date startTime = new Date();
        Response.Status status = null;

        try {
            checkInitComplete();

            FHIROperationContext operationContext =
                    FHIROperationContext.createInstanceOperationContext();
            operationContext.setProperty(FHIROperationContext.PROPNAME_URI_INFO, uriInfo);
            operationContext.setProperty(FHIROperationContext.PROPNAME_HTTP_HEADERS, httpHeaders);
            operationContext.setProperty(FHIROperationContext.PROPNAME_METHOD_TYPE, HttpMethod.GET);

            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl());
            Resource result = helper.doInvoke(operationContext, resourceTypeName, logicalId, versionId, operationName, 
                    null, uriInfo.getQueryParameters(), null);
            Response response = buildResponse(operationContext, resourceTypeName, result);
            status = Response.Status.fromStatusCode(response.getStatus());
            return response;
        } catch (FHIROperationException e) {
            status = issueListToStatus(e.getIssues());
            return exceptionResponse(e, status);
        } catch (Exception e) {
            status = Status.INTERNAL_SERVER_ERROR;
            return exceptionResponse(e, status);
        } finally {
            try {
                RestAuditLogger.logOperation(httpServletRequest, operationName, resourceTypeName, logicalId, versionId,
                        startTime, new Date(), status);
            } catch (Exception e) {
                log.log(Level.SEVERE, AUDIT_LOGGING_ERR_MSG, e);
            }

            log.exiting(this.getClass().getName(), "invoke(String,String,String,String)");
        }
    }

    @POST
    @Path("{resourceTypeName}/{logicalId}/_history/{versionId}/${operationName}")
    public Response invoke(@PathParam("resourceTypeName") String resourceTypeName,
            @PathParam("logicalId") String logicalId,
            @PathParam("versionId") String versionId,
            @PathParam("operationName") String operationName, Resource resource) {
        log.entering(this.getClass().getName(), "invoke(String,String,String,String,Resource)");
        Date startTime = new Date();
        Response.Status status = null;

        try {
            checkInitComplete();

            FHIROperationContext operationContext =
                    FHIROperationContext.createInstanceOperationContext();
            operationContext.setProperty(FHIROperationContext.PROPNAME_URI_INFO, uriInfo);
            operationContext.setProperty(FHIROperationContext.PROPNAME_HTTP_HEADERS, httpHeaders);
            operationContext.setProperty(FHIROperationContext.PROPNAME_METHOD_TYPE, HttpMethod.POST );

            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl());
            Resource result = helper.doInvoke(operationContext, resourceTypeName, logicalId, versionId, operationName,
                    resource, uriInfo.getQueryParameters(), null);
            Response response = buildResponse(operationContext, resourceTypeName, result);
            status = Response.Status.fromStatusCode(response.getStatus());
            return response;
        } catch (FHIROperationException e) {
            status = issueListToStatus(e.getIssues());
            return exceptionResponse(e, status);
        } catch (Exception e) {
            status = Status.INTERNAL_SERVER_ERROR;
            return exceptionResponse(e, status);
        } finally {
            try {
                RestAuditLogger.logOperation(httpServletRequest, operationName, resourceTypeName, logicalId, versionId,
                        startTime, new Date(), status);
            } catch (Exception e) {
                log.log(Level.SEVERE, AUDIT_LOGGING_ERR_MSG, e);
            }

            log.exiting(this.getClass().getName(), "invoke(String,String,String,String,Resource)");
        }
    }

    @POST
    public Response bundle(Resource resource) {
        log.entering(this.getClass().getName(), "bundle(Bundle)");
        Date startTime = new Date();
        Response.Status status = null;
        Bundle responseBundle = null;

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

            FHIRRestHelper helper = new FHIRRestHelper(getPersistenceImpl());
            responseBundle = helper.doBundle(inputBundle, null);
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
                RestAuditLogger.logBundle(httpServletRequest, responseBundle, startTime, new Date(), status);
            } catch (Exception e) {
                log.log(Level.SEVERE, AUDIT_LOGGING_ERR_MSG, e);
            }

            log.exiting(this.getClass().getName(), "bundle(Bundle)");
        }
    }

    private FHIROperationException buildRestException(String msg, IssueType issueType) {
        return buildRestException(msg, issueType, IssueSeverity.FATAL);
    }

    private FHIROperationException buildRestException(String msg, IssueType issueType, IssueSeverity severity) {
        return new FHIROperationException(msg).withIssue(buildOperationOutcomeIssue(severity, issueType, msg));
    }

    private long parseIfModifiedSince() {
        // Modified since date time in EpochMilli
        long modifiedSince = -1;
        try {
            // Handle RFC_1123 and RFC_850 formats first.
            // e.g "Sun, 06 Nov 1994 08:49:37 GMT", "Sunday, 06-Nov-94 08:49:37 GMT", "Sunday, 06-Nov-1994 08:49:37 GMT"
            // If 2 digits year is used, then means 1940 to 2039.
            modifiedSince = httpServletRequest.getDateHeader(HEADERNAME_IF_MODIFIED_SINCE);
        } catch (IllegalArgumentException e) {
            try {
                // Then handle ANSIC format, e.g, "Sun Nov  6 08:49:37 1994"
                // and touchStone specific format, e.g, "Sat, 28-Sep-19 16:11:14"
                // assuming the time zone is GMT.
                modifiedSince = PARSER_FORMATTER.parse(httpHeaders.getHeaderString(HEADERNAME_IF_MODIFIED_SINCE), LocalDateTime::from)
                        .atZone(ZoneId.of("GMT")).toInstant().toEpochMilli();
            } catch (DateTimeParseException e1) {
                    modifiedSince = -1;
            }
        }
        return modifiedSince;
    }

    /**
     * Builds an OperationOutcomeIssue with the respective values for some of the fields.
     */
    private OperationOutcome.Issue buildOperationOutcomeIssue(IssueSeverity severity, IssueType type, String msg) {
        return OperationOutcome.Issue.builder()
                .severity(severity)
                .code(type)
                .details(CodeableConcept.builder().text(string(msg)).build())
                .build();
    }

    /**
     * This function will build an absolute URI from the specified base URI and relative URI.
     *
     * @param baseUri
     *            the base URI to be used; this will be of the form <scheme>://<host>:<port>/<context-root>
     * @param relativeUri
     *            the path and query parts
     * @return the full URI value as a String
     */
    private String getAbsoluteUri(String baseUri, String relativeUri) {
        StringBuilder fullUri = new StringBuilder();
        fullUri.append(baseUri);
        if (!baseUri.endsWith("/")) {
            fullUri.append("/");
        }
        fullUri.append((relativeUri.startsWith("/") ? relativeUri.substring(1) : relativeUri));
        return fullUri.toString();
    }

    /**
     * Adds the Etag and Last-Modified headers to the specified response object.
     */
    private ResponseBuilder addHeaders(ResponseBuilder rb, Resource resource) {
        return rb.header(HttpHeaders.ETAG, getEtagValue(resource))
                // According to 3.3.1 of RTC2616(HTTP/1.1), we MUST only generate the RFC 1123 format for representing HTTP-date values
                // in header fields, e.g Sat, 28 Sep 2019 16:11:14 GMT
                .lastModified(Date.from(resource.getMeta().getLastUpdated().getValue().toInstant()));
    }

    private String getEtagValue(Resource resource) {
        return "W/\"" + resource.getMeta().getVersionId().getValue() + "\"";
    }

    private Response exceptionResponse(FHIRRestBundledRequestException e) {
        Response response;
        if (e.getResponseBundle() != null) {
            if (e.getIssues().size() > 0) {
                // R4 says we should return a single OperationOutcome with the issues:
                // http://www.hl7.org/fhir/r4/http.html#transaction-response
                String msg =
                        "FHIRRestBundledRequestException contains both a response bundle and a list of issues. "
                                + "Only the response bundle will be returned.";
                log.log(Level.WARNING, msg, e);
            }

            List<Bundle.Entry> toAdd = new ArrayList<Bundle.Entry>();
            // replace bundle entries that have an empty response
            for (int i = 0; i < e.getResponseBundle().getEntry().size(); i++) {
                Bundle.Entry entry = e.getResponseBundle().getEntry().get(i);
                if (entry.getResponse() != null && entry.getResponse().getStatus() == null) {
                    entry = entry.toBuilder()
                            .response(entry.getResponse().toBuilder()
                                .status(string(Integer.toString(Status.BAD_REQUEST.getStatusCode())))
                                .build())
                            .build();
                }
                toAdd.add(entry);
            }

            Bundle responseBundle = e.getResponseBundle().toBuilder().entry(toAdd).build();

            response = Response.status(Status.OK).entity(responseBundle).build();
        } else {
            // Override the status code with a generic client (400) or server (500) error code
            Status status = issueListToStatus(e.getIssues());
            if (status.getFamily() == Status.Family.CLIENT_ERROR) {
                status = Status.BAD_REQUEST;
            } else {
                status = Status.INTERNAL_SERVER_ERROR;
            }
            response = exceptionResponse(e, status);
        }
        return response;
    }

    private Response exceptionResponse(FHIROperationException e, Status status) {
        if (status == null) {
            status = issueListToStatus(e.getIssues());
        }

        if (status.getFamily() == Status.Family.SERVER_ERROR) {
            log.log(Level.SEVERE, e.getMessage(), e);
        } else {
            if (log.isLoggable(Level.FINE)) {
                log.log(Level.FINE, e.getMessage(), e);
            }
        }

        OperationOutcome operationOutcome = FHIRUtil.buildOperationOutcome(e, false);
        return exceptionResponse(operationOutcome, status);
    }

    private Response exceptionResponse(Exception e, Status status) {
        log.log(Level.SEVERE, "An unexpected exeption occurred while processing the request", e);
        OperationOutcome oo = FHIRUtil.buildOperationOutcome(e, false);
        return this.exceptionResponse(oo, status);
    }

    private Response exceptionResponse(OperationOutcome oo, Status status) {
        if (log.isLoggable(Level.FINE)) {
            StringBuilder sb = new StringBuilder();
            sb.append("\nOperationOutcome:\n").append(serializeOperationOutcome(oo));
            log.log(Level.FINE, sb.toString());
        }
        return Response.status(status).entity(oo).build();
    }

    private String serializeOperationOutcome(OperationOutcome oo) {
        try {
            StringWriter sw = new StringWriter();
            FHIRGenerator.generator(Format.JSON, false).generate(oo, sw);
            return sw.toString();
        } catch (Throwable t) {
            return "Error encountered while serializing OperationOutcome resource: "
                    + t.getMessage();
        }
    }

    private synchronized CapabilityStatement getCapabilityStatement() throws FHIROperationException {
        try {
            return buildCapabilityStatement();
        } catch (Throwable t) {
            String msg = "An error occurred while constructing the Conformance statement.";
            log.log(Level.SEVERE, msg, t);
            throw buildRestException(msg, IssueType.EXCEPTION);
        }
    }

    /**
     * Builds a CapabilityStatement resource instance which describes this server.
     *
     * @throws Exception
     */
    private CapabilityStatement buildCapabilityStatement() throws Exception {
        // Build the list of interactions that are supported for each resource type.

        List<Rest.Resource.Interaction> interactions = new ArrayList<>();
        interactions.add(buildInteractionStatement(TypeRestfulInteraction.CREATE));
        interactions.add(buildInteractionStatement(TypeRestfulInteraction.UPDATE));
        if (isDeleteSupported()) {
            interactions.add(buildInteractionStatement(TypeRestfulInteraction.DELETE));
        }
        interactions.add(buildInteractionStatement(TypeRestfulInteraction.READ));
        interactions.add(buildInteractionStatement(TypeRestfulInteraction.VREAD));
        interactions.add(buildInteractionStatement(TypeRestfulInteraction.HISTORY_INSTANCE));
        interactions.add(buildInteractionStatement(TypeRestfulInteraction.SEARCH_TYPE));
        interactions.add(buildInteractionStatement(TypeRestfulInteraction.PATCH));

        // Build the list of supported resources.
        List<Rest.Resource> resources = new ArrayList<>();
        ResourceType.ValueSet[] resourceTypes = ResourceType.ValueSet.values();
        for (ResourceType.ValueSet resourceType : resourceTypes) {
            String resourceTypeName = resourceType.value();
            // Build the set of ConformanceSearchParams for this resource type.
            List<Rest.Resource.SearchParam> conformanceSearchParams = new ArrayList<>();
            List<SearchParameter> searchParameters = SearchUtil.getSearchParameters(resourceTypeName);
            if (searchParameters != null) {
                for (SearchParameter searchParameter : searchParameters) {
                    // The name here is a natural language name, and intentionally not replaced with code.
                    Rest.Resource.SearchParam.Builder conformanceSearchParamBuilder =
                            Rest.Resource.SearchParam.builder()
                                .name(searchParameter.getName())
                                .type(searchParameter.getType());
                    if (searchParameter.getDescription() != null) {
                        conformanceSearchParamBuilder.documentation(searchParameter.getDescription());
                    }

                    Rest.Resource.SearchParam conformanceSearchParam =
                            conformanceSearchParamBuilder.build();
                    conformanceSearchParams.add(conformanceSearchParam);
                }
            }

            // Build the ConformanceResource for this resource type.
            Rest.Resource cr = Rest.Resource.builder()
                    .type(ResourceType.of(resourceType))
                    .profile(Canonical.of("http://hl7.org/fhir/profiles/" + resourceTypeName))
                    .interaction(interactions)
                    .conditionalCreate(com.ibm.fhir.model.type.Boolean.of(true))
                    .conditionalUpdate(com.ibm.fhir.model.type.Boolean.of(true))
                    .updateCreate(com.ibm.fhir.model.type.Boolean.of(isUpdateCreateEnabled()))
                    .conditionalDelete(ConditionalDeleteStatus.SINGLE)
                    .conditionalRead(ConditionalReadStatus.FULL_SUPPORT)
                    .searchParam(conformanceSearchParams)
                    .build();

            resources.add(cr);
        }

        // Determine if transactions are supported for this FHIR Server configuration.
        SystemRestfulInteraction transactionMode = SystemRestfulInteraction.BATCH;
        try {
            boolean txnSupported = getPersistenceImpl().isTransactional();
            transactionMode = (txnSupported ? SystemRestfulInteraction.TRANSACTION
                    : SystemRestfulInteraction.BATCH);
        } catch (Throwable t) {
            log.log(Level.WARNING, "Unexcepted error while reading server transaction mode setting", t);
        }

        String actualHost = new URI(getRequestUri()).getHost();

        String regURLTemplate = null;
        String authURLTemplate = null;
        String tokenURLTemplate = null;
        try {
            regURLTemplate = fhirConfig.getStringProperty(PROPERTY_OAUTH_REGURL, "");
            authURLTemplate = fhirConfig.getStringProperty(PROPERTY_OAUTH_AUTHURL, "");
            tokenURLTemplate = fhirConfig.getStringProperty(PROPERTY_OAUTH_TOKENURL, "");
        } catch (Exception e) {
            log.log(Level.SEVERE, "An error occurred while adding OAuth URLs to the conformance statement", e);
        }
        String tokenURL = tokenURLTemplate.replaceAll("<host>", actualHost);

        String authURL = authURLTemplate.replaceAll("<host>", actualHost);

        String regURL = regURLTemplate.replaceAll("<host>", actualHost);

        CapabilityStatement.Rest.Security restSecurity = CapabilityStatement.Rest.Security.builder()
                .service(CodeableConcept.builder()
                    .coding(Coding.builder()
                        .code(Code.of("SMART-on-FHIR"))
                        .system(Uri.of("http://terminology.hl7.org/CodeSystem/restful-security-service"))
                        .build())
                    .text(string("OAuth2 using SMART-on-FHIR profile (see http://docs.smarthealthit.org)"))
                    .build())
                .extension(Extension.builder()
                    .url("http://fhir-registry.smarthealthit.org/StructureDefinition/oauth-uris")
                    .extension(
                        Extension.builder().url("token").value(Url.of(tokenURL)).build(),
                        Extension.builder().url("authorize").value(Url.of(authURL)).build(),
                        Extension.builder().url("register").value(Url.of(regURL)).build())
                    .build())
                .build();

        CapabilityStatement.Rest rest = CapabilityStatement.Rest.builder()
                .mode(RestfulCapabilityMode.SERVER)
                .security(restSecurity)
                .resource(resources)
                .interaction(CapabilityStatement.Rest.Interaction.builder()
                    .code(transactionMode)
                    .build())
                .build();

        FHIRBuildIdentifier buildInfo = new FHIRBuildIdentifier();
        String buildDescription = FHIR_SERVER_NAME + " version " + buildInfo.getBuildVersion()
                + " build id " + buildInfo.getBuildId() + "";

        List<Code> format = new ArrayList<Code>();
        format.add(Code.of(Format.JSON.toString().toLowerCase()));
        format.add(Code.of(Format.XML.toString().toLowerCase()));
        format.add(Code.of(FHIRMediaType.APPLICATION_JSON));
        format.add(Code.of(FHIRMediaType.APPLICATION_FHIR_JSON));
        format.add(Code.of(FHIRMediaType.APPLICATION_XML));
        format.add(Code.of(FHIRMediaType.APPLICATION_FHIR_XML));

        // Finally, create the CapabilityStatement resource itself.
        CapabilityStatement conformance = CapabilityStatement.builder()
                .status(PublicationStatus.ACTIVE)
                .date(DateTime.of(ZonedDateTime.now(ZoneOffset.UTC)))
                .kind(CapabilityStatementKind.CAPABILITY)
                .fhirVersion(FHIRVersion.VERSION_4_0_1)
                .format(format)
                .patchFormat(Code.of(FHIRMediaType.APPLICATION_JSON_PATCH),
                             Code.of(FHIRMediaType.APPLICATION_FHIR_JSON),
                             Code.of(FHIRMediaType.APPLICATION_FHIR_XML))
                .version(string(buildInfo.getBuildVersion()))
                .name(string(FHIR_SERVER_NAME))
                .description(Markdown.of(buildDescription))
                .copyright(Markdown.of(FHIR_COPYRIGHT))
                .publisher(string("IBM Corporation"))
                .software(CapabilityStatement.Software.builder()
                          .name(string(FHIR_SERVER_NAME))
                          .version(string(buildInfo.getBuildVersion()))
                          .id(buildInfo.getBuildId())
                          .build())
                .rest(rest)
                .build();

        try {
            conformance = addExtensionElements(conformance);
        } catch (Exception e) {
            log.log(Level.SEVERE, "An error occurred while adding extension elements to the conformance statement", e);
        }

        return conformance;
    }

    private CapabilityStatement addExtensionElements(CapabilityStatement capabilityStatement)
        throws Exception {
        List<Extension> extentions = new ArrayList<Extension>();
        Extension extension = Extension.builder()
                .url(EXTENSION_URL + "/defaultTenantId")
                .value(string(fhirConfig.getStringProperty(FHIRConfiguration.PROPERTY_DEFAULT_TENANT_ID, FHIRConfiguration.DEFAULT_TENANT_ID)))
                .build();
        extentions.add(extension);

        extension = Extension.builder()
                .url(EXTENSION_URL + "/websocketNotificationsEnabled")
                .value(com.ibm.fhir.model.type.Boolean.of(fhirConfig.getBooleanProperty(FHIRConfiguration.PROPERTY_WEBSOCKET_ENABLED, Boolean.FALSE)))
                .build();
        extentions.add(extension);

        extension = Extension.builder()
                .url(EXTENSION_URL + "/kafkaNotificationsEnabled")
                .value(com.ibm.fhir.model.type.Boolean.of(fhirConfig.getBooleanProperty(FHIRConfiguration.PROPERTY_KAFKA_ENABLED, Boolean.FALSE)))
                .build();
        extentions.add(extension);

        String notificationResourceTypes = getNotificationResourceTypes();
        if ("".equals(notificationResourceTypes)) {
            notificationResourceTypes = "<not specified - all resource types>";
        }

        extension = Extension.builder()
                .url(EXTENSION_URL + "/notificationResourceTypes")
                .value(string(notificationResourceTypes))
                .build();
        extentions.add(extension);

        String auditLogServiceName =
                fhirConfig.getStringProperty(FHIRConfiguration.PROPERTY_AUDIT_SERVICE_CLASS_NAME);

        if (auditLogServiceName == null || "".equals(auditLogServiceName)) {
            auditLogServiceName = "<not specified>";
        } else {
            int lastDelimeter = auditLogServiceName.lastIndexOf(".");
            auditLogServiceName = auditLogServiceName.substring(lastDelimeter + 1);
        }

        extension = Extension.builder()
                .url(EXTENSION_URL + "/auditLogServiceName")
                .value(string(auditLogServiceName))
                .build();
        extentions.add(extension);

        PropertyGroup auditLogProperties =
                fhirConfig.getPropertyGroup(FHIRConfiguration.PROPERTY_AUDIT_SERVICE_PROPERTIES);
        String auditLogPropertiesString =
                auditLogProperties != null ? auditLogProperties.toString() : "<not specified>";
        extension = Extension.builder()
                .url(EXTENSION_URL + "/auditLogProperties")
                .value(string(auditLogPropertiesString))
                .build();
        extentions.add(extension);

        extension = Extension.builder()
                .url(EXTENSION_URL + "/persistenceType")
                .value(string(getPersistenceImpl().getClass().getSimpleName()))
                .build();
        extentions.add(extension);

        return capabilityStatement.toBuilder().extension(extentions).build();

    }

    private String getNotificationResourceTypes() throws Exception {
        Object[] notificationResourceTypes =
                fhirConfig.getArrayProperty(FHIRConfiguration.PROPERTY_NOTIFICATION_RESOURCE_TYPES);
        if (notificationResourceTypes == null) {
            notificationResourceTypes = new Object[0];
        }
        return Arrays.asList(notificationResourceTypes).toString().replace("[", "").replace("]", "").replace(" ", "");
    }

    private Interaction buildInteractionStatement(TypeRestfulInteraction value) {
        Interaction ci = Interaction.builder().code(value).build();
        return ci;
    }

    /**
     * Retrieves the shared persistence helper object from the servlet context.
     */
    private synchronized PersistenceHelper getPersistenceHelper() {
        if (persistenceHelper == null) {
            persistenceHelper =
                    (PersistenceHelper) context.getAttribute(FHIRPersistenceHelper.class.getName());
            if (log.isLoggable(Level.FINE)) {
                log.fine("Retrieved FHIRPersistenceHelper instance from servlet context: "
                        + persistenceHelper);
            }
        }
        return persistenceHelper;
    }

    private synchronized FHIRPersistence getPersistenceImpl() throws FHIRPersistenceException {
        if (persistence == null) {
            persistence = getPersistenceHelper().getFHIRPersistenceImplementation();
            if (log.isLoggable(Level.FINE)) {
                log.fine("Obtained new  FHIRPersistence instance: " + persistence);
            }
        }
        return persistence;
    }

    private boolean isDeleteSupported() throws FHIRPersistenceException {
        return getPersistenceImpl().isDeleteSupported();
    }

    private Boolean isUpdateCreateEnabled() {
        return fhirConfig.getBooleanProperty(PROPERTY_UPDATE_CREATE_ENABLED, Boolean.TRUE);
    }

    /**
     * Get the original request URI from either the HttpServletRequest or a configured Header (in case of re-writing proxies).
     *
     * <p>When the 'fhirServer/core/originalRequestUriHeaderName' property is empty, this method returns the equivalent of
     * uriInfo.getRequestUri().toString(), except that uriInfo.getRequestUri() will throw an IllegalArgumentException
     * when the query string portion contains a vertical bar | character. The vertical bar is one known case of a special character
     * causing the exception. There could be others.
     *
     * @return String The complete request URI
     * @throws Exception if an error occurs while reading the config
     */
    private String getRequestUri() throws Exception {
        return FHIRRequestContext.get().getOriginalRequestUri();
    }

    /**
     * This method returns the "base URI" associated with the current request. For example, if a client invoked POST
     * https://myhost:9443/fhir-server/api/v4/Patient to create a Patient resource, this method would return
     * "https://myhost:9443/fhir-server/api/v4".
     *
     * @return The base endpoint URI associated with the current request.
     * @throws Exception if an error occurs while reading the config
     * @implNote This method uses {@link #getRequestUri()} to get the original request URI and then strips it to the
     *           <a href="https://www.hl7.org/fhir/http.html#general">Service Base URL</a>
     */
    private String getRequestBaseUri(String type) throws Exception {
        String baseUri = null;

        String requestUri = getRequestUri();

        // Strip off everything after the path
        int queryPathSeparatorLoc = requestUri.indexOf("?");
        if (queryPathSeparatorLoc != -1) {
            baseUri = requestUri.substring(0, queryPathSeparatorLoc);
        } else {
            baseUri = requestUri;
        }

        // Strip off any path elements after the base
        if (type != null && !type.isEmpty()) {
            int resourceNamePathLocation = baseUri.lastIndexOf("/" + type);
            if (resourceNamePathLocation != -1) {
                baseUri = requestUri.substring(0, resourceNamePathLocation);
            } else {
                // Assume the request was a batch/transaction and just use the requestUri as the base
                baseUri = requestUri;
            }
        } else {
            if (baseUri.endsWith("/_history")) {
                baseUri = baseUri.substring(0, baseUri.length() - "/_history".length());
            } else if (baseUri.endsWith("/_search")) {
                baseUri = baseUri.substring(0, baseUri.length() - "/_search".length());
            } else if (baseUri.contains("/$")) {
                baseUri = baseUri.substring(0, baseUri.lastIndexOf("/$"));
            }
        }

        return baseUri;
    }

    /**
     * This method simply returns a URI object containing the specified URI string.
     *
     * @param uriString
     *            the URI string for which the URI object will be created
     * @throws URISyntaxException
     */
    private URI toUri(String uriString) throws URISyntaxException {
        return new URI(uriString);
    }

    private Response buildResponse(FHIROperationContext operationContext, String resourceTypeName, Resource resource)
            throws Exception {
        // The following code allows the downstream application to change the response code
        // This enables the 202 accepted to be sent back
        Response.Status status = Response.Status.OK;
        Object o = operationContext.getProperty(FHIROperationContext.PROPNAME_STATUS_TYPE);
        if (o != null) {
            status = (Response.Status) o;
            if (Response.Status.ACCEPTED.equals(status)) {
                // This change is for BulkData operations which manipulate the response code.
                // Operations that return Accepted need to implement their own approach.
                Object ox = operationContext.getProperty(FHIROperationContext.PROPNAME_RESPONSE);
                return (Response) ox;
            }
        }

        URI locationURI =
                (URI) operationContext.getProperty(FHIROperationContext.PROPNAME_LOCATION_URI);
        if (locationURI != null) {
            return Response.status(status)
                    .location(toUri(getAbsoluteUri(getRequestBaseUri(resourceTypeName), locationURI.toString())))
                    .entity(resource)
                    .build();
        }
        return Response.status(status).entity(resource).build();
    }
}
