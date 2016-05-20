/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.resources;

import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.getResourceType;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.id;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_METHOD_NOT_ALLOWED;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import com.ibm.watsonhealth.fhir.core.FHIRUtilities;
import com.ibm.watsonhealth.fhir.core.MediaType;
import com.ibm.watsonhealth.fhir.exception.FHIRException;
import com.ibm.watsonhealth.fhir.model.Bundle;
import com.ibm.watsonhealth.fhir.model.BundleEntry;
import com.ibm.watsonhealth.fhir.model.BundleTypeList;
import com.ibm.watsonhealth.fhir.model.Conformance;
import com.ibm.watsonhealth.fhir.model.ConformanceStatementKindList;
import com.ibm.watsonhealth.fhir.model.IssueSeverityList;
import com.ibm.watsonhealth.fhir.model.IssueTypeList;
import com.ibm.watsonhealth.fhir.model.NarrativeStatusList;
import com.ibm.watsonhealth.fhir.model.ObjectFactory;
import com.ibm.watsonhealth.fhir.model.OperationOutcome;
import com.ibm.watsonhealth.fhir.model.OperationOutcomeIssue;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.model.ResourceContainer;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.notification.FHIRNotificationService;
import com.ibm.watsonhealth.fhir.notification.util.FHIRNotificationEvent;
import com.ibm.watsonhealth.fhir.persistence.FHIRPersistence;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceResourceNotFoundException;
import com.ibm.watsonhealth.fhir.persistence.helper.FHIRPersistenceHelper;
import com.ibm.watsonhealth.fhir.persistence.interceptor.FHIRPersistenceEvent;
import com.ibm.watsonhealth.fhir.persistence.interceptor.impl.FHIRPersistenceInterceptorMgr;
import com.ibm.watsonhealth.fhir.search.Parameter;
import com.ibm.watsonhealth.fhir.search.exception.FHIRSearchException;
import com.ibm.watsonhealth.fhir.search.util.SearchUtil;
import com.ibm.watsonhealth.fhir.server.FHIRBuildIdentifier;
import com.ibm.watsonhealth.fhir.validation.Validator;

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
    
    private static final String NL = System.getProperty("line.separator");
    
    private static final String FHIR_SERVER_NAME = "IBM Watson Health Cloud FHIR Server";
    private static final String FHIR_SPEC_VERSION = "1.0.2";
    private boolean sendNotification = true;
    

    private Validator validator = null;
    private FHIRPersistenceHelper persistenceHelper = null;
    private FHIRPersistenceInterceptorMgr interceptorMgr = null;
    private FHIRPersistence persistence = null;
    private ObjectFactory objectFactory = new ObjectFactory();

    @Context
    private ServletContext context;
    
    @Context
    private UriInfo uriInfo;
    
    @Context
    private HttpHeaders httpHeaders;
    
    @Context
    private SecurityContext securityContext;

    public FHIRResource() {
        log.finest("In FHIRResource() ctor. handle=" + FHIRUtilities.getObjectHandle(this));
        log.finest(FHIRUtilities.getCurrentStacktrace());
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
        
        try {
            return Response.ok().entity(buildConformanceStatement()).build();
        } catch(Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
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

        log.entering(this.getClass().getName(), "create(Resource)", "this=" + FHIRUtilities.getObjectHandle(this));

        try {
            String resourceType = resource.getClass().getSimpleName();
            if (!resourceType.equals(type) && !"Basic".equals(resourceType)) {
                throw new FHIRException("Resource type '" + resourceType + "' does not match type specified in request URI: " + type);
            }
            
            // A new resource should not contain an ID.
            if (resource.getId() != null) {
                throw new FHIRException("A 'create' operation cannot be performed on a resource that contains an 'id' attribute.");
            }
            
            // Validate the input resource and return any validation errors.
            List<String> messages = getValidator().validate(resource);
            if (!messages.isEmpty()) {
                OperationOutcome operationOutcome = buildOperationOutcome(messages);
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(operationOutcome)
                        .build();
            }
            
            // If there were no validation errors, then create the resource and return the location header.
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(resource, buildPersistenceEventProperties());
            getInterceptorMgr().fireBeforeCreateEvent(event);
            
            getPersistenceImpl().create(resource);
            
            getInterceptorMgr().fireAfterCreateEvent(event);
            
            ResponseBuilder response = Response.created(buildLocationURI(type, resource));
            response = addHeaders(response, resource);
            
            // for now add boolean check ..so it can control later on with some kind of external parameter
            if (sendNotification) {
                // Send out the notification
                FHIRNotificationService.getInstance().publish(buildNotificationEvent("create", buildLocationURI(type, resource).toString(), resource));
            }
            return response.build();
        } catch (FHIRException e) {
            return exceptionResponse(e, Response.Status.BAD_REQUEST);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "create(Resource)", "this=" + FHIRUtilities.getObjectHandle(this));
        }
    }

    /**
     * Builds a collection of properties that will be passed to the persistence interceptors.
     */
    private Map<String, Object> buildPersistenceEventProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put(FHIRPersistenceEvent.PROPNAME_URI_INFO, uriInfo);
        props.put(FHIRPersistenceEvent.PROPNAME_HTTP_HEADERS, httpHeaders);
        props.put(FHIRPersistenceEvent.PROPNAME_SECURITY_CONTEXT, securityContext);
        return props;
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
    public Response update(
        @ApiParam(value = "The type of the resource to be updated.", required = true)
        @PathParam("type") String type, 
        @ApiParam(value = "The id of the resource to be updated.", required = true)
        @PathParam("id") String id, 
        @ApiParam(value = "The new contents of the resource to be updated.", required = true)
        Resource resource) {

        log.entering(this.getClass().getName(), "update(String,Resource)", "this=" + FHIRUtilities.getObjectHandle(this));

        try {
            
            // Validate the input resource and return any validation errors.
            List<String> messages = getValidator().validate(resource);
            if (!messages.isEmpty()) {
                OperationOutcome operationOutcome = buildOperationOutcome(messages);
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(operationOutcome)
                        .build();
            }
            
            // Make sure the resource has an 'id' attribute and that it matches the input 'id' parameter.
            if (resource.getId() == null) {
                throw new FHIRException("Input resource must contain an 'id' attribute.");
            } else if (!resource.getId().getValue().equals(id)) {
                throw new FHIRException("Input resource 'id' attribute must match 'id' parameter.");
            }
            
            // If there were no validation errors, then create the resource and return the location header.
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(resource, buildPersistenceEventProperties());
            getInterceptorMgr().fireBeforeUpdateEvent(event);
            
            getPersistenceImpl().update(id, resource);
            
            getInterceptorMgr().fireAfterUpdateEvent(event);

            ResponseBuilder response = Response.ok().header(HttpHeaders.LOCATION, buildLocationURI(type, resource));
            response = addHeaders(response, resource);
            if (sendNotification) {
                // Send out the notification
                FHIRNotificationService.getInstance().publish(buildNotificationEvent("update", buildLocationURI(type, resource).toString(), resource));
            }
            return response.build();
        } catch (FHIRPersistenceResourceNotFoundException e) {
            return exceptionResponse(e, Response.Status.METHOD_NOT_ALLOWED);
        } catch (FHIRException e) {
            return exceptionResponse(e, Response.Status.BAD_REQUEST);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
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
        try {
            String resourceTypeName = type;
            if (!FHIRUtil.isStandardResourceType(type)) {
                resourceTypeName = "Basic";
            }
//          Class<? extends Resource> resourceType = getResourceType(type);
            Class<? extends Resource> resourceType = getResourceType(resourceTypeName);
            Resource resource = getPersistenceImpl().read(resourceType, id);
            if (resource == null) {
//              throw new FHIRPersistenceResourceNotFoundException("Resource '" + resourceType.getSimpleName() + "/" + id + "' not found.");
                throw new FHIRPersistenceResourceNotFoundException("Resource '" + type + "/" + id + "' not found.");
            }
            ResponseBuilder response = Response.ok().entity(resource);
            response = addHeaders(response, resource);
            return response.build();
        } catch (FHIRPersistenceResourceNotFoundException e) {
            return exceptionResponse(e, Response.Status.NOT_FOUND);
        } catch (FHIRException e) {
            return exceptionResponse(e, Response.Status.BAD_REQUEST);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
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
        try {
            String resourceTypeName = type;
            if (!FHIRUtil.isStandardResourceType(type)) {
                resourceTypeName = "Basic";
            }
//          Class<? extends Resource> resourceType = getResourceType(type);
            Class<? extends Resource> resourceType = getResourceType(resourceTypeName);
            Resource resource = getPersistenceImpl().vread(resourceType, id, vid);
            if (resource == null) {
                throw new FHIRPersistenceResourceNotFoundException("Resource '" + resourceType.getSimpleName() + "/" + id + "' version " + vid + " not found.");
            }
            ResponseBuilder response = Response.ok().entity(resource);
            response = addHeaders(response, resource);
            return response.build();
        } catch (FHIRPersistenceResourceNotFoundException e) {
            return exceptionResponse(e, Response.Status.NOT_FOUND);
        } catch (FHIRException e) {
            return exceptionResponse(e, Response.Status.BAD_REQUEST);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
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
        try {
            String resourceTypeName = type;
            if (!FHIRUtil.isStandardResourceType(type)) {
                resourceTypeName = "Basic";
            }
//          Class<? extends Resource> resourceType = getResourceType(type);
            Class<? extends Resource> resourceType = getResourceType(resourceTypeName);
            List<Resource> resources = getPersistenceImpl().history(resourceType, id);
            Bundle bundle = createBundle(resources, BundleTypeList.HISTORY);
            return Response.ok(bundle).build();
        } catch (FHIRPersistenceException e) {
            return exceptionResponse(e, Response.Status.BAD_REQUEST);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
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
        @PathParam("type") String type, 
        @Context UriInfo uriInfo) {
        log.entering(this.getClass().getName(), "search(String,UriInfo)", "this=" + FHIRUtilities.getObjectHandle(this));
        try {
            String resourceTypeName = type;
            if (!FHIRUtil.isStandardResourceType(type)) {
                resourceTypeName = "Basic";
            }
//          Class<? extends Resource> resourceType = getResourceType(type);
            Class<? extends Resource> resourceType = getResourceType(resourceTypeName);
            Map<String, List<String>> queryParameters = uriInfo.getQueryParameters();
            List<Parameter> searchParameters = SearchUtil.parseQueryParameters(resourceType, queryParameters);
            List<Resource> resources = getPersistenceImpl().search(resourceType, searchParameters);
            Bundle bundle = createBundle(resources, BundleTypeList.SEARCHSET);
            return Response.ok(bundle).build();
        } catch (FHIRSearchException | FHIRPersistenceException e) {
            return exceptionResponse(e, Response.Status.BAD_REQUEST);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "search(String,UriInfo)", "this=" + FHIRUtilities.getObjectHandle(this));
        }
    }
    
    /**
     * Adds the Etag and Last-Modified headers to the specified response object.
     */
    private ResponseBuilder addHeaders(ResponseBuilder rb, Resource resource) {
        return rb.header(HttpHeaders.ETAG, "W/\"" + resource.getMeta().getVersionId().getValue() + "\"")
                .header(HttpHeaders.LAST_MODIFIED, resource.getMeta().getLastUpdated().getValue().toXMLFormat());
    }

    /**
     * Build an OperationOutcome that contains the specified list of validation messages.
     */
    private OperationOutcome buildOperationOutcome(List<String> messages) {
        // First, build the list of issues from the input messages.
        List<OperationOutcomeIssue> ooiList = new ArrayList<>();
        for (String msg : messages) {
            log.fine("Validation error: " + msg);
            OperationOutcomeIssue ooi = objectFactory.createOperationOutcomeIssue()
                    .withCode(objectFactory.createIssueType().withValue(IssueTypeList.STRUCTURE))
                    .withSeverity(objectFactory.createIssueSeverity().withValue(IssueSeverityList.ERROR))
                    .withDiagnostics(objectFactory.createString().withValue(msg));
            ooiList.add(ooi);
        }
        
        // Next, build the OperationOutcome.
        OperationOutcome oo = objectFactory.createOperationOutcome()
                .withId(objectFactory.createId().withValue("validationfail"))
                .withText(objectFactory.createNarrative()
                    .withStatus(objectFactory.createNarrativeStatus().withValue(NarrativeStatusList.GENERATED)))
                .withIssue(ooiList);
        return oo;
    }

    /**
     * Build an OperationOutcome for the specified exception.
     */
    private OperationOutcome buildOperationOutcome(Exception exception) {
        // First, build a set of exception messages to be included in the OperationOutcome.
        // We'll include the exception message from each exception in the hierarchy, 
        // following the "causedBy" exceptions.
        StringBuilder msgs = new StringBuilder();
        Throwable e = exception;
        String causedBy = "";
        while (e != null) {
            msgs.append(causedBy + e.getClass().getSimpleName() + ": " + (e.getMessage() != null ? e.getMessage() : "<null message>"));
            e = e.getCause();
            causedBy = NL + "Caused by: ";
        }
        
        log.fine("Building OperationOutcome for exception: " + msgs);
        
        // Build an OperationOutcomeIssue that contains the exception messages.
        OperationOutcomeIssue ooi = objectFactory.createOperationOutcomeIssue()
                .withCode(objectFactory.createIssueType().withValue(IssueTypeList.EXCEPTION))
                .withSeverity(objectFactory.createIssueSeverity().withValue(IssueSeverityList.FATAL))
                .withDiagnostics(objectFactory.createString().withValue(msgs.toString()));
        
        // Next, build the OperationOutcome.
        OperationOutcome oo = objectFactory.createOperationOutcome()
                .withId(objectFactory.createId().withValue("exception"))
                .withText(objectFactory.createNarrative()
                    .withStatus(objectFactory.createNarrativeStatus().withValue(NarrativeStatusList.GENERATED)))
                .withIssue(ooi);
        return oo;
    }
    
    private Response exceptionResponse(Exception e, Status status) {
        return Response.status(status).entity(buildOperationOutcome(e)).build();
    }
    
    /**
     * Builds a Conformance resource instance which describes this server.
     */
    private Resource buildConformanceStatement() {
        FHIRBuildIdentifier buildInfo = new FHIRBuildIdentifier();
        
        String buildDescription = FHIR_SERVER_NAME + " version " + buildInfo.getBuildVersion()
            + " build id " + buildInfo.getBuildId() + "";
        
        ObjectFactory of = new ObjectFactory();
        
        // TODO - we need to fill out more of the Conformance resource.
        Conformance conformance = of.createConformance()
                .withDate(of.createDateTime().withValue(new Date().toString()))
                .withFormat(
                    of.createCode().withValue(MediaType.APPLICATION_JSON), 
                    of.createCode().withValue(MediaType.APPLICATION_JSON_FHIR), 
                    of.createCode().withValue(MediaType.APPLICATION_XML),
                    of.createCode().withValue(MediaType.APPLICATION_XML_FHIR))
                .withVersion(of.createString().withValue(buildInfo.getBuildVersion()))
                .withFhirVersion(of.createId().withValue(FHIR_SPEC_VERSION))
                .withName(of.createString().withValue(FHIR_SERVER_NAME))
                .withDescription(of.createString().withValue(buildDescription))
                .withCopyright(of.createString().withValue("© Copyright IBM Corporation 2016"))
                .withPublisher(of.createString().withValue("IBM Corporation"))
                .withKind(of.createConformanceStatementKind().withValue(ConformanceStatementKindList.INSTANCE))
                .withSoftware(
                    of.createConformanceSoftware()
                        .withName(of.createString().withValue(FHIR_SERVER_NAME))
                        .withVersion(of.createString().withValue(buildInfo.getBuildVersion()))
                        .withId(buildInfo.getBuildId()))
                ;
        
        return conformance;
    }
    
    /**
     * Builds a relative "Location" header value for the specified resource.
     * This will be a string of the form "<resource-type>/<id>/_history/<version>".
     * Note that the server will turn this into an absolute URL prior to returning it to the client.
     * @param resource the resource for which the location header value should be returned
     */
    private URI buildLocationURI(String type, Resource resource) {
        String resourceTypeName = resource.getClass().getSimpleName();
        if (!resourceTypeName.equals(type)) {
            resourceTypeName = type;
        }
        return URI.create(
//          resource.getClass().getSimpleName() + "/"
            resourceTypeName + "/"
                    + resource.getId().getValue() + "/_history/"
                    + resource.getMeta().getVersionId().getValue());
    }

    private Bundle createBundle(List<Resource> resources, BundleTypeList type) {
        Bundle bundle = objectFactory.createBundle().withType(objectFactory.createBundleType().withValue(type));
        
        // generate ID for this bundle
        bundle.setId(id(UUID.randomUUID().toString()));

        for (Resource resource : resources) {
            Class<? extends Resource> resourceType = resource.getClass();
            BundleEntry entry = objectFactory.createBundleEntry();
            ResourceContainer container = objectFactory.createResourceContainer();
            entry.setResource(container);
            Method method;
            try {
                method = ResourceContainer.class.getMethod("set" + resourceType.getSimpleName(), resourceType);
                method.invoke(container, resource);
            } catch (Exception e) {
                e.printStackTrace();
            }
            bundle.getEntry().add(entry);
        }
        
        // Finally, set the "total" field.
        bundle.setTotal(
            objectFactory.createUnsignedInt()
//              .withValue(BigInteger.valueOf(bundle.getEntry().size())));
                .withValue(BigInteger.valueOf(resources.size())));
        
        return bundle;
    }

    /**
     * Retrieves the shared Validator instance from the servlet context.
     */
    private Validator getValidator() {
        if (validator == null) {
            validator = (Validator) context.getAttribute(Validator.class.getName());
            log.fine("Retrieved Validator instance from servlet context: " + validator);
        }
        return validator;
    }

    /**
     * Retrieves the shared interceptor mgr instance from the servlet context.
     */
    private FHIRPersistenceInterceptorMgr getInterceptorMgr() {
        if (interceptorMgr == null) {
            interceptorMgr = (FHIRPersistenceInterceptorMgr) context.getAttribute(FHIRPersistenceInterceptorMgr.class.getName());
            log.fine("Retrieved FHIRPersistenceInterceptorMgr instance from servlet context: " + interceptorMgr);
        }
        return interceptorMgr;
    }

    /**
     * Retrieves the shared persistence helper object from the servlet context.
     */
    private synchronized FHIRPersistenceHelper getPersistenceHelper() {
        if (persistenceHelper == null) {
            persistenceHelper = (FHIRPersistenceHelper) context.getAttribute(FHIRPersistenceHelper.class.getName());
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
    
    /**
     * 
     * @return
     */
    private FHIRNotificationEvent buildNotificationEvent(String type, String location, Resource resource) {
        try {
            FHIRNotificationEvent event = new FHIRNotificationEvent();
            event.setOperationType(type);
            event.setLastUpdated(resource.getMeta().getLastUpdated().getValue().toString());
            event.setLocation(location);
            event.setResourceId(resource.getId().getValue());
            return event;
        } catch (Exception e) {
            log.log(Level.WARNING, this.getClass().getName() + ": unable to build notification event", e);
        }
        return null;
    }
}
