/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.core.util.handler;

import java.net.UnknownHostException;
import java.util.logging.Logger;

/**
 * Reliable Hostname Treatment
 *
 * @implNote caches irrespective of adapter up/down and configuration changes.
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

        // in specific environments, this may be the setting in Liberty
        // @see https://www.ibm.com/support/knowledgecenter/SSEQTP_liberty/com.ibm.websphere.wlp.doc/ae/tagt_wlp_set_defaulthostname.html
        String hostname = System.getenv("defaultHostName");
        if (hostname == null) {
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
        }
        return hostname;
    }

    /**
     * gets the hostname
     *
     * @return
     */
    public String getHostname() {
        return HOSTNAME;
    }
}