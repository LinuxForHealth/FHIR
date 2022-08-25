/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.client.app;

import org.linuxforhealth.fhir.client.FHIRClient;
import org.linuxforhealth.fhir.client.FHIRResponse;
import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.resource.Resource;

/**
 * Represents a FHIR CREATE interaction
 */
public class ClientUpdateInteraction extends ClientInteraction {

    /**
     * Public constructor
     * @param resourceType
     * @param logicalId
     */
    public ClientUpdateInteraction(String resourceType, String logicalId) {
        this.resourceType = resourceType;
        this.logicalId = logicalId;
    }

    @Override
    public void invoke(IClientInteractionContext context, FHIRClient client) throws Exception {
        Resource resource = context.getResource();
        if (resource != null) {
            FHIRResponse response = client.update(context.getResource(), this.headers);
            context.setStatus(response.getStatus());
            context.setLocation(response.getLocation());
            if (!response.isEmpty()) {
                OperationOutcome opo = response.getResource(OperationOutcome.class);
                context.setOperationOutcome(opo);
            }
        } else {
            throw new IllegalStateException("Context does not have a resource value");
        }
    }
}