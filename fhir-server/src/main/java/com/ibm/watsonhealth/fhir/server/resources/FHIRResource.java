/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.resources;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
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
import com.ibm.watsonhealth.fhir.model.ObjectFactory;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.model.ResourceContainer;
import com.ibm.watsonhealth.fhir.persistence.FHIRPersistence;
import com.ibm.watsonhealth.fhir.persistence.FHIRPersistenceException;
import com.ibm.watsonhealth.fhir.search.Parameter;
import com.ibm.watsonhealth.fhir.search.util.SearchUtil;
import com.ibm.watsonhealth.fhir.server.helper.FHIRPersistenceHelper;
import com.ibm.watsonhealth.fhir.validation.Validator;

@Path("/")
public class FHIRResource {
    private static final Logger log = java.util.logging.Logger.getLogger(FHIRResource.class.getName());

	private static final String NL = System.getProperty("line.separator");

	@Inject
	private FHIRPersistenceHelper persistenceHelper;
	
	private Validator validator = null;	
	private FHIRPersistence persistence = null;
	private ObjectFactory objectFactory = new ObjectFactory();
	
	public FHIRResource() {
	    log.finest("In FHIRResource() ctor. handle=" + FHIRUtilities.getObjectHandle(this));
	    log.finest(FHIRUtilities.getCurrentStacktrace());
	    validator = new Validator();
	}
	
