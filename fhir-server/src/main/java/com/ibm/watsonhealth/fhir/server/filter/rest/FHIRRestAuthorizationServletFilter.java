/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.filter.rest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
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

import com.ibm.watsonhealth.fhir.config.FHIRConfigHelper;
import com.ibm.watsonhealth.fhir.config.FHIRConfiguration;

/**
 * This servlet filter is configured with the FHIR Server webapp and will perform an authorization "whitelist" check to
 * ensure that the incoming client CN and issuer OU values specified via request headers are valid.
 * 
 */
public class FHIRRestAuthorizationServletFilter implements Filter {
    private static final Logger log = Logger.getLogger(FHIRRestAuthorizationServletFilter.class.getName());

    private static final String CLIENT_CERT_CLIENT_CN_HEADER = "IBM-App-cli-CN";
    private static final String CLIENT_CERT_ISSUER_OU_HEADER = "IBM-App-iss-OU";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.entering(this.getClass().getName(), "doFilter");
        try {
            boolean failedAuthCheck = false;

            // If this filter is enabled, then check the supplied request headers against our whitelist values.
            Boolean enabled = FHIRConfigHelper.getBooleanProperty(FHIRConfiguration.PROPERTY_AUTHFILTER_ENABLED, Boolean.FALSE);
            if (enabled) {
                log.fine("FHIR Server authorization filter is enabled.");
                HttpServletRequest httpServletRequest = (HttpServletRequest) request;

                // Retrieve the two request headers from the incoming request.
                String clientCertClientCN = httpServletRequest.getHeader(CLIENT_CERT_CLIENT_CN_HEADER);
                log.fine("clientCertClientCN: " + clientCertClientCN);

                String clientCertIssuerOU = httpServletRequest.getHeader(CLIENT_CERT_ISSUER_OU_HEADER);
                log.fine("clientCertIssuerOU: " + clientCertIssuerOU);

                // If neither header is present, then we'll bypass the check.
                if (clientCertClientCN == null && clientCertIssuerOU == null) {
                    log.fine("Neither request header present, bypassing whitelist check...");
                }

                // Otherwise, perform the whitelist check.
                else {
                    String authorizedClientCertClientCN = FHIRConfigHelper.getStringProperty(FHIRConfiguration.PROPERTY_AUTHORIZED_CLIENT_CERT_CLIENT_CN, "");
                    log.fine("authorizedClientCertClientCN: " + authorizedClientCertClientCN);
                    List<String> authorizedClientCertClientCNs = Arrays.asList(authorizedClientCertClientCN.split(","));

                    String authorizedClientCertIssuerOU = FHIRConfigHelper.getStringProperty(FHIRConfiguration.PROPERTY_AUTHORIZED_CLIENT_CERT_ISSUER_OU, "");
                    log.fine("authorizedClientCertIssuerOU: " + authorizedClientCertIssuerOU);

                    // If we fail the whitelist check, then send back a FORBIDDEN response.
                    if (!authorizedClientCertClientCNs.contains(clientCertClientCN) || !authorizedClientCertIssuerOU.equals(clientCertIssuerOU)) {
                        String msg = "Incoming request failed authorization whitelist check; clientCertClientCN="
                                + (clientCertClientCN != null ? clientCertClientCN : "<null>") + ", clientCertIssuerOU="
                                + (clientCertIssuerOU != null ? clientCertIssuerOU : "<null>");
                        log.log(Level.SEVERE, msg);
                        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        failedAuthCheck = true;
                    }
                }
            }

            // If we passed the check, then invoke the downstream filters.
            if (!failedAuthCheck) {
                chain.doFilter(request, response);
            }
        } finally {
            log.exiting(this.getClass().getName(), "doFilter");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
