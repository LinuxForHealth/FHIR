/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.ConstrainedTo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.RuntimeType;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

@ConstrainedTo(RuntimeType.CLIENT)
@Produces({ com.ibm.watsonhealth.fhir.core.MediaType.APPLICATION_JSON_FHIR, MediaType.APPLICATION_JSON })
@Consumes({ com.ibm.watsonhealth.fhir.core.MediaType.APPLICATION_JSON_FHIR, MediaType.APPLICATION_JSON })
public class FHIRJsonProvider implements MessageBodyReader<JsonObject>, MessageBodyWriter<JsonObject> {
    private static final Logger log = Logger.getLogger(FHIRJsonProvider.class.getName());

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type == JsonObject.class;
    }

    @Override
    public JsonObject readFrom(Class<JsonObject> type, Type genericType, Annotation[] annotations, MediaType mediaType,
        MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        log.entering(this.getClass().getName(), "readFrom");
        try {
            return Json.createReader(entityStream).readObject();
        } catch (Exception e) {
            throw new WebApplicationException(e);
        } finally {
            log.exiting(this.getClass().getName(), "readFrom");
        }
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type == JsonObject.class;
    }

    @Override
    public void writeTo(JsonObject t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
        MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        log.entering(this.getClass().getName(), "writeTo");
        try {
            Json.createWriter(entityStream).writeObject(t);
        } catch (Exception e) {
            throw new WebApplicationException(e);
        } finally {
            log.exiting(this.getClass().getName(), "writeTo");
        }
    }

    @Override
    public long getSize(JsonObject t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }
}
