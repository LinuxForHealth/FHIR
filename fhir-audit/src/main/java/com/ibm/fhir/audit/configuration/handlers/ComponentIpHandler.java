/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.audit.configuration.handlers;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringJoiner;
import java.util.logging.Logger;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;

/**
 * Calculates the Component Ip and subsequent retrieval of the IPs
 */
public class ComponentIpHandler {
    private static final String CLASSNAME = ComponentIpHandler.class.getName();
    private static final Logger log = java.util.logging.Logger.getLogger(CLASSNAME);

    private static final String IP = determineIP();

    public ComponentIpHandler() {
        // No Operation
    }

    /*
     * determines the IP
     */
    private static String determineIP() {
        List<String> componentIps = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
            while(ifaces.hasMoreElements()) {
                NetworkInterface iface = ifaces.nextElement();
                Enumeration<InetAddress> iaddrs = iface.getInetAddresses();
                while(iaddrs.hasMoreElements()) {
                    InetAddress iaddr = iaddrs.nextElement();
                    componentIps.add(iaddr.getHostAddress());
                }
            }
        } catch (SocketException e) {
            log.severe("Failure acquiring host name or IP: " + e.getMessage());
        }

        // If we resolve to empty, we're going to mark UNKNOWN.
        String componentIp;
        if(componentIps.isEmpty()) {
            componentIp = "UNKNOWN";
        } else {
            StringJoiner joiner = new StringJoiner(",");
            for (String s : componentIps) {
                joiner.add(s);
            }
            componentIp = joiner.toString();
        }
        return componentIp;
    }

    /**
     * gets the ip from configuration, if not set uses the system ip as retrieved.
     *
     * @return
     */
    public String getIp() {
        String auditIp = FHIRConfigHelper.getStringProperty(FHIRConfiguration.PROPERTY_AUDIT_IP, null);
        return auditIp == null ? IP : auditIp;
    }
}