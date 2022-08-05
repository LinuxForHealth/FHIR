/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.core.util.handler;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Calculates the IP and subsequent retrieval of the IPs
 */
public class IPHandler {

    private static final String CLASSNAME = IPHandler.class.getName();
    private static final Logger log = java.util.logging.Logger.getLogger(CLASSNAME);

    private static final String IPS = determineIPS();
    private static final String IP = determineIP();

    public IPHandler() {
        // No Operation
    }

    /*
     * determines a single ip address for the given machine.
     *
     * @implNote If the interface goes up or down, the single IP address is still cached.
     * This is highly unlikely in a production environment.
     */
    private static String determineIP() {
        String ip = null;
        try {
            ip = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException | NumberFormatException e) {
            // Defaulting to the localhost, as the machine's name is not registered in the local lookup/dns.
            log.log(Level.SEVERE, "Failure acquiring local host IP address - be sure the name is registered in the name resolution service", e);
            ip = "127.0.0.1";
        }
        return ip;
    }

    /*
     * determines the IP
     *
     * @implNote If the interface goes up or down, the IP addresses is still cached.
     * This is highly unlikely in a production environment.
     */
    private static String determineIPS() {
        List<String> componentIps = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
            while (ifaces.hasMoreElements()) {
                NetworkInterface iface = ifaces.nextElement();
                Enumeration<InetAddress> iaddrs = iface.getInetAddresses();
                while (iaddrs.hasMoreElements()) {
                    InetAddress iaddr = iaddrs.nextElement();
                    componentIps.add(iaddr.getHostAddress());
                }
            }
        } catch (SocketException e) {
            log.severe("Failure acquiring host name or IP: " + e.getMessage());
        }

        // If we resolve to empty, we're going to mark UNKNOWN.
        String componentIp;
        if (componentIps.isEmpty()) {
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
    public String getIpAddress() {
        return IP;
    }

    /**
     * gets the main IP.
     *
     * @return
     */
    public String getIpAddresses() {
        return IPS;
    }
}
