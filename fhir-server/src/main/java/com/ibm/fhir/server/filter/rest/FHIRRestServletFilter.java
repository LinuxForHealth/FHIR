/*
 * (C) Copyright IBM Corp. 2016, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.filter.rest;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.owasp.encoder.Encode;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.core.HTTPHandlingPreference;
import com.ibm.fhir.core.HTTPReturnPreference;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.server.exception.FHIRRestServletRequestException;

/**
 * This class is a servlet filter which is registered with the REST API's servlet. The main purpose of the class is to
 * log entry/exit information and elapsed time for each REST API request processed by the server.
 */
public class FHIRRestServletFilter extends HttpFilter {
    private static final long serialVersionUID = 1L;

    private static final Logger log = Logger.getLogger(FHIRRestServletFilter.class.getName());

    private static String tenantIdHeaderName = null;
    private static String datastoreIdHeaderName = null;
    private static String originalRequestUriHeaderName = null;
    private static final String preferHeaderName = "Prefer";
    private static final String preferHandlingHeaderSectionName = "handling";
    private static final String preferReturnHeaderSectionName = "return";

    private static String defaultTenantId = null;
    private static final HTTPReturnPreference defaultHttpReturnPref = HTTPReturnPreference.MINIMAL;

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (log.isLoggable(Level.FINE)) {
            log.entering(this.getClass().getName(), "doFilter");
        }

        long initialTime = System.nanoTime();

        String tenantId = defaultTenantId;
        String dsId = FHIRConfiguration.DEFAULT_DATASTORE_ID;
        String requestUrl = getRequestURL(request);
        String originalRequestUri = getRequestURL(request);

        // Wrap the incoming servlet request with our own implementation.
        request = new FHIRHttpServletRequestWrapper(request);
        if (log.isLoggable(Level.FINEST)) {
            log.finest("Wrapped HttpServletRequest object...");
        }

        String t = request.getHeader(tenantIdHeaderName);
        if (t != null) {
            tenantId = t;
        }

        t = request.getHeader(datastoreIdHeaderName);
        if (t != null) {
            dsId = t;
        }

        t = getOriginalRequestURI(request);
        if (t != null) {
            originalRequestUri = t;
        }

        // Log a "request received" message.
        StringBuffer requestDescription = new StringBuffer();
        requestDescription.append("tenantId:[");
        requestDescription.append(tenantId);
        requestDescription.append("] dsId:[");
        requestDescription.append(dsId);
        requestDescription.append("] user:[");
        requestDescription.append(getRequestUserPrincipal(request));
        requestDescription.append("] method:[");
        requestDescription.append(getRequestMethod(request));
        requestDescription.append("] uri:[");
        requestDescription.append(requestUrl);
        if (!requestUrl.equals(originalRequestUri)) {
            requestDescription.append("] originalUri:[");
            requestDescription.append(originalRequestUri);
        }
        requestDescription.append("]");
        String encodedRequestDescription = Encode.forHtml(requestDescription.toString());
        log.info("Received request: " + encodedRequestDescription);

        int statusOnException = HttpServletResponse.SC_BAD_REQUEST;

