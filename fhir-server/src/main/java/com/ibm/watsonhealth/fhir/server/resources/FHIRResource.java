/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.resources;

import static com.ibm.watsonhealth.fhir.config.FHIRConfiguration.PROPERTY_ALLOWABLE_VIRTUAL_RESOURCE_TYPES;
import static com.ibm.watsonhealth.fhir.config.FHIRConfiguration.PROPERTY_UPDATE_CREATE_ENABLED;
import static com.ibm.watsonhealth.fhir.config.FHIRConfiguration.PROPERTY_USER_DEFINED_SCHEMATRON_ENABLED;
import static com.ibm.watsonhealth.fhir.config.FHIRConfiguration.PROPERTY_VIRTUAL_RESOURCES_ENABLED;
import static com.ibm.watsonhealth.fhir.config.FHIRConfiguration.PROPERTY_OAUTH_REGURL;
import static com.ibm.watsonhealth.fhir.config.FHIRConfiguration.PROPERTY_OAUTH_AUTHURL;
import static com.ibm.watsonhealth.fhir.config.FHIRConfiguration.PROPERTY_OAUTH_TOKENURL;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.bool;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.getResourceType;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.id;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.string;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.uri;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_METHOD_NOT_ALLOWED;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;

import java.math.BigInteger;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
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

import com.ibm.watsonhealth.fhir.config.FHIRConfiguration;
import com.ibm.watsonhealth.fhir.config.PropertyGroup;
import com.ibm.watsonhealth.fhir.core.FHIRUtilities;
import com.ibm.watsonhealth.fhir.core.MediaType;
import com.ibm.watsonhealth.fhir.core.context.FHIRPagingContext;
import com.ibm.watsonhealth.fhir.exception.FHIRException;
import com.ibm.watsonhealth.fhir.exception.FHIRVirtualResourceTypeException;
import com.ibm.watsonhealth.fhir.model.Bundle;
import com.ibm.watsonhealth.fhir.model.BundleEntry;
import com.ibm.watsonhealth.fhir.model.BundleLink;
import com.ibm.watsonhealth.fhir.model.BundleRequest;
import com.ibm.watsonhealth.fhir.model.BundleResponse;
import com.ibm.watsonhealth.fhir.model.BundleTypeList;
import com.ibm.watsonhealth.fhir.model.ConditionalDeleteStatusList;
import com.ibm.watsonhealth.fhir.model.Conformance;
import com.ibm.watsonhealth.fhir.model.ConformanceInteraction;
import com.ibm.watsonhealth.fhir.model.ConformanceResource;
import com.ibm.watsonhealth.fhir.model.ConformanceRest;
import com.ibm.watsonhealth.fhir.model.ConformanceSearchParam;
import com.ibm.watsonhealth.fhir.model.ConformanceStatementKindList;
import com.ibm.watsonhealth.fhir.model.Extension;
import com.ibm.watsonhealth.fhir.model.HTTPVerbList;
import com.ibm.watsonhealth.fhir.model.IssueSeverityList;
import com.ibm.watsonhealth.fhir.model.IssueTypeList;
import com.ibm.watsonhealth.fhir.model.NarrativeStatusList;
import com.ibm.watsonhealth.fhir.model.ObjectFactory;
import com.ibm.watsonhealth.fhir.model.OperationOutcome;
import com.ibm.watsonhealth.fhir.model.OperationOutcomeIssue;
import com.ibm.watsonhealth.fhir.model.Parameters;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.model.ResourceContainer;
import com.ibm.watsonhealth.fhir.model.RestfulConformanceModeList;
import com.ibm.watsonhealth.fhir.model.SearchParameter;
import com.ibm.watsonhealth.fhir.model.TransactionModeList;
import com.ibm.watsonhealth.fhir.model.TypeRestfulInteractionList;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.operation.FHIROperation;
import com.ibm.watsonhealth.fhir.operation.exception.FHIROperationException;
import com.ibm.watsonhealth.fhir.operation.registry.FHIROperationRegistry;
import com.ibm.watsonhealth.fhir.operation.util.FHIROperationUtil;
import com.ibm.watsonhealth.fhir.persistence.FHIRPersistence;
import com.ibm.watsonhealth.fhir.persistence.FHIRPersistenceTransaction;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRHistoryContext;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceResourceNotFoundException;
import com.ibm.watsonhealth.fhir.persistence.helper.FHIRPersistenceHelper;
import com.ibm.watsonhealth.fhir.persistence.helper.PersistenceHelper;
import com.ibm.watsonhealth.fhir.persistence.interceptor.FHIRPersistenceEvent;
import com.ibm.watsonhealth.fhir.persistence.interceptor.impl.FHIRPersistenceInterceptorMgr;
import com.ibm.watsonhealth.fhir.persistence.util.FHIRPersistenceUtil;
import com.ibm.watsonhealth.fhir.search.Parameter;
import com.ibm.watsonhealth.fhir.search.ParameterValue;
import com.ibm.watsonhealth.fhir.search.context.FHIRSearchContext;
import com.ibm.watsonhealth.fhir.search.util.SearchUtil;
import com.ibm.watsonhealth.fhir.server.FHIRBuildIdentifier;
import com.ibm.watsonhealth.fhir.server.exception.FHIRRestBundledRequestException;
import com.ibm.watsonhealth.fhir.server.exception.FHIRRestException;
import com.ibm.watsonhealth.fhir.server.helper.FHIRUrlParser;
import com.ibm.watsonhealth.fhir.server.util.RestAuditLogger;
import com.ibm.watsonhealth.fhir.validation.FHIRValidator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;

@Path("/")
@Produces({ MediaType.APPLICATION_JSON_FHIR, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML_FHIR, MediaType.APPLICATION_XML })
@Consumes({ MediaType.APPLICATION_JSON_FHIR, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML_FHIR, MediaType.APPLICATION_XML })
@Api
public class FHIRResource {
    private static final Logger log = java.util.logging.Logger.getLogger(FHIRResource.class.getName());
    
    private static final String FHIR_SERVER_NAME = "IBM Watson Health Cloud FHIR Server";
    private static final String FHIR_SPEC_VERSION = "1.0.2 - DSTU2";
    private static final String EXTENSION_URL = "http://ibm.com/watsonhealth/fhir/extension";
    private static final String BASIC_RESOURCE_TYPE_URL = "http://ibm.com/watsonhealth/fhir/basic-resource-type";

    private static Conformance conformance = null;

    private PersistenceHelper persistenceHelper = null;
    private FHIRPersistence persistence = null;
    private ObjectFactory objectFactory = new ObjectFactory();
    
    private List<String> allowableVirtualResourceTypes = null;
    private Boolean virtualResourceTypesFeatureEnabled = null;
    
    private Boolean userDefinedSchematronEnabled = null;
    
    private Boolean updateCreateEnabled = null;
    
    private Parameter basicCodeSearchParameter = null;

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

    public FHIRResource() throws Exception {
        log.entering(this.getClass().getName(), "FHIRResource ctor");
        try {
            fhirConfig = FHIRConfiguration.loadConfiguration();
            virtualResourceTypesFeatureEnabled = fhirConfig.getBooleanProperty(PROPERTY_VIRTUAL_RESOURCES_ENABLED, Boolean.TRUE);
            allowableVirtualResourceTypes = fhirConfig.getStringListProperty(PROPERTY_ALLOWABLE_VIRTUAL_RESOURCE_TYPES);
            userDefinedSchematronEnabled = fhirConfig.getBooleanProperty(PROPERTY_USER_DEFINED_SCHEMATRON_ENABLED, Boolean.TRUE);
            updateCreateEnabled = fhirConfig.getBooleanProperty(PROPERTY_UPDATE_CREATE_ENABLED, Boolean.TRUE);
        } catch (Throwable t) {
            log.severe("Unexpected error during initialization: " + t);
            throw t;
        } finally {
            log.exiting(this.getClass().getName(), "FHIRResource ctor");
        }
    }

    @GET
    @Path("metadata")
    @ApiOperation(value = "Returns information about the FHIR server.", 
        notes = "Currently, the information returned is minimal. The set of information will be expanded as new features are implemented in the server.",
        response = Conformance.class)
    @ApiResponses(value = {
        @ApiResponse(code = SC_OK, message = "The 'metadata' operation was successful and the Conformance resource has been returned in the response body."),
        @ApiResponse(code = SC_INTERNAL_SERVER_ERROR, message = "An unexpected server error occurred.", response = OperationOutcome.class)
    })
    public Response metadata() throws ClassNotFoundException {
        log.entering(this.getClass().getName(), "metadata()");
        Date startTime = new Date();
        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        try {
        	status = Response.Status.OK;
            return Response.ok().entity(getConformanceStatement()).build();
        } catch(Exception e) {
        	return exceptionResponse(e, status);
        } finally {
        	RestAuditLogger.logMetadata(httpServletRequest, startTime, new Date(), status);
            if (log.isLoggable(Level.FINE)) {
                log.exiting(this.getClass().getName(), "metadata()");
            }
        }
    }

    @POST
    @Path("{type}")
    @ApiOperation(value = "Creates a new resource.", notes = "The resource should be passed in the request body.")
    @ApiResponses(value = {
        @ApiResponse(code = SC_CREATED, message = "The 'create' operation was successful.", responseHeaders = @ResponseHeader(name = "Location", description = "Contains the location URI of the newly-created resource")),
        @ApiResponse(code = SC_BAD_REQUEST, message = "The 'create' operation resulted in an error.", response = OperationOutcome.class),
        @ApiResponse(code = SC_INTERNAL_SERVER_ERROR, message = "An unexpected server error occurred.", response = OperationOutcome.class)
    })
    public Response create(
        @ApiParam(value = "The type of the resource to be created.", required = true) 
        @PathParam("type") String type, 
        @ApiParam(value = "The resource to be created.", required = true) 
        Resource resource) {
    	
    	Date startTime = new Date();
    	Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;

        log.entering(this.getClass().getName(), "create(Resource)", "this=" + FHIRUtilities.getObjectHandle(this));

        try {
        	
        	URI locationURI = doCreate(type, resource);
                       
            ResponseBuilder response = Response.created(locationURI);
            status = Response.Status.CREATED;
            response = addHeaders(response, resource);
            return response.build();
        } catch (FHIRRestException e) {
        	status = e.getHttpStatus();
            return exceptionResponse(e);
        } catch (FHIRException e) {
        	status = e.getHttpStatus();
            return exceptionResponse(e);
        } catch (Exception e) {
        	return exceptionResponse(e, status);
        } finally {
        	RestAuditLogger.logCreate(httpServletRequest, resource, startTime, new Date(), status);
            log.exiting(this.getClass().getName(), "create(Resource)", "this=" + FHIRUtilities.getObjectHandle(this));
        }
    }

