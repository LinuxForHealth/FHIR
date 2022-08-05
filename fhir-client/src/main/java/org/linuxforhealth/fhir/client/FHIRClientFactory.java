/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.client;

import java.util.Properties;

import org.linuxforhealth.fhir.client.impl.FHIRClientImpl;

/**
 * This factory can be used to obtain instances of the FHIRClient interface.
 */
public class FHIRClientFactory {

    private FHIRClientFactory() {
    }
    
    public static FHIRClient getClient(Properties properties) throws Exception {
        return new FHIRClientImpl(properties);
    }
}
