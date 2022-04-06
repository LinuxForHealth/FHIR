/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.flow.impl;

import com.ibm.fhir.flow.api.FlowInteraction;
import com.ibm.fhir.flow.api.IFlowInteractionHandler;
import com.ibm.fhir.flow.api.ResourceIdentifier;

/**
 * A create or update request with a pending read of the resource
 * payload represented by the CompletableFuture
 */
public class Delete extends FlowInteraction {

    /**
     * Public constructor
     * 
     * @param identifier
     */
    public Delete(ResourceIdentifier identifier) {
        super(identifier);
    }

    @Override
    public void accept(IFlowInteractionHandler handler) {
        handler.delete(getIdentifier());
    }

    @Override
    public String getInteractionType() {
        return "DELETE";
    }
}