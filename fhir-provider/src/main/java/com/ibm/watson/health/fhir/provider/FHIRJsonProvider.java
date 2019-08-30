/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.provider;

import static com.ibm.watson.health.fhir.model.util.FHIRUtil.buildOperationOutcome;
import static com.ibm.watson.health.fhir.model.util.FHIRUtil.buildOperationOutcomeIssue;
import static com.ibm.watson.health.fhir.model.util.JsonSupport.nonClosingInputStream;
import static com.ibm.watson.health.fhir.model.util.JsonSupport.nonClosingOutputStream;
import static com.ibm.watson.health.fhir.provider.util.FHIRProviderUtil.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import com.ibm.watson.health.fhir.core.FHIRMediaType;
import com.ibm.watson.health.fhir.model.type.IssueSeverity;
import com.ibm.watson.health.fhir.model.type.IssueType;

@Consumes({ FHIRMediaType.APPLICATION_FHIR_JSON, MediaType.APPLICATION_JSON })
@Produces({ FHIRMediaType.APPLICATION_FHIR_JSON, MediaType.APPLICATION_JSON })
public class FHIRJsonProvider implements MessageBodyReader<JsonObject>, MessageBodyWriter<JsonObject> {
    private static final Logger log = Logger.getLogger(FHIRJsonProvider.class.getName());
    
    private static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);
    private static final JsonWriterFactory JSON_WRITER_FACTORY = Json.createWriterFactory(null);

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return JsonObject.class.isAssignableFrom(type);
    }

    @Override
    public JsonObject readFrom(Class<JsonObject> type, Type genericType, Annotation[] annotations, MediaType mediaType,
        MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        log.entering(this.getClass().getName(), "readFrom");
        try (JsonReader reader = JSON_READER_FACTORY.createReader(nonClosingInputStream(entityStream))) {
            return reader.readObject();
        } catch (JsonException e) {
            log.log(Level.WARNING, "an error occurred during resource deserialization", e);
            String acceptHeader = httpHeaders.getFirst(HttpHeaders.ACCEPT);
            Response response = buildResponse(
                buildOperationOutcome(Collections.singletonList(
                    buildOperationOutcomeIssue(IssueSeverity.ValueSet.FATAL, IssueType.ValueSet.EXCEPTION, "FHIRProvider: " + e.getMessage(), null))), getMediaType(acceptHeader));
            throw new WebApplicationException(response);
        } finally {
            log.exiting(this.getClass().getName(), "readFrom");
        }
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return JsonObject.class.isAssignableFrom(type);
    }

    @Override
    public void writeTo(JsonObject t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
        MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        log.entering(this.getClass().getName(), "writeTo");
        try (JsonWriter writer = JSON_WRITER_FACTORY.createWriter(nonClosingOutputStream(entityStream))) {
            writer.writeObject(t);
        } catch (JsonException e) {
            log.log(Level.WARNING, "an error occurred during resource serialization", e);
            Response response = buildResponse(
                buildOperationOutcome(Collections.singletonList(
                    buildOperationOutcomeIssue(IssueSeverity.ValueSet.FATAL, IssueType.ValueSet.EXCEPTION, "FHIRProvider: " + e.getMessage(), null))), mediaType);
            throw new WebApplicationException(response);
        } finally {
            log.exiting(this.getClass().getName(), "writeTo");
        }
    }

    @Override
    public long getSize(JsonObject t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }
}
