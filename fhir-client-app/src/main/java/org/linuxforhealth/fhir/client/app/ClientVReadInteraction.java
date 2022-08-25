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
public class ClientVReadInteraction extends ClientInteraction {
    final String version;

    /**
     * Public constructor
     * @param resourceType
     * @param logicalId
     * @param version
     */
    public ClientVReadInteraction(String resourceType, String logicalId, String version) {
        this.resourceType = resourceType;
        this.logicalId = logicalId;
        this.version = version;
    }

    @Override
    public void invoke(IClientInteractionContext context, FHIRClient client) throws Exception {
        FHIRResponse response = client.vread(this.resourceType, this.logicalId, this.version, this.headers);
        if (response.getStatus() == HttpStatus.SC_OK) {
            Class<? extends Resource> resourceClass = ModelSupport.getResourceType(this.resourceType);
            context.setResource(response.getResource(resourceClass));
        } else {
            context.setResource(null);
        }
    }
}
