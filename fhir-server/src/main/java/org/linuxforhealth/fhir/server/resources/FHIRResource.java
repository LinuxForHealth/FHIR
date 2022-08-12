/*
 * (C) Copyright IBM Corp. 2016, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.resources;

import static org.linuxforhealth.fhir.config.FHIRConfiguration.PROPERTY_UPDATE_CREATE_ENABLED;
import static org.linuxforhealth.fhir.model.type.String.string;
import static org.linuxforhealth.fhir.server.util.FHIRRestSupport.getEtagValue;
import static org.linuxforhealth.fhir.server.util.FHIRRestSupport.getEtagValueFromLocation;
import static org.linuxforhealth.fhir.server.util.IssueTypeToHttpStatusMapper.issueListToStatus;

import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.owasp.encoder.Encode;

import org.linuxforhealth.fhir.config.FHIRConfigHelper;
import org.linuxforhealth.fhir.config.FHIRConfiguration;
import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.config.PropertyGroup;
import org.linuxforhealth.fhir.core.FHIRConstants;
import org.linuxforhealth.fhir.core.FHIRVersionParam;
import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.generator.FHIRGenerator;
import org.linuxforhealth.fhir.model.resource.Bundle;
import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.resource.OperationOutcome.Issue;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.Meta;
import org.linuxforhealth.fhir.model.type.code.IssueSeverity;
import org.linuxforhealth.fhir.model.type.code.IssueType;
import org.linuxforhealth.fhir.model.util.FHIRUtil;
import org.linuxforhealth.fhir.model.util.ModelSupport;
import org.linuxforhealth.fhir.persistence.FHIRPersistence;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.persistence.helper.FHIRPersistenceHelper;
import org.linuxforhealth.fhir.persistence.helper.PersistenceHelper;
import org.linuxforhealth.fhir.search.util.SearchHelper;
import org.linuxforhealth.fhir.server.exception.FHIRRestBundledRequestException;
import org.linuxforhealth.fhir.server.listener.FHIRServletContextListener;
import org.linuxforhealth.fhir.server.resources.filters.FHIRVersionRequestFilter;

import net.jcip.annotations.NotThreadSafe;

/**
 * The base class for JAX-RS "Resource" classes which implement the FHIR HTTP API
 */
@NotThreadSafe
public class FHIRResource {
    private static final Logger log = java.util.logging.Logger.getLogger(FHIRResource.class.getName());

    public static final DateTimeFormatter HTTP_DATETIME_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("EEE")
            .optionalStart()
            // ANSIC date time format for If-Modified-Since
            .appendPattern(" MMM dd HH:mm:ss yyyy")
            .optionalEnd()
            .optionalStart()
            // Touchstone date time format for If-Modified-Since
            .appendPattern(", dd-MMM-yy HH:mm:ss")
            .optionalEnd().toFormatter();

    protected static final String AUDIT_LOGGING_ERR_MSG = "An error occurred while writing the audit log message.";

    private PersistenceHelper persistenceHelper = null;
    private FHIRPersistence persistence = null;

    @Context
    protected ServletContext context;

    @Context
    protected HttpServletRequest httpServletRequest;

    /**
     * UriInfo injected by the JAXRS framework.
     *
     * <p>Use {@link #getRequestUri()} instead to get the original request URI
     * when constructing URIs that will be sent back to the end user.
     */
    @Context
    protected UriInfo uriInfo;

    @Context
    protected SecurityContext securityContext;

    protected SearchHelper searchHelper;

    protected PropertyGroup fhirConfig = null;

    /**
     * This method will do a quick check of the "initCompleted" flag in the servlet context. If the flag is FALSE, then
     * we'll throw an error to short-circuit the current in-progress REST API invocation.
     */
    protected void checkInitComplete() throws FHIROperationException {
        Boolean fhirServerInitComplete =
                (Boolean) context.getAttribute(FHIRServletContextListener.FHIR_SERVER_INIT_COMPLETE);
        if (Boolean.FALSE.equals(fhirServerInitComplete)) {
            String msg =
                    "The FHIR Server web application cannot process requests because it did not initialize correctly";
            throw buildRestException(msg, IssueType.EXCEPTION);
        }
    }

    /**
     * This method will do a quick check of the {type} URL parameter. If the type is not a valid FHIR resource type, then
     * we'll throw an error to short-circuit the current in-progress REST API invocation.
     */
    protected void checkType(String type) throws FHIROperationException {
        if (!ModelSupport.isConcreteResourceType(type)) {
            throw buildUnsupportedResourceTypeException(type);
        }
    }

