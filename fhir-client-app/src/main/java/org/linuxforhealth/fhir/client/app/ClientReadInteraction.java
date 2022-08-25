/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.client.app;

import org.apache.http.HttpStatus;
import org.linuxforhealth.fhir.client.FHIRClient;
import org.linuxforhealth.fhir.client.FHIRResponse;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.util.ModelSupport;

/**
 * Represents a FHIR READ interaction
 */
public class ClientReadInteraction extends ClientInteraction {

    /**
     * Public constructor
     * @param resourceType
     * @param logicalId
     */
    public ClientReadInteraction(String resourceType, String logicalId) {
        this.resourceType = resourceType;
        this.logicalId = logicalId;
    }

    @Override
    public void invoke(IClientInteractionContext context, FHIRClient client) throws Exception {
        FHIRResponse response = client.read(this.resourceType, this.logicalId, this.headers);
        if (response.getStatus() == HttpStatus.SC_OK) {
            Class<? extends Resource> resourceClass = ModelSupport.getResourceType(this.resourceType);
            context.setResource(response.getResource(resourceClass));
        } else {
            context.setResource(null);
        }
    }
}
