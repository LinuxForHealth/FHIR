/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.resources;

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
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

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
import com.ibm.watsonhealth.fhir.model.Conformance;
import com.ibm.watsonhealth.fhir.model.ConformanceStatementKindList;
import com.ibm.watsonhealth.fhir.model.HTTPVerbList;
import com.ibm.watsonhealth.fhir.model.IssueSeverityList;
import com.ibm.watsonhealth.fhir.model.IssueTypeList;
import com.ibm.watsonhealth.fhir.model.NarrativeStatusList;
import com.ibm.watsonhealth.fhir.model.ObjectFactory;
import com.ibm.watsonhealth.fhir.model.OperationOutcome;
import com.ibm.watsonhealth.fhir.model.OperationOutcomeIssue;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.model.ResourceContainer;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.persistence.FHIRPersistence;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRHistoryContext;
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
import com.ibm.watsonhealth.fhir.search.exception.FHIRSearchException;
import com.ibm.watsonhealth.fhir.search.util.SearchUtil;
import com.ibm.watsonhealth.fhir.server.FHIRBuildIdentifier;
import com.ibm.watsonhealth.fhir.server.exception.FHIRRestException;
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
    private static final String FHIR_SPEC_VERSION = "1.0.2";
    private static final String ALLOWABLE_VIRTUAL_RESOURCE_TYPES = "com.ibm.watsonhealth.fhir.allowable.virtual.resource.types";
    private static final String VIRTUAL_RESOURCE_TYPES_FEATURE_ENABLED = "com.ibm.watsonhealth.fhir.virtual.resource.types.feature.enabled";
    private static final String USER_DEFINED_SCHEMATRON_ENABLED = "com.ibm.watsonhealth.fhir.validation.user.defined.schematron.enabled";
    private static final String BASIC_RESOURCE_TYPE_URL = "http://ibm.com/watsonhealth/fhir/basic-resource-type";

    private PersistenceHelper persistenceHelper = null;
    private FHIRPersistence persistence = null;
    private ObjectFactory objectFactory = new ObjectFactory();
    
    private List<String> allowableVirtualResourceTypes = null;
    private Boolean virtualResourceTypesFeatureEnabled = null;
    
    private Boolean userDefinedSchematronEnabled = null;
    
    private Parameter basicCodeSearchParameter = null;

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
            // Make sure the type specified in the URL string is congruent with the actual type of the resource.
            String resourceType = FHIRUtil.getResourceTypeName(resource);
            if (!resourceType.equals(type)) {
                throw new FHIRException("Resource type '" + resourceType + "' does not match type specified in request URI: " + type);
            }
            
            URI locationURI = doCreate(resource);
            
            ResponseBuilder response = Response.created(locationURI);
            response = addHeaders(response, resource);
            return response.build();
        } catch (FHIRRestException e) {
            return exceptionResponse(e);
        } catch (FHIRException e) {
            return exceptionResponse(e, Response.Status.BAD_REQUEST);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
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
    public Response update(
        @ApiParam(value = "The type of the resource to be updated.", required = true)
        @PathParam("type") String type, 
        @ApiParam(value = "The id of the resource to be updated.", required = true)
        @PathParam("id") String id, 
        @ApiParam(value = "The new contents of the resource to be updated.", required = true)
        Resource resource) {

        log.entering(this.getClass().getName(), "update(String,Resource)", "this=" + FHIRUtilities.getObjectHandle(this));

        try {
            String resourceType = FHIRUtil.getResourceTypeName(resource);
            if (!resourceType.equals(type)) {
                throw new FHIRException("Resource type '" + resourceType + "' does not match type specified in request URI: " + type);
            }
            
            URI locationURI = doUpdate(resource, id);

            ResponseBuilder response = Response.ok().header(HttpHeaders.LOCATION, locationURI);
            response = addHeaders(response, resource);
            return response.build();
        } catch (FHIRRestException e) {
            return exceptionResponse(e);
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
            Resource resource = doRead(type, id);
            ResponseBuilder response = Response.ok().entity(resource);
            response = addHeaders(response, resource);
            return response.build();
        } catch (FHIRRestException e) {
            return exceptionResponse(e);
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
            Resource resource = doVRead(type, id, vid);
            
            ResponseBuilder response = Response.ok().entity(resource);
            response = addHeaders(response, resource);
            return response.build();
        } catch (FHIRRestException e) {
            return exceptionResponse(e);
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
            Bundle bundle = doHistory(type, id, uriInfo.getQueryParameters());

            return Response.ok(bundle).build();
        } catch (FHIRRestException e) {
            return exceptionResponse(e);
        } catch (FHIRVirtualResourceTypeException | FHIRPersistenceException e) {
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
            MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
            Bundle bundle = doSearch(type, queryParameters);
            return Response.ok(bundle).build();
        } catch (FHIRRestException e) {
            return exceptionResponse(e);
        } catch (FHIRVirtualResourceTypeException | FHIRSearchException | FHIRPersistenceException e) {
            return exceptionResponse(e, Response.Status.BAD_REQUEST);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "search(String,UriInfo)", "this=" + FHIRUtilities.getObjectHandle(this));
        }
    }
    
    @POST
    @Path("Resource/$validate")
    public Response validate(Resource resource) {
        log.entering(this.getClass().getName(), "validate(Resource)", "this=" + FHIRUtilities.getObjectHandle(this));
        try {
            return Response.ok().entity(doValidate(resource)).build();
        } catch (FHIRRestException e) {
            return exceptionResponse(e);
        } catch (FHIRException e) {
            return exceptionResponse(e, Response.Status.BAD_REQUEST);
        } catch (Exception e) {
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "validate(Resource)", "this=" + FHIRUtilities.getObjectHandle(this));
        }
    }
    
    @POST
    @ApiOperation(value = "Performs a collection of operations as either a batch or transaction interaction.", 
    notes = "A Bundle resource containing the operations should be passed in the request body.")
    public Response bundle(
        @ApiParam(value = "The Bundle resource which contains the collection of operations to be performed.", required = true) 
        Bundle bundle) {

        log.entering(this.getClass().getName(), "bundle(Bundle)", "this=" + FHIRUtilities.getObjectHandle(this));

        try {
            // First, we need to do some validation of the bundle.
            if (bundle == null || bundle.getEntry() == null || bundle.getEntry().isEmpty()) {
                throw new FHIRException("Bundle parameter is missing or empty.");
            }
            
            if (bundle.getType() == null || bundle.getType().getValue() == null) {
                throw new FHIRException("Bundle.type is missing");
            }
            
            // Finally, invoke the correct persistence function, depending on bundle type.
            Bundle responseBundle = null;
            switch (bundle.getType().getValue()) {
            case BATCH:
                responseBundle = doBatch(bundle);
                break;
            case TRANSACTION:
                responseBundle = doTransaction(bundle);
                break;

            // For any other bundle type, we'll throw an error.
            default:
                throw new FHIRException("Bundle.type must be either 'batch' or 'transaction'.");
            }                
                
            ResponseBuilder response = Response.ok(responseBundle);
            return response.build();
        } catch (FHIRException e) {
            return exceptionResponse(e, Response.Status.BAD_REQUEST);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error encountered during request processing: ", e);
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "bundle(Bundle)", "this=" + FHIRUtilities.getObjectHandle(this));
        }
    }

    /**
     * Performs the heavy lifting associated with a 'create' interaction.
     * @param resource the Resource to be stored.
     * @return the location URI associated with the new Resource
     * @throws Exception
     */
    protected URI doCreate(Resource resource) throws Exception {
        log.entering(this.getClass().getName(), "doCreate");
        try {
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
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(resource, buildPersistenceEventProperties());
            getInterceptorMgr().fireBeforeCreateEvent(event);

            getPersistenceImpl().create(resource);

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
    protected URI doUpdate(Resource resource, String id) throws Exception {
        log.entering(this.getClass().getName(), "doUpdate");
        try {
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

            // First, invoke the 'beforeUpdate' interceptor methods.
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(resource, buildPersistenceEventProperties());
            getInterceptorMgr().fireBeforeUpdateEvent(event);

            getPersistenceImpl().update(id, resource);

            // Build our location URI and add it to the interceptor event structure since it is now known.
            URI locationURI = FHIRUtil.buildLocationURI(FHIRUtil.getResourceTypeName(resource), resource);
            event.getProperties().put(FHIRPersistenceEvent.PROPNAME_RESOURCE_LOCATION_URI, locationURI.toString());

            // Invoke the 'afterCreate' interceptor methods.
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
    protected Resource doRead(String type, String id) throws Exception {
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
            Resource resource = getPersistenceImpl().read(resourceType, id);
            if (resource == null) {
                throw new FHIRPersistenceResourceNotFoundException("Resource '" + type + "/" + id + "' not found.");
            }

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
            Resource resource = getPersistenceImpl().vread(resourceType, id, versionId);
            if (resource == null) {
                throw new FHIRPersistenceResourceNotFoundException("Resource '" + resourceType.getSimpleName() + "/" + id + "' version " + versionId + " not found.");
            }

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
    protected Bundle doHistory(String type, String id, MultivaluedMap<String, String> queryParameters) throws Exception {
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
            FHIRHistoryContext context = FHIRPersistenceUtil.parseHistoryParameters(queryParameters);
            List<Resource> resources = getPersistenceImpl().history(resourceType, id, context);
            Bundle bundle = createBundle(resources, BundleTypeList.HISTORY, context.getTotalCount());
            addLinks(context, bundle);

            return bundle;
        } finally {
            log.exiting(this.getClass().getName(), "enclosing_method");
        }
    }
    
    /**
     * Performs heavy lifting associated with a 'search' operation.
     * @param type the resource type associated with the search
     * @param queryParameters a Map containing the query parameters from the request URL
     * @return a Bundle containing the search result set
     * @throws Exception
     */
    protected Bundle doSearch(String type, MultivaluedMap<String, String> queryParameters) throws Exception {
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
            FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParameters);
            List<Parameter> searchParameters = context.getSearchParameters();
            if (implicitSearchParameter != null) {
                searchParameters.add(implicitSearchParameter);
            }
            
            List<Resource> resources = getPersistenceImpl().search(resourceType, context);
            Bundle bundle = createBundle(resources, BundleTypeList.SEARCHSET, context.getTotalCount());
            addLinks(context, bundle);
            
            return bundle;
        } finally {
            log.exiting(this.getClass().getName(), "doSearch");
        }
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
     * Processes a bundled request in 'transaction' mode.
     * 
     * @param requestBundle
     *            the request Bundle
     * @return
     */
    protected Bundle doTransaction(Bundle requestBundle) throws Exception {
        log.entering(this.getClass().getName(), "doTransaction");
        try {
            Bundle responseBundle = validateBundle(requestBundle);
            return responseBundle;
        } finally {
            log.exiting(this.getClass().getName(), "doTransaction");
        }
    }

    /**
     * Processes a bundled request in 'batch' mode.
     * 
     * @param requestBundle
     *            the request Bundle
     * @return
     */
    protected Bundle doBatch(Bundle requestBundle) throws Exception {
        log.entering(this.getClass().getName(), "doBatch");
        try {
            Bundle responseBundle = validateBundle(requestBundle);
            return responseBundle;
        } finally {
            log.exiting(this.getClass().getName(), "doBatch");
        }
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
        return rb.header(HttpHeaders.ETAG, "W/\"" + resource.getMeta().getVersionId().getValue() + "\"")
                .header(HttpHeaders.LAST_MODIFIED, resource.getMeta().getLastUpdated().getValue().toXMLFormat());
    }

    
    private Response exceptionResponse(FHIRRestException e) {
        return (e.getOperationOutcome() != null ? 
                Response.status(e.getHttpStatus()).entity(e.getOperationOutcome()).build() :
                exceptionResponse(e, Response.Status.BAD_REQUEST));
    }
    
    private Response exceptionResponse(Exception e, Status status) {
        return Response.status(status).entity(FHIRUtil.buildOperationOutcome(e)).build();
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
    
    private boolean isAllowableVirtualResourceType(String virtualResourceType) {
        return getAllowableVirtualResourceTypes().contains(virtualResourceType) || 
                getAllowableVirtualResourceTypes().contains("*");
    }
    
    @SuppressWarnings("unchecked")
    private List<String> getAllowableVirtualResourceTypes() {
        if (allowableVirtualResourceTypes == null) {
            allowableVirtualResourceTypes = (List<String>) context.getAttribute(ALLOWABLE_VIRTUAL_RESOURCE_TYPES);
        }
        return allowableVirtualResourceTypes;
    }
    
    private Boolean isVirtualResourceTypesFeatureEnabled() {
        return getVirtualResourceTypesFeatureEnabled();
    }
    
    private Boolean getVirtualResourceTypesFeatureEnabled() {
        if (virtualResourceTypesFeatureEnabled == null) {
            virtualResourceTypesFeatureEnabled = (Boolean) context.getAttribute(VIRTUAL_RESOURCE_TYPES_FEATURE_ENABLED);
        }
        return virtualResourceTypesFeatureEnabled;
    }
    
    private Boolean isUserDefinedSchematronEnabled() {
        return getUserDefinedSchematronEnabled();
    }
    
    private Boolean getUserDefinedSchematronEnabled() {
        if (userDefinedSchematronEnabled == null) {
            userDefinedSchematronEnabled = (Boolean) context.getAttribute(USER_DEFINED_SCHEMATRON_ENABLED);
        }
        return userDefinedSchematronEnabled;
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
    
    private void addLinks(FHIRPagingContext context, Bundle bundle) {
        // create 'self' link
        BundleLink selfLink = objectFactory.createBundleLink();
        selfLink.setRelation(string("self"));
        selfLink.setUrl(uri(uriInfo.getRequestUri().toString()));
        bundle.getLink().add(selfLink);
        
        int nextPageNumber = context.getPageNumber() + 1;
        if (nextPageNumber <= context.getLastPageNumber()) {
            // create 'next' link
            BundleLink nextLink = objectFactory.createBundleLink();
            nextLink.setRelation(string("next"));
            
            // starting with the original request URI
            String nextLinkUrl = uriInfo.getRequestUri().toString();
            
            // remove existing _page and _count parameters from the query string
            nextLinkUrl = nextLinkUrl
                    .replace("&_page=" + context.getPageNumber(), "")
                    .replace("_page=" + context.getPageNumber() + "&", "")                    
                    .replace("_page=" + context.getPageNumber(), "")
                    .replace("&_count=" + context.getPageSize(), "")
                    .replace("_count=" + context.getPageSize() + "&", "")
                    .replace("_count=" + context.getPageSize(), "");
                        
            if (!nextLinkUrl.endsWith("?")) {
                // there are other parameters in the query string
                nextLinkUrl += "&";
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
            String prevLinkUrl = uriInfo.getRequestUri().toString();
            
            // remove existing _page and _count parameters from the query string
            prevLinkUrl = prevLinkUrl
                    .replace("&_page=" + context.getPageNumber(), "")
                    .replace("_page=" + context.getPageNumber() + "&", "")                    
                    .replace("_page=" + context.getPageNumber(), "")
                    .replace("&_count=" + context.getPageSize(), "")
                    .replace("_count=" + context.getPageSize() + "&", "")
                    .replace("_count=" + context.getPageSize(), "");
            
            if (!prevLinkUrl.endsWith("?")) {
                // there are other parameters in the query string
                prevLinkUrl += "&";
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
            // Make sure the bundle type is either BATCH or TRANSACTION,
            // and determine the response bundle type.
            BundleTypeList responseBundleType = null;
            if (bundle.getType().getValue() != null) {
                switch (bundle.getType().getValue()) {
                case BATCH:
                    responseBundleType = BundleTypeList.BATCH_RESPONSE;
                    break;
                case TRANSACTION:
                    responseBundleType = BundleTypeList.TRANSACTION_RESPONSE;
                    break;

                // For any other bundle type, we'll throw an error.
                default:
                    throw new FHIRException("Bundle.type must be either 'batch' or 'transaction'.");
                }
            }

            // Create the response bundle with the appropriate type.
            Bundle responseBundle = objectFactory.createBundle().withType(objectFactory.createBundleType().withValue(responseBundleType));

            // Next, make sure that each bundle entry contains a valid request.
            // As we're validating the request bundle, we'll also construct entries for the response bundle.
            for (BundleEntry requestEntry : bundle.getEntry()) {
                BundleRequest request = requestEntry.getRequest();

                // Create a corresponding response entry and add it to the response bundle.
                BundleResponse response = objectFactory.createBundleResponse();
                BundleEntry responseEntry = objectFactory.createBundleEntry().withResponse(response);
                responseBundle.getEntry().add(responseEntry);

                // Validate 'requestEntry' and update 'responseEntry' with any errors.
                try {

                    // Validate the HTTP method.
                    HTTPVerbList method = request.getMethod().getValue();
                    switch (method) {
                    case GET:
                        if (requestEntry.getResource() != null) {
                            throw new FHIRException("BundleEntry.resource not allowed for BundleEntry with GET method.");
                        }
                        break;

                    case POST:
                    case PUT:
                        if (requestEntry.getResource() == null) {
                            throw new FHIRException("BundleEntry.resource is required for BundleEntry with POST or PUT method.");
                        }
                        break;

                    default:
                        throw new FHIRException("BundleEntry.request contains unsupported HTTP method: " + method.name());
                    }

                    // Validate the resource contained in the request entry.
                    Resource resource = getBundleEntryResource(requestEntry);
                    List<OperationOutcomeIssue> issues = FHIRValidator.getInstance().validate(resource, isUserDefinedSchematronEnabled());
                    if (!issues.isEmpty()) {
                        OperationOutcome oo = FHIRUtil.buildOperationOutcome(issues);
                        setBundleEntryResource(responseEntry, oo);
                        response.setStatus(objectFactory.createString().withValue(Integer.toString(SC_BAD_REQUEST)));
                    }
                } catch (FHIRException e) {
                    setBundleEntryResource(responseEntry, FHIRUtil.buildOperationOutcome(e));
                    response.setStatus(objectFactory.createString().withValue(Integer.toString(SC_BAD_REQUEST)));
                }
            }

            return responseBundle;
        } finally {
            log.exiting(this.getClass().getName(), "validateBundle");
        }
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
    private Map<String, Object> buildPersistenceEventProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put(FHIRPersistenceEvent.PROPNAME_URI_INFO, uriInfo);
        props.put(FHIRPersistenceEvent.PROPNAME_HTTP_HEADERS, httpHeaders);
        props.put(FHIRPersistenceEvent.PROPNAME_SECURITY_CONTEXT, securityContext);
        return props;
    }
}
