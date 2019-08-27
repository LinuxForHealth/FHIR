/**
 * (C) Copyright IBM Corp. 2016,2017,2019
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
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReaderFactory;
import javax.json.JsonWriterFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import com.ibm.watsonhealth.fhir.core.MediaType;

@Consumes({ MediaType.APPLICATION_FHIR_JSON, MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_FHIR_JSON, MediaType.APPLICATION_JSON })
public class FHIRJsonProvider implements MessageBodyReader<JsonObject>, MessageBodyWriter<JsonObject> {
    private static final Logger log = Logger.getLogger(FHIRJsonProvider.class.getName());
    
    private static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);
    private static final JsonWriterFactory JSON_WRITER_FACTORY = Json.createWriterFactory(null);

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, javax.ws.rs.core.MediaType mediaType) {
        return JsonObject.class.isAssignableFrom(type);
    }

    @Override
    public JsonObject readFrom(Class<JsonObject> type, Type genericType, Annotation[] annotations, javax.ws.rs.core.MediaType mediaType,
        MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        log.entering(this.getClass().getName(), "readFrom");
        try {
            return JSON_READER_FACTORY.createReader(nonClosingInputStream(entityStream)).readObject();
        } catch (Exception e) {
            throw new WebApplicationException(e);
        } finally {
            log.exiting(this.getClass().getName(), "readFrom");
        }
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, javax.ws.rs.core.MediaType mediaType) {
        return JsonObject.class.isAssignableFrom(type);
    }

    @Override
    public void writeTo(JsonObject t, Class<?> type, Type genericType, Annotation[] annotations, javax.ws.rs.core.MediaType mediaType,
        MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        log.entering(this.getClass().getName(), "writeTo");
        try {
            JSON_WRITER_FACTORY.createWriter(nonClosingOutputStream(entityStream)).writeObject(t);
        } catch (Exception e) {
            throw new WebApplicationException(e);
        } finally {
            log.exiting(this.getClass().getName(), "writeTo");
        }
    }

    @Override
    public long getSize(JsonObject t, Class<?> type, Type genericType, Annotation[] annotations, javax.ws.rs.core.MediaType mediaType) {
        return -1;
    }
}