	/**
	 * Retrieves an appropriate persistence implementation according to the 
	 * current configuration.
	 */
    private FHIRPersistence getPersistenceMgr() {
        if (persistence == null) {
            if (persistenceHelper == null) {
                log.warning("FHIRPersistenceHelper instance was not injected into FHIRResource instance!");
                persistenceHelper = new FHIRPersistenceHelper();
            }
            persistence = persistenceHelper.getFHIRPersistenceImpl();
        }
        
        return persistence;
    }
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON_FHIR })
	@Path("{type}")
	public Response create(Resource resource) {
        if (log.isLoggable(Level.FINE)) {
            log.entering(this.getClass().getName(), "create(Resource)", "this=" + FHIRUtilities.getObjectHandle(this));
        }
        Class<? extends Resource> resourceType = resource.getClass();
		try {
			List<String> messages = validator.validate(resource);
			if (!messages.isEmpty()) {
				StringBuffer buffer = new StringBuffer();
				for (String message : messages) {
					buffer.append(message);
					buffer.append(NL);
				}
				return Response.status(Response.Status.BAD_REQUEST).entity(buffer.toString()).build();
			} else {
				getPersistenceMgr().create(resource);
			}
			
	        String lastUpdated = resource.getMeta().getLastUpdated().getValue().toXMLFormat();
	        return Response.created(URI.create(resourceType.getSimpleName() + "/" + resource.getId().getValue())).header(HttpHeaders.LAST_MODIFIED, lastUpdated).build();
		} catch (Exception e) {
			throw new WebApplicationException(e);
		} finally {
		    if (log.isLoggable(Level.FINE)) {
                log.exiting(this.getClass().getName(), "create(Resource)", "this=" + FHIRUtilities.getObjectHandle(this));
            }
		}
		
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON_FHIR })
	@Path("{type}/{id}")
	public Resource read(@PathParam("type") String type, @PathParam("id") String id) throws ClassNotFoundException {
        if (log.isLoggable(Level.FINE)) {
            log.entering(this.getClass().getName(), "read(String,String)", "this=" + FHIRUtilities.getObjectHandle(this));
        }
		Class<? extends Resource> resourceType = getResourceType(type);
		Resource resource = null;
		try {
		    log.finest("Inside read(). using persistence helper: " + FHIRUtilities.getObjectHandle(persistenceHelper));
			resource = getPersistenceMgr().read(resourceType, id);
	        if (resource == null) {
	            throw new WebApplicationException(Response.Status.NOT_FOUND);
	        }
	        return resource;
		} catch (FHIRPersistenceException e) {
			throw new WebApplicationException(e);
		} finally {
		    if (log.isLoggable(Level.FINE)) {
                log.exiting(this.getClass().getName(), "read(String,String)", "this=" + FHIRUtilities.getObjectHandle(this));
            }
		}
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON_FHIR })
	@Path("{type}/{id}/_history/{vid}")
	public Resource vread(@PathParam("type") String type, @PathParam("id") String id, @PathParam("vid") String vid) {
        if (log.isLoggable(Level.FINE)) {
            log.entering(this.getClass().getName(), "vread(String,String,String)", "this=" + FHIRUtilities.getObjectHandle(this));
        }
		Class<? extends Resource> resourceType = getResourceType(type);
		Resource resource = null;
		try {
			resource = getPersistenceMgr().vread(resourceType, id, vid);
	        if (resource == null) {
	            throw new WebApplicationException(Response.Status.NOT_FOUND);
	        }
	        return resource;
		} catch (FHIRPersistenceException e) {
			throw new WebApplicationException(e);
		} finally {
		    if (log.isLoggable(Level.FINE)) {
                log.exiting(this.getClass().getName(), "vread(String,String,String)", "this=" + FHIRUtilities.getObjectHandle(this));
            }
		}
	}
	
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON_FHIR })
	@Path("{type}/{id}")
	public Response update(@PathParam("id") String id, Resource resource) {
        if (log.isLoggable(Level.FINE)) {
            log.entering(this.getClass().getName(), "update(String,Resource)", "this=" + FHIRUtilities.getObjectHandle(this));
        }
		try {
	        Class<? extends Resource> resourceType = resource.getClass();
			List<String> messages = validator.validate(resource);
			if (!messages.isEmpty()) {
				StringBuffer buffer = new StringBuffer();
				for (String message : messages) {
					buffer.append(message);
					buffer.append(NL);
				}
				return Response.status(Response.Status.BAD_REQUEST).entity(buffer.toString()).build();
			} else {
				getPersistenceMgr().update(id, resource);
			}
			
	        String lastUpdated = resource.getMeta().getLastUpdated().getValue().toXMLFormat();      
	        return Response.created(URI.create(resourceType.getSimpleName() + "/" + resource.getId().getValue())).header(HttpHeaders.LAST_MODIFIED, lastUpdated).build();
		} catch (Exception e) {
			throw new WebApplicationException(e);
		} finally {
		    if (log.isLoggable(Level.FINE)) {
                log.exiting(this.getClass().getName(), "update(String,Resource)", "this=" + FHIRUtilities.getObjectHandle(this));
            }
		}
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON_FHIR })
	@Path("{type}")
	public Response search(@PathParam("type") String type, @Context UriInfo uriInfo) {
        if (log.isLoggable(Level.FINE)) {
            log.entering(this.getClass().getName(), "search(String,UriInfo)", "this=" + FHIRUtilities.getObjectHandle(this));
        }
		try {
	        Class<? extends Resource> resourceType = getResourceType(type);
	        Map<String, List<String>> queryParameters = uriInfo.getQueryParameters();
	        
	        List<Parameter> searchParameters = SearchUtil.parseQueryParameters(resourceType, queryParameters);
			List<Resource> resources = getPersistenceMgr().search(resourceType, searchParameters);
			
			Bundle bundle = createBundle(resources);
			return Response.ok(bundle).build();
		} catch (FHIRPersistenceException e) {
			throw new WebApplicationException(e);
		} finally {
		    if (log.isLoggable(Level.FINE)) {
                log.exiting(this.getClass().getName(), "search(String,UriInfo)", "this=" + FHIRUtilities.getObjectHandle(this));
            }
		}
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
	
	@SuppressWarnings("unchecked")
	private Class<? extends Resource> getResourceType(String type) {
		try {
			return (Class<? extends Resource>) Class.forName("com.ibm.watsonhealth.fhir.model." + type);
		} catch (ClassNotFoundException e) { }
		return Resource.class;
	}
}
