/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.flow.api;

/**
 * Represents a resource being passed from the reader to the writer
 */
public abstract class FlowInteraction {
    // the change sequence number reported by the upstream server
    private final long changeId;

    // the ticket being used to track completion of this interaction
    private final ITrackerTicket trackerTicket;

    // The type/id of the resource
    private final ResourceIdentifier identifier;

    /**
     * Protected constructor
     * 
     * @param changeId
     * @param trackerTicket
     * @param identifier
     */
    protected FlowInteraction(long changeId, ITrackerTicket trackerTicket, ResourceIdentifier identifier) {
        this.changeId = changeId;
        this.trackerTicket = trackerTicket;
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return getInteractionType() + " " + identifier.toString();
    }

    /**
     * Get the type of interaction represented by this
     * @return
     */
    public abstract String getInteractionType();

    /**
     * Getter for the identifier
     * @return
     */
    public ResourceIdentifier getIdentifier() {
        return this.identifier;
    }

    /**
     * Getter for the changeId value
     * @return
     */
    public long getChangeId() {
        return this.changeId;
    }

    /**
     * Process this interaction using the given handler as a visitor
     * @param handler
     */
    public abstract void accept(IFlowInteractionHandler handler);

    /**
     * Invoke the completion callback
     */
    protected void complete() {
        this.trackerTicket.complete();
    }
}
