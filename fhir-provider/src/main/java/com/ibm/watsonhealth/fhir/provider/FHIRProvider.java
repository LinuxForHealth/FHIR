/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.provider;

import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.RuntimeType;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import com.ibm.watsonhealth.fhir.config.FHIRConfigHelper;
import com.ibm.watsonhealth.fhir.config.FHIRConfiguration;
import com.ibm.watsonhealth.fhir.model.IssueSeverityList;
import com.ibm.watsonhealth.fhir.model.IssueTypeList;
import com.ibm.watsonhealth.fhir.model.NarrativeStatusList;
import com.ibm.watsonhealth.fhir.model.ObjectFactory;
import com.ibm.watsonhealth.fhir.model.OperationOutcome;
import com.ibm.watsonhealth.fhir.model.OperationOutcomeIssue;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil.Format;

@Produces({ com.ibm.watsonhealth.fhir.core.MediaType.APPLICATION_JSON_FHIR, MediaType.APPLICATION_JSON,
        com.ibm.watsonhealth.fhir.core.MediaType.APPLICATION_XML_FHIR, MediaType.APPLICATION_XML })
@Consumes({ com.ibm.watsonhealth.fhir.core.MediaType.APPLICATION_JSON_FHIR, MediaType.APPLICATION_JSON,
        com.ibm.watsonhealth.fhir.core.MediaType.APPLICATION_XML_FHIR, MediaType.APPLICATION_XML })
public class FHIRProvider implements MessageBodyReader<Resource>, MessageBodyWriter<Resource> {
    private static final Logger log = Logger.getLogger(FHIRProvider.class.getName());

    @Context
    private UriInfo uriInfo;
    @Context
    private HttpHeaders requestHeaders;
    private RuntimeType runtimeType = null;
    private ObjectFactory objectFactory = new ObjectFactory();

    public FHIRProvider() {
        // default to client runtime type
        this(RuntimeType.CLIENT);
    }

    public FHIRProvider(RuntimeType runtimeType) {
        this.runtimeType = runtimeType;
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        if (RuntimeType.SERVER.equals(runtimeType)) {
            // server specific logic to ensure that this provider gets selected for standard resource types
            String typePathParameter = uriInfo.getPathParameters().getFirst("type");
            return Resource.class.isAssignableFrom(type) && (typePathParameter == null || FHIRUtil.isStandardResourceType(typePathParameter));
        }
        return Resource.class.isAssignableFrom(type);
    }

    @Override
    public Resource readFrom(Class<Resource> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
        InputStream entityStream) throws IOException, WebApplicationException {
        log.entering(this.getClass().getName(), "readFrom");
        try {
            return FHIRUtil.read(type, getFormat(mediaType), new FilterInputStream(entityStream) {
                @Override
                public void close() {
                    // do nothing
                }
            }, isLenient(), isValidating());
        } catch (Exception e) {
            Response response = buildResponse(
                buildOperationOutcome(
                    createIssue(IssueSeverityList.FATAL, IssueTypeList.EXCEPTION, "FHIRProviderException: an error occurred during resource deserialization")
                )
            );
            log.log(Level.WARNING, "an error occurred during resource deserialization", e);
            throw new WebApplicationException(response);
        } finally {
            log.exiting(this.getClass().getName(), "readFrom");
        }
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        if (RuntimeType.SERVER.equals(runtimeType)) {
            // server specific logic to ensure that this provider gets selected for standard resource types
            String typePathParameter = uriInfo.getPathParameters().getFirst("type");
            return (Resource.class.isAssignableFrom(type) && (typePathParameter == null || FHIRUtil.isStandardResourceType(typePathParameter)))
                    || type == OperationOutcome.class;
        }
        return Resource.class.isAssignableFrom(type);
    }

    @Override
    public void writeTo(Resource t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
        OutputStream entityStream) throws IOException, WebApplicationException {
        log.entering(this.getClass().getName(), "writeTo");

        try {
            FHIRUtil.write(t, getFormat(mediaType), new FilterOutputStream(entityStream) {
                @Override
                public void close() {
                    // do nothing
                }
            }, isPretty(requestHeaders));
        } catch (Exception e) {
            Response response = buildResponse(
                buildOperationOutcome(
                    createIssue(
                        IssueSeverityList.FATAL, 
                        IssueTypeList.EXCEPTION, 
                        "FHIRProviderException: an error occurred during resource serialization"
                    )
                )
            );
            log.log(Level.WARNING, "an error occurred during resource serialization", e);
            throw new WebApplicationException(response);
        } finally {
            log.exiting(this.getClass().getName(), "writeTo");
        }
    }

    protected static boolean isPretty(HttpHeaders httpHeaders) {
        // Header evaluation
        String headerValue = httpHeaders.getHeaderString(FHIRConfiguration.DEFAULT_PRETTY_RESPONSE_HEADER_NAME);
        if (headerValue != null) {
            if (Boolean.parseBoolean(headerValue)) {
                //explicitly on in the header
                return true;
            } else if (headerValue.toLowerCase().equals("false")) {
                //explicitly off in the header.  ignore header value if it doesn't specify "true" or false"
                return false;
            }
        }

        // Config evaluation (default false)
        return FHIRConfigHelper.getBooleanProperty(FHIRConfiguration.PROPERTY_DEFAULT_PRETTY_PRINT, false);
    }
    
    protected static boolean isLenient() {
        return FHIRConfigHelper.getBooleanProperty(FHIRConfiguration.PROPERTY_JSON_PARSER_LENIENT, false);
    }
    
    protected static boolean isValidating() {
        return FHIRConfigHelper.getBooleanProperty(FHIRConfiguration.PROPERTY_JSON_PARSER_VALIDATING, true);
    }

    @Override
    public long getSize(Resource t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    private Format getFormat(MediaType mediaType) {
        if (mediaType != null) {
            if (mediaType.isCompatible(com.ibm.watsonhealth.fhir.core.MediaType.APPLICATION_JSON_FHIR_TYPE)
                    || mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE)) {
                return Format.JSON;
            } else if (mediaType.isCompatible(com.ibm.watsonhealth.fhir.core.MediaType.APPLICATION_XML_FHIR_TYPE)
                    || mediaType.isCompatible(MediaType.APPLICATION_XML_TYPE)) {
                return Format.XML;
            }
        }
        return null;
    }
    
    private Response buildResponse(OperationOutcome operationOutcome) {
        Response response = Response.status(Response.Status.BAD_REQUEST)
                .header(HttpHeaders.CONTENT_TYPE, com.ibm.watsonhealth.fhir.core.MediaType.APPLICATION_JSON_FHIR)
                .entity(operationOutcome)
                .build();
        return response;
    }
    
    private OperationOutcome buildOperationOutcome(OperationOutcomeIssue... issues) {
        OperationOutcome outcome = objectFactory.createOperationOutcome()
                .withId(objectFactory.createId().withValue("providerfail"))
                .withText(objectFactory.createNarrative()
                    .withStatus(objectFactory.createNarrativeStatus().withValue(NarrativeStatusList.GENERATED)))
                .withIssue(issues);
        return outcome;
    }
    
    private OperationOutcomeIssue createIssue(IssueSeverityList severity, IssueTypeList code, String diagnostics) {
        OperationOutcomeIssue issue = objectFactory.createOperationOutcomeIssue()
            .withSeverity(objectFactory.createIssueSeverity().withValue(severity))
            .withCode(objectFactory.createIssueType().withValue(code))
            .withDiagnostics(objectFactory.createString().withValue(diagnostics));
        return issue;
    }
}
