/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.client.impl;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.MultivaluedMap;

/**
 * This class is responsible for adding the Authorization header to outbound REST API requests.
 */
public class FHIROAuth2Authenticator implements ClientRequestFilter {
    private String oAuth2EndpointURL;
    private String oidcRegURL;
	private String accessToken;
    
    // Prevent use of the default ctor.
    protected FHIROAuth2Authenticator() {
    }
    
    
    public FHIROAuth2Authenticator(String oAuth2EndpointURL, String oidcRegURL, String accessToken) {
        setAccessToken(accessToken);
    }
    
    public void setOAuth2EndpointURL(String oAuth2EndpointURL) {
        this.oAuth2EndpointURL = oAuth2EndpointURL;
    }

    public String getOAuth2EndpointURL() {
        return oAuth2EndpointURL;
    }
    
    //getters and setters for OpenID Connect Registration URL
    public void setOidcRegURL(String oidcRegURL) {
        this.oidcRegURL = oidcRegURL;
    }

    public String getOidcRegURL() {
        return oidcRegURL;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    /* (non-Javadoc)
     * @see javax.ws.rs.client.ClientRequestFilter#filter(javax.ws.rs.client.ClientRequestContext)
     * 
     * This method is called by the JAX-RS client runtime and will add an Authorization header to the
     * outbound REST API request to supply the necessary oauth 2.0 access token.
     */
    @Override
    public void filter(ClientRequestContext ctxt) throws IOException {
        if (getAccessToken() != null) {
            MultivaluedMap<String, Object> headers = ctxt.getHeaders();
            headers.add("Authorization", "Bearer " + getAccessToken());  
        }
    }
}