    @PUT
    @Path("{type}/{id}")
    @ApiOperation(value = "Updates a resource.", 
        notes = "The 'update' operation will create a new version of the resource.")
    @ApiResponses(value = {
            @ApiResponse(code = SC_OK, message = "The 'update' operation was successful.", responseHeaders = @ResponseHeader(name = "Location", description = "Contains the location URI of the updated resource")),
            @ApiResponse(code = SC_BAD_REQUEST, message = "The 'update' operation resulted in an error.", response = OperationOutcome.class),
            @ApiResponse(code = SC_METHOD_NOT_ALLOWED, message = "The specified resource could not be updated because it does not yet exist.", response = OperationOutcome.class),
            @ApiResponse(code = SC_INTERNAL_SERVER_ERROR, message = "An unexpected server error occurred.", response = OperationOutcome.class)
    })
    public Response update(@ApiParam(value = "The type of the resource to be updated.", required = true) @PathParam("type") String type,
        @ApiParam(value = "The id of the resource to be updated.", required = true) @PathParam("id") String id,
        @ApiParam(value = "The new contents of the resource to be updated.", required = true) Resource resource) {

    	Date startTime = new Date();
    	Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
    	Resource currentResource = null;
    	
        log.entering(this.getClass().getName(), "update(String,Resource)", "this=" + FHIRUtilities.getObjectHandle(this));

        try {
        	// Make sure the resource has an 'id' attribute.
            if (resource.getId() == null) {
                throw new FHIRException("Input resource must contain an 'id' attribute.");
            }
        	
        	currentResource = doRead(type, resource.getId().getValue(), false);
            URI locationURI = doUpdate(type, id, resource, currentResource, httpHeaders.getHeaderString(HttpHeaders.IF_MATCH));

            ResponseBuilder response = null;

            // Determine whether we actually did a create or an update operation in the persistence layer.
            if (currentResource == null) {
                // Must have been a create.
                response = Response.created(locationURI);
                status = Response.Status.CREATED;
            } else {
                // Must have been an update.
                response = Response.ok().location(locationURI);
                status = Response.Status.OK;
            }
            response = addHeaders(response, resource);
            return response.build();
        } catch (FHIRRestException e) {
            status = e.getHttpStatus();
            return exceptionResponse(e);
        } catch (FHIRPersistenceResourceNotFoundException e) {
            status = Response.Status.METHOD_NOT_ALLOWED;
            return exceptionResponse(e, status);
        } catch (FHIRException e) {
            status = e.getHttpStatus();
            return exceptionResponse(e);
        } catch (Exception e) {
            return exceptionResponse(e, status);
        } finally {
            if (status == Response.Status.CREATED) {
                RestAuditLogger.logCreate(httpServletRequest, resource, startTime, new Date(), status);
            } else {
                RestAuditLogger.logUpdate(httpServletRequest, currentResource, resource, startTime, new Date(), status);
            }
            log.exiting(this.getClass().getName(), "update(String,Resource)", "this=" + FHIRUtilities.getObjectHandle(this));
        }
    }

    @GET
    @Path("{type}/{id}")
    @ApiOperation(value = "Retrieves the most recent version of a resource.", 
        notes = "For a specific version, you can use the 'vread' API.",
        response = Resource.class)
    @ApiResponses(value = {
        @ApiResponse(code = SC_OK, message = "The 'read' operation was successful and the specified resource has been returned in the response body."),
        @ApiResponse(code = SC_NOT_FOUND, message = "The requested resource was not found.", response = OperationOutcome.class),
        @ApiResponse(code = SC_BAD_REQUEST, message = "The 'read' operation resulted in an error.", response = OperationOutcome.class),
        @ApiResponse(code = SC_INTERNAL_SERVER_ERROR, message = "An unexpected server error occurred.", response = OperationOutcome.class)
    })
    public Response read(
        @ApiParam(value = "The resource type to be retrieved.", required = true)
        @PathParam("type") String type, 
        @ApiParam(value = "The id of the resource to be retrieved.", required = true)
        @PathParam("id") String id) throws ClassNotFoundException {
        log.entering(this.getClass().getName(), "read(String,String)", "this=" + FHIRUtilities.getObjectHandle(this));
        
        Date startTime = new Date();
        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
    	Resource resource = null;
    	
        try {
            resource = doRead(type, id, true);
            ResponseBuilder response = Response.ok().entity(resource);
            status = Response.Status.OK;
            response = addHeaders(response, resource);
            return response.build();
        } catch (FHIRRestException e) {
        	status = e.getHttpStatus();
            return exceptionResponse(e);
        } catch (FHIRPersistenceResourceNotFoundException e) {
        	status = Response.Status.NOT_FOUND;
            return exceptionResponse(e, status);
        } catch (FHIRException e) {
        	status = e.getHttpStatus();
            return exceptionResponse(e);
        } catch (Exception e) {
        	return exceptionResponse(e, status);
        } finally {
        	RestAuditLogger.logRead(httpServletRequest, resource, startTime, new Date(), status);
            if (log.isLoggable(Level.FINE)) {
                log.exiting(this.getClass().getName(), "read(String,String)", "this=" + FHIRUtilities.getObjectHandle(this));
            }
        }
    }

    @GET
    @Path("{type}/{id}/_history/{vid}")
    @ApiOperation(value = "Retrieves a specific version of a resource.", 
        notes = "For the latest version of a resource, you can use the 'read' API.",
        response = Resource.class)
    @ApiResponses(value = {
        @ApiResponse(code = SC_OK, message = "The 'vread' operation was successful and the specified resource has been returned in the response body."),
        @ApiResponse(code = SC_NOT_FOUND, message = "The requested resource was not found.", response = OperationOutcome.class),
        @ApiResponse(code = SC_BAD_REQUEST, message = "The 'vread' operation resulted in an error.", response = OperationOutcome.class),
        @ApiResponse(code = SC_INTERNAL_SERVER_ERROR, message = "An unexpected server error occurred.", response = OperationOutcome.class)
    })
    public Response vread(
        @ApiParam(value = "The resource type to be retrieved.", required = true)
        @PathParam("type") String type, 
        @ApiParam(value = "The id of the resource to be retrieved.", required = true)
        @PathParam("id") String id, 
        @ApiParam(value = "The version of the resource to be retrieved.", required = true)
        @PathParam("vid") String vid) {
        if (log.isLoggable(Level.FINE)) {
            log.entering(this.getClass().getName(), "vread(String,String,String)", "this=" + FHIRUtilities.getObjectHandle(this));
        }
        Date startTime = new Date();
        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
    	Resource resource = null;
    	
        try {
            resource = doVRead(type, id, vid);
            
            ResponseBuilder response = Response.ok().entity(resource);
            status = Response.Status.OK;
            response = addHeaders(response, resource);
            return response.build();
        } catch (FHIRRestException e) {
        	status = e.getHttpStatus();
            return exceptionResponse(e);
        } catch (FHIRPersistenceResourceNotFoundException e) {
        	status = Response.Status.NOT_FOUND;
            return exceptionResponse(e, status);
        } catch (FHIRException e) {
        	status = e.getHttpStatus();
            return exceptionResponse(e);
        } catch (Exception e) {
        	return exceptionResponse(e, status);
        } finally {
        	RestAuditLogger.logVersionRead(httpServletRequest, resource, startTime, new Date(), status);
            log.exiting(this.getClass().getName(), "vread(String,String,String)", "this=" + FHIRUtilities.getObjectHandle(this));
        }
    }

    @GET
    @Path("{type}/{id}/_history")
    @ApiOperation(value = "Retrieves all of the versions of the specified resource.", 
                  notes = "To retrieve the most recent version, use the 'read' API.  To retrieve a specific version, use the 'vread' API",
                  response = Bundle.class)
    @ApiResponses(value = {
        @ApiResponse(code = SC_OK, message = "The '_history' operation was successful and the specified resources have been returned in the response body."),
        @ApiResponse(code = SC_BAD_REQUEST, message = "The '_history' operation resulted in an error.", response = OperationOutcome.class),
        @ApiResponse(code = SC_INTERNAL_SERVER_ERROR, message = "An unexpected server error occurred.", response = OperationOutcome.class)
    })
    public Response history(
        @ApiParam(value = "The resource type to be retrieved.", required = true)
        @PathParam("type") String type, 
        @ApiParam(value = "The id of the resource to be retrieved.", required = true)
        @PathParam("id") String id) {
        log.entering(this.getClass().getName(), "history(String,String)", "this=" + FHIRUtilities.getObjectHandle(this));
        
        Date startTime = new Date();
        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
    	Bundle bundle= null;
    	
        try {
            bundle = doHistory(type, id, uriInfo.getQueryParameters(), uriInfo.getRequestUri().toString());
            status = Response.Status.OK;
            return Response.ok(bundle).build();
        } catch (FHIRRestException e) {
        	status = e.getHttpStatus();
            return exceptionResponse(e);
        } catch (FHIRException e) {
        	status = e.getHttpStatus();
            return exceptionResponse(e);
        } catch (Exception e) {
        	return exceptionResponse(e, status);
        } finally {
        	RestAuditLogger.logHistory(httpServletRequest, bundle, startTime, new Date(), status);
            log.exiting(this.getClass().getName(), "history(String,String)", "this=" + FHIRUtilities.getObjectHandle(this));
        }
    }

    @GET
    @Path("{type}")
    @ApiOperation(value = "Performs a search to retrieve resources of the specified type.", 
        notes = "Search criteria are specified by using the query string or form parameters.",
        response = Bundle.class)
    @ApiResponses(value = {
        @ApiResponse(code = SC_OK, message = "The 'search' operation was successful and the search results have been returned in the response body."),
        @ApiResponse(code = SC_BAD_REQUEST, message = "The 'search' operation resulted in an error.", response = OperationOutcome.class),
        @ApiResponse(code = SC_INTERNAL_SERVER_ERROR, message = "An unexpected server error occurred.", response = OperationOutcome.class)
    })
    public Response search(
        @ApiParam(value = "The resource type which is the target of the 'search' operation.", required = true)
        @PathParam("type") String type) {
        log.entering(this.getClass().getName(), "search(String)", "this=" + FHIRUtilities.getObjectHandle(this));
        Date startTime = new Date();
        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
    	MultivaluedMap<String, String> queryParameters = null;
    	Bundle bundle = null;
    	
        try {
            queryParameters = uriInfo.getQueryParameters();
            bundle = doSearch(type, null, null, queryParameters, uriInfo.getRequestUri().toString());
            status = Response.Status.OK;
            return Response.ok(bundle).build();
        } catch (FHIRRestException e) {
        	status = e.getHttpStatus();
            return exceptionResponse(e);
        } catch (FHIRException e) {
        	status = e.getHttpStatus();
            return exceptionResponse(e);
        } catch (Exception e) {
        	return exceptionResponse(e, status);
        } finally {
        	RestAuditLogger.logSearch(httpServletRequest, queryParameters, bundle, startTime, new Date(), status);
            log.exiting(this.getClass().getName(), "search(String)", "this=" + FHIRUtilities.getObjectHandle(this));
        }
    }
    