    public FHIRResource() throws Exception {
        if (log.isLoggable(Level.FINEST)) {
            log.entering(this.getClass().getName(), "FHIRResource ctor");
        }
        try {
            fhirConfig = FHIRConfiguration.getInstance().loadConfiguration();
        } catch (Throwable t) {
            log.severe("Unexpected error during initialization: " + t);
            throw t;
        } finally {
            if (log.isLoggable(Level.FINEST)) {
                log.exiting(this.getClass().getName(), "FHIRResource ctor");
            }
        }
    }

    protected FHIROperationException buildRestException(String msg, IssueType issueType) {
        return buildRestException(msg, issueType, IssueSeverity.FATAL);
    }

    protected FHIROperationException buildRestException(String msg, IssueType issueType, IssueSeverity severity) {
        return new FHIROperationException(msg).withIssue(buildOperationOutcomeIssue(severity, issueType, msg));
    }

    protected long parseIfModifiedSince() {
        // Modified since date time in EpochMilli
        long modifiedSince = -1;
        try {
            // Handle RFC_1123 and RFC_850 formats first.
            // e.g "Sun, 06 Nov 1994 08:49:37 GMT", "Sunday, 06-Nov-94 08:49:37 GMT", "Sunday, 06-Nov-1994 08:49:37 GMT"
            // If 2 digits year is used, then means 1940 to 2039.
            modifiedSince = httpServletRequest.getDateHeader(HttpHeaders.IF_MODIFIED_SINCE);
        } catch (Exception e) {
            if (log.isLoggable(Level.FINE)) {
                String headerVal = httpServletRequest.getHeader(HttpHeaders.IF_MODIFIED_SINCE);
                log.log(Level.FINE, HttpHeaders.IF_MODIFIED_SINCE + " header value '" + headerVal + "' is not valid per rfc1123 or rfc850; "
                        + "continuing with alternate formats.", e);
            }
            try {
                // Then handle ANSIC format, e.g, "Sun Nov  6 08:49:37 1994"
                // and touchStone specific format, e.g, "Sat, 28-Sep-19 16:11:14"
                // assuming the time zone is GMT.
                modifiedSince = HTTP_DATETIME_FORMATTER.parse(httpServletRequest.getHeader(HttpHeaders.IF_MODIFIED_SINCE), LocalDateTime::from)
                        .atZone(ZoneId.of("GMT")).toInstant().toEpochMilli();
            } catch (DateTimeParseException e1) {
                if (log.isLoggable(Level.FINE)) {
                    String headerVal = httpServletRequest.getHeader(HttpHeaders.IF_MODIFIED_SINCE);
                    log.log(Level.FINE, HttpHeaders.IF_MODIFIED_SINCE + " header value '" + headerVal + "' is not valid per rfc1123 or rfc850; "
                            + "continuing without.", e);
                }
                modifiedSince = -1;
            }
        }
        return modifiedSince;
    }

    /**
     * Builds an OperationOutcomeIssue with the respective values for some of the fields.
     */
    protected OperationOutcome.Issue buildOperationOutcomeIssue(IssueSeverity severity, IssueType type, String msg) {
        return OperationOutcome.Issue.builder()
                .severity(severity)
                .code(type)
                .details(CodeableConcept.builder().text(string(msg)).build())
                .build();
    }

    /**
     * This function will build an absolute URI from the specified base URI and relative URI.
     *
     * @param baseUri
     *            the base URI to be used; this will be of the form <scheme>://<host>:<port>/<context-root>
     * @param relativeUri
     *            the path and query parts
     * @return the full URI value as a String
     */
    protected String buildAbsoluteUri(String baseUri, String relativeUri) {
        StringBuilder fullUri = new StringBuilder();
        fullUri.append(baseUri);
        if (!baseUri.endsWith("/")) {
            fullUri.append("/");
        }
        fullUri.append((relativeUri.startsWith("/") ? relativeUri.substring(1) : relativeUri));
        return fullUri.toString();
    }

    /**
     * Adds the Etag and Last-Modified headers to the response from the specified resource (if possible).
     *
     * @param rb
     * @param resource
     * @return
     */
    protected ResponseBuilder addETagAndLastModifiedHeaders(ResponseBuilder rb, Resource resource) {
        if (resource == null || resource.getMeta() == null) {
            log.warning("Skipping ETag and Last-Modified headers due to null resource or missing resource meta");
            return rb;
        }

        Meta meta = resource.getMeta();

        if (meta.getVersionId() == null || meta.getVersionId().getValue() == null) {
            log.warning("ETag not set due to null versionId [resourceType=" + resource.getClass().getSimpleName()
                    + ", id=" + resource.getId() + "]");
        } else {
            rb.header(HttpHeaders.ETAG, getEtagValue(resource));
        }

        if (meta.getLastUpdated() == null || meta.getLastUpdated().getValue() == null) {
            log.warning("Last-Modified not set due to null lastUpdated [resourceType=" + resource.getClass().getSimpleName()
                    + ", id=" + resource.getId() + "]");
        } else {
            // According to 3.3.1 of RTC2616(HTTP/1.1), we MUST use the RFC 1123 format for
            // representing HTTP-date values in header fields, e.g Sat, 28 Sep 2019 16:11:14 GMT
            rb.lastModified(Date.from(resource.getMeta().getLastUpdated().getValue().toInstant()));
        }

        return rb;
    }

