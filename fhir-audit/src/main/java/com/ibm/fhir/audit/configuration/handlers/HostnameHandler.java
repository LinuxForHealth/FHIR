/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.audit.configuration.handlers;

import java.net.UnknownHostException;
import java.util.logging.Logger;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;

/**
 * Reliable Hostname Treatment for the Audit Framework.
 */
public class HostnameHandler {
    private static final String CLASSNAME = HostnameHandler.class.getName();
    private static final Logger log = java.util.logging.Logger.getLogger(CLASSNAME);

    private static final String HOSTNAME = determineHostname();

    public HostnameHandler() {
        // No Operation
    }

    /*
     * the following code is operating system independent, and switches between
     * win and linux approaches.
     */
    private static String determineHostname() {
        String osName = System.getProperty("os.name");
        String hostname;
        if (osName.contains("win")) {
            hostname = System.getenv("COMPUTERNAME");
        } else {
            hostname = System.getenv("HOSTNAME");
        }

        // Check if the hostname is set, if not, fallback to the
        // InetAddress which can be laggy.
        if (hostname == null) {
            // falling back to
            try {
                hostname = java.net.InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e) {
                hostname = "UNKNOWN";
                log.warning("Hostname not determined switching to UNKNOWN -> '" + e.getMessage() + "'");
            }

        }
        return hostname;
    }

    /**
     * gets the hostname from configuration, if not set uses the system hostname.
     *
     * @return
     */
    public String getHostname() {
        String auditHostname = FHIRConfigHelper.getStringProperty(FHIRConfiguration.PROPERTY_AUDIT_HOSTNAME, null);
        return auditHostname == null ? HOSTNAME : auditHostname;
    }
}