/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.resources;

import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.getResourceType;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.ibm.watsonhealth.fhir.core.FHIRUtilities;
import com.ibm.watsonhealth.fhir.core.MediaType;
import com.ibm.watsonhealth.fhir.model.Bundle;
import com.ibm.watsonhealth.fhir.model.BundleEntry;
import com.ibm.watsonhealth.fhir.model.Conformance;
import com.ibm.watsonhealth.fhir.model.ObjectFactory;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.model.ResourceContainer;
import com.ibm.watsonhealth.fhir.persistence.FHIRPersistence;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceResourceNotFoundException;
import com.ibm.watsonhealth.fhir.search.Parameter;
import com.ibm.watsonhealth.fhir.search.util.SearchUtil;
import com.ibm.watsonhealth.fhir.server.helper.FHIRPersistenceHelper;
import com.ibm.watsonhealth.fhir.validation.Validator;

@Path("/")
public class FHIRResource {
    private static final Logger log = java.util.logging.Logger.getLogger(FHIRResource.class.getName());
    private static final String NL = System.getProperty("line.separator");
    
    private static final String FHIR_SERVER_VERSION = "0.1";
    private static final String FHIR_SPEC_VERSION = "1.0.2";
    

    private Validator validator = null;
    private FHIRPersistenceHelper persistenceHelper = null;
    private FHIRPersistence persistence = null;
    private ObjectFactory objectFactory = new ObjectFactory();

    @Context
    private ServletContext context;

    public FHIRResource() {
        log.finest("In FHIRResource() ctor. handle=" + FHIRUtilities.getObjectHandle(this));
        log.finest(FHIRUtilities.getCurrentStacktrace());
    }

    @POST
    @Consumes({ MediaType.APPLICATION_XML_FHIR, MediaType.APPLICATION_JSON_FHIR })
    @Path("{type}")
    public Response create(Resource resource) {
        log.entering(this.getClass().getName(), "create(Resource)", "this=" + FHIRUtilities.getObjectHandle(this));
        Class<? extends Resource> resourceType = resource.getClass();
        try {
            List<String> messages = getValidator().validate(resource);
            if (!messages.isEmpty()) {
                StringBuffer buffer = new StringBuffer();
                for (String message : messages) {
                    buffer.append(message);
                    buffer.append(NL);
                }
                log.fine("Validation errors:\n" + buffer.toString());
                return Response.status(Response.Status.BAD_REQUEST).entity(buffer.toString()).build();
            } else {
                getPersistenceImpl().create(resource);
            }
            String lastUpdated = resource.getMeta().getLastUpdated().getValue().toXMLFormat();
            return Response.created(URI.create(resourceType.getSimpleName() + "/"
                    + resource.getId().getValue())).header(HttpHeaders.LAST_MODIFIED, lastUpdated).build();
        } catch (FHIRPersistenceException e) {
            throw new WebApplicationException(e.getMessage(), Response.Status.BAD_REQUEST);
        } catch (Exception e) {
            throw new WebApplicationException(e);
        } finally {
            log.exiting(this.getClass().getName(), "create(Resource)", "this=" + FHIRUtilities.getObjectHandle(this));
        }

    }

