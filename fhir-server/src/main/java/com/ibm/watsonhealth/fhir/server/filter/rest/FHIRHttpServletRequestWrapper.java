/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.filter.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.HttpHeaders;

import com.ibm.watsonhealth.fhir.core.MediaType;

/**
 * This class is used to wrap a HttpServletRequest instance. The main purpose is to implement common behaviors for the
 * FHIR REST API layer. For example, we will initially use this class to support the "_format" query parameter as an
 * alternative to the "Accept" HTTP header.
 * 
 * @author padams
 */
public class FHIRHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private static final Logger log = Logger.getLogger(FHIRHttpServletRequestWrapper.class.getName());

    public final static String UTF8 = "utf-8";
    public final static String UTF16 = "utf-16";
    public final static String DEFAULT_ACCEPT_HEADER_VALUE = MediaType.APPLICATION_JSON_FHIR;
    public static final String HEADER_X_METHOD_OVERRIDE = "X-Method-Override";

    // The real HttpServletRequest instance that we'll delegate to.
    private HttpServletRequest delegate;

    // queryString that is built if form usage is detected
    private String queryString = null;

    // This map will store specific query parameters that are found in a
    // particular request URI.
    private Map<String, String> queryParameters;

    // This map will map http header names to query parameter names.
    private static Map<String, String> headerNameMappings;

    // This map allows us to use default values for selected HTTP headers.
    private static Map<String, String> defaultHeaderValues;

    // This map allows us to implement shortcuts/aliases for the values specified for the_format query parameter.
    private static Map<String, String> _formatShortcuts;

    static {
        initHeaderNameMappings();
        initDefaultHeaderValues();
        initFormatShortcuts();
    }

    public FHIRHttpServletRequestWrapper(HttpServletRequest req) {
        super(req);
        delegate = req;

        log.fine("Creating FHIRHttpServletRequestWrapper for HttpServletRequest: " + req.toString());

        // Extra query parameters that can override HTTP headers.
        initQueryParameterValues(req);

        String contentType = req.getContentType();
        log.finer("Content-Type is " + contentType);

        // If parameters are contained in a form, then be sure to pull them out and add them to our
        // set of query parameters.
        if (contentType != null && contentType.contains(MediaType.APPLICATION_FORM_URLENCODED)) {
            log.finer("Detected " + MediaType.APPLICATION_FORM_URLENCODED);
            formParameters(req);
        }
    }

    /**
     * This map will contain the "aliases" that we support as values of the _format 
     * query parameter (overrides the Accept header value).
     */
    private static void initFormatShortcuts() {
        _formatShortcuts = new HashMap<>();
        _formatShortcuts.put("xml", MediaType.APPLICATION_XML_FHIR);
        _formatShortcuts.put("application/xml", MediaType.APPLICATION_XML_FHIR);
        _formatShortcuts.put("text/xml", MediaType.APPLICATION_XML_FHIR);
        _formatShortcuts.put("json", MediaType.APPLICATION_JSON_FHIR);
        _formatShortcuts.put("application/json", MediaType.APPLICATION_JSON_FHIR);
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
        queryParameters = new HashMap<>();
        queryParameters.put("_format", req.getParameter("_format"));
        queryParameters.put("x-method-override", req.getParameter("x-method-override"));
        queryParameters.put("x-http-method-override", req.getParameter("x-http-method-override"));

        log.finer("Retrieved these query parameters from the request URI: " + queryParameters.toString());
    }

    /**
     * This method is responsible for initializing our mapping of HTTP header names to query parameter names. We'll use
     * this mapping when retrieving header values.
     */
    private static void initHeaderNameMappings() {
        headerNameMappings = new HashMap<>();

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
        log.finer("Processing form parameters, original queryString is " + originalQueryString);

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
                log.finest("parameter name: " + key);
                String[] values = paramEntry.getValue();
                if (values != null) {
                    // There can be multiple values for each key.
                    // I am going to write out each key=value separately.
                    // The implementation will choose the correct setting
                    // (probably the first in most cases).
                    for (String value : values) {
                        value = URLEncoder.encode(value, UTF8);
                        log.finest("parameter value: " + value);
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

        log.finer("After processing form parameters, queryString is " + queryString);
    }

    /**
     * This method allows us to support the overriding of HTTP headers with query parameters. For example, if this
     * method is called for the "Accept" header, we'll allow the "_format" query parameter to act as an override for the
     * HTTP header value. We support this behavior for several HTTP headers. They are inserted into the
     * "headerNameMappings" map defined above.
     * 
     * Also for selected HTTP headers, we'll support a default value in the event that no value is specified via the
     * HTTP request header or via the query string.
     * 
     * @param headerName
     *            the name of the HTTP header to be retrieved
     * @return the value of the specified header
     * @see javax.servlet.http.HttpServletRequest#getHeader(java.lang.String)
     */
    public String getHeader(String headerName) {

        if (headerName == null) {
            throw new IllegalArgumentException();
        }

        String headerNameLC = headerName.toLowerCase();

        String s = null;

        // First, check to see if this header is one that we also support as a
        // query parameter.
        String queryParamName = headerNameMappings.get(headerNameLC);

        // If we found this header name in our header name map,
        // then check to see if the user specified a value for it in the query
        // string.
        if (queryParamName != null) {
            String queryValue = queryParameters.get(queryParamName);
            if (queryValue != null && !queryValue.isEmpty()) {
                s = queryValue;
                s = possiblyMapQueryParameterValue(queryParamName, s);
            }
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

        return s;
    }

    /**
     * For specific query parameters (e.g. "_format"), we'll attempt to map the input value (specified by the user in
     * the URI string) to a more official value.
     * 
     * @param queryParamName
     *            the name of the query parameter that we're dealing with
     * @param value
     *            the value of the query parameter
     * @return a possibly mapped value or the original value if no mapping exists
     */
    private String possiblyMapQueryParameterValue(String queryParamName, String value) {
        if (queryParamName.equals("_format")) {
            String mappedValue = _formatShortcuts.get(value);
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
    public Enumeration<String> getHeaderNames() {
        // Retrieve the header names from our delegate.
        Enumeration<String> e = delegate.getHeaderNames();

        // Copy all the header names into a Vector and remember
        // whether or not we found the Accept header.
        Vector<String> v = new Vector<String>();
        boolean foundAccept = false;
        while (e.hasMoreElements()) {
            String s = (String) e.nextElement();
            if (s.equalsIgnoreCase(HttpHeaders.ACCEPT)) {
                foundAccept = true;
            }
            v.add(s);
        }

        // If we didn't find the Accept header, then add it to our Vector
        // before returning.
        if (!foundAccept) {
            v.add(HttpHeaders.ACCEPT);
        }

        return v.elements();
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
    public Enumeration<String> getHeaders(String headerName) {
        Enumeration<String> e = null;
        Vector<String> v = null;
        String s;

        String headerNameLC = headerName.toLowerCase();

        // If this is one of the headers that we allow to be overridden, then
        // use the query string value instead.
        String queryParamName = headerNameMappings.get(headerNameLC);
        if (queryParamName != null) {
            String queryValue = queryParameters.get(queryParamName);
            if (queryValue != null && !queryValue.isEmpty()) {
                v = new Vector<String>();
                s = queryValue;
                s = possiblyMapQueryParameterValue(queryParamName, s);
                v.add(s);
                e = v.elements();
            }
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

        return e;
    }

    @SuppressWarnings("unused")
    private String displayHeaderValues(Enumeration<String> headers) {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        while (headers.hasMoreElements()) {
            String s = (String) headers.nextElement();
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
    public Object getAttribute(String arg0) {
        return delegate.getAttribute(arg0);
    }

    /**
     * @return
     * @see javax.servlet.ServletRequest#getAttributeNames()
     */
    public Enumeration<String> getAttributeNames() {
        return delegate.getAttributeNames();
    }

    /**
     * @return
     * @see javax.servlet.http.HttpServletRequest#getAuthType()
     */
    public String getAuthType() {
        return delegate.getAuthType();
    }

    /**
     * @return
     * @see javax.servlet.ServletRequest#getCharacterEncoding()
     */
    public String getCharacterEncoding() {
        return delegate.getCharacterEncoding();
    }

    /**
     * @return
     * @see javax.servlet.ServletRequest#getContentLength()
     */
    public int getContentLength() {
        return delegate.getContentLength();
    }

    /**
     * @return
     * @see javax.servlet.ServletRequest#getContentType()
     */
    public String getContentType() {
        return delegate.getContentType();
    }

    /**
     * @return
     * @see javax.servlet.http.HttpServletRequest#getContextPath()
     */
    public String getContextPath() {
        return delegate.getContextPath();
    }

    /**
     * @return
     * @see javax.servlet.http.HttpServletRequest#getCookies()
     */
    public Cookie[] getCookies() {
        return delegate.getCookies();
    }

    /**
     * @param arg0
     * @return
     * @see javax.servlet.http.HttpServletRequest#getDateHeader(java.lang.String)
     */
    public long getDateHeader(String arg0) {
        return delegate.getDateHeader(arg0);
    }

    /**
     * @return
     * @throws IOException
     * @see javax.servlet.ServletRequest#getInputStream()
     */
    public ServletInputStream getInputStream() throws IOException {
        return delegate.getInputStream();
    }

    /**
     * @param arg0
     * @return
     * @see javax.servlet.http.HttpServletRequest#getIntHeader(java.lang.String)
     */
    public int getIntHeader(String arg0) {
        return delegate.getIntHeader(arg0);
    }

    /**
     * @return
     * @see javax.servlet.ServletRequest#getLocalAddr()
     */
    public String getLocalAddr() {
        return delegate.getLocalAddr();
    }

    /**
     * @return
     * @see javax.servlet.ServletRequest#getLocalName()
     */
    public String getLocalName() {
        return delegate.getLocalName();
    }

    /**
     * @return
     * @see javax.servlet.ServletRequest#getLocalPort()
     */
    public int getLocalPort() {
        return delegate.getLocalPort();
    }

    /**
     * @return
     * @see javax.servlet.ServletRequest#getLocale()
     */
    public Locale getLocale() {
        return delegate.getLocale();
    }

    /**
     * @return
     * @see javax.servlet.ServletRequest#getLocales()
     */
    public Enumeration<Locale> getLocales() {
        return delegate.getLocales();
    }

    /**
     * @return
     * @see javax.servlet.http.HttpServletRequest#getMethod()
     */
    public String getMethod() {
        String override = this.getHeader(HEADER_X_METHOD_OVERRIDE);
        if (override != null) {
            override = override.trim();
            log.finer("The HTTP method is overridden by the " + HEADER_X_METHOD_OVERRIDE + " header.  The value is (" + override + ")");
            return override;
        }
        return delegate.getMethod();
    }

    /**
     * @param arg0
     * @return
     * @see javax.servlet.ServletRequest#getParameter(java.lang.String)
     */
    public String getParameter(String arg0) {
        return delegate.getParameter(arg0);
    }

    /**
     * @return
     * @see javax.servlet.ServletRequest#getParameterMap()
     */
    public Map<String, String[]> getParameterMap() {
        return delegate.getParameterMap();
    }

    /**
     * @return
     * @see javax.servlet.ServletRequest#getParameterNames()
     */
    public Enumeration<String> getParameterNames() {
        return delegate.getParameterNames();
    }

    /**
     * @param arg0
     * @return
     * @see javax.servlet.ServletRequest#getParameterValues(java.lang.String)
     */
    public String[] getParameterValues(String arg0) {
        return delegate.getParameterValues(arg0);
    }

    /**
     * @return
     * @see javax.servlet.http.HttpServletRequest#getPathInfo()
     */
    public String getPathInfo() {
        return delegate.getPathInfo();
    }

    /**
     * @return
     * @see javax.servlet.http.HttpServletRequest#getPathTranslated()
     */
    public String getPathTranslated() {
        return delegate.getPathTranslated();
    }

    /**
     * @return
     * @see javax.servlet.ServletRequest#getProtocol()
     */
    public String getProtocol() {
        return delegate.getProtocol();
    }

    /**
     * @return
     * @see javax.servlet.http.HttpServletRequest#getQueryString()
     */
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
    public BufferedReader getReader() throws IOException {
        return delegate.getReader();
    }

    /**
     * @param arg0
     * @return
     * @deprecated
     * @see javax.servlet.ServletRequest#getRealPath(java.lang.String)
     */
    public String getRealPath(String arg0) {
        return delegate.getRealPath(arg0);
    }

    /**
     * @return
     * @see javax.servlet.ServletRequest#getRemoteAddr()
     */
    public String getRemoteAddr() {
        return delegate.getRemoteAddr();
    }

    /**
     * @return
     * @see javax.servlet.ServletRequest#getRemoteHost()
     */
    public String getRemoteHost() {
        return delegate.getRemoteHost();
    }

    /**
     * @return
     * @see javax.servlet.ServletRequest#getRemotePort()
     */
    public int getRemotePort() {
        return delegate.getRemotePort();
    }

    /**
     * @return
     * @see javax.servlet.http.HttpServletRequest#getRemoteUser()
     */
    public String getRemoteUser() {
        return delegate.getRemoteUser();
    }

    /**
     * @param arg0
     * @return
     * @see javax.servlet.ServletRequest#getRequestDispatcher(java.lang.String)
     */
    public RequestDispatcher getRequestDispatcher(String arg0) {
        return delegate.getRequestDispatcher(arg0);
    }

    /**
     * @return
     * @see javax.servlet.http.HttpServletRequest#getRequestURI()
     */
    public String getRequestURI() {
        return delegate.getRequestURI();
    }

    /**
     * @return
     * @see javax.servlet.http.HttpServletRequest#getRequestURL()
     */
    public StringBuffer getRequestURL() {
        return delegate.getRequestURL();
    }

    /**
     * @return
     * @see javax.servlet.http.HttpServletRequest#getRequestedSessionId()
     */
    public String getRequestedSessionId() {
        return delegate.getRequestedSessionId();
    }

    /**
     * @return
     * @see javax.servlet.ServletRequest#getScheme()
     */
    public String getScheme() {
        return delegate.getScheme();
    }

    /**
     * @return
     * @see javax.servlet.ServletRequest#getServerName()
     */
    public String getServerName() {
        return delegate.getServerName();
    }

    /**
     * @return
     * @see javax.servlet.ServletRequest#getServerPort()
     */
    public int getServerPort() {
        return delegate.getServerPort();
    }

    /**
     * @return
     * @see javax.servlet.http.HttpServletRequest#getServletPath()
     */
    public String getServletPath() {
        return delegate.getServletPath();
    }

    /**
     * @return
     * @see javax.servlet.http.HttpServletRequest#getSession()
     */
    public HttpSession getSession() {
        return delegate.getSession();
    }

    /**
     * @param arg0
     * @return
     * @see javax.servlet.http.HttpServletRequest#getSession(boolean)
     */
    public HttpSession getSession(boolean arg0) {
        return delegate.getSession(arg0);
    }

    /**
     * @return
     * @see javax.servlet.http.HttpServletRequest#getUserPrincipal()
     */
    public Principal getUserPrincipal() {
        return delegate.getUserPrincipal();
    }

    /**
     * @return
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromCookie()
     */
    public boolean isRequestedSessionIdFromCookie() {
        return delegate.isRequestedSessionIdFromCookie();
    }

    /**
     * @return
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromURL()
     */
    public boolean isRequestedSessionIdFromURL() {
        return delegate.isRequestedSessionIdFromURL();
    }

    /**
     * @return
     * @deprecated
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromUrl()
     */
    public boolean isRequestedSessionIdFromUrl() {
        return delegate.isRequestedSessionIdFromUrl();
    }

    /**
     * @return
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdValid()
     */
    public boolean isRequestedSessionIdValid() {
        return delegate.isRequestedSessionIdValid();
    }

    /**
     * @return
     * @see javax.servlet.ServletRequest#isSecure()
     */
    public boolean isSecure() {
        return delegate.isSecure();
    }

    /**
     * @param arg0
     * @return
     * @see javax.servlet.http.HttpServletRequest#isUserInRole(java.lang.String)
     */
    public boolean isUserInRole(String arg0) {
        return delegate.isUserInRole(arg0);
    }

    /**
     * @param arg0
     * @see javax.servlet.ServletRequest#removeAttribute(java.lang.String)
     */
    public void removeAttribute(String arg0) {
        delegate.removeAttribute(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     * @see javax.servlet.ServletRequest#setAttribute(java.lang.String, java.lang.Object)
     */
    public void setAttribute(String arg0, Object arg1) {
        delegate.setAttribute(arg0, arg1);
    }

    /**
     * @param arg0
     * @throws UnsupportedEncodingException
     * @see javax.servlet.ServletRequest#setCharacterEncoding(java.lang.String)
     */
    public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException {
        delegate.setCharacterEncoding(arg0);
    }
}
