/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.flow.impl;

import com.ibm.fhir.flow.api.FlowInteraction;
import com.ibm.fhir.flow.api.IFlowInteractionHandler;
import com.ibm.fhir.flow.api.ITrackerTicket;
import com.ibm.fhir.flow.api.ResourceIdentifier;

/**
 * A create or update request with a pending read of the resource
 * payload represented by the CompletableFuture
 */
public class Delete extends FlowInteraction {

    /**
     * Public constructor
     * 
     * @param entryId
     * @param trackerTicket
     * @param identifier
     */
    public Delete(String entryId, ITrackerTicket trackerTicket, ResourceIdentifier identifier) {
        super(entryId, trackerTicket, identifier);
    }

    @Override
    public void accept(IFlowInteractionHandler handler) {
        try {
            handler.delete(getEntryId(), getIdentifier());
        } finally {
            complete();
        }
    }

    @Override
    public String getInteractionType() {
        return "DELETE";
    }
}