    @GET
    @Produces({ MediaType.APPLICATION_XML_FHIR, MediaType.APPLICATION_JSON_FHIR })
    @Path("{type}/{id}")
    public Resource read(@PathParam("type") String type, @PathParam("id") String id) throws ClassNotFoundException {
        log.entering(this.getClass().getName(), "read(String,String)", "this=" + FHIRUtilities.getObjectHandle(this));
        Class<? extends Resource> resourceType = getResourceType(type);
        Resource resource = null;
        try {
            resource = getPersistenceImpl().read(resourceType, id);
            if (resource == null) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }
            return resource;
        } catch (FHIRPersistenceException e) {
            throw new WebApplicationException(e.getMessage(), e, Response.Status.BAD_REQUEST);
        } finally {
            if (log.isLoggable(Level.FINE)) {
                log.exiting(this.getClass().getName(), "read(String,String)", "this=" + FHIRUtilities.getObjectHandle(this));
            }
        }
    }

    @GET
    @Produces({ MediaType.APPLICATION_XML_FHIR, MediaType.APPLICATION_JSON_FHIR })
    @Path("{type}/{id}/_history/{vid}")
    public Resource vread(@PathParam("type") String type, @PathParam("id") String id, @PathParam("vid") String vid) {
        if (log.isLoggable(Level.FINE)) {
            log.entering(this.getClass().getName(), "vread(String,String,String)", "this=" + FHIRUtilities.getObjectHandle(this));
        }
        Class<? extends Resource> resourceType = getResourceType(type);
        Resource resource = null;
        try {
            resource = getPersistenceImpl().vread(resourceType, id, vid);
            if (resource == null) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }
            return resource;
        } catch (FHIRPersistenceException e) {
            throw new WebApplicationException(e.getMessage(), Response.Status.BAD_REQUEST);
        } finally {
            log.exiting(this.getClass().getName(), "vread(String,String,String)", "this=" + FHIRUtilities.getObjectHandle(this));
        }
    }

    @PUT
    @Consumes({ MediaType.APPLICATION_XML_FHIR, MediaType.APPLICATION_JSON_FHIR })
    @Path("{type}/{id}")
    public Response update(@PathParam("id") String id, Resource resource) {
        log.entering(this.getClass().getName(), "update(String,Resource)", "this=" + FHIRUtilities.getObjectHandle(this));
        try {
            Class<? extends Resource> resourceType = resource.getClass();
            List<String> messages = getValidator().validate(resource);
            if (!messages.isEmpty()) {
                StringBuffer buffer = new StringBuffer();
                for (String message : messages) {
                    buffer.append(message);
                    buffer.append(NL);
                }
                log.fine("Validation errors:\n" + buffer.toString());
                return Response.status(Response.Status.BAD_REQUEST).entity(buffer.toString()).build();
            } else {
                getPersistenceImpl().update(id, resource);
            }

            String lastUpdated = resource.getMeta().getLastUpdated().getValue().toXMLFormat();
            return Response.created(URI.create(resourceType.getSimpleName() + "/"
                    + resource.getId().getValue())).header(HttpHeaders.LAST_MODIFIED, lastUpdated).build();
        } catch (FHIRPersistenceResourceNotFoundException e) {
            throw new WebApplicationException(e.getMessage(), Response.Status.METHOD_NOT_ALLOWED);
        } catch (Exception e) {
            throw new WebApplicationException(e, Response.Status.BAD_REQUEST);
        } finally {
            log.exiting(this.getClass().getName(), "update(String,Resource)", "this=" + FHIRUtilities.getObjectHandle(this));
        }
    }
    
    @GET
    @Produces({ MediaType.APPLICATION_XML_FHIR, MediaType.APPLICATION_JSON_FHIR })
    @Path("{type}/{id}/_history")
    public Response history(@PathParam("type") String type, @PathParam("id") String id) {
        log.entering(this.getClass().getName(), "history(String,String)", "this=" + FHIRUtilities.getObjectHandle(this));
        try {
            Class<? extends Resource> resourceType = getResourceType(type);
            List<Resource> resources = getPersistenceImpl().history(resourceType, id);
            Bundle bundle = createBundle(resources);
            return Response.ok(bundle).build();
        } catch (FHIRPersistenceException e) {
            throw new WebApplicationException(e, Response.Status.BAD_REQUEST);
        } finally {
            log.exiting(this.getClass().getName(), "history(String,String)", "this=" + FHIRUtilities.getObjectHandle(this));
        }
    }

    @GET
    @Produces({ MediaType.APPLICATION_XML_FHIR, MediaType.APPLICATION_JSON_FHIR })
    @Path("{type}")
    public Response search(@PathParam("type") String type, @Context UriInfo uriInfo) {
        log.entering(this.getClass().getName(), "search(String,UriInfo)", "this=" + FHIRUtilities.getObjectHandle(this));
        try {
            Class<? extends Resource> resourceType = getResourceType(type);
            Map<String, List<String>> queryParameters = uriInfo.getQueryParameters();
            List<Parameter> searchParameters = SearchUtil.parseQueryParameters(resourceType, queryParameters);
            List<Resource> resources = getPersistenceImpl().search(resourceType, searchParameters);
            Bundle bundle = createBundle(resources);
            return Response.ok(bundle).build();
        } catch (FHIRPersistenceException e) {
            throw new WebApplicationException(e, Response.Status.BAD_REQUEST);
        } finally {
            log.exiting(this.getClass().getName(), "search(String,UriInfo)", "this=" + FHIRUtilities.getObjectHandle(this));
        }
    }

    @GET
    @Produces({ MediaType.APPLICATION_XML_FHIR, MediaType.APPLICATION_JSON_FHIR })
    @Path("metadata")
    public Resource metadata() throws ClassNotFoundException {
        log.entering(this.getClass().getName(), "metadata()");
        
        try {
            return buildConformanceStatement();
        } finally {
            if (log.isLoggable(Level.FINE)) {
                log.exiting(this.getClass().getName(), "metadata()");
            }
        }
    }
    
    /**
     * Builds a Conformance resource instance which describes this server.
     */
    private Resource buildConformanceStatement() {
        ObjectFactory of = new ObjectFactory();
        
        // TODO - we need to fill out more of the Conformance resource.
        Conformance conformance = of.createConformance().
                withDate(of.createDateTime().withValue(new Date().toString())).
                withFormat(of.createCode().withValue("json"), of.createCode().withValue("xml")).
                withVersion(of.createString().withValue(FHIR_SERVER_VERSION)).
                withFhirVersion(of.createId().withValue(FHIR_SPEC_VERSION));
        
        return conformance;
    }

    private Bundle createBundle(List<Resource> resources) {
        Bundle bundle = objectFactory.createBundle();
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
}
