/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.postgresql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.config.PropertyGroup;

/**
 * Command to apply optimizer configuration options to the PostgreSQL database connection
 * (see issue-1911 for details on why this is important).
 */
public class SetPostgresOptimizerOptions {

    private static final Logger log = Logger.getLogger(SetPostgresOptimizerOptions.class.getName());

    // Configuration constants. Further testing (issue-1993) shows 12 gives the best results overall.
    private static final String JOIN_COLLAPSE_LIMIT = "join_collapse_limit";
    private static final int DEFAULT_JOIN_COLLAPSE_LIMIT = 12;
    private static final String FROM_COLLAPSE_LIMIT = "from_collapse_limit";
    private static final int DEFAULT_FROM_COLLAPSE_LIMIT = 12;

    private static final String COMPARTMENT_OPT_LEVEL = "compartment_opt_level";
    private static final int COMPARTMENT_JOIN_COLLAPSE_LIMIT = 1; // join in the written order of the query

    private final Integer joinCollapseLimit;
    private final Integer fromCollapseLimit;

    /**
     * Public constructor
     * @param pg the FHIR configuration PropertyGroup containing the optimizer options
     */
    public SetPostgresOptimizerOptions(PropertyGroup pg, boolean isCompartment) {
        if (pg != null) {
            int fromCollapseLimit = pg.getIntProperty(FROM_COLLAPSE_LIMIT, DEFAULT_FROM_COLLAPSE_LIMIT);
            int joinCollapseLimit = pg.getIntProperty(JOIN_COLLAPSE_LIMIT, DEFAULT_JOIN_COLLAPSE_LIMIT);

            // If this is a compartment-based search, we may want to override the
            // optimizer settings to make sure the compartment filter is performed first
            if (isCompartment) {
                int compartmentOptLevel = pg.getIntProperty(COMPARTMENT_OPT_LEVEL, 0);
                if (compartmentOptLevel > 0 && log.isLoggable(Level.FINE)) {
                    log.fine("Compartment optimization level: " + compartmentOptLevel);
                }
                if (compartmentOptLevel > 0) {
                    // clamp the JOIN_COLLAPSE_LIMIT so the join order is fixed (not changed by the optimizer)
                    joinCollapseLimit = COMPARTMENT_JOIN_COLLAPSE_LIMIT;
                }
            }

            this.fromCollapseLimit = fromCollapseLimit;
            this.joinCollapseLimit = joinCollapseLimit;
        } else {
            // No options provided, so we go with defaults
            this.joinCollapseLimit = DEFAULT_JOIN_COLLAPSE_LIMIT;
            this.fromCollapseLimit = DEFAULT_FROM_COLLAPSE_LIMIT;
        }
    }

    /**
     * Apply the configuration represented by this class to the given connection
     * @param c
     */
    public void applyTo(Connection c) {

        // Configure optimization values on the connection c
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