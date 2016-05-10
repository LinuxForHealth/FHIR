/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.xml.bind.JAXBException;

import com.ibm.watsonhealth.fhir.model.Basic;
import com.ibm.watsonhealth.fhir.model.Bundle;
import com.ibm.watsonhealth.fhir.model.BundleEntry;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.model.ResourceContainer;
import com.ibm.watsonhealth.fhir.model.util.BasicResourceTransformer;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil.Format;
import com.ibm.watsonhealth.fhir.model.util.VirtualResourceTransformer;

@Produces({ com.ibm.watsonhealth.fhir.core.MediaType.APPLICATION_JSON_FHIR, MediaType.APPLICATION_JSON, com.ibm.watsonhealth.fhir.core.MediaType.APPLICATION_XML_FHIR, MediaType.APPLICATION_XML })
@Consumes({ com.ibm.watsonhealth.fhir.core.MediaType.APPLICATION_JSON_FHIR, MediaType.APPLICATION_JSON, com.ibm.watsonhealth.fhir.core.MediaType.APPLICATION_XML_FHIR, MediaType.APPLICATION_XML })
public class FHIRProvider implements MessageBodyReader<Resource>, MessageBodyWriter<Resource> {
    @Context
    private UriInfo uriInfo;
    
	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
	    return Resource.class.isAssignableFrom(type);
	}
	
	@Override
	public Resource readFrom(Class<Resource> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {
		try {
		    Format format = getFormat(mediaType);
//          String resourceTypeName = uriInfo.getPathSegments().get(0).getPath();
		    String resourceTypeName = getResourceTypeName();
		    if (Format.JSON.equals(format) && !FHIRUtil.isStandardResourceType(resourceTypeName) && type == Resource.class) {
		        JsonReader reader = Json.createReader(entityStream);
		        JsonObject jsonObject = reader.readObject();
		        VirtualResourceTransformer transformer = new VirtualResourceTransformer();
		        return transformer.transform(jsonObject);
		    }
			return FHIRUtil.read(type, format, entityStream);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
	    return Resource.class.isAssignableFrom(type);
	}

	@Override
	public void writeTo(Resource t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
			throws IOException, WebApplicationException {
		try {
		    Format format = getFormat(mediaType);
//		    String resourceTypeName = uriInfo.getPathSegments().get(0).getPath();
		    String resourceTypeName = getResourceTypeName();
		    if (Format.JSON.equals(format) && 
		        !FHIRUtil.isStandardResourceType(resourceTypeName) && 
		        (t instanceof Basic || t instanceof Bundle)) {
		        JsonWriter writer = Json.createWriter(entityStream);
		        if (t instanceof Basic) {
		            Basic basic = (Basic) t;
		            
		            // transform Basic w/extensions into virtual resource
		            BasicResourceTransformer transformer = new BasicResourceTransformer();
		            JsonObject virtualResource = transformer.transform(basic);
		            
		            // write virtual resource to entity stream
		            writer.write(virtualResource);
		        } else {
		            // t instanceof Bundle
		            Bundle bundle = (Bundle) t;
		            
		            JsonObject bundleObject = FHIRUtil.toJsonObject(bundle);
		            JsonObjectBuilder bundleObjectBuilder = FHIRUtil.toJsonObjectBuilder(bundleObject);
		            
		            JsonArray entryArray = bundleObject.getJsonArray("entry");
		            JsonArrayBuilder entryArrayBuilder = Json.createArrayBuilder();
		            
		            int entryIndex = 0;
		            for (JsonValue value : entryArray) {
		                JsonObject entryObject = (JsonObject) value;
		                JsonObjectBuilder entryObjectBuilder = FHIRUtil.toJsonObjectBuilder(entryObject);
		                
		                BundleEntry entry = bundle.getEntry().get(entryIndex);
		                ResourceContainer container = entry.getResource();
		                Basic basic = container.getBasic();
		                
		                if (basic != null) {
                            // transform Basic w/extensions into virtual resource
                            BasicResourceTransformer transformer = new BasicResourceTransformer();
                            JsonObject virtualResource = transformer.transform(basic);
                            
                            // replace "resource" with virtual resource
                            entryObjectBuilder.add("resource", virtualResource);
		                }
		                
		                entryArrayBuilder.add(entryObjectBuilder);
		                entryIndex++;
		            }

		            // replace "entry" with array of transformed entries
		            bundleObjectBuilder.add("entry", entryArrayBuilder);
		            
		            // write bundle of virtual resources to entity stream
		            writer.writeObject(bundleObjectBuilder.build());
		        }
		    } else {
		        FHIRUtil.write(t, format, entityStream);
		    }
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	@Override
	public long getSize(Resource t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}
	
	private Format getFormat(MediaType mediaType) {
		if (mediaType != null) {
			if (mediaType.isCompatible(com.ibm.watsonhealth.fhir.core.MediaType.APPLICATION_JSON_FHIR_TYPE) || mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE)) {
			    return Format.JSON;
			} else if (mediaType.isCompatible(com.ibm.watsonhealth.fhir.core.MediaType.APPLICATION_XML_FHIR_TYPE) || mediaType.isCompatible(MediaType.APPLICATION_XML_TYPE)) {
			    return Format.XML;
			}
		}
		return null;
	}

    // get the resource type name from uriInfo path
    private String getResourceTypeName() {
        String path = uriInfo.getPath();
        if (path.startsWith("http")) {
            // absolute URI
            return uriInfo.getPathSegments().get(4).getPath();
        } else {
            // relative URI
            return uriInfo.getPathSegments().get(0).getPath();
        }
    }
}
