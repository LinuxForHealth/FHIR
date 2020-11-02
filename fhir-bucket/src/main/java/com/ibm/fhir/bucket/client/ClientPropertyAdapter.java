/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.bucket.client;

import java.util.Properties;

/**
 * Property adapter for for the FHIR HTTP client
 */
public class ClientPropertyAdapter {
    public static final String READ_TIMEOUT = "read.timeout";
    public static final String CONNECT_TIMEOUT = "connect.timeout";
    public static final String FHIR_SERVER_HOST = "fhir.server.host";
    public static final String FHIR_SERVER_PORT = "fhir.server.port";
    public static final String FHIR_SERVER_ENDPOINT = "fhir.server.endpoint";
    public static final String FHIR_SERVER_USER = "fhir.server.user";
    public static final String FHIR_SERVER_PASS = "fhir.server.pass";

    public static final String TRUSTSTORE = "truststore";
    public static final String TRUSTSTORE_PASS = "truststore.pass";
    public static final String POOL_CONNECTIONS_MAX = "pool.connections.max";
    public static final String ENABLED_CIPHERS = "enabled.ciphers";
    public static final String DISABLE_HOSTNAME_VERIFICATION = "disable.hostname.verification";

    // The properties being adapted
    private final Properties properties;

    public ClientPropertyAdapter(Properties props) {
        this.properties = props;
    }

    /**
     * How long to wait for TCP connection
     * @return
     */
    public int getConnectTimeout() {
        String val = properties.getProperty(CONNECT_TIMEOUT, "60000");
        return Integer.parseInt(val);
    }

    public int getPoolConnectionsMax() {
        String val = properties.getProperty(POOL_CONNECTIONS_MAX, "10");
        return Integer.parseInt(val);
    }

    /**
     * How long to wait for a response to a request
     * @return
     */
    public int getReadTimeout() {
        String val = properties.getProperty(READ_TIMEOUT, "60000");
        return Integer.parseInt(val);
    }

    public String fhirServerHost() {
        return properties.getProperty(FHIR_SERVER_HOST);
    }

    public String fhirServerEndpoint() {
        return properties.getProperty(FHIR_SERVER_ENDPOINT);
    }

    public int fhirServerPort() {
        String val = properties.getProperty(FHIR_SERVER_PORT, "9443");
        return Integer.parseInt(val);
    }

    public String getTruststore() {
        return properties.getProperty(TRUSTSTORE);
    }

    public String getTruststorePass() {
        return properties.getProperty(TRUSTSTORE_PASS);
    }

    public String getEnabledCiphers() {
        return properties.getProperty(ENABLED_CIPHERS);
    }

    public String getFhirServerUser() {
        return properties.getProperty(FHIR_SERVER_USER);
    }

    public String getFhirServerPass() {
        return properties.getProperty(FHIR_SERVER_PASS);
    }

    public boolean isDisableHostnameVerification() {
        return "true".equalsIgnoreCase(properties.getProperty(DISABLE_HOSTNAME_VERIFICATION, "false"));
    }
}
