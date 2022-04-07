/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.flow.impl;

import java.util.function.Consumer;

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
    public Delete(long changeId, Consumer<Long> completionCallback, ResourceIdentifier identifier) {
        super(changeId, completionCallback, identifier);
    }

    @Override
    public void accept(IFlowInteractionHandler handler) {
        try {
            handler.delete(getChangeId(), getIdentifier());
        } finally {
            complete();
        }
    }

    @Override
    public String getInteractionType() {
        return "DELETE";
    }
}