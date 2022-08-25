/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.client.app;

import org.linuxforhealth.fhir.client.FHIRClient;

/**
 * Prints the current context resource value to standard out
 */
public class ClientPrintLocationInteraction extends ClientInteraction {

    /**
     * Public constructor
     */
    public ClientPrintLocationInteraction() {
    }

    @Override
    public void invoke(IClientInteractionContext context, FHIRClient client) throws Exception {
        String location = context.getLocation();
        if (location != null && !location.isEmpty()) {
            System.out.println("LOCATION: " + location);
        }
    }
}