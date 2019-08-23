/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.resources;

import static com.ibm.watsonhealth.fhir.config.FHIRConfiguration.PROPERTY_ALLOWABLE_VIRTUAL_RESOURCE_TYPES;
import static com.ibm.watsonhealth.fhir.config.FHIRConfiguration.PROPERTY_OAUTH_AUTHURL;
import static com.ibm.watsonhealth.fhir.config.FHIRConfiguration.PROPERTY_OAUTH_REGURL;
import static com.ibm.watsonhealth.fhir.config.FHIRConfiguration.PROPERTY_OAUTH_TOKENURL;
import static com.ibm.watsonhealth.fhir.config.FHIRConfiguration.PROPERTY_UPDATE_CREATE_ENABLED;
import static com.ibm.watsonhealth.fhir.config.FHIRConfiguration.PROPERTY_USER_DEFINED_SCHEMATRON_ENABLED;
import static com.ibm.watsonhealth.fhir.config.FHIRConfiguration.PROPERTY_VIRTUAL_RESOURCES_ENABLED;
import static com.ibm.watsonhealth.fhir.model.type.String.string;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.getResourceType;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_GONE;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;

import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.json.JsonArray;
import javax.json.JsonPatch;
import javax.json.JsonValue;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.owasp.encoder.Encode;

import com.ibm.watsonhealth.fhir.config.FHIRConfigHelper;
import com.ibm.watsonhealth.fhir.config.FHIRConfiguration;
import com.ibm.watsonhealth.fhir.config.FHIRRequestContext;
import com.ibm.watsonhealth.fhir.config.PropertyGroup;
import com.ibm.watsonhealth.fhir.core.MediaType;
import com.ibm.watsonhealth.fhir.core.context.FHIRPagingContext;
import com.ibm.watsonhealth.fhir.exception.FHIROperationException;
import com.ibm.watsonhealth.fhir.model.format.Format;
import com.ibm.watsonhealth.fhir.model.generator.FHIRGenerator;
import com.ibm.watsonhealth.fhir.model.patch.FHIRJsonPatch;
import com.ibm.watsonhealth.fhir.model.patch.FHIRPatch;
import com.ibm.watsonhealth.fhir.model.resource.Bundle;
import com.ibm.watsonhealth.fhir.model.resource.CapabilityStatement;
import com.ibm.watsonhealth.fhir.model.resource.CapabilityStatement.Rest;
import com.ibm.watsonhealth.fhir.model.resource.CapabilityStatement.Rest.Resource.Interaction;
import com.ibm.watsonhealth.fhir.model.resource.OperationOutcome;
import com.ibm.watsonhealth.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.watsonhealth.fhir.model.resource.Parameters;
import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.model.resource.SearchParameter;
import com.ibm.watsonhealth.fhir.model.type.BundleType;
import com.ibm.watsonhealth.fhir.model.type.Canonical;
import com.ibm.watsonhealth.fhir.model.type.CapabilityStatementKind;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Coding;
import com.ibm.watsonhealth.fhir.model.type.ConditionalDeleteStatus;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.FHIRVersion;
import com.ibm.watsonhealth.fhir.model.type.HTTPVerb;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.IssueSeverity;
import com.ibm.watsonhealth.fhir.model.type.IssueType;
import com.ibm.watsonhealth.fhir.model.type.PublicationStatus;
import com.ibm.watsonhealth.fhir.model.type.ResourceType;
import com.ibm.watsonhealth.fhir.model.type.RestfulCapabilityMode;
import com.ibm.watsonhealth.fhir.model.type.SystemRestfulInteraction;
import com.ibm.watsonhealth.fhir.model.type.TypeRestfulInteraction;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.type.Url;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.model.validation.FHIRValidator;
import com.ibm.watsonhealth.fhir.model.validation.exception.FHIRValidationException;
import com.ibm.watsonhealth.fhir.operation.FHIROperation;
import com.ibm.watsonhealth.fhir.operation.context.FHIROperationContext;
import com.ibm.watsonhealth.fhir.operation.registry.FHIROperationRegistry;
import com.ibm.watsonhealth.fhir.operation.util.FHIROperationUtil;
import com.ibm.watsonhealth.fhir.persistence.FHIRPersistence;
import com.ibm.watsonhealth.fhir.persistence.FHIRPersistenceTransaction;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRHistoryContext;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceNotSupportedException;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceResourceDeletedException;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceResourceNotFoundException;
import com.ibm.watsonhealth.fhir.persistence.helper.FHIRPersistenceHelper;
import com.ibm.watsonhealth.fhir.persistence.helper.FHIRTransactionHelper;
import com.ibm.watsonhealth.fhir.persistence.helper.PersistenceHelper;
import com.ibm.watsonhealth.fhir.persistence.interceptor.FHIRPersistenceEvent;
import com.ibm.watsonhealth.fhir.persistence.interceptor.impl.FHIRPersistenceInterceptorMgr;
import com.ibm.watsonhealth.fhir.persistence.util.FHIRPersistenceUtil;
import com.ibm.watsonhealth.fhir.rest.FHIRResourceHelpers;
import com.ibm.watsonhealth.fhir.rest.FHIRRestOperationResponse;
import com.ibm.watsonhealth.fhir.search.SearchConstants;
import com.ibm.watsonhealth.fhir.search.context.FHIRSearchContext;
import com.ibm.watsonhealth.fhir.search.exception.FHIRSearchException;
import com.ibm.watsonhealth.fhir.search.parameters.Parameter;
import com.ibm.watsonhealth.fhir.search.parameters.ParameterValue;
import com.ibm.watsonhealth.fhir.search.util.SearchUtil;
import com.ibm.watsonhealth.fhir.server.FHIRBuildIdentifier;
import com.ibm.watsonhealth.fhir.server.annotation.PATCH;
import com.ibm.watsonhealth.fhir.server.exception.FHIRHttpException;
import com.ibm.watsonhealth.fhir.server.exception.FHIRRestBundledRequestException;
import com.ibm.watsonhealth.fhir.server.exception.FHIRVirtualResourceTypeException;
import com.ibm.watsonhealth.fhir.server.helper.FHIRUrlParser;
import com.ibm.watsonhealth.fhir.server.listener.FHIRServletContextListener;
import com.ibm.watsonhealth.fhir.server.util.IssueTypeToHttpStatusMapper;
import com.ibm.watsonhealth.fhir.server.util.ReferenceMappingVisitor;
import com.ibm.watsonhealth.fhir.server.util.RestAuditLogger;

@Path("/")
@Produces({ MediaType.APPLICATION_FHIR_JSON, MediaType.APPLICATION_JSON, MediaType.APPLICATION_FHIR_XML, MediaType.APPLICATION_XML })
@Consumes({ MediaType.APPLICATION_FHIR_JSON, MediaType.APPLICATION_JSON, MediaType.APPLICATION_FHIR_XML, MediaType.APPLICATION_XML })
public class FHIRResource implements FHIRResourceHelpers {
    private static final Logger log = java.util.logging.Logger.getLogger(FHIRResource.class.getName());

    private static final String FHIR_SERVER_NAME = "IBM Watson Health Cloud FHIR Server";
    private static final String FHIR_COPY_RIGHT = "(c) Copyright IBM Corporation 2018, 2019";
    private static final String EXTENSION_URL = "http://ibm.com/watsonhealth/fhir/extension";
    private static final String BASIC_RESOURCE_TYPE_URL = "http://ibm.com/watsonhealth/fhir/basic-resource-type";
    private static final String REQUEST_HEADER_URL = "http://www.ibm.com/watsonhealth/fhir/extensions/http-request-header";

    private static final String LOCAL_REF_PREFIX = "urn:";
    private static final String HEADERNAME_IF_NONE_EXIST = "If-None-Exist";

    private PersistenceHelper persistenceHelper = null;
    private FHIRPersistence persistence = null;

    @Context
    private ServletContext context;

    @Context
    private HttpServletRequest httpServletRequest;

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpHeaders httpHeaders;

    @Context
    private SecurityContext securityContext;

    private PropertyGroup fhirConfig = null;

    // These values are used for correlating requests within a bundle.
    private String bundleTransactionCorrelationId = null;
    private String bundleRequestCorrelationId = null;

    /**
     * This method will do a quick check of the "initCompleted" flag in the servlet context. If the flag is FALSE, then
     * we'll throw an error to short-circuit the current in-progress REST API invocation.
     */
    private void checkInitComplete() throws FHIRHttpException {
        Boolean fhirServerInitComplete = (Boolean) context.getAttribute(FHIRServletContextListener.FHIR_SERVER_INIT_COMPLETE);
        if (Boolean.FALSE.equals(fhirServerInitComplete)) {
            String msg = "The FHIR Server web application cannot process requests because it did not initialize correctly";
            throw new FHIRHttpException(msg, Status.INTERNAL_SERVER_ERROR);
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
        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        String errMsg = "Caught exception while processing 'metadata' request.";

        try {
            checkInitComplete();

            status = Response.Status.OK;
            RestAuditLogger.logMetadata(httpServletRequest, startTime, new Date(), status);

            return Response.ok().entity(getCapabilityStatement()).build();
        } catch (FHIRHttpException e) {
            log.log(Level.SEVERE, errMsg, e);
            status = e.getHttpStatus();
            return exceptionResponse(e);
        } catch (Exception e) {
            log.log(Level.SEVERE, errMsg, e);
            return exceptionResponse(e, status);
        } finally {
            log.exiting(this.getClass().getName(), "metadata()");
        }
    }

    @POST
    @Path("{type}")
    public Response create(@PathParam("type") String type, Resource resource) {

        log.entering(this.getClass().getName(), "create(Resource)");

        try {
            checkInitComplete();

            String ifNoneExist = httpHeaders.getHeaderString(HEADERNAME_IF_NONE_EXIST);

            FHIRRestOperationResponse ior = doCreate(type, resource, ifNoneExist, null);

            ResponseBuilder response = Response.created(toUri(getAbsoluteUri(getRequestBaseUri(), ior.getLocationURI().toString())));
            resource = ior.getResource();
            response = addHeaders(response, resource);
            response.status(ior.getStatus());
            return response.build();
        } catch (FHIRHttpException e) {
            return exceptionResponse(e);
        } catch (FHIROperationException e) {
            return exceptionResponse(e);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "create(Resource)");
        }
    }

