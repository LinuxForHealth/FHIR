/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.notification.websocket;

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

    @Override
    public List<Class<? extends Encoder>> getEncoders() {
        return null;
    }

    @Override
    public List<Class<? extends Decoder>> getDecoders() {
        return null;
    }

    @Override
    public Map<String, Object> getUserProperties() {
        return null;
    }

    @Override
    public Class<?> getEndpointClass() {
        return FHIRNotificationServiceEndpoint.class;
    }

    @Override
    public String getPath() {
        return endpointURI;
    }

    @Override
    public List<String> getSubprotocols() {
        return null;
    }

    @Override
    public List<Extension> getExtensions() {
        return null;
    }

    @Override
    public Configurator getConfigurator() {
        return new ServerEndpointConfig.Configurator();
    }
}
