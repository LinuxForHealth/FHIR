/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.flow.impl;

import org.linuxforhealth.fhir.flow.api.FlowInteraction;
import org.linuxforhealth.fhir.flow.api.IFlowInteractionHandler;
import org.linuxforhealth.fhir.flow.api.ITrackerTicket;
import org.linuxforhealth.fhir.flow.api.ResourceIdentifier;

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