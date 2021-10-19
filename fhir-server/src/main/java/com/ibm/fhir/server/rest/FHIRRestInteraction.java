/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.rest;

/**
 * Defines a FHIR REST interaction. Implementations do not need to
 * be immutable. Some may update their internal state based on the
 * result of calling the interaction on the visitor in the {@link #accept(FHIRRestInteractionVisitor)}
 * method.
 */
public interface FHIRRestInteraction {

    /**
     * Perform this interaction on the given visitor. Some implementations may chose to
     * update their internal state based on the interaction.
     *
     * @param visitor
     * @throws Exception
     */
    void accept(FHIRRestInteractionVisitor visitor) throws Exception;

    /**
     * Get the index for the response bundle entry
     *
     * @return
     */
    int getEntryIndex();
}
