/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.notification.websocket.impl;

import java.util.List;
import java.util.Map;

import javax.websocket.Decoder;
import javax.websocket.Encoder;
import javax.websocket.Extension;
import javax.websocket.server.ServerEndpointConfig;

/**
 * This class is registered with the liberty "ServerContainer" (by our servlet context listener) 
 * for initializing our websocket endpoint to be used for notifications.
 */
public class FHIRNotificationServiceEndpointConfig implements ServerEndpointConfig {
    
    private static final String endpointURI = "/notification";

    public FHIRNotificationServiceEndpointConfig() {
    }

    /* (non-Javadoc)
     * @see javax.websocket.EndpointConfig#getEncoders()
     */
    @Override
    public List<Class<? extends Encoder>> getEncoders() {
        return null;
    }

    /* (non-Javadoc)
     * @see javax.websocket.EndpointConfig#getDecoders()
     */
    @Override
    public List<Class<? extends Decoder>> getDecoders() {
        return null;
    }

    /* (non-Javadoc)
     * @see javax.websocket.EndpointConfig#getUserProperties()
     */
    @Override
    public Map<String, Object> getUserProperties() {
        return null;
    }

    /* (non-Javadoc)
     * @see javax.websocket.server.ServerEndpointConfig#getEndpointClass()
     */
    @Override
    public Class<?> getEndpointClass() {
        return FHIRNotificationServiceEndpoint.class;
    }

    /* (non-Javadoc)
     * @see javax.websocket.server.ServerEndpointConfig#getPath()
     */
    @Override
    public String getPath() {
        return endpointURI;
    }

    /* (non-Javadoc)
     * @see javax.websocket.server.ServerEndpointConfig#getSubprotocols()
     */
    @Override
    public List<String> getSubprotocols() {
        return null;
    }

    /* (non-Javadoc)
     * @see javax.websocket.server.ServerEndpointConfig#getExtensions()
     */
    @Override
    public List<Extension> getExtensions() {
        return null;
    }

    /* (non-Javadoc)
     * @see javax.websocket.server.ServerEndpointConfig#getConfigurator()
     */
    @Override
    public Configurator getConfigurator() {
        return new ServerEndpointConfig.Configurator();
    }
}
