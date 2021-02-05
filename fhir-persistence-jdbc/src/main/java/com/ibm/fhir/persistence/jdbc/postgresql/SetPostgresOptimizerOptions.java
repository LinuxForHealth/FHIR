/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.postgresql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.config.PropertyGroup;

/**
 * Command to apply optimizer configuration options to the PostgreSQL database connection
 * (see issue-1911 for details on why this is important).
 */
public class SetPostgresOptimizerOptions {

    private static final Logger log = Logger.getLogger(SetPostgresOptimizerOptions.class.getName());

    // Configuration constants
    private static final String JOIN_COLLAPSE_LIMIT = "join_collapse_limit";
    private static final String FROM_COLLAPSE_LIMIT = "from_collapse_limit";

    // The optimizer options property group
    private final PropertyGroup propertyGroup;

    /**
     * Public constructor
     * @param optPG the FHIR configuration PropertyGroup containing the optimizer options
     */
    public SetPostgresOptimizerOptions(PropertyGroup optPG) {
        this.propertyGroup = optPG;
    }

    /**
     * Apply the configuration represented by this class to the given connection
     * @param c
     */
    public void applyTo(Connection c) {

        // Configure optimization values on the connection c
        Integer fromCollapseLimit = propertyGroup.getIntProperty(FROM_COLLAPSE_LIMIT, null);
        if (fromCollapseLimit != null) {
            final String SET = "SET from_collapse_limit = " + fromCollapseLimit;
            try (Statement s = c.createStatement()) {
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Applying optimizer option: " + SET);
                }
                s.executeUpdate(SET);
            } catch (SQLException x) {
                // Log the problem, but don't treat as fatal
                log.warning("Error setting optimizer option: " + SET + "; " + x.getMessage());
            }
        }

        Integer joinCollapseLimit = propertyGroup.getIntProperty(JOIN_COLLAPSE_LIMIT, null);
        if (joinCollapseLimit != null) {
            final String SET = "SET join_collapse_limit = " + joinCollapseLimit;
            try (Statement s = c.createStatement()) {
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Applying optimizer option: " + SET);
                }
                s.executeUpdate(SET);
            } catch (SQLException x) {
                // Log the problem, but don't treat as fatal
                log.warning("Error setting optimizer option: " + SET + "; " + x.getMessage());
            }
        }
    }
}