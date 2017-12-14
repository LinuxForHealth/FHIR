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
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.bool;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.getResourceType;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.id;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.string;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.uri;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_GONE;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;

import java.io.StringWriter;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
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

import com.ibm.watsonhealth.fhir.config.FHIRConfigHelper;
import com.ibm.watsonhealth.fhir.config.FHIRConfiguration;
import com.ibm.watsonhealth.fhir.config.FHIRRequestContext;
import com.ibm.watsonhealth.fhir.config.PropertyGroup;
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
import com.ibm.watsonhealth.fhir.model.ObjectFactory;
import com.ibm.watsonhealth.fhir.model.OperationOutcome;
import com.ibm.watsonhealth.fhir.model.OperationOutcomeIssue;
import com.ibm.watsonhealth.fhir.model.Parameters;
import com.ibm.watsonhealth.fhir.model.Reference;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.model.ResourceContainer;
import com.ibm.watsonhealth.fhir.model.RestfulConformanceModeList;
import com.ibm.watsonhealth.fhir.model.SearchParameter;
import com.ibm.watsonhealth.fhir.model.TransactionModeList;
import com.ibm.watsonhealth.fhir.model.TypeRestfulInteractionList;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil.Format;
import com.ibm.watsonhealth.fhir.model.util.ReferenceFinder;
import com.ibm.watsonhealth.fhir.operation.FHIROperation;
import com.ibm.watsonhealth.fhir.operation.context.FHIROperationContext;
import com.ibm.watsonhealth.fhir.operation.exception.FHIROperationException;
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
import com.ibm.watsonhealth.fhir.persistence.helper.PersistenceHelper;
import com.ibm.watsonhealth.fhir.persistence.interceptor.FHIRPersistenceEvent;
import com.ibm.watsonhealth.fhir.persistence.interceptor.impl.FHIRPersistenceInterceptorMgr;
import com.ibm.watsonhealth.fhir.persistence.util.FHIRPersistenceUtil;
import com.ibm.watsonhealth.fhir.rest.FHIRResourceHelpers;
import com.ibm.watsonhealth.fhir.rest.FHIRRestOperationResponse;
import com.ibm.watsonhealth.fhir.search.Parameter;
import com.ibm.watsonhealth.fhir.search.ParameterValue;
import com.ibm.watsonhealth.fhir.search.context.FHIRSearchContext;
import com.ibm.watsonhealth.fhir.search.util.SearchUtil;
import com.ibm.watsonhealth.fhir.server.FHIRBuildIdentifier;
import com.ibm.watsonhealth.fhir.server.exception.FHIRRestBundledRequestException;
import com.ibm.watsonhealth.fhir.server.exception.FHIRRestException;
import com.ibm.watsonhealth.fhir.server.helper.FHIRUrlParser;
import com.ibm.watsonhealth.fhir.server.listener.FHIRServletContextListener;
import com.ibm.watsonhealth.fhir.server.util.RestAuditLogger;
import com.ibm.watsonhealth.fhir.validation.FHIRValidator;

@Path("/")
@Produces({ MediaType.APPLICATION_JSON_FHIR, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML_FHIR, MediaType.APPLICATION_XML })
@Consumes({ MediaType.APPLICATION_JSON_FHIR, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML_FHIR, MediaType.APPLICATION_XML })
public class FHIRResource implements FHIRResourceHelpers {
    private static final Logger log = java.util.logging.Logger.getLogger(FHIRResource.class.getName());
    
    private static final String FHIR_SERVER_NAME = "IBM Watson Health Cloud FHIR Server";
    private static final String FHIR_SPEC_VERSION = "1.0.2 - DSTU2";
    private static final String EXTENSION_URL = "http://ibm.com/watsonhealth/fhir/extension";
    private static final String BASIC_RESOURCE_TYPE_URL = "http://ibm.com/watsonhealth/fhir/basic-resource-type";
    private static final String REQUEST_HEADER_URL = "http://www.ibm.com/watsohealth/fhir/extensions/http-request-header";

    private static final String LOCAL_REF_PREFIX = "urn:";
    private static final String HEADERNAME_IF_NONE_EXIST = "If-None-Exist";

    private PersistenceHelper persistenceHelper = null;
    private FHIRPersistence persistence = null;
    private ObjectFactory objectFactory = new ObjectFactory();
    
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
     * This method will do a quick check of the "initCompleted" flag in the servlet context.
     * If the flag is FALSE, then we'll throw an error to short-circuit the current in-progress REST API invocation.
     */
    private void checkInitComplete() throws FHIRRestException {
        Boolean fhirServerInitComplete = (Boolean) context.getAttribute(FHIRServletContextListener.FHIR_SERVER_INIT_COMPLETE);
        if (Boolean.FALSE.equals(fhirServerInitComplete)) {
            throw new FHIRRestException("The FHIR Server web application cannot process requests because it did not initialize correctly", null, Status.INTERNAL_SERVER_ERROR );
        }
    }
    
    public FHIRResource() throws Exception {
        log.entering(this.getClass().getName(), "FHIRResource ctor");
        try {
            fhirConfig = FHIRConfiguration.getInstance().loadConfiguration();
        } catch (Throwable t) {
            log.severe("Unexpected error during initialization: " + t);
            throw t;
        } finally {
            log.exiting(this.getClass().getName(), "FHIRResource ctor");
        }
    }

    @GET
    @Path("metadata")
    public Response metadata() throws ClassNotFoundException {
        log.entering(this.getClass().getName(), "metadata()");
        Date startTime = new Date();
        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        try {
            checkInitComplete();

            status = Response.Status.OK;
            return Response.ok().entity(getConformanceStatement()).build();
        } catch (FHIRRestException e) {
            status = e.getHttpStatus();
            return exceptionResponse(e);
        } catch(Exception e) {
        	return exceptionResponse(e, status);
        } finally {
        	RestAuditLogger.logMetadata(httpServletRequest, startTime, new Date(), status);
        	log.exiting(this.getClass().getName(), "metadata()");
        }
    }

