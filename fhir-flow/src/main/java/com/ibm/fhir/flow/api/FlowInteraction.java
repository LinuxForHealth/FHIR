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
    private final ResourceIdentifier identifier;

    /**
     * Protected constructor
     * 
     * @param identifier
     */
    protected FlowInteraction(ResourceIdentifier identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return getInteractionType() + " " + identifier.toString();
    }

    public abstract String getInteractionType();

    /**
     * Getter for the identifier
     * @return
     */
    public ResourceIdentifier getIdentifier() {
        return this.identifier;
    }

    /**
     * Process this interaction using the given handler as a visitor
     * @param handler
     */
    public abstract void accept(IFlowInteractionHandler handler);
}