    /**
     * Adds the Etag header to the specified response object.
     * @param rb
     * @param fallbackETagVersion
     * @return
     */
    protected ResponseBuilder addHeaders(ResponseBuilder rb, int fallbackETagVersion) {
        return rb.header(HttpHeaders.ETAG, getEtagValue(fallbackETagVersion));
    }

    /**
     * Add the etag header using the version obtained from the locationURI
     * @param rb
     * @param locationURI
     * @return
     */
    protected ResponseBuilder addHeaders(ResponseBuilder rb, URI locationURI) {
        String etag = getEtagValueFromLocation(locationURI);
        if (etag != null) {
            return rb.header(HttpHeaders.ETAG, etag);
        } else {
            return rb;
        }
    }


    protected Response exceptionResponse(FHIRRestBundledRequestException e) {
        Response response;
        if (e.getResponseBundle() != null) {
            if (e.getIssues().size() > 0) {
                // R4 says we should return a single OperationOutcome with the issues:
                // http://www.hl7.org/fhir/r4/http.html#transaction-response
                String msg =
                        "FHIRRestBundledRequestException contains both a response bundle and a list of issues. "
                                + "Only the response bundle will be returned.";
                log.log(Level.WARNING, msg, e);
            }

            List<Bundle.Entry> toAdd = new ArrayList<Bundle.Entry>();
            // replace bundle entries that have an empty response
            for (Bundle.Entry entry : e.getResponseBundle().getEntry()) {
                if (entry.getResponse() != null && entry.getResponse().getStatus() == null) {
                    entry = entry.toBuilder()
                            .response(entry.getResponse().toBuilder()
                                .status(string(Integer.toString(Status.BAD_REQUEST.getStatusCode())))
                                .build())
                            .build();
                }
                toAdd.add(entry);
            }

            Bundle responseBundle = e.getResponseBundle().toBuilder().entry(toAdd).build();

            response = Response.status(Status.OK).entity(responseBundle).build();
        } else {
            // Override the status code with a generic client (400) or server (500) error code
            Status status = issueListToStatus(e.getIssues());
            if (status.getFamily() == Status.Family.CLIENT_ERROR) {
                status = Status.BAD_REQUEST;
            } else {
                status = Status.INTERNAL_SERVER_ERROR;
            }
            response = exceptionResponse(e, status);
        }
        return response;
    }

    protected Response exceptionResponse(FHIROperationException e, Status status) {
        if (status == null) {
            status = issueListToStatus(e.getIssues());
        }

        // Only log full stack trace if server (5xx) error, or if logging level is at least FINE
        if (status.getFamily() == Status.Family.SERVER_ERROR) {
            log.log(Level.SEVERE, e.getMessage(), e);
        } else if (log.isLoggable(Level.FINE)) {
            // purposefully logged at level INFO because in one case we saw the stacktrace
            // suppressed at level FINE, although this can no longer be reproduced
            log.log(Level.INFO, e.getMessage(), e);
        } else if (log.isLoggable(Level.INFO)) {
            log.log(Level.INFO, e.getMessage());
        }

        OperationOutcome operationOutcome = FHIRUtil.buildOperationOutcome(e, false);
        return exceptionResponse(operationOutcome, status);
    }

    protected Response exceptionResponse(Exception e, Status status) {
        log.log(Level.SEVERE, "An unexpected exception occurred while processing the request", e);
        OperationOutcome oo = FHIRUtil.buildOperationOutcome(e, false);
        return this.exceptionResponse(oo, status);
    }

    protected Response exceptionResponse(OperationOutcome oo, Status status) {
        if (log.isLoggable(Level.FINE)) {
            StringBuilder sb = new StringBuilder();
            sb.append("\nOperationOutcome:\n").append(serializeOperationOutcome(oo));
            log.log(Level.FINE, sb.toString());
        }

        // Single location to ensure the OperationOutcome diagnostic strings are encoded for
        // use within HTML, avoiding potential XSS / injection attacks from naive usage.
        // However, it does NOT ensure that other fields in the OperationOutcome (e.g. OperationOutcomeIssue.details.text) are encoded.
        Collection<Issue> currentIssues = oo.getIssue();
        List<Issue> issues = new ArrayList<>();
        for (Issue current : currentIssues) {
            if (current.getDiagnostics() != null) {
                String diagnostics = current.getDiagnostics().getValue();
                issues.add(
                    current.toBuilder()
                       .diagnostics(string(Encode.forHtml(diagnostics)))
                    .build());
            } else {
                issues.add(current);
            }
        }

        return Response.status(status)
                .entity(oo.toBuilder()
                    .issue(issues)
                    .build())
                .build();
    }

