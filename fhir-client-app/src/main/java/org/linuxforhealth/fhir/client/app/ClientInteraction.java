/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.client.app;

import org.linuxforhealth.fhir.client.FHIRClient;
import org.linuxforhealth.fhir.client.FHIRRequestHeader;

/**
 *
 */
public abstract class ClientInteraction {
    protected String resourceType;
    protected String logicalId;
    protected FHIRRequestHeader[] headers;

    /**
     * Process the interaction using the client
     * @param context
     * @param client
     * @throws Exception
     */
    public abstract void invoke(IClientInteractionContext context, FHIRClient client) throws Exception;
}
