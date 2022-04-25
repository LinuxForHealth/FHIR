/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.flow.impl;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpStatus;

import com.ibm.fhir.flow.api.FlowInteraction;
import com.ibm.fhir.flow.api.IFlowInteractionHandler;
import com.ibm.fhir.flow.api.ITrackerTicket;
import com.ibm.fhir.flow.api.ResourceIdentifier;

/**
 * A create or update request with a pending read of the resource
 * payload represented by the CompletableFuture
 */
public class CreateOrUpdate extends FlowInteraction {
    private final CompletableFuture<FlowFetchResult> flowFetchFuture;

    /**
     * Public constructor
     * 
     * @param entryId
     * @param trackerTicket
     * @param identifier
     * @param cf
     */
    public CreateOrUpdate(String entryId, ITrackerTicket trackerTicket, ResourceIdentifier identifier, CompletableFuture<FlowFetchResult> cf) {
        super(entryId, trackerTicket, identifier);
        this.flowFetchFuture = cf;
    }

    @Override
    public void accept(IFlowInteractionHandler handler) {
        try {
            // Wait for the async read to complete
            FlowFetchResult result = flowFetchFuture.get();
            if (result.getStatus() == HttpStatus.SC_OK) {
                handler.createOrUpdate(getEntryId(), getIdentifier(), result.getResourceData(), result.getResource());
            } else {
                throw new RuntimeException("Resource vread failed for '" + result.toString() + "'; status code: " + result.getStatus());
            }
        } catch (ExecutionException | InterruptedException x) {
            // The fetch failed, so we can't continue
            throw new RuntimeException("trying to read " + getIdentifier().toString(), x);
        } finally {
            complete();
        }
    }

    @Override
    public String getInteractionType() {
        return "CREATE_OR_UPDATE";
    }
}