    private String serializeOperationOutcome(OperationOutcome oo) {
        try {
            StringWriter sw = new StringWriter();
            FHIRGenerator.generator(Format.JSON, false).generate(oo, sw);
            return sw.toString();
        } catch (Throwable t) {
            return "Error encountered while serializing OperationOutcome resource: "
                    + t.getMessage();
        }
    }

    /**
     * Retrieves the shared persistence helper object from the servlet context.
     */
    private PersistenceHelper getPersistenceHelper() {
        if (persistenceHelper == null) {
            persistenceHelper =
                    (PersistenceHelper) context.getAttribute(FHIRPersistenceHelper.class.getName());
            if (log.isLoggable(Level.FINE)) {
                log.fine("Retrieved FHIRPersistenceHelper instance from servlet context: "
                        + persistenceHelper);
            }
        }
        return persistenceHelper;
    }

    /**
     * Retrieves the shared persistence helper object from the servlet context.
     */
    protected SearchHelper getSearchHelper() {
        if (searchHelper == null) {
            searchHelper =
                    (SearchHelper) context.getAttribute(SearchHelper.class.getName());
            if (log.isLoggable(Level.FINE)) {
                log.fine("Retrieved FHIRPersistenceHelper instance from servlet context: "
                        + persistenceHelper);
            }
        }
        return searchHelper;
    }

    /**
     * Retrieves the persistence implementation to use for the current request.
     * @see {@link PersistenceHelper#getFHIRPersistenceImplementation()}
     */
    protected FHIRPersistence getPersistenceImpl() throws FHIRPersistenceException {
        if (persistence == null) {
            persistence = getPersistenceHelper().getFHIRPersistenceImplementation();
            if (log.isLoggable(Level.FINE)) {
                log.fine("Obtained new FHIRPersistence instance: " + persistence);
            }
        }
        return persistence;
    }

    protected boolean isDeleteSupported() throws FHIRPersistenceException {
        return getPersistenceImpl().isDeleteSupported();
    }

    protected Boolean isUpdateCreateEnabled() {
        return FHIRConfigHelper.getBooleanProperty(PROPERTY_UPDATE_CREATE_ENABLED, Boolean.TRUE);
    }

    /**
     * Get the original request URI from either the HttpServletRequest or a configured Header (in case of re-writing proxies).
     *
     * <p>When the 'fhirServer/core/originalRequestUriHeaderName' property is empty, this method returns the equivalent of
     * uriInfo.getRequestUri().toString(), except that uriInfo.getRequestUri() will throw an IllegalArgumentException
     * when the query string portion contains a vertical bar | character. The vertical bar is one known case of a special character
     * causing the exception. There could be others.
     *
     * @return String The complete request URI
     * @throws Exception if an error occurs while reading the config
     */
    protected String getRequestUri() throws Exception {
        return FHIRRequestContext.get().getOriginalRequestUri();
    }

    /**
     * This method simply returns a URI object containing the specified URI string.
     *
     * @param uriString
     *            the URI string for which the URI object will be created
     * @throws URISyntaxException
     */
    protected URI toUri(String uriString) throws URISyntaxException {
        return new URI(uriString);
    }

    protected FHIROperationException buildUnsupportedResourceTypeException(String resourceTypeName) {
        String msg = "'" + Encode.forHtml(resourceTypeName) + "' is not a valid resource type.";
        Issue issue = OperationOutcome.Issue.builder()
                .severity(IssueSeverity.FATAL)
                .code(IssueType.NOT_SUPPORTED.toBuilder()
                        .extension(Extension.builder()
                            .url(FHIRConstants.EXT_BASE + "not-supported-detail")
                            .value(Code.of("resource"))
                            .build())
                        .build())
                .details(CodeableConcept.builder().text(string(msg)).build())
                .build();
        return new FHIROperationException(msg).withIssue(issue);
    }

    /**
     * The FHIRVersion to use for the current request
     *
     * @return the corresponding FHIRVersion for the org.linuxforhealth.fhir.server.fhirVersion request context attribute
     */
    protected FHIRVersionParam getFhirVersion() {
        FHIRVersionParam fhirVersion = (FHIRVersionParam) httpServletRequest.getAttribute(FHIRVersionRequestFilter.FHIR_VERSION_PROP);
        if (fhirVersion == null) {
            log.warning("Missing request context attribute " + FHIRVersionRequestFilter.FHIR_VERSION_PROP + "; using 4.0");
            fhirVersion = FHIRVersionParam.VERSION_40;
        }
        return fhirVersion;
    }
}
