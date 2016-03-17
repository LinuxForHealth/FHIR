/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.resources;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.persistence.FHIRPersistence;
import com.ibm.watsonhealth.fhir.persistence.FHIRPersistenceException;
import com.ibm.watsonhealth.fhir.validation.Validator;

@Path("/")
public class FHIRResource {
	private static final String NL = System.getProperty("line.separator");

	private Validator validator = null;	
	private FHIRPersistence persistence = null;
	
	public FHIRResource(Validator validator, FHIRPersistence persistence) {
		this.validator = validator;
		this.persistence = persistence;
	}
	
	@POST
	@Consumes({ "application/json+fhir" })
	@Path("{type}")
	public Response create(Resource resource) {
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
				persistence.create(resource);
			}
		} catch (Exception e) {
			throw new WebApplicationException(e);
		}
		String lastUpdated = resource.getMeta().getLastUpdated().getValue().toXMLFormat();
		return Response.created(URI.create(resourceType.getSimpleName() + "/" + resource.getId().getValue())).header(HttpHeaders.LAST_MODIFIED, lastUpdated).build();
	}
	
	@GET
	@Produces({ "application/json+fhir" })
	@Path("{type}/{id}")
	public Resource read(@PathParam("type") String type, @PathParam("id") String id) throws ClassNotFoundException {
		Class<? extends Resource> resourceType = getResourceType(type);
		Resource resource = null;
		try {
			resource = persistence.read(resourceType, id);
		} catch (FHIRPersistenceException e) {
			throw new WebApplicationException(e);
		}
		if (resource == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return resource;
	}
	
	@GET
	@Produces({ "application/json+fhir" })
	@Path("{type}/{id}/_history/{vid}")
	public Resource vread(@PathParam("type") String type, @PathParam("id") String id, @PathParam("vid") String vid) throws ClassNotFoundException {
		Class<? extends Resource> resourceType = getResourceType(type);
		Resource resource = null;
		try {
			resource = persistence.vread(resourceType, id, vid);
		} catch (FHIRPersistenceException e) {
			throw new WebApplicationException(e);
		}
		if (resource == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return resource;
	}
	
	@SuppressWarnings("unchecked")
	private Class<? extends Resource> getResourceType(String type) {
		try {
			return (Class<? extends Resource>) Class.forName("com.ibm.watsonhealth.fhir.model." + type);
		} catch (ClassNotFoundException e) { }
		return Resource.class;
	}
}