    @POST
    @Path("{type}")
    public Response create(
        @PathParam("type") String type, 
        Resource resource) {
    	
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
        } catch (FHIRRestException e) {
        	return exceptionResponse(e);
        } catch (FHIRException e) {
        	return exceptionResponse(e);
        } catch (Exception e) {
        	return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
        	log.exiting(this.getClass().getName(), "create(Resource)");
        }
    }

    @PUT
    @Path("{type}/{id}")
    public Response update(
        @PathParam("type") String type, 
        @PathParam("id") String id, 
        Resource resource) {

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
        	status = Response.Status.INTERNAL_SERVER_ERROR;
            return exceptionResponse(e, status);
        } finally {
            log.exiting(this.getClass().getName(), "update(String,String,Resource)");
        }
    }

    @PUT
    @Path("{type}")
    public Response conditionalUpdate(
        @PathParam("type") String type, 
        Resource resource) {

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
                throw new FHIRException("A search query string is required for a conditional update operation.");
            }
            
            ior = doUpdate(type, null, resource, httpHeaders.getHeaderString(HttpHeaders.IF_MATCH), searchQueryString, null);
            
            ResponseBuilder response = Response.ok().location(toUri(getAbsoluteUri(getRequestBaseUri(), ior.getLocationURI().toString())));
            status = ior.getStatus();
            response.status(status);

            response = addHeaders(response, ior.getResource());
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
        	if (createAuditLogRecord) {
        		if (status == Response.Status.CREATED) {
        			RestAuditLogger.logCreate(httpServletRequest, (ior != null ? ior.getResource() : null), startTime, new Date(), status);
        		} else {
        			RestAuditLogger.logUpdate(httpServletRequest, (ior != null ? ior.getPrevResource() : null), (ior != null ? ior.getResource() : null), startTime, new Date(), status);
        		}
        	}
            log.exiting(this.getClass().getName(), "conditionalUpdate(String,Resource)");
        }
    }

    @DELETE
    @Path("{type}/{id}")
    public Response delete(
        @PathParam("type") String type, 
        @PathParam("id") String id) throws Exception {
        log.entering(this.getClass().getName(), "delete(String,String)");
        
        Response.Status status;

        FHIRRestOperationResponse ior = null;
        
        try {
            checkInitComplete();
            
            ior = doDelete(type, id, null, null);
            ResponseBuilder response = Response.noContent();
            if (ior.getResource() != null) {
                response = addHeaders(response, ior.getResource());
            }
            return response.build();
        } catch (FHIRRestException e) {
            status = e.getHttpStatus();
            return exceptionResponse(e);
        } catch (FHIRPersistenceResourceNotFoundException e) {
            status = Response.Status.NOT_FOUND;
            return exceptionResponse(e, status);
        } catch (FHIRPersistenceNotSupportedException e) {
            status = Response.Status.METHOD_NOT_ALLOWED;
            return exceptionResponse(e, status);
        } catch (FHIRException e) {
            status = e.getHttpStatus();
            return exceptionResponse(e);
        } catch (Exception e) {
        	status = Response.Status.INTERNAL_SERVER_ERROR;
            return exceptionResponse(e, status);
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
                throw new FHIRException("A search query string is required for a conditional delete operation.");
            }
            
            ior = doDelete(type, null, searchQueryString, null);
            ResponseBuilder response = Response.noContent();
            status = ior.getStatus();
            if (ior.getResource() != null) {
                response = addHeaders(response, ior.getResource());
            }
            return response.build();
        } catch (FHIRRestException e) {
            status = e.getHttpStatus();
            return exceptionResponse(e);
        } catch (FHIRPersistenceResourceNotFoundException e) {
            status = Response.Status.NOT_FOUND;
            return exceptionResponse(e, status);
        } catch (FHIRPersistenceNotSupportedException e) {
            status = Response.Status.METHOD_NOT_ALLOWED;
            return exceptionResponse(e, status);
        } catch (FHIRException e) {
            status = e.getHttpStatus();
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
    public Response read(
        @PathParam("type") String type, 
        @PathParam("id") String id) throws Exception {
        log.entering(this.getClass().getName(), "read(String,String)");
        
        Response.Status status;
    	Resource resource = null;
    	
        try {
            checkInitComplete();

            resource = doRead(type, id, true, false, null, null);
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
        } catch (FHIRPersistenceResourceDeletedException e) {
            status = Response.Status.GONE;
            return exceptionResponse(e, status);
        } catch (FHIRException e) {
        	status = e.getHttpStatus();
            return exceptionResponse(e);
        } catch (Exception e) {
        	status = Response.Status.INTERNAL_SERVER_ERROR;
        	return exceptionResponse(e, status);
        } finally {
        	log.exiting(this.getClass().getName(), "read(String,String)");
        }
    }

    @GET
    @Path("{type}/{id}/_history/{vid}")
    public Response vread(
        @PathParam("type") String type, 
        @PathParam("id") String id, 
        @PathParam("vid") String vid) {
      
        log.entering(this.getClass().getName(), "vread(String,String,String)");
        
        Response.Status status;
    	Resource resource = null;
    	
        try {
            checkInitComplete();

            resource = doVRead(type, id, vid, null);
            
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
        } catch (FHIRPersistenceResourceDeletedException e) {
            status = Response.Status.GONE;
            return exceptionResponse(e, status);
        } catch (FHIRException e) {
        	status = e.getHttpStatus();
            return exceptionResponse(e);
        } catch (Exception e) {
        	status = Response.Status.INTERNAL_SERVER_ERROR;
        	return exceptionResponse(e, status);
        } finally {
        	log.exiting(this.getClass().getName(), "vread(String,String,String)");
        }
    }

    @GET
    @Path("{type}/{id}/_history")
    public Response history(
        @PathParam("type") String type, 
        @PathParam("id") String id) {
        log.entering(this.getClass().getName(), "history(String,String)");
        
        Response.Status status;
    	Bundle bundle= null;
    	
        try {
            checkInitComplete();

            bundle = doHistory(type, id, uriInfo.getQueryParameters(), getRequestUri(), null);
            status = Response.Status.OK;
            return Response.ok(bundle).build();
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
        	log.exiting(this.getClass().getName(), "history(String,String)");
        }
    }

    @GET
    @Path("{type}")
    public Response search(
        @PathParam("type") String type) {
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
        	log.exiting(this.getClass().getName(), "search(String)");
        }
    }
    
    @GET
    @Path("{compartment}/{compartmentId}/{type}")
    public Response searchCompartment(
    	@PathParam("compartment") String compartment,
    	@PathParam("compartmentId") String compartmentId,
        @PathParam("type") String type) {
    	
        log.entering(this.getClass().getName(), "search(String, String, String)");
        Response.Status status;
    	MultivaluedMap<String, String> queryParameters = null;
    	Bundle bundle = null;
    	
        try {
            checkInitComplete();

            queryParameters = uriInfo.getQueryParameters();
            bundle = doSearch(type, compartment, compartmentId, queryParameters, getRequestUri(), null, null);
            status = Response.Status.OK;
            return Response.ok(bundle).build();
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
        	log.exiting(this.getClass().getName(), "search(String)");
        }
    }
    
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Path("{type}/_search")
    public Response _search(@PathParam("type") String type) {
        log.entering(this.getClass().getName(), "_search(String)");
        Date startTime = new Date();
        Response.Status  status = Response.Status.INTERNAL_SERVER_ERROR;
        MultivaluedMap<String, String> queryParameters = null;
        Bundle bundle = null;
        
        try {
            checkInitComplete();

            queryParameters = uriInfo.getQueryParameters();
            bundle = doSearch(type, null, null, queryParameters, getRequestUri(), null, null);
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
            log.exiting(this.getClass().getName(), "_search(String)");
        }
    }
    
    @GET
    @Path("_search")
    public Response searchAll() {
        log.entering(this.getClass().getName(), "searchAll()");
        Response.Status status;
        MultivaluedMap<String, String> queryParameters = null;
        Bundle bundle = null;
        
        try {
            checkInitComplete();

            queryParameters = uriInfo.getQueryParameters();
            bundle = doSearch("Resource", null, null, queryParameters, getRequestUri(), null, null);
            status = Response.Status.OK;
            return Response.ok(bundle).build();
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
            Resource result = doInvoke(operationContext, null, null, null, operationName, null, null);
            return buildResponse(operationContext, result);
        } catch (FHIROperationException e) {
            return exceptionResponse(e);
        } catch (FHIRRestException e) {
            return exceptionResponse(e);
        } catch (FHIRException e) {
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
            Resource result = doInvoke(operationContext, null, null, null, operationName, resource, null);
            return buildResponse(operationContext, result);
        } catch (FHIROperationException e) {
            return exceptionResponse(e);
        } catch (FHIRRestException e) {
            return exceptionResponse(e);
        } catch (FHIRException e) {
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
            Resource result = doInvoke(operationContext, resourceTypeName, null, null, operationName, null, null); 
            return buildResponse(operationContext, result);
        } catch (FHIROperationException e) {
            return exceptionResponse(e);
        } catch (FHIRRestException e) {
            return exceptionResponse(e);
        } catch (FHIRException e) {
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
            Resource result = doInvoke(operationContext, resourceTypeName, null, null, operationName, resource, null);
            return buildResponse(operationContext, result);
        } catch (FHIROperationException e) {
            return exceptionResponse(e);
        } catch (FHIRRestException e) {
            return exceptionResponse(e);
        } catch (FHIRException e) {
            return exceptionResponse(e);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "invoke(String,String,Resource)");
        }
    }
    
    @GET
    @Path("{resourceTypeName}/{logicalId}/${operationName}")
    public Response invoke(@PathParam("resourceTypeName") String resourceTypeName, @PathParam("logicalId") String logicalId, @PathParam("operationName") String operationName) {
        log.entering(this.getClass().getName(), "invoke(String,String,String)");
        try {
            checkInitComplete();

            FHIROperationContext operationContext = FHIROperationContext.createInstanceOperationContext();
            Resource result = doInvoke(operationContext, resourceTypeName, logicalId, null, operationName, null, null);
            return buildResponse(operationContext, result);
        } catch (FHIROperationException e) {
            return exceptionResponse(e);
        } catch (FHIRRestException e) {
            return exceptionResponse(e);
        } catch (FHIRException e) {
            return exceptionResponse(e);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "invoke(String,String,String)");
        }
    }

    @POST
    @Path("{resourceTypeName}/{logicalId}/${operationName}")
    public Response invoke(@PathParam("resourceTypeName") String resourceTypeName, @PathParam("logicalId") String logicalId, @PathParam("operationName") String operationName, Resource resource) {
        log.entering(this.getClass().getName(), "invoke(String,String,String,Resource)");
        try {
            checkInitComplete();

            FHIROperationContext operationContext = FHIROperationContext.createInstanceOperationContext();
            Resource result = doInvoke(operationContext, resourceTypeName, logicalId, null, operationName, resource, null);
            return buildResponse(operationContext, result);
        } catch (FHIROperationException e) {
            return exceptionResponse(e);
        } catch (FHIRRestException e) {
            return exceptionResponse(e);
        } catch (FHIRException e) {
            return exceptionResponse(e);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "invoke(String,String,String,Resource)");
        }
    }
    
    @GET
    @Path("{resourceTypeName}/{logicalId}/_history/{versionId}/${operationName}")
    public Response invoke(@PathParam("resourceTypeName") String resourceTypeName, @PathParam("logicalId") String logicalId, @PathParam("versionId") String versionId, @PathParam("operationName") String operationName) {
        log.entering(this.getClass().getName(), "invoke(String,String,String,String)");
        try {
            checkInitComplete();

            FHIROperationContext operationContext = FHIROperationContext.createInstanceOperationContext();
            Resource result = doInvoke(operationContext, resourceTypeName, logicalId, versionId, operationName, null, null);
            return buildResponse(operationContext, result);
        } catch (FHIROperationException e) {
            return exceptionResponse(e);
        } catch (FHIRRestException e) {
            return exceptionResponse(e);
        } catch (FHIRException e) {
            return exceptionResponse(e);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "invoke(String,String,String,String)");
        }
    }

    @POST
    @Path("{resourceTypeName}/{logicalId}/_history/{versionId}/${operationName}")
    public Response invoke(@PathParam("resourceTypeName") String resourceTypeName, @PathParam("logicalId") String logicalId, @PathParam("versionId") String versionId, @PathParam("operationName") String operationName, Resource resource) {
        log.entering(this.getClass().getName(), "invoke(String,String,String,String,Resource)");
        try {
            checkInitComplete();

            FHIROperationContext operationContext = FHIROperationContext.createInstanceOperationContext();
            Resource result = doInvoke(operationContext, resourceTypeName, logicalId, versionId, operationName, resource, null);
            return buildResponse(operationContext, result);
        } catch (FHIROperationException e) {
            return exceptionResponse(e);
        } catch (FHIRRestException e) {
            return exceptionResponse(e);
        } catch (FHIRException e) {
            return exceptionResponse(e);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "invoke(String,String,String,String,Resource)");
        }
    }

    @POST
    public Response bundle(Bundle bundle) {

        log.entering(this.getClass().getName(), "bundle(Bundle)");
        Response.Status status;
    	Bundle responseBundle = null;

        try {
            checkInitComplete();

            responseBundle = doBundle(bundle, null);
                
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
            status = Response.Status.INTERNAL_SERVER_ERROR;
        	log.log(Level.SEVERE, "Error encountered during bundle request processing: ", e);
            return exceptionResponse(e, status);
        } finally {
        	log.exiting(this.getClass().getName(), "bundle(Bundle)");
        }
    }

    /**
     * Performs the heavy lifting associated with a 'create' interaction.
     * @param type the resource type specified as part of the request URL
     * @param resource the Resource to be stored.
     * @return a FHIRRestOperationResponse object containing the results of the operation
     * @throws Exception
     */
    public FHIRRestOperationResponse doCreate(String type, Resource resource, String ifNoneExist, Map<String, String> requestProperties) throws Exception {
        log.entering(this.getClass().getName(), "doCreate");

        FHIRPersistenceTransaction txn = null;
        boolean txnStarted = false;
        Date startTime = new Date();
        Response.Status status = null;
        String errMsg = "Caught exception while processing 'create' request.";
        
        FHIRRestOperationResponse ior = new FHIRRestOperationResponse();
        
        // Save the current request context.
        FHIRRequestContext requestContext = FHIRRequestContext.get();

        try {
            
            // Make sure the expected type (specified in the URL string) is congruent with the actual type 
            // of the resource.
            String resourceType = FHIRUtil.getResourceTypeName(resource);
            if (!resourceType.equals(type)) {
                throw new FHIRException("Resource type '" + resourceType + "' does not match type specified in request URI: " + type);
            }
            
            // Check to see if we're supposed to perform a conditional 'create'.
            if (ifNoneExist != null && !ifNoneExist.isEmpty()) {
                log.fine("Performing conditional create with search criteria: " + ifNoneExist);
                Bundle responseBundle = null;

                // Perform the search using the "If-None-Exist" header value.
                try {
                    MultivaluedMap<String, String> searchParameters = getQueryParameterMap(ifNoneExist);
                    responseBundle = doSearch(type, null, null, searchParameters, null, requestProperties, resource);
                } catch (Throwable t) {
                    String msg = "An error occurred while performing the search for a conditional create operation.";
                    log.log(Level.SEVERE, msg, t);
                    throw new FHIRException(msg, Response.Status.BAD_REQUEST, null);
                }
                
                // Check the search results to determine whether or not to perform the create operation.
                int resultCount = responseBundle.getEntry().size();
                log.fine("Conditional create search yielded " + resultCount + " results.");

                if (resultCount == 0) {
                    // Do nothing and fall through to process the 'create' request.
                } else if (resultCount == 1) {
                    // If we found a single match, bypass the 'create' request and return information
                    // for the matched resource.
                    Resource matchedResource = FHIRUtil.getResourceContainerResource(responseBundle.getEntry().get(0).getResource());
                    ior.setLocationURI(FHIRUtil.buildLocationURI(type, matchedResource));
                    ior.setStatus(Response.Status.OK);
                    ior.setResource(matchedResource);
                    log.fine("Returning location URI of matched resource: " + ior.getLocationURI());
                    status = ior.getStatus();
                    return ior;
                } else {
                    String msg = "The search criteria specified for a conditional create operation returned multiple matches.";
                    throw new FHIRException(msg, Response.Status.PRECONDITION_FAILED, null);
                }
            }
            
            // A new resource should not contain an ID.
            if (resource.getId() != null) {
                throw new FHIRException("A 'create' operation cannot be performed on a resource that contains an 'id' attribute.");
            }

            // Validate the input resource and return any validation errors.
            List<OperationOutcomeIssue> issues = FHIRValidator.getInstance().validate(resource, isUserDefinedSchematronEnabled());
            if (!issues.isEmpty()) {
                OperationOutcome operationOutcome = FHIRUtil.buildOperationOutcome(issues);
                throw new FHIRRestException("Input resource failed validation.", operationOutcome, Response.Status.BAD_REQUEST);
            }

            // If there were no validation errors, then create the resource and return the location header.
            
            // Start a new txn in the persistence layer if one is not already active.
            txn = getTransaction();
            if (txn != null && !txn.isActive()) {
                txn.begin();
                txnStarted = true;
                log.fine("Started new transaction for 'create' operation.");
            }

            // First, invoke the 'beforeCreate' interceptor methods.
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(resource, buildPersistenceEventProperties(type, null, null, requestProperties));
            getInterceptorMgr().fireBeforeCreateEvent(event);

            FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(event);
            getPersistenceImpl().create(persistenceContext, resource);
            ior.setStatus(Response.Status.CREATED);
            ior.setResource(resource);

            // Build our location URI and add it to the interceptor event structure since it is now known.
            ior.setLocationURI(FHIRUtil.buildLocationURI(FHIRUtil.getResourceTypeName(resource), resource));
            event.getProperties().put(FHIRPersistenceEvent.PROPNAME_RESOURCE_LOCATION_URI, ior.getLocationURI().toString());

            // Invoke the 'afterCreate' interceptor methods.
            getInterceptorMgr().fireAfterCreateEvent(event);
            
            // Commit our transaction if we started one before.
            if (txnStarted) {
                log.fine("Committing transaction for 'create' operation.");
                txn.commit();
                txn = null;
                txnStarted = false;
            }

            status = ior.getStatus();
            return ior;
        } catch (FHIRException e) {
            log.log(Level.SEVERE, errMsg, e);
            status = e.getHttpStatus();
            throw e;
        } catch (Throwable t) {
            log.log(Level.SEVERE, errMsg, t);
            status = Response.Status.INTERNAL_SERVER_ERROR;
            throw t;
        } finally {
            // Restore the original request context.
            FHIRRequestContext.set(requestContext);
            
            // If we previously started a transaction and it's still active, we need to rollback due to an error.
            if (txnStarted) {
                rollback(txn, "create");
                txn = null;
                txnStarted = false;
            }
            RestAuditLogger.logCreate(httpServletRequest, resource, startTime, new Date(), status);
            log.exiting(this.getClass().getName(), "doCreate");
        }
    }
    
    
    /**
     * Performs an update operation (a new version of the Resource will be stored).
     * @param type the type of the resource to be updated
     * @param id the id of the Resource being updated
     * @param newResource the new resource to be stored
     * @param ifMatchValue an optional "If-Match" header value to request a version-aware update
     * @param searchQueryString an optional search query string to request a conditional update
     * @return a FHIRRestOperationResponse that contains the results of the operation
     * @throws Exception
     */
    public FHIRRestOperationResponse doUpdate(String type, String id, Resource newResource, String ifMatchValue, String searchQueryString, Map<String, String> requestProperties) throws Exception {
        log.entering(this.getClass().getName(), "doUpdate");

        FHIRPersistenceTransaction txn = null;
        boolean txnStarted = false;
        Date startTime = new Date();
        Response.Status status = null;
        String errMsg = "Caught exception while processing 'update' request.";

        // Save the current request context.
        FHIRRequestContext requestContext = FHIRRequestContext.get();
        
        FHIRRestOperationResponse ior = new FHIRRestOperationResponse();

        try {
            // Make sure the type specified in the URL string matches the resource type obtained from the new resource.
            String resourceType = FHIRUtil.getResourceTypeName(newResource);
            if (!resourceType.equals(type)) {
                throw new FHIRException("Resource type '" + resourceType + "' does not match type specified in request URI: " + type);
            }
            
            // Make sure the resource has an 'id' attribute.
            if (newResource.getId() == null) {
                throw new FHIRException("Input resource must contain an 'id' attribute.");
            }
            
            // Next, if a conditional update was invoked then use the search criteria to find the
            // resource to be updated.   Otherwise, we'll use the id value to retrieve the current
            // version of the resource.
            if (searchQueryString != null) {
                log.fine("Performing conditional update with search criteria: " + searchQueryString);
                Bundle responseBundle = null;
                try {
                    MultivaluedMap<String, String> searchParameters = getQueryParameterMap(searchQueryString);
                    responseBundle = doSearch(type, null, null, searchParameters, null, requestProperties, newResource);
                } catch (Throwable t) {
                    String msg = "An error occurred while performing the search for a conditional update operation.";
                    log.log(Level.SEVERE, msg, t);
                    throw new FHIRException(msg, Response.Status.BAD_REQUEST, null);
                }
                
                // Check the search results to determine whether or not to perform the update operation.
                int resultCount = responseBundle.getEntry().size();
                log.fine("Conditional update search yielded " + resultCount + " results.");

                if (resultCount == 0) {
                    // Search yielded no matches, so we'll do an update/create operation below.
                    ior.setPrevResource(null);
                    id = newResource.getId().getValue();
                } else if (resultCount == 1) {
                    // If we found a single match, then we'll perform a normal update on the matched resource.
                    ior.setPrevResource(FHIRUtil.getResourceContainerResource(responseBundle.getEntry().get(0).getResource()));
                    id = ior.getPrevResource().getId().getValue();
                } else {
                    String msg = "The search criteria specified for a conditional update operation returned multiple matches.";
                    throw new FHIRException(msg, Response.Status.PRECONDITION_FAILED, null);
                }
            } else {
                // Make sure an id value was passed in.
                if (id == null) {
                    throw new FHIRException("The 'id' parameter is required for an update operation.");
                }
                
                // If an id value was passed in (i.e. the id specified in the REST API URL string),
                // then make sure it's the same as the value in the resource.
                if (!newResource.getId().getValue().equals(id)) {
                    throw new FHIRException("Input resource 'id' attribute must match 'id' parameter.");
                }
                
                // Retrieve the resource to be updated using the type and id values.
                ior.setPrevResource(doRead(type, id, false, true, requestProperties, newResource));
            }
            
            // Validate the input resource and return any validation errors.
            List<OperationOutcomeIssue> issues = FHIRValidator.getInstance().validate(newResource, isUserDefinedSchematronEnabled());
            if (!issues.isEmpty()) {
                OperationOutcome operationOutcome = FHIRUtil.buildOperationOutcome(issues);
                throw new FHIRRestException("Input resource failed validation.", operationOutcome, Response.Status.BAD_REQUEST);
            }
            
            // Perform the "version-aware" update check.
            if (ior.getPrevResource() != null) {
                performVersionAwareUpdateCheck(ior.getPrevResource(), ifMatchValue);
            }

            // Start a new txn in the persistence layer if one is not already active.
            txn = getTransaction();
            if (txn != null && !txn.isActive()) {
                txn.begin();
                txnStarted = true;
                log.fine("Started new transaction for 'update' operation.");
            }
            
            // First, invoke the 'beforeUpdate' interceptor methods.
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(newResource, buildPersistenceEventProperties(type, newResource.getId().getValue(), null, requestProperties));
            boolean updateCreate = (ior.getPrevResource() == null); 
            if (updateCreate) {
            	getInterceptorMgr().fireBeforeCreateEvent(event);
            } else {
            	getInterceptorMgr().fireBeforeUpdateEvent(event);
            }

            FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(event);
            getPersistenceImpl().update(persistenceContext, id, newResource);
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
            	getInterceptorMgr().fireAfterUpdateEvent(event);
            }
            
            // Commit our transaction if we started one before.
            if (txnStarted) {
                log.fine("Committing transaction for 'update' operation.");
                txn.commit();
                txn = null;
                txnStarted = false;
            }
            status = ior.getStatus();
            
            return ior;
        } catch (FHIRPersistenceResourceNotFoundException e) {
            status = Response.Status.METHOD_NOT_ALLOWED;
            throw e;
        } catch (FHIRException e) {
            log.log(Level.SEVERE, errMsg, e);
            status = e.getHttpStatus();
            throw e;
        } catch (Throwable t) {
            log.log(Level.SEVERE, errMsg, t);
            status = Response.Status.INTERNAL_SERVER_ERROR;
            throw t;
        } finally {
            // Restore the original request context.
            FHIRRequestContext.set(requestContext);

            // If we still have a transaction at this point, we need to rollback due to an error.
            if (txnStarted) {
                rollback(txn, "update");
                txn = null;
                txnStarted = false;
            }
            if (status == Response.Status.CREATED) {
                RestAuditLogger.logCreate(httpServletRequest, (ior != null ? ior.getResource() : null), startTime, new Date(), status);
            } else {
                RestAuditLogger.logUpdate(httpServletRequest, (ior != null ? ior.getPrevResource() : null), (ior != null ? ior.getResource() : null), startTime, new Date(), status);
            }
            log.exiting(this.getClass().getName(), "doUpdate");
        }
    }
    
    /**
     * Performs a 'delete' operation on the specified resource.
     * @param type the resource type associated with the Resource to be deleted
     * @param id the id of the Resource to be deleted
     * @return a FHIRRestOperationResponse that contains the results of the operation
     * @throws Exception
     */
    public FHIRRestOperationResponse doDelete(String type, String id, String searchQueryString, Map<String, String> requestProperties) throws Exception {
        log.entering(this.getClass().getName(), "doDelete");

        // Save the current request context.
        FHIRRequestContext requestContext = FHIRRequestContext.get();
        FHIRPersistenceTransaction txn = null;
        boolean txnStarted = false;
        Date startTime = new Date();
        Response.Status status = null;
        String errMsg = "Caught exception while processing 'delete' request.";
        
        FHIRRestOperationResponse ior = new FHIRRestOperationResponse();

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
            
            // Next, if a conditional delete was invoked then use the search criteria to find the
            // resource to be deleted.   Otherwise, we'll use the id value to identify the resource
            // to be deleted.
            Resource resourceToDelete = null;
            if (searchQueryString != null) {
                log.fine("Performing conditional delete with search criteria: " + searchQueryString);
                Bundle responseBundle = null;
                try {
                    MultivaluedMap<String, String> searchParameters = getQueryParameterMap(searchQueryString);
                    responseBundle = doSearch(type, null, null, searchParameters, null, requestProperties, null);
                } catch (Throwable t) {
                    String msg = "An error occurred while performing the search for a conditional delete operation.";
                    log.log(Level.SEVERE, msg, t);
                    throw new FHIRException(msg, Response.Status.BAD_REQUEST, null);
                }
                
                // Check the search results to determine whether or not to perform the update operation.
                int resultCount = responseBundle.getEntry().size();
                log.fine("Conditional delete search yielded " + resultCount + " results.");

                if (resultCount == 0) {
                    // Search yielded no matches, so we'll return a 404 Not Found.
                    String msg = "Search criteria for a conditional delete operation yielded no matches.";
                    throw new FHIRPersistenceResourceNotFoundException(msg);
                } else if (resultCount == 1) {
                    // If we found a single match, then we'll delete this one.
                    Resource resource = FHIRUtil.getResourceContainerResource(responseBundle.getEntry().get(0).getResource());
                    id = resource.getId().getValue();
                    resourceToDelete = resource;
                } else {
                    String msg = "The search criteria specified for a conditional delete operation returned multiple matches.";
                    throw new FHIRException(msg, Response.Status.PRECONDITION_FAILED, null);
                }
            } else {
                // Make sure an id value was passed in.
                if (id == null) {
                    throw new FHIRException("The 'id' parameter is required for a delete operation.");
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
            txn = getTransaction();
            if (txn != null && !txn.isActive()) {
                txn.begin();
                txnStarted = true;
                log.fine("Started new transaction for 'delete' operation.");
            }
            
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
            if (txnStarted) {
                log.fine("Committing transaction for 'delete' operation.");
                txn.commit();
                txn = null;
                txnStarted = false;
            }
            status = ior.getStatus();
            
            return ior;
        } catch (FHIRPersistenceResourceNotFoundException e) {
            status = Response.Status.NOT_FOUND;
            throw e;
        } catch (FHIRPersistenceNotSupportedException e) {
            status = Response.Status.METHOD_NOT_ALLOWED;
            throw e;
        } catch (FHIRException e) {
            log.log(Level.SEVERE, errMsg, e);
            status = e.getHttpStatus();
            throw e;
        } catch (Throwable t) {
            log.log(Level.SEVERE, errMsg, t);
            status = Response.Status.INTERNAL_SERVER_ERROR;
            throw t;
        } finally {
            // Restore the original request context.
            FHIRRequestContext.set(requestContext);
            
            // If we previously started a transaction and it's still active, we need to rollback due to an error.
            if (txnStarted) {
                rollback(txn, "delete");
                txn = null;
                txnStarted = false;
            }
            RestAuditLogger.logDelete(httpServletRequest, ior != null ? ior.getResource() : null, startTime, new Date(), status);
            log.exiting(this.getClass().getName(), "doDelete");
        }
    }

    /**
     * Performs a 'read' operation to retrieve a Resource.
     * @param type the resource type associated with the Resource to be retrieved
     * @param id the id of the Resource to be retrieved
     * @return the Resource
     * @throws Exception
     */
    public Resource doRead(String type, String id, boolean throwExcOnNull, boolean includeDeleted, Map<String, String> requestProperties, Resource contextResource) throws Exception {
        log.entering(this.getClass().getName(), "doRead");
        
        FHIRPersistenceTransaction txn = null;
        boolean txnStarted = false;
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
                    throw new FHIRVirtualResourceTypeException("The virtual resource types feature is not enabled for this server");
                }
                if (!isAllowableVirtualResourceType(type)) {
                    throw new FHIRVirtualResourceTypeException("The virtual resource type '" + type
                            + "' is not allowed. Allowable virtual types for this server are: " + getAllowableVirtualResourceTypes().toString());
                }
                resourceTypeName = "Basic";
            }
            
            Class<? extends Resource> resourceType = getResourceType(resourceTypeName);
            
            // Start a new txn in the persistence layer if one is not already active.
            txn = getTransaction();
            if (txn != null && !txn.isActive()) {
                txn.begin();
                txnStarted = true;
                log.fine("Started new transaction for 'read' operation.");
            }
            
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
            if (txnStarted) {
                log.fine("Committing transaction for 'read' operation.");
                txn.commit();
                txn = null;
                txnStarted = false;
            }
            status = Response.Status.OK;
            
            return resource;
        } catch (FHIRPersistenceResourceNotFoundException e) {
        	status = Response.Status.NOT_FOUND;
            throw e;
        } catch (FHIRPersistenceResourceDeletedException e) {
            status = Response.Status.GONE;
            throw e;
        } catch (FHIRException e) {
            log.log(Level.SEVERE, errMsg, e);
            status = e.getHttpStatus();
            throw e;
        } catch (Throwable t) {
            log.log(Level.SEVERE, errMsg, t);
            status = Response.Status.INTERNAL_SERVER_ERROR;
            throw t;
        } finally {
            // Restore the original request context.
            FHIRRequestContext.set(requestContext);
            
            // If we previously started a transaction and it's still active, we need to rollback due to an error.
            if (txnStarted) {
                rollback(txn, "read");
                txn = null;
                txnStarted = false;
            }
            RestAuditLogger.logRead(httpServletRequest, resource, startTime, new Date(), status);
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
    public Resource doVRead(String type, String id, String versionId, Map<String, String> requestProperties) throws Exception {
        log.entering(this.getClass().getName(), "doVRead");
        
        FHIRPersistenceTransaction txn = null;
        boolean txnStarted = false;
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
                    throw new FHIRVirtualResourceTypeException("The virtual resource types feature is not enabled for this server");
                }
                if (!isAllowableVirtualResourceType(type)) {
                    throw new FHIRVirtualResourceTypeException("The virtual resource type '" + type
                            + "' is not allowed. Allowable virtual resource types for this server are: " + getAllowableVirtualResourceTypes().toString());
                }
                resourceTypeName = "Basic";
            }
            
            Class<? extends Resource> resourceType = getResourceType(resourceTypeName);
            
            // Start a new txn in the persistence layer if one is not already active.
            txn = getTransaction();
            if (txn != null && !txn.isActive()) {
                txn.begin();
                txnStarted = true;
                log.fine("Started new transaction for 'vread' operation.");
            }
            
            // First, invoke the 'beforeVread' interceptor methods.
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(null, buildPersistenceEventProperties(type, id, versionId, requestProperties));
            getInterceptorMgr().fireBeforeVreadEvent(event);
            
            FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(event);
            resource = getPersistenceImpl().vread(persistenceContext, resourceType, id, versionId);
            if (resource == null) {
                throw new FHIRPersistenceResourceNotFoundException("Resource '" + resourceType.getSimpleName() + "/" + id + "' version " + versionId + " not found.");
            }
            
            event.setFhirResource(resource);

            // Invoke the 'afterVread' interceptor methods.
            getInterceptorMgr().fireAfterVreadEvent(event);
            
            // Commit our transaction if we started one before.
            if (txnStarted) {
                log.fine("Committing transaction for 'vread' operation.");
                txn.commit();
                txn = null;
                txnStarted = false;
            }
            status = Response.Status.OK;
            
            return resource;
        } catch (FHIRPersistenceResourceNotFoundException e) {
        	status = Response.Status.NOT_FOUND;
            throw e;
        } catch (FHIRPersistenceResourceDeletedException e) {
            status = Response.Status.GONE;
            throw e;
        } catch (FHIRException e) {
            log.log(Level.SEVERE, errMsg, e);
        	status = e.getHttpStatus();
            throw e;
        } catch (Throwable t) {
            log.log(Level.SEVERE, errMsg, t);
            status = Response.Status.INTERNAL_SERVER_ERROR;
            throw t;
        } finally {
            // Restore the original request context.
            FHIRRequestContext.set(requestContext);
            
            // If we previously started a transaction and it's still active, we need to rollback due to an error.
            if (txnStarted) {
                rollback(txn, "vread");
                txn = null;
                txnStarted = false;
            }
            RestAuditLogger.logVersionRead(httpServletRequest, resource, startTime, new Date(), status);
            
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
    public Bundle doHistory(String type, String id, MultivaluedMap<String, String> queryParameters, String requestUri, Map<String, String> requestProperties) throws Exception {
        log.entering(this.getClass().getName(), "doHistory");
        
        FHIRPersistenceTransaction txn = null;
        boolean txnStarted = false;
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
            
            // Start a new txn in the persistence layer if one is not already active.
            txn = getTransaction();
            if (txn != null && !txn.isActive()) {
                txn.begin();
                txnStarted = true;
                log.fine("Started new transaction for 'history' operation.");
            }
            
            // First, invoke the 'beforeHistory' interceptor methods.
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(null, buildPersistenceEventProperties(type, id, null, requestProperties));
            getInterceptorMgr().fireBeforeHistoryEvent(event);

            FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(event, historyContext);
            List<Resource> resources = getPersistenceImpl().history(persistenceContext, resourceType, id);
            bundle = createHistoryBundle(resources, historyContext);
            addLinks(historyContext, bundle, requestUri);
            
            event.setFhirResource(bundle);

            // Invoke the 'afterHistory' interceptor methods.
            getInterceptorMgr().fireAfterHistoryEvent(event);
            
            // Commit our transaction if we started one before.
            if (txnStarted) {
                log.fine("Committing transaction for 'history' operation.");
                txn.commit();
                txn = null;
                txnStarted = false;
            }
            status = Response.Status.OK;
            return bundle;
        } catch (FHIRException e) {
            log.log(Level.SEVERE, errMsg, e);
        	status = e.getHttpStatus();
            throw e;
        } catch (Throwable t) {
            log.log(Level.SEVERE, errMsg, t);
            status = Response.Status.INTERNAL_SERVER_ERROR;
            throw t;
        } finally {
            // Restore the original request context.
            FHIRRequestContext.set(requestContext);
            
            // If we previously started a transaction and it's still active, we need to rollback due to an error.
            if (txnStarted) {
                rollback(txn, "history");
                txn = null;
                txnStarted = false;
            }
            RestAuditLogger.logHistory(httpServletRequest, bundle, startTime, new Date(), status);
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
    public Bundle doSearch(String type, String compartment, String compartmentId, MultivaluedMap<String, String> queryParameters, String requestUri, Map<String, String> requestProperties, Resource contextResource) throws Exception {
        log.entering(this.getClass().getName(), "doSearch");
        
        FHIRPersistenceTransaction txn = null;
        boolean txnStarted = false;
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
                    throw new FHIRVirtualResourceTypeException("The virtual resource types feature is not enabled for this server");
                }
                if (!isAllowableVirtualResourceType(type)) {
                    throw new FHIRVirtualResourceTypeException("The virtual resource type '" + type + "' is not allowed. Allowable virtual resource types for this server are: " + getAllowableVirtualResourceTypes().toString());
                }
                resourceTypeName = "Basic";
                implicitSearchParameter = createBasicCodeSearchParameter(type);
            }
            
            Class<? extends Resource> resourceType = getResourceType(resourceTypeName);
            
            // Start a new txn in the persistence layer if one is not already active.
            txn = getTransaction();
            if (txn != null && !txn.isActive()) {
                txn.begin();
                txnStarted = true;
                log.fine("Started new transaction for 'search' operation.");
            }
            
            // First, invoke the 'beforeSearch' interceptor methods.
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(contextResource, buildPersistenceEventProperties(type, null, null, requestProperties));
            getInterceptorMgr().fireBeforeSearchEvent(event);
            
            FHIRSearchContext searchContext = SearchUtil.parseQueryParameters(compartment, compartmentId, resourceType, queryParameters, httpServletRequest.getQueryString());
            List<Parameter> searchParameters = searchContext.getSearchParameters();
            if (implicitSearchParameter != null) {
                searchParameters.add(implicitSearchParameter);
            }
            
            FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(event, searchContext);
            List<Resource> resources = getPersistenceImpl().search(persistenceContext, resourceType);
            bundle = createSearchBundle(resources, searchContext);
            if (requestUri != null) {
                addLinks(searchContext, bundle, requestUri);
            }
            event.setFhirResource(bundle);

            // Invoke the 'afterSearch' interceptor methods.
            getInterceptorMgr().fireAfterSearchEvent(event);
            
            // Commit our transaction if we started one before.
            if (txnStarted) {
                log.fine("Committing transaction for 'search' operation.");
                txn.commit();
                txn = null;
                txnStarted = false;
            }
            status = Response.Status.OK;
            
            return bundle;
        } catch (FHIRException e) {
            log.log(Level.SEVERE, errMsg, e);
        	status = e.getHttpStatus();
            throw e;
        } catch (Throwable t) {
            log.log(Level.SEVERE, errMsg, t);
            status = Response.Status.INTERNAL_SERVER_ERROR;
            throw t;
        } finally {
            // Restore the original request context.
            FHIRRequestContext.set(requestContext);
            
            // If we previously started a transaction and it's still active, we need to rollback due to an error.
            if (txnStarted) {
                rollback(txn, "search");
                txn = null;
                txnStarted = false;
            }
            RestAuditLogger.logSearch(httpServletRequest, queryParameters, bundle, startTime, new Date(), status);
            log.exiting(this.getClass().getName(), "doSearch");
        }
    }
    
    /**
     * Helper method which invokes a custom operation.
     * @param operationContext the FHIROperationContext associated with the request
     * @param resourceTypeName the resource type associated with the request
     * @param logicalId the resource logical id associated with the request
     * @param versionId the resource version id associated with the request
     * @param operationName the name of the custom operation to be invoked
     * @param resource the input resource associated with the custom operation to be invoked
     * @return a Resource that represents the response to the custom operation
     * @throws Exception
     */
    public Resource doInvoke(FHIROperationContext operationContext, String resourceTypeName, String logicalId, String versionId, String operationName,
        Resource resource, Map<String, String> requestProperties) throws Exception {
        log.entering(this.getClass().getName(), "doInvoke");

        Date startTime = new Date();
        Response.Status status = null;
        String errMsg = "Caught exception while processing 'invoke' request.";
        
        // Save the current request context.
        FHIRRequestContext requestContext = FHIRRequestContext.get();
        
        try {
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
        } catch (FHIRException e) {
            log.log(Level.SEVERE, errMsg, e);
            status = e.getHttpStatus();
            throw e;
        } catch (Throwable t) {
            log.log(Level.SEVERE, errMsg, t);
            status = Response.Status.INTERNAL_SERVER_ERROR;
            throw t;
       } finally {
            // Restore the original request context.
            FHIRRequestContext.set(requestContext);
            RestAuditLogger.logOperation(httpServletRequest, operationName, resourceTypeName, logicalId, versionId, startTime, new Date(), status);
            log.exiting(this.getClass().getName(), "doInvoke");
        }
    }

    /**
     * Processes a bundled request.
     * 
     * @param bundle
     *            the request Bundle
     * @return the response Bundle
     */
    public Bundle doBundle(Bundle bundle, Map<String, String> requestProperties) throws Exception {
        log.entering(this.getClass().getName(), "doBundle");

        Date startTime = new Date();
        Response.Status status = null;
        String errMsg = "Caught exception while processing 'bundle' request.";
        
        // Save the current request context.
        FHIRRequestContext requestContext = FHIRRequestContext.get();
        
        try {
            // First, validate the bundle and create the response bundle.
            Bundle responseBundle = validateBundle(bundle);
            
            // Next, process each of the entries in the bundle.
            processBundleEntries(bundle, responseBundle, requestProperties);
            status = Response.Status.OK;
            
            return responseBundle;
        } catch (FHIRException e) {
            log.log(Level.SEVERE, errMsg, e);
        	status = e.getHttpStatus();
            throw e;
        } catch (Throwable t) {
            log.log(Level.SEVERE, errMsg, t);
            status = Response.Status.INTERNAL_SERVER_ERROR;
            throw t;
        } finally {
            // Restore the original request context.
            FHIRRequestContext.set(requestContext);
            RestAuditLogger.logBundle(httpServletRequest, bundle, startTime, new Date(), status);
            log.exiting(this.getClass().getName(), "doBundle");
        }
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.rest.FHIRResourceHelpers#getTransaction()
     */
    @Override
    public FHIRPersistenceTransaction getTransaction() throws Exception {
        return getPersistenceImpl().getTransaction();
    }

    /**
     * Sets various properties on the FHIROperationContext instance.
     * @param operationContext the FHIROperationContext on which to set the properties
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
     * This method will perform a rollback for the specified REST API operation and then
     * log any exception caught as a result.
     * Note: we'll absorb any exception caught while doing the rollback because the assumption
     * is that the rollback is being performed due an exception that occurred during a commit, 
     * and we'll want to report that exception in the REST API response, not the exception that
     * resulted from the rollback.
     * 
     * @param txn the transaction to be rolled back
     * @param operation the operation being processed when the rollback was requested
     */
    private void rollback(FHIRPersistenceTransaction txn, String operation) {
        log.fine("Rolling back transaction for '" + operation + "' operation.");
        try {
            txn.rollback();
        } catch (Throwable t) {
            String msg = "Unexpected exception while trying to rollback a transaction";
            log.log(Level.SEVERE, msg, t);
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
                        
                    case DELETE:
                        // If the "delete" operation isn't supported by the configured persistence layer,
                        // then we need to fail validation of this bundle entry.
                        if (!isDeleteSupported()) {
                            throw new FHIRException("BundleEntry.request contains unsupported HTTP method: " + method.name());
                        }
                        if (resource != null) {
                            throw new FHIRException("BundleEntry.resource not allowed for BundleEntry with DELETE method.");
                        }
                        break;

                    default:
                        throw new FHIRException("BundleEntry.request contains unsupported HTTP method: " + method.name());
                    }

                    // If the request entry contains a resource, then validate it now.
                    if (resource != null) {
                        if (method == HTTPVerbList.PUT) {
                            if (resource.getId() == null || resource.getId().getValue() == null) {
                                throw new FHIRException("BundleEntry.resource must contain an id field for a PUT operation.");
                            }
                        }
                        
                        List<OperationOutcomeIssue> issues = FHIRValidator.getInstance().validate(resource, isUserDefinedSchematronEnabled());
                        if (!issues.isEmpty()) {
                            OperationOutcome oo = FHIRUtil.buildOperationOutcome(issues);
                            setBundleEntryResource(responseEntry, oo);
                            response.setStatus(objectFactory.createString().withValue(Integer.toString(SC_BAD_REQUEST)));
                            numErrors++;
                        }
                    }
                } catch (FHIRException e) {
                    setBundleEntryResource(responseEntry, FHIRUtil.buildOperationOutcome(e, false));
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
    private void processBundleEntries(Bundle requestBundle, Bundle responseBundle, Map<String, String> requestProperties) throws Exception {
        log.entering(this.getClass().getName(), "processBundleEntries");
        
        FHIRPersistenceTransaction txn = null;
        
        // Generate a request correlation id for this request bundle.
        bundleRequestCorrelationId = UUID.randomUUID().toString();
        log.fine("Processing request bundle, request-correlation-id=" + bundleRequestCorrelationId);
        
        try {
            // If we're working on a 'transaction' type interaction, then start a new transaction now
            // and sort the request bundle entries by their "url" field.
            if (responseBundle.getType().getValue() == BundleTypeList.TRANSACTION_RESPONSE) {
                bundleTransactionCorrelationId = bundleRequestCorrelationId;
                txn = getTransaction();
                txn.begin();
                log.fine("Started new transaction for transaction bundle, txn-correlation-id=" + bundleTransactionCorrelationId);
            } 
            
            Map<String, String> localRefMap = new HashMap<>();
            
            // Next, process entries in the correct order.
            processEntriesForMethod(requestBundle, responseBundle, HTTPVerbList.DELETE, txn != null, localRefMap, requestProperties);
            processEntriesForMethod(requestBundle, responseBundle, HTTPVerbList.POST, txn != null, localRefMap, requestProperties);
            processEntriesForMethod(requestBundle, responseBundle, HTTPVerbList.PUT, txn != null, localRefMap, requestProperties);
            processEntriesForMethod(requestBundle, responseBundle, HTTPVerbList.GET, txn != null, localRefMap, requestProperties);
            
            if (txn != null) {
                log.fine("Committing transaction for transaction bundle, txn-correlation-id=" + bundleTransactionCorrelationId);
                txn.commit();
                txn = null;
            }
            
        } finally {
            log.fine("Finished processing request bundle, request-correlation-id=" + bundleRequestCorrelationId);
            
            // Clear both correlation id fields since we're done processing the bundle.
            bundleRequestCorrelationId = null;
            bundleTransactionCorrelationId = null;
            
            if (txn != null) {
                rollback(txn, "transaction");
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
    private void processEntriesForMethod(Bundle requestBundle, Bundle responseBundle, HTTPVerbList httpMethod, boolean failFast,
        Map<String, String> localRefMap, Map<String, String> bundleRequestProperties) throws Exception {
        log.entering(this.getClass().getName(), "processEntriesForMethod", new Object[] {
                "httpMethod", httpMethod
        });
        try {
            // First, obtain a list of request entry indices for the entries that we'll process.
            // This list will contain the indices associated with only the entries for the specified http method.
            List<Integer> entryIndices = getBundleRequestIndicesForMethod(requestBundle, responseBundle, httpMethod);
            log.finer("Bundle request indices to be processed: " + entryIndices.toString());

            // Next, for PUT (update) requests, extract any local identifiers and resolve them ahead of time.
            // We do this to prevent any local reference problems from occurring due to our re-ordering
            // of the PUT request entries.
            if (httpMethod == HTTPVerbList.PUT) {
                log.finer("Pre-processing bundle request entries for PUT method...");
                for (Integer index : entryIndices) {
                    BundleEntry requestEntry = requestBundle.getEntry().get(index);

                    // Retrieve the local identifier from the request entry (if present).
                    String localIdentifier = retrieveLocalIdentifier(requestEntry, localRefMap);

                    // Since this is for a PUT request (update) we should be able to resolve the local identifier
                    // prior to processing the request since the resource's id must already be contained in the resource
                    // within the request entry.
                    if (localIdentifier != null) {
                        Resource resource = FHIRUtil.getResourceContainerResource(requestEntry.getResource());
                        addLocalRefMapping(localRefMap, localIdentifier, resource);
                    }
                }
            }

            // Next, for PUT and DELETE requests, we need to sort the indices by the request url path value.
            if (httpMethod == HTTPVerbList.PUT || httpMethod == HTTPVerbList.DELETE) {
                sortBundleRequestEntries(requestBundle, entryIndices);
                log.finer("Sorted bundle request indices to be processed: " + entryIndices.toString());
            }

            // Now visit each of the request entries using the list of indices obtained above.
            for (Integer entryIndex : entryIndices) {
                BundleEntry requestEntry = requestBundle.getEntry().get(entryIndex);
                BundleEntry responseEntry = responseBundle.getEntry().get(entryIndex);
                BundleRequest request = requestEntry.getRequest();
                BundleResponse response = responseEntry.getResponse();
                
                try {
                    FHIRUrlParser requestURL = new FHIRUrlParser(request.getUrl().getValue());

                    String path = requestURL.getPath();
                    String query = requestURL.getQuery();
                    if (log.isLoggable(Level.FINER)) {
                        log.finer("Processing bundle request entry " + entryIndex + "; method=" + request.getMethod().getValue().value() + ", url="
                                + request.getUrl().getValue());
                        log.finer("--> path: " + path);
                        log.finer("--> query: " + query);
                    }
                    String[] pathTokens = requestURL.getPathTokens();
                    MultivaluedMap<String, String> queryParams = requestURL.getQueryParameters();

                    // Construct the absolute requestUri to be used for any response bundles associated
                    // with history and search requests.
                    String absoluteUri = getAbsoluteUri(getRequestUri(), request.getUrl().getValue());

                    Map<String, String> requestProperties = extractRequestPropertiesFromExtensions(request.getExtension(), bundleRequestProperties);

                    switch (request.getMethod().getValue()) {
                    case GET: {
                        Resource resource = null;
                        int httpStatus = SC_OK;

                        // Process a GET (read, vread, history, search, etc.).
                        // Determine the type of request from the path tokens.
                        if (pathTokens.length == 1) {
                            // This is a 'search' request.
                            resource = doSearch(pathTokens[0], null, null, queryParams, absoluteUri, requestProperties, null);
                        } else if (pathTokens.length == 2) {
                            // This is a 'read' request.
                            resource = doRead(pathTokens[0], pathTokens[1], true, false, requestProperties, null);
                        } else if (pathTokens.length == 3) {
                            if (pathTokens[2].equals("_history")) {
                                // This is a 'history' request.
                                resource = doHistory(pathTokens[0], pathTokens[1], queryParams, absoluteUri, requestProperties);
                            } else {
                                // This is a compartment based search
                                resource = doSearch(pathTokens[2], pathTokens[0], pathTokens[1], queryParams, absoluteUri, requestProperties, null);
                            }
                        } else if (pathTokens.length == 4 && pathTokens[2].equals("_history")) {
                            // This is a 'vread' request.
                            resource = doVRead(pathTokens[0], pathTokens[1], pathTokens[3], requestProperties);
                            setBundleEntryResource(responseEntry, resource);
                        } else {
                            throw new FHIRException("Unrecognized path in request URL: " + path);
                        }

                        // Save the results of the operation in the bundle response field.
                        setBundleResponseStatus(response, httpStatus);
                        setBundleEntryResource(responseEntry, resource);
                    }
                        break;

                    case POST: {
                        // Process a POST (create).
                        if (pathTokens.length != 1) {
                            throw new FHIRException("Request URL for bundled POST request should have path part with exactly one token (<resourceType>).");
                        }

                        // Retrieve the local identifier from the request entry (if present).
                        String localIdentifier = retrieveLocalIdentifier(requestEntry, localRefMap);

                        // Retrieve the resource from the request entry.
                        Resource resource = FHIRUtil.getResourceContainerResource(requestEntry.getResource());

                        // Convert any local references found within the resource to their
                        // corresponding external reference.
                        processLocalReferences(resource, localRefMap);

                        // Perform the 'create' operation.
                        String ifNoneExist = request.getIfNoneExist() != null ? request.getIfNoneExist().getValue() : null;
                        FHIRRestOperationResponse ior = doCreate(pathTokens[0], resource, ifNoneExist, requestProperties);
                        setBundleResponseFields(responseEntry, ior.getResource(), ior.getLocationURI(), ior.getStatus().getStatusCode());

                        // Next, if a local identifier was present, we'll need to map this to the
                        // correct external identifier (e.g. Patient/12345).
                        addLocalRefMapping(localRefMap, localIdentifier, resource);
                    }
                        break;

                    case PUT: {
                        String type = null;
                        String id = null;
                        
                        // Process a PUT (update).
                        if (pathTokens.length == 1) {
                            // A single-part url would be a conditional update: <type>?<query>
                            type = pathTokens[0];
                            if (query == null || query.isEmpty()) {
                                throw new FHIRException("A search query string is required for a conditional update operation.");
                            }
                        } else if (pathTokens.length == 2) {
                            // A two-part url would be a normal update: <type>/<id>.
                            type = pathTokens[0];
                            id = pathTokens[1];
                        } else {
                            // A url with any other pattern is an error.
                            throw new FHIRException("Request URL for bundled PUT request should have path part with either one or two tokens (<resourceType> or <resourceType>/<id>).");
                        }

                        // Retrieve the resource from the request entry.
                        Resource resource = FHIRUtil.getResourceContainerResource(requestEntry.getResource());

                        // Convert any local references found within the resource to their
                        // corresponding external reference.
                        processLocalReferences(resource, localRefMap);

                        // Perform the 'update' operation.
                        String ifMatchBundleValue = null;
                        if (request.getIfMatch() != null) {
                            ifMatchBundleValue = request.getIfMatch().getValue();
                        }
                        FHIRRestOperationResponse ior = doUpdate(type, id, resource, ifMatchBundleValue, query, requestProperties);
                        setBundleResponseFields(responseEntry, ior.getResource(), ior.getLocationURI(), ior.getStatus().getStatusCode());
                    }
                        break;

                    case DELETE: {
                        String type = null;
                        String id = null;
                        
                        // Process a DELETE.
                        if (pathTokens.length == 1) {
                            // A single-part url would be a conditional delete: <type>?<query>
                            type = pathTokens[0];
                            if (query == null || query.isEmpty()) {
                                throw new FHIRException("A search query string is required for a conditional delete operation.");
                            }
                        } else if (pathTokens.length == 2) {
                            type = pathTokens[0];
                            id = pathTokens[1];
                        } else {
                            throw new FHIRException("Request URL for bundled DELETE request should have path part with one or two tokens (<resourceType> or <resourceType>/<id>).");
                        }

                        // Perform the 'delete' operation.
                        FHIRRestOperationResponse ior = doDelete(type, id, query, requestProperties);
                        setBundleResponseFields(responseEntry, ior.getResource(), null, ior.getStatus().getStatusCode());
                    }
                        break;

                    default:
                        // Internal error, should not get here!
                        throw new IllegalStateException("Internal Server Error: reached an unexpected code location.");
                    }
                } catch (FHIRRestException e) {
                    setBundleResponseStatus(response, e.getHttpStatus().getStatusCode());
                    setBundleEntryResource(responseEntry, (e.getOperationOutcome() == null) ? FHIRUtil.buildOperationOutcome(e, false)
                            : e.getOperationOutcome());
                    if (failFast) {
                        throw new FHIRRestBundledRequestException("Error while processing request bundle.", e.getOperationOutcome(), Response.Status.BAD_REQUEST, responseBundle, e);
                    }
                } catch (FHIRPersistenceResourceNotFoundException e) {
                    setBundleResponseStatus(response, SC_NOT_FOUND);
                    setBundleEntryResource(responseEntry, FHIRUtil.buildOperationOutcome(e, false));
                    if (failFast) {
                        throw new FHIRRestBundledRequestException("Error while processing request bundle.", FHIRUtil.buildOperationOutcome(e, false), Response.Status.NOT_FOUND, responseBundle, e);
                    }
                } catch (FHIRPersistenceResourceDeletedException e) {
                    setBundleResponseStatus(response, SC_GONE);
                    setBundleEntryResource(responseEntry, FHIRUtil.buildOperationOutcome(e, false));
                    if (failFast) {
                        throw new FHIRRestBundledRequestException("Error while processing request bundle.", FHIRUtil.buildOperationOutcome(e, false), Response.Status.GONE, responseBundle, e);
                    }
                } catch (FHIRException e) {
                    setBundleResponseStatus(response, e.getHttpStatus().getStatusCode());
                    setBundleEntryResource(responseEntry, FHIRUtil.buildOperationOutcome(e, false));
                    if (failFast) {
                        throw new FHIRRestBundledRequestException("Error while processing request bundle.", FHIRUtil.buildOperationOutcome(e, false), e.getHttpStatus(), responseBundle, e);
                    }
                }
            }
        } finally {
            log.exiting(this.getClass().getName(), "processEntriesForMethod");
        }
    }

    /**
     * This function will return the set of request properties specified via extensions belonging to the BundleRequest
     * plus any request properties associated with the "parent" bundle request.
     * The "parent" bundle request refers to the situation where a request bundle contains a request that is itself
     * a request bundle.   In this situation, the higher-level bundle request might have request properties that should be
     * pushed down and used by bundle requests within that high-level bundle request. 
     * @param extensions the list of extensions belonging to the BundleRequest
     * @param bundleRequestProperties request properties associated with a "parent" bundle request
     * @return
     */
    private Map<String, String> extractRequestPropertiesFromExtensions(List<Extension> extensions, Map<String, String> bundleRequestProperties)
        throws FHIRException {
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
                        if (ext.getValueString() != null) {
                            value = ext.getValueString().getValue();
                        }

                        if (value == null) {
                            throw new FHIRException("The valueString field is required for a BundleRequest extension entry that specifies '" + REQUEST_HEADER_URL
                                    + "' for the URL identifier.");
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
                            throw new FHIRException("The proper syntax for a 'http-request-header' valueString field is '<headerName>: <headerValue>'");
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
     * Returns a list of Integers that provide the indices of the bundle entries associated
     * with the specified http method.
     * @param requestBundle the request bundle
     * @param httpMethod the http method to look for
     * @return
     */
    private List<Integer> getBundleRequestIndicesForMethod(Bundle requestBundle, Bundle responseBundle, HTTPVerbList httpMethod) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < requestBundle.getEntry().size(); i++) {
            BundleEntry responseEntry = responseBundle.getEntry().get(i);
            BundleEntry requestEntry = requestBundle.getEntry().get(i);
            BundleRequest request = requestEntry.getRequest();
            BundleResponse response = responseEntry.getResponse();
            
            // If our response bundle doesn't already have a response status set (perhaps as a result of
            // a validation error), and this request entry's http method is the one we're looking for, 
            // then record the index in our list.
            if (response.getStatus() == null && request.getMethod().getValue().equals(httpMethod)) {
                indices.add(Integer.valueOf(i));
            }
        }
        return indices;
    }
    
    /**
     * This function sorts the request entries in the specified bundle, based on the path
     * part of the entry's 'url' field.
     * @param bundle the bundle containing the request entries to be sorted.
     * @return an array of Integer which provides the "sorted" ordering of request entry index values.
     */
    private void sortBundleRequestEntries(Bundle bundle, List<Integer> indices) {
        // Sort the list of indices based on the contents of their entries in the bundle.
        Collections.sort(indices, new BundleEntryComparator(bundle.getEntry()));
    }
    
    public static class BundleEntryComparator implements Comparator<Integer> {
        private List<BundleEntry> entries;
        
        public BundleEntryComparator(List<BundleEntry> entries) {
            this.entries = entries;
        }

        /* (non-Javadoc)
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare(Integer indexA, Integer indexB) {
            BundleEntry a = entries.get(indexA);
            BundleEntry b = entries.get(indexB);
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
     * This function converts the specified query string (a String) into an
     * equivalent MultivaluedMap<String,String> containing the query parameters
     * defined in the query string.
     * @param queryString the query string to be processed
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
     * @param entry the bundle entry
     * @return the bundle entry's 'url' field's path component
     */
    private static String getUrlPath(BundleEntry entry) {
        String path = null;
        BundleRequest request = entry.getRequest();
        if (request != null) {
            if (request.getUrl() != null && request.getUrl().getValue() != null) {
                FHIRUrlParser requestURL = new FHIRUrlParser(request.getUrl().getValue());
                path = requestURL.getPath();
            }
        }
        
        return path;
    }

    /**
     * This method will add a mapping to the local-to-external identifier map if 
     * the specified localIdentifier is non-null.
     * @param localRefMap the map containing the local-to-external identifier mappings
     * @param localIdentifier the localIdentifier previously obtained for the resource
     * @param resource the resource for which an external identifier will be built
     */
    private void addLocalRefMapping(Map<String, String> localRefMap, String localIdentifier, Resource resource) {
        if (localIdentifier != null) {
            String externalIdentifier = FHIRUtil.getResourceTypeName(resource) + "/" + resource.getId().getValue();
            localRefMap.put(localIdentifier, externalIdentifier);
            log.finer("Added local/ext identifier mapping: " 
                    + localIdentifier + " --> " + externalIdentifier);
        }
    }

    /**
     * This method will retrieve the local identifier associated with the specified bundle request entry,
     * or return null if the fullUrl field is not specified or doesn't contain a local identifier.
     * @param requestEntry the bundle request entry
     * @param localRefMap the Map containing the local-to-external reference mappings
     * @return
     */
    private String retrieveLocalIdentifier(BundleEntry requestEntry, Map<String, String> localRefMap) throws Exception {
        String localIdentifier = null;
        if (requestEntry.getFullUrl() != null) {
            String fullUrl = requestEntry.getFullUrl().getValue();
            if (fullUrl != null && fullUrl.startsWith(LOCAL_REF_PREFIX)) {
                localIdentifier = fullUrl;
                log.finer("Request entry contains local identifier: " + localIdentifier);
                if (localRefMap.get(localIdentifier) != null) {
                    throw new FHIRException("Duplicate local identifier encountered in bundled request entry: " + localIdentifier);
                }
            }
        }
        return localIdentifier;
    }

    /**
     * This method will look for all fields of type Reference within the specfied resource,
     * and for each one that it finds it will check to see if it holds a local reference.
     * If so, the appropriate external reference will be substituted for it.
     * @param resource the resource whose local references will be updated
     * @param localRefMap the Map containing the local-to-external identifier mappings
     */
    private void processLocalReferences(Resource resource, Map<String, String> localRefMap) throws Exception {
        
        // Retrieve all fields of type Reference from the specified resource.
        List<Reference> references = ReferenceFinder.getReferences(resource);
        
        for (Reference ref : references) {
            String refValue = ref.getReference().getValue();
            if (refValue.startsWith(LOCAL_REF_PREFIX)) {
                String externalRef = localRefMap.get(refValue);
                if (externalRef == null) {
                    throw new FHIRException("Local reference '" + refValue + "' is undefined in the request bundle.");
                }
                ref.setReference(objectFactory.createString().withValue(externalRef));
                log.finer("Convert local ref '" + refValue + "' to external ref '" + externalRef + "'.");
            }
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
        if (resource != null) {
            response.setId(resource.getId().getValue());
            response.setLastModified(resource.getMeta().getLastUpdated());
            response.setEtag(objectFactory.createString().withValue(getEtagValue(resource)));
            setBundleEntryResource(responseEntry, resource);
        }
        if (locationURI != null) {
            response.setLocation(objectFactory.createUri().withValue(locationURI.toString()));
        }
    }

    private void setBundleResponseStatus(BundleResponse response, int httpStatus) {
        response.setStatus(objectFactory.createString().withValue(Integer.toString(httpStatus)));
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
            StringBuilder sb = new StringBuilder();
            sb.append(e.getMessage() != null ? e.getMessage() : "<exception message not present>");
            sb.append("\nOperationOutcome:\n").append(serializeOperationOutcome(e.getOperationOutcome()));
            log.log(Level.SEVERE, sb.toString());
            response = Response.status(e.getHttpStatus()).entity(e.getOperationOutcome()).build();
        } else {
            response = exceptionResponse(e, e.getHttpStatus());
        }
        
        return response;
    }

    private Response exceptionResponse(FHIROperationException e) {
        Response response;
        if (e.getOperationOutcome() != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(e.getMessage() != null ? e.getMessage() : "<exception message not present>");
            sb.append("\nOperationOutcome:\n").append(serializeOperationOutcome(e.getOperationOutcome()));
            log.log(Level.SEVERE, sb.toString());
            response = Response.status(e.getHttpStatus()).entity(e.getOperationOutcome()).build();
        } else {
            response = exceptionResponse(e, e.getHttpStatus());
        }
        
        return response;
    }
    
    private Response exceptionResponse(FHIRRestBundledRequestException e) {
        Response response;
        if (e.getResponseBundle() != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(e.getMessage() != null ? e.getMessage() : "<exception message not present>");
            if (e.getOperationOutcome() != null) {
                sb.append("\nOperationOutcome:\n").append(serializeOperationOutcome(e.getOperationOutcome()));
            }
            log.log(Level.SEVERE, sb.toString());
            response = Response.status(e.getHttpStatus()).entity(e.getResponseBundle()).build() ;
        } else {
           response = exceptionResponse(e, e.getHttpStatus()) ;
        }
        return response;
    }
    
    private Response exceptionResponse(FHIRException e) {
        return exceptionResponse(e, e.getHttpStatus());
    }
    
    private Response exceptionResponse(FHIRException e, Status status) {
        return exceptionResponse(e, status, true);
    }
    
    private Response exceptionResponse(FHIRException e, Status status, boolean logMsg) {
        String msg = e.getMessage() != null ? e.getMessage() : "<exception message not present>";
        log.log(Level.SEVERE, msg);
        return Response.status(status).entity(FHIRUtil.buildOperationOutcome(e, false)).build();
    }
    
    private Response exceptionResponse(Exception e, Status status) {
        String msg = e.getMessage() != null ? e.getMessage() : "<exception message not present>";
        log.log(Level.SEVERE, msg, e);
        return this.exceptionResponse(new FHIRException(e), status, false);
    }
    
    private String serializeOperationOutcome(OperationOutcome oo) {
        try {
            StringWriter sw = new StringWriter();
            FHIRUtil.write(oo, Format.JSON, sw);
            return sw.toString();
        } catch (Throwable t) {
            return "Error encountered while serializing OperationOutcome resource: " + t.getMessage();
        }
    }
    
    private synchronized Conformance getConformanceStatement() throws Exception {
        try {
            Conformance conformance = buildConformanceStatement();
            conformance.withDate(objectFactory.createDateTime().withValue(new Date().toString()));
            return conformance;
        } catch (Throwable t) {
            String msg = "An error occurred while constructing the Conformance statement.";
            log.log(Level.SEVERE, msg, t);
            throw new FHIRRestException(msg, null, Status.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Builds a Conformance resource instance which describes this server.
     * @throws Exception 
     */
    private Conformance buildConformanceStatement() throws Exception {
        // Build the list of interactions that are supported for each resource type.
        
        List<ConformanceInteraction> interactions = new ArrayList<>();
        interactions.add(buildConformanceInteraction(TypeRestfulInteractionList.CREATE));
        interactions.add(buildConformanceInteraction(TypeRestfulInteractionList.UPDATE));
        if (isDeleteSupported()) {
            interactions.add(buildConformanceInteraction(TypeRestfulInteractionList.DELETE));
        }
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
                    .withConditionalCreate(objectFactory.createBoolean().withValue(true))
                    .withConditionalUpdate(objectFactory.createBoolean().withValue(true))
                    .withConditionalDelete(objectFactory.createConditionalDeleteStatus().withValue(ConditionalDeleteStatusList.SINGLE))
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
        Conformance conformance = objectFactory.createConformance()
                .withFormat(
                    objectFactory.createCode().withValue(MediaType.APPLICATION_JSON), 
                    objectFactory.createCode().withValue(MediaType.APPLICATION_JSON_FHIR), 
                    objectFactory.createCode().withValue(MediaType.APPLICATION_XML),
                    objectFactory.createCode().withValue(MediaType.APPLICATION_XML_FHIR))
                .withVersion(objectFactory.createString().withValue(buildInfo.getBuildVersion()))
                .withFhirVersion(objectFactory.createId().withValue(FHIR_SPEC_VERSION))
                .withName(objectFactory.createString().withValue(FHIR_SERVER_NAME))
                .withDescription(objectFactory.createString().withValue(buildDescription))
                .withCopyright(objectFactory.createString().withValue("(c) Copyright IBM Corporation 2016, 2017"))
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
        extension.setUrl(EXTENSION_URL + "/defaultTenantId");
        extension.setValueString(string(fhirConfig.getStringProperty(FHIRConfiguration.PROPERTY_DEFAULT_TENANT_ID, FHIRConfiguration.DEFAULT_TENANT_ID)));
        conformance.getExtension().add(extension);
        
        extension = objectFactory.createExtension();
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

    /**
     * Creates a bundle that will hold results for a search operation.
     * @param resources the list of resources to include in the bundle
     * @param searchContext the FHIRSearchContext object associated with the search
     * @return the bundle
     * @throws FHIRException
     */
    private Bundle createSearchBundle(List<Resource> resources, FHIRSearchContext searchContext) throws FHIRException {
        Bundle bundle = objectFactory.createBundle().withType(objectFactory.createBundleType().withValue(BundleTypeList.SEARCHSET));

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
        bundle.setTotal(objectFactory.createUnsignedInt().withValue(BigInteger.valueOf(searchContext.getTotalCount())));

        return bundle;
    }
    
    /**
     * Creates a bundle that will hold the results of a history operation.
     * @param resources the list of resources to include in the bundle
     * @param historyContext the FHIRHistoryContext associated with the history operation
     * @return the bundle
     */
    private Bundle createHistoryBundle(List<Resource> resources, FHIRHistoryContext historyContext) throws FHIRException {
        Bundle bundle = objectFactory.createBundle().withType(objectFactory.createBundleType().withValue(BundleTypeList.HISTORY));

        // generate ID for this bundle
        bundle.setId(id(UUID.randomUUID().toString()));
        
        Map<String, List<Integer>> deletedResourcesMap = historyContext.getDeletedResources();

        for (int i = 0; i < resources.size(); i++) {
            Resource resource = resources.get(i);
            BundleEntry entry = objectFactory.createBundleEntry();

            Integer versionId = Integer.valueOf(resource.getMeta().getVersionId().getValue());
            String logicalId = resource.getId().getValue();
            String resourceType = FHIRUtil.getResourceTypeName(resource);
            List<Integer> deletedVersions = deletedResourcesMap.get(logicalId);
            
            // Determine the correct method to include in this history entry (POST, PUT, DELETE).
            HTTPVerbList method;
            if (deletedVersions != null && deletedVersions.contains(versionId)) {
                method = HTTPVerbList.DELETE;
            } else if (versionId == 1) {
                method = HTTPVerbList.POST;
            } else {
                method = HTTPVerbList.PUT;
            }
            
            // Create the 'request' entry
            BundleRequest request = objectFactory.createBundleRequest();
            entry.setRequest(request);
            request.setMethod(objectFactory.createHTTPVerb().withValue(method));
            
            // Set the request.url field.
            if (method == HTTPVerbList.POST) {
                // 'create' --> url = "<resourceType>"
                request.setUrl(objectFactory.createUri().withValue(resourceType));
            } else {
                // 'update'/'delete' --> url = "<resourceType>/<logicalId>"
                request.setUrl(objectFactory.createUri().withValue(resourceType + "/" + logicalId));
            }
            
            // Set the resource.
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
        bundle.setTotal(objectFactory.createUnsignedInt().withValue(BigInteger.valueOf(historyContext.getTotalCount())));

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
        return getAllowableVirtualResourceTypes().contains(virtualResourceType) || 
                getAllowableVirtualResourceTypes().contains("*");
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
        Parameter basicCodeSearchParameter = new Parameter(Parameter.Type.TOKEN, "code", null, null);
        ParameterValue value = new ParameterValue();
        value.setValueCode(type);
        value.setValueSystem(BASIC_RESOURCE_TYPE_URL);
        basicCodeSearchParameter.getValues().add(value);
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
     * @throws FHIRPersistenceException 
     */
    private Map<String, Object> buildPersistenceEventProperties(String type, String id, String version, Map<String, String> requestProperties) throws FHIRPersistenceException {
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
     * This method returns the equivalent of: uriInfo.getRequestUri().toString()
     * This method is necessary to provide a workaround to a bug in uriInfo.getRequestUri() where an IllegalArgumentException is thrown by
     * getRequestUri() when the query string portion contains a vertical bar | character. The vertical bar is one known case of a special
     * character causing the exception. There could be others.
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
     * This method returns the "base URI" associated with the current request.
     * For example, if a client invoked POST https://myhost:9443/fhir-server/api/v1/Patient to create a Patient resource,
     * this method would return "https://myhost:9443/fhir-server/api/v1".
     * @return The base endpoint URI associated with the current request.
     */
    private String getRequestBaseUri() {
        StringBuilder sb = new StringBuilder();
        sb.append(httpServletRequest.getScheme())
            .append("://")
            .append(httpServletRequest.getServerName())
            .append(":")
            .append(httpServletRequest.getServerPort())
            .append(httpServletRequest.getContextPath());
        String servletPath = httpServletRequest.getServletPath();
        if (servletPath != null && !servletPath.isEmpty()) {
            sb.append(servletPath);
        }
        
        return sb.toString();
    }
    
    /**
     * This method simply returns a URI object containing the specified URI string.
     * @param uriString the URI string for which the URI object will be created
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
