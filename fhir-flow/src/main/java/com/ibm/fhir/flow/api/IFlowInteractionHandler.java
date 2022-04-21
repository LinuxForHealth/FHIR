/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.flow.api;

import com.ibm.fhir.model.resource.Resource;

/**
 * An adapter/visitor interface to process an interaction
 */
public interface IFlowInteractionHandler {

    /**
     * Perform a logical DELETE interaction
     * @param changeId
     * @param identifier
     */
    void delete(long changeId, ResourceIdentifier identifier);

    /**
     * Perform a create-or-update (PUT) interaction
     * @param changeId
     * @param identifier
     * @param resourceData
     * @param resource
     */
    void createOrUpdate(long changeId, ResourceIdentifier identifier, String resourceData, Resource resource);
}
