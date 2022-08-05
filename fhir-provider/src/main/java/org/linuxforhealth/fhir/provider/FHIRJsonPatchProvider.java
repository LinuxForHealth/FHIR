/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.provider;

import static org.linuxforhealth.fhir.model.util.FHIRUtil.buildOperationOutcome;
import static org.linuxforhealth.fhir.model.util.FHIRUtil.buildOperationOutcomeIssue;
import static org.linuxforhealth.fhir.model.util.JsonSupport.nonClosingInputStream;
import static org.linuxforhealth.fhir.model.util.JsonSupport.nonClosingOutputStream;
import static org.linuxforhealth.fhir.provider.util.FHIRProviderUtil.buildResponse;
import static org.linuxforhealth.fhir.provider.util.FHIRProviderUtil.getMediaType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonException;
import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;
import jakarta.json.JsonWriter;
import jakarta.json.JsonWriterFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.RuntimeType;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import org.linuxforhealth.fhir.core.FHIRMediaType;
import org.linuxforhealth.fhir.model.type.code.IssueSeverity;
import org.linuxforhealth.fhir.model.type.code.IssueType;

@Consumes({ FHIRMediaType.APPLICATION_JSON_PATCH })
@Produces({ FHIRMediaType.APPLICATION_JSON_PATCH })
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
        try (JsonReader reader = JSON_READER_FACTORY.createReader(nonClosingInputStream(entityStream))) {
            return reader.readArray();
        } catch (JsonException e) {
            if (RuntimeType.SERVER.equals(runtimeType)) {
                String acceptHeader = httpHeaders.getFirst(HttpHeaders.ACCEPT);
                Response response = buildResponse(
                    buildOperationOutcome(Collections.singletonList(
                        buildOperationOutcomeIssue(IssueSeverity.FATAL, IssueType.INVALID, "FHIRProvider: " + e.getMessage(), null))), getMediaType(acceptHeader));
                throw new WebApplicationException(response);
            } else {
                throw new IOException("an error occurred during JSON Patch deserialization", e);
            }
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
        try (JsonWriter writer = JSON_WRITER_FACTORY.createWriter(nonClosingOutputStream(entityStream))) {
            writer.writeArray(t);
        } catch (JsonException e) {
            // log the error but don't throw because that seems to block to original IOException from bubbling for some reason
            log.log(Level.WARNING, "an error occurred during JSON Patch serialization", e);
            if (RuntimeType.SERVER.equals(runtimeType)) {
                String acceptHeader = (String) httpHeaders.getFirst(HttpHeaders.ACCEPT);
                Response response = buildResponse(
                    buildOperationOutcome(Collections.singletonList(
                        buildOperationOutcomeIssue(IssueSeverity.FATAL, IssueType.EXCEPTION, "FHIRProvider: " + e.getMessage(), null))), getMediaType(acceptHeader));
                throw new WebApplicationException(response);
            }
        } finally {
            log.exiting(this.getClass().getName(), "writeTo");
        }
    }

    @Override
    public long getSize(JsonArray t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }
}
