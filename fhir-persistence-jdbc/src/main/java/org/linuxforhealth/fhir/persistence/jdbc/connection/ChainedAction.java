/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.connection;

import java.sql.Connection;

import org.linuxforhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;

/**
 * Base for chaining actions together
 */
public class ChainedAction implements Action {

    // the next action in the chain
    private final Action next;

    /**
     * Public constructor
     * @param next the next action in the chain
     */
    public ChainedAction(Action next) {
        this.next = next;
    }

    /**
     * Public constructor where this action is the end of the chain
     */
    public ChainedAction() {
        this.next = null;
    }

    @Override
    public void performOn(FHIRDbFlavor flavor, Connection c) throws FHIRPersistenceDBConnectException {
        if (next != null) {
            next.performOn(flavor, c);
        }
    }
}
