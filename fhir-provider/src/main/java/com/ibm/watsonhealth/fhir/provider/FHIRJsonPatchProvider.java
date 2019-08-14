/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.provider;

import static com.ibm.watsonhealth.fhir.model.util.JsonSupport.nonClosingInputStream;
import static com.ibm.watsonhealth.fhir.model.util.JsonSupport.nonClosingOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReaderFactory;
import javax.json.JsonWriterFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.RuntimeType;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

@Consumes(com.ibm.watsonhealth.fhir.core.MediaType.APPLICATION_JSON_PATCH)
@Produces(com.ibm.watsonhealth.fhir.core.MediaType.APPLICATION_JSON_PATCH)
public class FHIRJsonPatchProvider implements MessageBodyReader<JsonArray>, MessageBodyWriter<JsonArray> {
    private static final Logger log = Logger.getLogger(FHIRJsonPatchProvider.class.getName());

    private static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);
    private static final JsonWriterFactory JSON_WRITER_FACTORY = Json.createWriterFactory(null);
    
    private final RuntimeType runtimeType;
    
    public FHIRJsonPatchProvider(RuntimeType runtimeType) {
        this.runtimeType = Objects.requireNonNull(runtimeType);
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return JsonArray.class.isAssignableFrom(type) && RuntimeType.SERVER.equals(runtimeType);
    }

    @Override
    public JsonArray readFrom(Class<JsonArray> type, Type genericType, Annotation[] annotations, MediaType mediaType,
        MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        log.entering(this.getClass().getName(), "readFrom");
        try {
            return JSON_READER_FACTORY.createReader(nonClosingInputStream(entityStream)).readArray();
        } catch (Exception e) {
            throw new WebApplicationException(e);
        } finally {
            log.exiting(this.getClass().getName(), "readFrom");
        }
    }
    
    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return JsonArray.class.isAssignableFrom(type) && RuntimeType.CLIENT.equals(runtimeType);
    }

    @Override
    public void writeTo(JsonArray t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
        MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        log.entering(this.getClass().getName(), "writeTo");
        try {
            JSON_WRITER_FACTORY.createWriter(nonClosingOutputStream(entityStream)).writeArray(t);
        } catch (Exception e) {
            throw new WebApplicationException(e);
        } finally {
            log.exiting(this.getClass().getName(), "writeTo");
        }
    }

    @Override
    public long getSize(JsonArray t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }
}
