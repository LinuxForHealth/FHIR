/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.audit.configuration;

import static org.testng.Assert.assertNotNull;

import org.testng.annotations.Test;

import com.ibm.fhir.audit.configuration.handlers.ComponentIpHandler;
import com.ibm.fhir.audit.configuration.handlers.HostnameHandler;

/**
 * Tests of the Configuration Handlers
 */
public class ConfigurationHandlerTest {

    @Test
    public void testHostname() {
        HostnameHandler handler = new HostnameHandler();
        assertNotNull(handler.getHostname());
    }

    @Test
    public void testComponentIp() {
        ComponentIpHandler handler = new ComponentIpHandler();
        assertNotNull(handler.getIp());
    }
}
