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
     * @param entryId
     * @param identifier
     */
    void delete(String entryId, ResourceIdentifier identifier);

    /**
     * Perform a create-or-update (PUT) interaction
     * @param entryId
     * @param identifier
     * @param resourceData
     * @param resource
     */
    void createOrUpdate(String entryId, ResourceIdentifier identifier, String resourceData, Resource resource);
}