        try {
            // Checks for Valid Tenant Configuration
            checkValidTenantConfiguration(tenantId);

            // Create a new FHIRRequestContext and set it on the current thread.
            FHIRRequestContext context = new FHIRRequestContext(tenantId, dsId);
            // Don't try using FHIRConfigHelper before setting the context!
            FHIRRequestContext.set(context);

            context.setOriginalRequestUri(originalRequestUri);

            // Set the request headers.
            extractRequestHeaders(context, request);

            // Set the handling preference.
            computeHandlingPref(context);

            // Set the return preference.
            computeReturnPref(context);

            // Check the FHIR version parameter.
            try {
                checkFhirVersionParameter(context);
            } catch (FHIRRestServletRequestException e) {
                statusOnException = e.getHttpStatusCode();
                throw e;
            }

            // Pass the request through to the next filter in the chain.
            chain.doFilter(request, response);
        } catch (Exception e) {
            log.log(Level.INFO, "Error while setting request context or processing request", e);

            OperationOutcome outcome = FHIRUtil.buildOperationOutcome(e, IssueType.INVALID, IssueSeverity.FATAL, false);

            response.setStatus(statusOnException);

            Format format = chooseResponseFormat(request.getHeader(HttpHeaders.ACCEPT));
            switch (format) {
            case XML:
                response.setContentType(com.ibm.fhir.core.FHIRMediaType.APPLICATION_FHIR_XML);
                break;
            case JSON:
            default:
                response.setContentType(com.ibm.fhir.core.FHIRMediaType.APPLICATION_FHIR_JSON);
                break;
            }

            try {
                FHIRGenerator.generator( format, false).generate(outcome, response.getWriter());
            } catch (FHIRException e1) {
                throw new ServletException(e1);
            }
        } finally {
            // If possible, include the status code in the "completed" message.
            StringBuffer statusMsg = new StringBuffer();
            if (response instanceof HttpServletResponse) {
                int status = response.getStatus();
                statusMsg.append(" status:[" + status + "]");
            } else {
                statusMsg.append(" status:[unknown (non-HTTP request)]");
            }

            double elapsedSecs = (System.nanoTime() - initialTime) / 1000000000.0;
            log.info("Completed request[" + elapsedSecs + " secs]: " + encodedRequestDescription + statusMsg.toString());

            // Remove the FHIRRequestContext from the current thread.
            FHIRRequestContext.remove();

            if (log.isLoggable(Level.FINE)) {
                log.exiting(this.getClass().getName(), "doFilter");
            }
        }
    }

    /*
     * Checks that the tenant has a valid configuration.
     *
     * @param tenantId the tenant that's configuration is going to be checked
     * @throws Exception
     */
    private void checkValidTenantConfiguration(String tenantId) throws Exception {
        try {
            PropertyGroup fhirConfig = FHIRConfiguration.getInstance().loadConfigurationForTenant(tenantId);
            if(fhirConfig == null) {
                log.severe("Missing tenant configuration for '"  + tenantId + "'");
                throw new FHIRException("Tenant configuration does not exist: " + tenantId);
            }
        } catch (FHIRException fe) {
            throw fe;
        } catch (Throwable t) {
            String msg = "Unexpected error while retrieving configuration.";
            log.severe(msg + " " + t);
            throw new Exception(msg);
        }
    }

    /**
     * Extracts a map of HTTP request headers, keyed by header name, and sets them in the request context.
     */
    private void extractRequestHeaders(FHIRRequestContext context, HttpServletRequest request) {
        // Uses LinkedHashMap just to preserve the order.
        Map<String, List<String>> requestHeaders = new LinkedHashMap<>();
        List<String> headerNames = Collections.list(request.getHeaderNames());
        for (String headerName : headerNames) {
            requestHeaders.put(headerName, Collections.list(request.getHeaders(headerName)));
        }
        context.setHttpHeaders(requestHeaders);
    }

    /**
     * Computes the handling preference from the Prefer header value.
     * The default handling preference for all interactions is STRICT.
     * @param context the context containing the HTTP headers
     * @throws FHIRException
     */
    private void computeHandlingPref(FHIRRequestContext context) throws FHIRException {
        HTTPHandlingPreference handlingPref = HTTPHandlingPreference.from(FHIRConfigHelper.getStringProperty(FHIRConfiguration.PROPERTY_DEFAULT_HANDLING, HTTPHandlingPreference.STRICT.value()));
        boolean allowClientHandlingPref = FHIRConfigHelper.getBooleanProperty(FHIRConfiguration.PROPERTY_ALLOW_CLIENT_HANDLING_PREF, true);
        if (allowClientHandlingPref) {
            String handlingPrefString = getHeaderTokenValue(context, preferHeaderName, preferHandlingHeaderSectionName);
            if (handlingPrefString != null) {
                try {
                    handlingPref = HTTPHandlingPreference.from(handlingPrefString);
                } catch (IllegalArgumentException e) {
                    String message = "Invalid HTTP handling preference 'handling=" + handlingPrefString + "' passed in header 'Prefer'";
                    if (handlingPref == HTTPHandlingPreference.STRICT) {
                        throw new FHIRException(message + "; use 'handling=strict' or 'handling=lenient'.");
                    } else {
                        log.fine(message + "; using 'handling=" + handlingPref.value() + "'.");
                    }
                }
            }
        }
        context.setHandlingPreference(handlingPref);
    }

    /**
     * Computes the return preference from the Prefer header value. The default return
     * preference for all interactions except system history is MINIMAL. The default for
     * system history is different and uses REPRESENTATION. To implement this without
     * disrupting the existing behavior we use a new returnPreferenceDefault flag
     * to indicate whether or not the default value has been overridden by a
     * client-specified header value.
     * The handling preference should be set in the context before calling this method.
     * @param context the context containing the HTTP headers
     * @throws FHIRException
     */
    private void computeReturnPref(FHIRRequestContext context) throws FHIRException {
        HTTPReturnPreference returnPref = defaultHttpReturnPref;
        boolean isDefault = true;
        String returnPrefString = getHeaderTokenValue(context, preferHeaderName, preferReturnHeaderSectionName);
        if (returnPrefString != null) {
            try {
                returnPref = HTTPReturnPreference.from(returnPrefString);
                isDefault = false;

                if (log.isLoggable(Level.FINE)) {
                    log.fine("Requested return preference = " + returnPref);
                }
            } catch (IllegalArgumentException e) {
                String message = "Invalid HTTP return preference 'return=" + returnPrefString + "' passed in header 'Prefer'";
                if (context.getHandlingPreference() == HTTPHandlingPreference.STRICT) {
                    throw new FHIRException(message + "; use 'return=minimal', 'return=representation', or 'return=OperationOutcome'.");
                } else {
                    log.fine(message + "; using 'return=" + returnPref.value() + "'.");
                }
            }
        }
        context.setReturnPreference(returnPref);
        context.setReturnPreferenceDefault(isDefault);
    }

    /**
     * Gets the value (without parameters) of the first occurrence of a token name in an HTTP header.
     * @param context the context containing the HTTP headers
     * @param headerName the header name
     * @param tokenName the token name
     * @return the value, or null if no match found
     */
    private String getHeaderTokenValue(FHIRRequestContext context, String headerName, String tokenName) {
        String preferValue = null;
        for (String curHeaderName : context.getHttpHeaders().keySet()) {
            if (curHeaderName.equalsIgnoreCase(headerName)) {
                for (String headerValue : context.getHttpHeaders().getOrDefault(curHeaderName, Collections.emptyList())) {
                    for (String headerValueElement : headerValue.split(",")) {
                        headerValueElement = headerValueElement.split(";")[0];
                        String[] tokenAndValue = headerValueElement.split("=", 2);
                        if (tokenName.equals(tokenAndValue[0].trim())) {
                            preferValue = "";
                            if (tokenAndValue.length > 1) {
                                preferValue = tokenAndValue[1].trim();
                            }
                            break;
                        }
                    }
                    if (preferValue != null) {
                        break;
                    }
                }
            }
            if (preferValue != null) {
                break;
            }
        }
        return preferValue;
    }

    /**
     * Check that FHIR version parameters in the HTTP headers are valid.
     * @param context the context containing the HTTP headers
     * @throws FHIRRestServletRequestException
     */
    void checkFhirVersionParameter(FHIRRequestContext context) throws FHIRRestServletRequestException {
        Map<String, Integer> headerStatusMap = new LinkedHashMap<>();
        // 415 Unsupported Media Type is the appropriate response when the client posts a format that is not supported to the server.
        headerStatusMap.put(HttpHeaders.CONTENT_TYPE, HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
        // 406 Not Acceptable is the appropriate response when the Accept header requests a format that the server does not support.
        headerStatusMap.put(HttpHeaders.ACCEPT, HttpServletResponse.SC_NOT_ACCEPTABLE);

        for (String headerName : headerStatusMap.keySet()) {
            String fhirVersion = null;
            for (String curHeaderName : context.getHttpHeaders().keySet()) {
                if (curHeaderName.equalsIgnoreCase(headerName)) {
                    for (String headerValue : context.getHttpHeaders().getOrDefault(curHeaderName, Collections.emptyList())) {
                        for (String headerValueElement : headerValue.split(",")) {
                            for (Map.Entry<String, String> parameter : MediaType.valueOf(headerValueElement).getParameters().entrySet()) {
                                if (FHIRMediaType.FHIR_VERSION_PARAMETER.equalsIgnoreCase(parameter.getKey())) {
                                    String curFhirVersion = parameter.getValue();
                                    if (curFhirVersion != null && !FHIRMediaType.SUPPORTED_FHIR_VERSIONS.contains(curFhirVersion)) {
                                        throw new FHIRRestServletRequestException("Invalid '" + FHIRMediaType.FHIR_VERSION_PARAMETER
                                            + "' parameter value in '" + headerName + "' header; the following FHIR versions are supported: "
                                                + FHIRMediaType.SUPPORTED_FHIR_VERSIONS, headerStatusMap.get(headerName));
                                    }
                                    // If Content-Type header, check for multiple different FHIR versions
                                    if (headerName.equalsIgnoreCase(HttpHeaders.CONTENT_TYPE)) {
                                        if (fhirVersion != null && !fhirVersion.equals(curFhirVersion)) {
                                            throw new FHIRRestServletRequestException("Multiple different '" + FHIRMediaType.FHIR_VERSION_PARAMETER
                                                + "' parameter values in '" + headerName + "' header", headerStatusMap.get(headerName));
                                        }
                                        fhirVersion = curFhirVersion;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private Format chooseResponseFormat(String acceptableContentTypes) {
        if (acceptableContentTypes.contains(com.ibm.fhir.core.FHIRMediaType.APPLICATION_FHIR_JSON) ||
                acceptableContentTypes.contains(MediaType.APPLICATION_JSON)) {
            return Format.JSON;
        } else if (acceptableContentTypes.contains(com.ibm.fhir.core.FHIRMediaType.APPLICATION_FHIR_XML) ||
                acceptableContentTypes.contains(MediaType.APPLICATION_XML)) {
            return Format.XML;
        } else {
            return Format.JSON;
        }
    }

    /**
     * Retrieves the username associated with the HTTP request.
     */
    private String getRequestUserPrincipal(ServletRequest request) {
        String user = null;

        if (request instanceof HttpServletRequest) {
            Principal principal = ((HttpServletRequest) request).getUserPrincipal();
            if (principal != null) {
                user = principal.getName();
            }
        }
        return (user != null ? user : "<unauthenticated>");
    }

    /**
     * Returns the HTTP method name associated with the specified request.
     */
    private String getRequestMethod(ServletRequest request) {
        String method = null;

        if (request instanceof HttpServletRequest) {
            method = ((HttpServletRequest) request).getMethod();
        }
        return (method != null ? method : "<unknown>");
    }

    /**
     * Returns the full request URL (i.e. http://host:port/a/path?queryString) associated with the specified request.
     */
    private String getRequestURL(HttpServletRequest request) {
        StringBuffer sb = request.getRequestURL();
        String queryString = request.getQueryString();
        if (queryString != null && !queryString.isEmpty()) {
            sb.append("?");
            sb.append(queryString);
        }
        return sb.toString();
    }

    /**
     * Get the original request URL from either the HttpServletRequest or a configured Header (in case of re-writing proxies).
     *
     * If configured as a header, the header value will only be used for the protocol, server name, port number, and server path;
     * the query string will come from the HttpServletRequest.
     *
     * @param request
     * @return String The full request URL
     */
    private String getOriginalRequestURI(HttpServletRequest request) {
        StringBuilder requestUriBuilder = null;

        // First, check the configured header for the original request URI (in case any proxies have overwritten the user-facing URL)
        if (originalRequestUriHeaderName != null) {
            String originalRequestUriString = request.getHeader(originalRequestUriHeaderName);
            if (originalRequestUriString != null && !originalRequestUriString.isEmpty()) {
                try {
                    String uriStringSansQuery = originalRequestUriString.contains("?") ?
                            originalRequestUriString.substring(0, originalRequestUriString.indexOf('?')) :
                            originalRequestUriString;
                    // Try to parse it as a URI to ensure its valid
                    URI originalRequestUri = new URI(uriStringSansQuery);

                    // If its not absolute, then construct an absolute URI (or else JAX-RS will append the path to the current baseUri)
                    if (originalRequestUri.isAbsolute()) {
                        requestUriBuilder = new StringBuilder(uriStringSansQuery);
                    } else {
                        requestUriBuilder = new StringBuilder(UriBuilder.fromUri(getRequestURL(request))
                            .replacePath(originalRequestUri.getPath()).build().toString());
                    }
                } catch (Exception e) {
                    log.log(Level.WARNING, "Error while computing the original request URI", e);
                }
            }
        }

        // If there was no configured header or the header wasn't present, construct it from the HttpServletRequest
        if (requestUriBuilder == null || requestUriBuilder.toString().isEmpty()) {
            requestUriBuilder = new StringBuilder(request.getRequestURL());
        }

        // Append the query string from the HttpServletRequest
        String queryString = request.getQueryString();
        if (queryString != null && !queryString.isEmpty()) {
            requestUriBuilder.append("?").append(queryString);
        }

        return requestUriBuilder.toString();
    }

    @Override
    public void destroy() {
        // Nothing to do here...
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        try {
            PropertyGroup config = FHIRConfiguration.getInstance().loadConfiguration();
            if (config == null) {
                throw new IllegalStateException("No FHIRConfiguration was found");
            }

            tenantIdHeaderName = config.getStringProperty(FHIRConfiguration.PROPERTY_TENANT_ID_HEADER_NAME,
                    FHIRConfiguration.DEFAULT_TENANT_ID_HEADER_NAME);
            log.info("Configured tenant-id header name is: " +  tenantIdHeaderName);

            datastoreIdHeaderName = config.getStringProperty(FHIRConfiguration.PROPERTY_DATASTORE_ID_HEADER_NAME,
                    FHIRConfiguration.DEFAULT_DATASTORE_ID_HEADER_NAME);
            log.info("Configured datastore-id header name is: " +  datastoreIdHeaderName);

            originalRequestUriHeaderName = config.getStringProperty(FHIRConfiguration.PROPERTY_ORIGINAL_REQUEST_URI_HEADER_NAME,
                    null);
            log.info("Configured original-request-uri header name is: " +  originalRequestUriHeaderName);

            defaultTenantId =
                    config.getStringProperty(FHIRConfiguration.PROPERTY_DEFAULT_TENANT_ID, FHIRConfiguration.DEFAULT_TENANT_ID);
            log.info("Configured default tenant-id value is: " +  defaultTenantId);
        } catch (Exception e) {
            throw new ServletException("Servlet filter initialization error.", e);
        }
    }
}