    @GET
    @Path("{compartment}/{compartmentId}/{type}")
    @ApiOperation(value = "Performs a compartment based search to retrieve resources of the specified compartment and type.", 
        notes = "Non-compartment search parameters are specified by using the query string or form parameters.",
        response = Bundle.class)
    @ApiResponses(value = {
        @ApiResponse(code = SC_OK, message = "The 'search' operation was successful and the search results have been returned in the response body."),
        @ApiResponse(code = SC_BAD_REQUEST, message = "The 'search' operation resulted in an error.", response = OperationOutcome.class),
        @ApiResponse(code = SC_INTERNAL_SERVER_ERROR, message = "An unexpected server error occurred.", response = OperationOutcome.class)
    })
    public Response searchCompartment(
    	@ApiParam(value = "The compartment name that contains the target resource of the 'search' operation.", required = true)
    	@PathParam("compartment") String compartment,
    	@ApiParam(value = "The id of the compartment that contains the target resource of the 'search' operation.", required = true)
    	@PathParam("compartmentId") String compartmentId,
        @ApiParam(value = "The resource type which is the target of the 'search' operation.", required = true)
        @PathParam("type") String type) {
    	
        log.entering(this.getClass().getName(), "search(String, String, String)", "this=" + FHIRUtilities.getObjectHandle(this));
        Date startTime = new Date();
        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
    	MultivaluedMap<String, String> queryParameters = null;
    	Bundle bundle = null;
    	
        try {
            queryParameters = uriInfo.getQueryParameters();
            //bundle = doSearch(type, queryParameters, uriInfo.getRequestUri().toString());
            bundle = doSearch(type, compartment, compartmentId, queryParameters, uriInfo.getRequestUri().toString());
            status = Response.Status.OK;
            return Response.ok(bundle).build();
        } catch (FHIRRestException e) {
        	status = e.getHttpStatus();
            return exceptionResponse(e);
        } catch (FHIRException e) {
        	status = e.getHttpStatus();
            return exceptionResponse(e);
        } catch (Exception e) {
        	return exceptionResponse(e, status);
        } finally {
        	RestAuditLogger.logSearch(httpServletRequest, queryParameters, bundle, startTime, new Date(), status);
            log.exiting(this.getClass().getName(), "search(String)", "this=" + FHIRUtilities.getObjectHandle(this));
        }
    }
    
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Path("{type}/_search")
    public Response _search(@PathParam("type") String type) {
        log.entering(this.getClass().getName(), "_search(String)", "this=" + FHIRUtilities.getObjectHandle(this));
        Date startTime = new Date();
        Response.Status  status = Response.Status.INTERNAL_SERVER_ERROR;
        MultivaluedMap<String, String> queryParameters = null;
        Bundle bundle = null;
        
        try {
            queryParameters = uriInfo.getQueryParameters();
            bundle = doSearch(type, null, null, queryParameters, uriInfo.getRequestUri().toString());
            status = Response.Status.OK;
            return Response.ok(bundle).build();
        } catch (FHIRRestException e) {
            status = e.getHttpStatus();
            return exceptionResponse(e);
        } catch (FHIRException e) {
            status = e.getHttpStatus();
            return exceptionResponse(e);
        } catch (Exception e) {
            return exceptionResponse(e, status);
        } finally {
            RestAuditLogger.logSearch(httpServletRequest, queryParameters, bundle, startTime, new Date(), status);
            log.exiting(this.getClass().getName(), "_search(String)", "this=" + FHIRUtilities.getObjectHandle(this));
        }
    }
    
    @GET
    @Path("_search")
    public Response searchAll() {
        log.entering(this.getClass().getName(), "searchAll()", "this=" + FHIRUtilities.getObjectHandle(this));
        Date startTime = new Date();
        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        MultivaluedMap<String, String> queryParameters = null;
        Bundle bundle = null;
        
        try {
            queryParameters = uriInfo.getQueryParameters();
            bundle = doSearch("Resource", null, null, queryParameters, uriInfo.getRequestUri().toString());
            status = Response.Status.OK;
            return Response.ok(bundle).build();
        } catch (FHIRRestException e) {
            status = e.getHttpStatus();
            return exceptionResponse(e);
        } catch (FHIRException e) {
            status = e.getHttpStatus();
            return exceptionResponse(e);
        } catch (Exception e) {
            return exceptionResponse(e, status);
        } finally {
            RestAuditLogger.logSearch(httpServletRequest, queryParameters, bundle, startTime, new Date(), status);
            log.exiting(this.getClass().getName(), "searchAll()", "this=" + FHIRUtilities.getObjectHandle(this));
        }
    }
    
