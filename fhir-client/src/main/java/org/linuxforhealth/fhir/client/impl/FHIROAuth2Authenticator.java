/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.client.impl;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.MultivaluedMap;

/**
 * This class is responsible for adding the Authorization header to outbound REST API requests.
 */
public class FHIROAuth2Authenticator implements ClientRequestFilter {
    private String accessToken;
    
    // Prevent use of the default ctor.
    protected FHIROAuth2Authenticator() {
    }
    
    
    public FHIROAuth2Authenticator(String accessToken) {
        setAccessToken(accessToken);
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