    @PUT
    @Path("{type}/{id}")
    public Response update(@PathParam("type") String type, @PathParam("id") String id, Resource resource) {
        Response.Status status;

        log.entering(this.getClass().getName(), "update(String,String,Resource)");
        FHIRRestOperationResponse ior = null;
        try {
            checkInitComplete();

            ior = doUpdate(type, id, resource, httpHeaders.getHeaderString(HttpHeaders.IF_MATCH), null, null);

            ResponseBuilder response = Response.ok().location(toUri(getAbsoluteUri(getRequestBaseUri(), ior.getLocationURI().toString())));
            status = ior.getStatus();
            response.status(status);

            response = addHeaders(response, ior.getResource());
            return response.build();
        } catch (FHIRPersistenceResourceNotFoundException e) {
            return exceptionResponse(e, Response.Status.METHOD_NOT_ALLOWED);
        } catch (FHIRHttpException e) {
            return exceptionResponse(e);
        } catch (FHIROperationException e) {
            return exceptionResponse(e);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "update(String,String,Resource)");
        }
    }
    
    @PUT
    @Path("{type}")
    public Response conditionalUpdate(@PathParam("type") String type, Resource resource) {
        Date startTime = new Date();
        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;

        log.entering(this.getClass().getName(), "conditionalUpdate(String,Resource)");

        FHIRRestOperationResponse ior = null;
        boolean createAuditLogRecord = false;

        try {
            checkInitComplete();

            String searchQueryString = httpServletRequest.getQueryString();
            if (searchQueryString == null || searchQueryString.isEmpty()) {
                createAuditLogRecord = true;
                String msg = "Cannot PUT to resource type endpoint unless a search query string is provided for a conditional update.";
                throw buildRestException(msg, Status.BAD_REQUEST, IssueType.ValueSet.INVALID);
            }

            ior = doUpdate(type, null, resource, httpHeaders.getHeaderString(HttpHeaders.IF_MATCH), searchQueryString, null);

            ResponseBuilder response = Response.ok().location(toUri(getAbsoluteUri(getRequestBaseUri(), ior.getLocationURI().toString())));
            status = ior.getStatus();
            response.status(status);

            response = addHeaders(response, ior.getResource());

            if (createAuditLogRecord) {
                if (status == Response.Status.CREATED) {
                    RestAuditLogger.logCreate(httpServletRequest, (ior != null ? ior.getResource() : null), startTime, new Date(), status);
                } else {
                    RestAuditLogger.logUpdate(httpServletRequest, (ior != null ? ior.getPrevResource() : null), (ior != null ? ior.getResource() : null), startTime, new Date(), status);
                }
            }

            return response.build();
        } catch (FHIRPersistenceResourceNotFoundException e) {
            status = Response.Status.METHOD_NOT_ALLOWED;
            return exceptionResponse(e, status);
        } catch (FHIRHttpException e) {
            status = e.getHttpStatus();
            return exceptionResponse(e);
        } catch (FHIROperationException e) {
            status = IssueTypeToHttpStatusMapper.issueListToStatus(e.getIssues());
            return exceptionResponse(e);
        } catch (Exception e) {
            return exceptionResponse(e, status);
        } finally {
            log.exiting(this.getClass().getName(), "conditionalUpdate(String,Resource)");
        }
    }

    @PATCH
    @Produces({ MediaType.APPLICATION_FHIR_JSON, MediaType.APPLICATION_JSON })
    @Consumes(MediaType.APPLICATION_JSON_PATCH)
    @Path("{type}/{id}")
    public Response patch(@PathParam("type") String type, @PathParam("id") String id, JsonArray array) {
        Response.Status status;
        
        log.entering(this.getClass().getName(), "patch(String,String,JsonArray)");
        FHIRRestOperationResponse ior = null;
        try {
            checkInitComplete();
            
            FHIRPatch patch = createPatch(array);
            
            ior = doPatch(type, id, patch, httpHeaders.getHeaderString(HttpHeaders.IF_MATCH), null, null);

            ResponseBuilder response = Response.ok().location(toUri(getAbsoluteUri(getRequestBaseUri(), ior.getLocationURI().toString())));
            status = ior.getStatus();
            response.status(status);

            response = addHeaders(response, ior.getResource());
            return response.build();
        } catch (FHIRPersistenceResourceNotFoundException e) {
            return exceptionResponse(e, Response.Status.METHOD_NOT_ALLOWED);
        } catch (FHIRHttpException e) {
            return exceptionResponse(e);
        } catch (FHIROperationException e) {
            return exceptionResponse(e);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "patch(String,String,JsonArray)");
        }
    }
    
    @PATCH
    @Produces({ MediaType.APPLICATION_FHIR_JSON, MediaType.APPLICATION_JSON })
    @Consumes(MediaType.APPLICATION_JSON_PATCH)
    @Path("{type}")
    public Response conditionalPatch(@PathParam("type") String type, JsonArray array) {
        Date startTime = new Date();
        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;

        log.entering(this.getClass().getName(), "conditionalPatch(String,String,JsonArray)");

        FHIRRestOperationResponse ior = null;
        boolean createAuditLogRecord = false;

        try {
            checkInitComplete();
            
            FHIRPatch patch = createPatch(array);

            String searchQueryString = httpServletRequest.getQueryString();
            if (searchQueryString == null || searchQueryString.isEmpty()) {
                createAuditLogRecord = true;
                String msg = "Cannot PATCH to resource type endpoint unless a search query string is provided for a conditional patch.";
                throw buildRestException(msg, Status.BAD_REQUEST, IssueType.ValueSet.INVALID);
            }

            ior = doPatch(type, null, patch, httpHeaders.getHeaderString(HttpHeaders.IF_MATCH), searchQueryString, null);

            ResponseBuilder response = Response.ok().location(toUri(getAbsoluteUri(getRequestBaseUri(), ior.getLocationURI().toString())));
            status = ior.getStatus();
            response.status(status);

            response = addHeaders(response, ior.getResource());

            if (createAuditLogRecord) {
                RestAuditLogger.logPatch(httpServletRequest, (ior != null ? ior.getPrevResource() : null), (ior != null ? ior.getResource() : null), startTime, new Date(), status);
            }

            return response.build();
        } catch (FHIRPersistenceResourceNotFoundException e) {
            status = Response.Status.METHOD_NOT_ALLOWED;
            return exceptionResponse(e, status);
        } catch (FHIRHttpException e) {
            status = e.getHttpStatus();
            return exceptionResponse(e);
        } catch (FHIROperationException e) {
            status = IssueTypeToHttpStatusMapper.issueListToStatus(e.getIssues());
            return exceptionResponse(e);
        } catch (Exception e) {
            return exceptionResponse(e, status);
        } finally {
            log.exiting(this.getClass().getName(), "conditionalPatch(String,String,JsonArray)");
        }
    }
        
    private FHIRPatch createPatch(JsonArray array) throws FHIRHttpException {
        try {
            FHIRPatch patch = FHIRPatch.patch(array);
            JsonPatch jsonPatch = patch.as(FHIRJsonPatch.class).getJsonPatch();
            for (JsonValue value : jsonPatch.toJsonArray()) {
                // validate path
                String path = value.asJsonObject().getString("path");
                if ("/id".equals(path) || 
                    "/meta/versionId".equals(path) || 
                    "/meta/lastUpdated".equals(path)) {
                    throw new IllegalArgumentException("Path: '" + path + "' is not allowed in a patch operation.");
                }
            }
            return patch;
        } catch (Exception e) {
            throw new FHIRHttpException("Invalid patch: " + e.getMessage(), Response.Status.BAD_REQUEST, e);
        }
    }

    @DELETE
    @Path("{type}/{id}")
    public Response delete(@PathParam("type") String type, @PathParam("id") String id) throws Exception {
        log.entering(this.getClass().getName(), "delete(String,String)");

        FHIRRestOperationResponse ior = null;

        try {
            checkInitComplete();

            ior = doDelete(type, id, null, null);
            ResponseBuilder response = Response.noContent();
            if (ior.getResource() != null) {
                response = addHeaders(response, ior.getResource());
            }
            return response.build();
        } catch (FHIRHttpException e) {
            return exceptionResponse(e);
        } catch (FHIRPersistenceResourceNotFoundException e) {
            return exceptionResponse(e, Response.Status.NOT_FOUND);
        } catch (FHIRPersistenceNotSupportedException e) {
            return exceptionResponse(e, Response.Status.METHOD_NOT_ALLOWED);
        } catch (FHIROperationException e) {
            return exceptionResponse(e);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "delete(String,String)");
        }
    }

    @DELETE
    @Path("{type}")
    public Response conditionalDelete(@PathParam("type") String type) throws Exception {
        log.entering(this.getClass().getName(), "conditionalDelete(String,String)");

        Date startTime = new Date();
        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        FHIRRestOperationResponse ior = null;
        boolean createAuditLogRecord = false;

        try {
            checkInitComplete();

            String searchQueryString = httpServletRequest.getQueryString();
            if (searchQueryString == null || searchQueryString.isEmpty()) {
                createAuditLogRecord = true;
                String msg = "A search query string is required for a conditional delete operation.";
                throw buildRestException(msg, status, IssueType.ValueSet.INVALID);
            }

            ior = doDelete(type, null, searchQueryString, null);
            ResponseBuilder response = Response.noContent();
            status = ior.getStatus();
            if (ior.getResource() != null) {
                response = addHeaders(response, ior.getResource());
            }
            return response.build();
        } catch (FHIRHttpException e) {
            status = e.getHttpStatus();
            return exceptionResponse(e);
        } catch (FHIRPersistenceResourceNotFoundException e) {
            status = Response.Status.NOT_FOUND;
            return exceptionResponse(e, status);
        } catch (FHIRPersistenceNotSupportedException e) {
            status = Response.Status.METHOD_NOT_ALLOWED;
            return exceptionResponse(e, status);
        } catch (FHIROperationException e) {
            status = IssueTypeToHttpStatusMapper.issueListToStatus(e.getIssues());
            return exceptionResponse(e);
        } catch (Exception e) {
            return exceptionResponse(e, status);
        } finally {
            if (createAuditLogRecord) {
                RestAuditLogger.logDelete(httpServletRequest, ior != null ? ior.getResource() : null, startTime, new Date(), status);
            }
            log.exiting(this.getClass().getName(), "delete(String,String)");
        }
    }

    @GET
    @Path("{type}/{id}")
    public Response read(@PathParam("type") String type, @PathParam("id") String id) throws Exception {
        log.entering(this.getClass().getName(), "read(String,String)");

        try {
            checkInitComplete();

            Resource resource = doRead(type, id, true, false, null, null);
            ResponseBuilder response = Response.ok().entity(resource);
            response = addHeaders(response, resource);
            return response.build();
        } catch (FHIRHttpException e) {
            return exceptionResponse(e);
        } catch (FHIRPersistenceResourceNotFoundException e) {
            return exceptionResponse(e, Response.Status.NOT_FOUND);
        } catch (FHIRPersistenceResourceDeletedException e) {
            return exceptionResponse(e, Response.Status.GONE);
        } catch (FHIROperationException e) {
            return exceptionResponse(e);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "read(String,String)");
        }
    }

    @GET
    @Path("{type}/{id}/_history/{vid}")
    public Response vread(@PathParam("type") String type, @PathParam("id") String id, @PathParam("vid") String vid) {

        log.entering(this.getClass().getName(), "vread(String,String,String)");

        try {
            checkInitComplete();

            Resource resource = doVRead(type, id, vid, null);
            ResponseBuilder response = Response.ok().entity(resource);
            response = addHeaders(response, resource);
            return response.build();
        } catch (FHIRHttpException e) {
            return exceptionResponse(e);
        } catch (FHIRPersistenceResourceNotFoundException e) {
            return exceptionResponse(e, Response.Status.NOT_FOUND);
        } catch (FHIRPersistenceResourceDeletedException e) {
            return exceptionResponse(e, Response.Status.GONE);
        } catch (FHIROperationException e) {
            return exceptionResponse(e);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "vread(String,String,String)");
        }
    }

    @GET
    @Path("{type}/{id}/_history")
    public Response history(@PathParam("type") String type, @PathParam("id") String id) {
        log.entering(this.getClass().getName(), "history(String,String)");

        try {
            checkInitComplete();

            Bundle bundle = doHistory(type, id, uriInfo.getQueryParameters(), getRequestUri(), null);
            return Response.ok(bundle).build();
        } catch (FHIRHttpException e) {
            return exceptionResponse(e);
        } catch (FHIROperationException e) {
            return exceptionResponse(e);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "history(String,String)");
        }
    }

    @GET
    @Path("{type}")
    public Response search(@PathParam("type") String type) {

        log.entering(this.getClass().getName(), "search(String,UriInfo)");
        Response.Status status;
        MultivaluedMap<String, String> queryParameters = null;
        Bundle bundle = null;

        try {
            checkInitComplete();

            queryParameters = uriInfo.getQueryParameters();
            bundle = doSearch(type, null, null, queryParameters, getRequestUri(), null, null);
            status = Response.Status.OK;
            return Response.ok(bundle).build();
        } catch (FHIRHttpException e) {
            status = e.getHttpStatus();
            return exceptionResponse(e);
        } catch (FHIROperationException e) {
            status = IssueTypeToHttpStatusMapper.issueListToStatus(e.getIssues());
            return exceptionResponse(e);
        } catch (Exception e) {
            status = Response.Status.INTERNAL_SERVER_ERROR;
            return exceptionResponse(e, status);
        } finally {
            log.exiting(this.getClass().getName(), "search(String)");
        }
    }

    @GET
    @Path("{compartment}/{compartmentId}/{type}")
    public Response searchCompartment(@PathParam("compartment") String compartment, @PathParam("compartmentId") String compartmentId,
        @PathParam("type") String type) {

        log.entering(this.getClass().getName(), "search(String, String, String)");

        try {
            checkInitComplete();

            MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
            Bundle bundle = doSearch(type, compartment, compartmentId, queryParameters, getRequestUri(), null, null);
            return Response.ok(bundle).build();
        } catch (FHIRHttpException e) {
            return exceptionResponse(e);
        } catch (FHIROperationException e) {
            return exceptionResponse(e);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "search(String)");
        }
    }

    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Path("{type}/_search")
    public Response _search(@PathParam("type") String type) {
        log.entering(this.getClass().getName(), "_search(String)");

        try {
            checkInitComplete();

            MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
            Bundle bundle = doSearch(type, null, null, queryParameters, getRequestUri(), null, null);
            return Response.ok(bundle).build();
        } catch (FHIRHttpException e) {
            return exceptionResponse(e);
        } catch (FHIROperationException e) {
            return exceptionResponse(e);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "_search(String)");
        }
    }

    @GET
    @Path("_search")
    public Response searchAll() {
        log.entering(this.getClass().getName(), "searchAll()");

        try {
            checkInitComplete();

            MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
            Bundle bundle = doSearch("Resource", null, null, queryParameters, getRequestUri(), null, null);
            return Response.ok(bundle).build();
        } catch (FHIRHttpException e) {
            return exceptionResponse(e);
        } catch (FHIROperationException e) {
            return exceptionResponse(e);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "searchAll()");
        }
    }

    @GET
    @Path("${operationName}")
    public Response invoke(@PathParam("operationName") String operationName) {
        log.entering(this.getClass().getName(), "invoke(String)");
        try {
            checkInitComplete();

            FHIROperationContext operationContext = FHIROperationContext.createSystemOperationContext();
            Resource result = doInvoke(operationContext, null, null, null, operationName, null, uriInfo.getQueryParameters(), null);
            return buildResponse(operationContext, result);
        } catch (FHIRHttpException e) {
            return exceptionResponse(e);
        } catch (FHIROperationException e) {
            return exceptionResponse(e);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "invoke(String)");
        }
    }

    @POST
    @Path("${operationName}")
    public Response invoke(@PathParam("operationName") String operationName, Resource resource) {
        log.entering(this.getClass().getName(), "invoke(String,Resource)");
        try {
            checkInitComplete();

            FHIROperationContext operationContext = FHIROperationContext.createSystemOperationContext();
            Resource result = doInvoke(operationContext, null, null, null, operationName, resource, uriInfo.getQueryParameters(), null);
            return buildResponse(operationContext, result);
        } catch (FHIRHttpException e) {
            return exceptionResponse(e);
        } catch (FHIROperationException e) {
            return exceptionResponse(e);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "invoke(String,Resource)");
        }
    }

    @GET
    @Path("{resourceTypeName}/${operationName}")
    public Response invoke(@PathParam("resourceTypeName") String resourceTypeName, @PathParam("operationName") String operationName) {
        log.entering(this.getClass().getName(), "invoke(String,String)");
        try {
            checkInitComplete();

            FHIROperationContext operationContext = FHIROperationContext.createResourceTypeOperationContext();
            Resource result = doInvoke(operationContext, resourceTypeName, null, null, operationName, null, uriInfo.getQueryParameters(), null);
            return buildResponse(operationContext, result);
        } catch (FHIRHttpException e) {
            return exceptionResponse(e);
        } catch (FHIROperationException e) {
            return exceptionResponse(e);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "invoke(String,String)");
        }
    }

    @POST
    @Path("{resourceTypeName}/${operationName}")
    public Response invoke(@PathParam("resourceTypeName") String resourceTypeName, @PathParam("operationName") String operationName, Resource resource) {
        log.entering(this.getClass().getName(), "invoke(String,String,Resource)");
        try {
            checkInitComplete();

            FHIROperationContext operationContext = FHIROperationContext.createResourceTypeOperationContext();
            Resource result = doInvoke(operationContext, resourceTypeName, null, null, operationName, resource, uriInfo.getQueryParameters(), null);
            return buildResponse(operationContext, result);
        } catch (FHIRHttpException e) {
            return exceptionResponse(e);
        } catch (FHIROperationException e) {
            // response 200 OK if no failure issue found.
            boolean isFailure = false;
            for (Issue issue: e.getIssues())
            {
                if (FHIRUtil.isFailure(issue.getSeverity())) {
                    isFailure = true;
                    break;
                }
            }
            if (isFailure) {
                return exceptionResponse(e);
            } else {
                return exceptionResponse(e, Response.Status.OK);
            }
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "invoke(String,String,Resource)");
        }
    }

    @GET
    @Path("{resourceTypeName}/{logicalId}/${operationName}")
    public Response invoke(@PathParam("resourceTypeName") String resourceTypeName, @PathParam("logicalId") String logicalId,
        @PathParam("operationName") String operationName) {
        log.entering(this.getClass().getName(), "invoke(String,String,String)");
        try {
            checkInitComplete();

            FHIROperationContext operationContext = FHIROperationContext.createInstanceOperationContext();
            Resource result = doInvoke(operationContext, resourceTypeName, logicalId, null, operationName, null, uriInfo.getQueryParameters(), null);
            return buildResponse(operationContext, result);
        } catch (FHIRHttpException e) {
            return exceptionResponse(e);
        } catch (FHIROperationException e) {
            return exceptionResponse(e);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "invoke(String,String,String)");
        }
    }

    @POST
    @Path("{resourceTypeName}/{logicalId}/${operationName}")
    public Response invoke(@PathParam("resourceTypeName") String resourceTypeName, @PathParam("logicalId") String logicalId,
        @PathParam("operationName") String operationName, Resource resource) {
        log.entering(this.getClass().getName(), "invoke(String,String,String,Resource)");
        try {
            checkInitComplete();

            FHIROperationContext operationContext = FHIROperationContext.createInstanceOperationContext();
            Resource result = doInvoke(operationContext, resourceTypeName, logicalId, null, operationName, resource, uriInfo.getQueryParameters(), null);
            return buildResponse(operationContext, result);
        } catch (FHIRHttpException e) {
            return exceptionResponse(e);
        } catch (FHIROperationException e) {
            return exceptionResponse(e);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "invoke(String,String,String,Resource)");
        }
    }

    @GET
    @Path("{resourceTypeName}/{logicalId}/_history/{versionId}/${operationName}")
    public Response invoke(@PathParam("resourceTypeName") String resourceTypeName, @PathParam("logicalId") String logicalId,
        @PathParam("versionId") String versionId, @PathParam("operationName") String operationName) {
        log.entering(this.getClass().getName(), "invoke(String,String,String,String)");
        try {
            checkInitComplete();

            FHIROperationContext operationContext = FHIROperationContext.createInstanceOperationContext();
            Resource result = doInvoke(operationContext, resourceTypeName, logicalId, versionId, operationName, null, uriInfo.getQueryParameters(), null);
            return buildResponse(operationContext, result);
        } catch (FHIRHttpException e) {
            return exceptionResponse(e);
        } catch (FHIROperationException e) {
            return exceptionResponse(e);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "invoke(String,String,String,String)");
        }
    }

    @POST
    @Path("{resourceTypeName}/{logicalId}/_history/{versionId}/${operationName}")
    public Response invoke(@PathParam("resourceTypeName") String resourceTypeName, @PathParam("logicalId") String logicalId,
        @PathParam("versionId") String versionId, @PathParam("operationName") String operationName, Resource resource) {
        log.entering(this.getClass().getName(), "invoke(String,String,String,String,Resource)");
        try {
            checkInitComplete();

            FHIROperationContext operationContext = FHIROperationContext.createInstanceOperationContext();
            Resource result = doInvoke(operationContext, resourceTypeName, logicalId, versionId, operationName, resource, uriInfo.getQueryParameters(), null);
            return buildResponse(operationContext, result);
        } catch (FHIRHttpException e) {
            return exceptionResponse(e);
        } catch (FHIROperationException e) {
            return exceptionResponse(e);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "invoke(String,String,String,String,Resource)");
        }
    }

    @POST
    public Response bundle(Resource resource) {

        log.entering(this.getClass().getName(), "bundle(Bundle)");

        try {
            checkInitComplete();

            Bundle responseBundle = doBundle(resource, null);
            ResponseBuilder response = Response.ok(responseBundle);
            return response.build();
        } catch (FHIRRestBundledRequestException e) {
            return exceptionResponse(e);
        } catch (FHIRHttpException e) {
            return exceptionResponse(e);
        } catch (FHIROperationException e) {
            return exceptionResponse(e);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "bundle(Bundle)");
        }
    }

    /**
     * Performs the heavy lifting associated with a 'create' interaction.
     * 
     * @param type
     *            the resource type specified as part of the request URL
     * @param resource
     *            the Resource to be stored.
     * @param requestProperties
     *            additional request properties which supplement the HTTP headers associated with this request
     * @return a FHIRRestOperationResponse object containing the results of the operation
     * @throws Exception
     */
    public FHIRRestOperationResponse doCreate(String type, Resource resource, String ifNoneExist, Map<String, String> requestProperties) throws Exception {
        log.entering(this.getClass().getName(), "doCreate");

        FHIRTransactionHelper txn = new FHIRTransactionHelper(getTransaction());
        Date startTime = new Date();
        Response.Status status = null;
        String errMsg = "Caught exception while processing 'create' request.";

        FHIRRestOperationResponse ior = new FHIRRestOperationResponse();

        // Save the current request context.
        FHIRRequestContext requestContext = FHIRRequestContext.get();

        // Pass end time the same as start time to tell cadf log service that this is a pending request.
        RestAuditLogger.logCreate(httpServletRequest, resource, startTime, startTime, Response.Status.OK);

        try {

            // Make sure the expected type (specified in the URL string) is congruent with the actual type
            // of the resource.
            String resourceType = FHIRUtil.getResourceTypeName(resource);
            if (!resourceType.equals(type)) {
                String msg = "Resource type '" + resourceType + "' does not match type specified in request URI: " + type;
                throw buildRestException(msg, Status.BAD_REQUEST, IssueType.ValueSet.INVALID);
            }

            // Check to see if we're supposed to perform a conditional 'create'.
            if (ifNoneExist != null && !ifNoneExist.isEmpty()) {
                log.fine("Performing conditional create with search criteria: " + ifNoneExist);
                Bundle responseBundle = null;

                // Perform the search using the "If-None-Exist" header value.
                try {
                    MultivaluedMap<String, String> searchParameters = getQueryParameterMap(ifNoneExist);
                    responseBundle = doSearch(type, null, null, searchParameters, null, requestProperties, resource);
                } catch (FHIROperationException e) {
                    throw e;
                } catch (Throwable t) {
                    String msg = "An error occurred while performing the search for a conditional create operation.";
                    log.log(Level.SEVERE, msg, t);
                    throw new FHIROperationException(msg, t);
                }

                // Check the search results to determine whether or not to perform the create operation.
                int resultCount = responseBundle.getEntry().size();
                log.fine("Conditional create search yielded " + resultCount + " results.");

                if (resultCount == 0) {
                    // Do nothing and fall through to process the 'create' request.
                } else if (resultCount == 1) {
                    // If we found a single match, bypass the 'create' request and return information
                    // for the matched resource.
                    Resource matchedResource = responseBundle.getEntry().get(0).getResource();
                    ior.setLocationURI(FHIRUtil.buildLocationURI(type, matchedResource));
                    ior.setStatus(Response.Status.OK);
                    ior.setResource(matchedResource);
                    log.fine("Returning location URI of matched resource: " + ior.getLocationURI());
                    status = ior.getStatus();
                    return ior;
                } else {
                    String msg = "The search criteria specified for a conditional create operation returned multiple matches.";
                    throw buildRestException(msg, Status.PRECONDITION_FAILED, IssueType.ValueSet.DUPLICATE);
                }
            }

            // For R4, resources may contain an id. For create, this should be ignored and
            // we no longer reject the request.
            if (resource.getId() != null && log.isLoggable(Level.FINE)) {
                log.fine(String.format("create request resource includes id: '%s'", resource.getId().getValue()));
            }

            // Validate the input resource and return any validation errors, but warnings are OK
            validateInput(resource);

            // If there were no validation errors, then create the resource and return the location header.

            // Start a new txn in the persistence layer if one is not already active.
            txn.begin();

            // First, invoke the 'beforeCreate' interceptor methods.
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(resource, buildPersistenceEventProperties(type, null, null, requestProperties));
            getInterceptorMgr().fireBeforeCreateEvent(event);

            FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(event);

            // R4: remember model objects are immutable, so we get back a new resource with the id/meta stuff
            resource = getPersistenceImpl().create(persistenceContext, resource);
            event.setFhirResource(resource); // update event with latest
            ior.setStatus(Response.Status.CREATED);
            ior.setResource(resource);

            // Build our location URI and add it to the interceptor event structure since it is now known.
            ior.setLocationURI(FHIRUtil.buildLocationURI(FHIRUtil.getResourceTypeName(resource), resource));
            event.getProperties().put(FHIRPersistenceEvent.PROPNAME_RESOURCE_LOCATION_URI, ior.getLocationURI().toString());

            // Invoke the 'afterCreate' interceptor methods.
            getInterceptorMgr().fireAfterCreateEvent(event);

            // Commit our transaction if we started one before.
            txn.commit();
            txn = null;

            status = ior.getStatus();
            return ior;
        } catch (FHIRHttpException e) {
            log.log(Level.SEVERE, errMsg, e);
            status = e.getHttpStatus();
            throw e;
        } catch (FHIROperationException e) {
            log.log(Level.SEVERE, errMsg, e);
            status = IssueTypeToHttpStatusMapper.issueListToStatus(e.getIssues());
            throw e;
        } catch (Throwable t) {
            log.log(Level.SEVERE, errMsg, t);
            status = Response.Status.INTERNAL_SERVER_ERROR;
            throw t;
        } finally {
            // Restore the original request context.
            FHIRRequestContext.set(requestContext);

            // If we previously started a transaction and it's still active, we need to rollback due to an error.
            if (txn != null) {
                txn.rollback();
            }

            // Now Audit log the final status of the request,
            // if fails to log, then log the error in local log file and ignore.
            try {
                RestAuditLogger.logCreate(httpServletRequest, resource, startTime, new Date(), status);
            } catch (Exception e) {
                log.log(Level.INFO, errMsg, e);
            }
            log.exiting(this.getClass().getName(), "doCreate");
        }
    }

    /**
     * Validate the input resource and throw if there are validation errors
     *
     * @param resource
     * @throws FHIRValidationException
     * @throws FHIRHttpException
     */
    private List<OperationOutcome.Issue> validateInput(Resource resource) throws FHIRValidationException, FHIRHttpException {
        List<OperationOutcome.Issue> issues = FHIRValidator.validator(resource).validate();
        if (!issues.isEmpty()) {
            boolean includesFailure = false;
            for (OperationOutcome.Issue issue : issues) {
                if (FHIRUtil.isFailure(issue.getSeverity())) {
                    includesFailure = true;
                }
            }

            if (includesFailure) {
                throw new FHIRHttpException("Input resource failed validation.", Response.Status.BAD_REQUEST).withIssue(issues);
            } else {
                String info =
                        issues.stream().flatMap(issue -> Stream.of(issue.getDetails())).flatMap(details -> Stream.of(details.getText())).flatMap(text -> Stream.of(text.getValue())).collect(Collectors.joining(", "));
                log.warning("TODO: Validation warnings should be added to response: " + info);
            }
        }
        return issues;
    }
    
    @Override
    public FHIRRestOperationResponse doPatch(String type, String id, FHIRPatch patch, String ifMatchValue, String searchQueryString,
        Map<String, String> requestProperties) throws Exception {
        return doPatchOrUpdate(type, id, patch, null, ifMatchValue, searchQueryString, requestProperties);
    }

    @Override
    public FHIRRestOperationResponse doUpdate(String type, String id, Resource newResource, String ifMatchValue, String searchQueryString,
        Map<String, String> requestProperties) throws Exception {
        return doPatchOrUpdate(type, id, null, newResource, ifMatchValue, searchQueryString, requestProperties);
    }

    private FHIRRestOperationResponse doPatchOrUpdate(String type, String id, FHIRPatch patch, Resource newResource, String ifMatchValue, String searchQueryString,
        Map<String, String> requestProperties) throws Exception {
        log.entering(this.getClass().getName(), "doPatchOrUpdate");

        FHIRTransactionHelper txn = new FHIRTransactionHelper(getTransaction());
        Date startTime = new Date();
        Response.Status status = null;
        String errMsg = "Caught exception while processing 'update/patch' request.";

        // Save the current request context.
        FHIRRequestContext requestContext = FHIRRequestContext.get();

        FHIRRestOperationResponse ior = new FHIRRestOperationResponse();

        // Pass end time the same as start time to tell cadf log service that this is a pending request.
        if (patch != null) {
            RestAuditLogger.logPatch(httpServletRequest, null, null, startTime, startTime, Response.Status.OK);
        } else {
            // At this time point, we don't have the updated resource, so use the input resource as the updated resource in
            // the pending request.
            RestAuditLogger.logUpdate(httpServletRequest, newResource, newResource, startTime, startTime, Response.Status.OK);
        }

        try {
            // Make sure the type specified in the URL string matches the resource type obtained from the new resource.
            if (patch == null) {
                String resourceType = FHIRUtil.getResourceTypeName(newResource);
                if (!resourceType.equals(type)) {
                    String msg = "Resource type '" + resourceType + "' does not match type specified in request URI: " + type;
                    throw buildRestException(msg, Status.BAD_REQUEST, IssueType.ValueSet.INVALID);
                }
    
                // Make sure the resource has an 'id' attribute.
                if (newResource.getId() == null) {
                    String msg = "Input resource must contain an 'id' attribute.";
                    throw buildRestException(msg, Status.BAD_REQUEST, IssueType.ValueSet.INVALID);
                }
            }

            // Next, if a conditional update was invoked then use the search criteria to find the
            // resource to be updated. Otherwise, we'll use the id value to retrieve the current
            // version of the resource.
            if (searchQueryString != null) {
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Performing conditional update/patch with search criteria: " + Encode.forHtml(searchQueryString));
                }
                Bundle responseBundle = null;
                try {
                    MultivaluedMap<String, String> searchParameters = getQueryParameterMap(searchQueryString);
                    responseBundle = doSearch(type, null, null, searchParameters, null, requestProperties, newResource);
                } catch (FHIROperationException e) {
                    throw e;
                } catch (Throwable t) {
                    String msg = "An error occurred while performing the search for a conditional update/patch operation.";
                    log.log(Level.SEVERE, msg, t);
                    throw new FHIROperationException(msg, t);
                }

                // Check the search results to determine whether or not to perform the update operation.
                int resultCount = responseBundle.getEntry().size();
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Conditional update/patch search yielded " + resultCount + " results.");
                }

                if (resultCount == 0) {
                    if (patch != null) {
                        String msg = "The search criteria specified for a conditional patch operation did not return any results.";
                        throw buildRestException(msg, Status.NOT_FOUND, IssueType.ValueSet.NOT_FOUND);
                    }
                    // Search yielded no matches, so we'll do an update/create operation below.
                    ior.setPrevResource(null);
                    id = newResource.getId().getValue();
                } else if (resultCount == 1) {
                    // If we found a single match, then we'll perform a normal update on the matched resource.
                    ior.setPrevResource(responseBundle.getEntry().get(0).getResource());
                    id = ior.getPrevResource().getId().getValue();
                } else {
                    String msg = "The search criteria specified for a conditional update/patch operation returned multiple matches.";
                    throw buildRestException(msg, Status.PRECONDITION_FAILED, IssueType.ValueSet.DUPLICATE);
                }
            } else {
                // Make sure an id value was passed in.
                if (id == null) {
                    String msg = "The 'id' parameter is required for an update/pach operation.";
                    throw buildRestException(msg, Status.BAD_REQUEST, IssueType.ValueSet.REQUIRED);
                }

                // If an id value was passed in (i.e. the id specified in the REST API URL string),
                // then make sure it's the same as the value in the resource.
                if (patch == null) {
                    if (!newResource.getId().getValue().equals(id)) {
                        String msg = "Input resource 'id' attribute must match 'id' parameter.";
                        throw buildRestException(msg, Status.BAD_REQUEST, IssueType.ValueSet.VALUE);
                    }
                }

                // Retrieve the resource to be updated using the type and id values.
                ior.setPrevResource(doRead(type, id, (patch != null), true, requestProperties, newResource));
            }
            
            if (patch != null) {
                newResource = patch.apply(ior.getPrevResource());
            }

            // Validate the input resource and return any validation errors.
            validateInput(newResource);

            // Perform the "version-aware" update check.
            if (ior.getPrevResource() != null) {
                performVersionAwareUpdateCheck(ior.getPrevResource(), ifMatchValue);
            }

            // Start a new txn in the persistence layer if one is not already active.
            txn.begin();

            // First, create the persistence event.
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(newResource, buildPersistenceEventProperties(type, newResource.getId().getValue(), null, requestProperties));

            // Next, set the "previous resource" in the persistence event.
            event.setPrevFhirResource(ior.getPrevResource());

            // Next, invoke the 'beforeUpdate' or 'beforeCreate' interceptor methods as appropriate.
            boolean updateCreate = (ior.getPrevResource() == null);
            if (updateCreate) {
                getInterceptorMgr().fireBeforeCreateEvent(event);
            } else {
                if (patch != null) {
                    event.getProperties().put(FHIRPersistenceEvent.PROPNAME_PATCH, patch);
                    getInterceptorMgr().fireBeforePatchEvent(event);
                } else {
                    getInterceptorMgr().fireBeforeUpdateEvent(event);
                }
            }

            FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(event);
            newResource = getPersistenceImpl().update(persistenceContext, id, newResource);
            event.setFhirResource(newResource); // update event with latest
            ior.setResource(newResource);

            // Build our location URI and add it to the interceptor event structure since it is now known.
            ior.setLocationURI(FHIRUtil.buildLocationURI(FHIRUtil.getResourceTypeName(newResource), newResource));
            event.getProperties().put(FHIRPersistenceEvent.PROPNAME_RESOURCE_LOCATION_URI, ior.getLocationURI().toString());

            // Invoke the 'afterUpdate' interceptor methods.
            if (updateCreate) {
                ior.setStatus(Response.Status.CREATED);
                getInterceptorMgr().fireAfterCreateEvent(event);
            } else {
                ior.setStatus(Response.Status.OK);
                if (patch != null) {
                    getInterceptorMgr().fireAfterPatchEvent(event);
                } else {
                    getInterceptorMgr().fireAfterUpdateEvent(event);
                }
            }

            // Commit our transaction if we started one before.
            txn.commit();
            txn = null;
            status = ior.getStatus();

            return ior;
        } catch (FHIRPersistenceResourceNotFoundException e) {
            log.log(Level.SEVERE, errMsg, e);
            status = Response.Status.METHOD_NOT_ALLOWED;
            throw e;
        } catch (FHIRHttpException e) {
            log.log(Level.SEVERE, errMsg, e);
            status = e.getHttpStatus();
            throw e;
        } catch (FHIROperationException e) {
            log.log(Level.SEVERE, errMsg, e);
            status = IssueTypeToHttpStatusMapper.issueListToStatus(e.getIssues());
            throw e;
        } catch (Throwable t) {
            log.log(Level.SEVERE, errMsg, t);
            status = Response.Status.INTERNAL_SERVER_ERROR;
            throw t;
        } finally {
            // Restore the original request context.
            FHIRRequestContext.set(requestContext);

            // If we still have a transaction at this point, we need to rollback due to an error.
            if (txn != null) {
                txn.rollback();
            }

            if (status == Response.Status.CREATED) {
                // Now Audit log the final status of the request,
                // if fails to log, then log the error in local log file and ignore.
                try {
                    RestAuditLogger.logCreate(httpServletRequest, (ior != null ? ior.getResource() : null), startTime, new Date(), status);
                } catch (Exception e) {
                    log.log(Level.INFO, errMsg, e);
                }
            } else {
                // Now Audit log the final status of the request,
                // if fails to log, then log the error in local log file and ignore.
                try {
                    if (patch != null) {
                        RestAuditLogger.logPatch(httpServletRequest, (ior != null ? ior.getPrevResource() : null), (ior != null ? ior.getResource() : null), startTime, new Date(), status);
                    } else {
                        RestAuditLogger.logUpdate(httpServletRequest, (ior != null ? ior.getPrevResource() : null), (ior != null ? ior.getResource() : null), startTime, new Date(), status);
                    }
                } catch (Exception e) {
                    log.log(Level.INFO, errMsg, e);
                }
            }
            log.exiting(this.getClass().getName(), "doPatchOrUpdate");
        }
    }

    /**
     * Performs a 'delete' operation on the specified resource.
     * 
     * @param type
     *            the resource type associated with the Resource to be deleted
     * @param id
     *            the id of the Resource to be deleted
     * @param requestProperties
     *            additional request properties which supplement the HTTP headers associated with this request
     * @return a FHIRRestOperationResponse that contains the results of the operation
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public FHIRRestOperationResponse doDelete(String type, String id, String searchQueryString, Map<String, String> requestProperties) throws Exception {
        log.entering(this.getClass().getName(), "doDelete");

        // Save the current request context.
        FHIRRequestContext requestContext = FHIRRequestContext.get();
        FHIRTransactionHelper txn = new FHIRTransactionHelper(getTransaction());
        Date startTime = new Date();
        Response.Status status = null;
        String errMsg = "Caught exception while processing 'delete' request.";

        FHIRRestOperationResponse ior = new FHIRRestOperationResponse();

        // Pass end time the same as start time to tell cadf log service that this is a pending request.
        RestAuditLogger.logDelete(httpServletRequest, null, startTime, startTime, Response.Status.OK);

        try {
            String resourceTypeName = type;
            if (!FHIRUtil.isStandardResourceType(type)) {
                if (!isVirtualResourceTypesFeatureEnabled()) {
                    String msg = "The virtual resource types feature is not enabled for this server";
                    throw buildVirtualResourceException(msg, Status.BAD_REQUEST, IssueType.ValueSet.NOT_SUPPORTED);
                }
                if (!isAllowableVirtualResourceType(type)) {
                    String msg = "The virtual resource type '" + type + "' is not allowed. Allowable virtual types for this server are: "
                            + getAllowableVirtualResourceTypes().toString();
                    throw buildVirtualResourceException(msg, Status.BAD_REQUEST, IssueType.ValueSet.NOT_SUPPORTED);
                }
                resourceTypeName = "Basic";
            }

            Class<? extends Resource> resourceType = (Class<? extends Resource>) getResourceType(resourceTypeName);

            // Next, if a conditional delete was invoked then use the search criteria to find the
            // resource to be deleted. Otherwise, we'll use the id value to identify the resource
            // to be deleted.
            Resource resourceToDelete = null;
            if (searchQueryString != null) {
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Performing conditional delete with search criteria: " + Encode.forHtml(searchQueryString));
                }
                Bundle responseBundle = null;
                try {
                    MultivaluedMap<String, String> searchParameters = getQueryParameterMap(searchQueryString);
                    responseBundle = doSearch(type, null, null, searchParameters, null, requestProperties, null);
                } catch (FHIROperationException e) {
                    throw e;
                } catch (Throwable t) {
                    String msg = "An error occurred while performing the search for a conditional delete operation.";
                    log.log(Level.SEVERE, msg, t);
                    throw new FHIROperationException(msg, t);
                }

                // Check the search results to determine whether or not to perform the update operation.
                int resultCount = responseBundle.getEntry().size();
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Conditional delete search yielded " + resultCount + " results.");
                }

                if (resultCount == 0) {
                    // Search yielded no matches, so we'll return a 404 Not Found.
                    String msg = "Search criteria for a conditional delete operation yielded no matches.";
                    throw new FHIRPersistenceResourceNotFoundException(msg);
                } else if (resultCount == 1) {
                    // If we found a single match, then we'll delete this one.
                    Resource resource = responseBundle.getEntry().get(0).getResource();
                    id = resource.getId().getValue();
                    resourceToDelete = resource;
                } else {
                    String msg = "The search criteria specified for a conditional delete operation returned multiple matches.";
                    throw new FHIRHttpException(msg, Status.PRECONDITION_FAILED);
                }
            } else {
                // Make sure an id value was passed in.
                if (id == null) {
                    String msg = "The 'id' parameter is required for a delete operation.";
                    throw buildRestException(msg, Status.BAD_REQUEST, IssueType.ValueSet.REQUIRED);
                }

                // Read the resource so it will be available to the beforeDelete interceptor methods.
                try {
                    resourceToDelete = doRead(type, id, false, false, requestProperties, null);
                } catch (FHIRPersistenceResourceDeletedException e) {
                    // Absorb this exception.
                    resourceToDelete = null;
                }
            }

            // Start a new txn in the persistence layer if one is not already active.
            txn.begin();

            // First, invoke the 'beforeDelete' interceptor methods.
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(null, buildPersistenceEventProperties(type, id, null, requestProperties));
            event.setFhirResource(resourceToDelete);
            getInterceptorMgr().fireBeforeDeleteEvent(event);

            FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(event);

            Resource resource = getPersistenceImpl().delete(persistenceContext, resourceType, id);
            ior.setResource(resource);
            event.setFhirResource(resource);
            ior.setStatus(Response.Status.NO_CONTENT);

            // Invoke the 'afterDelete' interceptor methods.
            getInterceptorMgr().fireAfterDeleteEvent(event);

            // Commit our transaction if we started one before.
            txn.commit();
            txn = null;
            status = ior.getStatus();

            return ior;
        } catch (FHIRPersistenceResourceNotFoundException e) {
            log.log(Level.SEVERE, errMsg, e);
            status = Response.Status.NOT_FOUND;
            throw e;
        } catch (FHIRPersistenceNotSupportedException e) {
            log.log(Level.SEVERE, errMsg, e);
            status = Response.Status.METHOD_NOT_ALLOWED;
            throw e;
        } catch (FHIRHttpException e) {
            log.log(Level.SEVERE, errMsg, e);
            status = e.getHttpStatus();
            throw e;
        } catch (FHIROperationException e) {
            log.log(Level.SEVERE, errMsg, e);
            status = IssueTypeToHttpStatusMapper.issueListToStatus(e.getIssues());
            throw e;
        } catch (Throwable t) {
            log.log(Level.SEVERE, errMsg, t);
            status = Response.Status.INTERNAL_SERVER_ERROR;
            throw t;
        } finally {
            // Restore the original request context.
            FHIRRequestContext.set(requestContext);

            // If we previously started a transaction and it's still active, we need to rollback due to an error.
            if (txn != null) {
                txn.rollback();
            }

            // Now Audit log the final status of the request,
            // if fails to log, then log the error in local log file and ignore.
            try {
                RestAuditLogger.logDelete(httpServletRequest, ior != null ? ior.getResource() : null, startTime, new Date(), status);
            } catch (Exception e) {
                log.log(Level.INFO, errMsg, e);
            }

            log.exiting(this.getClass().getName(), "doDelete");
        }
    }

    /**
     * Performs a 'read' operation to retrieve a Resource.
     * 
     * @param type
     *            the resource type associated with the Resource to be retrieved
     * @param id
     *            the id of the Resource to be retrieved
     * @param requestProperties
     *            additional request properties which supplement the HTTP headers associated with this request
     * @return the Resource
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Resource doRead(String type, String id, boolean throwExcOnNull, boolean includeDeleted, Map<String, String> requestProperties,
        Resource contextResource) throws Exception {
        log.entering(this.getClass().getName(), "doRead");

        FHIRTransactionHelper txn = new FHIRTransactionHelper(getTransaction());
        Resource resource = null;
        Date startTime = new Date();
        Response.Status status = null;
        String errMsg = "Caught exception while processing 'read' request.";

        // Save the current request context.
        FHIRRequestContext requestContext = FHIRRequestContext.get();

        try {
            String resourceTypeName = type;
            if (!FHIRUtil.isStandardResourceType(type)) {
                if (!isVirtualResourceTypesFeatureEnabled()) {
                    String msg = "The virtual resource types feature is not enabled for this server";
                    throw buildVirtualResourceException(msg, Status.BAD_REQUEST, IssueType.ValueSet.NOT_SUPPORTED);
                }
                if (!isAllowableVirtualResourceType(type)) {
                    String msg = "The virtual resource type '" + type + "' is not allowed. Allowable virtual types for this server are: "
                            + getAllowableVirtualResourceTypes().toString();
                    throw buildVirtualResourceException(msg, Status.BAD_REQUEST, IssueType.ValueSet.NOT_SUPPORTED);
                }
                resourceTypeName = "Basic";
            }

            Class<? extends Resource> resourceType = (Class<? extends Resource>) getResourceType(resourceTypeName);

            // Start a new txn in the persistence layer if one is not already active.
            txn.begin();

            // First, invoke the 'beforeRead' interceptor methods.
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(contextResource, buildPersistenceEventProperties(type, id, null, requestProperties));
            getInterceptorMgr().fireBeforeReadEvent(event);

            FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(event, includeDeleted);
            resource = getPersistenceImpl().read(persistenceContext, resourceType, id);
            if (resource == null && throwExcOnNull) {
                throw new FHIRPersistenceResourceNotFoundException("Resource '" + type + "/" + id + "' not found.");
            }

            event.setFhirResource(resource);

            // Invoke the 'afterRead' interceptor methods.
            getInterceptorMgr().fireAfterReadEvent(event);

            // Commit our transaction if we started one before.
            txn.commit();
            txn = null;

            status = Response.Status.OK;

            return resource;
        } catch (FHIRPersistenceResourceNotFoundException e) {
            log.log(Level.SEVERE, errMsg, e);
            status = Response.Status.NOT_FOUND;
            throw e;
        } catch (FHIRPersistenceResourceDeletedException e) {
            log.log(Level.SEVERE, errMsg, e);
            status = Response.Status.GONE;
            throw e;
        } catch (FHIRHttpException e) {
            log.log(Level.SEVERE, errMsg, e);
            status = e.getHttpStatus();
            throw e;
        } catch (FHIROperationException e) {
            log.log(Level.SEVERE, errMsg, e);
            status = IssueTypeToHttpStatusMapper.issueListToStatus(e.getIssues());
            throw e;
        } catch (Throwable t) {
            log.log(Level.SEVERE, errMsg, t);
            status = Response.Status.INTERNAL_SERVER_ERROR;
            throw t;
        } finally {
            // Restore the original request context.
            FHIRRequestContext.set(requestContext);

            // If we previously started a transaction and it's still active, we need to rollback due to an error.
            if (txn != null) {
                txn.rollback();
            }

            // Now Audit log the final status of the request,
            // if fails to log, then log the error in local log file and ignore.
            try {
                RestAuditLogger.logRead(httpServletRequest, resource, startTime, new Date(), status);
            } catch (Exception e) {
                log.log(Level.INFO, errMsg, e);
            }

            log.exiting(this.getClass().getName(), "doRead");
        }
    }

    /**
     * Performs a 'vread' operation by retrieving the specified version of a Resource.
     * 
     * @param type
     *            the resource type associated with the Resource to be retrieved
     * @param id
     *            the id of the Resource to be retrieved
     * @param versionId
     *            the version id of the Resource to be retrieved
     * @param requestProperties
     *            additional request properties which supplement the HTTP headers associated with this request
     * @return the Resource
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Resource doVRead(String type, String id, String versionId, Map<String, String> requestProperties) throws Exception {
        log.entering(this.getClass().getName(), "doVRead");

        FHIRTransactionHelper txn = new FHIRTransactionHelper(getTransaction());
        Resource resource = null;
        Date startTime = new Date();
        Response.Status status = null;
        String errMsg = "Caught exception while processing 'vread' request.";

        // Save the current request context.
        FHIRRequestContext requestContext = FHIRRequestContext.get();

        try {
            String resourceTypeName = type;
            if (!FHIRUtil.isStandardResourceType(type)) {
                if (!isVirtualResourceTypesFeatureEnabled()) {
                    String msg = "The virtual resource types feature is not enabled for this server";
                    throw buildVirtualResourceException(msg, Status.BAD_REQUEST, IssueType.ValueSet.NOT_SUPPORTED);
                }
                if (!isAllowableVirtualResourceType(type)) {
                    String msg = "The virtual resource type '" + type + "' is not allowed. Allowable virtual resource types for this server are: "
                            + getAllowableVirtualResourceTypes().toString();
                    throw buildVirtualResourceException(msg, Status.BAD_REQUEST, IssueType.ValueSet.NOT_SUPPORTED);
                }
                resourceTypeName = "Basic";
            }

            Class<? extends Resource> resourceType = (Class<? extends Resource>) getResourceType(resourceTypeName);

            // Start a new txn in the persistence layer if one is not already active.
            txn.begin();

            // First, invoke the 'beforeVread' interceptor methods.
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(null, buildPersistenceEventProperties(type, id, versionId, requestProperties));
            getInterceptorMgr().fireBeforeVreadEvent(event);

            FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(event);
            resource = getPersistenceImpl().vread(persistenceContext, resourceType, id, versionId);
            if (resource == null) {
                throw new FHIRPersistenceResourceNotFoundException("Resource '" + resourceType.getSimpleName() + "/" + id + "' version " + versionId
                        + " not found.");
            }

            event.setFhirResource(resource);

            // Invoke the 'afterVread' interceptor methods.
            getInterceptorMgr().fireAfterVreadEvent(event);

            // Commit our transaction if we started one before.
            txn.commit();
            txn = null;

            status = Response.Status.OK;

            return resource;
        } catch (FHIRPersistenceResourceNotFoundException e) {
            log.log(Level.SEVERE, errMsg, e);
            status = Response.Status.NOT_FOUND;
            throw e;
        } catch (FHIRPersistenceResourceDeletedException e) {
            log.log(Level.SEVERE, errMsg, e);
            status = Response.Status.GONE;
            throw e;
        } catch (FHIRHttpException e) {
            log.log(Level.SEVERE, errMsg, e);
            status = e.getHttpStatus();
            throw e;
        } catch (FHIROperationException e) {
            log.log(Level.SEVERE, errMsg, e);
            status = IssueTypeToHttpStatusMapper.issueListToStatus(e.getIssues());
            throw e;
        } catch (Throwable t) {
            log.log(Level.SEVERE, errMsg, t);
            status = Response.Status.INTERNAL_SERVER_ERROR;
            throw t;
        } finally {
            // Restore the original request context.
            FHIRRequestContext.set(requestContext);

            // If we previously started a transaction and it's still active, we need to rollback due to an error.
            if (txn != null) {
                txn.rollback();
            }

            // Now Audit log the final status of the request,
            // if fails to log, then log the error in local log file and ignore.
            try {
                RestAuditLogger.logVersionRead(httpServletRequest, resource, startTime, new Date(), status);
            } catch (Exception e) {
                log.log(Level.INFO, errMsg, e);
            }

            log.exiting(this.getClass().getName(), "doVRead");
        }
    }

    /**
     * Performs the work of retrieving versions of a Resource.
     *
     * @param type
     *            the resource type associated with the Resource to be retrieved
     * @param id
     *            the id of the Resource to be retrieved
     * @param queryparameters
     *            a Map containing the query parameters from the request URL
     * @param requestProperties
     *            additional request properties which supplement the HTTP headers associated with this request
     * @return a Bundle containing the history of the specified Resource
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Bundle doHistory(String type, String id, MultivaluedMap<String, String> queryParameters, String requestUri, Map<String, String> requestProperties)
        throws Exception {
        log.entering(this.getClass().getName(), "doHistory");

        FHIRTransactionHelper txn = new FHIRTransactionHelper(getTransaction());
        Bundle bundle = null;
        Date startTime = new Date();
        Response.Status status = null;
        String errMsg = "Caught exception while processing 'history' request.";

        // Save the current request context.
        FHIRRequestContext requestContext = FHIRRequestContext.get();

        try {
            String resourceTypeName = type;
            if (!FHIRUtil.isStandardResourceType(type)) {
                if (!isVirtualResourceTypesFeatureEnabled()) {
                    String msg = "The virtual resource types feature is not enabled for this server";
                    throw buildVirtualResourceException(msg, Status.BAD_REQUEST, IssueType.ValueSet.NOT_SUPPORTED);
                }
                if (!isAllowableVirtualResourceType(type)) {
                    String msg = "The virtual resource type '" + type + "' is not allowed. Allowable virtual resource types for this server are: "
                            + getAllowableVirtualResourceTypes().toString();
                    throw buildVirtualResourceException(msg, Status.BAD_REQUEST, IssueType.ValueSet.NOT_SUPPORTED);
                }
                resourceTypeName = "Basic";
            }

            Class<? extends Resource> resourceType = (Class<? extends Resource>) getResourceType(resourceTypeName);
            FHIRHistoryContext historyContext = FHIRPersistenceUtil.parseHistoryParameters(queryParameters);

            // Start a new txn in the persistence layer if one is not already active.
            txn.begin();

            // First, invoke the 'beforeHistory' interceptor methods.
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(null, buildPersistenceEventProperties(type, id, null, requestProperties));
            getInterceptorMgr().fireBeforeHistoryEvent(event);

            FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(event, historyContext);
            List<Resource> resources = getPersistenceImpl().history(persistenceContext, resourceType, id);
            bundle = createHistoryBundle(resources, historyContext);
            bundle = addLinks(historyContext, bundle, requestUri);

            event.setFhirResource(bundle);

            // Invoke the 'afterHistory' interceptor methods.
            getInterceptorMgr().fireAfterHistoryEvent(event);

            // Commit our transaction if we started one before.
            txn.commit();
            txn = null;

            status = Response.Status.OK;
            return bundle;
        } catch (FHIRHttpException e) {
            log.log(Level.SEVERE, errMsg, e);
            status = e.getHttpStatus();
            throw e;
        } catch (FHIROperationException e) {
            log.log(Level.SEVERE, errMsg, e);
            status = IssueTypeToHttpStatusMapper.issueListToStatus(e.getIssues());
            throw e;
        } catch (Throwable t) {
            log.log(Level.SEVERE, errMsg, t);
            status = Response.Status.INTERNAL_SERVER_ERROR;
            throw t;
        } finally {
            // Restore the original request context.
            FHIRRequestContext.set(requestContext);

            // If we previously started a transaction and it's still active, we need to rollback due to an error.
            if (txn != null) {
                txn.rollback();
            }

            // Now Audit log the final status of the request,
            // if fails to log, then log the error in local log file and ignore.
            try {
                RestAuditLogger.logHistory(httpServletRequest, bundle, startTime, new Date(), status);
            } catch (Exception e) {
                log.log(Level.INFO, errMsg, e);
            }

            log.exiting(this.getClass().getName(), "doHistory");
        }
    }

    /**
     * Performs heavy lifting associated with a 'search' operation.
     * 
     * @param type
     *            the resource type associated with the search
     * @param queryParameters
     *            a Map containing the query parameters from the request URL
     * @param requestProperties
     *            additional request properties which supplement the HTTP headers associated with this request
     * @return a Bundle containing the search result set
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Bundle doSearch(String type, String compartment, String compartmentId, MultivaluedMap<String, String> queryParameters, String requestUri,
        Map<String, String> requestProperties, Resource contextResource) throws Exception {
        log.entering(this.getClass().getName(), "doSearch");

        FHIRTransactionHelper txn = new FHIRTransactionHelper(getTransaction());
        Bundle bundle = null;
        Date startTime = new Date();
        Response.Status status = null;
        String errMsg = "Caught exception while processing 'search' request.";

        // Save the current request context.
        FHIRRequestContext requestContext = FHIRRequestContext.get();

        try {
            String resourceTypeName = type;
            Parameter implicitSearchParameter = null;
            if (!FHIRUtil.isStandardResourceType(type)) {
                if (!isVirtualResourceTypesFeatureEnabled()) {
                    String msg = "The virtual resource types feature is not enabled for this server";
                    throw buildVirtualResourceException(msg, Status.BAD_REQUEST, IssueType.ValueSet.NOT_SUPPORTED);
                }
                if (!isAllowableVirtualResourceType(type)) {
                    String msg = "The virtual resource type '" + type + "' is not allowed. Allowable virtual resource types for this server are: "
                            + getAllowableVirtualResourceTypes().toString();
                    throw buildVirtualResourceException(msg, Status.BAD_REQUEST, IssueType.ValueSet.NOT_SUPPORTED);
                }
                resourceTypeName = "Basic";
                implicitSearchParameter = createBasicCodeSearchParameter(type);
            }

            Class<? extends Resource> resourceType = (Class<? extends Resource>) getResourceType(resourceTypeName);

            // Start a new txn in the persistence layer if one is not already active.
            txn.begin();

            // First, invoke the 'beforeSearch' interceptor methods.
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(contextResource, buildPersistenceEventProperties(type, null, null, requestProperties));
            getInterceptorMgr().fireBeforeSearchEvent(event);

            FHIRSearchContext searchContext =
                    SearchUtil.parseQueryParameters(compartment, compartmentId, resourceType, queryParameters, httpServletRequest.getQueryString(), isSearchLenient(requestProperties));
            List<Parameter> searchParameters = searchContext.getSearchParameters();
            if (implicitSearchParameter != null) {
                searchParameters.add(implicitSearchParameter);
            }

            FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(event, searchContext);
            List<Resource> resources = getPersistenceImpl().search(persistenceContext, resourceType);

            bundle = createSearchBundle(resources, searchContext);
            if (requestUri != null) {
                bundle = addLinks(searchContext, bundle, requestUri);
            }
            event.setFhirResource(bundle);

            // Invoke the 'afterSearch' interceptor methods.
            getInterceptorMgr().fireAfterSearchEvent(event);

            // Commit our transaction if we started one before.
            txn.commit();
            txn = null;

            status = Response.Status.OK;

            return bundle;
        } catch (FHIRHttpException e) {
            log.log(Level.SEVERE, errMsg, e);
            status = e.getHttpStatus();
            throw e;
        } catch (FHIROperationException e) {
            log.log(Level.SEVERE, errMsg, e);
            status = IssueTypeToHttpStatusMapper.issueListToStatus(e.getIssues());
            throw e;
        } catch (Throwable t) {
            log.log(Level.SEVERE, errMsg, t);
            status = Response.Status.INTERNAL_SERVER_ERROR;
            throw t;
        } finally {
            // Restore the original request context.
            FHIRRequestContext.set(requestContext);

            // If we previously started a transaction and it's still active, we need to rollback due to an error.
            if (txn != null) {
                txn.rollback();
            }

            // Now Audit log the final status of the request,
            // if fails to log, then log the error in local log file and ignore.
            try {
                RestAuditLogger.logSearch(httpServletRequest, queryParameters, bundle, startTime, new Date(), status);
            } catch (Exception e) {
                log.log(Level.INFO, errMsg, e);
            }

            log.exiting(this.getClass().getName(), "doSearch");
        }
    }

    private boolean isSearchLenient(Map<String, String> requestProperties) {
        boolean lenient = true;

        String handlingStringValue = getHeaderValue(requestProperties, "Prefer", "handling");
        if ("strict".equals(handlingStringValue)) {
            lenient = false;
        }

        return lenient;
    }

    /**
     * Helper method for getting header values. Supports retrieval of a specific part from within a multipart header
     * value.
     * 
     * @partName optional
     */
    private String getHeaderValue(Map<String, String> requestProperties, String headerName, String partName) {

        String headerStringValue;
        if (requestProperties != null && requestProperties.containsKey(headerName)) {
            headerStringValue = requestProperties.get(headerName);
        } else {
            headerStringValue = httpHeaders.getHeaderString(headerName);
        }

        if (headerStringValue == null) {
            return null;
        }

        String[] splitHeaderStringValues = headerStringValue.split(",");
        if (splitHeaderStringValues.length > 1) {
            log.fine("Found multiple 'Prefer' header values; using the first one with partName '" + partName + "'");
        }

        // Return the first non-null headerPartValue we find
        for (String splitHeaderStringValue : splitHeaderStringValues) {
            String headerPartValue = getHeaderPartValue(splitHeaderStringValue, partName);
            if (headerPartValue != null) {
                return headerPartValue;
            }
        }

        return null;
    }

    /**
     * Helper method for getting header values from multipart headers
     * 
     * @return the value of the part or the full header value if partName is null; returns null if the partName is not
     *         found
     */
    private String getHeaderPartValue(String fullHeaderValue, String partName) {
        if (partName == null) {
            return fullHeaderValue;
        }

        if (fullHeaderValue != null) {
            String[] parts = fullHeaderValue.split(";");
            for (int i = 0; i < parts.length; i++) {
                String[] splitPart = parts[i].split("=", 2);
                if (partName.equals(splitPart[0].trim()) && splitPart.length == 2) {
                    return splitPart[1].trim();
                }
            }
        }

        return null;
    }

    /**
     * Helper method which invokes a custom operation.
     * 
     * @param operationContext
     *            the FHIROperationContext associated with the request
     * @param resourceTypeName
     *            the resource type associated with the request
     * @param logicalId
     *            the resource logical id associated with the request
     * @param versionId
     *            the resource version id associated with the request
     * @param operationName
     *            the name of the custom operation to be invoked
     * @param resource
     *            the input resource associated with the custom operation to be invoked
     * @param queryParameters
     *            query parameters may be passed instead of a Parameters resource for certain custom operations invoked
     *            via GET
     * @param requestProperties
     *            additional request properties which supplement the HTTP headers associated with this request
     * @return a Resource that represents the response to the custom operation
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Resource doInvoke(FHIROperationContext operationContext, String resourceTypeName, String logicalId, String versionId, String operationName,
        Resource resource, MultivaluedMap<String, String> queryParameters, Map<String, String> requestProperties) throws Exception {
        log.entering(this.getClass().getName(), "doInvoke");

        Date startTime = new Date();
        Response.Status status = null;
        String errMsg = "Caught exception while processing 'invoke' request.";

        // Save the current request context.
        FHIRRequestContext requestContext = FHIRRequestContext.get();

        // Pass end time the same as start time to tell cadf log service that this is a pending request.
        RestAuditLogger.logOperation(httpServletRequest, operationName, resourceTypeName, logicalId, versionId, startTime, startTime, Response.Status.OK);

        try {
            Class<? extends Resource> resourceType = null;
            if (resourceTypeName != null) {
                resourceType = (Class<? extends Resource>) getResourceType(resourceTypeName);
            }

            FHIROperation operation = FHIROperationRegistry.getInstance().getOperation(operationName);
            Parameters parameters = null;
            if (resource instanceof Parameters) {
                parameters = (Parameters) resource;
            } else {
                if (resource == null) {
                    // build parameters object from query parameters
                    parameters = FHIROperationUtil.getInputParameters(operation.getDefinition(), queryParameters);
                } else {
                    // wrap resource in a parameters object
                    parameters = FHIROperationUtil.getInputParameters(operation.getDefinition(), resource);
                }
            }

            // Add properties to the FHIR operation context
            setOperationContextProperties(operationContext, requestProperties);

            if (log.isLoggable(Level.FINE)) {
                log.fine("Invoking operation '" + operationName + "', context=\n" + operationContext.toString());
            }
            Parameters result = operation.invoke(operationContext, resourceType, logicalId, versionId, parameters, this);
            if (log.isLoggable(Level.FINE)) {
                log.fine("Returned from invocation of operation '" + operationName + "'...");
            }

            status = Response.Status.OK;

            // if single resource output parameter, return the resource
            if (FHIROperationUtil.hasSingleResourceOutputParameter(result)) {
                return FHIROperationUtil.getSingleResourceOutputParameter(result);
            }

            return result;
        } catch (FHIRHttpException e) {
            log.log(Level.SEVERE, errMsg, e);
            status = e.getHttpStatus();
            throw e;
        } catch (FHIROperationException e) {
            log.log(Level.SEVERE, errMsg, e);
            status = IssueTypeToHttpStatusMapper.issueListToStatus(e.getIssues());
            throw e;
        } catch (Throwable t) {
            log.log(Level.SEVERE, errMsg, t);
            status = Response.Status.INTERNAL_SERVER_ERROR;
            throw t;
        } finally {
            // Restore the original request context.
            FHIRRequestContext.set(requestContext);
            // Now Audit log the final status of the request,
            // if fails to log, then log the error in local log file and ignore.
            try {
                RestAuditLogger.logOperation(httpServletRequest, operationName, resourceTypeName, logicalId, versionId, startTime, new Date(), status);
            } catch (Exception e) {
                log.log(Level.INFO, errMsg, e);
            }

            log.exiting(this.getClass().getName(), "doInvoke");
        }
    }

    /**
     * Processes a bundled request.
     *
     * @param bundle
     *            the request Bundle
     * @param requestProperties
     *            additional request properties which supplement the HTTP headers associated with this request
     * @return the response Bundle
     */
    public Bundle doBundle(Resource bundleResource, Map<String, String> requestProperties) throws Exception {
        log.entering(this.getClass().getName(), "doBundle");

        Date startTime = new Date();
        Response.Status status = null;
        String errMsg = "Caught exception while processing 'bundle' request.";
        Bundle inputBundle = null;

        // Save the current request context.
        FHIRRequestContext requestContext = FHIRRequestContext.get();

        // Pass end time the same as start time to tell cadf log service that this is a pending request.
        RestAuditLogger.logBundle(httpServletRequest, bundleResource instanceof Bundle ? (Bundle) bundleResource
                : null, startTime, startTime, Response.Status.OK);

        try {
            if (bundleResource instanceof Bundle) {
                inputBundle = (Bundle) bundleResource;
            } else {
                String msg = "A 'Bundle' resource type is required but a '" + bundleResource.getClass().getSimpleName() + "' resource type was sent.";
                throw buildRestException(msg, Status.BAD_REQUEST, IssueType.ValueSet.INVALID);
            }
            // First, validate the bundle and create the response bundle.
            Bundle responseBundle = validateBundle(inputBundle);
           
            // Next, process each of the entries in the bundle.
            responseBundle = processBundleEntries(inputBundle, responseBundle, requestProperties);

            status = Response.Status.OK;

            return responseBundle;
        } catch (FHIRHttpException e) {
            log.log(Level.SEVERE, errMsg, e);
            status = e.getHttpStatus();
            throw e;
        } catch (FHIROperationException e) {
            log.log(Level.SEVERE, errMsg, e);
            status = IssueTypeToHttpStatusMapper.issueListToStatus(e.getIssues());
            throw e;
        } catch (Throwable t) {
            log.log(Level.SEVERE, errMsg, t);
            status = Response.Status.INTERNAL_SERVER_ERROR;
            throw t;
        } finally {
            // Restore the original request context.
            FHIRRequestContext.set(requestContext);
            // Now Audit log the final status of the request,
            // if fails to log, then log the error in local log file and ignore.
            try {
                RestAuditLogger.logBundle(httpServletRequest, inputBundle, startTime, new Date(), status);
            } catch (Exception e) {
                log.log(Level.INFO, errMsg, e);
            }

            log.exiting(this.getClass().getName(), "doBundle");
        }
    }

    /*
     * (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.rest.FHIRResourceHelpers#getTransaction()
     */
    @Override
    public FHIRPersistenceTransaction getTransaction() throws Exception {
        return getPersistenceImpl().getTransaction();
    }

    /**
     * Sets various properties on the FHIROperationContext instance.
     * 
     * @param operationContext
     *            the FHIROperationContext on which to set the properties
     * @throws FHIRPersistenceException
     */
    private void setOperationContextProperties(FHIROperationContext operationContext, Map<String, String> requestProperties) throws FHIRPersistenceException {
        operationContext.setProperty(FHIROperationContext.PROPNAME_REQUEST_BASE_URI, getRequestBaseUri());
        operationContext.setProperty(FHIROperationContext.PROPNAME_RESOURCE_HELPER, this);
        operationContext.setProperty(FHIROperationContext.PROPNAME_PERSISTENCE_IMPL, getPersistenceImpl());
        operationContext.setProperty(FHIROperationContext.PROPNAME_URI_INFO, uriInfo);
        operationContext.setProperty(FHIROperationContext.PROPNAME_HTTP_HEADERS, httpHeaders);
        operationContext.setProperty(FHIROperationContext.PROPNAME_SECURITY_CONTEXT, securityContext);
        operationContext.setProperty(FHIROperationContext.PROPNAME_REQUEST_PROPERTIES, requestProperties);
    }
    
    /**
     * @param issues
     * @return
     */
    private boolean anyFailueInIssues(List<OperationOutcome.Issue> issues) {
        boolean hasFailure = false;
        for (OperationOutcome.Issue issue: issues) {
            if (FHIRUtil.isFailure(issue.getSeverity())) {
                hasFailure = true;
            }
        }
        return hasFailure;
    }


    /**
     * Performs validation of a request Bundle and returns a Bundle containing response entries corresponding to the
     * request entries in the request Bundle. holding the responses for the requests contained in the request Bundle.
     *
     * @param bundle
     *            the bundle to be validated
     * @return a response Bundle
     * @throws Exception
     */
    private Bundle validateBundle(Bundle bundle) throws Exception {
        log.entering(this.getClass().getName(), "validateBundle");

        try {
            // Make sure the bundle isn't empty and has a type.
            if (bundle == null || bundle.getEntry() == null || bundle.getEntry().isEmpty()) {
                String msg = "Bundle parameter is missing or empty.";
                throw buildRestException(msg, Status.BAD_REQUEST, IssueType.ValueSet.REQUIRED);
            }

            if (bundle.getType() == null || bundle.getType().getValue() == null) {
                String msg = "Bundle.type is missing";
                throw buildRestException(msg, Status.BAD_REQUEST, IssueType.ValueSet.REQUIRED);
            }

            // Determine the bundle type of the response bundle.
            BundleType responseBundleType = null;

            if (bundle.getType().equals(BundleType.BATCH)) {
                responseBundleType = BundleType.BATCH_RESPONSE;
            } else if (bundle.getType().equals(BundleType.TRANSACTION)) {
                responseBundleType = BundleType.TRANSACTION_RESPONSE;
                // For a 'transaction' interaction, if the underlying persistence layer doesn't support
                // transactions, then throw an error.
                if (!getPersistenceImpl().isTransactional()) {
                    String msg = "Bundled 'transaction' request cannot be processed because the configured persistence layer does not support transactions.";
                    throw buildRestException(msg, Status.BAD_REQUEST, IssueType.ValueSet.NOT_SUPPORTED);
                }
                // For any other bundle type, we'll throw an error.
            } else {
                String msg = "Bundle.type must be either 'batch' or 'transaction'.";
                throw buildRestException(msg, Status.BAD_REQUEST, IssueType.ValueSet.VALUE);
            }

            // Next, make sure that each bundle entry contains a valid request.
            // As we're validating the request bundle, we'll also construct entries for the response bundle.
            int numErrors = 0;
            List<Bundle.Entry> responseList = new ArrayList<Bundle.Entry>();
            for (Bundle.Entry requestEntry : bundle.getEntry()) {
                // Create a corresponding response entry and add it to the response bundle.
                Bundle.Entry.Response response;
                Bundle.Entry responseEntry = null;

                // Validate 'requestEntry' and update 'responseEntry' with any errors.
                try {
                    Bundle.Entry.Request request = requestEntry.getRequest();
                    // Verify that the request field is present.
                    if (request == null) {
                        String msg = "Bundle.Entry is missing the 'request' field.";
                        throw buildRestException(msg, Status.BAD_REQUEST, IssueType.ValueSet.REQUIRED);
                    }

                    // Verify that a method was specified.
                    if (request.getMethod() == null || request.getMethod().getValue() == null) {
                        String msg = "Bundle.Entry.request is missing the 'method' field";
                        throw buildRestException(msg, Status.BAD_REQUEST, IssueType.ValueSet.REQUIRED);
                    }

                    // Verify that a URL was specified.
                    if (request.getUrl() == null || request.getUrl().getValue() == null) {
                        String msg = "Bundle.Entry.request is missing the 'url' field";
                        throw buildRestException(msg, Status.BAD_REQUEST, IssueType.ValueSet.REQUIRED);
                    }

                    // Retrieve the resource from the request entry to prepare for some validations below.
                    Resource resource = getBundleEntryResource(requestEntry);

                    // Validate the HTTP method.
                    HTTPVerb method = request.getMethod();
                    if (method.equals(HTTPVerb.GET)) {
                        if (resource != null) {
                            String msg = "Bundle.Entry.resource not allowed for BundleEntry with GET method.";
                            throw buildRestException(msg, Status.BAD_REQUEST, IssueType.ValueSet.INVALID);
                        }
                    } else if (method.equals(HTTPVerb.POST)) {
                    } else if (method.equals(HTTPVerb.PUT)) {
                        if (resource == null) {
                            String msg = "Bundle.Entry.resource is required for BundleEntry with PUT method.";
                            throw buildRestException(msg, Status.BAD_REQUEST, IssueType.ValueSet.INVALID);
                        }
                    } else if (method.equals(HTTPVerb.DELETE)) {
                        // If the "delete" operation isn't supported by the configured persistence layer,
                        // then we need to fail validation of this bundle entry.
                        if (!isDeleteSupported()) {
                            String msg = "Bundle.Entry.request contains unsupported HTTP method: " + method.getValue();
                            throw buildRestException(msg, Status.BAD_REQUEST, IssueType.ValueSet.NOT_SUPPORTED);
                        }
                        if (resource != null) {
                            String msg = "Bundle.Entry.resource not allowed for BundleEntry with DELETE method.";
                            throw buildRestException(msg, Status.BAD_REQUEST, IssueType.ValueSet.INVALID);
                        }
                    } else {
                        String msg = "Bundle.Entry.request contains unsupported HTTP method: " + method.getValue();
                        throw buildRestException(msg, Status.METHOD_NOT_ALLOWED, IssueType.ValueSet.INVALID);
                    }

                    // If the request entry contains a resource, then validate it now.
                    if (resource != null) {
                        if (method.equals(HTTPVerb.PUT)) {
                            if (resource.getId() == null || resource.getId().getValue() == null) {
                                String msg = "Bundle.Entry.resource must contain an id field for a PUT operation.";
                                throw buildRestException(msg, Status.BAD_REQUEST, IssueType.ValueSet.REQUIRED);
                            }
                        }

                        List<OperationOutcome.Issue> issues = FHIRValidator.validator(resource).validate();
                        if (!issues.isEmpty()) {
                            OperationOutcome oo = FHIRUtil.buildOperationOutcome(issues);
                            if (anyFailueInIssues(issues)) {
                                response = Bundle.Entry.Response.builder().status(string(Integer.toString(SC_BAD_REQUEST))).build();
                                numErrors++;
                            }else {
                                response = Bundle.Entry.Response.builder().status(string(Integer.toString(SC_OK))).build();
                            }
                            responseEntry = Bundle.Entry.builder().response(response).resource(oo).build();
                            continue;
                        }
                    }
                    response = Bundle.Entry.Response.builder().status(string(Integer.toString(SC_OK))).build();
                    responseEntry = Bundle.Entry.builder().response(response).build();
                } catch (FHIROperationException e) {
                    log.log(Level.INFO, "Failed to process BundleEntry [" + bundle.getEntry().indexOf(requestEntry) + "]", e);
                    response = Bundle.Entry.Response.builder().status(string(Integer.toString(SC_BAD_REQUEST))).build();
                    responseEntry = Bundle.Entry.builder().response(response).resource(FHIRUtil.buildOperationOutcome(e, false)).build();
                    numErrors++;
                } finally {
                    if (responseEntry != null) {
                        responseList.add(responseEntry);
                    }
                }
            } // End foreach requestEntry

            // Create the response bundle with the appropriate type.
            Bundle responseBundle = Bundle.builder().type(responseBundleType).entry(responseList).build();

            // If this is a "transaction" interaction and we encountered any errors, then we'll
            // abort processing this request right now since a transaction interaction is supposed to be
            // all or nothing.
            if (numErrors > 0 && responseBundle.getType().equals(BundleType.TRANSACTION_RESPONSE)) {
                String msg = "One or more errors were encountered while validating a 'transaction' request bundle.";
                OperationOutcome.Issue issue = buildOperationOutcomeIssue(IssueSeverity.ValueSet.ERROR, IssueType.ValueSet.EXCEPTION, msg);
                throw new FHIRRestBundledRequestException(msg, Response.Status.BAD_REQUEST, responseBundle).withIssue(issue);
            }

            return responseBundle;
        } finally {
            log.exiting(this.getClass().getName(), "validateBundle");
        }
    }

    /**
     * This function will perform the version-aware update check by making sure that the If-Match request header value
     * (if present) specifies a version # equal to the current latest version of the resource. If the check fails, then
     * a FHIRRestException will be thrown. If the check succeeds then nothing occurs and processing continues.
     *
     * @param currentResource
     *            the current latest version of the resource
     */
    private void performVersionAwareUpdateCheck(Resource currentResource, String ifMatchValue) throws FHIRHttpException {
        if (ifMatchValue != null) {
            log.fine("Performing a version aware update. ETag value =  " + ifMatchValue);

            String ifMatchVersion = getVersionIdFromETagValue(ifMatchValue);

            // Make sure that we got a version # from the request header.
            // If not, then return a 400 Bad Request status code.
            if (ifMatchVersion == null || ifMatchVersion.isEmpty()) {
                throw buildRestException("Invalid ETag value specified in request: " + ifMatchValue, Status.BAD_REQUEST, IssueType.ValueSet.PROCESSING);
            }

            log.fine("Version id from ETag value specified in request: " + ifMatchVersion);

            // Retrieve the version #'s from the current and updated resources.
            String currentVersion = null;
            if (currentResource.getMeta() != null && currentResource.getMeta().getVersionId() != null) {
                currentVersion = currentResource.getMeta().getVersionId().getValue();
            }

            // Next, make sure that the If-Match version matches the version # found
            // in the current latest version of the resource.
            // If they don't match we'll return a 409 Conflict status code.
            if (!ifMatchVersion.equals(currentVersion)) {
                String msg = "If-Match version '" + ifMatchVersion + "' does not match current latest version of resource: " + currentVersion;
                throw buildRestException(msg, Status.CONFLICT, IssueType.ValueSet.CONFLICT);
            }
        }
    }

    private FHIRVirtualResourceTypeException buildVirtualResourceException(String msg, Status status, IssueType.ValueSet issueType)
        throws FHIRVirtualResourceTypeException {
        OperationOutcome.Issue ooi = FHIRUtil.buildOperationOutcomeIssue(msg, issueType);
        return new FHIRVirtualResourceTypeException(msg, status).withIssue(ooi);
    }

    private FHIRHttpException buildRestException(String msg, Status status, IssueType.ValueSet issueType) throws FHIRHttpException {
        return buildRestException(msg, status, issueType, IssueSeverity.ValueSet.FATAL);
    }

    private FHIRHttpException buildRestException(String msg, Status status, IssueType.ValueSet issueType, IssueSeverity.ValueSet severity)
        throws FHIRHttpException {
        final String location = "<empty>";
        OperationOutcome.Issue ooi = FHIRUtil.buildOperationOutcomeIssue(severity, issueType, msg, location);
        return new FHIRHttpException(msg, status).withIssue(ooi);
    }

    /**
     * Retrieves the version id value from an ETag header value. The ETag header value will be of the form:
     * W/"<version-id>".
     * 
     * @param ifMatchValue
     *            the value of the If-Match request header.
     */
    private String getVersionIdFromETagValue(String ifMatchValue) {
        String result = null;
        if (ifMatchValue != null) {
            if (ifMatchValue.startsWith("W/")) {
                String s = ifMatchValue.substring(2);
                // If the part after "W/" starts and ends with a ",
                // then extract the part between the " characters and we're done.
                if (s.charAt(0) == '\"' && s.charAt(s.length() - 1) == '\"') {
                    result = s.substring(1, s.length() - 1);
                }
            }
        }
        return result;
    }

    /**
     * This function will process each request contained in the specified request bundle, and update the response bundle
     * with the appropriate response information.
     * 
     * @param requestBundle
     *            the bundle containing the requests
     * @param responseBundle
     *            the bundle containing the responses
     */
    private Bundle processBundleEntries(Bundle requestBundle, Bundle responseBundle, Map<String, String> requestProperties) throws Exception {
        log.entering(this.getClass().getName(), "processBundleEntries");

        FHIRTransactionHelper txn = null;

        // Generate a request correlation id for this request bundle.
        bundleRequestCorrelationId = UUID.randomUUID().toString();
        log.fine("Processing request bundle, request-correlation-id=" + bundleRequestCorrelationId);

        try {
            // If we're working on a 'transaction' type interaction, then start a new transaction now
            // and sort the request bundle entries by their "url" field.
            if (responseBundle.getType() == BundleType.TRANSACTION_RESPONSE) {
                bundleTransactionCorrelationId = bundleRequestCorrelationId;
                txn = new FHIRTransactionHelper(getTransaction());
                txn.begin();
                log.fine("Started new transaction for transaction bundle, txn-correlation-id=" + bundleTransactionCorrelationId);
            }

            Map<String, String> localRefMap = new HashMap<>();

            // Next, process entries in the correct order.
            responseBundle =
                    processEntriesForMethod(requestBundle, responseBundle, HTTPVerb.DELETE, txn != null, localRefMap, requestProperties, bundleRequestCorrelationId);
            responseBundle =
                    processEntriesForMethod(requestBundle, responseBundle, HTTPVerb.POST, txn != null, localRefMap, requestProperties, bundleRequestCorrelationId);
            responseBundle =
                    processEntriesForMethod(requestBundle, responseBundle, HTTPVerb.PUT, txn != null, localRefMap, requestProperties, bundleRequestCorrelationId);
            responseBundle =
                    processEntriesForMethod(requestBundle, responseBundle, HTTPVerb.GET, txn != null, localRefMap, requestProperties, bundleRequestCorrelationId);

            if (txn != null) {
                log.fine("Committing transaction for transaction bundle, txn-correlation-id=" + bundleTransactionCorrelationId);
                txn.commit();
                txn = null;
            }
            return responseBundle;

        } finally {
            log.fine("Finished processing request bundle, request-correlation-id=" + bundleRequestCorrelationId);

            // Clear both correlation id fields since we're done processing the bundle.
            bundleRequestCorrelationId = null;
            bundleTransactionCorrelationId = null;

            if (txn != null) {
                txn.rollback();
                txn = null;
            }
            log.exiting(this.getClass().getName(), "processBundleEntries");
        }
    }

    /**
     * Processes request entries in the specified request bundle whose method matches 'httpMethod'.
     *
     * @param requestBundle
     *            the bundle containing the request entries
     * @param responseBundle
     *            the bundle containing the corresponding response entries
     * @param httpMethod
     *            the HTTP method (GET, POST, PUT, etc.) to be processed
     */
    private Bundle processEntriesForMethod(Bundle requestBundle, Bundle responseBundle, HTTPVerb httpMethod, boolean failFast, Map<String, String> localRefMap,
        Map<String, String> bundleRequestProperties, String bundleRequestCorrelationId) throws Exception {
        log.entering(this.getClass().getName(), "processEntriesForMethod", new Object[] { "httpMethod", httpMethod });
        try {
            // First, obtain a list of request entry indices for the entries that we'll process.
            // This list will contain the indices associated with only the entries for the specified http method.
            List<Integer> entryIndices = getBundleRequestIndicesForMethod(requestBundle, responseBundle, httpMethod);
            log.finer("Bundle request indices to be processed: " + entryIndices.toString());

            // Next, for PUT (update) requests, extract any local identifiers and resolve them ahead of time.
            // We do this to prevent any local reference problems from occurring due to our re-ordering
            // of the PUT request entries.
            if (httpMethod.equals(HTTPVerb.PUT)) {
                log.finer("Pre-processing bundle request entries for PUT method...");
                for (Integer index : entryIndices) {
                    Bundle.Entry requestEntry = requestBundle.getEntry().get(index);

                    // Retrieve the local identifier from the request entry (if present).
                    String localIdentifier = retrieveLocalIdentifier(requestEntry, localRefMap);

                    // Since this is for a PUT request (update) we should be able to resolve the local identifier
                    // prior to processing the request since the resource's id must already be contained in the resource
                    // within the request entry.
                    if (localIdentifier != null) {
                        Resource resource = requestEntry.getResource();
                        addLocalRefMapping(localRefMap, localIdentifier, resource);
                    }
                }
            }

            // Next, for PUT and DELETE requests, we need to sort the indices by the request url path value.
            if (httpMethod.equals(HTTPVerb.PUT) || httpMethod.equals(HTTPVerb.DELETE)) {
                sortBundleRequestEntries(requestBundle, entryIndices);
                log.finer("Sorted bundle request indices to be processed: " + entryIndices.toString());
            }

            // Now visit each of the request entries using the list of indices obtained above.
            // Use hashmap to store both the index and the according updated response bundle entry.
            HashMap<Integer, Bundle.Entry> responseIndexAndEntries = new HashMap<Integer, Bundle.Entry>();
            for (Integer entryIndex : entryIndices) {
                Bundle.Entry requestEntry = requestBundle.getEntry().get(entryIndex);
                Bundle.Entry responseEntry = responseBundle.getEntry().get(entryIndex);
                Bundle.Entry.Builder responseEntryBuilder = responseEntry.toBuilder();

                Bundle.Entry.Request request = requestEntry.getRequest();
                Bundle.Entry.Response response = responseEntry.getResponse();

                StringBuffer requestDescription = new StringBuffer();
                long initialTime = System.currentTimeMillis();
                try {
                    FHIRUrlParser requestURL = new FHIRUrlParser(request.getUrl().getValue());

                    String path = requestURL.getPath();
                    String query = requestURL.getQuery();
                    if (log.isLoggable(Level.FINER)) {
                        log.finer("Processing bundle request entry " + entryIndex + "; method=" + request.getMethod().getValue() + ", url="
                                + request.getUrl().getValue());
                        log.finer("--> path: " + path);
                        log.finer("--> query: " + query);
                    }

                    // Log our initial info message for this request.
                    requestDescription.append("entryIndex:[");
                    requestDescription.append(entryIndex);
                    requestDescription.append("] correlationId:[");
                    requestDescription.append(bundleRequestCorrelationId);
                    requestDescription.append("] method:[");
                    requestDescription.append(request.getMethod().getValue());
                    requestDescription.append("] uri:[");
                    requestDescription.append(request.getUrl().getValue());
                    requestDescription.append("]");
                    log.info("Received bundle request: " + requestDescription.toString());

                    String[] pathTokens = requestURL.getPathTokens();
                    MultivaluedMap<String, String> queryParams = requestURL.getQueryParameters();

                    // Construct the absolute requestUri to be used for any response bundles associated
                    // with history and search requests.
                    String absoluteUri = getAbsoluteUri(getRequestUri(), request.getUrl().getValue());

                    Map<String, String> requestProperties = extractRequestPropertiesFromExtensions(request.getExtension(), bundleRequestProperties);

                    if (request.getMethod().equals(HTTPVerb.GET)) {
                        Resource resource = null;
                        int httpStatus = SC_OK;

                        // Process a GET (read, vread, history, search, etc.).
                        // Determine the type of request from the path tokens.
                        if (pathTokens.length > 0 && pathTokens[pathTokens.length - 1].startsWith("$")) {
                            // This is a custom operation request
                            checkInitComplete();

                            // Chop off the '$' and save the name
                            String operationName = pathTokens[pathTokens.length - 1].substring(1);

                            // FHIROperationContext operationContext;
                            switch (pathTokens.length) {
                            case 1: {
                                FHIROperationContext operationContext = FHIROperationContext.createSystemOperationContext();
                                resource = doInvoke(operationContext, null, null, null, operationName, null, queryParams, null);
                            }
                                break;
                            case 2: {
                                FHIROperationContext operationContext = FHIROperationContext.createResourceTypeOperationContext();
                                resource = doInvoke(operationContext, pathTokens[0], null, null, operationName, null, queryParams, null);
                            }
                                break;
                            case 3: {
                                FHIROperationContext operationContext = FHIROperationContext.createInstanceOperationContext();
                                resource = doInvoke(operationContext, pathTokens[0], pathTokens[1], null, operationName, null, queryParams, null);
                            }
                                break;
                            default:
                                String msg = "Invalid URL for custom operation '" + pathTokens[pathTokens.length - 1] + "'";
                                throw buildRestException(msg, Status.BAD_REQUEST, IssueType.ValueSet.NOT_FOUND);
                            }
                        } else if (pathTokens.length == 1) {
                            // This is a 'search' request.
                            if ("_search".equals(pathTokens[0])) {
                                resource = doSearch("Resource", null, null, queryParams, absoluteUri, requestProperties, null);
                            } else {
                                resource = doSearch(pathTokens[0], null, null, queryParams, absoluteUri, requestProperties, null);
                            }
                        } else if (pathTokens.length == 2) {
                            // This is a 'read' request.
                            resource = doRead(pathTokens[0], pathTokens[1], true, false, requestProperties, null);
                        } else if (pathTokens.length == 3) {
                            if ("_history".equals(pathTokens[2])) {
                                // This is a 'history' request.
                                resource = doHistory(pathTokens[0], pathTokens[1], queryParams, absoluteUri, requestProperties);
                            } else {
                                // This is a compartment based search
                                resource = doSearch(pathTokens[2], pathTokens[0], pathTokens[1], queryParams, absoluteUri, requestProperties, null);
                            }
                        } else if (pathTokens.length == 4 && pathTokens[2].equals("_history")) {
                            // This is a 'vread' request.
                            resource = doVRead(pathTokens[0], pathTokens[1], pathTokens[3], requestProperties);
                        } else {
                            String msg = "Unrecognized path in request URL: " + path;
                            throw buildRestException(msg, Status.BAD_REQUEST, IssueType.ValueSet.NOT_FOUND);
                        }

                        // Save the results of the operation in the bundle response field.
                        Bundle.Entry.Response.Builder responseBuilder = response.toBuilder();
                        responseBuilder.status(string(Integer.toString(httpStatus)));
                        setBundleResponseStatus(response, httpStatus, requestDescription.toString(), initialTime);

                        responseIndexAndEntries.put(entryIndex, responseEntryBuilder.resource(resource).response(responseBuilder.build()).build());
                    } else if (request.getMethod().equals(HTTPVerb.POST)) {
                        // Process a POST (create or search, or custom operation).
                        if (pathTokens.length > 0 && pathTokens[pathTokens.length - 1].startsWith("$")) {
                            // This is a custom operation request
                            checkInitComplete();

                            // Chop off the '$' and save the name
                            String operationName = pathTokens[pathTokens.length - 1].substring(1);

                            // Retrieve the resource from the request entry.
                            Resource resource = requestEntry.getResource();

                            FHIROperationContext operationContext;
                            Resource result;
                            switch (pathTokens.length) {
                            case 1:
                                operationContext = FHIROperationContext.createSystemOperationContext();
                                result = doInvoke(operationContext, null, null, null, operationName, resource, queryParams, null);
                                break;
                            case 2:
                                operationContext = FHIROperationContext.createResourceTypeOperationContext();
                                result = doInvoke(operationContext, pathTokens[0], null, null, operationName, resource, queryParams, null);
                                break;
                            case 3:
                                operationContext = FHIROperationContext.createInstanceOperationContext();
                                result = doInvoke(operationContext, pathTokens[0], pathTokens[1], null, operationName, resource, queryParams, null);
                                break;
                            default:
                                String msg = "Invalid URL for custom operation '" + pathTokens[pathTokens.length - 1] + "'";
                                throw buildRestException(msg, Status.BAD_REQUEST, IssueType.ValueSet.NOT_FOUND);
                            }

                            Bundle.Entry.Response.Builder responseBuilder = response.toBuilder();
                            // Add warning and hint issues to response outcome if any.
                            if (result instanceof OperationOutcome) {
                                if (((OperationOutcome)result).getIssue() != null) {
                                    responseBuilder.outcome(result);
                                }
                            }

                            responseBuilder.status(string(Integer.toString(SC_OK)));
                            responseIndexAndEntries.put(entryIndex, responseEntryBuilder.resource(result).response(responseBuilder.build()).build());
                            setBundleResponseStatus(response, SC_OK, requestDescription.toString(), initialTime);

                        } else if (pathTokens.length == 2 && "_search".equals(pathTokens[1])) {
                            // This is a 'search' request.
                            Bundle searchResults = doSearch(pathTokens[0], null, null, queryParams, absoluteUri, requestProperties, null);

                            // Save the results of the operation in the bundle response field.
                            Bundle.Entry.Response.Builder responseBuilder = response.toBuilder();
                            responseBuilder.status(string(Integer.toString(SC_OK)));

                            responseIndexAndEntries.put(entryIndex, responseEntryBuilder.resource(searchResults).response(responseBuilder.build()).build());

                            setBundleResponseStatus(response, SC_OK, requestDescription.toString(), initialTime);
                        } else if (pathTokens.length == 1) {
                            // This is a 'create' request.

                            // Retrieve the local identifier from the request entry (if present).
                            String localIdentifier = retrieveLocalIdentifier(requestEntry, localRefMap);

                            // Retrieve the resource from the request entry.
                            Resource resource = requestEntry.getResource();
                            if (resource == null) {
                                String msg = "BundleEntry.resource is required for bundled create requests.";
                                throw buildRestException(msg, Status.BAD_REQUEST, IssueType.ValueSet.NOT_FOUND);
                            }

                            // Convert any local references found within the resource to their
                            // corresponding external reference.

                            ReferenceMappingVisitor<Resource> visitor = new ReferenceMappingVisitor<Resource>(localRefMap);
                            resource.accept(visitor);
                            final String errorMsg = visitor.getErrorMsg();
                            if (errorMsg != null) {
                                final String location = "<empty>";
                                OperationOutcome.Issue ooi =
                                        FHIRUtil.buildOperationOutcomeIssue(IssueSeverity.ValueSet.FATAL, IssueType.ValueSet.INVALID, errorMsg, location);

                                throw new FHIRHttpException(errorMsg, Status.BAD_REQUEST).withIssue(ooi);
                            }
                            resource = visitor.getResult();

                            // Perform the 'create' operation.
                            String ifNoneExist = request.getIfNoneExist() != null ? request.getIfNoneExist().getValue() : null;
                            FHIRRestOperationResponse ior = doCreate(pathTokens[0], resource, ifNoneExist, requestProperties);
                            
                            // Get the updated resource from FHIRRestOperationResponse which has the correct ID, meta etc.
                            resource = ior.getResource();

                            // Process and replace bundler Entry
                            Bundle.Entry resultEntry =
                                    setBundleResponseFields(responseEntry, ior.getResource(), ior.getLocationURI(), ior.getStatus().getStatusCode(), requestDescription.toString(), initialTime);

                            responseIndexAndEntries.put(entryIndex, resultEntry);

                            // Next, if a local identifier was present, we'll need to map this to the
                            // correct external identifier (e.g. Patient/12345).
                            addLocalRefMapping(localRefMap, localIdentifier, resource);
                        } else {
                            String msg = "Request URL for bundled create requests should have a path with exactly one token (<resourceType>).";
                            throw buildRestException(msg, Status.BAD_REQUEST, IssueType.ValueSet.NOT_FOUND);
                        }
                    } else if (request.getMethod().equals(HTTPVerb.PUT)) {
                        String type = null;
                        String id = null;

                        // Process a PUT (update).
                        if (pathTokens.length == 1) {
                            // A single-part url would be a conditional update: <type>?<query>
                            type = pathTokens[0];
                            if (query == null || query.isEmpty()) {
                                String msg = "A search query string is required for a conditional update operation.";
                                throw buildRestException(msg, Status.BAD_REQUEST, IssueType.ValueSet.INVALID);
                            }
                        } else if (pathTokens.length == 2) {
                            // A two-part url would be a normal update: <type>/<id>.
                            type = pathTokens[0];
                            id = pathTokens[1];
                        } else {
                            // A url with any other pattern is an error.
                            String msg =
                                    "Request URL for bundled PUT request should have path part with either one or two tokens (<resourceType> or <resourceType>/<id>).";
                            throw buildRestException(msg, Status.BAD_REQUEST, IssueType.ValueSet.INVALID);
                        }

                        // Retrieve the resource from the request entry.
                        Resource resource = requestEntry.getResource();

                        // Convert any local references found within the resource to their
                        // corresponding external reference.
                        ReferenceMappingVisitor<Resource> visitor = new ReferenceMappingVisitor<Resource>(localRefMap);
                        resource.accept(visitor);
                        final String errorMsg = visitor.getErrorMsg();
                        if (errorMsg != null) {
                            final String location = "<empty>";
                            OperationOutcome.Issue ooi =
                                    FHIRUtil.buildOperationOutcomeIssue(IssueSeverity.ValueSet.FATAL, IssueType.ValueSet.INVALID, errorMsg, location);

                            throw new FHIRHttpException(errorMsg, Status.BAD_REQUEST).withIssue(ooi);
                        }
                        resource = visitor.getResult();

                        // Perform the 'update' operation.
                        String ifMatchBundleValue = null;
                        if (request.getIfMatch() != null) {
                            ifMatchBundleValue = request.getIfMatch().getValue();
                        }
                        FHIRRestOperationResponse ior = doUpdate(type, id, resource, ifMatchBundleValue, query, requestProperties);

                        // Process and replace bundler Entry
                        Bundle.Entry resultEntry =
                                setBundleResponseFields(responseEntry, ior.getResource(), ior.getLocationURI(), ior.getStatus().getStatusCode(), requestDescription.toString(), initialTime);

                        responseIndexAndEntries.put(entryIndex, resultEntry);

                    } else if (request.getMethod().equals(HTTPVerb.DELETE)) {
                        String type = null;
                        String id = null;

                        // Process a DELETE.
                        if (pathTokens.length == 1) {
                            // A single-part url would be a conditional delete: <type>?<query>
                            type = pathTokens[0];
                            if (query == null || query.isEmpty()) {
                                String msg = "A search query string is required for a conditional delete operation.";
                                throw buildRestException(msg, Status.BAD_REQUEST, IssueType.ValueSet.INVALID);
                            }
                        } else if (pathTokens.length == 2) {
                            type = pathTokens[0];
                            id = pathTokens[1];
                        } else {
                            String msg =
                                    "Request URL for bundled DELETE request should have path part with one or two tokens (<resourceType> or <resourceType>/<id>).";
                            throw buildRestException(msg, Status.BAD_REQUEST, IssueType.ValueSet.INVALID);
                        }

                        // Perform the 'delete' operation.
                        FHIRRestOperationResponse ior = doDelete(type, id, query, requestProperties);

                        // Process and replace bundler Entry
                        Bundle.Entry resultEntry =
                                setBundleResponseFields(responseEntry, ior.getResource(), null, ior.getStatus().getStatusCode(), requestDescription.toString(), initialTime);

                        responseIndexAndEntries.put(entryIndex, resultEntry);
                    } else {
                        // Internal error, should not get here!
                        throw new IllegalStateException("Internal Server Error: reached an unexpected code location.");
                    }
                } catch (FHIRHttpException e) {
                    Bundle.Entry.Response.Builder responseBuilder = response.toBuilder();
                    responseBuilder.status(string(Integer.toString(e.getHttpStatus().getStatusCode())));

                    responseIndexAndEntries.put(entryIndex, responseEntryBuilder.resource(FHIRUtil.buildOperationOutcome(e, false)).response(responseBuilder.build()).build());

                    setBundleResponseStatus(response, e.getHttpStatus().getStatusCode(), requestDescription.toString(), initialTime);

                    if (failFast) {
                        String msg = "Error while processing request bundle.";
                        // Now, let's re-construct the responseBundle
                        responseBundle = reconstructResponseBundle(responseBundle, responseIndexAndEntries);
                        throw new FHIRRestBundledRequestException(msg, Response.Status.BAD_REQUEST, responseBundle, e).withIssue(e.getIssues());
                    }
                } catch (FHIRPersistenceResourceNotFoundException e) {
                    Bundle.Entry.Response.Builder responseBuilder = response.toBuilder();
                    responseBuilder.status(string(Integer.toString(SC_NOT_FOUND)));

                    responseIndexAndEntries.put(entryIndex, responseEntryBuilder.resource(FHIRUtil.buildOperationOutcome(e, false)).response(responseBuilder.build()).build());

                    setBundleResponseStatus(response, SC_NOT_FOUND, requestDescription.toString(), initialTime);

                    if (failFast) {
                        String msg = "Error while processing request bundle.";
                        // Now, let's re-construct the responseBundle
                        responseBundle = reconstructResponseBundle(responseBundle, responseIndexAndEntries);
                        throw new FHIRRestBundledRequestException(msg, Response.Status.NOT_FOUND, responseBundle, e).withIssue(e.getIssues());
                    }
                } catch (FHIRPersistenceResourceDeletedException e) {
                    Bundle.Entry.Response.Builder responseBuilder = response.toBuilder();
                    responseBuilder.status(string(Integer.toString(SC_GONE)));

                    responseIndexAndEntries.put(entryIndex, responseEntryBuilder.resource(FHIRUtil.buildOperationOutcome(e, false)).response(responseBuilder.build()).build());

                    setBundleResponseStatus(response, SC_GONE, requestDescription.toString(), initialTime);

                    if (failFast) {
                        String msg = "Error while processing request bundle.";
                        // Now, let's re-construct the responseBundle
                        responseBundle = reconstructResponseBundle(responseBundle, responseIndexAndEntries);
                        throw new FHIRRestBundledRequestException(msg, Response.Status.GONE, responseBundle, e).withIssue(e.getIssues());
                    }
                } catch (FHIROperationException e) {
                    Status status;
                    if (e instanceof FHIRSearchException) {
                        status = Status.BAD_REQUEST;              
                    } else {            
                        status = IssueTypeToHttpStatusMapper.issueListToStatus(e.getIssues());
                    }

                    Bundle.Entry.Response.Builder responseBuilder = response.toBuilder();
                    responseBuilder.status(string(Integer.toString(status.getStatusCode())));

                    responseIndexAndEntries.put(entryIndex, responseEntryBuilder.resource(FHIRUtil.buildOperationOutcome(e, false)).response(responseBuilder.build()).build());

                    setBundleResponseStatus(response, status.getStatusCode(), requestDescription.toString(), initialTime);

                    if (failFast) {
                        String msg = "Error while processing request bundle.";
                        // Now, let's re-construct the responseBundle
                        responseBundle = reconstructResponseBundle(responseBundle, responseIndexAndEntries);
                        throw new FHIRRestBundledRequestException(msg, status, responseBundle, e).withIssue(e.getIssues());
                    }
                }
            }
            // Now, let's re-construct the responseBundle
            responseBundle = reconstructResponseBundle(responseBundle, responseIndexAndEntries);
            return responseBundle;

        } finally {
            log.exiting(this.getClass().getName(), "processEntriesForMethod");
        }
    }
    
    
    
    /**
     * @param responseBundle
     * @param responseIndexAndEntries
     * @return
     */
    private Bundle reconstructResponseBundle(Bundle responseBundle, HashMap<Integer, Bundle.Entry> responseIndexAndEntries) {
        // Re-construct the responseBundle
        List<Bundle.Entry> responseEntries = new ArrayList<Bundle.Entry>();
        for (int i = 0; i < responseBundle.getEntry().size(); i++) {
            Bundle.Entry bundleEntry = responseIndexAndEntries.get(Integer.valueOf(i)) == null ? responseBundle.getEntry().get(i)
                    : responseIndexAndEntries.get(Integer.valueOf(i));
            responseEntries.add(bundleEntry);
        }

        responseBundle = responseBundle.toBuilder().entry(responseEntries).build();
        return responseBundle;
    }

    /**
     * This function will return the set of request properties specified via extensions belonging to the BundleRequest
     * plus any request properties associated with the "parent" bundle request. The "parent" bundle request refers to
     * the situation where a request bundle contains a request that is itself a request bundle. In this situation, the
     * higher-level bundle request might have request properties that should be pushed down and used by bundle requests
     * within that high-level bundle request.
     * 
     * @param extensions
     *            the list of extensions belonging to the BundleRequest
     * @param bundleRequestProperties
     *            request properties associated with a "parent" bundle request
     * @return
     */
    private Map<String, String> extractRequestPropertiesFromExtensions(List<Extension> extensions, Map<String, String> bundleRequestProperties)
        throws FHIROperationException {
        log.entering(this.getClass().getName(), "extractRequestPropertiesFromExtensions");

        try {
            Map<String, String> result = new HashMap<>();

            // First, add all the bundle request properties.
            if (bundleRequestProperties != null) {
                result.putAll(bundleRequestProperties);
            }

            // Next, extract request properties from the list of extensions.
            if (extensions != null) {
                for (Extension ext : extensions) {
                    String url = ext.getUrl();

                    // If we see an entry for the "http-request-header" URL, then parse the value.
                    if (REQUEST_HEADER_URL.equals(url)) {
                        String value = null;
                        if (ext.getValue() != null) {
                            value = ((com.ibm.watsonhealth.fhir.model.type.String) (ext.getValue())).getValue();
                        }

                        if (value == null) {
                            String msg = "The valueString field is required for a BundleRequest extension entry that specifies '" + REQUEST_HEADER_URL
                                    + "' for the URL identifier.";
                            throw buildRestException(msg, Status.BAD_REQUEST, IssueType.ValueSet.INVALID);
                        }

                        if (log.isLoggable(Level.FINER)) {
                            log.finer("Retrieved valueString from 'http-request-header' extension: " + value);
                        }

                        String[] tokens = value.split(":");
                        if (tokens.length >= 2) {
                            String headerName = tokens[0].trim();
                            String headerValue = tokens[1].trim();
                            result.put(headerName, headerValue);
                        } else {
                            String msg = "The proper syntax for a 'http-request-header' valueString field is '<headerName>: <headerValue>'";
                            throw buildRestException(msg, Status.BAD_REQUEST, IssueType.ValueSet.INVALID);
                        }
                    }
                }

                if (log.isLoggable(Level.FINER)) {
                    log.finer("Retrieved the following request properties: " + result.toString());
                }
            }

            return result;
        } finally {
            log.exiting(this.getClass().getName(), "extractRequestPropertiesFromExtensions");
        }
    }

    /**
     * Returns a list of Integers that provide the indices of the bundle entries associated with the specified http
     * method.
     * 
     * @param requestBundle
     *            the request bundle
     * @param httpMethod
     *            the http method to look for
     * @return
     */
    private List<Integer> getBundleRequestIndicesForMethod(Bundle requestBundle, Bundle responseBundle, HTTPVerb httpMethod) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < requestBundle.getEntry().size(); i++) {
            Bundle.Entry responseEntry = responseBundle.getEntry().get(i);
            Bundle.Entry requestEntry = requestBundle.getEntry().get(i);
            Bundle.Entry.Request request = requestEntry.getRequest();
            Bundle.Entry.Response response = responseEntry.getResponse();

            // If the response status is SC_OK which means the request passed the validation, 
            // and this request entry's http method is the one we're looking for,
            // then record the index in our list. 
            // (please notice that status can not be null since R4, So we set the response status as SC_OK
            // after the resource validation. )
            if (response.getStatus().equals(string(Integer.toString(SC_OK))) && request.getMethod().equals(httpMethod)) {
                indices.add(Integer.valueOf(i));
            }
        }
        return indices;
    }

    /**
     * This function sorts the request entries in the specified bundle, based on the path part of the entry's 'url'
     * field.
     * 
     * @param bundle
     *            the bundle containing the request entries to be sorted.
     * @return an array of Integer which provides the "sorted" ordering of request entry index values.
     */
    private void sortBundleRequestEntries(Bundle bundle, List<Integer> indices) {
        // Sort the list of indices based on the contents of their entries in the bundle.
        Collections.sort(indices, new BundleEntryComparator(bundle.getEntry()));
    }

    public static class BundleEntryComparator implements Comparator<Integer> {
        private List<Bundle.Entry> entries;

        public BundleEntryComparator(List<Bundle.Entry> entries) {
            this.entries = entries;
        }

        /*
         * (non-Javadoc)
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare(Integer indexA, Integer indexB) {
            Bundle.Entry a = entries.get(indexA);
            Bundle.Entry b = entries.get(indexB);
            String pathA = getUrlPath(a);
            String pathB = getUrlPath(b);

            log.fine("Comparing request entry URL paths: " + pathA + ", " + pathB);
            if (pathA != null && pathB != null) {
                return pathA.compareTo(pathB);
            } else if (pathA != null) {
                return 1;
            } else if (pathB != null) {
                return -1;
            }
            return 0;
        }
    }

    /**
     * This function converts the specified query string (a String) into an equivalent MultivaluedMap<String,String>
     * containing the query parameters defined in the query string.
     * 
     * @param queryString
     *            the query string to be processed
     * @return
     */
    private MultivaluedMap<String, String> getQueryParameterMap(String queryString) {
        MultivaluedMap<String, String> result = null;
        FHIRUrlParser parser = new FHIRUrlParser("foo?" + queryString);
        result = parser.getQueryParameters();
        return result;
    }

    /**
     * Returns the specified BundleEntry's path component of the 'url' field.
     * 
     * @param entry
     *            the bundle entry
     * @return the bundle entry's 'url' field's path component
     */
    private static String getUrlPath(Bundle.Entry entry) {
        String path = null;
        Bundle.Entry.Request request = entry.getRequest();
        if (request != null) {
            if (request.getUrl() != null && request.getUrl().getValue() != null) {
                FHIRUrlParser requestURL = new FHIRUrlParser(request.getUrl().getValue());
                path = requestURL.getPath();
            }
        }

        return path;
    }

    /**
     * This method will add a mapping to the local-to-external identifier map if the specified localIdentifier is
     * non-null.
     * 
     * @param localRefMap
     *            the map containing the local-to-external identifier mappings
     * @param localIdentifier
     *            the localIdentifier previously obtained for the resource
     * @param resource
     *            the resource for which an external identifier will be built
     */
    private void addLocalRefMapping(Map<String, String> localRefMap, String localIdentifier, Resource resource) {
        if (localIdentifier != null) {
            String externalIdentifier = FHIRUtil.getResourceTypeName(resource) + "/" + resource.getId().getValue();
            localRefMap.put(localIdentifier, externalIdentifier);
            log.finer("Added local/ext identifier mapping: " + localIdentifier + " --> " + externalIdentifier);
        }
    }

    /**
     * This method will retrieve the local identifier associated with the specified bundle request entry, or return null
     * if the fullUrl field is not specified or doesn't contain a local identifier.
     * 
     * @param requestEntry
     *            the bundle request entry
     * @param localRefMap
     *            the Map containing the local-to-external reference mappings
     * @return
     */
    private String retrieveLocalIdentifier(Bundle.Entry requestEntry, Map<String, String> localRefMap) throws Exception {
        String localIdentifier = null;
        if (requestEntry.getFullUrl() != null) {
            String fullUrl = requestEntry.getFullUrl().getValue();
            if (fullUrl != null && fullUrl.startsWith(LOCAL_REF_PREFIX)) {
                localIdentifier = fullUrl;
                log.finer("Request entry contains local identifier: " + localIdentifier);
                if (localRefMap.get(localIdentifier) != null) {
                    String msg = "Duplicate local identifier encountered in bundled request entry: " + localIdentifier;
                    throw buildRestException(msg, Status.BAD_REQUEST, IssueType.ValueSet.DUPLICATE);
                }
            }
        }
        return localIdentifier;
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

    private Bundle.Entry setBundleResponseFields(Bundle.Entry responseEntry, Resource resource, URI locationURI, int httpStatus, String requestDescription,
        long initialTime) throws FHIROperationException {
        Bundle.Entry.Response response = responseEntry.getResponse();
        Bundle.Entry.Response.Builder resBuilder = response.toBuilder();
        resBuilder.status(string(Integer.toString(httpStatus)));

        Bundle.Entry.Builder bundleEntryBuilder = responseEntry.toBuilder();

        if (resource != null) {
            resBuilder = resBuilder.id(resource.getId().getValue()).lastModified(resource.getMeta().getLastUpdated()).etag(string(getEtagValue(resource)));

            bundleEntryBuilder = bundleEntryBuilder.resource(resource);

        }
        if (locationURI != null) {
            resBuilder = resBuilder.location(Uri.of(locationURI.toString()));
        }

        logBundleRequestCompletedMsg(requestDescription, initialTime, httpStatus);
        return bundleEntryBuilder.response(resBuilder.build()).build();
    }

    private void setBundleResponseStatus(Bundle.Entry.Response response, int httpStatus, String requestDescription, long initialTime) {
        logBundleRequestCompletedMsg(requestDescription, initialTime, httpStatus);
    }

    private void logBundleRequestCompletedMsg(String requestDescription, long initialTime, int httpStatus) {
        StringBuffer statusMsg = new StringBuffer();
        statusMsg.append(" status:[" + httpStatus + "]");
        double elapsedSecs = (System.currentTimeMillis() - initialTime) / 1000.0;
        log.info("Completed bundle request[" + elapsedSecs + " secs]: " + requestDescription.toString() + statusMsg.toString());
    }

    /**
     * Adds the Etag and Last-Modified headers to the specified response object.
     */
    private ResponseBuilder addHeaders(ResponseBuilder rb, Resource resource) {
        return rb.header(HttpHeaders.ETAG, getEtagValue(resource)).header(HttpHeaders.LAST_MODIFIED, resource.getMeta().getLastUpdated().getValue().toString());
    }

    private String getEtagValue(Resource resource) {
        return "W/\"" + resource.getMeta().getVersionId().getValue() + "\"";
    }

    private Response exceptionResponse(FHIRRestBundledRequestException e) {
        Response response;
        if (e.getResponseBundle() != null) {
            if (e.getIssues() != null && e.getIssues().size() > 0) {
                // R4 says we should return a single OperationOutcome with the issues:
                // http://www.hl7.org/fhir/r4/http.html#transaction-response
                String msg =
                        "FHIRRestBundledRequestException contains both a response bundle and a list of issues. " + "Only the response bundle will be returned.";
                log.log(Level.WARNING, msg, e);
            }

            List<Bundle.Entry> toAdd = new ArrayList<Bundle.Entry>();
            // replace bundle entries that have an empty response
            for (int i = 0; i < e.getResponseBundle().getEntry().size(); i++) {
                Bundle.Entry entry = e.getResponseBundle().getEntry().get(i);
                if (entry.getResponse() != null && entry.getResponse().getStatus() == null) {

                    entry = entry.toBuilder().response(entry.getResponse().toBuilder()
                            .status(string(Integer.toString(Status.BAD_REQUEST.getStatusCode()))).build()).build();
                }
                toAdd.add(entry);
            }

            Bundle responseBundle = e.getResponseBundle().toBuilder().entry(toAdd).build();

            response = Response.status(e.getHttpStatus()).entity(responseBundle).build();
        } else {
            response = exceptionResponse(e, e.getHttpStatus());
        }
        return response;
    }

    private Response exceptionResponse(FHIRHttpException e) {
        return exceptionResponse(e, e.getHttpStatus());
    }

    private Response exceptionResponse(FHIROperationException e) {
        Status status;
        if (e instanceof FHIRSearchException) {
            status = Status.BAD_REQUEST;              
        } else {            
            status = IssueTypeToHttpStatusMapper.issueListToStatus(e.getIssues());
        }
        return exceptionResponse(e, status);
    }

    private Response exceptionResponse(FHIROperationException e, Status status) {
        if (status == null) {
            status = IssueTypeToHttpStatusMapper.issueListToStatus(e.getIssues());
        }
        OperationOutcome operationOutcome = FHIRUtil.buildOperationOutcome(e, false);
        return exceptionResponse(operationOutcome, status);
    }

    private Response exceptionResponse(Exception e, Status status) {
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
            FHIRGenerator.generator( Format.JSON, false).generate(oo, sw);
            return sw.toString();
        } catch (Throwable t) {
            return "Error encountered while serializing OperationOutcome resource: " + t.getMessage();
        }
    }

    private synchronized CapabilityStatement getCapabilityStatement() throws Exception {
        try {
            CapabilityStatement capability = buildCapabilityStatement();
            return capability;
        } catch (Throwable t) {
            String msg = "An error occurred while constructing the Conformance statement.";
            log.log(Level.SEVERE, msg, t);
            throw new FHIRHttpException(msg, Status.INTERNAL_SERVER_ERROR);
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
        List<String> resourceTypes = FHIRUtil.getResourceTypeNames();
        for (String resourceType : resourceTypes) {

            // Build the set of ConformanceSearchParams for this resource type.
            List<Rest.Resource.SearchParam> conformanceSearchParams = new ArrayList<>();
            List<SearchParameter> searchParameters = SearchUtil.getSearchParameters(resourceType);
            if (searchParameters != null) {
                for (SearchParameter searchParameter : searchParameters) {
                    // Issue 202: the name here is a natural language name, and intentionally not replaced with code.
                    Rest.Resource.SearchParam.Builder conformanceSearchParamBuilder =
                            Rest.Resource.SearchParam.builder().name(searchParameter.getName()).type(searchParameter.getType());
                    if (searchParameter.getDescription() != null) {
                        conformanceSearchParamBuilder.documentation(searchParameter.getDescription());
                    }

                    Rest.Resource.SearchParam conformanceSearchParam = conformanceSearchParamBuilder.build();
                    conformanceSearchParams.add(conformanceSearchParam);
                }
            }

            // Build the ConformanceResource for this resource type.
            Rest.Resource cr =
                    Rest.Resource.builder().type(ResourceType.of(resourceType)).profile(Canonical.of("http://hl7.org/fhir/profiles/"
                            + resourceType)).interaction(interactions)
                    .conditionalCreate(com.ibm.watsonhealth.fhir.model.type.Boolean.of(true))
                    .conditionalUpdate(com.ibm.watsonhealth.fhir.model.type.Boolean.of(true))
                    .updateCreate(com.ibm.watsonhealth.fhir.model.type.Boolean.of(isUpdateCreateEnabled()))
                    .conditionalDelete(ConditionalDeleteStatus.of(ConditionalDeleteStatus.ValueSet.SINGLE))
                    .build();

            resources.add(cr);
        }

        // Determine if transactions are supported for this FHIR Server configuration.
        SystemRestfulInteraction transactionMode = SystemRestfulInteraction.BATCH;
        try {
            boolean txnSupported = getPersistenceImpl().isTransactional();
            transactionMode = (txnSupported ? SystemRestfulInteraction.TRANSACTION : SystemRestfulInteraction.BATCH);
        } catch (Throwable t) {
            log.log(Level.WARNING, "Unexcepted error while reading server transaction mode setting", t);
        }

        String actualHost = uriInfo.getBaseUri().getHost();

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
                .service(CodeableConcept.builder().coding(Coding.builder().code(Code.of("SMART-on-FHIR"))
                        .system(Uri.of("http://terminology.hl7.org/CodeSystem/restful-security-service")).build())
                        .text(string("OAuth2 using SMART-on-FHIR profile (see http://docs.smarthealthit.org)")).build())
                .extension(Extension.builder().url("http://fhir-registry.smarthealthit.org/StructureDefinition/oauth-uris")
                        .extension(Extension.builder().url("token").value(Url.of(tokenURL)).build(), 
                                Extension.builder().url("authorize").value(Url.of(authURL)).build(), 
                                Extension.builder().url("register").value(Url.of(regURL)).build()).build())
                .build();

        CapabilityStatement.Rest rest = CapabilityStatement.Rest.builder().mode(RestfulCapabilityMode.SERVER)
                .security(restSecurity)
                .resource(resources)
                .interaction(CapabilityStatement.Rest.Interaction.builder().code(transactionMode).build())
                .build();

        FHIRBuildIdentifier buildInfo = new FHIRBuildIdentifier();
        String buildDescription = FHIR_SERVER_NAME + " version " + buildInfo.getBuildVersion() + " build id " + buildInfo.getBuildId() + "";

        List<Code> format = new ArrayList<Code>();
        format.add(Code.of(MediaType.APPLICATION_JSON));
        format.add(Code.of(MediaType.APPLICATION_FHIR_JSON));
        format.add(Code.of(MediaType.APPLICATION_XML));
        format.add(Code.of(MediaType.APPLICATION_FHIR_XML));

        // Finally, create the Conformance resource itself.
        CapabilityStatement conformance = CapabilityStatement.builder()
                                          .status(PublicationStatus.ACTIVE)
                                          .date(DateTime.of(ZonedDateTime.now(ZoneOffset.UTC)))
                                          .kind(CapabilityStatementKind.of(CapabilityStatementKind.ValueSet.CAPABILITY))
                                          .fhirVersion(FHIRVersion.VERSION_4_0_0)
                                          .format(format)
                                          .patchFormat(Code.of(MediaType.APPLICATION_JSON_PATCH))
                                          .version(string(buildInfo.getBuildVersion()))
                                          .name(string(FHIR_SERVER_NAME))
                                          .description(com.ibm.watsonhealth.fhir.model.type.Markdown.of(buildDescription))
                                          .copyright(com.ibm.watsonhealth.fhir.model.type.Markdown.of(FHIR_COPY_RIGHT))
                                          .publisher(string("IBM Corporation"))
                                          .software(CapabilityStatement.Software.builder()
                                                    .name(string(FHIR_SERVER_NAME))
                                                    .version(string(buildInfo.getBuildVersion()))
                                                    .id(buildInfo.getBuildId()).build())
                                          .rest(rest)
                                          .build();

        try {
            conformance = addExtensionElements(conformance);
        } catch (Exception e) {
            log.log(Level.SEVERE, "An error occurred while adding extension elements to the conformance statement", e);
        }

        return conformance;
    }

    private CapabilityStatement addExtensionElements(CapabilityStatement capabilityStatement) throws Exception {
        List<Extension> extentions = new ArrayList<Extension>();
        Extension extension = Extension.builder().url(EXTENSION_URL
                + "/defaultTenantId").value(string(fhirConfig.getStringProperty(FHIRConfiguration.PROPERTY_DEFAULT_TENANT_ID, FHIRConfiguration.DEFAULT_TENANT_ID))).build();
        extentions.add(extension);

        extension = Extension.builder().url(EXTENSION_URL
                + "/encryptionEnabled").value(com.ibm.watsonhealth.fhir.model.type.Boolean.of(fhirConfig.getPropertyGroup(FHIRConfiguration.PROPERTY_ENCRYPTION).getBooleanProperty("enabled", Boolean.FALSE))).build();
        extentions.add(extension);

        extension = Extension.builder().url(EXTENSION_URL
                + "/userDefinedSchematronEnabled").value(com.ibm.watsonhealth.fhir.model.type.Boolean.of(isUserDefinedSchematronEnabled())).build();
        extentions.add(extension);

        extension = Extension.builder().url(EXTENSION_URL
                + "/virtualResourcesEnabled").value(com.ibm.watsonhealth.fhir.model.type.Boolean.of(isVirtualResourceTypesFeatureEnabled())).build();
        extentions.add(extension);

        extension = Extension.builder().url(EXTENSION_URL
                + "/allowableVirtualResourceTypes").value(string(getAllowableVirtualResourceTypes().toString().replace("[", "").replace("]", "").replace(" ", ""))).build();
        extentions.add(extension);

        extension = Extension.builder().url(EXTENSION_URL
                + "/websocketNotificationsEnabled").value(com.ibm.watsonhealth.fhir.model.type.Boolean.of(fhirConfig.getBooleanProperty(FHIRConfiguration.PROPERTY_WEBSOCKET_ENABLED, Boolean.FALSE))).build();
        extentions.add(extension);

        extension = Extension.builder().url(EXTENSION_URL
                + "/kafkaNotificationsEnabled").value(com.ibm.watsonhealth.fhir.model.type.Boolean.of(fhirConfig.getBooleanProperty(FHIRConfiguration.PROPERTY_KAFKA_ENABLED, Boolean.FALSE))).build();
        extentions.add(extension);

        String notificationResourceTypes = getNotificationResourceTypes();
        if ("".equals(notificationResourceTypes)) {
            notificationResourceTypes = "<not specified - all resource types>";
        }

        extension = Extension.builder().url(EXTENSION_URL + "/notificationResourceTypes").value(string(notificationResourceTypes)).build();
        extentions.add(extension);

        String auditLogServiceName = fhirConfig.getStringProperty(FHIRConfiguration.PROPERTY_AUDIT_SERVICE_CLASS_NAME);

        if (auditLogServiceName == null || "".equals(auditLogServiceName)) {
            auditLogServiceName = "<not specified>";
        } else {
            int lastDelimeter = auditLogServiceName.lastIndexOf(".");
            auditLogServiceName = auditLogServiceName.substring(lastDelimeter + 1);
        }

        extension = Extension.builder().url(EXTENSION_URL + "/auditLogServiceName").value(string(auditLogServiceName)).build();
        extentions.add(extension);

        PropertyGroup auditLogProperties = fhirConfig.getPropertyGroup(FHIRConfiguration.PROPERTY_AUDIT_SERVICE_PROPERTIES);
        String auditLogPropertiesString = auditLogProperties != null ? auditLogProperties.toString() : "<not specified>";
        extension = Extension.builder().url(EXTENSION_URL + "/auditLogProperties").value(string(auditLogPropertiesString)).build();
        extentions.add(extension);

        extension = Extension.builder().url(EXTENSION_URL + "/persistenceType").value(string(getPersistenceImpl().getClass().getSimpleName())).build();
        extentions.add(extension);

        return capabilityStatement.toBuilder().extension(extentions).build();

    }

    private String getNotificationResourceTypes() throws Exception {
        Object[] notificationResourceTypes = fhirConfig.getArrayProperty(FHIRConfiguration.PROPERTY_NOTIFICATION_RESOURCE_TYPES);
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
     * Creates a bundle that will hold results for a search operation.
     * 
     * @param resources
     *            the list of resources to include in the bundle
     * @param searchContext
     *            the FHIRSearchContext object associated with the search
     * @return the bundle
     * @throws FHIROperationException
     */
    private Bundle createSearchBundle(List<Resource> resources, FHIRSearchContext searchContext) throws FHIROperationException {
        // generate ID for this bundle and set total

        Bundle.Builder bundleBuider =
                Bundle.builder().type(BundleType.SEARCHSET).id(Id.of(UUID.randomUUID().toString())).total(com.ibm.watsonhealth.fhir.model.type.UnsignedInt.of((int) (long) searchContext.getTotalCount()));

        for (Resource resource : resources) {
            if (resource.getId() == null || !resource.getId().hasValue()) {
                throw new IllegalStateException("Returned resources must have an id.");
            }
            Bundle.Entry entry = Bundle.Entry.builder()
                    .fullUrl(Uri.of(getRequestBaseUri() + "/" + resource.getClass().getSimpleName() + "/" + resource.getId().getValue()))
                    .resource(resource).build();

            bundleBuider.entry(entry);
        }

        Bundle bundle = bundleBuider.build();

        // Add the SUBSETTED tag, if the _elements search result parameter was applied to limit elements included in
        // returned resources.
        if (searchContext.hasElementsParameters()) {
            bundle = (Bundle) FHIRUtil.addTag(bundle, SearchConstants.SUBSETTED_TAG);
        }

        return bundle;
    }

    /**
     * Creates a bundle that will hold the results of a history operation.
     * 
     * @param resources
     *            the list of resources to include in the bundle
     * @param historyContext
     *            the FHIRHistoryContext associated with the history operation
     * @return the bundle
     */
    private Bundle createHistoryBundle(List<Resource> resources, FHIRHistoryContext historyContext) throws FHIROperationException {
        // generate ID for this bundle and set the "total" field for the bundle
        Bundle.Builder bundleBuilder =
                Bundle.builder().type(BundleType.HISTORY).id(Id.of(UUID.randomUUID().toString())).total(com.ibm.watsonhealth.fhir.model.type.UnsignedInt.of((int) (long) historyContext.getTotalCount()));

        Map<String, List<Integer>> deletedResourcesMap = historyContext.getDeletedResources();

        for (int i = 0; i < resources.size(); i++) {
            Resource resource = resources.get(i);

            if (resource.getId() == null || !resource.getId().hasValue()) {
                throw new IllegalStateException("Returned resources must have an id.");
            }

            Integer versionId = Integer.valueOf(resource.getMeta().getVersionId().getValue());
            String logicalId = resource.getId().getValue();
            String resourceType = FHIRUtil.getResourceTypeName(resource);
            List<Integer> deletedVersions = deletedResourcesMap.get(logicalId);

            // Determine the correct method to include in this history entry (POST, PUT, DELETE).
            HTTPVerb method;
            if (deletedVersions != null && deletedVersions.contains(versionId)) {
                method = HTTPVerb.DELETE;
            } else if (versionId == 1) {
                method = HTTPVerb.POST;
            } else {
                method = HTTPVerb.PUT;
            }

            // Create the 'request' entry, and set the request.url field.
            // 'create' --> url = "<resourceType>"
            // 'update'/'delete' --> url = "<resourceType>/<logicalId>"
            Bundle.Entry.Request request = Bundle.Entry.Request.builder()
                                           .method(method)
                                           .url(Url.of(method == HTTPVerb.POST ? resourceType : resourceType + "/" + logicalId))
                                           .build();

            Bundle.Entry.Response response = Bundle.Entry.Response.builder().status(string("200")).build();
            
            Bundle.Entry entry = Bundle.Entry.builder()
                                 .request(request)
                                 .fullUrl(Uri.of(getRequestBaseUri() + "/" + resource.getClass().getSimpleName() + "/" + resource.getId().getValue()))
                                 .response(response)
                                 .resource(resource)
                                 .build();

            bundleBuilder.entry(entry);
        }

        return bundleBuilder.build();
    }

    /**
     * Retrieves the shared interceptor mgr instance from the servlet context.
     */
    private FHIRPersistenceInterceptorMgr getInterceptorMgr() {
        return FHIRPersistenceInterceptorMgr.getInstance();
    }

    /**
     * Retrieves the shared persistence helper object from the servlet context.
     */
    private synchronized PersistenceHelper getPersistenceHelper() {
        if (persistenceHelper == null) {
            persistenceHelper = (PersistenceHelper) context.getAttribute(FHIRPersistenceHelper.class.getName());
            if (log.isLoggable(Level.FINE)) {
                log.fine("Retrieved FHIRPersistenceHelper instance from servlet context: " + persistenceHelper);
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

    private boolean isAllowableVirtualResourceType(String virtualResourceType) throws Exception {
        return getAllowableVirtualResourceTypes().contains(virtualResourceType) || getAllowableVirtualResourceTypes().contains("*");
    }

    private List<String> getAllowableVirtualResourceTypes() throws Exception {
        List<String> result = FHIRConfigHelper.getStringListProperty(PROPERTY_ALLOWABLE_VIRTUAL_RESOURCE_TYPES);
        if (result == null) {
            result = new ArrayList<>();
            result.add("*");
        }
        return result;
    }

    private Boolean isVirtualResourceTypesFeatureEnabled() {
        return FHIRConfigHelper.getBooleanProperty(PROPERTY_VIRTUAL_RESOURCES_ENABLED, Boolean.FALSE);
    }

    private Boolean isUserDefinedSchematronEnabled() {
        return FHIRConfigHelper.getBooleanProperty(PROPERTY_USER_DEFINED_SCHEMATRON_ENABLED, Boolean.FALSE);
    }

    private boolean isDeleteSupported() throws FHIRPersistenceException {
        return getPersistenceImpl().isDeleteSupported();
    }

    private Boolean isUpdateCreateEnabled() {
        return fhirConfig.getBooleanProperty(PROPERTY_UPDATE_CREATE_ENABLED, Boolean.TRUE);
    }

    private Parameter createBasicCodeSearchParameter(String type) {
        Parameter basicCodeSearchParameter = new Parameter(com.ibm.watsonhealth.fhir.search.SearchConstants.Type.TOKEN, "code", null, null);
        ParameterValue value = new ParameterValue();
        value.setValueCode(type);
        value.setValueSystem(BASIC_RESOURCE_TYPE_URL);
        basicCodeSearchParameter.getValues().add(value);
        return basicCodeSearchParameter;
    }

    private Bundle addLinks(FHIRPagingContext context, Bundle bundle, String requestUri) {
        String selfUri = null;
        Bundle.Builder bundleBuilder = bundle.toBuilder();
        if (context instanceof FHIRSearchContext) {
            try {
                selfUri = SearchUtil.buildSearchSelfUri(requestUri, (FHIRSearchContext) context);
            } catch (Exception e) {
                log.log(Level.WARNING, "Unable to construct self link for search result bundle; using the request URI instead.", e);
            }
        }
        if (selfUri == null) {
            selfUri = requestUri;
        }
        // create 'self' link
        Bundle.Link selfLink = Bundle.Link.builder().relation(string("self")).url(Url.of(selfUri)).build();
        bundleBuilder.link(selfLink);

        int nextPageNumber = context.getPageNumber() + 1;
        if (nextPageNumber <= context.getLastPageNumber()) {

            // starting with the self URI
            String nextLinkUrl = selfUri;

            // remove existing _page parameters from the query string
            nextLinkUrl = nextLinkUrl.replace("&_page=" + context.getPageNumber(), "").replace("_page=" + context.getPageNumber() + "&", "").replace("_page="
                    + context.getPageNumber(), "");

            if (nextLinkUrl.contains("?")) {
                if (!nextLinkUrl.endsWith("?")) {
                    // there are other parameters in the query string
                    nextLinkUrl += "&";
                }
            } else {
                nextLinkUrl += "?";
            }

            // add new _page parameter to the query string
            nextLinkUrl += "_page=" + nextPageNumber;

            // create 'next' link
            Bundle.Link nextLink = Bundle.Link.builder().relation(string("next")).url(Url.of(nextLinkUrl)).build();
            bundleBuilder.link(nextLink);
        }

        int prevPageNumber = context.getPageNumber() - 1;
        if (prevPageNumber > 0) {

            // starting with the original request URI
            String prevLinkUrl = requestUri;

            // remove existing _page parameters from the query string
            prevLinkUrl = prevLinkUrl.replace("&_page=" + context.getPageNumber(), "").replace("_page=" + context.getPageNumber() + "&", "").replace("_page="
                    + context.getPageNumber(), "");

            if (prevLinkUrl.contains("?")) {
                if (!prevLinkUrl.endsWith("?")) {
                    // there are other parameters in the query string
                    prevLinkUrl += "&";
                }
            } else {
                prevLinkUrl += "?";
            }

            // add new _page parameter to the query string
            prevLinkUrl += "_page=" + prevPageNumber;

            // create 'previous' link
            Bundle.Link prevLink = Bundle.Link.builder().relation(string("previous")).url(Url.of(prevLinkUrl)).build();
            bundleBuilder.link(prevLink);
        }
        return bundleBuilder.build();
    }

    /**
     * Builds an OperationOutcomeIssue with the respective values for some of the fields.
     */
    private OperationOutcome.Issue buildOperationOutcomeIssue(IssueSeverity.ValueSet severity, IssueType.ValueSet type, String msg) {
        return OperationOutcome.Issue.builder().severity(IssueSeverity.of(severity)).code(IssueType.of(type)).diagnostics(string(msg)).build();

    }

    /**
     * Retrieves the Resource from the specified BundleEntry's ResourceContainer.
     *
     * @param entry
     *            the BundleEntry holding the Resource
     * @return the Resource
     * @throws FHIROperationException
     */
    private Resource getBundleEntryResource(Bundle.Entry entry) throws FHIROperationException {
        try {
            return entry.getResource();
        } catch (Throwable t) {
            FHIROperationException e = new FHIROperationException("Unable to retrieve resource from BundleEntry's resource container.", t);
            log.log(Level.SEVERE, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Builds a collection of properties that will be passed to the persistence interceptors.
     * 
     * @throws FHIRPersistenceException
     */
    private Map<String, Object> buildPersistenceEventProperties(String type, String id, String version, Map<String, String> requestProperties)
        throws FHIRPersistenceException {
        Map<String, Object> props = new HashMap<>();
        props.put(FHIRPersistenceEvent.PROPNAME_PERSISTENCE_IMPL, getPersistenceImpl());
        props.put(FHIRPersistenceEvent.PROPNAME_URI_INFO, uriInfo);
        props.put(FHIRPersistenceEvent.PROPNAME_HTTP_HEADERS, httpHeaders);
        props.put(FHIRPersistenceEvent.PROPNAME_REQUEST_PROPERTIES, requestProperties);
        props.put(FHIRPersistenceEvent.PROPNAME_SECURITY_CONTEXT, securityContext);
        props.put(FHIRPersistenceEvent.PROPNAME_TXN_CORRELATION_ID, bundleTransactionCorrelationId);
        props.put(FHIRPersistenceEvent.PROPNAME_REQUEST_CORRELATION_ID, bundleRequestCorrelationId);
        if (type != null) {
            props.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, type);
        }
        if (id != null) {
            props.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID, id);
        }
        if (version != null) {
            props.put(FHIRPersistenceEvent.PROPNAME_VERSION_ID, version);
        }
        return props;
    }

    /**
     * This method returns the equivalent of: uriInfo.getRequestUri().toString() This method is necessary to provide a
     * workaround to a bug in uriInfo.getRequestUri() where an IllegalArgumentException is thrown by getRequestUri()
     * when the query string portion contains a vertical bar | character. The vertical bar is one known case of a
     * special character causing the exception. There could be others.
     * 
     * @return String The complete request URI
     */
    private String getRequestUri() {

        String queryString = null;
        StringBuilder requestUri = new StringBuilder();

        requestUri.append(httpServletRequest.getRequestURL());
        queryString = httpServletRequest.getQueryString();
        if (queryString != null && !queryString.isEmpty()) {
            requestUri.append("?").append(queryString);
        }
        return requestUri.toString();
    }

    /**
     * This method returns the "base URI" associated with the current request. For example, if a client invoked POST
     * https://myhost:9443/fhir-server/api/v4/Patient to create a Patient resource, this method would return
     * "https://myhost:9443/fhir-server/api/v4".
     * 
     * @return The base endpoint URI associated with the current request.
     */
    private String getRequestBaseUri() {
        StringBuilder sb = new StringBuilder();
        sb.append(httpServletRequest.getScheme()).append("://").append(httpServletRequest.getServerName()).append(":").append(httpServletRequest.getServerPort()).append(httpServletRequest.getContextPath());
        String servletPath = httpServletRequest.getServletPath();
        if (servletPath != null && !servletPath.isEmpty()) {
            sb.append(servletPath);
        }

        return sb.toString();
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

    private Response buildResponse(FHIROperationContext operationContext, Resource resource) throws URISyntaxException {
        URI locationURI = (URI) operationContext.getProperty(FHIROperationContext.PROPNAME_LOCATION_URI);
        if (locationURI != null) {
            return Response.ok().location(toUri(getAbsoluteUri(getRequestBaseUri(), locationURI.toString()))).entity(resource).build();
        }
        return Response.ok().entity(resource).build();
    }
}