    @GET
    @Path("${operationName}")
    public Response invoke(@PathParam("operationName") String operationName) {
        log.entering(this.getClass().getName(), "invoke(String)", "this=" + FHIRUtilities.getObjectHandle(this));
        try {
            Resource result = doInvoke(FHIROperation.Context.SYSTEM, null, null, null, operationName, null);
            return Response.ok().entity(result).build();
        } catch (FHIROperationException e) {
            return exceptionResponse(e);
        } catch (FHIRException e) {
            return exceptionResponse(e);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "invoke(String)", "this=" + FHIRUtilities.getObjectHandle(this));
        }
    }

    @POST
    @Path("${operationName}")
    public Response invoke(@PathParam("operationName") String operationName, Resource resource) {
        log.entering(this.getClass().getName(), "invoke(String,Resource)", "this=" + FHIRUtilities.getObjectHandle(this));
        try {
            Resource result = doInvoke(FHIROperation.Context.SYSTEM, null, null, null, operationName, resource);
            return Response.ok().entity(result).build();
        } catch (FHIROperationException e) {
            return exceptionResponse(e);
        } catch (FHIRException e) {
            return exceptionResponse(e);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "invoke(String,Resource)", "this=" + FHIRUtilities.getObjectHandle(this));
        }
    }
    
    @GET
    @Path("{resourceTypeName}/${operationName}")
    public Response invoke(@PathParam("resourceTypeName") String resourceTypeName, @PathParam("operationName") String operationName) {
        log.entering(this.getClass().getName(), "invoke(String,String)", "this=" + FHIRUtilities.getObjectHandle(this));
        try {
            Resource result = doInvoke(FHIROperation.Context.RESOURCE_TYPE, resourceTypeName, null, null, operationName, null); 
            return Response.ok().entity(result).build();
        } catch (FHIROperationException e) {
            return exceptionResponse(e);
        } catch (FHIRException e) {
            return exceptionResponse(e);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "invoke(String,String)", "this=" + FHIRUtilities.getObjectHandle(this));
        }
    }

    @POST
    @Path("{resourceTypeName}/${operationName}")
    public Response invoke(@PathParam("resourceTypeName") String resourceTypeName, @PathParam("operationName") String operationName, Resource resource) {
        log.entering(this.getClass().getName(), "invoke(String,String,Resource)", "this=" + FHIRUtilities.getObjectHandle(this));
        try {
            Resource result = doInvoke(FHIROperation.Context.RESOURCE_TYPE, resourceTypeName, null, null, operationName, resource);
            return Response.ok().entity(result).build();
        } catch (FHIROperationException e) {
            return exceptionResponse(e);
        } catch (FHIRException e) {
            return exceptionResponse(e);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "invoke(String,String,Resource)", "this=" + FHIRUtilities.getObjectHandle(this));
        }
    }
    
    @GET
    @Path("{resourceTypeName}/{logicalId}/${operationName}")
    public Response invoke(@PathParam("resourceTypeName") String resourceTypeName, @PathParam("logicalId") String logicalId, @PathParam("operationName") String operationName) {
        log.entering(this.getClass().getName(), "invoke(String,String,String)", "this=" + FHIRUtilities.getObjectHandle(this));
        try {
            Resource result = doInvoke(FHIROperation.Context.INSTANCE, resourceTypeName, logicalId, null, operationName, null);
            return Response.ok().entity(result).build();
        } catch (FHIROperationException e) {
            return exceptionResponse(e);
        } catch (FHIRException e) {
            return exceptionResponse(e);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "invoke(String,String,String)", "this=" + FHIRUtilities.getObjectHandle(this));
        }
    }

    @POST
    @Path("{resourceTypeName}/{logicalId}/${operationName}")
    public Response invoke(@PathParam("resourceTypeName") String resourceTypeName, @PathParam("logicalId") String logicalId, @PathParam("operationName") String operationName, Resource resource) {
        log.entering(this.getClass().getName(), "invoke(String,String,String,Resource)", "this=" + FHIRUtilities.getObjectHandle(this));
        try {
            Resource result = doInvoke(FHIROperation.Context.INSTANCE, resourceTypeName, logicalId, null, operationName, resource);
            return Response.ok().entity(result).build();
        } catch (FHIROperationException e) {
            return exceptionResponse(e);
        } catch (FHIRException e) {
            return exceptionResponse(e);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "invoke(String,String,String,Resource)", "this=" + FHIRUtilities.getObjectHandle(this));
        }
    }
    
    @GET
    @Path("{resourceTypeName}/{logicalId}/_history/{versionId}/${operationName}")
    public Response invoke(@PathParam("resourceTypeName") String resourceTypeName, @PathParam("logicalId") String logicalId, @PathParam("versionId") String versionId, @PathParam("operationName") String operationName) {
        log.entering(this.getClass().getName(), "invoke(String,String,String,String)", "this=" + FHIRUtilities.getObjectHandle(this));
        try {
            Resource result = doInvoke(FHIROperation.Context.INSTANCE, resourceTypeName, logicalId, versionId, operationName, null);
            return Response.ok().entity(result).build();
        } catch (FHIROperationException e) {
            return exceptionResponse(e);
        } catch (FHIRException e) {
            return exceptionResponse(e);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "invoke(String,String,String,String)", "this=" + FHIRUtilities.getObjectHandle(this));
        }
    }

    @POST
    @Path("{resourceTypeName}/{logicalId}/_history/{versionId}/${operationName}")
    public Response invoke(@PathParam("resourceTypeName") String resourceTypeName, @PathParam("logicalId") String logicalId, @PathParam("versionId") String versionId, @PathParam("operationName") String operationName, Resource resource) {
        log.entering(this.getClass().getName(), "invoke(String,String,String,String,Resource)", "this=" + FHIRUtilities.getObjectHandle(this));
        try {
            Resource result = doInvoke(FHIROperation.Context.INSTANCE, resourceTypeName, logicalId, versionId, operationName, resource);
            return Response.ok().entity(result).build();
        } catch (FHIROperationException e) {
            return exceptionResponse(e);
        } catch (FHIRException e) {
            return exceptionResponse(e);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "invoke(String,String,String,String,Resource)", "this=" + FHIRUtilities.getObjectHandle(this));
        }
    }
    /*
    @POST
    @Path("Resource/$validate")
    public Response validate(Resource resource) {
        log.entering(this.getClass().getName(), "validate(Resource)", "this=" + FHIRUtilities.getObjectHandle(this));
        Date startTime = new Date();
    	Response.Status status = null;
    	 
        try {
        	status = Response.Status.OK;
            return Response.ok().entity(doValidate(resource)).build();
        } catch (FHIRRestException e) {
        	status = e.getHttpStatus();
            return exceptionResponse(e);
        } catch (FHIRException e) {
        	status = e.getHttpStatus();
            return exceptionResponse(e);
        } catch (Exception e) {
        	status = Response.Status.INTERNAL_SERVER_ERROR;
            return exceptionResponse(e, status);
        } finally {
        	RestAuditLogger.logValidate(httpServletRequest, resource, startTime, new Date(), status);
            log.exiting(this.getClass().getName(), "validate(Resource)", "this=" + FHIRUtilities.getObjectHandle(this));
        }
    }
    */
    @POST
    @ApiOperation(value = "Performs a collection of operations as either a batch or transaction interaction.", 
    notes = "A Bundle resource containing the operations should be passed in the request body.")
    public Response bundle(
        @ApiParam(value = "The Bundle resource which contains the collection of operations to be performed.", required = true) 
        Bundle bundle) {

        log.entering(this.getClass().getName(), "bundle(Bundle)", "this=" + FHIRUtilities.getObjectHandle(this));
        Date startTime = new Date();
    	Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
    	Bundle responseBundle = null;

        try {
            responseBundle = doBundle(bundle);
                
            ResponseBuilder response = Response.ok(responseBundle);
            status = Response.Status.OK;
            return response.build();
        } catch (FHIRRestBundledRequestException e) {
        	status = e.getHttpStatus();
            return exceptionResponse(e);
        } catch (FHIRRestException e) {
        	status = e.getHttpStatus();
            return exceptionResponse(e);
        } catch (FHIRException e) {
        	status = e.getHttpStatus();
            return exceptionResponse(e);
        } catch (Exception e) {
        	log.log(Level.SEVERE, "Error encountered during bundle request processing: ", e);
            return exceptionResponse(e, status);
        } finally {
        	RestAuditLogger.logBundle(httpServletRequest, bundle, startTime, new Date(), status);
            log.exiting(this.getClass().getName(), "bundle(Bundle)", "this=" + FHIRUtilities.getObjectHandle(this));
        }
    }

    /**
     * Performs the heavy lifting associated with a 'create' interaction.
     * @param resource the Resource to be stored.
     * @return the location URI associated with the new Resource
     * @throws Exception
     */
    protected URI doCreate(String type, Resource resource) throws Exception {
        log.entering(this.getClass().getName(), "doCreate");
        try {
            // Make sure the expected type (specified in the URL string) is congruent with the actual type 
            // of the resource.
            String resourceType = FHIRUtil.getResourceTypeName(resource);
            if (!resourceType.equals(type)) {
                throw new FHIRException("Resource type '" + resourceType + "' does not match type specified in request URI: " + type);
            }
            
            // A new resource should not contain an ID.
            if (resource.getId() != null) {
                throw new FHIRException("A 'create' operation cannot be performed on a resource that contains an 'id' attribute.");
            }

            // Validate the input resource and return any validation errors.
            List<OperationOutcomeIssue> issues = FHIRValidator.getInstance().validate(resource, isUserDefinedSchematronEnabled());
            if (!issues.isEmpty()) {
                OperationOutcome operationOutcome = FHIRUtil.buildOperationOutcome(issues);
                throw new FHIRRestException(null, operationOutcome, Response.Status.BAD_REQUEST);
            }

            // If there were no validation errors, then create the resource and return the location header.

            // First, invoke the 'beforeCreate' interceptor methods.
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(resource, buildPersistenceEventProperties(type, null, null));
            getInterceptorMgr().fireBeforeCreateEvent(event);

            FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(event);
            getPersistenceImpl().create(persistenceContext, resource);

            // Build our location URI and add it to the interceptor event structure since it is now known.
            URI locationURI = FHIRUtil.buildLocationURI(FHIRUtil.getResourceTypeName(resource), resource);
            event.getProperties().put(FHIRPersistenceEvent.PROPNAME_RESOURCE_LOCATION_URI, locationURI.toString());

            // Invoke the 'afterCreate' interceptor methods.
            getInterceptorMgr().fireAfterCreateEvent(event);

            return locationURI;
        } finally {
            log.exiting(this.getClass().getName(), "doCreate");
        }
    }
    
    /**
     * Performs an update operation (a new version of the Resource will be stored).
     * @param resource the Resource being updated
     * @param id the id of the Resource being updated
     * @return the location URI associated with the new version of the Resource
     * @throws Exception
     */
    protected URI doUpdate(String type, String id, Resource resource, Resource currentResource, String ifMatchValue) throws Exception {
        log.entering(this.getClass().getName(), "doUpdate");
        try {
            // Make sure the type specified in the URL string matches the resource type obtained from the resource.
            String resourceType = FHIRUtil.getResourceTypeName(resource);
            if (!resourceType.equals(type)) {
                throw new FHIRException("Resource type '" + resourceType + "' does not match type specified in request URI: " + type);
            }
            
            // Validate the input resource and return any validation errors.
            List<OperationOutcomeIssue> issues = FHIRValidator.getInstance().validate(resource, isUserDefinedSchematronEnabled());
            if (!issues.isEmpty()) {
                OperationOutcome operationOutcome = FHIRUtil.buildOperationOutcome(issues);
                throw new FHIRRestException(null, operationOutcome, Response.Status.BAD_REQUEST);
            }

            // Make sure the resource has an 'id' attribute.
            if (resource.getId() == null) {
                throw new FHIRException("Input resource must contain an 'id' attribute.");
            }

            // If an id value was passed in (i.e. the id specified in the REST API URL string),
            // then make sure it's the same as the value in the resource.
            if (id != null & !resource.getId().getValue().equals(id)) {
                throw new FHIRException("Input resource 'id' attribute must match 'id' parameter.");
            }
            
            // Perform the "version-aware" update check.
            if (currentResource != null) {
                performVersionAwareUpdateCheck(currentResource, ifMatchValue);
            }

            // First, invoke the 'beforeUpdate' interceptor methods.
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(resource, buildPersistenceEventProperties(type, resource.getId().getValue(), null));
            getInterceptorMgr().fireBeforeUpdateEvent(event);

            FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(event);
            getPersistenceImpl().update(persistenceContext, id, resource);

            // Build our location URI and add it to the interceptor event structure since it is now known.
            URI locationURI = FHIRUtil.buildLocationURI(FHIRUtil.getResourceTypeName(resource), resource);
            event.getProperties().put(FHIRPersistenceEvent.PROPNAME_RESOURCE_LOCATION_URI, locationURI.toString());

            // Invoke the 'afterUpdate' interceptor methods.
            getInterceptorMgr().fireAfterUpdateEvent(event);

            return locationURI;
        } finally {
            log.exiting(this.getClass().getName(), "doUpdate");
        }
    }


    /**
     * Performs a 'read' operation to retrieve a Resource.
     * @param type the resource type associated with the Resource to be retrieved
     * @param id the id of the Resource to be retrieved
     * @return the Resource
     * @throws Exception
     */
    protected Resource doRead(String type, String id, boolean throwExcOnNull) throws Exception {
        log.entering(this.getClass().getName(), "doRead");
        try {
            String resourceTypeName = type;
            if (!FHIRUtil.isStandardResourceType(type)) {
                if (!isVirtualResourceTypesFeatureEnabled()) {
                    throw new FHIRVirtualResourceTypeException("The virtual resource types feature is not enabled for this server");
                }
                if (!isAllowableVirtualResourceType(type)) {
                    throw new FHIRVirtualResourceTypeException("The virtual resource type '" + type
                            + "' is not allowed. Allowable virtual types for this server are: " + getAllowableVirtualResourceTypes().toString());
                }
                resourceTypeName = "Basic";
            }
            
            Class<? extends Resource> resourceType = getResourceType(resourceTypeName);
            
            // First, invoke the 'beforeRead' interceptor methods.
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(null, buildPersistenceEventProperties(type, id, null));
            getInterceptorMgr().fireBeforeReadEvent(event);
            
            FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(event);
            Resource resource = getPersistenceImpl().read(persistenceContext, resourceType, id);
            if (resource == null && throwExcOnNull) {
                throw new FHIRPersistenceResourceNotFoundException("Resource '" + type + "/" + id + "' not found.");
            }
            
            event.setFhirResource(resource);

            // Invoke the 'afterRead' interceptor methods.
            getInterceptorMgr().fireAfterReadEvent(event);

            return resource;
        } finally {
            log.exiting(this.getClass().getName(), "doRead");
        }
    }

    /**
     * Performs a 'vread' operation by retrieving the specified version of a Resource.
     * @param type the resource type associated with the Resource to be retrieved
     * @param id the id of the Resource to be retrieved
     * @param versionId the version id of the Resource to be retrieved
     * @return the Resource
     * @throws Exception
     */
    protected Resource doVRead(String type, String id, String versionId) throws Exception {
        log.entering(this.getClass().getName(), "doVRead");
        try {
            String resourceTypeName = type;
            if (!FHIRUtil.isStandardResourceType(type)) {
                if (!isVirtualResourceTypesFeatureEnabled()) {
                    throw new FHIRVirtualResourceTypeException("The virtual resource types feature is not enabled for this server");
                }
                if (!isAllowableVirtualResourceType(type)) {
                    throw new FHIRVirtualResourceTypeException("The virtual resource type '" + type
                            + "' is not allowed. Allowable virtual resource types for this server are: " + getAllowableVirtualResourceTypes().toString());
                }
                resourceTypeName = "Basic";
            }
            
            Class<? extends Resource> resourceType = getResourceType(resourceTypeName);
            
            // First, invoke the 'beforeVread' interceptor methods.
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(null, buildPersistenceEventProperties(type, id, versionId));
            getInterceptorMgr().fireBeforeVreadEvent(event);
            
            FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(event);
            Resource resource = getPersistenceImpl().vread(persistenceContext, resourceType, id, versionId);
            if (resource == null) {
                throw new FHIRPersistenceResourceNotFoundException("Resource '" + resourceType.getSimpleName() + "/" + id + "' version " + versionId + " not found.");
            }
            
            event.setFhirResource(resource);

            // Invoke the 'afterVread' interceptor methods.
            getInterceptorMgr().fireAfterVreadEvent(event);

            return resource;
        } finally {
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
     * @return a Bundle containing the history of the specified Resource
     * @throws Exception
     */
    protected Bundle doHistory(String type, String id, MultivaluedMap<String, String> queryParameters, String requestUri) throws Exception {
        log.entering(this.getClass().getName(), "doHistory");
        try {
            String resourceTypeName = type;
            if (!FHIRUtil.isStandardResourceType(type)) {
                if (!isVirtualResourceTypesFeatureEnabled()) {
                    throw new FHIRVirtualResourceTypeException("The virtual resource types feature is not enabled for this server");
                }
                if (!isAllowableVirtualResourceType(type)) {
                    throw new FHIRVirtualResourceTypeException("The virtual resource type '" + type
                            + "' is not allowed. Allowable virtual resource types for this server are: " + getAllowableVirtualResourceTypes().toString());
                }
                resourceTypeName = "Basic";
            }

            Class<? extends Resource> resourceType = getResourceType(resourceTypeName);
            FHIRHistoryContext historyContext = FHIRPersistenceUtil.parseHistoryParameters(queryParameters);
            
            // First, invoke the 'beforeHistory' interceptor methods.
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(null, buildPersistenceEventProperties(type, id, null));
            getInterceptorMgr().fireBeforeHistoryEvent(event);

            FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(event, historyContext);
            List<Resource> resources = getPersistenceImpl().history(persistenceContext, resourceType, id);
            Bundle bundle = createBundle(resources, BundleTypeList.HISTORY, historyContext.getTotalCount());
            addLinks(historyContext, bundle, requestUri);
            
            event.setFhirResource(bundle);

            // Invoke the 'afterHistory' interceptor methods.
            getInterceptorMgr().fireAfterHistoryEvent(event);

            return bundle;
        } finally {
            log.exiting(this.getClass().getName(), "doHistory");
        }
    }
    
    /**
     * Performs heavy lifting associated with a 'search' operation.
     * @param type the resource type associated with the search
     * @param queryParameters a Map containing the query parameters from the request URL
     * @return a Bundle containing the search result set
     * @throws Exception
     */
    protected Bundle doSearch(String type, String compartment, String compartmentId, MultivaluedMap<String, String> queryParameters, String requestUri) throws Exception {
        log.entering(this.getClass().getName(), "doSearch");
        try {
            String resourceTypeName = type;
            Parameter implicitSearchParameter = null;
            if (!FHIRUtil.isStandardResourceType(type)) {
                if (!isVirtualResourceTypesFeatureEnabled()) {
                    throw new FHIRVirtualResourceTypeException("The virtual resource types feature is not enabled for this server");
                }
                if (!isAllowableVirtualResourceType(type)) {
                    throw new FHIRVirtualResourceTypeException("The virtual resource type '" + type + "' is not allowed. Allowable virtual resource types for this server are: " + getAllowableVirtualResourceTypes().toString());
                }
                resourceTypeName = "Basic";
                implicitSearchParameter = getBasicCodeSearchParameter();
            }
            
            Class<? extends Resource> resourceType = getResourceType(resourceTypeName);
            
            // First, invoke the 'beforeSearch' interceptor methods.
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(null, buildPersistenceEventProperties(type, null, null));
            getInterceptorMgr().fireBeforeSearchEvent(event);
            
            FHIRSearchContext searchContext = SearchUtil.parseQueryParameters(compartment, compartmentId, resourceType, queryParameters, httpServletRequest.getQueryString());
            List<Parameter> searchParameters = searchContext.getSearchParameters();
            if (implicitSearchParameter != null) {
                searchParameters.add(implicitSearchParameter);
            }
            
            FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(event, searchContext);
            List<Resource> resources = getPersistenceImpl().search(persistenceContext, resourceType);
            Bundle bundle = createBundle(resources, BundleTypeList.SEARCHSET, searchContext.getTotalCount());
            addLinks(searchContext, bundle, requestUri);
            
            event.setFhirResource(bundle);

            // Invoke the 'afterSearch' interceptor methods.
            getInterceptorMgr().fireAfterSearchEvent(event);
            
            return bundle;
        } finally {
            log.exiting(this.getClass().getName(), "doSearch");
        }
    }
    
    protected Resource doInvoke(FHIROperation.Context context, String resourceTypeName, String logicalId, String versionId, String operationName, Resource resource) throws Exception {
        Class<? extends Resource> resourceType = null;
        if (resourceTypeName != null) {
            resourceType = getResourceType(resourceTypeName);
        }
        
        FHIROperation operation = FHIROperationRegistry.getInstance().getOperation(operationName);
        Parameters parameters = null;
        if (resource instanceof Parameters) {
            parameters = (Parameters) resource;
        } else {
            if (resource == null) {
                // build parameters object from query parameters
                parameters = FHIROperationUtil.getInputParameters(operation.getDefinition(), uriInfo.getQueryParameters());
            } else {
                // wrap resource in a parameters object
                parameters = FHIROperationUtil.getInputParameters(operation.getDefinition(), resource);
            }
        }
        
        Parameters result = operation.invoke(context, resourceType, logicalId, versionId, parameters, getPersistenceImpl());
        
        // if single resource output parameter, return the resource
        if (FHIROperationUtil.hasSingleResourceOutputParameter(result)) {
            return FHIROperationUtil.getSingleResourceOutputParameter(result);
        }
        
        return result;
    }

    /**
     * Performs a validation of the specified Resource
     * @param resource the Resource to be validated
     * @return an OperationOutcome with the validation results
     * @throws Exception
     */
    protected OperationOutcome doValidate(Resource resource) throws Exception {
        log.entering(this.getClass().getName(), "doValidate");
        try {
            List<OperationOutcomeIssue> issues = FHIRValidator.getInstance().validate(resource, isUserDefinedSchematronEnabled());
            if (!issues.isEmpty()) {
                OperationOutcome operationOutcome = FHIRUtil.buildOperationOutcome(issues);
                throw new FHIRRestException(null, operationOutcome, Response.Status.BAD_REQUEST);
            }
            return buildResourceValidOperationOutcome();
        } finally {
            log.exiting(this.getClass().getName(), "doValidate");
        }
    }

    /**
     * Processes a bundled request.
     * 
     * @param bundle
     *            the request Bundle
     * @return the response Bundle
     */
    protected Bundle doBundle(Bundle bundle) throws Exception {
        log.entering(this.getClass().getName(), "doBundle");
        try {
            // First, validate the bundle and create the response bundle.
            Bundle responseBundle = validateBundle(bundle);
            
            // Next, process each of the entries in the bundle.
            processBundleEntries(bundle, responseBundle);
            
            return responseBundle;
        } finally {
            log.exiting(this.getClass().getName(), "doBundle");
        }
    }
    
    /**
     * This function will perform the version-aware update check by making sure that
     * the If-Match request header value (if present) specifies a version # equal to
     * the current latest version of the resource.
     * If the check fails, then a FHIRRestException will be thrown.
     * If the check succeeds then nothing occurs and processing continues.
     * 
     * @param currentResource the current latest version of the resource
     */
    private void performVersionAwareUpdateCheck(Resource currentResource, String ifMatchValue) throws FHIRRestException {
        if (ifMatchValue != null) {
        	log.fine("Performing a version aware update. ETag value =  " + ifMatchValue);
        	
            String ifMatchVersion = getVersionIdFromETagValue(ifMatchValue);
            
            // Make sure that we got a version # from the request header.
            // If not, then return a 400 Bad Request status code.
            if (ifMatchVersion == null || ifMatchVersion.isEmpty()) {
            	throw new FHIRRestException("Invalid ETag value specified in request: " + ifMatchValue, null, Status.BAD_REQUEST);
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
                throw new FHIRRestException("If-Match version '" + ifMatchVersion + "' does not match current latest version of resource: " + currentVersion, null, Status.CONFLICT);
            }
        }
    }

    /**
     * Retrieves the version id value from an ETag header value.
     * The ETag header value will be of the form: W/"<version-id>".
     * @param ifMatchValue the value of the If-Match request header.
     */
    private String getVersionIdFromETagValue(String ifMatchValue) {
        String result = null;
        if (ifMatchValue != null) {
            if (ifMatchValue.startsWith("W/")) {
                String s = ifMatchValue.substring(2);
                // If the part after "W/" starts and ends with a ",
                // then extract the part between the " characters and we're done.
                if (s.charAt(0) == '\"' && s.charAt(s.length()-1) == '\"') {
                    result = s.substring(1, s.length() - 1);
                }
            }
        }
        return result;
    }
   
    /**
     * This function will process each request contained in the specified request bundle,
     * and update the response bundle with the appropriate response information.
     * @param requestBundle the bundle containing the requests
     * @param responseBundle the bundle containing the responses
     */
    private void processBundleEntries(Bundle requestBundle, Bundle responseBundle) throws Exception {
        log.entering(this.getClass().getName(), "processBundleEntries");
        
        FHIRPersistenceTransaction txn = null;
        
        try {
            // If we're working on a 'transaction' type interaction, then start a new transaction now.
            if (responseBundle.getType().getValue() == BundleTypeList.TRANSACTION_RESPONSE) {
                txn = getPersistenceImpl().getTransaction();
                txn.begin();
                log.fine("Started new transaction for bundled 'transaction' request.");
            }
            
            // Next, process entries in the correct order.
            processEntriesForMethod(requestBundle, responseBundle, HTTPVerbList.POST, txn != null);
            processEntriesForMethod(requestBundle, responseBundle, HTTPVerbList.PUT, txn != null);
            processEntriesForMethod(requestBundle, responseBundle, HTTPVerbList.GET, txn != null);
            
            if (txn != null) {
                log.fine("Committing transaction for bundled request.");
                txn.commit();
                txn = null;
            }
            
        } finally {
            if (txn != null) {
                log.fine("Rolling back transaction for bundled request.");
                txn.rollback();
            }
            log.exiting(this.getClass().getName(), "processBundleEntries");
        }
    }

    /**
     * Processes request entries in the specified request bundle whose method matches 'httpMethod'.
     * @param requestBundle the bundle containing the request entries
     * @param responseBundle the bundle containing the corresponding response entries
     * @param httpMethod the HTTP method (GET, POST, PUT, etc.) to be processed
     */
    private void processEntriesForMethod(Bundle requestBundle, Bundle responseBundle, 
        HTTPVerbList httpMethod, boolean failFast) throws Exception {
        log.entering(this.getClass().getName(), "processEntriesForMethod");
        try {
            for (int i = 0; i < requestBundle.getEntry().size(); i++) {
                BundleEntry requestEntry = requestBundle.getEntry().get(i);
                BundleRequest request = requestEntry.getRequest();
                BundleEntry responseEntry = responseBundle.getEntry().get(i);
                BundleResponse response = responseEntry.getResponse();
                
                // During the request bundle validation step, if we detected an error
                // with a particular request entry, then we would have set the status of
                // the corresponding response entry to an appropriate value.
                // So here, we'll only process request entries whose corresponding response entry
                // contains a null status value and whose http method matches 'httpMethod'.
                if (response.getStatus() == null && request.getMethod().getValue().equals(httpMethod)) {
                    try {
                        // Parse the request URL string to determine the path and query strings.
                        if (request.getUrl() == null || request.getUrl().getValue() == null || request.getUrl().getValue().isEmpty()) {
                            throw new FHIRException("BundleEntry.request is missing the 'url' field.");
                        }
                        FHIRUrlParser requestURL = new FHIRUrlParser(request.getUrl().getValue());
                        
                        String path = requestURL.getPath();
                        String query = requestURL.getQuery();
                        log.finer("Processing bundle request; method=" + request.getMethod().getValue().value()
                            + ", url=" + request.getUrl().getValue());
                        log.finer("--> path: " + path);
                        log.finer("--> query: " + query);
                        String[] pathTokens = requestURL.getPathTokens();
                        MultivaluedMap<String, String> queryParams = requestURL.getQueryParameters();
                        
                        // Construct the absolute requestUri to be used for any response bundles associated 
                        // with history and search requests.
                        String absoluteUri = getAbsoluteUri(uriInfo.getRequestUri().toString(), request.getUrl().getValue());
                        
                        switch (request.getMethod().getValue()) {
                        case GET:
                        {
                            Resource resource = null;
                            int httpStatus = SC_OK;
                            
                            // Process a GET (read, vread, history, search, etc.).
                            // Determine the type of request from the path tokens.
                            if (pathTokens.length == 1) {
                                // This is a 'search' request.
                                resource = doSearch(pathTokens[0], null, null, queryParams, absoluteUri);
                            }
                            else if (pathTokens.length == 2) {
                                // This is a 'read' request.
                                resource = doRead(pathTokens[0], pathTokens[1], true);
                            } 
                            else if (pathTokens.length == 3) {
                            	if (pathTokens[2].equals("_history")) {
                            		// This is a 'history' request.
                                    resource = doHistory(pathTokens[0], pathTokens[1], queryParams, absoluteUri);
                            	}
                            	else {
                            		// This is a compartment based search
                            		resource = doSearch(pathTokens[2], pathTokens[0], pathTokens[1], queryParams, absoluteUri);
                            	}
                            }
                            else if (pathTokens.length == 4 && pathTokens[2].equals("_history")) {
                                // This is a 'vread' request.
                                resource = doVRead(pathTokens[0], pathTokens[1], pathTokens[3]);
                                setBundleEntryResource(responseEntry, resource);
                            }
                            else {
                                throw new FHIRException("Unrecognized path in request URL: " + path);
                            }
                            
                            // Save the results of the operation in the bundle response field.
                            setBundleResponseStatus(response, httpStatus);
                            setBundleEntryResource(responseEntry, resource);
                        }
                        break;
                            
                        case POST:
                        {
                            // Process a POST (create).
                            if (pathTokens.length != 1) {
                                throw new FHIRException("Request URL for bundled POST request should have path part with exactly one token (<resourceType>).");
                            }
                            Resource resource = FHIRUtil.getResourceContainerResource(requestEntry.getResource());
                            URI locationURI = doCreate(pathTokens[0], resource);
                            setBundleResponseFields(responseEntry, resource, locationURI, SC_CREATED);
                        }
                        break;

                        case PUT:
                        {
                            // Process a PUT (update).
                            if (pathTokens.length != 2) {
                                throw new FHIRException("Request URL for bundled PUT request should have path part with exactly two tokens (<resourceType>/<id>).");
                            }
                            Resource resource = FHIRUtil.getResourceContainerResource(requestEntry.getResource());
                            Resource currentResource = doRead(pathTokens[0], pathTokens[1], false);
                            String ifMatchBundleValue = null;
                            if(request.getIfMatch() != null) {
                            	ifMatchBundleValue = request.getIfMatch().getValue();
                            }
                            URI locationURI = doUpdate(pathTokens[0], pathTokens[1], resource, currentResource, ifMatchBundleValue);
                            setBundleResponseFields(responseEntry, resource, locationURI, (currentResource == null ? SC_CREATED : SC_OK));
                        }
                        break;

                        default:
                            // Internal error, should not get here!
                            throw new IllegalStateException("Internal Server Error: reached an unexpected code location.");
                        }
                    } catch (FHIRRestException e) {
                        setBundleResponseStatus(response, e.getHttpStatus().getStatusCode());
                        setBundleEntryResource(responseEntry, (e.getOperationOutcome() == null)? FHIRUtil.buildOperationOutcome(e): e.getOperationOutcome());
                        if (failFast) {
                            throw new FHIRRestBundledRequestException(null, e.getOperationOutcome(), Response.Status.BAD_REQUEST, responseBundle, e);
                        }
                    } catch (FHIRPersistenceResourceNotFoundException e) {
                        setBundleResponseStatus(response, SC_NOT_FOUND);
                        setBundleEntryResource(responseEntry, FHIRUtil.buildOperationOutcome(e));
                        if (failFast) {
                            throw new FHIRRestBundledRequestException(null, FHIRUtil.buildOperationOutcome(e), Response.Status.NOT_FOUND, responseBundle, e);
                        }
                    } catch (FHIRException e) {
                        setBundleResponseStatus(response, SC_BAD_REQUEST);
                        setBundleEntryResource(responseEntry, FHIRUtil.buildOperationOutcome(e));
                        if (failFast) {
                            throw new FHIRRestBundledRequestException(null, FHIRUtil.buildOperationOutcome(e), Response.Status.BAD_REQUEST, responseBundle, e);
                        }
                    }
                }
            }
        } finally {
            log.exiting(this.getClass().getName(), "processEntriesForMethod");
        }
    }

    /**
     * This function will build an absolute URI from the specified base URI and relative URI.
     * @param baseUri the base URI to be used; this will be of the form <scheme>://<host>:<port>/<context-root>
     * @param relativeUri the path and query parts 
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

    private void setBundleResponseFields(BundleEntry responseEntry, Resource resource, URI locationURI, int httpStatus) throws FHIRException {
        BundleResponse response = responseEntry.getResponse();
        response.setStatus(objectFactory.createString().withValue(Integer.toString(httpStatus)));
        response.setLocation(objectFactory.createUri().withValue(locationURI.toString()));
        response.setEtag(objectFactory.createString().withValue(getEtagValue(resource)));
        response.setId(resource.getId().getValue());
        response.setLastModified(resource.getMeta().getLastUpdated());
        setBundleEntryResource(responseEntry, resource);
    }

    private void setBundleResponseStatus(BundleResponse response, int httpStatus) {
        response.setStatus(objectFactory.createString().withValue(Integer.toString(httpStatus)));
    }

    private OperationOutcome buildResourceValidOperationOutcome() {
        OperationOutcome operationOutcome = objectFactory.createOperationOutcome()
                .withId(id("allok"))
                .withText(objectFactory.createNarrative()
                    .withStatus(objectFactory.createNarrativeStatus().withValue(NarrativeStatusList.ADDITIONAL))
                    .withDiv(FHIRUtil.div("<div><p>All OK</p></div>")))
                .withIssue(objectFactory.createOperationOutcomeIssue()
                    .withSeverity(objectFactory.createIssueSeverity().withValue(IssueSeverityList.INFORMATION))
                    .withCode(objectFactory.createIssueType().withValue(IssueTypeList.INFORMATIONAL))
                    .withDetails(objectFactory.createCodeableConcept().withText(string("All OK"))));
        return operationOutcome;
    }
    
    /**
     * Adds the Etag and Last-Modified headers to the specified response object.
     */
    private ResponseBuilder addHeaders(ResponseBuilder rb, Resource resource) {
        return rb.header(HttpHeaders.ETAG, getEtagValue(resource))
                .header(HttpHeaders.LAST_MODIFIED, resource.getMeta().getLastUpdated().getValue().toXMLFormat());
    }
    
    private String getEtagValue(Resource resource) {
        return "W/\"" + resource.getMeta().getVersionId().getValue() + "\"";
    }

    
    private Response exceptionResponse(FHIRRestException e) {
        Response response;
        if (e.getOperationOutcome() != null) {
            String msg = e.getMessage() != null ? e.getMessage() : "<exception message not present>";
            log.log(Level.SEVERE, msg, e);
            response = Response.status(e.getHttpStatus()).entity(e.getOperationOutcome()).build();
        } else {
            response = exceptionResponse(e, e.getHttpStatus());
        }
        
        return response;
    }
    
    private Response exceptionResponse(FHIROperationException e) {
        Response response;
        if (e.getOperationOutcome() != null) {
            String msg = e.getMessage() != null ? e.getMessage() : "<exception message not present>";
            log.log(Level.SEVERE, msg, e);
            response = Response.status(e.getHttpStatus()).entity(e.getOperationOutcome()).build();
        } else {
            response = exceptionResponse(e, e.getHttpStatus());
        }
        
        return response;
    }
    
    private Response exceptionResponse(FHIRRestBundledRequestException e) {
        Response response;
        if (e.getResponseBundle() != null) {
            String msg = e.getMessage() != null ? e.getMessage() : "<exception message not present>";
            log.log(Level.SEVERE, msg, e);
            response = Response.status(e.getHttpStatus()).entity(e.getResponseBundle()).build() ;
        } else {
           response = exceptionResponse(e, e.getHttpStatus()) ;
        }
        return response;
    }
    
    private Response exceptionResponse(FHIRException e) {
        return exceptionResponse(e, e.getHttpStatus());
    }
    
    private Response exceptionResponse(Exception e, Status status) {
        String msg = e.getMessage() != null ? e.getMessage() : "<exception message not present>";
        log.log(Level.SEVERE, msg, e);
        return Response.status(status).entity(FHIRUtil.buildOperationOutcome(e)).build();
    }
    
    private synchronized Conformance getConformanceStatement() {
        if (conformance == null) {
            conformance = buildConformanceStatement();
        }
        conformance.withDate(objectFactory.createDateTime().withValue(new Date().toString()));
        return conformance;
    }
    
    /**
     * Builds a Conformance resource instance which describes this server.
     */
    private Conformance buildConformanceStatement() {
        // Build the list of interactions that are supported for each resource type.
        List<ConformanceInteraction> interactions = new ArrayList<>();
        interactions.add(buildConformanceInteraction(TypeRestfulInteractionList.CREATE));
        interactions.add(buildConformanceInteraction(TypeRestfulInteractionList.UPDATE));
        interactions.add(buildConformanceInteraction(TypeRestfulInteractionList.READ));
        interactions.add(buildConformanceInteraction(TypeRestfulInteractionList.VREAD));
        interactions.add(buildConformanceInteraction(TypeRestfulInteractionList.HISTORY_INSTANCE));
        interactions.add(buildConformanceInteraction(TypeRestfulInteractionList.VALIDATE));
        interactions.add(buildConformanceInteraction(TypeRestfulInteractionList.SEARCH_TYPE));
        
        
        // Build the list of supported resources.
        List<ConformanceResource> resources = new ArrayList<>();
        List<String> resourceTypes = FHIRUtil.getResourceTypeNames();
        for (String resourceType : resourceTypes) {
            
            // Build the set of ConformanceSearchParams for this resource type.
            List<ConformanceSearchParam> conformanceSearchParams = new ArrayList<>();
            List<SearchParameter> searchParameters = SearchUtil.getSearchParameters(resourceType);
            if (searchParameters != null) {
                for (SearchParameter searchParameter : searchParameters) {
                    ConformanceSearchParam conformanceSearchParam = objectFactory.createConformanceSearchParam();
                    conformanceSearchParam.setName(searchParameter.getName());
                    if (searchParameter.getDescription() != null) {
                        conformanceSearchParam.setDocumentation(searchParameter.getDescription());
                    }
                    conformanceSearchParam.setType(searchParameter.getType());
                    if (searchParameter.getType().getValue().equals("reference")) {
                        conformanceSearchParam.getTarget().addAll(searchParameter.getTarget());
                    }
                    
                    conformanceSearchParams.add(conformanceSearchParam);
                }
            }
            
            // Build the ConformanceResource for this resource type.
            ConformanceResource cr = objectFactory.createConformanceResource()
                    .withType(objectFactory.createCode().withValue(resourceType))
                    .withProfile(objectFactory.createReference().withReference(objectFactory.createString().withValue("http://hl7.org/fhir/profiles/" + resourceType)))
                    .withInteraction(interactions)
                    .withConditionalCreate(objectFactory.createBoolean().withValue(false))
                    .withConditionalUpdate(objectFactory.createBoolean().withValue(false))
                    .withConditionalDelete(objectFactory.createConditionalDeleteStatus().withValue(ConditionalDeleteStatusList.NOT_SUPPORTED))
                    .withUpdateCreate(objectFactory.createBoolean().withValue(isUpdateCreateEnabled()))
                    .withSearchParam(conformanceSearchParams);
            
            resources.add(cr);
        }
        
        // Determine if transactions are supported for this FHIR Server configuration.
        TransactionModeList transactionMode = TransactionModeList.NOT_SUPPORTED;
        try {
            boolean txnSupported = getPersistenceImpl().isTransactional();
            transactionMode = (txnSupported ? TransactionModeList.BOTH : TransactionModeList.BATCH);
        } catch (Throwable t) {
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

        ConformanceRest rest = objectFactory.createConformanceRest()
                .withMode(objectFactory.createRestfulConformanceMode().withValue(RestfulConformanceModeList.SERVER))
                .withTransactionMode(objectFactory.createTransactionMode().withValue(transactionMode))
                .withSecurity(objectFactory.createConformanceSecurity()
                		.withService(objectFactory.createCodeableConcept().withCoding(objectFactory.createCoding()
                												.withCode(objectFactory.createCode().withValue("SMART-on-FHIR"))
                												.withSystem(objectFactory.createUri().withValue("http://hl7.org/fhir/restful-security-service")))
                											.withText(string("OAuth2 using SMART-on-FHIR profile (see http://docs.smarthealthit.org)")))
                		.withExtension(objectFactory.createExtension().withUrl("http://fhir-registry.smarthealthit.org/StructureDefinition/oauth-uris")
                									.withExtension(objectFactory.createExtension().withUrl("token")
                														.withValueUri(objectFactory.createUri()
                																.withValue(tokenURL)),
                													objectFactory.createExtension().withUrl("authorize")
                														.withValueUri(objectFactory.createUri()
                																.withValue(authURL)),
                													objectFactory.createExtension().withUrl("register")
                														.withValueUri(objectFactory.createUri()
                																.withValue(regURL)))))
                .withResource(resources);
        
        FHIRBuildIdentifier buildInfo = new FHIRBuildIdentifier();
        String buildDescription = FHIR_SERVER_NAME + " version " + buildInfo.getBuildVersion()
            + " build id " + buildInfo.getBuildId() + "";
        
        // Finally, create the Conformance resource itself.
        conformance = objectFactory.createConformance()
                .withFormat(
                    objectFactory.createCode().withValue(MediaType.APPLICATION_JSON), 
                    objectFactory.createCode().withValue(MediaType.APPLICATION_JSON_FHIR), 
                    objectFactory.createCode().withValue(MediaType.APPLICATION_XML),
                    objectFactory.createCode().withValue(MediaType.APPLICATION_XML_FHIR))
                .withVersion(objectFactory.createString().withValue(buildInfo.getBuildVersion()))
                .withFhirVersion(objectFactory.createId().withValue(FHIR_SPEC_VERSION))
                .withName(objectFactory.createString().withValue(FHIR_SERVER_NAME))
                .withDescription(objectFactory.createString().withValue(buildDescription))
                .withCopyright(objectFactory.createString().withValue("(c) Copyright IBM Corporation 2016"))
                .withPublisher(objectFactory.createString().withValue("IBM Corporation"))
                .withKind(objectFactory.createConformanceStatementKind().withValue(ConformanceStatementKindList.INSTANCE))
                .withSoftware(
                    objectFactory.createConformanceSoftware()
                        .withName(objectFactory.createString().withValue(FHIR_SERVER_NAME))
                        .withVersion(objectFactory.createString().withValue(buildInfo.getBuildVersion()))
                        .withId(buildInfo.getBuildId()))
                .withRest(rest);
        
        try {
            addExtensionElements(conformance);
        } catch (Exception e) {
            log.log(Level.SEVERE, "An error occurred while adding extension elements to the conformance statement", e);
        }
        
        return conformance;
    }
    
    private void addExtensionElements(Conformance conformance) throws Exception {
        Extension extension = objectFactory.createExtension();
        extension.setUrl(EXTENSION_URL + "/encryptionEnabled");
        extension.setValueBoolean(bool(fhirConfig.getPropertyGroup(FHIRConfiguration.PROPERTY_ENCRYPTION).getBooleanProperty("enabled", Boolean.FALSE)));
        conformance.getExtension().add(extension);
        
        extension = objectFactory.createExtension();
        extension.setUrl(EXTENSION_URL + "/userDefinedSchematronEnabled");
        extension.setValueBoolean(bool(isUserDefinedSchematronEnabled()));
        conformance.getExtension().add(extension);
        
        extension = objectFactory.createExtension();
        extension.setUrl(EXTENSION_URL + "/virtualResourcesEnabled");
        extension.setValueBoolean(bool(isVirtualResourceTypesFeatureEnabled()));
        conformance.getExtension().add(extension);
        
        extension = objectFactory.createExtension();
        extension.setUrl(EXTENSION_URL + "/allowableVirtualResourceTypes");
        extension.setValueString(string(getAllowableVirtualResourceTypes().toString().replace("[", "").replace("]", "").replace(" ", "")));
        conformance.getExtension().add(extension);
        
        extension = objectFactory.createExtension();
        extension.setUrl(EXTENSION_URL + "/websocketNotificationsEnabled");
        extension.setValueBoolean(bool(fhirConfig.getBooleanProperty(FHIRConfiguration.PROPERTY_WEBSOCKET_ENABLED, Boolean.FALSE)));
        conformance.getExtension().add(extension);
        
        extension = objectFactory.createExtension();
        extension.setUrl(EXTENSION_URL + "/kafkaNotificationsEnabled");
        extension.setValueBoolean(bool(fhirConfig.getBooleanProperty(FHIRConfiguration.PROPERTY_KAFKA_ENABLED, Boolean.FALSE)));
        conformance.getExtension().add(extension);
        
        extension = objectFactory.createExtension();
        extension.setUrl(EXTENSION_URL + "/notificationResourceTypes");
        
        String notificationResourceTypes = getNotificationResourceTypes();
        if ("".equals(notificationResourceTypes)) {
            notificationResourceTypes = "<not specified - all resource types>";
        }
        extension.setValueString(string(notificationResourceTypes));
        conformance.getExtension().add(extension);
        
        extension = objectFactory.createExtension();
        extension.setUrl(EXTENSION_URL + "/auditLogPath");
        String auditLogPath = fhirConfig.getStringProperty(FHIRConfiguration.PROPERTY_AUDIT_LOGPATH, "");
        if ("".equals(auditLogPath)) {
            auditLogPath = "<not specified>";
        }
        extension.setValueString(string(auditLogPath));
        conformance.getExtension().add(extension);
        
        extension = objectFactory.createExtension();
        extension.setUrl(EXTENSION_URL + "/persistenceType");
        extension.setValueString(string(getPersistenceImpl().getClass().getSimpleName()));
        conformance.getExtension().add(extension);
    }

    private String getNotificationResourceTypes() throws Exception {
        Object[] notificationResourceTypes = fhirConfig.getArrayProperty(FHIRConfiguration.PROPERTY_NOTIFICATION_RESOURCE_TYPES);
        if (notificationResourceTypes == null) {
            notificationResourceTypes = new Object[0];
        }
        return Arrays.asList(notificationResourceTypes).toString().replace("[", "").replace("]", "").replace(" ", "");
    }

    private ConformanceInteraction buildConformanceInteraction(TypeRestfulInteractionList value) {
        ConformanceInteraction ci = objectFactory.createConformanceInteraction()
                .withCode(objectFactory.createTypeRestfulInteraction().withValue(value));
        return ci;
    }

    private Bundle createBundle(List<Resource> resources, BundleTypeList type, long total) throws FHIRException {
        Bundle bundle = objectFactory.createBundle().withType(objectFactory.createBundleType().withValue(type));

        // generate ID for this bundle
        bundle.setId(id(UUID.randomUUID().toString()));

        for (Resource resource : resources) {
            BundleEntry entry = objectFactory.createBundleEntry();
            ResourceContainer container = objectFactory.createResourceContainer();
            entry.setResource(container);
            try {
                FHIRUtil.setResourceContainerResource(container, resource);
            } catch (Exception e) {
                throw new FHIRException("Unable to set resource in bundle entry.", e);
            }
            bundle.getEntry().add(entry);
        }

        // Finally, set the "total" field.
        bundle.setTotal(objectFactory.createUnsignedInt().withValue(BigInteger.valueOf(total)));

        return bundle;
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
            log.fine("Retrieved FHIRPersistenceHelper instance from servlet context: " + persistenceHelper);
        }
        return persistenceHelper;
    }

    private synchronized FHIRPersistence getPersistenceImpl() throws FHIRPersistenceException {
        if (persistence == null) {
            persistence = getPersistenceHelper().getFHIRPersistenceImplementation();
            log.fine("Obtained new  FHIRPersistence instance: " + persistence);
        }
        return persistence;
    }
    
    private boolean isAllowableVirtualResourceType(String virtualResourceType) throws Exception {
        return getAllowableVirtualResourceTypes().contains(virtualResourceType) || 
                getAllowableVirtualResourceTypes().contains("*");
    }
    
    private List<String> getAllowableVirtualResourceTypes() throws Exception {
        return allowableVirtualResourceTypes;
    }
    
    private Boolean isVirtualResourceTypesFeatureEnabled() {
        return virtualResourceTypesFeatureEnabled;
    }
    
    private Boolean isUserDefinedSchematronEnabled() {
        return userDefinedSchematronEnabled;
    }
    
    private Boolean isUpdateCreateEnabled() {
        return updateCreateEnabled;
    }
    
    private Parameter getBasicCodeSearchParameter() {
        if (basicCodeSearchParameter == null) {
            basicCodeSearchParameter = new Parameter(Parameter.Type.TOKEN, "code", null, null);
            ParameterValue value = new ParameterValue();
            value.setValueCode(uriInfo.getPathParameters().getFirst("type"));
            value.setValueSystem(BASIC_RESOURCE_TYPE_URL);
            basicCodeSearchParameter.getValues().add(value);
        }
        return basicCodeSearchParameter;
    }
    
    private void addLinks(FHIRPagingContext context, Bundle bundle, String requestUri) {
        // create 'self' link
        BundleLink selfLink = objectFactory.createBundleLink();
        selfLink.setRelation(string("self"));
        selfLink.setUrl(uri(requestUri));
        bundle.getLink().add(selfLink);
        
        int nextPageNumber = context.getPageNumber() + 1;
        if (nextPageNumber <= context.getLastPageNumber()) {
            // create 'next' link
            BundleLink nextLink = objectFactory.createBundleLink();
            nextLink.setRelation(string("next"));
            
            // starting with the original request URI
            String nextLinkUrl = requestUri;
            
            // remove existing _page and _count parameters from the query string
            nextLinkUrl = nextLinkUrl
                    .replace("&_page=" + context.getPageNumber(), "")
                    .replace("_page=" + context.getPageNumber() + "&", "")                    
                    .replace("_page=" + context.getPageNumber(), "")
                    .replace("&_count=" + context.getPageSize(), "")
                    .replace("_count=" + context.getPageSize() + "&", "")
                    .replace("_count=" + context.getPageSize(), "");
            
            if (nextLinkUrl.contains("?")) {
                if (!nextLinkUrl.endsWith("?")) {
                    // there are other parameters in the query string
                    nextLinkUrl += "&";
                }
            } else {
                nextLinkUrl += "?";
            }
            
            // add new _page and _count parameters to the query string
            nextLinkUrl += "_page=" + nextPageNumber + "&_count=" + context.getPageSize();
            nextLink.setUrl(uri(nextLinkUrl));
            bundle.getLink().add(nextLink);
        }
        
        int prevPageNumber = context.getPageNumber() - 1;
        if (prevPageNumber > 0) {
            // create 'previous' link
            BundleLink prevLink = objectFactory.createBundleLink();
            prevLink.setRelation(string("previous"));
            
            // starting with the original request URI
            String prevLinkUrl = requestUri;
            
            // remove existing _page and _count parameters from the query string
            prevLinkUrl = prevLinkUrl
                    .replace("&_page=" + context.getPageNumber(), "")
                    .replace("_page=" + context.getPageNumber() + "&", "")                    
                    .replace("_page=" + context.getPageNumber(), "")
                    .replace("&_count=" + context.getPageSize(), "")
                    .replace("_count=" + context.getPageSize() + "&", "")
                    .replace("_count=" + context.getPageSize(), "");
            
            if (prevLinkUrl.contains("?")) {
                if (!prevLinkUrl.endsWith("?")) {
                    // there are other parameters in the query string
                    prevLinkUrl += "&";
                }
            } else {
                prevLinkUrl += "?";
            }
            
            // add new _page and _count parameters to the query string
            prevLinkUrl += "_page=" + prevPageNumber + "&_count=" + context.getPageSize();
            prevLink.setUrl(uri(prevLinkUrl));
            bundle.getLink().add(prevLink);
        }
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
                throw new FHIRException("Bundle parameter is missing or empty.");
            }
            
            if (bundle.getType() == null || bundle.getType().getValue() == null) {
                throw new FHIRException("Bundle.type is missing");
            }
            
            // Determine the bundle type of the response bundle.
            BundleTypeList responseBundleType;
            switch (bundle.getType().getValue()) {
            case BATCH:
                responseBundleType = BundleTypeList.BATCH_RESPONSE;
                break;
            case TRANSACTION:
                responseBundleType = BundleTypeList.TRANSACTION_RESPONSE;
                // For a 'transaction' interaction, if the underlying persistence layer doesn't support
                // transactions, then throw an error.
                if (!getPersistenceImpl().isTransactional()) {
                    throw new FHIRException("Bundled 'transaction' request cannot be processed because the configured persistence layer does not support transactions.");
                }
                break;

            // For any other bundle type, we'll throw an error.
            default:
                throw new FHIRException("Bundle.type must be either 'batch' or 'transaction'.");
            }  
            
            // Create the response bundle with the appropriate type.
            Bundle responseBundle = objectFactory.createBundle().withType(objectFactory.createBundleType().withValue(responseBundleType));

            // Next, make sure that each bundle entry contains a valid request.
            // As we're validating the request bundle, we'll also construct entries for the response bundle.
            int numErrors = 0;
            for (BundleEntry requestEntry : bundle.getEntry()) {
                // Create a corresponding response entry and add it to the response bundle.
                BundleResponse response = objectFactory.createBundleResponse();
                BundleEntry responseEntry = objectFactory.createBundleEntry().withResponse(response);
                responseBundle.getEntry().add(responseEntry);
                
                // Validate 'requestEntry' and update 'responseEntry' with any errors.
                try {
                    BundleRequest request = requestEntry.getRequest();
                    // Verify that the request field is present.
                    if (request == null) {
                        throw new FHIRException("BundleEntry is missing the 'request' field.");
                    }
                    
                    // Verify that a method was specified.
                    if (request.getMethod() == null || request.getMethod().getValue() == null) {
                        throw new FHIRException("BundleEntry.request is missing the 'method' field");
                    }

                    // Verify that a URL was specified.
                    if (request.getUrl() == null || request.getUrl().getValue() == null) {
                        throw new FHIRException("BundleEntry.request is missing the 'url' field");
                    }
                    
                    // Retrieve the resource from the request entry to prepare for some validations below.
                    Resource resource = getBundleEntryResource(requestEntry);

                    // Validate the HTTP method.
                    HTTPVerbList method = request.getMethod().getValue();
                    switch (method) {
                    case GET:
                        if (resource != null) {
                            throw new FHIRException("BundleEntry.resource not allowed for BundleEntry with GET method.");
                        }
                        break;

                    case POST:
                    case PUT:
                        if (resource == null) {
                            throw new FHIRException("BundleEntry.resource is required for BundleEntry with POST or PUT method.");
                        }
                        break;

                    default:
                        throw new FHIRException("BundleEntry.request contains unsupported HTTP method: " + method.name());
                    }

                    // If the request entry contains a resource, then validate it now.
                    if (resource != null) {
                        List<OperationOutcomeIssue> issues = FHIRValidator.getInstance().validate(resource, isUserDefinedSchematronEnabled());
                        if (!issues.isEmpty()) {
                            OperationOutcome oo = FHIRUtil.buildOperationOutcome(issues);
                            setBundleEntryResource(responseEntry, oo);
                            response.setStatus(objectFactory.createString().withValue(Integer.toString(SC_BAD_REQUEST)));
                            numErrors++;
                        }
                    }
                } catch (FHIRException e) {
                    setBundleEntryResource(responseEntry, FHIRUtil.buildOperationOutcome(e));
                    response.setStatus(objectFactory.createString().withValue(Integer.toString(SC_BAD_REQUEST)));
                    numErrors++;
                }
            }
            
            // If this is a "transaction" interaction and we encountered any errors, then we'll
            // abort processing this request right now since a transaction interaction is supposed to be
            // all or nothing.
            if (numErrors > 0 && responseBundle.getType().getValue() == BundleTypeList.TRANSACTION_RESPONSE) {
                String msg = "One or more errors were encountered while validating a 'transaction' request bundle.";
                OperationOutcomeIssue issue = buildOperationOutcomeIssue(IssueSeverityList.ERROR, IssueTypeList.EXCEPTION, msg);
                OperationOutcome oo = FHIRUtil.buildOperationOutcome(Collections.singletonList(issue));
                throw new FHIRRestBundledRequestException(msg, oo, Response.Status.BAD_REQUEST, responseBundle);
            }

            return responseBundle;
        } finally {
            log.exiting(this.getClass().getName(), "validateBundle");
        }
    }

    
    /**
     * Builds an OperationOutcomeIssue with the respective values for some of the fields.
     */
    private OperationOutcomeIssue buildOperationOutcomeIssue(IssueSeverityList severity, IssueTypeList type, String msg) {
        OperationOutcomeIssue issue = objectFactory.createOperationOutcomeIssue()
                .withSeverity(objectFactory.createIssueSeverity().withValue(severity))
                .withCode(objectFactory.createIssueType().withValue(type))
                .withDiagnostics(objectFactory.createString().withValue(msg));
        return issue;
    }
    /**
     * Sets the specified Resource in the specified BundleEntry's 'resource' field. We do this via reflection because
     * the FHIR spec gods were determined to make it as difficult as possible to set a resource within the BundleEntry
     * since they defined a distinct field within the ResourceContainer for each possible resource type. (No, I didn't
     * make this up!)
     * 
     * @param entry
     *            the BundleEntry that will hold the resource
     * @param resource
     *            the Resource to be set in the BundleEntry
     * @throws FHIRException
     */
    private void setBundleEntryResource(BundleEntry entry, Resource resource) throws FHIRException {
        ResourceContainer container = objectFactory.createResourceContainer();
        entry.setResource(container);
        try {
            FHIRUtil.setResourceContainerResource(container, resource);
        } catch (Throwable t) {
            String resourceType = resource.getClass().getSimpleName();
            FHIRException e = new FHIRException("Internal error: unable to set resource of type '" + resourceType + "' in bundle entry", t);
            log.log(Level.SEVERE, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Retrieves the Resource from the specified BundleEntry's ResourceContainer.
     * 
     * @param entry
     *            the BundleEntry holding the Resource
     * @return the Resource
     * @throws FHIRException
     */
    private Resource getBundleEntryResource(BundleEntry entry) throws FHIRException {
        try {
            return FHIRUtil.getResourceContainerResource(entry.getResource());
        } catch (Throwable t) {
            FHIRException e = new FHIRException("Internal error: unable to retrieve resource from BundleEntry's resource container.", t);
            log.log(Level.SEVERE, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Builds a collection of properties that will be passed to the persistence interceptors.
     */
    private Map<String, Object> buildPersistenceEventProperties(String type, String id, String version) {
        Map<String, Object> props = new HashMap<>();
        props.put(FHIRPersistenceEvent.PROPNAME_URI_INFO, uriInfo);
        props.put(FHIRPersistenceEvent.PROPNAME_HTTP_HEADERS, httpHeaders);
        props.put(FHIRPersistenceEvent.PROPNAME_SECURITY_CONTEXT, securityContext);
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
}
