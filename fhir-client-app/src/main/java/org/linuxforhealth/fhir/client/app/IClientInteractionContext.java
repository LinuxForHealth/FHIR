/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.client.app;

import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.resource.Resource;

/**
 * Context used to pass data to and receive data from {@link ClientInteraction} instances.
 */
public interface IClientInteractionContext {
    /**
     * Get the current resource value
     * @return
     */
    Resource getResource();

    /**
     * Set the resource value
     * @param r
     */
    void setResource(Resource r);

    /**
     * Set the HTTP status from the last interaction
     * @param status
     */
    void setStatus(int status);

    /**
     * Read the HTTP status from the last interaction
     * @return
     */
    int getStatus();

    /**
     * Pretty print when rendering the resource as JSON
     * @return
     */
    boolean isPretty();

    /**
     * Get the OperationOutcome of the last interaction
     * @return
     */
    OperationOutcome getOperationOutcome();

    /**
     * Set the OperationOutcome from the last interaction
     * @param opo
     */
    void setOperationOutcome(OperationOutcome opo);

    /**
     * Get the location returned by the last interaction
     * @return
     */
    String getLocation();

    /**
     * Set the location returned by the last interaction
     * @param location
     */
    void setLocation(String location);
}
