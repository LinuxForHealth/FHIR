/*
 * (C) Copyright IBM Corp. 2016, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.filter.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.HttpHeaders;

import org.owasp.encoder.Encode;

import com.ibm.fhir.core.FHIRConstants;
import com.ibm.fhir.core.FHIRMediaType;

/**
 * This class is used to wrap an HttpServletRequest instance. The main purpose is to implement common behaviors for the
 * FHIR REST API layer. For example, we will initially use this class to support the "_format" query parameter as an
 * alternative to the "Accept" HTTP header.
 */
public class FHIRHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private static final Logger log = Logger.getLogger(FHIRHttpServletRequestWrapper.class.getName());

    public static final String UTF8 = "utf-8";
    public static final String UTF16 = "utf-16";
    public static final String DEFAULT_ACCEPT_HEADER_VALUE = FHIRMediaType.APPLICATION_FHIR_JSON;
    public static final String HEADER_X_METHOD_OVERRIDE = "X-Method-Override";
    public static final String CHARSET = "charset";
    public static final String ACCEPT = "Accept";
    public static final String ACCEPT_CHARSET = "Accept-Charset";

    // The real HttpServletRequest instance that we'll delegate to.
    private HttpServletRequest delegate;

    // queryString that is built if form usage is detected
    private String queryString = null;

    // This map contains header values that were specified as part of the query string.
    private Map<String, String> headerQueryParameters;

    // This map will map http header names to query parameter names.
    // This allows us to support certain http headers to be specified via the query string.
    private static Map<String, String> headerNameMappings;

    // This map allows us to use default values for selected HTTP headers.
    private static Map<String, String> defaultHeaderValues;

    // This map allows us to implement shortcuts/aliases for the values specified for the _format query parameter.
    private static Map<String, String> _formatShortcuts;

    static {
        initHeaderNameMappings();
        initDefaultHeaderValues();
        initFormatShortcuts();
    }

    public FHIRHttpServletRequestWrapper(HttpServletRequest req) {
        super(req);
        delegate = req;

        if (log.isLoggable(Level.FINER)) {
            log.finer("Creating FHIRHttpServletRequestWrapper for HttpServletRequest: " +
                    Encode.forHtml(req.toString()));
        }

        // Extra query parameters that can override HTTP headers.
        initQueryParameterValues(req);

        String contentType = req.getContentType();
        log.finer("Content-Type is " + Encode.forHtml(contentType));

        // If parameters are contained in a form, then be sure to pull them out and add them to our
        // set of query parameters.
        if (contentType != null && contentType.contains(FHIRMediaType.APPLICATION_FORM_URLENCODED)) {
            if (log.isLoggable(Level.FINER)) {
                log.finer("Detected " + FHIRMediaType.APPLICATION_FORM_URLENCODED);
            }
            formParameters(req);
        }
    }

    /**
     * This map will contain the "aliases" that we support as values of the _format
     * query parameter (overrides the Accept header value).
     */
    private static void initFormatShortcuts() {
        _formatShortcuts = new HashMap<>();
        _formatShortcuts.put("xml", FHIRMediaType.APPLICATION_FHIR_XML);
        _formatShortcuts.put("application/xml", FHIRMediaType.APPLICATION_FHIR_XML);
        _formatShortcuts.put("application/fhir+xml", FHIRMediaType.APPLICATION_FHIR_XML);
        _formatShortcuts.put("text/xml", FHIRMediaType.APPLICATION_FHIR_XML);
        _formatShortcuts.put("json", FHIRMediaType.APPLICATION_FHIR_JSON);
        _formatShortcuts.put("application/json", FHIRMediaType.APPLICATION_FHIR_JSON);
        _formatShortcuts.put("application/fhir+json", FHIRMediaType.APPLICATION_FHIR_JSON);
    }

    /**
     * Initialize any default HTTP header values that we need to support.
     */
    private static void initDefaultHeaderValues() {
        defaultHeaderValues = new HashMap<>();
        defaultHeaderValues.put("accept", DEFAULT_ACCEPT_HEADER_VALUE);
    }

    /**
     * This method is responsible for extracting specific query parameters from the request URI and storing them in a
     * Map for use later by our "getHeader" type methods.
     */
    private void initQueryParameterValues(HttpServletRequest req) {
        headerQueryParameters = new HashMap<>();
        for (Map.Entry<String,String> mapEntry : headerNameMappings.entrySet()) {
            String headerName = mapEntry.getKey();
            String queryParameterName = mapEntry.getValue();
            String headerValue = req.getParameter(queryParameterName);
            if (headerValue != null) {
                headerQueryParameters.put(headerName, headerValue);
            }
        }

        if (log.isLoggable(Level.FINER)) {
            log.finer("Retrieved these 'header' query parameters from the request URI: " +
                    Encode.forHtml(headerQueryParameters.toString()));
        }
    }

    /**
     * This method is responsible for initializing our mapping of HTTP header names to query parameter names. We'll use
     * this mapping when retrieving header values.
     */
    private static void initHeaderNameMappings() {
        headerNameMappings = new HashMap<>();

        //                  header name, query parameter name
        headerNameMappings.put("accept", "_format");
        headerNameMappings.put("x-method-override", "x-method-override");
        headerNameMappings.put("x-http-method-override", "x-http-method-override");

    }

    /**
     * Called when the parameters are in a form. The parameters are appended to the query String
     *
     * @param req
     *            HTTpServletRequest
     */
    private void formParameters(HttpServletRequest req) {
        // Get the original queury String
        String originalQueryString = req.getQueryString();
        if (log.isLoggable(Level.FINER)) {
            log.finer("Processing form parameters, original queryString is " +
                    Encode.forHtml(originalQueryString));
        }

        // Append each parameter to the end of the new queryString
        // Note that the parameter map contains the form values and the query
        // string values
        queryString = "";
        String separator = "";
        Map<String, String[]> paramMap = req.getParameterMap();
        try {
            for (Map.Entry<String, String[]> paramEntry : paramMap.entrySet()) {
                String key = paramEntry.getKey();
                key = URLEncoder.encode(key, UTF8);
                if (log.isLoggable(Level.FINEST)) {
                    log.finest("parameter name: " + Encode.forHtml(key));
                }
                String[] values = paramEntry.getValue();
                if (values != null) {
                    // There can be multiple values for each key.
                    // I am going to write out each key=value separately.
                    // The implementation will choose the correct setting
                    // (probably the first in most cases).
                    for (String value : values) {
                        value = URLEncoder.encode(value, UTF8);
                        if (log.isLoggable(Level.FINEST)) {
                            log.finest("parameter value: " + Encode.forHtml(value));
                        }
                        queryString += separator + key + "=" + value;
                        separator = "&";
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            // This is an unanticipated exception since we are using UTF-8
            // encoding
            log.severe("Unexpected error while processing form parameters: " + e);
            throw new RuntimeException(e);
        }

        if (log.isLoggable(Level.FINER)) {
            log.finer("After processing form parameters, queryString is " +
                    Encode.forHtml(queryString));
        }
    }


    /**
     * This function is called to modified the accept header to add the missing charset setting,
     * the content of the updated accept header will be used in content-type header of the response by the javax
     * framework.
     * This function fixes the missing charset errors which are caused by:
     * (1) charset is defined in "Accept-Charset" header instead of in "Accept" header.
     * (2) _format overrides json/xml only, but charset is defined in either "Accept-Charset" or "Accept" header.
     * @param s
     * @return
     */
    private String updateAcceptHeader(String s) {
        if (s!= null && !s.contains(CHARSET)) {
            String originalHeaderValue = delegate.getHeader(ACCEPT);
            if (originalHeaderValue != null && originalHeaderValue.contains(CHARSET)) {
                s = s + ";" + originalHeaderValue.substring(originalHeaderValue.indexOf(CHARSET));
            } else {
                String originalAcceptCharset = delegate.getHeader(ACCEPT_CHARSET);
                if (originalAcceptCharset != null) {
                    s = s + ";" + CHARSET + "=" + originalAcceptCharset;
                }
            }
        }
        return s;
    }

    /**
     * This method allows us to support overriding of HTTP headers with query parameters. For example, if this
     * method is called for the "Accept" header, we'll allow the "_format" query parameter to act as an override for the
     * HTTP header value. We support this behavior for several HTTP headers. They are inserted into the
     * "headerNameMappings" map defined above.
     *
     * Also for selected HTTP headers, we'll support a default value in the event that no value is specified via the
     * HTTP request header or via the query string.
     *
     * Finally, if headerName includes a ":" we interpret that as a request for the value of a specific part of a complex header.
     * For example, given a header value like:
     * <pre>
     * X-TEST: part1=a;part2=multipart;part3=value;
     * </pre>
     * getHeader("X-TEST:part2") would return "multipart".
     *
     * @param headerName
     *            the name of the HTTP header to be retrieved
     * @return the value of the specified header
     * @see javax.servlet.http.HttpServletRequest#getHeader(java.lang.String)
     */
    @Override
    public String getHeader(String headerName) {
        if (headerName == null) {
            throw new IllegalArgumentException();
        }

        String s = null;

        if (headerName.contains(":")) {
            // If the header name contains a ":", interpret that as a request for just a part of the header
            String[] splitHeaderName = headerName.split(":", 2);
            String realHeaderName = splitHeaderName[0].trim();
            String partName = splitHeaderName[1].trim();

            String fullHeaderValue = delegate.getHeader(realHeaderName);
            if (fullHeaderValue != null) {
                String[] parts = fullHeaderValue.split(";");
                for (int i = 0; i < parts.length; i++) {
                    String[] splitPart = parts[i].split("=", 2);
                    if (partName.equals(splitPart[0].trim()) && splitPart.length == 2) {
                        s = splitPart[1].trim();
                        break;
                    }
                }
            }
        } else {

            String headerNameLC = headerName.toLowerCase();

            // First, check to see if the user specified this header as a query parameter.
            String queryValue = headerQueryParameters.get(headerNameLC);
            if (queryValue != null && !queryValue.isEmpty()) {
                s = queryValue;
                s = possiblyMapQueryParameterValue(headerNameLC, s);
            }

            // If we didn't find an override value via the query string, then call
            // our delegate.
            if (s == null) {
                s = delegate.getHeader(headerName);
            }

            // Finally, if we still don't have a value for the requested header,
            // then check to see if we should return a default value for this header.
            if (s == null || s.isEmpty()) {
                String defaultValue = defaultHeaderValues.get(headerNameLC);
                if (defaultValue != null) {
                    s = defaultValue;
                }
            }
        }

        // Add charset according to the Accept|Accept-Charset header of the request
        if (headerName.equalsIgnoreCase(ACCEPT)) {
            s = updateAcceptHeader(s);
        }

        if (log.isLoggable(Level.FINEST)) {
            log.finest("getHeader(\"" + headerName + "\"): " + s);
        }

        return s;
    }

    /**
     * For specific request headers specified as a query parameter (e.g. "accept/_format"), we'll attempt
     * to map the input value (specified by the user in the URI string) to a more official value.
     *
     * @param headerName
     *            the name of the request header that was specified as a query parameter
     * @param value
     *            the value of the query parameter (header value)
     * @return a possibly mapped value or the original value if no mapping exists
     */
    private String possiblyMapQueryParameterValue(String headerName, String value) {
        if (headerName.equals("accept")) {

            String mappedValue = _formatShortcuts.get(value);

            // We've entered a special situation where the value may have been remapped via the next step
            // in the jaxrs framework
            if (mappedValue == null) {
                boolean addCharset = false;
                String delegateQueryString = delegate.getQueryString();
                if(delegateQueryString.contains("_format=application/fhir+json")){
                    value = FHIRMediaType.APPLICATION_FHIR_JSON;
                    addCharset = true;
                }
                else if(delegateQueryString.contains("_format=application/fhir+xml")) {
                    value = FHIRMediaType.APPLICATION_FHIR_XML;
                    addCharset = true;
                }

                // if there is charset defined in _format, then take it.
                if (addCharset) {
                    List<String> formats = Arrays.asList(delegate.getParameter(FHIRConstants.FORMAT).split(";"));
                    for (String format : formats) {
                        if (format.contains(CHARSET)) {
                            value = value + ";" + format;
                            break;
                        }
                    }
                }
            }

            if (mappedValue != null) {
                value = mappedValue;
            }
        }
        return value;
    }

    /**
     * We need to make sure that the Accept header is returned as one of the header names.
     *
     * @return
     * @see javax.servlet.http.HttpServletRequest#getHeaderNames()
     */
    @Override
    public Enumeration<String> getHeaderNames() {

        // Retrieve the header names from our delegate.
        // If the delegate returns null, that means the servlet container does not want
        // this method to be used.
        Enumeration<String> e = delegate.getHeaderNames();
        if (e == null) {
            return null;
        }

        // Copy all the header names into a Vector, then
        // add to it any headers that were specified via the query string.
        Vector<String> v = new Vector<String>();
        while (e.hasMoreElements()) {
            v.add(e.nextElement());
        }

        // Make sure the ACCEPT header is in the returned list since we
        // have a default value for that one.
        addHeaderNameIfNotPresent(v, HttpHeaders.ACCEPT);

        // Next, add names of headers that were specified via the query string.
        for (String s : headerQueryParameters.keySet()) {
            addHeaderNameIfNotPresent(v, s);
        }

        if (log.isLoggable(Level.FINEST)) {
            log.finest("getHeaderNames() returning: " + v.toString());
        }

        return v.elements();
    }

    private void addHeaderNameIfNotPresent(Vector<String> v, String value) {
        // Walk through the vecter 'v', looking for 'value'.
        boolean foundIt = false;
        for (int i = 0; i < v.size(); i++) {
            String element = v.get(i);
            if (value.equalsIgnoreCase(element)) {
                foundIt = true;
                break;
            }
        }

        // If we didn't find it, then add it to the vector.
        if (!foundIt) {
            v.add(value);
        }
    }

    /**
     * This method allows us to support the overriding of HTTP headers with query parameters. For example, if this
     * method is called for the "Accept" header, we'll allow the "accept" query parameter to act as an override for the
     * HTTP header value. We support this behavior for several HTTP headers. They are inserted into the
     * "headerNameMappings" map defined above.
     *
     * Also for selected HTTP headers, we'll support a default value in the event that no value is specified via the
     * HTTP request header or via the query string.
     *
     * @return the value of the specified header
     * @see javax.servlet.http.HttpServletRequest#getHeaders(java.lang.String)
     */
    @Override
    public Enumeration<String> getHeaders(String headerName) {
        Enumeration<String> e = null;
        Vector<String> v = null;
        String s;

        String headerNameLC = headerName.toLowerCase();

        // If this is one of the headers that we allow to be overridden, then
        // use the query string value instead.
        String queryValue = headerQueryParameters.get(headerNameLC);
        if (queryValue != null && !queryValue.isEmpty()) {
            v = new Vector<String>();
            s = queryValue;
            s = possiblyMapQueryParameterValue(headerNameLC, s);
            v.add(s);
            e = v.elements();
        }

        // If we haven't retrieved a value yet, then call our delegate.
        if (e == null) {
            e = delegate.getHeaders(headerName);
        }

        // If we still don't have a value to return, then check to see if
        // we should return a default value for this header.
        if (e == null || !e.hasMoreElements()) {
            String defaultValue = defaultHeaderValues.get(headerNameLC);
            if (defaultValue != null) {
                v = new Vector<String>();
                s = defaultValue;
                v.add(s);
                e = v.elements();
            }
        }

        // Add charset according to the Accept|Accept-Charset header of the request
        if (headerName.equalsIgnoreCase(ACCEPT)) {
                v = new Vector<String>();
                while (e.hasMoreElements()) {
                    v.add(updateAcceptHeader(e.nextElement()));
                }
                e = v.elements();
        }

        // In order to display the header values in a trace message, we actually need to
        // get at the vector containing the individual values.  Otherwise if we visit the values
        // in the returned Enumeration, the caller won't be able to see the values.
        if (log.isLoggable(Level.FINEST)) {
            if (v == null) {
                v = new Vector<String>();
                while (e.hasMoreElements()) {
                    v.add(e.nextElement());
                }
                e = v.elements();
            }
            log.finest("getHeaders(\"" + headerName + "\") : " + v.toString());
        }

        return e;
    }

    @SuppressWarnings("unused")
    private String displayHeaderValues(Enumeration<String> headers) {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        while (headers.hasMoreElements()) {
            String s = headers.nextElement();
            sb.append("{");
            sb.append(s);
            sb.append("}");
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * @param arg0
     * @return
     * @see javax.servlet.ServletRequest#getAttribute(java.lang.String)
     */
    @Override
    public Object getAttribute(String arg0) {
        return delegate.getAttribute(arg0);
    }

    /**
     * @return
     * @see javax.servlet.ServletRequest#getAttributeNames()
     */
    @Override
    public Enumeration<String> getAttributeNames() {
        return delegate.getAttributeNames();
    }

    /**
     * @return
     * @see javax.servlet.http.HttpServletRequest#getAuthType()
     */
    @Override
    public String getAuthType() {
        return delegate.getAuthType();
    }

    /**
     * @return
     * @see javax.servlet.ServletRequest#getCharacterEncoding()
     */
    @Override
    public String getCharacterEncoding() {
        return delegate.getCharacterEncoding();
    }

    /**
     * @return
     * @see javax.servlet.ServletRequest#getContentLength()
     */
    @Override
    public int getContentLength() {
        return delegate.getContentLength();
    }

    /**
     * @return
     * @see javax.servlet.ServletRequest#getContentType()
     */
    @Override
    public String getContentType() {
        return delegate.getContentType();
    }

    /**
     * @return
     * @see javax.servlet.http.HttpServletRequest#getContextPath()
     */
    @Override
    public String getContextPath() {
        return delegate.getContextPath();
    }

    /**
     * @return
     * @see javax.servlet.http.HttpServletRequest#getCookies()
     */
    @Override
    public Cookie[] getCookies() {
        return delegate.getCookies();
    }

    /**
     * @param headerName
     * @return
     * @see javax.servlet.http.HttpServletRequest#getDateHeader(java.lang.String)
     * @throws IllegalArgumentException
     */
    @Override
    public long getDateHeader(String headerName) {
        return delegate.getDateHeader(headerName);
    }

    /**
     * @return
     * @throws IOException
     * @see javax.servlet.ServletRequest#getInputStream()
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        return delegate.getInputStream();
    }

    /**
     * @param arg0
     * @return
     * @see javax.servlet.http.HttpServletRequest#getIntHeader(java.lang.String)
     */
    @Override
    public int getIntHeader(String arg0) {
        return delegate.getIntHeader(arg0);
    }

    /**
     * @return
     * @see javax.servlet.ServletRequest#getLocalAddr()
     */
    @Override
    public String getLocalAddr() {
        return delegate.getLocalAddr();
    }

    /**
     * @return
     * @see javax.servlet.ServletRequest#getLocalName()
     */
    @Override
    public String getLocalName() {
        return delegate.getLocalName();
    }

    /**
     * @return
     * @see javax.servlet.ServletRequest#getLocalPort()
     */
    @Override
    public int getLocalPort() {
        return delegate.getLocalPort();
    }

    /**
     * @return
     * @see javax.servlet.ServletRequest#getLocale()
     */
    @Override
    public Locale getLocale() {
        return delegate.getLocale();
    }

    /**
     * @return
     * @see javax.servlet.ServletRequest#getLocales()
     */
    @Override
    public Enumeration<Locale> getLocales() {
        return delegate.getLocales();
    }

    /**
     * @return
     * @see javax.servlet.http.HttpServletRequest#getMethod()
     */
    @Override
    public String getMethod() {
        String override = this.getHeader(HEADER_X_METHOD_OVERRIDE);
        if (override != null) {
            override = override.trim();
            if (log.isLoggable(Level.FINER)) {
                log.finest("The HTTP method is overridden by the " + HEADER_X_METHOD_OVERRIDE + " header.  The value is (" + override + ")");
            }
            return override;
        }
        return delegate.getMethod();
    }

    /**
     * @param arg0
     * @return
     * @see javax.servlet.ServletRequest#getParameter(java.lang.String)
     */
    @Override
    public String getParameter(String arg0) {
        return delegate.getParameter(arg0);
    }

    /**
     * @return
     * @see javax.servlet.ServletRequest#getParameterMap()
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        return delegate.getParameterMap();
    }

    /**
     * @return
     * @see javax.servlet.ServletRequest#getParameterNames()
     */
    @Override
    public Enumeration<String> getParameterNames() {
        return delegate.getParameterNames();
    }

    /**
     * @param arg0
     * @return
     * @see javax.servlet.ServletRequest#getParameterValues(java.lang.String)
     */
    @Override
    public String[] getParameterValues(String arg0) {
        return delegate.getParameterValues(arg0);
    }

    /**
     * @return
     * @see javax.servlet.http.HttpServletRequest#getPathInfo()
     */
    @Override
    public String getPathInfo() {
        return delegate.getPathInfo();
    }

    /**
     * @return
     * @see javax.servlet.http.HttpServletRequest#getPathTranslated()
     */
    @Override
    public String getPathTranslated() {
        return delegate.getPathTranslated();
    }

    /**
     * @return
     * @see javax.servlet.ServletRequest#getProtocol()
     */
    @Override
    public String getProtocol() {
        return delegate.getProtocol();
    }

    /**
     * @return
     * @see javax.servlet.http.HttpServletRequest#getQueryString()
     */
    @Override
    public String getQueryString() {
        // Return the amended queryString containing the form parameters
        if (queryString != null) {
            return queryString;
        }
        return delegate.getQueryString();
    }

    /**
     * @return
     * @throws IOException
     * @see javax.servlet.ServletRequest#getReader()
     */
    @Override
    public BufferedReader getReader() throws IOException {
        return delegate.getReader();
    }

    /**
     * @param arg0
     * @return
     * @deprecated
     * @see javax.servlet.ServletRequest#getRealPath(java.lang.String)
     */
    @Deprecated
    @Override
    public String getRealPath(String arg0) {
        return delegate.getRealPath(arg0);
    }

    /**
     * @return
     * @see javax.servlet.ServletRequest#getRemoteAddr()
     */
    @Override
    public String getRemoteAddr() {
        return delegate.getRemoteAddr();
    }

    /**
     * @return
     * @see javax.servlet.ServletRequest#getRemoteHost()
     */
    @Override
    public String getRemoteHost() {
        return delegate.getRemoteHost();
    }

    /**
     * @return
     * @see javax.servlet.ServletRequest#getRemotePort()
     */
    @Override
    public int getRemotePort() {
        return delegate.getRemotePort();
    }

    /**
     * @return
     * @see javax.servlet.http.HttpServletRequest#getRemoteUser()
     */
    @Override
    public String getRemoteUser() {
        return delegate.getRemoteUser();
    }

    /**
     * @param arg0
     * @return
     * @see javax.servlet.ServletRequest#getRequestDispatcher(java.lang.String)
     */
    @Override
    public RequestDispatcher getRequestDispatcher(String arg0) {
        return delegate.getRequestDispatcher(arg0);
    }

    /**
     * @return
     * @see javax.servlet.http.HttpServletRequest#getRequestURI()
     */
    @Override
    public String getRequestURI() {
        return delegate.getRequestURI();
    }

    /**
     * @return
     * @see javax.servlet.http.HttpServletRequest#getRequestURL()
     */
    @Override
    public StringBuffer getRequestURL() {
        return delegate.getRequestURL();
    }

    /**
     * @return
     * @see javax.servlet.http.HttpServletRequest#getRequestedSessionId()
     */
    @Override
    public String getRequestedSessionId() {
        return delegate.getRequestedSessionId();
    }

    /**
     * @return
     * @see javax.servlet.ServletRequest#getScheme()
     */
    @Override
    public String getScheme() {
        return delegate.getScheme();
    }

    /**
     * @return
     * @see javax.servlet.ServletRequest#getServerName()
     */
    @Override
    public String getServerName() {
        return delegate.getServerName();
    }

    /**
     * @return
     * @see javax.servlet.ServletRequest#getServerPort()
     */
    @Override
    public int getServerPort() {
        return delegate.getServerPort();
    }

    /**
     * @return
     * @see javax.servlet.http.HttpServletRequest#getServletPath()
     */
    @Override
    public String getServletPath() {
        return delegate.getServletPath();
    }

    /**
     * @return
     * @see javax.servlet.http.HttpServletRequest#getSession()
     */
    @Override
    public HttpSession getSession() {
        return delegate.getSession();
    }

    /**
     * @param arg0
     * @return
     * @see javax.servlet.http.HttpServletRequest#getSession(boolean)
     */
    @Override
    public HttpSession getSession(boolean arg0) {
        return delegate.getSession(arg0);
    }

    /**
     * @return
     * @see javax.servlet.http.HttpServletRequest#getUserPrincipal()
     */
    @Override
    public Principal getUserPrincipal() {
        return delegate.getUserPrincipal();
    }

    /**
     * @return
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromCookie()
     */
    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return delegate.isRequestedSessionIdFromCookie();
    }

    /**
     * @return
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromURL()
     */
    @Override
    public boolean isRequestedSessionIdFromURL() {
        return delegate.isRequestedSessionIdFromURL();
    }

    /**
     * @return
     * @deprecated
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromUrl()
     */
    @Deprecated
    @Override
    public boolean isRequestedSessionIdFromUrl() {
        return delegate.isRequestedSessionIdFromUrl();
    }

    /**
     * @return
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdValid()
     */
    @Override
    public boolean isRequestedSessionIdValid() {
        return delegate.isRequestedSessionIdValid();
    }

    /**
     * @return
     * @see javax.servlet.ServletRequest#isSecure()
     */
    @Override
    public boolean isSecure() {
        return delegate.isSecure();
    }

    /**
     * @param arg0
     * @return
     * @see javax.servlet.http.HttpServletRequest#isUserInRole(java.lang.String)
     */
    @Override
    public boolean isUserInRole(String arg0) {
        return delegate.isUserInRole(arg0);
    }

    /**
     * @param arg0
     * @see javax.servlet.ServletRequest#removeAttribute(java.lang.String)
     */
    @Override
    public void removeAttribute(String arg0) {
        delegate.removeAttribute(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     * @see javax.servlet.ServletRequest#setAttribute(java.lang.String, java.lang.Object)
     */
    @Override
    public void setAttribute(String arg0, Object arg1) {
        delegate.setAttribute(arg0, arg1);
    }

    /**
     * @param arg0
     * @throws UnsupportedEncodingException
     * @see javax.servlet.ServletRequest#setCharacterEncoding(java.lang.String)
     */
    @Override
    public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException {
        delegate.setCharacterEncoding(arg0);
    }
}
