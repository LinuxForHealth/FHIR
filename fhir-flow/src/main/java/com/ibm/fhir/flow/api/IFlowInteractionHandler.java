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
    void delete(ResourceIdentifier identifier);
    void createOrUpdate(ResourceIdentifier identifier, Resource resource);
}
