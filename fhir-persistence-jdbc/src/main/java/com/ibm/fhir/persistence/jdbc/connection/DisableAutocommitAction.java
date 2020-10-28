/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
/**
 * Command to turn off autocommit
 */
public class DisableAutocommitAction extends ChainedAction {
    private static final Logger log = Logger.getLogger(DisableAutocommitAction.class.getName());
    /**
     * Public constructor
     */
    public DisableAutocommitAction() {
        // No Op
    }

    /**
     * Public constructor
     * @param next action in a chain
     */
    public DisableAutocommitAction(Action next) {
        super(next);
    }

    @Override
    public void performOn(FHIRDbFlavor flavor, Connection c) throws FHIRPersistenceDBConnectException {
            
        try {
            c.setAutoCommit(false);
        } catch (SQLException x) {
            log.log(Level.SEVERE, "failed to set autocommit (false)", x);
            
            // schemaName is a secret, so don't emit in the exception to avoid propagating to client
            throw new FHIRPersistenceDBConnectException("Failed disabling autocommit on connection");
        }
        
        // call the next action in the chain
        super.performOn(flavor, c);
    }
}
