/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.filter.rest;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.watsonhealth.fhir.config.FHIRConfiguration;
import com.ibm.watsonhealth.fhir.config.FHIRRequestContext;

/**
 * This class is a servlet filter which is registered with the REST API's servlet. The main purpose of the class is to
 * log entry/exit information and elapsed time for each REST API request processed by the server.
 * 
 * @author padams
 */
public class FHIRRestServletFilter implements Filter {
    private static final Logger log = Logger.getLogger(FHIRRestServletFilter.class.getName());

    private static String tenantIdHeaderName = null;
    private static String datastoreIdHeaderName = null;

    /*
     * (non-Javadoc)
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse,
     * javax.servlet.FilterChain) This method will intercept incoming HTTP requests and log entry/exit messages.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (log.isLoggable(Level.FINE)) {
            log.entering(this.getClass().getName(), "doFilter");
        }

        try {
            String tenantId = FHIRConfiguration.DEFAULT_TENANT_ID;
            String dsId = FHIRConfiguration.DEFAULT_DATASTORE_ID;
            
            // Wrap the incoming servlet request with our own implementation.
            if (request instanceof HttpServletRequest) {
                FHIRHttpServletRequestWrapper requestWrapper = new FHIRHttpServletRequestWrapper((HttpServletRequest) request);
                request = requestWrapper;
                if (log.isLoggable(Level.FINEST)) {
                    log.finest("Wrapped HttpServletRequest object...");
                }
                
                String t = ((HttpServletRequest) request).getHeader(tenantIdHeaderName);
                if (t != null) {
                    tenantId = t;
                }
                
                t = ((HttpServletRequest) request).getHeader(datastoreIdHeaderName);
                if (t != null) {
                    dsId = t;
                }
            }

            // Log a "request received" message.
            StringBuffer requestDescription = new StringBuffer();
            requestDescription.append("tenantId[");
            requestDescription.append(tenantId);
            requestDescription.append("] dsId:[");
            requestDescription.append(dsId);
            requestDescription.append("] user:[");
            requestDescription.append(getRequestUserPrincipal(request));
            requestDescription.append("] method:[");
            requestDescription.append(getRequestMethod(request));
            requestDescription.append("] uri:[");
            requestDescription.append(getRequestURL(request));
            requestDescription.append("]");

            log.info("Received request: " + requestDescription.toString());

            long initialTime = System.currentTimeMillis();

            // Display the contents of the HTTP request body
            // Uncomment this ONLY for debugging when all else fails!
            // displayRequestBody(request);
            
            // Create a new FHIRRequestContext and set it on the current thread.
            FHIRRequestContext context = new FHIRRequestContext(tenantId, dsId);
            FHIRRequestContext.set(context);

            // Pass the request through to the next filter in the chain.
            chain.doFilter(request, response);

            // If possible, include the status code in the "completed" message.
            StringBuffer statusMsg = new StringBuffer();
            if (response instanceof HttpServletResponse) {
                int status = ((HttpServletResponse) response).getStatus();
                statusMsg.append(" status:[" + status + "]");
            } else {
                statusMsg.append(" status:[unknown (non-HTTP request)]");
            }

            double elapsedSecs = (System.currentTimeMillis() - initialTime) / 1000.0;
            log.info("Completed request[" + elapsedSecs + " secs]: " + requestDescription.toString() + statusMsg.toString());
        } finally {
            if (log.isLoggable(Level.FINE)) {
                log.exiting(this.getClass().getName(), "doFilter");
            }
            
            // Remove the FHIRRequestContext from the current thread.
            FHIRRequestContext.remove();
        }
    }

    /**
     * Displays the contents of the servlet request body. Note that this consumes the content so that downstream errors
     * will occur. But this function is still useful in certain debug situations. TODO: If necessary, we could wrap the
     * incoming servlet request object with our own implementation that could serve up the body contents to downstream
     * filters/servlets to avoid this issue.
     */
    @SuppressWarnings("unused")
    private void displayRequestBody(ServletRequest request) throws IOException {
        StringBuffer sb = new StringBuffer();
        InputStream requestBodyIS = request.getInputStream();
        try {
            int b = requestBodyIS.read();
            while (b != -1) {
                sb.append((char) b);
                b = requestBodyIS.read();
            }
        } finally {
            requestBodyIS.close();
            log.info("Request body contents: \n<\n" + sb.toString() + "\n,> (" + sb.toString().length());
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
    private String getRequestURL(ServletRequest request) {
        String url = null;

        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            StringBuffer sb = httpRequest.getRequestURL();
            String queryString = httpRequest.getQueryString();
            if (queryString != null && !queryString.isEmpty()) {
                sb.append("?");
                sb.append(queryString);
            }
            url = sb.toString();
        }
        return (url != null ? url : "<unknown>");
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
        // Nothing to do here...
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(FilterConfig config) throws ServletException {
        try {
            tenantIdHeaderName = 
                    FHIRConfiguration.getInstance().loadConfiguration()
                    .getStringProperty(FHIRConfiguration.PROPERTY_TENANT_ID_HEADER_NAME, FHIRConfiguration.DEFAULT_TENANT_ID_HEADER_NAME);

            log.info("Configured tenant-id header name is: " +  tenantIdHeaderName);

            datastoreIdHeaderName = 
                    FHIRConfiguration.getInstance().loadConfiguration()
                    .getStringProperty(FHIRConfiguration.PROPERTY_DATASTORE_ID_HEADER_NAME, FHIRConfiguration.DEFAULT_DATASTORE_ID_HEADER_NAME);

            log.info("Configured datastore-id header name is: " +  datastoreIdHeaderName);
        } catch (Exception e) {
            throw new ServletException("Servlet filter initialization error.", e);
        }
    }
}
